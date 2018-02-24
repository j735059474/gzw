package nc.scap.pub.model;

import java.util.List;

import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.notice.util.NoticeUtil;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.CpCtrlUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.ViewComponents;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.core.refnode.NCRefNode;
import nc.vo.scapco.work_notice_contacts.notice_contact_info;

import org.apache.commons.lang.StringUtils;

import uap.web.bd.pub.AppUtil;

public class NoticeListPageModel  extends PageModel {
	@Override
	protected void initUIMetaStruct() {
		super.initUIMetaStruct();
		String tzxf_notice_style = (String) LfwRuntimeEnvironment.getWebContext()
				.getOriginalParameter(IGlobalConstants.TZXF_NOTICE_STYLE);
		String tzxf_message_type_code = (String) LfwRuntimeEnvironment.getWebContext()
				.getOriginalParameter(IGlobalConstants.TZXF_MESSAGE_TYPE_CODE);
		String tzxf_report_body = (String) LfwRuntimeEnvironment.getWebContext()
				.getOriginalParameter(IGlobalConstants.TZXF_REPORTBODY);
		String pageCode = getPageCode();
		AppUtil.addAppAttr(IGlobalConstants.TZXF_NOTICE_STYLE, tzxf_notice_style);
		AppUtil.addAppAttr(IGlobalConstants.TZXF_MESSAGE_TYPE_CODE, tzxf_message_type_code);
		AppUtil.addAppAttr(IGlobalConstants.TZXF_REPORTBODY, tzxf_report_body);
		// 进入画面前先根据当前的用户信息，去查询常用联系人信息子表。
		// 如果为接收方，则可以进入查看画面，否则不可以进入通知查看画面；
		// 如果为发送方，则可以进入通知下发画面，否则不可以进入通知下发画面
		List<notice_contact_info> list = NoticeUtil.getContactInfoByCurrentUser(CntUtil.getCntUserPk());
		if (ScapCoConstants.PAGECODE_MANAGER.equals(pageCode)) {// 管理页面
			// 如果当前登陆用户，在常用联系人中没有一个类型中是作为发送方存在的，那么抛message。提示 不能进入画面。
			if (StringUtils.isBlank(tzxf_notice_style)) {
				if(!NoticeUtil.isSender(list)){
					throw new LfwRuntimeException("当前用户不是通知发送方，不能进入通知下发画面！");
				}
			}
			hideOperateButton(getManagerPageHideBtn());
		} else if (ScapCoConstants.PAGECODE_BROWSE.equals(pageCode)) {// 查看页面
			// 如果当前登陆用户，在常用联系人中没有一个类型中是作为接收方存在的，那么抛message。提示 不能进入画面。
			if (StringUtils.isBlank(tzxf_notice_style)) {
				if(!NoticeUtil.isReceiver(list)){
					throw new LfwRuntimeException("当前用户不是通知接收方，不能进入通知查看画面！");
				}
			}
			hideOperateButton(getBrowsePageHideBtn());
		}
		ViewComponents views = getPageMeta().getView("main").getViewComponents();
		GridComp noticeGrid = (GridComp)views.getComponent("notice_manager_grid");
		GridComp receiveNoticeGrid = (GridComp)views.getComponent("receive_notice_grid");
		GridComp receiveManGrid = (GridComp)views.getComponent("receive_man_grid");
		if (StringUtils.isNotBlank(tzxf_notice_style)) {
			//如果是普通消息通知的话进行相应字段的隐藏
			if (IGlobalConstants.NOTICE_STYLE_NORMAL_MESSAGE.equals(tzxf_notice_style)) {
				String[] elesNoticeGrid = new String[] { "business_type_name","notice_type_report_type","data_type_name",
						"urge_related","is_auto_urge","urge_info_trans_way", 
						"day_num", "urge_frequency", "urge_content_gzw","urge_content_qy",
						"remind_related","is_remind","remind_way","remind_info","expiration_date"};
				CpCtrlUtil.gridColmVisiable(noticeGrid,elesNoticeGrid,false);
				CpCtrlUtil.gridColmVisiable(noticeGrid,new String[]{"def4"},true);
				String[] elesReceiveNoticeGrid = new String[] { "report_status","report_time"};
				CpCtrlUtil.gridColmVisiable(receiveNoticeGrid,elesReceiveNoticeGrid,false);
				String[] elesReceiveManGrid = new String[] { "report_status","report_time"};
				CpCtrlUtil.gridColmVisiable(receiveManGrid,elesReceiveManGrid,false);
				hideOperateButton(new String[] {ScapCoConstants.URGE});
			}
		} else {
			CpCtrlUtil.gridColmVisiable(noticeGrid,new String[]{"def4"},false);
		}
		if (ScapCoConstants.PAGECODE_MANAGER.equals(pageCode)) {// 管理页面
			// 配置的查询模板业务类型和报告类型字段的过滤
			LfwView searchView = LfwRuntimeEnvironment.getWebContext().getPageMeta().getView("simplequery");
//			FormComp formcomp = (FormComp) searchView.getViewComponents()
//			.getComponent("mainform");
			if (searchView != null) {
				IRefNode[] IRefNodeArray = searchView.getViewModels().getRefNodes();
				//E9AA0118_refnode_mainds_business_type
				// E9AA0118_refnode_mainds_notice_type
				NCRefNode refNode1 = (NCRefNode)searchView.getViewModels().getRefNode("E9AA0117_refnode_mainds_notice_type");
				if (refNode1 != null) {
					refNode1.setDataListener("nc.scap.pub.notice_manager.RepportTypeGridRefCtrl");	
				}
				NCRefNode refNode2 = (NCRefNode)searchView.getViewModels().getRefNode("E9AA0117_refnode_mainds_business_type");
				if (refNode2 != null) {
					refNode2.setDataListener("nc.scap.pub.notice_manager.BusinessTypeGridRefCtrl");
				}
			}
		} else if (ScapCoConstants.PAGECODE_BROWSE.equals(pageCode)) {// 查看页面
			// 配置的查询模板业务类型和报告类型字段的过滤
			LfwView searchView = LfwRuntimeEnvironment.getWebContext().getPageMeta().getView("simplequery");
//			FormComp formcomp = (FormComp) searchView.getViewComponents()
//			.getComponent("mainform");
			if (searchView != null) {
				IRefNode[] IRefNodeArray = searchView.getViewModels().getRefNodes();
				NCRefNode refNode1 = (NCRefNode)searchView.getViewModels().getRefNode("E9AA0118_refnode_mainds_notice_type");
				if (refNode1 != null) {
					refNode1.setDataListener("nc.scap.pub.notice_manager.RepportTypeGridRefCtrl");	
				}
				NCRefNode refNode2 = (NCRefNode)searchView.getViewModels().getRefNode("E9AA0118_refnode_mainds_business_type");
				if (refNode2 != null) {
					refNode2.setDataListener("nc.scap.pub.notice_manager.BusinessTypeGridRefCtrl");
				}
			}
		}


	}

	/**
	 * 获取管理页面需要隐藏的按钮
	 * 
	 * @return
	 */
	protected String[] getManagerPageHideBtn() {
		return new String[] { ScapCoConstants.APPROVE,ScapCoConstants.RETURN};
	}

	/**
	 * 获取查看页面需要隐藏的按钮
	 * 
	 * @return
	 */
	protected String[] getBrowsePageHideBtn() {
		return new String[] { ScapCoConstants.ADD, ScapCoConstants.EDIT, ScapCoConstants.DEL,ScapCoConstants.BTN_TAKEBACK,
				ScapCoConstants.BTN_SUBMIT,
				ScapCoConstants.APPROVE,
				ScapCoConstants.SUMMARY,
				ScapCoConstants.RETURN,
				ScapCoConstants.NOTICEISSUE,
				ScapCoConstants.URGE};
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
