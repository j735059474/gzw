package nc.uap.ctrl.filemgr;
import java.util.List;

import nc.uap.cpb.log.CpLogger;
import nc.uap.ctrl.file.DefaultFileViewUtil;
import nc.uap.ctrl.file.FileViewVO;
import nc.uap.ctrl.file.IFileViewUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.ctrl.IController;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.serializer.impl.SuperVO2DatasetSerializer;
import nc.uap.lfw.file.LfwFileConstants;
/** 
 * @author chouhl
 */
public class FileViewController implements IController {
  private static final long serialVersionUID=1L;
  public void onDataLoad_fileds(  DataLoadEvent dataLoadEvent){
    Dataset ds = dataLoadEvent.getSource();
    String sysid = (String)LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParameter(LfwFileConstants.SYSID);
	if(sysid==null || sysid.equals(""))
		sysid = "bafile";
    String billitem = (String)LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParameter(LfwFileConstants.BILLITEM);
	String billtype = (String)LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParameter(LfwFileConstants.BILLTYPE);
	ds.clear();
	IFileViewUtil util =  getFileViewUtil();
	try {
		List<FileViewVO> files =  util.loadFiles(billitem, billtype, sysid);
		if(files != null && files.size() > 0)
			new SuperVO2DatasetSerializer().serialize(files.toArray(new FileViewVO[0]), ds);
	} catch (LfwBusinessException e) {
		CpLogger.error(e);
	}
	
  }
  public void beforeShow(  DialogEvent dialogEvent){
//	  SelfDefComp comp = (SelfDefComp)AppLifeCycleContext.current().getViewContext().getView().getViewComponents().getComponent("filelist");
  }
  private IFileViewUtil getFileViewUtil(){
	  return new DefaultFileViewUtil();
  }
}
