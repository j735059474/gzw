package nc.scap.sysinit.util;

import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.scap.sysinit.constant.ISysinitConstant;
import nc.uap.cpb.org.itf.ICpSysinitQry;
import nc.uap.cpb.org.vos.CpSysinitVO;
import nc.uap.portal.log.ScapLogger;
import nc.vo.pub.BusinessException;
import uap.web.bd.cache.SysInitCacheHelper;

public class ScapSysinitUtil {

	public static Map<String, String> map = new HashMap<String, String>();

	public static String getSysinitStrByCode(String code) {

		String value = null;
		try {
			CpSysinitVO initVO = SysInitCacheHelper.getFormCache(code);
			if (initVO == null) {
				initVO = getSysinitService().getSysinitByCode(code);
				if (initVO != null) {
					value = initVO.getValue();
					SysInitCacheHelper.putCache(code, initVO);
					// value = initVO.getValue();
					return value;
				}
			}
			if (initVO != null)
				value = initVO.getValue();
		} catch (BusinessException e) {
			ScapLogger.error("根据参数编码" + code + "获取系统参数异常");
		}
		return value;
	}
    /**
     * 判断组织参照或组织树是否需要显示单位简称（通过参数设置判断组织是否需要显示简称专用方法，
     * 其他可参照getSysinitStrByCode（String code）方法）
     * @ return true 显示简称
     * @ return false 显示全称
     * add yhl 20130910
     */
	public static boolean isShowShortName() {
		String value = getSysinitStrByCode(ISysinitConstant.ORG_SHOWTYPE);
		if (value != null && value.equals(ISysinitConstant.SHORTNAME))
			return true;
		else
			return false;

	}
	
	 /**
     * 判断组织参照或组织树是否需要显示单位简称（通过参数设置判断组织是否需要显示简称专用方法，
     * 其他可参照getSysinitStrByCode（String code）方法）
     * @ return true 显示简称
     * @ return false 显示全称
     * add yhl 20130910
     */
	public static boolean isFilterOrg() {
		String value = getSysinitStrByCode(ISysinitConstant.ORG_FILTER);
		if (value != null && value.equals(ISysinitConstant.ORG_FILTER_TRUE))
			return true;
		else
			return false;
		
	}

	/**
	 * 获取系统参数接口实例
	 * 
	 * @author taoye 2013-7-16
	 */
	private static ICpSysinitQry getSysinitService() {
		return NCLocator.getInstance().lookup(ICpSysinitQry.class);
	}
	/**
     * 财务分析是否显示本部
     * 
     * @ return true 显示本部
     * @ return false 只显示集团
     * add houlg2014-10-10
     */
	public static boolean isShowCorp() {
		String value = getSysinitStrByCode(ISysinitConstant.ISSHOWCORP);
		if (value != null && value.equals(ISysinitConstant.ORG_FILTER_TRUE))
			return true;
		else
			return false;

	}
	
	/**
     * 预警组织是否选择报表组织体系
     * 
     * @ return true 显示报表组织体系
     * @ return false 业务单元
     * add houlg2014-10-23
     */
	public static boolean isShowRepotOrg() {
		String value = getSysinitStrByCode(ISysinitConstant.ISSHOWReportOrg);
		if (value != null && value.equals(ISysinitConstant.ORG_FILTER_TRUE))
			return true;
		else
			return false;

	}
}
