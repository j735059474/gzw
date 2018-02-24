package nc.scap.ref.model;

import nc.scap.ref.control.DatatypeTreeRefController;
import uap.lfw.ref.model.LfwTreeRefModel;
import uap.lfw.ref.vo.LfwTreeRefGroupVO;

public class DatatypeTreeRefModel extends LfwTreeRefModel {
	
	@Override
	public String getControllerClazzName() {
		return DatatypeTreeRefController.class.getName();
	}

	@Override
	public LfwTreeRefGroupVO getGroupVO() {
		return new LfwTreeRefGroupVO() {
		
			/*主键字段*/
			@Override
			public String getPkField() {
				return "pk_datetype_h";
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
			/*父节点字段*/
			@Override
			public String getFatherField() {
				return "pk_father";
			}
			/*子节点字段*/
			@Override
			public String getChildField() {
				return "pk_datetype_h";
			}
			/*数据集显示字段集合*/
			@Override
			public String[] getFieldCodes() {
//				return new String[]{"code","name"};
				return new String[]{"pk_datetype_h","pk_father","code","name"};
			}
			/*多语字段集合*/
			@Override
			public String[] getMultiLanFieldCodes() {
				return new String[]{""};
			}
			
		};
	}
}