package nc.scap.pub.sms.comps;

import nc.uap.lfw.core.cmd.UifAddCmd;
import nc.vo.pub.lang.UFDateTime;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.cmd.UifPlugoutCmd;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import nc.scap.pub.sms.SmsManageService;
import nc.scap.pub.sms.itf.ISmsExtentionService;
import nc.scap.pub.sms.tools.ScapSmsExtentionUtil;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.data.Dataset;
import nc.scap.pub.util.ScapDAO;
import nc.scap.pub.vos.ScapSmsTaskVO;
import nc.uap.lfw.core.cmd.UifSaveCmdRV;
import java.util.List;
import nc.uap.lfw.core.cmd.UifDelCmdRV;
import nc.uap.lfw.core.comp.IDataBinding;
import nc.scap.pub.vos.ScapSmsStatusEnum;
import nc.scap.pub.vos.ScapSmsTaskStatusEnum;
import nc.uap.lfw.core.comp.GridComp;
import nc.scap.pub.vos.ScapSmsVO;
import nc.vo.pub.BusinessException;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.constants.AppConsts;
import uap.web.bd.pub.AppUtil;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.portal.log.ScapLogger;

/**
 * ��Ƭ����Ĭ���߼�
 */
public class SmsManageCardWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {

	private static final String PLUGOUT_ID = "afterSavePlugout";

