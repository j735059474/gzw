package nc.uap.lfw.core.cmd;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.md.innerservice.MDQueryService;
import nc.md.model.IBusinessEntity;
import nc.md.model.MetaDataException;
import nc.uap.cpb.baseservice.IUifCpbService;
import nc.uap.cpb.log.CpLogger;
import nc.uap.cpb.persist.dao.PtBaseDAO;
import nc.uap.lfw.core.cache.LfwCacheManager;
import nc.uap.lfw.core.cmd.base.UifCommand;
import nc.uap.lfw.core.common.ExtAttrConstants;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.ViewContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.data.DatasetRelations;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.exception.LfwValidateException;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.serializer.impl.Dataset2SuperVOSerializer;
import nc.uap.lfw.core.serializer.impl.Datasets2RichVOSerializer;
import nc.uap.lfw.core.uif.delegator.DefaultDataValidator;
import nc.uap.lfw.core.uif.delegator.IDataValidator;
import nc.uap.lfw.md.LfwWfmBizItf;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;

import org.apache.commons.lang.StringUtils;

import uap.lfw.core.md.util.LfwMdUtil;
import uap.web.bd.pub.CpSqlTranslateUtil;
import uap.web.bd.vo.IBillStatus;

/**
 * 通用保存命令,区别于UifSaveCmd的以聚合VO为保存对象，本命令以RichVO为保存对象
 * 如果是在弹出页面进行保存    需刷新父页面   需要传入父页面parentViewId   parentDsId   parentDetailDsIds
 *加入保存前验证vo的方法
 */
public class UifSaveCmdRV extends UifCommand {
	private static final String IFLOW_BIZ_ITF = "nc.itf.uap.pf.metadata.IFlowBizItf";
	private String masterDsId;
	private String[] detailDsIds;
	private boolean bodyNotNull;
	private List<String> notNullBodyList;
	private String billPk;
	//是否是弹出编辑页面
	private boolean isEditView = false;
	//父页面ID
	private String parentViewId;
	//父页面主ds
	private String parentDsId;
	//父页面子ds
	private String[] parentDetailDsIds;
	
	private Map<String,String> checkFieldMap;
	
	private Datasets2RichVOSerializer richVOSer;
	
	private String editState;
	private static String ADD_OPER = "add_oper";
	private static String EDIT_OPER = "edit_oper";
	
