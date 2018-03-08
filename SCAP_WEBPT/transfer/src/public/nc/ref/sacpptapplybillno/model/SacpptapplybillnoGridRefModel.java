package nc.ref.sacpptapplybillno.model;

import uap.lfw.ref.model.LfwGridRefModel;
import nc.ref.sacpptapplybillno.control.SacpptapplybillnoGridRefController;
import uap.lfw.ref.vo.LfwRefGroupVO;

public class SacpptapplybillnoGridRefModel extends LfwGridRefModel {
	
	@Override
	public String getControllerClazzName() {
		return SacpptapplybillnoGridRefController.class.getName();
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
//				return new String[]{"pk_apply_h","pk_org","pk_org_name","pk_group","pk_group_name","billmaker","billmaker_user_name","billmakedate","billno","approver","approver_user_name","approvedate","formstate","formtitle","vrequestno","cprocesstype","cprocesstype_name","cchangetype","cchangetype_name","ctradetype","ctradetype_name","cgreattype","cgreattype_name","pk_rorg","vapproveno","capplicant","capplicant_user_name","capplicantdate","vdef1","vdef2","vdef3","vdef4","vdef5","vdef6","vdef7","vdef8","vdef9","vdef10","status","dr","ts"};
				return new String[]{"billno"};
			}
			/*数据集显示字段名称集合*/
			@Override
			public String[] getFieldNames() {
//				return new String[]{"主键","组织主键","组织主键","集团主键","集团主键","制单人","制单人","制单日期","单据号","审核人","审核人","审核日期","单据状态","单据标题","申请单号","审批流程类型","审批流程类型","产权变动类型","产权变动类型","产权交易方式","产权交易方式","重大资产处置方式","重大资产处置方式","转让方","批准文号","申请人","申请人","申请日期","自定义字段1","自定义字段2","自定义字段3","自定义字段4","自定义字段5","自定义字段6","自定义字段7","自定义字段8","自定义字段9","自定义字段10","vostatus","dr","ts"};
				return new String[]{"申请单号"};
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