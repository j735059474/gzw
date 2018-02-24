package nc.uap.lfw.core.page;

public final class PlugoutHelper {
	/**
	 * 获取代理的真正的plugout
	 * @param id
	 * @return
	 */
	public static PlugoutDesc getTruePlugout(LfwWindow win, String id){
		PlugoutProxy plug = (PlugoutProxy) win.getPlugoutDesc(id);
		if (plug == null) 
			return null;
		String plugoutId = plug.getId();
		for (Connector conn : win.getConnectors()){
			if (plugoutId.equals(conn.getPluginId())){
				String viewId = conn.getSource();
				String truePlugoutId = conn.getPlugoutId();
				if(win.getView(viewId)==null){
					continue;
				}
				if(win.getView(viewId).getPlugoutDesc(truePlugoutId)==null){
					continue;
				}
				PlugoutDesc plugout = win.getView(viewId).getPlugoutDesc(truePlugoutId);
				return plugout;
			}
			//return null;
		}
		return null;
	}
	
	public static LfwView getPlugSourceView(LfwWindow win, String id){
		PlugoutProxy plug = (PlugoutProxy) win.getPlugoutDesc(id);
		if (plug == null) return null;
		String plugoutId = plug.getId();
		for (Connector conn : win.getConnectors()){
			if (plugoutId.equals(conn.getPluginId())){
				String viewId = conn.getSource();
				LfwView view = win.getView(viewId);
				return view;
			}
		}
		return null;
	}
	
	public static Connector getConnByPlugoutId(LfwWindow win, String plugoutId){
		for (Connector conn : win.getConnectors()){
			if (plugoutId.equals(conn.getPlugoutId()))
				return conn;
		}
		return null;
	}

	public static void setConnector(LfwWindow window, String delegatedView, String plugoutId, String id) {
		Connector conn = getConnByPlugoutId(window, plugoutId);
		if(conn == null){
			conn = new Connector();
			window.addConnector(conn);
		}
		conn.setId("proxyout_" + id + "_" + plugoutId);
		conn.setConnType(Connector.WINDOW_PROXY_OUT);
		conn.setSource(delegatedView);
		conn.setTarget(window.getFullId());
		conn.setPluginId(id);
		conn.setPlugoutId(plugoutId);
	}
}
