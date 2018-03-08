package nc.ref.librarycode.model;

import uap.lfw.ref.model.LfwGridRefModel;
import nc.ref.librarycode.control.LibrarycodeGridRefController;
import uap.lfw.ref.vo.LfwRefGroupVO;

public class LibrarycodeGridRefModel extends LfwGridRefModel {
	
	@Override
	public String getControllerClazzName() {
		return LibrarycodeGridRefController.class.getName();
	}

	@Override
	public LfwRefGroupVO getGroupVO() {
		return new LfwRefGroupVO() {
		
			/*�����ֶ�*/
			@Override
			public String getPkField() {
				return "pk_library";
			}
			/*�����ֶ�*/
			@Override
			public String getCodeField() {
				return "pk_library";
			}
			/*�����ֶ�*/
			@Override
			public String getNameField() {
				return "vname";
			}
			/*���ݼ���ʾ�ֶμ���*/
			@Override
			public String[] getFieldCodes() {
//				return new String[]{"pk_library","pk_org","pk_org_name","pk_group","pk_group_name","vname","vaddress","vheader","vphone","vrange","vwebsite","vagency","vcontrolphone","def1","def2","def3","def4","def5","status","dr","ts"};
				return new String[]{"pk_library","vname","vheader","vphone"};
			}
			/*���ݼ���ʾ�ֶ����Ƽ���*/
			@Override
			public String[] getFieldNames() {
//				return new String[]{"����","��֯����","��֯����","��������","��������","��������","�칫��ַ","������","��ϵ�绰","ҵ��Χ","��ַ","�϶�����","�ල�绰","�Զ�����1","�Զ�����2","�Զ�����3","�Զ�����4","�Զ�����5","vostatus","dr","ts"};
				return new String[]{"��������","������","��ϵ�绰"};
			}
			/*���ݼ������ֶμ���*/
			@Override
			public String[] getHiddenFieldCodes() {
				return new String[]{"pk_library"};
			}
			/*�����ֶμ���*/
			@Override
			public String[] getMultiLanFieldCodes() {
				return new String[]{""};
			}
			
		};
	}
}