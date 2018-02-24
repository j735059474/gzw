package nc.uap.wfm.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import nc.bs.uap.oid.OidGenerator;
import nc.uap.cpb.org.exception.CpbBusinessException;
import nc.uap.cpb.org.itf.ICpUserQry;
import nc.uap.cpb.org.util.CpbServiceFacility;
import nc.uap.cpb.org.vos.CpUserVO;
import nc.uap.lfw.core.cmd.UifPlugoutCmd;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.ApplicationContext;
import nc.uap.lfw.core.ctx.OpenProperties;
import nc.uap.lfw.core.ctx.ViewContext;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.ViewComponents;
import nc.uap.lfw.file.FileManager;
import nc.uap.lfw.file.LfwFileConstants;
import nc.uap.lfw.file.UploadFileHelper;
import nc.uap.lfw.file.vo.LfwFileVO;
import nc.uap.lfw.format.LfwFormater;
import nc.uap.wfm.adapter.factory.WfmEngineCmdAdapterFactory;
import nc.uap.wfm.adapter.factory.WfmEngineUIAdapterFactory;
import nc.uap.wfm.adapter.itf.IWfmEngineCmdAdapter;
import nc.uap.wfm.adapter.itf.IWfmEngineUIAdapter;
import nc.uap.wfm.exe.WfmBatchNextInfoCmd;
import nc.uap.wfm.logger.WfmLogger;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.format.FormatResult;
import nc.vo.pub.format.exception.FormatException;
import nc.vo.pub.lang.UFDateTime;
import org.apache.commons.lang.StringUtils;
import uap.lfw.core.ml.LfwResBundle;
import uap.web.bd.pub.AppUtil;
import uap.web.bd.pub.user.CpUserManager;

public class WfmUtilFacade
{
  public static final String POP_NCFLOWIMGWIDTH = "1000";
  public static final String POP_CPFLOWIMGWIDTH = "1100";

  public static Object getFormVO(String pk_flwtype, String pk_formins)
  {
    return WfmEngineUIAdapterFactory.getInstance().getFormVo(pk_flwtype, pk_formins);
  }
  public static boolean isThereActtach(Object task) {
    String billtype = "wfm_attach";
    String billitem = WfmProinsUtil.getProInsPkByProIns(WfmTaskUtil.getProInsByTask(task));
    FileManager filemanager = FileManager.getSystemFileManager(null);
    LfwFileVO[] files = null;
    try {
      files = filemanager.getAttachFileByItemID(billitem, billtype);
    } catch (LfwBusinessException e) {
      WfmLogger.error(e.getMessage(), e);
    }
    if ((files == null) || (files.length == 0))
      return false;
    return true;
  }
  public static void recallHandle(String viewId) {
    LfwView view = AppUtil.getView(viewId);
    addRecallBackInfo(view);
    new UifPlugoutCmd(viewId, "plugout_exetask").execute();
  }

  public static void addRecallBackInfo(LfwView view)
  {
    AppUtil.addAppAttr("$$$$$$$$EXEACTION", "recallBack");
    AppUtil.addAppAttr("$$$$$$$$Opinion", getOpinionByView(view));
    AppUtil.addAppAttr("$$$$$$$$transmituserpk", getTransmitUserPk());
  }

  public static String getTransmitUserPk()
  {
    ViewContext viewCtx = AppLifeCycleContext.current().getViewContext();
    LfwView view_exetask = viewCtx.getView();
    return getTransmitUserPk(view_exetask);
  }
  public static String getTransmitUserPk(LfwView view_exetask) {
    TextComp text_transmituser = (TextComp)view_exetask.getViewComponents().getComponent("text_transmituser");
    String value = null;
    if (text_transmituser != null) {
      value = text_transmituser.getValue();
    }
    return value;
  }

  public static String getOpinion()
  {
    ViewContext viewCtx = AppLifeCycleContext.current().getViewContext();
    LfwView view_exetask = viewCtx.getView();
    String value = getOpinionByView(view_exetask);
    return value;
  }
  public static String getOpinionByView(LfwView view) {
    TextComp text_opinion = (TextComp)view.getViewComponents().getComponent("text_opinion");

    String value = null;
    if (text_opinion != null)
      value = text_opinion.getValue();
    else {
      value = (String)AppUtil.getAppAttr("$$$$$$$$Opinion");
    }
    return value;
  }

