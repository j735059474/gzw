package nc.uap.wfm.exetask;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.cmd.UifAttachCmd;
import nc.uap.lfw.core.cmd.UifPlugoutCmd;
import nc.uap.lfw.core.combodata.CombItem;
import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.comp.LabelComp;
import nc.uap.lfw.core.comp.ReferenceComp;
import nc.uap.lfw.core.comp.TextAreaComp;
import nc.uap.lfw.core.comp.text.ComboBoxComp;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.constants.ReferenceConstants;
import nc.uap.lfw.core.ctrl.IController;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.ApplicationContext;
import nc.uap.lfw.core.ctx.OpenProperties;
import nc.uap.lfw.core.ctx.ViewContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.event.FocusEvent;
import nc.uap.lfw.core.event.LinkEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.ScriptEvent;
import nc.uap.lfw.core.event.TextEvent;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.file.FillFileInfoHelper;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.file.LfwFileConstants;
import nc.uap.lfw.jsp.uimeta.UICardLayout;
import nc.uap.lfw.jsp.uimeta.UIFlowhPanel;
import nc.uap.lfw.jsp.uimeta.UIFlowvPanel;
import nc.uap.lfw.jsp.uimeta.UILabelComp;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.portal.plugins.PluginManager;
import nc.uap.portal.plugins.model.PtExtension;
import nc.uap.wfm.adapter.factory.WfmEngineCmdAdapterFactory;
import nc.uap.wfm.adapter.factory.WfmEngineUIAdapterFactory;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.wfm.constant.WfmTaskStatus;
import nc.uap.wfm.engine.TaskProcessUI;
import nc.uap.wfm.exception.WfmServiceException;
import nc.uap.wfm.itf.IWfmCommonWordQry;
import nc.uap.wfm.itf.IWfmStepOpinionQry;
import nc.uap.wfm.message.TaskMessageReadedMgr;
import nc.uap.wfm.pubview.ExecuteTaskWidgetProvider;
import nc.uap.wfm.pubview.ITaskMessageTmp;
import nc.uap.wfm.utils.WfmPublicViewUtil;
import nc.uap.wfm.utils.WfmTaskUtil;
import nc.uap.wfm.utils.WfmUtilFacade;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;

import org.apache.commons.lang.StringUtils;

import uap.lfw.core.locator.ServiceLocator;
import uap.web.bd.pub.AppUtil;

/**
 * @author chouhl
 */
public class ExeTaskMainCtrl implements IController {
	private static final long serialVersionUID = 1L;
	
	public static final String POP_WIDTH = "800";
	public static final String POP_HEIGHT = "500";
	public static final String POP_ADDSIGNWIDTH = "1000";
	public static final String BILLSTATUS_NOTT_START = "NottStart";
	
	/**
	 * 获得当前审批公共view的id
	 * @return
	 */
	protected String getPubView() {
		return WfmConstants.WfmPubView_ExeTask;
	}
	
