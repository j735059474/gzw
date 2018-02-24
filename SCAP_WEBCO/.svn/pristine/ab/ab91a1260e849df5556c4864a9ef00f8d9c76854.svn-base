package nc.ref.noticemng.model;

import uap.lfw.ref.model.LfwGridRefModel;
import nc.ref.noticemng.control.NoticemngGridRefController;
import uap.lfw.ref.vo.LfwRefGroupVO;

public class NoticemngGridRefModel extends LfwGridRefModel {
	
	@Override
	public String getControllerClazzName() {
		return NoticemngGridRefController.class.getName();
	}

	@Override
	public LfwRefGroupVO getGroupVO() {
		return new LfwRefGroupVO() {
		
			/*主键字段*/
			@Override
			public String getPkField() {
				return "pk_notice";
			}
			/*编码字段*/
			@Override
			public String getCodeField() {
				return "notice_no";
			}
			/*名称字段*/
			@Override
			public String getNameField() {
				return "notice_title";
			}
			/*数据集显示字段集合*/
			@Override
			public String[] getFieldCodes() {
				return new String[]{"notice_no","notice_title","notice_content"};
			}
			/*数据集显示字段名称集合*/
			@Override
			public String[] getFieldNames() {
				return new String[]{"通知编号","通知标题","通知内容"};
			}
			/*数据集隐藏字段集合*/
			@Override
			public String[] getHiddenFieldCodes() {
				return new String[]{"pk_notice"};
			}
			/*多语字段集合*/
			@Override
			public String[] getMultiLanFieldCodes() {
				return new String[]{""};
			}
			
		};
	}
}