package nc.scap.pub.record;

import nc.scap.pub.util.CpWinUtil;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.portal.log.ScapLogger;

public class RecordUtil {
	
//	private static final String VIEW_ID = "pubview_record";
//	private static final String DS_ID = "record_ds";
//	private static final String Field_ID = "record";
	private static final String VIEW_ID = "main";
	private static final String DS_ID = "main_ds";
	private static final String Field_ID = "plan";
	
	/*导入久其数据的面板输出控制方法*/
	public static void standardRecord(String record) {
		writeRecord(record);
		ScapLogger.console(record.replace("\n", ""));
	}
	
	public static void writeRecord(String record) {
		try {
			Dataset ds = getDs();
			Row row = ds.getSelectedRow();
			if(row == null) {
				row = ds.getEmptyRow();
				ds.setRowSelectIndex(ds.getRowIndex(row));
			}
			String context = "";
			if(row.getValue(ds.nameToIndex(Field_ID)) != null) {
				context = row.getValue(ds.nameToIndex(Field_ID)).toString();
			}
			context += record;
			row.setValue(ds.nameToIndex(Field_ID), context);
			
		} catch (Exception e) {
		}
	}
	
	public static LfwView getView() {
		return CpWinUtil.getView(VIEW_ID);
	}
	
	public static Dataset getDs() {
		return CpWinUtil.getDataset(getView(), DS_ID);
	}

}
