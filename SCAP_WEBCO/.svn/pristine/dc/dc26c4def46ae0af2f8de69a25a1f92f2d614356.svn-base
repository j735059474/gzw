package nc.scap.pub.selfquery;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import nc.uap.cpb.log.CpLogger;
import nc.uap.ctrl.tpl.qry.ICpQryTemplateInnerQryService;
import nc.uap.ctrl.tpl.qry.base.CpQueryTemplateTotalVO;
import nc.uap.ctrl.tpl.qry.base.CpQueryTemplateTranslator;
import nc.uap.ctrl.tpl.qry.base.CpQueryTemplateVO;
import nc.uap.ctrl.tpl.qry.base.QuerySchemeObject;
import nc.uap.ctrl.tpl.qry.base.QuerySchemeVO;
import nc.uap.ctrl.tpl.qry.meta.FilterMeta;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.ctrlfrm.ModePhase;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.model.IWidgetUIProvider;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.jsp.uimeta.UIButton;
import nc.uap.lfw.jsp.uimeta.UIControl;
import nc.uap.lfw.jsp.uimeta.UIFlowhLayout;
import nc.uap.lfw.jsp.uimeta.UIFlowhPanel;
import nc.uap.lfw.jsp.uimeta.UIFlowvLayout;
import nc.uap.lfw.jsp.uimeta.UIFlowvPanel;
import nc.uap.lfw.jsp.uimeta.UIFormComp;
import nc.uap.lfw.jsp.uimeta.UIImageComp;
import nc.uap.lfw.jsp.uimeta.UILabelComp;
import nc.uap.lfw.jsp.uimeta.UILayoutPanel;
import nc.uap.lfw.jsp.uimeta.UILinkComp;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIPanel;
import nc.uap.lfw.jsp.uimeta.UIPanelPanel;
import nc.vo.pub.BusinessException;
import uap.lfw.core.locator.ServiceLocator;
import uap.lfw.core.ml.LfwResBundle;
import uap.lfw.imp.query.base.QuerySchemeValidHelper;
import uap.web.bd.pub.user.CpUserManager;

public class SelfQueryWidgetUIProvider implements IWidgetUIProvider {

