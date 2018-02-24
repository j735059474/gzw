package nc.scap.pub.messageing.impl;

import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.util.CpExtUtil;
import nc.scap.sysinit.util.ScapSysinitUtil;
import nc.uap.cpb.org.vos.CpUserVO;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.portal.login.thirdparty.ThirdPartyLoginHelper;
import nc.uap.portal.plugins.PluginManager;
import nc.uap.portal.plugins.model.PtExtension;
import nc.uap.portal.task.itf.ITaskQryTmp;
import nc.uap.portal.vo.PtTrdauthVO;
import nc.uap.wfm.engine.TaskProcessUI;
import nc.uap.wfm.message.TaskCreatedMessageSender;
import nc.uap.wfm.message.TaskMessageSenderMgr;
import nc.uap.wfm.model.Task;
import nc.uap.wfm.vo.WfmMessageVO;
import nc.vo.pub.lang.UFDateTime;
import net.extsoft.webservice.service.Generic;
/**
 * 向E-Link推送消息通知
 * @author liyuchen 20140113
 * 
 */
public class MessageReleaseImpl implements TaskCreatedMessageSender{
	
	/** 
	 * 流程启动运行后，会自动回调此方法
	 */
	@Override
	public void sendTaskCteatedMessage(Map<String, Object> messageMap){
		BaseDAO baseDAO = new BaseDAO();
		//获得任务对象
		Task taskVO = (Task)messageMap.get(TaskMessageSenderMgr.CurrentTask);
		//任务标题
		String title = messageMap.get(WfmMessageVO.TITLE)==null?"":messageMap.get(WfmMessageVO.TITLE).toString();
		//获得用户pk
		String pk_user = messageMap.get(TaskMessageSenderMgr.ReceiverUser)==null?"":messageMap.get(TaskMessageSenderMgr.ReceiverUser).toString();
		//获得消息内容
		String content = messageMap.get(WfmMessageVO.CONTENT)==null?"":messageMap.get(WfmMessageVO.CONTENT).toString();
		try{
			PtExtension extension = PluginManager.newIns().getExtension("wfmtaskqry");
			ITaskQryTmp taskQry = extension.newInstance(nc.uap.portal.task.itf.ITaskQryTmp.class);
			TaskProcessUI tpi = taskQry.getTaskProcessUrl(taskVO.getPk_task());
			String url = tpi.getUrl();
			CpUserVO cpUserVO = (CpUserVO)baseDAO.retrieveByPK(CpUserVO.class, pk_user);
			String clientIP = LfwRuntimeEnvironment.getClientIP();
			String loginURL = this.getLoginURL(pk_user,clientIP+url);
			if(content!=null&&!"".equals(content)){
				content += "<a href="+loginURL+" target=_blank >点击查看</a>";
			}
			//获得系统变量Elink地址
			String elinkURL = ScapSysinitUtil.getSysinitStrByCode("elinkpath");
			Generic service = CpExtUtil.getGeneric(elinkURL);
			service.sendSystemMessageEx(IGlobalConstants.DOMAIN_NAME,cpUserVO.getUser_code(), title, content, null, null,
				null, null, "2", null, null);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	/** 
	 * 生成简单登录url
	 */
	public String getLoginURL(String pk_user,String url){
		String loginUrl = LfwRuntimeEnvironment.getClientIP()+"/portal/auth/";
		try {
			/** 令牌登录 */
			PtTrdauthVO ptTrdauthVO = new PtTrdauthVO();
			ptTrdauthVO.setPk_user(pk_user);
			ptTrdauthVO.setUrl(url);
			ptTrdauthVO.setTtl(new UFDateTime(System.currentTimeMillis()+System.currentTimeMillis()));
			String key = ThirdPartyLoginHelper.registerPtTrdauthVO(ptTrdauthVO);
			loginUrl += key;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginUrl;
	}

}
