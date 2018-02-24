package nc.uap.wfm.pubview;

import nc.uap.lfw.core.combodata.StaticComboData;
import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.comp.RadioGroupComp;
import nc.uap.lfw.core.comp.TextAreaComp;
import nc.uap.lfw.core.comp.text.ComboBoxComp;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.jsp.uimeta.UIBorder;
import nc.uap.lfw.jsp.uimeta.UICardLayout;
import nc.uap.lfw.jsp.uimeta.UICardPanel;
import nc.uap.lfw.jsp.uimeta.UIConstant;
import nc.uap.lfw.jsp.uimeta.UIFlowhLayout;
import nc.uap.lfw.jsp.uimeta.UIFlowhPanel;
import nc.uap.lfw.jsp.uimeta.UIFlowvLayout;
import nc.uap.lfw.jsp.uimeta.UIFlowvPanel;
import nc.uap.lfw.jsp.uimeta.UILabelComp;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIPanel;
import nc.uap.lfw.jsp.uimeta.UIPartComp;
import nc.uap.lfw.jsp.uimeta.UITextField;
import nc.uap.wfm.adapter.factory.WfmEngineUIAdapterFactory;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.wfm.exetask.ApproveExeTaskMainCtrl;
import nc.uap.wfm.exetask.ExeTaskApproveState;
import nc.uap.wfm.exetask.ExeTaskBaseState;
import nc.uap.wfm.exetask.ExeTaskBroseState;
import nc.uap.wfm.exetask.ExeTaskReadState;
import nc.uap.wfm.utils.WfmTaskUtil;
import nc.vo.ml.NCLangRes4VoTransl;
import uap.web.bd.pub.AppUtil;

public class ApproveExeTaskWidgetProvider extends ExecuteTaskWidgetProvider {
	
	/* (non-Javadoc)
	 * @see nc.uap.wfm.pubview.ExecuteTaskWidgetProvider#initWidget(nc.uap.lfw.core.page.LfwView, java.lang.String)
	 */
	@Override
	protected void initWidget(LfwView widget, String taskPk,String billstate) {
		widget.setControllerClazz(ApproveExeTaskMainCtrl.class.getName());
		
		new ExeTaskApproveState().createApproveWidget(widget, taskPk);
		Object task = WfmTaskUtil.getTaskFromSessionCache(taskPk);
		if(WfmConstants.BILLSTATE_MAKEBILL.equals(billstate)){ 
			clearOperate(widget);
		} else if (WfmConstants.BILLSTATE_BROWSE.equals(billstate) ){ 
			clearOperate(widget);
			if(WfmTaskUtil.isCanReCall(task))
				new ExeTaskBaseState().createReCall(widget, task, false);
		} else if (WfmConstants.BILLSTATE_READ.equals(billstate)){
			if(!WfmTaskUtil.isReadEndState(task)){
				//�ıϰ�ť
				new ExeTaskReadState().createReadEndCall(widget,task);
			}
		} else {
			//�����������ľ������õ�Сһ��
			RadioGroupComp rgc = (RadioGroupComp) widget.getViewComponents().getComponent(TEXT_EXEACTION);
			rgc.setSepWidth(5);	
		}
	}

	/**
	 * ȥ�����̲���ѡ��
	 * @param widget
	 * @return
	 */
	private StaticComboData clearOperate(LfwView widget) {
		StaticComboData staticComboData = (StaticComboData) widget.getViewModels().getComboData(ExecuteTaskWidgetProvider.COMBODATA_EXECUTION);
		//ȥ��ת��
		staticComboData.removeComboItem(ExecuteTaskWidgetProvider.EXE_TRANSMIT);
		//ȥ����ֹ
		staticComboData.removeComboItem(ExecuteTaskWidgetProvider.EXE_STOP);
		//ȥ��ǰ��ǩ
		staticComboData.removeComboItem(ExecuteTaskWidgetProvider.EXE_BEFORE_ADD_SIGN);
		//ȥ������
		staticComboData.removeComboItem(ExecuteTaskWidgetProvider.EXE_REJECT);
		return staticComboData;
	}

