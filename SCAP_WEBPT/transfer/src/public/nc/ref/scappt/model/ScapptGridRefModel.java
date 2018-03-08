package nc.ref.scappt.model;

import uap.lfw.ref.model.LfwGridRefModel;
import nc.ref.scappt.control.ScapptGridRefController;
import uap.lfw.ref.vo.LfwRefGroupVO;

public class ScapptGridRefModel extends LfwGridRefModel {
	
	@Override
	public String getControllerClazzName() {
		return ScapptGridRefController.class.getName();
	}

	@Override
	public LfwRefGroupVO getGroupVO() {
		return new LfwRefGroupVO() {
		
			/*�����ֶ�*/
			@Override
			public String getPkField() {
				return "pk_transfer_h";
			}
			/*�����ֶ�*/
			@Override
			public String getCodeField() {
				return "pk_transfer_h";
			}
			/*�����ֶ�*/
			@Override
			public String getNameField() {
				return "billno";
			}
			/*���ݼ���ʾ�ֶμ���*/
			@Override
			public String[] getFieldCodes() {
				return new String[]{"pk_transfer_h","billno"};
			}
			/*���ݼ���ʾ�ֶ����Ƽ���*/
			@Override
			public String[] getFieldNames() {
				return new String[]{"����","���ݺ�"};
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