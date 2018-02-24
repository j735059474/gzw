package nc.scap.pub.entmatter.util;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import uap.web.bd.pub.AppUtil;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.scap.pub.util.CpOrgUtil;
import nc.scap.pub.util.ScapDAO;
import nc.scap.sysinit.constant.ISysinitConstant;
import nc.scap.sysinit.util.ScapSysinitUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.log.ScapLogger;
import nc.uap.wfm.exception.WfmServiceException;
import nc.uap.wfm.handler.PortAndEdgeHandler;
import nc.uap.wfm.itf.IWfmProInsQry;
import nc.uap.wfm.logger.WfmLogger;
import nc.uap.wfm.model.IPort;
import nc.uap.wfm.model.ProIns;
import nc.uap.wfm.model.StartEvent;
import nc.uap.wfm.model.Task;
import nc.uap.wfm.vo.WfmTaskVO;

public class MatterUtil {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean isUserCanApproveBill(String pk){
		String pk_user = LfwRuntimeEnvironment.getLfwSessionBean().getPk_user();
		String sql = "select * from wfm_task where pk_owner='"+pk_user+"' and state in ('State_Run','State_Plmnt') and pk_frmins='"+pk+"'";
		try {
			List<Map> tmpData = (List<Map>) ScapDAO.getPtBaseDAO().executeQuery( sql , new MapListProcessor());
			if(tmpData==null||tmpData.isEmpty()) return false;
			return true;
		} catch (DAOException e) {
			ScapLogger.error("数据查询出错!"+e.getMessage());
			return false;
		}
	}
	/**
	 * 判断是否当前单据驳回给了制单人,下一个处理单据的人是制单人,且当前用户是制单人。
	 * @param pk_form
	 */
	@SuppressWarnings("unchecked")
	public static boolean isBillBackCrrUser(String pk_form){
		String startHumId = null;
		//通过表单pk获得制单活动id
		try {
			ProIns proIns = NCLocator.getInstance().lookup(IWfmProInsQry.class).getProInsByFormInsPk(pk_form);
			if(proIns==null){ //该单据没有流程
				throw new LfwRuntimeException("单据没有定义流程!");
			}
			StartEvent start = proIns.getProDef().getStart();
			IPort[] ports = PortAndEdgeHandler.getNextHumActs(start);
			startHumId = ports[0].getId();
		} catch (WfmServiceException e) {
			WfmLogger.error(e);
			throw new LfwRuntimeException(e.getMessage());
		}
		try {
			//获得制单活动上待办的任务,已经进入逐级审批的待办任务(pk_dynamiconwer不是空)除外
			String pk_user = LfwRuntimeEnvironment.getLfwSessionBean().getPk_user();
			StringBuilder sql = new StringBuilder();
			sql.append("select * from wfm_task where pk_frmins='")
				.append(pk_form)
				.append("' and state='")
				.append(Task.State_Run)
				.append("' and port_id='")
				.append(startHumId)
				.append("' and (pk_executer='~' or pk_executer is null) and pk_owner='")
				.append(pk_user)
				.append("' and (pk_dynamiconwer is null or pk_dynamiconwer = '~')");
			List<WfmTaskVO> vos = (List<WfmTaskVO>) ScapDAO.getPtBaseDAO().executeQuery(sql.toString(), new BeanListProcessor(WfmTaskVO.class));
			if (vos != null && vos.size() >0){
				return true;
			} else {
				return false;
			}
		} catch (DAOException e) {
			WfmLogger.error(e);
			throw new LfwRuntimeException(e.getMessage());
		}
	}
	
	public static String getCntDept(String pk_user){
		if(pk_user==null){
			pk_user = LfwRuntimeEnvironment.getLfwSessionBean().getPk_user();
		}
		return CpOrgUtil.getDeptPkByUserPk(pk_user);
	}
	
	/**
	 * 功能类型
	 * @return
	 */
	public static String getMatterFuncType(){
		return getAddressParameter("func_type");
	}
	