	/* (non-Javadoc)
	 * @see nc.uap.wfm.pubview.ExecuteTaskWidgetProvider#getDefaultUIMeta(nc.uap.lfw.core.page.LfwView)
	 */
	@Override
	public UIMeta getDefaultUIMeta(LfwView widget) {
		String widgetId = widget.getId();
		UIMeta um = new UIMeta();
		um.setId(widgetId + "_um");
		um.setFlowmode(Boolean.TRUE);
		
		UIFlowhLayout gflowh = new UIFlowhLayout();
		gflowh.setAutoFill(0);
		gflowh.setId("gflowh");
		um.setElement(gflowh);
		
		//1:��߾�panel
		UIFlowhPanel leftPaddingPanel = gflowh.addElementToPanel(null);
		leftPaddingPanel.setWidth("1");
		
		//2.Բ�α߿���м���������
		UIBorder gborder = new UIBorder();
		gborder.setId("gborder");
		gborder.setCssStyle("background:#FCFCFC");
		gborder.setRoundBorder(UIConstant.TRUE);
		gflowh.addElementToPanel(gborder);
		createApprovePanel(widget, gborder);
		
		//3.�ұ߾�
		UIFlowhPanel rightPaddingPanel = gflowh.addElementToPanel(null);
		rightPaddingPanel.setWidth("1");
		
		um.adjustUI(widgetId);
		
		AppUtil.addAppAttr("approveWiget", "Y");
		return um;
	}

	/**
	 * Բ�α߿���м���������
	 * @param widget
	 * @param gborder
	 */
	private void createApprovePanel(LfwView widget, UIBorder gborder) {
		//Բ�α߿���м���������
		UIFlowhLayout bgFlowh = new UIFlowhLayout();
		bgFlowh.setId("bgflowh");
		gborder.addElementToPanel(bgFlowh);
		
		// 1.�м�������߾�
		UIFlowhPanel bgLeftPadding = bgFlowh.addElementToPanel(null);
		bgLeftPadding.setWidth("5");
		
		//2.�м��������������������
		createInnerApprovePanel(widget, bgFlowh);
		
		//3.�м������ұ߾�
		UIFlowhPanel rightPaddingPanel = bgFlowh.addElementToPanel(null);
		rightPaddingPanel.setWidth("5");
	}

	/**
	 * ��������������������
	 * @param widget
	 * @param bgFlowh
	 */
	private void createInnerApprovePanel(LfwView widget, UIFlowhLayout bgFlowh) {
		//��������������������
		UIPanel approvePanel = new UIPanel();
		approvePanel.setId("panel1");
		approvePanel.setTitle(NCLangRes4VoTransl.getNCLangRes().getStrByID("wfm", "ExecuteTaskWidgetProvider-000020")/*�������*/);
		bgFlowh.addElementToPanel(approvePanel);
		approvePanel.setRenderType(null); 
		approvePanel.setExpand(UIConstant.TRUE);
		
		UIFlowvLayout contentFlowh = new UIFlowvLayout();
		contentFlowh.setId("contentFlowh");
		approvePanel.addElementToPanel(contentFlowh);
		
		//������ͬ״̬����ҳ��
		createApproveByState(widget, contentFlowh);
		
		//������ʷ
		setWfmHistory(widget, contentFlowh);
	}

	/**
	 * ������ͬ״̬����ҳ��
	 * @param widget
	 * @param contentFlowh
	 */
	private void createApproveByState(LfwView widget, UIFlowvLayout contentFlowh) {
		UIFlowvLayout flowv = new UIFlowvLayout();
		flowv.setId("flowvlayout0191");
		UIFlowvPanel contentFlowhP = contentFlowh.addElementToPanel(flowv);
		contentFlowhP.setId("contentFlowhP");
//		contentFlowhP.setHeight("500");
		
		
//		String taskPk = (String) LfwRuntimeEnvironment.getWebContext().getParameter(WfmConstants.WfmUrlConst_TaskPk);
//		Object task = WfmTaskUtil.getTaskFromSessionCache(taskPk);;
		
		//������ͬ״̬��ҳ��
		String billstate = (String) AppUtil.getAppAttr(WfmConstants.BILLSTATE);
		//�Ƶ�̬
		if(WfmConstants.BILLSTATE_MAKEBILL.equals(billstate)){
			createMakeBillPage(widget, flowv);
		}
		//���̬
		else if(WfmConstants.BILLSTATE_BROWSE.equals(billstate)){
			createBrowsePage(widget, flowv);
		}
		//����̬
		else if(WfmConstants.BILLSTATE_READ.equals(billstate)){
			createReadPage(widget, flowv);
		}
		//����̬
		else{
			createApprovePage(widget, flowv);
		}
	}

