package nc.scap.pub.entmatter.util;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import nc.scap.pub.util.CpCtrlUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.portal.log.ScapLogger;

/**
 * nc.scap.pub.entmatter.util.ListWinPageModel
 * @author admin
 *
 */
public class ListWinPageModel extends PageModel {
	@Override
	protected void initUIMetaStruct() {
		super.initUIMetaStruct();

		String pageCode = getPageCode();
		if ("manager".equals(pageCode)) {// 管理页面
			hideOperateButton(getManagerPageHideBtn());
		} else if ("approve".equals(pageCode)) {// 审核页面
			hideOperateButton(getApprovePageHideBtn());
		} else if ("browse".equals(pageCode)) {// 查看页面
			hideOperateButton(getBrowsePageHideBtn());
		}else if ("release".equals(pageCode)) {// 发布页面
			hideOperateButton(getReleasePageHideBtn());
		}
	    String report_user = MatterUtil.getAddressParameter("report_user");
		if (StringUtils.isNotBlank(report_user) && "qy".equals(report_user)){
			GridComp grid = (GridComp)getPageMeta().getView("main").getViewComponents().getComponent("ent_matters_grid");
			CpCtrlUtil.visiableGridColms(grid,false,new String[]{"office","office_name"});
		}
		//操作完成后的操作
		afterInitUIMetaStruct(pageCode);
	}

	protected void afterInitUIMetaStruct(String pageCode){
		if(hidePrintBtn()){
			hideOperateButton(new String[] {"print"});
		}
	}

	protected boolean hideOrgColumn(){
		return true;
	}

	protected boolean hidePrintBtn(){
		return true;
	}

	/**
	 * 获取管理页面需要隐藏的按钮
	 *
	 * @return
	 */
	protected String[] getManagerPageHideBtn() {
		return new String[] {"approve","release","unrelease"};
	}

	/**
	 * 获取审核页面需要隐藏的按钮
	 *
	 * @return
	 */
	protected String[] getApprovePageHideBtn() {
		return new String[] {"add","edit","del","attachfile","release","unrelease"};
	}

	/**
	 * 获取查看页面需要隐藏的按钮
	 *
	 * @return
	 */
	protected String[] getBrowsePageHideBtn() {
		return new String[] { "add", "edit","del","approve","release","unrelease" };
	}

	/**
	 * 获取发布页面需要隐藏的按钮
	 *
	 * @return
	 */
	protected String[] getReleasePageHideBtn() {
		return new String[] { "add", "edit","del","approve" };
	}

	/**
	 * 获取页面编码
	 *
	 * @return
	 */
	protected String getPageCode() {
		return LfwRuntimeEnvironment.getWebContext()
				.getOriginalParameter("pagecode");
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

	@Override
	protected void initPageMetaStruct() {
		super.initPageMetaStruct();
		LfwView main = getPageMeta().getView("main");
		String id = getPageMeta().getId();
		if ((id != null) && (id.indexOf("listwin") != -1)) {
			GridComp gc = getViewGridComp(main, "ent_matters_grid");
			if("release".equals(getPageCode())){
				setWcVisible(gc,"def2",true);
				setWcVisible(gc,"formstate",false);
			}else{
				setWcVisible(gc,"def2",false);
				setWcVisible(gc,"formstate",true);
			}
		}
	}

	/**
	 * 获取视图的指定表格
	 *
	 * @param viewID
	 * @param gridID
	 * @return
	 */
	public static GridComp getViewGridComp(LfwView view, String gridID) {
		GridComp mc = null;
		if (view != null) {
			mc = (GridComp) view.getViewComponents().getComponent(gridID);
		}
		return mc;
	}


	private void setWcVisible(WebComponent wc,String id,boolean flag) {
		try {
			if(wc instanceof FormComp){
				((FormComp)wc).getElementById(id).setVisible(flag);
			}else if(wc instanceof GridComp){
				((GridComp)wc).getColumnById(id).setVisible(flag);
			}
		} catch (Exception e) {
			ScapLogger.debug("WebComponent 转换异常！");
			e.printStackTrace();
		}
	}

	/**
	 * 隐藏所属集团字段的表格id
	 *
	 * @return
	 */
	protected String[] getListGridIds() {
		return null;
	}

	/**
	 * 表格所属集团对应的字段名字
	 *
	 * @return
	 */
	protected String[] getOrgFieldNames() {
		return new String[] { "pk_org_name" };
	}

	@Override
	public String getEtag(){
		String s = UUID.randomUUID().toString();
		return s.substring(0, 8);
	}
}
