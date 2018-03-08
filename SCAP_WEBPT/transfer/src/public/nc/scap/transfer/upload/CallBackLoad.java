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
 * Excel数据导入原则: 1.唯一性优先,违反唯一性导入失败 2.Excel数据中各字段与数据表检验不一致或者参照未找到也能通过,通过提示信息用户自己调整
 * 
 * @author wupengl
 * 
 */
public class CallBackLoad {
	/***
	 * 1.解析Excel数据2.检验数据 3.初始化数据 4.保存
	 * 
	 * @param e
	 */
	public void onUploadedExcelFile(ScriptEvent e) {
		List<ScapptTransferHVO> mainvos = onMainImport();// 解析Excel数据
		String msg = checkData(mainvos);// 数据检验
		if (!"".equals(msg)) {
			throw new LfwRuntimeException(msg);
		}
		String onlyStr = onlyOneCheck(mainvos);// 数据是否覆盖检验
		if (!"".equals(onlyStr)) {
			if (!AppInteractionUtil.showConfirmDialog("提示", onlyStr + " 是否覆盖?")) {
				return;
			}
		}
		List<ScapptTransferHVO> insertvos = new ArrayList<ScapptTransferHVO>();// 新增数据结果
		List<ScapptTransferAssessVO> childinstvos = new ArrayList<ScapptTransferAssessVO>();// 新增产权评估结果
		List<ScapptTransferHVO> updatevos = new ArrayList<ScapptTransferHVO>();// 匹配数据结果
		List<ScapptTransferAssessVO> childudtvos = new ArrayList<ScapptTransferAssessVO>();// 匹配产权评估数据结果
		getDBData(mainvos, insertvos, updatevos);// 分隔数据,完成简单数据复制
		String sus = "成功匹配编码:";// 成功匹配数据
		String noper = "未匹配编码:";// 未找到数据来源
		initData(insertvos, updatevos, childinstvos, childudtvos, sus, noper);// 数据初始化(字定义数据/子表数据)
		ScapptImportitf itf = NCLocator.getInstance().lookup(
				ScapptImportitf.class);
		itf.save(insertvos, updatevos, childinstvos, childudtvos);
		AppInteractionUtil.showMessageDialog(noper + "<br/>" + sus);
	}

//	/***
//	 * 保存
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
//			throw new LfwRuntimeException("数据批量保存有误！" + e.getMessage());
//		}
//	}

	/**
	 * 数据初始化
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
			cvo.setVproject("净资产");
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
							&& "净资产".equals(tmpvo.getVproject())) {
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
	 * 根据list code 和doc code 查找
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
	 * 数据检验
	 * 
	 * @param mainvos
	 * @return
	 */
	public String checkData(List<ScapptTransferHVO> mainvos) {
		String erg = "";
		if (mainvos == null || mainvos.size() <= 0) {
			erg = "无数据";
		}
		// Excel中编码唯一性检验
		for (int i = 0; i < mainvos.size() - 1; i++) {
			for (int j = i; j < mainvos.size() - i - 1; j++) {
				ScapptTransferHVO vo1 = mainvos.get(i);
				ScapptTransferHVO vo2 = mainvos.get(j + 1);
				if (vo1.getVrequestno().equals(vo2.getVrequestno())) {
					erg = "项目编码不能相同";
					return erg;
				}
			}
		}
		return erg;
	}

