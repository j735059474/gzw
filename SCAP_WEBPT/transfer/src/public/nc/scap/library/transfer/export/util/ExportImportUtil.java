package nc.scap.library.transfer.export.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.scap.pt.vos.ScapptExportTransHVO;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.ScapDAO;
import nc.scap.vos.prabs.FirmtreeVO;
import nc.uap.cpb.baseservice.IUifCpbService;
import nc.uap.cpb.org.exception.CpbBusinessException;
import nc.uap.cpb.org.orgs.CpOrgVO;
import nc.uap.ctrl.excel.UifExcelImportCmd;
import nc.uap.lfw.core.AppInteractionUtil;
import nc.uap.lfw.core.ContextResourceUtil;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.event.ScriptEvent;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

import com.sz.common.excel.ExcelSheetConfig;
import com.sz.common.excel.ExcelToVoUtil;
import com.sz.common.excel.Property;
import com.sz.common.excel.Type;
import com.sz.common.exception.ExcelException;

public class ExportImportUtil {
	// 回调方法
		public void onUploadedExcelFile(ScriptEvent e) {
			AppLifeCycleContext ctx = AppLifeCycleContext.current();
			String relativePath = ctx.getParameter(UifExcelImportCmd.EXCEL_PATH);
			String appPath = ContextResourceUtil.getCurrentAppPath();
			String fullPath = appPath + "/" + relativePath;
			ExcelSheetConfig sconfig1=new ExcelSheetConfig();
			sconfig1.setBeginRow(5);
			sconfig1.setSheetName("产权交易中心省属企业项目统计表");
			
//			ExcelSheetConfig sconfig2=new ExcelSheetConfig();
//			sconfig2.setBeginRow(2);
//			sconfig2.setSheetName("专家库-参与项目");
			
			Map<String, String[]> maps1 = new LinkedHashMap<String, String[]>() {
				{
					put(ScapptExportTransHVO.PK_SGROUP_S,new String[]{"委托单位","STRING"});
					put(ScapptExportTransHVO.PK_ZGROUP_S,new String[]{"省属企业集团","STRING"});
//					put(ScapptExportTransHVO.PK_BORG_S,new String[]{"标的物","STRING"});
					put(ScapptExportTransHVO.DEF1,new String[]{"标的物","STRING"});
					put(ScapptExportTransHVO.VWPERIOD,new String[]{"地址","STRING"});
					put(ScapptExportTransHVO.ATRDATA,new String[]{"面积/数量","STRING"});
					put(ScapptExportTransHVO.VACCEPTNO,new String[]{"批准文件","STRING"});
					put(ScapptExportTransHVO.BILLNO,new String[]{"备案审批单位(含文号)","STRING"});
					put(ScapptExportTransHVO.DBPERCENT,new String[]{"业务性质","STRING"});
					put(ScapptExportTransHVO.VWTRANSTYPE_S,new String[]{"交易方式","STRING"});
					put(ScapptExportTransHVO.IBINPOST_D,new String[]{"公告次数","DOUBLE"});
					put(ScapptExportTransHVO.IBRETIRED_D,new String[]{"竞买人数","DOUBLE"});
					put(ScapptExportTransHVO.IF_SUS_S,new String[]{"是否交易成功","STRING"});
					put(ScapptExportTransHVO.VBDETAIL,new String[]{"评估价/原租价(万元)","DOUBLE"});
					put(ScapptExportTransHVO.DWPRICE,new String[]{"挂牌价/交易低价(万元)","DOUBLE"});
					put(ScapptExportTransHVO.DWTRANSPRICE,new String[]{"成交价(万元)","DOUBLE"});
					put(ScapptExportTransHVO.CWCTDATE_S,new String[]{"交易时间","STRING"});
					put(ScapptExportTransHVO.VSNAME,new String[]{"名称","STRING"});
					put(ScapptExportTransHVO.ADDRESS,new String[]{"住址","STRING"});
					put(ScapptExportTransHVO.DSCAPITAL,new String[]{"联系电话","STRING"});
					put(ScapptExportTransHVO.MEMO,new String[]{"备注","STRING"});
				}
			};
			
			
			int j=1;
			for (Map.Entry<String, String[]> entry : maps1.entrySet()) {
				String[] strs=entry.getValue();
				Property prop=new  Property(j,entry.getKey(),strs[0],Type.valueOf(strs[1].trim()));
				sconfig1.addProp(prop);
				j++;
			}
			
			
			List<ScapptExportTransHVO> listm1=new ArrayList<ScapptExportTransHVO>();
			try {
				ExcelToVoUtil<ScapptExportTransHVO> sutil1 = new ExcelToVoUtil<ScapptExportTransHVO>();
				ScapptExportTransHVO vo1 = new ScapptExportTransHVO();
				listm1 = sutil1.excelToVOList(sconfig1, fullPath, vo1);
				for (ScapptExportTransHVO amExpertHVO : listm1) {
					//交易时间
					String cwctdate_s = amExpertHVO.getCwctdate_s();
					if (null != cwctdate_s && !"".equals(cwctdate_s)) {
						amExpertHVO.setCwctdate(new UFDate(cwctdate_s));
					}
					
					//业务性质
					String dbpercent = amExpertHVO.getDbpercent();
					if (null != dbpercent && !"".equals(dbpercent)) {
						if("股权".equals(dbpercent)) dbpercent = "1";
						else if("实物资产".equals(dbpercent)) dbpercent = "2";
						else if("土地资产".equals(dbpercent)) dbpercent = "3";
						else if("金融".equals(dbpercent)) dbpercent = "4";
						else if("环境权益".equals(dbpercent)) dbpercent = "5";
						else if("公共资源".equals(dbpercent)) dbpercent = "6";
						else if("技术产权".equals(dbpercent)) dbpercent = "7";
						else if("融资服务".equals(dbpercent)) dbpercent = "8";
						else if("文化产权".equals(dbpercent)) dbpercent = "9";
						amExpertHVO.setDbpercent(dbpercent);
					}
					
					//交易方式
					String vwtranstype_s = amExpertHVO.getVwtranstype_s();
					if (null != vwtranstype_s && !"".equals(vwtranstype_s)) {
//						amExpertHVO.setCtradetype(getvwtranstypemap().get(vwtranstype_s));
						if("密封式报价".equals(vwtranstype_s)) vwtranstype_s = "1";
						else if("密封式加网络".equals(vwtranstype_s)) vwtranstype_s = "2";
						else if("动态报价".equals(vwtranstype_s)) vwtranstype_s = "3";
						else if("拍卖".equals(vwtranstype_s)) vwtranstype_s = "4";
						else if("协议".equals(vwtranstype_s)) vwtranstype_s = "5";
						else if("其他".equals(vwtranstype_s)) vwtranstype_s = "6";
						amExpertHVO.setVwtranstype(vwtranstype_s);
					}
					
					//委托单位
					String pk_sgroup_s = amExpertHVO.getPk_sgroup_s();
					if (null != pk_sgroup_s && !"".equals(pk_sgroup_s)) {
						amExpertHVO.setPk_sgroup(getfirmtreemap().get(pk_sgroup_s));
					}
					
//					//省属企业集团
//					String pk_zgroup_s = amExpertHVO.getPk_zgroup_s();
//					if (null != pk_zgroup_s && !"".equals(pk_zgroup_s)) {
//						amExpertHVO.setPk_zgroup(getfirmtreemap().get(pk_zgroup_s));
//					}
					
					//省属企业集团
					String pk_zgroup_s = amExpertHVO.getPk_zgroup_s();
					if (null != pk_zgroup_s && !"".equals(pk_zgroup_s)) {
						amExpertHVO.setPk_org(Getpkorg(pk_zgroup_s));
					}
					//标的物
//					String pk_borg_s = amExpertHVO.getPk_borg_s();
//					if (null != pk_borg_s && !"".equals(pk_borg_s)) {
//						amExpertHVO.setPk_borg(getfirmtreemap().get(pk_borg_s));
//					}
					
					//公告次数
					UFDouble ibinpost_d = amExpertHVO.getIbinpost_d();
					if (null != ibinpost_d && !"".equals(ibinpost_d)) {
						amExpertHVO.setIbinpost(ibinpost_d.intValue());
					}
					//竞买人数
					UFDouble ibretired_d = amExpertHVO.getIbretired_d();
					if (null != ibretired_d && !"".equals(ibretired_d)) {
						amExpertHVO.setIbretired(ibretired_d.intValue());
					}
					
					//是否交易成功
					String if_sus_s = amExpertHVO.getIf_sus_s();
					if (null != if_sus_s && !"".equals(if_sus_s)) {
						amExpertHVO.setIf_sus(new UFBoolean(if_sus_s.equals("Y") ? true : false));
					}
					//制单人、制单日期、组织、集团
//					amExpertHVO.setBillmaker(CntUtil.getCntUserPk());
//					amExpertHVO.setBillmakedate(new UFDateTime());
//					amExpertHVO.setPk_org(CntUtil.getCntOrgPk());
//					amExpertHVO.setPk_group(CntUtil.getCntGroupPk());
//					amExpertHVO.setFormstate("NottStart");
					amExpertHVO.setStatus(VOStatus.NEW);
					
					amExpertHVO.setPk_group(CntUtil.getCntGroupPk());
//					amExpertHVO.setPk_org(CntUtil.getCntOrgPk());
				}
				
//				if (null != listm1 && listm1.size() > 0) {
//				for (int m=0;m<listm1.size();m++) {
//					ScapptExportTransHVO hvo = listm1.get(m);
//					for (int n=m+1;n<listm1.size();n++) {
//						
//					}
//				}
//			}
//			
//			IUifCpbService cpbService = NCLocator.getInstance().lookup(IUifCpbService.class);
//			try {
//				SuperVO[] svoarray = ScapDAO.retrieveByConditionDR(ScapptExportTransHVO.class, "");
//				String rownum = "";
//				List<ScapptExportTransHVO> listm2 = new ArrayList<ScapptExportTransHVO>();
//				if (null != svoarray && svoarray.length > 0 && null != listm1 && listm1.size() > 0) {
//					ScapptExportTransHVO[] svos = (ScapptExportTransHVO[])svoarray;
//					int t = sconfig1.getBeginRow() + 1;
//					for (ScapptExportTransHVO tvo : listm1) {
//						boolean flag = true;
//						for (ScapptExportTransHVO svo : svos) {
//							if (null != tvo.getPk_sgroup() && null != svo.getPk_sgroup() && tvo.getPk_sgroup().equals(svo.getPk_sgroup())) {
//								tvo.setPk_main(svo.getPk_main());
//								rownum += t + ",";
//								flag = false;
//							}
//						}
//						if (flag) {
//							listm2.add(tvo);
//						}
//						t++;
//					}
//				}
//				if (!"".equals(rownum)) {
//					rownum = rownum.substring(0, rownum.length()-1);
//					if (AppInteractionUtil.show3ButtonsDialog("exportTrans","提示", "即将导入数据，其中行号为"+rownum+"的数据重复，是否执行更新？",new String[]{"是，更新数据","忽略更新，仅插入非重复数据","取消导入"}).equals(AppInteractionUtil.TbsDialogResult.FIRST)) {
//						ScapDAO.insertOrUpdateVOs(listm1.toArray(new ScapptExportTransHVO[listm1.size()]));
//						AppInteractionUtil.showMessageDialog("导入完成，新增"+listm2.size()+"条,更新"+(listm1.size()-listm2.size())+"条,请刷新页面");
//					} else if (AppInteractionUtil.show3ButtonsDialog("exportTrans","提示", "即将导入数据，其中行号为"+rownum+"的数据重复，是否执行更新？",new String[]{"是，更新数据","忽略更新，仅插入非重复数据","取消导入"}).equals(AppInteractionUtil.TbsDialogResult.SECOND)){
//						ScapDAO.insertVOArray(listm2.toArray(new ScapptExportTransHVO[listm2.size()]));
//						AppInteractionUtil.showMessageDialog("导入完成，新增"+listm2.size()+"条,请刷新页面");
//					} else {
//						AppInteractionUtil.showMessageDialog("导入已取消");
//					}
//				} else {
//					cpbService.insertSuperVOs(listm1.toArray(new ScapptExportTransHVO[listm1.size()]), false);
////					AppInteractionUtil.showMessageDialog("导入完成，新增"+nlist.size()+"条，其中"+(newlistm.size()-nlist.size())+"条已存在，请刷新页面");
//					AppInteractionUtil.showMessageDialog("导入完成，新增"+listm1.size()+"条,请刷新页面");
//				}
//			} catch (CpbBusinessException e1) {
//				throw new ExcelException(e1.getMessage());
//			}
			
			IUifCpbService cpbService = NCLocator.getInstance().lookup(IUifCpbService.class);
			try {
				cpbService.insertSuperVOs(listm1.toArray(new ScapptExportTransHVO[listm1.size()]), false);
//				AppInteractionUtil.showMessageDialog("导入完成，新增"+nlist.size()+"条，其中"+(newlistm.size()-nlist.size())+"条已存在，请刷新页面");
				AppInteractionUtil.showMessageDialog("导入完成，新增"+listm1.size()+"条,请刷新页面");
			} catch (CpbBusinessException e1) {
				throw new ExcelException(e1.getMessage());
			}
			
			} catch (ExcelException e1) {
				throw new ExcelException(e1.getMessage());
			}
		}
		
