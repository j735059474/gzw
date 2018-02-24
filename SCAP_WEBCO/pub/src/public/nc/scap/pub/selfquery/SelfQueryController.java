package nc.scap.pub.selfquery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.ddc.IBizObjStorage;
import nc.uap.cpb.log.CpLogger;
import nc.uap.ctrl.tpl.print.TemplateConstantArgs;
import nc.uap.ctrl.tpl.qry.FromWhereSQLImpl;
import nc.uap.lfw.app.plugin.AppControlPlugin;
import nc.uap.lfw.core.base.ExtAttribute;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifPlugoutCmd;
import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.comp.ImageComp;
import nc.uap.lfw.core.comp.LinkComp;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.OpenProperties;
import nc.uap.lfw.core.ctx.ViewContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.event.LinkEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.jsp.uimeta.UIFlowvLayout;
import nc.uap.lfw.jsp.uimeta.UILayoutPanel;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.core.ObjectNodeVO;

import org.apache.commons.lang.StringUtils;

import uap.lfw.core.ml.LfwResBundle;
import uap.lfw.imp.query.base.CpSimpleQueryUtils;
import uap.lfw.imp.query.comp.SimpleQueryUtils;
import uap.lfw.imp.query.event.SimpleQueryDefaultQueryManager;
import uap.lfw.imp.query.event.SimpleQuerySearchEventManager;

public class SelfQueryController {

	// 查询模板触发快速查询的plugin
	public static String QUERYTEMPALTEPLUGIN = "queryTemplatePlugin";

	// 查询模板与快速查询的plugout
	public static String QUERYTEMPALTEPLUGOUT = "queryTemplatePlugout";
	
	public static final String ADVANCED_QUERY_NODE = "uap.lfw.imp.query.advquery";

	public void loadSimpleQueryData(DataLoadEvent e) {
		e.getOriginalKeyValue();
	}
	
	/**
	 * 清空快速查询中设置值
	 * @param e
	 */
	public void onClearValue(MouseEvent<?> e){
		SimpleQueryUtils.clearValue();
	}

	/**
	 * 设置高级查询的pagemodel
	 * @return
	 */
	public String getAdvacePageModel(){
		return null;
	}
	
	/**
	 * 加载页面数据
	 */
	public void beforeShowLoad(DialogEvent dialogEvent){
		if(CpSimpleQueryUtils.isLoadQryBtOk()){
			onQryBtOk(null);
		}
	}
	
	/**
	 * 打开高级查询页面
	 * @param e
	 */
	public void onQueryClick(LinkEvent e) {
		Map<String,String> queryMap = new HashMap<String, String>();
		String model = getAdvacePageModel();
		if(model != null){
			queryMap.put("model", model);
		}
		String queryNode = (String) AppLifeCycleContext.current().getApplicationContext().getAppSession().getOriginalParamMap().get(AppControlPlugin.NODECODE);
		String nodekey = (String) AppLifeCycleContext.current().getApplicationContext().getAppAttribute(LfwWindow.$QUERYTEMPLATE);
		queryMap.put(LfwWindow.$QUERYTEMPLATE, queryNode);
		queryMap.put(SelfQueryWidgetProvider.NODEKEY, nodekey);
		OpenProperties props = new OpenProperties();
		props.setWidth(TemplateConstantArgs.ADV_WIDTH);
		props.setHeight(TemplateConstantArgs.ADV_HEIGHT);
		props.setTitle(NCLangRes4VoTransl.getNCLangRes().getStrByID("uapquerytemplate", "SimpleQueryController-000001")/*高级查询*/);
		props.setOpenId(ADVANCED_QUERY_NODE);
		props.setParamMap(queryMap);
		AppLifeCycleContext.current().getViewContext().navgateTo(props);
	}

	/**
	 * 点击简单查询"搜索"时触发
	 * @param e
	 */
	public void onQryBtOk(MouseEvent<ButtonComp> e) {
		SelfQuerySearchEventManager.simpleSearch();
	}

	/**
	 * 高级查询qryout触发，中转到简单查询conditionQueryPlugin
	 * @param paramMap
	 */
	public void conditionQueryPlugin(Map<String, Object> paramMap) {
		AppLifeCycleContext ctx = AppLifeCycleContext.current();
		ViewContext viewCtx = ctx.getWindowContext().getCurrentViewContext();
		CmdInvoker.invoke(new UifPlugoutCmd(viewCtx.getId(), SelfQueryWidgetProvider.QRYOUT, paramMap));
	}
	
