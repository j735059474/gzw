package nc.scap.pub.attlist.comp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import uap.web.bd.pub.AppUtil;

import nc.scap.pub.attlist.AttachRoleDao;
import nc.scap.pub.vos.AttachGroupVO;
import nc.scap.pub.vos.AttachRoleVO;
import nc.uap.lfw.core.common.StringDataTypeConst;
import nc.uap.lfw.core.comp.GridColumn;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.IGridColumn;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.core.model.IWidgetContentProvider;
import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.page.PluginDesc;
import nc.uap.lfw.core.page.PluginDescItem;
import nc.uap.lfw.core.page.PluginProxy;
import nc.uap.lfw.core.uimodel.Application;

public class AttachRoleCompProvider implements IWidgetContentProvider {

	private static String[] columnColIds = new String[] {
		AttachRoleVO.PK_ATTACH_ROLE,
		AttachRoleVO.PK_ATTACH_GROUP,
		AttachRoleVO.CODE,
		AttachRoleVO.NAME,
		AttachRoleVO.MAX_COUNT, 
		AttachRoleVO.MIN_COUNT, 
		"current_count" 
	};
	private static String[] columnColNames = new String[] {
		"����", 
		"�ϲ㵥������",
		"����",
		"��������",
		"����ϴ��ļ�����", 
		"�����ϴ��ļ�����",
		"���ϴ��ļ�����" 
	};
	private static String[] hiddenColIds = new String[] { AttachRoleVO.PK_ATTACH_ROLE, AttachRoleVO.CODE, AttachRoleVO.PK_ATTACH_GROUP };

	/**
	 * ����grid�ؼ���
	 */
	public static List<IGridColumn> genGridColumnList() {
		List<IGridColumn> colList = new ArrayList<IGridColumn>();
		List<String> hiddenColList = Arrays.asList(hiddenColIds);

		for (int i = 0; i < columnColIds.length; i++) {
			String id = columnColIds[i];
			// ����������
			if (hiddenColList.contains(id)) {
				continue;
			} else {
				String name = columnColNames[i];
				GridColumn col = new GridColumn();
				col.setId(id);
				col.setField(id);
				col.setText(name);
				col.setDataType(StringDataTypeConst.STRING);
				col.setVisible(true);
				col.setEditable(false);

				if (AttachRoleVO.NAME.equals(id)) { // ���������ơ��п��
					col.setWidth(400);
				}

				colList.add(col);
			}
		}

		return colList;
	}

	/**
	 * �������ݼ�
	 */
	public static Dataset genDataset(String dsId) {
		Dataset ds = new Dataset(dsId);

		for (int i = 0; i < columnColIds.length; i++) {
			String id = columnColIds[i];
			Field field = new Field();
			field.setId(id);
			field.setOriId(id);
			field.setField(id);
			field.setDataType(StringDataTypeConst.STRING);
			ds.getFieldSet().addField(field);
		}

		return ds;
	}
	
