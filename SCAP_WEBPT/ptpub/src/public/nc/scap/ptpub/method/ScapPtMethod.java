package nc.scap.ptpub.method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.scap.def.util.ScapDefUtil;
import nc.scap.prregiest.vos.ScapFirmBaseBVO;
import nc.scap.prregiest.vos.ScapFirmBaseHVO;
import nc.scap.pt.vos.LandVO;
import nc.scap.pt.vos.ScapptApplyBVO;
import nc.scap.pt.vos.ScapptApplyHVO;
import nc.scap.pt.vos.ScapptTransferHVO;
import nc.scap.ptpub.PtConstants;
import nc.scap.pub.util.ScapDAO;
import nc.scap.pub.util.ScapPAPPubmethod;
import nc.scap.vos.prabs.FirmtreeVO;
import nc.uap.cpb.baseservice.IUifCpbService;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.constants.AppConsts;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.OpenProperties;
import nc.uap.lfw.core.event.ScriptEvent;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.jsp.uimeta.UIFlowvPanel;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIPanel;
import nc.uap.lfw.jsp.uimeta.UITabComp;
import nc.uap.lfw.jsp.uimeta.UITabItem;
import nc.uap.portal.log.ScapLogger;
import nc.uap.wfm.context.PwfmContext;
import nc.uap.wfm.engine.IFrmNumBillGen;
import nc.uap.wfm.itf.IWfmFrmNumRuleQry;
import nc.uap.wfm.model.ProDef;
import nc.uap.wfm.vo.WfmFrmNumRuleVO;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDateTime;

import org.apache.commons.lang.StringUtils;

import uap.web.bd.pub.AppUtil;

public class ScapPtMethod {
	public static String getWebParter(String para) {
		return LfwRuntimeEnvironment.getWebContext().getOriginalParameter(para);
	}

	/***
	 * 根据产权申请数据初始化交易数据
	 * 
	 * @param applyvo
	 */
	public static void initTrsInfoByApplyVO(ScapptApplyHVO applyvo) {
		ScapptApplyBVO[] appbvo = applyvo.getId_scappt_apply_b();
		if (appbvo == null || appbvo.length <= 0) {
			throw new LfwRuntimeException("产权交易申请没有标地信息");
		}
		List<ScapptTransferHVO> lists = new ArrayList<ScapptTransferHVO>();
		for (ScapptApplyBVO tmpvo : appbvo) {
			ScapptTransferHVO trsvo = new ScapptTransferHVO();
			addData(trsvo, applyvo, tmpvo);
			lists.add(trsvo);
		}
		for (ScapptTransferHVO savevo : lists) {
			try {
				IUifCpbService cpbService = NCLocator.getInstance().lookup(
						IUifCpbService.class);
				cpbService.insertOrUpdateSuperVO(savevo, false);
			} catch (BusinessException e) {
				ScapLogger.error(e.getMessage());
				throw new LfwRuntimeException("关联保存产权交易信息时出错!");
			}
		}
		// String[]results = ScapDAO.getBaseDAO().insertVOList(lists);
	}

	/***
	 * 更新VO状态
	 * 
	 * @param applyvo
	 */
	public static void refshState(ScapptApplyHVO applyvo) {
		String condition = "billno='" + applyvo.getBillno() + "' and dr=0";
		ScapptTransferHVO[] vos = (ScapptTransferHVO[]) ScapDAO
				.retrieveByCondition(ScapptTransferHVO.class, condition);
		if (vos != null && vos.length > 0) {
			vos[0].setApprover(applyvo.getApprover());
			vos[0].setApprovedate(applyvo.getApprovedate());
			vos[0].setFormstate(applyvo.getFormstate());
			try {
				ScapDAO.getBaseDAO().updateVO(vos[0]);
			} catch (DAOException e) {
				ScapLogger.error(e.getMessage());
				throw new LfwRuntimeException("更新交易信息时出错!");
			}
		}
	}

