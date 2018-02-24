package nc.scap.pub.model;

import uap.web.bd.pub.AppUtil;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.CpOrgUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.model.PageModel;

public class ContactsListPageModel  extends PageModel {
	@Override
	protected void initUIMetaStruct() {
		super.initUIMetaStruct();
		String pageCode = getPageCode();
		//国资委用户时 ,list画面显示新增,修改,和删除按钮;企业用户时,list画面只显示修改按钮
		boolean flag = CpOrgUtil.isCompanyOrg(CntUtil.getCntOrgPk());
		if (!flag) {// 国资委用户
			//donothing
		} else if (flag) {//企业用户
			hideOperateButton(getQyPageHideBtn());
		}

	}

	/**
	 * 获取企业用户进入list页面时需要隐藏的按钮
	 * 
	 * @return
	 */
	protected String[] getQyPageHideBtn() {
		return new String[] { ScapCoConstants.ADD, ScapCoConstants.DEL,ScapCoConstants.STARTORSTROP};
	}
	/**
	 * 获取页面编码
	 * 
	 * @return
	 */
	protected String getPageCode() {
		return (String) LfwRuntimeEnvironment.getWebContext()
				.getOriginalParameter("pagecode");
	}
	/**
	 * 获取用户类型
	 * 
	 * @return
	 */
	protected String getUserType() {
		return (String) LfwRuntimeEnvironment.getWebContext()
				.getOriginalParameter("user_type");
	}
	/**
	 * 隐藏list页面的按钮
	 * 
	 * @param btns
	 *            隐藏按钮的名字
	 */
	protected void hideOperateButton(String... btns) {
		if (btns == null || btns.length == 0) {
			return;
		}
		MenubarComp menuBar = getPageMeta().getView("main").getViewMenus()
				.getMenuBar("menubar");
		for (String btn : btns) {
			if (menuBar.getItem(btn) != null) {
				menuBar.getItem(btn).setVisible(false);
			}
		}
	}
}
