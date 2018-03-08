package nc.scap.transfer.upload;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.scap.prregiest.vos.ScapFirmBaseHVO;
import nc.scap.pt.vos.ScapptTransferAssessVO;
import nc.scap.pt.vos.ScapptTransferHVO;
import nc.scap.ptpub.PtConstants;
import nc.scap.pub.util.ScapDAO;
import nc.scap.transfer.itf.ScapptImportitf;
import nc.scap.vos.prabs.FirmtreeVO;
import nc.uap.ctrl.excel.UifExcelImportCmd;
import nc.uap.lfw.core.AppInteractionUtil;
import nc.uap.lfw.core.ContextResourceUtil;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.event.ScriptEvent;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.pub.lang.UFBoolean;

import com.sz.common.excel.ExcelSheetConfig;
import com.sz.common.excel.ExcelToVoUtil;
import com.sz.common.excel.Property;
import com.sz.common.excel.Type;
import com.sz.common.exception.ExcelException;

/***
 * Excel���ݵ���ԭ��: 1.Ψһ������,Υ��Ψһ�Ե���ʧ�� 2.Excel�����и��ֶ������ݱ���鲻һ�»��߲���δ�ҵ�Ҳ��ͨ��,ͨ����ʾ��Ϣ�û��Լ�����
 * 
 * @author wupengl
 * 
 */
public class CallBackLoad {
	/***
	 * 1.����Excel����2.�������� 3.��ʼ������ 4.����
	 * 
	 * @param e
	 */
	public void onUploadedExcelFile(ScriptEvent e) {
		List<ScapptTransferHVO> mainvos = onMainImport();// ����Excel����
		String msg = checkData(mainvos);// ���ݼ���
		if (!"".equals(msg)) {
			throw new LfwRuntimeException(msg);
		}
		String onlyStr = onlyOneCheck(mainvos);// �����Ƿ񸲸Ǽ���
		if (!"".equals(onlyStr)) {
			if (!AppInteractionUtil.showConfirmDialog("��ʾ", onlyStr + " �Ƿ񸲸�?")) {
				return;
			}
		}
		List<ScapptTransferHVO> insertvos = new ArrayList<ScapptTransferHVO>();// �������ݽ��
		List<ScapptTransferAssessVO> childinstvos = new ArrayList<ScapptTransferAssessVO>();// ������Ȩ�������
		List<ScapptTransferHVO> updatevos = new ArrayList<ScapptTransferHVO>();// ƥ�����ݽ��
		List<ScapptTransferAssessVO> childudtvos = new ArrayList<ScapptTransferAssessVO>();// ƥ���Ȩ�������ݽ��
		getDBData(mainvos, insertvos, updatevos);// �ָ�����,��ɼ����ݸ���
		String sus = "�ɹ�ƥ�����:";// �ɹ�ƥ������
		String noper = "δƥ�����:";// δ�ҵ�������Դ
		initData(insertvos, updatevos, childinstvos, childudtvos, sus, noper);// ���ݳ�ʼ��(�ֶ�������/�ӱ�����)
		ScapptImportitf itf = NCLocator.getInstance().lookup(
				ScapptImportitf.class);
		itf.save(insertvos, updatevos, childinstvos, childudtvos);
		AppInteractionUtil.showMessageDialog(noper + "<br/>" + sus);
	}

//	/***
//	 * ����
//	 * 
//	 * @param mainvos
//	 */
//	public void save(List<ScapptTransferHVO> insertvos,
//			List<ScapptTransferHVO> updatevos,
//			List<ScapptTransferAssessVO> childistvos,
//			List<ScapptTransferAssessVO> childudtvos) {
//		try {
//			ScapDAO.getBaseDAO().insertVOList(insertvos);
//			ScapDAO.getBaseDAO().insertVOList(childistvos);
//			ScapDAO.getBaseDAO().updateVOList(updatevos);
//			ScapDAO.getBaseDAO().updateVOList(childudtvos);
//		} catch (DAOException e) {
//			throw new LfwRuntimeException("����������������" + e.getMessage());
//		}
//	}

