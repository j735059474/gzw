package nc.scap.dsm.material.wfm;

import nc.bs.framework.common.NCLocator;
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
import nc.uap.wfm.vo.WfmFlwTypeVO;
import nc.uap.wfm.vo.WfmFormInfoCtx;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;

import org.apache.commons.lang.StringUtils;

public class WfmFlwFormOper extends DefaultFormOper {
	public WfmFormInfoCtx save(WfmFormInfoCtx frmInfoCtx, WfmFlowInfoCtx flwInfoCtx) {
		//当单据PK不存在的时候发挥作用
		if (frmInfoCtx == null) {
			return null;
		}
		try {
			IUifCpbService cpbService = NCLocator.getInstance().lookup(IUifCpbService.class);
			cpbService.insertOrUpdateSuperVO((SuperVO)frmInfoCtx,false);
			((SuperVO) frmInfoCtx).setStatus(VOStatus.UPDATED);		
			/*
			此处需开发者添加逻辑， 将子表状态置为UPDATED，多子表需使用for循环将所有子表状态置为UPDATE
			代码参考上面主表状态置UPDATE的写法
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
		
		String approvestate = (String) frmInfoCtx.getAttributeValue(frmInfoCtx.getFrmStateField());
                if(StringUtils.isBlank(approvestate)){
                }else if("NottStart".equalsIgnoreCase(approvestate)){
                }else if("Run".equalsIgnoreCase(approvestate)){
                }else if("Suspended".equalsIgnoreCase(approvestate)){
                }else if("Cancellation".equalsIgnoreCase(approvestate)){
                }else if("End".equalsIgnoreCase(approvestate)){
                        frmInfoCtx.setAttributeValue("fbzt", "1");
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
		return new Class[] { nc.vo.scapjj.material.Material_HVO.class};
	}
	
	@Override
	public WfmFormInfoCtx getWfmFormInfoCtx(String pk_frmins,String pk_flwtype) {
		return null;
	}
	
	@Override
	public TaskProcessUI getHanlderUrlByTask(Task task) {
		if (task == null)
			return null;
		String url = "/app/dsm_material/materialuiComps.material_cardwin";
		if (url == null || url.length() == 0) {
			throw new LfwRuntimeException("无法找到对应的URL");
		}
		url = url + "&" + WfmConstants.WfmUrlConst_TaskPk + "=" + task.getPk_task() + "&pk_material_h=" + task.getPk_frmins()
				+ "&" + AppConsts.OPE_SIGN + "=" + "APPR";
		String urlExt="&model=nc.scap.dsm.material.MaterialPageModel&node_type=add";
		TaskProcessUI processUI = new TaskProcessUI();	
		processUI.setUrl(LfwRuntimeEnvironment.getRootPath() + "/" + url+urlExt);
		String title = task.getTitile();
		if("".equals(title) || title == null){
			title = "资料共享";
		}
		processUI.setTitle(title);
		return processUI;
	}
	
}