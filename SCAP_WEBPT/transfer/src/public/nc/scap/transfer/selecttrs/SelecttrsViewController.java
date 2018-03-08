package nc.scap.transfer.selecttrs;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.serializer.impl.Dataset2SuperVOSerializer;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.scap.library.vos.LibraryVO;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import nc.scap.pub.util.UifExcelImportCmd;
import uap.web.bd.pub.AppUtil;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.data.Row;
import nc.scap.pub.util.CpWinUtil;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.event.MouseEvent;
public class SelecttrsViewController {
  private static final long serialVersionUID=1L;
  private static final long ID=5L;
  public void onCancle(  MouseEvent mouseEvent){
    CpWinUtil.getWinCtx().closeView("selecttrs");
  }
  public void onOk(  MouseEvent mouseEvent){
	  onCancle(mouseEvent);
    Dataset ds = CpWinUtil.getDataset("selecttrs_ds");
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("请选中机构库");
		}
		Row row = ds.getSelectedRow();
		LibraryVO[] tmpvo = new Dataset2SuperVOSerializer<LibraryVO>()
				.serialize(ds, row);
		AppUtil.addAppAttr("selecttrsinfo", tmpvo[0]);// 机库库信息
		UifExcelImportCmd cmd = new UifExcelImportCmd(
				"nc.scap.transfer.upload.CallBackLoad",
				"dataimpComps.dataimp_listwin");
		CmdInvoker.invoke(cmd);
  }
  public void beforeShow(  DialogEvent dialogEvent){
    
  }
  public void onDataLoad(  DataLoadEvent dataLoadEvent){
    Dataset ds = dataLoadEvent.getSource();
	  	CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
  }
}
