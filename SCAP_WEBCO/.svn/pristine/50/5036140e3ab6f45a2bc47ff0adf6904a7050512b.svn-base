package nc.scap.pub.attlist.comp;

import java.util.List;
import java.util.Map;

import nc.scap.pub.attlist.AttachRoleUtil;
import nc.scap.pub.vos.AttachRoleVO;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifAttachCmd;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.IDataBinding;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.file.LfwFileConstants;
import nc.uap.lfw.file.UploadFileHelper;
import nc.uap.lfw.file.vo.LfwFileVO;
import nc.uap.lfw.jsp.uimeta.UITabComp;
import uap.web.bd.pub.AppUtil;

public class AttachRoleCompController implements IAttachRoleCompConstant {

	/*
	 * 公共View显示
	 */
	public void beforeShow(DialogEvent event) {
		
	}

	/*
	 * 选中附件规则
	 */
	public void onAfterRoleSelect(DatasetEvent event) {
		updateButtons(event.getSource());
	}

	/*
	 * 查看附件 按钮
	 */
	public void onGridClick(MouseEvent<?> event) {
		GridComp gridComp = (GridComp) event.getSource();
		Dataset ds = gridComp.getView().getViewModels().getDataset(gridComp.getDataset());
		Row row = ds.getSelectedRow();
		if (row != null) {
			String pk_billType = (String) row.getValue(ds.nameToIndex(FIELD_PK_ATTACH_ROLE));
			String pk_billItem = AttachRoleUtil.getPkBillItem();
			// 显示附件列表
			Map<String, String> params = UploadFileHelper.BuildDefaultPamater(LfwFileConstants.SYSID_BAFILE, pk_billItem, pk_billType, "");
			params.put(LfwFileConstants.SYSID, LfwFileConstants.SYSID_BAFILE);
			String parmState = (String) AppUtil.getAppAttr(IAttachRoleCompConstant.FILE_MGR_CONSTANTS);
			if (parmState != null) {
				params.put(UifAttachCmd.Param_State, parmState);
			}
			params.put(LfwFileConstants.Filemgr_Para_OperateClazz, AttachAfterChangeController.class.getName());
			CmdInvoker.invoke(new UifAttachCmd("附件", params));
		}
	}
	
	public void doRefresh(Map map) {
		
		// 刷新AttachRoleComp附件数量
		UITabComp tabComp = (UITabComp) AppUtil.getCntViewCtx().getUIMeta().getElement();
		String idx = tabComp.getCurrentItem();
		if (idx != null && idx.length() > 0) {
			String dsid = "grid" + idx + "_ds";
			Dataset ds = AppUtil.getCntView().getViewModels().getDataset(dsid);
			Row row = ds.getSelectedRow();
			
			String pk_billitem = AttachRoleUtil.getPkBillItem();
			String pk_billtype = row.getString(ds.nameToIndex(FIELD_PK_ATTACH_ROLE));
			
			// 懒得改了
			AttachRoleVO temp = new AttachRoleVO();
			temp.setPk_attach_role(pk_billtype);
			LfwFileVO[] fileVOs = AttachRoleUtil.getFiles(pk_billitem, temp);
			
			row.setValue(ds.nameToIndex(FIELD_CURRENT_COUNT), fileVOs.length);
		}
	}
	
	/**
	 * 更新按钮状态。如果有选中行，则全部启用。
	 */
	public static void updateButtons(Dataset ds) {
		boolean selected = ds.getSelectedRow() != null;
		IDataBinding[] gridComps = ds.getView().getComponentsByDataset(ds.getId());
		for (IDataBinding gridComp : gridComps) {
			List<MenuItem> menuItems = ((GridComp) gridComp).getMenuBar().getMenuList();
			for (MenuItem menuItem : menuItems) {
				menuItem.setEnabled(selected);
			}
		}
	}

}