	/**
	 * 为每个标地企业创建数据
	 * 
	 * @param trsvo
	 * @param applyvo
	 * @param tmpvo
	 */
	public static void addData(ScapptTransferHVO trsvo, ScapptApplyHVO applyvo,
			ScapptApplyBVO tmpvo) {
		trsvo.setStatus(VOStatus.NEW);
		trsvo.setDef9(applyvo.getPk_apply_h());// 设置交易与申请关联
		trsvo.setPk_org(applyvo.getPk_org());
		trsvo.setPk_group(applyvo.getPk_group());
		// trsvo.setBillno(applyvo.getBillno());
		trsvo.setPk_reportno(applyvo.getBillno());
		trsvo.setBillmaker(applyvo.getBillmaker());
		trsvo.setBillmakedate(new UFDateTime(applyvo.getBillmakedate()
				.toString()));
		// trsvo.setApprover(applyvo.getApprover());
		// trsvo.setApprovedate(new
		// UFDateTime(applyvo.getApprovedate().toString()));
		// trsvo.setFormstate(applyvo.getFormstate());
		// trsvo.setFormtitle(applyvo.getFormtitle());
		trsvo.setCprocesstype(applyvo.getCprocesstype());
		trsvo.setCchangetype(applyvo.getCchangetype());
		trsvo.setCtradetype(applyvo.getCtradetype());
		trsvo.setCgreattype(applyvo.getCgreattype());
		// trsvo.setCapplicant(applyvo.getCapplicant());
		// trsvo.setCapplicantdate(new UFDate(applyvo.getCapplicantdate()
		// .toString()));
		trsvo.setPk_borg(tmpvo.getPk_borg());// 标地企业
		trsvo.setPk_rorg(applyvo.getPk_rorg());// 转让方企业
		trsvo.setDbpercent(tmpvo.getDbpercent());// 拟转股份
		trsvo.setVcassessname(tmpvo.getVcassessname());// 资产名称，还有资产明细没有加
		trsvo.setDef4(tmpvo.getVcdetail());// DEF存明细
		if (tmpvo.getPk_borg() == null || "".equals(tmpvo.getPk_borg())) {
			throw new LfwRuntimeException("标地企业不成为空!");
		}
		//jiangqf6 2018年3月8日 09:44:20
		/*if (applyvo.getPk_rorg() == null || "".equals(applyvo.getPk_rorg())) {
			throw new LfwRuntimeException("转让方不成为空!");
		}*/
		// 获取产权树对象
		FirmtreeVO firmvo = (FirmtreeVO) ScapDAO.retrieveByPK(FirmtreeVO.class,
				tmpvo.getPk_borg());
		if (firmvo == null) {
			throw new LfwRuntimeException("产权树中无此标地企业!");
		}
		// 获取产权树对象
		FirmtreeVO mainfirmvo = (FirmtreeVO) ScapDAO.retrieveByPK(
				FirmtreeVO.class, applyvo.getPk_rorg());
		/*if (mainfirmvo == null) {
			throw new LfwRuntimeException("产权树中无此转让方企业!");
		}*/
		ScapFirmBaseHVO[] basevos = (ScapFirmBaseHVO[]) ScapDAO
				.retrieveByCondition(ScapFirmBaseHVO.class,
						"vcode='" + firmvo.getOrg_code()
								+ "' and dr=0 and if_now='1'");
		/*ScapFirmBaseHVO[] mainvos = (ScapFirmBaseHVO[]) ScapDAO
				.retrieveByCondition(ScapFirmBaseHVO.class, "vcode='"
						+ mainfirmvo.getOrg_code()
						+ "' and dr=0 and if_now='1'");*/
		if (basevos != null && basevos.length > 0) {
			trsvo.setPk_bfirmbase_h(basevos[0].getPk_firmbase_h());
			List<ScapFirmBaseBVO> basebvo = null;
			try {
				basebvo = (List<ScapFirmBaseBVO>) ScapDAO.getBaseDAO()
						.retrieveByClause(
								ScapFirmBaseBVO.class,
								"pk_firmbase_h='"
										+ basevos[0].getPk_firmbase_h()
										+ "' and dr=0");
				if (basebvo != null && basebvo.size() > 0) {
					ScapPAPPubmethod.initTrsChildVo(trsvo, basebvo);
				}
			} catch (DAOException e) {
				throw new LfwRuntimeException("获取标的企业登记信息出错!");
			}

		}
		/*if (mainvos != null && mainvos.length > 0) {
			trsvo.setPk_rfirmbase_h(mainvos[0].getPk_firmbase_h());
			List<ScapFirmBaseBVO> basebvo = null;
			try {
				basebvo = (List<ScapFirmBaseBVO>) ScapDAO.getBaseDAO()
						.retrieveByClause(
								ScapFirmBaseBVO.class,
								"pk_firmbase_h='"
										+ mainvos[0].getPk_firmbase_h()
										+ "' and dr=0");
				if (basebvo != null && basebvo.size() > 0) {
					ScapPAPPubmethod.initTransChildVo(trsvo, basebvo);
				}
			} catch (DAOException e) {
				throw new LfwRuntimeException("获取转让方登记信息出错!");
			}
		}*/

	}