		//交易方式
		private Map<String, String> vwtranstypemap;
		private Map<String, String> getvwtranstypemap() {
			if (null == vwtranstypemap) {
				vwtranstypemap = new HashMap<String, String>();
				List<DefdocVO> defdocVOList = (List<DefdocVO>)ScapDAO.executeQuery("select * from bd_defdoc where pk_defdoclist = (select pk_defdoclist from bd_defdoclist where code = 'scappt_0003') and nvl(dr,0)=0", new BeanListProcessor(DefdocVO.class));
				for (DefdocVO defdocVO : defdocVOList) {
					vwtranstypemap.put(defdocVO.getName(), defdocVO.getPk_defdoc());
				}
			}
			return vwtranstypemap;
		}
		
		//职能
		private Map<String, Integer> ifunctionsmap;
		private Map<String, Integer> getifunctionsmap() {
			if (null == ifunctionsmap) {
				ifunctionsmap = new HashMap<String, Integer>();
				ifunctionsmap.put("招投标项目", 0);
				ifunctionsmap.put("重大资产评估项目", 1);
				ifunctionsmap.put("两者皆可", 2);
			}
			return ifunctionsmap;
		}
		
		
		//产权树
		private Map<String, String> firmtreemap;
		private Map<String, String> getfirmtreemap() {
			if (null == firmtreemap) {
				firmtreemap = new HashMap<String, String>();
				FirmtreeVO[] voArray = (FirmtreeVO[])ScapDAO.retrieveByCondition(FirmtreeVO.class, "nvl(dr,0)=0");
				for (FirmtreeVO firmtreeVO : voArray) {
					firmtreemap.put(firmtreeVO.getVname(), firmtreeVO.getPk_firmtree());
				}
			}
			return firmtreemap;
		}
		
		//cp_orgs
		private String Getpkorg(String vname) {
			String pk_org = "";
			pk_org = (String) ScapDAO.executeQuery("SELECT o.pk_org FROM cp_orgs o WHERE o.name = '"+vname+"'", new ColumnProcessor());
			return pk_org;
		}
}
