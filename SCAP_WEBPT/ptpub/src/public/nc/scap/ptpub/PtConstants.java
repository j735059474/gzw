package nc.scap.ptpub;

public interface PtConstants {
	// 产权转让类型与方式
	public static final String GPZR = "gpzr"; // 挂牌转让
	public static final String XYZR = "xyzr";// 协议转让
	public static final String WCZR = "wczr";// 无偿转让
	public static final String BTBLZZ = "btblzz";// 不同比例增资
	public static final String TBLZZ = "tblzz";// 不同比例增资
	public static final String BTBLJZ = "btbljz";// 不同比例减资
	public static final String QYBG = "qybg";// 企业并购
	public static final String QYBGBA = "qybgba";// 企业并购国资委备案
	public static final String ZDZCXYZR = "majoragree";// 重大资产协议转让
	public static final String ZDZCGPZR = "majorlist";// 重大资产挂牌转让
	public static final String ZFZCZL = "majorlease";// 重大资产租赁
	public static final String TDCZ= "tdcz";// 土地资产处置

	//节点编码
	public static final String NODECODE_GPZR = "E9AI020218"; // 挂牌转让
	public static final String NODECODE_XYZR = "E9AI020220";// 协议转让
	public static final String NODECODE_WCZR = "E9AI020203";// 无偿转让
	public static final String NODECODE_BTBLZZ = "E9AI020204";// 不同比例增(减)资
	public static final String NODECODE_BTBLJZ = "E9AI020205";// 产权置换 
	public static final String NODECODE_TDCZ = "E9AI020227";// 土地占有
	public static final String NODECODE_TBLZZ = "E9AI020241";// 同比例增资
	//废弃
	public static final String NODECODE_QYBG = "E9AI020213";// 企业并购
	public static final String NODECODE_ZDZCXYZR = "E9AI020302";// 重大资产协议转让--产权交易
	public static final String NODECODE_ZDZCGPZR = "E9AI020301";// 重大资产挂牌转让
	public static final String NODECODE_ZFZCZL = "E9AI020303";// 重大资产租赁
	
	public static final String NODECODE_JYSQ = "E9AI020218"; // 交易申请
	
	public static final String NODECODE_SQGPZR = "E9AI020216"; // 交易申请挂牌转让
	public static final String NODECODE_SQXYZR = "E9AI020219";// 交易申请协议转让
	public static final String NODECODE_SQWCZR = "E9AI020222";// 交易申请无偿转让
	public static final String NODECODE_SQBTBLZZ = "E9AI020225";// 交易申请不同比例增资
	public static final String NODECODE_SQBTBLJZ = "E9AI020229";// 交易申请不同比例减资
	public static final String NODECODE_SQQYBG = "E9AI020231";// 交易申请企业并购
	public static final String NODECODE_SQZDZCXYZR = "E9AI020307";// 重大资产协议转让--产权交易
	public static final String NODECODE_SQZDZCGPZR = "E9AI020310";// 重大资产挂牌转让--交易申请
	public static final String NODECODE_SQZFZCZL = "E9AI020313";// 重大资产租赁--交易申请
	// 页面编码
	public final static String PAGECODE_MANAGER = "manager";// 管理页面
	public final static String PAGECODE_APPROVE = "approve";// 审核页面
	public final static String PAGECODE_BROWSE = "browse";// 查看页面
	public final static String NODE_TYPE = "node_type";// 节点类型
	public final static String NODE_TYPE2 = "node_type2";// 节点类型
	// 界面参数
	public final static String OPERATIONCMD = "operation";
	public final static String QUERYOPER = "queryoper";
	
	public final static String SCAPPT_CQBD = "scappt_0002";//产权变动类型
	public final static String SCAPPT_CQJY = "scappt_0003";//产权交易类型
	public final static String SCAPPT_ZDZC = "scappt_0004";//重大资产
	public final static String SCAPPT_GZJG = "scappt_0007";//国资监管类型
	public final static String SCAPPT_CQJJ = "scappt_0006";//产权交易经济类型
	public final static String SCAPPT_PGJG = "scappt_0005";//评估机构
	
	public final static String CCHANGE_TYPE_CQZR = "001";//产权
	public final static String CCHANGE_TYPE_BLZZ = "002";//不同比例增资
	public final static String CCHANGE_TYPE_QYBG = "003";//企业并购
	public final static String CCHANGE_TYPE_MAJOR = "004";//重大资产
	public final static String CCHANGE_TYPE_BLJZ = "005";//不同比例减资
	public final static String CCHANGE_TYPE_TBLJZ = "006";//同比例增资
	
	public final static String CTRADETTYPE_GPZR = "001";//挂牌
	public final static String CTRADETTYPE_XYZR = "002";//协议
	public final static String CTRADETTYPE_WCZR = "003";//无偿
	
	
	//pos 重大资产处理方式
	public final static String MAJORLISTMeTHODS = "001";//挂 牌
	public final static String MAJORAGREEMeTHODS = "002";//协议
	public final static String MAJORLEASEMeTHODS = "003";//租赁
	//审批类型
	public final static String SP_HZ = "00101";//核准
	public final static String SP_SQBA = "00103";//事前备案
	public final static String SP_SHBA = "00104";//事后备案
	public final static String SH_HZ = "00101";//核准
	
	//挂牌价格异常提示预警提示信息
	public final static String GPYJ_1 = "交易申请数据的挂牌价格不得低于评估价格的90%；";
	public final static String GPYJ_2 = "交易结果数据的挂牌价格不得低于评估价格的90%；";
	public final static String GPYJ_3 = "交易申请数据和交易结果数据的挂牌价格不一致；";
	public final static String GPYJ_4 = "交易申请数据的挂牌价格和交易结果的成交价格不一致;";
	
	//页面窗口常量
	public final static String WIN_GPZR = "transferComps.trs_gpzr_cardwin";
	public final static String WIN_XYZR = "transferComps.trs_xyzr_cardwin";
	public final static String WIN_WCZR = "transferComps.trs_wczr_cardwin";
	public final static String WIN_QYBG = "transferComps.commerger_cardwin"; //企业并购
	
	
}
