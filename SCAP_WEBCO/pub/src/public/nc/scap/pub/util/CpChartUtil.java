package nc.scap.pub.util;

import java.util.Map;

import nc.jdbc.framework.processor.MapProcessor;
import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.util.CpWinUtil;
import nc.scap.pub.util.ScapDAO;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.exception.LfwRuntimeException;

public class CpChartUtil implements IGlobalConstants {
	
	public final static String CON_TYPE_LAYOUT = "-1";//布局
	public final static String CON_TYPE_TAB = "0";//页签
	public final static String CON_TYPE_ROW = "1";//行
	public final static String CON_TYPE_COL = "2";//列
	public final static String CON_TYPE_CHART = "3";//图元
	public final static String CON_TYPE_CARD = "4";//卡片
	public final static String CON_TYPE_POP = "5";//弹出
	
	public final static String CON_DISPLAY_TYPE_CHART = "0";/*图形*/
	public final static String CON_DISPLAY_TYPE_FINEREPORT = "1";
	public final static String CON_DISPLAY_TYPE_FREEMARKER = "2";/*freemarker*/
	
	
	public static String getChildContainerType(String pkContainer) {	
		String sql = "select distinct(container_type) type from scap_layout_container where parent_container = '" + pkContainer + "';";
		Map<String, Object> map = (Map<String, Object>) ScapDAO.executeQuery(sql, new MapProcessor());
		if(map != null && map.get("type") != null) {
			return (String) map.get("type");
		}else {
			return null;
		}
	}
	
	public static String getParentContainer() {
		  Dataset container = CpWinUtil.getDataset(CpWinUtil.getView("main"), "scap_layout_container");
		  if(container.getSelectedRow() != null) {
			  String pkValue = container.getSelectedRow().getValue(container.nameToIndex("pk_layout_container")).toString();
			  String type = container.getSelectedRow().getValue(container.nameToIndex("container_type")).toString();
			  if(type.equals(CON_TYPE_CHART)) {
				  throw new LfwRuntimeException("图元容器中不允许装载容器！");
			  }else if(type.equals(CON_TYPE_POP)) {
				  return pkValue;
			  }
			  String childType = getChildContainerType(pkValue);
			  if(childType != null && childType.equals(CON_TYPE_CHART)) {
				  throw new LfwRuntimeException("任何容器中最多只能装载一个图元容器！");
			  }
			  return pkValue;
		  }else {
			  Dataset layout = CpWinUtil.getDataset(CpWinUtil.getView("main"), "scap_layout");
			  if(layout.getSelectedRow() != null) {
				  String pkValue = layout.getSelectedRow().getValue(layout.nameToIndex("pk_layout")).toString();
				  return pkValue;
			  }else {
				  throw new LfwRuntimeException("请先选中一个主题或容器！");
			  }
		  }
	}
}