	private static final String SIMPLE_QRY_LANGDIR = "cpb_nodes";
	private static final String SIMPLE_QRY_RES = "w_cp_role-000133";
	private static final String SIMPLE_QUERY_PANEL_ID = "simpleQueryPanel";
	private CpQueryTemplateTotalVO totalVO;
	@Override
	public UIMeta getDefaultUIMeta(LfwView view) {
		String widgetId = view.getId();
		UIMeta um = new UIMeta();
		um.setId(widgetId + "_um");
		um.setFlowmode(Boolean.TRUE);
		if (!LfwRuntimeEnvironment.getModePhase().equals(ModePhase.dft)) {
			return um;
		}
//		String queryNodeKey = null;
//		String queryNode = null;
//		LfwWindow pm = view.getWindow();
//		if (pm != null) {
//			if (view.getExtendAttribute(LfwWindow.$QUERYTEMPLATE) != null)
//				queryNodeKey = (String) view.getExtendAttribute(
//						LfwWindow.$QUERYTEMPLATE).getValue();
//			if (queryNodeKey == null) {
//				if (pm.getExtendAttribute(LfwWindow.$QUERYTEMPLATE) != null) {
//					queryNodeKey = (String) pm.getExtendAttribute(
//							LfwWindow.$QUERYTEMPLATE).getValue();
//				}
//			}
//			queryNode = (String) AppLifeCycleContext.current()
//					.getApplicationContext()
//					.getAppAttribute(AppControlPlugin.NODECODE);
//		}
//		// 如果url没有配置nodecode
//		if (queryNode == null || "".equals(queryNode)) {
//			String pageId = (String) pm.getId();
//			String pagePath = LFWAllComponetsFetcher.getWindowPath(pageId);
//			if (pagePath != null) {
//				// String nodePath = "html/nodes/" + pagePath +
//				// "/node.properties";
//				String nodePath = pagePath + "/node.properties";
//				Properties props = AbstractPresentPlugin
//						.loadNodePropertie(nodePath);
//				if (props != null) {
//					queryNode = props.getProperty(AppControlPlugin.NODECODE);
//					AppLifeCycleContext
//							.current()
//							.getApplicationContext()
//							.addAppAttribute(AppControlPlugin.NODECODE,
//									queryNode);
//
//				}
//				// 设置node.properties中的属性
//			}
//		}
		if(view.getExtendAttribute(SelfQueryWidgetProvider.TOTALVO_KEY) != null){
			totalVO = (CpQueryTemplateTotalVO) view.removeExtendAttribute(SelfQueryWidgetProvider.TOTALVO_KEY).getValue();
		}
		boolean designMode = LfwRuntimeEnvironment.getMode().equals(
				WebConstant.MODE_DESIGN)
				|| LfwRuntimeEnvironment.getLfwSessionBean() == null;
		boolean notMock = false;
		if (designMode) {
			mockNullWidget(view, um, false);
			return um;
		}
		if (totalVO == null) {
			mockNullWidget(view, um, false);
		} else {
			List<FilterMeta> defaultMetas = null;
			boolean ifDisplay = true;
			CpQueryTemplateTranslator loader = new CpQueryTemplateTranslator();
			if (totalVO != null) {
				CpQueryTemplateVO templateVO = totalVO.getTempletVO();
				if (templateVO != null && templateVO.getIfdisplay() != null)
					ifDisplay = templateVO.getIfdisplay().booleanValue();
				loader.loadData(totalVO);
				defaultMetas = loader.getDefaultMetas();
				notMock = true;
			}
			if (defaultMetas == null || defaultMetas.size() == 0) {
				notMock = false;
				if (ifDisplay)
					advanceSearchWidget(widgetId, um, view, true);
				else
					mockNullWidget(view, um, true);

			}
		}
		if (notMock) {
			UIPanelPanel panelPanel = null;
			UIPanel panel = null;
			String autoGenPanel = (String) view.getExtendAttributeValue(LfwView.WITHTITLE);
			if (autoGenPanel != null && "true".equals(autoGenPanel)) {
				panel = new UIPanel();
				panel.setId(SIMPLE_QUERY_PANEL_ID);
				panel.setTitle(LfwResBundle.getInstance().getStrByID("imp",
						"SimpleQueryWidgetUIProvider-000000")/* 快捷查询 */);
				panel.setI18nName(SIMPLE_QRY_RES);
				panel.setLangDir(SIMPLE_QRY_LANGDIR);
				panelPanel = new UIPanelPanel();
				panel.addPanel(panelPanel);
			}

			UIFlowvLayout layout = new UIFlowvLayout();
			layout.setId("flowvlayout");
			layout.setWidgetId(widgetId);
			if (panelPanel != null) {
				panelPanel.setElement(layout);
				um.setElement(panel);
			} else {
				um.setElement(layout);
			}

			UIFlowvPanel queryPlanPanel = new UIFlowvPanel();
			queryPlanPanel.setId("queryPlanPanel");
			queryPlanPanel.setCssStyle("min-height:12px;");
			layout.addPanel(queryPlanPanel);

			// 查询方案的ui生成器
			String withQueryPlan = (String) view
					.getExtendAttributeValue(LfwView.WITHQUERYPLAN);
			if (withQueryPlan == null || withQueryPlan.equals("true")) {
				getQueryPlanUIWidget(view, queryPlanPanel);
			}

			UIFlowvPanel p2 = new UIFlowvPanel();
			p2.setId("p2");
			layout.addPanel(p2);

			UIFormComp form = new UIFormComp();
			form.setId("mainform");
			form.setLabelPosition("top");
			form.setWidgetId(widgetId);
			p2.setElement(form);
			p2.setBottomPadding("14");

			UIFlowvPanel p3 = new UIFlowvPanel();
			p3.setId("p3");
			p3.setHeight("40");
//			p3.setTopPadding("14");
//			p3.setTopBorder("14");
//			p3.setRightPadding("5");
			layout.addPanel(p3);

			UIFlowhLayout hLayout = new UIFlowhLayout();
			hLayout.setId("flowhlayout");
			p3.setElement(hLayout);

			UIButton bt = new UIButton();
			bt.setId("queryBt");
			bt.setAlign(UIControl.ALIGN_LEFT);
			bt.setWidth("65");
			bt.setClassName("blue_button_div");
			UIFlowhPanel hP2 = new UIFlowhPanel();
			hP2.setId("hp2");
			hP2.setWidth("65");
			hP2.setElement(bt);
			hLayout.addPanel(hP2);

			boolean ifDisplay = false;
			if (totalVO != null) {
				CpQueryTemplateVO vo = totalVO.getTempletVO();
				if (vo.getIfdisplay() == null
						|| (vo.getIfdisplay() != null && "Y".equals(vo
								.getIfdisplay().toString()))) {
					ifDisplay = true;
				}
			}
			if (ifDisplay) {
				UILinkComp clean = new UILinkComp();
				clean.setId("advlink");
//				clean.setAlign(UIControl.ALIGN_RIGHT);
//				clean.setTop("5");
				clean.setWidth("50");
				clean.setLeft("5");
				UIFlowhPanel hP3 = new UIFlowhPanel();
				hP3.setTopPadding("5");
				hP3.setWidth("56");
				hP3.setId("hp3");
				hP3.setElement(clean);
				hLayout.addPanel(hP3);
			}

//			UIFlowhPanel hBlank = new UIFlowhPanel();
//			hBlank.setId("blank");
//			hLayout.addPanel(hBlank);
//			
//
			
			UIImageComp clearImg = new UIImageComp();
			clearImg.setId("clearImg");
//			clearImg.setWidth("18");
//			clearImg.setHeight("18");
//			clearImg.setAlign(UIControl.ALIGN_RIGHT);
//			clearImg.setTop("3");
			UIFlowhPanel hP1 = new UIFlowhPanel();
			hP1.setId("hp1");
			hP1.setFloat("right");
			hP1.setWidth("18");
			hP1.setTopPadding("3");
			hP1.setElement(clearImg);
			hLayout.addPanel(hP1);
		}
		um.adjustUI(widgetId);
		return um;
	}

