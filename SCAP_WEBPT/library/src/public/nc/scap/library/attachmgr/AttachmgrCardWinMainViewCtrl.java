package nc.scap.library.attachmgr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.scap.library.AttachMgrVO;
import nc.scap.library.vos.AttachEntVO;
import nc.scap.pub.util.ScapDAO;
import nc.uap.cpb.baseservice.IUifCpbService;
import nc.uap.ctrl.tpl.print.ICpPrintTemplateService;
import nc.uap.ctrl.tpl.print.init.DefaultPrintService;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifAddCmd;
import nc.uap.lfw.core.cmd.UifAttachCmd;
import nc.uap.lfw.core.cmd.UifCopyCmd;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.cmd.UifDelCmdRV;
import nc.uap.lfw.core.cmd.UifEnableCmd;
import nc.uap.lfw.core.cmd.UifLineDelCmd;
import nc.uap.lfw.core.cmd.UifPlugoutCmd;
import nc.uap.lfw.core.cmd.UifSaveCmdRV;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.constants.AppConsts;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.file.FillFileInfoHelper;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.log.ScapLogger;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import uap.lfw.core.locator.ServiceLocator;

/**
 * ��Ƭ����Ĭ���߼�
 * 
 */
public class AttachmgrCardWinMainViewCtrl<T extends WebElement> extends
		AbstractMasterSlaveViewController {
	private static final long serialVersionUID = -1;
	private static final String PARAM_BILLITEM = "billitem";
	private static final String PLUGOUT_ID = "afterSavePlugout";

	/**
	 * ҳ����ʾ�¼�
	 * 
	 * @param dialogEvent
	 */
	public void beforeShow(DialogEvent dialogEvent) {
		Dataset masterDs = this.getMasterDs();
		masterDs.clear();

		String oper = getOperator();
		if (AppConsts.OPE_ADD.equals(oper)) {
			CmdInvoker.invoke(new UifAddCmd(getMasterDsId()) {
				protected void onBeforeRowAdd(Row row) {
					setAutoFillValue(row);

					Dataset masterDs = getMasterDs();
					String pk_primarykey = generatePk();
					row.setValue(
							masterDs.nameToIndex(masterDs.getPrimaryKeyField()),
							pk_primarykey);
					FillFileInfoHelper.resetItem(pk_primarykey);
					FillFileInfoHelper.fillFileInfo(masterDs, row);
				}
			});
		} else if (AppConsts.OPE_EDIT.equals(oper)) {
			CmdInvoker.invoke(new UifDatasetLoadCmd(masterDs) {
				@Override
				protected void onAfterDatasetLoad() {
					this.getDs().setEnabled(true);

					String primaryKey = this.getDs().getPrimaryKeyField();
					if (primaryKey == null) {
						throw new LfwRuntimeException("��ǰDatasetû����������!");
					}
					String primaryKeyValue = (String) this.getDs()
							.getSelectedRow()
							.getValue(this.getDs().nameToIndex(primaryKey));
					FillFileInfoHelper.resetItem(primaryKeyValue);
				}
			});
		}
	}

	/**
	 * ������ѡ���߼�
	 * 
	 * @param datasetEvent
	 */
	public void onAfterRowSelect(DatasetEvent dsEvent) {
		Dataset ds = dsEvent.getSource();
		CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()));
	}

	public String checkData(SuperVO aggVo) {
		String errmsg = "";
		AttachMgrVO vo = (AttachMgrVO) aggVo;
		String pk_primary = vo.getPk_filetype();
//		String vyear = vo.getV_year();
		String vname = vo.getV_name();
		String vcode = vo.getV_code();
		String sql = "select * from " + vo.getTableName() + " where v_name='" + vname + "' and v_code='" + vcode
				+ "' and dr=0";
		List<AttachMgrVO> vos = queryVOs(AttachMgrVO.class, sql);
		if (vos != null && vos.size() > 0) {
			String pk_tmp = vos.get(0).getPk_filetype();
			if (!pk_tmp.equals(pk_primary)) {
				errmsg += "��Ȩ�ı�����"+vname+"�Ѵ���" + "<br/>";
			}
		}
		return errmsg;
	}

	public List queryVOs(Class className, String condition) {
		try {
			ScapLogger.error(condition);
			List PersonContratVOs = (List) ScapDAO.getBaseDAO().executeQuery(
					condition, new BeanListProcessor(className));
			return PersonContratVOs;
		} catch (DAOException e) {
			Logger.error(e.getMessage());
			throw new LfwRuntimeException("��ѯ���ִ���");
		}

	}

	/**
	 * ����
	 * 
	 * @param datasetEvent
	 */

	public void onSave(MouseEvent<?> mouseEvent) throws BusinessException {
		Dataset masterDs = this.getMasterDs();
		CmdInvoker.invoke(new UifSaveCmdRV(this.getMasterDsId(), this
				.getDetailDsIds(), false) {
			protected void onVoSave(SuperVO aggVo) {
				try {
					String errmsg = checkData(aggVo);// ��ȡ������Ϣ
					if (!errmsg.equals("")) {
						throw new LfwRuntimeException(errmsg).setTitle("��ʾ");
					}
					IUifCpbService cpbService = NCLocator.getInstance().lookup(
							IUifCpbService.class);
					cpbService.insertOrUpdateSuperVO(aggVo,
							this.isNotifyBDCache());
					// getCrudService().saveBusinessVO(aggVo);
				} catch (BusinessException e) {
					Logger.error(e, e);
					throw new LfwRuntimeException(e.getMessage());
				}

			}
		});
		masterDs.setEnabled(true);
		this.getCurrentAppCtx().closeWinDialog();

		CmdInvoker.invoke(new UifPlugoutCmd(this.getCurrentView().getId(),
				PLUGOUT_ID));
	}

	/**
	 * ����
	 */
	public void onAdd(MouseEvent<?> mouseEvent) throws BusinessException {
		CmdInvoker.invoke(new UifAddCmd(this.getMasterDsId()) {
			@Override
			protected void onBeforeRowAdd(Row row) {
				setAutoFillValue(row);
			}
		});
	}

	/**
	 * ����
	 * 
	 * @param mouseEvent
	 */
	public void onCopy(MouseEvent<?> mouseEvent) {
		CmdInvoker.invoke(new UifCopyCmd(this.getMasterDsId()));
	}

	/**
	 * ɾ��
	 */
	public void onDelete(MouseEvent<?> mouseEvent) throws BusinessException {
		CmdInvoker.invoke(new UifDelCmdRV(this.getMasterDsId()));
	}

	/**
	 * ����
	 */
	public void onBack(MouseEvent<?> mouseEvent) throws BusinessException {
		this.getCurrentAppCtx().closeWinDialog();
	}

	/**
	 * ����
	 * 
	 * @param mouseEvent
	 * @throws BusinessException
	 */
	public void onStart(MouseEvent<?> mouseEvent) throws BusinessException {
		CmdInvoker.invoke(new UifEnableCmd(this.getMasterDsId(), true));
	}

	/**
	 * ͣ��
	 * 
	 * @param mouseEvent
	 * @throws BusinessException
	 */
	public void onStop(MouseEvent<?> mouseEvent) throws BusinessException {
		CmdInvoker.invoke(new UifEnableCmd(this.getMasterDsId(), false));
	}

	/**
	 * ����
	 * 
	 * @param mouseEvent
	 * @throws BusinessException
	 */
	public void onAttchFile(MouseEvent<?> mouseEvent) throws BusinessException {
		Dataset ds = this.getMasterDs();
		Row row = ds.getSelectedRow();
		if (row == null) {
			throw new LfwRuntimeException("��ѡ������!");
		}
		String primaryKey = ds.getPrimaryKeyField();
		if (primaryKey == null) {
			throw new LfwRuntimeException("��ǰDatasetû����������!");
		}
		String primaryKeyValue = (String) row.getValue(ds
				.nameToIndex(primaryKey));
		Map<String, String> paramMap = null;
		if (primaryKeyValue != null) {
			paramMap = new HashMap<String, String>(2);
			paramMap.put(PARAM_BILLITEM, primaryKeyValue);
		}
		CmdInvoker.invoke(new UifAttachCmd("����", paramMap));
	}

	/**
	 * ��ӡ
	 * 
	 * @param mouseEvent
	 * @throws BusinessException
	 */
	public void onPrint(MouseEvent<?> mouseEvent) throws BusinessException {
		Dataset ds = this.getMasterDs();
		Row row = ds.getSelectedRow();
		if (row == null) {
			throw new LfwRuntimeException("��ѡ������!");
		}
		try {
			List<Dataset> list = new ArrayList<Dataset>(1);
			list.add(ds);
			DefaultPrintService printService = new DefaultPrintService();
			printService.setDatasetList(list);
			ICpPrintTemplateService service = ServiceLocator
					.getService(ICpPrintTemplateService.class);
			service.print(printService);
		} catch (Exception e) {
			LfwLogger.error(e);
			throw new LfwRuntimeException(e.getMessage());
		}
	}

	/**
	 * ����PK_ORG�ֶ�ֵ
	 * 
	 * @param row
	 */
	private void setAutoFillValue(Row row) {
		if (row != null) {
			Dataset ds = this.getCurrentView().getViewModels()
					.getDataset(this.getMasterDsId());

			String pkOrg = this.getCurrentAppCtx().getAppEnvironment()
					.getPk_org();
			if (pkOrg != null) {
				int pkOrgIndex = ds.nameToIndex("pk_org");
				if (pkOrgIndex >= 0) {
					row.setValue(pkOrgIndex, pkOrg);
				}
			}
			String pkGroup = this.getCurrentAppCtx().getAppEnvironment()
					.getPk_group();
			if (pkGroup != null) {
				int pkGroupIndex = ds.nameToIndex(PK_GROUP);
				if (pkGroupIndex >= 0) {
					row.setValue(pkGroupIndex, pkGroup);
				}
			}
			row.setValue(ds.nameToIndex(AttachMgrVO.V_YEAR),
					new UFDate().getYear());
		}
	}

	/**
	 * �ӱ�����
	 */
	public void onGridAddClick(MouseEvent<?> mouseEvent) {
		GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		Dataset ds = this.getCurrentView().getViewModels().getDataset(dsId);
		Row emptyRow = ds.getEmptyRow();
		emptyRow.setValue(ds.nameToIndex(AttachEntVO.V_NUM),
				ds.getRowCount() + 1);
		ds.addRow(emptyRow);
		ds.setRowSelectIndex(ds.getRowIndex(emptyRow));
		ds.setEnabled(true);
	}

	/**
	 * �ӱ�༭
	 */
	public void onGridEditClick(MouseEvent<?> mouseEvent) {
		GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		Dataset ds = this.getCurrentView().getViewModels().getDataset(dsId);
		ds.setEnabled(true);
	}

	/**
	 * �ӱ�ɾ��
	 */
	public void onGridDeleteClick(MouseEvent<?> mouseEvent) {
		GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		CmdInvoker.invoke(new UifLineDelCmd(dsId));
	}

	@Override
	protected String getMasterDsId() {
		return "scappt_attachmgr";
	}
}