	/**
	 * 刷新查询方案
	 * @param paramMap
	 */
	public void refreshViewPlugin(Map<String, Object> paramMap) {
		AppLifeCycleContext ctx = AppLifeCycleContext.current();
		ViewContext viewCtx = ctx.getWindowContext().getCurrentViewContext();
		ctx.getWindowContext().refreshView(viewCtx.getId());
	}
	

//	public void pluginadvancePlugin(Map paramMap) {
//		AppLifeCycleContext ctx = AppLifeCycleContext.current();
//		ViewContext viewCtx = ctx.getWindowContext().getCurrentViewContext();
//		CmdInvoker
//				.invoke(new UifPlugoutCmd(viewCtx.getId(), "qryout", paramMap));
//		AppLifeCycleContext.current().getApplicationContext().closeWinDialog();
//
//	}

//	/**
//	 * 查询模板确定时触发plugin,plugin的id是queryTemplatePlugin
//	 */
//	public void pluginqueryTemplatePlugin(Map<Object, Object> keys) {
//		AppLifeCycleContext ctx = AppLifeCycleContext.current();
//		ViewContext viewCtx = ctx.getWindowContext().getCurrentViewContext();
//		Map<String,Object> paramMap = new HashMap<String,Object>();
//		paramMap.put("whereSql", "");
//		CmdInvoker
//				.invoke(new UifPlugoutCmd(viewCtx.getId(), "qryout", paramMap));
//	}

	public void onCleanBtOk(MouseEvent<ButtonComp> e) {
		AppLifeCycleContext ctx = AppLifeCycleContext.current();
		ViewContext viewCtx = ctx.getWindowContext().getCurrentViewContext();
		Dataset ds = viewCtx.getView().getViewModels().getDataset(
				SelfQueryWidgetProvider.MAINDS);
		ds.removeRow(0);
		ds.addRow(ds.getEmptyRow());
		ds.setRowSelectIndex(0);
	}

	/**
	 * 快速搜索重置查询编辑默认值，widgetId是快速搜索公共片段id
	 * 
	 * @param widgetId
	 */
	public static void cleanFormDs(String widgetId) {
		AppLifeCycleContext ctx = AppLifeCycleContext.current();
		if (ctx == null)
			return;
		ViewContext viewCtx = ctx.getWindowContext().getViewContext(widgetId);
		if (viewCtx == null)
			return;
		Dataset ds = viewCtx.getView().getViewModels().getDataset(
				SelfQueryWidgetProvider.MAINDS);
		if (ds == null)
			return;
		ds.clear();
		Row row = ds.getEmptyRow();
		ds.addRow(row);
		ds.setRowSelectIndex(ds.getRowIndex(row));
	}

	public static FromWhereSQLImpl autoLoadDefaultQuery(String widgetId) {
		return SimpleQueryDefaultQueryManager.defaultQuery(widgetId);
	}
	
	public void noconditionQueryPlugin(Map<Object, Object> keys){
		onQryBtOk(null);
	}
	
