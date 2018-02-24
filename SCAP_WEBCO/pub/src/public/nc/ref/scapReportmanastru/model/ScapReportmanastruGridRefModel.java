package nc.ref.scapReportmanastru.model;

import uap.lfw.ref.model.LfwGridRefModel;
import nc.ref.scapReportmanastru.control.ScapReportmanastruGridRefController;
import uap.lfw.ref.vo.LfwRefGroupVO;

public class ScapReportmanastruGridRefModel extends LfwGridRefModel {
	
	@Override
	public String getControllerClazzName() {
		return ScapReportmanastruGridRefController.class.getName();
	}

	@Override
	public LfwRefGroupVO getGroupVO() {
		return new LfwRefGroupVO() {
		
			/*�����ֶ�*/
			@Override
			public String getPkField() {
				return "pk_reportmanastru";
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
			/*���ݼ���ʾ�ֶμ���*/
			@Override
			public String[] getFieldCodes() {
				return new String[]{"code","name"};
			}
			/*���ݼ���ʾ�ֶ����Ƽ���*/
			@Override
			public String[] getFieldNames() {
				return new String[]{"����","����"};
			}
			/*���ݼ������ֶμ���*/
			@Override
			public String[] getHiddenFieldCodes() {
				return new String[]{"pk_reportmanastru"};
			}
			/*�����ֶμ���*/
			@Override
			public String[] getMultiLanFieldCodes() {
				return new String[]{""};
			}
			
		};
	}
}