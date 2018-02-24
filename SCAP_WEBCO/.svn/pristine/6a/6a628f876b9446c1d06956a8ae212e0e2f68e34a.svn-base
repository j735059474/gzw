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
		
			/*主键字段*/
			@Override
			public String getPkField() {
				return "pk_reportmanastru";
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
				return new String[]{"pk_reportmanastru"};
			}
			/*多语字段集合*/
			@Override
			public String[] getMultiLanFieldCodes() {
				return new String[]{""};
			}
			
		};
	}
}