	/**
	 * ���ݳ�ʼ��
	 * 
	 * @param
	 */
	public void initData(List<ScapptTransferHVO> insertvos,
			List<ScapptTransferHVO> updatevos,
			List<ScapptTransferAssessVO> childistvos,
			List<ScapptTransferAssessVO> childudtvos, String sus, String noper) {
		for (ScapptTransferHVO invo : insertvos) {
			sus += invo.getVrequestno() + " ";
			qryDefDataByCodeAndName(invo);
			ScapptTransferAssessVO cvo = new ScapptTransferAssessVO();
			cvo.setVproject("���ʲ�");
			cvo.setDassessvalue(invo.getJzcpgz());
			childistvos.add(cvo);
		}
		for (ScapptTransferHVO udtvo : updatevos) {
			noper += udtvo.getVrequestno() + " ";
			qryDefDataByCodeAndName(udtvo);
			ScapptTransferAssessVO[] pdvos = udtvo
					.getId_scappt_transfer_assess();
			if (pdvos != null && pdvos.length > 0) {
				for (ScapptTransferAssessVO tmpvo : pdvos) {
					if (tmpvo.getVproject() != null
							&& "���ʲ�".equals(tmpvo.getVproject())) {
						tmpvo.setDassessvalue(udtvo.getJzcpgz());
					}
				}
			}
		}
	}

	public void qryDefDataByCodeAndName(ScapptTransferHVO vo) {
		DefdocVO vo1 = getDefdocByListCodeAndName(PtConstants.SCAPPT_GZJG,
				vo.getCassetstype());
		DefdocVO vo2 = getDefdocByListCodeAndName(PtConstants.SCAPPT_CQJJ,
				vo.getVcasstype());
		// DefdocVO vo3 = getDefdocByListCodeAndName(docListCode, vo);
		if (vo1 != null) {
			vo.setCassetstype(vo1.getPk_defdoc());
		} else {
			vo.setCassetstype(null);
		}
		if (vo2 != null) {
			vo.setVcasstype(vo2.getPk_defdoc());
		} else {
			vo.setVcasstype(null);
		}
		// if (vo3 != null) {
		//
		// }

	}

	/**
	 * ����list code ��doc code ����
	 * 
	 * @param docListCode
	 * @param docCode
	 * @return
	 */
	public DefdocVO getDefdocByListCodeAndName(String docListCode,
			String docName) {
		DefdocVO defdocvo = null;
		String sql = "select * from bd_defdoc bd where BD.PK_DEFDOCLIST =(select B.PK_DEFDOCLIST from bd_defdoclist b where B.CODE='"
				+ docListCode + "') and BD.CODE='" + docName + "' ";
		defdocvo = (DefdocVO) ScapDAO.executeQuery(sql, new BeanProcessor(
				DefdocVO.class));
		return defdocvo;

	}

	/***
	 * ���ݼ���
	 * 
	 * @param mainvos
	 * @return
	 */
	public String checkData(List<ScapptTransferHVO> mainvos) {
		String erg = "";
		if (mainvos == null || mainvos.size() <= 0) {
			erg = "������";
		}
		// Excel�б���Ψһ�Լ���
		for (int i = 0; i < mainvos.size() - 1; i++) {
			for (int j = i; j < mainvos.size() - i - 1; j++) {
				ScapptTransferHVO vo1 = mainvos.get(i);
				ScapptTransferHVO vo2 = mainvos.get(j + 1);
				if (vo1.getVrequestno().equals(vo2.getVrequestno())) {
					erg = "��Ŀ���벻����ͬ";
					return erg;
				}
			}
		}
		return erg;
	}

	/**
	 * Ψһ�Լ���(ͬʱ��ѯ���滻ת�÷��������ҵ)
	 * 
	 * @param mainvos
	 * @return
	 */
	public String onlyOneCheck(List<ScapptTransferHVO> mainvos) {
		// Excel�б��������ݿ�����Ψһ�Լ���
		String erg = "";
		for (ScapptTransferHVO tmpvo : mainvos) {
			try {
				// ת�÷�
				List<FirmtreeVO> rnamevo = (List<FirmtreeVO>) ScapDAO
						.getBaseDAO().retrieveByClause(FirmtreeVO.class,
								"vname='" + tmpvo.getVrname() + "' and dr=0");
				if (rnamevo == null || rnamevo.size() <= 0) {
					throw new LfwRuntimeException("��ҵδ�Ǽ�:" + tmpvo.getVrname());
				} else {
					tmpvo.setVrname(rnamevo.get(0).getVname());
					tmpvo.setPk_rorg(rnamevo.get(0).getPk_firmtree());
				}
				// �����ҵ
				List<FirmtreeVO> borgvo = (List<FirmtreeVO>) ScapDAO
						.getBaseDAO().retrieveByClause(FirmtreeVO.class,
								"vname='" + tmpvo.getPk_borg() + "' and dr=0");
				if (borgvo == null || borgvo.size() <= 0) {
					throw new LfwRuntimeException("��ҵ�Ǽ���Ϣ��δ�ҵ�:"
							+ tmpvo.getPk_borg());
				} else {
					tmpvo.setPk_borg(borgvo.get(0).getPk_firmtree());
				}
				String conditon = "vrname='" + tmpvo.getVrname()
						+ "' and pk_borg='" + tmpvo.getPk_borg()
						+ "' and vcassessname='" + tmpvo.getVcassessname()
						+ "' and dr=0 and isleadin='Y'";
				List<ScapptTransferHVO> rtvo = (List<ScapptTransferHVO>) ScapDAO
						.getBaseDAO().retrieveByClause(ScapptTransferHVO.class,
								conditon);
				if (rtvo != null && rtvo.size() > 0) {
					erg += "��Ŀ����:" + rtvo.get(0).getVrequestno() + "�Ѵ���<br/>";
				}
			} catch (DAOException e) {
				throw new LfwRuntimeException("������Ŀ����Ψһ��ʱ����:"
						+ tmpvo.getVrequestno());
			}
		}
		return erg;
	}

