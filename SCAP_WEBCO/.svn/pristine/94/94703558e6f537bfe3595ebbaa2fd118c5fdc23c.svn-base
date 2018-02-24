package nc.scap.pub.util;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.uap.cpb.baseservice.IUifCpbService;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifUpdateUIDataCmdRV;
import nc.uap.lfw.core.cmd.base.UifCommand;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.serializer.impl.Dataset2SuperVOSerializer;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

/**
 * 此 命令 用来 更新列表界面 值  类似于启用、停用 功能
 *  ifdel 用来移除列表行，不做任何数据库操作
 *  
 *  xulong
 * 
 */
public class UpdateListDataCmd extends UifCommand {

	private String masterDsId;
	private boolean ifDel=false;

	public UpdateListDataCmd(String masterDsId,boolean ifDel) {
		this.masterDsId = masterDsId;
		this.ifDel=ifDel;
	}

	@Override
	public void execute() {
		LfwView widget = getLifeCycleContext().getViewContext().getView();
		if (widget == null)
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("pub", "UifMultiDelCmd-000000")/* 片段为空! */);

		if (this.masterDsId == null)
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("pub", "UifMultiDelCmd-000000")/* 未指定数据集id! */);
		Dataset masterDs = widget.getViewModels().getDataset(masterDsId);
		if (masterDs == null)
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("pub", "UifMultiDelCmd-000001")/* 数据集为空,数据集id= */
					+ masterDsId + "!");
		// 要删除的所有行
		Row[] enRows = masterDs.getAllSelectedRows();
		ArrayList<Dataset> detailDss = null;
		DatasetRelation[] rels = null;
		if (widget.getViewModels().getDsrelations() != null) {
			rels = widget.getViewModels().getDsrelations().getDsRelations(masterDsId);
		}
		if (rels != null) {
			// 所有子表
			detailDss = new ArrayList<Dataset>();
			for (int i = 0; i < rels.length; i++) {
				int index = masterDs.getFieldSet().nameToIndex(rels[i].getMasterKeyField());
				Dataset detailDs = widget.getViewModels().getDataset(rels[i].getDetailDataset());
				detailDs.setExtendAttribute("parent_index", index);
				if (detailDs != null) {
					detailDss.add(detailDs);
				}
			}
		}
		if (enRows != null && enRows.length > 0) {
			Dataset2SuperVOSerializer ser = new Dataset2SuperVOSerializer();
			SuperVO[] superVOs = ser.serialize(masterDs, enRows);
			onEnable(superVOs[0]);
			int i=0;
			for (SuperVO supervo : superVOs) {
				CmdInvoker.invoke(new UifUpdateUIDataCmdRV(supervo, masterDs
						.getId()));
				if (ifDel){
					masterDs.removeRow(masterDs.getRowIndex(enRows[i]));
					if (detailDss != null) {
						int size = detailDss.size();
						if (size > 0) {
							for (int j = 0; j < size; j++) {
								Dataset detailDs = detailDss.get(j);
								// 删除子表数据
								for (int k = 0; k < enRows.length; k++) {
									Integer index = (Integer) detailDs.getExtendAttributeValue("parent_index");
									// 移除子表对应的数据片段
									if (enRows[i].getValue(index) != null)
										detailDs.removeRowSet((String) enRows[i].getValue(index));
									detailDs.removeRowSet(detailDs.getCurrentKey());
								}
							}
						}
					}
				}
				i++;
				
			}
		}
		updateButtons();

	}

	protected void onEnable(SuperVO vos) {
		try {
			if (vos != null) {
				IUifCpbService cpbService = NCLocator.getInstance().lookup(
						IUifCpbService.class);
				cpbService.insertOrUpdateSuperVO(vos,false);
			}
		} catch (BusinessException e) {
			Logger.error(e, e);
			throw new LfwRuntimeException(e);
		}
	}
}
