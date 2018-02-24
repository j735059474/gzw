package nc.uap.portal.user.impl;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import nc.bs.framework.common.UserExit;
import nc.scap.portal.auth.ca.jit.AuthenUtil;
import nc.scap.sysinit.constant.ISysinitConstant;
import nc.scap.sysinit.util.ScapSysinitUtil;
import nc.uap.cpb.org.exception.CpbBusinessException;
import nc.uap.cpb.org.itf.ICpSysinitQry;
import nc.uap.cpb.org.vos.CpUserVO;
import nc.uap.lfw.core.AppInteractionUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cache.LfwCacheManager;
import nc.uap.lfw.core.common.CookieConstant;
import nc.uap.lfw.core.common.ParamConstant;
import nc.uap.lfw.core.common.SessionConstant;
import nc.uap.lfw.core.comp.text.ComboBoxComp;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.IUIContext;
import nc.uap.lfw.core.exception.LfwInteractionException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.util.HttpUtil;
import nc.uap.lfw.login.vo.LfwSessionBean;
import nc.uap.portal.cache.PortalCacheManager;
import nc.uap.portal.deploy.vo.PtSessionBean;
import nc.uap.portal.exception.BreakPortalLoginException;
import nc.uap.portal.exception.PortalServerRuntimeException;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.log.PortalLogger;
import nc.uap.portal.log.ScapLogger;
import nc.uap.portal.login.itf.ILoginHandler;
import nc.uap.portal.login.itf.ILoginSsoService;
import nc.uap.portal.login.itf.IMaskerHandler;
import nc.uap.portal.login.itf.LoginInterruptedException;
import nc.uap.portal.login.vo.AuthenticationUserVO;
import nc.uap.portal.om.Page;
import nc.uap.portal.plugins.PluginManager;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.service.itf.IPtPageQryService;
import nc.uap.portal.servlet.PortalLoginFilter;
import nc.uap.portal.user.entity.IOrgVO;
import nc.uap.portal.user.entity.IUserVO;
import nc.uap.portal.user.itf.IUserLoginPlugin;
import nc.uap.portal.util.PortalPageDataWrap;
import nc.uap.portal.util.ToolKit;
import nc.uap.portal.vo.PtPageVO;
import nc.vo.bd.format.FormatDocVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;

import org.apache.commons.lang.StringUtils;

import com.scap.pub.login.AuthzCodeServlet;

import uap.lfw.core.locator.AdapterServiceLocator;
import uap.lfw.core.ml.LfwResBundle;
import uap.lfw.portal.user.itf.IUserBill;

/**
 * Portal登陆默认实现
 * 
 * @author licza
 * 
 */
/**
 * 
 * @modified by jizhg
 * 2013.11.28 增加保存密码功能
 */
