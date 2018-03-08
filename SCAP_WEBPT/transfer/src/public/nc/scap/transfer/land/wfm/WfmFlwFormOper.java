package nc.scap.transfer.land.wfm;

import nc.bs.framework.common.NCLocator;
import nc.scap.transfer.land.utils.ILandConstants;
import nc.uap.cpb.baseservice.IUifCpbService;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.constants.AppConsts;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.wfm.context.HumActInfoEngCtx;
import nc.uap.wfm.context.PwfmContext;
import nc.uap.wfm.context.WfmFlowInfoCtx;
import nc.uap.wfm.dftimpl.DefaultFormOper;
import nc.uap.wfm.engine.TaskProcessUI;
import nc.uap.wfm.model.Task;
import nc.uap.wfm.utils.WfmTaskUtil;
import nc.uap.wfm.vo.WfmFlwTypeVO;
import nc.uap.wfm.vo.WfmFormInfoCtx;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;

import org.apache.commons.lang.StringUtils;

public class WfmFlwFormOper extends DefaultFormOper {
	public WfmFormInfoCtx save(WfmFormInfoCtx frmInfoCtx, WfmFlowInfoCtx flwInfoCtx) {
		//������PK�����ڵ�ʱ�򷢻�����
		if (frmInfoCtx == null) {
			return null;
		}
		try {
			IUifCpbService cpbService = NCLocator.getInstance().lookup(IUifCpbService.class);
			cpbService.insertOrUpdateSuperVO((SuperVO)frmInfoCtx,false);
			((SuperVO) frmInfoCtx).setStatus(VOStatus.UPDATED);		
			/*
			�˴��迪��������߼��� ���ӱ�״̬��ΪUPDATED�����ӱ���ʹ��forѭ���������ӱ�״̬��ΪUPDATE
			����ο���������״̬��UPDATE��д��
			*/	
			
		} 
		catch (BusinessException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage());
		}
		
		return frmInfoCtx;
	}

	public WfmFormInfoCtx update(WfmFormInfoCtx frmInfoCtx, WfmFlowInfoCtx flwInfoCtx) {
		if (frmInfoCtx == null) {
			return null;
		}
		WfmFlwFormVO infoCtx=	(WfmFlwFormVO)frmInfoCtx;
		 String state=infoCtx.getFormstate();
		if(StringUtils.isNotEmpty(state)){
			
			if(ILandConstants.FOMRSTATE_NOTSTART.equals(state)){
				//δ��������
				infoCtx.setApprovestatus(ILandConstants.APPROVESTATE_ADD);
				
			}else if(ILandConstants.FOMRSTATE_RUN.equals(state)){
				infoCtx.setApprovestatus(ILandConstants.APPROVESTATE_APPROVING);
				
				//���ص��Ƶ���
				if ("reject".equals(WfmTaskUtil.getOperatorFromSession())) {
			        HumActInfoEngCtx rejectInfo = PwfmContext.getCurrentBpmnSession().getRejectInfo();
			        String[] rejectUsers = rejectInfo.getUserPks();
			        String pk_billuser = (String)infoCtx.getBillmaker();
			        if ((rejectUsers == null) || (rejectUsers.length == 0)) {
			          throw new LfwRuntimeException("��ѡ���˻���");
			        }
			        int count = 0;
			        for (int i = 0; i < rejectUsers.length; i++) {
			          if (pk_billuser.equals(rejectUsers[i])) {
			            count++;
			          }
			        }
			        if (count == 0) {
			        	//���̽�����
						infoCtx.setApprovestatus(ILandConstants.APPROVESTATE_APPROVING);
			        } else {
			        	//���̽����в��ص��Ƶ��ˡ����״̬Ϊ����
						infoCtx.setApprovestatus(ILandConstants.APPROVESTATE_ADD);
						
			        }
			      }else if("recallBack".equals(WfmTaskUtil.getOperatorFromSession())) {
			    	//�ջص��Ƶ��ˡ����״̬Ϊ����
						infoCtx.setApprovestatus(ILandConstants.APPROVESTATE_ADD);
				  }
				
			}else if(ILandConstants.FOMRSTATE_END.equals(state)){
				//���̽���
				infoCtx.setApprovestatus(ILandConstants.APPROVESTATE_APPROVED);
			}else if(ILandConstants.FOMRSTATE_SUSPENDED.equals(state)){
				//���̹���
				infoCtx.setApprovestatus(ILandConstants.APPROVESTATE_CANCELLATION);
			}else if(ILandConstants.FOMRSTATE_CANCELLATION.equals(state)){
				//���̷���
				infoCtx.setApprovestatus(ILandConstants.APPROVESTATE_CANCELLATION);
			}
		}
		try {
			IUifCpbService cpbService = NCLocator.getInstance().lookup(IUifCpbService.class);
			cpbService.updateSuperVO((SuperVO) frmInfoCtx, false);
		} catch (BusinessException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage());
		}
		return frmInfoCtx;
	}
	
	
	public Class<WfmFormInfoCtx>[] getBizMetaDataDesc(WfmFlwTypeVO flowTypeVo) {
		return new Class[] { nc.scap.pt.vos.LandVO.class};
	}
	
	@Override
	public WfmFormInfoCtx getWfmFormInfoCtx(String pk_frmins,String pk_flwtype) {
		return null;
	}
	
	@Override
	public TaskProcessUI getHanlderUrlByTask(Task task) {
		if (task == null)
			return null;
		String url = "/app/transfer_land/transferComps.land_cardwin";
		if (url == null || url.length() == 0) {
			throw new LfwRuntimeException("�޷��ҵ���Ӧ��URL");
		}
		url = url + "&" + WfmConstants.WfmUrlConst_TaskPk + "=" + task.getPk_task() + "&openBillId=" + task.getPk_frmins()
				+ "&" + AppConsts.OPE_SIGN + "=" + AppConsts.OPE_EDIT;
		url = url + "&" + "model=nc.scap.transfer.model.LandPopPageModel&audits=audit";
		TaskProcessUI processUI = new TaskProcessUI();	
		processUI.setUrl(LfwRuntimeEnvironment.getRootPath() + "/" + url);
		String title = task.getTitile();
		if("".equals(title) || title == null){
			title = "����ռ�м�����";
		}
		processUI.setTitle(title);
		return processUI;
	}
	
}