package nc.scap.pub.record;
import nc.scap.pub.util.CpWinUtil;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.page.LfwView;
public class RecordWinMainVCtrl {
  private static final long serialVersionUID=1L;
  private static final long ID=5L;
  public void beforeShow(  DialogEvent dialogEvent){
	  LfwView view = CpWinUtil.getView("pubview_record");
	  Dataset ds = CpWinUtil.getDataset(view, "record_ds");
	  Row row = ds.getEmptyRow();
	  ds.addRow(row);
	  ds.setRowSelectIndex(ds.getRowIndex(row));
  }
}