	public static String projectCode(){
		HttpServletRequest request = LfwRuntimeEnvironment.getWebContext().getRequest();
		String url = request.getRequestURL().toString();
		if(url.matches("(http://lipanfeng)(.)*")){
			return "dl";
		}
		if(url.matches("(http://gzw01:8088)(.)*")){
			return "dl";
		}
		if(url.matches("(http://gzw01:7017)(.)*")){
			return "dl";
		}
		String province_id = ScapSysinitUtil.getSysinitStrByCode(ISysinitConstant.PROVINCE_ID);
		return province_id;
	}
	
	/**
	 * 获取处理类    nc.scap.pub.entmatter.operate.功能类型.(项目)MatterOperate
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T diffOperateInstance(Class<T> clazz){
		String func_type = getMatterFuncType();
		if(func_type==null){
			func_type = "";
		}
		String project_code = projectCode();
		if(project_code==null){
			project_code = "";
		}
		StringBuilder className = new StringBuilder(clazz.getName());
		int index = className.lastIndexOf(".");
		String lowerCode = func_type.toLowerCase();
		String uperCode = project_code.toUpperCase();
		//xx.xx.xx.功能类型.项目名+类名
		className.insert(index+1, lowerCode+"."+uperCode);
		Class<?> clazz1 = null;
		try {
			clazz1 = Class.forName(className.toString());
		} catch (ClassNotFoundException e) {
			//xx.xx.xx.功能类型.类名
			className = new StringBuilder(clazz.getName());
			index = className.lastIndexOf(".");
			className.insert(index+1, lowerCode+".");
			try {
				clazz1 = Class.forName(className.toString());
			} catch (ClassNotFoundException e1) {
				clazz1 = clazz;
				ScapLogger.error("没有找到类:"+className.toString()
						+",使用默认类"+clazz.getName());
			}
		}
		if(clazz1==null) return null;
		try {
			return (T) clazz1.newInstance();
		} catch (InstantiationException e) {
			ScapLogger.warn("实例化类报错!");
			return null;
		} catch (IllegalAccessException e) {
			ScapLogger.warn("实例化类报错!");
			return null;
		}
	}
	
	public static void forceCloseWindow(){
		AppLifeCycleContext.current().getApplicationContext().getCurrentWindowContext().getWindow().setHasChanged(false);
		AppLifeCycleContext.current().getApplicationContext().closeWinDialog();
	}
	
	@SuppressWarnings("unchecked")
	public static WfmTaskVO getLasterTaskByBillIdAndUser(  String billId,  String pk_user,  String operation) throws WfmServiceException {
	    if (billId == null || billId.length() == 0) {
				return null;
			}
			String sql = "";
			if (operation != null && !"wf".equals(operation)) {
				sql = "SELECT * FROM wfm_task WHERE pk_frmins = ? and pk_owner = ? and state in ('State_Run','State_Plmnt') order by ts desc";
			} else {
				sql = "SELECT * FROM wfm_task WHERE pk_frmins = ? and pk_owner = ?  order by ts desc";
			}
			SQLParameter param = new SQLParameter();
			param.addParam(billId);
			param.addParam(pk_user);
			try {
				List<WfmTaskVO> vos = (List<WfmTaskVO>) ScapDAO.getPtBaseDAO().executeQuery(sql,
						param, new BeanListProcessor(WfmTaskVO.class));
				if (vos != null && vos.size() > 0) {
					return vos.get(0);
				}
			} catch (DAOException e) {
				LfwLogger.error(e.getMessage(), e);
				throw new WfmServiceException(e.getMessage());
			}
			return null;
	  }
	
	public static String getAddressParameter(String key){
		String value = (String) LfwRuntimeEnvironment.getWebContext().getOriginalParameter(key);
		if(value == null){
			value = (String)AppLifeCycleContext.current().getApplicationContext().getAppAttribute(key);
		}
		return value;
	}
	
	public static String getPageCode(){
		String pagecode = (String) LfwRuntimeEnvironment.getWebContext()
				.getOriginalParameter("pagecode");
		if(pagecode==null){
			pagecode = (String)AppUtil.getAppAttr("pagecode");
		}
		return pagecode;
	}
}
