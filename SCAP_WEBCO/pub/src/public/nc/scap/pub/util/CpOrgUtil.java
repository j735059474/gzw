package nc.scap.pub.util;

import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.itf.org.IDeptQryService;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.sysinit.constant.ISysinitConstant;
import nc.scap.sysinit.util.ScapSysinitUtil;
import nc.uap.cpb.org.exception.CpbBusinessException;
import nc.uap.cpb.org.itf.ICpOrgQry;
import nc.uap.cpb.org.itf.ICpUserQry;
import nc.uap.cpb.org.orgs.CpOrgVO;
import nc.uap.cpb.org.pubview.mode.ModeOrgFilter;
import nc.uap.cpb.org.pubview.mode.ModeOrgHelper;
import nc.uap.cpb.org.util.CpbServiceFacility;
import nc.uap.cpb.org.vos.CpSysinitVO;
import nc.uap.cpb.org.vos.CpUserVO;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.portal.log.ScapLogger;
import nc.vo.org.DeptVO;
import nc.vo.pub.BusinessException;

import org.apache.commons.lang.StringUtils;

import uap.web.bd.pub.AppUtil;

public class CpOrgUtil {
	
	/**
	 * 省直属监管企业节点SYS_ORG
	 * @author ydyanyh 2014-6-11
	 */
	public static final String SZS_SYS_ORG = "HZQYB0025";
	public static final String QBSS_SYS_ORG = "HZQYB0014";
	
	/**
	 * 根据用户主键判断是否企业用户，返回true为企业用户，返回false为国资办用户
	 * 
	 * @author taoye 2013-7-16
	 */
	public static boolean isCompanyUser(String pkUser) {
		boolean flag = false;
		CpUserVO userVo = getUserVoByPk(pkUser);
		String pkOrg = userVo.getPk_org();
		CpOrgVO orgVo = CpOrgUtil.getOrgVoByPk(pkOrg);
		// 获取系统参数中配置的国资委组织机构码
		String gzwOrgCode = ScapSysinitUtil
				.getSysinitStrByCode(IGlobalConstants.GZWORGCODE);
		// 判断如果系统参数中配置的组织机构码等于传入组织的组织机构码 则返回false
		if (orgVo != null
				&& orgVo.getCode() != null
				&& gzwOrgCode != null
				&& (gzwOrgCode.equals(orgVo.getCode()) || "jxgzw".equals(orgVo
						.getCode()))) {
			return flag;
		}
		if (orgVo.getOrglevel().trim().equals("2")
				&& orgVo.getPk_fatherorg() != null
				&& !orgVo.getPk_fatherorg().trim().equals("")
				&& !orgVo.getPk_fatherorg().trim().equals("~")) {
			flag = true;
		}
		ScapLogger.debug("判断主键为" + pkUser + ",编码为" + getUserCodeByPk(pkUser)
				+ "的登录用户是" + " " + (flag == true ? "企业" : "国资委") + " 用户。");
		return flag;
	}

