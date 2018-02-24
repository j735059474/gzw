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
			
			/*�����ֶ�*/
			@Override
			public String getPkField() {
				return "pk_notice_contacts";
			}
			/*�����ֶ�*/
			@Override
			public String getCodeField() {
				return "pk_notice_contacts";
			}
			/*�����ֶ�*/
			@Override
			public String getNameField() {
				return "contacts_name";
			}
			/*���ݼ���ʾ�ֶμ���*/
			@Override
			public String[] getFieldCodes() {
				return new String[]{"pk_notice_contacts","contacts_name","post","phone_no","email"};
			}
			/*���ݼ���ʾ�ֶ����Ƽ���*/
			@Override
			public String[] getFieldNames() {
				return new String[]{"��ϵ������","��ϵ������","ְ��","�ֻ���","����"};
			}
			/*���ݼ������ֶμ���*/
			@Override
			public String[] getHiddenFieldCodes() {
				return new String[]{""};
			}
			/*����ֶ�*/
			@Override
			public String getDocJoinField() {
				return "pk_org";
			}
			/*�������ֶ�*/
			@Override
			public String getClassPkField() {
				return "pk_org";
			}
			/*�������ֶ�*/
			@Override
			public String getClassCodeField() {
				return "pk_org";
			}
			/*�������ֶ�*/
			@Override
			public String getClassNameField() {
				return "name";
			}
			/*�����ڵ��ֶ�*/
			@Override
			public String getClassFatherField() {
				return "pk_fatherorg";
			}
			/*���ӽڵ��ֶ�*/
			@Override
			public String getClassChildField() {
				return "pk_org";
			}
			/*�����ݼ��ֶμ���*/
			@Override
			public String[] getClassFieldCodes() {
				return new String[]{"pk_org","code","name","pk_fatherorg"};
			}
			/*�����ֶμ���*/
			@Override
			public String[] getMultiLanFieldCodes() {
				return new String[]{""};
			}
			
		};
	}
}