package nc.scap.pub.index.portlets;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import nc.bs.framework.common.NCLocator;
import nc.scap.pub.util.HttpUtils;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.integrate.credential.PtCredentialVO;
import nc.uap.portal.integrate.sso.itf.ISSOQueryService;
import nc.uap.portal.log.ScapLogger;
import nc.uap.portal.task.ctrl.TaskQryParam;
import nc.uap.portal.task.ui.TaskPaginationQueryImpl;
import nc.uap.portal.util.PortalRenderEnv;
import nc.uap.portal.util.PtUtil;
import nc.uap.portal.util.freemarker.FreeMarkerTools;
import nc.uap.portlet.base.PtBasePortlet;
import nc.uap.wfm.vo.WfmTaskVO;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import sun.misc.BASE64Encoder;

import com.sursen.interfaces.filelist.service.IFilelistService;

public class MyWorkPortlet extends PtBasePortlet {
	
	@Override
	@SuppressWarnings(value = { "unchecked" })
	protected void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {

		Map root = new HashMap();
		
		try {
			Object sursenData = SursenData.getDataWithWinId(request.getWindowID());
			root.put(SursenData.SYSTEM_ID, sursenData);
		} catch (Throwable e) {
			ScapLogger.error("MyWorkPortlet获取数据过程出错：" + SursenData.SYSTEM_ID, e);
		}
		
		try {
			Object hangfeiData = HangfeiData.getFullData();
			root.put(HangfeiData.SYSTEM_ID, hangfeiData);
		} catch (Throwable e) {
			ScapLogger.error("MyWorkPortlet获取数据过程出错：" + HangfeiData.SYSTEM_ID, e);
		}
		
		try {
			String template = "portlets/MyWorkPortlet.html";
			String html = FreeMarkerTools.render(template, root);
			response.getWriter().print(html);
		} catch (Throwable e) {
			ScapLogger.error("渲染MyWorkPortlet出错", e);
		}
	}

	/**
	 * 书生OA
	 */
	@SuppressWarnings(value = { "unchecked", "unused" })
	static class SursenData {
		
		public static final String SYSTEM_ID = "sursen";

		private static final String TASK_LIST_UNREAD = "TASK_LIST_UNREAD";//待办
		private static final String TASK_LIST_READED = "TASK_LIST_READED";//已办
		private static final String TASK_LIST_END = "TASK_LIST_END";//办结
		private static final String TASK_LIST_SUSPEND = "TASK_LIST_SUSPEND";//挂起

		private static final String DOC_LIST_RECV = "recvdoc";//待签收公文
		private static final String DOC_LIST_RECV_NUM = "recvdocnum";//待签收公文条数
		private static final String DOC_LIST_SENDENC = "senddocEncryption";//发文待盖章
		private static final String DOC_LIST_SENDENC_NUM = "senddocEncryptionnum";//发文待盖章条数
		private static final String DOC_LIST_SEND = "senddoc";//发文待发送
		private static final String DOC_LIST_SEND_NUM = "senddocnum";//发文待发送条数
		
