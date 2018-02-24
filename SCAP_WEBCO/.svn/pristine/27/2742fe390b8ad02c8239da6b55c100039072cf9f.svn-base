package nc.scap.pub.util;

import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.util.CpWinUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.page.LfwView;

public class AttachAddPkUtil extends AbstractMasterSlaveViewController{
	/**
	 *  设置附件 主键 针对以前实现的附件功能
	 * @param ds
	 * @param row
	 */
	public void setBillAttachPK(Dataset ds,Row row){
		String pk_primarykey = generatePk();
		row.setValue(ds.nameToIndex(ds.getPrimaryKeyField()), pk_primarykey);
		LfwView view = CpWinUtil.getView("attachlist");
		view.setExtendAttribute(IGlobalConstants.ATTACH_BILL_ID, pk_primarykey);
	}
	/**
	 *  设置附件 主键 针对以前实现的附件功能
	 * @param ds
	 * @param row
	 */
	public static void setBillAttachPK(Dataset ds, String pkid){
		LfwView view = CpWinUtil.getView("attachlist");
		view.setExtendAttribute(IGlobalConstants.ATTACH_BILL_ID, pkid);
	}
	
	public static void  clearAttchDs(){
		 LfwView view = LfwRuntimeEnvironment.getWebContext().getPageMeta().getView(
					"attachlist");
		    Dataset attchDs = view.getViewModels().getDataset("attach_ds");
			 if (attchDs != null) attchDs.clear();
	}
}
