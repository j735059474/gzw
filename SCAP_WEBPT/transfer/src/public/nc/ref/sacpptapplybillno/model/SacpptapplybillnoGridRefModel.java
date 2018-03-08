package nc.ref.sacpptapplybillno.model;

import uap.lfw.ref.model.LfwGridRefModel;
import nc.ref.sacpptapplybillno.control.SacpptapplybillnoGridRefController;
import uap.lfw.ref.vo.LfwRefGroupVO;

public class SacpptapplybillnoGridRefModel extends LfwGridRefModel {
	
	@Override
	public String getControllerClazzName() {
		return SacpptapplybillnoGridRefController.class.getName();
	}

	@Override
	public LfwRefGroupVO getGroupVO() {
		return new LfwRefGroupVO() {
		
			/*�����ֶ�*/
			@Override
			public String getPkField() {
				return "pk_apply_h";
			}
			/*�����ֶ�*/
			@Override
			public String getCodeField() {
				return "billno";
			}
			/*�����ֶ�*/
			@Override
			public String getNameField() {
				return "billno";
			}
			/*���ݼ���ʾ�ֶμ���*/
			@Override
			public String[] getFieldCodes() {
//				return new String[]{"pk_apply_h","pk_org","pk_org_name","pk_group","pk_group_name","billmaker","billmaker_user_name","billmakedate","billno","approver","approver_user_name","approvedate","formstate","formtitle","vrequestno","cprocesstype","cprocesstype_name","cchangetype","cchangetype_name","ctradetype","ctradetype_name","cgreattype","cgreattype_name","pk_rorg","vapproveno","capplicant","capplicant_user_name","capplicantdate","vdef1","vdef2","vdef3","vdef4","vdef5","vdef6","vdef7","vdef8","vdef9","vdef10","status","dr","ts"};
				return new String[]{"billno"};
			}
			/*���ݼ���ʾ�ֶ����Ƽ���*/
			@Override
			public String[] getFieldNames() {
//				return new String[]{"����","��֯����","��֯����","��������","��������","�Ƶ���","�Ƶ���","�Ƶ�����","���ݺ�","�����","�����","�������","����״̬","���ݱ���","���뵥��","������������","������������","��Ȩ�䶯����","��Ȩ�䶯����","��Ȩ���׷�ʽ","��Ȩ���׷�ʽ","�ش��ʲ����÷�ʽ","�ش��ʲ����÷�ʽ","ת�÷�","��׼�ĺ�","������","������","��������","�Զ����ֶ�1","�Զ����ֶ�2","�Զ����ֶ�3","�Զ����ֶ�4","�Զ����ֶ�5","�Զ����ֶ�6","�Զ����ֶ�7","�Զ����ֶ�8","�Զ����ֶ�9","�Զ����ֶ�10","vostatus","dr","ts"};
				return new String[]{"���뵥��"};
			}
			/*���ݼ������ֶμ���*/
			@Override
			public String[] getHiddenFieldCodes() {
				return new String[]{""};
			}
			/*�����ֶμ���*/
			@Override
			public String[] getMultiLanFieldCodes() {
				return new String[]{""};
			}
			
		};
	}
}