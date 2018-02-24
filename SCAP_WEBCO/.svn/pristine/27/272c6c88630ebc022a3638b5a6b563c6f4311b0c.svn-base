package nc.scap.pub.scindex.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.RuntimeEnv;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.ScapDAO;
import nc.uap.lfw.servletplus.annotation.Action;
import nc.uap.lfw.servletplus.annotation.Servlet;
import nc.uap.lfw.servletplus.constant.Keys;
import nc.uap.lfw.servletplus.core.impl.BaseAction;
import nc.uap.portal.log.ScapLogger;
import nc.uap.ws.console.utils.vo.Json;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.geotools.data.ows.Request;

@Servlet(path = "/scIndexAction_sc")
public class SCIndexAction_SC extends BaseAction {
	
	public static String configHome = RuntimeEnv.getInstance().getNCHome() + "/hotwebs/portal/scap/page/scindex/config.xml";
	public static Map<String, Map<String, String>> params = new HashMap<String, Map<String,String>>(); 
	
	@Action(method = Keys.POST)
	public void getItems() {
		String code = request.getParameter("code");
		try {
			List<Map<String, String>> maplist = new ArrayList<Map<String,String>>();
			SAXReader saxReader = new SAXReader();
			Document doc = saxReader.read(new File(configHome));
			Element root = doc.getRootElement();
			List<Element> list = root.elements();
			Map<String, String> qxmap = getPortalPageWithAuthority();
			for(Element e : list) {
				if(e.attributeValue("id") != null && e.attributeValue("id").equals(code)) {
					List<Element> items = e.elements();
					for(Element item : items) {
						Map<String , String> map = new HashMap<String, String>();
						map.put("id", item.attributeValue("id"));
						map.put("type", item.attributeValue("type"));
						map.put("code", item.attributeValue("code"));
						map.put("name", item.attributeValue("name"));
						map.put("openStyle", item.attributeValue("openStyle"));
						map.put("icon", "/portal/scap/image/scindex/" + item.attributeValue("icon"));
						if( (item.attributeValue("withoutCtrl") != null && item.attributeValue("withoutCtrl").equals("1")) || 
								(item.attributeValue("code") != null && qxmap.containsKey(item.attributeValue("code"))) ) {
							map.put("enable", "1");
						}else {
							map.put("enable", "0");
						}
						if(item.attributeValue("openStyle") != null && item.attributeValue("openStyle").equals("link")) {
							map.put("url", item.elementText("url"));
						}
						if(item.attributeValue("params") != null && !"".equals(item.attributeValue("params"))) {
							map.put("params", item.attributeValue("params"));
//							String[] params = item.attributeValue("params").split(";");
//							for(String param : params) {
//								map.put(param.substring(0, param.indexOf(":")), param.substring(param.indexOf(":") + 1));
//								setParam(param.substring(0, param.indexOf(":")), param.substring(param.indexOf(":") + 1));
//							}
						}
						maplist.add(map);
					}
					break;
				}
			}
			String json = Json.list2json(maplist);
			print(json);
		}catch (Exception e) {
			e.printStackTrace();
			ScapLogger.error("SC首页获取菜单栏详情时异常，可能的原因：解析XML时出错或空指针异常。", e);
		}
	}
	
