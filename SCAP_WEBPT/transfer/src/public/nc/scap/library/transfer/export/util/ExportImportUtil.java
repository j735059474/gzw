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
	// �ص�����
		public void onUploadedExcelFile(ScriptEvent e) {
			AppLifeCycleContext ctx = AppLifeCycleContext.current();
			String relativePath = ctx.getParameter(UifExcelImportCmd.EXCEL_PATH);
			String appPath = ContextResourceUtil.getCurrentAppPath();
			String fullPath = appPath + "/" + relativePath;
			ExcelSheetConfig sconfig1=new ExcelSheetConfig();
			sconfig1.setBeginRow(5);
			sconfig1.setSheetName("��Ȩ��������ʡ����ҵ��Ŀͳ�Ʊ�");
			
//			ExcelSheetConfig sconfig2=new ExcelSheetConfig();
//			sconfig2.setBeginRow(2);
//			sconfig2.setSheetName("ר�ҿ�-������Ŀ");
			
			Map<String, String[]> maps1 = new LinkedHashMap<String, String[]>() {
				{
					put(ScapptExportTransHVO.PK_SGROUP_S,new String[]{"ί�е�λ","STRING"});
					put(ScapptExportTransHVO.PK_ZGROUP_S,new String[]{"ʡ����ҵ����","STRING"});
//					put(ScapptExportTransHVO.PK_BORG_S,new String[]{"�����","STRING"});
					put(ScapptExportTransHVO.DEF1,new String[]{"�����","STRING"});
					put(ScapptExportTransHVO.VWPERIOD,new String[]{"��ַ","STRING"});
					put(ScapptExportTransHVO.ATRDATA,new String[]{"���/����","STRING"});
					put(ScapptExportTransHVO.VACCEPTNO,new String[]{"��׼�ļ�","STRING"});
					put(ScapptExportTransHVO.BILLNO,new String[]{"����������λ(���ĺ�)","STRING"});
					put(ScapptExportTransHVO.DBPERCENT,new String[]{"ҵ������","STRING"});
					put(ScapptExportTransHVO.VWTRANSTYPE_S,new String[]{"���׷�ʽ","STRING"});
					put(ScapptExportTransHVO.IBINPOST_D,new String[]{"�������","DOUBLE"});
					put(ScapptExportTransHVO.IBRETIRED_D,new String[]{"��������","DOUBLE"});
					put(ScapptExportTransHVO.IF_SUS_S,new String[]{"�Ƿ��׳ɹ�","STRING"});
					put(ScapptExportTransHVO.VBDETAIL,new String[]{"������/ԭ���(��Ԫ)","DOUBLE"});
					put(ScapptExportTransHVO.DWPRICE,new String[]{"���Ƽ�/���׵ͼ�(��Ԫ)","DOUBLE"});
					put(ScapptExportTransHVO.DWTRANSPRICE,new String[]{"�ɽ���(��Ԫ)","DOUBLE"});
					put(ScapptExportTransHVO.CWCTDATE_S,new String[]{"����ʱ��","STRING"});
					put(ScapptExportTransHVO.VSNAME,new String[]{"����","STRING"});
					put(ScapptExportTransHVO.ADDRESS,new String[]{"סַ","STRING"});
					put(ScapptExportTransHVO.DSCAPITAL,new String[]{"��ϵ�绰","STRING"});
					put(ScapptExportTransHVO.MEMO,new String[]{"��ע","STRING"});
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
					//����ʱ��
					String cwctdate_s = amExpertHVO.getCwctdate_s();
					if (null != cwctdate_s && !"".equals(cwctdate_s)) {
						amExpertHVO.setCwctdate(new UFDate(cwctdate_s));
					}
					
					//ҵ������
					String dbpercent = amExpertHVO.getDbpercent();
					if (null != dbpercent && !"".equals(dbpercent)) {
						if("��Ȩ".equals(dbpercent)) dbpercent = "1";
						else if("ʵ���ʲ�".equals(dbpercent)) dbpercent = "2";
						else if("�����ʲ�".equals(dbpercent)) dbpercent = "3";
						else if("����".equals(dbpercent)) dbpercent = "4";
						else if("����Ȩ��".equals(dbpercent)) dbpercent = "5";
						else if("������Դ".equals(dbpercent)) dbpercent = "6";
						else if("������Ȩ".equals(dbpercent)) dbpercent = "7";
						else if("���ʷ���".equals(dbpercent)) dbpercent = "8";
						else if("�Ļ���Ȩ".equals(dbpercent)) dbpercent = "9";
						amExpertHVO.setDbpercent(dbpercent);
					}
					
					//���׷�ʽ
					String vwtranstype_s = amExpertHVO.getVwtranstype_s();
					if (null != vwtranstype_s && !"".equals(vwtranstype_s)) {
//						amExpertHVO.setCtradetype(getvwtranstypemap().get(vwtranstype_s));
						if("�ܷ�ʽ����".equals(vwtranstype_s)) vwtranstype_s = "1";
						else if("�ܷ�ʽ������".equals(vwtranstype_s)) vwtranstype_s = "2";
						else if("��̬����".equals(vwtranstype_s)) vwtranstype_s = "3";
						else if("����".equals(vwtranstype_s)) vwtranstype_s = "4";
						else if("Э��".equals(vwtranstype_s)) vwtranstype_s = "5";
						else if("����".equals(vwtranstype_s)) vwtranstype_s = "6";
						amExpertHVO.setVwtranstype(vwtranstype_s);
					}
					
					//ί�е�λ
					String pk_sgroup_s = amExpertHVO.getPk_sgroup_s();
					if (null != pk_sgroup_s && !"".equals(pk_sgroup_s)) {
						amExpertHVO.setPk_sgroup(getfirmtreemap().get(pk_sgroup_s));
					}
					
//					//ʡ����ҵ����
//					String pk_zgroup_s = amExpertHVO.getPk_zgroup_s();
//					if (null != pk_zgroup_s && !"".equals(pk_zgroup_s)) {
//						amExpertHVO.setPk_zgroup(getfirmtreemap().get(pk_zgroup_s));
//					}
					
					//ʡ����ҵ����
					String pk_zgroup_s = amExpertHVO.getPk_zgroup_s();
					if (null != pk_zgroup_s && !"".equals(pk_zgroup_s)) {
						amExpertHVO.setPk_org(Getpkorg(pk_zgroup_s));
					}
					//�����
//					String pk_borg_s = amExpertHVO.getPk_borg_s();
//					if (null != pk_borg_s && !"".equals(pk_borg_s)) {
//						amExpertHVO.setPk_borg(getfirmtreemap().get(pk_borg_s));
//					}
					
					//�������
					UFDouble ibinpost_d = amExpertHVO.getIbinpost_d();
					if (null != ibinpost_d && !"".equals(ibinpost_d)) {
						amExpertHVO.setIbinpost(ibinpost_d.intValue());
					}
					//��������
					UFDouble ibretired_d = amExpertHVO.getIbretired_d();
					if (null != ibretired_d && !"".equals(ibretired_d)) {
						amExpertHVO.setIbretired(ibretired_d.intValue());
					}
					
					//�Ƿ��׳ɹ�
					String if_sus_s = amExpertHVO.getIf_sus_s();
					if (null != if_sus_s && !"".equals(if_sus_s)) {
						amExpertHVO.setIf_sus(new UFBoolean(if_sus_s.equals("Y") ? true : false));
					}
					//�Ƶ��ˡ��Ƶ����ڡ���֯������
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
//					if (AppInteractionUtil.show3ButtonsDialog("exportTrans","��ʾ", "�����������ݣ������к�Ϊ"+rownum+"�������ظ����Ƿ�ִ�и��£�",new String[]{"�ǣ���������","���Ը��£���������ظ�����","ȡ������"}).equals(AppInteractionUtil.TbsDialogResult.FIRST)) {
//						ScapDAO.insertOrUpdateVOs(listm1.toArray(new ScapptExportTransHVO[listm1.size()]));
//						AppInteractionUtil.showMessageDialog("������ɣ�����"+listm2.size()+"��,����"+(listm1.size()-listm2.size())+"��,��ˢ��ҳ��");
//					} else if (AppInteractionUtil.show3ButtonsDialog("exportTrans","��ʾ", "�����������ݣ������к�Ϊ"+rownum+"�������ظ����Ƿ�ִ�и��£�",new String[]{"�ǣ���������","���Ը��£���������ظ�����","ȡ������"}).equals(AppInteractionUtil.TbsDialogResult.SECOND)){
//						ScapDAO.insertVOArray(listm2.toArray(new ScapptExportTransHVO[listm2.size()]));
//						AppInteractionUtil.showMessageDialog("������ɣ�����"+listm2.size()+"��,��ˢ��ҳ��");
//					} else {
//						AppInteractionUtil.showMessageDialog("������ȡ��");
//					}
//				} else {
//					cpbService.insertSuperVOs(listm1.toArray(new ScapptExportTransHVO[listm1.size()]), false);
////					AppInteractionUtil.showMessageDialog("������ɣ�����"+nlist.size()+"��������"+(newlistm.size()-nlist.size())+"���Ѵ��ڣ���ˢ��ҳ��");
//					AppInteractionUtil.showMessageDialog("������ɣ�����"+listm1.size()+"��,��ˢ��ҳ��");
//				}
//			} catch (CpbBusinessException e1) {
//				throw new ExcelException(e1.getMessage());
//			}
			
			IUifCpbService cpbService = NCLocator.getInstance().lookup(IUifCpbService.class);
			try {
				cpbService.insertSuperVOs(listm1.toArray(new ScapptExportTransHVO[listm1.size()]), false);
//				AppInteractionUtil.showMessageDialog("������ɣ�����"+nlist.size()+"��������"+(newlistm.size()-nlist.size())+"���Ѵ��ڣ���ˢ��ҳ��");
				AppInteractionUtil.showMessageDialog("������ɣ�����"+listm1.size()+"��,��ˢ��ҳ��");
			} catch (CpbBusinessException e1) {
				throw new ExcelException(e1.getMessage());
			}
			
			} catch (ExcelException e1) {
				throw new ExcelException(e1.getMessage());
			}
		}
		
		//���׷�ʽ
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
		
		//ְ��
		private Map<String, Integer> ifunctionsmap;
		private Map<String, Integer> getifunctionsmap() {
			if (null == ifunctionsmap) {
				ifunctionsmap = new HashMap<String, Integer>();
				ifunctionsmap.put("��Ͷ����Ŀ", 0);
				ifunctionsmap.put("�ش��ʲ�������Ŀ", 1);
				ifunctionsmap.put("���߽Կ�", 2);
			}
			return ifunctionsmap;
		}
		
		
		//��Ȩ��
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
