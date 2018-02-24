package nc.uap.portal.user.login.chain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import nc.bs.dao.DAOException;
import nc.bs.logging.Logger;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.uap.cpb.org.itf.ICpSysinitQry;
import nc.uap.cpb.org.vos.CpUserVO;
import nc.uap.cpb.persist.dao.PtBaseDAO;
import nc.uap.lfw.core.cache.LfwCacheManager;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.util.CookieUtil;
import nc.uap.portal.login.itf.LoginInterruptedException;
import nc.uap.portal.login.util.LfwLoginFetcher;
import nc.uap.portal.login.vo.AuthenticationUserVO;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.user.chain.AbstractVerifyChain;
import nc.uap.portal.user.chain.IUserVerifyChain;
import nc.uap.portal.user.chain.VerifyAtomChain;
import nc.vo.bd.psn.PsndocVO;
import nc.vo.pub.lang.UFBoolean;
import uap.lfw.core.locator.ServiceLocator;
import uap.lfw.itf.security.IRSAService;

import com.scap.pub.login.AuthzCodeServlet;

/**
 * 保持登录校验链
 * 
 * @author jizhg
 */
public class UserLoginChain extends AbstractVerifyChain {

	public static final String ERRMSG = "errmsg";

	@SuppressWarnings({ "restriction", "unchecked" })
	@Override
	public void doVerify(HttpServletRequest request, VerifyAtomChain chain) {
		// loginType：1，CA登录；0普通登录。
		String loginType = request.getParameter("loginType");
		if(loginType!=null&&loginType.equals("1")){
			this.doCAVerify(request, chain);
		}else{
			this.doNormalVerify(request, chain);
		}
		
	}
	@Override
	public int compareTo(IUserVerifyChain o) {
		return 0;
	}

