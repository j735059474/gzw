package nc.scap.transfer.trs;

import java.util.Map;

import nc.scap.pt.vos.ScapptTransferHVO;
import nc.scap.pub.util.CpWinUtil;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DatasetCellEvent;

/**
 * ¿¨Æ¬´°¿ÚÄ¬ÈÏÂß¼­
 */
public class XYZRCardWinMainViewCtrl<T extends WebElement> extends
		TrsCardWinMainViewCtrl {
	@Override
	protected void initSelfUI(String operation) {
	}

	@Override
	protected void initSelfData(Dataset masterDs, Row row) {
	}

	@Override
	protected void initChildData(Dataset ds) {
	}

	public void doTaskExecute(Map keys) {
		super.doTaskExecute(keys);
	}

	@Override
	public void onAfterDataChange(DatasetCellEvent datasetCellEvent) {
		super.onAfterDataChange(datasetCellEvent);
	}
}