	private static final String STORAGE_CLASS_NAME = "nc.bs.pub.quertytemplate.MyFavoritesStorage";
	public void loadQueryPlan(DataLoadEvent e){
		String SQLStr = getLoadSQL();
		if(SQLStr == null)
			return;
		Dataset ds = e.getSource();
		IBizObjStorage storage = (IBizObjStorage) NCLocator.getInstance().lookup(IBizObjStorage.class.getName());
		// 查找出数据库中满足SQL条件的所有节点。
		try{
			ObjectNodeVO[] objNodeVOs = storage.loadAllObjectNodeData(null, STORAGE_CLASS_NAME, SQLStr);
			if(objNodeVOs != null){
				for(ObjectNodeVO obj : objNodeVOs){
					Row row = ds.getEmptyRow();
					ds.addRow(row);
					row.setString(0, obj.getId());
					row.setString(1, obj.getGuid());
					row.setString(2, obj.getParentguid());
					row.setString(3, obj.getDisplay());
					row.setString(4, obj.getKind());
					row.setString(5, obj.getNote());
				}
			}
		}
		//此异常无需继续抛出
		catch(Exception ex){
			CpLogger.error(ex.getMessage(), ex);
		}
	}
	
	
	/**
	 * 点击查询方案时执行
	 * @param e
	 */
	public void onQueryPlanClick(LinkEvent e){ 
		LinkComp link = e.getSource();
		ExtAttribute attr = link.getExtendAttribute("queryResult");
		ExtAttribute mapAttr = link.getExtendAttribute("queryFieldMap");
		ExtAttribute queryId = link.getExtendAttribute("queryId");
		ExtAttribute queryName = link.getExtendAttribute("queryName");
		String whereSqlString = null;
		String queryInfo = null;
		if(queryId!=null){
			queryInfo = queryId.getValue().toString();
			if(queryName!=null){
				queryInfo += ";:;"+queryName.getValue().toString();
			}else{
				queryInfo += ";:;default";
			}
		}
		if(attr != null){
			whereSqlString = (String) attr.getValue();
		}
		if(StringUtils.isEmpty(whereSqlString)){
			whereSqlString = " 1=1 ";
		}
		AppLifeCycleContext ctx = AppLifeCycleContext.current();
		ViewContext viewCtx = ctx.getWindowContext().getCurrentViewContext();
		FromWhereSQLImpl whereSql = new FromWhereSQLImpl(null, whereSqlString);//new QueryTool().getFromWhere(null, null);
		Map<String,String> fieldAndSqlMap = new HashMap<String, String>();
		if(mapAttr!=null){
			String[] maps = mapAttr.getValue().toString().split(",");
			if(maps!=null){
				for(int index = 0;index<maps.length;index++){
					String map = maps[index];
					map = map.replaceAll("\\{", "");
					map = map.replaceAll("\\}", "");
					String[] key_values = map.split("=");
					if(key_values.length==2){
						String key = key_values[0];
						String value = key_values[1];
						fieldAndSqlMap.put(key, value);
					}
				}
			}
		}
		whereSql.setFieldAndSqlMap(fieldAndSqlMap);
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		paramMap.put("whereSql", whereSql);
		paramMap.put("queryInfo", queryInfo);
		
		CmdInvoker.invoke(new UifPlugoutCmd(viewCtx.getId(), "qryout", paramMap));
		//CmdInvoker.invoke(new UifPlugoutCmd(viewCtx.getId(), "queryPlanPlugout", paramMap));
	}
	
	/**
	 * 点更多或收起时执行
	 * @param e
	 */
	public void onMoreQueryPlanClick(MouseEvent<ImageComp> e){ 
		ImageComp image = e.getSource();
		UIMeta uimeta = AppLifeCycleContext.current().getViewContext().getUIMeta();
		UIFlowvLayout flowvLayout = (UIFlowvLayout) uimeta.findChildById("queryPlanflowvLayout");
		List<UILayoutPanel> panelList = flowvLayout.getPanelList();
		LinkComp link = (LinkComp)image.getView().getViewComponents().getComponent("expandLink");
		UILayoutPanel panel5 = panelList.get(10);
		if(!panel5.getVisible()){
			panel5.setVisible(true);
			image.setAlt(LfwResBundle.getInstance().getStrByID("imp", "SimpleQueryController-000000")/*收起*/);
			image.setImage1Changed(true);
			image.setImage1("frame/device_pc/themes/${theme}/ui/ctrl/qry/images/dropup.png");
			if(link!=null){
				link.setText(LfwResBundle.getInstance().getStrByID("imp", "SimpleQueryController-000000")/*收起*/);
			}
			for (int i = 11; i < panelList.size() -2; i++) {
				UILayoutPanel panel = panelList.get(i);
				if(!panel.getVisible()){
					panel.setVisible(true);
				}
			}
		}
		else{
			panel5.setVisible(false);
			image.setAlt(LfwResBundle.getInstance().getStrByID("imp", "SimpleQueryController-000001")/*更多*/);
			image.setImage1Changed(true);
			image.setImage1("frame/device_pc/themes/${theme}/ui/ctrl/qry/images/dropdown.png");
			if(link!=null){
				link.setText(LfwResBundle.getInstance().getStrByID("imp", "SimpleQueryController-000001")/*更多*/);
			}
			for (int i = 11; i < panelList.size() -2; i++) {
				UILayoutPanel panel = panelList.get(i);
				if(panel.getVisible()){
					panel.setVisible(false);
				}
			}
		}
	}
	
