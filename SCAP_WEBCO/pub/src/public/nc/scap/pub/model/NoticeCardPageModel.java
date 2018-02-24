package nc.scap.pub.model;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.util.CpCtrlUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.combodata.MDComboDataConf;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.ViewComponents;
import nc.uap.lfw.jsp.uimeta.UIMeta;

import org.apache.commons.lang.StringUtils;

import uap.web.bd.pub.AppUtil;

public class NoticeCardPageModel extends PageModel {
	@Override
	protected void initUIMetaStruct() {
		super.initUIMetaStruct();
		String pro_action = getOperateAction();
		if (pro_action == null || pro_action.isEmpty())
			return;
		// ���ز˵�����ť
		Class<? extends NoticeCardPageModel> clazz = this.getClass();
		try {
			Method method = clazz.getMethod(pro_action + "Action");
			if (method == null) {
				return;
			}
			method.invoke(this);
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
		UIMeta uimeta = (UIMeta) LfwRuntimeEnvironment.getWebContext()
				.getUIMeta();
		ViewComponents views = getPageMeta().getView("main").getViewComponents();
		FormComp noticeManagerForm = (FormComp)views.getComponent("notice_manager_form");
		GridComp receiveNoticeGrid = (GridComp)views.getComponent("receive_notice_grid");
		GridComp receiveManGrid = (GridComp)views.getComponent("receive_man_grid");
		String tzxf_notice_style = (String) AppUtil.getAppAttr(IGlobalConstants.TZXF_NOTICE_STYLE);
		String tzxf_message_type_code = (String) AppUtil.getAppAttr(IGlobalConstants.TZXF_MESSAGE_TYPE_CODE);
		if (StringUtils.isNotBlank(tzxf_notice_style)) {
			//�������ͨ��Ϣ֪ͨ�Ļ�������Ӧ�ֶε�����
			if (IGlobalConstants.NOTICE_STYLE_NORMAL_MESSAGE.equals(tzxf_notice_style)) {
				String[] elesNoticeManagerForm = new String[] { "business_type_name","notice_type_report_type","data_type_name",
						"urge_related","is_auto_urge","urge_info_trans_way", 
						"day_num", "urge_frequency", "urge_content_gzw","urge_content_qy",
						"remind_related","is_remind","remind_way","remind_info","expiration_date"};
				CpCtrlUtil.visiableFormEles(noticeManagerForm, false, elesNoticeManagerForm);
				CpCtrlUtil.visiableFormEles(noticeManagerForm, true, new String[]{"def4"});
				CpCtrlUtil.editableFormEles(noticeManagerForm, false, new String[] {"notice_style"});
				CpCtrlUtil.nullableFormEles(noticeManagerForm, true, new String[] {"expiration_date"});
				String[] elesReceiveNoticeGrid = new String[] { "report_status","report_time"};
				CpCtrlUtil.gridColmVisiable(receiveNoticeGrid,elesReceiveNoticeGrid,false);
				String[] elesReceiveManGrid = new String[] { "report_status","report_time"};
				CpCtrlUtil.gridColmVisiable(receiveManGrid,elesReceiveManGrid,false);
			}
		} else {
			MDComboDataConf cd = (MDComboDataConf) getPageMeta().getView("main").getViewModels().getComboData("combo_notice_manager_notice_style");
			cd.removeComboItem("3");
			// ��ʱ������Ϣ���������ֶ�
			CpCtrlUtil.visiableFormEles(noticeManagerForm, false, new String[]{"def5"});
		}
		// �����ֱ�ť
		hideSlaveGridsButtons(pro_action);
		// ���ظ�������ť
		if (isHideWfAttachFileBtn()) {
			hideAuditButton(ScapCoConstants.LINK_ADDATTACH);
		}
		// ��ť���غ�Ĳ���
		afterInitUIMetaStruct(pro_action);
	}

	@Override
	protected void initPageMetaStruct() {
		super.initPageMetaStruct();
		String node_type = LfwRuntimeEnvironment.getWebContext()
				.getOriginalParameter(ScapCoConstants.NODE_TYPE);
		// if (node_type == null)
		// node_type = (String) AppLifeCycleContext.current()
		// .getApplicationContext()
		// .getAppAttribute(SCAPBODConstants.NODE_TYPE);
		LfwView main = getPageMeta().getView("main");
		MenubarComp mc = getViewMenubarComp(main, "menubar");
		List<MenuItem> gcs = mc.getMenuList();

		if (node_type == null || node_type.length() == 0) {
			return;
		}

		String module = LfwRuntimeEnvironment.getWebContext()
				.getOriginalParameter(ScapCoConstants.MODULE_CODE);

		String method_type = LfwRuntimeEnvironment.getWebContext()
				.getOriginalParameter(IGlobalConstants.METHOD_TYPE);

		String id = getPageMeta().getId();

		if ((id != null) && (id.indexOf("listwin") != -1)) {
			if (module != null || node_type.length() != 0) {
				AppUtil.addAppAttr(ScapCoConstants.MODULE_CODE, module);
			}
			if (ScapCoConstants.SCAP_GZW.equals(node_type)) {
				AppUtil.addAppAttr(ScapCoConstants.NODE_TYPE, node_type);
				// ���ز���Ҫ�İ�ť
				for (MenuItem gc : gcs) {
					if (gc.getId().equals(ScapCoConstants.ADD)
							|| gc.getId().equals(ScapCoConstants.EDIT)
							|| gc.getId().equals(ScapCoConstants.DEL)) {
						gc.setVisible(false);
					}
				}
			}
		}
		if (IGlobalConstants.BTN_LOOK.equals(method_type)) {
			setItemState(mc, ScapCoConstants.CANCLE);
			setItemState(mc, IGlobalConstants.BTN_SAVE);
			setItemState(mc, ScapCoConstants.NOTICEISSUE);
			// ���ر��Ĳ˵�
			WebComponent[] comps = main.getViewComponents().getComponents();
			if (comps == null || comps.length == 0)
				return;
			for (WebComponent comp : comps) {
				if (comp instanceof GridComp) {
					GridComp grid = (GridComp) comp;
					MenubarComp gridMenuBar = grid.getMenuBar();
					if (gridMenuBar == null)
						return;
					List<MenuItem> items = gridMenuBar.getMenuList();
					if (items == null || items.isEmpty())
						return;
					for (MenuItem item : items) {
						item.setVisible(false);
					}
				}
			}
		}
	}
	/**
	 * ��ҵ��ϸ��Ϣ�鿴����
	 */
	public void detailAction() {
		// ���ر��水ť
		hideOperateButton(getDetailHideBtns());
		// ����һЩ������ť
		hideAuditButton(ScapCoConstants.BTN_SAVE, ScapCoConstants.LINK_ADDATTACH,
				ScapCoConstants.BTN_OK);
		
	}
	/**
	 * ����ί��ϸ��Ϣ�鿴����
	 */
	public void detail_gzwAction() {
		// ���ر��水ť
		hideOperateButton(ScapCoConstants.CONFIRMLOOK);
		// ����һЩ������ť
		hideAuditButton(ScapCoConstants.BTN_SAVE, ScapCoConstants.LINK_ADDATTACH,
				ScapCoConstants.BTN_OK);
		
	}
	/**
	 * ��������
	 */
	public void addAction() {
		// ����һЩ��ť
		hideOperateButton(ScapCoConstants.CONFIRMLOOK);
	}

	/**
	 * �޸Ĳ���
	 */
	public void editAction() {
		// ����һЩ��ť
		hideOperateButton(ScapCoConstants.CONFIRMLOOK);
	}

	/**
	 * �Ƿ��������̵ĸ�������
	 * 
	 * @return
	 */
	protected boolean isHideWfAttachFileBtn() {
		return true;
	}

	/**
	 * ���ؿ�Ƭҳ���ֱ�Ĳ�����ť
	 * 
	 * @param pro_action
	 */
	protected void hideSlaveGridsButtons(String pro_action) {
		if (ScapCoConstants.ADD.equals(pro_action)
				|| ScapCoConstants.EDIT.equals(pro_action))
			return;
		String[] gridIds = this.getSlaveGridIds();
		if (gridIds == null || gridIds.length == 0) {
			return;
		}
		for (String gridId : gridIds) {
			// �����ӱ�İ�ť
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
	 * ��ȡ��Ƭҳ���ֱ��id
	 * 
	 * @return
	 */
	protected String[] getSlaveGridIds() {
		return null;
	}

	/**
	 * InitUIMetaStruct��ִ��
	 * 
	 * @param pro_action
	 */
	protected void afterInitUIMetaStruct(String pro_action) {

	}

	/**
	 * ��ȡ��ϸ��Ϣ�鿴ʱ��Ҫ���صİ�ť
	 * 
	 * @return
	 */
	protected String[] getDetailHideBtns() {
		return new String[] { ScapCoConstants.SAVE };
	}

	/**
	 * ��ȡ������
	 * 
	 * @return
	 */
	protected String getOperateAction() {
		return (String) LfwRuntimeEnvironment.getWebContext()
				.getOriginalParameter("pro_action");
	}

	/**
	 * ���ؿ�Ƭҳ��İ�ť
	 * 
	 * @param btns
	 *            ���ذ�ť������
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
	 * ����������ť
	 * 
	 * @param btns
	 *            ��Ҫ���ذ�ť������
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
	 * �鿴��������button
	 * 
	 * @param btns
	 *            ��Ҫ���ذ�ť������
	 */
	protected void hideButtonForLook() {
		String pro_action = getOperateAction();
		LfwView main = getPageMeta().getView("main");
		MenubarComp mc = getViewMenubarComp(main, "menubar");
		List<MenuItem> gcs = mc.getMenuList();
		if (IGlobalConstants.BTN_LOOK.equals(pro_action)) {
			setItemState(mc, IGlobalConstants.BTN_DEL);
			setItemState(mc, IGlobalConstants.BTN_COPY);
			setItemState(mc, IGlobalConstants.BTN_SAVE);

			// ���ر��Ĳ˵�
			WebComponent[] comps = main.getViewComponents().getComponents();
			if (comps == null || comps.length == 0)
				return;
			for (WebComponent comp : comps) {
				if (comp instanceof GridComp) {
					GridComp grid = (GridComp) comp;
					MenubarComp gridMenuBar = grid.getMenuBar();
					if (gridMenuBar == null)
						return;
					List<MenuItem> items = gridMenuBar.getMenuList();
					if (items == null || items.isEmpty())
						return;
					for (MenuItem item : items) {
						item.setVisible(false);
					}
				}
			}
		}
	}
	/**
	 * ����������ť
	 * 
	 * @param btns
	 *            ��Ҫ���ذ�ť������
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

	/**
	 * ��ȡ��ͼ��ָ���˵�
	 * 
	 * @param view
	 * @param menuID
	 * @return
	 */
	public static MenubarComp getViewMenubarComp(LfwView view, String menuID) {
		MenubarComp mc = null;
		if (view != null) {
			mc = view.getViewMenus().getMenuBar(menuID);
		}
		return mc;
	}
	private void setItemState(MenubarComp mb, String itemid) {
		if (mb == null) {
			return;
		}
		MenuItem it1 = mb.getItem(itemid);
		if (it1 != null)
			it1.setVisible(false);
	}
	
}
