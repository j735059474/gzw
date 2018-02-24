package nc.uap.cpb.org.user;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.uap.rbac.MiddleEntityPersister;
import nc.itf.uap.rbac.IUserExService;
import nc.itf.uap.rbac.IUserLockService;
import nc.itf.uap.rbac.IUserManage;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.itf.uap.rbac.userpassword.IUserPasswordChecker;
import nc.itf.uap.rbac.userpassword.IUserPasswordManage;
import nc.uap.cpb.baseservice.util.BDPKLockUtil;
import nc.uap.cpb.log.CpLogger;
import nc.uap.cpb.org.itf.ICpUserExService;
import nc.uap.cpb.org.itf.ICpUserPasswordService;
import nc.uap.cpb.org.util.CpbServiceFacility;
import nc.uap.cpb.org.vos.CpUserVO;
import nc.uap.cpb.persist.dao.PtBaseDAO;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.vo.framework.rsa.Encode;
import nc.vo.org.GroupVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.sm.UserVO;
import nc.vo.uap.rbac.UserShareVO;
import nc.vo.uap.rbac.userpassword.PasswordSecurityLevelFinder;
import nc.vo.uap.rbac.userpassword.PasswordSecurityLevelVO;
import nc.vo.uap.rbac.userpassword.UserPasswordVO;
import nc.vo.uap.rbac.util.RbacUserPwdUtil;
import nc.vo.uap.rbac.util.UserExManageUtil;
import nc.vo.util.innercode.RandomSeqUtil;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import uap.lfw.bd.loginexception.CpLoginRunTimeException;
import uap.lfw.core.locator.ServiceLocator;
import uap.lfw.core.ml.LfwResBundle;
import uap.web.bd.pub.CpSqlTranslateUtil;

/**
 * patched by jizhg 解决用户初次登录 强制修改密码
 * @author jizhg
 *
 */
public class CpUserPasswordServiceImpl_bak implements ICpUserPasswordService {

	@Override
	public String resetUserPassWord(String userId) {
		IUserPasswordManage passMgr = NCLocator.getInstance().lookup(IUserPasswordManage.class);
		try {
			return passMgr.resetUserPassWord(userId);
		} 
		catch (BusinessException e) {
			CpLogger.error(e);
			throw new LfwRuntimeException(LfwResBundle.getInstance().getStrByID("ad", "CpUserPasswordServiceImpl-000000")/*重置用户密码时出错*/, e.getMessage());
		}
	}

