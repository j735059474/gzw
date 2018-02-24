package nc.scap.pub.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import nc.uap.lfw.core.bm.ButtonStateManager;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.data.DatasetRelations;
import nc.uap.lfw.core.data.EmptyRow;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.FieldSet;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwView;

public class CopyRowUtil {
	
	public CopyRowUtil(Dataset ds ,Dataset detailDs,Row[] rows,Row row){
		CopyRow(ds, detailDs, rows, row);
	}
	public CopyRowUtil(Dataset ds ,Row row){
		CopyRow(ds, row);
	}
	
	public CopyRowUtil(Dataset ds ,Map<Dataset,Row[]> dmap,Dataset[] detailds,Row row){
		CopyRow(ds,dmap,detailds,row);
	}

	public void CopyRow(Dataset ds,Dataset detailDs,Row[] rows,  Row row){
	    Row copyRow = (Row) row.clone();
			copyRow.setRowId(UUID.randomUUID().toString());
			copyRow.setState(Row.STATE_ADD);
			Field primaryField = null;
			FieldSet fields = ds.getFieldSet();
			for (int i = 0, count = fields.getFieldCount(); i < count; i++) {
				if (fields.getField(i).isPrimaryKey()) {
					primaryField = fields.getField(i);
					break;
				}
			}
			if (primaryField == null)
				throw new LfwRuntimeException(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes().getStrByID("weberm_0", "0E010001-0044")/* * @res * "数据集"*/
						+ ds.getId()
						+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
								"weberm_0", "0E010001-0045")/* @res "没有配置主键字段!" */);
			copyRow.setValue(ds.nameToIndex(primaryField.getId()), null);
			onBeforeRowCopy(ds, copyRow);
			ds.addRow(copyRow);
			ds.setSelectedIndex(ds.getCurrentRowCount() - 1);
			ds.setEnabled(true);
			
//			Field childPrimaryField = null;
//			FieldSet childFields = detailDs.getFieldSet();
//			for (int j = 0; j < childFields.getFieldCount(); j++) {
//				if (childFields.getField(j).isPrimaryKey()) {
//					childPrimaryField = childFields.getField(j);
//					break;
//				}
//			}
//			if (childPrimaryField == null)
//				throw new LfwRuntimeException(nc.vo.ml.NCLangRes4VoTransl
//						.getNCLangRes().getStrByID("weberm_0", "0E010001-0044")/* * @res* "数据集"*/
//						+ detailDs.getId()
//						+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
//								"weberm_0", "0E010001-0045")/* @res "没有配置主键字段!" */);
//			detailDs.setCurrentKey(copyRow.getRowId());
//
//			for (int j = 0; j < rows.length; j++) {
//				Row childRow = rows[j];
//				if (childRow instanceof EmptyRow)
//					continue;
//				Row childCopyRow = (Row) childRow.clone();
//				onBeforeDetailRowCopy(detailDs, childRow);
//				childCopyRow.setValue(
//						detailDs.nameToIndex(childPrimaryField.getId()), null);
//				childCopyRow.setState(Row.STATE_ADD);
//				detailDs.addRow(childCopyRow);
//				detailDs.setSelectedIndex(detailDs.getRowIndex(childRow));
//			}
//			onAfterRowCopy(ds,copyRow);
//			detailDs.setEnabled(true);
			
