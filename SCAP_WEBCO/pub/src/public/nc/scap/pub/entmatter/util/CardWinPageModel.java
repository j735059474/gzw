package nc.scap.pub.entmatter.util;

import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import nc.scap.pub.entmatter.operate.MatterOperate;
import nc.scap.pub.util.CpCtrlUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.ViewComponents;
import nc.uap.lfw.jsp.uimeta.UIElementFinder;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UISplitter;
import nc.uap.lfw.jsp.uimeta.UIView;

/**
 * nc.scap.pub.entmatter.util.CardWinPageModel
 * @author admin
 *
 */
public class CardWinPageModel extends PageModel {
	@Override
	protected void initUIMetaStruct() {
		super.initUIMetaStruct();
		String pro_action = getOperateAction();
		if (pro_action != null && !pro_action.isEmpty()){
			// 隐藏菜单栏按钮
			Class<? extends CardWinPageModel> clazz = this.getClass();
			try {
				Method method = clazz.getMethod(pro_action + "Action");
				if (method != null) {
					method.invoke(this);
				}
			} catch (Exception e) {
				throw new LfwRuntimeException("页面出错!");
			}
			/*if("add".equals(pro_action)){
				this.addAction();
			}else if("edit".equals(pro_action)){
				this.editAction();
			}else if("approve".equals(pro_action)){
				this.approveAction();
			}else if("detail".equals(pro_action)){
				this.detailAction();
			}*/
			
			// 隐藏子表按钮
			if(isHideGridsButtons(pro_action)){
				hideAllGridBtns();
			}
			// 隐藏附件管理按钮
			if (isHideWfAttachFileBtn()) {
				hideAuditButton("link_addattach");
			}
		}
		// 按钮隐藏后的操作
		afterInitUIMetaStruct(pro_action);
	}

	/**
	 * 详细信息查看操作
	 */
	public void detailAction() {
		// 隐藏保存按钮
		hideOperateButton(getDetailHideBtns());
		// 隐藏审批流按钮和审批界面
		removeAuditUI();
		// 隐藏一些审批按钮
		hideAuditButton("btn_save", "link_addattach","btn_ok","reject");
		
	}

	/**
	 * 新增操作
	 */
	public void addAction() {
		// 隐藏审批页面
		removeAuditUI();
		// 隐藏一些审批按钮
		hideAuditButton("btn_save", "link_addattach", "allFlow");
	}

	/**
	 * 修改操作
	 */
	public void editAction() {
		// 隐藏审批页面
		removeAuditUI();
		// 隐藏一些审批按钮
		hideAuditButton("btn_save", "link_addattach", "allFlow");
	}

	/**
	 * 审核操作
	 */
	public void approveAction() {
		// 隐藏保存按钮
		hideOperateButton(getApproveHideBtns());
		// 隐藏暂存按钮
		hideAuditButton("btn_save");
	}

	/**
	 * 是否隐藏流程的附件管理
	 * 
	 * @return
	 */
	protected boolean isHideWfAttachFileBtn() {
		return true;
	}

	/**
	 * 隐藏卡片页面字表的操作按钮
	 * 
	 * @param pro_action
	 */
	protected boolean isHideGridsButtons(String pro_action) {
		return "approve".equals(pro_action)
				|| "detail".equals(pro_action);
	}
	
	/**
	 * 隐藏所有子表的菜单按钮
	 */
	protected void hideAllGridBtns(){
		String[] gridIds = this.getSlaveGridIds();
		if (gridIds == null || gridIds.length == 0) {
			return;
		}
		for (String gridId : gridIds) {
			// 隐藏子表的按钮
			GridComp grid = (GridComp) getPageMeta().getView("main")
					.getViewComponents().getComponent(gridId);
			if (grid == null)
				continue;
			List<MenuItem> items = grid.getMenuBar().getMenuList();
			if (items == null || items.isEmpty())
				continue;
			for (MenuItem item : items) {
				item.setVisible(false);
			}
		}
	}

