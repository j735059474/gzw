package nc.scap.transfer.fileback;

import nc.bs.dao.DAOException;
import nc.scap.pt.vos.ScapptApplyAttachVO;
import nc.scap.pt.vos.ScapptAttachVO;
import nc.scap.ptpub.method.ScapPtMethod;
import nc.scap.pub.util.ScapDAO;
import nc.uap.ctrl.filrmgr.IOccupyOperate;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.serializer.impl.SuperVO2DatasetSerializer;
import nc.uap.lfw.file.LfwFileConstants;
import uap.web.bd.pub.AppUtil;

public class TraFileBackImp implements IOccupyOperate {

	@Override
	public void handleWfmInfo() {
		
		Dataset ds = AppLifeCycleContext.current().getApplicationContext()
				.getWindowContext(ScapPtMethod.getWinByType())
				.getViewContext("main").getView().getViewModels()
				.getDataset("scappt_attach");
		String billitem = (String) LfwRuntimeEnvironment.getWebContext()
				.getWebSession()
				.getOriginalParameter(LfwFileConstants.BILLITEM);
		ScapptAttachVO[] attvos = (ScapptAttachVO[]) AppUtil
				.getAppAttr("attvos");
		for (ScapptAttachVO tmp : attvos) {
			if (billitem.equals(tmp.getPk_attach())) {
				tmp.setSubmitstate("已上传");
				try {
					ScapDAO.getBaseDAO().updateVO(tmp);
				} catch (DAOException e) {
					throw new LfwRuntimeException("更新附件状态出错!");
				}
			}
		}
		ds.clear();
		new SuperVO2DatasetSerializer().serialize(attvos, ds);
		AppUtil.addAppAttr("attvos", null);
	}

}
