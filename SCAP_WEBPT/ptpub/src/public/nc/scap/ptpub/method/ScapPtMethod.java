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
	 * ���ݲ�Ȩ�������ݳ�ʼ����������
	 * 
	 * @param applyvo
	 */
	public static void initTrsInfoByApplyVO(ScapptApplyHVO applyvo) {
		ScapptApplyBVO[] appbvo = applyvo.getId_scappt_apply_b();
		if (appbvo == null || appbvo.length <= 0) {
			throw new LfwRuntimeException("��Ȩ��������û�б����Ϣ");
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
				throw new LfwRuntimeException("���������Ȩ������Ϣʱ����!");
			}
		}
		// String[]results = ScapDAO.getBaseDAO().insertVOList(lists);
	}

	/***
	 * ����VO״̬
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
				throw new LfwRuntimeException("���½�����Ϣʱ����!");
			}
		}
	}

	/**
	 * Ϊÿ�������ҵ��������
	 * 
	 * @param trsvo
	 * @param applyvo
	 * @param tmpvo
	 */
	public static void addData(ScapptTransferHVO trsvo, ScapptApplyHVO applyvo,
			ScapptApplyBVO tmpvo) {
		trsvo.setStatus(VOStatus.NEW);
		trsvo.setDef9(applyvo.getPk_apply_h());// ���ý������������
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
		trsvo.setPk_borg(tmpvo.getPk_borg());// �����ҵ
		trsvo.setPk_rorg(applyvo.getPk_rorg());// ת�÷���ҵ
		trsvo.setDbpercent(tmpvo.getDbpercent());// ��ת�ɷ�
		trsvo.setVcassessname(tmpvo.getVcassessname());// �ʲ����ƣ������ʲ���ϸû�м�
		trsvo.setDef4(tmpvo.getVcdetail());// DEF����ϸ
		if (tmpvo.getPk_borg() == null || "".equals(tmpvo.getPk_borg())) {
			throw new LfwRuntimeException("�����ҵ����Ϊ��!");
		}
		//jiangqf6 2018��3��8�� 09:44:20
		/*if (applyvo.getPk_rorg() == null || "".equals(applyvo.getPk_rorg())) {
			throw new LfwRuntimeException("ת�÷�����Ϊ��!");
		}*/
		// ��ȡ��Ȩ������
		FirmtreeVO firmvo = (FirmtreeVO) ScapDAO.retrieveByPK(FirmtreeVO.class,
				tmpvo.getPk_borg());
		if (firmvo == null) {
			throw new LfwRuntimeException("��Ȩ�����޴˱����ҵ!");
		}
		// ��ȡ��Ȩ������
		FirmtreeVO mainfirmvo = (FirmtreeVO) ScapDAO.retrieveByPK(
				FirmtreeVO.class, applyvo.getPk_rorg());
		/*if (mainfirmvo == null) {
			throw new LfwRuntimeException("��Ȩ�����޴�ת�÷���ҵ!");
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
				throw new LfwRuntimeException("��ȡ�����ҵ�Ǽ���Ϣ����!");
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
				throw new LfwRuntimeException("��ȡת�÷��Ǽ���Ϣ����!");
			}
		}*/

	}

	/***
	 * ��ȡ��̬����
	 * 
	 * @return
	 */
	public static String getWFMBillNum(String pk_flowType) {
		ProDef proDef = PwfmContext.getCurrentBpmnSession().getProDef();
		String frmNumBillValue = "";// ���ݱ��붯̬ǰ��
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
	 * ���ݲ������ͻ�ȡ�ڵ����
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
		String change_type = vo.getCchangetype();// ��Ȩ�䶯����
		String ctradtype = vo.getCtradetype();// ��Ȩ��������
		String cgretype = vo.getCgreattype();// �ش��ʲ����÷�ʽ
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
		} else if (type.equals(PtConstants.BTBLZZ)) {// ������
			code = PtConstants.NODECODE_BTBLZZ;
		} else if (type.equals(PtConstants.TBLZZ)) {// ������
			code = PtConstants.NODECODE_TBLZZ;
		} else if (type.equals(PtConstants.ZDZCXYZR)) {// ��Ȩ�û�
			code = PtConstants.NODECODE_ZDZCXYZR;
		} else if (type.equals(PtConstants.GPZR)) {
			code = PtConstants.NODECODE_GPZR;
		}
		return code;
	}

	/**
	 * ������Ϣ��ȡ��Ӧ�ڵ�
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
			nodecode = PtConstants.NODECODE_BTBLJZ;// ��Ȩ�û� �滻��ͬ��������
		} else if (type.equals(PtConstants.TDCZ)) {
			nodecode = PtConstants.NODECODE_TDCZ;// ��Ȩ�û� �滻��ͬ��������
		}
		// String change_type = vo.getCchangetype();// ��Ȩ�䶯����
		// String ctradtype = vo.getCtradetype();// ��Ȩ��������
		// String cgretype = vo.getCgreattype();// �ش��ʲ����÷�ʽ
		// String cprocesstype = vo.getCprocesstype();// ������������
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
		// if (PtConstants.SH_HZ.equals(cprocessTypeVo.getCode())) {// ��ҵ��������ί��׼
		// nodecode = PtConstants.NODECODE_QYBG;
		// } else {
		// nodecode = PtConstants.NODECODE_JYSQ;// ��ҵ��������ί����
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
	 * ��ȡ������Ϣ
	 * 
	 * @param vo
	 * @return
	 */
	public static String getBillcnt(ScapptApplyHVO vo) {
		String title = "";
		String type = vo.getVdef1();
		if (type.equals(PtConstants.GPZR)) {
			title = "��������";
		} else if (type.equals(PtConstants.XYZR)) {
			title = "Э��ת��";
		} else if (type.equals(PtConstants.WCZR)) {
			title = "�޳�ת��";
		} else if (type.equals(PtConstants.BTBLZZ)) {
			title = "��ͬ����������";
		} else if (type.equals(PtConstants.TBLZZ)) {
			title = "ͬ��������";
		} else if (type.equals(PtConstants.BTBLJZ)) {
			title = "��Ȩ�û�";// ��Ȩ�û� �滻��ͬ��������
		} else if (type.equals(PtConstants.TDCZ)) {
			title = "�����ʲ�����";// ��Ȩ�û� �滻��ͬ��������
		}

		// String change_type = vo.getCchangetype();// ��Ȩ�䶯����
		// String ctradtype = vo.getCtradetype();// ��Ȩ��������
		// String cgretype = vo.getCgreattype();// �ش��ʲ����÷�ʽ
		// DefdocVO defvo = ScapDefUtil.getDefDocVo(change_type);
		// String change_code = defvo.getCode();
		// if (change_code.equals(PtConstants.CCHANGE_TYPE_CQZR)) {
		// DefdocVO trad_defvo = ScapDefUtil.getDefDocVo(ctradtype);
		// String trad_code = trad_defvo.getCode();
		// if (trad_code.equals(PtConstants.CTRADETTYPE_GPZR)) {
		// content.append("��������");
		// } else if (trad_code.equals(PtConstants.CTRADETTYPE_XYZR)) {
		// content.append("Э��ת�� ");
		// } else if (trad_code.equals(PtConstants.CTRADETTYPE_WCZR)) {
		// content.append("�޳�ת��");
		// }
		// } else if (change_code.equals(PtConstants.CCHANGE_TYPE_BLZZ)) {
		// content.append("��ͬ�������� ");
		// } else if (change_code.equals(PtConstants.CCHANGE_TYPE_QYBG)) {
		// content.append("��ҵ����");
		// } else if (change_code.equals(PtConstants.CCHANGE_TYPE_MAJOR)) {
		// DefdocVO trad_defvo = ScapDefUtil.getDefDocVo(cgretype);
		// String trad_code = trad_defvo.getCode();
		// if (trad_code.equals(PtConstants.MAJORLISTMeTHODS)) {
		// content.append("��Ȩ�û�");
		// } else if (trad_code.equals(PtConstants.MAJORAGREEMeTHODS)) {
		// content.append("��Ȩ�û�");
		// } else if (trad_code.equals(PtConstants.MAJORLEASEMeTHODS)) {
		// content.append("��Ȩ�û�");
		// }
		//
		// } else if (change_code.equals(PtConstants.CCHANGE_TYPE_BLJZ)) {
		// content.append("��ͬ��������");
		// }

		return title;
	}

	/**
	 * ����list code ��doc code ����
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
	 * ͬʱ���ؿ�Ƭ����ĳ����FlowvPanel��ĳ����PanelLayout
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
	 * ���ؿ�Ƭ����ĳ��FlowvPanel��ĳ��PanelLayout
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
	 * ͬʱ���ؿ�Ƭ����ĳ����FlowvPanel��ĳ����PanelLayou
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
	 * �����б�����ĳ��FlowvPanel�µ�ĳ��TabItem
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
	 * ��ȡ����ID
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
			// pos 2014-05-26 �ش��ʲ�����ת��
			winid = "transferComps.trsmajorlist_cardwin";
		} else if (PtConstants.ZDZCXYZR.equals(node_type)) {
			// pos 2014-05-26 �ش��ʲ�Э��ת��
			winid = "transferComps.trsmajoragree_cardwin";
		} else if (PtConstants.ZFZCZL.equals(node_type)) {
			// pos 2014-05-26 �ش��ʲ�����
			winid = "transferComps.trsmajorlease_cardwin";
		} else if (PtConstants.BTBLZZ.equals(node_type)||PtConstants.TBLZZ.equals(node_type)) {
			// pos 2014-05-30 ��ͬ��������
			winid = "transferComps.capitalIncrease_cardwin";

		} else if (PtConstants.BTBLJZ.equals(node_type)) {
			// pos 2014-05-30 ��ͬ��������
			winid = "transferComps.trs_czzh_cardwin";
		} else if (PtConstants.QYBG.equals(node_type)) {
			// pos 2014-05-30 �ϲ�
			winid = "transferComps.commerger_cardwin";
		} else if (PtConstants.TDCZ.equals(node_type)) {
			// pos 2014-05-30 �ϲ�
			winid = "transferComps.land_cardwin";
		}
		return winid;
	}

	/**
	 * ���ݽ�������������ѯ���彻��
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
			ScapLogger.error("���ݽ������뵥�Ų�ѯ���彻��ʱ����!");
		}
		return billpk;
	}
	/**
	 * ���ݽ�������������ѯ���彻��(����)
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
			ScapLogger.error("���ݽ������뵥�Ų�ѯ���彻��ʱ����!");
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
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		return pk;
	}

	/**
	 * ��ȡURL����
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
