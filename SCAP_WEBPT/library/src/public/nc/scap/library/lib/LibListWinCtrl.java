package nc.scap.library.lib;
import java.io.Serializable;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifPlugoutCmd;
import nc.uap.lfw.core.event.PageEvent;

public class LibListWinCtrl implements Serializable {
  private static final long serialVersionUID = -1;
  
   public void afterPageInit(PageEvent pageEvent){
	  CmdInvoker.invoke(new UifPlugoutCmd("queryPlugin"));
  }
}