public class PortalLoginHandler implements ILoginHandler<PtSessionBean>, IMaskerHandler<PtSessionBean> {
	private static final String INFO = "INFO";
	private static final String ERROR = "ERROR";
	private static final String LEVEL2 = "level";
	private static final String CHALLLID2 = "challlid";
	private static final String DESC = "DESC";
	private static final String CODE = "CODE";
	private static final String AFTER = "after";
	private static final String CA_USER_ID = "p_userId";
	private static final String CA_USER_PWD = "p_userPwd";
	private static final String SIGNDATA = "p_signdata";
	private static final String MAXWIN = "p_maxwin";
	private static final String LANGUAGE = "p_language";
	protected static final String LOGINDATE = "logindate";
	protected static final String FORCE = "force";
	private static final String BEFORE = "before";
	public static final String KEY = "ufida&UAP!102";
	public static final String SAVECHECK = "savecheck";
	private List<IUserLoginPlugin> plugins = null;
	IUserBill ub = null;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PtSessionBean doAuthenticate(AuthenticationUserVO userInfo) throws LoginInterruptedException {
		
		ScapLogger.debug("doAuthenticate - loginId=" + userInfo.getUserID());
		
		HttpServletRequest request = LfwRuntimeEnvironment.getWebContext().getRequest();
		HttpSession session = request.getSession();
		
		try {
			Map<String, String> extMap = (Map<String, String>) userInfo.getExtInfo();
			/**
			 * 登陆前Plugin操作
			 * 
			 * ydyanyh 2015-01-19 CA用户不验证密码
			 * ydyanyh 2015-01-20 CA登录不验证用户名
			 */
			Map rsl = null;
			loginPluginExecutor(userInfo, BEFORE);
			
			if (request.getParameter("auth_data") != null && request.getParameter("auth_data").toString().trim().length() != 0) {    // ca登录
				rsl = new HashMap();
				rsl.put(CODE, "2");
			} else {    // 密码登录
				rsl = getUserBill().userVerify(userInfo.getUserID(), userInfo.getPassword(), extMap);
			}
			String userid = userInfo.getUserID();
			String rslCode = (String) rsl.get(CODE);
			String rslMsg = (String) rsl.get(DESC);
			String level = (String) rsl.get(LEVEL2);
			
			
			/**
			 * 认证通过
			 */
			if ("0".equals(rslCode)) {
				/**
				 * 强制修改密码
				 */
				if(AppLifeCycleContext.current() != null){
					if (rslMsg != null) {
						if(ERROR.equals(level)){
							ensureChangePasswd(userid,rslMsg);
							return null;
						}
						else if(INFO.equals(level)){
							AppInteractionUtil.showMessageDialogWithRePost(rslMsg);
						}
					}
				}else{
					if (rslMsg != null) {
						if(ERROR.equals(level)){
							rslMsg = userid + ":" + rslMsg;
							throw new LoginInterruptedException(rslMsg);
						}
					}
				}
				
			}
			
			/**
			 * 致命异常
			 */
			if ("1".equals(rslCode)) {
				getUserBill().doLoginErrorLog(userInfo, rslMsg);
				throw new LoginInterruptedException(rslMsg);
			}
//			/**
//			 * 静态密码验证通过，需要CA验证
//			 */
//			if ("2".equals(rslCode)) {
//				HttpSession session = LfwRuntimeEnvironment.getWebContext().getRequest().getSession();
//				String challlid = UUID.randomUUID().toString();
//				session.setAttribute(CHALLLID2, challlid);
//				AppLifeCycleContext.current().getWindowContext()
//						.addExecScript("calogin('" + challlid + "','" + userid + "')");
//				return null;
//			}
			
			/**
			 * CA验证
			 * ydyanyh 2015-01-19
			 */
			if ("2".equals(rslCode)) {
				
				String authContent = (String) session.getAttribute("Auth_Content");    // session原文
				String authData = request.getParameter("auth_data");    // request原文
				String signedData = request.getParameter("signed_data");    // 签名
				
				// 校验CA证书
				try {
	                userid = AuthenUtil.check(authContent, authData, signedData, request.getRemoteAddr());
                } catch (Exception e) {
	                throw new LoginInterruptedException(e.getMessage());
                }
				IUserVO iUser = getUserBill().getUser(userid);
				if (iUser != null && iUser.getUser() != null && iUser.getUser() instanceof CpUserVO) {

					CpUserVO cpUser = (CpUserVO) iUser.getUser();
					if (cpUser.getIsca() != null && cpUser.getIsca().booleanValue()) {
						userInfo.setUserID(userid);
						rsl.put("USER", iUser);
					} else {
						rslMsg = "当前用户没有CA登录权限";
						throw new LoginInterruptedException(rslMsg);
					}
				} else {
					rslMsg = "用户不存在";
					throw new LoginInterruptedException(rslMsg);
				}
			}
			
			String langCode = UserExit.DEFAULT_LANG_CODE_VALUE;
			if (extMap.get(LANGUAGE) != null) {
				langCode = extMap.get(LANGUAGE);
			} else {
				langCode = LfwRuntimeEnvironment.getLangCode();
			}
			IUserVO ptUser = (IUserVO) rsl.get("USER");
			ptUser.setLangcode(langCode);
			PtSessionBean sbean = createSessionBean(ptUser);
			String tzOffset = extMap.get("p_tz");
			if(tzOffset != null ){
				int rawOffset = Integer.parseInt(tzOffset) * 60 * -1 * 1000;
				TimeZone tz = new SimpleTimeZone(rawOffset, "GMT " + (rawOffset / 60 / 60 / 1000));
				sbean.setTimeZone(tz);
			}
			
			return sbean;
		} catch (Exception e) {
			if (e instanceof LfwInteractionException) {
				throw (LfwInteractionException) e;
			}
			if(e instanceof LoginInterruptedException){
				throw (LoginInterruptedException) e;
			}
			PortalLogger.error("Login Error:" + e.getMessage(), e);
			throw new LoginInterruptedException(e.getMessage());
		}
	}
	/**
	 * 强制修改密码
	 * @param rslMsg
	 */
	private void ensureChangePasswd(String userid, String rslMsg) {
		HttpSession session = LfwRuntimeEnvironment.getWebContext().getRequest().getSession();
		session.setAttribute("USER_SESSION_ID", userid);
		StringBuffer urlBuf = new StringBuffer();
		urlBuf.append("/portal/app/mockapp/passwordmng?model=nc.uap.portal.mng.pwdmng.PasswordManagerModel");
		urlBuf.append("&" + ParamConstant.OTHER_PAGE_UNIQUE_ID + "=" + LfwRuntimeEnvironment.getWebContext().getWebSession().getWebSessionId());
		AppLifeCycleContext.current().getApplicationContext().popOuterWindow(urlBuf.toString(), rslMsg, "480", "280", IUIContext.TYPE_DIALOG);
	}

