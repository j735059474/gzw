package nc.scap.pub.index.portlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import nc.message.NCMessageAdapter;
import nc.message.vo.MessageVO;
import nc.message.vo.NCMessage;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.ScapDAO;
import nc.scap.pub.vos.ScapMeetingVO;
import nc.scap.pub.vos.ScapOfficialdocVO;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.log.PortalLogger;
import nc.uap.portal.task.ctrl.TaskQryParam;
import nc.uap.portal.task.ui.TaskPaginationQueryImpl;
import nc.uap.portal.util.PortalRenderEnv;
import nc.uap.portal.util.PtUtil;
import nc.uap.portal.util.freemarker.FreeMarkerTools;
import nc.uap.portlet.base.PtBasePortlet;
import nc.uap.wfm.vo.WfmTaskVO;
import nc.vo.ml.NCLangRes4VoTransl;

public class MyWarnPortlet extends PtBasePortlet {

	private static final String TASK_LIST = "TASK_LIST";

	@Override
	protected void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
	
		Map<String,Object> root = new HashMap<String,Object>();
		
		MessageVO[] msgs = (MessageVO[]) ScapDAO.retrieveByCondition(MessageVO.class, "msgsourcetype = 'prealert' and destination = 'inbox'", "sendtime desc");
		MessageVO[] result = null;
		if(msgs != null) {
			if(msgs.length > 10) {
				result = new MessageVO[10];
				for(int n = 0; n < 10; n++) {
					result[n] = msgs[n];
				}
			}else {
				result = msgs;
			}
		}
		
		String condition = "pk_meeting in (select c2.pk_meeting from scapco_meetingstaff c1 " +
				"left join scapco_meetingreport c2 on c1.pk_meetingreport = c2.pk_meetingreport " +
				"where staff = '" + CntUtil.getCntUserPk() + "') " +
				"and state in ('待反馈','反馈中') and meetingdate > to_char(sysdate, 'yyyy-mm-dd HH24:MI:SS ')";
		ScapMeetingVO[] meetings = (ScapMeetingVO[]) ScapDAO.retrieveByCondition(ScapMeetingVO.class, condition, "meetingdate");
		ScapMeetingVO[] meeting10 = null;
		if(meetings != null) {
			meeting10 = new ScapMeetingVO[10];
			if(meetings.length > 10) {
				for(int n = 0; n < 10; n++) {
					meeting10[n] = meetings[n];
				}
			}else {
				meeting10 = meetings;
			}
		}
		
		condition = "pk_officialdoc in (select pk_officialdoc from scapco_feedbackuser c " +
				"where c.staff = '" + CntUtil.getCntUserPk() + "') and state in ('待反馈','反馈中','无需反馈')";
		ScapOfficialdocVO[] officials = (ScapOfficialdocVO[]) ScapDAO.retrieveByCondition(ScapOfficialdocVO.class, condition, "senddocdate");
		ScapOfficialdocVO[] official10 = null;
		if(officials != null) {
			official10 = new ScapOfficialdocVO[10];
			if(officials.length > 10) {
				for(int n = 0; n < 10; n++) {
					official10[n] = officials[n];
				}
			}else {
				official10 = officials;
			}
		}
		
		String html = null;
		
		String resourcePath = PtUtil.getResourcePath(PortalRenderEnv.getCurrentPage());
		root.put("RES_PATH", resourcePath);
		try {
			root.put("WINID", request.getWindowID());
			if(result != null && result.length > 0) {
				root.put("WARN_LIST", result);
			}
			if(meeting10 != null && meeting10.length > 0) {
				root.put("MEETING_LIST", meeting10);
			}
			if(official10 != null && official10.length > 0) {
				root.put("OFFICIAL_LIST", official10);
			}
			String template = "portlets/MyWarnPortlet.html";
//			html = FreeMarkerTools.contextTemplatRender(template, root);
			html = FreeMarkerTools.render(template, root);
		} catch (Exception e) {
			html = NCLangRes4VoTransl.getNCLangRes().getStrByID("pint", "MyWflPortlet-000000")/*获取待办流程失败:*/ + e.getMessage();
			PortalLogger.error(html, e);
		}
		response.getWriter().print(html);
//		response.getWriter().flush();
		
	}

}
