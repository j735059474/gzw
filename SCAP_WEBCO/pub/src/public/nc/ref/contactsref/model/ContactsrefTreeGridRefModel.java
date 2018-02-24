package nc.ref.contactsref.model;

import uap.lfw.ref.model.LfwTreeGridRefModel;
import nc.ref.contactsref.control.ContactsrefTreeGridRefController;
import uap.lfw.ref.vo.LfwTreeGridRefGroupVO;

public class ContactsrefTreeGridRefModel extends LfwTreeGridRefModel {
	
	@Override
	public String getControllerClazzName() {
		return ContactsrefTreeGridRefController.class.getName();
	}

	@Override
	public LfwTreeGridRefGroupVO getGroupVO() {
		return new LfwTreeGridRefGroupVO() {
			
			/*主键字段*/
			@Override
			public String getPkField() {
				return "pk_notice_contacts";
			}
			/*编码字段*/
			@Override
			public String getCodeField() {
				return "pk_notice_contacts";
			}
			/*名称字段*/
			@Override
			public String getNameField() {
				return "contacts_name";
			}
			/*数据集显示字段集合*/
			@Override
			public String[] getFieldCodes() {
				return new String[]{"pk_notice_contacts","contacts_name","post","phone_no","email"};
			}
			/*数据集显示字段名称集合*/
			@Override
			public String[] getFieldNames() {
				return new String[]{"联系人主键","联系人姓名","职务","手机号","邮箱"};
			}
			/*数据集隐藏字段集合*/
			@Override
			public String[] getHiddenFieldCodes() {
				return new String[]{""};
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
				return "pk_org";
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
				return "pk_org";
			}
			/*树数据集字段集合*/
			@Override
			public String[] getClassFieldCodes() {
				return new String[]{"pk_org","code","name","pk_fatherorg"};
			}
			/*多语字段集合*/
			@Override
			public String[] getMultiLanFieldCodes() {
				return new String[]{""};
			}
			
		};
	}
}