	/**
	 * 登陆后回调
	 */
	@Override
	public void afterLogin(LfwSessionBean userVO) {

		HttpServletRequest request = LfwRuntimeEnvironment.getWebContext().getRequest();
		try {
			LfwRuntimeEnvironment.setLfwSessionBean(userVO);
			LfwRuntimeEnvironment.setClientIP(HttpUtil.getIp());
			LfwRuntimeEnvironment.setDatasource(userVO.getDatasource());
			if(!"annoyuser".equals(userVO.getUser_code()))
				changeSessionIdentifier(request);
			request.getSession().setAttribute(SessionConstant.LOGIN_SESSION_BEAN, userVO);
			initUser(userVO);
			regOnlineUser(userVO, request);
			ILoginSsoService<PtSessionBean> ssoService = getLoginSsoService();
			ssoService.addSsoSign((PtSessionBean) userVO, getSysType());
			UFBoolean loginResult = UFBoolean.TRUE;
			/**
			 * 登陆后处理插件
			 */
			loginPluginExecutor(userVO, AFTER);
			getUserBill().doLoginLog(userVO, loginResult, LfwResBundle.getInstance().getStrByID("pserver", "PortalLoginHandler-000023")/*普通登陆*/);
		} catch (BusinessException e) {
			PortalLogger.error(e.getMessage(), e);
		}
	}