  /** @deprecated */
  public static void batchExecute(String[] taskPks)
  {
    batchExecute(taskPks, "agree", null);
  }

  public static Object batchExecute(String[] taskPks, String operator, String opnion)
  {
    WfmBatchNextInfoCmd cmd = new WfmBatchNextInfoCmd(taskPks);
    cmd.setOpnion(opnion);
    cmd.setOperator(operator);
    cmd.execute();
    return cmd.doBatchTask();
  }

  public static Object featchProdef(String taskPk, String prodefPk, String flowTypePk, String pk_user, String pk_org, String pk_group, Object formvo)
  {
    if (StringUtils.isNotBlank(taskPk))
      return WfmTaskUtil.getProDefByTask(WfmTaskUtil.getTaskFromSessionCache(taskPk));
    if (StringUtils.isNotBlank(prodefPk)) {
      return WfmProDefUtil.getProDefByProDefPk(prodefPk);
    }
    return WfmEngineCmdAdapterFactory.getInstance().getProDefByFlowTypeAndOrg(flowTypePk, pk_user, pk_org, pk_group, formvo);
  }

  public static Object featchProdefFromSession()
  {
    String prodefPk = WfmTaskUtil.getProdefPkFromSession();
    String taskPk = WfmTaskUtil.getTaskPkFromSession();
    String flowTypePk = WfmTaskUtil.getFlowTypePkFromSession();
    String pk_user = WfmTaskUtil.getCurUserPkFromSession();
    String pk_org = WfmTaskUtil.getCurOrgPkFromSession();
    String pk_group = WfmTaskUtil.getCurGroupPkFromSession();
    Object formInfoCtx = WfmTaskUtil.getWfmFormInfoCtxFromSession();
    return featchProdef(taskPk, prodefPk, flowTypePk, pk_user, pk_org, pk_group, formInfoCtx);
  }

  public static int getFileMgrStateByHumAct(Object humact, Object task)
  {
    int state = 0;
    if (humact == null) {
      state = 31;
    } else {
      state += (WfmProDefUtil.allowAttachUploadByHumAct(humact) ? 16 : 0);
      state += (WfmProDefUtil.allowAttachDownloadByHumAct(humact) ? 8 : 0);
      if (task != null) {
        state += ((!WfmTaskUtil.isAddSignOrAssist(task)) && (WfmProDefUtil.allowAttachDeleteByHumAct(humact)) ? 4 : 0);
        state += ((!WfmTaskUtil.isAddSignOrAssist(task)) && (WfmProDefUtil.allowAttachEditByHumAct(humact)) ? 2 : 0);
      } else {
        state += (WfmProDefUtil.allowAttachDeleteByHumAct(humact) ? 4 : 0);
        state += (WfmProDefUtil.allowAttachEditByHumAct(humact) ? 2 : 0);
      }

      state += (WfmProDefUtil.allowAttachLookByHumAct(humact) ? 1 : 0);
    }
    return state;
  }

