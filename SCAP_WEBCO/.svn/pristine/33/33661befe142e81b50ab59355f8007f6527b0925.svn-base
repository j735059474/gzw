package nc.scap.ref.model;

import uap.lfw.ref.model.LfwGridRefModel;
import nc.scap.ref.control.ReportTypeGridRefController;
import uap.lfw.ref.vo.LfwRefGroupVO;

public class ReportTypeGridRefModel extends LfwGridRefModel {
	
	@Override
	public String getControllerClazzName() {
		return ReportTypeGridRefController.class.getName();
	}

	@Override
	public LfwRefGroupVO getGroupVO() {
		return new LfwRefGroupVO() {
		
			/*主键字段*/
			@Override
			public String getPkField() {
				return "pk_work_report_type";
			}
			/*编码字段*/
			@Override
			public String getCodeField() {
				return "report_code";
			}
			/*名称字段*/
			@Override
			public String getNameField() {
				return "report_type";
			}
			/*数据集显示字段集合*/
			@Override
			public String[] getFieldCodes() {
				return new String[]{"pk_work_report_type","report_type","report_code"};
			}
			/*数据集显示字段名称集合*/
			@Override
			public String[] getFieldNames() {
				return new String[]{"报告类型","报告编码"};
			}
			/*数据集隐藏字段集合*/
			@Override
			public String[] getHiddenFieldCodes() {
				return new String[]{"pk_work_report_type"};
			}
			/*多语字段集合*/
			@Override
			public String[] getMultiLanFieldCodes() {
				return new String[]{""};
			}
			
		};
	}
}