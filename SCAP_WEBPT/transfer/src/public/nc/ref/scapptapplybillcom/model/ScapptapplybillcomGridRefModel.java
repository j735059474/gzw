package nc.ref.scapptapplybillcom.model;

import uap.lfw.ref.model.LfwGridRefModel;
import nc.ref.scapptapplybillcom.control.ScapptapplybillcomGridRefController;
import uap.lfw.ref.vo.LfwRefGroupVO;

public class ScapptapplybillcomGridRefModel extends LfwGridRefModel {
	
	@Override
	public String getControllerClazzName() {
		return ScapptapplybillcomGridRefController.class.getName();
	}

	@Override
	public LfwRefGroupVO getGroupVO() {
		return new LfwRefGroupVO() {
		
			/*�����ֶ�*/
			@Override
			public String getPkField() {
				return "pk_borg";
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
			/*�����ֶμ���*/
			@Override
			public String[] getMultiLanFieldCodes() {
				return new String[]{""};
			}
			
		};
	}
}