	/**
	 * 根据组织主键判断是否企业组织，返回true为企业组织，返回false为国资办组织
	 * 
	 * @author taoye 2013-7-16
	 * @throws CpbBusinessException
	 */
	public static boolean isCompanyOrg(String pkOrg)
			{
		boolean flag = true;
		//新疆特殊处理2015-09-15houlg
		String provinceId= ScapSysinitUtil.getSysinitStrByCode(ISysinitConstant.PROVINCE_ID);
		if("XJ".equals(provinceId)) {
			if(StringUtils.isNotBlank(pkOrg)&&(pkOrg.equals(getXJGzwOrgPk()))){
				return false;
			}return true;
		}
		if("SC".equals(provinceId)) {
			if(StringUtils.isNotBlank(pkOrg)&&(pkOrg.equals(getSCGzwOrgPk()))){
				return false;
			}return true;
		}
		if ("JX".equals(provinceId)) {
			if (StringUtils.isNotBlank(pkOrg)
					&& (pkOrg.equals(getJXGzwOrgPk()))) {
				return false;
			}
			// return true;
		}
		// 获取系统参数中配置的国资委组织编码
		String gzwOrgCode = ScapSysinitUtil
				.getSysinitStrByCode(IGlobalConstants.GZWORGCODE);
		CpOrgVO orgVo = CpOrgUtil.getOrgVoByPk(pkOrg);
		//判断系统参数中配置的编码是否等于传入组织的组织编码
		if (orgVo != null
				&& orgVo.getCode() != null
				&& gzwOrgCode != null
				&& (gzwOrgCode.equals(orgVo.getCode()) || "jxgzw".equals(orgVo
						.getCode()))) {
			flag = false;
			return flag;
		}
		if (orgVo.getOrglevel().trim().equals("2")
				&& orgVo.getPk_fatherorg() != null
				&& !orgVo.getPk_fatherorg().trim().equals("")
				&& !orgVo.getPk_fatherorg().trim().equals("~")) {
			flag = true;
		}
		ScapLogger.debug("判断主键为" + pkOrg + ",编码为"
				+ CpOrgUtil.getOrgVoByPk(pkOrg) + "的登录组织是" + " "
				+ (flag == true ? "企业" : "国资委") + " 用户。");
		return flag;
	}
	/**
	 * 根据组织主键判断是否企业组织，返回-1为企业组织，返回0为国资办组织1是分类
	 * 
	 * @author taoye 2013-7-16
	 * @throws CpbBusinessException
	 */
	public static int isDlCompanyOrg(String pkOrg)
			{
		boolean flag = true;
		// 获取系统参数中配置的国资委组织机构码
		String gzwOrgCode = ScapSysinitUtil
				.getSysinitStrByCode(IGlobalConstants.GZWORGCODE);
		CpOrgVO orgVo = CpOrgUtil.getOrgVoByPk(pkOrg);
		// 判断如果系统参数中配置的组织机构码等于传入组织的组织机构码 则返回false
		if (orgVo != null
				&& orgVo.getCode() != null
				&& gzwOrgCode != null
				&& (gzwOrgCode.equals(orgVo.getCode()) || "jxgzw".equals(orgVo
						.getCode()))) {
			return 0;
		}else if(orgVo != null&&("0001".equals(orgVo.getCode())||"0002".equals(orgVo.getCode())||"0003".equals(orgVo.getCode()))){
			return 1;
		}
		if (orgVo.getOrglevel().trim().equals("2")
				&& orgVo.getPk_fatherorg() != null
				&& !orgVo.getPk_fatherorg().trim().equals("")
				&& !orgVo.getPk_fatherorg().trim().equals("~")) {
			flag = true;
		}
		ScapLogger.debug("判断主键为" + pkOrg + ",编码为"
				+ CpOrgUtil.getOrgVoByPk(pkOrg) + "的登录组织是" + " "
				+ (flag == true ? "企业" : "国资委") + " 用户。");
		return -1;
	}
	/**
	 * 1是国资委用户，2为企业用户，3为中介机构用户
	 * @author taoye 2013-7-16
	 */
	public static int intGwzOrCompanyOrPartnerUser(String pkUser) {
		int flag = 1;
		CpUserVO userVo = getUserVoByPk(pkUser);
		String pkOrg = userVo.getPk_org();
		CpOrgVO orgVo = CpOrgUtil.getOrgVoByPk(pkOrg);
		String provinceId= ScapSysinitUtil.getSysinitStrByCode(ISysinitConstant.PROVINCE_ID);
		if("SC".equals(provinceId)) {
			if (orgVo != null && orgVo.getCode().equals(getGzwOrgPk())) {
				return flag;
			}
		} else {
			if (orgVo != null && orgVo.getPk_org().equals(getGzwOrgPk())) {
				return flag;
			}
		}
		if(orgVo.getOrglevel().trim().equals("2") && orgVo.getPk_fatherorg() != null && !orgVo.getPk_fatherorg().trim().equals("") && !orgVo.getPk_fatherorg().trim().equals("~")) {
		
			if(getEnterpriseUserOrgPk(pkOrg)>0)
			{
				flag = 2;
			}
			else
			{
				flag = 3;	
			}
//			if (getPartnerUserOrgPk(pkOrg)>0)
//			{
//				flag = 3;				
//			}			
		}
		else
		{
			if(orgVo.getDef14()!=null&&orgVo.getDef14().equals("Y"))
				flag=3;
			else
				flag = 1;
		}
		ScapLogger.debug("判断主键为" + pkUser + ",编码为" + getUserCodeByPk(pkUser) + "的登录用户是" +
				" " + (flag == 2 ? "企业" : "国资委") + " 用户。");
		return flag;
	}
	