	/**
	 * ��������������Ҫ�޸�����
	 * 
	 * @param mainvos
	 * @param insertvos
	 * @param updatevos
	 */
	public void getDBData(List<ScapptTransferHVO> mainvos,
			List<ScapptTransferHVO> insertvos, List<ScapptTransferHVO> updatevos) {
		for (ScapptTransferHVO tmpvo : mainvos) {
			try {
				String conditon = "vrname='" + tmpvo.getVrname()
						+ "' and pk_borg='" + tmpvo.getPk_borg()
						+ "' and vcassessname='" + tmpvo.getVcassessname()
						+ "' and dr=0 and isleadin='Y'";
				List<ScapptTransferHVO> rtvo = (List<ScapptTransferHVO>) ScapDAO
						.getBaseDAO().retrieveByClause(ScapptTransferHVO.class,
								conditon);
				if (rtvo != null && rtvo.size() > 0) {
					copyData(tmpvo, rtvo.get(0));// �������ݲ������ݺϲ�
					List<ScapptTransferAssessVO> childvos = (List<ScapptTransferAssessVO>) ScapDAO
							.getBaseDAO().retrieveByClause(
									ScapptTransferAssessVO.class,
									"pk_transfer_h='"
											+ rtvo.get(0).getPk_transfer_h()
											+ "' and dr=0");
					rtvo.get(0).setId_scappt_transfer_assess(
							childvos.toArray(new ScapptTransferAssessVO[0]));// ��ʼ����Ȩ������
					updatevos.addAll(rtvo);
				} else {
					tmpvo.setIsmerge("N");
					tmpvo.setIsleadin(new UFBoolean(true));
					insertvos.add(tmpvo);
				}
			} catch (DAOException e) {
				throw new LfwRuntimeException("������Ŀ����Ψһ��ʱ����:"
						+ tmpvo.getVrequestno());
			}
		}
	}

	/**
	 * ���ݸ���
	 * 
	 * @param importvo
	 * @param preductvo
	 */
	public void copyData(ScapptTransferHVO importvo, ScapptTransferHVO preductvo) {
		// δд�롡ת�÷��������͡����ʼ�����͡����÷��������͡�
		preductvo.setVrequestno(importvo.getVrequestno());// ��Ŀ���
		preductvo.setVrname(importvo.getVrname());

		preductvo.setPk_borg(importvo.getPk_borg());
		preductvo.setVcassessname(importvo.getVcassessname());
		preductvo.setVrname(importvo.getVrname());
		preductvo.setVpcode(importvo.getVpcode());

		preductvo.setVporgname(importvo.getVporgname());
		preductvo.setDbpercent(importvo.getDbpercent());
		preductvo.setDpcarrying(importvo.getDpcarrying());
		preductvo.setVwassignname(importvo.getVwassignname());
		preductvo.setDwprice(importvo.getDwprice());
		preductvo.setDwtransprice(importvo.getDwtransprice());
		preductvo.setDjprice(importvo.getDjprice());
		preductvo.setDbassets(importvo.getDbassets());
		preductvo.setCwctdate(importvo.getCwctdate());
		preductvo.setCwforensicdate(importvo.getCwforensicdate());
		preductvo.setIndustry(importvo.getIndustry());
		preductvo.setTramethod(importvo.getTramethod());
		preductvo.setTraarea(importvo.getTraarea());
		preductvo.setTraname(importvo.getTraname());
		preductvo.setIsmerge("Y");// �Ƿ�ϲ�
		preductvo.setIsleadin(new UFBoolean(true));// �Ƿ�������
		preductvo.setJzcpgz(importvo.getJzcpgz());// �ʲ�����
	}

