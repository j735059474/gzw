package nc.scap.pub.userchoice;
import nc.uap.cpb.org.orgs.CpOrgVO;
import nc.uap.lfw.core.cmd.CmdInvoker;
import java.util.Map;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.DatasetCellEvent;
import nc.uap.lfw.core.exception.StringInputItem;
import nc.uap.lfw.core.exception.InputItem;
import java.util.HashMap;
import nc.uap.lfw.core.cmd.UifPlugoutCmd;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import nc.uap.lfw.core.cmd.UifLineDelCmd;
import nc.uap.lfw.core.cmd.UifCloseViewCmd;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.data.Dataset;
import nc.scap.pub.util.ScapDAO;
import nc.uap.lfw.core.AppInteractionUtil;
import nc.scap.pub.util.CpVOUtil;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.cmd.UifDatasetEnableCmd;
import uap.web.bd.pub.AppUtil;
import nc.scap.pub.util.CntUtil;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.scap.pub.util.CpWinUtil;
import nc.scap.pub.vos.ScapUserChoiceVO;
import nc.scap.pub.vos.ScapUserChoiceBVO;
import nc.uap.lfw.core.event.MouseEvent;
public class UserchoiceViewController {
  public static final String DO_OK_PLUGOUT_PARAM_NAME="users";//userchoice_b_grid$add
  public static final CpVOUtil<ScapUserChoiceVO> userChoiceVOUtil=new CpVOUtil<ScapUserChoiceVO>();
  public static final CpVOUtil<ScapUserChoiceBVO> userChoiceBVOUtil=new CpVOUtil<ScapUserChoiceBVO>();
  protected String getMasterDatasetId(){
    return "userchoice_ds";
  }
  protected String[] getDetailDatasetIds(){
    return new String[] { "userchoice_b_ds" };
  }
  public void beforeShow(  DialogEvent dialogEvent){
    CpWinUtil.getView().setExtendAttribute("viewStatus", "select");
		CpWinUtil.getView().setExtendAttribute("curSelectedRow", -1);
		
//		CmdInvoker.invoke(new UifDatasetLoadCmd(getMasterDatasetId()));
		CpWinUtil.setMenuItemsEnable(CpWinUtil.getView(), "menubar", new String[] {"add"}, true);
		CpWinUtil.setMenuItemsEnable(CpWinUtil.getView(), "menubar", new String[] {"edit", "del", "save", "ok"}, false);
		CpWinUtil.setMenuItemsVisible(CpWinUtil.getView(), "menubar", new String[] { "cancel"}, false);
		GridComp staffGrid = (GridComp) CpWinUtil.getComponent("userchoice_b_grid");
		for(MenuItem item : staffGrid.getMenuBar().getMenuList()) {
			item.setEnabled(false);
		}
//		staffGrid.getMenuBar().setEnabled(false);
  }
  public void onDataLoad(  DataLoadEvent dataLoadEvent){
    Dataset ds = CpWinUtil.getDataset(getMasterDatasetId());
	  String type =(String) AppUtil.getAppAttr("userChoiceType");
	  
	  StringBuffer condition = new StringBuffer();
	  condition.append(" type ='"+type+"'");
	  ScapUserChoiceVO[] scapUserChoiceVOs = (ScapUserChoiceVO[]) ScapDAO.retrieveByCondition(ScapUserChoiceVO.class, condition.toString());
	  if(scapUserChoiceVOs!=null&&scapUserChoiceVOs.length>0){
		  for(ScapUserChoiceVO scapUserChoiceVO:scapUserChoiceVOs){
			  Row row = userChoiceVOUtil.getRow(ds,scapUserChoiceVO);
			  ds.addRow(row);
		  }
	  }
	  if(ds.getRowCount() > 0) {
		  ds.setRowSelectIndex(0);
	  }
//    Dataset ds = dataLoadEvent.getSource();
//		CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
  }
  public void onAfterRowSelect(  DatasetEvent datasetEvent){
	    Dataset ds = datasetEvent.getSource();
		  String status = CpWinUtil.getView().getExtendAttributeValue("viewStatus").toString();
		  if(status.equals("edit")) {
			  Dataset bds = CpWinUtil.getDataset("userchoice_b_ds");
			  Row[] rows = bds.getAllChangedRows();
			  ScapUserChoiceBVO[] bvos = new ScapUserChoiceBVO[rows.length];
			  for(int n = 0; n < rows.length; n++) {
				  bvos[n] = new CpVOUtil<ScapUserChoiceBVO>().getVO(bds, rows[n], new ScapUserChoiceBVO());
			  }
			  ScapDAO.insertOrUpdateVOs(bvos);
		  }

			Row row = ds.getSelectedRow();
			String pk_choice_person = (String) row.getValue(ds.nameToIndex(ds.getPrimaryKeyField()));
			
			CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()));
			CpWinUtil.setMenuItemsEnable(CpWinUtil.getView(), "menubar", new String[] {"add", "edit", "del", "ok"}, true);
			CpWinUtil.setMenuItemsEnable(CpWinUtil.getView(), "menubar", new String[] {"save"}, false);
			GridComp staffGrid = (GridComp) CpWinUtil.getComponent("userchoice_b_grid");
			staffGrid.setShowImageBtn(true);
			
			StringBuffer condition = new StringBuffer();
			condition.append(" pk_choice_person = '"+pk_choice_person+"'");
			ScapUserChoiceBVO[] userChoiceBVOs = (ScapUserChoiceBVO[]) ScapDAO.retrieveByCondition(ScapUserChoiceBVO.class, condition.toString());
			Dataset userchoice_b_ds = CpWinUtil.getDataset("userchoice_b_ds");
			
			userchoice_b_ds.clear();
			if(userChoiceBVOs != null && userChoiceBVOs.length > 0){
				for(ScapUserChoiceBVO userChoiceBVO:userChoiceBVOs){
					userChoiceBVOUtil.getRowWithRefshData(userchoice_b_ds, userChoiceBVO);
//					userchoice_b_ds.addRow(row);
				}
			}
			staffGrid.getMenuBar().getItem("userchoice_b_grid$add").setEnabled(true);
	  }
  public void onUserGroupAdd(  MouseEvent mouseEvent){
    Dataset ds = CpWinUtil.getDataset(getMasterDatasetId());
		StringInputItem ucnameInput = new StringInputItem("ucname", "群组名称", true);
		StringInputItem ucmemoInput = new StringInputItem("ucmemo", "备注", false);
		
		AppInteractionUtil.showInputDialog("请输入群组名", new InputItem[] {ucnameInput, ucmemoInput});
		Map<String, String> result = AppInteractionUtil.getInputDialogResult();
		
	    Row row = ds.getEmptyRow();
	    row.setValue(ds.nameToIndex(ScapUserChoiceVO.NAME), result.get("ucname"));
	    String type =(String) AppUtil.getAppAttr("userChoiceType");
	    row.setValue(ds.nameToIndex(ScapUserChoiceVO.TYPE), type);
	    row.setValue(ds.nameToIndex(ScapUserChoiceVO.PK_USER), CntUtil.getCntUserPk());
	    row.setValue(ds.nameToIndex(ScapUserChoiceVO.REMARK), result.get("ucmemo") != null ? result.get("ucmemo") : "");
	    ds.addRow(row);
	    ScapUserChoiceVO vo = new CpVOUtil<ScapUserChoiceVO>().getVO(ds, row, new ScapUserChoiceVO());
	    String pkid = ScapDAO.insertOrUpdateVO(vo);
	    row.setValue(ds.nameToIndex(ScapUserChoiceVO.PK_CHOICE_PERSON), pkid);
	    ds.setRowSelectIndex(ds.getRowIndex(row));
//		CpWinUtil.getView().setExtendAttribute("viewStatus", "edit");
//		CpWinUtil.getView().setExtendAttribute("curSelectedRow", ds.getRowIndex(row));
  }
  public void onUserGroupEdit(  MouseEvent mouseEvent){
	    Dataset ds = CpWinUtil.getDataset(getMasterDatasetId());
		  Row row = ds.getSelectedRow();
		  ScapUserChoiceVO userChoiceVO = userChoiceVOUtil.getVO(ds, row, new ScapUserChoiceVO());
		  
		  StringInputItem ucnameInput = new StringInputItem("ucname", "群组名称", true);
		  ucnameInput.setValue(userChoiceVO.getName());
		  StringInputItem ucmemoInput = new StringInputItem("ucmemo", "备注", false);
		  ucmemoInput.setValue(userChoiceVO.getRemark());
			
		  AppInteractionUtil.showInputDialog("请输入群组名", new InputItem[] {ucnameInput, ucmemoInput});
		  Map<String, String> result = AppInteractionUtil.getInputDialogResult();
		  
		  	row.setValue(ds.nameToIndex(ScapUserChoiceVO.NAME), result.get("ucname"));
		    String type =(String) AppUtil.getAppAttr("userChoiceType");
		    row.setValue(ds.nameToIndex(ScapUserChoiceVO.TYPE), type);
		    row.setValue(ds.nameToIndex(ScapUserChoiceVO.PK_USER), CntUtil.getCntUserPk());
		    row.setValue(ds.nameToIndex(ScapUserChoiceVO.REMARK), result.get("ucmemo") != null ? result.get("ucmemo") : "");
		    ScapUserChoiceVO vo = new CpVOUtil<ScapUserChoiceVO>().getVO(ds, row, new ScapUserChoiceVO());
		    String pkid = ScapDAO.insertOrUpdateVO(vo);
		    row.setValue(ds.nameToIndex(ScapUserChoiceVO.PK_CHOICE_PERSON), pkid);
		    ds.setRowSelectIndex(ds.getRowIndex(row));
//			CpWinUtil.getView().setExtendAttribute("viewStatus", "edit");
	  }
  public void onUserGroupDel(  MouseEvent mouseEvent){
	    Dataset ds = null;
			// ScapUserchoiceVO
			ds = CpWinUtil.getDataset("userchoice_ds");
			
			if (ds.getSelectedIndex() < 0) {
				throw new LfwRuntimeException("请选中待删除数据");
			}
			Row row = ds.getSelectedRow();
			ds.removeRow(row);
			String pk_choice_person = (String) row.getValue(ds.nameToIndex(ds.getPrimaryKeyField()));
			
			StringBuffer condition = new StringBuffer();
			condition.append(" pk_choice_person ='"+pk_choice_person+"'");
			ScapUserChoiceBVO[] userChoiceVOs = (ScapUserChoiceBVO[]) ScapDAO.retrieveByCondition(ScapUserChoiceBVO.class, condition.toString());
			if(userChoiceVOs!=null&&userChoiceVOs.length>0){
				for(ScapUserChoiceBVO userChoiceVO:userChoiceVOs){
					ScapDAO.deleteByPk(ScapUserChoiceBVO.class, userChoiceVO.getPk_choice_person_b());
				}
			}
			ds = CpWinUtil.getDataset("userchoice_b_ds");
			ds.clear();
			
			ScapDAO.deleteByPk(ScapUserChoiceVO.class,pk_choice_person);
	  }
  public void onAfterRowSelect_B(  DatasetEvent datasetEvent){
    
  }
  public void onUserAdd(  MouseEvent mouseEvent){
    Dataset ds = CpWinUtil.getDataset(getMasterDatasetId());
	  Dataset bds = CpWinUtil.getDataset("userchoice_b_ds");
	  String pkid = ds.getSelectedRow().getString(ds.nameToIndex(ScapUserChoiceVO.PK_CHOICE_PERSON));
	  Row row = bds.getEmptyRow();
	  row.setValue(bds.nameToIndex(ScapUserChoiceBVO.PK_CHOICE_PERSON), pkid);
	  bds.addRow(row);
	  bds.setRowSelectIndex(bds.getRowIndex(row));
	  bds.setEnabled(true);
	  CpWinUtil.getView().setExtendAttribute("viewStatus", "edit");
	  CpWinUtil.setMenuItemsEnable(CpWinUtil.getView(), "menubar", new String[] { "save"}, true);
	  CpWinUtil.setMenuItemsEnable(CpWinUtil.getView(), "menubar", new String[] { "add", "edit", "del", "ok"}, false);
  }
  public void onSave(  MouseEvent mouseEvent){
    //		LinkedList<SuperVO> voList = new LinkedList<SuperVO>();

		// ScapUserchoiceVO
		Dataset ds = CpWinUtil.getDataset("userchoice_ds");
		String pk_choice_person = ds.getSelectedRow().getString(ds.nameToIndex(ScapUserChoiceVO.PK_CHOICE_PERSON));
//		ScapUserChoiceVO userChoiceVO = userChoiceVOUtil.getVO(ds, ds.getSelectedRow(), new ScapUserChoiceVO());
//		String pk_choice_person = ScapDAO.insertOrUpdateVO(userChoiceVO);
		// voList.add(userChoiceVOUtil.getVO(ds, ds.getSelectedRow(), new ScapUserChoiceVO()));
		// ScapUserchoiceBVO
		Dataset bds = CpWinUtil.getDataset("userchoice_b_ds");
		try {
			Row[] rows = bds.getAllChangedRows();
			ScapUserChoiceBVO[] vos = new ScapUserChoiceBVO[rows.length];
			for(int i=0;i<rows.length;i++){
				vos[i] = userChoiceBVOUtil.getVO(bds, rows[i], new ScapUserChoiceBVO());
				vos[i].setPk_choice_person(pk_choice_person);
			}
			boolean flag = ScapDAO.insertOrUpdateVOs(vos);
			if(flag) {
				CpWinUtil.getView().setExtendAttribute("viewStatus", "save");
			}
//			voList.addAll(Arrays.asList(vos));
		} catch (Exception e) {
		}

		// persist voList
//		ScapDAO.insertVOArray(voList.toArray(new SuperVO[0]));
		bds.setEnabled(false);
		CpWinUtil.setMenuItemsEnable(CpWinUtil.getView(), "menubar", new String[] {"add", "edit", "del", "ok"}, true);
		CpWinUtil.setMenuItemsEnable(CpWinUtil.getView(), "menubar", new String[] {"save"}, false);
  }
  public void onCancel(  MouseEvent mouseEvent){
    new UifCloseViewCmd("userChoice").execute();
  }
  public void onUserDel(  MouseEvent mouseEvent){
    Dataset userChoiceBSet = CpWinUtil.getDataset("userchoice_b_ds");
			Row row = userChoiceBSet.getSelectedRow();
			GridComp grid = (GridComp) mouseEvent.getSource();
			String dsId = grid.getDataset();
			CmdInvoker.invoke(new UifLineDelCmd(dsId));
			
			ScapUserChoiceBVO vo =  userChoiceBVOUtil.getVO(userChoiceBSet, row, new ScapUserChoiceBVO());
			if(vo!=null&&vo.getPk_choice_person_b()!=null){
				ScapDAO.deleteByPk(ScapUserChoiceBVO.class,vo.getPk_choice_person_b());
			}
  }
  public void onOkClick(  MouseEvent mouseEvent){
    Map<String, Object> paramMap = new HashMap<String, Object>();
		try {
			Dataset ds = CpWinUtil.getDataset("userchoice_b_ds");
			Row[] rows = ds.getAllChangedRows();
			ScapUserChoiceBVO[] vos = new ScapUserChoiceBVO[rows.length];
			for(int i=0;i<rows.length;i++){
				vos[i] = userChoiceBVOUtil.getVO(ds, rows[i], new ScapUserChoiceBVO());
			}
			
			paramMap.put(DO_OK_PLUGOUT_PARAM_NAME, vos);
		} catch (Exception e) {
		}
		
		new UifPlugoutCmd("userChoice", "doOk", paramMap).execute();
		new UifCloseViewCmd("userChoice").execute();
  }
  public void onAfterDataChange(  DatasetCellEvent datasetCellEvent){
	  if(datasetCellEvent.getColIndex() != 2) {
		  return;
	  }
	  Dataset ds = datasetCellEvent.getSource();
//	  if(datasetCellEvent.getRowIndex()==ds.getRowCount()){
//		  return;
//	  }
	  Row eventRow = ds.getCurrentRowData().getRow(datasetCellEvent.getRowIndex());
	  Row [] rows = ds.getAllRow();
	  String name = (String) eventRow.getValue(2);
	  int i = 0;
	  for(Row row:rows){
		  String rowName = (String) row.getValue(2);
		  if(name.equals(rowName)){
			  i++;
		  }
	  }
	  if(i>1){
		  ds.removeRow(eventRow);
		  AppInteractionUtil.showMessageDialog("已经存在相同用户");
	  }else {
		  String pkOrg = eventRow.getString(ds.nameToIndex(ScapUserChoiceBVO.REMARK));
		  CpOrgVO org = (CpOrgVO) ScapDAO.retrieveByPK(CpOrgVO.class, pkOrg);
		  if(org != null) {
			  eventRow.setValue(ds.nameToIndex(ScapUserChoiceBVO.REMARK), org.getName());
		  }
	  }
  }
}