	/**
	 * 获取卡片页面字表的id
	 * 
	 * @return
	 */
	protected String[] getSlaveGridIds() {
		return null;
	}

	/**
	 * InitUIMetaStruct后执行
	 * 
	 * @param pro_action
	 */
	protected void afterInitUIMetaStruct(String pro_action) {
		ViewComponents views = this.getPageMeta().getView("main").getViewComponents();
		MatterOperate matterOperate = MatterUtil.diffOperateInstance(MatterOperate.class);
		
		//表单操作
		FormComp form = (FormComp)views.getComponent("ent_matters_form");
		
		String[] eles = matterOperate.visiableEles(pro_action);
		CpCtrlUtil.visiableFormEles(form, true, eles);
		
		eles = matterOperate.editableEles(pro_action);
		CpCtrlUtil.editableFormEles(form, true, eles);
		
		eles = matterOperate.notnullableEles(pro_action);
		CpCtrlUtil.nullableFormEles(form, false, eles);
		
	}

	/**
	 * 获取详细信息查看时需要隐藏的按钮
	 * 
	 * @return
	 */
	protected String[] getDetailHideBtns() {
		return new String[] { "save" , "resetdata"};
	}

	/**
	 * 获取审核时需要隐藏的按钮
	 * 
	 * @return
	 */
	protected String[] getApproveHideBtns() {
		return new String[] { "attachfile","save" };
	}

	@Override
	protected void initPageMetaStruct() {
		super.initPageMetaStruct();
	}

	/**
	 * 获取操作码
	 * 
	 * @return
	 */
	protected String getOperateAction() {
		return (String) LfwRuntimeEnvironment.getWebContext()
				.getOriginalParameter("pro_action");
	}

	/**
	 * 隐藏卡片页面的按钮
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

	/**
	 * 去掉审批页面
	 */
	protected void removeAuditUI() {
		UIMeta uimeta = (UIMeta) this.getUIMeta();
		UIView mainView = UIElementFinder.findUIWidget(uimeta, "main");
		UISplitter split = (UISplitter) UIElementFinder.findElementById(uimeta,"mainWinSpliter");
		if (split == null)
			return;
		uimeta.removeElement(split);
		uimeta.setElement(mainView);
	}

	/**
	 * 隐藏审批按钮
	 * 
	 * @param btns
	 *            需要隐藏按钮的名字
	 */
	protected void hideAuditButton(String... btns) {
		LfwView pubview_simpleexetask = getPageMeta().getView(
				"pubview_simpleexetask");
		if (pubview_simpleexetask == null)
			return;
		MenubarComp pubbar = pubview_simpleexetask.getViewMenus().getMenuBar(
				"simpleExeMenubar");
		if (pubbar == null)
			return;
		for (String btn : btns) {
			if (pubbar.getItem(btn) != null) {
				pubbar.getItem(btn).setVisible(false);
			}
		}
	}

	/**
	 * 隐藏审批按钮
	 * 
	 * @param btns
	 *            需要隐藏按钮的名字
	 */
	protected void hideButton(String... btns) {
		LfwView pubview_simpleexetask = getPageMeta().getView(
				"pubview_simpleexetask");
		if (pubview_simpleexetask == null)
			return;
		MenubarComp pubbar = pubview_simpleexetask.getViewMenus().getMenuBar(
				"simpleExeMenubar");
		if (pubbar == null)
			return;
		List<MenuItem> list = pubbar.getMenuList();
		for (MenuItem tmp : list) {
			if (tmp.isVisible()) {
				for (String btn : btns) {
					if (tmp.getId().equals(btn)) {
						tmp.setVisible(false);
					}
				}
			}
		}

	}
	
	@Override
	public String getEtag(){
		String s = UUID.randomUUID().toString();
		return s.substring(0, 8);
	}
}
