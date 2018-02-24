package nc.scap.jjpub.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uap.web.bd.pub.AppUtil;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.scap.dsm.material.MaterialListWinMainViewCtrl;
import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.ScapDAO;
import nc.scap.scapjj.pub.IConst4scapjj;
import nc.uap.cpb.log.CpLogger;
import nc.uap.cpb.org.util.CpbUtil;
import nc.uap.cpb.persist.dao.PtBaseDAO;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scapjj.dsm.Datetype_HVO;
/**
 * 2014-09-15
 * 
 * @author thx
 *
 */
public class JjUtil {
	
	/**
	 * Object 转 Integer
	 * @param obj
	 * @return
	 */
	static public Integer getInteger(Object obj){
		try{
			return Integer.valueOf(obj.toString());
		}catch(Exception e){
			return (Integer)0;
		}
	}
	
	/**
	 * Object 转UFDouble
	 * @param obj
	 * @return
	 */
	static public UFDouble getUFD(Object obj){
		try{
			return new UFDouble(obj.toString().trim());
		}catch(Exception e){
			return UFDouble.ZERO_DBL;
		}
		
	}
	/**
	 * 获取ds 的 row 的 field 字段 值 
	 * @param ds
	 * @param row
	 * @param field
	 * @return
	 */
	static public String getDSvalue(Dataset ds,Row row,String field){
		int index=ds.nameToIndex(field);
		if(index < 0)
			return "";
		String obj=(row==null||row.size()<=0)?"":row.getString(index);
		return obj;
	}
	
	/**
	 * 
	 * 设置 ds 的 row 的 field 字段 值  value
	 * @param ds
	 * @param row
	 * @param field
	 * @param value
	 */
	static public void setDSvalue(Dataset ds,Row row,String field,String value){
		int index=ds.nameToIndex(field);
		if(index >= 0)
			row.setValue(index, value);
	}
	
	/**
	 * DS选中行主键
	 * @param ds
	 * @return
	 */
	static public String getDSPK(Dataset ds){
		return getDSvalue(ds, ds.getSelectedRow(), ds.getPrimaryKeyField());
	}
	
	/**
	 * Object 转String
	 * @param obj
	 * @return
	 */
	static public String getStr(Object obj){
		return obj==null?"":obj.toString();
	}
	
	//========================================================================================================================================
	
	/**
	 * 2015-4-11 add by yanghlc
	 * 根据 资料类型主键数组 通过递归的方式 获取该资料类型主键数组里所有资料类型主键及其所有上级资料类型的pk
	 * @param String[] dyPks   本级资料类型pk数组
	 * @return String
	 * 说明：返回的资料类型pk字符串,单个pk加单引号，每个pk间以逗号分开；如 " 'pk1','pk2','pk3','pkn' "
	 */
	public static String getAllFatherAndSameLevelDataTypeIds(String[] dyPks) {
		if(dyPks==null || dyPks.length==0)
			return null;
		Map<String,String> fatherdtsMap = new HashMap<String,String>();
		Datetype_HVO[] dtVos = getAllDataTypeVos();//获取所有资料类型vo
		
		for (int i=0;i<dyPks.length;i++){
			getFatherDataTypeByRecursive(dyPks[i], dtVos, fatherdtsMap);//
		}
		String[] allids = fatherdtsMap.values().toArray(new String[0]);
		String str = CpbUtil.joinQryArrays(allids);//返回的资料类型pk字符串,单个pk加单引号，每个pk间以逗号分开；如 " 'pk1','pk2','pk3','pkn' "
		return str;
	}
	

	/**
	 * 2015-4-11 add by yanghlc
	 *
	 * @param String[] dyPks   本级资料类型pk数组
	 * @return String
	 * 说明：返回的资料类型pk字符串,单个pk加单引号，每个pk间以逗号分开；如 " 'pk1','pk2','pk3','pkn' "
	 */
	public static String getAllSameLevelDataTypeIds(String[] dyPks) {
		if(dyPks==null || dyPks.length==0)
			return null;
		String str = CpbUtil.joinQryArrays(dyPks);//返回的资料类型pk字符串,单个pk加单引号，每个pk间以逗号分开；如 " 'pk1','pk2','pk3','pkn' "
		return str;
	}
	
