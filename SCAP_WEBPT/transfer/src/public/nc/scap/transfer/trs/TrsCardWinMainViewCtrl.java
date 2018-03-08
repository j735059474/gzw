package nc.scap.transfer.trs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.scap.library.vos.AttachEntVO;
import nc.scap.pt.vos.ScapptApplyAttachVO;
import nc.scap.pt.vos.ScapptApplyHVO;
import nc.scap.pt.vos.ScapptAttachVO;
import nc.scap.pt.vos.ScapptTransferAssessVO;
import nc.scap.pt.vos.ScapptTransferHVO;
import nc.scap.pt.vos.ScapptTransferTargetVO;
import nc.scap.pt.vos.ScapptTransferTransfereeVO;
import nc.scap.pt.vos.ScapptTransferTransferorVO;
import nc.scap.ptpub.PtConstants;
import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.CpPubMethod;
import nc.scap.pub.util.CpUIctrl;
import nc.scap.pub.util.CpWinUtil;
import nc.scap.pub.util.ScapDAO;
import nc.scap.pub.util.ScapPAPPubmethod;
import nc.scap.transfer.trs.wfm.WfmFlwFormVO;
import nc.uap.cpb.baseservice.IUifCpbService;
import nc.uap.ctrl.tpl.print.ICpPrintTemplateService;
import nc.uap.ctrl.tpl.print.init.DefaultPrintService;
import nc.uap.lfw.core.AppInteractionUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifAddCmd;
import nc.uap.lfw.core.cmd.UifAttachCmd;
import nc.uap.lfw.core.cmd.UifCopyCmd;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.cmd.UifDelCmdRV;
import nc.uap.lfw.core.cmd.UifLineDelCmd;
import nc.uap.lfw.core.cmd.UifPlugoutCmd;
import nc.uap.lfw.core.cmd.UifSaveCmdRV;
import nc.uap.lfw.core.cmd.UifUpdateUIDataCmdRV;
import nc.uap.lfw.core.cmd.base.CommandStatus;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.constants.AppConsts;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.EmptyRow;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.data.RowData;
import nc.uap.lfw.core.event.DatasetCellEvent;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.file.FillFileInfoHelper;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.serializer.impl.Dataset2SuperVOSerializer;
import nc.uap.lfw.core.uif.delegator.DefaultDataValidator;
import nc.uap.lfw.file.LfwFileConstants;
import nc.uap.lfw.file.UploadFileHelper;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIElementFinder;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.portal.log.ScapLogger;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.wfm.exe.WfmCmd;
import nc.uap.wfm.utils.WfmCPUtilFacade;
import nc.uap.wfm.utils.WfmTaskUtil;
import nc.uap.wfm.vo.WfmFormInfoCtx;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import uap.lfw.core.locator.ServiceLocator;
import uap.lfw.dbl.uiengine.CommonObjectConstants;
import uap.web.bd.pub.AppUtil;

/**
 * 卡片窗口默认逻辑
 */