		public static Map getDataWithWinId(String winId) {
			
			Map result = new HashMap();

			List<WfmTaskVO> wtlist_unread = new ArrayList<WfmTaskVO>();
			List<WfmTaskVO> wtlist_readed = new ArrayList<WfmTaskVO>();
			List<WfmTaskVO> wtlist_end = new ArrayList<WfmTaskVO>();
			List<WfmTaskVO> wtlist_suspend = new ArrayList<WfmTaskVO>();
			List<Map<String, String>> recvdoclist = new ArrayList<Map<String,String>>();
			List<Map<String, String>> senddocenclist = new ArrayList<Map<String,String>>();
			List<Map<String, String>> senddoclist = new ArrayList<Map<String,String>>();
			
			int count = 10;
			TaskQryParam param = new TaskQryParam();
			param.setId(TaskQryParam.ID_TASK);
			PaginationInfo pi = new PaginationInfo();
			pi.setPageSize(count);
			
			InputStream is = null;
			try {
				/**
				 * 获取凭证VO，读取对方系统用户名、密码
				 */
				ISSOQueryService service = NCLocator.getInstance().lookup(ISSOQueryService.class);
				String userId = LfwRuntimeEnvironment.getLfwSessionBean().getUser_code();
				PtCredentialVO vo = service.getCredentials(userId, "", SYSTEM_ID, pi.getPageIndex());
				String userName = null;
				String base64Pwd = null;
				URL url = null;
				if(vo != null){
					userName = vo.getUserid();
					String password = vo.getPassword();
					if (password != null) {
						BASE64Encoder encoder = new BASE64Encoder();
						base64Pwd = encoder.encode(password.getBytes());
					}
					String wsdlLocation = vo.getCredentialReference().getValue("wsdlLocation");
					if (wsdlLocation != null) {
						url = new URL(wsdlLocation);
					}
				}
				
				/**
				 * 调用WebService获取公文
				 */
				IFilelistService filelistSrv = new IFilelistService(url);
				String xmlString = filelistSrv.getIFilelistServiceHttpPort().getToDoList(userName, base64Pwd, 1);
				is = new ByteArrayInputStream(xmlString.getBytes());
			} catch (Throwable e) {
				ScapLogger.error("与非涉密公文系统对接异常：获取公文列表失败", e);
			}
			
			try {
				param.setStatus(TaskQryParam.STATUS_UNREAD);
				wtlist_unread = TaskPaginationQueryImpl.query(param, pi) ;

				param.setStatus(TaskQryParam.STATUS_READED);
				wtlist_readed = TaskPaginationQueryImpl.query(param, pi) ;

				param.setStatus(TaskQryParam.STATUS_END);
				wtlist_end = TaskPaginationQueryImpl.query(param, pi) ;

				param.setStatus(TaskQryParam.STATUS_SUSPEND);
				wtlist_suspend = TaskPaginationQueryImpl.query(param, pi) ;
				
				recvdoclist = getDocList(is, DOC_LIST_RECV);
				senddocenclist = getDocList(is, DOC_LIST_SENDENC);
				senddoclist = getDocList(is, DOC_LIST_SEND);
			} catch (Exception e) {
			}
			
			String html = null;
			
			String resourcePath = PtUtil.getResourcePath(PortalRenderEnv.getCurrentPage());
			result.put("RES_PATH", resourcePath);
			result.put("WINID", winId);
			if(wtlist_unread.size() > 0){
				result.put(TASK_LIST_UNREAD, ((wtlist_unread.size() > count) ? wtlist_unread.subList(0, count) : wtlist_unread).toArray(new WfmTaskVO[]{}));
			}
			if(wtlist_readed.size() > 0){
				result.put(TASK_LIST_READED, ((wtlist_readed.size() > count) ? wtlist_readed.subList(0, count) : wtlist_readed).toArray(new WfmTaskVO[]{}));
			}
			if(wtlist_end.size() > 0){
				result.put(TASK_LIST_END, ((wtlist_end.size() > count) ? wtlist_end.subList(0, count) : wtlist_end).toArray(new WfmTaskVO[]{}));
			}
//			if(wtlist_suspend.size() > 0){
//				root.put(TASK_LIST_SUSPEND, ((wtlist_suspend.size() > count) ? wtlist_suspend.subList(0, count) : wtlist_suspend).toArray(new WfmTaskVO[]{}));
//			}
			if(recvdoclist.size() > 0){
				result.put(DOC_LIST_RECV, recvdoclist);
			}
			if(senddocenclist.size() > 0){
				result.put(DOC_LIST_SENDENC, senddocenclist);
			}
			if(senddoclist.size() > 0){
				result.put(DOC_LIST_SEND, senddoclist);
			}
			result.put(DOC_LIST_RECV_NUM, recvdoclist.size());
			result.put(DOC_LIST_SENDENC_NUM, senddocenclist.size());
			result.put(DOC_LIST_SEND_NUM, senddoclist.size());
			
			return result;
		}
		
