package nc.scap.pub.util;

import org.apache.commons.lang.StringUtils;

import nc.bs.framework.common.NCLocator;
import nc.bs.pub.filesystem.IFileSystemService;
import nc.uap.cpb.org.orgs.CpOrgVO;
import nc.uap.cpb.org.vos.CpUserVO;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.portal.log.ScapLogger;

public class CntUtil {
	
	/**
	 * 判断当前登录用户是否企业用户，返回true为企业用户，返回false为国资办用户
	 * @author taoye 2013-7-16
	 */
	public static boolean CtnUserIsCompanyUser() {
		boolean flag = false;
		flag = CpOrgUtil.isCompanyUser(getCntUserPk());
		ScapLogger.debug("判断当前主键为" + getCntUserPk() + ",用户名为" + getCntUserName() + "的登录用户是" +
				" " + (flag == true ? "企业" : "国资委") + " 用户。");
		return flag;
	}
	
	/**
	 * 1是国资委用户，2为企业用户，3为中介机构用户
	 * @author taoye 2013-7-16
	 */
	public static int CtnGwzOrCompanyOrPartnerUser() {
		int flag = 1;
		flag = CpOrgUtil.intGwzOrCompanyOrPartnerUser(getCntUserPk());
		ScapLogger.debug("判断当前主键为" + getCntUserPk() + ",用户名为" + getCntUserName() + "的登录用户是" +
				" " + (flag == 2 ? "企业" : "国资委") + " 用户。");
		return flag;
	}
	
	/**
	 * 获取当前登录用户主键
	 * @author taoye 2013-7-15
	 */
	public static String getCntUserPk() {
		String pkUser = LfwRuntimeEnvironment.getLfwSessionBean().getPk_user();
		if(pkUser == null || pkUser.equals(""))
			ScapLogger.error("登录异常！ 从Session中获取的当前登录用户主键为空！");
		return pkUser;
	}
	
	/**
	 * 获取当前登录用户编码
	 * @author taoye 2013-7-15
	 */
	public static String getCntUserCode() {
		String userCode = LfwRuntimeEnvironment.getLfwSessionBean().getUser_code();
		if(userCode == null)
			ScapLogger.error("登录异常！从Session中获取的当前登录用户编码为空！");
		return userCode;
	}
	
	/**
	 * 获取当前登录用户姓名
	 * @author taoye 2013-7-15
	 */
	public static String getCntUserName() {
		String userName = LfwRuntimeEnvironment.getLfwSessionBean().getUser_name();
		if(userName == null) 
			ScapLogger.error("登录异常！从Session中获取的当前登录用户姓名为空！");
		return userName;
	}
	
	/**
	 * 获取当前登录用户Vo
	 * @author taoye 2013-7-15
	 */
	public static CpUserVO getCntUserVo() {
		CpUserVO userVo = LfwRuntimeEnvironment.getLfwSessionBean().getUserObj(CpUserVO.class);
		if(userVo == null)
			ScapLogger.error("登录异常！从Session中获取的当前登录用户VO为空！");
		return userVo;
	}
	
	/**
	 * 获取当前登录用户所属集团主键
	 * @author taoye 2013-7-15
	 */
	public static String getCntGroupPk() {
		String pkGroup = LfwRuntimeEnvironment.getLfwSessionBean().getPk_unit();
		if(pkGroup == null || pkGroup.equals(""))
			ScapLogger.error("登录异常！没有从Session中获取到当前登录用户的集团主键！");
		return pkGroup;
	}
	
	/**
	 * 获取当前登录用户所属集团编码
	 * @author taoye 2013-7-15
	 */
	public static String getCntGroupCode() {
		String groupCode = LfwRuntimeEnvironment.getLfwSessionBean().getUnitNo();
		if(groupCode == null)
			ScapLogger.error("登录异常！没有从Session中获取到当前登录用户的集团编码！");
		return groupCode;
	}
	
	/**
	 * 获取当前登录用户所属集团名称
	 * @author taoye 2013-7-15
	 */
	public static String getCntGroupName() {
		String groupName = LfwRuntimeEnvironment.getLfwSessionBean().getUnitName();
		if(groupName == null)
			ScapLogger.error("登录异常！没有从Session中获取到当前登录用户的集团名称！");
		return groupName;
	}
	
