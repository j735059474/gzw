package nc.scap.transfer.merge;

import nc.bs.dao.DAOException;
import nc.scap.pt.vos.ScapptTransferHVO;
import nc.scap.pub.util.CpWinUtil;
import nc.scap.pub.util.ScapDAO;
import nc.uap.lfw.core.AppInteractionUtil;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.serializer.impl.Dataset2SuperVOSerializer;
import nc.vo.pub.lang.UFBoolean;
import uap.web.bd.pub.AppUtil;

public class MergeViewController {
	private static final long serialVersionUID = 1L;
	private static final long ID = 5L;

	public void onOk(MouseEvent mouseEvent) {
		Dataset ds = CpWinUtil.getDataset("merge_ds");
		Row row = ds.getSelectedRow();
		if (row == null) {
			AppInteractionUtil.showShortMessage("无选中数据!");
			return;
		}
		ScapptTransferHVO vo = (ScapptTransferHVO) AppUtil
				.getAppAttr("mergeinfo");// 列表选中行信息
		if (vo == null)
			return;
		ScapptTransferHVO[] tmpvo = new Dataset2SuperVOSerializer<ScapptTransferHVO>()
				.serialize(ds, row);// 目标行信息
		if (tmpvo == null || tmpvo.length <= 0)
			return;
		copyData(vo, tmpvo[0]);// 信息合并
		try {
			ScapDAO.getBaseDAO().updateVO(tmpvo[0]);
			ScapDAO.getBaseDAO().deleteVO(vo);
			onCancle(mouseEvent);
			AppInteractionUtil.showShortMessage("信息合并成功!");
		} catch (DAOException e) {
			throw new LfwRuntimeException("信息合并数据异常!");
		}
	}

	/**
	 * 信息合并
	 * 
	 * @param importvo
	 * @param preductvo
	 */
	public void copyData(ScapptTransferHVO importvo, ScapptTransferHVO preductvo) {
		preductvo.setVrequestno(importvo.getVrequestno());// 项目编号?
		preductvo.setCassetstype(importvo.getCassetstype());// 国资监管类型
		preductvo.setVcsasstype(importvo.getVcsasstype());// 受让方经济类型　
		preductvo.setVcasstype(importvo.getVcasstype());// 转让方经济类型
		preductvo.setVrname(importvo.getVrname());
		preductvo.setPk_borg(importvo.getPk_borg());
		preductvo.setPk_passess(importvo.getPk_passess());
		preductvo.setVporgname(importvo.getVporgname());
		preductvo.setDbpercent(importvo.getDbpercent());
		preductvo.setDpcarrying(importvo.getDpcarrying());
		preductvo.setVwassignname(importvo.getVwassignname());
		preductvo.setDwprice(importvo.getDwprice());
		preductvo.setDwtransprice(importvo.getDwtransprice());
		preductvo.setDjprice(importvo.getDjprice());
		preductvo.setDbassets(importvo.getDbassets());
		preductvo.setCwctdate(importvo.getCwctdate());
		preductvo.setCwforensicdate(importvo.getCwforensicdate());
		preductvo.setIndustry(importvo.getIndustry());
		preductvo.setTramethod(importvo.getTramethod());
		preductvo.setTraarea(importvo.getTraarea());
		preductvo.setTraname(importvo.getTraname());
		preductvo.setIsmerge("N");// 是否合并
		preductvo.setIsleadin(new UFBoolean(true));// 是否导入数据
//		preductvo.setJzcpgz(importvo.getJzcpgz());// 资产评估
	}

	public void onCancle(MouseEvent mouseEvent) {
		CpWinUtil.getWinCtx().closeView("merge");
	}

	public void beforeShow(DialogEvent dialogEvent) {

	}

	public void onDataLoad(DataLoadEvent dataLoadEvent) {
		Dataset ds = dataLoadEvent.getSource();
		ds.setLastCondition("isleadin='N' and dr=0 and ismerge='N'");
		CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
	}
}