	/***
	 * 获取动态编码
	 * 
	 * @return
	 */
	public static String getWFMBillNum(String pk_flowType) {
		ProDef proDef = PwfmContext.getCurrentBpmnSession().getProDef();
		String frmNumBillValue = "";// 单据编码动态前段
		WfmFrmNumRuleVO frmNumRuleVo = NCLocator.getInstance()
				.lookup(IWfmFrmNumRuleQry.class)
				.getFrmNumRulrVoByFlowTypePk(pk_flowType);
		if (frmNumRuleVo == null) {
			pk_flowType = proDef.getFlwtype().getPk_parent();
			if (!StringUtils.isEmpty(pk_flowType))
				frmNumRuleVo = NCLocator.getInstance()
						.lookup(IWfmFrmNumRuleQry.class)
						.getFrmNumRulrVoByFlowTypePk(pk_flowType);
		}
		if (frmNumRuleVo != null) {
			frmNumBillValue = NCLocator.getInstance()
					.lookup(IFrmNumBillGen.class)
					.genFrmNumBillByFrmDefPk_RequiresNew(pk_flowType);
		}
		return frmNumBillValue;
	}

	/**
	 * 根据参数类型获取节点编码
	 * 
	 * @return
	 */
	public static String getNodeCodeByType() {
		String node_code = "";
		String node_type = (String) AppUtil.getAppAttr(PtConstants.NODE_TYPE);
		if (PtConstants.GPZR.equals(node_type)) {
			node_code = PtConstants.NODECODE_SQGPZR;
		} else if (PtConstants.XYZR.equals(node_type)) {
			node_code = PtConstants.NODECODE_SQXYZR;
		} else if (PtConstants.WCZR.equals(node_type)) {
			node_code = PtConstants.NODECODE_SQWCZR;
		} else if (PtConstants.ZDZCGPZR.equals(node_type)) {
			node_code = PtConstants.NODECODE_SQZDZCGPZR;
		} else if (PtConstants.ZDZCXYZR.equals(node_type)) {
			node_code = PtConstants.NODECODE_SQZDZCXYZR;
		} else if (PtConstants.ZFZCZL.equals(node_type)) {
			node_code = PtConstants.NODECODE_SQZFZCZL;
		} else if (PtConstants.BTBLZZ.equals(node_type)) {
			node_code = PtConstants.NODECODE_SQBTBLZZ;
		} else if (PtConstants.BTBLJZ.equals(node_type)) {
			node_code = PtConstants.NODECODE_SQBTBLJZ;
		} else if (PtConstants.QYBG.equals(node_type)) {
			node_code = PtConstants.NODECODE_SQQYBG;
		}
		return node_code;
	}

	public static String getWinId(ScapptTransferHVO vo) {
		String winid = "";
		String change_type = vo.getCchangetype();// 产权变动类型
		String ctradtype = vo.getCtradetype();// 产权交易类型
		String cgretype = vo.getCgreattype();// 重大资产处置方式
		DefdocVO defvo = ScapDefUtil.getDefDocVo(change_type);
		String change_code = defvo.getCode();
		if (change_code.equals(PtConstants.CCHANGE_TYPE_CQZR)) {
			DefdocVO trad_defvo = ScapDefUtil.getDefDocVo(ctradtype);
			String trad_code = trad_defvo.getCode();
			if (trad_code.equals(PtConstants.CTRADETTYPE_GPZR)) {
				winid = PtConstants.WIN_GPZR;
			} else if (trad_code.equals(PtConstants.CTRADETTYPE_XYZR)) {
				winid = PtConstants.WIN_XYZR;
			} else if (trad_code.equals(PtConstants.CTRADETTYPE_WCZR)) {
				winid = PtConstants.WIN_WCZR;
			}
		} else if (change_code.equals("002")) {

		} else if (change_code.equals("003")) {

		} else if (change_code.equals("004")) {
			DefdocVO trad_defvo = ScapDefUtil.getDefDocVo(cgretype);
			String trad_code = trad_defvo.getCode();
			if (trad_code.equals("001")) {
				winid = "transferComps.trsmajorlist_cardwin";
			} else if (trad_code.equals("002")) {
				winid = "transferComps.trsmajoragree_cardwin";
			} else if (trad_code.equals("003")) {
				winid = "transferComps.trslease_cardwin";
			}

		} else if (change_code.equals("005")) {

		}

		return winid;
	}