	/**
	 * 获取当前登录用户所属集团Vo
	 * @author taoye 2013-7-15
	 */
	public static CpOrgVO getCntGroupVo() {
		String pkGroup = getCntGroupPk();
		CpOrgVO groupVo = CpOrgUtil.getOrgVoByPk(pkGroup);
		if(groupVo == null) 
			ScapLogger.error("登录异常！没有从Session中获取到当前登录用户的集团VO！");
		return groupVo;
	}
	
	/**
	 * 获取当前登录用户所属业务单元主键
	 * @author taoye 2013-7-15
	 */
	public static String getCntOrgPk() {
		String pkOrg = LfwRuntimeEnvironment.getLfwSessionBean().getUser_org();
		if(pkOrg == null || pkOrg.equals(""))
			ScapLogger.error("登录异常！没有从Session中获取到当前登录用户的业务单元主键！");
		return pkOrg;
	}
	
	/**
	 * 获取当前登录用户所属业务单元编码
	 * @author taoye 2013-7-15
	 */
	public static String getCntOrgCode() {
		CpOrgVO orgVo = getCntOrgVo();
		String orgCode = orgVo.getCode();
		if(orgVo == null) 
			ScapLogger.error("登录异常！没有从Session中获取到当前登录用户的业务单元主键！");
		return orgCode;
	}
	
	/**
	 * 获取当前登录用户所属业务单元名称
	 * @author taoye 2013-7-15
	 */
	public static String getCntOrgName() {
		CpOrgVO orgVo = getCntOrgVo();
		String orgName = orgVo.getName();
		if(orgVo == null) 
			ScapLogger.error("登录异常！没有从Session中获取到当前登录用户的业务单元主键！");
		return orgName;
	}
	
	/**
	 * 获取当前登录用户所属业务单元Vo
	 * @author taoye 2013-7-15
	 */
	public static CpOrgVO getCntOrgVo() {
		String pkOrg = getCntOrgPk();
		CpOrgVO orgVo = CpOrgUtil.getOrgVoByPk(pkOrg);
		if(orgVo == null) 
			ScapLogger.error("登录异常！没有从Session中获取到当前登录用户的业务单元VO！");
		return orgVo;
	}
	
	/**
	 * 获取当前登录用户所属部门主键
	 * @author taoye 2013-7-15
	 */
	public static String getCntDeptPk() {
		String pkDept = LfwRuntimeEnvironment.getLfwSessionBean().getPk_dept();
		if(pkDept == null || pkDept.equals("")) 
			ScapLogger.debug("主键为" + getCntUserPk() + "，编码为" + getCntUserCode() + "的当前登录用户无部门信息！");
		return pkDept;
	}
	
	/**
	 * 获取当前登录用户所属部门编码
	 * @author taoye 2013-7-15
	 */
	public static String getCntDeptCode() {
		CpOrgVO deptVo = getCntDeptVo();
		String deptCode = deptVo.getCode();
		return deptCode;
	}	
	
	/**
	 * 获取当前登录用户所属部门名称
	 * @author taoye 2013-7-15
	 */
	public static String getCntDeptName() {
		CpOrgVO deptVo = getCntDeptVo();
		String deptName = deptVo.getName();
		return deptName;
	}
	
	/**
	 * 获取当前登录用户所属部门Vo
	 * @author taoye 2013-7-15
	 */
	public static CpOrgVO getCntDeptVo() {
		String pkDept = getCntDeptPk();
		CpOrgVO deptVo = CpOrgUtil.getOrgVoByPk(pkDept);
		if(deptVo == null) 
			ScapLogger.debug("根据部门主键" + pkDept + "查询不到结果！");
		return deptVo;
	}
	/**
	 * 获取当前登录用户的def12
	 * @author taoye 2013-7-15
	 */
	public static String getOrgDef12(String pk_org){
		if(StringUtils.isBlank(pk_org)){
			 pk_org=CntUtil.getCntOrgPk();
		}
		CpOrgVO vo=CpOrgUtil.getOrgVoByPk(pk_org);
		String sys_org=vo!=null?vo.getDef12():"-1";
		return sys_org;
	}

}
