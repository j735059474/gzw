package nc.scap.pub.reference.model;

import uap.lfw.ref.model.LfwTreeGridRefModel;
import nc.scap.pub.reference.control.CpOrgUserTreeGridRefController;
import nc.vo.ml.NCLangRes4VoTransl;
import uap.lfw.ref.vo.LfwTreeGridRefGroupVO;

public class CpOrgUserTreeGridRefModel extends LfwTreeGridRefModel {
	
	@Override
	public String getControllerClazzName() {
		return CpOrgUserTreeGridRefController.class.getName();
	}

	@Override
	public LfwTreeGridRefGroupVO getGroupVO() {
		return new LfwTreeGridRefGroupVO() {
			
			@Override
			public String getPkField() {
				return "cuserid";
			}
			
			@Override
			public String getNameField() {
				return "user_name";
			}
			
			@Override
			public String[] getFieldNames() {
				return new String[]{ NCLangRes4VoTransl.getNCLangRes().getStrByID("bd", "CpUserTreeGridNCRefModel-000003")/*用户编码*/, NCLangRes4VoTransl.getNCLangRes().getStrByID("bd", "CpUserTreeGridNCRefModel-000004")/*用户名称*/};
			}
			
			@Override
			public String getCodeField() {
				return "user_code";
			}
			
			@Override
			public String getDocJoinField() {
				return "pk_org";
			}
			
			@Override
			public String getClassPkField() {
				return "pk_org";
			}
			
			@Override
			public String getClassNameField() {
				return "name";
			}
			
			@Override
			public String getClassFatherField() {
				return "pk_fatherorg";
			}
			
			@Override
			public String getClassCodeField() {
				return "code";
			}
			
			@Override
			public String getClassChildField() {
				return "pk_org";
			}
		};
	}
}