	/**
	 * ����̬
	 * @param widget
	 * @param flowv
	 */
	private void createApprovePage(LfwView widget, UIFlowvLayout flowv) {
		//�������̲���
		createOperator(flowv);
		
		//��������ͳ�����
		createOpinionAndComLan(widget, flowv, true);
		
		{//Ĭ�ϡ�ת����ǰ��ǩ ��Ƭ
			UICardLayout card1 = new UICardLayout();
			card1.setId("card1");
			card1.setCurrentItem("0");
			flowv.addElementToPanel(card1);
			UICardPanel cp1 = new UICardPanel();
			cp1.setId("cardpanel1");
			card1.addPanel(cp1);
			
			//ת���û�
			UICardPanel cp2 = new UICardPanel();
			cp2.setId("cardpanel2");
			card1.addPanel(cp2);
			UIFlowvLayout cp2flowv = new UIFlowvLayout();
			cp2flowv.setId("flowvlayout1683");
			cp2.setElement(cp2flowv);
			UITextField transuser = new UITextField();
			transuser.setId(ExecuteTaskWidgetProvider.TEXT_TRANSMITUSER);
			transuser.setWidth("250");
			UIFlowvPanel cp2flowvPanel = cp2flowv.addElementToPanel(transuser);
			cp2flowvPanel.setHeight("30");
			
			//ǰ��ǩ�û�
			UICardPanel cp3 = new UICardPanel();
			cp3.setId("cardpanel3");
			card1.addPanel(cp3);
			UIFlowvLayout cp3flowv = new UIFlowvLayout();
			cp3flowv.setId("flowvlayout1783");
			cp3.setElement(cp3flowv);
			
			UIFlowhLayout addsignFlowh = new UIFlowhLayout();
			addsignFlowh.setId("addsignFlowh");
			UIFlowvPanel cp3flowvP1 = cp3flowv.addElementToPanel(addsignFlowh);
			cp3flowvP1.setHeight("30");
			
			UITextField addsignUser = new UITextField();
			addsignUser.setId(ExecuteTaskWidgetProvider.TEXT_BEFOREADDSIGNUSER);
			UIFlowhPanel addsignUserPanel = addsignFlowh.addElementToPanel(addsignUser);
			
//			//��϶
//			UIFlowhPanel addsignPempty = new UIFlowhPanel();
//			addsignPempty.setWidth("20");
//			addsignFlowh.addPanel(addsignPempty);
			
			//������
			UITextField logic = new UITextField();
			logic.setId(ExecuteTaskWidgetProvider.TEXT_LOGIC);
			UIFlowhPanel logicPanel = addsignFlowh.addElementToPanel(logic);
			
		}
		{//��������������
			UIFlowhLayout flowh = new UIFlowhLayout();
			flowh.setId("flowhlayout5101");
			flowv.addElementToPanel(flowh);
			
			//����
			if(isVisible(widget,LINK_COPYSEND)){//��ʱ�ò��������ε���modify yhl 20130822
//				new ExeTaskBaseState().createUILinkComp(widget,flowh, LINK_COPYSEND,"40",null,null,null,null,null);
			}
			//��ǩ
			if(isVisible(widget,TEXT_SCRATCHPAD)){
				new ExeTaskBaseState().createUILinkComp(widget,flowh, TEXT_SCRATCHPAD,"40",null,null,null,null,null);
			}
			//���ǩ                                                                                                  //��ʱ�ò��������ε���modify yhl 20130822
			if(isVisible(widget,LINK_AFTADDSIGN)&&!WfmEngineUIAdapterFactory.getInstance().isBeforeAddSignCreatedTask(WfmTaskUtil.getTaskFromSessionCache(WfmTaskUtil.getTaskPkFromSession()))){
//				new ExeTaskBaseState().createUILinkComp(widget,flowh, LINK_AFTADDSIGN,"50",null,null,null,null,null);
			}
			//��ǩ����
			if(isVisible(widget,LINK_ADDSIGNMGR)){//��ʱ�ò��������ε���modify yhl 20130822
//				new ExeTaskBaseState().createUILinkComp(widget,flowh, LINK_ADDSIGNMGR,"60",null,null,null,null,null);
			}
			//���̽���
			if(isVisible(widget,LINK_FLOWIMG)){
				new ExeTaskBaseState().createUILinkComp(widget,flowh, LINK_FLOWIMG,"60",null,null,null,null,null);
			}
			//�߰���ʷ
			if(isVisible(widget,LINK_URGENCYHISTORY)){//��ʱ�ò��������ε���modify yhl 20130822
//				new ExeTaskBaseState().createUILinkComp(widget,flowh, LINK_URGENCYHISTORY,"60",null,null,null,null,null);
			}
			//�׶����
			if(isVisible(widget,LINK_STEPOPINION)){//��ʱ�ò��������ε���modify yhl 20130822
//				new ExeTaskBaseState().createUILinkComp(widget,flowh, LINK_STEPOPINION,"60",null,null,null,null,null);
			}
			//����
			if(isVisible(widget,LINK_ADDATTACH)){
				new ExeTaskBaseState().createUIAttachAndImageComp(widget,flowh,"60",null,null,null,null,null);
			}
		}
			
		{//���͡���ǰ��ǩ�û���Ϣ + �ײ���ť����
			UIFlowvLayout dynFlowv = new UIFlowvLayout();
			dynFlowv.setId(DYN_FLOWVLAYOUT);
			flowv.addElementToPanel(dynFlowv);
			{//����
				UIFlowvPanel dynp1 = new UIFlowvPanel();
				dynp1.setId(DYN_FLOWVPANEL1);
				dynFlowv.addPanel(dynp1);
				UIFlowhLayout layout_copySend = new UIFlowhLayout();
				layout_copySend.setId("layout_copysend");
				dynp1.setElement(layout_copySend);
				
				//���ͱ���
				UILabelComp csTextL = new UILabelComp();
				csTextL.setId(LABEL_COPYSEND);
				csTextL.setWidth(null);//modify by zhaohb�����ȿ������Ӧ
				UIFlowhPanel copySend = layout_copySend.addElementToPanel(csTextL);
				copySend.setWidth("50");
				
				//�����û�name
				UILabelComp csTextName = new UILabelComp();
				csTextName.setWidth(null);
				csTextName.setId(NAME_COPYSEND);
				csTextName.setTextAlign("left");
				layout_copySend.addElementToPanel(csTextName);
				
				//�����û�pk
				UILabelComp csText = new UILabelComp();
				csText.setId(VALUE_COPYSEND);
				layout_copySend.addElementToPanel(csText);
			}
			{//���ǩ
				UIFlowvPanel dynp3 = new UIFlowvPanel();
				dynp3.setId(DYN_FLOWVPANEL3);
				dynFlowv.addPanel(dynp3);
				
				UIFlowhLayout layout_afteraddsign = new UIFlowhLayout();
				layout_afteraddsign.setId("layout_afteraddsign");
				dynp3.setElement(layout_afteraddsign);
				
				UILabelComp afterSignText = new UILabelComp();
				afterSignText.setWidth(null);
				afterSignText.setId(LABEL_AFTERADDSIGN);
				UIFlowhPanel afterSign = layout_afteraddsign.addElementToPanel(afterSignText);
				afterSign.setWidth("80");
				
				//���ǩ�û�name
				UILabelComp afterSignName = new UILabelComp();
				afterSignName.setWidth(null);
				afterSignName.setId(NAME_AFTERADDSIGN);
				layout_afteraddsign.addElementToPanel(afterSignName);
				
				//���ǩ�û�pk
				UILabelComp affterSignValue = new UILabelComp();
				affterSignValue.setId(VALUE_AFTERADDSIGN);
				layout_afteraddsign.addElementToPanel(affterSignValue);
			}
		}
		{
			//�ݴ桢�ύ���׶��ύ���ջذ�ť
			UIFlowhLayout bottomFlowh = new UIFlowhLayout();
			bottomFlowh.setId("bottomFlowh");
			flowv.addElementToPanel(bottomFlowh);
			//�ύ
			new ExeTaskBroseState().createUIButton(widget,bottomFlowh,BTN_OK,"64","right",null,"0",null,"0","blue_button_div");	
			//�׶��ύ
			ButtonComp stepButton = (ButtonComp) widget.getViewComponents().getComponent(BTN_STEP_OK);
			if(stepButton!=null){
				new ExeTaskBroseState().createUIButton(widget,bottomFlowh,BTN_STEP_OK,"64","right",null,"0",null,"0",null);
			}
			//�ݴ�
			new ExeTaskBroseState().createUIButton(widget,bottomFlowh,BTN_SAVE,"64","right",null,"0",null,"0",null);	
		}
	}

