package com.scap.pub.defdoc;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.base.ExtAttribute;
import java.util.Map;
import nc.uap.cpb.org.exception.CpbBusinessException;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.vo.pub.lang.UFDateTime;
import nc.uap.lfw.core.serializer.impl.Dataset2SuperVOSerializer;
import nc.uap.lfw.jsp.uimeta.UICardLayout;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.serializer.impl.Datasets2RichVOSerializer;
import nc.uap.lfw.core.event.DatasetCellEvent;
import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.cpb.org.querycmd.QueryCmd;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.cmd.base.FromWhereSQL;
import nc.uap.lfw.core.data.Dataset;
import nc.vo.bd.defdoc.DefdoclistVO;
import nc.scap.pub.itf.IGlobalConstants;
import nc.vo.bd.defdoc.DefdocVO;
import nc.bs.framework.common.NCLocator;
import nc.scap.pub.util.ScapDAO;
import nc.vo.pub.SuperVO;
import nc.uap.lfw.core.AppInteractionUtil;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.cpb.baseservice.IUifCpbService;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.constants.AppConsts;
import uap.web.bd.pub.AppUtil;
import nc.scap.pub.util.CntUtil;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.scap.def.util.ScapDefUtil;
import nc.uap.portal.log.ScapLogger;
import nc.scap.pub.util.CpWinUtil;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.comp.MenubarComp;
/** 
 * 信息列表默认逻辑
 */