	private void mockNullWidget(LfwView view, UIMeta um, boolean notMock) {
		UIFlowvLayout layout = new UIFlowvLayout();
		layout.setId("flowvlayout");

		UIPanelPanel panelPanel = null;
		UIPanel panel = null;
		String autoGenPanel = (String) view.getExtendAttributeValue(LfwView.WITHTITLE);
		if (autoGenPanel != null && "true".equals(autoGenPanel)) {
			panel = new UIPanel();
			panel.setId(SIMPLE_QUERY_PANEL_ID);
			panel.setTitle(LfwResBundle.getInstance().getStrByID("imp",
					"SimpleQueryWidgetUIProvider-000000")/* 快捷查询 */);
			panel.setI18nName(SIMPLE_QRY_RES);
			panel.setLangDir(SIMPLE_QRY_LANGDIR);
			panelPanel = new UIPanelPanel();
			panel.addPanel(panelPanel);
		}
		if (panelPanel != null) {
			panelPanel.setElement(layout);
			um.setElement(panel);
		} else {
			um.setElement(layout);
		}

		if (notMock) {
			UIFlowvPanel queryPlanPanel = new UIFlowvPanel();
			queryPlanPanel.setId("queryPlanPanel");
			layout.addPanel(queryPlanPanel);

			// 查询方案的ui生成器
			String withQueryPlan = (String) view
					.getExtendAttributeValue(LfwView.WITHQUERYPLAN);
			if (withQueryPlan == null || withQueryPlan.equals("true")) {
				getQueryPlanUIWidget(view, queryPlanPanel);
			}
		}

		UIFlowvPanel p2 = new UIFlowvPanel();
		p2.setId("p2");
		layout.addPanel(p2);

		UILabelComp label = new UILabelComp();
		label.setId("label");
		p2.setElement(label);

		UIFlowvPanel p3 = new UIFlowvPanel();
		p3.setId("p3");
		layout.addPanel(p3);

		UIFormComp form = new UIFormComp();
		form.setId("mainform");
		form.setVisible(false);
		p3.setElement(form);
	}

