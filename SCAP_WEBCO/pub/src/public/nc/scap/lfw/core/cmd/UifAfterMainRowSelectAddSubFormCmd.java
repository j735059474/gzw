package nc.scap.lfw.core.cmd;
import java.util.ArrayList;
import java.util.List;
import nc.uap.lfw.core.cmd.base.UifCommand;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.ViewContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.data.DatasetRelations;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwView;
import nc.vo.ml.NCLangRes4VoTransl;
/**
 * "����"�˵��߼�����
 * 
 * @author gd 2010-3-26
 * 
 */
public class UifAfterMainRowSelectAddSubFormCmd extends UifCommand {
	private String dsId;
	private String navDatasetId;
	private String navStr;
	public UifAfterMainRowSelectAddSubFormCmd(String dsId) {
		this.dsId = dsId;
	}
	public UifAfterMainRowSelectAddSubFormCmd(String dsId, String navDatasetId, String navStr) {
		this.dsId = dsId;
		this.navDatasetId = navDatasetId;
		this.navStr = navStr;
	}
	public void execute() {
		ViewContext widgetctx = getLifeCycleContext().getViewContext();
		boolean pageRecordUndo = false;
		boolean widgetRecordUndo = false;
		LfwView widget = widgetctx.getView();
		if (this.dsId == null)
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pub", "UifAddCmd-000000")/*δָ�����ݼ�id!*/);
		Dataset ds = widget.getViewModels().getDataset(dsId);
		if (ds == null)
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pub", "UifAddCmd-000001")/*���ݼ�Ϊ��,���ݼ�id=*/ + dsId + "!");
		String currKey = ds.getCurrentKey();
		if (currKey == null || currKey.equals("")) {
			if (this.navDatasetId != null)
				throw new LfwRuntimeException(navStr);
			ds.getRowSet(Dataset.MASTER_KEY, true);
			ds.setCurrentKey(Dataset.MASTER_KEY);
		}
		if (ds.isControlwidgetopeStatus())
			widgetRecordUndo = true;
		List<String> idList = new ArrayList<String>();
		idList.add(dsId);
		DatasetRelations dsRels = widget.getViewModels().getDsrelations();
		if (dsRels != null) {
			DatasetRelation[] rels = dsRels.getDsRelations(dsId);
			if (rels != null) {
				for (int i = 0; i < rels.length; i++) {
					String detailDsId = rels[i].getDetailDataset();
					idList.add(detailDsId);
					Dataset detailDs = widget.getViewModels().getDataset(detailDsId);
					Row row = detailDs.getEmptyRow();
					detailDs.addRow(row);
					detailDs.setRowSelectIndex(detailDs.getRowIndex(row));
					onBeforeRowAdd(detailDs,row);
					detailDs.setEnabled(true);
					if (detailDs.isControlwidgetopeStatus())
						widgetRecordUndo = true;
				}
			}
		}
		if (pageRecordUndo)
			getLifeCycleContext().getApplicationContext().addBeforeExecScript("pageUI.recordUndo();\n");
		if (widgetRecordUndo)
			getLifeCycleContext().getApplicationContext().addBeforeExecScript("pageUI.getWidget('" + widget.getId() + "').recordUndo();\n");
		{
			for (int i = 0; i < idList.size(); i++) {
				getLifeCycleContext().getApplicationContext().addBeforeExecScript("pageUI.getWidget('" + widget.getId() + "').getDataset('" + idList.get(i) + "').recordUndo();\n");
			}
		}

	}
	protected void setNavPkToRow(Row row, String navId, Dataset slaveDs) {
		if (navId == null)
			return;
		LfwView widget = AppLifeCycleContext.current().getViewContext().getView();
		// �����ӱ�
		DatasetRelations dsRels = widget.getViewModels().getDsrelations();
		if (dsRels != null) {
			DatasetRelation rel = dsRels.getDsRelation(navId, slaveDs.getId());
			Dataset ds = widget.getViewModels().getDataset(navId);
			Row navrow = ds.getSelectedRow();
			if (navrow == null) {
				throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pub", "UifAddCmd-000002")/*��ѡ�񵼺����ݼ�*/);
			}
			Object value = navrow.getValue(ds.nameToIndex(rel.getMasterKeyField()));
			row.setValue(slaveDs.nameToIndex(rel.getDetailForeignKey()), value);
		}
	}
	protected void onAfterRowAdd(Row row) {
		updateButtons();
	}
	protected void onBeforeRowAdd(Dataset ds ,Row row) {}
}