	/**
	 * 判断当前用户所在组织是否在中介机构节点下
	 * userorg 用户所在组织pk
	 */
	public static int getPartnerUserOrgPk(String userorg){
		
		BaseDAO bd = ScapDAO.getBaseDAO();
		String sql = " select count(c.pk_org) from cp_orgs c where c.orglevel='2' and (c.pk_orglevel1 in (select p.pk_group from org_group p where p.pk_fathergroup!='~')) and c.pk_org='"+userorg+"' ";
		try {
//		Integer count=(Integer)bd.executeQuery(sql, new CountQryProcessor());
		List listcount=(List)bd.executeQuery(sql, new ColumnListProcessor());
		Integer count = (Integer) listcount.get(0);
		if(count==null){
			count=0;
		}
		return count;
		} catch (DAOException e) {
			ScapLogger.error("查询异常！查询用户所属组织主键出错！", e);
		}
		return 0;
	}
	
	
	/**
	 * 判断当前用户所在组织是否在企业机构节点下
	 * userorg 用户所在组织pk
	 */
	public static int getEnterpriseUserOrgPk(String userorg){
		
		BaseDAO bd = ScapDAO.getBaseDAO();
		String sql = " select count(c.pk_org) from cp_orgs c where c.orglevel='2' and (c.pk_orglevel1 in (select p.pk_group from org_group p where p.pk_fathergroup='~')) and c.pk_org='"+userorg+"' ";
		try {
//		Integer count=(Integer)bd.executeQuery(sql, new CountQryProcessor());
		List listcount=(List)bd.executeQuery(sql, new ColumnListProcessor());
		Integer count = (Integer) listcount.get(0);
		if(count==null){
			count=0;
		}
		return count;
		} catch (DAOException e) {
			ScapLogger.error("查询异常！查询用户所属组织主键出错！", e);
		}
		return 0;
	}
	
	/**
	 * 查询国资委业务单元主键
	 * @author taoye 2013-7-22
	 */
	public static String getGzwOrgPk() {
		BaseDAO dao = new BaseDAO();
		String provinceId= ScapSysinitUtil.getSysinitStrByCode(ISysinitConstant.PROVINCE_ID);
		try {
			CpSysinitVO[] result = (CpSysinitVO[]) ScapDAO.retrieveByCondition(CpSysinitVO.class, "initcode = '" + IGlobalConstants.GZWORGCODE + "'");
			String sql=" orglevel = '2' and " + " (pk_fatherorg = '~' or pk_fatherorg is null) ";
			if(result != null && result.length > 0) {
				String value = result[0].getValue();
				if(value != null && !"".equals(value)) {
					if(value.length() == 20) {
						return value;
					}else {
						sql=" orglevel = '2' and code = '" + value + "'";
					}
				}
			}
			if("AH".equals(provinceId)||"HN".equals(provinceId)){
				sql +=" and (pk_orglevel2 = '~' or pk_orglevel2 is null) ";
			}
			List<CpOrgVO> orgs = (List<CpOrgVO>) dao.retrieveByClause(CpOrgVO.class, sql);
			if(orgs.size() > 0 && orgs.get(0) != null) {
				return orgs.get(0).getPk_org();
			}
		} catch (DAOException e) {
			ScapLogger.error("查询异常！查询国资委业务单元主键出错！", e);
		}
		return null;
	}
	/**
	 * 查询国资委业务单元主键
	 * @author taoye 2013-7-22
	 */
	public static String getXJGzwOrgPk() {
		BaseDAO dao = new BaseDAO();
		try {
			//modify  2015年9月21日18:58:05 lht
			String sql=" def12= 'HZQYB0014'";
			List<CpOrgVO> orgs = (List<CpOrgVO>) dao.retrieveByClause(CpOrgVO.class, sql);
			
			if(orgs.size() > 0 && orgs.get(0) != null)
				return orgs.get(0).getPk_org();
		} catch (DAOException e) {
			ScapLogger.error("查询异常！查询国资委业务单元主键出错！", e);
		}
		return null;
	}
	/**
	 * 查询四川国资委业务单元主键
	 * @author luqzh 2015-10-9
	 */
	public static String getSCGzwOrgPk() {
		//固定写死四川省国资委的pk(全部企业的下一级组织)
		return "0001C110000000009D7O";
	}
	
