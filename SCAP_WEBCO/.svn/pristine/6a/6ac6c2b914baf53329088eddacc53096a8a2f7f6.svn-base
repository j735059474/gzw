package nc.scap.pub.work_type;
import nc.uap.lfw.core.cmd.UifAddCmd;
import nc.uap.lfw.core.cmd.CmdInvoker;
import java.util.Map;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.event.DatasetCellEvent;
import nc.vo.scapco.work_notice_manager.Notice_manager;
import nc.uap.lfw.core.cmd.UifCopyCmd;
import java.util.HashMap;
import nc.vo.scap.work_report_type.Work_report_type;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.cmd.UifPlugoutCmd;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import nc.vo.scapco.pub_visualorganization.visualOrganization;
import nc.uap.lfw.core.cmd.UifEnableCmd;
import nc.uap.lfw.core.ctx.OpenProperties;
import nc.uap.lfw.core.cmd.UifLineDelCmd;
import nc.uap.cpb.org.orgs.CpOrgVO;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.data.Dataset;
import nc.scap.pub.notice.util.NoticeUtil;
import java.util.ArrayList;
import nc.uap.lfw.core.cmd.UifSaveCmdRV;
import nc.uap.ctrl.tpl.print.init.DefaultPrintService;
import nc.scap.pub.itf.IGlobalConstants;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.vo.scapco.work_report_type.receive_org;
import java.util.List;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.cmd.UifDelCmdRV;
import nc.uap.lfw.core.file.FillFileInfoHelper;
import nc.uap.ctrl.tpl.print.ICpPrintTemplateService;
import nc.uap.lfw.core.comp.GridComp;
import nc.vo.pub.BusinessException;
import nc.uap.lfw.core.cmd.UifAttachCmd;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.constants.AppConsts;
import uap.web.bd.pub.AppUtil;
import nc.uap.portal.log.ScapLogger;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.scap.pub.util.CpWinUtil;
import uap.lfw.core.locator.ServiceLocator;
import org.apache.commons.lang.StringUtils;
import nc.uap.lfw.core.event.MouseEvent;
/** 
 * 卡片窗口默认逻辑
 */
