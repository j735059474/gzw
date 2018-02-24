package nc.scap.pub.attlist.comp;

import nc.uap.ctrl.filrmgr.IOccupyOperate;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifPlugoutCmd;

public class AttachAfterChangeController implements IOccupyOperate {

	@Override
	public void handleWfmInfo() {
		
		// 刷新AttachRoleComp附件数量
		CmdInvoker.invoke(new UifPlugoutCmd("main", "afterOperate_plugout"));
	}

}
