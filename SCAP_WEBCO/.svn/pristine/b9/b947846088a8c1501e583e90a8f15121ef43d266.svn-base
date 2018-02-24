package nc.scap.pub.entmatter.util;

import nc.uap.lfw.core.bm.IStateManager;
import nc.uap.lfw.core.bm.dft.AbstractStateManager;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.page.LfwView;

/**
 * 审核按钮权限管理器
 * 审核页面能看到的数据都是当前用户需要处理或审批的的单据，如果当局不是驳回给当前制单人的就可以审核
 * nc.scap.pub.entmatter.util.ApproveStateManager
 * @author lpf
 *
 */
public class ApproveStateManager extends AbstractStateManager {
	
	@Override
	public State getState(WebComponent target, LfwView view) {
		// TODO 自动生成的方法存根
		Dataset ds = getCtrlDataset(view);
		if(ds == null)
			return IStateManager.State.DISABLED;
		Row rs = ds.getSelectedRow();
		if(rs == null)
			return IStateManager.State.DISABLED;
		
		String formstate = (String)ds.getValue("formstate");
		if(formstate==null||!"Run".equals(formstate)){
			return State.DISABLED;
		}
		
		String pkValue = (String)ds.getValue(ds.getPrimaryKeyField());
		//需要当前用户审核的单据
		if(MatterUtil.isUserCanApproveBill(pkValue)){
			return State.ENABLED;
		}
		return State.DISABLED;
	}

}