	/**
	 * 查询江西国资委业务单元主键
	 * 
	 * @author luqzh 2015-10-9
	 */
	public static String getJXGzwOrgPk() {
		// 固定写死江西省国资委的pk(全部企业的下一级组织)
		return "0001A1100000000007Q4";
	}

	/**
	 * 查询国资委下属部门VO，返回的是DeptVO[]
	 * 
	 * @author taoye 2013-7-22
	 */
	public static DeptVO[] getGzwDept() {
		IDeptQryService service = NCLocator.getInstance().lookup(IDeptQryService.class);
		String pk_org_gzw = getGzwOrgPk();
		if(pk_org_gzw == null || pk_org_gzw.equals("")) {
			ScapLogger.error("查询异常！为查询到国资委业务单元主键,pk_org is null！");
		}
		try {
			DeptVO[] depts = service.queryAllDeptVOsByOrgID(pk_org_gzw);
			return depts;
		} catch (BusinessException e) {
			ScapLogger.error("查询异常！根据国资委主键" + pk_org_gzw + "查询所属部门出错！", e);
		}
		return null;
	}
	
	/**
	 * 根据业务单元主键查询该业务单元的所有部门VO，返回的是DeptVO[]
	 * @author taoye 2013-7-22
	 */
	public static DeptVO[] getCorpDept(String pk_org) {
		IDeptQryService service = NCLocator.getInstance().lookup(IDeptQryService.class);
		try {
			DeptVO[] depts = service.queryAllDeptVOsByOrgID(pk_org);
			return depts;
		} catch (BusinessException e) {
			ScapLogger.error("查询异常！根据业务单元主键" + pk_org + "查询所属部门出错！", e);
		}
		return null;
		
	}
	
	/**
	 * 根据用户主键生成CpUserVO
	 * @author taoye 2013-7-15
	 */
	public static CpUserVO getUserVoByPk(String pkUser) {
		if(pkUser == null || pkUser.equals(""))
			ScapLogger.error("异常！当前用户主键为空，无法查询用户VO！");
		ICpUserQry userQry = CpbServiceFacility.getCpUserQry();
		try {
			CpUserVO userVo = userQry.getUserByPk(pkUser);
			return userVo;
		}catch(Exception e) {
			ScapLogger.error("异常！根据用户主键 " + pkUser + " 生成CpUserVO数据错误！", e);
		}
		return null;
	}
	
	/**
	 * 根据用户主键获取用户编码
	 * @author taoye 2013-7-16
	 */
	public static String getUserCodeByPk(String pkUser) {
		CpUserVO userVo = getUserVoByPk(pkUser);
		return userVo == null ? null : userVo.getUser_code();
	}
	
	/**
	 * 根据用户主键获取用户名称
	 * @author taoye 2013-7-16
	 */
	public static String getUserNameByPk(String pkUser) {
		CpUserVO userVo = getUserVoByPk(pkUser);
		return userVo == null ? null : userVo.getUser_name();
	}
	
