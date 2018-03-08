package nc.ref.scapptapplycom.model;

import uap.lfw.ref.model.LfwTreeGridRefModel;
import nc.ref.scapptapplycom.control.ScapptapplycomTreeGridRefController;
import uap.lfw.ref.vo.LfwTreeGridRefGroupVO;

public class ScapptapplycomTreeGridRefModel extends LfwTreeGridRefModel {
	
	@Override
	public String getControllerClazzName() {
		return ScapptapplycomTreeGridRefController.class.getName();
	}

	@Override
	public LfwTreeGridRefGroupVO getGroupVO() {
		return new LfwTreeGridRefGroupVO() {
			
			/*�����ֶ�*/
			@Override
			public String getPkField() {
				return "pk_apply_b";
			}
			/*�����ֶ�*/
			@Override
			public String getCodeField() {
				return "pk_apply_b";
			}
			/*�����ֶ�*/
			@Override
			public String getNameField() {
				return "pk_borg_vname";
			}
			/*���ݼ���ʾ�ֶμ���*/
			@Override
			public String[] getFieldCodes() {
//				return new String[]{"pk_apply_b","pk_borg","pk_borg_vname","dbpercent","vbdef1","vbdef2","vbdef3","vbdef4","vbdef5","status","dr","ts","pk_apply_h"};
				return new String[]{"pk_borg_vname"};
			}
			/*���ݼ���ʾ�ֶ����Ƽ���*/
			@Override
			public String[] getFieldNames() {
//				return new String[]{"����","�����ҵ","�����ҵ","��ת�ùɱ�(%)","�Զ����ֶ�1","�Զ����ֶ�2","�Զ����ֶ�3","�Զ����ֶ�4","�Զ����ֶ�5","vostatus","dr","ts","�ϲ㵥������"};
				return new String[]{"�����ҵ"};
			}
			/*���ݼ������ֶμ���*/
			@Override
			public String[] getHiddenFieldCodes() {
				return new String[]{""};
			}
			/*����ֶ�*/
			@Override
			public String getDocJoinField() {
				return "pk_apply_h";
			}
			/*�������ֶ�*/
			@Override
			public String getClassPkField() {
				return "pk_apply_h";
			}
			/*�������ֶ�*/
			@Override
			public String getClassCodeField() {
				return "pk_apply_h";
			}
			/*�������ֶ�*/
			@Override
			public String getClassNameField() {
				return "billno";
			}
			/*�����ڵ��ֶ�*/
			@Override
			public String getClassFatherField() {
				return "";
			}
			/*���ӽڵ��ֶ�*/
			@Override
			public String getClassChildField() {
				return "";
			}
			/*�����ݼ��ֶμ���*/
			@Override
			public String[] getClassFieldCodes() {
				return new String[]{"pk_apply_h","pk_org","pk_org_name","pk_group","pk_group_name","billmaker","billmaker_user_name","billmakedate","billno","approver","approver_user_name","approvedate","formstate","formtitle","vrequestno","cprocesstype","cprocesstype_name","cchangetype","cchangetype_name","ctradetype","ctradetype_name","cgreattype","cgreattype_name","pk_rorg","vapproveno","capplicant","capplicantdate","vdef1","vdef2","vdef3","vdef4","vdef5","vdef6","vdef7","vdef8","vdef9","vdef10","status","dr","ts"};
			}
			/*�����ֶμ���*/
			@Override
			public String[] getMultiLanFieldCodes() {
				return new String[]{""};
			}
			
		};
	}
}