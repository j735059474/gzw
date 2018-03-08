package nc.scap.transfer.trs;
import java.util.Map;
import nc.uap.lfw.core.comp.WebElement;
import nc.scap.pub.util.CpWinUtil;
import nc.uap.lfw.core.data.Row;
import nc.scap.pt.vos.ScapptTransferHVO;
import nc.uap.lfw.core.data.Dataset;
public class GPZRCardWinMainCtrl<T extends WebElement> extends TrsCardWinMainViewCtrl {
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
    super.initSelfData(masterDs, row);
  }
  @Override protected void initChildData(  Dataset ds){
    super.initChildData(ds);
  }
  @Override protected void initAssDsRow(  Dataset assds){
    super.initAssDsRow(assds);
  }
  public void doTaskExecute(  Map keys){
    super.doTaskExecute(keys);
  }
}