	public void onMoreQueryPlanLinkClick(LinkEvent e){ 
		LinkComp link = e.getSource();
		UIMeta uimeta = AppLifeCycleContext.current().getViewContext().getUIMeta();
		UIFlowvLayout flowvLayout = (UIFlowvLayout) uimeta.findChildById("queryPlanflowvLayout");
		List<UILayoutPanel> panelList = flowvLayout.getPanelList();
		ImageComp image = (ImageComp)link.getView().getViewComponents().getComponent("expandImg");
		UILayoutPanel panel5 = panelList.get(10);
		if(!panel5.getVisible()){
			panel5.setVisible(true);
			image.setAlt(LfwResBundle.getInstance().getStrByID("imp", "SimpleQueryController-000000")/*收起*/);
			image.setImage1Changed(true);
			image.setImage1("frame/device_pc/themes/${theme}/ui/ctrl/qry/images/dropup.png");
			link.setText(LfwResBundle.getInstance().getStrByID("imp", "SimpleQueryController-000000")/*收起*/);
			for (int i = 11; i < panelList.size() -2; i++) {
				UILayoutPanel panel = panelList.get(i);
				if(!panel.getVisible()){
					panel.setVisible(true);
				}
			}
		}
		else{
			panel5.setVisible(false);
			image.setAlt(LfwResBundle.getInstance().getStrByID("imp", "SimpleQueryController-000001")/*更多*/);
			image.setImage1Changed(true);
			image.setImage1("frame/device_pc/themes/${theme}/ui/ctrl/qry/images/dropdown.png");
			link.setText(LfwResBundle.getInstance().getStrByID("imp", "SimpleQueryController-000001")/*更多*/);
			for (int i = 11; i < panelList.size() -2; i++) {
				UILayoutPanel panel = panelList.get(i);
				if(panel.getVisible()){
					panel.setVisible(false);
				}
			}
		}
	}
	
	
	
	private String getLoadSQL() {
//		TemplateInfo templateInfo = null;//
		StringBuffer loadSelectSQL= new StringBuffer("select kind,guid,id,display,parentguid, ");
		//loadSelectSQL.append(StringUtil.getUnionStr(MyFavoritesNode.PRIVATE_FLDS, ",",""));
		loadSelectSQL.append(" from pub_myfavorite where 1=1");
//		//if(m_selfLoadSQL==null || m_selfLoadSQL.trim().length()==0) {
//			//<li>1.如果Templateid不为空,功能节点号就不能起作用,因为可能其就是跨功能节点的模板
//			//<li>2.当是通过自定义设置的条件，Templateid是可能为空的!此时起作用的是node_code
//			//<li>3.如果整个Tempinfo为空，则查不出任何东西			
//			
//		if(templateInfo == null) {
//			loadSelectSQL.append(" 1=0 ");
//		}
//		else {
//			String tmpSQLSegment ="";
//			tmpSQLSegment = templateInfo.getTemplateId()==null ? " templateid is null " : "templateid = '" + templateInfo.getTemplateId() + "'";			
//			loadSelectSQL.append(tmpSQLSegment);
//			
//			if(templateInfo.getTemplateId()==null) {//see <li>2
//				tmpSQLSegment = templateInfo.getFunNode()==null ? " node_code is null ":"node_code = '" + templateInfo.getFunNode() + "'";
//				loadSelectSQL.append(" and "+ tmpSQLSegment);			
//			}
//			
//			//用户ID为空的收藏夹，为公司共享；一并查出来				
//			tmpSQLSegment = templateInfo.getUserid()==null ? " cuserid is null ":"( cuserid is null or cuserid = '" + templateInfo.getUserid() + "')";
//			loadSelectSQL.append(" and "+ tmpSQLSegment);
//			
//			
//			//组织PK为空的收藏夹，为跨公司共享，一并查出来
//			tmpSQLSegment = templateInfo.getPk_Org()==null ? " pk_org is null ":"( pk_org is null or pk_org = '" + templateInfo.getPk_Org() + "')";
//			loadSelectSQL.append(" and "+ tmpSQLSegment);	
//			
//			//如果orgtype==null，则不考虑之.....现在默认把orgtype为空的也作为默认的查出来
//			if(templateInfo.getOrgType()!=null){
//				tmpSQLSegment = "(orgtypecode is null or orgtypecode = '" + templateInfo.getOrgType()+"')";
//				loadSelectSQL.append(" and "+ tmpSQLSegment);
//			}
////				loadSelectSQL.append(" order by ts desc");
//		}			
//		}
//		else 
//			loadSelectSQL.append(m_selfLoadSQL);
		
		return loadSelectSQL.toString();
	}
}
