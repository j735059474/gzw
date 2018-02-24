package nc.scap.ref.model;

import nc.scap.ref.control.DatatypeTreeRefController;
import uap.lfw.ref.model.LfwTreeRefModel;
import uap.lfw.ref.vo.LfwTreeRefGroupVO;

public class DatatypeTreeRefModel extends LfwTreeRefModel {
	
	@Override
	public String getControllerClazzName() {
		return DatatypeTreeRefController.class.getName();
	}

	@Override
	public LfwTreeRefGroupVO getGroupVO() {
		return new LfwTreeRefGroupVO() {
		
			/*�����ֶ�*/
			@Override
			public String getPkField() {
				return "pk_datetype_h";
			}
			/*�����ֶ�*/
			@Override
			public String getCodeField() {
				return "code";
			}
			/*�����ֶ�*/
			@Override
			public String getNameField() {
				return "name";
			}
			/*���ڵ��ֶ�*/
			@Override
			public String getFatherField() {
				return "pk_father";
			}
			/*�ӽڵ��ֶ�*/
			@Override
			public String getChildField() {
				return "pk_datetype_h";
			}
			/*���ݼ���ʾ�ֶμ���*/
			@Override
			public String[] getFieldCodes() {
//				return new String[]{"code","name"};
				return new String[]{"pk_datetype_h","pk_father","code","name"};
			}
			/*�����ֶμ���*/
			@Override
			public String[] getMultiLanFieldCodes() {
				return new String[]{""};
			}
			
		};
	}
}