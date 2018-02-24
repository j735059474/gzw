package com.scap.pub.defineLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import nc.scap.pub.util.CpChartUtil;
import nc.scap.pub.util.CpStrUtil;
import nc.scap.pub.util.CpVOUtil;
import nc.scap.pub.util.CpWinUtil;
import nc.scap.pub.util.ScapDAO;
import nc.scap.pub.vos.ScapLayoutContainerVO;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.constants.AppConsts;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DatasetCellEvent;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.portal.log.ScapLogger;
import uap.web.bd.pub.AppUtil;
public class ContainerViewController {
  private static final long serialVersionUID=1L;
  private static final long ID=5L;
  /** 
 * 数据准备
 * @author taoye 2013-8-26
 */
  public void beforeShow(  DialogEvent dialogEvent){
    //判断状态是新增还是修改
	  String ope = "";
	  if(AppUtil.getAppAttr(AppConsts.OPE_SIGN) != null) {
		   ope = AppUtil.getAppAttr(AppConsts.OPE_SIGN).toString();
	  }
	  
	  //获取数据集
	  Dataset layout = CpWinUtil.getDataset(CpWinUtil.getView("main"), "scap_layout");
	  Dataset container = CpWinUtil.getDataset(CpWinUtil.getView("main"), "scap_layout_container");
	  Dataset ds = CpWinUtil.getDataset(CpWinUtil.getView("container"), "container_ds");
	  ds.clear();
	  Row row = ds.getEmptyRow();
	  
	  if(ope.equals(AppConsts.OPE_ADD)) {
		  //准备数据
		  String pkParent = CpChartUtil.getParentContainer();
		  String pkLayout = layout.getSelectedRow().getValue(layout.nameToIndex("pk_layout")).toString();
		  row.setValue(ds.nameToIndex("parent_container"), pkParent);
		  row.setValue(ds.nameToIndex("layout_container"), pkLayout);
		  
		  //控制容器类型
		  String container_type = CpChartUtil.getChildContainerType(pkParent);
		  FormComp form = (FormComp) CpWinUtil.getView("container").getViewComponents().getComponent("container_form");
		  if(container_type != null) {
			  row.setValue(ds.nameToIndex("container_type"), container_type);
			  form.getElementById("container_type").setEnabled(false);
		  }
	  
		  ds.addRow(row);
		  
	  }else if(ope.equals(AppConsts.OPE_EDIT)) {
		  ScapLayoutContainerVO vo = (ScapLayoutContainerVO) ScapDAO.retrieveByPK(ScapLayoutContainerVO.class, 
				  container.getSelectedRow().getValue(container.nameToIndex("pk_layout_container")).toString());
		  row = new CpVOUtil<ScapLayoutContainerVO>().getRowWithRefshData(ds, vo);
		  FormComp form = (FormComp) CpWinUtil.getView("container").getViewComponents().getComponent("container_form");
		  form.getElementById("container_type").setEnabled(false);
		  
	  }else {
		  ScapLogger.error("===error:ContainerViewController.beforeShow 41行 未获取到当前操作状态，关闭窗口！");
		    AppLifeCycleContext.current().getApplicationContext().closeWinDialog();//未获取到当前操作状态，关闭窗口
	  }
	  
	  ds.setRowSelectIndex(ds.getRowIndex(row));
	  ds.setEnabled(true);
	  
	  setBtnStatusByContainerType();//设置页面状态
  }
  /** 
 * 保存
 * @author taoye 2013-8-26
 */
  public void onSave(  MouseEvent mouseEvent){
	  //判断状态是新增还是修改
	  String ope = "";
	  if(AppUtil.getAppAttr(AppConsts.OPE_SIGN) != null) {
		   ope = AppUtil.getAppAttr(AppConsts.OPE_SIGN).toString();
	  }
	  Dataset layout = CpWinUtil.getDataset(CpWinUtil.getView("main"), "scap_layout");
	  Dataset ds = CpWinUtil.getDataset(CpWinUtil.getView("container"), "container_ds");
	  Dataset container = CpWinUtil.getDataset(CpWinUtil.getView("main"), "scap_layout_container");
	  Row row = ds.getSelectedRow();
	  String pkParent = row.getValue(container.nameToIndex("parent_container")).toString();
	  String pkLayout = layout.getSelectedRow().getValue(layout.nameToIndex("pk_layout")).toString();
	  String type = CpStrUtil.blankStr(row.getValue(container.nameToIndex(ScapLayoutContainerVO.CONTAINER_TYPE)));
	  if(!pkParent.equals(pkLayout)) {
		  if(type != null && (type.equals(CpChartUtil.CON_TYPE_TAB) || type.equals(CpChartUtil.CON_TYPE_POP))) {
			  throw new LfwRuntimeException("只有布局下面可以建立页签容器和弹出容器！");
		  }
	  }
	  Row r = container.getSelectedRow();
	  if(r != null) {
		  String rtype = CpStrUtil.blankStr(r.getValue(container.nameToIndex(ScapLayoutContainerVO.CONTAINER_TYPE)));
		  if(ope.equals(AppConsts.OPE_ADD) && rtype.equals(CpChartUtil.CON_TYPE_POP) && !type.equals(CpChartUtil.CON_TYPE_CHART)) {
			  throw new LfwRuntimeException("弹出容器下只能新建图元容器！");
		  }
	  }

	  ScapLayoutContainerVO vo = new CpVOUtil<ScapLayoutContainerVO>().getVO(ds, ds.getSelectedRow(), new ScapLayoutContainerVO());
	  String pkValue = ScapDAO.insertOrUpdateVO(vo);
	  
	  if(pkValue != null) {
		  //关闭对话框
		  AppLifeCycleContext.current().getApplicationContext().closeWinDialog();
		  
		  if(ope.equals(AppConsts.OPE_ADD)) {
			  new CpVOUtil<ScapLayoutContainerVO>().getRowWithRefshData(container, vo);
		  }else {
			  CpWinUtil.RowData2Row(container, container.getSelectedRow(), ds, ds.getSelectedRow());
		  }
		  
	  }else {
		  throw new LfwRuntimeException("保存失败");
	  }
  }
  /** 
 * 返回
 * @author taoye 2013-8-26
 */
  public void onBack(  MouseEvent mouseEvent){
    AppLifeCycleContext.current().getApplicationContext().closeWinDialog();
  }
  public void onAfterDataChange(  DatasetCellEvent datasetCellEvent){
    Dataset ds = CpWinUtil.getDataset(CpWinUtil.getView("container"), "container_ds");
	  if(datasetCellEvent.getColIndex() == ds.nameToIndex("container_type")) {
		  setBtnStatusByContainerType();
	  }
  }
  private void setBtnStatusByContainerType(){
    FormComp form = (FormComp) CpWinUtil.getView("container").getViewComponents().getComponent("container_form");
//	  Dataset container = CpWinUtil.getDataset(CpWinUtil.getView("main"), "scap_layout_container");
	  Dataset ds = CpWinUtil.getDataset(CpWinUtil.getView("container"), "container_ds");
	  String type = "";
	  if(ds.getSelectedRow().getValue(ds.nameToIndex("container_type")) != null) {
		  type = ds.getSelectedRow().getValue(ds.nameToIndex("container_type")).toString();
	  }
	  List<FormElement> elelist = form.getElementList();
	  String showEles = "";
	  String hideEles = "";
	  String randomStr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
//	  String parentWidht = "990";
//	  String parentHeight = "800";
//	  if(container.getSelectedRow() != null) {
//		  if(container.getSelectedRow().getValue(container.nameToIndex(ScapLayoutContainerVO.WIDTH)) != null) {
//			  	parentWidht = container.getSelectedRow().getValue(container.nameToIndex(ScapLayoutContainerVO.WIDTH)).toString();
//		  }
//		  if(container.getSelectedRow().getValue(container.nameToIndex(ScapLayoutContainerVO.HEIGHT)) != null) {
//			  	parentHeight = container.getSelectedRow().getValue(container.nameToIndex(ScapLayoutContainerVO.WIDTH)).toString();
//		  }
//	  }
	  if(type.equals("0")) {
		  showEles = "title";
		  hideEles = "height,width,display_type,pk_chart_name,isdrill,report_url";
		  if(ds.getValue(ds.nameToIndex(ScapLayoutContainerVO.CODE)) == null) {
			  ds.setValue(ds.nameToIndex(ScapLayoutContainerVO.CODE), "tab" + randomStr);
		  }
//		  ds.setValue(ds.nameToIndex(ScapLayoutContainerVO.WIDTH), parentWidht);
//		  ds.setValue(ds.nameToIndex(ScapLayoutContainerVO.HEIGHT), parentHeight);
	  }else if(type.equals("1")) {
		  showEles = "title,height";
		  hideEles = "width,display_type,pk_chart_name,isdrill,report_url";
		  if(ds.getValue(ds.nameToIndex(ScapLayoutContainerVO.CODE)) == null) {
			  ds.setValue(ds.nameToIndex(ScapLayoutContainerVO.CODE), "row" + randomStr);
		  }
//		  ds.setValue(ds.nameToIndex(ScapLayoutContainerVO.WIDTH), parentWidht);
	  }else if(type.equals("2")) {
		  showEles = "title,width";
		  hideEles = "height,display_type,pk_chart_name,isdrill,report_url";
		  if(ds.getValue(ds.nameToIndex(ScapLayoutContainerVO.CODE)) == null) {
			  ds.setValue(ds.nameToIndex(ScapLayoutContainerVO.CODE), "col" + randomStr);
		  }
//		  ds.setValue(ds.nameToIndex(ScapLayoutContainerVO.HEIGHT), parentHeight);
	  }else if(type.equals("3")) {
		  showEles = "title,display_type,pk_chart_name,isdrill,report_url";
		  hideEles = "width,height";
		  if(ds.getValue(ds.nameToIndex(ScapLayoutContainerVO.CODE)) == null) {
			  ds.setValue(ds.nameToIndex(ScapLayoutContainerVO.CODE), "ty" + randomStr);
		  }
	  }else if(type.equals("5")) {
		  showEles = "title,width,height,report_url";
		  hideEles = "display_type,pk_chart_name,isdrill";
		  if(ds.getValue(ds.nameToIndex(ScapLayoutContainerVO.CODE)) == null) {
			  ds.setValue(ds.nameToIndex(ScapLayoutContainerVO.CODE), "ty" + randomStr);
		  }
//		  ds.setValue(ds.nameToIndex(ScapLayoutContainerVO.WIDTH), parentWidht);
//		  ds.setValue(ds.nameToIndex(ScapLayoutContainerVO.HEIGHT), parentHeight);
	  }else {
		  hideEles = "title,height,width,display_type,pk_chart_name,isdrill,report_url";
	  }
	  for(FormElement ele : elelist) {
		  if(showEles != null && !showEles.equals("")) {
			  String[] showEleArr = showEles.split(",");
			  for(int i = 0; i < showEleArr.length; i++) {
				  if(showEleArr[i].equals(ele.getId())) {
					  ele.setEnabled(true);
//					  ele.setVisible(true);
					  break;
				  }
			  }
		  }
		  if(hideEles != null && !hideEles.equals("")) {
			  String[] hideEleArr = hideEles.split(",");
			  for(int i = 0; i < hideEleArr.length; i++) {
				  if(hideEleArr[i].equals(ele.getId())) {
					  ele.setEnabled(false);
//					  ele.setVisible(false);
					  ds.getSelectedRow().setValue(ds.nameToIndex(hideEleArr[i]), "");
					  break;
				  }
			  }
		  }
	  }
  }
}
