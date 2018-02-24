package nc.scap.pub.entmatter.util;

import nc.uap.lfw.core.bm.IStateManager;
import nc.uap.lfw.core.bm.dft.AbstractStateManager;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.page.LfwView;

/**
 * ��˰�ťȨ�޹�����
 * ���ҳ���ܿ��������ݶ��ǵ�ǰ�û���Ҫ����������ĵĵ��ݣ�������ֲ��ǲ��ظ���ǰ�Ƶ��˵ľͿ������
 * nc.scap.pub.entmatter.util.ApproveStateManager
 * @author lpf
 *
 */
public class ApproveStateManager extends AbstractStateManager {
	
	@Override
	public State getState(WebComponent target, LfwView view) {
		// TODO �Զ����ɵķ������
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
		//��Ҫ��ǰ�û���˵ĵ���
		if(MatterUtil.isUserCanApproveBill(pkValue)){
			return State.ENABLED;
		}
		return State.DISABLED;
	}

}
