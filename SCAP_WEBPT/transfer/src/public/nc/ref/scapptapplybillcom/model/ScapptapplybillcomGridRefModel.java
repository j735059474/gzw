package nc.ref.scapptapplybillcom.model;

import uap.lfw.ref.model.LfwGridRefModel;
import nc.ref.scapptapplybillcom.control.ScapptapplybillcomGridRefController;
import uap.lfw.ref.vo.LfwRefGroupVO;

public class ScapptapplybillcomGridRefModel extends LfwGridRefModel {
	
	@Override
	public String getControllerClazzName() {
		return ScapptapplybillcomGridRefController.class.getName();
	}

	@Override
	public LfwRefGroupVO getGroupVO() {
		return new LfwRefGroupVO() {
		
			/*主键字段*/
			@Override
			public String getPkField() {
				return "pk_borg";
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
			/*数据集显示字段集合*/
			@Override
			public String[] getFieldCodes() {
//				return new String[]{"pk_apply_b","pk_borg","pk_borg_vname","dbpercent","vbdef1","vbdef2","vbdef3","vbdef4","vbdef5","status","dr","ts","pk_apply_h"};
				return new String[]{"pk_borg_vname"};
			}
			/*数据集显示字段名称集合*/
			@Override
			public String[] getFieldNames() {
//				return new String[]{"主键","标的企业","标的企业","拟转让股比(%)","自定义字段1","自定义字段2","自定义字段3","自定义字段4","自定义字段5","vostatus","dr","ts","上层单据主键"};
				return new String[]{"标的企业"};
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