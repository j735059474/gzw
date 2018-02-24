package nc.scap.pub.attlist.cmd;

import nc.scap.pub.attlist.AttachRoleUtil;
import nc.uap.lfw.core.cmd.base.ICommand;

public class ReloadRolesCmd implements ICommand {

	private String pk_billitem;

	public ReloadRolesCmd(String pk_billitem) {
		this.pk_billitem = pk_billitem;
	}
	
	@Override
	public void execute() {
		AttachRoleUtil.setPkBillItem(pk_billitem);
		AttachRoleUtil.refreshWidget(true);
	}

}
