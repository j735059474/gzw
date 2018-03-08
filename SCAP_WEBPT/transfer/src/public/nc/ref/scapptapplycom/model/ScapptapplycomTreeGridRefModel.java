package nc.ref.scapptapplycom.model;

import uap.lfw.ref.model.LfwTreeGridRefModel;
import nc.ref.scapptapplycom.control.ScapptapplycomTreeGridRefController;
import uap.lfw.ref.vo.LfwTreeGridRefGroupVO;

public class ScapptapplycomTreeGridRefModel extends LfwTreeGridRefModel {
	
	@Override
	public String getControllerClazzName() {
		return ScapptapplycomTreeGridRefController.class.getName();
	}

	@Override
	public LfwTreeGridRefGroupVO getGroupVO() {
		return new LfwTreeGridRefGroupVO() {
			
			/*主键字段*/
			@Override
			public String getPkField() {
				return "pk_apply_b";
			}
			/*编码字段*/
			@Override
			public String getCodeField() {
				return "pk_apply_b";
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
			/*外键字段*/
			@Override
			public String getDocJoinField() {
				return "pk_apply_h";
			}
			/*树主键字段*/
			@Override
			public String getClassPkField() {
				return "pk_apply_h";
			}
			/*树编码字段*/
			@Override
			public String getClassCodeField() {
				return "pk_apply_h";
			}
			/*树名称字段*/
			@Override
			public String getClassNameField() {
				return "billno";
			}
			/*树父节点字段*/
			@Override
			public String getClassFatherField() {
				return "";
			}
			/*树子节点字段*/
			@Override
			public String getClassChildField() {
				return "";
			}
			/*树数据集字段集合*/
			@Override
			public String[] getClassFieldCodes() {
				return new String[]{"pk_apply_h","pk_org","pk_org_name","pk_group","pk_group_name","billmaker","billmaker_user_name","billmakedate","billno","approver","approver_user_name","approvedate","formstate","formtitle","vrequestno","cprocesstype","cprocesstype_name","cchangetype","cchangetype_name","ctradetype","ctradetype_name","cgreattype","cgreattype_name","pk_rorg","vapproveno","capplicant","capplicantdate","vdef1","vdef2","vdef3","vdef4","vdef5","vdef6","vdef7","vdef8","vdef9","vdef10","status","dr","ts"};
			}
			/*多语字段集合*/
			@Override
			public String[] getMultiLanFieldCodes() {
				return new String[]{""};
			}
			
		};
	}
}