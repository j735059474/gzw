package nc.scap.pub.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import uap.lfw.ref.util.LfwReferenceUtil;
import uap.web.bd.pub.AppUtil;

import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.scap.sysinit.constant.ISysinitConstant;
import nc.scap.sysinit.util.ScapSysinitUtil;
import nc.uap.lfw.core.AppInteractionUtil;
import nc.uap.lfw.core.WebContext;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.portal.log.ScapLogger;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.sm.UserVO;
import nc.vo.uap.rbac.util.RbacUserPwdUtil;

public class CpFdUtil {
	
	   public static  void setProptity(Class clazz,String field,Object setValue,SuperVO vo){
				try {
					Class<?> type = clazz.getDeclaredField(field).getType();
					String setMethodname=getMethodName("set", field);
					Method setMethod = clazz.getMethod(setMethodname, type);
					if(setValue instanceof BigDecimal){
						BigDecimal b=((BigDecimal)setValue);
						if(b!=null){
							if(type.equals(UFDouble.class)){
								setMethod.invoke(vo,new UFDouble(b.doubleValue()));
							}else if(type.equals(Integer.class)){
								setMethod.invoke(vo,b.intValue());
							}else if(type.equals(Integer.class)){
								setMethod.invoke(vo,b.doubleValue());
							}else if(type.equals(Integer.class)){
								setMethod.invoke(vo,String.valueOf(b));
							}
						}
					}else if(setValue instanceof String){
						if(type.equals(String.class)){
							setMethod.invoke(vo,setValue);
						}else if(type.equals(UFDouble.class)){
							setMethod.invoke(vo,StringUtils.isEmpty((String)setValue)   ? null : new UFDouble(setValue.toString()));
						}else if(type.equals(Integer.class)){
							setMethod.invoke(vo,StringUtils.isEmpty((String)setValue) ? null : Integer.parseInt(setValue.toString()));
						}else if(type.equals(UFDate.class)){
							setMethod.invoke(vo,StringUtils.isEmpty((String)setValue) ? null : new UFDate(setValue.toString()));
						}else if(type.equals(UFDateTime.class)){
							setMethod.invoke(vo,StringUtils.isEmpty((String)setValue)? null : new UFDateTime(setValue.toString()));
						}else if(type.equals(Object.class)){
							setMethod.invoke(vo,setValue);
						}
					}else if(setValue instanceof Integer){
							if(type.equals(UFDouble.class)){
								setValue=new UFDouble((Integer)setValue);
								setMethod.invoke(vo, setValue);
							}else{
								setMethod.invoke(vo, (Integer)setValue);
							}
					}else
					 setMethod.invoke(vo, setValue);
				} catch (SecurityException e) {
					ScapLogger.error("无法定位"+clazz+getMethodName("set", field)+e.getMessage());
				} catch (NoSuchMethodException e) {
					ScapLogger.error("未找到对应的方法"+clazz+getMethodName("set", field)+e.getMessage());
				} catch (IllegalArgumentException e) {
					ScapLogger.error("参数不正确"+clazz+getMethodName("set", field)+e.getMessage());
				} catch (IllegalAccessException e) {
					ScapLogger.error("安全权限异常"+clazz+getMethodName("set", field)+e.getMessage());
				} catch (InvocationTargetException e) {
					ScapLogger.error("装换异常"+clazz+getMethodName("set", field)+e.getMessage());
				} catch (NoSuchFieldException e) {
					ScapLogger.error("未找到对应的属性"+clazz+getMethodName("set", field)+e.getMessage());
				}
	}
	   