  public static Object getHumAct(String taskPk)
  {
    Object humact = null;
    if (StringUtils.isNotBlank(taskPk)) {
      Object task = WfmTaskUtil.getTaskFromSessionCache(taskPk);
      if (task == null)
        throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("wfm", "WfmCmd-000008"));
      humact = WfmEngineUIAdapterFactory.getInstance().getHumActByTask(task);
    } else {
      Object prodef = featchProdefFromSession();
      humact = WfmProDefUtil.getStartHumanAct(prodef);
    }
    return humact;
  }

  public static Map<String, String> getFileMgrParamsbyBillitem(String billitem)
  {
    Map paramMap = null;

    paramMap = getFileMgrParamsbyTaskPk(null, billitem);

    return paramMap;
  }

  public static Map<String, String> getFileMgrParamsByTask(String pk_task)
  {
    Map paramMap = null;
    if (StringUtils.isNotBlank(pk_task))
    {
      String billitem = WfmTaskUtil.getBillItemByTaskpk(pk_task);
      paramMap = getFileMgrParamsbyTaskPk(pk_task, billitem);
    }
    else {
      String pk_proins = null;
      if (AppUtil.getAppAttr("AttachFileList_Temp_Billitem") == null) {
        pk_proins = OidGenerator.getInstance().nextOid();
        AppUtil.addAppAttr("AttachFileList_Temp_Billitem", pk_proins);
      } else {
        pk_proins = (String)AppUtil.getAppAttr("AttachFileList_Temp_Billitem");
      }
      paramMap = getFileMgrParamsbyTaskPk(null, pk_proins);
    }

    return paramMap;
  }

  public static String getFileMgrUrlbyTaskPk(String taskPk)
  {
    Map map = getFileMgrParamsbyTaskPk(taskPk, null);
    String url = "app/mockapp/filemgr?";
    return getUrlByParam(url, map);
  }
  public static String getUrlByParam(String url, Map<String, String> map) {
    if (url.indexOf("?") < 0)
      url = url + "?";
    if (map != null) {
      Iterator entryIt = map.entrySet().iterator();
      while (entryIt.hasNext()) {
        Map.Entry entry = (Map.Entry)entryIt.next();
        url = url + "&" + (String)entry.getKey() + "=" + (String)entry.getValue();
      }
    }
    return url;
  }

  public static String getProinsPkFromSession(String taskPk) {
    String pk_proins = null;
    if (StringUtils.isNotBlank(taskPk)) {
      Object task = WfmTaskUtil.getTaskFromSessionCache(taskPk);
      if (task == null)
        throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("wfm", "WfmCmd-000008"));
      pk_proins = WfmEngineUIAdapterFactory.getInstance().getProinsPkByTask(task);
    }
    return pk_proins;
  }

  public static String getTaskPkPkByPkFormIns(String pk_formIns) {
    String taskPk = null;
    Object proins = WfmProinsUtil.getProinsByPkFormIns(pk_formIns);
    if (proins != null) {
      Object[] tasks = WfmTaskUtil.getTaskVosByPkProinsAndOnwer(WfmProinsUtil.getProInsPkByProIns(proins), CpUserManager.getPk_user());
      if ((tasks != null) && (tasks.length > 0)) {
        taskPk = WfmTaskUtil.getTaskPkByTaskVO(tasks[0]);
      }
    }
    return taskPk;
  }

  public static Map<String, String> getFileMgrParamsbyTaskPk(String taskPk, String billitem)
  {
    String usescanable = "false";
    String printAble = "false";
    String editOnlieAble = "false";
    String sysid = (String)AppUtil.getAppAttr("sysid");
    String billtype = "wfm_attach";
    int state = 0;
    String billState = (String)AppUtil.getAppAttr("$$$$$$billstate");
    String bamodule = (String)AppUtil.getAppAttr("bamodule");
    Object task = WfmTaskUtil.getTaskFromSessionCache(taskPk);
    if ("billstate_browse".equals(billState)) {
      if (WfmProinsUtil.isEndStateProins(WfmTaskUtil.getProInsByTask(task))) {
        state++;
        Object prodef = WfmTaskUtil.getProDefByTask(task);
        if (WfmProDefUtil.allowAttachDownloadAfterEnd(prodef)) {
          state += 8;
        }
        if (WfmProDefUtil.allowAttachDeleteAfaterEnd(prodef))
          state += 4;
      }
      else
      {
        state += 9;
      }
    } else if (StringUtils.isNotBlank(taskPk))
    {
      if ((task != null) && ((WfmTaskUtil.isUnreadState(task)) || (WfmTaskUtil.isReadedState(task)) || (WfmTaskUtil.isReadEndState(task)) || (WfmTaskUtil.isEndState(task)) || (WfmTaskUtil.isSuspendedState(task)) || (WfmTaskUtil.isCanceledState(task))))
      {
        if (WfmTaskUtil.allowAttachLook(task))
          state++;
      }
      else {
        Object humact = getHumAct(taskPk);
        printAble = WfmProDefUtil.allowAttachPrintByHumAct(humact) ? "true" : "false";
        editOnlieAble = (!WfmTaskUtil.isAddSignOrAssist(task)) && (WfmProDefUtil.allowAttachEditOnlineByHumAct(humact)) ? "true" : "false";
        usescanable = WfmEngineUIAdapterFactory.getInstance().allowUseScanByHumAct(humact) ? "true" : "false";
        state += getFileMgrStateByHumAct(humact, task);
      }
    } else {
      String pk_prodef = (String)AppUtil.getAppAttr("$$$$$$$$PRODEFPK");
      if (StringUtils.isNotBlank(pk_prodef))
      {
        Object humact = getHumAct(null);
        printAble = WfmProDefUtil.allowAttachPrintByHumAct(humact) ? "true" : "false";
        editOnlieAble = WfmProDefUtil.allowAttachEditOnlineByHumAct(humact) ? "true" : "false";
        state += getFileMgrStateByHumAct(humact, null);
        usescanable = WfmEngineUIAdapterFactory.getInstance().allowUseScanByHumAct(humact) ? "true" : "false";
      }
      else {
        state = 31;
      }
    }
    String operateClazz = "";
    if (AppUtil.getAppAttr(LfwFileConstants.Filemgr_Para_OperateClazz) != null) {
      operateClazz = (String)AppUtil.getAppAttr(LfwFileConstants.Filemgr_Para_OperateClazz);
    }
    if (StringUtils.isBlank(sysid)) {
      throw new LfwRuntimeException(LfwResBundle.getInstance().getStrByID("wfm", "WfmUtilFacade-000008"));
    }

    Map paramMap = UploadFileHelper.BuildDefaultPamater(sysid, billitem, billtype, bamodule);
    paramMap.put("state", String.valueOf(state));
    paramMap.put("usescanable", usescanable);
    paramMap.put("printable", printAble);
    paramMap.put("editonlieable", editOnlieAble);
    paramMap.put("operateClazz", operateClazz);

    return paramMap;
  }

  public static boolean isContainsTaskByBillId(String billId)
  {
    return WfmEngineCmdAdapterFactory.getInstance().isContainsTaskByBillId(billId);
  }

  public static void activeProins(String proinsPK, String reason)
  {
    WfmEngineCmdAdapterFactory.getInstance().activeProins(proinsPK, reason);
  }

  public static void suspendProins(String proinsPK, String reason)
  {
    WfmEngineCmdAdapterFactory.getInstance().suspendProins(proinsPK, reason);
  }

  public static Object getProDefByFlowType(String pk_flwtype, String pk_user, String pk_curOrg, String pk_group)
  {
    return WfmEngineCmdAdapterFactory.getInstance().getProDefByFlowTypeAndOrg(pk_flwtype, pk_user, pk_curOrg, pk_group, WfmTaskUtil.getWfmFormInfoCtxFromSession());
  }

  public static String getWordUrlByFrmDefPk(String frmDefPk) {
    return null;
  }
  public static String getWordUrlbyTaskPk(String taskPk) {
    return null;
  }
  public static LfwFileVO[] getAttachFiles(String proInsPk, String sysid) {
    if (StringUtils.isBlank(proInsPk)) return null;
    FileManager filemanager = FileManager.getSystemFileManager(sysid);
    LfwFileVO[] files = null;
    try {
      files = filemanager.getAttachFileByItemID(proInsPk, "wfm_attach");
    } catch (Exception e) {
      WfmLogger.error(e.getMessage(), e);
    }
    return files;
  }

  public static StringBuffer generateHistoryContent()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("<div style='overflow-y:auto'>");
    WfmTaskUtil.addParamIntoSession();
    String taskPk = (String)AppUtil.getAppAttr("$$$$$$$$TaskPk");
    buffer.append("<table style='width:100%'>");
    String isNcWorkFlow = WfmTaskUtil.isNc() == null ? null : WfmTaskUtil.isNc();

    String isMore = AppUtil.getAppAttr("More") == null ? null : (String)AppUtil.getAppAttr("More");
    HashMap params = new HashMap(4);
    if ("Y".equals(isNcWorkFlow)) {
      String billType = WfmTaskUtil.getFlowTypePkFromSession();
      String billId = WfmTaskUtil.getBillIdFromSession();
      params.put("$$$$$$$$FLOWTYPEPK", billType);
      params.put("billId", billId);
      StringBuffer resultStr = WfmTaskUtil.getHistoryStr(params);
      buffer.append(resultStr);
    }
    else if ((taskPk != null) && (taskPk.length() != 0)) {
      String billId = WfmTaskUtil.getBillIdFromSession();
      params.put("billId", billId);
      params.put("More", isMore);
      params.put("$$$$$$$$TaskPk", taskPk);
      StringBuffer resultStr = WfmTaskUtil.getHistoryStr(params);
      buffer.append(resultStr);
    } else {
      buffer.append("<tr><td align='center' style='color#121212;font-weight:normal;'>");
      buffer.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("wfm", "WebPartContentFetcherImpl-000004"));
      buffer.append("</tr></td>");
    }
    buffer.append("</table>");
    buffer.append("</div>");
    return buffer;
  }

  public static void generateHistory(StringBuffer buffer, String executer, String endDate, String approveResult, String opinion, Object task) {
    buffer.append("<tr>");
    buffer.append("<td width='14px'>").append("<img src='images/contact.png' style='height:13px;width:13px'>").append("</td>");
    buffer.append("<td align='left' width='90px'  style='color#121212;font-weight:bold;'>");

    CpUserVO userVO = null;
    try {
      userVO = CpbServiceFacility.getCpUserQry().getGlobalUserByPk(executer);
    } catch (CpbBusinessException e) {
      WfmLogger.error(e.getMessage(), e);
    }
    buffer.append("<font style='color#121212;font-weight:normal;'>").append(userVO == null ? "" : userVO.getUser_name()).append("</font>");
    buffer.append("</td>");
    buffer.append("<td width='140px' style='color#121212;font-weight:normal;' align='right'>").append(getPubDate(endDate)).append("</td>");
    buffer.append("</tr>");
    buffer.append("<tr>");
    buffer.append("<td width='14px'></td>");
    buffer.append("<td colspan='3'>");
    buffer.append("<span style='color:red;font-weight:bold;margin-left:0px;'>");
    if (approveResult != null) {
      String rstr = getResutltString(approveResult, task);
      buffer.append(rstr);
    }
    buffer.append("&nbsp;&nbsp;").append("</span></br>");
    opinion = "&nbsp;&nbsp;" + opinion;
    buffer.append("<font style='color#121212;font-weight:normal;'>" + opinion + "</font>");
    buffer.append("</td>");
    buffer.append("</tr>");
  }

  private static String getPubDate(Object pubDate)
  {
    try {
      String pubDateSt = pubDate == null ? null : pubDate.toString();
      if (StringUtils.isBlank(pubDateSt)) return "";

      return LfwFormater.formatDateTime(new UFDateTime(pubDateSt)).getValue(); } catch (FormatException e) {
    }
    return pubDate.toString();
  }

  private static String getResutltString(String approveResult, Object task)
  {
    String rstr = "";
    boolean isNC = WfmBillUtil.isNCBill();

    if (approveResult.equals("R"))
    {
      rstr = WfmEngineUIAdapterFactory.getInstance().getTaskDispName("disp_reject", task);
    }
    else if (approveResult.equals("D")) {
      rstr = WfmEngineUIAdapterFactory.getInstance().getTaskDispName("disp_readover", task);
    }
    else if (approveResult.equals("Y")) {
      rstr = WfmEngineUIAdapterFactory.getInstance().getTaskDispName("disp_agree", task);
      if ((WfmEngineUIAdapterFactory.getInstance().isBillMakerStatus(task)) && (!isNC)) {
        rstr = "发起";
      }
    }
    else if (approveResult.equals("N")) {
      rstr = WfmEngineUIAdapterFactory.getInstance().getTaskDispName("disp_unagree", task);
    }
    else if (approveResult.equals("T"))
    {
      rstr = WfmEngineUIAdapterFactory.getInstance().getTaskDispName("disp_transmit", task);
    }
    else if (approveResult.equals("S")) {
      rstr = NCLangRes4VoTransl.getNCLangRes().getStrByID("wfm", "WebPartContentFetcherImpl-0000010");
    }
    else if (approveResult.equals("C")) {
      rstr = NCLangRes4VoTransl.getNCLangRes().getStrByID("wfm", "WebPartContentFetcherImpl-0000011");
    } else if (approveResult.equals("READ")) {
      rstr = WfmEngineUIAdapterFactory.getInstance().getTaskDispName("disp_read", task);
      if ((WfmTaskUtil.getParentTask(task) == null) && (!isNC))
        rstr = NCLangRes4VoTransl.getNCLangRes().getStrByID("wfm", "WebPartContentFetcherImpl-0z00012");
    }
    else if (approveResult.equals("AUTO")) {
      rstr = NCLangRes4VoTransl.getNCLangRes().getStrByID("wfm", "WebPartContentFetcherImpl-0z00013");
    }
    return rstr;
  }
  public static void closeWfmWindow() {
    AppLifeCycleContext.current().getApplicationContext().closeWinDialog("wfl");
    AppLifeCycleContext.current().getApplicationContext().closeWinDialog("assignuser");
    AppLifeCycleContext.current().getApplicationContext().closeWinDialog("rejectassignuser");
  }

  public static void closeWfmWindowSelf()
  {
    AppLifeCycleContext.current().getApplicationContext().closeWinDialog("assignuser");
    AppLifeCycleContext.current().getApplicationContext().closeWinDialog("rejectassignuser");
  }

  public static void openFlowImage() {
    String windowId = "wfm_flowhistory";
    String isNcWorkFlow = (String)AppUtil.getAppAttr("NC");
    Map paramMap = new HashMap();

    if ("Y".equals(isNcWorkFlow)) {
      windowId = "pfinfo";
    } else {
      isExistFloInf();

      String taskPk = WfmTaskUtil.getTaskPkFromSession();
      if (StringUtils.isBlank(taskPk)) {
        taskPk = (String)AppUtil.getAppAttr("returnTaskPk");
        AppUtil.addAppAttr("$$$$$$$$TaskPk", taskPk);
      }
      Object task = WfmTaskUtil.getTaskFromSessionCache(taskPk);
      if ((taskPk != null) && (!"".equals(taskPk))) {
        String proInsPK = WfmEngineUIAdapterFactory.getInstance().getRootProinsPkByTask(task);
        AppUtil.addAppAttr("$$$$$$$$ProInsId", proInsPK);
      }
    }
    String width = "1100";
    if ("Y".equals(isNcWorkFlow)) {
      width = "1000";
    }
    String title = NCLangRes4VoTransl.getNCLangRes().getStrByID("wfm", "ExeTaskMainCtrl-000020");

    OpenProperties props = new OpenProperties();
    props.setWidth(width);
    props.setHeight("650");
    props.setTitle(title);
    props.setOpenId(windowId);
    props.setParamMap(paramMap);
    AppLifeCycleContext.current().getViewContext().navgateTo(props);
  }

  public static void isExistFloInf()
  {
    String proDefPk = null;
    String billId = WfmTaskUtil.getBillIdFromSession();
    if (StringUtils.isNotBlank(billId)) {
      Object taskVO = WfmEngineUIAdapterFactory.getInstance().getTaskVoByFormPk(billId);
      if (taskVO == null)
        throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("wfm", "FlowImgCtrlnew-000001"));
      proDefPk = WfmEngineUIAdapterFactory.getInstance().getproDefPkByTaskVo(taskVO);
    } else {
      Object prodef = featchProdefFromSession();
      proDefPk = WfmProDefUtil.getProDefPk(prodef);
    }
    if (StringUtils.isNotBlank(proDefPk))
      AppUtil.addAppAttr("$$$$$$$$PRODEFPK", proDefPk);
    else
      throw new LfwRuntimeException(LfwResBundle.getInstance().getStrByID("wfm", "ExeTaskMainCtrl-000024"));
  }

  public static void clearWfmInfo()
  {
    AppUtil.addAppAttr("$$$$$$$$TaskPk", null);
  }
}

/* Location:           F:\GZtesthome\modules\webimp\classes\
 * Qualified Name:     nc.uap.wfm.utils.WfmUtilFacade
 * JD-Core Version:    0.6.2
 */