	/**
	 * 初始化用户信息
	 * 
	 * @param sbean
	 * @throws PortalServiceException
	 */
	private void initUser(LfwSessionBean sbean) throws PortalServiceException {
		IPtPageQryService qry = PortalServiceUtil.getPageQryService();
		PtSessionBean sb = (PtSessionBean) sbean;
		IUserVO user = sb.getUser();
		PtPageVO[] pageVOs = qry.getPagesByUser(user);
		if (pageVOs == null || pageVOs.length == 0)
			throw new PortalServerRuntimeException(LfwResBundle.getInstance().getStrByID("pserver", "PortalLoginHandler-000024")/*初始化用户信息失败,未找到当前用户可用的布局!*/);
		pageVOs = PortalPageDataWrap.filterPagesByUserType(pageVOs, sb.getUser_type());
		if (pageVOs == null || pageVOs.length == 0)
			throw new PortalServerRuntimeException(LfwResBundle.getInstance().getStrByID("pserver", "PortalLoginHandler-000025")/*初始化用户信息失败,未找到当前类型用户可用的布局!*/);
		List<Page> pageList = PortalPageDataWrap.praseUserPages(pageVOs);
		if (pageList.isEmpty())
			throw new PortalServerRuntimeException(LfwResBundle.getInstance().getStrByID("pserver", "PortalLoginHandler-000026")/*初始化用户信息失败:解析布局出现异常!*/);
		Map<String, Page> pagesCache = PortalPageDataWrap.praseUserPages(pageList.toArray(new Page[] {}));
		PortalCacheManager.getUserPageCache().clear();
		PortalCacheManager.getUserPageCache().putAll(pagesCache);
	}

	/**
	 * 获取登陆插件
	 * 
	 * @return
	 */
	private List<IUserLoginPlugin> getLoginPlugins() {
		if (plugins == null)
			plugins = PluginManager.newIns().getExtInstances(IUserLoginPlugin.ID, IUserLoginPlugin.class);
		return plugins;
	}

	/**
	 * 创建会话Bean
	 * 
	 * @param user
	 * @param ub
	 * @return
	 */
	private PtSessionBean createSessionBean(IUserVO user) {
		String groupNo;
		String groupName;
		IOrgVO org = getUserBill().getOrg(user.getPk_group());
		if (org != null) {
			groupNo = org.getCode();
			groupName = org.getName();
		} else {
			groupNo = "0000";
			groupName = "0000";
		}

		PtSessionBean sbean = new PtSessionBean();

		sbean.setDatasource(LfwRuntimeEnvironment.getDatasource());
		sbean.setUnitNo(groupNo);
		sbean.setUnitName(groupName);
		sbean.setUserType(user.getUsertype());
		sbean.setUser(user);
		sbean.setTimespan(System.currentTimeMillis());
		String themeId = LfwRuntimeEnvironment.getThemeId();
		sbean.setThemeId(themeId);
		return sbean;
	}