	/**
	 * 2015-4-11 add by yanghlc
	 * 根据 资料类型主键 通过递归的方式获取该资料类型及其所有上级资料类型的pk,以map方式存储key:pk_datetype_h; value:pk_datetype_h
	 * @param pk_datetype_h   资料类型pk
	 * @param dts             所有资料类型vo数组
	 * @param fatherdts       map形式存储所有满足条件的资料类型pk,key:pk_datetype_h; value:pk_datetype_h
	 * @return
	 */
	@SuppressWarnings("unused")
	private static void getFatherDataTypeByRecursive(String pk_datetype_h, Datetype_HVO[] dts, Map<String,String> fatherdts) {
		for (Object obj : dts) {
			Datetype_HVO dt = (Datetype_HVO) obj;
			if (dt.getPk_datetype_h().equals(pk_datetype_h)) {
				if(fatherdts.containsKey(pk_datetype_h)) 
					continue;
				fatherdts.put(pk_datetype_h,dt.getPk_datetype_h());
				if (dt.getPk_father() == null || dt.getPk_father().equals("")
						|| dt.getPk_father().equals("~"))
					continue;
				getFatherDataTypeByRecursive(dt.getPk_father(), dts, fatherdts);
			}
		}
	}
	/**
	 * 2015-4-11 add by yanghlc
	 * 获取所有资料类型vo数组
	 * @return Datetype_HVO[]
	 */
	public static Datetype_HVO[] getAllDataTypeVos( ) {
		String sql = " 1 = 1 and dr='0' ";

		try {
			Collection list = new PtBaseDAO().retrieveByClause(Datetype_HVO.class, sql);
			return (Datetype_HVO[]) list.toArray(new Datetype_HVO[0]);
		} catch (Throwable e) {
			CpLogger.error(e);
			throw new LfwRuntimeException(e);
		}
	}
	
	/**
	 * 2015-4-11 add by yanghlc
	 * 获取所有资料类型vo数组
	 * @return Datetype_HVO[]
	 */
	public static Datetype_HVO[] getDataTypeVoByCondition(String wheresql) {
//		String wheresql = " 1=1 and dr=0 and code='"+code+"'";
	       return (Datetype_HVO[]) ScapDAO.retrieveByCondition(Datetype_HVO.class, wheresql, "ts desc");
	}
	/**
	 * 2015-4-11 add by yanghlc
	 * 根据 当前登陆用户id及节点类型获取<资料维护>、<资料权限维护>、<资料查询>这三个节点 能看到的资料类型pk（以字符串数组形式返回）
	 * @param scapjj_material_h   资料维护表
	 * @param zllx             资料维护表存储资料类型字段
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String[] getDataTypePksByUseridAndNodeTypeOrDataType(String dataTypeCode) {
		String node_type = JjUtil.getStr(AppUtil
				.getAppAttr(IGlobalConstants.NODE_TYPE));
		String cuserid = CntUtil.getCntUserPk();
//		String pk_org = CntUtil.getCntOrgPk();
		String wheresql = " select pk_datetype_h from scapjj_datetype_h where dr=0 and enablestate='Y' ";
//		String statussql = " and formstate = '"
//				+ IGlobalConstants.SCAPPM_BILLSTATE_END + "' " + /** *  * 审批状态为审批结束 */" and fbzt = '" + IConst4scapjj.FBZT[0] + "' ";/** 发布状态为 发布 */
		
