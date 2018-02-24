package nc.scap.pub.reference.model;

import uap.lfw.ref.model.LfwGridRefModel;
import nc.scap.pub.reference.control.ChartdsRefGridRefController;
import uap.lfw.ref.vo.LfwRefGroupVO;

public class ChartdsRefGridRefModel extends LfwGridRefModel {
	
	@Override
	public String getControllerClazzName() {
		return ChartdsRefGridRefController.class.getName();
	}

	@Override
	public LfwRefGroupVO getGroupVO() {
		return new LfwRefGroupVO() {
		
			/*主键字段*/
			@Override
			public String getPkField() {
				return "pk_chart_ds";
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
				return new String[]{"code","name","controller","table_name","source_sql"};
			}
			/*数据集显示字段名称集合*/
			@Override
			public String[] getFieldNames() {
				return new String[]{"编码","名称","控制类","来源表名","来源SQL"};
			}
			/*数据集隐藏字段集合*/
			@Override
			public String[] getHiddenFieldCodes() {
				return new String[]{"pk_chart_ds"};
			}
			/*多语字段集合*/
			@Override
			public String[] getMultiLanFieldCodes() {
				return new String[]{""};
			}
			
		};
	}
}