	/**
	 * 根据CA传入身份证号获得登录用户
	 * @param idNo
	 * @return
	 */
	public CpUserVO getUserInfoByID(String idNo) {
		try {
			PtBaseDAO dao = new PtBaseDAO();
			String pk_psn_doc = "";
			String sqlPSN = "select * from bd_psndoc where id = '" + idNo + "'";
			List<PsndocVO>  vos = (List) dao.executeQuery(sqlPSN, new BeanListProcessor(PsndocVO.class));
				if(vos.size()==0){
					Logger.error("sql:"+sqlPSN+"该CA没有关联系统人员，请与管理员联系。");
					return null;
				}else{
					pk_psn_doc = vos.get(0).getPk_psndoc();					
				}
			String sqluser = "select * from cp_user where pk_base_doc = '" + pk_psn_doc +"'";
			List<CpUserVO>  vosUser = (List<CpUserVO>) dao.executeQuery(sqluser, new BeanListProcessor(CpUserVO.class));
			if(vosUser.size()>0){
				return vosUser.get(0);
			}else{
				Logger.error("sql:"+sqluser+"该人员没有关联系统用户，请与管理员联系。");
				return null;
			}
		} catch (DAOException e) {
			Logger.error(e.getMessage());
		}
		return null;
	}
	/**
	 * CA登录验证
	 * @param request
	 * @param chain
	 */
	public void doCAVerify(HttpServletRequest request, VerifyAtomChain chain){
		// 身份证号：
		String key = request.getParameter("key");
		String idNo = (String) LfwCacheManager.getStrongCache("hzgzw", null).get(key);
		LfwCacheManager.getStrongCache("hzgzw", null).remove(key);
		CpUserVO user = getUserInfoByID(idNo);
		if(user==null){
			request.getSession().setAttribute(ERRMSG, "CA登录失败");
			return;
		}
		String uid = user.getUser_code();
		AuthenticationUserVO userVO = new AuthenticationUserVO(
				uid == null ? null : uid.trim(), null);
		Map<String, String> map = new HashMap<String, String>();
		map.put("p_language", "simpchn");
		userVO.setExtInfo(map);
		try {
			doLogin(userVO);
		} catch (LoginInterruptedException e) {
			LfwLogger.error(e.getMessage(), e);
		}
		if (hasLogin()) {
			chain.doFilter(request);
		}
	}
	/**
	 * 普通登录验证
	 * @param request
	 * @param chain
	 */
	public void doNormalVerify(HttpServletRequest request, VerifyAtomChain chain){

		ICpSysinitQry sysInitQry = PortalServiceUtil.getCpSysinitQry();
		
		// 用户名、密码
		String uid = null;
		String upwd = null;
		uid = request.getParameter("uid");
		upwd = request.getParameter("upwd");
		
		String rsaLogin = "N";
		try {
			rsaLogin = sysInitQry.getSysinitValueByCodeAndPkorg("RSA_LOGIN", null);	
		} catch (Exception e) {
			 nc.uap.portal.log.PortalLogger.error(e.getMessage(), e);
		}
		if (UFBoolean.valueOf(rsaLogin).booleanValue()) {
			upwd = ServiceLocator.getService(IRSAService.class).decryptStringByJs(upwd);
		}

		
		// isFirst用于判断是否第一次点击登录请求
		String isFirst = request.getParameter("isFirst");

		AuthenticationUserVO userVO = new AuthenticationUserVO(
				uid  == null ? null : uid.trim(),
		        upwd == null ? null : upwd.trim()
		);
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("p_language", "simpchn");
		userVO.setExtInfo(map);
		// 获取cookie中的用户名
		String userId = CookieUtil.get(request.getCookies(), "p_userId");
		request.getSession().setAttribute("userId", userId);
		try {

			//是否显示验证码
			String showRanImg = "N";
			try {
				showRanImg = sysInitQry.getSysinitValueByCodeAndPkorg("randomimg",null);	
			} catch (Exception e) {
				 nc.uap.portal.log.PortalLogger.error(e.getMessage(), e);
			}			
			if (UFBoolean.valueOf(showRanImg).booleanValue()) {
				
				String expectAuthzCode = (String) request.getSession().getAttribute(AuthzCodeServlet.ATTR_AUTHZ_CODE);
				String authzCode = request.getParameter("verifyCode");
				if (expectAuthzCode == null || !expectAuthzCode.equalsIgnoreCase(authzCode)) {
					throw new LoginInterruptedException("验证码错误");
				}
				request.getSession().removeAttribute(AuthzCodeServlet.ATTR_AUTHZ_CODE);
			}
			//短信验证码逻辑 add luqzh 20150924 start
			String smsFlag = (String)request.getSession().getAttribute("smscodeflag");
			if (StringUtils.isNotBlank(smsFlag) && "true".equals(smsFlag)) {
				String smscode_p = request.getParameter("SmsCheckCode");
				if(StringUtils.isBlank(smscode_p)){
					throw new LoginInterruptedException("请输入短信验证码");
				}
				String smscode_s = (String) request.getSession().getAttribute("smscode");
				if(StringUtils.isBlank(smscode_s)){
					throw new LoginInterruptedException("请获取短信验证码");
				}
				if (!smscode_p.equals(smscode_s)) {
					throw new LoginInterruptedException("短信验证码错误");
				} else {
					request.getSession().removeAttribute("smscodeflag");	
				}
			}
			//短信验证码逻辑 add luqzh 20150924 end
			//处理登陆逻辑的
			LfwLoginFetcher.getGeneralInstance().getLoginHelper()
					.createLoginHandler().doAuthenticate(userVO);
		} catch (Exception e) {
			if (isFirst != null && "false".equals(isFirst)) {
				request.getSession().setAttribute("isFirst", isFirst);
				request.getSession().setAttribute(ERRMSG, e.getMessage());
			} else {
				request.getSession().removeAttribute("isFirst");
				request.getSession().removeAttribute(ERRMSG);
			}
			return;
		}
		try {
			doLogin(userVO);
		} catch (LoginInterruptedException e) {
			LfwLogger.error(e.getMessage(), e);
		}
		if (hasLogin()) {
			chain.doFilter(request);
		}

	}
}