	/**
	 * 创建查询方案的ui部分
	 * 
	 * @param widget
	 * @return
	 */
	public void getQueryPlanUIWidget(LfwView widget, UIFlowvPanel queryPlanPanel) {
		String widgetId = widget.getId();
		UIFlowvLayout flowvLayout = new UIFlowvLayout();
		flowvLayout.setId("queryPlanflowvLayout");
		queryPlanPanel.setElement(flowvLayout);

		boolean mode = LfwRuntimeEnvironment.isEditMode();
		if (mode) {
		} 
		else {
			if (totalVO != null) {
				String pk_template = null;
				try {
					pk_template = totalVO.getParentVO().getPrimaryKey();
				} catch (BusinessException e) {
					CpLogger.error(e.getMessage());
				}
				boolean issys = totalVO.getTempletVO().getIssys().booleanValue();
				String pk_org = CpUserManager.getPk_org();
				String pk_user = CpUserManager.getPk_user();
				QuerySchemeVO[] vos = null;
				try {
					ICpQryTemplateInnerQryService service = ServiceLocator
							.getService(ICpQryTemplateInnerQryService.class);
					vos = service.getQuerySchemeVOsBy(pk_org, pk_template, pk_user);
					if (!issys) {
						// 增加全局查询方案
						String funcode = totalVO.getTempletVO().getNodecode();
						String funkey = totalVO.getTempletVO().getNodekey();
						QuerySchemeVO[] sysVos = service.getQuerySchemeVosBy(
								funcode, funkey);
						if (sysVos != null && sysVos.length > 0) {
							vos = QuerySchemeValidHelper.getQuerySchemes(sysVos, vos, false);
						}
					}else{
						if(StringUtils.isNotBlank(pk_org)){
							//处理不同于系统模板组织的模板，寻找其中的全局查询方案
							String funcode = totalVO.getTempletVO().getNodecode();
							String funkey = totalVO.getTempletVO().getNodekey();
							QuerySchemeVO[] sysVos = service.getQuerySchemeVosBy(
									funcode, funkey);
							if (sysVos != null && sysVos.length > 0) {
								vos = QuerySchemeValidHelper.getQuerySchemes(sysVos, vos, false);
							}
						}
					}
					if (vos != null) {
						AppLifeCycleContext ctx = AppLifeCycleContext.current();
						UIMeta uimeta = null;
						List<UILayoutPanel> panelList = null;
						UIFlowvLayout view_flowvLayout = null;
						if (ctx != null && ctx.getViewContext() != null) {
							uimeta = ctx.getViewContext().getUIMeta();
							if (uimeta != null) {
								view_flowvLayout = (UIFlowvLayout) uimeta.findChildById("queryPlanflowvLayout");
								if(view_flowvLayout != null){
									panelList = view_flowvLayout.getPanelList();
									if (panelList != null && panelList.size() > 0) {
										panelList.clear();
									}
								}
							}
						}
						for (int i = 0; i < 5 && i < vos.length; i++) {
							QuerySchemeVO vo = vos[i];
							QuerySchemeObject object = null;
							try {
								object = vo.getQSObject4Blob();
							} catch (Exception e) {
							}
							if (object == null)
								continue;
							UILinkComp link = new UILinkComp();
							link.setId("link" + i);
							link.setClassName("link_data_div");
							link.setWidth("145");
							
							UIFlowvPanel flowvPanel = new UIFlowvPanel();
							flowvPanel.setId("flowvPanel" + i);
							flowvPanel.setLeftPadding("8");
							flowvPanel.setHeight("22");
							flowvLayout.addPanel(flowvPanel);
							flowvPanel.setElement(link);

							UIFlowvPanel dotPanel = new UIFlowvPanel();
							dotPanel.setId("dotPanel" + i);
							flowvLayout.addPanel(dotPanel);
							dotPanel.setClassName("panel_dot");
							dotPanel.setCssStyle("margin-bottom:5px;");
							if (uimeta != null && view_flowvLayout != null) {
								view_flowvLayout.addPanel(flowvPanel);
								view_flowvLayout.addPanel(dotPanel);
							}
						}
						if (vos.length > 5) {
							for (int i = 5; i < vos.length; i++) {
								QuerySchemeVO vo = vos[i];
								QuerySchemeObject object = null;
								try {
									object = vo.getQSObject4Blob();
								} catch (Exception e) {
								}
								if (object == null)
									continue;
								UILinkComp link = new UILinkComp();
								link.setId("link" + i);
								link.setClassName("link_data_div");
								link.setWidth("145");	
								UIFlowvPanel flowvPanel = new UIFlowvPanel();
								flowvPanel.setId("flowvPanel" + i);
								flowvPanel.setLeftPadding("8");
								flowvPanel.setHeight("22");
								flowvLayout.addPanel(flowvPanel);
								flowvPanel.setElement(link);
								flowvPanel.setVisible(false);

								UIFlowvPanel dotPanel = new UIFlowvPanel();
								dotPanel.setId("dotPanel" + i);
								flowvLayout.addPanel(dotPanel);
								dotPanel.setClassName("panel_dot");
								dotPanel.setCssStyle("margin-bottom:5px;");
								dotPanel.setVisible(false);
								if (uimeta != null && view_flowvLayout != null) {
									view_flowvLayout.addPanel(flowvPanel);
									view_flowvLayout.addPanel(dotPanel);
								}
							}

							UIFlowhLayout expandLayout = new UIFlowhLayout();
							expandLayout.setId("expandLayout");

							UIFlowhPanel expandhLabelPanel = new UIFlowhPanel();
							expandhLabelPanel.setId("expandhLabelPanel");
							expandhLabelPanel.setWidth("35");
							expandLayout.addPanel(expandhLabelPanel);

							UIFlowhPanel expandhImagePanel = new UIFlowhPanel();
							expandhImagePanel.setId("expandhImagePanel");
							expandhImagePanel.setWidth("10");
							expandLayout.addPanel(expandhImagePanel);

							// UILabelComp expandLabel = new UILabelComp();
							// expandLabel.setId("expandLabel");
							// expandLabel.setWidth("45");
							UILinkComp expandLink = new UILinkComp();
							expandLink.setId("expandLink");
							expandLink.setWidth("48");
							expandLink.setAlign(UIControl.ALIGN_RIGHT);
							expandhLabelPanel.setElement(expandLink);

							UIImageComp expandImg = new UIImageComp();
							expandImg.setId("expandImg");
							expandImg.setWidth("7");
							expandImg.setHeight("5");
							expandImg.setAlign(UIControl.ALIGN_RIGHT);
							expandImg.setTop("3");
							expandhImagePanel.setElement(expandImg);

							UIFlowvPanel expandFlowvPanel = new UIFlowvPanel();
							expandFlowvPanel.setId("expandImg");
							expandFlowvPanel.setLeftPadding("102");
							expandFlowvPanel.setHeight("25");
							flowvLayout.addPanel(expandFlowvPanel);
							expandFlowvPanel.setElement(expandLayout);

							UIFlowvPanel dotBottomPanel = new UIFlowvPanel();
							dotBottomPanel.setId("dotBootmPanel");
							flowvLayout.addPanel(dotBottomPanel);
							dotBottomPanel.setClassName("panel_dot");
							dotBottomPanel.setCssStyle("margin-bottom:5px;");
							if (uimeta != null && view_flowvLayout != null) {
								view_flowvLayout.addPanel(expandFlowvPanel);
								view_flowvLayout.addPanel(dotBottomPanel);
							} 
						}
					}
				} catch (BusinessException e) {
					CpLogger.error(e.getMessage());
				}
			} else {
				UILabelComp label = new UILabelComp();
				label.setId("label");
				label.setWidgetId(widgetId);
				queryPlanPanel.setElement(label);
			}
		}
	}