	public static String getCodeByNodetype() {
		String code = "";
		String type = (String) AppUtil.getAppAttr(PtConstants.NODE_TYPE2);
		if (type.equals(PtConstants.GPZR)) {
			code = PtConstants.NODECODE_GPZR;
		} else if (type.equals(PtConstants.XYZR)) {
			code = PtConstants.NODECODE_XYZR;
		} else if (type.equals(PtConstants.WCZR)) {
			code = PtConstants.NODECODE_WCZR;
		} else if (type.equals(PtConstants.BTBLZZ)) {// 增减资
			code = PtConstants.NODECODE_BTBLZZ;
		} else if (type.equals(PtConstants.TBLZZ)) {// 增减资
			code = PtConstants.NODECODE_TBLZZ;
		} else if (type.equals(PtConstants.ZDZCXYZR)) {// 产权置换
			code = PtConstants.NODECODE_ZDZCXYZR;
		} else if (type.equals(PtConstants.GPZR)) {
			code = PtConstants.NODECODE_GPZR;
		}
		return code;
	}

	/**
	 * 根据信息获取相应节点
	 * 
	 * @param vo
	 * @return
	 */
	public static String getNodeCode(ScapptApplyHVO vo) {
		String nodecode = "";
		String type = vo.getVdef1();
		if (type.equals(PtConstants.GPZR)) {
			nodecode = PtConstants.NODECODE_GPZR;
		} else if (type.equals(PtConstants.XYZR)) {
			nodecode = PtConstants.NODECODE_XYZR;
		} else if (type.equals(PtConstants.WCZR)) {
			nodecode = PtConstants.NODECODE_WCZR;
		} else if (type.equals(PtConstants.BTBLZZ)) {
			nodecode = PtConstants.NODECODE_BTBLZZ;
		} else if (type.equals(PtConstants.TBLZZ)) {
			nodecode = PtConstants.NODECODE_TBLZZ;
		} else if (type.equals(PtConstants.BTBLJZ)) {
			nodecode = PtConstants.NODECODE_BTBLJZ;// 产权置换 替换不同比例减资
		} else if (type.equals(PtConstants.TDCZ)) {
			nodecode = PtConstants.NODECODE_TDCZ;// 产权置换 替换不同比例减资
		}
		// String change_type = vo.getCchangetype();// 产权变动类型
		// String ctradtype = vo.getCtradetype();// 产权交易类型
		// String cgretype = vo.getCgreattype();// 重大资产处置方式
		// String cprocesstype = vo.getCprocesstype();// 审批流程类型
		// DefdocVO defvo = ScapDefUtil.getDefDocVo(change_type);
		// String change_code = defvo.getCode();
		// if (change_code.equals(PtConstants.CCHANGE_TYPE_CQZR)) {
		// DefdocVO trad_defvo = ScapDefUtil.getDefDocVo(ctradtype);
		// String trad_code = trad_defvo.getCode();
		// if (trad_code.equals(PtConstants.CTRADETTYPE_GPZR)) {
		// nodecode = PtConstants.NODECODE_GPZR;
		// } else if (trad_code.equals(PtConstants.CTRADETTYPE_XYZR)) {
		// nodecode = PtConstants.NODECODE_XYZR;
		// } else if (trad_code.equals(PtConstants.CTRADETTYPE_WCZR)) {
		// nodecode = PtConstants.NODECODE_WCZR;
		// }
		// } else if (change_code.equals(PtConstants.CCHANGE_TYPE_BLZZ)) {
		// nodecode = PtConstants.NODECODE_BTBLZZ;
		// } else if (change_code.equals(PtConstants.CCHANGE_TYPE_QYBG)) {
		// DefdocVO cprocessTypeVo = ScapDefUtil.getDefDocVo(cprocesstype);
		// if (PtConstants.SH_HZ.equals(cprocessTypeVo.getCode())) {// 企业并购国资委核准
		// nodecode = PtConstants.NODECODE_QYBG;
		// } else {
		// nodecode = PtConstants.NODECODE_JYSQ;// 企业并购国资委备案
		// }
		// } else if (change_code.equals(PtConstants.CCHANGE_TYPE_MAJOR)) {
		// DefdocVO trad_defvo = ScapDefUtil.getDefDocVo(cgretype);
		// String trad_code = trad_defvo.getCode();
		// if (trad_code.equals(PtConstants.MAJORLISTMeTHODS)) {
		// nodecode = PtConstants.NODECODE_ZDZCGPZR;
		// } else if (trad_code.equals(PtConstants.MAJORAGREEMeTHODS)) {
		// nodecode = PtConstants.NODECODE_ZDZCXYZR;
		// } else if (trad_code.equals(PtConstants.MAJORLEASEMeTHODS)) {
		// nodecode = PtConstants.NODECODE_ZFZCZL;
		// }
		//
		// } else if (change_code.equals(PtConstants.CCHANGE_TYPE_BLJZ)) {
		// nodecode = PtConstants.NODECODE_BTBLJZ;
		// }

		return nodecode;
	}

