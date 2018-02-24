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
 * �� ���� ���� �����б���� ֵ  ���������á�ͣ�� ����
 *  ifdel �����Ƴ��б��У������κ����ݿ����
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
					.getStrByID("pub", "UifMultiDelCmd-000000")/* Ƭ��Ϊ��! */);

		if (this.masterDsId == null)
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("pub", "UifMultiDelCmd-000000")/* δָ�����ݼ�id! */);
		Dataset masterDs = widget.getViewModels().getDataset(masterDsId);
		if (masterDs == null)
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("pub", "UifMultiDelCmd-000001")/* ���ݼ�Ϊ��,���ݼ�id= */
					+ masterDsId + "!");
		// Ҫɾ����������
		Row[] enRows = masterDs.getAllSelectedRows();
		ArrayList<Dataset> detailDss = null;
		DatasetRelation[] rels = null;
		if (widget.getViewModels().getDsrelations() != null) {
			rels = widget.getViewModels().getDsrelations().getDsRelations(masterDsId);
		}
		if (rels != null) {
			// �����ӱ�
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
								// ɾ���ӱ�����
								for (int k = 0; k < enRows.length; k++) {
									Integer index = (Integer) detailDs.getExtendAttributeValue("parent_index");
									// �Ƴ��ӱ��Ӧ������Ƭ��
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
