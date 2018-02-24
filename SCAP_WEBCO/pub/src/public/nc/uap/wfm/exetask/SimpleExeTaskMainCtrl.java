package nc.uap.wfm.exetask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.OpenProperties;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.wfm.pubview.ExecuteTaskWidgetProvider;
import nc.uap.wfm.utils.WfmProDefUtil;
import nc.uap.wfm.utils.WfmTaskUtil;
import nc.uap.wfm.utils.WfmUtilFacade;
import nc.vo.ml.NCLangRes4VoTransl;
import uap.web.bd.pub.AppUtil;

public class SimpleExeTaskMainCtrl extends ApproveExeTaskMainCtrl {
	public static final String CP_DOCFORMNODE = "cp_docformnode";
	public static final String PUBVIEW_APPROVEEEXETASK = "pubview_approveeexetask";
	/**
	 * 获得当前审批公共view的id
	 * @return
	 */
	protected String getPubView() {
		return WfmConstants.WfmPubView_Simple_ExeTask;
	}
	/**
	 * 提交
	 * @param mouseEvent
	 */
	public void simpleBtnok_click(MouseEvent<MenuItem> mouseEvent) {
//		okClick(ExecuteTaskWidgetProvider.EXE_AGREE);
		okClick(null);
	}
	//add by zhaohb同一个window引入两个控制类具有继承关系的view，一个事件会触发两遍
	@Override
	public void beforeShow(DialogEvent e){
		List<Map<String, String>>  list=AppLifeCycleContext.current().getGroupParamMapList();
		boolean containFatherPage=false;
		if(list!=null)
			for (Map<String, String> map : list) {
				if(ApproveExeTaskMainCtrl.class.getName().equals((String)map.get("clc"))){
					containFatherPage=true;
				}
			}
		if(!containFatherPage)
			super.beforeShow(e);
	}
	private void okClick(String exeAction) {
		String taskPk = WfmTaskUtil.getTaskPkFromSession();
		Object task = WfmTaskUtil.getTaskFromSessionCache(taskPk);
		Object human = null;
		if (task == null) {
			//没有任务则制单时，取开始活动
			Object prodef = WfmUtilFacade.featchProdefFromSession();
			human = WfmProDefUtil.getStartHumanAct(prodef);
		} else {
			human = WfmTaskUtil.getHumActByTask(task);
		}
		
		Boolean isAllowEdit = WfmProDefUtil.allowOpinionEditByHumAct(human);
		Boolean needOpinion = WfmProDefUtil.isOpinionNeedByHumAct(human);
		//制单态必输以及审批环节可编辑意见
		if ((task==null&&needOpinion)||(task!=null&&isAllowEdit)) {
			Object hasInputOpinion = (Object) AppUtil.getAppAttr(WfmConstants.WfmAppAttr_HasInputOpinion);
			if (hasInputOpinion == null) {
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("model", "nc.uap.wfm.pubview.SimpleWfmOpionionPageModel");
				paramMap.put("action", exeAction);
				paramMap.put("needOpinion", needOpinion.booleanValue()?"Y":"N");
				OpenProperties props = new OpenProperties();
				props.setWidth("520");
				props.setHeight("300");
				props.setOpenId("opinion");
				props.setTitle(NCLangRes4VoTransl.getNCLangRes().getStrByID("wfm", "DispStrategy-000088")/*意见*/);
				props.setParamMap(paramMap);
				AppLifeCycleContext.current().getViewContext().navgateTo(props);
			} else { 
				AppUtil.addAppAttr(WfmConstants.WfmAppAttr_HasInputOpinion, null);
				AppUtil.addAppAttr(WfmConstants.WfmAppAttr_ExeAction, exeAction);
				super.btnok_click(null);
			}
		} else {
			AppUtil.addAppAttr(WfmConstants.WfmAppAttr_ExeAction, exeAction);
			super.btnok_click(null);
		}
	}

	/**
	 * 暂存
	 * @param mouseEvent
	 */
	public void simpleBtnsave_click(MouseEvent<MenuItem> mouseEvent) {
		super.btnsave_click(null);
	}
	
//	/**
//	 * 关闭时提交链接、指派时调用
//	 * @param e
//	 */
//	public void titleok_click(ScriptEvent e) {
//		super.titleok_click(null);
//	}
	
	/**
	 * 附件
	 * @param linkEvent
	 */
	public void simpleAttachClick(MouseEvent<MenuItem> mouseEvent) {
		super.attachClick(null);
	}
	
	/**
	 * 流程进度
	 * @param mouseEvent
	 */
	public void simpleFlowImageClick(MouseEvent<MenuItem> mouseEvent) {
		super.flowImgClick(null);
	}
	
	/**
	 * 收回
	 * @param mouseEvent
	 */
	public void simplerecallClick(MouseEvent<MenuItem> mouseEvent) {
		super.btnrecall_click(null);
	}
	
	/**
	 * 阅毕
	 * @param mouseEvent
	 */
	public void simpleReadEndClick(MouseEvent<MenuItem> mouseEvent) {
		super.btnreadend_click(null);
	}
	
	/**
	 * 驳回
	 * @param mouseEvent
	 */
	public void simpleRejectClick(MouseEvent<MenuItem> mouseEvent) {
		okClick(ExecuteTaskWidgetProvider.EXE_REJECT);
	}
	
}