	protected void onBeforeSubmit(){
		LfwView widget = AppUtil.getView(getPubView());
		ReferenceComp refComp = (ReferenceComp) widget.getViewComponents().getComponent(WfmConstants.WfmComp_BeforeAddSignUser);
		String userPks = null;
		if(refComp!=null){
			userPks = refComp.getValue();
		}
		
		TextComp textComp = (TextComp) widget.getViewComponents().getComponent(WfmConstants.WfmComp_Logic);
		String value = null;
		if(textComp != null){
			value = textComp.getValue();
		}

		String operator = (String)AppUtil.getAppAttr(WfmConstants.WfmAppAttr_ExeAction);;
		if(WfmConstants.WfmOperator_BeforeAddSign.equalsIgnoreCase(operator)){
			if(StringUtils.isBlank(userPks))
				throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("wfm", "ExeTaskMainCtrl-000022")/*前选择前加签用户*/);
			else{
				AppUtil.addAppAttr(WfmConstants.WfmAppAttr_BeforeAddSignUserPks, userPks);	
				AppUtil.addAppAttr(WfmConstants.WfmAppAttr_BeforeAddSignLogic, value);	}
			}
	
		
	}
	
	/**
	 * 提交操作
	 * @param mouseEvent
	 */
	public void btnok_click(MouseEvent<ButtonComp> mouseEvent) {
		//将操作清空
		//AppUtil.addAppAttr(WfmConstants.WfmAppAttr_ExeAction, null);
		this.addApproveInfo();
		onBeforeSubmit();
		new UifPlugoutCmd(getPubView(), WfmPublicViewUtil.PLUGOUT_EXETASK).execute();	
	}
	

	
	/**
	 * 阶段提交操作
	 * @param mouseEvent
	 */
	public void btnstepok_click(MouseEvent<ButtonComp> mouseEvent) {
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_ExeAction, WfmConstants.WfmOperator_StepSubmit);
		LfwView view = AppUtil.getView(getPubView());
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_Opinion, WfmUtilFacade.getOpinionByView(view));
		new UifPlugoutCmd(getPubView(), "plugout_exetask").execute();	
	}
	
	
	
	/**
	 * 收回按钮操作
	 * @param mouseEvent
	 */
	public void btnrecall_click(MouseEvent<ButtonComp> mouseEvent) {
		String taskPk = WfmTaskUtil.getTaskPkFromSession();
		String isNC = (String) AppUtil.getAppAttr(WfmConstants.WfmAppAttr_IsNC);
		isNC="Y";
		if(taskPk!=null && !"".equals(taskPk) && !"Y".equals(isNC) ){
			Object task = WfmTaskUtil.getTaskFromSessionCache(taskPk);
			if(task==null)
				throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("wfm", "WfmCmd-000008")/*当前任务已经删除!*/);
			if (WfmTaskUtil.isRecallTaskOnEndProins(task)) {
				WfmEngineUIAdapterFactory.getInstance().recallEndProinsHandler(task, getPubView());
			}else{
				WfmUtilFacade.recallHandle(getPubView());	
			}
		}
		else if("Y".equals(isNC)){
			WfmUtilFacade.recallHandle(getPubView());
		}
	}
	
	
	/**
	 * 调用阅毕按钮
	 * @param mouseEvent
	 */
	public void  btnreadend_click(MouseEvent<ButtonComp> mouseEvent) {
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_ExeAction, ExecuteTaskWidgetProvider.Exe_ReadEnd);
		LfwView view = AppUtil.getView(getPubView());
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_Opinion, WfmUtilFacade.getOpinionByView(view));
		new UifPlugoutCmd(getPubView(), "plugout_exetask").execute();
	}
	
	
	/**
	 * 取回按钮操作
	 * @param mouseEvent
	 */
	public void btnreback_click(MouseEvent<ButtonComp> mouseEvent) {
		this.addRetractInfo();
		new UifPlugoutCmd(getPubView(), "plugout_exetask").execute();	
	}
	
	/**
	 * 提交操作
	 * @param mouseEvent
	 */
	public void titleok_click(ScriptEvent e) {
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_ExeAction, ExecuteTaskWidgetProvider.EXE_AGREE);
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_Opinion, "");
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_TransmitUserPk, "");
		new UifPlugoutCmd(getPubView(), "plugout_exetask").execute();	
		
		
		//new UifParentPlugoutCmd(WfmConstants.WfmPubView_ExeTask, "plugout_exetask").execute();
	}
	
	
	/**
	 * 点便签时处理
	 * @param e
	 */
	public void scrtchPadClick(LinkEvent e){
//		WfmUtilFacade.isExistFloInf();
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("model", "nc.uap.wfm.pubview.ScratchPadPageModel");
		
		OpenProperties props = new OpenProperties();
		props.setWidth("520");
		props.setHeight("380");
		props.setOpenId(e.getSource().getId());
		props.setTitle(NCLangRes4VoTransl.getNCLangRes().getStrByID("wfm", "ExeTaskMainCtrl-000011")/*便签*/);
		props.setParamMap(paramMap);
		AppLifeCycleContext.current().getViewContext().navgateTo(props);
		
		
		//AppUtil.getCntAppCtx().dynNavgateTo("scratchPad", NCLangRes4VoTransl.getNCLangRes().getStrByID("wfm", "ExeTaskMainCtrl-000011")/*便签*/, "520", "380", paramMap);
	}
	
	
	/**
	 * 点击意见时处理
	 * @param e
	 */
	public void opinionClick(LinkEvent e){
		WfmUtilFacade.isExistFloInf();
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("model", "nc.uap.wfm.pubview.OpinionPageModel");
		OpenProperties props = new OpenProperties();
		props.setWidth("520");
		props.setButtonZone(false);
		props.setHeight("300");
		props.setOpenId(e.getSource().getId());
		props.setTitle(NCLangRes4VoTransl.getNCLangRes().getStrByID("wfm", "DispStrategy-000088")/*意见*/);
		props.setParamMap(paramMap);
		AppLifeCycleContext.current().getViewContext().navgateTo(props);
	}
	
	/**
	 * 点审批历史
	 * @param linkEvent
	 */
	public void onMoreHistory(LinkEvent linkEvent){
		WfmUtilFacade.isExistFloInf();
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("model", "nc.uap.wfm.pubview.CpWfmFlowHistoryPageModel");
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_IsMore , "Y");
		AppUtil.getCntAppCtx().dynNavgateTo("flow_history", NCLangRes4VoTransl.getNCLangRes().getStrByID("wfm", "ExeTaskMainCtrl-000012")/*流程历史*/, POP_WIDTH, POP_HEIGHT, paramMap);
	}
	
	public void link_morehistory(LinkEvent linkEvent){
		flowImgClick(linkEvent);;
	}
	
	/**
	 * 协同消息执行事件
	 * @param linkEvent
	 */
	public void link_taskmessage(LinkEvent linkEvent){
		PtExtension extension = null;
		ITaskMessageTmp taskMessageImpl = null;
		try {
			extension = PluginManager.newIns().getExtension(ITaskMessageTmp.TASK_MESSAGE_PID);
			taskMessageImpl =  extension.newInstance(ITaskMessageTmp.class);
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
			//throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pint", "TaskDisplayHelper-000000", null, new String[]{e.getMessage()})/*获取任务插件失败:{0}*/,e);
		}
		String taskPk = (String) AppUtil.getAppAttr(WfmConstants.WfmAppAttr_TaskPk);
		TaskProcessUI tpi = taskMessageImpl.getTaskMessageUrl(taskPk);
		if (tpi != null) {
			ApplicationContext ctx = AppLifeCycleContext.current()
					.getApplicationContext();
			ctx.popOuterWindow(tpi.getUrl(), tpi.getTitle(), tpi.getWidth(), tpi.getHeight(), tpi.getOpenType());
		}
	}
	
	/**
	 * 上方按钮的暂存,提交操作
	 * @param mouseEvent
	 */
	public void titlesave_click(ScriptEvent e) {
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_ExeAction, WfmConstants.WfmOperator_Interim);
		LfwView view = AppUtil.getView(getPubView());
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_Opinion, WfmUtilFacade.getOpinionByView(view));
		new UifPlugoutCmd(getPubView(), "plugout_exetask").execute();
	}
	
	/**
	 * 暂存
	 * @param mouseEvent
	 */
	public void btnsave_click(MouseEvent<ButtonComp> mouseEvent) {
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_ExeAction, WfmConstants.WfmOperator_Interim);
		LfwView view = AppUtil.getView(getPubView());
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_Opinion, WfmUtilFacade.getOpinionByView(view));
		new UifPlugoutCmd(getPubView(), "plugout_exetask").execute();
	}
	/**
	 * 阶段提交意见清空
	 * @param event
	 */
	public void step_approve_opinion_edit(FocusEvent<?> event){
//		AppInteractionUtil.showConfirmDialog("确认框","是否清空意见框！");
//		if(!AppInteractionUtil.getConfirmDialogResult())return;
		//清空过就不再清空(即只清空上一次的意见)
//		WfmUtilFacade.isExistFloInf();
		boolean isStep=false;
		//需要判断本是否为阶段提交
		try {
			SuperVO[] vos=ServiceLocator.getService(IWfmStepOpinionQry.class).queryStepOpinionByTaskPK(WfmTaskUtil.getTaskPkFromSession());
			if(vos!=null&&vos.length>0){
				isStep=true;
			}
		} catch (WfmServiceException e) {
			LfwLogger.error(e.getMessage(), e);
		}
		String isCleared=(String)AppUtil.getAppAttr(WfmConstants.WfmAppAttr_StepOpinionCleared);
		TextAreaComp textAreaComp=(TextAreaComp)event.getSource();
		if(!"true".equalsIgnoreCase(isCleared)&&isStep){
			AppUtil.addAppAttr(WfmConstants.WfmAppAttr_StepOpinionCleared, "true");
			
			
			if(StringUtils.isNotBlank(textAreaComp.getValue())){
				textAreaComp.setValue("");
			}
		}
		textAreaComp.setMaxSize(250);
	}
	/**
	 * 执行类似参照的代码
	 * @param map
	 */
	public void pluginrefOkPlugin(Map<String, Object> map) {
		LfwView currWidget = AppUtil.getView(getPubView());
		String type = (String) map.get(ReferenceConstants.TYPE);
		String id = (String) map.get(ReferenceConstants.ID);
		String afterAddSign = (String) map.get(WfmConstants.WFM_WINDOW_AFTADDSIGN);
		String wfmDeliver = (String) map.get(WfmConstants.WFM_WINDOW_DELIVER);
		String wfmCommonWord=(String) map.get(WfmConstants.WFM_WINDOW_COMMONWORD);
		UIMeta uimeta = AppUtil.getViewCtx(getPubView()).getUIMeta();
		if(UFBoolean.valueOf(wfmCommonWord).booleanValue()){
			String key=(String) map.get("$itemkey");
			String value=(String) map.get("$itemvalue");
			String oper=(String) map.get("$combooper");
			ComboData comboData=currWidget.getViewModels().getComboData(ExecuteTaskWidgetProvider.COMBODATA_COMMONWORD);
			comboData.setCtxChanged(true);
			if("add".equals(oper)){
				CombItem comboItem=new CombItem();
				comboItem.setValue(key);
				comboItem.setText(value);
				//触发ComboData更新,需要调整顺序
				comboData.addCombItem(comboItem);
				
				comboData.removeComboItem(WfmConstants.COMMONWORDSEP);
				CombItem compItemExt = new CombItem();
				compItemExt.setText(WfmConstants.COMMONWORDSEP);
				compItemExt.setValue(WfmConstants.COMMONWORDSEP);
				comboData.addCombItem(compItemExt);
				
				comboData.removeComboItem(WfmConstants.COMMONWORD);
				CombItem compItem = new CombItem();
				compItem.setText(NCLangRes4VoTransl.getNCLangRes().getStrByID("wfm", "WfmConstants-000002")/*常用语设置*/);
				compItem.setValue(WfmConstants.COMMONWORD);
				comboData.addCombItem(compItem);
			}else if("del".equals(oper)){
				comboData.removeComboItem(key);
			}
			return;
		}else if("Y".equals(afterAddSign)){
			//将后加签用户的信息保存AppUtil.getCntViewCtx
//			UIMeta uimeta = appCtx.getCurrentWindowContext().getCurrentViewContext().getUIMeta();
			UIFlowvPanel pane3 = (UIFlowvPanel) uimeta.findChildById(ExecuteTaskWidgetProvider.DYN_FLOWVPANEL3);
			pane3.setVisible(true);
			String key = (String) map.get("key");
			String value = (String) map.get("value"); 
			
			// 后加签title
			LabelComp label_afteraddsign = (LabelComp) currWidget.getViewComponents().getComponent(ExecuteTaskWidgetProvider.LABEL_AFTERADDSIGN);
			
			if(label_afteraddsign != null){
				label_afteraddsign.setVisible(true);
			}
			//后加签用户name
			LabelComp name_afterAddSign = (LabelComp) currWidget.getViewComponents().getComponent(ExecuteTaskWidgetProvider.NAME_AFTERADDSIGN);
			name_afterAddSign.setText(value);
			//给出宽度才能显示
			UILabelComp uILabelComp=(UILabelComp)uimeta.findChildById(ExecuteTaskWidgetProvider.NAME_AFTERADDSIGN);
			uILabelComp.setWidth("200");
			//后加签用户pk
			LabelComp value_afterAddSign = (LabelComp) currWidget.getViewComponents().getComponent(ExecuteTaskWidgetProvider.VALUE_AFTERADDSIGN);
			value_afterAddSign.setVisible(false);
			value_afterAddSign.setText(key);
			
			// 串并行
			String logic = (String) map.get(WfmConstants.WfmAppAttr_AfterAddSignLogic);
			if(StringUtils.isNotBlank(logic)){
				if (StringUtils.isNotBlank(value)) {
					value+="  ";
					if("and".equals(logic)) {
						value += NCLangRes4VoTransl.getNCLangRes().getStrByID("wfm", "ExeTaskMainCtrl-000013")/*  串行*/;
					} else {
						value += NCLangRes4VoTransl.getNCLangRes().getStrByID("wfm", "ExeTaskMainCtrl-000014")/*  并行*/;
					}
				}
			}
			//串并行逻辑
			AppUtil.addAppAttr(WfmConstants.WfmAppAttr_BeforeAddSignLogic, null);	
			AppUtil.addAppAttr(WfmConstants.WfmAppAttr_AfterAddSignLogic, logic);	
			name_afterAddSign.setText(value);
		
		}else if("Y".equals(wfmDeliver)){
//			UIMeta uimeta = appCtx.getCurrentWindowContext().getCurrentViewContext().getUIMeta();
			UIFlowvPanel pane1 = (UIFlowvPanel) uimeta.findChildById(ExecuteTaskWidgetProvider.DYN_FLOWVPANEL1);
			pane1.setVisible(true);
			String key = (String) map.get("key");
			String value = (String) map.get("value"); 
			
			//设置抄送用户name
			LabelComp name_copysend = (LabelComp) currWidget.getViewComponents().getComponent(ExecuteTaskWidgetProvider.NAME_COPYSEND);
			name_copysend.setVisible(true);
			name_copysend.setText(value);
			
			//给出宽度才能显示
			UILabelComp uILabelComp=(UILabelComp)uimeta.findChildById(ExecuteTaskWidgetProvider.NAME_COPYSEND);
			uILabelComp.setWidth("200");
			//设置抄送用户pk
			LabelComp value_copysend = (LabelComp) currWidget.getViewComponents().getComponent(ExecuteTaskWidgetProvider.VALUE_COPYSEND);
			value_copysend.setText(key);
			value_copysend.setVisible(false);
		}
		else{
			if (ReferenceConstants.TYPE_DS.equals(type)){
				Map<String, String> valueMap = (Map<String, String>)map.get(ReferenceConstants.WRITEFIELDS);
				Dataset ds = currWidget.getViewModels().getDataset(id);
				Row row = ds.getSelectedRow();
				Iterator<?> it =   valueMap.keySet().iterator();
				while(it.hasNext()){
					String key = (String)it.next();
					String value = valueMap.get(key); 
					if(ds.getFieldSet().nameToIndex(key) == -1)
						continue;
					row.setValue(ds.getFieldSet().nameToIndex(key), value);
				}
				ds.setCtxChanged(true);
			}
			else if (ReferenceConstants.TYPE_TEXT.equals(type)){
				ReferenceComp comp =  (ReferenceComp)currWidget.getViewComponents().getComponent(id);
				String key = (String) map.get("key");
				String value = (String) map.get("value"); 
				comp.setValue(key);
				comp.setShowValue(value);
			}else if(type == null){
				TextComp textComp = (TextComp) currWidget.getViewComponents().getComponent(ExecuteTaskWidgetProvider.TEXT_OPINION);
				String value = (String) map.get("commonWord");
				if(null != value){
					textComp.setValue(textComp.getValue() + value);
					
					ComboData commmonWordComb = currWidget.getViewModels().getComboData(ExecuteTaskWidgetProvider.COMBODATA_COMMONWORD);
					commmonWordComb.setCtxChanged(true);
				}
			}
		}
		
		AppLifeCycleContext.current().getApplicationContext().closeWinDialog();
	}
	
	/**
	 * 与打开流程公共view属于同一app范围时，每次需要清除app中的变量
	 */
	private void clearWfmInfo() {
		//每次重新加载页面，都清空提交或暂存后返回的任务pk。这里的任务pk是为了解决在同一个页面中重复提交问题
		AppUtil.addAppAttr(WfmConstants.RETURN_PK_TASK,null);
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_ProInsPk, null);
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_ProdefPk, null);
		//阶段提交：焦点初次进入意见编辑框需要清空意见内容
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_ExeAction, null);
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_StepOpinionCleared, null);
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_ScratchPad, null);
		AppUtil.addAppAttr(WfmConstants.AttachFileList_Temp_Billitem, null);
	}
	
	public void beforeShow(DialogEvent e) {
		setUIState();
		//清空流程信息
		clearWfmInfo();
		
		String opinion=null;
		String taskPk = WfmTaskUtil.getTaskPkFromSession();
		String isNC = (String) AppUtil.getAppAttr(WfmConstants.WfmAppAttr_IsNC);
		if(taskPk!=null && taskPk.length() > 0 && !"Y".equals(isNC)){
			//如果是待阅的任务将其置成已阅状态
//			Task task = WfmTaskUtil.getTaskFromSessionCache(taskPk);
			Object task = WfmTaskUtil.getTaskFromSessionCache(taskPk);	
			if(task == null)
				throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("wfm", "WfmCmd-000008")/*当前任务已经删除!*/);
			if(WfmEngineUIAdapterFactory.getInstance().isUnreadStateTask(task)){
				HashMap<String, Object> params=new HashMap<String, Object>();
				params.put(WfmConstants.WfmAppAttr_Status, WfmTaskStatus.State_Readed);
				WfmEngineCmdAdapterFactory.getInstance().updateTask(task, params);
				AppUtil.getCntAppCtx().addExecScript("try{getTrueParent().reloadData();}catch(e){}");
			}
			//标识看过件
			if(WfmTaskUtil.isHandlerPieceUnRead(task)){
				HashMap<String, Object> params=new HashMap<String, Object>();
				params.put(WfmConstants.WfmAppAttr_Handlepiece, WfmTaskStatus.HandlerPiece_Readed);
				WfmEngineCmdAdapterFactory.getInstance().updateTask(task, params);
//				AppUtil.getCntAppCtx().addExecScript("try{getTrueParent().reloadData();}catch(e){}");
			}
			if(WfmTaskUtil.isRunState(task)||WfmTaskUtil.isBeforeAddSignCmpltState(task)){
				HashMap<String, Object> params=new HashMap<String, Object>();
				//在办
				//params.put(WfmConstants.WfmAppAttr_Status, WfmTaskStatus.State_Plmnt);
				params.put(WfmConstants.WfmAppAttr_signDate, new UFDateTime());
				WfmEngineCmdAdapterFactory.getInstance().updateTask(task, params);
				AppUtil.getCntAppCtx().addExecScript("try{getTrueParent().reloadData();}catch(e){}");
			}
			String billstate = (String) AppUtil.getAppAttr(WfmConstants.BILLSTATE);
			if(task != null){
				String scratchPad = WfmTaskUtil.getScratchpad(task);
//				scratchPad = scratchPad==null?null:scratchPad.replaceAll("\n", "<br>");
				if(StringUtils.isNotBlank(scratchPad)&&WfmConstants.BILLSTATE_APPROVE.equals(billstate)){
//					AppInteractionUtil.showMessageDialog(scratchPad);
					AppLifeCycleContext.current().getApplicationContext().getWindowContext("scrathpad");
					AppLifeCycleContext.current().getViewContext();
					Map<String, String> paramMap = new HashMap<String, String>();
					paramMap.put("model", "nc.uap.wfm.pubview.ScratchPadPageModel");
					paramMap.put("isShowScratchpad", "true");
					OpenProperties props = new OpenProperties();
					props.setWidth("520");
					props.setHeight("380");
					props.setOpenId("scrathpad");
					props.setTitle(NCLangRes4VoTransl.getNCLangRes().getStrByID("wfm", "ExeTaskMainCtrl-000011")/*便签*/);
					props.setParamMap(paramMap);
					AppLifeCycleContext.current().getViewContext().navgateTo(props);
				}
				//弹出意见页面需要的参数
				opinion=WfmTaskUtil.getOpinion(task);
			}
			String pk_user=InvocationInfoProxy.getInstance().getUserId();
			boolean isMakeBillPort=WfmEngineUIAdapterFactory.getInstance().isBillMakerStatus(task);
			Map<String,Object> otherParam =new HashMap<String, Object>(2);
			otherParam.put("curtask", task);
			TaskMessageReadedMgr.setMsgAutoReadedByTask(pk_user, taskPk, "Type_AfterOpenBill", isMakeBillPort, otherParam);
		}
		
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_Opinion,opinion);
	}
	
	/**
	 * 添加界面信息到内容中
	 * TODO 本部分应该根据不同操作梳理不同信息
	 */
	protected void addApproveInfo() {
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_ExeAction, this.getOperator());
		
		LfwView view = AppUtil.getView(getPubView());
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_Opinion, WfmUtilFacade.getOpinionByView(view));
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_TransmitUserPk, WfmUtilFacade.getTransmitUserPk(view));
		String isNC = (String) AppUtil.getAppAttr(WfmConstants.WfmAppAttr_IsNC);
		if(!("Y".equals(isNC))){
			AppUtil.addAppAttr(WfmConstants.WfmAppAttr_DeliverUserPks, this.getDeliverUserPk(view));
			AppUtil.addAppAttr(WfmConstants.WfmAppAttr_AfterAddSignUserPks, this.getafterAddSignUserPk(view));	
		}

		if(this.getOperator() == null){
			AppUtil.addAppAttr(WfmConstants.WfmAppAttr_ExeAction, ExecuteTaskWidgetProvider.EXE_AGREE);
			AppUtil.addAppAttr(WfmConstants.WfmAppAttr_TransmitUserPk, "");
			String billstate = (String) AppUtil.getAppAttr(WfmConstants.BILLSTATE);
			if(WfmConstants.BILLSTATE_MAKEBILL.equals(billstate)){
				String value = (String) AppUtil.getAppAttr(WfmConstants.WfmAppAttr_Opinion);
				AppUtil.addAppAttr(WfmConstants.WfmAppAttr_Opinion,value);
			}else{
				AppUtil.addAppAttr(WfmConstants.WfmAppAttr_Opinion, "");

			}
		}
	}

	
	
	/**
	 * 收回
	 */
	private void addRetractInfo() {
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_ExeAction, WfmConstants.WfmOperator_Retract);
		LfwView view = AppUtil.getView(getPubView());
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_Opinion, WfmUtilFacade.getOpinionByView(view));
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_TransmitUserPk, WfmUtilFacade.getTransmitUserPk(view));
	}
	
	
	/**
	 * 获取操作的动作
	 * 
	 * @return
	 */
	protected String getOperator() {
		String operator = (String) AppUtil.getAppAttr(WfmConstants.WfmAppAttr_ExeAction);
		if(operator == null){
			LfwView view_exetask = AppUtil.getView(getPubView());
			TextComp text_exeaction = (TextComp) view_exetask.getViewComponents().getComponent("text_exeaction");
			if(text_exeaction!=null){
				operator = text_exeaction.getValue();
			}
		}
		return operator;
	}
	
	/**
	 * 获取抄送用户信息
	 * @return
	 */
	protected String getDeliverUserPk(LfwView view_exetask) {
		LabelComp textComp = (LabelComp) view_exetask.getViewComponents().getComponent(ExecuteTaskWidgetProvider.VALUE_COPYSEND);
		if(textComp != null){
			String value = textComp.getText();
			return value;
		}
		return null;
	}
	
	/**
	 * 获取后加签用户信息
	 * @return
	 */
	protected String getafterAddSignUserPk(LfwView view_exetask) {
		LabelComp textComp = (LabelComp) view_exetask.getViewComponents().getComponent(ExecuteTaskWidgetProvider.VALUE_AFTERADDSIGN);
		if(textComp != null){
			String value = textComp.getText();
			return value;
		}
		return null;
	}
	
	
	
	/**
	 * 流程历史
	 * @param linkEvent
	 */
	public void historyClick(LinkEvent linkEvent) {
		AppUtil.getCntAppCtx().popOuterWindow("app/mockapp/mockwin?model=nc.uap.wfm.pubview.CpWfmHistoryPageModel", NCLangRes4VoTransl.getNCLangRes().getStrByID("wfm", "ExeTaskMainCtrl-000012")/*流程历史*/, POP_WIDTH, POP_HEIGHT, ApplicationContext.TYPE_DIALOG);
	}
	
	/**
	 * 后加签
	 * @param linkEvent
	 */
	public void afterAddSignClick(LinkEvent linkEvent) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("model", "nc.uap.wfm.pubview.CpWfmAftAddSignPageModel");
		paramMap.put("isReference", "true");
		String currPageId = AppUtil.getCntAppCtx().getCurrentWindowContext().getId();
		paramMap.put("otherPageId", currPageId);
		String currWidgetId = AppUtil.getCntAppCtx().getCurrentWindowContext().getCurrentViewContext().getId();
		paramMap.put("widgetId", currWidgetId);
		paramMap.put("pageId", "aftaddsignpm");
		//paramMap.put("afterassign", "Y");
		AppLifeCycleContext.current().getApplicationContext().addAppAttribute(WfmConstants.WFM_WINDOW_AFTADDSIGN, "Y");
		
		
		OpenProperties props = new OpenProperties();
		props.setWidth(POP_ADDSIGNWIDTH);
		props.setHeight(POP_HEIGHT);
		props.setTitle(NCLangRes4VoTransl.getNCLangRes().getStrByID("wfm", "ExeTaskMainCtrl-000015")/*后加签*/);
		props.setOpenId("aftaddsignpm");
		props.setParamMap(paramMap);
		
		AppLifeCycleContext.current().getViewContext().navgateTo(props);
		
		//AppUtil.getCntAppCtx().dynNavgateTo(WfmConstants.WFM_WINDOW_AFTADDSIGN, NCLangRes4VoTransl.getNCLangRes().getStrByID("wfm", "ExeTaskMainCtrl-000015")/*后加签*/, POP_ADDSIGNWIDTH, POP_HEIGHT, paramMap);
	}
	
	/**
	 * 指派用户
	 * @param linkEvent
	 */
	public void onAssignUserClick(LinkEvent linkEvent) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("model", "nc.uap.wfm.pubview.assignuser.WfmAssignUserPageModel");
		AppUtil.getCntAppCtx().dynNavgateTo("pubview_assignuser", NCLangRes4VoTransl.getNCLangRes().getStrByID("wfm", "ExeTaskMainCtrl-000016")/*指派用户*/, POP_WIDTH, POP_HEIGHT, paramMap);
	}
	
	/**
	 * 加签管理
	 * @param linkEvent
	 */
	public void addsignMgrClick(LinkEvent linkEvent) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("model", "nc.uap.wfm.pubview.addsignmgr.AddSignMgrPageModel");
		OpenProperties props = new OpenProperties();
		props.setWidth(POP_WIDTH);
		props.setHeight(POP_HEIGHT);
		props.setTitle(NCLangRes4VoTransl.getNCLangRes().getStrByID("wfm", "ExeTaskMainCtrl-000017")/*加签管理*/);
		props.setOpenId("addsignpm");
		props.setParamMap(paramMap);
		AppLifeCycleContext.current().getViewContext().navgateTo(props);
		//AppUtil.getCntAppCtx().dynNavgateTo("addsignmgr", NCLangRes4VoTransl.getNCLangRes().getStrByID("wfm", "ExeTaskMainCtrl-000017")/*加签管理*/, POP_WIDTH, POP_HEIGHT, paramMap);
	}
	
	/**
	 * 抄送
	 * @param linkEvent
	 */
	public void deliverClick(LinkEvent linkEvent) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("model", "nc.uap.wfm.pubview.CpWfmDeliverPageModel");
		paramMap.put("isReference", "true");
		String currPageId = AppUtil.getCntAppCtx().getCurrentWindowContext().getId();
		paramMap.put("otherPageId", currPageId);
		String currWidgetId = AppUtil.getCntAppCtx().getCurrentWindowContext().getCurrentViewContext().getId();
		paramMap.put("widgetId", currWidgetId);
		paramMap.put("pageId", "deliverpm");
		//paramMap.put("afterassign", "Y");
		AppLifeCycleContext.current().getApplicationContext().addAppAttribute(WfmConstants.WFM_WINDOW_DELIVER, "Y");
		
		OpenProperties props = new OpenProperties();
		props.setWidth(POP_ADDSIGNWIDTH);
		props.setHeight(POP_HEIGHT);
		props.setTitle(NCLangRes4VoTransl.getNCLangRes().getStrByID("wfm", "ExeTaskMainCtrl-000018"));/*抄送用户*/
		props.setOpenId("deliverpm");
		props.setParamMap(paramMap);
		AppLifeCycleContext.current().getViewContext().navgateTo(props);
		
		//AppUtil.getCntAppCtx().dynNavgateTo(WfmConstants.WFM_WINDOW_DELIVER, NCLangRes4VoTransl.getNCLangRes().getStrByID("wfm", "ExeTaskMainCtrl-000018")/*抄送用户*/, POP_ADDSIGNWIDTH, POP_HEIGHT, paramMap);
	}
	
	/**
	 * 查看催办历史
	 * @param linkEvent
	 */
	public void urgencyHistory(LinkEvent linkEvent){
		//通过任务获取流程实例Pk
		String taskPk = WfmTaskUtil.getTaskPkFromSession();
		Object task = WfmTaskUtil.getTaskFromSessionCache(taskPk);
		String proInsPk = WfmEngineUIAdapterFactory.getInstance().getProinsPkByTask(task);
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put(WfmConstants.WfmAppAttr_ProInsPk, proInsPk);
		
		OpenProperties props = new OpenProperties();
		props.setWidth("500");
		props.setHeight("400");
		props.setTitle(NCLangRes4VoTransl.getNCLangRes().getStrByID("wfm", "FlowHistory-000003")/*催办历史*/);
		props.setOpenId("wfm_urgencyhistory");
		props.setParamMap(paramMap);
		AppLifeCycleContext.current().getViewContext().navgateTo(props);
	}
	
	/**
	 * 查看详细阶段意见
	 * @param scriptEvent
	 */
	public void openStepOpinion(LinkEvent linkEvent){
		String taskPk = WfmTaskUtil.getTaskPkFromSession();
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put(WfmConstants.WfmAppAttr_TaskPk, taskPk);
		OpenProperties props = new OpenProperties();
		props.setWidth("800");
		props.setHeight("500");
		props.setTitle(NCLangRes4VoTransl.getNCLangRes().getStrByID("wfm", "FlowHistory-000004"))/*详细阶段意见*/;
		props.setOpenId("wfm_stepopinion");
		props.setParamMap(paramMap);
		AppUtil.getCntViewCtx().navgateTo(props);
	}
	
	/**
	 * 单据附件时，要求必须传入了单据主键billitem,否则认为是流程附件
	 * @param linkEvent
	 */
	public void attachClick(LinkEvent linkEvent) {
		String billitem = (String) AppUtil.getAppAttr(WfmConstants.WfmAppAttr_FormInFoCtx_Billitem);
		String taskPk = WfmTaskUtil.getTaskPkFromSession();
		if (StringUtils.isNotBlank(taskPk)) {
			String tbillitem =  WfmTaskUtil.getBillItemByTaskpk(taskPk);
			if (StringUtils.isBlank(billitem)||!tbillitem.equals(billitem) ) {
				billitem = tbillitem; // 只有一样时才取流程实例上的billitem，否则取传入的
			}
		} else {
			if (StringUtils.isBlank(billitem)) {
				billitem = FillFileInfoHelper.getOrCreateItem();
			}
		}
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_FormInFoCtx_Billitem, billitem);
		AppUtil.addAppAttr(LfwFileConstants.Filemgr_Para_OperateClazz,"nc.uap.wfm.filemgr.OccupyOperateImp");
		WfmUtilFacade.isExistFloInf();
		Map<String, String> paramMap = WfmUtilFacade.getFileMgrParamsbyTaskPk(taskPk, billitem);
		String title = NCLangRes4VoTransl.getNCLangRes().getStrByID("wfm", "ExeTaskMainCtrl-000019"/*流程附件*/);
		UifAttachCmd cmd = new UifAttachCmd(title,paramMap);
		cmd.execute();
	}	
	
	/**
	 * 流程进度,适配新版流程
	 * @param linkEvent
	 */
	public void flowImgClick(LinkEvent linkEvent) {
		//WfmUtilFacade.isExistFloInf();
		WfmUtilFacade.openFlowImage();
	}
	