	@Action(method = Keys.POST)
	public void getFuncs() {
		String code = request.getParameter("code");
		String sql = "select t.*  from pt_portalpage t where t.pagename = '" + code + "'";
		Map<String, Object> result = (Map<String, Object>) ScapDAO.executeQuery(sql, new MapProcessor());
		if(result != null) {
			byte[] r = (byte[]) result.get("settings");
			try {
				ByteArrayInputStream bais = new ByteArrayInputStream(r);
//				FileInputStream fis = new FileInputStream(new File("setting.xml"));
//				fis.read(r);
				SAXReader reader = new SAXReader();
				reader.setEncoding("GB2312");
				Document doc = reader.read(bais, "GBK");
				Element rootEle = doc.getRootElement();
				String linkGroup = rootEle.attributeValue("linkgroup");
				if(linkGroup == null || "".equals(linkGroup)) {
					print("-1");
					return;
				}

				Map<String, String> qxmap = getPortalPageWithAuthority();
				List<Func> roots = new ArrayList<Func>();
				List<Func> funcs = getMenuitems(linkGroup, roots, qxmap);
				List<Func> rs = new ArrayList<Func>();
				for(Func root : roots) {
					judgeFunc(root, funcs);
					if(root.childs != null) {
						rs.add(root);
					}
				}
				String json = Json.list2json(rs);
				print(json);
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}else {
			print("-1");
		}
	}
	
	public List<Func> getMenuitems(String linkGroup, List<Func> roots, Map<String, String> qxmap) {
		Map<String, String[]> map = request.getParameterMap();
		List<Func> funcs = new ArrayList<Func>();
		StringBuffer sb = new StringBuffer();
		sb.append("select t.pk_menuitem, t.pk_parent, t.name, t.isnotleaf, t.code menuid, c.id funcid, c.devcomponent, c.appid, c.url ");
		sb.append("from cp_menuitem t ");
		sb.append("left join cp_appsnode c on t.pk_funnode = c.pk_appsnode ");
		sb.append("where t.pk_menucategory = '" + linkGroup + "' ");
		sb.append("order by t.pk_parent, t.ordernum");
		List<Map<String, String>> results = (List<Map<String, String>>) ScapDAO.executeQuery(sb.toString(), new MapListProcessor());
		if(results != null && results.size() > 0) {
			for(Map<String, String> result : results) {
				if(result.get("isnotleaf").equals("Y") || (result.get("isnotleaf").equals("N") && qxmap.containsKey(result.get("funcid")))) {
					Func func = new Func();
					func.pkMenuitem = result.get("pk_menuitem");
					func.pkParent = result.get("pk_parent");
					func.name = result.get("name");
					func.menuid = result.get("menuid");
					func.funcid = result.get("funcid");
					func.devcomponent = result.get("devcomponent");
					func.appid = result.get("appid");
					func.url = result.get("url");
					if(result.get("isnotleaf").equals("Y")) {
						func.isnotleaf = true;
						if(func.pkParent != null && !"".equals(func.pkParent)) {
							funcs.add(func);
						}else {
							roots.add(func);
						}
					}else if(result.get("isnotleaf").equals("N") && qxmap.containsKey(result.get("funcid"))) {
						func.isnotleaf = false;
						funcs.add(func);
					}
				}
			}
		}
		return funcs;
	}
	
	public void judgeFunc(Func parent, List<Func> funcs) {
		for(Func func : funcs) {
			if(func.pkParent.equals(parent.pkMenuitem)) {
				if(func.isnotleaf) {
					judgeFunc(func, funcs);
					if(func.childs != null) {
						if(parent.childs == null) {
							parent.childs = new ArrayList<Func>();
						}
						parent.childs.add(func);
					}
				}else {
					if(parent.childs == null) {
						parent.childs = new ArrayList<Func>();
					}
					parent.childs.add(func);
				}
			}
		}
	}
	
	public class Func {
		private String pkMenuitem;
		private String pkParent;
		private String name;
		private String pkFunnode;
		private String menuid;
		private String funcid;
		private String devcomponent;
		private String appid;
		private String url;
		private List<Func> childs;
		private boolean isnotleaf;
		private Map<String, String> params;
		public String getPkMenuitem() {
			return pkMenuitem;
		}
		public void setPkMenuitem(String pkMenuitem) {
			this.pkMenuitem = pkMenuitem;
		}
		public String getPkParent() {
			return pkParent;
		}
		public void setPkParent(String pkParent) {
			this.pkParent = pkParent;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getPkFunnode() {
			return pkFunnode;
		}
		public void setPkFunnode(String pkFunnode) {
			this.pkFunnode = pkFunnode;
		}
		public String getMenuid() {
			return menuid;
		}
		public void setMenuid(String menuid) {
			this.menuid = menuid;
		}
		public String getFuncid() {
			return funcid;
		}
		public void setFuncid(String funcid) {
			this.funcid = funcid;
		}
		public String getDevcomponent() {
			return devcomponent;
		}
		public void setDevcomponent(String devcomponent) {
			this.devcomponent = devcomponent;
		}
		public String getAppid() {
			return appid;
		}
		public void setAppid(String appid) {
			this.appid = appid;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public List<Func> getChilds() {
			return childs;
		}
		public void setChilds(List<Func> childs) {
			this.childs = childs;
		}
		public boolean isIsnotleaf() {
			return isnotleaf;
		}
		public void setIsnotleaf(boolean isnotleaf) {
			this.isnotleaf = isnotleaf;
		}
		public Map<String, String> getParams() {
			return params;
		}
		public void setParams(Map<String, String> params) {
			this.params = params;
		}
	}
	
	public Map<String, String> getPortalPageWithAuthority() {
		Map<String, String> result = new HashMap<String, String>();
		StringBuffer sb = new StringBuffer();
		sb.append("select r.rescode ");
		sb.append("from cp_res r ");
		sb.append("where r.pk_res in ");
		sb.append("		(select distinct (t.pk_res) ");
		sb.append("		 from cp_resp_res t ");
		sb.append("		 where t.pk_responsibility in ");
		sb.append("				(select rr.pk_responsibility ");
		sb.append("				 from cp_roleresp rr ");
		sb.append("				 where rr.pk_role in ");
		sb.append("						(select ur.pk_role ");
		sb.append("						 from cp_userrole ur ");
		sb.append("						 where ur.pk_user = '" + CntUtil.getCntUserPk() + "')))");
		List<Map<String, String>> results = (List<Map<String, String>>) ScapDAO.executeQuery(sb.toString(), new MapListProcessor());
		if(results != null && results.size() > 0) {
			for(Map<String, String> r : results) {
				if(r.get("rescode") != null) {
					String rescode = "";
					if(r.get("rescode").contains(":")) {
						if(r.get("rescode").split(":").length == 2) {
							rescode = r.get("rescode").split(":")[1];
						}
					}else {
						rescode = r.get("rescode");
					}
					result.put(rescode, rescode);
				}
			}
		}
		return result;
	}

	@Action(method = Keys.POST)
	public void setParam() {
		String paramStr = request.getParameter("params");
		if(paramStr != null && !"".equals(paramStr)) {
			String pkUser = CntUtil.getCntUserPk();
			if(params.get(pkUser) == null) {
				params.put(pkUser, new HashMap<String, String>());
			}
			String[] paramArr = paramStr.split(";");
			for(String param : paramArr) {
				if(param.contains(":")) {
					params.get(pkUser).put(param.substring(0, param.indexOf(":")), param.substring(param.indexOf(":") + 1));
				}
			}
		}
	}
	
	public static String getParam(String key) {
		String pkUser = CntUtil.getCntUserPk();
		if(params.get(pkUser) != null) {
			return params.get(pkUser).get(key);
		}
		return null;
	}
	
}
