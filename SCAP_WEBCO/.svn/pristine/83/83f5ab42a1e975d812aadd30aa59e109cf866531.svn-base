package uap.web.bd.pub.session;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import nc.bs.framework.common.NCLocator;
import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.util.AESUtils;
import nc.scap.pub.util.EncryptUtil;
import nc.scap.pub.util.PropertiesUtil;
import nc.uap.cpb.log.CpLogger;
import nc.uap.cpb.log.OperatorLogHelper;
import nc.uap.cpb.log.operatorlog.CpOperatorLogVO;
import nc.uap.cpb.org.exception.CpbBusinessException;
import nc.uap.cpb.org.itf.ICpAppsNodeQry;
import nc.uap.cpb.org.itf.ICpModuleQry;
import nc.uap.cpb.org.vos.CpAppsCategoryVO;
import nc.uap.cpb.org.vos.CpAppsNodeVO;
import nc.uap.cpb.org.vos.CpModuleVO;
import nc.uap.lfw.app.filter.AppFilter;
import nc.uap.lfw.app.plugin.AppControlPlugin;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebContext;
import nc.uap.lfw.core.WebSession;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.ctrlfrm.ModePhase;
import nc.uap.lfw.core.exception.LfwLicenseException;
import nc.uap.lfw.core.exception.LfwSecurityException;
import nc.uap.lfw.core.servlet.IWebSessionEvent;
import nc.uap.lfw.core.servlet.dft.LfwDefaultWebSessionListener;
import nc.uap.lfw.login.vo.LfwSessionBean;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;

import org.apache.commons.lang.StringUtils;

import uap.lfw.core.factory.LfwMultiSysFactory;
import uap.wap.bd.file.CPFileLockHelper;
import uap.web.bd.pub.BDUriHelper;

