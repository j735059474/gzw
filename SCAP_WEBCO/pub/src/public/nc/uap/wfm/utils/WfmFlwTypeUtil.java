package nc.uap.wfm.utils;

import java.util.LinkedHashMap;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.wfm.dftimpl.DefaultWfmFlowCateOper;
import nc.uap.wfm.engine.IWfmFlowCateOper;
import nc.uap.wfm.itf.IWfmFlowCateQry;
import nc.uap.wfm.itf.IWfmFlwTypeQry;
import nc.uap.wfm.logger.WfmLogger;
import nc.uap.wfm.vo.WfmFlwCatVO;
import nc.uap.wfm.vo.WfmFlwTypeVO;
import nc.vo.ml.NCLangRes4VoTransl;

import org.apache.commons.lang.StringUtils;

import uap.lfw.core.locator.ServiceLocator;
import uap.web.bd.pub.AppUtil;
/**
 * 平台级相关流程类型操作
 * @author zhaohb
 *
 */
public class WfmFlwTypeUtil {
	public static String getFlwTypePkFromSession() {
		return (String) AppUtil.getAppAttr(WfmConstants.WfmAppAttr_FolwTypePk);
	}
	public static void addFlwTypePkToSession(String flwTypePk){
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_FolwTypePk, flwTypePk);
	}
	public static String getServerClass(String flwTypePk){
		String serverClazz =null;
		WfmFlwTypeVO flowTypeVo = ServiceLocator.getService(IWfmFlwTypeQry.class).getFlwTypeVoByPk(flwTypePk);
		if(flowTypeVo!=null)
			serverClazz= flowTypeVo.getServerclass();
		return serverClazz;
	}
	public static WfmFlwTypeVO getWfmFlwTpeVoByPkFromDB(String flwTypePk){
		return  ServiceLocator.getService(IWfmFlwTypeQry.class).getFlwTypeVoByPk(flwTypePk);
	}
	/**
	 * 此流程类型是否下级可用
	 * @param flwTypePk
	 * @return
	 */
	public static boolean isSubCanUse(String flwTypePk){
		IWfmFlwTypeQry service  = NCLocator.getInstance().lookup(IWfmFlwTypeQry.class);
		boolean isSubCanUse = service.isSubCanUse(flwTypePk);
		return isSubCanUse;
	}

	/**
	 * 登录人是否为管理员isManganer时，
	 * 流程类型flowTypePk对于 当前组织curOrgPk是否可见（可用）
	 * 
	 * @param flowTypePk
	 * @param curOrgPk
	 * @param isManganer
	 * @return
	 */
	public static boolean isSeeAble(String flowTypePk, String curOrgPk,
			boolean isManganer) {
		if (StringUtils.isBlank(flowTypePk))
			return false;
		IWfmFlwTypeQry service = NCLocator.getInstance().lookup(
				IWfmFlwTypeQry.class);
		WfmFlwTypeVO wfmFlwTypeVO = service.getFlwTypeVoByPk(flowTypePk);
		String belongOrgPk = null;
		if (wfmFlwTypeVO != null)
			belongOrgPk = wfmFlwTypeVO.getPk_org();
		if (StringUtils.isBlank(belongOrgPk)) {
			return isManganer;
		}

		// 当前组织为此流程类型所属组织
		if (belongOrgPk.equalsIgnoreCase(curOrgPk)) {
			return true;
		}
		// 当前组织为此流程的下级组织，可改一定可见
		if (wfmFlwTypeVO.getUseflag() != null
				&& wfmFlwTypeVO.getUseflag().booleanValue())
			return true;
		if (wfmFlwTypeVO.getSeeflag() == null
				|| !wfmFlwTypeVO.getSeeflag().booleanValue())
			return false;
		return true;

	}

	/**
	 * 登录人是否为管理员isManganer时，
	 * 流程类型flowTypePk对于 当前组织curOrgPk是否可以在新建流程
	 * 
	 * @param flowTypePk
	 * @param curOrgPk
	 * @param isManganer
	 * @return
	 */
	public static boolean isModifyAble(String flowTypePk, String curOrgPk,
			boolean isManganer) {
		if (StringUtils.isBlank(flowTypePk))
			return false;
		IWfmFlwTypeQry service = NCLocator.getInstance().lookup(
				IWfmFlwTypeQry.class);
		WfmFlwTypeVO wfmFlwTypeVO =service.getFlwTypeVoByPk(flowTypePk);
		String belongOrgPk = null;
		if (wfmFlwTypeVO != null)
			belongOrgPk = wfmFlwTypeVO.getPk_org();
		if (StringUtils.isBlank(belongOrgPk)) {
			return isManganer;
		}

		// 当前组织为此流程类型所属组织
		if (belongOrgPk.equalsIgnoreCase(curOrgPk)) {
			return true;
		}
		// 当前组织为此流程的下级组织，通过流程类型接口，下级是否可改查询,未设置或者为false为不可改
		if (wfmFlwTypeVO.getUseflag() == null
				|| !wfmFlwTypeVO.getUseflag().booleanValue())
			return false;

		return true;
	}
	/**
	 * 根据流程类型pk获得流程大类服务类
	 * @param pk_flwtype
	 * @return
	 */
	public static IWfmFlowCateOper getWfmFlowCatOper(String pk_flwtype) {
		WfmFlwTypeVO wfmFlwTypeVO=getWfmFlwTpeVoByPkFromDB(pk_flwtype);
		String pk_flwcat=wfmFlwTypeVO.getPk_flwcat();
		WfmFlwCatVO wfmFlwCatVO=ServiceLocator.getService(IWfmFlowCateQry.class).getWfmFlwCatVO(pk_flwcat);
		String flowCateOperClass=wfmFlwCatVO==null?null:wfmFlwCatVO.getServerclass();
		if(StringUtils.isBlank(flowCateOperClass)){
			flowCateOperClass = DefaultWfmFlowCateOper.class.getName();
		}
		IWfmFlowCateOper flowCateOper = null;
		try {
			flowCateOper = (IWfmFlowCateOper) Class.forName(flowCateOperClass).newInstance();
		} catch (Exception e) {
			WfmLogger.error(e);
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("flowsetting", "WfmFlowMainCtrl-000009")/**获取流程大类服务类失败!**/);
		}
		return flowCateOper;
	}
	/**
	 * 个性化设置所需参数
	 * @param pk_flwType
	 * @param pk_prodef
	 * @param portId
	 * @param appNodeId
	 * @param appId
	 * @param windowId
	 * @return
	 */
	public static LinkedHashMap<String, String> getTemplateDimensions(String pk_flwType,String pk_prodef,String portId,String appNodeId,String appId,String windowId){
		 LinkedHashMap<String, String> dimensions = new LinkedHashMap<String, String>();
		 dimensions.put("appid", appId);
		 dimensions.put("windowid", windowId);
		 dimensions.put("busiid", pk_flwType);
		 dimensions.put("pk_prodef", pk_prodef);
		 dimensions.put("port_id", portId);
		 dimensions.put("pk_funcnode", appNodeId);
		 return dimensions;
	}
}
