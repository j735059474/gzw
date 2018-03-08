package nc.scap.treansfer.applyref.model;

import uap.lfw.ref.model.LfwTreeRefModel;
import nc.scap.treansfer.applyref.control.ApplyborgnoTreeRefController;
import uap.lfw.ref.vo.LfwTreeRefGroupVO;

public class ApplyborgnoTreeRefModel extends LfwTreeRefModel {
	
	@Override
	public String getControllerClazzName() {
		return ApplyborgnoTreeRefController.class.getName();
	}

	@Override
	public LfwTreeRefGroupVO getGroupVO() {
		return new LfwTreeRefGroupVO() {
		
			/*主键字段*/
			@Override
			public String getPkField() {
				return "pk_apply_b";
			}
			/*编码字段*/
			@Override
			public String getCodeField() {
				return "pk_borg";
			}
			/*名称字段*/
			@Override
			public String getNameField() {
				return "pk_borg_vname";
			}
			/*父节点字段*/
			@Override
			public String getFatherField() {
				return "";
			}
			/*子节点字段*/
			@Override
			public String getChildField() {
				return "";
			}
			/*数据集显示字段集合*/
			@Override
			public String[] getFieldCodes() {
				return new String[]{"pk_apply_b","pk_borg","pk_borg_vname","pk_apply_h"};
			}
			/*多语字段集合*/
			@Override
			public String[] getMultiLanFieldCodes() {
				return new String[]{""};
			}
			
		};
	}
}