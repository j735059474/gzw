package nc.scap.transfer.trs;

import java.util.Map;

import nc.scap.pt.vos.ScapptTransferHVO;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;

public class TrsCardWinMajorLeaseViewCtr extends TrsCardWinMainViewCtrl {
	private static final String PLUGOUT_ID = "afterSavePlugout";

	@Override
	protected void initChildData(Dataset ds) {
		super.initChildData(ds);
	}

	@Override
	protected void initAssDsRow(Dataset assds) {
		super.initAssDsRow(assds);
	}

	@Override
	protected void initSelfData(Dataset masterDs, Row row) {
		ScapptTransferHVO vo = getMainvo(MAINVO);
		if (vo == null)
			return;
		row.setValue(masterDs.nameToIndex("pgjgmc"), vo.getVporgname());
		row.setValue(masterDs.nameToIndex("pgbgbm"), vo.getVpcode());
		// row.setValue(masterDs.nameToIndex("pgjzr"),
		// vo.getVpbasedate());
		// row.setValue(masterDs.nameToIndex("pgjz"),
		// vo.getVpnumber());
		row.setValue(masterDs.nameToIndex("pgbabm"), vo.getVpnumber());
		row.setValue(masterDs.nameToIndex("pgbadw"), vo.getVpbasedate());
		row.setValue(masterDs.nameToIndex("pgbarq"), vo.getVpdate());
		super.initSelfData(masterDs, row);
	}

	public void doTaskExecute(Map keys) {
		super.doTaskExecute(keys);
	}
}
