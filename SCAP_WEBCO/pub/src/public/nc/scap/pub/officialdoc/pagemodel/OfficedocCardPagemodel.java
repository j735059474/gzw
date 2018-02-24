package nc.scap.pub.officialdoc.pagemodel;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.jsp.uimeta.UIElementFinder;
import nc.uap.lfw.jsp.uimeta.UIFlowvPanel;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIPanel;
import nc.uap.lfw.jsp.uimeta.UIView;

/**
 * nc.scap.pub.officialdoc.pagemodel.OfficedocCardPagemodel
 * @author zhuzhiqiang
 *
 */
public class OfficedocCardPagemodel extends PageModel {

	@Override
	protected void initUIMetaStruct() {
		// TODO 自动生成的方法存根
		LfwRuntimeEnvironment.getWebContext().getWebSession().setAttribute(PageModel.PK_TEMPLATE_DB, this.pk_template);
		super.initUIMetaStruct();
	}

}