	/**
	 * �Ķ�̬
	 * @param widget
	 * @param flowv
	 */
	private void createReadPage(LfwView widget, UIFlowvLayout flowv) {
		//�������̲���
		createOperator(flowv);
		
		//��������ͳ�����
		createOpinionAndComLan(widget, flowv, false);
		
		UIFlowhLayout flowh = new UIFlowhLayout();
		flowh.setId("flowhlayout5101");
		flowv.addElementToPanel(flowh);
		//���̽���
		if(isVisible(widget, LINK_FLOWIMG)){
			new ExeTaskBaseState().createUILinkComp(widget,flowh,ExecuteTaskWidgetProvider.LINK_FLOWIMG,"60",null,null,null,null,null);
		}
		//����
		if(isVisible(widget,LINK_ADDATTACH)){
			new ExeTaskBaseState().createUIAttachAndImageComp(widget, flowh, "60", null, null, null, null, null);
		}
		
		UIFlowhLayout bottomFlowh = new UIFlowhLayout();
		bottomFlowh.setId("bottomFlowh");
		flowv.addElementToPanel(bottomFlowh);
		//�ıϰ�ť
		ButtonComp readEnd = (ButtonComp) widget.getViewComponents().getComponent(Exe_ReadEnd);
		if(readEnd != null){
			new ExeTaskBroseState().createUIButton(widget, bottomFlowh, Exe_ReadEnd, "64", "right", null, "0", null, "0", null);	
		}
	}
	