	/**
	 * 根据集团/业务单元/部门主键生成CpOrgVO
	 * @author taoye 2013-7-15
	 */
	public static CpOrgVO getOrgVoByPk(String pkOrg) {
		if(pkOrg == null || pkOrg.equals(""))
			ScapLogger.error("异常！当前业务单元主键为空，无法查询业务单元VO！");
		ICpOrgQry orgQry = CpbServiceFacility.getCpOrgRefefenceQry();
		try {
			CpOrgVO orgVo = orgQry.getOrg(pkOrg);
			return orgVo;
		}catch(Exception e) {
			ScapLogger.error("异常！根据业务单元主键 " + pkOrg + " 生成CpOrgVO数据错误！", e);
		}
		return null;
	}
	
	/**
	 * 根据集团/业务单元/部门主键获取集团/业务单元/部门名称
	 * @author taoye 2013-7-16
	 */
	public static String getNameByPk(String pkOrg) {
		CpOrgVO orgVo = getOrgVoByPk(pkOrg);
		return orgVo == null ? null : orgVo.getName();
	}
	
	/**
	 * 根据用户主键获取所属集团主键
	 * @author taoye 2013-7-16
	 */
	public static String getGroupPkByUserPk(String pkUser) {
		CpUserVO userVo = getUserVoByPk(pkUser);
		return userVo == null ? null : userVo.getPk_group();
	}
	
	/**
	 * 根据用户主键获取所属集团Vo
	 * @author taoye 2013-7-16
	 */
	public static CpOrgVO getGroupVoByUserPk(String pkUser) {
		CpOrgVO groupVo = getOrgVoByPk(getGroupPkByUserPk(pkUser));
		return groupVo == null ? null : groupVo;
	}
	
	/**
	 * 根据用户主键获取所属集团编码
	 * @author taoye 2013-7-16
	 */
	public static String getGroupCodeByUserPk(String pkUser) {
		CpOrgVO groupVo = getGroupVoByUserPk(pkUser);
		return groupVo == null ? null : groupVo.getCode();
	}
	
	/**
	 * 根据用户主键获取所属集团名称
	 * @author taoye 2013-7-16
	 */
	public static String getGroupNameByUserPk(String pkUser) {
		CpOrgVO groupVo = getGroupVoByUserPk(pkUser);
		return groupVo == null ? null : groupVo.getName();
	}
	
	/**
	 * 根据用户主键获取所属业务单元主键
	 * @author taoye 2013-7-16
	 */
	public static String getOrgPkByUserPk(String pkUser) {
		CpUserVO userVo = getUserVoByPk(pkUser);
		return userVo == null ? null : userVo.getPk_org();
	}
	
	/**
	 * 根据用户主键获取所属业务单元Vo
	 * @author taoye 2013-7-16
	 */
	public static CpOrgVO getOrgVoByUserPk(String pkUser) {
		CpOrgVO orgVo = getOrgVoByPk(getOrgPkByUserPk(pkUser));
		return orgVo == null ? null : orgVo;
	}
	
	/**
	 * 根据用户主键获取所属业务单元编码
	 * @author taoye 2013-7-16
	 */
	public static String getOrgCodeByUserPk(String pkUser) {
		CpOrgVO orgVo = getOrgVoByUserPk(pkUser);
		return orgVo == null ? null : orgVo.getCode();
	}
	
	/**
	 * 根据用户主键获取所属业务单元名称
	 * @author taoye 2013-7-16
	 */
	public static String getOrgNameByUserPk(String pkUser) {
		CpOrgVO orgVo = getOrgVoByUserPk(pkUser);
		return orgVo == null ? null : orgVo.getName();
	}

	/**
	 * 根据用户主键获取所属部门主键
	 * @author taoye 2013-7-16
	 */
	public static String getDeptPkByUserPk(String pkUser) {
		CpUserVO userVo = getUserVoByPk(pkUser);
		return userVo == null ? null : userVo.getPk_dept();
	}
	
