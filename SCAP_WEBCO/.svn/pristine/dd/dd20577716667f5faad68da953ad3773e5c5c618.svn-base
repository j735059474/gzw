package nc.scap.pub.entmatter.util;

import nc.uap.lfw.core.bm.IStateManager;
import nc.uap.lfw.core.bm.dft.AbstractStateManager;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.page.LfwView;

/**
 * 删除按钮状态管理器
 * nc.scap.pub.entmatter.util.DeleteStateManager
 * @author lpf
 *
 */
public class DeleteStateManager extends AbstractStateManager {
	
	@Override
	public State getState(WebComponent target, LfwView view) {
		Dataset ds = getCtrlDataset(view);
		if(ds == null)
			return IStateManager.State.DISABLED;
		Row rs = ds.getSelectedRow();
		if(rs == null)
			return IStateManager.State.DISABLED;
		//一般情况下，没有进入流程的单据可以删除
		String formstate = (String)ds.getValue(ds.nameToIndex("formstate"));
		if(formstate==null||"".equals(formstate)||"NottStart".equals(formstate)
				||"Cancellation".equals(formstate)){
			return State.ENABLED;
		}
		return State.DISABLED;
	}

}