		/**
		 * 获取方法名 oper为“set”或者“get”或者其他前缀，fieldName为列名，自动首字母大写
		 * @author taoye 2013-7-24
		 */
		public static String getMethodName(String oper, String fieldName) {
			return oper + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		}	 
		/**
		 * 处理原始表数据
		 * @param l
		 * @param pkOrg
		 * @param sYear
		 * @param sMonth
		 * @param sType
		 * @return
		 */
		public static Map<String, List<Map>> getData(List l,String pkOrg,String sYear,String sMonth,String sType){
			Map<String, List<Map>> orgMap = new LinkedHashMap<String, List<Map>>();
			Map<String, List<Map>> orgMapL = new LinkedHashMap<String, List<Map>>();
			if(l==null){
				return orgMapL;
			}
			for (Iterator iterator = l.iterator(); iterator.hasNext();) {
				Map m = (Map) iterator.next();
//				String sys_zdm = (String) m.get(pkOrg);
//				String org = sys_zdm.substring(0, 9);
				String org =  (String) m.get(pkOrg);
				
//				String type =  (String) m.get(sType);
//				String vproject = ScapSysinitUtil.getSysinitStrByCode(ISysinitConstant.PROVINCE_ID);
//				if("XJ".equals(vproject)){
//					org=org+type;
//				}
				if (orgMap.get(org) == null) {
					List<Map> listMap = new ArrayList<Map>();
					listMap.add(m);
					orgMap.put(org, listMap);
				} else {
					List<Map> listMap = orgMap.get(org);
					listMap.add(m);
				}
			}
			for (Iterator iterator = orgMap.keySet().iterator(); iterator.hasNext();) {
				String org = (String) iterator.next();
				List<Map> listmap = orgMap.get(org);
				listmap = filterData(listmap,sYear,sMonth,sType);
				//pos 2014-8-11 因为新疆有公司合并，数据没有合并，所以需手动把数据合并在一起。
				l = MostOfOrgDataMerge(l, new String[]{"jq_level","sys_fjd","yl","zzxs","jygm"}, pkOrg, null, null);
				orgMapL.put(org, listmap);
			}
			return orgMapL;
		}
		/**
		 * 处理原始表数据
		 * @param l
		 * @param pkOrg
		 * @param sYear
		 * @param sMonth
		 * @param sType
		 * * @param flag 是否需要加类型拆分
		 * @return
		 */
		public static Map<String, List<Map>> getData(List l,String pkOrg,String sYear,String sMonth,String sType,boolean flag){
			if(!flag){
				return getData(l, pkOrg, sYear, sMonth, sType);
			}
			Map<String, List<Map>> orgMap = new LinkedHashMap<String, List<Map>>();
			Map<String, List<Map>> orgMapL = new LinkedHashMap<String, List<Map>>();
			for (Iterator iterator = l.iterator(); iterator.hasNext();) {
				Map m = (Map) iterator.next();
//				String sys_zdm = (String) m.get(pkOrg);
//				String org = sys_zdm.substring(0, 9);
				String org =  (String) m.get(pkOrg);
				
				String type =  (String) m.get(sType);
				boolean  vproject = ScapSysinitUtil.isShowCorp();
				if(vproject){
					org=org+type;
				}
				if (orgMap.get(org) == null) {
					List<Map> listMap = new ArrayList<Map>();
					listMap.add(m);
					orgMap.put(org, listMap);
				} else {
					List<Map> listMap = orgMap.get(org);
					listMap.add(m);
				}
			}
			for (Iterator iterator = orgMap.keySet().iterator(); iterator.hasNext();) {
				String org = (String) iterator.next();
				List<Map> listmap = orgMap.get(org);
				listmap = filterData(listmap,sYear,sMonth,sType);
				//pos 2014-8-11 因为新疆有公司合并，数据没有合并，所以需手动把数据合并在一起。
				l = MostOfOrgDataMerge(l, new String[]{"jq_level","sys_fjd","yl","zzxs","jygm"}, pkOrg, null, null);
				orgMapL.put(org, listmap);
			}
			return orgMapL;
		}
		/**
		 * 筛选同一公司同年同月的数据
		 * @param listmap
		 * @return
		 */
		private static List<Map> filterData(List<Map> listmap,String sYear,String sMonth,String sType) {
			List<Map> returnlistMap = new ArrayList<Map>();
			Map<Object, List<Map>> map = new LinkedHashMap<Object, List<Map>>();
			//根据年月进行分组
			for (int i = 0; i < listmap.size(); i++) {
				Map m = listmap.get(i);
				Object year = m.get(sYear);
				Object month = m.get(sMonth);
				String key = year.toString() + month.toString();
				if (map.get(key) == null) {
					List<Map> list = new ArrayList<Map>();
					list.add(m);
					map.put(key, list);
				} else {
					List<Map> list = map.get(key);
					list.add(m);
				}

			}

			// 根据数据类型筛选数据
			for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
				Object m = (Object) iterator.next();
				List<Map> list = map.get(m);
				List<Map> listMap9 = new ArrayList<Map>();
				List<Map> listMap0 = new ArrayList<Map>();
				List<Map> listMapZM = new ArrayList<Map>();
				List<Map> listMap7 = new ArrayList<Map>();
				List<Map> listMap2 = new ArrayList<Map>();
				List<Map> listMap4 = new ArrayList<Map>();
				List<Map> listMap5 = new ArrayList<Map>();
				String reg = "[a-zA-Z]";
				for (Map map2 : list) {
					Object type = map2.get(sType);
					if (type.toString().matches(reg)) {
						listMapZM.add(map2);
						}
					if ("7".equals(type)) {
						listMap7.add(map2);
					}
					if ("9".equals(type)) {
						listMap9.add(map2);
					}
					if ("0".equals(type)) {
						listMap0.add(map2);
					}
					if ("2".equals(type)) {
						listMap2.add(map2);
					}
					if ("4".equals(type)) {
						listMap4.add(map2);
					}
					if ("5".equals(type)) {
						listMap5.add(map2);
					}
				}
				if(listMapZM != null && listMapZM.size()>0){
					returnlistMap.addAll(listMapZM);
				}else if (listMap7 != null && listMap7.size() > 0){
					returnlistMap.addAll(listMap7);
					returnlistMap.addAll(listMap0);
				}else if (listMap9 != null && listMap9.size() > 0){
					returnlistMap.addAll(listMap9);
					boolean vproject = ScapSysinitUtil.isShowCorp();
					if(vproject){
						returnlistMap.addAll(listMap0);
					}
				}else {
						returnlistMap.addAll(listMap0);
						returnlistMap.addAll(listMap2);
						returnlistMap.addAll(listMap4);
						returnlistMap.addAll(listMap5);
					}
			}
			return returnlistMap;

		}
		/**
		 * 按参数拆分list
		 * @param l
		 * @param partPro
		 * @return
		 */
		public static Map partitionList(List l,String partPro){
			 LinkedHashMap partmap = new LinkedHashMap();
			 if(l==null){
				 return partmap;
			 }
		        for (Iterator iterator = l.iterator(); iterator.hasNext();) {
		        	Map map = (Map) iterator.next();
		            String partkey = (String) map.get(partPro);
		            if(partmap.get(partkey) == null){
		                partmap.put(partkey, new ArrayList());
		            }
		            List list = (List) partmap.get(partkey);
		            list.add(map);
		        }
		        return partmap;
		}	
		/**
		 * 按参数拆分list
		 * @param l
		 * @param partPro
		 * @return
		 */
		public static Map partitionList(List l,String partPro1,String partPro2){
			 LinkedHashMap partmap = new LinkedHashMap();
			 if(l==null){
				 return partmap;
			 }
		        for (Iterator iterator = l.iterator(); iterator.hasNext();) {
		        	Map map = (Map) iterator.next();
		            String partkey = (String) map.get(partPro1);
		            String partkey2 = (String) map.get(partPro2);
		            partkey+=partkey2;
		            if(partmap.get(partkey) == null){
		                partmap.put(partkey, new ArrayList());
		            }
		            List list = (List) partmap.get(partkey);
		            list.add(map);
		        }
		        return partmap;
		}	
		//若该公司有类型为9的先取9，没有取类型为0的数据
		/**
		 * 过滤
		 * @param map
		 * @return
		 */
		public static List filterMap(Map map){
			List returnList=new ArrayList();
			for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
				Object m = (Object) iterator.next();
				List<Map> list = (List<Map>) map.get(m);
				List<Map> listMap9 = new ArrayList<Map>();
				List<Map> listMap0 = new ArrayList<Map>();
				List<Map> listMapZM = new ArrayList<Map>();
				List<Map> listMap7 = new ArrayList<Map>();
				List<Map> listMap2 = new ArrayList<Map>();
				List<Map> listMap4 = new ArrayList<Map>();
				List<Map> listMap5 = new ArrayList<Map>();
				String reg = "[a-zA-Z]";
				for (Map map2 : list) {
					Object type = map2.get("n");
					if(StringUtils.isBlank((String)type)){
						continue;
					}
					if (type.toString().matches(reg)) {
						listMapZM.add(map2);
						}
					if ("7".equals(type)) {
						listMap7.add(map2);
					}
					if ("9".equals(type)) {
						listMap9.add(map2);
					}
					if ("0".equals(type)) {
						listMap0.add(map2);
					}
					if ("2".equals(type)) {
						listMap2.add(map2);
					}
					if ("4".equals(type)) {
						listMap4.add(map2);
					}
					if ("5".equals(type)) {
						listMap5.add(map2);
					}
				}
				if(listMapZM != null && listMapZM.size()>0){
					returnList.addAll(listMapZM);
				}else if (listMap7 != null && listMap7.size() > 0){
					returnList.addAll(listMap7);
					returnList.addAll(listMap0);
				}else if (listMap9 != null && listMap9.size() > 0){
						returnList.addAll(listMap9);
					boolean t = ScapSysinitUtil.isShowCorp();
					if(t){
						returnList.addAll(listMap0);
					}
				}
				else {
					returnList.addAll(listMap0);
					returnList.addAll(listMap2);
					returnList.addAll(listMap4);
					returnList.addAll(listMap5);
				}
			}
			
			return returnList;
		}
		/**
		 * 过滤单户
		 * @param map
		 * @return
		 */
		public static List getDHfilterMap(Map map){
			List returnList=new ArrayList();
			for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
				Object m = (Object) iterator.next();
				List<Map> list = (List<Map>) map.get(m);
				List<Map> listMap9 = new ArrayList<Map>();
				List<Map> listMap0 = new ArrayList<Map>();
				List<Map> listMapZM = new ArrayList<Map>();
				List<Map> listMap7 = new ArrayList<Map>();
				List<Map> listMap2 = new ArrayList<Map>();
				List<Map> listMap4 = new ArrayList<Map>();
				List<Map> listMap5 = new ArrayList<Map>();
				String reg = "[a-zA-Z]";
				for (Map map2 : list) {
					Object type = map2.get("n");
					if ("0".equals(type)) {
						listMap0.add(map2);
					}
				}
					returnList.addAll(listMap0);	
			}
			
			return returnList;
		}
		/**
		 * 根据开始和结束年份返回从开始到结束的年数组
		 * @param syear
		 * @param eyear
		 * @return
		 */
		public static String[] dealYears(String syear,String eyear) {
			int year_sta = Integer.parseInt(syear); // 开始年
			int year_end = Integer.parseInt(eyear); // 结束年
			List<String> l = new ArrayList<String>();
			while (year_sta <= year_end) {
				l.add(Integer.toString(year_sta));
				year_sta++;
			}
			String[] years = new String[l.size()];
			return l.toArray(years);
		}
		
		/**
		 * 
		 * @param vproject 所属项目（如安徽，海南）
		 * @param type   （快报，决算，国资，预算）
		 * @param year   年
		 * @return
		 */
		public static Map  getVirtualOrgMap(String vproject,String type,String vyear){
			vproject = ScapSysinitUtil.getSysinitStrByCode(ISysinitConstant.PROVINCE_ID);
			Map returnMap=new HashMap();
			String sql="select vcode||'_'||tcode ss from  scapfd_bd_virtual_b where pk_virtual_h in (select pk_virtual_h from scapfd_bd_virtual_h  aa where aa.vproject='"+vproject+"' and " +
					" aa.type='"+type+"' and aa.vyear='"+vyear+"' )";
			List l=(List)ScapDAO.executeQuery(sql, new MapListProcessor());
			if(l!=null&&l.size()>0){
				for (Iterator iterator = l.iterator(); iterator.hasNext();) {
					Map m = (Map) iterator.next();
					for (Iterator iterator2 =m.keySet().iterator(); iterator2.hasNext();) {
						String key = (String) iterator2.next();
						 String str=(String)m.get(key);
						 if(str!=null){
							 String[] values=str.split("_");
							 returnMap.put(values[0], values[1]);
						 }
					}
				}
			}
		   return 	returnMap;
		}
		/**
		 * 快报年份对应统一编码
		 * @param vproject
		 * @param type
		 * @param vyear
		 * @return
		 */
		public static Map  getKBVirtualOrgMap(String vproject,String type,String vyear){
			vproject = ScapSysinitUtil.getSysinitStrByCode(ISysinitConstant.PROVINCE_ID);
			Map<String,Map> returnMap=new HashMap<String,Map>();
			Map yearMap=new HashMap();
			
			String sql="select vcode || '_' || tcode || '_' ||vyear ss, aa.vdef1 from scapfd_bd_virtual_b b left join scapfd_bd_virtual_h aa "
   +" on aa.pk_virtual_h = b.pk_virtual_h where aa.vproject='"+vproject+"' and " +" aa.type='"+type+"' ";
			if(vyear!=null){
				sql+=" and aa.vyear='"+vyear+"' ";
			}
			sql+=" and ( aa.vdef1='~' or aa.vdef1 is null )";
			List l=(List)ScapDAO.executeQuery(sql, new MapListProcessor());
			if(l!=null&&l.size()>0){
				for (Iterator iterator = l.iterator(); iterator.hasNext();) {
					Map m = (Map) iterator.next();
					for (Iterator iterator2 =m.keySet().iterator(); iterator2.hasNext();) {
						String key = (String) iterator2.next();
						 String str=(String)m.get(key);
						 if(str!=null){
							 String[] values=str.split("_");
							 yearMap.put(values[0]+"_"+values[2], values[1]);
						 }
					}
				}
			}
			returnMap.put("year", yearMap);
			returnMap=getKBMonthVirtualOrgMap(vproject, type, vyear, returnMap);
		   return 	returnMap;
		}
		/**
		 * 快报月份对应统一编码
		 * @param vproject
		 * @param type
		 * @param vyear
		 * @param returnMap
		 * @return
		 */
		public static Map  getKBMonthVirtualOrgMap(String vproject,String type,String vyear ,Map returnMap){
			vproject = ScapSysinitUtil.getSysinitStrByCode(ISysinitConstant.PROVINCE_ID);
			Map monthMap=new HashMap();
			String sql="select vcode || '_' || tcode || '_' ||vyear|| '_' || aa.vdef1 ss from scapfd_bd_virtual_b b left join scapfd_bd_virtual_h aa "
					   +" on aa.pk_virtual_h = b.pk_virtual_h where aa.vproject='"+vproject+"' and " +" aa.type='"+type+"' ";
			if(vyear!=null){
			sql+=" and aa.vyear='"+vyear+"' ";
			}
			sql+=" and  aa.vdef1<>'~' and aa.vdef1 is not null ";
			List l=(List)ScapDAO.executeQuery(sql, new MapListProcessor());
			if(l!=null&&l.size()>0){
				for (Iterator iterator = l.iterator(); iterator.hasNext();) {
					Map m = (Map) iterator.next();
					for (Iterator iterator2 =m.keySet().iterator(); iterator2.hasNext();) {
						String key = (String) iterator2.next();
						 String str=(String)m.get(key);
						 if(str!=null){
							 String[] values=str.split("_");
							 monthMap.put(values[0]+"_"+values[2]+values[3], values[1]);
						 }
					}
				}
			}
			returnMap.put("month", monthMap);
		   return 	returnMap;
		}
		

		
		/**
		 * 根据年份和月份获取相应时期快报封面数据中省直属企业的虚拟组织机构码，系统中省直属企业节点组织机构码为：HZQYB0025
		 */
		public static String getKBVirtualSzsOrg(String vyear, String vmonth) {
			String vproject = ScapSysinitUtil.getSysinitStrByCode(ISysinitConstant.PROVINCE_ID);
			String szsCode = "HZQYB0025";
			StringBuffer sb = new StringBuffer();
			sb.append("select b.vcode from scapfd_bd_virtual_b b left join scapfd_bd_virtual_h h on b.pk_virtual_h = h.pk_virtual_h " );
			sb.append("where h.vproject = '"+vproject+"' and h.type = '1' and h.vyear = '" + vyear + "' and h.vdef1 = '" + (vmonth.length()==1?"0"+vmonth:vmonth) + "' ");
			sb.append("and b.tcode = 'HZQYB0025' and b.dr = '0' and h.dr = '0' ");
			sb.append("union all ");
			sb.append("select b.vcode from scapfd_bd_virtual_b b left join scapfd_bd_virtual_h h on b.pk_virtual_h = h.pk_virtual_h ");
			sb.append("where h.vproject = '"+vproject+"' and h.type = '1' and h.vyear = '" + vyear + "' and (h.vdef1 = '~' or h.vdef1 is null) ");
			sb.append("and b.tcode = 'HZQYB0025' and b.dr = '0' and h.dr = '0' ");
			List<Map<String, Object>> result = (List<Map<String, Object>>) ScapDAO.executeQuery(sb.toString(), new MapListProcessor());
			if(result != null && result.size() > 0 && result.get(0).get("vcode") != null) {
				szsCode = result.get(0).get("vcode").toString();
			}
			return szsCode + vyear + (vmonth.length()==1?"0"+vmonth:vmonth) + "7";
		}
		
		/**
		 *   获取当前组织的下级企业的企业代码
		 * 
		 * @param sys_org  父组织企业代码
		 * @param year
		 * @param list  返回符合条件的企业代码
		 * @return
		 */
		public static List<String> getTotalOrgs(String sys_org,String year,List<String> list){
			List l=(List)ScapDAO.executeQuery("select * from scapfd_zc_base a  where a.vyear = '"+year+"' and a.sys_fjd in " +
					"(select sys_zdm from  scapfd_zc_base  where  vyear='"+year+"' and sys_org='"+sys_org+"') ", new MapListProcessor());
			for (Iterator iterator = l.iterator(); iterator.hasNext();) {
				Map m = (Map) iterator.next();
				String sys=(String)m.get("sys_zdm");
				if(sys != null  && (sys.endsWith("9")||sys.endsWith("0"))){
					list.add(m.get("sys_org").toString());
				}else
				getTotalOrgs((String)m.get("sys_org"),year,list);
			}
			return list;
		}
		
		/**
		 *   获取当前组织的下级企业的企业代码
		 * 
		 * @param sys_org  父组织企业代码
		 * @param year
		 * @param list  返回符合条件的企业代码
		 * @return
		 */
		public static String getTotalOrgs(String sys_zdm,String year){
			String provinceId= ScapSysinitUtil.getSysinitStrByCode(ISysinitConstant.PROVINCE_ID);
			Map<String,String> m= getTableNameMap();
			String table=m.get(year+"");
			if(table==null){
				AppInteractionUtil.showShortMessage("该年没有数据，请重新选择");
				return null;
			}
			boolean istableexisted = false;
			try {
				istableexisted = ScapDAO.getBaseDAO().isTableExisted(table);
				if(!istableexisted){
					AppInteractionUtil.showShortMessage("该年没有数据，请重新选择");
					return null;
				}
			} catch (DAOException e1) {
				ScapLogger.debug(e1.getMessage());
			}
			String sql="select nvl(tcode,qydm) sys_org from  (select distinct qydm from "+table+"  start with sys_zdm='"+sys_zdm+"' connect by sys_fjd=prior sys_zdm) aa "
+" left join  (select b.* from scapfd_bd_virtual_h h left join scapfd_bd_virtual_b b   on h.pk_virtual_h = b.pk_virtual_h where"
+" h.vproject='"+provinceId+"' and h.vyear='"+year+"'  and h.type = '4') bb on aa.qydm=bb.vcode";
			return sql;
		}
		private static Map<String,String>  getTableNameMap(){
	    	Map<String,String> m=new HashMap<String,String>();
	    	m.put("2011", "scapfd_zc_qyfmdm_11");
	    	m.put("2012", "scapfd_zc_qyfmdm_12");
	    	m.put("2013", "scapfd_zc_qyfmdm_13");
	    	m.put("2014", "scapfd_zc_qyfmdm_14");
	    	m.put("2015", "scapfd_zc_qyfmdm_15");
	    	m.put("2016", "scapfd_zc_qyfmdm_16");
	    	return m;
	    }
		/**
		 *  将list转化成'','','',格式
		 * @param l  企业代码 list
		 * @return
		 */
		public static String dealOrgs(List<String> l) {
			StringBuffer sb = new StringBuffer();
			int inNum = 1; //已拼装IN条件数量
			for(int i=0; i<l.size(); i++) {
			    if(l.get(i) == null || "".equals(l.get(i))) continue;
			    if(i == (l.size()-1))
			        sb.append("'" + l.get(i) + "'");    //SQL拼装，最后一条不加“,”。
			    else if(inNum==1000 && i>0) {
			        sb.append("'" + l.get(i) + "' ) OR ba.sys_org in (");    //解决ORA-01795问题
			        inNum = 1;
			    } else {
			        sb.append("'" + l.get(i) + "', ");
			        inNum++;
			    }
			}
			return sb.toString();
		}	
		
		/**
		 * 将编码为user_code的用户的密码重置为123qwe
		 * @param user_code
		 */
		public static void updatePadTo123qwe(String user_code){
			UserVO user = (UserVO) ScapDAO.executeQuery("select * from sm_user where user_code = '"+ user_code +"'", new BeanProcessor(UserVO.class));
			try{
				String initpwd = RbacUserPwdUtil.getEncodedPassword(user, "123qwe");//加密
				String upsql = "update cp_user set user_password ='"+ initpwd +"', pwdparam = '2099-12-31' where " +
						" user_code = '"+ user_code +"' ";
				ScapDAO.executeUpdate(upsql);
			}catch(BusinessException ex){
				ex.printStackTrace();
			}
		}
		
		/**
		 * 为新疆公司合并数据所用
		 * @param l
		 * @param province
		 * @return list
		 * l 要转换的list；str 不用合并，相加项；pkOrgfileName 公司主键字段名；province 省份字段
		 * 将pkOrg2的数据合并到pkOrg1公司中，合并后，pkOrg1存在，pkOrg2消失。
		 */
		public static List MostOfOrgDataMerge(List l,String[] str,String pkOrgfileName,String pkOrg1,String pkOrg2){
			if(l!=null){
				return l;
			}
			String provinceId= ScapSysinitUtil.getSysinitStrByCode(ISysinitConstant.PROVINCE_ID);
			if(provinceId != null && provinceId.equals(ISysinitConstant.XJ)){
				if(l != null){
					Map<Object, Object> map1 = null;
					Map<Object, Object> map2 = null;
					for(int i = l.size();i >0;i--){
						String val = (String) ((Map)l.get(i)).get(pkOrgfileName);
						if(val != null && val.equals(pkOrg1)){
							map1 = (Map<Object, Object>) l.get(i);
							l.remove(l.get(i));
						}else if(val != null && val.equals(pkOrg2)){
							map2 = (Map<Object, Object>) l.get(i);
							l.remove(l.get(i));
						}
						if(map1.size() > 0 && map2.size() > 0){
							//pos 两个公司都找出来。就跳出循环
							for (Iterator iterator = map1.keySet().iterator(); iterator.hasNext();) {
								String key = (String) iterator.next();
								List<String> templist = Arrays.asList(str);
								if(templist.contains(key)){
									continue;
								}else if("sys_org".equals(key) || "pk_org".equals(key) || "sys_zdm".equals(key)
										||"vyear".equals(key) || "vmonth".equals(key) || "ts".equals(key)
										||"pk_id".equals(key)||"n".equals(key)){
									continue;
								}else{
									BigDecimal aa=getBigDecimalByObject(map1, key);
									BigDecimal bb=getBigDecimalByObject(map2, key);
									aa=aa.add(bb);
									map1.put(key, aa);
								}
							}
							break;
						}
					}
					if(map1!=null){
					  l.add(map1);
					}
				}
			}
			return l;
		}
		public static BigDecimal getBigDecimalByObject(Map m,String key){
			Object o=m.get(key);
			if(o==null){
				return new BigDecimal(0);
			}else{
				return getBigDecimalByObject(o);
			}
		}
		public static BigDecimal getBigDecimalByObject(Object o){
			BigDecimal s1=new BigDecimal(0);
			if(o instanceof BigDecimal){
				 s1=(BigDecimal) o;
			}else if(o instanceof Integer){
				Integer ss=(Integer)o;
				s1=new BigDecimal(ss.intValue());
			}
			return s1;
		}
		/**
		 * 快报、预算、决算、国有资产等报表左侧查询模板组织体系参照关联“组织体系查询”
		 * @return
		 */
		public static String joinQryOrgsSwh(String type_name,String vyear,String vmonth){
			ScapDAO dao = new ScapDAO();
			String sql1 = "select reportorgs,rownum from scap_qryorgs a inner join bd_defdoc b on a.pk_type = b.pk_defdoc " +
					" where b.name = '"+ type_name +"' and a.vyear = '"+ vyear +"' and a.vmonth = '"+ vmonth +"' " +
							"and nvl(a.dr,0) = 0  and rownum = 1";
			String sql2 = "select reportorgs,rownum from scap_qryorgs a inner join bd_defdoc b on a.pk_type = b.pk_defdoc  " +
					" where b.name = '"+ type_name +"' and a.vyear = '"+ vyear +"' and (a.vmonth = '~' or a.vmonth is null)  " +
							" and nvl(a.dr,0) = 0  and rownum = 1";
			Object  reportorgs1[] =  (Object[]) dao.executeQuery(sql1, new ArrayProcessor());
			Object  reportorgs2[] =  (Object[]) dao.executeQuery(sql2, new ArrayProcessor());
			String reportorgs_real = "";
			if(reportorgs1 == null && reportorgs2 == null){
				reportorgs_real = CntUtil.getCntOrgPk(); //两个都没有配置则取当前用户所在的公司
				return " and pk_org in ('"+ reportorgs_real +"' )";
			}else {
				//有月份的取月份数据，没有的取当年数据
				if(reportorgs1 != null && reportorgs1.length > 0){
					reportorgs_real = (String)reportorgs1[0];
				}else {
					reportorgs_real = (String)reportorgs2[0];
				}
			}
			
			return " and pk_org in ( select t.pk_org from org_rmsmember t where pk_rms in ('"+ reportorgs_real +"') )";
		}
		
		/**
		 * 快报、预算、决算、国有资产等报表左侧查询模板组织体系参照关联“组织体系查询”
		 * @return
		 */
		public static String joinQryOrgsSwh1(String type_name,String vyear,String vmonth){
			String  reportorgs_real =getQryOrgsSwh(type_name, vyear, vmonth);
			if(reportorgs_real==null){
				 return " and pk_org in ('"+ CntUtil.getCntOrgPk() +"' )";//两个都没有配置则取当前用户所在的公司
			}
			return " and pk_org in ( select t.pk_org from org_rmsmember t where pk_rms in ('"+ reportorgs_real +"') )";
		}
		public static String joinQryOrgsSwh1(String reportorgs_real){
			
			if(reportorgs_real==null){
				 return " and pk_org in ('"+ CntUtil.getCntOrgPk() +"' )";//两个都没有配置则取当前用户所在的公司
			}
			return " and cp_orgs.pk_org in ( select t.pk_org from org_rmsmember t where pk_rms in ('"+ reportorgs_real +"') )";
		}
		/**
		  * 查询对应年月报表组织体系，若没有则找上一年。一直找到未找到为止
		  * @param type_name
		  * @param vyear
		  * @param vmonth
		  * @return
		  */
		 public static String getQryOrgsSwh(String type_name,String vyear,String vmonth){
			 if(StringUtils.isEmpty(vyear)){
				 if("快报".equals(type_name)){
				  vyear=new UFDateTime().getYear()+""; 
				 }else{
					 vyear=(new UFDateTime().getYear()-1)+"";  
				 }
			 }
			 ScapDAO dao = new ScapDAO();
				String sql1 = "select reportorgs,rownum from scap_qryorgs a inner join bd_defdoc b on a.pk_type = b.pk_defdoc " +
						" where b.name = '"+ type_name +"' and a.vyear = '"+ vyear +"' and a.vmonth = '"+ vmonth +"' " +
								"and nvl(a.dr,0) = 0  and rownum = 1";
				String sql2 = "select reportorgs,rownum from scap_qryorgs a inner join bd_defdoc b on a.pk_type = b.pk_defdoc  " +
						" where b.name = '"+ type_name +"' and a.vyear = '"+ vyear +"' and (a.vmonth = '~' or a.vmonth is null)  " +
								" and nvl(a.dr,0) = 0  and rownum = 1";
				Object  reportorgs1[] =  (Object[]) dao.executeQuery(sql1, new ArrayProcessor());
				Object  reportorgs2[] =  (Object[]) dao.executeQuery(sql2, new ArrayProcessor());
				String reportorgs_real = "";
				if(reportorgs1 == null && reportorgs2 == null){
					//reportorgs_real = CntUtil.getCntOrgPk(); //两个都没有配置则取当前用户所在的公司
					boolean tag=true;
					if(tag){
						int year=Integer.parseInt(vyear);
						vyear=(year-1)+"";
						 sql2 = "select reportorgs,rownum from scap_qryorgs a inner join bd_defdoc b on a.pk_type = b.pk_defdoc  " +
								" where b.name = '"+ type_name +"' and a.vyear = '"+ vyear +"' and (a.vmonth = '~' or a.vmonth is null)  " +
										" and nvl(a.dr,0) = 0  and rownum = 1";
						 reportorgs2 =  (Object[]) dao.executeQuery(sql2, new ArrayProcessor());
						 if(reportorgs2!=null){
							 reportorgs_real = (String)reportorgs2[0];
							 tag=false;
							 return reportorgs_real;
						 }
					}
					
					return null;
				}else {
					//有月份的取月份数据，没有的取当年数据
					if(reportorgs1 != null && reportorgs1.length > 0){
						reportorgs_real = (String)reportorgs1[0];
					}else {
						reportorgs_real = (String)reportorgs2[0];
					}
				}
				return reportorgs_real;
		 }
		 /**
		  * 只查询对应年月报表组织体系
		  * @param type_name
		  * @param vyear
		  * @param vmonth
		  * @return
		  */
		 public static String getQryOrgsSwh2(String type_name,String vyear,String vmonth){
			
			 ScapDAO dao = new ScapDAO();
				String sql1 = "select reportorgs,rownum from scap_qryorgs a inner join bd_defdoc b on a.pk_type = b.pk_defdoc " +
						" where b.name = '"+ type_name +"' and a.vyear = '"+ vyear +"' and a.vmonth = '"+ vmonth +"' " +
								"and nvl(a.dr,0) = 0  and rownum = 1";
				String sql2 = "select reportorgs,rownum from scap_qryorgs a inner join bd_defdoc b on a.pk_type = b.pk_defdoc  " +
						" where b.name = '"+ type_name +"' and a.vyear = '"+ vyear +"' and (a.vmonth = '~' or a.vmonth is null)  " +
								" and nvl(a.dr,0) = 0  and rownum = 1";
				Object  reportorgs1[] =  (Object[]) dao.executeQuery(sql1, new ArrayProcessor());
				Object  reportorgs2[] =  (Object[]) dao.executeQuery(sql2, new ArrayProcessor());
				String reportorgs_real = "";
				if(reportorgs1 == null && reportorgs2 == null){
					return null;
				}else {
					//有月份的取月份数据，没有的取当年数据
					if(reportorgs1 != null && reportorgs1.length > 0){
						reportorgs_real = (String)reportorgs1[0];
					}else {
						reportorgs_real = (String)reportorgs2[0];
					}
				}
				return reportorgs_real;
		 }
		 
		public static String getReportSql(String type_name,String vyear,String vmonth){
			boolean isFilterOrg = ScapSysinitUtil.isFilterOrg();
			String sql = "";
			int this_year = new UFDateTime().getYear();//当前年份
			//zll 2015-1-21 预警左侧组织树 多版本查询
			String search_year = (String)AppUtil.getAppAttr("yjorg_year");
			if(StringUtils.isNotBlank(search_year)){
				this_year = Integer.parseInt(search_year);
			}
			
			String vproject = ScapSysinitUtil.getSysinitStrByCode(ISysinitConstant.PROVINCE_ID);
			String str="";
			if(ScapSysinitUtil.isShowCorp()){
				str=" and substr(cp_orgs.code,-2,length(cp_orgs.code))<>'99' ";
			}
			String report_type=type_name+"_"+vyear;
			if(StringUtils.isNotBlank(vmonth)){
				report_type+="_"+vmonth;
			}else{
				report_type+="_";
			}
			AppUtil.addAppAttr("report_type",report_type);
			if(isFilterOrg){
				String wheresql = CpOrgUtil.getFilterOrgWhereSql();
				if(StringUtils.isNotBlank(type_name)){
					String pk_rms=getQryOrgsSwh(type_name, vyear, vmonth);
					String qryorgs_swh = joinQryOrgsSwh1(pk_rms);
					
					if(StringUtils.isEmpty(pk_rms)){
						if(StringUtils.isEmpty(wheresql)){
							wheresql=" where 1=1  "+str + qryorgs_swh;
						}else{
							wheresql=" where "+wheresql + qryorgs_swh+str;
						}
						sql = " SELECT pk_org,code,name,name2,name3,name4,name5,name6,def11,cp_orgs.innercode,(case when pk_fatherorg = '~' then pk_orglevel1 else pk_fatherorg END) AS pk_fatherorg " +
								"FROM cp_orgs " + wheresql ;
					}else{
						if(StringUtils.isEmpty(wheresql)){
							wheresql=" where 1=1 " +str+ qryorgs_swh;
						}else{
							wheresql=" where "+wheresql.replace("pk_org", "cp_orgs.pk_org") + qryorgs_swh+str;
						}
						sql = " SELECT cp_orgs.pk_org,code,name,name2,name3,name4,name5,name6,def11,cp_orgs.innercode, o.pk_fatherorg  " +
								"FROM cp_orgs inner join org_rmsmember o on o.pk_org=cp_orgs.pk_org and o.pk_rms='"+pk_rms+"'" + wheresql ;
					}
					
						
				}else{
					if(StringUtils.isEmpty(wheresql)){
						wheresql=" where 1=1 ";
					}else{
						wheresql=" where "+wheresql ;
					}
					sql = " select * from (select * from   cp_orgs  where cp_orgs.pk_org in  (select a.pk_org from org_orgs_v  a where a.vname='"+this_year+"' ) and cp_orgs.def13 is not null ) cp_orgs "+wheresql;
				}
				
					
			}
			else{
				if(StringUtils.isNotBlank(type_name)){
					String pk_rms=getQryOrgsSwh(type_name, vyear, vmonth);
					String qryorgs_swh = joinQryOrgsSwh1(pk_rms);
					if(StringUtils.isEmpty(pk_rms)){
				sql = "SELECT cp_orgs.pk_org,code,name,name2,name3,name4,name5,name6,def11,cp_orgs.innercode,(case when cp_orgs.pk_fatherorg = '~' then pk_orglevel1 else pk_fatherorg END) AS pk_fatherorg " +
						"FROM cp_orgs WHERE  enablestate = '2'  "+str+ qryorgs_swh;
					}else{
						sql = " SELECT cp_orgs.pk_org,code,name,name2,name3,name4,name5,name6,def11,cp_orgs.innercode, o.pk_fatherorg " +
								"FROM cp_orgs inner join org_rmsmember o on o.pk_org=cp_orgs.pk_org and o.pk_rms='"+pk_rms+"' WHERE  enablestate = '2' "+str + qryorgs_swh ;
					}
				}else{
					sql = " select cp_orgs.* from   cp_orgs where cp_orgs.pk_org in  (select a.pk_org from org_orgs_v  a where a.vname='"+this_year+"' ) and cp_orgs.def13 is not null ";
				}
			}
			
			return  sql;
		}
		
		/**
		 * 快报、预算、决算、国有资产等报表左侧查询模板得到查询条件中的年份和月份
		 * @param type 报表类型，只有快报才需要得到月份
		 * @return
		 */
		public static String[] getQryYearMonth(String type){
			String year_mon[] = new String[2];
			String pWid = LfwReferenceUtil.getWebContextParamValue(WebContext.WIDGET_ID);
			if(pWid==null){
				return null;
			}
			Dataset fromds = AppLifeCycleContext.current().getWindowContext().getParentWindowContext().getViewContext(pWid).getView().getViewModels().getDataset("mainds");
			int vyear = 0;
			Object omon = null;
			String vmonth = null;

			Field[] fields = fromds.getFieldSet().getFields();
			for(int i=0;i<fields.length;i++){
				if(fields[i].getId().contains("year")){
					vyear = (Integer) fromds.getValue(fields[i].getId());
				}
				if("KB".equals(type) && fields[i].getId().contains("month")){
					omon = fromds.getValue(fields[i].getId());
				}
			}
			if(StringUtils.isNotBlank((String)omon)&&omon.toString().length()==7){
				vyear=Integer.parseInt(omon.toString().substring(0, 4));
			}
			vyear = vyear == 0 ? (new UFDate().getYear()) : vyear ;//默认取当前年
			
			year_mon[0] = vyear + "";
			if(!"KB".equals(type)){
				year_mon[1] = null;
			}else {
				int mon = 0;
				if(omon == null){
					int this_month = new UFDate().getMonth();//当前月份
					mon =  this_month == 1 ? 12 : this_month - 1; //默认取当前月份的上一个月
				}else {
					String vmonth_class = omon.getClass().toString();
					if("Integer".equals(vmonth_class)){
						mon = (Integer) omon;
					}else {
						if(StringUtils.isNotBlank((String)omon)&&omon.toString().length()==7){
							vmonth=omon.toString().substring(5, 7);
						}
						//vmonth = (String)omon;
					}
				}
				if(vmonth != null){
					year_mon[1] = vmonth ;
				}else {
					if(mon <= 9){
						year_mon[1] = "0" + mon;
					}else {
						year_mon[1] = "" + mon;
					}
				}
			}
			
			return year_mon;
		}
		
		
		public static SuperVO merge(SuperVO vo, SuperVO vo1) {
			String[] fieldNames=vo.getAttributeNames();
			for(String fieldName:fieldNames){
				Object obj=vo1.getAttributeValue(fieldName);
				if(obj!=null){
					vo.setAttributeValue(fieldName, obj);
				}
			}
			return vo;
		}
		
		 /**
	     * 获取文件大小
	     * @param filesize
	     * @return
	     */
	  	public static String convertFileSize(long filesize) {
	  		String strUnit = "Bytes";
	  		String strAfterComma = "";
	  		int intDivisor = 1;
	  		if (filesize >= 1024 * 1024)
	  		{
	  			strUnit = "MB";
	  			intDivisor = 1024 * 1024;
	  		}
	  		else if (filesize >= 1024)
	  		{
	  			strUnit = "KB";
	  			intDivisor = 1024;
	  		}
	  		if (intDivisor == 1)
	  			return filesize + " " + strUnit;
	  		strAfterComma = "" + 100 * (filesize % intDivisor) / intDivisor;
	  		if (strAfterComma == "")
	  			strAfterComma = ".0";
	  		return filesize / intDivisor + "." + strAfterComma + " " + strUnit;
	  	}
	  	
	  	 //两段数据合并
		/**
		 * 
		 * @param xmlist 项目集合
		 * @param tjlist 企业统计集合
		 */
		public static List<Map> mergeList(List<Map>  xmlist,List<Map> tjlist){
			if(xmlist==null){
				return tjlist;
			}
			if(tjlist==null){
				return xmlist;
			}
			int xmcount=0;int tjcount=0;
			if(xmlist!=null&&tjlist!=null){
				xmcount=xmlist.size();
				tjcount=tjlist.size();
				if(xmcount>tjcount){
					for (Map map : xmlist) {
						if(map==null){
							continue;
						}
						String sys_zdm=(String) map.get("sys_zdm");
						for (Map map1 : tjlist) {
							if(map1==null){
								continue;
							}
							String sys_zdm1=(String) map1.get("sys_zdm");
							if(StringUtils.isNotBlank(sys_zdm)&&StringUtils.isNotBlank(sys_zdm1)&&sys_zdm.equals(sys_zdm1)){
								for (Iterator iterator = map1.keySet().iterator(); iterator.hasNext();) {
									String  key = (String ) iterator.next();
									map.put(key, map1.get(key));
								}
							}
						}
					}
				}else{
					for (Map map : tjlist) {
						if(map==null){
							continue;
						}
						String sys_zdm=(String) map.get("sys_zdm");
						for (Map map1 : xmlist) {
							if(map1==null){
								continue;
							}
							String sys_zdm1=(String) map1.get("sys_zdm");
							if(StringUtils.isNotBlank(sys_zdm)&&StringUtils.isNotBlank(sys_zdm1)&&sys_zdm.equals(sys_zdm1)){
								for (Iterator iterator = map1.keySet().iterator(); iterator.hasNext();) {
									String  key = (String ) iterator.next();
									map.put(key, map1.get(key));
								}
							}
						}
					}
					xmlist=tjlist;
					
				}
				
			}
			
			return xmlist;
		} 	
}
