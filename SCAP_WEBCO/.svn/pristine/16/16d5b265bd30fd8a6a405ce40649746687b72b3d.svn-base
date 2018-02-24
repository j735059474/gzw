package nc.scap.pub.scindex.action;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.dom4j.Attribute;
import org.dom4j.Document;  
import org.dom4j.DocumentException;  
import org.dom4j.Element;  
import org.dom4j.io.SAXReader; 
import org.xml.sax.InputSource;

import nc.bs.framework.common.RuntimeEnv;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.message.vo.MessageVO;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.ScapDAO;
import nc.scap.pub.vos.NewsVO;
import nc.uap.lfw.core.ContextResourceUtil;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.lfw.servletplus.annotation.Action;
import nc.uap.lfw.servletplus.annotation.Servlet;
import nc.uap.lfw.servletplus.constant.Keys;
import nc.uap.lfw.servletplus.core.impl.BaseAction;
import nc.uap.portal.log.ScapLogger;
import nc.uap.portal.task.ctrl.TaskQryParam;
import nc.uap.portal.task.ui.TaskPaginationQueryImpl;
import nc.uap.wfm.vo.WfmTaskVO;
import nc.uap.ws.console.utils.vo.Json;
import nc.vo.pub.lang.UFBoolean;
import uap.json.JSONArray;
import uap.json.JSONObject;

@Servlet(path = "/scIndexAction")
public class SCIndexAction extends BaseAction{
	
	public static String configHome = RuntimeEnv.getInstance().getNCHome() + "/hotwebs/portal/scap/page/scindex/config.xml";
	