	public String getBillPk() {
		return billPk;
	}
	public void setBillPk(String billPk) {
		this.billPk = billPk;
	}
	public List<String> getNotNullBodyList() {
		return notNullBodyList;
	}
	public void setNotNullBodyList(List<String> notNullBodyList) {
		this.notNullBodyList = notNullBodyList;
	}
	public UifSaveCmdRV(String masterDsId, String[] detailDsIds, boolean bodyNotNull) {
		this.masterDsId = masterDsId;
		this.detailDsIds = detailDsIds;
		this.bodyNotNull = bodyNotNull;
	}
	public UifSaveCmdRV(String masterDsId, String[] detailDsIds) {
		this(masterDsId, detailDsIds, true);
	}
	public void execute() {
		ViewContext viewCtx = getLifeCycleContext().getViewContext();
		LfwView widget = viewCtx.getView();
		if (widget == null)
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pub", "UifSaveCmd-000000")/*片段为空!*/);
		if (this.masterDsId == null)
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pub", "UifSaveCmd-000001")/*未指定主数据集id!*/);
		Dataset masterDs = widget.getViewModels().getDataset(masterDsId);
		if (masterDs == null)
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pub", "UifMultiDelCmd-000001")/*数据集为空,数据集id=*/ + masterDsId + "!");
		List<String> idList = new ArrayList<String>();
		idList.add(masterDsId);
		if (detailDsIds != null && detailDsIds.length > 0)
			idList.addAll(Arrays.asList(detailDsIds));
		ArrayList<Dataset> detailDs = new ArrayList<Dataset>();
		if (detailDsIds != null && detailDsIds.length > 0) {
			for (int i = 0; i < detailDsIds.length; i++) {
				Dataset ds = widget.getViewModels().getDataset(detailDsIds[i]);
				if (ds != null)
					detailDs.add(ds);
			}
		}
		doValidate(masterDs, detailDs);
		
		Dataset[] detailDss = detailDs.toArray(new Dataset[0]);
		SuperVO richVO = getRichVO(masterDs, detailDss);
		if(richVO == null)
			return;
		
		if(richVO.getStatus() == VOStatus.NEW)
			editState = ADD_OPER;
		else
			editState = EDIT_OPER;
		
		fillCachedDeletedVO(richVO, detailDss);
		try {
			setBillStatus(masterDs, richVO);
			//如果有唯一字段验证 则重写此方法   调用getCheckFieldMap()  like getCheckFieldMap().put(WfmFlwTypeVO.TYPENAME, "流程名称");
			setCheckField();
			// 保存前检查唯一性
			onBeforeVOSave(richVO);
			// 检查是否有阻止保存的异常
			if (!checkBeforeVOSave(richVO))
				return;
			onVoSave(richVO);
		} 
		catch (Exception e) {
			dealWithException(e);
		}
		//如果需要刷新父页面   需重写此方法设置父页面参数
		setParentView();
		// 保存后执行的逻辑
		if(!isEditView)
			onAfterVOSave(widget, masterDs, detailDss, richVO);
		else{
			onUpdateParentView(richVO);
			//设置页面改变状态
			getLifeCycleContext().getViewContext().getView().getPagemeta()
					.setHasChanged(false);
			//关闭当前窗口
			getLifeCycleContext()
					.getApplicationContext()
					.getCurrentWindowContext()
					.closeView(
							AppLifeCycleContext.current().getViewContext().getId());
		}
		onAfterSave(masterDs, detailDss);	
	}
	
	
	private Datasets2RichVOSerializer getRichVOSerializer() {
		if(richVOSer == null)
			richVOSer = new Datasets2RichVOSerializer();
		return richVOSer;
	}
	/**
	 * 如果需要刷新父页面   需重写此方法设置父页面参数  将isEditView设置为true   并传入父页面参数  如
	 */
	protected void setParentView(){
		return;
	}
	
	//判断字段是否已经存在
	@SuppressWarnings("unchecked")
	protected void checkDupliVO(SuperVO vo) {
		StringBuffer whereSql = new StringBuffer(" 1 = 2 ");
		StringBuffer message = new StringBuffer();
		
		if(checkFieldMap != null && checkFieldMap.size() > 0){
			Iterator<Entry<String, String>> it = checkFieldMap.entrySet().iterator();
			while(it.hasNext()){
				Entry<String, String> entry = it.next();
				String key = entry.getKey();
				String value = (String) vo.getAttributeValue(key);
				whereSql.append(" OR ").append(key).append(" = '").append(CpSqlTranslateUtil.tmsql(value)).append("' ");
				message.append((String)entry.getValue());
				if(it.hasNext())
					message.append("/");
			}
			message.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("pub", "UifSaveCmd-000004")/*已经存在,请重新输入!*/);
		}
		try {
			List<SuperVO> vos = (List<SuperVO>) new PtBaseDAO().retrieveByClause(vo.getClass(), whereSql.toString());
			if(vos != null && vos.size() > 0){
				for(SuperVO  suervo : vos){
					String pk = suervo.getPrimaryKey();
					if(!StringUtils.isBlank(pk)&&!pk.equals(vo.getPrimaryKey()))
						throw new LfwRuntimeException(message.toString());
				}
			}
		} catch (DAOException e) {
			CpLogger.error(e.getMessage());
			throw new LfwRuntimeException(e.getMessage());
		}	
	}
	
	protected void onUpdateParentView(SuperVO richVO){
		if(StringUtils.isNotBlank(parentViewId) && StringUtils.isNotBlank(parentDsId)){
			ViewContext parentViewCtx = getLifeCycleContext().getWindowContext().getViewContext(parentViewId);
			if(parentViewCtx==null)
				throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pub", "UifEditOkClickCmd-000002")/*父窗口不能为空*/);
			LfwView parentWidget = parentViewCtx.getView();
			Dataset parentDs = parentWidget.getViewModels().getDataset(parentDsId);
			if(parentDs==null)
				throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pub", "UifSaveCmd-000007")/*父窗口主数据集不能为空*/);
			ArrayList<Dataset> parentDetailDs = null;
//			Dataset[] parentDetailDss = null ;
//			if (parentDetailDsIds != null && parentDetailDsIds.length > 0) {
//				parentDetailDs = new ArrayList<Dataset>();
//				for (int i = 0; i < parentDetailDsIds.length; i++) {
//					Dataset ds = parentWidget.getViewModels().getDataset(parentDetailDsIds[i]);
//					if (ds != null)
//						parentDetailDs.add(ds);
//				}
//				if(parentDetailDs!=null&&parentDetailDs.size()!=0)
//					parentDetailDss = parentDetailDs.toArray(new Dataset[0]);
//			}
			
//			LfwBeanUtil<SuperVO> bu = new LfwBeanUtil<SuperVO>();
//			//如果是添加操作   rowId需要设置为空
//			if(isAddOper()){
//				bu.setUIID(richVO,null);
//			}
//			else{
//				Row row = parentDs.getSelectedRow();
//				if(row == null )
//					throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pub", "UifSaveCmd-000008")/*父页面选中行为空！*/); 
//				bu.setUIID(richVO, row.getRowId());
//			}
			onAfterVOSave(parentWidget, parentDs, new Dataset[0], richVO);
		}
	}
	
	//如果有唯一字段验证 则重写此方法   调用getCheckFieldMap()  like getCheckFieldMap().put(WfmFlwTypeVO.TYPENAME, "流程名称");
	protected void setCheckField(){
		return;
	}
	
	
