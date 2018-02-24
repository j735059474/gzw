package nc.scap.pub.meeting.pagemodel;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.model.PageModel;

public class MeetingCardPagemodel extends PageModel {
	
	@Override
	protected void initUIMetaStruct() {
		// TODO 自动生成的方法存根
		LfwRuntimeEnvironment.getWebContext().getWebSession().setAttribute(PageModel.PK_TEMPLATE_DB, this.pk_template);
		super.initUIMetaStruct();
	}
	
}
