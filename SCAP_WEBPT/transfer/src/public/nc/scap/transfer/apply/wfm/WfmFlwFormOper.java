package nc.scap.transfer.apply.wfm;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.message.util.IDefaultMsgConst;
import nc.message.vo.NCMessage;
import nc.scap.pt.vos.ScapptApplyAttachVO;
import nc.scap.pt.vos.ScapptApplyBVO;
import nc.scap.pt.vos.ScapptApplyHVO;
import nc.scap.ptpub.method.ScapPtMethod;
import nc.scap.pub.util.CpPubMethod;
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
		try {
			IUifCpbService cpbService = NCLocator.getInstance().lookup(
					IUifCpbService.class);
			cpbService.insertOrUpdateSuperVO((SuperVO) frmInfoCtx, false);
			((SuperVO) frmInfoCtx).setStatus(VOStatus.UPDATED);
			ScapptApplyHVO mainvo = (ScapptApplyHVO) frmInfoCtx;
			ScapptApplyBVO[] bvos = mainvo.getId_scappt_apply_b();
			ScapptApplyAttachVO[] avos = mainvo.getId_scappt_apply_attach();
			if (bvos != null) {
				for (ScapptApplyBVO dtmp : mainvo.getId_scappt_apply_b()) {
					if (dtmp.getStatus() == VOStatus.NEW)
						dtmp.setStatus(VOStatus.UPDATED);
				}
			}

			if (avos != null) {
				for (ScapptApplyAttachVO dtmp : mainvo
						.getId_scappt_apply_attach()) {
						dtmp.setStatus(VOStatus.UPDATED);
				}
			}

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

	public WfmFormInfoCtx update(WfmFormInfoCtx frmInfoCtx,
			WfmFlowInfoCtx flwInfoCtx) {
		if (frmInfoCtx == null) {
			return null;
		}
		// HumAct humact =
		// PwfmContext.getCurrentBpmnSession().getCurrentHumAct();// 获取当前活动
		// IPort[] port = PortAndEdgeHandler.getSourcePorts(PortAndEdgeHandler
		// .getInEdges(humact)[0]);
		// if (port != null && port.length > 0) {
		// if (port[0] instanceof StartEvent) {// 判断上一活动是否为开始节点
		// ScapPtMethod.initTrsInfoByApplyVO((ScapptApplyHVO) frmInfoCtx);//
		// 只在第一次新增交易信息
		// } else {// 后续更新单据状态（通过billno来查询相应数据）
		// ScapPtMethod.refshState((ScapptApplyHVO) frmInfoCtx);
		// }
		// }
		ScapptApplyHVO mainvo = (ScapptApplyHVO) frmInfoCtx;
		String state = mainvo.getFormstate();
		if (state.equals("End")) {
			ScapPtMethod.initTrsInfoByApplyVO(mainvo);// 初始化交易信息
			CpPubMethod
					.msgSend(new NCMessage[] { CpPubMethod.createMsg(
							"text/plain", "交易申请审批通过通知",
							"单据号:" + mainvo.getBillno() + " 审批通过,请进行产权评估工作！",
							mainvo.getPk_group(), mainvo.getBillmaker(), "nc",
							IDefaultMsgConst.NOTICE, mainvo.getPk_apply_h(),
							null, mainvo.getPk_org(), "scappt") });// 通知消息发送
		}

		try {
			IUifCpbService cpbService = NCLocator.getInstance().lookup(
					IUifCpbService.class);
			cpbService.updateSuperVO((SuperVO) frmInfoCtx, false);
		} catch (BusinessException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage());
		}
		return frmInfoCtx;
	}

	public Class<WfmFormInfoCtx>[] getBizMetaDataDesc(WfmFlwTypeVO flowTypeVo) {
		return new Class[] { nc.scap.pt.vos.ScapptApplyHVO.class };
	}

	@Override
	public WfmFormInfoCtx getWfmFormInfoCtx(String pk_frmins, String pk_flwtype) {
		WfmFlwFormVO vo=(WfmFlwFormVO) ScapDAO.retrieveByPK(WfmFlwFormVO.class, pk_frmins);
		ScapptApplyBVO[] vos=(ScapptApplyBVO[]) ScapDAO.retrieveByCondition(ScapptApplyBVO.class, "pk_apply_h='"+pk_frmins+"'");
		if(vos!=null){
			vo.setId_scappt_apply_b(vos);
		}
		return vo;
	}

	@Override
	public TaskProcessUI getHanlderUrlByTask(Task task) {
		if (task == null)
			return null;
		ScapptApplyHVO vo = null;
		try {
			vo = (ScapptApplyHVO) ScapDAO.getBaseDAO().retrieveByPK(
					ScapptApplyHVO.class, task.getPk_frmins());
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
			throw new LfwRuntimeException("任务被删除！").setTitle("提示");
		}
		boolean flag = false;
		try {
			flag = WfmCPUtilFacade.isCanDelBill(vo.getPk_apply_h());
		} catch (Exception e) {
			flag = false;
		}
		String operation = "approve";
		if (flag) {
			operation = AppConsts.OPE_EDIT;
		}
		String url = "/app/transfer_apply/nc.scap.transfer.applyComps.apply_cardwin";
		if (url == null || url.length() == 0) {
			throw new LfwRuntimeException("无法找到对应的URL");
		}
		url = url + "&" + WfmConstants.WfmUrlConst_TaskPk + "="
				+ task.getPk_task() + "&pk_apply_h=" + task.getPk_frmins()
				+ "&" + AppConsts.OPE_SIGN + "=" + operation
				+ "&model=nc.scap.transfer.model.ApplyEditPageModel";
		TaskProcessUI processUI = new TaskProcessUI();
		processUI.setUrl(LfwRuntimeEnvironment.getRootPath() + "/" + url);
		String title = task.getTitile();
		if ("".equals(title) || title == null) {
			title = "产权交易审核";
		}
		processUI.setTitle(title);
		return processUI;
	}

}