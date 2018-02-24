package nc.scap.dsm.material.state;

import java.util.ArrayList;
import java.util.List;

import nc.scap.pub.model.ScapCoConstants;
import nc.uap.lfw.core.bm.IStateManager;
import nc.uap.lfw.core.bm.dft.AbstractStateManager;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.page.LfwView;

import org.apache.commons.lang.StringUtils;

public class ButtonStateManager extends AbstractStateManager {

	@Override
	public State getState(WebComponent menuItem, LfwView view) {
       String itemId = menuItem.getId();
		
		Dataset ds = getCtrlDataset(view);
		
		if(StringUtils.isBlank(itemId)) return IStateManager.State.DISABLED;
		
		//�½�
		if(ScapCoConstants.ADD.equalsIgnoreCase(itemId)) return IStateManager.State.ENABLED;
		
		Row[] rs =ds.getSelectedRows();
		if(rs == null || rs.length < 1) return IStateManager.State.DISABLED;
		
		
		//�༭
		if(ScapCoConstants.EDIT.equalsIgnoreCase(itemId)){
			if(isSingleSelected(ds) && isState(ds, new String[] {"","NottStart"})){
				return IStateManager.State.ENABLED;
			}
			return IStateManager.State.DISABLED;
		}
		
		//ɾ��
		if(ScapCoConstants.DEL.equalsIgnoreCase(itemId)){
			if(isSelected(ds) && isState(ds, new String[] {"","NottStart"})){
				return IStateManager.State.ENABLED;
			}
			return IStateManager.State.DISABLED;
		}
		
		//����
		if(ScapCoConstants.WF.equalsIgnoreCase(itemId)){
			if(isSingleSelected(ds)){
				return IStateManager.State.ENABLED;
			}
			return IStateManager.State.DISABLED;
		}
		
		//�鿴
		if("scan".equalsIgnoreCase(itemId) || ScapCoConstants.LOOK.equalsIgnoreCase(itemId)){
			if(isSingleSelected(ds)) return IStateManager.State.ENABLED;
			return IStateManager.State.DISABLED;
		}
		
		
		//����
		if("appr".equalsIgnoreCase(itemId) || ScapCoConstants.APPROVE.equalsIgnoreCase(itemId)){
			if(isSingleSelected(ds) && isState(ds, new String[] {"Run"})){
				return IStateManager.State.ENABLED;
			}
			return IStateManager.State.DISABLED;
		}
		
		//����
                if("sendback".equalsIgnoreCase(itemId) || "reject".equalsIgnoreCase(itemId)){
                        if(isSingleSelected(ds) && isState(ds, new String[] {"Run"})){
                                return IStateManager.State.ENABLED;
                        }
                        return IStateManager.State.DISABLED;
                }
                
              //�ύ
                if("submit".equalsIgnoreCase(itemId)){
                        if(isSingleSelected(ds) && isState(ds, new String[] {"","NottStart"})){
                                return IStateManager.State.ENABLED;
                        }
                        return IStateManager.State.DISABLED;
                }
                
                //�ջ�
                if("recycle".equalsIgnoreCase(itemId)){
                        if(isSingleSelected(ds) && isState(ds, new String[] {"Run"})){
                                return IStateManager.State.ENABLED;
                        }
                        return IStateManager.State.DISABLED;
                }
                
                //����/ȡ������
                if("fborwfb".equalsIgnoreCase(itemId)){
                        if(isSingleSelected(ds) && isState(ds, new String[] {"End"})){
                                return IStateManager.State.ENABLED;
                        }
                        return IStateManager.State.DISABLED;
                }
                
              //����
                if("save".equalsIgnoreCase(itemId)){
                        if(isSingleSelected(ds) && isState(ds, new String[] {"Run"})){
                                return IStateManager.State.ENABLED;
                        }
                        return IStateManager.State.DISABLED;
                }
		
		
		return IStateManager.State.ENABLED;
	}
	
	//�ж�state
		private boolean isState(Dataset ds,String[] states){
		    if(ds.nameToIndex("formstate") == -1) {
                        return true;
                    }
			if(ds == null || states == null || states.length == 0) return false;
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < states.length; i++) {
			    if(states[i] != null && !list.contains(states[i])) {
			        list.add(states[i]);
			    }
			}
			
			String formstate = (String)ds.getValue(ds.nameToIndex("formstate"));
			
			if (formstate == null || list.contains(formstate)) {
			    return true;
			}
			return false;
		}
		
		private boolean isState(Dataset ds,String state){
			return isState(ds, new String[]{state});
		}
		
		private boolean isSingleSelected(Dataset ds){
			if(ds == null) return false;
			Row[] rs = ds.getSelectedRows();
			if(rs == null) return false;
			return (rs.length == 1)?true:false;
		}
		
		private boolean isSelected(Dataset  ds){
			if(ds == null) return false;
			Row[] rs = ds.getSelectedRows();
			if(rs == null) return false;
			return (rs.length > 0)?true:false;
		}
		
		private boolean isMultiSelected(Dataset ds){
			if(ds == null) return false;
			Row[] rs = ds.getSelectedRows();
			if(rs == null) return false;
			return (rs.length > 1)? true:false;
		}

}
