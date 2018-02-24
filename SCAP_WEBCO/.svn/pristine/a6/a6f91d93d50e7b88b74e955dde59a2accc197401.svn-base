package com.scap.pub.login;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import nc.scap.pub.util.ScapDAO;
import nc.uap.cpb.org.vos.CpUserVO;
import nc.uap.lfw.servletplus.annotation.Action;
import nc.uap.lfw.servletplus.annotation.Servlet;
import nc.uap.lfw.servletplus.constant.Keys;
import nc.uap.lfw.servletplus.core.impl.BaseAction;
import nc.uap.portal.deploy.vo.PtSessionBean;
import nc.uap.portal.user.impl.CpUser;

@Servlet(path = "/changeuserinfo")
public class ChangeUserInfoAction extends BaseAction {
	
	@Action(method = Keys.POST)
	public void change() {
		HttpSession session = request.getSession();
		String userId = (String)session.getAttribute("userId");
		PtSessionBean sessionbean = (PtSessionBean)session.getAttribute("LOGIN_SESSION_BEAN");
		CpUser user = (CpUser)sessionbean.getUser();
		String pkuser = user.getPk_user();
		CpUserVO uservo = user.getUser();
		String pk_base_doc = uservo.getPk_base_doc();
		String username = request.getParameter("username");
		String mobilenum = request.getParameter("mobilenum");
		if (StringUtils.isNotBlank(username)) {
			String sqlUpdate_cp = " update cp_user t set t.user_name = '" + username + "' where t.cuserid = '" + pkuser + "' ";
			String sqlUpdate_bd = " update bd_psndoc t set t.NAME = '" + username + "' where t.pk_psndoc = '" + pk_base_doc + "' ";
			ScapDAO.executeUpdate(sqlUpdate_cp);
			ScapDAO.executeUpdate(sqlUpdate_bd);
		}
		if (StringUtils.isNotBlank(mobilenum)) {
			String sqlUpdate_bd = " update bd_psndoc t set t.mobile = '" + mobilenum + "' where t.pk_psndoc = '" + pk_base_doc + "' ";
			ScapDAO.executeUpdate(sqlUpdate_bd);
		}
	}
}