public class TrsCardWinMainViewCtrl<T extends WebElement> extends
		AbstractMasterSlaveViewController {
	private static final String PLUGOUT_ID = "afterSavePlugout";
	public static final String OPEN_BILL_ID = "openBillId";
	public static final String MAINVO = "mainvo";
	public static final String MAINVO_R = "mainvo_r";
	public static final String MAINVO_S = "mainvo_s";
	private ScapptTransferHVO mainvo;
	private ScapptTransferHVO mainvo_r;
	private ScapptTransferHVO mainvo_s;

	protected ScapptTransferHVO getMainvo(String attr) {
		return (ScapptTransferHVO) LfwRuntimeEnvironment.getWebContext()
				.getWebSession().getAttribute(attr);
	}

	/**
	 * 获取复制主VO
	 * 
	 * @param mainvo
	 */
	protected void setMainvo(String attr, ScapptTransferHVO mainvo) {
		LfwRuntimeEnvironment.getWebContext().getWebSession()
				.setAttribute(attr, mainvo);
	}

	public String getPKBorg() {
		String oper = LfwRuntimeEnvironment.getWebContext()
				.getOriginalParameter("app_pk_borg");
		if (oper == null) {
			oper = (String) AppLifeCycleContext.current()
					.getApplicationContext().getAppAttribute("app_pk_borg");
		}
		return oper;
	}

	public String getPKRorg() {
		String oper = LfwRuntimeEnvironment.getWebContext()
				.getOriginalParameter("app_pk_rorg");
		if (oper == null) {
			oper = (String) AppLifeCycleContext.current()
					.getApplicationContext().getAppAttribute("app_pk_rorg");
		}
		return oper;
	}

	public String getBillNoPk() {
		String oper = LfwRuntimeEnvironment.getWebContext()
				.getOriginalParameter("app_pk_billno");
		if (oper == null) {
			oper = (String) AppLifeCycleContext.current()
					.getApplicationContext().getAppAttribute("app_pk_rorg");
		}
		return oper;
	}

	public String getMainPk() {
		String currentValue = LfwRuntimeEnvironment.getWebContext()
				.getWebSession().getOriginalParameter("openBillId");
		if (currentValue == null) {
			String value = (String) LfwRuntimeEnvironment.getWebContext()
					.getWebSession().getOriginalParameter("pk_transfer_h");
			LfwRuntimeEnvironment.getWebContext().getWebSession()
					.addOriginalParameter("openBillId", value);
		}
		return currentValue;
	}

	/**
	 * 页面显示事件
	 * 
	 * @param dialogEvent
	 */
	public void beforeShow(DialogEvent dialogEvent) {
		Dataset masterDs = this.getMasterDs();
		masterDs.clear();

		final String oper = this.getOperator();

		uap.web.bd.pub.AppUtil.addAppAttr(LfwFileConstants.SYSID,
				LfwFileConstants.SYSID_BAFILE);

		if (AppConsts.OPE_ADD.equals(oper)) {
			CmdInvoker.invoke(new UifAddCmd(this.getMasterDsId()) {
				@Override
				protected void onBeforeRowAdd(Row row) {
					setAutoFillValue(row);
					Dataset masterDs = getMasterDs();
					String pk_primarykey = generatePk();
					row.setValue(
							masterDs.nameToIndex(masterDs.getPrimaryKeyField()),
							pk_primarykey);
					init(oper, masterDs, row);// 初始化界面
					row.setValue(masterDs.nameToIndex("attach"), pk_primarykey);
					FillFileInfoHelper.resetItem(pk_primarykey);
					FillFileInfoHelper.fillFileInfo(masterDs, row);
				}
			});
		} else if (AppConsts.OPE_EDIT.equals(oper)) {
			getMainPk();
			CmdInvoker.invoke(new UifDatasetLoadCmd(masterDs) {
				@Override
				protected void onAfterDatasetLoad() {
					setDSEnabledByTask(this.getDs());
					init(oper, this.getDs(), this.getDs().getSelectedRow());// 初始化界面
					String primaryKey = this.getDs().getPrimaryKeyField();
					if (primaryKey == null) {
						throw new LfwRuntimeException("当前Dataset没有设置主键!");
					}
					String primaryKeyValue = (String) this.getDs()
							.getSelectedRow()
							.getValue(this.getDs().nameToIndex(primaryKey));
					FillFileInfoHelper.resetItem(primaryKeyValue);
				}
			});
		} else if ("approve".equals(oper)) {
			getMainPk();
			CmdInvoker.invoke(new UifDatasetLoadCmd(masterDs) {
				@Override
				protected void onAfterDatasetLoad() {
					setDSEnabledByTask(this.getDs());
					init(oper, this.getDs(), this.getDs().getSelectedRow());// 初始化界面
					String primaryKey = this.getDs().getPrimaryKeyField();
					if (primaryKey == null) {
						throw new LfwRuntimeException("当前Dataset没有设置主键!");
					}
					String primaryKeyValue = (String) this.getDs()
							.getSelectedRow()
							.getValue(this.getDs().nameToIndex(primaryKey));
					FillFileInfoHelper.resetItem(primaryKeyValue);
				}
			});
		} else if ("look".equals(oper)) {
			getMainPk();
			CmdInvoker.invoke(new UifDatasetLoadCmd(masterDs) {
				@Override
				protected void onAfterDatasetLoad() {
					setDSEnabledByTask(this.getDs());
					init(oper, this.getDs(), this.getDs().getSelectedRow());// 初始化界面
					String primaryKey = this.getDs().getPrimaryKeyField();
					if (primaryKey == null) {
						throw new LfwRuntimeException("当前Dataset没有设置主键!");
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
	 * 保存
	 * 
	 * @param datasetEvent
	 */
	public void onSave(MouseEvent<?> mouseEvent) throws BusinessException {
		Dataset masterDs = this.getMasterDs();
		CmdInvoker.invoke(new UifSaveCmdRV(this.getMasterDsId(), this
				.getDetailDsIds(), false) {
			protected void onVoSave(SuperVO aggVo) {
				try {
					String errmsg = checkData(aggVo);// 获取错误信息
					if (errmsg != null && !errmsg.equals("")) {
						throw new LfwRuntimeException(errmsg).setTitle("提示");
					}
					checkPassDeal(aggVo);// 检验通过后处理逻辑
					IUifCpbService cpbService = NCLocator.getInstance().lookup(
							IUifCpbService.class);
					cpbService.insertOrUpdateSuperVO(aggVo,
							this.isNotifyBDCache());
				} catch (BusinessException e) {
					Logger.error(e, e);
					throw new LfwRuntimeException(e.getMessage());
				}

			}
		});
		masterDs.setEnabled(true);
		AppInteractionUtil.showShortMessage("保存成功!");
		// 设置页面改变状态
		AppLifeCycleContext.current().getViewContext().getView().getPagemeta()
				.setHasChanged(false);
		// 关闭当前窗口
		AppLifeCycleContext
				.current()
				.getApplicationContext()
				.getCurrentWindowContext()
				.closeView(
						AppLifeCycleContext.current().getViewContext().getId());
		this.getCurrentAppCtx().closeWinDialog();

		CmdInvoker.invoke(new UifPlugoutCmd(this.getCurrentView().getId(),
				PLUGOUT_ID));
	}

	/**
	 * 数据检验
	 * 
	 * @param aggVo
	 * @return
	 */
	public String checkData(SuperVO aggVo) {
		String esg = "";
		esg = checkSelfData(aggVo);
		return esg;
	}

	/**
	 * 检验自己的逻辑
	 * 
	 * @param aggVo
	 * @return
	 */
	protected String checkSelfData(SuperVO aggVo) {
		String err = "";
		ScapptTransferHVO mainvo = (ScapptTransferHVO) aggVo;
		ScapptTransferAssessVO[] assvo = mainvo.getId_scappt_transfer_assess();
		ScapptTransferTargetVO[] targetvo = mainvo
				.getId_scappt_transfer_target();
		ScapptTransferTransfereeVO[] trfervo = mainvo
				.getId_scappt_transfer_transferee();
		ScapptTransferTransferorVO[] trafervos = mainvo
				.getId_scappt_transfer_transferor();
		if (assvo != null) {
			UFDouble assDouble = new UFDouble();
			for (ScapptTransferAssessVO tmp : assvo) {
				if (tmp.getStatus() == VOStatus.DELETED) {
					continue;
				}
				if (tmp.getDchangepercent() != null) {
					assDouble = assDouble.add(tmp.getDchangepercent());
					err += new UFDouble(100).sub(tmp.getDchangepercent())
							.toDouble() >= 0 ? "" : "资产评估->评估增值率不能超过100% <br/>";
				}
				if (tmp.getVproject() != null) {
					if (tmp.getVproject().equals("转让标的对应价值值")
							|| tmp.getVproject().equals("并购标的对应价值值")) {
						tmp.setVbdef5("Y");
					}
				}
			}
			if (assDouble.sub(new UFDouble(100)).toDouble() > 0) {
				return "资产评估->评估增值率总和不能超过100%";
			}
		}
		if (targetvo != null) {
			UFDouble assDouble = new UFDouble();
			for (ScapptTransferTargetVO tmp : targetvo) {
				if (tmp.getStatus() == VOStatus.DELETED) {
					continue;
				}
				if (tmp.getDpercent() != null) {
					assDouble = assDouble.add(tmp.getDpercent());
					err += new UFDouble(100).sub(tmp.getDpercent()).toDouble() >= 0 ? ""
							: "股权结构->股权比例不能超过100% <br/>";
				}
			}
			if (assDouble.sub(new UFDouble(100)).toDouble() > 0) {
				return "股权结构->股权比例总和不能超过100%";
			}
		}
		if (trfervo != null) {
			UFDouble assDouble = new UFDouble();
			for (ScapptTransferTransfereeVO tmp : trfervo) {
				if (tmp.getStatus() == VOStatus.DELETED) {
					continue;
				}
				if (tmp.getDpercent() != null) {
					assDouble = assDouble.add(tmp.getDpercent());
					err += new UFDouble(100).sub(tmp.getDpercent()).toDouble() >= 0 ? ""
							: "股权结构->股权比例不能超过100% <br/>";
				}
			}
			if (assDouble.sub(new UFDouble(100)).toDouble() > 0) {
				return "股权结构->股权比例总和不能超过100%";
			}
		}
		if (trafervos != null) {
			UFDouble assDouble = new UFDouble();
			for (ScapptTransferTransferorVO tmp : trafervos) {
				if (tmp.getStatus() == VOStatus.DELETED) {
					continue;
				}
				if (tmp.getDpercent() != null) {
					assDouble = assDouble.add(tmp.getDpercent());
					err += new UFDouble(100).sub(tmp.getDpercent()).toDouble() >= 0 ? ""
							: "股权结构->股权比例不能超过100% <br/>";
				}
			}
			if (assDouble.sub(new UFDouble(100)).toDouble() > 0) {
				return "股权结构->股权比例总和不能超过100%";
			}
		}
		return err;
	}

	/**
	 * 检验通过处理
	 * 
	 * @param aggVo
	 */
	public void checkPassDeal(SuperVO aggVo) {
		checkPassSelfDeal(aggVo);
	}

	/**
	 * 检验通过后处理自身逻辑
	 * 
	 * @param aggVo
	 */
	public void checkPassSelfDeal(SuperVO aggVo) {

	}

	/**
	 * 初始化界面
	 * 
	 * @param operation
	 */
	public void init(String operation, Dataset masterDs, Row row) {
		initData(masterDs, row);
		initUI(operation);
	}

	/**
	 * 新增初始化
	 * 
	 * @param masterDs
	 * @param row
	 */
	public void initData(Dataset masterDs, Row row) {
		String oper = this.getOperator();
		String billno = getBillNoPk();// 默认使用def9存储申请主键
		String pk_borg = getPKBorg();// 产权树　标地企业PK
		String pk_rorg = getPKRorg();// 转让方产权登记PK
		String mainpk = getMainPk();
		if (mainpk != null) {
			row.setValue(masterDs.nameToIndex(masterDs.getPrimaryKeyField()),
					mainpk);
		}
		row.setValue(masterDs.nameToIndex(ScapptTransferHVO.PK_BORG), pk_borg);
		row.setValue(masterDs.nameToIndex(ScapptTransferHVO.DEF9), billno);
		row.setValue(masterDs.nameToIndex(ScapptTransferHVO.PK_RFIRMBASE_H),
				pk_rorg);
		if (oper != null && oper.equals("add")) {
			row.setValue(masterDs.nameToIndex(ScapptTransferHVO.BILLMAKER),
					CntUtil.getCntUserPk());
			row.setValue(masterDs.nameToIndex(ScapptTransferHVO.BILLMAKEDATE),
					new UFDateTime());
			row.setValue(masterDs.nameToIndex(ScapptTransferHVO.FORMSTATE),
					IGlobalConstants.SCAPPM_BILLSTATE_NOTTSTART);
		}
		setFormDataByTypt(billno, pk_borg, pk_rorg, null, masterDs, row);
		initSelfData(masterDs, row);
	}

	/**
	 * 根据类型进行数据初始化
	 * 
	 * @param billno
	 * @param pk_borg
	 * @param pk_rorg
	 * @param pk_sorg
	 * @param masterDs
	 * @param row
	 */
	public void setFormDataByTypt(String billno, String pk_borg,
			String pk_rorg, String pk_sorg, Dataset masterDs, Row row) {
		if (pk_borg != null) {
			mainvo = ScapPAPPubmethod.queryFirmVOByPk(pk_borg, billno, true);
			setMainvo(MAINVO, mainvo);
			row.setValue(
					masterDs.nameToIndex(ScapptTransferHVO.PK_BFIRMBASE_H),
					mainvo.getPk_bfirmbase_h());
			// row.setValue(masterDs.nameToIndex(ScapptTransferHVO.BDFGZJGJG),
			// mainvo.getBdgzjgjg());
			// 补充标的企业其它信息
			// row.setValue(masterDs.nameToIndex(ScapptTransferHVO.VRNAME),
			// mainvo.getDef1());
			row.setValue(masterDs.nameToIndex(ScapptTransferHVO.BDFZCJGDM),
					mainvo.getBdzcjgdm());
			row.setValue(masterDs.nameToIndex(ScapptTransferHVO.BDFGZJGJG),
					mainvo.getBdgzjgjg());
			row.setValue(masterDs.nameToIndex(ScapptTransferHVO.VBADDRESS),
					mainvo.getVbaddress());
			row.setValue(masterDs.nameToIndex(ScapptTransferHVO.CBSETUPDATE),
					mainvo.getCbsetupdate());
			row.setValue(masterDs.nameToIndex(ScapptTransferHVO.DBCAPITAL),
					mainvo.getDbcapital());
			row.setValue(masterDs.nameToIndex(ScapptTransferHVO.VBFACTYPE),
					mainvo.getVbfactype());
			// row.setValue(masterDs.nameToIndex(ScapptTransferHVO.VBUNIT),
			// mainvo.getVbunit());
			// row.setValue(masterDs.nameToIndex(ScapptTransferHVO.PK_BCURRENCY),
			// mainvo.getPk_bcurrency());

		}
		if (pk_rorg != null) {
			mainvo_r = ScapPAPPubmethod.getTrsVO(pk_rorg);
			setMainvo(MAINVO_R, mainvo_r);
			row.setValue(masterDs.nameToIndex(ScapptTransferHVO.VRNAME),
					mainvo_r.getDef1());
			row.setValue(masterDs.nameToIndex(ScapptTransferHVO.ZRFZCJGDM),
					mainvo_r.getBdzcjgdm());
			row.setValue(masterDs.nameToIndex(ScapptTransferHVO.ZRFGZJGJG),
					mainvo_r.getBdgzjgjg());
			row.setValue(masterDs.nameToIndex(ScapptTransferHVO.VGADDRESS),
					mainvo_r.getVbaddress());
			row.setValue(masterDs.nameToIndex(ScapptTransferHVO.CBSETUPDATE),
					mainvo_r.getCbsetupdate());
			row.setValue(masterDs.nameToIndex(ScapptTransferHVO.CGSETUPDATE),
					mainvo_r.getCbsetupdate());
			row.setValue(masterDs.nameToIndex(ScapptTransferHVO.DRCAPITAL),
					mainvo_r.getDbcapital());
			row.setValue(masterDs.nameToIndex(ScapptTransferHVO.DGCAPITAL),
					mainvo_r.getDbcapital());
			row.setValue(masterDs.nameToIndex(ScapptTransferHVO.VRFACTYPE),
					mainvo_r.getVbfactype());
			// row.setValue(masterDs.nameToIndex(ScapptTransferHVO.VGUNIT),
			// mainvo_r.getVbunit());
			// row.setValue(masterDs.nameToIndex(ScapptTransferHVO.PK_GCURRENCY),
			// mainvo_r.getPk_bcurrency());
		}
		if (pk_sorg != null) {
			mainvo_s = ScapPAPPubmethod.queryFirmVOByPk(pk_sorg, billno, true);
			setMainvo(MAINVO_S, mainvo_s);
			row.setValue(masterDs.nameToIndex(ScapptTransferHVO.VSFACTYPE),
					mainvo_s.getVbfactype());
			row.setValue(masterDs.nameToIndex(ScapptTransferHVO.VAGENCY),
					mainvo_s.getBdgzjgjg());
			row.setValue(masterDs.nameToIndex(ScapptTransferHVO.DSCAPITAL),
					mainvo_s.getDbcapital());
		}
	}

	/**
	 * 初始化界面－控制界面按钮及字段显隐
	 * 
	 * @param operation
	 */
	public void initUI(String operation) {
		if ("look".equals(operation)) {
			CpUIctrl.hideMenuBarReal(CpWinUtil.getView(), "menubar",
					new String[] { "back" });
			setDatasetEable(new String[] { "scappt_transfer_h",
					"scappt_transfer_transferor", "scappt_transfer_target",
					"scappt_transfer_assess", "scappt_transfer_transferee" },
					false);
			setBtnImgHidden(new String[] { "scappt_transfer_transferor_grid",
					"scappt_transfer_target_grid",
					"scappt_transfer_assess_grid",
					"scappt_transfer_transferee_grid" });
		} else if ("approve".equals(operation)) {
			CpUIctrl.hideMenuBarReal(CpWinUtil.getView(), "menubar", null);
			// setDatasetEable(new String[] { "scappt_transfer_h",
			// "scappt_transfer_transferor", "scappt_transfer_target",
			// "scappt_transfer_assess", "scappt_transfer_transferee" },
			// false);
			// setBtnImgHidden(new String[] { "scappt_transfer_transferor_grid",
			// "scappt_transfer_target_grid",
			// "scappt_transfer_assess_grid",
			// "scappt_transfer_transferee_grid" });
		}
		initSelfUI(operation);
	}

	public void setBtnImgShow(String[] gridIds) {
		for (String tmp : gridIds) {
			GridComp gr = (GridComp) CpWinUtil.getComponent(tmp);
			if (gr != null) {
				gr.setShowImageBtn(true);
			}
		}
	}

	public void setBtnImgHidden(String[] gridIds) {
		for (String tmp : gridIds) {
			GridComp gr = (GridComp) CpWinUtil.getComponent(tmp);
			if (gr != null) {
				gr.setShowImageBtn(false);
			}
		}
	}

	public void setDatasetEable(String[] datasets, boolean flag) {
		for (String tmp : datasets) {
			Dataset ds = CpWinUtil.getDataset(tmp);
			if (ds != null) {
				ds.setEnabled(flag);
			}
		}
	}

	public void setCompHidden(String[] comps) {
		for (String tmp : comps) {
			UIElement ui = (UIElement) UIElementFinder.findElementById(
					(UIMeta) CpWinUtil.getWinCtx().getUiMeta(), tmp);
			if (ui != null) {
				ui.setVisible(false);
			}
		}
	}

	/**
	 * 初始化自身的数据
	 * 
	 * @param masterDs
	 * @param row
	 */
	protected void initSelfData(Dataset masterDs, Row row) {
		ScapptTransferHVO vo = getMainvo(MAINVO);
		if (vo == null)
			return;
		String m_vpcode = (String) row.getValue(masterDs
				.nameToIndex(ScapptTransferHVO.VPCODE));
		// 先用评估编码判断
		if (m_vpcode == null || "".equals(m_vpcode)) {
			// 初始化资产评估基础信息
			row.setValue(masterDs.nameToIndex(ScapptTransferHVO.PK_PASSESS),
					vo.getPk_passess());
			row.setValue(masterDs.nameToIndex(ScapptTransferHVO.VPCODE),
					vo.getVpcode());
			row.setValue(masterDs.nameToIndex(ScapptTransferHVO.VPORGNAME),
					vo.getVporgname());
			row.setValue(masterDs.nameToIndex(ScapptTransferHVO.VPNUMBER),
					vo.getVpnumber());
			row.setValue(masterDs.nameToIndex(ScapptTransferHVO.VPDATE),
					vo.getVpdate());
			row.setValue(masterDs.nameToIndex(ScapptTransferHVO.VPBASEDATE),
					vo.getVpbasedate());
			row.setValue(masterDs.nameToIndex(ScapptTransferHVO.VGUNIT), "人民币");
			row.setValue(masterDs.nameToIndex(ScapptTransferHVO.PK_GCURRENCY),
					"1002Z0100000000001K1");
			row.setValue(masterDs.nameToIndex(ScapptTransferHVO.VJUNIT), "人民币");
			row.setValue(masterDs.nameToIndex(ScapptTransferHVO.PK_JCURRENCY),
					"1002Z0100000000001K1");
			// row.setValue(masterDs.nameToIndex(ScapptTransferHVO.ZCPGBZ),
			// "人民币");
			// row.setValue(masterDs.nameToIndex(ScapptTransferHVO.ZCPGDW),
			// "万元");
		}
	}

	/**
	 * 初始化自身的UI界面
	 * 
	 * @param operation
	 */
	protected void initSelfUI(String operation) {

	}

	/**
	 * 获取任务PK
	 * 
	 * @return String
	 */
	private String getPkTask() {
		String pk = LfwRuntimeEnvironment.getWebContext().getOriginalParameter(
				WfmConstants.WfmUrlConst_TaskPk);
		if (pk == null) {
			pk = (String) this.getCurrentAppCtx().getAppAttribute(
					WfmConstants.WfmUrlConst_TaskPk);
		}
		return pk;
	}

	/**
	 * 根据流程任务设置数据集使用状态
	 * 
	 * @param ds
	 */
	private void setDSEnabledByTask(Dataset ds) {
		if (ds != null) {
			Object task = WfmTaskUtil.getTaskFromSessionCache(this.getPkTask());
			if (task != null) {
				if (WfmTaskUtil.isEndState(task)
						|| WfmTaskUtil.isFinishState(task)
						|| WfmTaskUtil.isSuspendedState(task)
						|| WfmTaskUtil.isCanceledState(task)) {
					ds.setEnabled(false);
				} else {
					ds.setEnabled(true);
				}
			} else {
				ds.setEnabled(true);
			}
		}
	}

	/**
	 * 设置PK_ORG字段值
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
		}
	}

	/**
	 * 主数据选中逻辑
	 * 
	 * @param datasetEvent
	 */
	public void onAfterRowSelect(DatasetEvent dsEvent) {
		Dataset ds = dsEvent.getSource();
		CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()) {
			protected void updateButtons() {

			}
		});
		initChildData(ds);
		String value = (String) ds.getValue(ds
				.nameToIndex(ScapptTransferHVO.PK_TRANSFER_H));
		Dataset attachDs = CpWinUtil.getDataset("scappt_attach");
		if (attachDs.getRowCount() <= 0) {
			queryAttach(CpWinUtil.getDataset("scappt_attach"), value);
		}
	}

	/**
	 * 初始化子表数据
	 * 
	 * @param ds
	 */
	protected void initChildData(Dataset ds) {
		Dataset trgds = CpWinUtil.getDataset("scappt_transfer_target");
		Dataset assds = CpWinUtil.getDataset("scappt_transfer_assess");
		Dataset trands = CpWinUtil.getDataset("scappt_transfer_transferor");
		if (getMainvo(MAINVO) == null)
			return;
		// 子表为空初始化，否则获取数据库数据
		if (trgds.getRowCount() <= 0) {
			ScapptTransferTargetVO[] trgvos = getMainvo(MAINVO)
					.getId_scappt_transfer_target();
			if (trgvos != null && trgvos.length > 0) {
				for (ScapptTransferTargetVO tmp : trgvos) {
					Row emp = trgds.getEmptyRow();
					emp.setRowId(UUID.randomUUID().toString());
					emp.setValue(
							trgds.nameToIndex(ScapptTransferTargetVO.INUMBER),
							tmp.getInumber());
					emp.setValue(
							trgds.nameToIndex(ScapptTransferTargetVO.VNAME),
							tmp.getVname());
					emp.setValue(
							trgds.nameToIndex(ScapptTransferTargetVO.DPERCENT),
							tmp.getDpercent());
					trgds.addRow(emp);

				}
			}
		}
		if (trands.getRowCount() <= 0) {
			if (getMainvo(MAINVO_R) == null)
				return;
			ScapptTransferTransferorVO[] tranvos = getMainvo(MAINVO_R)
					.getId_scappt_transfer_transferor();
			if (tranvos != null && tranvos.length > 0) {
				for (ScapptTransferTransferorVO tmp : tranvos) {
					Row emp = trands.getEmptyRow();
					emp.setRowId(UUID.randomUUID().toString());
					emp.setValue(trands
							.nameToIndex(ScapptTransferTransferorVO.INUMBER),
							tmp.getInumber());
					emp.setValue(trands
							.nameToIndex(ScapptTransferTransferorVO.VNAME), tmp
							.getVname());
					emp.setValue(trands
							.nameToIndex(ScapptTransferTransferorVO.DPERCENT),
							tmp.getDpercent());
					trands.addRow(emp);

				}
			}
		}
		initAssDsRow(assds);
	}

	protected void initAssDsRow(Dataset assds) {
		if (assds.getRowCount() <= 0) {
			ScapptTransferAssessVO[] assvos = getMainvo(MAINVO)
					.getId_scappt_transfer_assess();
			if (assvos != null && assvos.length > 0) {
				for (ScapptTransferAssessVO tmp : assvos) {
					Row emp = assds.getEmptyRow();
					emp.setRowId(UUID.randomUUID().toString());
					emp.setValue(
							assds.nameToIndex(ScapptTransferAssessVO.VPROJECT),
							tmp.getVproject());
					emp.setValue(
							assds.nameToIndex(ScapptTransferAssessVO.DCARRYINGVALUE),
							tmp.getDcarryingvalue());
					emp.setValue(assds
							.nameToIndex(ScapptTransferAssessVO.DASSESSVALUE),
							tmp.getDassessvalue());
					emp.setValue(
							assds.nameToIndex(ScapptTransferAssessVO.DCHANGEPERCENT),
							tmp.getDchangepercent());
					assds.addRow(emp);
				}
				Row tmpRow = assds.getEmptyRow();
				tmpRow.setRowId(UUID.randomUUID().toString());
				tmpRow.setValue(
						assds.nameToIndex(ScapptTransferAssessVO.VPROJECT),
						"转让标的对应价值值");
				tmpRow.setValue(
						assds.nameToIndex(ScapptTransferAssessVO.VBDEF5), "Y");
				assds.addRow(tmpRow);
			}
			CpWinUtil.getDataset("scappt_transfer_assess").setEnabled(true);
		}
	}

	/**
	 * 新增
	 */
	public void onAdd(MouseEvent<?> mouseEvent) throws BusinessException {
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_BillID,
				null);
		this.resetWfmParameter();
		this.getCurrentAppCtx().addAppAttribute(
				WfmConstants.WfmAppAttr_FolwTypePk, this.getFlwTypePk());

		CmdInvoker.invoke(new UifAddCmd(this.getMasterDsId()) {
			protected void onBeforeRowAdd(Row row) {
				setAutoFillValue(row);
			}
		});
	}

	/**
	 * 查询模板对应附件
	 * 
	 * @param ds
	 */
	public void queryAttach(Dataset ds, String value) {
		String oper = this.getOperator();
		if (oper != null && oper.equals(AppConsts.OPE_ADD)) {
			String sql = "select * from scappt_attachent sa where sa.id_entity=(select sc.pk_filetype from scappt_attachmgr sc where sc.v_code='"
					+ CpPubMethod.getAppParter(PtConstants.NODE_TYPE2)
					+ "') order by v_num asc";
			try {
				ScapLogger.debug(sql);
				@SuppressWarnings("unchecked")
				List<AttachEntVO> vos = (List<AttachEntVO>) ScapDAO
						.getBaseDAO().executeQuery(sql,
								new BeanListProcessor(AttachEntVO.class));
				if (vos != null && vos.size() > 0) {
					for (AttachEntVO tmpvo : vos) {
						Row row = ds.getEmptyRow();
						row.setValue(
								ds.nameToIndex(ScapptAttachVO.ATTACH_NAME),
								tmpvo.getPk_attchfile());
						row.setValue(ds.nameToIndex(ScapptAttachVO.PK_ATTACH),
								this.generatePk());
						row.setValue(
								ds.nameToIndex(ScapptAttachVO.SUBMITSTATE),
								"未上传");
						row.setValue(
								ds.nameToIndex(ScapptAttachVO.PK_TRANSFER_H),
								value);
						ds.addRow(row);
					}
				}
			} catch (DAOException e) {
				ScapLogger.error(sql);
				throw new LfwRuntimeException("查询附件时出错!");

			}
		}
	}

	/**
	 * 打印
	 * 
	 * @param mouseEvent
	 * @throws BusinessException
	 */
	public void onPrint(MouseEvent<?> mouseEvent) throws BusinessException {
		Dataset masterDs = this.getMasterDs();
		Row row = masterDs.getSelectedRow();
		if (row == null) {
			throw new LfwRuntimeException("请选中数据!");
		}
		try {
			List<Dataset> list = new ArrayList<Dataset>(1);
			list.add(masterDs);
			DefaultPrintService printService = new DefaultPrintService();
			printService.setDatasetList(list);
			ICpPrintTemplateService service = ServiceLocator
					.getService(ICpPrintTemplateService.class);
			service.print(printService, null, this.getNodeCode());
		} catch (Exception e) {
			LfwLogger.error(e);
			throw new LfwRuntimeException(e.getMessage());
		}
	}

	private String getNodeCode() {
		return "配置了打印模板的功能节点的nodecode";
	}

	public String getFlwTypePk() {
		return "0001ZZ100000000DWU3D";
	}

	private void resetWfmParameter() {
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_TaskPk,
				null);
		this.getCurrentAppCtx().addAppAttribute(
				WfmConstants.WfmAppAttr_ScratchPad, null);
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.BILLSTATE, null);
		this.getCurrentAppCtx().addAppAttribute(
				WfmConstants.AttachFileList_Temp_Billitem, null);
		this.getCurrentAppCtx().addAppAttribute(
				WfmConstants.WfmAppAttr_ProInsPk, null);
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.RETURN_PK_TASK,
				null);
		this.getCurrentAppCtx().addAppAttribute(
				WfmConstants.WfmAppAttr_FormInFoCtx_Billitem, null);
	}

	/**
	 * 删除
	 */
	public void onDelete(MouseEvent<?> mouseEvent) throws BusinessException {
		String pk_form = LfwRuntimeEnvironment.getWebContext().getWebSession()
				.getOriginalParameter("openBillId");
		if (pk_form == null) {
			pk_form = (String) LfwRuntimeEnvironment.getWebContext()
					.getWebSession().getOriginalParameter("pk_transfer_h");
			LfwRuntimeEnvironment.getWebContext().getWebSession()
					.addOriginalParameter("openBillId", pk_form);
		}
		if (pk_form != null && !pk_form.equals("")) {
			boolean isCanDel = WfmCPUtilFacade.isCanDelBill(pk_form);
			if (isCanDel) {
				WfmCPUtilFacade.delWfmInfo(pk_form);
				CmdInvoker.invoke(new UifDelCmdRV(this.getMasterDsId()));
			} else {
				throw new LfwRuntimeException("流程已启动，无法删除单据");
			}
		} else {
			throw new LfwRuntimeException("未获取到流程单据主键值");
		}
	}

	/**
	 * 返回
	 */
	public void onBack(MouseEvent<?> mouseEvent) throws BusinessException {
		this.getCurrentAppCtx().closeWinDialog();
	}

	/**
	 * 复制
	 */
	public void onCopy(MouseEvent<?> mouseEvent) throws BusinessException {
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_BillID,
				null);
		this.resetWfmParameter();
		CmdInvoker.invoke(new UifCopyCmd(this.getMasterDsId()));
	}

	public void doTaskExecute(Map keys) {
		// 平台默认校验
		new DefaultDataValidator().validate(this.getMasterDs(),
				this.getCurrentView());
		WfmFormInfoCtx formCtx = this.getWfmFormInfoCtx();
		// 设置流程form
		this.getCurrentAppCtx().addAppAttribute(
				WfmConstants.WfmAppAttr_FormInFoCtx, formCtx);
		// 设置流程类型pk
		this.getCurrentAppCtx().addAppAttribute(
				WfmConstants.WfmAppAttr_FolwTypePk, this.getFlwTypePk());
		// 设置任务pk
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_TaskPk,
				this.getPkTask());
		// 调用流程
		CmdInvoker.invoke(new WfmCmd());
		if (CommandStatus.SUCCESS.equals(CommandStatus.getCommandStatus())) {
			CmdInvoker.invoke(new UifUpdateUIDataCmdRV((SuperVO) formCtx,
					getMasterDsId()));
			// 设置页面改变状态
			AppLifeCycleContext.current().getViewContext().getView()
					.getPagemeta().setHasChanged(false);
			// 关闭当前窗口
			AppLifeCycleContext
					.current()
					.getApplicationContext()
					.getCurrentWindowContext()
					.closeView(
							AppLifeCycleContext.current().getViewContext()
									.getId());
			this.getCurrentAppCtx().closeWinDialog();
			CmdInvoker.invoke(new UifPlugoutCmd(this.getCurrentView().getId(),
					PLUGOUT_ID));
		}
	}

	/**
	 * 子表新增
	 */
	public void onGridAddClick(MouseEvent<?> mouseEvent) {
		GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		Dataset ds = this.getCurrentView().getViewModels().getDataset(dsId);
		Row emptyRow = ds.getEmptyRow();
		ds.addRow(emptyRow);
		ds.setRowSelectIndex(ds.getRowIndex(emptyRow));
		ds.setEnabled(true);
	}

	/**
	 * 子表编辑
	 */
	public void onGridEditClick(MouseEvent<?> mouseEvent) {
		GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		Dataset ds = this.getCurrentView().getViewModels().getDataset(dsId);
		ds.setEnabled(true);
	}

	/**
	 * 子表删除
	 */
	public void onGridDeleteClick(MouseEvent<?> mouseEvent) {
		GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		CmdInvoker.invoke(new UifLineDelCmd(dsId));
	}

	/**
	 * 子表附件
	 */
	public void onAttachChild(MouseEvent<?> mouseEvent) {
		GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		Dataset ds = this.getCurrentView().getViewModels().getDataset(dsId);
		Row row = ds.getSelectedRow();
		if (row == null) {
			throw new LfwRuntimeException("请选中数据!");
		}
		uap.web.bd.pub.AppUtil.addAppAttr(LfwFileConstants.SYSID,
				LfwFileConstants.SYSID_BAFILE);
		String primaryKey = ds.getPrimaryKeyField();
		if (primaryKey == null) {
			throw new LfwRuntimeException("当前Dataset没有设置主键!");
		}
		String primaryKeyValue = (String) row.getValue(ds
				.nameToIndex(primaryKey));
		// 附件参数
		Map<String, String> param = UploadFileHelper.BuildDefaultPamater("",
				primaryKeyValue, CommonObjectConstants.AttachFileType, "");
		param.put("usescanable", "true");
		param.put("state", String.valueOf(31));
		param.put(LfwFileConstants.Filemgr_Para_OperateClazz,
				"nc.scap.transfer.fileback.TraFileBackImp");
		ScapptAttachVO[] attvos = (ScapptAttachVO[]) new Dataset2SuperVOSerializer<SuperVO>()
				.serialize(ds, ds.getCurrentRowSet().getCurrentRowData()
						.getRows());
		AppUtil.addAppAttr("attvos", attvos);
		LfwRuntimeEnvironment
		.getWebContext()
		.getWebSession()
		.addOriginalParameter(LfwFileConstants.SYSID,LfwFileConstants.SYSID_BAFILE);
		String title = "附件";
		CmdInvoker.invoke(new UifAttachCmd(title, param));
	}

	@Override
	protected String getMasterDsId() {
		return "scappt_transfer_h";
	}

	protected WfmFormInfoCtx getWfmFormInfoCtx() {
		Dataset masterDs = this.getMasterDs();
		Dataset[] detailDss = this.getDetailDs(this.getDetailDsIds());
		SuperVO richVO = this.getDs2RichVOSerializer().serialize(masterDs,
				detailDss, this.getRichVoClazz());
		return (WfmFormInfoCtx) richVO;
	}

	protected String getRichVoClazz() {
		return WfmFlwFormVO.class.getName();
	}

	public void setFormElementsEdit(Dataset ds, boolean flag) {
		FormComp form = (FormComp) ds.getView().getViewComponents()
				.getComponent("scappt_transfer_h_form");
		form.getElementById("pk_passess_name").setEditable(flag);
		form.getElementById("vpcode").setEditable(flag);
		form.getElementById("vporgname").setEditable(flag);
		form.getElementById("vpnumber").setEditable(flag);
		form.getElementById("vpdate").setEditable(flag);
		form.getElementById("vpbasedate").setEditable(flag);
	}

	public void onAfterDataChange(DatasetCellEvent datasetCellEvent) {
		// Dataset ds = datasetCellEvent.getSource();
		// if (ds.nameToIndex("pk_sorg_vname") ==
		// datasetCellEvent.getColIndex()) {
		// String pk_sorg = (String) ds.getSelectedRow().getValue(
		// ds.nameToIndex("pk_sorg"));
		// String value = (String) datasetCellEvent.getNewValue();
		// if (value != null && !value.equals("") && pk_sorg != null
		// && !pk_sorg.equals("")) {
		// // LfwRuntimeEnvironment.getWebContext().getWebSession()
		// // .addOriginalParameter("pk_sorg", value);
		// setFormDataByTypt(null, null, null, pk_sorg, ds,
		// ds.getSelectedRow());
		// Dataset assds = CpWinUtil
		// .getDataset("scappt_transfer_transferee");
		// assds.clear();
		// initTranRow(assds);
		// } else {
		// // String sorg = LfwRuntimeEnvironment.getWebContext()
		// // .getWebSession().getOriginalParameter("pk_sorg");
		// // ds.getSelectedRow().setValue(
		// // ds.nameToIndex(ScapptTransferHVO.PK_SORG), sorg);
		// // FormComp form = (FormComp) ds.getView().getViewComponents()
		// // .getComponent("scappt_transfer_h_form");
		// // form.getElementById("pk_sorg_vname").setValue(sorg);
		// // ds.getSelectedRow().setValue(ds.nameToIndex("pk_sorg_vname"),
		// // sorg);
		// ds.getSelectedRow().setValue(
		// ds.nameToIndex(ScapptTransferHVO.VSFACTYPE), null);
		// ds.getSelectedRow().setValue(
		// ds.nameToIndex(ScapptTransferHVO.VAGENCY), null);
		// ds.getSelectedRow().setValue(
		// ds.nameToIndex(ScapptTransferHVO.DSCAPITAL), null);
		// }
		// }
		Dataset ds = datasetCellEvent.getSource();
		if (ds.nameToIndex("pk_sorg_vname") == datasetCellEvent.getColIndex()) {
			String pk_sorg = (String) ds.getSelectedRow().getValue(
					ds.nameToIndex("pk_sorg"));
			String value = (String) datasetCellEvent.getNewValue();
			if (value != null && !value.equals("") && pk_sorg != null
					&& !pk_sorg.equals("")) {
				setFormDataByTypt(null, null, null, pk_sorg, ds,
						ds.getSelectedRow());
				ScapptTransferHVO svo = getMainvo(MAINVO_S);
				if (svo == null)
					return;
				Row row = ds.getSelectedRow();
				row.setValue(ds.nameToIndex(ScapptTransferHVO.PK_SCURRENCY),
						svo.getPk_bcurrency());
				row.setValue(ds.nameToIndex(ScapptTransferHVO.VSUNIT),
						svo.getVbunit());
				row.setValue(ds.nameToIndex(ScapptTransferHVO.VSNAME),
						svo.getDef1());
				row.setValue(ds.nameToIndex(ScapptTransferHVO.DEF4), null);
				initTrsFreeChild(true);
			} else {
				ds.getSelectedRow().setValue(
						ds.nameToIndex(ScapptTransferHVO.VSFACTYPE), null);
				ds.getSelectedRow().setValue(
						ds.nameToIndex(ScapptTransferHVO.VAGENCY), null);
				ds.getSelectedRow().setValue(
						ds.nameToIndex(ScapptTransferHVO.VSNAME), null);
				ds.getSelectedRow().setValue(
						ds.nameToIndex(ScapptTransferHVO.DSCAPITAL), null);
				deleteRow();// 受让方股权结构自动清除
			}
		}
		if (ds.nameToIndex("def4") == datasetCellEvent.getColIndex()) {
			String value = (String) datasetCellEvent.getNewValue();
			if (value != null && !"".equals(value)) {
				ds.getSelectedRow().setValue(
						ds.nameToIndex(ScapptTransferHVO.PK_SORG), null);
				initTrsFreeChild(false);
			}
		}
		if (ds.nameToIndex("dwdownpayment") == datasetCellEvent.getColIndex()) {
			UFDouble dwtransprice = (UFDouble) ds.getSelectedRow().getValue(
					ds.nameToIndex("dwtransprice"));
			if (dwtransprice == null)
				return;
			String dwdownpayment = (String) datasetCellEvent.getNewValue();
			if (new UFDouble(dwdownpayment).sub(dwtransprice).toDouble() > 0) {
				AppInteractionUtil.showShortMessage("首付大于成交金额!");
				ds.getSelectedRow().setValue(
						ds.nameToIndex(ScapptTransferHVO.DWDOWNPAYMENT), null);
			}

		}
		if (ds.nameToIndex("dwdownpercent") == datasetCellEvent.getColIndex()) {
			String dwdownpercent = (String) datasetCellEvent.getNewValue();
			if (new UFDouble(dwdownpercent).sub(100).toDouble() > 0) {
				AppInteractionUtil.showShortMessage("首付比例超过100%!");
				ds.getSelectedRow().setValue(
						ds.nameToIndex(ScapptTransferHVO.DWDOWNPERCENT), null);
			}

		}
	}

	/**
	 * 受让方组织机构编码修改之后,受让方股权结构自动清除
	 */
	public void deleteRow() {
		Dataset trands = CpWinUtil.getDataset("scappt_transfer_transferee");
		RowData rowdata = trands.getCurrentRowSet().getCurrentRowData();
		if (rowdata != null) {
			Row[] rows = rowdata.getRows();
			if (rows != null) {
				for (Row tmpRow : rows) {
					if (tmpRow instanceof EmptyRow) {
						continue;
					}
					trands.setSelectedIndex(trands.getRowIndex(tmpRow));
					CmdInvoker.invoke(new UifLineDelCmd(trands.getId()));
				}
			}
		}
	}

	public void initTrsFreeChild(boolean flag) {
		Dataset trands = CpWinUtil.getDataset("scappt_transfer_transferee");
		if (getMainvo(MAINVO_S) == null)
			return;
		deleteRow();// 受让方股权结构自动清除
		trands.clear();
		if (flag) {
			// 子表为空初始化，否则获取数据库数据
			if (trands.getRowCount() <= 0) {
				ScapptTransferTargetVO[] trgvos = getMainvo(MAINVO_S)
						.getId_scappt_transfer_target();
				if (trgvos != null && trgvos.length > 0) {
					for (ScapptTransferTargetVO tmp : trgvos) {
						Row emp = trands.getEmptyRow();
						emp.setRowId(UUID.randomUUID().toString());
						emp.setValue(trands
								.nameToIndex(ScapptTransferTargetVO.INUMBER),
								tmp.getInumber());
						emp.setValue(trands
								.nameToIndex(ScapptTransferTargetVO.VNAME), tmp
								.getVname());
						emp.setValue(trands
								.nameToIndex(ScapptTransferTargetVO.DPERCENT),
								tmp.getDpercent());
						trands.addRow(emp);

					}
				}
			}
		}
	}

	/**
	 * 初始化子表数据行
	 * 
	 * @param assvos
	 * @param assds
	 */
	protected void initTranRow(Dataset assds) {
		ScapptTransferHVO mainvo_s = getMainvo(MAINVO_S);
		if (mainvo_s == null) {
			return;
		}
		ScapptTransferTargetVO[] assvos = mainvo_s
				.getId_scappt_transfer_target();
		if (assvos != null && assvos.length > 0) {
			for (ScapptTransferTargetVO tmp : assvos) {
				Row emp = assds.getEmptyRow();
				emp.setRowId(UUID.randomUUID().toString());
				emp.setValue(
						assds.nameToIndex(ScapptTransferTransferorVO.INUMBER),
						tmp.getInumber());
				emp.setValue(
						assds.nameToIndex(ScapptTransferTransferorVO.VNAME),
						tmp.getVname());
				emp.setValue(
						assds.nameToIndex(ScapptTransferTransferorVO.DPERCENT),
						tmp.getDpercent());
				assds.addRow(emp);
			}
		}
	}

	public void onAttach(MouseEvent mouseEvent) {
		GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		Dataset ds = this.getCurrentView().getViewModels().getDataset(dsId);
		Row row = ds.getSelectedRow();
		if (row == null) {
			throw new LfwRuntimeException("请选中数据!");
		}
		String primaryKey = ds.getPrimaryKeyField();
		if (primaryKey == null) {
			throw new LfwRuntimeException("当前Dataset没有设置主键!");
		}
		int cstate = 1;
		boolean flag = CntUtil.CtnUserIsCompanyUser();
		if (flag) {
			cstate = 31;
		} else {
			cstate = 9;
		}
		String oper = this.getOperator();
		if ("look".equals(oper)) {
			cstate = 1;
		}
		String primaryKeyValue = (String) row.getValue(ds
				.nameToIndex(primaryKey));
		ScapptAttachVO[] attvos = (ScapptAttachVO[]) new Dataset2SuperVOSerializer<SuperVO>()
				.serialize(ds, ds.getCurrentRowSet().getCurrentRowData()
						.getRows());
		AppUtil.addAppAttr("attvos", attvos);
		AppUtil.addAppAttr(LfwFileConstants.SYSID,
				LfwFileConstants.SYSID_BAFILE);
		Map<String, String> param = UploadFileHelper.BuildDefaultPamater(
				LfwFileConstants.SYSID_BAFILE, primaryKeyValue,
				CommonObjectConstants.AttachFileType, "");
		param.put("usescanable", "true");
		param.put("state", String.valueOf(cstate));
		param.put(LfwFileConstants.Filemgr_Para_OperateClazz,
				"nc.scap.transfer.fileback.TraFileBackImp");
		String title = "附件";
		LfwRuntimeEnvironment
		.getWebContext()
		.getWebSession()
		.addOriginalParameter(LfwFileConstants.SYSID,LfwFileConstants.SYSID_BAFILE);
		CmdInvoker.invoke(new UifAttachCmd(title, param));
	}
}
