package nc.scap.dsm.material;
import nc.uap.lfw.core.base.ExtAttribute;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.cmd.UifAddCmd;
import nc.uap.lfw.core.page.ViewComponents;
import nc.vo.pub.lang.UFDateTime;
import nc.uap.cpb.org.exception.CpbBusinessException;
import nc.uap.lfw.core.cmd.UifUpdateUIDataCmdRV;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import nc.bs.dao.DAOException;
import nc.uap.lfw.core.cmd.UifLineDelCmd;
import nc.vo.util.BDPKLockUtil;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.util.LfwClassUtil;
import nc.scap.jjpub.util.JjUtil;
import nc.uap.lfw.core.page.ViewModels;
import nc.uap.lfw.core.cmd.UifSaveCmdRV;
import nc.uap.cpb.org.vos.CpUserVO;
import nc.scap.pub.itf.IGlobalConstants;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.file.LfwFileConstants;
import java.util.List;
import nc.uap.lfw.core.log.LfwLogger;
import nc.scap.scapjj.pub.IConst4scapjj;
import nc.uap.lfw.core.file.FillFileInfoHelper;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.cpb.log.CpLogger;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.CpOrgUtil;
import nc.uap.lfw.jsp.uimeta.UIElementFinder;
import java.text.DecimalFormat;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.wfm.exe.WfmCmd;
import org.apache.commons.lang.StringUtils;
import nc.uap.lfw.core.uif.delegator.DefaultDataValidator;
import nc.uap.lfw.core.cmd.CmdInvoker;
import java.util.Map;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.ctrl.filemgr.LfwFileDsVO;
import nc.uap.lfw.core.cmd.UifCopyCmd;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.bs.dao.BaseDAO;
import java.util.HashMap;
import nc.scap.dsm.material.wfm.WfmFlwFormVO;
import nc.uap.cpb.org.itf.ICpUserQry;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.cmd.UifPlugoutCmd;
import java.util.regex.Pattern;
import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.wfm.utils.WfmCPUtilFacade;
import nc.jdbc.framework.SQLParameter;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.data.Dataset;
import java.util.ArrayList;
import nc.uap.ctrl.tpl.print.init.DefaultPrintService;
import nc.uap.lfw.file.FileManager;
import nc.uap.lfw.core.cmd.base.CommandStatus;
import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.cmd.UifDelCmdRV;
import nc.vo.pub.SuperVO;
import nc.uap.lfw.core.AppInteractionUtil;
import nc.uap.ctrl.tpl.print.ICpPrintTemplateService;
import nc.uap.lfw.core.page.ViewMenus;
import nc.vo.pub.BusinessException;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.wfm.utils.WfmTaskUtil;
import nc.uap.lfw.file.vo.LfwFileVO;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.lfw.core.constants.AppConsts;
import uap.web.bd.pub.AppUtil;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.scap.pub.util.CpWinUtil;
import nc.uap.lfw.core.serializer.impl.SuperVO2DatasetSerializer;
import uap.lfw.core.locator.ServiceLocator;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.wfm.vo.WfmFormInfoCtx;
/** 
 * ��Ƭ����Ĭ���߼�
 */
