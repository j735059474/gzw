package nc.scap.transfer.trs.wfm;

import java.util.Map;

import uap.web.bd.pub.AppUtil;

import nc.scap.ptpub.PtConstants;
import nc.uap.wfm.engine.IProDefExtHandler;
import nc.uap.wfm.model.ProDef;
import nc.uap.wfm.model.ProIns;

public class TrsExtHandler implements IProDefExtHandler {

	@Override
	public String getFrmNumBillServerClass() {
		return "nc.scap.transfer.trs.wfm.TrsBillNumGen";
	}

	@Override
	public Map<String, String> getExtAttr(ProDef proDef, String frmDefPk) {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public Map<String, String> getExtAttrName(ProDef proDef, String frmDefPk) {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public Map<String, String> getQryTaskAttr(ProDef proDef, String frmDefPk) {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public String getMyPrtptPageModel(String frmDefPk) {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public void afterCreateProIns(ProIns proIns) {
		// TODO �Զ����ɵķ������

	}

	@Override
	public void afterCompleteProIns(ProIns proIns) {
		// TODO �Զ����ɵķ������

	}

}
