package nc.scap.treansfer.applyref.model;

import uap.lfw.ref.model.LfwTreeRefModel;
import nc.scap.treansfer.applyref.control.ApplyborgnoTreeRefController;
import uap.lfw.ref.vo.LfwTreeRefGroupVO;

public class ApplyborgnoTreeRefModel extends LfwTreeRefModel {
	
	@Override
	public String getControllerClazzName() {
		return ApplyborgnoTreeRefController.class.getName();
	}

	@Override
	public LfwTreeRefGroupVO getGroupVO() {
		return new LfwTreeRefGroupVO() {
		
			/*�����ֶ�*/
			@Override
			public String getPkField() {
				return "pk_apply_b";
			}
			/*�����ֶ�*/
			@Override
			public String getCodeField() {
				return "pk_borg";
			}
			/*�����ֶ�*/
			@Override
			public String getNameField() {
				return "pk_borg_vname";
			}
			/*���ڵ��ֶ�*/
			@Override
			public String getFatherField() {
				return "";
			}
			/*�ӽڵ��ֶ�*/
			@Override
			public String getChildField() {
				return "";
			}
			/*���ݼ���ʾ�ֶμ���*/
			@Override
			public String[] getFieldCodes() {
				return new String[]{"pk_apply_b","pk_borg","pk_borg_vname","pk_apply_h"};
			}
			/*�����ֶμ���*/
			@Override
			public String[] getMultiLanFieldCodes() {
				return new String[]{""};
			}
			
		};
	}
}