	/**
	 * 获得登录信息VO
	 */
	@Override
	public AuthenticationUserVO getAuthenticateVO() throws LoginInterruptedException {
		AuthenticationUserVO userVO = new AuthenticationUserVO();
		Map<String, String> extMap = new HashMap<String, String>();
		LfwView widget = getCurrentWidget();
		TextComp userIdComp = (TextComp) widget.getViewComponents().getComponent("userid");
		TextComp randomImageComp = (TextComp) widget.getViewComponents().getComponent("randimg");

		ICpSysinitQry cpSysinitQry = PortalServiceUtil.getCpSysinitQry();
		boolean enabledRandomImage = false;
		try {
			String showRanImg = cpSysinitQry.getSysinitValueByCodeAndPkorg("randomimg", null);
			enabledRandomImage = UFBoolean.valueOf(showRanImg).booleanValue();
		} catch (CpbBusinessException e) {
			PortalLogger.error(e.getMessage(), e);
		}
		String userId = null;

		AppLifeCycleContext pctx = AppLifeCycleContext.current();
		HttpSession session = LfwRuntimeEnvironment.getWebContext().getRequest().getSession();
		String signdata = pctx.getParameter(SIGNDATA);
		String sn = pctx.getParameter("p_sn");
		String tz = pctx.getParameter("p_tz");
		if (userIdComp != null) {
			if (enabledRandomImage) {
				String rand = (String) session.getAttribute("rand");
				String ricv = randomImageComp.getValue();
				if (!StringUtils.equals(rand, ricv))
					throw new LoginInterruptedException(LfwResBundle.getInstance().getStrByID("pserver",
							"PortalLoginHandler-000006")/* 验证码错误! */);
			}
			userId = userIdComp.getValue();
			if (userId == null || userId.equals("")) {
				throw new LoginInterruptedException(LfwResBundle.getInstance().getStrByID("pserver",
						"PortalLoginHandler-000007")/* 用户名不能为空 */);
			}
		}
		
		TextComp passComp = (TextComp) widget.getViewComponents().getComponent("password");
		String passValue = null;
		if (passComp != null) {
			passValue = passComp.getValue();
			if (passValue == null) {
				passValue = ("");
			}
		}

		ComboBoxComp multiLanguageCombo = (ComboBoxComp) widget.getViewComponents().getComponent("multiLanguageCombo");
		String language = multiLanguageCombo.getValue();

		userVO.setUserID(userId);
		userVO.setPassword(passValue);
		extMap.put(LANGUAGE, language);
		extMap.put(MAXWIN, "N");
		extMap.put(SIGNDATA, signdata);
		extMap.put("p_sn", sn);
		//Timezone
		extMap.put("p_tz", tz);
		String challlid = (String) session.getAttribute(CHALLLID2);
		extMap.put(CHALLLID2, challlid);
		userVO.setExtInfo(extMap);
		return userVO;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public Cookie[] getCookies(AuthenticationUserVO userVO) {
		List<Cookie> list = new ArrayList<Cookie>();
		String userId = userVO.getUserID();
		String userpwd = userVO.getPassword();
		Map<String, String> extMap = (Map<String, String>) userVO.getExtInfo();
		String sysId = "" + LfwRuntimeEnvironment.getSysId();
		String themeId = LfwRuntimeEnvironment.getLfwSessionBean().getThemeId();
		String language = extMap.get(LANGUAGE);
		String maxwin = extMap.get(MAXWIN);
		String keepPwdInClient = LfwRuntimeEnvironment.getWebContext().getRequest().getParameter("savepwd");
		String useridEncode = null;
		String userpwdEncode = null;
		String cookiePath = LfwRuntimeEnvironment.getRootPath();
		try {
			useridEncode = URLEncoder.encode(userId, "UTF-8");
			userpwdEncode = URLEncoder.encode(userpwd, "UTF-8");
		} catch (Exception e) {
			PortalLogger.warn(e.getMessage());
		}

		//saveFlag 是否保存密码复选框
		Cookie saveCheck = new Cookie(SAVECHECK, keepPwdInClient);
		saveCheck.setPath("/");
		saveCheck.setMaxAge(CookieConstant.MAX_AGE);
		list.add(saveCheck);
		
		// Theme
		Cookie tc = new Cookie(CookieConstant.THEME_CODE + sysId, themeId);
		tc.setPath("/");
		tc.setMaxAge(CookieConstant.MAX_AGE);
		list.add(tc);

		// 语种
		Cookie lc = new Cookie(CookieConstant.LANG_CODE + sysId, language);
		lc.setPath("/");
		lc.setMaxAge(CookieConstant.MAX_AGE);
		list.add(lc);
		// 用户账号
		Cookie uc = new Cookie(CA_USER_ID, useridEncode);
		uc.setPath(cookiePath);
		uc.setMaxAge(CookieConstant.MAX_AGE);
		list.add(uc);
		// 用户密码
		Cookie pwd = new Cookie(CA_USER_PWD, userpwdEncode);
		pwd.setPath(cookiePath);
		pwd.setMaxAge(CookieConstant.MAX_AGE);
		String saveFlag = ScapSysinitUtil.getSysinitStrByCode(ISysinitConstant.SAVEPWD);
		if(saveFlag!=null&&saveFlag.equals("Y") || StringUtils.equals(keepPwdInClient, "Y")){
			list.add(pwd);
		}else{
			pwd.setValue("");
			list.add(pwd);
		};
		// 最大化打开
		Cookie mc = new Cookie("isMaxWindow", maxwin);
		mc.setPath(cookiePath);
		mc.setMaxAge(CookieConstant.MAX_AGE);
		list.add(mc);

		Cookie p_auth = new Cookie("p_logoutflag", null);
		p_auth.setMaxAge(CookieConstant.MAX_AGE);
		p_auth.setPath(cookiePath);
		list.add(p_auth);
		
		return list.toArray(new Cookie[0]);
	}

	@Override
	public ILoginSsoService<PtSessionBean> getLoginSsoService() {
		return new PortalSSOServiceImpl();
	}

	@Override
	public String getSysType() {
		return PortalLoginFilter.PORTAL_SYS_TYPE;
	}

	public LfwView getCurrentWidget() {
		return AppLifeCycleContext.current().getViewContext().getView();
	}

	@Override
	public FormatDocVO getMaskerInfo(PtSessionBean loginBean) {
		return getUserBill().getMaskerInfo(loginBean);
	}

	/**
	 * 注册Portal在线人数
	 * 
	 * @param request
	 * @throws BusinessException
	 */
	private void regOnlineUser(LfwSessionBean sb, HttpServletRequest request) throws BusinessException {
		String clientIP = HttpUtil.getIp();
		String sessionid = request.getSession().getId();
		getUserBill().regOnlineUser(sb, sessionid, clientIP);
	}

	/**
	 * 登陆插件执行器
	 * 
	 * @param userInfo
	 */
	private void loginPluginExecutor(Object userInfo, String cmd) {
		/**
		 * 
		 * 登录前操作
		 */
		if (ToolKit.notNull(getLoginPlugins())) {
			for (IUserLoginPlugin ex : getLoginPlugins()) {
				boolean isBefore = BEFORE.equals(cmd);
				try {
					if (isBefore)
						ex.beforeLogin((AuthenticationUserVO) userInfo);
					else
						ex.afterLogin((PtSessionBean) userInfo);
				} catch (BreakPortalLoginException e) {
					PortalLogger.error(e.getMessage(), e);
					if (isBefore)
						getUserBill().doLoginErrorLog((AuthenticationUserVO) userInfo, e.getHint());
					else
						getUserBill().doLoginLog((LfwSessionBean) userInfo, UFBoolean.FALSE, e.getHint());
					throw new LfwRuntimeException(e.getHint());
				} catch (Throwable a) {
					PortalLogger.error(LfwResBundle.getInstance().getStrByID("pserver", "PortalLoginHandler-000027")/*登录操作异常:*/ + a.getMessage(), a);
				}
			}
		}
	}

	private IUserBill getUserBill() {
		if (ub == null) {
			ub = AdapterServiceLocator.newIns().get(IUserBill.class);
		}
		return ub;
	}
	
	private HttpSession changeSessionIdentifier(HttpServletRequest request)  {
	       // get the current session
	       HttpSession oldSession = request.getSession();

	       // make a copy of the session content
	       Map<String,Object> temp = new ConcurrentHashMap<String,Object>();
	       Map<String, Object> oldSessionCache = new ConcurrentHashMap<String,Object>();
	       oldSessionCache.putAll(LfwCacheManager.getSessionCache());
	       Enumeration<String> e = oldSession.getAttributeNames();
	       while (e != null && e.hasMoreElements()) {
	           String name = (String) e.nextElement();
	           Object value = oldSession.getAttribute(name);
	           temp.put(name, value);
	       }
	       oldSession.setAttribute(SessionConstant.SESSION_SELF_DESTORY , Boolean.TRUE);
	       // kill the old session and create a new one
	       oldSession.invalidate();
	       HttpSession newSession = request.getSession(true);
	       
	       // copy back the session content
	      for (Map.Entry<String, Object> stringObjectEntry : temp.entrySet())
	      {
	         newSession.setAttribute(stringObjectEntry.getKey(), stringObjectEntry.getValue());
	       }
	      Map<String, Object> newSessionCache = LfwCacheManager.getSessionCache();
	      newSessionCache.putAll(oldSessionCache);
	       return newSession;
	    }

}