		if ("add".equals(node_type)) {// 资料维护 填报
			wheresql += "and pk_datetype_h in ("
					+ "select dh.pk_datetype_h from scapjj_poweruser_b ub "
					+ "join scapjj_datetype_h dh on dh.pk_datetype_h=ub.pk_datetype_h "
					+ "where ub.dr=0 and dh.dr=0  and dh.enablestate='Y' and ub.qxlx = '"  /**查询scapjj_datetype_h表数据时加 是否启用 等于 是 的过滤条件dh.enablestate='Y'  */
					+ IConst4scapjj.QXLX_REP
					+ "' "
					+ /** 维护权限的用户 */
					"and ub.yh = '"
					+ cuserid
					+ "' "
					+ "union all "
					+ "select dh.pk_datetype_h from scapjj_powerrole_b rb "
					+ "join scapjj_datetype_h dh on dh.pk_datetype_h=rb.pk_datetype_h "
					+ "join cp_userrole ur on ur.pk_role=rb.jsmx "
					+ "where rb.dr=0 and dh.dr=0  and dh.enablestate='Y' and rb.qxlx = '"   /**查询scapjj_datetype_h表数据时加 是否启用 等于 是 的过滤条件dh.enablestate='Y'  */
					+ IConst4scapjj.QXLX_REP + "' " + /** 维护权限的角色 */
					"and ur.pk_user = '" + cuserid + "' " + ") ";

		} else if ("approve".equals(node_type)) {// 资料维护审核
			wheresql = " select distinct(zllx) from scapjj_material_h where 1=1 ";//考虑到资料查询是要根据资料权限维护下的其他人员调出资料类型,所以基于资料维护表调出资料类型树
			wheresql += "and formstate = '"
					+ IGlobalConstants.SCAPPM_BILLSTATE_RUN + "' ";
			MaterialListWinMainViewCtrl mc = new MaterialListWinMainViewCtrl();
			String flwTypePk = mc.getFlwTypePk();
			String pkFieldName = "pk_material_h";
			wheresql += "and " + pkFieldName + " in "
					+ "(select pk_frmins from wfm_task " + "where pk_owner='"
					+ cuserid + "' and state='State_Run' "
					+ "and pk_flowtype='" + flwTypePk + "')";
	     }else if ("power".equals(node_type)) {// 资料权限维护
			wheresql += "and pk_datetype_h in ("
					+ "select dh.pk_datetype_h from scapjj_poweruser_b ub "
					+ "join scapjj_datetype_h dh on dh.pk_datetype_h=ub.pk_datetype_h "
					+ "where ub.dr=0 and dh.dr=0 and dh.enablestate='Y' and ub.qxlx = '"    /**查询scapjj_datetype_h表数据时加 是否启用 等于 是 的过滤条件dh.enablestate='Y'  */
					+ IConst4scapjj.QXLX_MAN
					+ "' "
					+ /** 管理权限的用户 */
					"and ub.yh = '"
					+ cuserid
					+ "' "
					+ "union all "
					+ "select dh.pk_datetype_h from scapjj_powerrole_b rb "
					+ "join scapjj_datetype_h dh on dh.pk_datetype_h=rb.pk_datetype_h "
					+ "join cp_userrole ur on ur.pk_role=rb.jsmx "
					+ "where rb.dr=0 and dh.dr=0 and dh.enablestate='Y' and rb.qxlx = '"   /**查询scapjj_datetype_h表数据时加 是否启用 等于 是 的过滤条件dh.enablestate='Y'  */
					+ IConst4scapjj.QXLX_MAN + "' " + /** 管理权限的角色 */
					"and ur.pk_user = '" + cuserid + "' " + ") ";
		} else if ("query".equals(node_type)) {// 资料查询
			wheresql = " select distinct(zllx) from scapjj_material_h where 1=1 ";//考虑到资料查询是要根据资料权限维护下的其他人员调出资料类型,所以基于资料维护表调出资料类型树
			
			wheresql += "and (zllx in ("
					+ "select dh.pk_datetype_h from scapjj_poweruser_b ub "
					+ "join scapjj_datetype_h dh on dh.pk_datetype_h=ub.pk_datetype_h "
					+ "where ub.dr=0 and dh.dr=0 and dh.enablestate='Y' and ub.qxlx = '"  /**查询scapjj_datetype_h表数据时加 是否启用 等于 是 的过滤条件dh.enablestate='Y'  */
					+ IConst4scapjj.QXLX_BRO
					+ "' "
					+ /** 浏览权限的用户 */
					"and ub.yh = '"
					+ cuserid
					+ "' "
					+ "union all "
					+ "select dh.pk_datetype_h from scapjj_powerrole_b rb "
					+ "join scapjj_datetype_h dh on dh.pk_datetype_h=rb.pk_datetype_h "
					+ "join cp_userrole ur on ur.pk_role=rb.jsmx "
					+ "where rb.dr=0 and dh.dr=0 and dh.enablestate='Y' and rb.qxlx = '"  /**by yhl* 查询scapjj_datetype_h表数据时加 是否启用 等于 是 的过滤条件dh.enablestate='Y'  */
					+ IConst4scapjj.QXLX_BRO
					+ "' "
					+ /** 浏览权限的角色 */
					"and ur.pk_user = '"
					+ cuserid
					+ "' "
					+ ")  or pk_material_h in ("                                              /**资料的其他人员 */
					+ "select distinct h.pk_material_h from scapjj_otheruser_b b "
					+ "join scapjj_material_h h on h.pk_material_h=b.pk_material_h "
					+ "where h.dr=0 and b.dr=0 and  b.yh = '" + cuserid + "' " + 
					")" +
					" or pk_material_h in ("                                                  /**当前登陆用户默认可以查看  制单人等于当前登录人并且审批通过的资料*/
					+ "select distinct mh.pk_material_h from scapjj_material_h mh "
					+ "where mh.dr=0 and  mh.billmaker = '" + cuserid + "' " +
					") " +
					")";
		}
		String[] dyPks =  queryPks(wheresql);//获取当前登陆用户可以看到的本级资料类型pk数组
		
