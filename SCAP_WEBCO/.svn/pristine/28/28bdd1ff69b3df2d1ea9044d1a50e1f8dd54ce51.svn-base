package nc.uap.lfw.core.page;

public final class PluginHelper {
	
	/**
	 * 获取代理的真正的plugin
	 * @param id
	 * @return
	 */
	public static PluginDesc getTruePlugin(LfwWindow win, String id){
		PluginProxy plug = (PluginProxy) win.getPluginDesc(id);
		if (plug == null) 
			return null;
		String pluginId = plug.getId();
		for (Connector conn : win.getConnectors()){
			if (pluginId.equals(conn.getPlugoutId())){
				String viewId = conn.getTarget();
				String truePluginId = conn.getPluginId();
				if(win.getView(viewId)==null){
					continue;
				}
				if(win.getView(viewId).getPluginDesc(truePluginId)==null){
					continue;
				}
				PluginDesc plugin = win.getView(viewId).getPluginDesc(truePluginId);
				return plugin;
			}
		}
		return null;
	}
	
	
	public static Connector getConnByPluginId(LfwWindow win, String pluginId){
		for (Connector conn : win.getConnectors()){
			if (pluginId.equals(conn.getPluginId()))
				return conn;
		}
		return null;
	}
	
	public static LfwView getPlugTargetView(LfwWindow win, String id){
		PluginProxy plug = (PluginProxy) win.getPluginDesc(id);
		if (plug == null) return null;
		String pluginId = plug.getId();
		for (Connector conn : win.getConnectors()){
			if (pluginId.equals(conn.getPlugoutId())){
				String viewId = conn.getTarget();
				if(win.getView(viewId)==null){
					continue;
				}
				LfwView view = win.getView(viewId);
				return view;
			}
		}
		return null;
	}

	/**
	 * 设置Connector，如果存在则直接修改
	 * @param win
	 * @param delegatedView
	 * @param pluginId
	 * @param id
	 */
	public static void setConnector(LfwWindow win, String delegatedView, String pluginId, String id) {
		Connector conn = getConnByPluginId(win, pluginId);
		if(conn == null){
			conn = new Connector();
			win.addConnector(conn);
		}
		conn.setId("proxyin_" + id + "_" + pluginId);
		conn.setConnType(Connector.WINDOW_RPOXY_INT);
		conn.setSource(win.getFullId());
		conn.setTarget(delegatedView);
		conn.setPluginId(pluginId);
		conn.setPlugoutId(id);
	}
}