	/**
	 * 获取内容信息
	 * 
	 * @param vo
	 * @return
	 */
	public static String getBillcnt(ScapptApplyHVO vo) {
		String title = "";
		String type = vo.getVdef1();
		if (type.equals(PtConstants.GPZR)) {
			title = "进场交易";
		} else if (type.equals(PtConstants.XYZR)) {
			title = "协议转让";
		} else if (type.equals(PtConstants.WCZR)) {
			title = "无偿转让";
		} else if (type.equals(PtConstants.BTBLZZ)) {
			title = "不同比例增减资";
		} else if (type.equals(PtConstants.TBLZZ)) {
			title = "同比例增资";
		} else if (type.equals(PtConstants.BTBLJZ)) {
			title = "产权置换";// 产权置换 替换不同比例减资
		} else if (type.equals(PtConstants.TDCZ)) {
			title = "土地资产处置";// 产权置换 替换不同比例减资
		}

		// String change_type = vo.getCchangetype();// 产权变动类型
		// String ctradtype = vo.getCtradetype();// 产权交易类型
		// String cgretype = vo.getCgreattype();// 重大资产处置方式
		// DefdocVO defvo = ScapDefUtil.getDefDocVo(change_type);
		// String change_code = defvo.getCode();
		// if (change_code.equals(PtConstants.CCHANGE_TYPE_CQZR)) {
		// DefdocVO trad_defvo = ScapDefUtil.getDefDocVo(ctradtype);
		// String trad_code = trad_defvo.getCode();
		// if (trad_code.equals(PtConstants.CTRADETTYPE_GPZR)) {
		// content.append("进场交易");
		// } else if (trad_code.equals(PtConstants.CTRADETTYPE_XYZR)) {
		// content.append("协议转让 ");
		// } else if (trad_code.equals(PtConstants.CTRADETTYPE_WCZR)) {
		// content.append("无偿转让");
		// }
		// } else if (change_code.equals(PtConstants.CCHANGE_TYPE_BLZZ)) {
		// content.append("不同比例增资 ");
		// } else if (change_code.equals(PtConstants.CCHANGE_TYPE_QYBG)) {
		// content.append("企业并购");
		// } else if (change_code.equals(PtConstants.CCHANGE_TYPE_MAJOR)) {
		// DefdocVO trad_defvo = ScapDefUtil.getDefDocVo(cgretype);
		// String trad_code = trad_defvo.getCode();
		// if (trad_code.equals(PtConstants.MAJORLISTMeTHODS)) {
		// content.append("产权置换");
		// } else if (trad_code.equals(PtConstants.MAJORAGREEMeTHODS)) {
		// content.append("产权置换");
		// } else if (trad_code.equals(PtConstants.MAJORLEASEMeTHODS)) {
		// content.append("产权置换");
		// }
		//
		// } else if (change_code.equals(PtConstants.CCHANGE_TYPE_BLJZ)) {
		// content.append("不同比例减资");
		// }

		return title;
	}

