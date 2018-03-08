package nc.scap.treansfer.applyref.model;

import uap.lfw.ref.model.LfwGridRefModel;
import nc.scap.treansfer.applyref.control.ApplyreportnoGridRefController;
import uap.lfw.ref.vo.LfwRefGroupVO;

public class ApplyreportnoGridRefModel extends LfwGridRefModel {
	
	@Override
	public String getControllerClazzName() {
		return ApplyreportnoGridRefController.class.getName();
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
				return new String[]{"billno","pk_apply_h"};
			}
			/*���ݼ���ʾ�ֶ����Ƽ���*/
			@Override
			public String[] getFieldNames() {
				return new String[]{"���뵥��"};
			}
			/*���ݼ������ֶμ���*/
			@Override
			public String[] getHiddenFieldCodes() {
				return new String[]{"pk_apply_h"};
			}
			/*�����ֶμ���*/
			@Override
			public String[] getMultiLanFieldCodes() {
				return new String[]{""};
			}
			
		};
	}
}