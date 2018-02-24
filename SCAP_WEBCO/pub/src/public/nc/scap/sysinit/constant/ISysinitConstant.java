package nc.scap.sysinit.constant;

public interface ISysinitConstant {
	
	/*当前环境所属省份*/
	public static final String PROVINCE_ID = "provinceId";
	public static final String HN = "HN";
	public static final String AH = "AH";
	public static final String ZJ = "ZJ";
	public static final String XJ = "XJ";
	public static final String SC = "SC";
	public static final String DL = "DL";
	public static final String FJ = "FJ";
	public static final String JX = "JX";
	public static final String PRODUCT = "PRODUCT";

	/*单位显示全称或简称*/
	public static final String ORG_SHOWTYPE = "ORG_SHOWTYPE";
	public static final String ALL = "ALL";
	public static final String SHORTNAME = "SHORTNAME";
	
	/*总会评委人员个数*/
	public static final String PWSUM = "pwsum";
	
	public static final String SAVEPWD = "savepwd";
	
	/*是否控制组织参照、组织树过滤;系统参数编码为ORG_FILTER;过滤值为"Y",不过滤值为"N"*/
	public static final String ORG_FILTER = "ORG_FILTER";
	public static final String ORG_FILTER_TRUE = "Y";
	public static final String ORG_FILTER_FALSE = "N";
	
	/*监事会监督检查填报是否需要意见参照;系统参数编码为yjcz;过滤值为"Y",不过滤值为"N"*/
	public static final String YJCZ = "yjcz";
	public static final String YJCZ_Y = "Y";
	public static final String YJCZ_N = "N";
	
	/**财务分析是否显示本部,true时是显示false时不显示**/
	public static final String ISSHOWCORP = "isShowCorp";
	
	/**预警组织是否选择报表组织体系,true时是显示false时不显示**/
	public static final String ISSHOWReportOrg = "isShowRepotOrg";
	
	
	/*人事管理中培训和任免是否放开编辑功能*/
	public static final String PM_TRAIN = "trainID";
	public static final String PM_DURY = "dutyID";
	public static final String PM_Y = "Y";
	public static final String PM_N = "N";
}