public class Work_typeCardWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
  private static final long serialVersionUID=-1;
  private static final String PARAM_BILLITEM="billitem";
  private static final String PLUGOUT_ID="afterSavePlugout";
  /** 
 * 页面显示事件
 * @param dialogEvent
 */
  public void beforeShow(  DialogEvent dialogEvent){
    Dataset masterDs = this.getMasterDs();
		masterDs.clear();
	
		String oper = getOperator();
		if(AppConsts.OPE_ADD.equals(oper)){
			CmdInvoker.invoke(new UifAddCmd(getMasterDsId()) {
				protected void onBeforeRowAdd(Row row) {
					setAutoFillValue(row);
					
					Dataset masterDs = getMasterDs();
					String pk_primarykey = generatePk();
					row.setValue(masterDs.nameToIndex(masterDs.getPrimaryKeyField()), pk_primarykey);
					// 报送方式
					row.setValue(masterDs.nameToIndex(Work_report_type.REPORT_WAY),"1");
					// 报告格式
					row.setValue(masterDs.nameToIndex(Work_report_type.REPORT_PATTERN),"1");
					// 报送频率
					row.setValue(masterDs.nameToIndex(Work_report_type.ATTRNAME),"3");
					// 报告主体
					row.setValue(masterDs.nameToIndex(Work_report_type.REPORT_BODY),"1");
					// 是否关联通知
					row.setValue(masterDs.nameToIndex(Work_report_type.IS_ASS_NOTI),"0");
					// 是否自动催报
					row.setValue(masterDs.nameToIndex(Work_report_type.IS_AUTO_URGE),"0");
					// 催报信息发送方式
					row.setValue(masterDs.nameToIndex(Work_report_type.URGE_INFO_TRANS_WAY),"1");
					// 催报频率
					row.setValue(masterDs.nameToIndex(Work_report_type.URGE_FREQUENCY),"1");
					// 报送统计内容格式（国资委用户）
					row.setValue(masterDs.nameToIndex(Work_report_type.URGE_CONTENT_GZW),IGlobalConstants.CONTENT_URGE_CONTENT_GZW);
					// 催报消息内容格式（企业用户）
					row.setValue(masterDs.nameToIndex(Work_report_type.URGE_CONTENT_QY),IGlobalConstants.CONTENT_URGE_CONTENT_QY);

					FillFileInfoHelper.resetItem(pk_primarykey);
					FillFileInfoHelper.fillFileInfo(masterDs, row);
				}
			});
		}else if(AppConsts.OPE_EDIT.equals(oper)){
			CmdInvoker.invoke(new UifDatasetLoadCmd(masterDs){
				@Override
				protected void onAfterDatasetLoad() {
					this.getDs().setEnabled(true);
					
					String primaryKey = this.getDs().getPrimaryKeyField();
					if (primaryKey == null) {
						throw new LfwRuntimeException("当前Dataset没有设置主键!");
					}
					String primaryKeyValue = (String) this.getDs().getSelectedRow().getValue(this.getDs().nameToIndex(primaryKey));					
					uap.web.bd.pub.AppUtil.addAppAttr("pk_business_type", (String)this.getDs().getSelectedRow().getValue(this.getDs().nameToIndex(Work_report_type.YE_TYPE)));
					FillFileInfoHelper.resetItem(primaryKeyValue);
				}
			});
		}
  }
  /** 
 * 主数据选中逻辑
 * @param datasetEvent
 */
  public void onAfterRowSelect(  DatasetEvent dsEvent){
    Dataset masterDs = dsEvent.getSource();

		  String report_way = masterDs.getValue(masterDs.nameToIndex(Work_report_type.REPORT_WAY))==null?"":
	  		   masterDs.getValue(masterDs.nameToIndex(Work_report_type.REPORT_WAY)).toString();
		LfwView view = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		FormComp form = (FormComp) view
				.getComponentsByDataset("work_report_type")[0];
		  // 1:定期；2：不定期
		  if ("1".equals(report_way)) {
				// 【催报相关】
				FormElement urge_related = form.getElementById("urge_related"); // 【催报相关】
				urge_related.setVisible(true);
				// 是否自动催报
				FormElement is_auto_urge = form.getElementById("is_auto_urge"); // 是否自动催报
				is_auto_urge.setVisible(true);
				// 催报信息发送方式
				FormElement urge_info_trans_way = form
						.getElementById("urge_info_trans_way"); // 催报信息发送方式
				urge_info_trans_way.setVisible(true);
				// 提前催报天数
				FormElement day_num = form.getElementById("urge_time"); // 提前催报天数
				day_num.setVisible(true);
				// 催报频率
				FormElement urge_frequency = form
						.getElementById("urge_frequency"); // 催报频率
				urge_frequency.setVisible(true);

				// 报送统计内容格式（国资委用户）
				FormElement urge_content_gzw = form
						.getElementById("urge_content_gzw"); // 报送统计内容格式（国资委用户）
				urge_content_gzw.setVisible(true);

				// 催报消息内容格式（企业用户）
				FormElement urge_content_qy = form
						.getElementById("urge_content_qy"); // 催报消息内容格式（企业用户）
				urge_content_qy.setVisible(true);
				// 报告截止日期
				FormElement expiration_date = form
						.getElementById("expiration_date"); // 报告截止日期
				expiration_date.setVisible(true);
				expiration_date.setNullAble(false);
				// 报送频率
				FormElement attrname = form
						.getElementById("attrname"); // 报送频率
				attrname.setVisible(true);
		  } else if ("2".equals(report_way)) {
				// 【催报相关】
				FormElement urge_related = form.getElementById("urge_related"); // 【催报相关】
				urge_related.setVisible(false);
				// 是否自动催报
				FormElement is_auto_urge = form.getElementById("is_auto_urge"); // 是否自动催报
				is_auto_urge.setVisible(false);
				// 催报信息发送方式
				FormElement urge_info_trans_way = form
						.getElementById("urge_info_trans_way"); // 催报信息发送方式
				urge_info_trans_way.setVisible(false);
				// 提前催报天数
				FormElement day_num = form.getElementById("urge_time"); // 提前催报天数
				day_num.setVisible(false);
				// 催报频率
				FormElement urge_frequency = form
						.getElementById("urge_frequency"); // 催报频率
				urge_frequency.setVisible(false);

				// 报送统计内容格式（国资委用户）
				FormElement urge_content_gzw = form
						.getElementById("urge_content_gzw"); // 报送统计内容格式（国资委用户）
				urge_content_gzw.setVisible(false);

				// 催报消息内容格式（企业用户）
				FormElement urge_content_qy = form
						.getElementById("urge_content_qy"); // 催报消息内容格式（企业用户）
				urge_content_qy.setVisible(false);
				// 报告截止日期
				FormElement expiration_date = form
						.getElementById("expiration_date"); // 报告截止日期
				expiration_date.setVisible(false);
				expiration_date.setNullAble(true);
				// 报送频率
				FormElement attrname = form
						.getElementById("attrname"); // 报送频率
				attrname.setVisible(false);
		  }
		CmdInvoker.invoke(new UifDatasetAfterSelectCmd(masterDs.getId()));
        CpWinUtil
        .refshAttachList("main", "attachout",
        		masterDs.getValue(masterDs.getPrimaryKeyField())
                                        .toString(), true, true, true);
  }
  /** 
 * 保存
 * @param datasetEvent
 */
  public void onSave(  MouseEvent<?> mouseEvent) throws BusinessException {
    Dataset masterDs = this.getMasterDs();
    	// 保存前做check
    	saveCheck();
		CmdInvoker.invoke(new UifSaveCmdRV(this.getMasterDsId(), this.getDetailDsIds(), false));
		masterDs.setEnabled(true);
		this.getCurrentAppCtx().closeWinDialog();
		
		CmdInvoker.invoke(new UifPlugoutCmd(this.getCurrentView().getId(), PLUGOUT_ID));
  }
  /** 
 * 报告类型数据保存时候的check
 * 1：是否该业务类型已经存在使用这个编码的报告类型，如果存在提示不能保存
 * 2：报告截至时间格式check
 */
  private void saveCheck(){
    Dataset masterDs = this.getMasterDs();

		Row rowm = masterDs.getSelectedRow();
		String oper = getOperator();
		if(AppConsts.OPE_ADD.equals(oper)){
			// 是否已经存在该报告类型check
			String businessType = (String)rowm.getValue(masterDs.nameToIndex(Work_report_type.YE_TYPE));
			String reportCode = (String)rowm.getValue(masterDs.nameToIndex(Work_report_type.REPORT_CODE));
			String noticeTypePK = NoticeUtil.getNoticeTypePKByBusinessTypeAndCode(businessType, reportCode);
			if (noticeTypePK != null && StringUtils.isNotEmpty(noticeTypePK)) {
				throw new LfwRuntimeException("该报告编码已经存在，请输入其他报告编码!");
			}
		}
		String report_way = (String)rowm.getValue(masterDs.nameToIndex(Work_report_type.REPORT_WAY));
		  // 1:定期；2：不定期
		  if ("1".equals(report_way)) {
				// 催报时间格式约定check start
				//催报时间格式约定：Y.Q.MM.DD，共8位，中间用英文逗号相隔。
				String expiration_date = (String)rowm.getValue(masterDs.nameToIndex(Work_report_type.EXPIRATION_DATE));
				// 获得报送频率 (年：1，季:2，月:3，旬：4，日:5)
				String submitFrecy = (String)rowm.getValue(masterDs.nameToIndex(Work_report_type.ATTRNAME));
				if (expiration_date==null){
					return;
				}
				if (expiration_date.length() != 9) {
					throw new LfwRuntimeException("报告日期长度错误!");
				}
				String[] values = expiration_date.split("\\.");
				if (values.length != 4) {
					throw new LfwRuntimeException("报告日期格式应分为四段，请确认!");
				}
				// 取出约定格式中 【年】 的部分
				String year = values[0];
				// 取出约定格式中 【季】 的部分
				String quarter = values[1];
				// 取出约定格式中 【月】 的部分
				String month = values[2];
				// 取出约定格式中 【日】 的部分
				String day = values[3];
				StringBuffer errorMessage = new StringBuffer();
				// 年报
				if ("1".equals(submitFrecy)){
					// 年字段
					if (!"Y".equals(year)) {
						errorMessage.append("【年】字段不是Y;");
					}
					// 季字段
					if(!"0".equals(quarter)) {
						errorMessage.append("【季】字段不是0;");
					}
					// 月字段
					if(month.compareTo("01") < 0 || month.compareTo("12") > 0) {
						errorMessage.append("【月】字段不是01-12;");
					}
					// 日字段
					if (day.compareTo("01") < 0 || day.compareTo("31") > 0) {
						errorMessage.append("【日】字段不是01-31;");
					}
					if (errorMessage.length() > 0) {
						throw new LfwRuntimeException("报告截止日期格式错误:报送频率为年报，但是截止日期中 " + errorMessage.toString() + "!");
					}
				// 季报	
				} else if ("2".equals(submitFrecy)) {
					// 年字段
					if(!"0".equals(year)) {
						errorMessage.append("【年】字段不是0;");
					}
					// 季字段
					if (!"Q".equals(quarter)) {
						errorMessage.append("【季】字段不是Q;");
					}
					// 月字段
					if (!("M1".equals(month) || "M2".equals(month) || "M3".equals(month))) {
						errorMessage.append("【月】字段不是M1-M3;");
					}
					// 日字段
					if (day.compareTo("01") < 0 || day.compareTo("31") > 0) {
						errorMessage.append("【日】字段不是01-31;");
					}
					if (errorMessage.length() > 0) {
						throw new LfwRuntimeException("报告截止日期格式错误:报送频率为季报，但是截止日期中 " + errorMessage.toString() + "!");
					}
				// 月报	
				} else if ("3".equals(submitFrecy)) {
					// 年字段
					if(!"0".equals(year)) {
						errorMessage.append("【年】字段不是0;");
					}
					// 季字段
					if(!"0".equals(quarter)) {
						errorMessage.append("【季】字段不是0;");
					}
					// 月字段
					if (!"MM".equals(month)) {
						errorMessage.append("【月】字段不是MM;");
					}
					// 日字段
					if (day.compareTo("01") < 0 || day.compareTo("31") > 0) {
						errorMessage.append("【日】字段不是01-31;");
					}
					if (errorMessage.length() > 0) {
						throw new LfwRuntimeException("报告截止日期格式错误:报送频率为月报，但是截止日期中 " + errorMessage.toString() + "!");
					}
				// 旬报	
				} else if ("4".equals(submitFrecy)) {
					// 年字段
					if(!"0".equals(year)) {
						errorMessage.append("【年】字段不是0;");
					}
					// 季字段
					if(!"0".equals(quarter)) {
						errorMessage.append("【季】字段不是0;");
					}
					// 月字段
					if (!"MM".equals(month)) {
						errorMessage.append("【月】字段不是MM;");
					}
					// 日字段
					if (!("D1".equals(day) || "D2".equals(day) || "D3".equals(day))) {
						errorMessage.append("【日】字段不是D1-D3;");
					}
					if (errorMessage.length() > 0) {
						throw new LfwRuntimeException("报告截止日期格式错误:报送频率为旬报，但是截止日期中 " + errorMessage.toString() + "!");
					}
				// 日报	
				} else if ("5".equals(submitFrecy)) {
					// 年字段
					if(!"0".equals(year)) {
						errorMessage.append("【年】字段不是0;");
					}
					// 季字段
					if(!"0".equals(quarter)) {
						errorMessage.append("【季】字段不是0;");
					}
					// 月字段
					if (!"00".equals(month)) {
						errorMessage.append("【月】字段不是00;");
					}
					// 日字段
					if (!"DD".equals(day)) {
						errorMessage.append("【日】字段不是DD;");
					}
					if (errorMessage.length() > 0) {
						throw new LfwRuntimeException("报告截止日期格式错误:报送频率为日报，但是截止日期中 " + errorMessage.toString() + "!");
					}
				}
		  } else if ("2".equals(report_way)) {
			  
		  }

  }
  /** 
 * 新增
 */
  public void onAdd(  MouseEvent<?> mouseEvent) throws BusinessException {
    CmdInvoker.invoke(new UifAddCmd(this.getMasterDsId()) {
			@Override
			protected void onBeforeRowAdd(Row row) {
				setAutoFillValue(row);
			}
		});
  }
  /** 
 * 复制
 * @param mouseEvent
 */
  public void onCopy(  MouseEvent<?> mouseEvent){
    CmdInvoker.invoke(new UifCopyCmd(this.getMasterDsId()));
  }
  /** 
 * 删除
 */
  public void onDelete(  MouseEvent<?> mouseEvent) throws BusinessException {
    CmdInvoker.invoke(new UifDelCmdRV(this.getMasterDsId()));
  }
  /** 
 * 返回
 */
  public void onBack(  MouseEvent<?> mouseEvent) throws BusinessException {
    this.getCurrentAppCtx().closeWinDialog();
  }
  /** 
 * 启用
 * @param mouseEvent
 * @throws BusinessException
 */
  public void onStart(  MouseEvent<?> mouseEvent) throws BusinessException {
    CmdInvoker.invoke(new UifEnableCmd(this.getMasterDsId(), true));
  }
  /** 
 * 停用
 * @param mouseEvent
 * @throws BusinessException
 */
  public void onStop(  MouseEvent<?> mouseEvent) throws BusinessException {
    CmdInvoker.invoke(new UifEnableCmd(this.getMasterDsId(), false));
  }
  /** 
 * 附件
 * @param mouseEvent
 * @throws BusinessException
 */
  public void onAttchFile(  MouseEvent<?> mouseEvent) throws BusinessException {
    Dataset ds = this.getMasterDs();
		Row row = ds.getSelectedRow();
		if (row == null) {
			throw new LfwRuntimeException("请选中数据!");
		}
		String primaryKey = ds.getPrimaryKeyField();
		if (primaryKey == null){
			throw new LfwRuntimeException("当前Dataset没有设置主键!");
		}
		String primaryKeyValue = (String) row.getValue(ds.nameToIndex(primaryKey));
		Map<String, String> paramMap = null;
		if (primaryKeyValue != null){
			paramMap = new HashMap<String, String>(2);
			paramMap.put(PARAM_BILLITEM, primaryKeyValue);
		}
		CmdInvoker.invoke(new UifAttachCmd("附件", paramMap));
  }
  /** 
 * 打印
 * @param mouseEvent
 * @throws BusinessException
 */
  public void onPrint(  MouseEvent<?> mouseEvent) throws BusinessException {
    Dataset ds = this.getMasterDs();
		Row row = ds.getSelectedRow();
		if (row == null) {
			throw new LfwRuntimeException("请选中数据!");
		}
		try{
			List<Dataset> list = new ArrayList<Dataset>(1);
			list.add(ds);
			DefaultPrintService printService = new DefaultPrintService();
			printService.setDatasetList(list);
			ICpPrintTemplateService service = ServiceLocator.getService(ICpPrintTemplateService.class);
			service.print(printService);
		}catch(Exception e){
			LfwLogger.error(e);
			throw new LfwRuntimeException(e.getMessage());
		}
  }
  /** 
 * 设置PK_ORG字段值
 * @param row
 */
  private void setAutoFillValue(  Row row){
    if(row != null){
			Dataset ds = this.getCurrentView().getViewModels().getDataset(this.getMasterDsId());
			
			String pkOrg = this.getCurrentAppCtx().getAppEnvironment().getPk_org();
			if(pkOrg != null){
				int pkOrgIndex = ds.nameToIndex("pk_org");
				if(pkOrgIndex >= 0){
					row.setValue(pkOrgIndex, pkOrg);		
				}
			}
			String pkGroup = this.getCurrentAppCtx().getAppEnvironment().getPk_group();
			if(pkGroup != null){
				int pkGroupIndex = ds.nameToIndex(PK_GROUP);
				if(pkGroupIndex >= 0){
					row.setValue(pkGroupIndex, pkGroup);		
				}
			}
		}
  }
  /** 
 * 子表新增
 */
  public void onGridAddClick(  MouseEvent<?> mouseEvent){
    Dataset masterDs = getMasterDs();
		  Row selectedRow = masterDs.getSelectedRow();
		String report_body = (String)selectedRow.getValue(masterDs.nameToIndex(Work_report_type.REPORT_BODY));
		AppUtil.addAppAttr(IGlobalConstants.APPATTR_REPORT_BODY, report_body);
		OpenProperties props = new OpenProperties(
				"visualOrganization", "选择企业或组织", "300",
				"600");
		props.setButtonZone(false);
		this.getCurrentAppCtx().getCurrentWindowContext().popView(props);
  }
  /** 
 * 子表编辑
 */
  public void onGridEditClick(  MouseEvent<?> mouseEvent){
    GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		Dataset ds = this.getCurrentView().getViewModels().getDataset(dsId);
		ds.setEnabled(true);
  }
  /** 
 * 子表删除
 */
  public void onGridDeleteClick(  MouseEvent<?> mouseEvent){
    GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		CmdInvoker.invoke(new UifLineDelCmd(dsId));
  }
  @Override protected String getMasterDsId(){
    return "work_report_type";
  }
  public void onAfterDataChange(  DatasetCellEvent datasetCellEvent){
    Dataset masterDs = getMasterDs();
	  int colIndex = datasetCellEvent.getColIndex();
	  Row selectedRow = masterDs.getSelectedRow();
	  LfwView main = LfwRuntimeEnvironment.getWebContext().getPageMeta()
				.getView("main");
	  FormComp formcomp = (FormComp) main.getViewComponents()
			.getComponent("work_report_type_form");
	  //如果业务类型改变,通知内容设置
	  if(colIndex==masterDs.nameToIndex(Work_report_type.YE_TYPE)){
		  String ye_type_name = formcomp.getElementById("ye_type_name").getValue();
		// 保存的一个业务类型
		String def1 = (String) masterDs.getValue(masterDs
				.nameToIndex(Work_report_type.DEF1));
		  uap.web.bd.pub.AppUtil.addAppAttr("pk_business_type", (String)formcomp.getElementById("ye_type").getValue());
		  String contentGzwS = masterDs.getValue(masterDs.nameToIndex(Work_report_type.URGE_CONTENT_GZW))==null?"":
				  		   masterDs.getValue(masterDs.nameToIndex(Work_report_type.URGE_CONTENT_GZW)).toString();
			String contentGzwNew = "";
			if (def1 != null) {
				contentGzwNew = contentGzwS.replace(def1,
						ye_type_name);
			} else {
				contentGzwNew = contentGzwS.replace("业务类型",
						ye_type_name);
			}
		  masterDs.setValue(masterDs.nameToIndex(Work_report_type.URGE_CONTENT_GZW), contentGzwNew);
		  String contentQyS = masterDs.getValue(masterDs.nameToIndex(Work_report_type.URGE_CONTENT_QY))==null?"":
			  				masterDs.getValue(masterDs.nameToIndex(Work_report_type.URGE_CONTENT_QY)).toString();
			String contentQyNew = "";
			if (def1 != null) {
				contentQyNew = contentQyS.replace(def1,
						ye_type_name);
			} else {
				contentQyNew = contentQyS.replace("业务类型",
						ye_type_name);
			}
		  masterDs.setValue(masterDs.nameToIndex(Work_report_type.URGE_CONTENT_QY), contentQyNew);
			// 保存新的业务类型到def1中
			masterDs.setValue(masterDs.nameToIndex(Work_report_type.DEF1),
					ye_type_name);
	  }
	  //如果报告类型改变,通知内容设置
	  if(colIndex==masterDs.nameToIndex(Work_report_type.REPORT_TYPE)){
		  String reportType = masterDs.getValue(masterDs.nameToIndex(Work_report_type.REPORT_TYPE))==null?"":
				  		   masterDs.getValue(masterDs.nameToIndex(Work_report_type.REPORT_TYPE)).toString();
			// 保存的一个报告类型
			String def2 = (String) masterDs.getValue(masterDs
					.nameToIndex(Work_report_type.DEF2));
		  String contentGzwS = masterDs.getValue(masterDs.nameToIndex(Work_report_type.URGE_CONTENT_GZW))==null?"":
	  		   masterDs.getValue(masterDs.nameToIndex(Work_report_type.URGE_CONTENT_GZW)).toString();

			String contentGzwNew = "";
			if (def2 != null) {
				contentGzwNew = contentGzwS.replace(def2,
						reportType);
			} else {
				contentGzwNew = contentGzwS.replace("工作报告类型",
						reportType);
			}
		  masterDs.setValue(masterDs.nameToIndex(Work_report_type.URGE_CONTENT_GZW), contentGzwNew);
		  String contentQyS = masterDs.getValue(masterDs.nameToIndex(Work_report_type.URGE_CONTENT_QY))==null?"":
				masterDs.getValue(masterDs.nameToIndex(Work_report_type.URGE_CONTENT_QY)).toString();

			String contentQyNew = "";
			if (def2 != null) {
				contentQyNew = contentQyS.replace(def2,
						reportType);
			} else {
				contentQyNew = contentQyS.replace("工作报告类型",
						reportType);
			}
		  masterDs.setValue(masterDs.nameToIndex(Work_report_type.URGE_CONTENT_QY), contentQyNew);
			// 保存新的报告类型到def2中
			masterDs.setValue(masterDs.nameToIndex(Work_report_type.DEF2),
					reportType);
	  }
	  //如果报送方式改变
	  if(colIndex==masterDs.nameToIndex(Work_report_type.REPORT_WAY)){
		  String report_way = masterDs.getValue(masterDs.nameToIndex(Work_report_type.REPORT_WAY))==null?"":
	  		   masterDs.getValue(masterDs.nameToIndex(Work_report_type.REPORT_WAY)).toString();
		LfwView view = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		FormComp form = (FormComp) view
				.getComponentsByDataset("work_report_type")[0];
		  // 1:定期；2：不定期
		  if ("1".equals(report_way)) {
				// 【催报相关】
				FormElement urge_related = form.getElementById("urge_related"); // 【催报相关】
				urge_related.setVisible(true);
				// 是否自动催报
				FormElement is_auto_urge = form.getElementById("is_auto_urge"); // 是否自动催报
				masterDs.setValue(
						masterDs.nameToIndex(Work_report_type.IS_AUTO_URGE), "0");
				is_auto_urge.setVisible(true);
				// 催报信息发送方式
				FormElement urge_info_trans_way = form
						.getElementById("urge_info_trans_way"); // 催报信息发送方式
				masterDs.setValue(masterDs
						.nameToIndex(Work_report_type.URGE_INFO_TRANS_WAY), "1");
				urge_info_trans_way.setVisible(true);
				// 提前催报天数
				FormElement day_num = form.getElementById("urge_time"); // 提前催报天数
				masterDs.setValue(masterDs.nameToIndex(Work_report_type.URGE_TIME),
						"");
				day_num.setVisible(true);
				// 催报频率
				FormElement urge_frequency = form
						.getElementById("urge_frequency"); // 催报频率
				masterDs.setValue(
						masterDs.nameToIndex(Work_report_type.URGE_FREQUENCY),
						"1");
				urge_frequency.setVisible(true);

				// 报送统计内容格式（国资委用户）
				FormElement urge_content_gzw = form
						.getElementById("urge_content_gzw"); // 报送统计内容格式（国资委用户）
				masterDs.setValue(
						masterDs.nameToIndex(Work_report_type.URGE_CONTENT_GZW),
						IGlobalConstants.CONTENT_URGE_CONTENT_GZW);
				urge_content_gzw.setVisible(true);

				// 催报消息内容格式（企业用户）
				FormElement urge_content_qy = form
						.getElementById("urge_content_qy"); // 催报消息内容格式（企业用户）
				masterDs.setValue(
						masterDs.nameToIndex(Work_report_type.URGE_CONTENT_QY),
						IGlobalConstants.CONTENT_URGE_CONTENT_QY);
				urge_content_qy.setVisible(true);
				// 报告截止日期
				FormElement expiration_date = form
						.getElementById("expiration_date"); // 报告截止日期
				masterDs.setValue(
						masterDs.nameToIndex(Work_report_type.EXPIRATION_DATE),
						null);
				expiration_date.setVisible(true);
				expiration_date.setNullAble(false);
				// 报送频率
				FormElement attrname = form
						.getElementById("attrname"); // 报送频率
				masterDs.setValue(
						masterDs.nameToIndex(Work_report_type.ATTRNAME),
						"3");
				attrname.setVisible(true);
				attrname.setNullAble(true);
		  } else if ("2".equals(report_way)) {
				// 【催报相关】
				FormElement urge_related = form.getElementById("urge_related"); // 【催报相关】
				urge_related.setVisible(false);
				// 是否自动催报
				FormElement is_auto_urge = form.getElementById("is_auto_urge"); // 是否自动催报
				masterDs.setValue(
						masterDs.nameToIndex(Work_report_type.IS_AUTO_URGE), null);
				is_auto_urge.setVisible(false);
				// 催报信息发送方式
				FormElement urge_info_trans_way = form
						.getElementById("urge_info_trans_way"); // 催报信息发送方式
				masterDs.setValue(masterDs
						.nameToIndex(Work_report_type.URGE_INFO_TRANS_WAY), null);
				urge_info_trans_way.setVisible(false);
				// 提前催报天数
				FormElement day_num = form.getElementById("urge_time"); // 提前催报天数
				masterDs.setValue(masterDs.nameToIndex(Work_report_type.URGE_TIME),
						null);
				day_num.setVisible(false);
				// 催报频率
				FormElement urge_frequency = form
						.getElementById("urge_frequency"); // 催报频率
				masterDs.setValue(
						masterDs.nameToIndex(Work_report_type.URGE_FREQUENCY),
						null);
				urge_frequency.setVisible(false);

				// 报送统计内容格式（国资委用户）
				FormElement urge_content_gzw = form
						.getElementById("urge_content_gzw"); // 报送统计内容格式（国资委用户）
				masterDs.setValue(
						masterDs.nameToIndex(Work_report_type.URGE_CONTENT_GZW),
						null);
				urge_content_gzw.setVisible(false);

				// 催报消息内容格式（企业用户）
				FormElement urge_content_qy = form
						.getElementById("urge_content_qy"); // 催报消息内容格式（企业用户）
				masterDs.setValue(
						masterDs.nameToIndex(Work_report_type.URGE_CONTENT_QY),
						null);
				urge_content_qy.setVisible(false);
				// 报告截止日期
				FormElement expiration_date = form
						.getElementById("expiration_date"); // 报告截止日期
				masterDs.setValue(
						masterDs.nameToIndex(Work_report_type.EXPIRATION_DATE),
						null);
				expiration_date.setVisible(false);
				expiration_date.setNullAble(true);
				// 报送频率
				FormElement attrname = form
						.getElementById("attrname"); // 报送频率
				masterDs.setValue(
						masterDs.nameToIndex(Work_report_type.ATTRNAME),
						null);
				attrname.setVisible(false);
				attrname.setNullAble(true);
		  }
	  }
	  //如果“是否自动催报”改变
	  if(colIndex==masterDs.nameToIndex(Work_report_type.IS_AUTO_URGE)){
		  String is_auto_urge = masterDs.getValue(masterDs.nameToIndex(Work_report_type.IS_AUTO_URGE))==null?"":
	  		   masterDs.getValue(masterDs.nameToIndex(Work_report_type.IS_AUTO_URGE)).toString();
		LfwView view = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		FormComp form = (FormComp) view
				.getComponentsByDataset("work_report_type")[0];
		  // 0:是；1：否
		  if ("0".equals(is_auto_urge)) {
				// 催报信息发送方式
				FormElement urge_info_trans_way = form
						.getElementById("urge_info_trans_way"); // 催报信息发送方式
				masterDs.setValue(masterDs
						.nameToIndex(Work_report_type.URGE_INFO_TRANS_WAY), "1");
				urge_info_trans_way.setVisible(true);
				urge_info_trans_way.setEnabled(true);
				// 提前催报天数
				FormElement day_num = form.getElementById("urge_time"); // 提前催报天数
				masterDs.setValue(masterDs.nameToIndex(Work_report_type.URGE_TIME),
						"");
				day_num.setVisible(true);
				day_num.setEnabled(true);
				// 催报频率
				FormElement urge_frequency = form
						.getElementById("urge_frequency"); // 催报频率
				masterDs.setValue(
						masterDs.nameToIndex(Work_report_type.URGE_FREQUENCY),
						"1");
				urge_frequency.setVisible(true);
				urge_frequency.setEnabled(true);
				// 报送统计内容格式（国资委用户）
				FormElement urge_content_gzw = form
						.getElementById("urge_content_gzw"); // 报送统计内容格式（国资委用户）
				masterDs.setValue(
						masterDs.nameToIndex(Work_report_type.URGE_CONTENT_GZW),
						IGlobalConstants.CONTENT_URGE_CONTENT_GZW);
				urge_content_gzw.setVisible(true);
				urge_content_gzw.setEnabled(true);
				// 催报消息内容格式（企业用户）
				FormElement urge_content_qy = form
						.getElementById("urge_content_qy"); // 催报消息内容格式（企业用户）
				masterDs.setValue(
						masterDs.nameToIndex(Work_report_type.URGE_CONTENT_QY),
						IGlobalConstants.CONTENT_URGE_CONTENT_QY);
				urge_content_qy.setVisible(true);
				urge_content_qy.setEnabled(true);
				// 报告截止日期
				FormElement expiration_date = form
						.getElementById("expiration_date"); // 报告截止日期
				masterDs.setValue(
						masterDs.nameToIndex(Work_report_type.EXPIRATION_DATE),
						null);
				expiration_date.setVisible(true);
				expiration_date.setEnabled(true);
				expiration_date.setNullAble(false);
		  }
		  if ("1".equals(is_auto_urge)) {
				// 催报信息发送方式
				FormElement urge_info_trans_way = form
						.getElementById("urge_info_trans_way"); // 催报信息发送方式
				urge_info_trans_way.setEnabled(false);
				// 提前催报天数
				FormElement day_num = form.getElementById("urge_time"); // 提前催报天数
				day_num.setEnabled(false);
				// 催报频率
				FormElement urge_frequency = form
						.getElementById("urge_frequency"); // 催报频率
				urge_frequency.setEnabled(false);

				// 报送统计内容格式（国资委用户）
				FormElement urge_content_gzw = form
						.getElementById("urge_content_gzw"); // 报送统计内容格式（国资委用户）
				urge_content_gzw.setEnabled(false);

				// 催报消息内容格式（企业用户）
				FormElement urge_content_qy = form
						.getElementById("urge_content_qy"); // 催报消息内容格式（企业用户）
				urge_content_qy.setEnabled(false);
				// 报告截止日期
				FormElement expiration_date = form
						.getElementById("expiration_date"); // 报告截止日期
				expiration_date.setEnabled(false);
				expiration_date.setNullAble(true);
		  }
	  }
  }
  public void onTimeDefineInfoClick(  MouseEvent mouseEvent){
    OpenProperties props = new OpenProperties("timeDefineInfo",
					"报告截止日期设定说明", "800", "750");
			props.setButtonZone(false);
			this.getCurrentAppCtx().getCurrentWindowContext().popView(props);
  }
  public void doSelectVisualOrgs(  Map keys){
    ScapLogger.console("doSelectVisualOrgs");

	    LfwView view = AppLifeCycleContext.current().getWindowContext()
					.getViewContext("main").getView();
			LfwView view_comp = AppLifeCycleContext.current().getWindowContext()
					.getViewContext("visualOrganization").getView();
			Dataset masterDs = this.getMasterDs();
			Row selectedRow = masterDs.getSelectedRow();
			Object pk_work_report_type = selectedRow.getValue(masterDs.nameToIndex(masterDs
					.getPrimaryKeyField()));
			String reportBody = (String)selectedRow.getValue(masterDs.nameToIndex(Work_report_type.REPORT_BODY));
			// 取出各个子表的dataset
			Dataset receive_orgds = view.getViewModels().getDataset(
					"receive_org");
			
			Dataset visualOrganizationDs = view_comp.getViewModels().getDataset(
					"visualOrganizationDs");
			Dataset cpOrgsDs = view_comp.getViewModels().getDataset(
					"cp_orgsDs");
			// 根据报告主体进行相应处理
			if (reportBody != null && ("2".equals(reportBody) || "3".equals(reportBody))) {//填报主体是组织或个人
	
				// start：组织相关处理
				Row[] selRows_v = visualOrganizationDs.getAllSelectedRows();
				String[] pk_orgArray_v = new String[selRows_v.length];
				StringBuffer compSelected_v = new StringBuffer();
				// 循环接受企业list，根据接收组织，
				for (int i = 0; i < selRows_v.length; i++) {
					String pk_org = (String) selRows_v[i].getValue(visualOrganizationDs
							.nameToIndex(visualOrganization.PK_VISUALORG));
					pk_orgArray_v[i] = pk_org;
					compSelected_v.append(pk_org).append(",");
				}
				// 改成子表实现方式后，在这里只需要子grid的相应字段回写上即可。
				Map<String, String> mapExistedComp_v = isVisualCompExisted();
				// 回写 接受范围子表的ds
				for (int i = 0; i < pk_orgArray_v.length; i++) {
					// 去重处理
					if (mapExistedComp_v.containsKey(pk_orgArray_v[i])) {
						continue;
					}
					Row row = receive_orgds.getEmptyRow();
					String pk_org = pk_orgArray_v[i];
					String pk_primarykey = generatePk();
					// 主键
					row.setValue(
							receive_orgds.nameToIndex(receive_org.PK_RECEIVE_ORG),
							pk_primarykey);
	
					// 报告类型主键 主表pk
					row.setValue(
							receive_orgds.nameToIndex(receive_org.PK_WORK_REPORT_TYPE),
							pk_work_report_type.toString());
	
					// 接收组织
					row.setValue(
							receive_orgds.nameToIndex(receive_org.NOTICE_VISUAL_ORG),
							pk_org);
	
					receive_orgds.addRow(row);
				}
				// end：组织相关处理
			}
			// 根据报告主体进行相应处理
			if (reportBody != null && ("1".equals(reportBody) || "3".equals(reportBody))) {//填报主体是企业或个人
		
				// start：企业相关处理
				Row[] selRows_r = cpOrgsDs.getAllSelectedRows();
				String[] pk_orgArray_r = new String[selRows_r.length];
				StringBuffer compSelected_r = new StringBuffer();
				// 循环接受企业list，根据接收组织，
				for (int i = 0; i < selRows_r.length; i++) {
					String pk_org = (String) selRows_r[i].getValue(cpOrgsDs
							.nameToIndex(CpOrgVO.PK_ORG));
					pk_orgArray_r[i] = pk_org;
					compSelected_r.append(pk_org).append(",");
				}
				// 改成子表实现方式后，在这里只需要子grid的相应字段回写上即可。
				Map<String, String> mapExistedComp_r = isRealCompExisted();
				// 回写 接受范围子表的ds
				for (int i = 0; i < pk_orgArray_r.length; i++) {
					// 去重处理
					if (mapExistedComp_r.containsKey(pk_orgArray_r[i])) {
						continue;
					}
					Row row = receive_orgds.getEmptyRow();
					String pk_org = pk_orgArray_r[i];
					String pk_primarykey = generatePk();
					// 主键
					row.setValue(
							receive_orgds.nameToIndex(receive_org.PK_RECEIVE_ORG),
							pk_primarykey);
		
					// 报告类型主键 主表pk
					row.setValue(
							receive_orgds.nameToIndex(receive_org.PK_WORK_REPORT_TYPE),
							pk_work_report_type.toString());
		
					// 接收企业
					row.setValue(
							receive_orgds.nameToIndex(receive_org.NOTICE_ORG),
							pk_org);
		
					receive_orgds.addRow(row);
				}
				// end：企业相关处理
			}
  }
  private Map<String,String> isRealCompExisted(){
    LfwView view = AppLifeCycleContext.current().getWindowContext()
					.getViewContext("main").getView();
			Dataset receive_orgds = view.getViewModels().getDataset(
					"receive_org");
			// 接受范围子表去重处理
			Map<String, String> mapExisted = new HashMap<String, String>();
			Row[] rowExisted = receive_orgds.getAllRow();
			for (int i = 0; i < rowExisted.length; i++) {
				String pkOrg = (String) rowExisted[i].getValue(receive_orgds
						.nameToIndex(receive_org.NOTICE_ORG));
				if (mapExisted.containsKey(pkOrg)) {
					continue;
				} else {
					mapExisted.put(pkOrg, "1");
				}
			}
			return mapExisted;
  }
  private Map<String,String> isVisualCompExisted(){
    LfwView view = AppLifeCycleContext.current().getWindowContext()
					.getViewContext("main").getView();
			Dataset receive_orgds = view.getViewModels().getDataset(
					"receive_org");
			// 接受范围子表去重处理
			Map<String, String> mapExisted = new HashMap<String, String>();
			Row[] rowExisted = receive_orgds.getAllRow();
			for (int i = 0; i < rowExisted.length; i++) {
				String pkVisualOrg = (String) rowExisted[i].getValue(receive_orgds
						.nameToIndex(receive_org.NOTICE_VISUAL_ORG));
				if (mapExisted.containsKey(pkVisualOrg)) {
					continue;
				} else {
					mapExisted.put(pkVisualOrg, "1");
				}
			}
			return mapExisted;
  }
}
