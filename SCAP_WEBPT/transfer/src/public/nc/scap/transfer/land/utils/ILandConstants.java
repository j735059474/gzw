package nc.scap.transfer.land.utils;

public interface ILandConstants {
	//人民币
	public static final String PK_RMB = "1002Z0100000000001K1";
	//单据状态
	public static final String FOMRSTATE_END = "End";//结束
	public static final String FOMRSTATE_NOTSTART = "NottStart";// 拟稿中、
	public static final String FOMRSTATE_RUN = "Run";// 运行中
	public static final String FOMRSTATE_SUSPENDED = "Suspended";// 挂起
	public static final String FOMRSTATE_CANCELLATION = "Cancellation";// 作废
	
	// 带流程的审批状态
	public final static String APPROVESTATE_ADD = "0";//新增
	public final static String APPROVESTATE_APPROVING = "1";//审批中
	public final static String APPROVESTATE_BACK = "2";//驳回
	public final static String APPROVESTATE_APPROVED = "3";//已审批
	public final static String APPROVESTATE_CANCELLATION = "4";//作废
	//节点参数
	public static final String IS_AUDIT = "isAudit";
	public static final String AUDIT_FALSE = "false";//填报
	public static final String AUDIT_VIEW = "view";//查看
	public static final String AUDIT_TRUE = "true";//审核
	
	//土地主表FORM
	public static final String FORM_ID_LAND = "scappt_land_form";
	
	//DTASET
	public static final String DATASET_ID_LAND = "scappt_land";

	//主VIEW
	public static final String MAIN_VIEW_ID="main"; 

	//参数
	public static final String SAVE_OPE = "OPE";//保存标志
	public static final String SAVE_SIGN_ADD = "add";
	public static final String SAVE_SIGN_EDIT = "edit";
	public static final String SAVE_SIGN_VIEW = "view";
	public static final String SAVE_SIGN_AUDIT = "audit";
	
	public static final String SIGN_FINEREPORT = "fineReport"; // FineReport标志
	
	//按钮
	public static final String MENUBAR = "menubar";
	public static final String EXEMENUBAR = "simpleExeMenubar";
	
	public static final String BTN_ADD = "add";//新增按钮
	public static final String BTN_EDIT = "edit";//修改按钮
	public static final String BTN_DEL = "del";//删除按钮
	
	public static final String BTN_ATTACHFILE = "attachfile";//附件按钮
	public static final String BTN_PRINT="print"; //打印按钮
	public static final String BTN_QYTY = "startorstrop";//启用停用
	
	
	public static final String BTN_COPY="copy"; //复制按钮
	public static final String BTN_SAVE="save"; //保存按钮
	public static final String BTN_BACK="back"; //返回按钮
	public static final String BTN_AUDIT="audit"; //审核按钮
	public static final String BTN_WF="wf"; //流程按钮
	public static final String BTN_SYNCHRO="synchro";//同步按钮
	public static final String BTN_CANCEL="cancel";//撤销按钮
	public static final String BTN_VIEW="view";//查看按钮
	
	public static final String BTN_ZANCUN = "btn_save";
	public static final String BTN_TIJIAO = "btn_ok";
	public static final String BTN_FUJIAN = "link_addattach";
	public static final String BTN_LIUCHENG = "allFlow";
	public static final String BTN_BOHUI = "reject";
	
}