	/**
	 * 根据list code 和doc code 查找
	 * 
	 * @param docListCode
	 * @param docCode
	 * @return
	 */
	public static DefdocVO getDefdocByListCodeAndCode(String docListCode,
			String docCode) {
		DefdocVO defdocvo = null;
		String sql = "select * from bd_defdoc bd where BD.PK_DEFDOCLIST =(select B.PK_DEFDOCLIST from bd_defdoclist b where B.CODE='"
				+ docListCode + "') and BD.CODE='" + docCode + "' ";
		defdocvo = (DefdocVO) ScapDAO.executeQuery(sql, new BeanProcessor(
				DefdocVO.class));
		return defdocvo;

	}

	/**
	 * 同时隐藏卡片界面某个几FlowvPanel下某几个PanelLayout
	 * 
	 * @param um
	 * @param flowvIds
	 * @param panelIds
	 */
	public static void removeGridPanle(UIMeta um, String[] flowvIds,
			String[] panelIds) {
		for (int i = 0; i < flowvIds.length; i++) {
			removeGridPanle(um, flowvIds[i], panelIds[i]);
		}
	}

	/**
	 * 隐藏卡片界面某个FlowvPanel下某个PanelLayout
	 * 
	 * @param um
	 * @param flowvIds
	 * @param panelIds
	 */
	public static void removeGridPanle(UIMeta um, String flowvId, String panelId) {
		UIFlowvPanel uiFP = (UIFlowvPanel) um.findChildById(flowvId);// "scapav_state_attchfile_flowvpane2"
		UIPanel uiTI = (UIPanel) uiFP.findChildById(panelId);// "scapav_state_expert_panelPanel"
		uiFP.removeElement(uiTI);
	}

	/**
	 * 同时隐藏卡片界面某几个FlowvPanel下某几个PanelLayou
	 * 
	 * @param um
	 * @param flowvIds
	 * @param panelId
	 * @param tabItemId
	 */
	public static void removeTabItem(UIMeta um, String[] flowvIds,
			String panelId[], String[] tabItemId) {
		for (int i = 0; i < flowvIds.length; i++) {
			removeTabItem(um, flowvIds[i], panelId[i], tabItemId[i]);
		}
	}

	/**
	 * 隐藏列表界面的某个FlowvPanel下的某个TabItem
	 * 
	 * @param um
	 * @param flowvId
	 * @param panelId
	 * @param tabItemId
	 */
	public static void removeTabItem(UIMeta um, String flowvId, String panelId,
			String tabItemId) {
		UIFlowvPanel uiFP = (UIFlowvPanel) um.findChildById(flowvId);
		UITabComp uiUC = (UITabComp) uiFP.findChildById(panelId);
		UITabItem uiTI = (UITabItem) uiUC.findChildById(tabItemId);
		uiUC.removePanel(uiTI);
	}

	public static void onLink(String winid, String title, String operation,
			String pkValue, String pk_borg, String pk_rorg, String billno) {
		OpenProperties props = new OpenProperties(winid, title);
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put(AppConsts.OPE_SIGN, operation);
		if (pkValue != null) {
			paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
		}
		paramMap.put("app_pk_billno", billno);
		paramMap.put("app_pk_borg", pk_borg);
		paramMap.put("app_pk_rorg", pk_rorg);
		// paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
		paramMap.put("model", "nc.scap.transfer.model.TransferCardPageModel");
		props.setParamMap(paramMap);
		AppLifeCycleContext.current().getApplicationContext().navgateTo(props);

	}

