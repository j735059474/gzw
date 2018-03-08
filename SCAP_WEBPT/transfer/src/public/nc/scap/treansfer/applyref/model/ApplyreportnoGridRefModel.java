package nc.scap.treansfer.applyref.model;

import uap.lfw.ref.model.LfwGridRefModel;
import nc.scap.treansfer.applyref.control.ApplyreportnoGridRefController;
import uap.lfw.ref.vo.LfwRefGroupVO;

public class ApplyreportnoGridRefModel extends LfwGridRefModel {
	
	@Override
	public String getControllerClazzName() {
		return ApplyreportnoGridRefController.class.getName();
	}

	@Override
	public LfwRefGroupVO getGroupVO() {
		return new LfwRefGroupVO() {
		
			/*主键字段*/
			@Override
			public String getPkField() {
				return "pk_apply_h";
			}
			/*编码字段*/
			@Override
			public String getCodeField() {
				return "billno";
			}
			/*名称字段*/
			@Override
			public String getNameField() {
				return "billno";
			}
			/*数据集显示字段集合*/
			@Override
			public String[] getFieldCodes() {
				return new String[]{"billno","pk_apply_h"};
			}
			/*数据集显示字段名称集合*/
			@Override
			public String[] getFieldNames() {
				return new String[]{"申请单号"};
			}
			/*数据集隐藏字段集合*/
			@Override
			public String[] getHiddenFieldCodes() {
				return new String[]{"pk_apply_h"};
			}
			/*多语字段集合*/
			@Override
			public String[] getMultiLanFieldCodes() {
				return new String[]{""};
			}
			
		};
	}
}