public final class BDWebSessionListener extends LfwDefaultWebSessionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2921489884292966597L;
	private static Map map = new HashMap();
	//创建session
	@Override
	public void sessionCreated(IWebSessionEvent sesEvent) {
		try {
			WebSession ws = sesEvent.getWebSession();
			if (isAppSession(ws)) {
				if (LfwRuntimeEnvironment.getModePhase() != ModePhase.dft) {
					return;
				}
				//如果是debug模式直接跳过
				if (LfwRuntimeEnvironment.getMode() == WebConstant.MODE_DEBUG) {
					return;
				}
				LfwSessionBean bean = LfwRuntimeEnvironment.getLfwSessionBean();
				if (bean == null)
					return;
				String pk_user = LfwRuntimeEnvironment.getLfwSessionBean().getPk_user();

				int usertype = LfwRuntimeEnvironment.getLfwSessionBean().getUser_type();
				
				if(usertype == 3)
					return;
				HttpServletRequest request = LfwRuntimeEnvironment.getWebContext().getRequest();

				String uri = (String) request.getAttribute(AppFilter.ORIURL);
				
				CpLogger.error("[bdlicense ] uri is: "+uri + ",usertype is: " + usertype);
				
				ICpAppsNodeQry nodeqry = NCLocator.getInstance().lookup(ICpAppsNodeQry.class);
				CpAppsNodeVO node = null;
				String nodecode = request.getParameter(AppControlPlugin.NODECODE);
				node = nodeqry.getNodeById(nodecode);

				// doopenlog(node,bean,request);
				if(node == null){
					return;
				}
//				if (node == null) {
//					boolean isfree = false;
//					ICpFreeNodeQry freenodeqry = NCLocator.getInstance()
//							.lookup(ICpFreeNodeQry.class);
//					isfree = freenodeqry.checkUrl(uri);
//					if (!isfree)
//						throw new LfwSecurityException(NCLangRes4VoTransl
//								.getNCLangRes().getStrByID("pub",
//										"BDWebSessionListener-000000")/*
//																	 * 当前节点未注册，不允许访问
//																	 */);
//					else
//						return;
//				} else {
//					if (node.getPk_appscategory() != null
//							&& node.getPk_appscategory().equals(
//									"0001z0100category006")) {// 自由表单
//						ICpFreeNodeQry freenodeqry = NCLocator.getInstance()
//								.lookup(ICpFreeNodeQry.class);
//						boolean isfree = false;
//						isfree = freenodeqry.checkUrl(uri);
//						if (isfree)
//							return;
//					}
//				}
				//7020在检查安全时出问题，所以注掉本段代码
//				checkSecutiry(pk_user, usertype, uri, node);

				ICpModuleQry moduleqry = NCLocator.getInstance().lookup(
						ICpModuleQry.class);
				CpAppsCategoryVO catvo = nodeqry.getCategoryByPk(node
						.getPk_appscategory());
				if (catvo == null) {
					throw new LfwSecurityException(NCLangRes4VoTransl
							.getNCLangRes().getStrByID("pub",
									"BDWebSessionListener-000001")/*
																 * 当前节点未绑定功能分组，不允许访问
																 */);
				}
				CpModuleVO mvo = moduleqry.getModuleBypk(catvo.getPk_module());
				if (mvo == null) {
					throw new LfwSecurityException(NCLangRes4VoTransl
							.getNCLangRes().getStrByID("pub",
									"BDWebSessionListener-000002")/*
																 * 当前节点未绑定模块，不允许访问
																 */);
				}
				if (mvo.getId() == null || mvo.getId().equals("")) {
					throw new LfwSecurityException(NCLangRes4VoTransl
							.getNCLangRes().getStrByID("pub",
									"BDWebSessionListener-000003")/*
																 * 当前节点绑定模块编码为空，
																 * 不允许访问
																 */);
				}
				//控制节点权限的
				if(mvo.getId().startsWith("11")){
					return;
				}
				//如果使用国资监管系统，则进入授权判断
				if(mvo.getId().startsWith("E9")){
					String md5Key = "";
					String nodeKey = "";
					String dateKey = "";
					if(map.get(IGlobalConstants.MD5_KEY)==null||"".equals(map.get(IGlobalConstants.MD5_KEY).toString())){
						//读取授权文件，解密，做判断,文件地址固定为，nchome/bin
						String nchomeStr = LfwMultiSysFactory.getMultiSysFactory().getRuntimeEnv().getBaseHome();//获取NChome路径
						String filePath = nchomeStr+"/bin/"+IGlobalConstants.LICENSE_NAME;
						File file = new File(filePath);
						if(!file.isFile()||!file.exists()){
							throw new RuntimeException(" 查找授权文件失败!");
						}
						//修改成以properties方式读取配置文件
						md5Key = PropertiesUtil.readProValue(filePath, IGlobalConstants.MD5_KEY,false);
						if(md5Key==null||"".equals(md5Key.toString())){
							throw new RuntimeException(" 授权文件版本过低,请重新申请!");
						}
						nodeKey = PropertiesUtil.readProValue(filePath, IGlobalConstants.NODE_KEY, false);
						dateKey = PropertiesUtil.readProValue(filePath, IGlobalConstants.DATE_KEY, false);
						map.put(IGlobalConstants.MD5_KEY, md5Key);
						map.put(IGlobalConstants.NODE_KEY,nodeKey);
						map.put(IGlobalConstants.DATE_KEY,dateKey);
					}else{
						md5Key = map.get(IGlobalConstants.MD5_KEY).toString();
						nodeKey = map.get(IGlobalConstants.NODE_KEY).toString();
						dateKey = map.get(IGlobalConstants.DATE_KEY).toString();
					}
					byte[] appnotePwd = AESUtils.parseHexStr2Byte(nodeKey);//获得节点的加密码
					String localmd5 = EncryptUtil.makeMD5(EncryptUtil.getLocalMac()+EncryptUtil.getHdSerialInfo()); //获取服务器
					if(!md5Key.equals(localmd5)){
						throw new RuntimeException(" 系统授权错误!请联系管理员");
					}
					
					byte[] dateKeyByte = AESUtils.parseHexStr2Byte(dateKey);//获得到期日期的加密码
					String dateKeyStr = new String(AESUtils.decrypt(dateKeyByte, md5Key));//获得到期日期
					if(dateKeyStr!=null&&!"".equals(dateKeyStr)){
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						Date date = sdf.parse(dateKeyStr);
						if(new Date().getTime()>date.getTime()){
							throw new RuntimeException(" 系统授权已过期，请重新申请!");
						}
					}
					
					String appnoteStr = new String(AESUtils.decrypt(appnotePwd, md5Key));//获得节点编码以 逗号分隔
					if(appnoteStr!=null&&!"".equals(appnoteStr)){
						String[] split = appnoteStr.split(",");
						for (int i = 0; i < split.length; i++) {
							String appnote = split[i];
							if(nodecode.startsWith(appnote)){
								return;
							}
						}
					}
					//测试时必须屏蔽掉,否则很麻烦
					throw new RuntimeException(" 此节点没有授权!");
				}
				
				/** 原系统自带NC授权,暂时不用 */
/*				String modulecode = mvo.getId();
				this.checklicense(nodecode, node.getTitle(), modulecode,
						pk_user, request.getSession().getId(),
						HttpUtil.getIp(), bean);	*/
			}
		} catch (Throwable e) {
			if (e instanceof LfwSecurityException)
				throw (LfwSecurityException) e;
			else if (e instanceof LfwLicenseException)
				throw (LfwLicenseException) e;
			else
				throw new LfwSecurityException(NCLangRes4VoTransl
						.getNCLangRes().getStrByID("pub",
								"BDWebSessionListener-000004")/* 检查节点权限失败 */
						+ e.getMessage(), e);
		}
	}
	//销毁session
	@Override
	public void sessionDestroyed(IWebSessionEvent sesEvent) {
		try {
			WebSession ws = sesEvent.getWebSession();
			String dsname = (String) ws.getAttribute(AppControlPlugin.DSName);
			String nodecode = (String) ws
					.getAttribute(AppControlPlugin.NODECODE);
			if (nodecode == null) {
				return;
			}
			if (!StringUtils.isEmpty(dsname))
				LfwRuntimeEnvironment.setDatasource(dsname);
			if (isAppSession(ws)) {
				this.releaseLicense(dsname, ws);
			}
			// 释放
			this.releaseFilelockByApp(ws.getWebSessionId());
		} catch (Throwable e) {
			CpLogger.error(e);
		}
	}
	//发布session
	private void releaseLicense(String dsname, WebSession ws)
			throws CpbBusinessException {
		IWebBDLicenseProvider provider = getLicenseProvider();
		CpAppsNodeVO node = null;
		String nodecode = (String) ws.getAttribute(AppControlPlugin.NODECODE);
		if (nodecode == null) {
			return;
		}
		String sessionid = (String) ws.getAttribute(WebContext.Http_Session_ID);
		ICpAppsNodeQry nodeqry = NCLocator.getInstance().lookup(
				ICpAppsNodeQry.class);
		node = nodeqry.getNodeById(nodecode);

		if (node == null) {
			return;
		}
		ICpModuleQry moduleqry = NCLocator.getInstance().lookup(
				ICpModuleQry.class);
		CpAppsCategoryVO catvo = nodeqry.getCategoryByPk(node
				.getPk_appscategory());
		if (catvo == null) {
			return;
		}
		CpModuleVO mvo = moduleqry.getModuleBypk(catvo.getPk_module());
		if (mvo == null) {
			return;
		}
		if (mvo.getId() == null || mvo.getId().equals("")) {
			return;
		}
		provider.unregisterOpenNode(dsname, sessionid, nodecode, mvo.getId());

	}

	private void checkSecutiry(String userpk, Integer usertype, String url,
			CpAppsNodeVO node) throws LfwSecurityException {

		try {
			ICpAppsNodeQry nodeqry = NCLocator.getInstance().lookup(
					ICpAppsNodeQry.class);

			String nodeurl = node.getUrl();
			if (StringUtils.isEmpty(nodeurl))
				throw new LfwSecurityException(NCLangRes4VoTransl
						.getNCLangRes().getStrByID("pub",
								"BDWebSessionListener-000005")/* 当前节点注册路径为空 */);
			boolean needfilterwinid = true;
			if (url.toLowerCase().indexOf("mockapp") > 0) {
				needfilterwinid = false;
			}
			if (needfilterwinid) {
				url = BDUriHelper.removeWinID(url);
				nodeurl = BDUriHelper.removeWinID(nodeurl);
			}

			if (url.indexOf(nodeurl) < 0)
				throw new LfwSecurityException(NCLangRes4VoTransl
						.getNCLangRes().getStrByID("pub",
								"BDWebSessionListener-000006")/*
															 * 您请求的地址与当前节点地址不符，请检查
															 */);

			if (usertype == 0 || usertype == 2) {
				// if(usertype != -1 ){
				return;
			}

			// 检查权限
			if (!nodeqry.CheckSecurity(userpk, node.getPk_appsnode()))
				throw new LfwSecurityException(NCLangRes4VoTransl
						.getNCLangRes().getStrByID("pub",
								"BDWebSessionListener-000007")/* 您没有请求地址的操作权限 */);
		} catch (CpbBusinessException e) {
			throw new LfwSecurityException(NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("pub", "BDWebSessionListener-000004")/* 检查节点权限失败 */
					+ e.getMessage(), e);
		}
	}

	private void checklicense(String nodecode, String nodename,
			String modulecode, String userid, String sessionID, String ipAddr,
			LfwSessionBean sessionbean) {
		IWebBDLicenseProvider provider = getLicenseProvider();
		provider.doOpen(nodecode, nodename, modulecode, userid, sessionID,
				ipAddr, sessionbean);
	}

	private IWebBDLicenseProvider getLicenseProvider() {
		return new BDLicenseProvider();
	}

	private void doopenlog(CpAppsNodeVO node, LfwSessionBean bean,
			HttpServletRequest request) {
		CpOperatorLogVO operLogVO = new CpOperatorLogVO();
		if (node != null) {
			operLogVO.setAppid(node.getAppid());
			operLogVO.setFuncnodecode(node.getPk_appsnode());
			operLogVO.setPk_funcnode(node.getPk_appsnode());
			operLogVO.setFuncnodename(node.getTitle());
		}
		operLogVO.setMethodname("Open Node");

		operLogVO.setOperresult(UFBoolean.TRUE);
		operLogVO.setDetail("");
		operLogVO.setClientip(request.getRemoteAddr());

		operLogVO.setPk_logingroup(bean.getPk_unit());
		operLogVO.setLogingroupcode(bean.getUnitName());
		operLogVO.setPk_user(bean.getPk_user());
		operLogVO.setUsercode(bean.getUser_code());
		operLogVO.setUsername(bean.getUser_name());
		operLogVO.setSessionid(request.getSession().getId());
		operLogVO.setButtonname("Open Node");
		operLogVO.setOpertime(new UFDateTime());

		doopenlog(operLogVO);
	}

	private void doopenlog(CpOperatorLogVO operLogVO) {
		try {
			OperatorLogHelper.doOpenLog(operLogVO);
		} catch (Throwable ex) {
			CpLogger.error(ex);
		}
	}

	private void releaseFilelockByApp(String appsessionid) {
		try {
			CPFileLockHelper.releaseFileLockByAppSession(appsessionid);
		} catch (Throwable ex) {
			CpLogger.error(ex);
		}
	}
}