	/**
	 * 唯一性检验(同时查询并替换转让方，标地企业)
	 * 
	 * @param mainvos
	 * @return
	 */
	public String onlyOneCheck(List<ScapptTransferHVO> mainvos) {
		// Excel中编码与数据库数据唯一性检验
		String erg = "";
		for (ScapptTransferHVO tmpvo : mainvos) {
			try {
				// 转让方
				List<FirmtreeVO> rnamevo = (List<FirmtreeVO>) ScapDAO
						.getBaseDAO().retrieveByClause(FirmtreeVO.class,
								"vname='" + tmpvo.getVrname() + "' and dr=0");
				if (rnamevo == null || rnamevo.size() <= 0) {
					throw new LfwRuntimeException("企业未登记:" + tmpvo.getVrname());
				} else {
					tmpvo.setVrname(rnamevo.get(0).getVname());
					tmpvo.setPk_rorg(rnamevo.get(0).getPk_firmtree());
				}
				// 标地企业
				List<FirmtreeVO> borgvo = (List<FirmtreeVO>) ScapDAO
						.getBaseDAO().retrieveByClause(FirmtreeVO.class,
								"vname='" + tmpvo.getPk_borg() + "' and dr=0");
				if (borgvo == null || borgvo.size() <= 0) {
					throw new LfwRuntimeException("企业登记信息中未找到:"
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
					erg += "项目编码:" + rtvo.get(0).getVrequestno() + "已存在<br/>";
				}
			} catch (DAOException e) {
				throw new LfwRuntimeException("检验项目编码唯一性时出错:"
						+ tmpvo.getVrequestno());
			}
		}
		return erg;
	}

	/**
	 * 区分新增数据与要修改数据
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
					copyData(tmpvo, rtvo.get(0));// 导入数据部分数据合并
					List<ScapptTransferAssessVO> childvos = (List<ScapptTransferAssessVO>) ScapDAO
							.getBaseDAO().retrieveByClause(
									ScapptTransferAssessVO.class,
									"pk_transfer_h='"
											+ rtvo.get(0).getPk_transfer_h()
											+ "' and dr=0");
					rtvo.get(0).setId_scappt_transfer_assess(
							childvos.toArray(new ScapptTransferAssessVO[0]));// 初始化产权评估表
					updatevos.addAll(rtvo);
				} else {
					tmpvo.setIsmerge("N");
					tmpvo.setIsleadin(new UFBoolean(true));
					insertvos.add(tmpvo);
				}
			} catch (DAOException e) {
				throw new LfwRuntimeException("检验项目编码唯一性时出错:"
						+ tmpvo.getVrequestno());
			}
		}
	}

	/**
	 * 数据复制
	 * 
	 * @param importvo
	 * @param preductvo
	 */
	public void copyData(ScapptTransferHVO importvo, ScapptTransferHVO preductvo) {
		// 未写入　转让方经济类型　国资监管类型　受让方经济类型　
		preductvo.setVrequestno(importvo.getVrequestno());// 项目编号
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
		preductvo.setIsmerge("Y");// 是否合并
		preductvo.setIsleadin(new UFBoolean(true));// 是否导入数据
		preductvo.setJzcpgz(importvo.getJzcpgz());// 资产评估
	}

	/**
	 * Excel数据读取
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
		// sconfig.setSheetName("交易纪录");
		Map<String, String[]> maps = new LinkedHashMap<String, String[]>() {
			{
				put(ScapptTransferHVO.VREQUESTNO, new String[] { "项目编号",
						"STRING", "false" });
				put(ScapptTransferHVO.VRNAME, new String[] { "转让方名称", "STRING",
						"false" });
				put(ScapptTransferHVO.VCASSTYPE, new String[] { "转让方经济类型",
						"STRING", "true" });
				put(ScapptTransferHVO.CASSETSTYPE, new String[] { "国资监管类型",
						"STRING", "true" });
				put(ScapptTransferHVO.VCASSESSNAME, new String[] { "转让标的名称",
						"STRING", "false" });// 企业名称+股权
				put(ScapptTransferHVO.VPCODE, new String[] { "评估核准（备案）机构",
						"STRING", "true" });
				put(ScapptTransferHVO.VPORGNAME, new String[] { "批准单位名称",
						"STRING", "true" });
				put(ScapptTransferHVO.PK_BORG, new String[] { "转让标的企业名称",
						"STRING", "false" });
				put(ScapptTransferHVO.INDUSTRY, new String[] { "行业", "STRING",
						"true" });
				put(ScapptTransferHVO.JZCPGZ, new String[] { "净资产评估值（万元）",
						"DOUBLE", "true" });
				put(ScapptTransferHVO.DBPERCENT, new String[] { "转让产股权比例",
						"DOUBLE", "true" });
				put(ScapptTransferHVO.DPCARRYING, new String[] { "转让标的评估值（万元）",
						"DOUBLE", "true" });
				put(ScapptTransferHVO.VWASSIGNNAME, new String[] { "受让方名称",
						"STRING", "true" });
				put(ScapptTransferHVO.VWASSIGTYPE, new String[] { "受让方经济类型",
						"STRING", "true" });
				put(ScapptTransferHVO.DWPRICE, new String[] { "挂牌价格（万元）",
						"DOUBLE", "true" });
				put(ScapptTransferHVO.DWTRANSPRICE, new String[] { "成交金额（万元）",
						"DOUBLE", "true" });
				put(ScapptTransferHVO.DJPRICE, new String[] { "交易增减值",
						"DOUBLE", "true" });
				put(ScapptTransferHVO.DBASSETS, new String[] { "资产总额（万元）",
						"DOUBLE", "true" });
				put(ScapptTransferHVO.VWTRANSTYPE, new String[] { "成交方式",
						"STRING", "true" });
				put(ScapptTransferHVO.CWCTDATE, new String[] { "合同签署日期",
						"STRING", "true" });
				put(ScapptTransferHVO.CWFORENSICDATE, new String[] { "成交日期",
						"STRING", "true" });
				put(ScapptTransferHVO.TRAAREA, new String[] { "交易区域", "STRING",
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
			throw new LfwRuntimeException("Excel读取异常:" + e1.getMessage());
		}
		return listm;
	}
}
