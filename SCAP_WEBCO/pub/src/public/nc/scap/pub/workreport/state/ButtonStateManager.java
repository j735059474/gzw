package nc.scap.pub.workreport.state;

import java.util.ArrayList;
import java.util.List;

import nc.scap.pub.model.ScapCoConstants;
import nc.scap.pub.vos.WorkReportVO;
import nc.uap.lfw.core.bm.IStateManager;
import nc.uap.lfw.core.bm.dft.AbstractStateManager;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.page.LfwView;

public class ButtonStateManager extends AbstractStateManager {

	@Override
	public State getState(WebComponent target, LfwView view) {
		// TODO 自动生成的方法存根
		String itemId = target.getId();
		Dataset ds = getCtrlDataset(view);
		if(ds == null || itemId == null)
			return IStateManager.State.DISABLED;
		
		if (ScapCoConstants.ADD.equals(itemId)) {
			return IStateManager.State.ENABLED;
		} else if (ScapCoConstants.EDIT.equals(itemId)) {
			if (isSingleSelected(ds)
					&& (isState(ds, new String[]{"0","2"}))) {
				return IStateManager.State.ENABLED;
			}
			else {
				return IStateManager.State.DISABLED;
			}
			
		} else if (ScapCoConstants.DEL.equals(itemId)) {
			if ((isSingleSelected(ds)
					|| isMultiSelected(ds))
					&& isState(ds, new String[]{"0","2"})) {
				return IStateManager.State.ENABLED;
			}
			else {
				return IStateManager.State.DISABLED;
			}
			
		}
		else if (ScapCoConstants.LOOK.equals(itemId)) {
			if (isSingleSelected(ds)) {
				return IStateManager.State.ENABLED;
			}
			else {
				return IStateManager.State.DISABLED;
			}
		} else if (ScapCoConstants.APPROVE.equals(itemId)) {
			if (isSingleSelected(ds) 
					&& isState(ds, "1")
					&& isHandleState(ds, "0")) {
				return IStateManager.State.ENABLED;
			}
			else {
				return IStateManager.State.DISABLED;
			}
		} else if (ScapCoConstants.REAPPROVE.equals(itemId)) {
			if (isSingleSelected(ds) 
					&& isState(ds, "3")
					&& isHandleState(ds, "1")) {
				return IStateManager.State.ENABLED;
			}
			else {
				return IStateManager.State.DISABLED;
			}
		} else if (ScapCoConstants.RECEIVE.equals(itemId)) {
			if ((isSingleSelected(ds)
					|| isMultiSelected(ds))
					&& isState(ds, "1")
					&& isHandleState(ds, "0")) {
				return IStateManager.State.ENABLED;
			}
			else {
				return IStateManager.State.DISABLED;
			}
		} else if (ScapCoConstants.RECYCLE.equals(itemId)) {
			if ((isSingleSelected(ds)
					|| isMultiSelected(ds))
					&& isState(ds, "1")) {
				return IStateManager.State.ENABLED;
			}
			else {
				return IStateManager.State.DISABLED;
			}
		} else if (ScapCoConstants.ROLLBACK.equals(itemId)) {
			if ((isSingleSelected(ds)
					|| isMultiSelected(ds))
					&& isState(ds, "1")
					&& isHandleState(ds, "0")) {
				return IStateManager.State.ENABLED;
			}
			else {
				return IStateManager.State.DISABLED;
			}
		} else if (ScapCoConstants.SUBMIT.equals(itemId)) {
			if ((isSingleSelected(ds)
					|| isMultiSelected(ds))
					&& isState(ds, new String[]{"0","2"})) {
				return IStateManager.State.ENABLED;
			}
			else {
				return IStateManager.State.DISABLED;
			}
		} else if("back".equals(itemId)){
			if ((isSingleSelected(ds)
					|| isMultiSelected(ds))
					&& isState(ds, "1")) {
				return IStateManager.State.ENABLED;
			}
			else {
				return IStateManager.State.DISABLED;
			}
		} else if("return".equals(itemId)){
			if ((isSingleSelected(ds)
					|| isMultiSelected(ds))
					&& isState(ds, "1")
					&& isHandleState(ds, "0")) {
				return IStateManager.State.ENABLED;
			}
			else {
				return IStateManager.State.DISABLED;
			}
		}
		
		return IStateManager.State.ENABLED;
	}
	
	private boolean isSingleSelected(Dataset ds) {
		if(ds == null)
			return false;
		Row[] rs = ds.getSelectedRows();
		if(rs == null)
			return false;
		return rs.length == 1 ? true : false;
	}
	
	private boolean isMultiSelected(Dataset ds) {
		if(ds == null)
			return false;
		Row[] rs = ds.getSelectedRows();
		if(rs == null)
			return false;
		return rs.length > 0 ? true : false;
	}
	
	private boolean isState(Dataset ds, String state) {
		if(ds == null || state == null)
			return false;
		Row[] rs = ds.getSelectedRows();
		if(rs == null || rs.length < 1)
			return false;
		String st = "";
		for(int i = 0; i < rs.length; i++) {
			st = (String) rs[i].getValue(ds
					.nameToIndex(WorkReportVO.STATE));
			if(!state.equals(st)) {
				return false;
			}
		}
		
		return true;
	}
	

	private boolean isState(Dataset ds, String[] states) {
		if(ds == null || states == null || states.length == 0)
			return false;

		Row[] rs = ds.getSelectedRows();
		if(rs == null || rs.length < 1)
			return false;
		
		List<String> stateList = new ArrayList<String>();
		for(int i = 0; i < states.length; i++) {
			if(states[i] != null && states[i].length() != 0) {
				stateList.add(states[i]);
			}
		}
		
		String st = "";
		boolean result = true;
		for(int i = 0; i < rs.length; i++) {
			st = (String) rs[i].getValue(ds
					.nameToIndex(WorkReportVO.STATE));
			if(!stateList.contains(st)) {
				result = false;
				break;
			}
		}
		
		return result;
	}
	
	private boolean isHandleState(Dataset ds, String state) {
		if(ds == null || state == null)
			return false;
		Row[] rs = ds.getSelectedRows();
		if(rs == null || rs.length < 1)
			return false;
		String st = "";
		for(int i = 0; i < rs.length; i++) {
			st = (String) rs[i].getValue(ds
					.nameToIndex(WorkReportVO.HANDLE_STATE));
			if(!state.equals(st)) {
				return false;
			}
		}
		
		return true;
	}

}