	/**
	 * 获取界面ID
	 * 
	 * @return
	 */
	public static String getWinByType() {
		String winid = "transferComps.trs_gpzr_cardwin";
		String node_type = (String) AppUtil.getAppAttr(PtConstants.NODE_TYPE2);
		if (PtConstants.GPZR.equals(node_type)) {
			winid = PtConstants.WIN_GPZR;
		} else if (PtConstants.XYZR.equals(node_type)) {
			winid = PtConstants.WIN_XYZR;
		} else if (PtConstants.WCZR.equals(node_type)) {
			winid = PtConstants.WIN_WCZR;
		} else if (PtConstants.ZDZCGPZR.equals(node_type)) {
			// pos 2014-05-26 重大资产挂牌转让
			winid = "transferComps.trsmajorlist_cardwin";
		} else if (PtConstants.ZDZCXYZR.equals(node_type)) {
			// pos 2014-05-26 重大资产协议转让
			winid = "transferComps.trsmajoragree_cardwin";
		} else if (PtConstants.ZFZCZL.equals(node_type)) {
			// pos 2014-05-26 重大资产租赁
			winid = "transferComps.trsmajorlease_cardwin";
		} else if (PtConstants.BTBLZZ.equals(node_type)||PtConstants.TBLZZ.equals(node_type)) {
			// pos 2014-05-30 不同比例增资
			winid = "transferComps.capitalIncrease_cardwin";

		} else if (PtConstants.BTBLJZ.equals(node_type)) {
			// pos 2014-05-30 不同比例减资
			winid = "transferComps.trs_czzh_cardwin";
		} else if (PtConstants.QYBG.equals(node_type)) {
			// pos 2014-05-30 合并
			winid = "transferComps.commerger_cardwin";
		} else if (PtConstants.TDCZ.equals(node_type)) {
			// pos 2014-05-30 合并
			winid = "transferComps.land_cardwin";
		}
		return winid;
	}

	/**
	 * 根据交易申请主键查询具体交易
	 * 
	 * @param st1
	 * @param pk_borg
	 * @return
	 */
	public static String getBillPk(String st1, String pk_borg) {
		String billpk = null;
		String condition = "def9='" + st1 + "' and pk_borg='" + pk_borg + "'";
		try {
			List<ScapptTransferHVO> lists = (List<ScapptTransferHVO>) ScapDAO
					.getBaseDAO().retrieveByClause(ScapptTransferHVO.class,
							condition);
			if (lists != null && lists.size() > 0) {
				return lists.get(0).getPk_transfer_h();
			}
		} catch (DAOException e) {
			ScapLogger.error("根据交易申请单号查询具体交易时出错!");
		}
		return billpk;
	}
	/**
	 * 根据交易申请主键查询具体交易(土地)
	 * 
	 * @param st1
	 * @param pk_borg
	 * @return
	 */
	public static String getBillPkland(String st1, String pk_borg) {
		String billpk = null;
		String condition = "def9='" + st1 + "' and pk_firmtree='" + pk_borg + "'";
		try {
			List<LandVO> lists = (List<LandVO>) ScapDAO
					.getBaseDAO().retrieveByClause(LandVO.class,
							condition);
			if (lists != null && lists.size() > 0) {
				return lists.get(0).getPk_land();
			}
		} catch (DAOException e) {
			ScapLogger.error("根据交易申请单号查询具体交易时出错!");
		}
		return billpk;
	}

	public static String getFirmBaseHVOByTreePk(String treepk) {
		String pk = null;
		String sql = "SELECT b.* FROM scappr_firmbase_h b ,scappr_firmtree c WHERE c.pk_firmtree='"
				+ treepk
				+ "' AND b.vcode = c.org_code AND b.if_now='1' and b.itype='1' AND b.dr=0 ";
		ScapLogger.debug(sql);
		List<ScapFirmBaseHVO> lists;
		try {
			lists = (List<ScapFirmBaseHVO>) ScapDAO.getBaseDAO().executeQuery(
					sql, new BeanListProcessor(ScapFirmBaseHVO.class));
			if (lists != null && lists.size() > 0)
				return lists.get(0).getPk_firmbase_h();

		} catch (DAOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return pk;
	}

	/**
	 * 获取URL类型
	 * 
	 * @return
	 */
	public static String getNodeType() {
		String node_type = LfwRuntimeEnvironment.getWebContext()
				.getOriginalParameter(PtConstants.NODE_TYPE2);
		if (node_type == null || "".equals(node_type)) {
			node_type = (String) AppUtil.getAppAttr(PtConstants.NODE_TYPE2);
		}
		return node_type;
	}

}
