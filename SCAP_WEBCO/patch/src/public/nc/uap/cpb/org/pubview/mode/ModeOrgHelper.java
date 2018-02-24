package nc.uap.cpb.org.pubview.mode;

import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.uap.cpb.log.CpLogger;
import nc.uap.cpb.org.exception.CpbBusinessException;
import nc.uap.cpb.org.itf.ICpAppsNodeQry;
import nc.uap.cpb.org.itf.ICpUserQry;
import nc.uap.cpb.org.orgs.CpOrgVO;
import nc.uap.cpb.org.vos.CpAppsNodeVO;
import nc.uap.lfw.core.crud.CRUDHelper;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.util.LfwClassUtil;

import org.apache.commons.lang.StringUtils;

import uap.lfw.core.ml.LfwResBundle;
import uap.web.bd.pub.AppUtil;

public class ModeOrgHelper {
	public static String ModeFilter_AttID = "ModeOrg_Filter_Att";
 
	private static String ModeOrg_AttID = "modeorgPk";
	
	/**
	 * modify by maokun  2012.09.03   采用协同组织
	 * @param curuserpk
	 * @param curGrouppk
	 * @param filter
	 * @return
	 * @throws LfwBusinessException
	 */
	public static CpOrgVO[] GetCpOrg(String curuserpk,String curGrouppk,ModeOrgFilter filter) throws LfwBusinessException{
		CpOrgVO[] cpOrgs;
		String wheresql = buildOrgSql(curuserpk, curGrouppk, filter);
		
//		wheresql += " ) ";
//		if(filter.isIsneedGlobal()){
//			wheresql += " or pk_group ='" + IOrgConst.GLOBEORG +"'";
//		}
		//全部组织,//权限过滤		
		//集团过滤
		PaginationInfo pinfo = new PaginationInfo();
		Map<String, Object> extMap = new HashMap<String, Object>();
		String sql = "select * from "+ CpOrgVO.getDefaultTableName() +" where "  + wheresql + " ORDER BY code";
		cpOrgs =CRUDHelper.getMdCRUDService().queryVOs(sql, CpOrgVO.class,pinfo, "",extMap);
		String filterClass = filter.getFilterClass();
		if(StringUtils.isNotBlank(filterClass)){
			try{
				Object orgFilter = LfwClassUtil.newInstance(filterClass);
				if(orgFilter instanceof IModeOrgFilter)
					cpOrgs = ((IModeOrgFilter)orgFilter).filter(cpOrgs);
			}
			catch(Throwable e){
				CpLogger.error("组织参照过滤时出错：" + e.getMessage());
				CpLogger.error(e);
			}
		}
		return cpOrgs;
	}

	/**
	 * 构造where条件
	 * @param curuserpk
	 * @param curGrouppk
	 * @param filter
	 * @return
	 * @throws LfwBusinessException
	 * @throws CpbBusinessException
	 */
	public static String buildOrgSql(String curuserpk, String curGrouppk,
			ModeOrgFilter filter) throws LfwBusinessException,
			CpbBusinessException {
		if(StringUtils.isEmpty(filter.getPk_fun())){
			if(StringUtils.isEmpty(filter.getNodecode())){
				throw new  LfwBusinessException(LfwResBundle.getInstance().getStrByID("bd", "ModeOrgHelper-000000")/*查询已分配组织出错,功能节点不能为空*/);
			}
			ICpAppsNodeQry nodeqry = NCLocator.getInstance().lookup(ICpAppsNodeQry.class);
			CpAppsNodeVO node  = nodeqry.getNodeById(filter.getNodecode());
			if(node == null){
				throw new  LfwBusinessException(LfwResBundle.getInstance().getStrByID("bd", "ModeOrgHelper-000000")/*查询已分配组织出错,功能节点不能为空*/);
			}
			filter.setPk_fun(node.getPk_appsnode());
			
		}
//		String wheresql = "( 1=1  " ;
		String wheresql;
		if(filter.isIsneedUnit())
			wheresql = " ( orglevel = '1' OR orglevel = '2' ) ";
		else
			wheresql = " ( orglevel = '1') ";
		wheresql += " and enablestate = '2' ";
		if(curGrouppk != null && !curGrouppk.equals(""))
			wheresql +=" AND pk_orglevel1 = '" + curGrouppk + "' ";
		//集团管理员是组织合集,不做权限过滤
		if(!filter.isIsgrpadmin()){
			if(filter.isIsfilterSecurity()){
				wheresql += " and pk_org in ( " +
						"select distinct pk_org from cp_roleorg  a " +
						"left join cp_userrole b on a.pk_role = b.pk_role " +
						"left join cp_roleresp c on c.pk_role = b.pk_role " +
//						"left join cp_resp_func d on c.pk_responsibility = d.pk_responsibility " +
//						"where b.pk_user = '"+curuserpk+"' and d.busi_pk = '"+filter.getPk_fun() + "'" +
						"left join cp_resp_res d on c.pk_responsibility = d.pk_responsibility " +
						"where b.pk_user = '"+curuserpk+"' and d.pk_res = '"+filter.getPk_fun() + "'" +
						") ";
			}
		}
		if(!filter.isIsneedGroup()){
			wheresql += " and pk_org != '"+ curGrouppk + "' "; 
		}
		return wheresql;
	}
		
	public static String getModeOrgPK(){
		if(AppLifeCycleContext.current().getApplicationContext().getAppEnvironment() != null)
			return AppLifeCycleContext.current().getApplicationContext().getAppEnvironment().getPk_org();
		else
			return null;
	}
	public static String[] getCurNodeOrgs() throws CpbBusinessException{
		ModeOrgFilter filter = new ModeOrgFilter();
		filter.setIsneedGlobal(false);
		filter.setIsfilterSecurity(true);
		filter.setNodecode(AppUtil.getCurNodeCode());
		filter.setIsneedGroup(true);
		InvocationInfoProxy invocproxy = InvocationInfoProxy.getInstance();
		String pk_user = invocproxy.getUserId();
		ICpUserQry userqry = NCLocator.getInstance().lookup(ICpUserQry.class);
		if(userqry.isSysAdmin(pk_user))
			 filter.setIsgrpadmin(true);
		String pk_group = invocproxy.getGroupId();
		CpOrgVO[] orgs;
		try {
			orgs = ModeOrgHelper.GetCpOrg(pk_user, pk_group, filter);
			String[] pk_orgs = null;
			if(orgs != null){
				pk_orgs = new String[orgs.length];
				for(int i = 0 ; i < orgs.length ; ++i)
					pk_orgs[i] = orgs[i].getPk_org();
			}
			return pk_orgs;
		} catch (LfwBusinessException e) {
			CpLogger.error(e);
			throw new CpbBusinessException(e.getMessage());
		}
	}
}
