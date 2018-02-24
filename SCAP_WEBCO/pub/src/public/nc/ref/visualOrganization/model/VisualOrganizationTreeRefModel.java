package nc.ref.visualOrganization.model;

import uap.lfw.ref.model.LfwTreeRefModel;
import nc.ref.visualOrganization.control.VisualOrganizationTreeRefController;
import uap.lfw.ref.vo.LfwTreeRefGroupVO;

public class VisualOrganizationTreeRefModel extends LfwTreeRefModel {
	
	@Override
	public String getControllerClazzName() {
		return VisualOrganizationTreeRefController.class.getName();
	}

	@Override
	public LfwTreeRefGroupVO getGroupVO() {
		return new LfwTreeRefGroupVO() {
		
			/*主键字段*/
			@Override
			public String getPkField() {
				return "pk_visualorg";
			}
			/*编码字段*/
			@Override
			public String getCodeField() {
				return "visualorg_code";
			}
			/*名称字段*/
			@Override
			public String getNameField() {
				return "visualorg_name";
			}
			/*父节点字段*/
			@Override
			public String getFatherField() {
				return "pk_parent";
			}
			/*子节点字段*/
			@Override
			public String getChildField() {
				return "pk_visualorg";
			}
			/*数据集显示字段集合*/
			@Override
			public String[] getFieldCodes() {
				return new String[]{"pk_visualorg","pk_parent","parent_name","visualorg_name","visualorg_code","visualorg_type","enablestate"};
			}
			/*多语字段集合*/
			@Override
			public String[] getMultiLanFieldCodes() {
				return new String[]{""};
			}
			
		};
	}
}