	public static void bindWithFileMgr(Application app, LfwWindow pm, LfwView conf) {
		
		// ��������view Plugin
		PluginDesc plugin = new PluginDesc();
		plugin.setId("afterOperate_plugin");
		plugin.setMethodName("doRefresh");
		
		// View���Connection
		Connector conn = new Connector();
		conn.setConnType(Connector.INLINEWINDOW_VIEW);
		conn.setId("attachrole_mainview_connector");
		conn.setPluginId(plugin.getId());
		conn.setPlugoutId("proxyAfterOperate_plugout");
		conn.setSource("filemgr");

		// ��������window ProxyPlugin
		PluginProxy pluginProxy = new PluginProxy();
		pluginProxy.setId("proxyAfterOperate_plugin");
		pluginProxy.setDelegatedViewId(conf.getId());
		
		// ��������window Connector
		Connector pluginConnector = new Connector();
		pluginConnector.setId("afterOperate_connector_in");
		pluginConnector.setPluginId(plugin.getId());
		pluginConnector.setPlugoutId(pluginProxy.getId());
		pluginConnector.setSource(pm.getFullId());
		pluginConnector.setTarget(conf.getId());
		pluginConnector.setConnType(Connector.WINDOW_RPOXY_INT);
		
		// ���²���ǰ����Ҫ���޸�LFWĬ�ϵ�filemgr window��pagemeta��main view��widget������plugout
		
		// Ӧ��Connector����proxyPlugout��proxyPlugin
		Connector appConnector = new Connector();
		appConnector.setId(pm.getId() + "_filemgr_connector");
		appConnector.setPluginId(pluginProxy.getId());
		appConnector.setPlugoutId("proxyAfterOperate_plugout");
		appConnector.setSource("filemgr");
		appConnector.setTarget(pm.getFullId());
		appConnector.setConnType(Connector.WINDOW_WINDOW);

		if (conf.getPluginDesc(plugin.getId()) == null)
			conf.addPluginDescs(plugin);
		if (conf.getConnector(conn.getId()) == null)
			conf.addConnector(conn);
		if (pm.getPluginDesc(pluginProxy.getId()) == null)
			pm.addPluginDesc(pluginProxy);
		if (pm.getConnectorMap().get(pluginConnector.getId()) == null)
			pm.addConnector(pluginConnector);
		
		boolean b = true;
		for (Connector connector : app.getConnectors()) {
			// �ж��Ƿ��������ظ���Connector������о���������ֹ�ظ����� app.addConnector(appConnector);
			if (appConnector.getId().equals(connector.getId())){
				b = false;
				break;
			}
		}
	}

	@Override
	public LfwView buildWidget(LfwWindow pm, LfwView conf, Map<String, Object> paramMap, String currWidgetId) {

		// add plugin & plugout
		Application app = AppUtil.getCntApplication();
		bindWithFileMgr(app, pm, conf);
		
		// bind events
		conf.setControllerClazz(AttachRoleCompController.class.getName());
		
		EventConf beforeShow = DialogEvent.getBeforeShowEvent();
		beforeShow.setMethodName("beforeShow");
		conf.addEventConf(beforeShow);
		
		String codenode = (String) AppUtil.getAppAttr("nodecode");
		AttachGroupVO[] groups = AttachRoleDao.retrieveAttachGroupByNodeCode(codenode);

		// add components
		for (int i = 0; i < (groups == null ? IAttachRoleCompConstant.MAX_ROLE_GROUP_COUNT : groups.length); i++) {

			// GridComp
			GridComp gridComp = new GridComp();
			gridComp.setId("grid" + i);
			gridComp.setView(conf);
			gridComp.setShowImageBtn(true);
			conf.getViewComponents().addComponent(gridComp);

			// Menubar
			MenubarComp menubar = new MenubarComp();
			menubar.setId("menubar" + i);
			menubar.setView(conf);
			gridComp.setMenuBar(menubar);

			// Menubar item
			MenuItem attachBtn = new MenuItem();
			attachBtn.setId(menubar.getId() + "_attachbtn");
			attachBtn.setView(conf);
			attachBtn.setText("�鿴����");
			attachBtn.setEnabled(false);
			menubar.addMenuItem(attachBtn);

			EventConf onGridClick = MouseEvent.getOnClickEvent();
			onGridClick.setMethodName("onGridClick");
			attachBtn.addEventConf(onGridClick);

			// Dataset
			Dataset ds = genDataset(gridComp.getId() + "_ds");
			gridComp.setDataset(ds.getId());

			EventConf onAfterRowSelect = DatasetEvent.getOnAfterRowSelectEvent();
			onAfterRowSelect.setMethodName("onAfterRoleSelect");
			ds.addEventConf(onAfterRowSelect);

			conf.getViewModels().addDataset(ds);

			// GridColumns
			List<IGridColumn> columnList = genGridColumnList();
			gridComp.setColumnList(columnList);
		}

		return conf;
	}

}
