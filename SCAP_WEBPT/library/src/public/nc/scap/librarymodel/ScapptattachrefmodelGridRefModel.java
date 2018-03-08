package nc.scap.librarymodel;

import uap.lfw.ref.model.LfwGridRefModel;
import nc.scap.librarycontrol.ScapptattachrefmodelGridRefController;
import uap.lfw.ref.vo.LfwRefGroupVO;

public class ScapptattachrefmodelGridRefModel extends LfwGridRefModel {
	
	@Override
	public String getControllerClazzName() {
		return ScapptattachrefmodelGridRefController.class.getName();
	}

	@Override
	public LfwRefGroupVO getGroupVO() {
		return new LfwRefGroupVO() {
		
			/*�����ֶ�*/
			@Override
			public String getPkField() {
				return "pk_attchfile";
			}
			/*�����ֶ�*/
			@Override
			public String getCodeField() {
				return "vcode";
			}
			/*�����ֶ�*/
			@Override
			public String getNameField() {
				return "vname";
			}
			/*���ݼ���ʾ�ֶμ���*/
			@Override
			public String[] getFieldCodes() {
				return new String[]{"pk_attchfile","v_num","vname","vcode","ifqy","ifgzw","ifqynull","ifgzwnull","vdef1","vdef2","vdef3","vdef4","vdef5","id_entity","status","dr","ts"};
			}
			/*���ݼ���ʾ�ֶ����Ƽ���*/
			@Override
			public String[] getFieldNames() {
				return new String[]{"����","���","��������","�������","��ҵ��","����ί��","��ҵΪ��","����ίΪ��","�Զ�����1","�Զ�����2","�Զ�����3","�Զ�����4","�Զ�����5","�ϲ㵥������","vostatus","dr","ts"};
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