public class DefdocListWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
  private static final long serialVersionUID=-1;
  private static final String MAIN_VIEW_ID="main";
  private static final String CARD_WIN_ID="com.scap.pub.defdoc.defdoc_cardwin";
  private static final String CARD_WIN_TITLE="编辑";
  public void beforeShow(  DialogEvent dialogEvent){
    Dataset ds = getDefdocListDs();
    Row row = ds.getEmptyRow();
    ds.addRow(row);
    ds.setRowSelectIndex(ds.getRowIndex(row));
	String typeCode = LfwRuntimeEnvironment.getWebContext().getOriginalParameter("typeCode");
	if(isdefdoclistSelected(typeCode)) {
	    refreshDefDoc();
	}else {
	    setMenuBarEnable(false);
	    getCardLayout().setCurrentItem("0");
	    ds.setEnabled(true);
	}
  }
  public void onAfterTypeSelect(  DatasetCellEvent datasetCellEvent){
    refreshDefDoc();
  }
  /** 
 * 主数据选中逻辑
 * @param datasetEvent
 */
  public void onAfterRowSelect(  DatasetEvent datasetEvent){
    Dataset ds = datasetEvent.getSource();
  }
  /** 
 * 新增
 * @param mouseEvent
 */
  public void onAdd(  MouseEvent mouseEvent){
    Dataset defdoclist = getDefdocListDs();
	Dataset defdoc = getDefdocDs();
	Row selectedRow = defdoc.getSelectedRow();
    Row row = defdoc.getEmptyRow();
    defdoc.addRow(row);
    row.setValue(defdoc.nameToIndex("pk_defdoclist"), defdoclist.getSelectedRow().getValue(defdoclist.nameToIndex("code")));
    row.setValue(defdoc.nameToIndex("pk_group"), CntUtil.getCntGroupPk());
    row.setValue(defdoc.nameToIndex("pk_org"), CntUtil.getCntOrgPk());
    row.setValue(defdoc.nameToIndex(DefdocVO.CREATOR), CntUtil.getCntUserPk());
    row.setValue(defdoc.nameToIndex(DefdocVO.CREATIONTIME), new UFDateTime());
    row.setValue(defdoc.nameToIndex(DefdocVO.MODIFIER), CntUtil.getCntUserPk());
    row.setValue(defdoc.nameToIndex(DefdocVO.MODIFIEDTIME), new UFDateTime());
    row.setValue(defdoc.nameToIndex(DefdocVO.ENABLESTATE), "2");
    row.setValue(defdoc.nameToIndex("status"), "2");
    if(selectedRow != null) {
    	row.setValue(defdoc.nameToIndex("pid"), selectedRow.getValue(defdoc.nameToIndex("pk_defdoc")));
    	row.setValue(defdoc.nameToIndex("pid_name"), selectedRow.getValue(defdoc.nameToIndex("name")));
    }
    defdoc.setRowSelectIndex(defdoc.getRowIndex(row));
	defdoclist.setEnabled(false);
    defdoc.setEnabled(true);
    setMenuBarEnable(true);
    setMenuItemEnable("edit", true);
  }
  /** 
 * 编辑
 * @param scriptEvent
 */
  public void onEdit(  MouseEvent mouseEvent){
    Dataset defdoclist = getDefdocListDs();
	Dataset defdoc = getDefdocDs();
	defdoclist.setEnabled(false);
	defdoc.setEnabled(true);
    setMenuBarEnable(true);
    setMenuItemEnable("edit", false);
  }
  /** 
 * 删除
 * @param scriptEvent
 */
  public void onDelete(  MouseEvent mouseEvent){
    Dataset defdoc = getDefdocDs();
    if (defdoc.getSelectedIndex() < 0)
	    throw new LfwRuntimeException("请选中行！");
    Row row = defdoc.getSelectedRow();
//	Datasets2RichVOSerializer ser = new Datasets2RichVOSerializer();
//	SuperVO vo = ser.serialize(defdoc, row, null);
//	IUifCpbService cpbService = NCLocator.getInstance().lookup(IUifCpbService.class);
	String pkid = row.getString(defdoc.nameToIndex(defdoc.getPrimaryKeyField()));
	try {
		if(pkid != null && !pkid.equals("")) {
			ScapDAO.deleteByPk(DefdocVO.class, pkid);
		}
//		cpbService.deleteSuperVO(vo, false, true);
		defdoc.removeRow(row);
	} catch (Exception e) {
		ScapLogger.error("删除失败！删除自定义档案" + (pkid == null ? "" : pkid) + "失败", e);
		e.printStackTrace();
	}
  }
  public void onCancel(  MouseEvent mouseEvent){
    refreshDefDoc();
  }
  public void onSave(  MouseEvent mouseEvent){
    Dataset defdoc = getDefdocDs();
//	Row[] rows = defdoc.getAllRow();
//	for(Row r : rows) {
//		if(r.getValue(defdoc.nameToIndex("status")).toString().equals("2")) {
//			String code = r.getValue(defdoc.nameToIndex("code")).toString();
//			if(ScapDefUtil.getDefDocVoByCode(code) != null) {
//				throw new LfwRuntimeException("编码" + code + "重复");
//			}
//		}
//	}
	Row row = defdoc.getSelectedRow();
	Object pkid = row.getValue(defdoc.nameToIndex(DefdocVO.PK_DEFDOC));
	if(row.getValue(defdoc.nameToIndex("status")).toString().equals("2") && (pkid == null || pkid.toString().equals("")) ) {
		String code = row.getValue(defdoc.nameToIndex(DefdocVO.CODE)).toString();
		String pkDefdoclist = row.getString(defdoc.nameToIndex(DefdocVO.PK_DEFDOCLIST));
		String condition = "code = '" + code + "' and pk_defdoclist = '" + pkDefdoclist + "'";
		DefdocVO[] vos = (DefdocVO[]) ScapDAO.retrieveByCondition(DefdocVO.class, condition);
		if(vos != null) {
			throw new LfwRuntimeException("编码" + code + "重复");
		}
	}
	Row[] rows = {row};
	Dataset2SuperVOSerializer<SuperVO> serializer = new Dataset2SuperVOSerializer<SuperVO>();
	IUifCpbService cpbService = NCLocator.getInstance().lookup(IUifCpbService.class);
	SuperVO[] vos = serializer.serialize(defdoc, rows);
	if(vos == null || vos.length < 1) {
		return;
	}
	for(SuperVO vo : vos) { 
		try {
			cpbService.insertOrUpdateSuperVO(vo,false);
			AppInteractionUtil.showShortMessage("保存成功！");
		} catch (Exception e) {
			ScapLogger.error("保存失败！数据集" + defdoc.getId() + "保存失败" , e);
			e.printStackTrace();
		}
	}
	defdoc.setEnabled(false);
	setMenuBarEnable(false);
	setMenuItemEnable("add", true);
	setMenuItemEnable("edit", true);
    setMenuItemEnable("del", true);
//	refreshDefDoc();
  }
  /** 
 * 外部触发刷新
 * @param keys
 */
  public void doRefresh(  Map<?,?> keys){
    Row savedRow = (Row) keys.get(OPERATE_ROW);
			if (savedRow != null) {
				Dataset ds = this.getMasterDs();
				savedRow.setRowId(ds.getEmptyRow().getRowId());
				String sign = this.getOperator();
				if (AppConsts.OPE_EDIT.equals(sign)) {
					int index = ds.getRowIndex(ds.getSelectedRow());
					if (index >= 0) {
						ds.removeRow(index);
						ds.insertRow(index, savedRow);
					}
				} else if (AppConsts.OPE_ADD.equals(sign)) {
					ds.addRow(savedRow);
				}
			}
  }
  /** 
 * 主组织变化
 * @param keys
 */
  public void doOrgChange(  Map<?,?> keys){
    CmdInvoker.invoke(new UifDatasetLoadCmd(this.getMasterDsId()) {
				protected String postProcessQueryVO(SuperVO vo, Dataset ds) {
					String where = AppUtil.getGroupOrgFielterSqlForDesign(false, PK_ORG);
					ds.setLastCondition(where);
					return where;
				}
	
				// 避免翻页时重走缓存
				protected void changeCurrentKey() {
					setChangeCurrentKey(true);
				}
			});
			this.clearDetailDs();
  }
  /** 
 * 查询plugin
 * @param keys
 */
  public void doQueryChange(  Map<?,?> keys){
    FromWhereSQL whereSql = (FromWhereSQL) keys.get(FromWhereSQL.WHERE_SQL_CONST);
			CmdInvoker.invoke(new QueryCmd(MAIN_VIEW_ID, this.getMasterDsId(), whereSql.getWhere()));
  }
  private void clearDetailDs(){
    Dataset[] detailDss = this.getDetailDs(this.getDetailDsIds());
		int len = detailDss != null ? detailDss.length : 0;
		for (int i = 0; i < len; i++) {
			if (detailDss[i] != null) {
				detailDss[i].clear();
			}
		}
  }
  @Override protected String getMasterDsId(){
    return "defdoc";
  }
  public void refreshDefDoc(){
    Dataset defdoclist = getDefdocListDs();
		String code = (String) defdoclist.getValue(defdoclist.nameToIndex("code"));
		if(code == null) {
	    	getCardLayout().setCurrentItem("0");
			return;
		}
		DefdoclistVO type = ScapDefUtil.getDefdoclistVoByPk(code);
	    Dataset defdoc = getDefdocDs();
	    int selectIndex = -1;
	    if(defdoc.getSelectedRow() != null) {
	    	selectIndex = defdoc.getSelectedIndex();
	    }
	    if(type.getIsgrade().booleanValue())
	    	getCardLayout().setCurrentItem("2");
	    else
	    	getCardLayout().setCurrentItem("1");
		defdoc.setLastCondition(" pk_defdoclist = '" + code + "' ");
		CmdInvoker.invoke(new UifDatasetLoadCmd(defdoc.getId()));
		if(selectIndex > -1) {
			if(defdoc.getAllRow().length > selectIndex) {
				defdoc.setRowSelectIndex(selectIndex);
			}
		}
		if(!getIsDefdoclistSelected()) {
			defdoclist.setEnabled(true);
		}
		setMenuBarEnable(false);
		setMenuItemEnable("add", true);
		setMenuItemEnable("edit", true);
	    setMenuItemEnable("del", true);
  }
  private Dataset getDefdocListDs(){
    return CpWinUtil.getDataset(CpWinUtil.getView(MAIN_VIEW_ID), "defdoclist_ds");
  }
  private Dataset getDefdocDs(){
    return CpWinUtil.getDataset(CpWinUtil.getView(MAIN_VIEW_ID), "defdoc");
  }
  private void setMenuItemEnable(  String itemId,  boolean enable){
    MenubarComp menubar = CpWinUtil.getView(MAIN_VIEW_ID).getViewMenus().getMenuBar("menubar");
	  menubar.getItem(itemId).setEnabled(enable);
  }
  private void setMenuBarEnable(  boolean enable){
    MenubarComp menubar = CpWinUtil.getView(MAIN_VIEW_ID).getViewMenus().getMenuBar("menubar");
	  for(MenuItem item : menubar.getMenuList()) {
		  item.setEnabled(enable);
	  }
  }
  private UICardLayout getCardLayout(){
    return (UICardLayout) CpWinUtil.getWinCtx().getViewContext(MAIN_VIEW_ID).getUIMeta().findChildById("defdoc_card");
  }
  /** 
 * 判断是否是固定自定义档案类型的维护
 * @author taoye 2013-8-8
 */
  private boolean isdefdoclistSelected(  String typeCode){
    if(typeCode == null || typeCode.equals("")) {
			CpWinUtil.getView(MAIN_VIEW_ID).addAttribute(new ExtAttribute(IGlobalConstants.IS_DEFDOCLIST_SELECTED, "false"));
			return false;
		}
	    Dataset defdoclist = getDefdocListDs();
		DefdoclistVO vo = ScapDefUtil.getDefDocListVosByCode(typeCode);
		Row row = defdoclist.getSelectedRow();
		row.setValue(defdoclist.nameToIndex("code"), vo.getPk_defdoclist());
		row.setValue(defdoclist.nameToIndex("name"), vo.getName());
		CpWinUtil.getView(MAIN_VIEW_ID).addAttribute(new ExtAttribute(IGlobalConstants.IS_DEFDOCLIST_SELECTED, "true"));
		return true;
  }
  /** 
 * 判断是否是固定自定义档案类型的维护
 * @author taoye 2013-8-8
 */
  private boolean getIsDefdoclistSelected(){
    String flagStr = (String) CpWinUtil.getView(MAIN_VIEW_ID).getExtendAttribute(IGlobalConstants.IS_DEFDOCLIST_SELECTED).getValue();
	  return Boolean.valueOf(flagStr);
  }
}