	/**
	 * �Ƶ�̬ҳ��
	 * @param widget
	 * @param flowv
	 */
	private void createMakeBillPage(LfwView widget, UIFlowvLayout flowv) {
		//�������̲���
		createOperator(flowv);
		
		//��������ͳ�����
		createOpinionAndComLan(widget, flowv, false);
		
		UIFlowhLayout flowh = new UIFlowhLayout();
		flowh.setId("flowhlayout5101");
		flowv.addElementToPanel(flowh);
		//��ǩ
		if(isVisible(widget, TEXT_SCRATCHPAD)){
			new ExeTaskBaseState().createUILinkComp(widget,flowh,ExecuteTaskWidgetProvider.TEXT_SCRATCHPAD,"40",null,null,null,null,null);
		}
		//���̽���
		if(isVisible(widget, LINK_FLOWIMG)){
			new ExeTaskBaseState().createUILinkComp(widget,flowh,ExecuteTaskWidgetProvider.LINK_FLOWIMG,"60",null,null,null,null,null);
		}
		//����
		if(isVisible(widget,LINK_ADDATTACH)){
			new ExeTaskBaseState().createUIAttachAndImageComp(widget,flowh,"60",null,null,null,null,null);
		}
		
		UIFlowhLayout bottomFlowh = new UIFlowhLayout();
		bottomFlowh.setId("bottomFlowh");
		flowv.addElementToPanel(bottomFlowh);
		//�ύ
		new ExeTaskBroseState().createUIButton(widget,bottomFlowh,BTN_OK,"64","right",null,"0",null,"0","blue_button_div");	
		//�ݴ�
		new ExeTaskBroseState().createUIButton(widget,bottomFlowh,BTN_SAVE,"64","right",null,"0",null,"0",null);	
	
	}

