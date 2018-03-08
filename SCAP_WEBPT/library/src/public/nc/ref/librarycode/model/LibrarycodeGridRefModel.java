package nc.ref.librarycode.model;

import uap.lfw.ref.model.LfwGridRefModel;
import nc.ref.librarycode.control.LibrarycodeGridRefController;
import uap.lfw.ref.vo.LfwRefGroupVO;

public class LibrarycodeGridRefModel extends LfwGridRefModel {
	
	@Override
	public String getControllerClazzName() {
		return LibrarycodeGridRefController.class.getName();
	}

	@Override
	public LfwRefGroupVO getGroupVO() {
		return new LfwRefGroupVO() {
		
			/*主键字段*/
			@Override
			public String getPkField() {
				return "pk_library";
			}
			/*编码字段*/
			@Override
			public String getCodeField() {
				return "pk_library";
			}
			/*名称字段*/
			@Override
			public String getNameField() {
				return "vname";
			}
			/*数据集显示字段集合*/
			@Override
			public String[] getFieldCodes() {
//				return new String[]{"pk_library","pk_org","pk_org_name","pk_group","pk_group_name","vname","vaddress","vheader","vphone","vrange","vwebsite","vagency","vcontrolphone","def1","def2","def3","def4","def5","status","dr","ts"};
				return new String[]{"pk_library","vname","vheader","vphone"};
			}
			/*数据集显示字段名称集合*/
			@Override
			public String[] getFieldNames() {
//				return new String[]{"主键","组织主键","组织主键","集团主键","集团主键","机构名称","办公地址","负责人","联系电话","业务范围","网址","认定机构","监督电话","自定义项1","自定义项2","自定义项3","自定义项4","自定义项5","vostatus","dr","ts"};
				return new String[]{"机构名称","负责人","联系电话"};
			}
			/*数据集隐藏字段集合*/
			@Override
			public String[] getHiddenFieldCodes() {
				return new String[]{"pk_library"};
			}
			/*多语字段集合*/
			@Override
			public String[] getMultiLanFieldCodes() {
				return new String[]{""};
			}
			
		};
	}
}