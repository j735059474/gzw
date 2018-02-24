package nc.scap.pub.selfquery.reference.model;

import nc.scap.pub.selfquery.reference.control.ReportOrgsQryRefController;
import uap.lfw.ref.model.LfwGridRefModel;
import uap.lfw.ref.vo.LfwRefGroupVO;

public class ReportOrgsQryRefModel extends LfwGridRefModel{

	@Override
	public LfwRefGroupVO getGroupVO() {

		return new LfwRefGroupVO() {
			
			/*主键字段*/
			@Override
			public String getPkField() {
				return "PK_REPORTMANASTRU";
			}
			/*编码字段*/
			@Override
			public String getCodeField() {
				return "code";
			}
			/*名称字段*/
			@Override
			public String getNameField() {
				return "name";
			}
			/*数据集显示字段集合*/
			@Override
			public String[] getFieldCodes() {
				return new String[]{"code","name"};
			}
			/*数据集显示字段名称集合*/
			@Override
			public String[] getFieldNames() {
				return new String[]{"编码","名称"};
			}
			/*数据集隐藏字段集合*/
			@Override
			public String[] getHiddenFieldCodes() {
				return new String[]{"PK_REPORTMANASTRU"};
			}
			/*多语字段集合*/
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

