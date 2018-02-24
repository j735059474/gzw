package nc.scap.pub.notice_manager;
import java.io.Serializable;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifPlugoutCmd;
import nc.uap.lfw.core.event.PageEvent;

public class Notice_managerListWinCtrl implements Serializable {
  private static final long serialVersionUID = -1;
  
   public void afterPageInit(PageEvent pageEvent){
	  CmdInvoker.invoke(new UifPlugoutCmd("queryPlugin"));
  }
}