//	/**
//	 * 如果是add操作  返回true  否则返回false   
//	 * @return
//	 */
//	protected boolean isAddOper(){
//		boolean isadd = false;
//		String oper = (String) getLifeCycleContext().getWindowContext().getAppAttribute(UifAddOrEditMenuClickCmd.OPERATE_STATUS);
//		if(StringUtils.isBlank(oper)){
//			if(ADD_OPER.equals(editState))
//				isadd = true;
//		}
//		else if(ADD_OPER.equals(oper))
//			isadd = true;
//		
//		return isadd;
//	}
	
	/**
	 * 保存完成之后调用逻辑
	 * 
	 * @param masterDs
	 * @param detailDss
	 */
	protected void onAfterSave(Dataset masterDs, Dataset[] detailDss) {
		masterDs.setEnabled(false);
		for (int i = 0; i < detailDss.length; i++) {
			detailDss[i].setEnabled(false);
		}
		updateButtons();
		
		Row rows[] = masterDs.getAllRow();
		
		for(int i=0;i<rows.length;i++){
			rows[i].setState(0);
		}
		
		for (int j = 0; j < detailDss.length; j++) {
			Dataset ds = detailDss[j];
			Row[] childrows=ds.getCurrentRowSet().getCurrentRowData().getRows();
			
			for(int i=0;i<childrows.length;i++){
				childrows[i].setState(0);
			}
		}
	}
	
	/**
	 * 异常的处理类
	 * 
	 * @param e
	 */
	protected void dealWithException(Exception e) {
		Logger.error(e, e);
		if(e instanceof LfwValidateException){
			throw (LfwValidateException)e;
		}
		throw new LfwRuntimeException(e.getMessage());
	}
	/**
	 * 保存后操作后, 重新设置页面数据
	 * 
	 * @param widget
	 * @param masterDs
	 * @param ser
	 * @param detailDss
	 * @param richVO
	 */
	protected void onAfterVOSave(LfwView widget, Dataset masterDs, Dataset[] detailDss, SuperVO richVO) {
		
		if(detailDss == null){
			DatasetRelations dsRels = widget.getViewModels().getDsrelations();
			if(dsRels != null){
				DatasetRelation[] drs = dsRels.getDsRelations(masterDs.getId());
				if(drs != null && drs.length > 0){
					detailDss = new Dataset[drs.length];
					for(int i = 0 ; i < drs.length ; ++i)
						detailDss[i] = widget.getViewModels().getDataset(drs[i].getDetailDataset());
				}
			}
		}
		
		if (detailDss != null && detailDss.length > 0) {
			DatasetRelation dr = widget.getViewModels().getDsrelations().getDsRelation(masterDs.getId(), detailDss[0].getId());
			String newKeyValue = (String) richVO.getAttributeValue(dr.getMasterKeyField());
			for (int i = 0; i < detailDss.length; i++) {
				if (newKeyValue != null && !newKeyValue.equals(detailDss[i].getCurrentKey()))
					detailDss[i].replaceKeyValue(detailDss[i].getCurrentKey(), newKeyValue);
			}
		}
		getRichVOSerializer().update(richVO, masterDs, detailDss);
		
		String primaryKey = null;
		Field[] fields = masterDs.getFieldSet().getFields();
		for (int i = 0; i < fields.length; i++) {
			if(fields[i].isPrimaryKey()){
				primaryKey = fields[i].getField();
				break;
			}
		}
		if(primaryKey != null){
			String primaryValue = (String) richVO.getAttributeValue(primaryKey);
			setBillPk(primaryValue);
		}
	}
	// 如果有记录的删除的子表行，则将子表的vo标志为delete状态
	@SuppressWarnings("unchecked")
	protected void fillCachedDeletedVO(SuperVO richVO, Dataset[] detailDss) {
		if (LfwCacheManager.getSessionCache() == null)
			return;
		if (richVO.getPrimaryKey() == null)
			return;
		// 删除放入缓存中的数据
		List<SuperVO> delBodyVoList = (List<SuperVO>) LfwCacheManager.getSessionCache().get(richVO.getPrimaryKey());
		int dbvSize = delBodyVoList != null ? delBodyVoList.size() : 0;
		if (dbvSize == 0)
			return;
		
		Dataset2SuperVOSerializer<SuperVO> ser = new Dataset2SuperVOSerializer<SuperVO>();
		Datasets2RichVOSerializer rser = new Datasets2RichVOSerializer();
		
		int dLen = detailDss != null ? detailDss.length : 0;
		for (int i = 0; i < dLen; i++){
			Dataset detailDs = detailDss[i];
			if(detailDs == null){
				continue;
			}
			String delRowForeignKey = richVO.getPrimaryKey() + "_" + detailDs.getId();
			// 子ds中删除的行
			List<Row> delRowList = (List<Row>) LfwCacheManager.getSessionCache().get(delRowForeignKey);
			if (delRowList == null || delRowList.size() == 0){
				continue;
			}
			// 将删除的数据序列化
			SuperVO[] vos = ser.serialize(detailDs, delRowList.toArray(new Row[0]));
			int len = vos != null ? vos.length : 0;
			if(len > 0){
				rser.add(richVO, vos, detailDs.getVoMeta());
			}
			
			LfwCacheManager.getSessionCache().remove(delRowForeignKey);
		}

		rser.updateStatus(richVO, delBodyVoList);

		LfwCacheManager.getSessionCache().remove(richVO.getPrimaryKey());
	}
	protected void setBillStatus(Dataset masterDs, SuperVO richVO) {
		Object metaObj = masterDs.getExtendAttributeValue(ExtAttrConstants.DATASET_METAID);
		if (metaObj != null) {
			String metaId = metaObj.toString();
			try {
				IBusinessEntity entity = MDQueryService.lookupMDQueryService().getBusinessEntityByFullName(metaId);
				String billStateColumn = LfwMdUtil.getMdItfAttr(entity, IFLOW_BIZ_ITF, LfwWfmBizItf.ATTRIBUTE_FORMSTATE);
				if (billStateColumn != null) {
					Integer state = (Integer) richVO.getAttributeValue(billStateColumn);
					if (state == null)
						richVO.setAttributeValue(billStateColumn, IBillStatus.FREE);
				}
			} catch (MetaDataException e) {
				Logger.error(e.getMessage(), e);
			}
		}
	}
	protected void doValidate(Dataset masterDs, List<Dataset> detailDs) throws LfwValidateException {
		ViewContext viewCtx = getLifeCycleContext().getViewContext();
		LfwView widget = viewCtx.getView();
		IDataValidator validator = getValidator();
		validator.validate(masterDs, widget);
		if (detailDs != null) {
			int size = detailDs.size();
			if (size > 0) {
				for (int i = 0; i < size; i++) {
					Dataset ds = detailDs.get(i);
					validator.validate(ds, widget);
					if (notNullBodyList != null && notNullBodyList.contains(ds.getId())) {
						doSingleValidateBodyNotNull(ds);
					}
				}
				if (bodyNotNull) {
					doValidateBodyNotNull(detailDs);
				}
			}
		}
	}
	/**
	 * 检查单个dataset是否存在数据
	 * 
	 * @param detailDs
	 * @throws LfwValidateException
	 */
	protected void doSingleValidateBodyNotNull(Dataset detailDs) throws LfwValidateException {
		boolean hasBody = false;
		if (detailDs.getCurrentRowData() == null) {
			hasBody = false;
		}
		if (detailDs.getCurrentRowCount() > 0) {
			hasBody = true;
		}
		if (!hasBody) {
			throw new LfwValidateException(detailDs.getCaption() + NCLangRes4VoTransl.getNCLangRes().getStrByID("pub", "UifSaveCmd-000002")/*表体数据不能为空*/);
		}
	}
	protected void doValidateBodyNotNull(List<Dataset> detailDs) throws LfwValidateException {
		int size = detailDs.size();
		for (int i = 0; i < size; i++) {
			Dataset ds = detailDs.get(i);
			//此表体数据不能为空
			if(ds.isNotNullBody()){
				if (ds.getCurrentRowData() == null) {
					throw new LfwValidateException(ds.getCaption() + NCLangRes4VoTransl.getNCLangRes().getStrByID("pub", "UifSaveCmd-000003")/*的表体数据不能为空*/);
				}
			}
		}
	}
	protected IDataValidator getValidator() {
		return new DefaultDataValidator();
	}
	
	/**
	 * 保存前校验
	 * 
	 * @param richVO
	 */
	protected void onBeforeVOSave(SuperVO richVO) {
		checkDupliVO(richVO);
	}
	/***
	 * 检查是否有阻止保存的异常抛出
	 * 
	 * @param richVO
	 * @return
	 */
	protected boolean checkBeforeVOSave(SuperVO richVO) throws Exception {
		return true;
	}
	protected void onVoSave(SuperVO aggVo) {
		try {
			IUifCpbService cpbService = NCLocator.getInstance().lookup(IUifCpbService.class);
			cpbService.insertOrUpdateSuperVO(aggVo,this.isNotifyBDCache());
			//getCrudService().saveBusinessVO(aggVo);
		} catch (BusinessException e) {
			Logger.error(e, e);
			throw new LfwRuntimeException(e.getMessage());
		}
		
	}
	public void setEditView(boolean isEditView) {
		this.isEditView = isEditView;
	}
	public boolean isEditView() {
		return isEditView;
	}
	public String getParentViewId() {
		return parentViewId;
	}
	public void setParentViewId(String parentViewId) {
		this.parentViewId = parentViewId;
	}
	public String getParentDsId() {
		return parentDsId;
	}
	public void setParentDsId(String parentDsId) {
		this.parentDsId = parentDsId;
	}
	public String[] getParentDetailDsIds() {
		return parentDetailDsIds;
	}
	public void setParentDetailDsIds(String[] parentDetailDsIds) {
		this.parentDetailDsIds = parentDetailDsIds;
	}
	public void setCheckFieldMap(Map<String,String> checkFieldMap) {
		this.checkFieldMap = checkFieldMap;
	}
	public Map<String,String> getCheckFieldMap() {
		if(checkFieldMap==null)
			checkFieldMap = new HashMap<String, String>();
		return checkFieldMap;
	}
	
	protected SuperVO getRichVO(Dataset masterDs, Dataset[] detailDss){
		return getRichVOSerializer().serialize(masterDs, detailDss);
	}
}
