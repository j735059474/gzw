package nc.scap.librarymodel;

import uap.lfw.ref.model.LfwGridRefModel;
import nc.scap.librarycontrol.ScapptattachrefmodelGridRefController;
import uap.lfw.ref.vo.LfwRefGroupVO;

public class ScapptattachrefmodelGridRefModel extends LfwGridRefModel {
	
	@Override
	public String getControllerClazzName() {
		return ScapptattachrefmodelGridRefController.class.getName();
	}

	@Override
	public LfwRefGroupVO getGroupVO() {
		return new LfwRefGroupVO() {
		
			/*主键字段*/
			@Override
			public String getPkField() {
				return "pk_attchfile";
			}
			/*编码字段*/
			@Override
			public String getCodeField() {
				return "vcode";
			}
			/*名称字段*/
			@Override
			public String getNameField() {
				return "vname";
			}
			/*数据集显示字段集合*/
			@Override
			public String[] getFieldCodes() {
				return new String[]{"pk_attchfile","v_num","vname","vcode","ifqy","ifgzw","ifqynull","ifgzwnull","vdef1","vdef2","vdef3","vdef4","vdef5","id_entity","status","dr","ts"};
			}
			/*数据集显示字段名称集合*/
			@Override
			public String[] getFieldNames() {
				return new String[]{"主键","序号","规则名称","规则编码","企业端","国资委端","企业为空","国资委为空","自定义项1","自定义项2","自定义项3","自定义项4","自定义项5","上层单据主键","vostatus","dr","ts"};
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