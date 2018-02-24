package nc.uap.lfw.core.event;

import java.util.Map;

import uap.lfw.core.env.EnvProvider;

import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.ViewContext;
import nc.uap.lfw.core.ctx.WindowContext;
import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.page.PluginDesc;
import nc.uap.lfw.core.page.PluginHelper;
import nc.uap.lfw.core.page.PlugoutDesc;
import nc.uap.lfw.core.page.PlugoutHelper;

/**
 * 
 * 处理Connector为7的connector
 *
 */
public class PlugHandler7 extends AbstractPlugHandler {

	@Override
	public PlugHandleInfo getHandlerInfo(Connector conn, Map<String, Object> paramMap) {
		AppLifeCycleContext ctx = AppLifeCycleContext.current();
		ViewContext viewCtx = ctx.getWindowContext().getViewContext(conn.getSource());
		PlugHandleInfo info = new PlugHandleInfo();
		LfwView hostView = EnvProvider.getInstance().getHostView();
		if(hostView != null){
			Connector[] viewConns = hostView.getConnectors();
			for (Connector viewConn : viewConns) {
				String inlineWindow = AppLifeCycleContext.current().getWindowContext().getId();
				if (viewConn.getConnType().equals(Connector.INLINEWINDOW_VIEW) && viewConn.getSource().equals(inlineWindow) && viewConn.getPlugoutId().equals(conn.getPluginId())){
					PluginDesc plugin = hostView.getPluginDesc(viewConn.getPluginId());
					PlugoutDesc plugout = PlugoutHelper.getTruePlugout(ctx.getWindowContext().getWindow(), conn.getPluginId());
					LfwWindow targetWindow = hostView.getWindow();
					LfwView targetView = hostView;
					info.plugin = plugin;
					info.connMap = getConnMap(plugout, plugin);
					info.targetWindow = targetWindow;
					info.targetView = targetView;
					info.resultMap = buildPlugContent(plugout, viewCtx, paramMap);
					break;
				}
			}
		}
		else{
			Connector[] appConnectors = ctx.getApplicationContext().getApplication().getConnectors();
			PlugoutDesc plugout = PlugoutHelper.getTruePlugout(ctx.getWindowContext().getWindow(), conn.getPluginId());
			PluginDesc plugin = null;
			LfwWindow targetWindow = null;
			LfwView targetView = null;
			for (Connector appConn : appConnectors) {
				if(appConn.getPlugoutId().equals(conn.getPluginId())){
					WindowContext targentWinCtx = ctx.getApplicationContext().getWindowContext(appConn.getTarget());
					if(targentWinCtx == null)
						continue;
					targetWindow = ctx.getApplicationContext().getWindowContext(appConn.getTarget()).getWindow();
					plugin = PluginHelper.getTruePlugin(targetWindow, appConn.getPluginId());
					targetView = PluginHelper.getPlugTargetView(targetWindow, appConn.getPluginId());
					break;
				}
				
			}
			
			info.targetWindow = targetWindow;
			info.targetView = targetView;
			info.plugin = plugin;
			if (plugout != null && plugin != null)
				info.connMap = getConnMap(plugout, plugin);
			if (plugout != null)
				info.resultMap = buildPlugContent(plugout, viewCtx, paramMap);
			
//			Connector[] windowConns =  ctx.getWindowContext().getWindow().getConnectors();
//				for (Connector windowConn : windowConns) {
//					if (Connector.WINDOW_PROXY_OUT.equals(windowConn.getConnType())){
//						PlugoutDesc plugout = PlugoutHelper.getTruePlugout(ctx.getWindowContext().getWindow(), conn.getPluginId());
//						PluginDesc plugin = null;
//						LfwWindow targetWindow = null;
//						LfwView targetView = null;
//						Connector[] appConnectors = ctx.getApplicationContext().getApplication().getConnectors();
//						for (Connector appConn : appConnectors) {
//							if(appConn.getPlugoutId().equals(windowConn.getPluginId())){
//								WindowContext targentWinCtx = ctx.getApplicationContext().getWindowContext(appConn.getTarget());
//								if(targentWinCtx == null)
//									break;
//								targetWindow = ctx.getApplicationContext().getWindowContext(appConn.getTarget()).getWindow();
//								plugin = PluginHelper.getTruePlugin(targetWindow, appConn.getPluginId());
//								targetView = PluginHelper.getPlugTargetView(targetWindow, appConn.getPluginId());
//							}
//							if(targetView != null)
//								break;
//						}
//						if(plugin == null)
//							break;
//						info.plugin = plugin;
//						info.connMap = getConnMap(plugout, plugin);
//						info.targetWindow = targetWindow;
//						info.targetView = targetView;
//						info.resultMap = buildPlugContent(plugout, viewCtx, paramMap);
//						break;
//				}
//			}
//			return info;
		}
		return info;
	}
}
