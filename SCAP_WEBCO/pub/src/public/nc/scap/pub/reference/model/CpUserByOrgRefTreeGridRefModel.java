package nc.scap.pub.reference.model;

import uap.lfw.ref.model.LfwTreeGridRefModel;
import nc.scap.pub.reference.control.CpUserByOrgRefTreeGridRefController;
import uap.lfw.ref.vo.LfwTreeGridRefGroupVO;

public class CpUserByOrgRefTreeGridRefModel extends LfwTreeGridRefModel {
	
	@Override
	public String getControllerClazzName() {
		return CpUserByOrgRefTreeGridRefController.class.getName();
	}

	@Override
	public LfwTreeGridRefGroupVO getGroupVO() {
		return new LfwTreeGridRefGroupVO() {
			
			/*主键字段*/
			@Override
			public String getPkField() {
				return "cuserid";
			}
			/*编码字段*/
			@Override
			public String getCodeField() {
				return "user_code";
			}
			/*名称字段*/
			@Override
			public String getNameField() {
				return "user_name";
			}
			/*数据集显示字段集合*/
			@Override
			public String[] getFieldCodes() {
				return new String[]{"user_code","user_name","user_password","user_note","pwdlevelcode","pwdparam","identityverifycode","abledate","disabledate","islocked","pk_base_doc","pk_base_doc_name","base_doc_type","pk_org","pk_org_name","pk_group","pk_group_name","creator","creator_user_name","creationtime","modifier","modifier_user_name","modifiedtime","user_type","pk_usergroupforcreate","pk_usergroupforcreate_group_name","format","format_name","isca","enablestate","contentlang","contentlang_dislpayname","user_code_q","dataoriginflag","passwordtrytimes","isasyntonc","ncpk","original","usersigniconfilepk","pk_dept","pk_dept_name","status","dr","ts"};
			}
			/*数据集显示字段名称集合*/
			@Override
			public String[] getFieldNames() {
				return new String[]{"用户编码","用户名称","用户密码","备注","密码安全级别编码","密码参数","认证类型","生效日期","失效日期","锁定","身份","身份","身份类型","所属组织","所属组织","所属集团","所属集团","创建人","创建人","创建时间","最后修改人","最后修改人","最后修改时间","用户类型","所属用户组","所属用户组","数据格式","数据格式","CA用户","启用状态","内容语种","内容语种","用户编码（查询）","分布式","尝试次数","是否同步到NC","nc主键","来源","签名图片","部门","部门","vostatus","dr","ts"};
			}
			/*数据集隐藏字段集合*/
			@Override
			public String[] getHiddenFieldCodes() {
				return new String[]{"cuserid",""};
			}
			/*外键字段*/
			@Override
			public String getDocJoinField() {
				return "pk_org";
			}
			/*树主键字段*/
			@Override
			public String getClassPkField() {
				return "pk_org";
			}
			/*树编码字段*/
			@Override
			public String getClassCodeField() {
				return "code";
			}
			/*树名称字段*/
			@Override
			public String getClassNameField() {
				return "name";
			}
			/*树父节点字段*/
			@Override
			public String getClassFatherField() {
				return "pk_fatherorg";
			}
			/*树子节点字段*/
			@Override
			public String getClassChildField() {
				return "";
			}
			/*树数据集字段集合*/
			@Override
			public String[] getClassFieldCodes() {
				return new String[]{"code","name"};
			}
			/*多语字段集合*/
			@Override
			public String[] getMultiLanFieldCodes() {
				return new String[]{""};
			}
			
		};
	}
}