package nc.ref.scappt.model;

import uap.lfw.ref.model.LfwGridRefModel;
import nc.ref.scappt.control.ScapptGridRefController;
import uap.lfw.ref.vo.LfwRefGroupVO;

public class ScapptGridRefModel extends LfwGridRefModel {
	
	@Override
	public String getControllerClazzName() {
		return ScapptGridRefController.class.getName();
	}

	@Override
	public LfwRefGroupVO getGroupVO() {
		return new LfwRefGroupVO() {
		
			/*主键字段*/
			@Override
			public String getPkField() {
				return "pk_transfer_h";
			}
			/*编码字段*/
			@Override
			public String getCodeField() {
				return "pk_transfer_h";
			}
			/*名称字段*/
			@Override
			public String getNameField() {
				return "billno";
			}
			/*数据集显示字段集合*/
			@Override
			public String[] getFieldCodes() {
				return new String[]{"pk_transfer_h","billno"};
			}
			/*数据集显示字段名称集合*/
			@Override
			public String[] getFieldNames() {
				return new String[]{"主键","单据号"};
			}
			/*数据集隐藏字段集合*/
			@Override
			public String[] getHiddenFieldCodes() {
				return new String[]{""};
			}
			/*多语字段集合*/
			@Override
			public String[] getMultiLanFieldCodes() {
				return new String[]{""};
			}
			
		};
	}
}