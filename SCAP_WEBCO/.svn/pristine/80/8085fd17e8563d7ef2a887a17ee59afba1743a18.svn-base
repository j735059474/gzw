package nc.scap.jjpub.checkutil;

import java.util.List;

import uap.lfw.core.ml.LfwResBundle;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.uap.cpb.persist.dao.PtBaseDAO;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.wfm.exception.WfmServiceException;
import nc.uap.wfm.handler.PortAndEdgeHandler;
import nc.uap.wfm.itf.IWfmProInsQry;
import nc.uap.wfm.logger.WfmLogger;
import nc.uap.wfm.model.IPort;
import nc.uap.wfm.model.ProIns;
import nc.uap.wfm.model.StartEvent;
import nc.uap.wfm.model.Task;
import nc.uap.wfm.utils.WfmCPUtilFacade;
import nc.uap.wfm.vo.WfmTaskVO;

/**
 * 校验单据是否是审批通过或审批中
 * 
 * @author thx
 *
 */
public class CheckFormState {

	
	/**
	 * 校验单据是否是 运行态或结束态,
	 * 如果是 ,则抛出异常 msg
	 * @param ds
	 * @param msg
	 * @throws LfwRuntimeException
	 */
	public void execute(Dataset ds,String msg)throws LfwRuntimeException{
		Row row=ds.getSelectedRow();
		if(row==null)
			return;
		String pkValue = (String) row.getValue(ds.nameToIndex(ds.getPrimaryKeyField()));
		pkValue=pkValue==null?" ":pkValue;
		if (pkValue != null && !pkValue.equals("")) {
			boolean isCanDel = isCanEditBill(pkValue, msg);
			if (isCanDel) {
			} else {
//				throw new LfwRuntimeException(msg==null?"流程已启动，无法操作单据":msg);
				throw new LfwRuntimeException(msg==null?"已经进入流程的单据不能操作!":msg);
			}
		}
	
	}
	/**
	 * 返回 单据是否进入审批流
	 * false 进入
	 * true 没进入
	 * @param pk_form
	 * @param msg
	 * @return
	 */
	public boolean isCanEditBill(String pk_form,String msg){
		String startHumId = null;
		//通过表单pk获得制单活动id
		try {
			ProIns proIns = NCLocator.getInstance().lookup(IWfmProInsQry.class).getProInsByFormInsPk(pk_form);
			if(proIns==null){ //该单据没有流程时，也可以删除
				return true;
			}
			StartEvent start = proIns.getProDef().getStart();
			IPort[] ports = PortAndEdgeHandler.getNextHumActs(start);
			startHumId = ports[0].getId();
		} catch (WfmServiceException e) {
			WfmLogger.error(e);
			throw new LfwRuntimeException(e.getMessage());
		}
		try {
			//获得制单活动上待办的任务,已经进入逐级审批的待办任务(pk_dynamiconwer不是空)除外
			PtBaseDAO dao = new PtBaseDAO();
			String sql = "SELECT * FROM wfm_task WHERE pk_frmins = '" + pk_form +
				"' and (state = '" + Task.State_Run + "' "+"or state = '" + Task.State_BeforeAddSignPlmnt + "'"+" or state = '" + Task.State_Plmnt + "'"+") and port_id = '" + startHumId + 
				"' and (pk_dynamiconwer is null or pk_dynamiconwer = '~')" + " order by ts desc";
			List<WfmTaskVO> vos = (List<WfmTaskVO>) dao.executeQuery(sql, new BeanListProcessor(WfmTaskVO.class));
			if (vos != null && vos.size() >0){
				return true;
			} else {
				return false;
			}
		} catch (DAOException e) {
			WfmLogger.error(e);
			throw new LfwRuntimeException(e.getMessage());
		}
	}
	
	
	
}