	/**
	 * 根据用户主键获取所属部门Vo
	 * @author taoye 2013-7-16
	 */
	public static CpOrgVO getDeptVoByUserPk(String pkUser) {
		CpOrgVO deptVo = getOrgVoByPk(getDeptPkByUserPk(pkUser));
		return deptVo == null ? null : deptVo;
	}
	
	/**
	 * 根据用户主键获取所属部门编码
	 * @author taoye 2013-7-16
	 */
	public static String getDeptCodeByUserPk(String pkUser) {
		CpOrgVO deptVo = getDeptVoByUserPk(pkUser);
		return deptVo == null ? null : deptVo.getCode();
	}
	
	/**
	 * 根据用户主键获取所属部门名称
	 * @author taoye 2013-7-16
	 */
	public static String getDeptNameByUserPk(String pkUser) {
		CpOrgVO deptVo = getDeptVoByUserPk(pkUser);
		return deptVo == null ? null : deptVo.getName();
	}
	
	/*
	 * 
	 */
	public static String getGlobalOrgPk() {
		CpOrgVO[] globals = (CpOrgVO[]) ScapDAO.retrieveByCondition(CpOrgVO.class, "code = 'global'");
		if(globals != null && globals.length > 0) {
			return globals[0].getPk_org();
		}else {
			return "";
		}
	}
	
	/**
	 * 获取过滤组织sql语句
	 * @author yhl 2014-4-15
	 */
	public static String getFilterOrgWhereSql() {
		String curuserpk = LfwRuntimeEnvironment.getLfwSessionBean().getPk_user();
		String curGrouppk = LfwRuntimeEnvironment.getLfwSessionBean().getPk_unit();
		ModeOrgFilter filter = (ModeOrgFilter)AppLifeCycleContext.current().getApplicationContext().getAppAttribute(ModeOrgHelper.ModeFilter_AttID);
		if(filter == null){
			filter = new ModeOrgFilter();
			filter.setNodecode(AppUtil.getCurNodeCode());
		}
		filter.setIsneedGroup(false);//不显示集团
		
//        String wheresql=" dr=0 START WITH  pk_org = '"+AppUtil.getPk_org()+"' CONNECT BY PRIOR pk_org=pk_fatherorg";//默认条件为当前登录用户所在组织及下级组织
        String wheresql="";//默认条件为当前登录用户所在组织及下级组织
		try {
			 wheresql = ModeOrgHelper.buildOrgSql(curuserpk, curGrouppk, filter);
		} catch (CpbBusinessException e) {
			ScapLogger.error(e.getMessage());
		} catch (LfwBusinessException e) {
			ScapLogger.error(e.getMessage());
		}
		return wheresql;
	}
	
	/**
	 * 根据年份获取多版本的省属企业
	 * @param year
	 * @return
	 */
	public static String  getMoreVersionSSOrgByYear(String year){
		String sql="select b.* from  org_orgs_v  a left join cp_orgs b on a.pk_org=b.pk_org where a.vname='"+year+"' and b.def13 is not null";
	    return sql;
	} 
	
	/**
	 * 根据年份获取多版本的省属企业
	 * @param year
	 * @return
	 */
	public static String  getMoreVersionSSOrgByYear1(String year){
		String sql="select a.* from  org_orgs_v  a where a.entitytype <> '~'";
	    return sql;
	} 
	
	/**
	 * rmsCode:报表组织体系编码，level:企业级次
	 * @param rmsCode
	 * @param level
	 * @return
	 */
	public static int getOrgCount(String rmsCode, int level) {
		int count = 0;
		String rootSql = "select t.pk_org from org_rmsmember t " +
				"where t.pk_fathermember = '~' and t.pk_fatherorg = '~' " +
				"and t.pk_rms = (select c.pk_reportmanastru from org_reportmanastru c where c.code = '" + rmsCode + "')";
		List<Map<String, String>> results = (List<Map<String, String>>) ScapDAO.executeQuery(rootSql, new MapListProcessor());
		if(results == null || level < 1) {
			return count;
		}
		
		for(int n = 1; n <= level; n++) {
			String orgSql = "select t.pk_org from org_rmsmember t where t.pk_fatherorg in (''";
			for(Map<String, String> result : results) {
				orgSql += ",'" + result.get("pk_org") + "'";
			}
			orgSql += ")";
			results = (List<Map<String, String>>) ScapDAO.executeQuery(orgSql, new MapListProcessor());
			if(results == null || results.size() == 0) {
				break;
			}
			if(n == level) {
				count = results.size();
			}
		}
		
		return count;
	}
	
