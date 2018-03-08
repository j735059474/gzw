package nc.scap.transfer.trs;
import nc.scap.pt.vos.ScapptTransferAssessVO;
import java.util.Map;
import nc.scap.pub.util.CpWinUtil;
import nc.uap.lfw.core.data.Row;
import java.util.UUID;
import nc.scap.pt.vos.ScapptTransferHVO;
import nc.uap.lfw.core.data.Dataset;
/** 
 * @author Administrator
 */
public class ComMergerCardWinMainViewCtr extends TrsCardWinMainViewCtrl {
  private static final String PLUGOUT_ID="afterSavePlugout";
  @Override protected void initSelfUI(  String operation){
    ScapptTransferHVO vo = getMainvo(MAINVO);
		if (vo == null)
			return;
		String pk_passess = vo.getPk_passess();
		if (pk_passess == null || "".equals(pk_passess)) {
			setFormElementsEdit(CpWinUtil.getDataset("scappt_transfer_h"), true);
			// setBtnImgShow(new String[] { "scappt_transfer_assess_grid" });
			CpWinUtil.getDataset("scappt_transfer_assess").setEnabled(true);
		} else {
			setFormElementsEdit(CpWinUtil.getDataset("scappt_transfer_h"),
					false);
			setBtnImgHidden(new String[] { "scappt_transfer_assess_grid" });
		}
  }
  @Override protected void initSelfData(  Dataset masterDs,  Row row){
    ScapptTransferHVO vo = getMainvo(MAINVO);
		if (vo == null)
			return;
		// 初始化标地基本信息
		row.setValue(masterDs.nameToIndex(ScapptTransferHVO.BI_1), vo.getDef4());// 行业
		row.setValue(masterDs.nameToIndex(ScapptTransferHVO.ZZXS), vo.getDef3());// 组织形式
		row.setValue(masterDs.nameToIndex(ScapptTransferHVO.BI_2), vo.getDef2());
		super.initSelfData(masterDs, row);
  }
  @Override protected void initChildData(  Dataset ds){
    super.initChildData(ds);
  }
  @Override protected void initAssDsRow(  Dataset assds){
    ScapptTransferAssessVO[] assvos = getMainvo(MAINVO)
				.getId_scappt_transfer_assess();
		initAssDsRow(assvos, assds);
  }
  /** 
 * 初始化子表数据行
 * @param assvos
 * @param assds
 */
  private void initAssDsRow(  ScapptTransferAssessVO[] assvos,  Dataset assds){
    if (assvos != null && assvos.length > 0) {
			for (ScapptTransferAssessVO tmp : assvos) {
				Row emp = assds.getEmptyRow();
				emp.setRowId(UUID.randomUUID().toString());
				emp.setValue(
						assds.nameToIndex(ScapptTransferAssessVO.VPROJECT),
						tmp.getVproject());
				emp.setValue(assds
						.nameToIndex(ScapptTransferAssessVO.DCARRYINGVALUE),
						tmp.getDcarryingvalue());
				emp.setValue(
						assds.nameToIndex(ScapptTransferAssessVO.DASSESSVALUE),
						tmp.getDassessvalue());
				emp.setValue(assds
						.nameToIndex(ScapptTransferAssessVO.DCHANGEPERCENT),
						tmp.getDchangepercent());
				assds.addRow(emp);
			}
			Row tmpRow = assds.getEmptyRow();
			tmpRow.setRowId(UUID.randomUUID().toString());
			tmpRow.setValue(assds.nameToIndex(ScapptTransferAssessVO.VPROJECT),
					"并购标的对应价值值");
			assds.addRow(tmpRow);
		}
		CpWinUtil.getDataset("scappt_transfer_assess").setEnabled(true);
  }
  public void doTaskExecute(  Map keys){
    super.doTaskExecute(keys);
  }
}