//	public void commonword1_valueChanged(TextEvent textEvent) {
	public void commonword1_valueChanged(ScriptEvent scriptEvent) {
		LfwView widget = AppUtil.getView(getPubView());
		TextAreaComp textComp = (TextAreaComp) widget.getViewComponents().getComponent("text_opinion");
		ComboBoxComp comBoxComp = (ComboBoxComp) scriptEvent.getSource();
		String value = comBoxComp.getValue();
		if(WfmConstants.COMMONWORDSEP.equals(value))
			return;
		if(WfmConstants.COMMONWORD.equals(value)){
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("model", "nc.uap.wfm.pubview.CpWfmCommonWordPageModel");
//			AppUtil.getCntAppCtx().dynNavgateTo("pubview_commonword", "常用语", "700", "530", paramMap);
			paramMap.put("isReference", "true");
			String currPageId = AppUtil.getCntAppCtx().getCurrentWindowContext().getId();
			paramMap.put("otherPageId", currPageId);
			String currWidgetId = AppUtil.getCntAppCtx().getCurrentWindowContext().getCurrentViewContext().getId();
			paramMap.put("widgetId", currWidgetId);
			//paramMap.put("pageId", "commonWordPm");
			OpenProperties props = new OpenProperties();
			props.setWidth("580");
			props.setHeight("400");
			props.setTitle(NCLangRes4VoTransl.getNCLangRes().getStrByID("wfm", "ExeTaskMainCtrl-000021")/*常用语*/);
			props.setOpenId("commonWordPm");
			props.setParamMap(paramMap);
			AppLifeCycleContext.current().getViewContext().navgateTo(props);
			//AppUtil.getCntAppCtx().dynNavgateTo("pubview_commonword", NCLangRes4VoTransl.getNCLangRes().getStrByID("wfm", "ExeTaskMainCtrl-000021")/*常用语*/, "580", "400", paramMap);
		}else{
//			String oldValue = textComp.getValue();
//			value = oldValue + value;
//			textComp.setValue(value);
			
			String oldValue = textComp.getValue();
			IWfmCommonWordQry commonWordWQry = NCLocator.getInstance().lookup(IWfmCommonWordQry.class);
			try {
				String content = commonWordWQry.getCommonContentsByPk(value);
				if(StringUtils.isBlank(content)){
					content=value;
				}
				value = oldValue + content;
				textComp.setValue(value);
				AppUtil.addAppAttr(WfmConstants.WfmAppAttr_StepOpinionCleared, "true");
			} catch (WfmServiceException e) {
				LfwLogger.error(e.getMessage(), e);
			}

		}
	}
	
	/**
	 * 响应审批操作状态切换时，相关操作按钮的可用状态
	 * @param textEvent
	 * TODO 不需要执行script，直接操作模型类
	 */
	public void textValueChanged(TextEvent textEvent) {
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_ExeAction, null);
		this.addApproveInfo();
		String operator = WfmTaskUtil.getOperatorFromSession();
		//String str = null;
		UIMeta uimeta = AppUtil.getViewCtx(getPubView()).getUIMeta();
		
		Object card1=uimeta.findChildById("card1");
		if (WfmConstants.WfmOperator_Agree.equalsIgnoreCase(operator) || WfmConstants.WfmOperator_Read.equals(operator)) {
			this.setAgreeState();
			if(card1!=null){
				((UICardLayout)card1).setCurrentItem("0");
			}
			//str = "if(window.pageUI.getWidget('pubview_exetask') && window.pageUI.getWidget('pubview_exetask') != null) window.pageUI.getWidget('pubview_exetask').getCard('card1').setPage(0)";
		} else if (WfmConstants.WfmOperator_UnAgree.equalsIgnoreCase(operator)) {
			this.setUnAgreeState();
			if(card1!=null){
				((UICardLayout)card1).setCurrentItem("0");
			}
			//str = "if(window.pageUI.getWidget('pubview_exetask') && window.pageUI.getWidget('pubview_exetask') != null) window.pageUI.getWidget('pubview_exetask').getCard('card1').setPage(0)";
		} else if (WfmConstants.WfmOperator_Reject.equalsIgnoreCase(operator)) {
			this.setRejectState();
			if(card1!=null){
				((UICardLayout)card1).setCurrentItem("0");
			}
			//str = "if(window.pageUI.getWidget('pubview_exetask') && window.pageUI.getWidget('pubview_exetask') != null) window.pageUI.getWidget('pubview_exetask').getCard('card1').setPage(0)";
		} else if ("stop".equalsIgnoreCase(operator)) {
			setSotpState();
			if(card1!=null){
				((UICardLayout)card1).setCurrentItem("0");
			}
			//str = "if(window.pageUI.getWidget('pubview_exetask') && window.pageUI.getWidget('pubview_exetask') != null) window.pageUI.getWidget('pubview_exetask').getCard('card1').setPage(0)";
		} else if (WfmConstants.WfmOperator_Transmit.equalsIgnoreCase(operator)) {
			this.setTransmitState();
			if(card1!=null){
				((UICardLayout)card1).setCurrentItem("1");
			}
			//str = "if(window.pageUI.getWidget('pubview_exetask') && window.pageUI.getWidget('pubview_exetask') != null) window.pageUI.getWidget('pubview_exetask').getCard('card1').setPage(1)";
		} else if (WfmConstants.WfmOperator_BeforeAddSign.equalsIgnoreCase(operator)) {
			this.setBeforeAddSignState();
			if(card1!=null){
				((UICardLayout)card1).setCurrentItem("2");
			}
			//str = "if(window.pageUI.getWidget('pubview_exetask') && window.pageUI.getWidget('pubview_exetask') != null) window.pageUI.getWidget('pubview_exetask').getCard('card1').setPage(2)";
		}
		//AppUtil.getCntAppCtx().addExecScript(str);
		
		// 清空审批公共view的后加签人
		String isNC = (String) AppUtil.getAppAttr(WfmConstants.WfmAppAttr_IsNC);
		if (!"Y".equals(isNC)) {
			ViewContext viewContext = AppUtil.getCntWindowCtx().getViewContext("pubview_exetask");
			if (viewContext == null) {
				viewContext = AppUtil.getCntWindowCtx().getViewContext("pubview_approveeexetask");
			}
			if (viewContext != null) {
				LabelComp textComp = (LabelComp) viewContext.getView().getViewComponents().getComponent(ExecuteTaskWidgetProvider.VALUE_AFTERADDSIGN);
				if(textComp != null){
					textComp.setText("");
				}
				LabelComp nameTextComp = (LabelComp) viewContext.getView().getViewComponents().getComponent(ExecuteTaskWidgetProvider.NAME_AFTERADDSIGN);
				if(nameTextComp != null){
					nameTextComp.setText("");
				}
				
				LabelComp label_afteraddsign = (LabelComp) viewContext.getView().getViewComponents().getComponent(ExecuteTaskWidgetProvider.LABEL_AFTERADDSIGN);
				if(label_afteraddsign != null){
					label_afteraddsign.setVisible(false);
				}
			}
		}
	}
	
	
	/**
	 * 终止的时候设置状态
	 */
	public void setSotpState() {
		WfmEngineCmdAdapterFactory.getInstance().setSotpState();
	}
	
	public void setAgreeState() {
		WfmEngineCmdAdapterFactory.getInstance().setAgreeState();
	}

	
	public void setUnAgreeState() {
		WfmEngineCmdAdapterFactory.getInstance().setUnAgreeState();
	}
	public void setRejectState() {
		WfmEngineCmdAdapterFactory.getInstance().setRejectState();
	}
	public void setTransmitState() {
		WfmEngineCmdAdapterFactory.getInstance().setTransmitState();
	}
	/**
	 * 
	 */
	public void setBeforeAddSignState() {
		WfmEngineCmdAdapterFactory.getInstance().setBeforeAddSignState();
	}
	/**
	 * 设置界面按钮状态,控件机制导致只能先将创建可视控件再将其隐藏掉
	 */
	private void setUIState(){
		UIMeta uimeta = AppUtil.getViewCtx(getPubView()).getUIMeta();
		//抄送panel
		UIFlowvPanel panel = (UIFlowvPanel) uimeta.findChildById(ExecuteTaskWidgetProvider.DYN_FLOWVPANEL1);
		if(panel != null)
			panel.setVisible(false);
		//附件列表panel
		UIFlowvPanel pane2 = (UIFlowvPanel) uimeta.findChildById(ExecuteTaskWidgetProvider.DYN_FLOWVPANEL2);
		if(pane2 != null)
			pane2.setVisible(false);
		//加签用户panel
		UIFlowvPanel pane3 = (UIFlowvPanel) uimeta.findChildById(ExecuteTaskWidgetProvider.DYN_FLOWVPANEL3);
		if(pane3 != null)
			pane3.setVisible(false);
		//指派
		UIFlowhPanel panel4 = (UIFlowhPanel) uimeta.findChildById(ExecuteTaskWidgetProvider.LINK_ASSIGNUSER);
		if(panel4 != null)
			panel4.setVisible(false);
		

	}
}
