package nc.scap.pub.entmatter.util;

import nc.uap.lfw.core.bm.IStateManager;
import nc.uap.lfw.core.bm.dft.AbstractStateManager;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.page.LfwView;

/**
 * 修改按钮状态管理器
 * nc.scap.pub.entmatter.util.EditStateManager
 * @author lpf
 *
 */
public class EditStateManager extends AbstractStateManager {

	@Override
	public State getState(WebComponent target, LfwView view) {
		// TODO 自动生成的方法存根
		Dataset ds = getCtrlDataset(view);
		if(ds == null)
			return IStateManager.State.DISABLED;
		Row rs = ds.getSelectedRow();
		if(rs == null)
			return IStateManager.State.DISABLED;

		//单据没有状态或是还没进入运行状态时,可以修改
		String formstate = (String)ds.getValue(ds.nameToIndex("formstate"));
		if(formstate==null||"".equals(formstate)||"NottStart".equals(formstate)){
			return State.ENABLED;
		}
		if("End".equals(formstate)){
			return State.DISABLED;
		}
		//驳回给制单人的单据可修改
		String pkValue = (String)ds.getValue(ds.getPrimaryKeyField());
		try{
			if(MatterUtil.isBillBackCrrUser(pkValue)){
				return State.ENABLED;
			}
		}catch(Exception e){
			//没有流程定义或是查询出错
			return State.DISABLED;
		}
		return State.DISABLED;
	}

}
