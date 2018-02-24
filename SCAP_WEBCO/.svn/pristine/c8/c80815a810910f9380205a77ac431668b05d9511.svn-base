package nc.scap.pub.util;

import org.apache.commons.lang.StringUtils;

import uap.lfw.core.ml.LfwResBundle;
import nc.uap.cpb.org.itf.ICpUserExService;
import nc.uap.cpb.org.user.ICpUserConst;
import nc.uap.cpb.org.util.CpbServiceFacility;
import nc.uap.cpb.org.vos.CpUserVO;
import nc.uap.lfw.core.cache.ServiceCacheManger;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.servletplus.annotation.Action;
import nc.uap.lfw.servletplus.annotation.Servlet;
import nc.uap.lfw.servletplus.constant.Keys;
import nc.uap.lfw.servletplus.core.impl.BaseAction;
import nc.uap.portal.log.PortalLogger;
import nc.uap.portal.log.ScapLogger;
import nc.vo.ml.NCLangRes4VoTransl;

@Servlet(path = "/loginHandler")
public class LoginHandler extends BaseAction {
	
	@Action(method = Keys.POST)
	public void changePassword() {
		String usercode = request.getParameter("usercode");
		String oldpwd = request.getParameter("oldpwd");
		String newpwd = request.getParameter("newpwd");
		String confirmpwd = request.getParameter("confirmpwd");
		if(usercode != null && StringUtils.equals(newpwd, confirmpwd)){
			try {
				CpUserVO user = CpbServiceFacility.getCpUserQry().getUserByCode(usercode);
				if(user == null){
					user = CpbServiceFacility.getCpUserQry().getGlobalUserByCode(usercode);
					user.setOriginal(ICpUserConst.ORIGINAL_NC);
				}
				CpbServiceFacility.getCpUserBill().changeUserPwd(user, oldpwd, newpwd);
				/**
				 * 自动调度的通知有延迟，所以这里直接更新缓存。
				 */
				ServiceCacheManger.clearAllCache(ICpUserExService.class.getName(), true);
				print("success");

			} catch (Exception e) {
				PortalLogger.error(e.getMessage(), e);
				ScapLogger.error(e.getMessage(), e);
				String msg = e.getMessage().replace("'", "`").replace("\n", "");
				msg = msg.replace("nc.vo.pub.BusinessException:", "");
				print(msg);
			}
		}else{
			String msg = NCLangRes4VoTransl.getNCLangRes().getStrByID("pserver", "PortalSettingAction-000001")/*两次输入的密码不同!*/;
			ScapLogger.error(msg);
			print(msg);
		}		
		
	}

}
