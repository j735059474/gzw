package nc.uap.portal.mng.pwdmng;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpSession;

import nc.bs.logging.Logger;
import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.sysinit.util.ScapSysinitUtil;
import nc.uap.cpb.org.itf.ICpUserExService;
import nc.uap.cpb.org.user.ICpUserConst;
import nc.uap.cpb.org.util.CpbServiceFacility;
import nc.uap.cpb.org.vos.CpUserVO;
import nc.uap.lfw.core.AppInteractionUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cache.ServiceCacheManger;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.ctrl.WindowController;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.login.vo.LfwSessionBean;
import nc.uap.portal.log.PortalLogger;
import nc.vo.ml.NCLangRes4VoTransl;

import org.apache.commons.lang.StringUtils;

import uap.lfw.core.ml.LfwResBundle;

/**
 * 修改密码控制类,走原changePassword.jsp对应的servlet的逻辑
 * 
 * @author liujmc
 * @date 2012-07-27
 */
@SuppressWarnings("rawtypes")

public class PasswordManageController implements WindowController, Serializable {
	private static final long serialVersionUID = 7532916478964732880L;

	public void onCancelClick(MouseEvent mouseEvent) {
		AppLifeCycleContext.current().getApplicationContext().closeWinDialog();
	}

	public void onOkClick(MouseEvent mouseEvent) {
		LfwSessionBean sb = LfwRuntimeEnvironment.getLfwSessionBean();
		// 获取表单项
		LfwView mainWidget = AppLifeCycleContext.current().getWindowContext().getCurrentViewContext().getView();
		Dataset ds = mainWidget.getViewModels().getDataset("passwordds");
		Row row = ds.getSelectedRow();
		String oldPwd = String.valueOf(row.getValue(ds.nameToIndex("oldpwd")));
		String newPwd = String.valueOf(row.getValue(ds.nameToIndex("newpwd")));
		String confirmPwd = String.valueOf(row.getValue(ds.nameToIndex("confirmpwd")));
		String userCode = null;
		TextComp passwdComp = null;
		if(sb != null){
			userCode = sb.getUser_code();
		}else{
			HttpSession session = LfwRuntimeEnvironment.getWebContext().getRequest().getSession();
			userCode = (String) session.getAttribute("USER_SESSION_ID");
			passwdComp = (TextComp)LfwRuntimeEnvironment.getWebContext().getParentPageMeta().getView("main").getViewComponents().getComponent("password");
			oldPwd = passwdComp.getValue();
		}
		if(userCode == null)
			throw new LfwRuntimeException(LfwResBundle.getInstance().getStrByID("pmng", "PasswordManageController-000000")/*获取用户信息失败!*/);
		
		if(StringUtils.equals(newPwd, confirmPwd)){
			try {
				CpUserVO user = CpbServiceFacility.getCpUserQry().getUserByCode(userCode);
				if(user == null){
					user = CpbServiceFacility.getCpUserQry().getGlobalUserByCode(userCode);
					user.setOriginal(ICpUserConst.ORIGINAL_NC);
				}
				CpbServiceFacility.getCpUserBill().changeUserPwd(user, oldPwd, newPwd);
				/**
				 * 自动调度的通知有延迟，所以这里直接更新缓存。
				 */
				ServiceCacheManger.clearAllCache(ICpUserExService.class.getName(), true);

			} catch (Exception e) {
				PortalLogger.error(e.getMessage(), e);
				String msg = e.getMessage().replace("'", "`").replace("\n", "");
				msg = msg.replace("nc.vo.pub.BusinessException:", "");
				throw new LfwRuntimeException(msg);
			}
		}else{
			String msg = NCLangRes4VoTransl.getNCLangRes().getStrByID("pserver", "PortalSettingAction-000001")/*两次输入的密码不同!*/;
			throw new LfwRuntimeException(msg);
		}		
		if(sb != null){
			String tip = NCLangRes4VoTransl.getNCLangRes().getStrByID("pserver", "PortalSettingAction-000000")/*修改成功,请重新登录!*/;
			AppInteractionUtil.showMessageDialog(tip);
			AppLifeCycleContext.current().getApplicationContext().closeWinDialog();
		}else{
			ServiceCacheManger.clearAllCache(ICpUserExService.class.getName());
		  	passwdComp.setValue(newPwd);
			String script = "parent.callLoginFunc();";
			AppLifeCycleContext.current().getApplicationContext().addAfterExecScript(script);
		}
		
		
//		String iClassName = "nc.scap.mdmpub.service.IServiceObjectUser";
		String provinceId = ScapSysinitUtil
				.getSysinitStrByCode(IGlobalConstants.PROVINCE_ID);
		if(IGlobalConstants.AH.equals(provinceId)) {
			String impClassName = "nc.scap.mdmpub.service.impl.UserServiceImp";
			try {
//				Class iClass = Class.forName(iClassName);
				Class impClass = Class.forName(impClassName);
				@SuppressWarnings("unchecked")
				Method method = impClass.getMethod("resetMdmPwd", new Class[] { String.class, String.class, String.class});
				method.invoke(impClass.newInstance(), userCode, oldPwd, newPwd);
			} catch (ClassNotFoundException e) {
				Logger.error(e, e);
//	            throw new LfwRuntimeException(e.getMessage());
			} catch (NoSuchMethodException e) {
				Logger.error(e, e);
//	            throw new LfwRuntimeException(e.getMessage());
			} catch (InvocationTargetException e) {
				Logger.error(e, e);
//	            throw new LfwRuntimeException(e.getMessage());
			} catch (IllegalAccessException e) {
				Logger.error(e, e);
//	            throw new LfwRuntimeException(e.getMessage());
			} catch(InstantiationException e) {
				Logger.error(e, e);
//	            throw new LfwRuntimeException(e.getMessage());
			}
		}
	}

	public void onDataLoad_passwordds(DataLoadEvent dataLoadEvent) {
		// 初始化表单对应的ds数据
		Dataset ds = dataLoadEvent.getSource();
		Row row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("oldpwd"), "");
		ds.addRow(row);
		ds.setRowSelectIndex(ds.getRowIndex(row));
	}
}