		public static List<Map<String, String>> getDocList(InputStream is, String tagname) {
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();

			try {
				SAXReader saxReader = new SAXReader();
				Document doc = saxReader.read(is);
				Element xmls = doc.getRootElement();
				Element xml = xmls.element("xml");

				Element tag = xml.element(tagname);
				if (tag != null) {
					List<Element> files = tag.elements("files");
					if (files != null && files.size() > 0) {
						for (Element file : files) {
							Map<String, String> map = new HashMap<String, String>();
							List<Element> attrs = file.elements();
							for (Element attr : attrs) {
								map.put(attr.getName(), attr.getText());
							}
							list.add(map);
						}
					}
				}

			} catch (Exception e) {
				ScapLogger.error("解析公文出现异常", e);
			}
			return list;
		}
	}
	
	/**
	 * 杭飞OA
	 */
	static class HangfeiData {
		
		public static final String SYSTEM_ID = "hfoa";
		
		private static String url_GetTotalCount = "http://10.241.12.15/weboa/system/othersys.nsf/getTotalCount?openAgent";
		private static String url_GetListByType = "http://10.241.12.15/weboa/system/othersys.nsf/getListByType?openAgent";
		
		public static Map getFullData() {
			
			Map result = new HashMap();
			
			// 获取sso信息
			String userName = null;
			String password = null;
			try {
				ISSOQueryService service = NCLocator.getInstance().lookup(ISSOQueryService.class);
				String userId = LfwRuntimeEnvironment.getLfwSessionBean().getUser_code();
				PtCredentialVO vo = service.getCredentials(userId, "", SYSTEM_ID, 1);
				
				userName = vo.getUserid();
				password = vo.getPassword();
				{
					String temp = null;
					temp = vo.getCredentialReference().getValue("url_GetTotalCount");
					if (temp != null && temp.length() > 0) {
						url_GetTotalCount = temp;
					}
					temp = vo.getCredentialReference().getValue("url_GetListByType");
					if (temp != null && temp.length() > 0) {
						url_GetListByType = temp;
					}
				}
			} catch (PortalServiceException e) {
				throw new LfwRuntimeException("为关联凭据：" + SYSTEM_ID, e);
			}
			
			// getTotalCount
			Map<String, Integer> countMap = new HashMap<String, Integer>();
			try {
				String xmlStr = HttpUtils.post(url_GetTotalCount, "user=" + userName + "&" + "pwd=" + password, "GB2312");
				SAXReader saxReader = new SAXReader();
				Document doc = saxReader.read(new ByteArrayInputStream(xmlStr.getBytes()));
				Element root = doc.getRootElement();
				for (Element item : root.elements("item")) {
					String type = item.element("type").getStringValue();
					String count = item.element("count").getStringValue();
					countMap.put(type, Integer.parseInt(count));
				}
			} catch(Exception e) {
				throw new LfwRuntimeException("解析OA数据条数出错", e);
			}
			
			// getListByType
			Map subjectListMap = new HashMap();
			try {
				for (Entry<String, Integer> entry : countMap.entrySet()) {
					String type = entry.getKey();
					
					List<Map> subjectList = new ArrayList<Map>();
					subjectListMap.put(type + "List", subjectList);
					
					String xmlStr = HttpUtils.post(url_GetListByType, "user=" + userName + "&" + "pwd=" + password + "&count=5&" + "type=" + type, "GB2312");
					SAXReader saxReader = new SAXReader();
					Document doc = saxReader.read(new ByteArrayInputStream(xmlStr.getBytes()));
					Element root = doc.getRootElement();
					
					List<Element> itemList = root.elements("item");
					for (Element item : itemList) {
						String title = item.element("subject").getStringValue();
						String href = item.element("link").getStringValue();
						
						Map<String, String> subject = new HashMap<String, String>();
						subject.put("title", title);
						subject.put("href", href);
						subjectList.add(subject);
					}
				}
			} catch(Exception e) {
				throw new LfwRuntimeException("解析OA数据条数出错", e);
			}
			
			// result to return
			
			for (Entry<String, Integer> countEntry : countMap.entrySet()) {
				result.put(countEntry.getKey() + "Count", countEntry.getValue());
			}
			result.putAll(subjectListMap);
			
			return result;
		}
	}
}
