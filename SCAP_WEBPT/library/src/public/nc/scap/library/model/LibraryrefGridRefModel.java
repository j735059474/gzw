package nc.scap.library.model;

import uap.lfw.ref.model.LfwGridRefModel;
import nc.scap.library.control.LibraryrefGridRefController;
import uap.lfw.ref.vo.LfwRefGroupVO;

public class LibraryrefGridRefModel extends LfwGridRefModel {
	
	@Override
	public String getControllerClazzName() {
		return LibraryrefGridRefController.class.getName();
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
				return "vname";
			}
			/*�����ֶ�*/
			@Override
			public String getNameField() {
				return "vheader";
			}
			/*���ݼ���ʾ�ֶμ���*/
			@Override
			public String[] getFieldCodes() {
				return new String[]{"pk_library","pk_org","pk_org_name","pk_group","pk_group_name","vname","vaddress","vheader","vphone","vrange","vwebsite","vagency","vcontrolphone","def1","def2","def3","def4","def5","status","dr","ts"};
			}
			/*���ݼ���ʾ�ֶ����Ƽ���*/
			@Override
			public String[] getFieldNames() {
				return new String[]{"����","��֯����","��֯����","��������","��������","��������","�칫��ַ","������","��ϵ�绰","ҵ��Χ","��ַ","�϶�����","�ල�绰","�Զ�����1","�Զ�����2","�Զ�����3","�Զ�����4","�Զ�����5","vostatus","dr","ts"};
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