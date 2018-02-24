package nc.scap.pub.selfquery.reference.model;

import nc.scap.pub.selfquery.reference.control.ReportOrgsQryRefController;
import uap.lfw.ref.model.LfwGridRefModel;
import uap.lfw.ref.vo.LfwRefGroupVO;

public class ReportOrgsQryRefModel extends LfwGridRefModel{

	@Override
	public LfwRefGroupVO getGroupVO() {

		return new LfwRefGroupVO() {
			
			/*�����ֶ�*/
			@Override
			public String getPkField() {
				return "PK_REPORTMANASTRU";
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
				return new String[]{"PK_REPORTMANASTRU"};
			}
			/*�����ֶμ���*/
			@Override
			public String[] getMultiLanFieldCodes() {
				return new String[]{""};
			}
			
		};
	}

	@Override
	public String getControllerClazzName() {
		
		return ReportOrgsQryRefController.class.getName();
	}

}