	/**
	 * Excel���ݶ�ȡ
	 * 
	 * @return
	 */
	public List<ScapptTransferHVO> onMainImport() {
		AppLifeCycleContext ctx = AppLifeCycleContext.current();
		String relativePath = ctx.getParameter(UifExcelImportCmd.EXCEL_PATH);
		String appPath = ContextResourceUtil.getCurrentAppPath();
		String fullPath = appPath + "/" + relativePath;
		ExcelSheetConfig sconfig = new ExcelSheetConfig();
		sconfig.setBeginRow(3);
		// sconfig.setSheetName("���׼�¼");
		Map<String, String[]> maps = new LinkedHashMap<String, String[]>() {
			{
				put(ScapptTransferHVO.VREQUESTNO, new String[] { "��Ŀ���",
						"STRING", "false" });
				put(ScapptTransferHVO.VRNAME, new String[] { "ת�÷�����", "STRING",
						"false" });
				put(ScapptTransferHVO.VCASSTYPE, new String[] { "ת�÷���������",
						"STRING", "true" });
				put(ScapptTransferHVO.CASSETSTYPE, new String[] { "���ʼ������",
						"STRING", "true" });
				put(ScapptTransferHVO.VCASSESSNAME, new String[] { "ת�ñ������",
						"STRING", "false" });// ��ҵ����+��Ȩ
				put(ScapptTransferHVO.VPCODE, new String[] { "������׼������������",
						"STRING", "true" });
				put(ScapptTransferHVO.VPORGNAME, new String[] { "��׼��λ����",
						"STRING", "true" });
				put(ScapptTransferHVO.PK_BORG, new String[] { "ת�ñ����ҵ����",
						"STRING", "false" });
				put(ScapptTransferHVO.INDUSTRY, new String[] { "��ҵ", "STRING",
						"true" });
				put(ScapptTransferHVO.JZCPGZ, new String[] { "���ʲ�����ֵ����Ԫ��",
						"DOUBLE", "true" });
				put(ScapptTransferHVO.DBPERCENT, new String[] { "ת�ò���Ȩ����",
						"DOUBLE", "true" });
				put(ScapptTransferHVO.DPCARRYING, new String[] { "ת�ñ������ֵ����Ԫ��",
						"DOUBLE", "true" });
				put(ScapptTransferHVO.VWASSIGNNAME, new String[] { "���÷�����",
						"STRING", "true" });
				put(ScapptTransferHVO.VWASSIGTYPE, new String[] { "���÷���������",
						"STRING", "true" });
				put(ScapptTransferHVO.DWPRICE, new String[] { "���Ƽ۸���Ԫ��",
						"DOUBLE", "true" });
				put(ScapptTransferHVO.DWTRANSPRICE, new String[] { "�ɽ�����Ԫ��",
						"DOUBLE", "true" });
				put(ScapptTransferHVO.DJPRICE, new String[] { "��������ֵ",
						"DOUBLE", "true" });
				put(ScapptTransferHVO.DBASSETS, new String[] { "�ʲ��ܶ��Ԫ��",
						"DOUBLE", "true" });
				put(ScapptTransferHVO.VWTRANSTYPE, new String[] { "�ɽ���ʽ",
						"STRING", "true" });
				put(ScapptTransferHVO.CWCTDATE, new String[] { "��ͬǩ������",
						"STRING", "true" });
				put(ScapptTransferHVO.CWFORENSICDATE, new String[] { "�ɽ�����",
						"STRING", "true" });
				put(ScapptTransferHVO.TRAAREA, new String[] { "��������", "STRING",
						"true" });
			}
		};
		int i = 1;
		for (Map.Entry<String, String[]> entry : maps.entrySet()) {
			String[] strs = entry.getValue();
			Property prop = new Property(i, entry.getKey(), strs[0],
					Type.valueOf(strs[1].trim()), Boolean.valueOf(
							strs[2].trim()).booleanValue());
			sconfig.addProp(prop);
			i++;
		}
		List<ScapptTransferHVO> listm = new ArrayList<ScapptTransferHVO>();
		try {
			ExcelToVoUtil<ScapptTransferHVO> sutil = new ExcelToVoUtil<ScapptTransferHVO>();
			ScapptTransferHVO vo = new ScapptTransferHVO();
			listm = sutil.excelToVOList(sconfig, fullPath, vo);
		} catch (ExcelException e1) {
			throw new LfwRuntimeException("Excel��ȡ�쳣:" + e1.getMessage());
		}
		return listm;
	}
}