	public static CpOrgVO getDefaultQueryOrgValue(){
		String pk_unit = LfwRuntimeEnvironment.getLfwSessionBean().getPk_unit();
		String pk_org=CntUtil.getCntOrgPk();
		int flag=CntUtil.CtnGwzOrCompanyOrPartnerUser();//1是国资委用户，2为企业用户，3为中介机构用户
		boolean isFilterOrg = ScapSysinitUtil.isFilterOrg();
		String tbSql = "";
		if(isFilterOrg){
			if(flag==2){
				tbSql="(SELECT pk_org,code,name,name2,name3,name4,name5,name6,def11,innercode,(case when pk_fatherorg = '~' then pk_orglevel1 else pk_fatherorg END) AS pk_fatherorg " +
						"FROM cp_orgs   START WITH pk_org = '"+pk_org+"' CONNECT BY PRIOR pk_org = pk_fatherorg )" +
								"cp_orgs";
			}else {			
		        String wheresql = CpOrgUtil.getFilterOrgWhereSql();
			if(StringUtils.isEmpty(wheresql)){
				wheresql="  START WITH pk_org = '"+pk_org+"' CONNECT BY PRIOR pk_org = pk_fatherorg ";
			}else{
				wheresql=" where "+wheresql;
			}
				tbSql = "(SELECT pk_org,code,name,name2,name3,name4,name5,name6,def11,innercode,(case when pk_fatherorg = '~' then pk_orglevel1 else pk_fatherorg END) AS pk_fatherorg " +
						"FROM cp_orgs " + wheresql + ")"+"cp_orgs";
			}
		}else{
			if(flag==2){
				tbSql = "(SELECT pk_org,code,name,name2,name3,name4,name5,name6,def11,innercode,(case when pk_fatherorg = '~' then pk_orglevel1 else pk_fatherorg END) AS pk_fatherorg " +
						"FROM cp_orgs   START WITH pk_org = '"+pk_org+"' CONNECT BY PRIOR pk_org = pk_fatherorg )" +
						"cp_orgs";
			}else {
				tbSql = "(SELECT pk_org,code,name,name2,name3,name4,name5,name6,def11,innercode,(case when pk_fatherorg = '~' then pk_orglevel1 else pk_fatherorg END) AS pk_fatherorg " +
						"FROM cp_orgs " +
						"WHERE ((orglevel = '1' and pk_org = '"+ pk_unit +"' ) OR (pk_orglevel1 = '"+pk_unit+"' AND orglevel = '2' ) ) and  enablestate = '2' )" +
								"cp_orgs";
			}
		}
	List<CpOrgVO> l=	(List<CpOrgVO>) ScapDAO.executeQuery(" select * from "+tbSql+"  order by code ", new BeanListProcessor(CpOrgVO.class));
	 if(l!=null&&l.size()>0){
		 if(l.get(0)!=null&&l.get(0).getPk_org().equals(getGzwOrgPk())&&l.size()>1){
			 return l.get(1); 
		 }else{
		 return l.get(0);
		 }
	 }else{
		 return CntUtil.getCntOrgVo();
	 }
	}
	/**
	 * 获取所有的组织机构码def12
	 * @return
	 */
	public static List getAllOrgs(){
		String sql="select distinct pk_org from cp_orgs where pk_org is not null";
    	List list=(List) ScapDAO.executeQuery(sql,  new MapListProcessor()); 
    	return list;
	}
}
