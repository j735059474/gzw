package nc.scap.transfer.trs;

import nc.scap.ptpub.method.ScapPtMethod;
import java.util.Map;
import java.util.UUID;

import nc.scap.ptpub.PtConstants;
import nc.uap.lfw.core.AppInteractionUtil;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.DatasetCellEvent;
import nc.uap.lfw.core.data.Row;
import nc.scap.pt.vos.ScapptTransferHVO;
import nc.scap.pt.vos.ScapptTransferTargetVO;
import nc.scap.pub.util.CpWinUtil;
import nc.uap.lfw.core.data.Dataset;
import nc.vo.pub.lang.UFDate;

/**
 * 卡片窗口默认逻辑
 */
public class WCZRCardWinMainViewCtrl<T extends WebElement> extends
		TrsCardWinMainViewCtrl {
	@Override
	protected void initSelfUI(String operation) {

	}

	@Override
	protected void initSelfData(Dataset masterDs, Row row) {
		super.initSelfData(masterDs, row);
	}

	@Override
	protected void initChildData(Dataset ds) {
		 super.initChildData(ds);
	}

	public void doTaskExecute(Map keys) {
		super.doTaskExecute(keys);
	}

	@Override
	public void onAfterDataChange(DatasetCellEvent datasetCellEvent) {
		super.onAfterDataChange(datasetCellEvent);
		
		Dataset ds = datasetCellEvent.getSource();
		Row row = ds.getSelectedRow();
		Integer colIndex = datasetCellEvent.getColIndex();
		
		Integer Icwctdate = ds.nameToIndex("cwctdate");
		Integer Icwforensicdate = ds.nameToIndex("cwforensicdate");
		
		if((Icwctdate == colIndex)||(Icwforensicdate == colIndex)){
			UFDate UDcwctdate = (UFDate) CpWinUtil.getValue(ds, "cwctdate");
			UFDate UDcwforensicdate = (UFDate) CpWinUtil.getValue(ds, "cwforensicdate");
			if(UDcwctdate != null && UDcwforensicdate!=null && !UDcwctdate.equals(UDcwforensicdate)){
				if(UDcwctdate.after(UDcwforensicdate)){
					AppInteractionUtil.showMessageDialog("交易完成日期不能早于同签订日期！");
					row.setValue(colIndex, null);
				}
			}
		}
		
//		Dataset ds = datasetCellEvent.getSource();
//		if (ds.nameToIndex("pk_sorg_vname") == datasetCellEvent.getColIndex()) {
//			String pk_sorg = (String) ds.getSelectedRow().getValue(
//					ds.nameToIndex("pk_sorg"));
//			String value = (String) datasetCellEvent.getNewValue();
//			if (value != null && !value.equals("") && pk_sorg != null
//					&& !pk_sorg.equals("")) {
//				setFormDataByTypt(null, null, null, pk_sorg, ds,
//						ds.getSelectedRow());
//				ScapptTransferHVO svo = getMainvo(MAINVO_S);
//				if (svo == null)
//					return;
//				Row row = ds.getSelectedRow();
//				row.setValue(ds.nameToIndex(ScapptTransferHVO.PK_SCURRENCY),
//						svo.getPk_bcurrency());
//				row.setValue(ds.nameToIndex(ScapptTransferHVO.VSUNIT),
//						svo.getVbunit());
//				row.setValue(ds.nameToIndex(ScapptTransferHVO.VSNAME),
//						svo.getDef1());
//				row.setValue(ds.nameToIndex(ScapptTransferHVO.DEF4), null);
//				initTrsFreeChild(true);
//			} else {
//				ds.getSelectedRow().setValue(
//						ds.nameToIndex(ScapptTransferHVO.VSFACTYPE), null);
//				ds.getSelectedRow().setValue(
//						ds.nameToIndex(ScapptTransferHVO.VAGENCY), null);
//				ds.getSelectedRow().setValue(
//						ds.nameToIndex(ScapptTransferHVO.VSNAME), null);
//				ds.getSelectedRow().setValue(
//						ds.nameToIndex(ScapptTransferHVO.DSCAPITAL), null);
//			}
//		}
//		if (ds.nameToIndex("def4") == datasetCellEvent.getColIndex()) {
//			String value = (String) datasetCellEvent.getNewValue();
//			if (value != null && !"".equals(value)) {
//				ds.getSelectedRow().setValue(
//						ds.nameToIndex(ScapptTransferHVO.PK_SORG), null);
//				initTrsFreeChild(false);
//			}
//		}
	}
	
}