	private void advanceSearchWidget(String widgetId, UIMeta um, LfwView view,
			boolean notMock) {
		UIFlowvLayout layout = new UIFlowvLayout();
		layout.setId("flowvlayout");
		layout.setWidgetId(widgetId);
		UIPanelPanel panelPanel = null;
		UIPanel panel = null;
		String autoGenPanel = (String) view
				.getExtendAttributeValue(LfwView.WITHTITLE);
		if (autoGenPanel != null && "true".equals(autoGenPanel)) {
			panel = new UIPanel();
			panel.setId(SIMPLE_QUERY_PANEL_ID);
			panel.setTitle(LfwResBundle.getInstance().getStrByID("imp",
					"SimpleQueryWidgetUIProvider-000000")/* 快捷查询 */);
			panel.setI18nName(SIMPLE_QRY_RES);
			panel.setLangDir(SIMPLE_QRY_LANGDIR);
			panelPanel = new UIPanelPanel();
			panel.addPanel(panelPanel);
		}
		if (panelPanel != null) {
			panelPanel.setElement(layout);
			um.setElement(panel);
		} else {
			um.setElement(layout);
		}
		if (notMock) {
			UIFlowvPanel queryPlanPanel = new UIFlowvPanel();
			queryPlanPanel.setId("queryPlanPanel");
			layout.addPanel(queryPlanPanel);

			// 查询方案的ui生成器
			String withQueryPlan = (String) view
					.getExtendAttributeValue(LfwView.WITHQUERYPLAN);
			if (withQueryPlan == null || withQueryPlan.equals("true")) {
				getQueryPlanUIWidget(view, queryPlanPanel);
			}
		}

		UIFlowvPanel p2 = new UIFlowvPanel();
		p2.setId("p2");
		layout.addPanel(p2);

		UIButton bt = new UIButton();
		bt.setId("queryBt");
		bt.setAlign(UIControl.ALIGN_RIGHT);
		bt.setWidth("80");
		bt.setClassName("blue_button_div");
		UIFlowhPanel hP2 = new UIFlowhPanel();
		hP2.setId("hp2");
		hP2.setWidth("80");
		hP2.setElement(bt);
		layout.addPanel(hP2);

		UILinkComp clean = new UILinkComp();
		clean.setId("advlink");
		clean.setAlign(UIControl.ALIGN_RIGHT);
		clean.setTop("5");
		clean.setWidth("50");
		UIFlowhPanel hP3 = new UIFlowhPanel();
		hP3.setId("hp3");
		hP3.setElement(clean);
		layout.addPanel(hP3);

		UIFormComp form = new UIFormComp();
		form.setId("mainform");
		form.setWidgetId(widgetId);
		form.setVisible(false);
		UIFlowhPanel hP4 = new UIFlowhPanel();
		hP4.setId("hp4");
		layout.addPanel(hP4);
		hP4.setElement(form);
	}
}
