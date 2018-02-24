package nc.ref.userpsn.model;

import uap.lfw.ref.model.LfwGridRefModel;
import nc.ref.userpsn.control.UserpsnGridRefController;
import uap.lfw.ref.vo.LfwRefGroupVO;

public class UserpsnGridRefModel extends LfwGridRefModel {
	
	@Override
	public String getControllerClazzName() {
		return UserpsnGridRefController.class.getName();
	}

	@Override
	public LfwRefGroupVO getGroupVO() {
		return new LfwRefGroupVO() {
		
			/*主键字段*/
			@Override
			public String getPkField() {
				return "cuserid";
			}
			/*编码字段*/
			@Override
			public String getCodeField() {
				return "user_code";
			}
			/*名称字段*/
			@Override
			public String getNameField() {
				return "user_name";
			}
			/*数据集显示字段集合*/
			@Override
			public String[] getFieldCodes() {
				return new String[]{"user_code","user_name","szdw","sex","zw","zc","tel","mobile","email"};
			}
			/*数据集显示字段名称集合*/
			@Override
			public String[] getFieldNames() {
				return new String[]{"用户编码","用户名称","所在单位","性别","职务","职称","联系电话","手机号码","邮箱"};//
			}
			/*数据集隐藏字段集合*/
			@Override
			public String[] getHiddenFieldCodes() {
				return new String[]{"cuserid"};
			}
			/*多语字段集合*/
			@Override
			public String[] getMultiLanFieldCodes() {
				return new String[]{""};
			}
			
		};
	}
}