	public String unescape4Content(Object content) {
		ScriptEngineManager sem = new ScriptEngineManager();
		ScriptEngine se = sem.getEngineByName("javascript");
		String callbackvalue="";
		try {
			se.eval("function sayHello() {"
			+ " return decodeURIComponent(strname); }");
			Invocable invocableEngine = (Invocable) se;
			se.put("strname", content);
			callbackvalue=(String) invocableEngine.invokeFunction("sayHello");
		} catch (ScriptException e) {
//	　　 		// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
//	　　 		// TODO Auto-generated catch block
		 	e.printStackTrace();
		}
		return callbackvalue;
	}
	public String xmlElements4URL(String xmlDoc) {

        //创建一个新的字符串
        StringReader read = new StringReader(xmlDoc);
        //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
        InputSource source = new InputSource(read);
        //创建一个新的SAXBuilder
        SAXReader sb = new SAXReader();
        String url = "";
        try {
            //通过输入源构造一个Document
            Document doc = sb.read(source);
            //取的根元素
            Element root = doc.getRootElement();
            System.out.println(root.getName());//输出根元素的名称（测试）
            //得到根元素所有子元素的集合
            List elements = root.elements();
            Element et = null;
            for(int i=0;i<elements.size();i++){
                et = (Element) elements.get(i);//循环依次得到子元素
                if (null!=et.getName()&&!("".equalsIgnoreCase(et.getName()))
                		&&"img".equalsIgnoreCase(et.getName())) {
                	url = et.attributeValue("src");
                	break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }
	
	public String getPicURL(Object obj) {
		String url="";
		String[] list = unescape4Content(obj).trim().split("<p>");
		if (list==null||list.length==0) {
			return url;
		}
		for (String s:list) {
			url = xmlElements4URL("<p>"+s);
			if (url!=null&&!("".equalsIgnoreCase(url))) {
				break;
			}
		}
		return url;
	}
	
	@Action(method = Keys.POST)
	public void getPicById() {
		String pkNews = request.getParameter("pkid");
		NewsVO newsVO = (NewsVO) ScapDAO.retrieveByPK(NewsVO.class, pkNews);
		print(unescape4Content(newsVO.getContent()));
	}
	
	@Action(method = Keys.POST)
	public void getPics() {
		/**
		File picFolder = new File(ContextResourceUtil.getCurrentAppPath() + "/scap/image/scindex/pics");
		File[] pics = picFolder.listFiles();
		String[] picUrls = new String[pics.length];
		String url = "/portal/scap/image/scindex/pics/";
		for(int i = 0; i < pics.length; i++) {
			picUrls[i] = url + pics[i].getName();
		}
		String json = Json.array2json(picUrls);
		print(json);*/

		int length = 5;
		StringBuffer sb= new StringBuffer();
		sb.append(" select t.*,Rownum rn from scap_news t ");
		sb.append(" where type = '8' and xiajia!='2' and rowNum <= ").append(length); // and formstate = 'End'
		sb.append(" order by ts desc ");
		List<Map<String, Object>> results = (List<Map<String, Object>>)ScapDAO.executeQuery(sb.toString(), new MapListProcessor());
		
		if(results != null && results.size() > 0) {
			String[][] news = new String[results.size()][4];
			for (int i = 0; i < results.size(); i++) {
				news[i][0] = results.get(i).get(NewsVO.PK_NEWS).toString();
				
				news[i][1] = getPicURL(results.get(i).get(NewsVO.CONTENT));
				if(results.get(i).get(NewsVO.BILLMAKEDATE) != null) {
					news[i][2] = results.get(i).get(NewsVO.BILLMAKEDATE).toString().substring(0, 10);
				}
				news[i][3] = results.get(i).get(NewsVO.TITLE).toString();
			}
			String json = Json.array2json(news);
			print(json);
		} else {
			print(-1);
		}
	}
	
	@Action(method = Keys.POST)
	public void getNotices() {
		NewsVO[] vos = (NewsVO[]) ScapDAO.retrieveByCondition(NewsVO.class, " xiajia!='2' and type = '3' and formstate = 'End' and rowNum <= 5");	
		if(vos != null && vos.length > 0) {
			String[][] ctr = new String[vos.length][3];
			for(int i = 0; i < vos.length; i++) {
				ctr[i][0] = vos[i].getPk_news();
				ctr[i][1] = vos[i].getTitle();
				if(vos[i].getBillmakedate() != null) {
					ctr[i][2] = vos[i].getBillmakedate().getDate().toString().substring(0, 10);
				}
				
			}
			String json = Json.array2json(ctr);
			print(json);
		}else {
			print(-1);
		}
	}
	
	@Action(method = Keys.POST)
	public void getMyWorks() {			
		List<WfmTaskVO> wtlist = new ArrayList<WfmTaskVO>();
		TaskQryParam param = new TaskQryParam();
		param.setId(TaskQryParam.ID_TASK);
		PaginationInfo pi = new PaginationInfo();
		pi.setPageSize(6);
		param.setStatus(TaskQryParam.STATUS_UNREAD);	
		
		try {
			wtlist = TaskPaginationQueryImpl.query(param, pi) ;
			if(wtlist != null && wtlist.size() > 0) {
				String[][] wts = new String[wtlist.size()][5];
				for(int i = 0; i < wtlist.size(); i++) {
					String title = "[" + wtlist.get(i).getFlowtypename() + "] " + wtlist.get(i).getSysext4() + "提交了一张单据，请您审核！";
					wts[i][0] = wtlist.get(i).getPk_task();
					wts[i][1] = title;
					if(wtlist.get(i).getFormatstartdate() != null) {
						wts[i][2] = wtlist.get(i).getFormatstartdate().substring(0, 10);
					}
					if(wtlist.get(i).getSysext3() != null) {
						wts[i][3] = wtlist.get(i).getSysext3();
					}else {
						wts[i][3] = "wfmtaskqry";
					}
					if(wtlist.get(i).getHumactname() != null) {
						wts[i][4] = wtlist.get(i).getHumactname();
					}else {
						wts[i][4] = "";
					}
				}
				String json = Json.array2json(wts);
				print(json);
			}else {
				print(-1);
			}
		} catch (Exception e) {
			ScapLogger.error("SC首页获取我的工作：待办列表时异常。", e);
			print(-1);
		}
	}
	
	@Action(method = Keys.POST)
	public void getGzNews() {
		NewsVO[] vos = (NewsVO[]) ScapDAO.retrieveByCondition(NewsVO.class, " xiajia!='2' and type = '1' and formstate = 'End' and rowNum <= 10");	
		if(vos != null && vos.length > 0) {
			String[][] news = new String[vos.length][3];
			for(int i = 0; i < vos.length; i++) {
				news[i][0] = vos[i].getPk_news();
				news[i][1] = vos[i].getTitle();
				if(vos[i].getBillmakedate() != null) {
					news[i][2] = vos[i].getBillmakedate().getDate().toString().substring(0, 10);
				}
			}
			String json = Json.array2json(news);
			print(json);
		}else {
			print(-1);
		}
	}
	/**
	 * 获取总新闻数
	 */
	@Action(method = Keys.POST)
	public void getGzNewsNum() {
		String condition =  " xiajia!='2' and  type = '1' and formstate = 'End' ";
		int count = ScapDAO.getCount(NewsVO.class, condition);
		if(count > 0) {
			int length = 10;
			int pagenum = count/length+(count%length==0?0:1);
			print(pagenum);
		}else {
			print(-1);
		}
	}
	@Action(method = Keys.POST)
	public void getAllGzNews() {
		
		String pagenum = request.getParameter("pagenum")==null?"":request.getParameter("pagenum").toString();
		if ("".equalsIgnoreCase(pagenum)) {
			print(-1);
			return;
		}
		int length = 10;
		int before = length*(Integer.parseInt(pagenum)-1);
		int after = length*(Integer.parseInt(pagenum));
		
		StringBuffer sb= new StringBuffer();
		sb.append(" select * from (select t.*,Rownum rn from scap_news t ");
		sb.append(" where type = '1' and xiajia!='2' and formstate = 'End' and rowNum <= ").append(after);
		sb.append(" order by ts desc) a where a.rn > ").append(before);
		List<Map<String, Object>> results = (List<Map<String, Object>>)ScapDAO.executeQuery(sb.toString(), new MapListProcessor());
		
		if(results != null && results.size() > 0) {
			String[][] news = new String[results.size()][3];
			for (int i = 0; i < results.size(); i++) {
				news[i][0] = results.get(i).get(NewsVO.PK_NEWS).toString();
				news[i][1] = results.get(i).get(NewsVO.TITLE).toString();
				if(results.get(i).get(NewsVO.BILLMAKEDATE) != null) {
					news[i][2] = results.get(i).get(NewsVO.BILLMAKEDATE).toString().substring(0, 10);
				}
			}
			String json = Json.array2json(news);
			print(json);
		} else {
			print(-1);
		}
		
	}
	
	/**
	 * 获取未读消息条数
	 */
	@Action(method = Keys.POST)
	public void getMyMsgNum() {
		String condition = "receiver = '" + CntUtil.getCntUserPk() + "' and isread = 'N'";
		int count = ScapDAO.getCount(MessageVO.class, condition);
		if(count > 0) {
			int length = 10;
			int pagenum = count/length+(count%length==0?0:1);
			print(count+"|"+pagenum);
		}else {
			print(-1+"|"+-1);
		}
	}
	
	/**
	 * 获取未读消息列表
	 */
	@Action(method = Keys.POST)
	public void getMyMsgs() {
		StringBuffer sb = new StringBuffer();
		sb.append("select *");
		sb.append("from (");
		sb.append("      select t.pk_message, t.subject, t.sendtime ");
		sb.append("      from sm_msg_content t where t.receiver = '" + CntUtil.getCntUserPk() + "' and t.isread = 'N' ");
		sb.append("      order by t.sendtime desc ) ");
		sb.append("where rowNum <= 10");
		List<Map<String, String>> results = (List<Map<String, String>>) ScapDAO.executeQuery(sb.toString(), new MapListProcessor());
		if(results != null && results.size() > 0) {
			JSONArray jsons = new JSONArray();
			for(Map<String, String> result : results) {
				JSONObject json = new JSONObject();
				json.put("pkid", result.get("pk_message"));
				json.put("subject", result.get("subject"));
				if(result.get("sendtime") != null) {
					json.put("sendtime", result.get("sendtime").substring(0, 10));
				}
				jsons.put(json);
			}
			print(jsons);
		}else {
			print(-1);
		}
	}

	/**
	 * 获取未读消息列表
	 */
	@Action(method = Keys.POST)
	public void getAllMyMsgs() {
		
		String pagenum = request.getParameter("pagenum")==null?"":request.getParameter("pagenum").toString();
		if ("".equalsIgnoreCase(pagenum)) {
			print(-1);
			return;
		}
		int length = 10;
		int before = length*(Integer.parseInt(pagenum)-1);
		int after = length*(Integer.parseInt(pagenum));
		
		StringBuffer sb= new StringBuffer();
		sb.append(" select * from (select t.*,Rownum rn from sm_msg_content t ");
		sb.append(" where receiver = '").append(CntUtil.getCntUserPk()).append("' and isread = 'N' and rowNum <= ").append(after);
		sb.append(" order by sendtime desc) a where a.rn > ").append(before);
		List<Map<String, Object>> results = (List<Map<String, Object>>)ScapDAO.executeQuery(sb.toString(), new MapListProcessor());
		
		if(results != null && results.size() > 0) {
			String[][] msgs = new String[results.size()][3];
			for(int i = 0; i < results.size(); i++) {
				
				String table = MessageVO.getDefaultTableName();
						
				msgs[i][0] = results.get(i).get(MessageVO.PK_MESSAGE).toString();
				/**msgs[i][1] = results.get(i).get(MessageVO.CONTENT).toString();*/
				msgs[i][1] = results.get(i).get(MessageVO.SUBJECT)==null?"":results.get(i).get(MessageVO.SUBJECT).toString();
				if(results.get(i).get(MessageVO.SENDTIME) != null) {
					msgs[i][2] = results.get(i).get(MessageVO.SENDTIME).toString().substring(0, 10);
				}
			}
			String json = Json.array2json(msgs);
			print(json);
		} else {
			print(-1);
		}
	}
	@Action(method = Keys.POST)
	public void getMsgById() {
		String pkMessage = request.getParameter("pkid");
		MessageVO msg = (MessageVO) ScapDAO.retrieveByPK(MessageVO.class, pkMessage);
		msg.setIsread(new UFBoolean(true));
		ScapDAO.updateVO(msg);
		if(msg != null) {
			Object content = msg.getContent();
			if(content != null && !content.equals("")) {
				String c = content.toString();
				print(c);	
			}
		}
	}
	
	/**
	 * 获取政策法规库列表
	 */
	@Action(method = Keys.POST)
	public void getZcfgk() {
		String sql = "select pk_policy_regulation, reg_no, reg_name, billmakedate " +
				"from scapcr_policy_regulation " +
				"where dr=0 and is_gzw='2' and submit_status='1' and  rowNum <= 10" +
				"order by billmakedate desc";
		List<Map<String, String>> results = (List<Map<String, String>>) ScapDAO.executeQuery(sql, new MapListProcessor());
		
		if(results != null && results.size() > 0) {
			String[][] zcfg = new String[results.size()][3];
			for(int i = 0; i < results.size(); i++) {
				zcfg[i][0] = results.get(i).get("pk_policy_regulation");
				zcfg[i][1] = results.get(i).get("reg_name");
				if(results.get(i).get("billmakedate") != null) {
					zcfg[i][2] = results.get(i).get("billmakedate").substring(0, 10);
				}
			}
			String json = Json.array2json(zcfg);
			print(json);
		}else {
			print(-1);
		}
	}
	
	/**
	 * 获取纪检案件信息列表
	 */
	@Action(method = Keys.POST)
	public void getJjajxx() {
		NewsVO[] vos = (NewsVO[]) ScapDAO.retrieveByCondition(NewsVO.class, " xiajia!='2' and type = '5' and formstate = 'End' and rowNum <= 10");	
		if(vos != null && vos.length > 0) {
			String[][] news = new String[vos.length][3];
			for(int i = 0; i < vos.length; i++) {
				news[i][0] = vos[i].getPk_news();
				news[i][1] = vos[i].getTitle();
				if(vos[i].getBillmakedate() != null) {
					news[i][2] = vos[i].getBillmakedate().getDate().toString().substring(0, 10);
				}
			}
			String json = Json.array2json(news);
			print(json);
		}else {
			print(-1);
		}
	}
	
	/**
	 * 获取工程建设失信行为列表
	 */
	@Action(method = Keys.POST)
	public void getGcjssxxwxx() {
		NewsVO[] vos = (NewsVO[]) ScapDAO.retrieveByCondition(NewsVO.class, " xiajia!='2' and type = '6' and formstate = 'End' and rowNum <= 10");	
		if(vos != null && vos.length > 0) {
			String[][] news = new String[vos.length][3];
			for(int i = 0; i < vos.length; i++) {
				news[i][0] = vos[i].getPk_news();
				news[i][1] = vos[i].getTitle();
				if(vos[i].getBillmakedate() != null) {
					news[i][2] = vos[i].getBillmakedate().getDate().toString().substring(0, 10);
				}
			}
			String json = Json.array2json(news);
			print(json);
		}else {
			print(-1);
		}
	}
	
	/**
	 * 获取企业文化专栏信息
	 */
	@Action(method = Keys.POST)
	public void getQywhzl() {
		NewsVO[] vos = (NewsVO[]) ScapDAO.retrieveByCondition(NewsVO.class, " xiajia!='2' and type = '7' and formstate = 'End' and rowNum <= 10");	
		if(vos != null && vos.length > 0) {
			String[][] news = new String[vos.length][3];
			for(int i = 0; i < vos.length; i++) {
				news[i][0] = vos[i].getPk_news();
				news[i][1] = vos[i].getTitle();
				if(vos[i].getBillmakedate() != null) {
					news[i][2] = vos[i].getBillmakedate().getDate().toString().substring(0, 10);
				}
			}
			String json = Json.array2json(news);
			print(json);
		}else {
			print(-1);
		}
	}
	
	/**
	 * 获取友情链接
	 */
	@Action(method = Keys.POST)
	public void getLinks() {
		try {
			List<Map<String, String>> maplist = new ArrayList<Map<String,String>>();
			SAXReader saxReader = new SAXReader();
			Document doc = saxReader.read(new File(configHome));
			Element root = doc.getRootElement();
			Element links = root.element("links");
			List<Element> linkList = links.elements();
			if(linkList != null && linkList.size() > 0) {
				JSONArray json = new JSONArray();
				for(Element link : linkList) {
					String name = link.attributeValue("name");
					String url = link.attributeValue("url");
					JSONObject jo = new JSONObject();
					jo.put("name", name);
					jo.put("url", url);
					json.put(jo);
				}
				print(json);
			}else {
				print(-1);
			}
		}catch (Exception e) {
			e.printStackTrace();
			ScapLogger.error("SC首页获取菜单栏详情时异常，可能的原因：解析XML时出错或空指针异常。", e);
		}
	}
	
	@Action(method = Keys.POST)
	public void getDownloads() {
		try {
			List<Map<String, String>> maplist = new ArrayList<Map<String,String>>();
			SAXReader saxReader = new SAXReader();
			Document doc = saxReader.read(new File(configHome));
			Element root = doc.getRootElement();
			Element download = root.element("download");
			List<Element> fileList = download.elements();
			if(fileList != null && fileList.size() > 0) {
				JSONArray json = new JSONArray();
				for(Element file : fileList) {
					String name = file.attributeValue("name");
					String i18n = file.attributeValue("i18n");
					String memo = file.attributeValue("memo");
					JSONObject jo = new JSONObject();
					jo.put("name", name);
					jo.put("i18n", i18n);
					jo.put("memo", memo);
					json.put(jo);
				}
				print(json);
			}else {
				print(-1);
			}
		}catch (Exception e) {
			e.printStackTrace();
			ScapLogger.error("SC首页获取菜单栏详情时异常，可能的原因：解析XML时出错或空指针异常。", e);
		}
	}
	
	@Action(method = Keys.POST)
	public void getNewsById(){
		String pkNews = request.getParameter("pkid");
		NewsVO news = (NewsVO) ScapDAO.retrieveByPK(NewsVO.class, pkNews);
		if(news != null) {
			try {
				Object content = news.getContent();
				String c = "";
				if(content != null) {
					c = URLDecoder.decode(content.toString(), "UTF-8");
					
				}
				print(c);	
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
	}
}