		String[] dataTypeCodes = null;
		List<String> dyPksList = new ArrayList<String>();
		
		 /**如果在节点注册里传入了data_code={02,03...,0n}形式 的参数，02,03..为资料类型编码。
		  * 则传入的资料类型优先级高，如果当前登陆用户有资料类型A、B、C这三种类型的权限，节点注册里传入的有A、B,
		  * 那么当前登陆用户只能看到资料类型A、B及对资料类型A、B进行相关操作（资料维护、资料权限维护、资料查询操作）
		 *  注意：通过节点注册传入的资料类型虽然优先级高，如果在资料类型权限维护里没有把传入的资料类型授予当前登陆用户，那么传入的资料类型对于当前登陆用户来说也是没有权限的，
		 * 
		 **/
		if(dataTypeCode!=null){
			dataTypeCodes = dataTypeCode.substring(1,dataTypeCode.length() - 1).split(",");
			String dealDatatypecodes = CpbUtil.joinQryArrays(dataTypeCodes);
			String sql = "select pk_datetype_h from scapjj_datetype_h where code in ("+dealDatatypecodes+")";
			String[] dataTypePks = queryPks(sql);
			for (int i=0;i<dataTypePks.length;i++){
				if(CpbUtil.joinQryArrays(dyPks).indexOf(dataTypePks[i])!=-1){
					dyPksList.add(dataTypePks[i]);
				}
			}
			dyPks = (String[]) dyPksList.toArray(new String[0]);//
			
		}
		return dyPks;
	}

	public static String[] queryPks(String wheresql) {
		PtBaseDAO dao = new PtBaseDAO();
		List<Object[]> list = null;
		try {
			list = (List<Object[]>) dao.executeQuery(wheresql, new ArrayListProcessor());
		} catch (DAOException e) {
			throw new LfwRuntimeException(e.getMessage());
		}
		if (list == null || list.size() == 0) {
			return null;
		}
		String[] strAray = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			strAray[i] = (String) list.get(i)[0];
		}
		
		return strAray;
	}
	
	//========================================================================================================================================
	
	
}