			//String forgignKey = rels[i].getDetailForeignKey();
				
				
				Field childPrimaryField = null;
				FieldSet childFields = detailDs.getFieldSet();
				for (int j = 0; j < childFields.getFieldCount(); j++) {
					if(childFields.getField(j).isPrimaryKey()){
						childPrimaryField = childFields.getField(j);
						break;
					}
				}
				if(childPrimaryField == null)
					throw new LfwRuntimeException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("weberm_0","0E010001-0044")/*@res "数据集"*/ + detailDs.getId() + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("weberm_0","0E010001-0045")/*@res "没有配置主键字段!"*/);
				//增加新的一页
				//RowSet rowset = new RowSet(copyRow.getRowId());
				//detailDs.addRowSet(copyRow.getRowId(), rowset);
				detailDs.setCurrentKey(copyRow.getRowId());
				detailDs.setEnabled(true);
				//RowData rd = new RowData(0);
				//rowset.addRowData(0, rd);
				//rowset.setRowSetChanged(true);
				for (int j = 0; j < rows.length; j++) {
					Row childRow = rows[j];
					if(childRow instanceof EmptyRow)
						continue;
					Row childCopyRow = (Row) childRow.clone();
					onBeforeDetailRowCopy(detailDs, childRow);
					childCopyRow.setValue(detailDs.nameToIndex(childPrimaryField.getId()), null);
				//	childCopyRow.setValue(detailDs.nameToIndex(forgignKey), null);
//					childCopyRow.setState(Row.STATE_ADD);
					detailDs.addRow(childCopyRow);
//					detailDs.setSelectedIndex(detailDs.getRowIndex(childRow));
				}
				onAfterRowCopy(ds,copyRow);
				detailDs.setEnabled(true);
	  }
	
	
	public void CopyRow(Dataset ds,Map<Dataset,Row[]> dmap,Dataset[] detailds, Row row){
		LfwView widget =  AppLifeCycleContext.current().getWindowContext().getCurrentViewContext().getView();
	    Row copyRow = (Row) row.clone();
			copyRow.setRowId(UUID.randomUUID().toString());
			copyRow.setState(Row.STATE_ADD);
			Field primaryField = null;
			FieldSet fields = ds.getFieldSet();
			for (int i = 0, count = fields.getFieldCount(); i < count; i++) {
				if (fields.getField(i).isPrimaryKey()) {
					primaryField = fields.getField(i);
					break;
				}
			}
			if (primaryField == null)
				throw new LfwRuntimeException(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes().getStrByID("weberm_0", "0E010001-0044")/* * @res * "数据集"*/
						+ ds.getId()
						+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
								"weberm_0", "0E010001-0045")/* @res "没有配置主键字段!" */);
			copyRow.setValue(ds.nameToIndex(primaryField.getId()), null);
			onBeforeRowCopy(ds, copyRow);
			ds.addRow(copyRow);
			ds.setSelectedIndex(ds.getCurrentRowCount() - 1);
			ds.setEnabled(true);
			List<String> idList = new ArrayList<String>();
			detailds=new Dataset[dmap.size()];
			int k=0;
			for (Map.Entry<Dataset,Row[]> entry : dmap.entrySet()) {
						Dataset detailDs = entry.getKey();
						//String forgignKey = rels[i].getDetailForeignKey();
						if(detailDs != null){
							if (entry.getValue() == null) {
								continue;
							}
							Row[] rows = entry.getValue();
							if(rows == null)
								continue;
							Field childPrimaryField = null;
							FieldSet childFields = detailDs.getFieldSet();
							for (int j = 0; j < childFields.getFieldCount(); j++) {
								if(childFields.getField(j).isPrimaryKey()){
									childPrimaryField = childFields.getField(j);
									break;
								}
							}
							if(childPrimaryField == null)
								throw new LfwRuntimeException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("weberm_0","0E010001-0044")/*@res "数据集"*/ + detailDs.getId() + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("weberm_0","0E010001-0045")/*@res "没有配置主键字段!"*/);
							//增加新的一页
							//RowSet rowset = new RowSet(copyRow.getRowId());
							//detailDs.addRowSet(copyRow.getRowId(), rowset);
							detailDs.setCurrentKey(copyRow.getRowId());
							detailDs.setEnabled(true);
							//RowData rd = new RowData(0);
							//rowset.addRowData(0, rd);
							//rowset.setRowSetChanged(true);
							for (int j = 0; j < rows.length; j++) {
								Row childRow = rows[j];
								if(childRow instanceof EmptyRow)
									continue;
								Row childCopyRow = (Row) childRow.clone();
								onBeforeDetailRowCopy(detailDs, childRow);
								childCopyRow.setValue(detailDs.nameToIndex(childPrimaryField.getId()), null);
								childCopyRow.setValue(detailDs.nameToIndex("pk_year_plan_id"), null);
								childCopyRow.setState(Row.STATE_ADD);
								 widget.getViewModels().addDataset(detailDs);
								detailDs.addRow(childCopyRow);
								detailDs.setSelectedIndex(detailDs.getRowIndex(childRow));
							}
							detailDs.setEnabled(true);
							detailds[k]=detailDs;
							k++;
						}
						
				for (int i = 0; i < idList.size(); i++) {
					getLifeCycleContext().getApplicationContext().addBeforeExecScript("pageUI.getWidget('" + widget.getId() + "').getDataset('" + idList.get(i) + "').recordUndo();\n");
				}
				onAfterRowCopy(ds,copyRow);
				ButtonStateManager.updateButtons();
			}
		
	  }
	
	public void CopyRow(Dataset ds, Row row){
		LfwView widget =  AppLifeCycleContext.current().getWindowContext().getCurrentViewContext().getView();
		//LfwView widget =  AppLifeCycleContext.current().getWindowContext().getCurrentViewContext().getView();
	    Row copyRow = (Row) row.clone();
			copyRow.setRowId(UUID.randomUUID().toString());
			copyRow.setState(Row.STATE_ADD);
			Field primaryField = null;
			FieldSet fields = ds.getFieldSet();
			for (int i = 0, count = fields.getFieldCount(); i < count; i++) {
				if (fields.getField(i).isPrimaryKey()) {
					primaryField = fields.getField(i);
					break;
				}
			}
			if (primaryField == null)
				throw new LfwRuntimeException(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes().getStrByID("weberm_0", "0E010001-0044")/* * @res * "数据集"*/
						+ ds.getId()
						+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
								"weberm_0", "0E010001-0045")/* @res "没有配置主键字段!" */);
			copyRow.setValue(ds.nameToIndex(primaryField.getId()), null);
			onBeforeRowCopy(ds, copyRow);
			ds.addRow(copyRow);
			ds.setSelectedIndex(ds.getCurrentRowCount() - 1);
			ds.setEnabled(true);
			List<String> idList = new ArrayList<String>();
			DatasetRelations dsRels = widget.getViewModels().getDsrelations();
			if(dsRels != null){
				DatasetRelation[] rels = dsRels.getDsRelations(ds.getId());
				if(rels != null){
					for (int i = 0; i < rels.length; i++) {
						String detailDsId = rels[i].getDetailDataset();
						idList.add(detailDsId);
						Dataset detailDs = widget.getViewModels().getDataset(detailDsId);
						String forgignKey = rels[i].getDetailForeignKey();
						if(detailDs != null){
							if (detailDs.getCurrentRowData() == null) {
								continue;
							}
							Row[] rows = detailDs.getCurrentRowData().getRows();
							if(rows == null)
								continue;
							Field childPrimaryField = null;
							FieldSet childFields = detailDs.getFieldSet();
							for (int j = 0; j < childFields.getFieldCount(); j++) {
								if(childFields.getField(j).isPrimaryKey()){
									childPrimaryField = childFields.getField(j);
									break;
								}
							}
							if(childPrimaryField == null)
								throw new LfwRuntimeException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("weberm_0","0E010001-0044")/*@res "数据集"*/ + detailDs.getId() + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("weberm_0","0E010001-0045")/*@res "没有配置主键字段!"*/);
							//增加新的一页
							//RowSet rowset = new RowSet(copyRow.getRowId());
							//detailDs.addRowSet(copyRow.getRowId(), rowset);
							detailDs.setCurrentKey(copyRow.getRowId());
							detailDs.setEnabled(true);
							//RowData rd = new RowData(0);
							//rowset.addRowData(0, rd);
							//rowset.setRowSetChanged(true);
							for (int j = 0; j < rows.length; j++) {
								Row childRow = rows[j];
								if(childRow instanceof EmptyRow)
									continue;
								Row childCopyRow = (Row) childRow.clone();
								childCopyRow.setValue(detailDs.nameToIndex(childPrimaryField.getId()), null);
								childCopyRow.setValue(detailDs.nameToIndex(forgignKey), null);
								detailDs.addRow(childCopyRow);
							}

						}

					}
				}
			}

						
				for (int i = 0; i < idList.size(); i++) {
					getLifeCycleContext().getApplicationContext().addBeforeExecScript("pageUI.getWidget('" + widget.getId() + "').getDataset('" + idList.get(i) + "').recordUndo();\n");
				}
				onAfterRowCopy(ds,copyRow);
				ButtonStateManager.updateButtons();
		
	  }
	
	protected void onBeforeRowCopy(  Dataset ds,  Row copyRow){
	  }
	protected void onBeforeDetailRowCopy(Dataset detailds,Row childrow){
	}
	protected void onAfterRowCopy(Dataset ds,Row row) {
	}
	protected AppLifeCycleContext getLifeCycleContext() {
		return AppLifeCycleContext.current();
	}
	
	public static Map<Dataset,Row[]> getDetailDss(String mainds){
		Map<Dataset,Row[]> dmap=new HashMap<Dataset,Row[]>();
		LfwView widget =  AppLifeCycleContext.current().getWindowContext().getCurrentViewContext().getView();
		DatasetRelations dsRels = widget.getViewModels().getDsrelations();
		DatasetRelation[] rels = dsRels.getDsRelations(mainds);
		if(rels != null){
			for (int i = 0; i < rels.length; i++) {
				String detailDsId = rels[i].getDetailDataset();
				Dataset detailDs = widget.getViewModels().getDataset(detailDsId);
				Row[] rows = detailDs.getCurrentRowData().getRows();
				dmap.put(detailDs, rows);
			}
		}
		return dmap;
	}
	
}
