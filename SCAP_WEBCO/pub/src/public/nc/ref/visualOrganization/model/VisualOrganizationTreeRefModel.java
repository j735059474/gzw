package nc.ref.visualOrganization.model;

import uap.lfw.ref.model.LfwTreeRefModel;
import nc.ref.visualOrganization.control.VisualOrganizationTreeRefController;
import uap.lfw.ref.vo.LfwTreeRefGroupVO;

public class VisualOrganizationTreeRefModel extends LfwTreeRefModel {
	
	@Override
	public String getControllerClazzName() {
		return VisualOrganizationTreeRefController.class.getName();
	}

	@Override
	public LfwTreeRefGroupVO getGroupVO() {
		return new LfwTreeRefGroupVO() {
		
			/*�����ֶ�*/
			@Override
			public String getPkField() {
				return "pk_visualorg";
			}
			/*�����ֶ�*/
			@Override
			public String getCodeField() {
				return "visualorg_code";
			}
			/*�����ֶ�*/
			@Override
			public String getNameField() {
				return "visualorg_name";
			}
			/*���ڵ��ֶ�*/
			@Override
			public String getFatherField() {
				return "pk_parent";
			}
			/*�ӽڵ��ֶ�*/
			@Override
			public String getChildField() {
				return "pk_visualorg";
			}
			/*���ݼ���ʾ�ֶμ���*/
			@Override
			public String[] getFieldCodes() {
				return new String[]{"pk_visualorg","pk_parent","parent_name","visualorg_name","visualorg_code","visualorg_type","enablestate"};
			}
			/*�����ֶμ���*/
			@Override
			public String[] getMultiLanFieldCodes() {
				return new String[]{""};
			}
			
		};
	}
}