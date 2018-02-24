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
	 * �жϵ�ǰ��¼�û��Ƿ���ҵ�û�������trueΪ��ҵ�û�������falseΪ���ʰ��û�
	 * @author taoye 2013-7-16
	 */
	public static boolean CtnUserIsCompanyUser() {
		boolean flag = false;
		flag = CpOrgUtil.isCompanyUser(getCntUserPk());
		ScapLogger.debug("�жϵ�ǰ����Ϊ" + getCntUserPk() + ",�û���Ϊ" + getCntUserName() + "�ĵ�¼�û���" +
				" " + (flag == true ? "��ҵ" : "����ί") + " �û���");
		return flag;
	}
	
	/**
	 * 1�ǹ���ί�û���2Ϊ��ҵ�û���3Ϊ�н�����û�
	 * @author taoye 2013-7-16
	 */
	public static int CtnGwzOrCompanyOrPartnerUser() {
		int flag = 1;
		flag = CpOrgUtil.intGwzOrCompanyOrPartnerUser(getCntUserPk());
		ScapLogger.debug("�жϵ�ǰ����Ϊ" + getCntUserPk() + ",�û���Ϊ" + getCntUserName() + "�ĵ�¼�û���" +
				" " + (flag == 2 ? "��ҵ" : "����ί") + " �û���");
		return flag;
	}
	
	/**
	 * ��ȡ��ǰ��¼�û�����
	 * @author taoye 2013-7-15
	 */
	public static String getCntUserPk() {
		String pkUser = LfwRuntimeEnvironment.getLfwSessionBean().getPk_user();
		if(pkUser == null || pkUser.equals(""))
			ScapLogger.error("��¼�쳣�� ��Session�л�ȡ�ĵ�ǰ��¼�û�����Ϊ�գ�");
		return pkUser;
	}
	
	/**
	 * ��ȡ��ǰ��¼�û�����
	 * @author taoye 2013-7-15
	 */
	public static String getCntUserCode() {
		String userCode = LfwRuntimeEnvironment.getLfwSessionBean().getUser_code();
		if(userCode == null)
			ScapLogger.error("��¼�쳣����Session�л�ȡ�ĵ�ǰ��¼�û�����Ϊ�գ�");
		return userCode;
	}
	
	/**
	 * ��ȡ��ǰ��¼�û�����
	 * @author taoye 2013-7-15
	 */
	public static String getCntUserName() {
		String userName = LfwRuntimeEnvironment.getLfwSessionBean().getUser_name();
		if(userName == null) 
			ScapLogger.error("��¼�쳣����Session�л�ȡ�ĵ�ǰ��¼�û�����Ϊ�գ�");
		return userName;
	}
	
	/**
	 * ��ȡ��ǰ��¼�û�Vo
	 * @author taoye 2013-7-15
	 */
	public static CpUserVO getCntUserVo() {
		CpUserVO userVo = LfwRuntimeEnvironment.getLfwSessionBean().getUserObj(CpUserVO.class);
		if(userVo == null)
			ScapLogger.error("��¼�쳣����Session�л�ȡ�ĵ�ǰ��¼�û�VOΪ�գ�");
		return userVo;
	}
	
	/**
	 * ��ȡ��ǰ��¼�û�������������
	 * @author taoye 2013-7-15
	 */
	public static String getCntGroupPk() {
		String pkGroup = LfwRuntimeEnvironment.getLfwSessionBean().getPk_unit();
		if(pkGroup == null || pkGroup.equals(""))
			ScapLogger.error("��¼�쳣��û�д�Session�л�ȡ����ǰ��¼�û��ļ���������");
		return pkGroup;
	}
	
	/**
	 * ��ȡ��ǰ��¼�û��������ű���
	 * @author taoye 2013-7-15
	 */
	public static String getCntGroupCode() {
		String groupCode = LfwRuntimeEnvironment.getLfwSessionBean().getUnitNo();
		if(groupCode == null)
			ScapLogger.error("��¼�쳣��û�д�Session�л�ȡ����ǰ��¼�û��ļ��ű��룡");
		return groupCode;
	}
	
	/**
	 * ��ȡ��ǰ��¼�û�������������
	 * @author taoye 2013-7-15
	 */
	public static String getCntGroupName() {
		String groupName = LfwRuntimeEnvironment.getLfwSessionBean().getUnitName();
		if(groupName == null)
			ScapLogger.error("��¼�쳣��û�д�Session�л�ȡ����ǰ��¼�û��ļ������ƣ�");
		return groupName;
	}
	
	/**
	 * ��ȡ��ǰ��¼�û���������Vo
	 * @author taoye 2013-7-15
	 */
	public static CpOrgVO getCntGroupVo() {
		String pkGroup = getCntGroupPk();
		CpOrgVO groupVo = CpOrgUtil.getOrgVoByPk(pkGroup);
		if(groupVo == null) 
			ScapLogger.error("��¼�쳣��û�д�Session�л�ȡ����ǰ��¼�û��ļ���VO��");
		return groupVo;
	}
	
	/**
	 * ��ȡ��ǰ��¼�û�����ҵ��Ԫ����
	 * @author taoye 2013-7-15
	 */
	public static String getCntOrgPk() {
		String pkOrg = LfwRuntimeEnvironment.getLfwSessionBean().getUser_org();
		if(pkOrg == null || pkOrg.equals(""))
			ScapLogger.error("��¼�쳣��û�д�Session�л�ȡ����ǰ��¼�û���ҵ��Ԫ������");
		return pkOrg;
	}
	
	/**
	 * ��ȡ��ǰ��¼�û�����ҵ��Ԫ����
	 * @author taoye 2013-7-15
	 */
	public static String getCntOrgCode() {
		CpOrgVO orgVo = getCntOrgVo();
		String orgCode = orgVo.getCode();
		if(orgVo == null) 
			ScapLogger.error("��¼�쳣��û�д�Session�л�ȡ����ǰ��¼�û���ҵ��Ԫ������");
		return orgCode;
	}
	
	/**
	 * ��ȡ��ǰ��¼�û�����ҵ��Ԫ����
	 * @author taoye 2013-7-15
	 */
	public static String getCntOrgName() {
		CpOrgVO orgVo = getCntOrgVo();
		String orgName = orgVo.getName();
		if(orgVo == null) 
			ScapLogger.error("��¼�쳣��û�д�Session�л�ȡ����ǰ��¼�û���ҵ��Ԫ������");
		return orgName;
	}
	
	/**
	 * ��ȡ��ǰ��¼�û�����ҵ��ԪVo
	 * @author taoye 2013-7-15
	 */
	public static CpOrgVO getCntOrgVo() {
		String pkOrg = getCntOrgPk();
		CpOrgVO orgVo = CpOrgUtil.getOrgVoByPk(pkOrg);
		if(orgVo == null) 
			ScapLogger.error("��¼�쳣��û�д�Session�л�ȡ����ǰ��¼�û���ҵ��ԪVO��");
		return orgVo;
	}
	
	/**
	 * ��ȡ��ǰ��¼�û�������������
	 * @author taoye 2013-7-15
	 */
	public static String getCntDeptPk() {
		String pkDept = LfwRuntimeEnvironment.getLfwSessionBean().getPk_dept();
		if(pkDept == null || pkDept.equals("")) 
			ScapLogger.debug("����Ϊ" + getCntUserPk() + "������Ϊ" + getCntUserCode() + "�ĵ�ǰ��¼�û��޲�����Ϣ��");
		return pkDept;
	}
	
	/**
	 * ��ȡ��ǰ��¼�û��������ű���
	 * @author taoye 2013-7-15
	 */
	public static String getCntDeptCode() {
		CpOrgVO deptVo = getCntDeptVo();
		String deptCode = deptVo.getCode();
		return deptCode;
	}	
	
	/**
	 * ��ȡ��ǰ��¼�û�������������
	 * @author taoye 2013-7-15
	 */
	public static String getCntDeptName() {
		CpOrgVO deptVo = getCntDeptVo();
		String deptName = deptVo.getName();
		return deptName;
	}
	
	/**
	 * ��ȡ��ǰ��¼�û���������Vo
	 * @author taoye 2013-7-15
	 */
	public static CpOrgVO getCntDeptVo() {
		String pkDept = getCntDeptPk();
		CpOrgVO deptVo = CpOrgUtil.getOrgVoByPk(pkDept);
		if(deptVo == null) 
			ScapLogger.debug("���ݲ�������" + pkDept + "��ѯ���������");
		return deptVo;
	}
	/**
	 * ��ȡ��ǰ��¼�û���def12
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