	@Override
	public String getEncodedPassword(CpUserVO cpUserVO, String expresslyPWD) {
		if(cpUserVO == null || StringUtils.isBlank(cpUserVO.getPrimaryKey()))
			throw new LfwRuntimeException("illegal arguments");
		
		UserVO userVO = new UserVO();
		try {
			BeanUtils.copyProperties(userVO, cpUserVO);
			String codecPWD = null;
			codecPWD = RbacUserPwdUtil.getEncodedPassword(userVO, expresslyPWD);
			return codecPWD;
		} catch (Exception e) {
			CpLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage());
		} 
	}

	@Override
	public String getUserDefaultPassword(String pk_group) throws BusinessException {
		IUserManageQuery userQry = NCLocator.getInstance().lookup(IUserManageQuery.class);
		return userQry.getUserDefaultPassword(pk_group);
	}

	@Override
	public void updateNcUserPassword(CpUserVO cpUserVO, String inputOldPwd,
			String inputNewPwd) throws BusinessException {
		
		try {
			//modify by maokun 2013.04.09    NC IUserPubService中提供的updateUserPassWord方法不更新用户密码修改时间
//			UserVO ncuser = new UserVO();
//			BeanUtils.copyProperties(ncuser, cpUserVO);
//			//更新密码
//			IUserPubService service = NCLocator.getInstance().lookup(IUserPubService.class);
//			service.updateUserPassWord(ncuser.getCuserid(), inputOldPwd, inputNewPwd);
			
			IUserManageQuery userQry = NCLocator.getInstance().lookup(IUserManageQuery.class);
			UserVO oldUser = userQry.getUser(cpUserVO.getCuserid());
			
			//记录密码修改时间
			String stmp = new UFDateTime(System.currentTimeMillis()).toString();
			oldUser.setPwdparam(stmp.substring(0, stmp.indexOf(" ")).trim());
			
			//修改密码
			IUserPasswordManage upwdManage = NCLocator.getInstance().lookup(IUserPasswordManage.class);
			upwdManage.changeUserPassWord(oldUser,inputNewPwd);
			
			
			//删除用户重置密码状态
			delResetUserInfo(cpUserVO.getCuserid());
			delInitUserInfo(cpUserVO.getCuserid());
			
		} 
		catch (Exception e) {
			CpLogger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		} 
	}
	
	/**
	 * 修改密码时按照密码等级要求校验新密码
	 */
	@Override
	public void checkPwdLevel(CpUserVO cpUserVO)throws BusinessException{
		UserVO ncuser = new UserVO();
		try {
			BeanUtils.copyProperties(ncuser, cpUserVO);
		} catch (Exception e) {
			CpLogger.error(e.getMessage(), e);
			throw new BusinessException(e);
		} 
		PasswordSecurityLevelVO pwdLevel = PasswordSecurityLevelFinder.getPWDLV(ncuser);
		if(pwdLevel!=null){
			IUserPasswordChecker upchecher = (IUserPasswordChecker) NCLocator.getInstance().lookup(IUserPasswordChecker.class.getName());
			upchecher.checkNewpassword(ncuser, ncuser.getUser_password(),pwdLevel, ncuser.getUser_type());
		}
	}

	@Override
	public String getResetCpUserPassWord(CpUserVO cpUserVO) throws BusinessException {
		UserVO ncuser = new UserVO();
		try {
			BeanUtils.copyProperties(ncuser, cpUserVO);
		} catch (Exception e) {
			CpLogger.error(e.getMessage(), e);
			throw new BusinessException(e);
		} 
		PasswordSecurityLevelVO pslVO = PasswordSecurityLevelFinder.getPWDLV(ncuser);
		int len = pslVO.getMinimumLength() == null ? 0 : pslVO.getMinimumLength().intValue();
		// TODO 这里生成一个随机串 以后可能根据密码配置来进行
		String randomSeq = len > 0 ? RandomSeqUtil.getRandomSeq(len) : RandomSeqUtil.getRandomSeq();
		return randomSeq;
	}
	
	@Override
	public void doStaticPasswordVerify(CpUserVO cpUserVO, String password)
			throws BusinessException ,LfwRuntimeException{
		String userPasswd = null;
		PasswordSecurityLevelVO pwdLevel = null;
		UserVO user = new UserVO();
		try {
			if(password == null || password.isEmpty())
				throw new BusinessException(getErrorMessage(9));
			BeanUtils.copyProperties(user, cpUserVO);
			String loginPassword = cpUserVO.getUser_password();
			pwdLevel = PasswordSecurityLevelFinder.getPWDLV(user);
			String message = getErrorMessage(0);
			userPasswd = RbacUserPwdUtil.getEncodedPassword(user, password);
			if (loginPassword == null || !loginPassword.equals(userPasswd)) {
				Integer passwordtimes = 0;
				if (loginPassword == null || !loginPassword.equals(userPasswd)) {
					passwordtimes = cpUserVO.getPasswordtrytimes() == null ? 0 : cpUserVO.getPasswordtrytimes();
					passwordtimes ++;
					//获取密码允许输错次数
					if(pwdLevel!=null && pwdLevel.getErrorloginThreshold() != null){
						if(passwordtimes > pwdLevel.getErrorloginThreshold().intValue()){
							//锁定操作
							cpUserVO.setIslocked(UFBoolean.TRUE);
							cpUserVO.setPasswordtrytimes(0);
							CpbServiceFacility.getCpUserBill().updateCpUserPswTryTimes_RequiresNew(cpUserVO);
							throw new BusinessException(getErrorMessage(5));
						}
					}
					cpUserVO.setPasswordtrytimes(passwordtimes);
					CpbServiceFacility.getCpUserBill().updateCpUserPswTryTimes_RequiresNew(cpUserVO);
					throw new BusinessException(message);
				}
			}
			if(cpUserVO.getPasswordtrytimes()!= null && cpUserVO.getPasswordtrytimes()!=0){//清除输错记录(当错误次数小于7时)
				cpUserVO.setPasswordtrytimes(0);
				CpbServiceFacility.getCpUserBill().updateCpUserPswTryTimes_RequiresNew(cpUserVO);
			}
		} catch (InvocationTargetException e) {
			CpLogger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		} catch (IllegalAccessException e) {
			CpLogger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		} 
		//modified by jizhg 去掉初次登录强制修改密码的逻辑
		//doPasswordCheck(password, user, pwdLevel);
	}
	
	
	/**
	 * 获取错误消息
	 * @param code
	 * @return
	 */
	private String getErrorMessage(int code) {
		String message = null;
		switch (code) {
		case 0:
			message = LfwResBundle.getInstance().getStrByID("ad", "CpUserPasswordServiceImpl-000001")/*账号或密码错误!*/;
			break;
		case 1:
			message = LfwResBundle.getInstance().getStrByID("ad", "CpUserPasswordServiceImpl-000002")/*用户未启用或已停用，请联系管理员*/;
			break;
		case 2:
			message = LfwResBundle.getInstance().getStrByID("ad", "CpUserPasswordServiceImpl-000003")/*用户被锁定,请联系管理员!*/;
			break;
		case 3:
			message = LfwResBundle.getInstance().getStrByID("ad", "CpUserPasswordServiceImpl-000004")/*用户还未生效，请联系管理员*/;
			break;
		case 4:
			message = LfwResBundle.getInstance().getStrByID("ad", "CpUserPasswordServiceImpl-000005")/*此用户已过期,无法登陆!*/;
			break;
		case 5:
			message = LfwResBundle.getInstance().getStrByID("ad", "CpUserPasswordServiceImpl-000006")/*输错次数超过最大限制。用户被锁定!*/;
			break;
		case 6:
			message = LfwResBundle.getInstance().getStrByID("ad", "CpUserPasswordServiceImpl-000007")/*密码不符合安全策略，请修改！*/;
			break;
		case 7:
			message = LfwResBundle.getInstance().getStrByID("ad", "CpUserPasswordServiceImpl-000008")/*您使用的是默认密码，请及时修改密码！*/;
			break;	
		case 8:
			message = LfwResBundle.getInstance().getStrByID("ad", "CpUserPasswordServiceImpl-000009")/*签名验证失败!*/;
			break;		
		case 9:
			message = LfwResBundle.getInstance().getStrByID("ad", "CpUserPasswordServiceImpl-000012")/*密码不能为空!*/;
			break;		
		default:
			break;
		}
		return message;
	}

	/**
	 * 用户登录时密码策略校验
	 * @param password
	 * @param user
	 * @param pwdLevel
	 * @throws LfwRuntimeException
	 * @throws BusinessException
	 */
	private void doPasswordCheck(String password, UserVO user,
			PasswordSecurityLevelVO pwdLevel) throws LfwRuntimeException, BusinessException{
		//检验是否初始用户
		boolean isInit = isInitUser(user.getCuserid());
		if(isInit)
			throw new LfwRuntimeException(LfwResBundle.getInstance().getStrByID("ad", "CpUserPasswordServiceImpl-000010")/*初始用户，请修改密码后登陆系统!*/);
		
		//检验是否是重置密码
		if(isResetUser(user.getCuserid()))
			throw new LfwRuntimeException(LfwResBundle.getInstance().getStrByID("ad", "CpUserPasswordServiceImpl-000011")/*不能使用系统重置的密码，请修改!*/);
		
		IUserPasswordChecker upchecher = (IUserPasswordChecker) NCLocator.getInstance().lookup(IUserPasswordChecker.class.getName());		
		//用户登录时，得到校验与密码等级相关项得到的信息
		String implicitPwd = new Encode().encode(password);
		String pwdCheckMsg = upchecher.getPwdCheckMsg(user, pwdLevel, implicitPwd);
		if(!"ok".equals(pwdCheckMsg))
			throw new LfwRuntimeException(pwdCheckMsg);
		
		//密码过期时间检验
		String  hinttip=upchecher.getValidateTip(user.getPwdparam(), pwdLevel);
		if(hinttip != null)
			throw new CpLoginRunTimeException(hinttip);
	}

	@Override
	public void addPwdResetUser(String userid) throws BusinessException {
		ServiceLocator.getService(IUserExService.class).addPwdResetUser(userid);
		
	}

	@Override
	public void addInitUser(String userid) throws BusinessException {
		ServiceLocator.getService(IUserExService.class).addInitUser(userid);
	}

	@Override
	public void delResetUserInfo(String user_id) {
		UserExManageUtil.getInstance().delResetUserInfo(user_id);
	}
	
	

	@Override
	public void addUserPswHistory(CpUserVO cpUserVO, String pswWord) throws BusinessException {
		
		UserVO ncuser = new UserVO();
		try {
			BeanUtils.copyProperties(ncuser, cpUserVO);
		} catch (Exception e) {
			CpLogger.error(e.getMessage(), e);
			throw new BusinessException(e);
		} 
		PasswordSecurityLevelVO pwdLevel = PasswordSecurityLevelFinder.getPWDLV(ncuser);
		
		UserPasswordVO pswvo = new UserPasswordVO();
		pswvo.setCuserid(ncuser.getCuserid());
		pswvo.setUser_password(pswWord);
		ServiceLocator.getService(IUserPasswordChecker.class).updateUPVO(ncuser.getCuserid(), pswWord, pwdLevel);
	}

	@Override
	public void delInitUserInfo(String user_id) throws BusinessException {
		UserExManageUtil.getInstance().delInitUser(user_id);
	}

	@Override
	public boolean isInitUser(String user_id) throws BusinessException {
		boolean isInit = false;
		List<String> initUserList = ServiceLocator.getCacheableService(ICpUserExService.class).getInitUsers();
		if(initUserList.contains(user_id))
			isInit = true;
		return isInit;
	}

	@Override
	public boolean isResetUser(String user_id) throws BusinessException {
		boolean isReset = false;
		List<String> resetUserList = ServiceLocator.getCacheableService(ICpUserExService.class).getResetUsers();
		if(resetUserList.contains(user_id))
			isReset = true;
		return isReset;
	}

	@Override
	public void lockNcUser(CpUserVO user) throws BusinessException {
		ServiceLocator.getService(IUserLockService.class).updateLockedTag(user.getNcpk(), true);
	}

	@Override
	public String getNcUserPKByUserCode(String code) throws BusinessException {
		String cuserid = null;
		try {
			String where = " user_code_q = '" + CpSqlTranslateUtil.tmsql(code.trim().toUpperCase()) + "' ";
			PtBaseDAO dao = new PtBaseDAO();
			List<UserVO>users =  (List<UserVO>) dao.retrieveByClause(UserVO.class, where);
			//UserVO ncUserVo = NCLocator.getInstance().lookup(IUserManageQuery.class).findUserByCode(code);
			if (users != null && users.size() >0){
				UserVO ncUserVo = users.get(0);
				if(ncUserVo != null)
					cuserid = ncUserVo.getCuserid();
			}
			return cuserid;
		} catch (Exception e) {
			CpLogger.error(e);
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public boolean isUsedInNC(String cuserId, String pk_base_doc) throws BusinessException {
		boolean isUsedInNC = false;
		//ncPK不为空
		String where = " pk_base_doc = '" + pk_base_doc + "' and cuserId !='" + cuserId +"'";
		PtBaseDAO dao = new PtBaseDAO();
		try {
			SuperVO[] users = dao.queryByCondition(UserVO.class, where);
			if(!ArrayUtils.isEmpty(users))
				isUsedInNC = true;
		} catch (DAOException e) {
			CpLogger.error(e);
			throw new BusinessException(e.getMessage());
		}
		
		return isUsedInNC;
	}

	@Override
	public void updateNcUserCode(String cuserId, String newCode)
			throws BusinessException {
		try {
			UserVO ncUserVo = (UserVO) new PtBaseDAO().retrieveByPK(UserVO.class, cuserId);
			ncUserVo.setUser_code(newCode);
			ServiceLocator.getService(IUserManage.class).updateUser(ncUserVo);
		} catch (Exception e) {
			CpLogger.error(e);
			throw new BusinessException(e.getMessage());
		}
		
	}

	@Override
	public void shareUser2Group(String cuserid, String[] original_group,
			String[] new_group) throws BusinessException {
//		ServiceLocator.getService(IUserManage.class).shareUser2Group(cuserid, original_group, new_group);
		
		CpUserVO cpuser = (CpUserVO) new BaseDAO().retrieveByPK(CpUserVO.class, cuserid);
		UserVO user = CpbUtil.convert(cpuser);
	    // 加锁
	    BDPKLockUtil.lockSuperVO(user);
	    // 插入用户--共享集团关联
	    HashMap<String, Object> name_value_map = new HashMap<String, Object>();
	    name_value_map.put("cuserid", cuserid);
	    MiddleEntityPersister<UserShareVO> persister =
	        new MiddleEntityPersister<UserShareVO>(UserShareVO.class,
	            name_value_map, "pk_group");
	    persister.doPersist(original_group, new_group);
	    GroupVO[] orgs =
	    		ServiceLocator.getService(IUserManageQuery.class).queryUserSharedGroup(cuserid);

	    this.writeUserShareLog(user, orgs);

//	    return orgs;
	}
	
	private void writeUserShareLog(UserVO user, GroupVO[] orgs)
		      throws BusinessException {
//	    CpUserGroupShareLogger groupShareLogger =
//	        new CpUserGroupShareLogger(user, orgs);
//	    groupShareLogger.doLog();
	  }

	
}