	/**
	 * ���̬ҳ��
	 * @param widget
	 * @param flowv
	 */
	private void createBrowsePage(LfwView widget, UIFlowvLayout flowv) {
		//�������̲���
		createOperator(flowv);
		
		//��������ͳ�����
		createOpinionAndComLan(widget, flowv, false);
		
		UIFlowhLayout flowh = new UIFlowhLayout();
		flowh.setId("flowhlayout5101");
		flowv.addElementToPanel(flowh);
		//���̽���
		if(isVisible(widget, LINK_FLOWIMG)){
			new ExeTaskBaseState().createUILinkComp(widget,flowh,ExecuteTaskWidgetProvider.LINK_FLOWIMG,"60",null,null,null,null,null);
		}
		//����
		if(isVisible(widget,LINK_ADDATTACH)){
			new ExeTaskBaseState().createUIAttachAndImageComp(widget,flowh,"60",null,null,null,null,null);
		}
		
		UIFlowhLayout bottomFlowh = new UIFlowhLayout();
		bottomFlowh.setId("bottomFlowh");
		flowv.addElementToPanel(bottomFlowh);
		//�ջذ�ť
		ButtonComp recall = (ButtonComp) widget.getViewComponents().getComponent(BTN_RECALL);
		if(recall != null){
			
			new ExeTaskBroseState().createUIButton(widget,bottomFlowh,BTN_RECALL,"64","right",null,"0",null,"0","blue_button_div");	
		}
	}

	/**
	 * �������̲���
	 * @param flowv
	 */
	private void createOperator(UIFlowvLayout flowv) {
		UIFlowvLayout execFlowh = new UIFlowvLayout();
		execFlowh.setId("execflowh");
		flowv.addElementToPanel(execFlowh);
		
		//�������̲���
		UITextField text = new UITextField();
		text.setId(TEXT_EXEACTION);
		text.setWidth("100%");
		UIFlowvPanel panel1 = execFlowh.addElementToPanel(text);
		panel1.setHeight("50");
		panel1.setId(TEXT_EXEACTION);
	}

	/**
	 * ��������ͳ�����
	 * @param widget
	 * @param flowv
	 * @param enable
	 */
	private void createOpinionAndComLan(LfwView widget, UIFlowvLayout flowv, boolean enable) {
		//���ó�����
		UITextField tf1 = new UITextField();
		tf1.setId(TEXT_COMMONWORD1);
		tf1.setWidth("100%");
//		tf1.setHeight("30");
		flowv.addElementToPanel(tf1);
		
		//�������
	    UITextField tf2 = new UITextField();
		tf2.setId(TEXT_OPINION);
		tf2.setWidth("100%");
		tf2.setHeight("100");
		flowv.addElementToPanel(tf2);
		
		ComboBoxComp comLan = (ComboBoxComp) widget.getViewComponents().getComponent(TEXT_COMMONWORD1);
		comLan.setEnabled(enable);
		
		TextAreaComp textAreaComp = (TextAreaComp) widget.getViewComponents().getComponent(TEXT_OPINION);
		textAreaComp.setEnabled(enable);
	}

	/**
	 * ������ʷ
	 * @param contentFlowh
	 */
	private void setWfmHistory(LfwView widget, UIFlowvLayout contentFlowh) {
		UIFlowvLayout historyLayout = new UIFlowvLayout();
		historyLayout.setId("historyLayout");
		contentFlowh.addElementToPanel(historyLayout);
		// 1.������ʷ
		UIFlowvPanel historyPanel1 = new UIFlowvPanel();
		historyPanel1.setId("historyPanel1");
		historyLayout.addPanel(historyPanel1);
		
		UIPartComp part = new UIPartComp();
		part.setId(HTML_CONTENT);
		historyPanel1.setElement(part);
		
		// 2.����
//		UIFlowvPanel historyPanel2 = new UIFlowvPanel();
//		historyPanel2.setId("historyPanel2");
//		historyLayout.addPanel(historyPanel2);
//		
//		UIFlowhLayout historyHlayout = new UIFlowhLayout();
//		historyHlayout.setId("historyHlayout");
//		historyPanel2.setElement(historyHlayout);
//		
//		UIFlowhPanel historyHpanel1 = new UIFlowhPanel();
//		historyHpanel1.setId("historyHpanel1");
//		historyHlayout.addPanel(historyHpanel1);
//		
//		UIFlowhPanel historyHpanel2 = new UIFlowhPanel();
//		historyHpanel2.setId("historyHpanel2");
//		historyHpanel2.setWidth("50");
//		historyHlayout.addPanel(historyHpanel2);
//		
//		UILinkComp historyLink = new UILinkComp();
//		historyLink.setId(LINK_MOREHISTORY);
//		historyHpanel2.setElement(historyLink);
		
	}
	
}