	/**
	 * ҳ����ʾ�¼�
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
					row.setValue(masterDs.nameToIndex(masterDs.getPrimaryKeyField()), pk_primarykey);

					IDataBinding[] components = AppUtil.getCntView().getComponentsByDataset(getMasterDsId());
					if (components != null && components.length > 0 && components[0] instanceof FormComp) {
						FormComp form = (FormComp) components[0];
						for (FormElement element : form.getElementList()) {
							if (ScapSmsTaskVO.ADDRESS.equals(element.getId())) {
								element.setEditable(true);
								element.setEnabled(true);
							}
							if (ScapSmsTaskVO.CONTENT.equals(element.getId())) {
								element.setEditable(true);
								element.setEnabled(true);
							}
						}
					}
				}
			});
		} else if (AppConsts.OPE_EDIT.equals(oper)) {
			CmdInvoker.invoke(new UifDatasetLoadCmd(masterDs) {

				@Override
				protected void onAfterDatasetLoad() {
					getMasterDs().setEnabled(true);
				}
			});
		}
	}

	/**
	 * ������ѡ���߼�
	 */
	public void onAfterRowSelect(DatasetEvent dsEvent) {
		Dataset ds = dsEvent.getSource();
		CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()));
	}

	/**
	 * ����
	 */
	public void onSave(MouseEvent<?> mouseEvent) throws BusinessException {
		Dataset taskDs = getMasterDs();
		String oper = getOperator();
		int taskAddrIdx = taskDs.nameToIndex(ScapSmsTaskVO.ADDRESS);
		// ������������Ҫ�������ֳɶ���
		if (taskAddrIdx >= 0 && AppConsts.OPE_ADD.equals(oper)) {
			String taskAddr = (String) taskDs.getValue(taskAddrIdx);
			List<String> addressList = SmsManageService.splitNumbers(taskAddr);
			// ��ʽ���ռ��˵�ַ
			taskAddr = SmsManageService.joinNumbers(addressList);
			taskDs.setValue(taskAddrIdx, taskAddr);
			// �����ӱ�
			Dataset smsDs = getDetailDs(getDetailDsIds())[0];
			int smsAddrIdx = smsDs.nameToIndex(ScapSmsVO.ADDRESS);
			int smsStatusIdx = smsDs.nameToIndex(ScapSmsVO.SENDSTATUS);
			int smsSendtimesIdx = smsDs.nameToIndex(ScapSmsVO.SENDTIMES);
			int smsCreatetimeIdx = smsDs.nameToIndex(ScapSmsVO.CREATETIME);
			UFDateTime sendtime = new UFDateTime(System.currentTimeMillis());

			for (String smsAddr : addressList) {
				Row row = smsDs.getEmptyRow();
				// �ռ���
				if (smsAddrIdx >= 0) {
					row.setValue(smsAddrIdx, smsAddr);
				}
				// ����״̬
				if (smsStatusIdx >= 0) {
					row.setValue(smsStatusIdx, ScapSmsStatusEnum.NOT_SENT);
				}
				// ���ʹ���
				if (smsSendtimesIdx >= 0) {
					row.setValue(smsSendtimesIdx, 0);
				}
				// ��������
				if (smsCreatetimeIdx >= 0) {
					row.setValue(smsCreatetimeIdx, sendtime);
				}
				smsDs.addRow(row);
			}
		}

		CmdInvoker.invoke(new UifSaveCmdRV(this.getMasterDsId(), this.getDetailDsIds(), false));
		this.getCurrentAppCtx().closeWinDialog();
		CmdInvoker.invoke(new UifPlugoutCmd(this.getCurrentView().getId(), PLUGOUT_ID));
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

	private void setAutoFillValue(Row row) {
		if (row != null) {
			Dataset ds = this.getCurrentView().getViewModels().getDataset(this.getMasterDsId());
			// ����״̬
			int sendStatusIdx = ds.nameToIndex(ScapSmsTaskVO.SENDSTATUS);
			if (sendStatusIdx >= 0) {
				row.setValue(sendStatusIdx, ScapSmsTaskStatusEnum.NOT_FINISHED);
			}
			// ���ʹ���
			int sendTimesIdx = ds.nameToIndex(ScapSmsTaskVO.SENDTIMES);
			if (sendTimesIdx >= 0) {
				row.setValue(sendTimesIdx, 0);
			}
			// ��������
			int createTimeIdx = ds.nameToIndex(ScapSmsTaskVO.CREATETIME);
			if (createTimeIdx >= 0) {
				row.setValue(createTimeIdx, new UFDateTime(System.currentTimeMillis()));
			}
			// ��Դģ��
			int mdNameIdx = ds.nameToIndex(ScapSmsTaskVO.MDNAME);
			if (mdNameIdx >= 0) {
				row.setValue(mdNameIdx, SmsManageService.getMdName(getCurrentAppCtx()));
			}
			// ��Դ�û�
			int userNameIdx = ds.nameToIndex(ScapSmsTaskVO.USERNAME);
			if (userNameIdx >= 0) {
				row.setValue(userNameIdx, SmsManageService.getUserName(getCurrentAppCtx()));
			}
		}
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
	 * �ӱ���
	 */
	public void onGridSendClick(MouseEvent mouseEvent) {
		
		GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		Dataset ds = this.getCurrentView().getViewModels().getDataset(dsId);
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("��ѡ�д����Ͷ���");
		}
		
		List<ISmsExtentionService> exts = ScapSmsExtentionUtil.getSmsServiceList();
		if (exts.size() != 1) {
			throw new LfwRuntimeException("δ���û������˶��������չ��");
		}
		
		Row row = ds.getSelectedRow();
		String pkSms = (String) row.getValue(ds.nameToIndex(ds.getPrimaryKeyField()));
		ScapSmsVO sms = (ScapSmsVO) ScapDAO.retrieveByPK(ScapSmsVO.class, pkSms);
		sms.setSendstatus(ScapSmsStatusEnum.FAILED);
		ISmsExtentionService ext = exts.get(0);
		try {
	        ext.send(sms);
        } catch (Exception e) {
	        ScapLogger.error("smsManage���Ͷ��ų����쳣", e);
        }
		
		ScapDAO.insertOrUpdateVO(sms);
		CmdInvoker.invoke(new UifDatasetLoadCmd(getMasterDs()));
		CmdInvoker.invoke(new UifPlugoutCmd(this.getCurrentView().getId(), PLUGOUT_ID));
	}

	@Override
	protected String getMasterDsId() {
		return "ScapSmsTaskVO";
	}
}
