package nc.scap.transfer.trs.wfm;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.scap.pt.vos.ScapptTransferHVO;
import nc.scap.ptpub.PtConstants;
import nc.scap.ptpub.method.ScapPtMethod;
import nc.scap.pub.util.ScapDAO;
import nc.uap.cpb.baseservice.IUifCpbService;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.constants.AppConsts;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.wfm.context.WfmFlowInfoCtx;
import nc.uap.wfm.dftimpl.DefaultFormOper;
import nc.uap.wfm.engine.TaskProcessUI;
import nc.uap.wfm.model.Task;
import nc.uap.wfm.utils.WfmCPUtilFacade;
import nc.uap.wfm.vo.WfmFlwTypeVO;
import nc.uap.wfm.vo.WfmFormInfoCtx;
import nc.uap.wfm.vo.WfmTaskVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;

public class WfmFlwFormOper extends DefaultFormOper {

	public WfmFormInfoCtx save(WfmFormInfoCtx frmInfoCtx,
			WfmFlowInfoCtx flwInfoCtx) {
		// 当单据PK不存在的时候发挥作用
		if (frmInfoCtx == null) {
			return null;
		}
		beforeSave(frmInfoCtx, flwInfoCtx);// 保存之前增加处理逻辑
		try {
			IUifCpbService cpbService = NCLocator.getInstance().lookup(
					IUifCpbService.class);
			cpbService.insertOrUpdateSuperVO((SuperVO) frmInfoCtx, false);
			((SuperVO) frmInfoCtx).setStatus(VOStatus.UPDATED);
			afterSave(frmInfoCtx, flwInfoCtx);// 保存之后增加处理逻辑
			/*
			 * 此处需开发者添加逻辑， 将子表状态置为UPDATED，多子表需使用for循环将所有子表状态置为UPDATE
			 * 代码参考上面主表状态置UPDATE的写法
			 */

		} catch (BusinessException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage());
		}

		return frmInfoCtx;
	}

	protected void afterSave(WfmFormInfoCtx frmInfoCtx,
			WfmFlowInfoCtx flwInfoCtx) {
		// TODO 自动生成的方法存根

	}

	protected void beforeSave(WfmFormInfoCtx frmInfoCtx,
			WfmFlowInfoCtx flwInfoCtx) {
		// TODO 自动生成的方法存根

	}

	public WfmFormInfoCtx update(WfmFormInfoCtx frmInfoCtx,
			WfmFlowInfoCtx flwInfoCtx) {
		if (frmInfoCtx == null) {
			return null;
		}
		beforeUpdate(frmInfoCtx, flwInfoCtx);// 更新之前业务处理
		try {
			IUifCpbService cpbService = NCLocator.getInstance().lookup(
					IUifCpbService.class);
			cpbService.updateSuperVO((SuperVO) frmInfoCtx, false);
			afterUpdate(frmInfoCtx, flwInfoCtx);// 更新之后业务处理
		} catch (BusinessException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage());
		}
		return frmInfoCtx;
	}

	protected void afterUpdate(WfmFormInfoCtx frmInfoCtx,
			WfmFlowInfoCtx flwInfoCtx) {
		// TODO 自动生成的方法存根

	}

	protected void beforeUpdate(WfmFormInfoCtx frmInfoCtx,
			WfmFlowInfoCtx flwInfoCtx) {
		// TODO 自动生成的方法存根

	}

	public Class<WfmFormInfoCtx>[] getBizMetaDataDesc(WfmFlwTypeVO flowTypeVo) {
		return new Class[] { nc.scap.pt.vos.ScapptTransferHVO.class };
	}

	@Override
	public WfmFormInfoCtx getWfmFormInfoCtx(String pk_frmins, String pk_flwtype) {
		return null;
	}

	@Override
	public TaskProcessUI getHanlderUrlByTask(Task task) {
		if (task == null)
			return null;
		ScapptTransferHVO vo = null;
		try {
			vo = (ScapptTransferHVO) ScapDAO.getBaseDAO().retrieveByPK(
					ScapptTransferHVO.class, task.getPk_frmins());
		} catch (DAOException e) {
			throw new LfwRuntimeException("根据任务主键查询对应VO异常!");
		}
		if (vo == null) {
			try {
				ScapDAO.getBaseDAO().deleteByPK(WfmTaskVO.class,
						task.getPk_task());
			} catch (DAOException e) {
				throw new LfwRuntimeException("任务删除异常!");
			}
			throw new LfwRuntimeException("单据已被删除！").setTitle("提示");
		}
		boolean flag = false;
		try {
			flag = WfmCPUtilFacade.isCanDelBill(vo.getPk_transfer_h());
		} catch (Exception e) {
			flag = false;
		}
		String operation = "approve";
		if (flag) {
			operation = AppConsts.OPE_EDIT;
		}

		String url = "/app/transfer_trs/" + ScapPtMethod.getWinId(vo);
		if (url == null || url.length() == 0) {
			throw new LfwRuntimeException("无法找到对应的URL");
		}
		url = url + "&" + WfmConstants.WfmUrlConst_TaskPk + "="
				+ task.getPk_task() + "&pk_transfer_h=" + task.getPk_frmins()
				+ "&" + AppConsts.OPE_SIGN + "=" + operation
				+ "&model=nc.scap.transfer.model.TransferCardPageModel";
		TaskProcessUI processUI = new TaskProcessUI();
		processUI.setUrl(LfwRuntimeEnvironment.getRootPath() + "/" + url);
		String title = task.getTitile();
		title = getTitle();
		if ("".equals(title) || title == null) {
			title = "审批";
		}
		processUI.setTitle(title);
		return processUI;
	}

	protected String getTitle() {
		// TODO 自动生成的方法存根
		return null;
	}

}