public class MaterialCardWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
  private static final String PLUGOUT_ID="afterSavePlugout";
  public static final String OPEN_BILL_ID="openBillId";
  BaseDAO dao;
  BaseDAO getBaseDAO(){
    if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
  }
  /** 
 * ҳ����ʾ�¼�
 * @param dialogEvent
 */
  public void beforeShow(  DialogEvent dialogEvent){
    Dataset masterDs = this.getMasterDs();
		masterDs.clear();
		
		String oper = this.getOperator();
		
		uap.web.bd.pub.AppUtil.addAppAttr(LfwFileConstants.SYSID,LfwFileConstants.SYSID_BAFILE);
		hiddenTab4Yh();
		if(AppConsts.OPE_ADD.equals(oper)){
			CmdInvoker.invoke(new UifAddCmd(this.getMasterDsId()){
				@Override
				protected void onBeforeRowAdd(Row row) {
					setAutoFillValue(row);
					
					Dataset masterDs = getMasterDs();
					String pk_primarykey = generatePk();
					row.setValue(masterDs.nameToIndex(masterDs.getPrimaryKeyField()), pk_primarykey);
					row.setValue(masterDs.nameToIndex("attach"), pk_primarykey);
					FillFileInfoHelper.resetItem(pk_primarykey);
					FillFileInfoHelper.fillFileInfo(masterDs, row);
				}
			});
		}else if(AppConsts.OPE_EDIT.equals(oper)) {		
			String currentValue = LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParameter("openBillId");
			if(currentValue == null){
				String value = (String)LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParameter("pk_material_h");
				LfwRuntimeEnvironment.getWebContext().getWebSession().addOriginalParameter("openBillId", value);
			}
			CmdInvoker.invoke(new UifDatasetLoadCmd(masterDs){
				@Override
				protected void onAfterDatasetLoad() {
					setDSEnabledByTask(this.getDs());
					
					String primaryKey = this.getDs().getPrimaryKeyField();
					if (primaryKey == null) {
						throw new LfwRuntimeException("��ǰDatasetû����������!");
					}
					String primaryKeyValue = (String) this.getDs().getSelectedRow().getValue(this.getDs().nameToIndex(primaryKey));					
					FillFileInfoHelper.resetItem(primaryKeyValue);
				}
			});
		} else if ("SCAN".equals(oper)) {
			CpLogger.debug("click button scan!");
			String currentValue = LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParameter("openBillId");
			if(currentValue == null){
				String value = (String)LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParameter("pk_material_h");
				LfwRuntimeEnvironment.getWebContext().getWebSession().addOriginalParameter("openBillId", value);
			}
			CmdInvoker.invoke(new UifDatasetLoadCmd(masterDs){
				@Override
				protected void onAfterDatasetLoad() {
					setDSEnabledByTask(this.getDs());
					this.getDs().setEnabled(false);
					String primaryKey = this.getDs().getPrimaryKeyField();
					if (primaryKey == null) {
						throw new LfwRuntimeException("��ǰDatasetû����������!");
					}
					String primaryKeyValue = (String) this.getDs().getSelectedRow().getValue(this.getDs().nameToIndex(primaryKey));					
					FillFileInfoHelper.resetItem(primaryKeyValue);
				}
			});
			
		} else if ("APPR".equals(oper)) {
			CpLogger.debug("click button appr!");
			String currentValue = LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParameter("openBillId");
			if(currentValue == null){
				String value = (String)LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParameter("pk_material_h");
				LfwRuntimeEnvironment.getWebContext().getWebSession().addOriginalParameter("openBillId", value);
			}
			CmdInvoker.invoke(new UifDatasetLoadCmd(masterDs){
				@Override
				protected void onAfterDatasetLoad() {
					setDSEnabledByTask(this.getDs());
					this.getDs().setEnabled(false);
					String primaryKey = this.getDs().getPrimaryKeyField();
					if (primaryKey == null) {
						throw new LfwRuntimeException("��ǰDatasetû����������!");
					}
					String primaryKeyValue = (String) this.getDs().getSelectedRow().getValue(this.getDs().nameToIndex(primaryKey));					
					FillFileInfoHelper.resetItem(primaryKeyValue);
				}
			});
			
		}
		
		if(!AppConsts.OPE_ADD.equals(oper)){
			String pk=JjUtil.getDSvalue(masterDs, masterDs.getSelectedRow(),masterDs.getPrimaryKeyField());
			refshData(pk, null);
		}
  }
  public String getSysId(){
    return LfwFileConstants.SYSID_BAFILE;
  }
  /** 
 * ��ȡ�ļ�������
 * @author taoye 2013-7-22
 */
  private FileManager getFileManager(  String filemanager,  String sysid){
    FileManager fileManager = null;
			if (StringUtils.isNotBlank(filemanager)) {
				fileManager = (FileManager) LfwClassUtil.newInstance(filemanager);
			} else if (StringUtils.isNotBlank(sysid))
				fileManager = FileManager.getSystemFileManager(sysid);
			return fileManager;
  }
  /** 
 * ��ȡ�����ļ����͵��ļ���
 * @author taoye 2013-7-22
 */
  private String getSimpleName(  String filename,  String type){
    if (null == filename || filename.equals(""))
				return "";
			if (null == type || type.equals(""))
				return filename;
			if (type.equals("NaN")) {
				return filename;
			}
			String simplename = "";
			Pattern pattern = Pattern.compile("\\." + type + "$");
			simplename = pattern.matcher(filename).replaceAll("");

			return simplename;
  }
  /** 
 * ��ȡ�ļ���С�ַ���
 * @author taoye 2013-7-22
 */
  private String getSizeStr(  long size){
    String sizestr = size + "B";
			if (size <= 0)
				sizestr = size + "B";
			else {
				sizestr = size + "";
				int sizelen = sizestr.length();
				if (sizelen <= 3)
					sizestr = size + "B";
				else if (sizelen > 3 && sizelen <= 6) {
					double newsize = size / Math.pow(10.0, 3);
					DecimalFormat dicf = new DecimalFormat("0.00");
					sizestr = dicf.format(newsize) + "K";
				} else if (sizelen > 6 && sizelen <= 9) {
					double newsize = size / Math.pow(10.0, 6);
					DecimalFormat dicf = new DecimalFormat("0.00");
					sizestr = dicf.format(newsize) + "M";
				} else {
					double newsize = size / Math.pow(10.0, 9);
					DecimalFormat dicf = new DecimalFormat("0.00");
					sizestr = dicf.format(newsize) + "G";
				}
			}
			return sizestr;
  }
  /** 
 * ���ļ���Ϣ�������ļ��б����ݼ�
 * @author taoye 2013-7-22
 */
  public void bindFiletoDS(  LfwFileVO[] files,  Dataset ds) throws CpbBusinessException {
    ds.clear();
			List<LfwFileDsVO> list = new ArrayList<LfwFileDsVO>();
			if (files != null) {
				for (LfwFileVO file : files) {
					LfwFileDsVO vo = new LfwFileDsVO();
					vo.setId(file.getPk_lfwfile());
					vo.setName(getSimpleName(file.getDisplayname(),
							file.getFiletypo()));
					vo.setType(file.getFiletypo());
					vo.setSize(getSizeStr(file.getFilesize()));
					vo.setLastmodified(file.getLastmodifytime());
					vo.setFilemanager(file.getFilemgr());
					String pk_user = file.getLastmodifyer();
					if (null != pk_user && !pk_user.equals("")) {
						CpUserVO user = NCLocator.getInstance()
								.lookup(ICpUserQry.class).getUserByPk(pk_user);
						if (user != null) {
							vo.setLastmodifier(user.getUser_code());
						} else {
							vo.setLastmodifier(pk_user);
						}
					}
					list.add(vo);

				}
			}
			new SuperVO2DatasetSerializer().serialize(
					(SuperVO[]) list.toArray(new LfwFileDsVO[0]), ds);
			ds.getCurrentRowData().getRows();
  }
  public void refshData(  String billitem,  String billtype){
    String sysid = getSysId();
			String filemgr = (String) LfwRuntimeEnvironment.getWebContext()
					.getWebSession()
					.getOriginalParameter(LfwFileConstants.Par_FILEMANAGER);
			FileManager filemanager = getFileManager(filemgr, sysid);
			try {
				LfwFileVO[] files = filemanager.getAttachFileByItemID(billitem,
						billtype);
				Dataset ds = AppLifeCycleContext.current().getWindowContext()
						.getViewContext("attachlist").getView().getViewModels()
						.getDataset("attach_ds");
				ds.clear();
				bindFiletoDS(files, ds);
			} catch (LfwBusinessException e) {
				CpLogger.error(e);
				throw new LfwRuntimeException(e.getMessage());
			}
  }
  /** 
 * ��Ͳ�ѯ�� ����������Աҳǩ
 * ����������Աҳǩ
 */
  private void hiddenTab4Yh(){
    String node_type = (String) AppUtil.getAppAttr(IGlobalConstants.NODE_TYPE);
	  if("add".equals(node_type)||"approve".equals(node_type)||"query".equals(node_type)){
  		  UIMeta uimeta= (UIMeta) LfwRuntimeEnvironment.getWebContext().getUIMeta();
		  UIElement split = UIElementFinder.findElementById(uimeta, "otheruser_b_flowvpane2");
		  split.setVisible(false);
	  }
  }
  /** 
 * ��ȡ����PK
 * @return String
 */
  private String getPkTask(){
    String pk = LfwRuntimeEnvironment.getWebContext().getOriginalParameter(WfmConstants.WfmUrlConst_TaskPk);
		if(pk == null){
			pk = (String)this.getCurrentAppCtx().getAppAttribute(WfmConstants.WfmUrlConst_TaskPk);
		}
		return pk;
  }
  /** 
 * �������������������ݼ�ʹ��״̬
 * @param ds
 */
  private void setDSEnabledByTask(  Dataset ds){
    if(ds != null){
			Object task = WfmTaskUtil.getTaskFromSessionCache(this.getPkTask());
			if(task != null){
				if(WfmTaskUtil.isEndState(task) || WfmTaskUtil.isFinishState(task) ||  WfmTaskUtil.isSuspendedState(task) || WfmTaskUtil.isCanceledState(task)){
					ds.setEnabled(false);
				}else{
					ds.setEnabled(true);
				}
			}else{
				ds.setEnabled(true);
			}
		}
  }
  /** 
 * ����PK_ORG�ֶ�ֵ
 * @param row
 */
  private void setAutoFillValue(  Row row){
    if(row != null){
    	Dataset ds = this.getCurrentView().getViewModels()
				.getDataset(this.getMasterDsId());

		String pkOrg = CntUtil.getCntOrgPk();
		//this.getCurrentAppCtx().getAppEnvironment().getPk_org();
		if (pkOrg != null) {
			int pkOrgIndex = ds.nameToIndex("pk_org");
			if (pkOrgIndex >= 0) {
				row.setValue(pkOrgIndex, pkOrg);
			}
		}
		String pkGroup = this.getCurrentAppCtx().getAppEnvironment()
				.getPk_group();
		if (pkGroup != null) {
			int pkGroupIndex = ds.nameToIndex(PK_GROUP);
			if (pkGroupIndex >= 0) {
				row.setValue(pkGroupIndex, pkGroup);
			}
		}

		String creator = this.getCurrentAppCtx().getAppEnvironment()
				.getLoginUser();
		if (creator != null) {
			int creator_index = ds.nameToIndex("creator");
			if (creator_index >= 0) {
				row.setValue(creator_index, creator);
			}
			int billmaker_index = ds.nameToIndex("billmaker");
			if (billmaker_index >= 0) {
				row.setValue(billmaker_index, creator);
			}
		}

		UFDateTime ts = new UFDateTime(System.currentTimeMillis());
		int creationtime_index = ds.nameToIndex("creationtime");
		if (creationtime_index >= 0) {
			row.setValue(creationtime_index, ts.toString());
		}
		int billdate_index = ds.nameToIndex("billdate");
		if (billdate_index >= 0) {
			row.setValue(billdate_index, ts.toString());
		}
		int formstate_index = ds.nameToIndex("formstate");
		if (formstate_index >= 0) {
			row.setValue(formstate_index, IGlobalConstants.SCAPPM_BILLSTATE_NOTTSTART);
		}
		
		int wlorww_index = ds.nameToIndex("wlorww");
		if(wlorww_index >= 0){
			if(CpOrgUtil.isCompanyUser(creator))
				row.setValue(wlorww_index,"");//��ҵ�û�Ĭ�Ͽ�
			else
				row.setValue(wlorww_index,"1");//�����û�Ĭ��1ί��
		}

		int year_index = ds.nameToIndex("year");
		if (year_index >= 0) {
			row.setValue(year_index, ts.getYear());
		}	
		int month_index = ds.nameToIndex("month");
		int month = ts.getMonth();
		String monthStr = month < 10 ? "0" + month : String.valueOf(month);
		if (month_index >= 0) {
			row.setValue(month_index, monthStr);
		}	
		
		int fbzt_index = ds.nameToIndex("fbzt");
		if (fbzt_index >= 0) {
			row.setValue(fbzt_index, IConst4scapjj.FBZT[1]);
		}
		int djcs_index = ds.nameToIndex("djcs");
		if (djcs_index >= 0) {
			row.setValue(djcs_index, 0);
		}
		String[] currzllx=(String[]) AppUtil.getAppAttr(MaterialListWinMainViewCtrl.currZllx);	
		if(currzllx != null && currzllx.length > 0){
			int zllx_index = ds.nameToIndex("zllx");
			if (zllx_index >= 0) {
				row.setValue(zllx_index, currzllx[0]);
			}
		}
		
		
	}
  }
  /** 
 * ������ѡ���߼�
 * @param datasetEvent
 */
  public void onAfterRowSelect(  DatasetEvent dsEvent){
    Dataset ds = dsEvent.getSource();
    Dataset otheruser_b = this.getCurrentView().getViewModels().getDataset("otheruser_b");
    otheruser_b.setOrderByPart("order by def2 asc");
		CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()){
		   protected void updateButtons(){
		    
		   }
		  });
		String pk=ds.getValue(ds.getPrimaryKeyField())
				.toString();
		String node_type=(String) AppUtil.getAppAttr(IGlobalConstants.NODE_TYPE);
		boolean canUpload=false;
		boolean canDownload=false;
		boolean canDel=false;
		
		if("add".equals(node_type)){
			canUpload=true;
			canDownload=true;
			canDel=true;
			CpWinUtil
			.refshAttachList("main", "attachout",
					pk, canUpload, canDownload, canDel);
		}else if("power".equals(node_type)){
		    canUpload=false;
                    canDownload=true;
                    canDel=false;
			
			CpWinUtil
			.refshAttachList("main", "attachout",
					pk, canUpload, canDownload, canDel);
		}else if("query".equals(node_type)){
			canUpload=false;
			canDownload=true;
			canDel=false;
			CpWinUtil
			.refshAttachList("main", "attachout",
					pk, canUpload, canDownload, canDel);
		}else if("approve".equals(node_type)){
                    canUpload=false;
                    canDownload=true;
                    canDel=false;
                    CpWinUtil
                    .refshAttachList("main", "attachout",
                                    pk, canUpload, canDownload, canDel);
                }else {
			CpWinUtil
			.refshAttachList("main", "attachout",
					pk, false, false, false);
		}
			
		LfwView view = CpWinUtil.getView("attachlist");
		view.addAttribute(new ExtAttribute(IGlobalConstants.ATTACH_BILL_ID,pk));
		view.addAttribute(new ExtAttribute(
				IGlobalConstants.ATTACH_CAN_UPLOAD,getStrByBool(canUpload)));
		view.addAttribute(new ExtAttribute(
				IGlobalConstants.ATTACH_CAN_DOWNLOAD, getStrByBool(canDownload)));
		view.addAttribute(new ExtAttribute(
				IGlobalConstants.ATTACH_CAN_DEL,getStrByBool(canDel)));
  }
  public String getStrByBool(  boolean flag){
    if (flag) {
			return "true";
		} else {
			return "false";
		}
  }
  /** 
 * ����
 */
  public void onAdd(  MouseEvent<?> mouseEvent) throws BusinessException {
    this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_BillID, null);
		this.resetWfmParameter();
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_FolwTypePk, this.getFlwTypePk());
		
		CmdInvoker.invoke(new UifAddCmd(this.getMasterDsId()){
			protected void onBeforeRowAdd(Row row){
				setAutoFillValue(row);
			}
		});
  }
  /** 
 * ��ӡ
 * @param mouseEvent
 * @throws BusinessException
 */
  public void onPrint(  MouseEvent<?> mouseEvent) throws BusinessException {
    Dataset masterDs = this.getMasterDs();
		Row row = masterDs.getSelectedRow();
		if (row == null){
			throw new LfwRuntimeException("��ѡ������!");
		}
		try{
			List<Dataset> list = new ArrayList<Dataset>(1);
			list.add(masterDs);
			DefaultPrintService printService = new DefaultPrintService();
			printService.setDatasetList(list);
			ICpPrintTemplateService service = ServiceLocator.getService(ICpPrintTemplateService.class);
			service.print(printService, null, this.getNodeCode());
		}
		catch(Exception e){
			LfwLogger.error(e);
			throw new LfwRuntimeException(e.getMessage());
		}
  }
  private String getNodeCode(){
    return "�����˴�ӡģ��Ĺ��ܽڵ��nodecode";
  }
  private String getFlwTypePk(){
    //    return "0001ZZ100000000HGRSX";
      return "0001ZZ100000000HGQ9D";
  }
  private void resetWfmParameter(){
    this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_TaskPk, null);
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_ScratchPad, null);
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.BILLSTATE,null);
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.AttachFileList_Temp_Billitem,null);
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_ProInsPk,null);
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.RETURN_PK_TASK, null);
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_FormInFoCtx_Billitem, null);
  }
  /** 
 * ɾ��
 */
  public void onDelete(  MouseEvent<?> mouseEvent) throws BusinessException {
    String pk_form = LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParameter("openBillId");
		if(pk_form == null){
			pk_form = (String)LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParameter("pk_material_h");
			LfwRuntimeEnvironment.getWebContext().getWebSession().addOriginalParameter("openBillId", pk_form);
		}
		if(pk_form != null && !pk_form.equals("")){
			boolean isCanDel = WfmCPUtilFacade.isCanDelBill(pk_form);
			if(isCanDel){
				WfmCPUtilFacade.delWfmInfo(pk_form);
				CmdInvoker.invoke(new UifDelCmdRV(this.getMasterDsId()));
			}
			else{
				throw new LfwRuntimeException("�������������޷�ɾ������");
			}
		}else{
			throw new LfwRuntimeException("δ��ȡ�����̵�������ֵ");
		}
  }
  /** 
 * ����
 */
  public void onBack(  MouseEvent<?> mouseEvent) throws BusinessException {
    this.getCurrentAppCtx().closeWinDialog();
  }
  /** 
 * ����
 */
  public void onCopy(  MouseEvent<?> mouseEvent) throws BusinessException {
    this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_BillID, null);
		this.resetWfmParameter();		
		CmdInvoker.invoke(new UifCopyCmd(this.getMasterDsId()));
  }
  public void doTaskExecute(  Map keys){
    onSave(null);
      
    //ƽ̨Ĭ��У��
		new DefaultDataValidator().validate(this.getMasterDs(), this.getCurrentView());
		WfmFormInfoCtx formCtx = this.getWfmFormInfoCtx();
		//��������form
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_FormInFoCtx, formCtx);
		//������������pk
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_FolwTypePk, this.getFlwTypePk());
		//��������pk
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_TaskPk, this.getPkTask());
		// ��������
		CmdInvoker.invoke(new WfmCmd());
		if (CommandStatus.SUCCESS.equals(CommandStatus.getCommandStatus())) {
			CmdInvoker.invoke(new UifUpdateUIDataCmdRV((SuperVO)formCtx, getMasterDsId()));
			
			disableLfwView(this.getCurrentView(),null);
			
//			boolean canUpload=false;
//		          boolean canDownload=true;
//		          boolean canDel=false;
//		          
//		              CpWinUtil
//		              .refshAttachList("main", "attachout",
//		                      formCtx.getFrmInsPk(), canUpload, canDownload, canDel);
		          
			// ����ҳ��ı�״̬
		            AppLifeCycleContext.current().getViewContext().getView()
		                    .getWindow().setHasChanged(false);

		            AppLifeCycleContext
		                    .current()
		                    .getApplicationContext()
		                    .getCurrentWindowContext()
		                    .closeView(
		                            AppLifeCycleContext.current().getViewContext()
		                                    .getId());

		            this.getCurrentAppCtx().closeWinDialog("materialuiComps.material_cardwin");

		            Map<String, Object> paramMap = new HashMap<String, Object>();

		            CmdInvoker.invoke(new UifPlugoutCmd(this.getCurrentView().getId(),
		                    PLUGOUT_ID, paramMap));
		}
  }
  /** 
 * �ӱ�����
 */
  public void onGridAddClick(  MouseEvent<?> mouseEvent){
    GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		Dataset ds = this.getCurrentView().getViewModels().getDataset(dsId);
		Row emptyRow = ds.getEmptyRow();
		ds.addRow(emptyRow);
		ds.setRowSelectIndex(ds.getRowIndex(emptyRow));
		ds.setEnabled(true);
  }
  /** 
 * �ӱ�༭
 */
  public void onGridEditClick(  MouseEvent<?> mouseEvent){
    GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		Dataset ds = this.getCurrentView().getViewModels().getDataset(dsId);
		ds.setEnabled(true);
  }
  /** 
 * �ӱ�ɾ��
 */
  public void onGridDeleteClick(  MouseEvent<?> mouseEvent){
    GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		CmdInvoker.invoke(new UifLineDelCmd(dsId));
  }
  @Override protected String getMasterDsId(){
    return "material_h";
  }
  protected WfmFormInfoCtx getWfmFormInfoCtx(){
    Dataset masterDs = this.getMasterDs();
		Dataset[] detailDss = this.getDetailDs(this.getDetailDsIds());
		SuperVO richVO = this.getDs2RichVOSerializer().serialize(masterDs, detailDss, this.getRichVoClazz());
		return (WfmFormInfoCtx) richVO;
  }
  protected String getRichVoClazz(){
    return WfmFlwFormVO.class.getName();
  }
  public void onDataLoad(  DataLoadEvent dataLoadEvent){
    Dataset ds = dataLoadEvent.getSource();
	  	CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
  }
  public void onSave(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
		CmdInvoker.invoke(new UifSaveCmdRV(this.getMasterDsId(), this.getDetailDsIds(), false));
//		
		if (mouseEvent != null) {
		    ds.setEnabled(true);
		    AppInteractionUtil.showMessageDialog("����ɹ�!");
                    // ����ҳ��ı�״̬
                    AppLifeCycleContext.current().getViewContext().getView()
                            .getWindow().setHasChanged(false);

                    AppLifeCycleContext
                            .current()
                            .getApplicationContext()
                            .getCurrentWindowContext()
                            .closeView(
                                    AppLifeCycleContext.current().getViewContext()
                                            .getId());

                    this.getCurrentAppCtx().closeWinDialog();

                    Map<String, Object> paramMap = new HashMap<String, Object>();

                    CmdInvoker.invoke(new UifPlugoutCmd(this.getCurrentView().getId(),
                            PLUGOUT_ID, paramMap));
		}
  }
  /** 
 * ����ҳ��İ�ť,��ֹ�޸�ҳ��ı��ͱ�
 * @param lfwView
 * @param enableBtnMap �г�����ʹ�õ�view id��Ӧ��ťid
 */
  public static void disableLfwView(  LfwView lfwView,  Map<String,String[]> enableBtnMap){
    if(lfwView==null) return;
          ViewComponents views = lfwView.getViewComponents();
          if(views==null) return;
          
          //���ز˵��еı��水ť
          ViewMenus menus = lfwView.getViewMenus();
          
          if(menus!=null){
                  MenubarComp menuBar = menus.getMenuBar("menubar");
                  if(menuBar!=null)
                          disableButton(menuBar,"save");
          }
          
          disableApproveBtns(lfwView);
          
          WebComponent[] comps = views.getComponents();
          if(comps==null||comps.length==0) return;
          ViewModels models = lfwView.getViewModels();
          
          for(WebComponent comp : comps){
                  if(comp instanceof FormComp){
                          FormComp form = (FormComp)comp;
                          String dsId = form.getDataset();
                          disableDataset(models,dsId);
                  }else if(comp instanceof GridComp){
                          GridComp grid = (GridComp)comp;
                          String dsId = grid.getDataset();
                          disableDataset(models,dsId);
                          MenubarComp gridMenuBar = grid.getMenuBar();
                          if(enableBtnMap!=null){
                                  disableButtonExcept(gridMenuBar,enableBtnMap.get(grid.getId()));
                          }else{
                                  disableButton(gridMenuBar);
                          }
                  }
          }
          
          GridComp attchlist= (GridComp) AppLifeCycleContext.current().getWindowContext().getWindow().getView("attachlist").getViewComponents().getComponent("attach_grid");
          MenubarComp clmc=attchlist.getMenuBar();
          if (clmc != null && clmc.getItem("attach_grid$HeaderBtn_Upload") != null) {
              clmc.getItem("attach_grid$HeaderBtn_Upload").setVisible(false);
          }
          if (clmc != null && clmc.getItem("attach_grid$HeaderBtn_Delete") != null) {
              clmc.getItem("attach_grid$HeaderBtn_Delete").setVisible(false);
          }
  }
  private static void disableApproveBtns(  LfwView lfwView){
    LfwWindow win = lfwView.getWindow();
          if(win==null) return;
          LfwView pubview_simpleexetask = win.getView("pubview_simpleexetask");
          if (pubview_simpleexetask == null)
                  return;
          MenubarComp pubbar = pubview_simpleexetask.getViewMenus().getMenuBar("simpleExeMenubar");
          if (pubbar == null)
                  return;
          List<MenuItem> menus = pubbar.getMenuList();
          for (MenuItem menu : menus) {
                  menu.setEnabled(false);
          }
  }
  private static void disableDataset(  ViewModels models,  String dsId){
    if(models==null) return;
      Dataset ds = models.getDataset(dsId);
      if(ds==null) return;
      ds.setEnabled(false);
  }
  private static void disableButtonExcept(  MenubarComp menuBar,  String[] enableBtns){
    if(menuBar==null) return;
          Map<String,String> enableMap = new HashMap<String,String>();
          if(enableBtns!=null&&enableBtns.length!=0){
                  for(String btnId : enableBtns){
                          enableMap.put(btnId, btnId);
                  }
          }
          List<MenuItem> items = menuBar.getMenuList();
          if(items==null||items.isEmpty()) return;
          for(MenuItem item:items){
                  if(!enableMap.containsKey(item.getId()))
                          item.setEnabled(false);
          }
  }
  private static void disableButton(  MenubarComp menuBar,  String... btns){
    if(menuBar==null) return;
          if(btns==null||btns.length==0){
                  List<MenuItem> items = menuBar.getMenuList();
                  if(items==null||items.isEmpty()) return;
                  for(MenuItem item:items){
                          item.setEnabled(false);
                  }
          }else{
                  for(String btn:btns){
                          if(menuBar.getItem(btn)!=null){
                                  menuBar.getItem(btn).setEnabled(false);
                          }
                  }
          }
  }
  public void onDownLoadFile(  MouseEvent mouseEvent){
    
  }
}
