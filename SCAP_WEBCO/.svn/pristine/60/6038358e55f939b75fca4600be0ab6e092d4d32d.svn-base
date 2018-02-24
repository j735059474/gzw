package nc.scap.pub.selfquery;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import nc.jdbc.framework.processor.ColumnProcessor;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.CpOrgUtil;
import nc.scap.pub.util.ScapDAO;
import nc.scap.sysinit.constant.ISysinitConstant;
import nc.scap.sysinit.util.ScapSysinitUtil;
import nc.uap.cpb.log.CpLogger;
import nc.uap.ctrl.tpl.TemplateConstant;
import nc.uap.ctrl.tpl.exp.TplBusinessException;
import nc.uap.ctrl.tpl.qry.ICpQryTemplateInnerQryService;
import nc.uap.ctrl.tpl.qry.QueryTemplateUtils;
import nc.uap.ctrl.tpl.qry.SimpleQueryWidgetProvider;
import nc.uap.ctrl.tpl.qry.base.CpQueryTemplateTotalVO;
import nc.uap.ctrl.tpl.qry.base.CpQueryTemplateTranslator;
import nc.uap.ctrl.tpl.qry.base.CpQueryTemplateVO;
import nc.uap.ctrl.tpl.qry.base.IQueryConstArgs;
import nc.uap.ctrl.tpl.qry.base.QueryResult;
import nc.uap.ctrl.tpl.qry.base.QuerySchemeObject;
import nc.uap.ctrl.tpl.qry.base.QuerySchemeUtils;
import nc.uap.ctrl.tpl.qry.base.QuerySchemeVO;
import nc.uap.ctrl.tpl.qry.meta.AndRule;
import nc.uap.ctrl.tpl.qry.meta.ContainerRule;
import nc.uap.ctrl.tpl.qry.meta.DefaultFilter;
import nc.uap.ctrl.tpl.qry.meta.FilterMeta;
import nc.uap.ctrl.tpl.qry.meta.IQueryConstants;
import nc.uap.ctrl.tpl.qry.meta.IRule;
import nc.uap.ctrl.tpl.qry.meta.QueryRule;
import nc.uap.ctrl.tpl.qry.operator.AndOperator;
import nc.uap.ctrl.tpl.qry.operator.OrOperator;
import nc.uap.lfw.app.plugin.AppControlPlugin;
import nc.uap.lfw.core.AbstractPresentPlugin;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.base.ExtAttribute;
import nc.uap.lfw.core.base.ExtendAttributeSupport;
import nc.uap.lfw.core.combodata.CombItem;
import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.common.EditorTypeConst;
import nc.uap.lfw.core.common.StringDataTypeConst;
import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.comp.ImageComp;
import nc.uap.lfw.core.comp.LabelComp;
import nc.uap.lfw.core.comp.LinkComp;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.core.event.conf.EventSubmitRule;
import nc.uap.lfw.core.event.conf.ViewRule;
import nc.uap.lfw.core.model.IViewProviderForDesign;
import nc.uap.lfw.core.model.IWidgetContentProvider;
import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.core.page.IPluginDesc;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.page.PluginDesc;
import nc.uap.lfw.core.page.PlugoutDesc;
import nc.uap.lfw.core.refnode.NCRefNode;
import nc.uap.lfw.core.sysvar.ISysvarProvider;
import nc.uap.lfw.core.uimodel.ViewConfig;
import nc.uap.lfw.core.uimodel.WindowConfig;
import nc.uap.lfw.core.util.LFWAllComponetsFetcher;
import nc.uap.lfw.util.LanguageUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;

import org.apache.commons.lang.StringUtils;

import uap.lfw.core.language.LfwLanguageVO;
import uap.lfw.core.locator.ServiceLocator;
import uap.lfw.core.ml.LfwResBundle;
import uap.lfw.imp.math.CheckUtils;
import uap.lfw.imp.query.base.CpQueryMetasFilter;
import uap.lfw.imp.query.base.QuerySchemeValidHelper;
import uap.lfw.imp.query.comp.SimpleQueryUtils;
import uap.web.bd.pub.AppUtil;
import uap.web.bd.pub.user.CpUserManager;

public class SelfQueryWidgetProvider implements IWidgetContentProvider, IViewProviderForDesign {
	protected static final String TOTALVO_KEY = ExtendAttributeSupport.DYN_ATTRIBUTE_KEY + "TEMP_TOTALVO";
	private static final String METHOD_BEFORE_SHOW_NEED_LOAD = "beforeShowLoad";
	private static final String COND_QUERY_PLUGIN = "conditionQueryPlugin";
	//更新公共查询view的plugin
	private static final String REFRESH_VIEW_PLUGIN = "refreshViewPlugin";
	private static final String METHOD_ON_QUERY_CLICK = "onQueryClick";
	private static final String METHOD_SIMPLE_VALUE_CHANGED = "simpleValueChanged";
	private static final String METHOD_SIMPLE_QUERYON_DATA_LOAD = "simpleQueryonDataLoad";
	public static final String QRYOUT = "qryout";
	public static final String REFRESH_VIEW_PLUGOUT = "refreshViewPluginOut";
	public static final String NOCOND_QUERY_PLUGIN = "noconditionQueryPlugin";
	public static final String MAINDS = "mainds";
	public static final String MASTERDS = "masterds";
	public static final String MAINFORM = "mainform";
	public static final String NODEKEY = "nodekey";

	private CpQueryTemplateTotalVO totalVO;
	private String sys_metaclass;
	private String[] fieldCodes;
	@Override
	public LfwView buildWidget(LfwWindow pm, LfwView conf, Map<String, Object> paramMap, String currViewId) {
		
		LfwView view = new LfwView();
		view.setId(currViewId);
		view.setControllerClazz(SelfQueryController.class.getName());
		//生成输入和输出
		genPlugoutAndPlugins(view);
	
		//不可个性化设置
		view.setCanFreeDesign(false);
		
		
		getQueryTemplateTotalVO(pm, conf);
		conf.setExtendAttribute(TOTALVO_KEY, totalVO);
		
		/**
		 * 创建查询方案
		 */
		String queryPlanKeys = null;
		//是否显示查询方案
		String withQueryPlan = (String) conf.getExtendAttributeValue(LfwView.WITHQUERYPLAN);
		if(withQueryPlan == null || withQueryPlan.equals("true")){
			queryPlanKeys = createQueryPlan(view, pm, conf);
		}
			
		/**
		 * 是否需要在页面初始化后执行beforeShow事件去加载数据
		 */
		String needLoad = (String) conf.getExtendAttributeValue(LfwView.AUTOQUERY);
		/*调整内容：改成true默认为从查询View开始加载 By 陶冶*/
		needLoad = "true";
		if(needLoad != null && needLoad.equals("true")){
			EventConf needLoadevent = DialogEvent.getBeforeShowEvent();
			needLoadevent.setMethodName(METHOD_BEFORE_SHOW_NEED_LOAD);
			needLoadevent.setOnserver(true);
			view.addEventConf(needLoadevent);
		}
		

		if (totalVO == null) {
			mockNullWidget(view);
		} 
		else {
			genAdvancedWindow(view);
			CpQueryTemplateTranslator loader = new CpQueryTemplateTranslator();
			//wujd 添加，过滤编辑条件
//			totalVO = filterTotalVO(totalVO);
			List<FilterMeta> defaultMetas = null;
			List<FilterMeta> requiredMetas = null;
			List<FilterMeta> usedBMetas = null;
			boolean ifDisplay = true;
			if(totalVO!=null){
				CpQueryTemplateVO templateVO = totalVO.getTempletVO();
				
				if(templateVO!=null && templateVO.getIfdisplay()!=null)
					ifDisplay = templateVO.getIfdisplay().booleanValue();
				loader.loadData(totalVO);
				defaultMetas = loader.getDefaultMetas();
				requiredMetas = loader.getRequiredMetas();
				usedBMetas = loader.getUsedMetas();
			}
			if (defaultMetas == null || defaultMetas.size() == 0) {
				if(ifDisplay)
					advanceSearchWidget(view);
				else
					mockNullWidget(view);
			} 
			else {
				String queryController = null;
				if(conf.getExtendAttribute(TemplateConstant.QUERYCONTROLLER) != null && conf.getExtendAttributeValue(TemplateConstant.QUERYCONTROLLER) != null)
					queryController = conf.getExtendAttributeValue(TemplateConstant.QUERYCONTROLLER).toString();
				if(queryController==null || "".equals(queryController)){
					if(pm.getExtendAttribute(TemplateConstant.QUERYCONTROLLER)!=null && pm.getExtendAttributeValue(TemplateConstant.QUERYCONTROLLER)!=null)
						queryController = pm.getExtendAttributeValue(TemplateConstant.QUERYCONTROLLER).toString();
					if(queryController==null || "".equals(queryController)){
						ViewConfig[] viewConfigs = pm.getViewConfigs();
						if(viewConfigs!=null && viewConfigs.length>0){
							for(ViewConfig widgetConfig : viewConfigs){
								if(widgetConfig!=null && widgetConfig.getRefId().equals("../"+conf.getRefId())){
									if(widgetConfig.getExtendAttribute(TemplateConstant.QUERYCONTROLLER)!=null && widgetConfig.getExtendAttributeValue(TemplateConstant.QUERYCONTROLLER)!=null){
										queryController = widgetConfig.getExtendAttributeValue(TemplateConstant.QUERYCONTROLLER).toString();
										break;
									}
								}
							}
						}
					}
				}
				if(queryController != null && !"".equals(queryController)){
					AppLifeCycleContext.current().getApplicationContext().addAppAttribute(TemplateConstant.QUERYCONTROLLER, queryController);
				}
				Dataset ds = new Dataset();
				ds.setId(MAINDS);
				Dataset master = new Dataset();
				master.setId(MASTERDS);
				if(view.getViewModels().getDataset(MAINDS)==null){
					view.getViewModels().addDataset(ds);
				}
				view.getViewModels().addDataset(master);
				FormComp form = new FormComp();
				form.setId(MAINFORM);
				form.setDataset(MAINDS);
				form.setEllipsis(true);
				view.getViewComponents().addComponent(form);
				Iterator<FilterMeta> it = defaultMetas.iterator();
				ISysvarProvider sysProvider = ServiceLocator.getService(ISysvarProvider.class);
				
				String refCodes = null;
				if (pm != null) {
					if(conf.getExtendAttribute(LfwWindow.$REFNODESINGLESEL) !=null){
						refCodes = (String)conf.getExtendAttribute(LfwWindow.$REFNODESINGLESEL).getValue();
					}
				}
				boolean iscur=true;
				while (it.hasNext()) {
					FilterMeta meta = it.next();
					String tmp_field = meta.getFieldCode();
					if(CheckUtils.isExist(tmp_field, fieldCodes)){
						meta.setLangSeq(LanguageUtil.getCurrentLangSeq());
						
					}
					FormElement fme = QuerySchemeUtils.setMetaToFormEle(meta, view, queryController,sysProvider,refCodes);
					if (fme == null)
						continue;
					//houlg 财务分析左侧查询条件变为单选
					if(tmp_field.equals("pk_org")){
						String queryNode = (String) AppLifeCycleContext.current().getApplicationContext().getAppAttribute(SimpleQueryWidgetProvider.SIMPLEQUERYNODECODE);;
						if(StringUtils.isBlank(queryNode) || "null".equals(queryNode)){
							queryNode = (String) AppLifeCycleContext.current().getApplicationContext().getAppSession().getOriginalParamMap().get(AppControlPlugin.NODECODE);
						}
						NCRefNode orgref=(NCRefNode) view.getViewModels().getRefNode("refnode_mainds_pk_org");
						if(orgref==null){
							 orgref=(NCRefNode) view.getViewModels().getRefNode(queryNode+"_refnode_mainds_pk_org");
						}
						if(orgref!=null){
							orgref.setMultiSel(false);
							 HashMap m=new HashMap();
							 m.put("refcode", orgref.getRefcode());
							 AppUtil.addAppAttr("report",m);
							 String provinceId= ScapSysinitUtil.getSysinitStrByCode(ISysinitConstant.PROVINCE_ID);
									if("HN".equals(provinceId)){
										 if(orgref!=null&&"预算协同当前集团+业务单元".equals(orgref.getRefcode())){
											 //orgref.setSelLeafOnly(true); //设置跟节点不可选
											 iscur=false;
										 }
									}
									if("SC".equals(provinceId)){
//										if(orgref!=null&&("预算协同当前集团+业务单元".equals(orgref.getRefcode())||
//												"快报协同当前集团+业务单元".equals(orgref.getRefcode())||"决算协同当前集团+业务单元".equals(orgref.getRefcode()))){
//											 orgref.setSelLeafOnly(true); 
											 iscur=false;
//										 }
									}
						}
						 
						}
					
					form.addElement(fme);
					String defaultCondition = meta.getDefaultCondition();
					String varType = meta.getVarType();
					int dataType = meta.getDataType();
					String defaultValue = meta.getDefaultValue();
					if(dataType==IQueryConstants.DATE||dataType==IQueryConstants.TIME || dataType==IQueryConstants.LITERALDATE || dataType == IQueryConstants.INTEGER || dataType == IQueryConstants.DECIMAL || dataType==IQueryConstants.UFTIME){
						if(null!=defaultCondition && "between".equals(defaultCondition)){
							if(StringUtils.isNotEmpty(defaultValue) && sysProvider!=null){
								String[] dateValues = defaultValue.split(",");
								if(dateValues!=null && dateValues.length>1){
									Object value = sysProvider.parseSysvar((String)dateValues[1]);
									if(value!=null){
										defaultValue = value.toString();
									}
								}
							}
							FormElement fme1 = new FormElement();
							fme1.setEditorType(fme.getEditorType());
							fme1.setId(meta.getFieldCode().replaceAll("\\.", "_")+"_end");
							fme1.setField(meta.getFieldCode().replaceAll("\\.", "_")+"_end");
							fme1.setText(LfwResBundle.getInstance().getStrByID("uapquerytemplate", "SimpleQueryWidgetProvider-000004")/*至*/);
							fme1.setDataType(fme.getDataType());
//							fme1.setI18nName(meta.getResId());
//							fme1.setLangDir(meta.getLangdir());
							fme.setNullAble(fme.isNullAble());
							form.addElement(fme1);
							Field field = new Field();
							field.setId(meta.getFieldCode().replaceAll("\\.", "_")+"_end");
							field.setText(meta.getFieldCode()+"_end");
							field.setDataType(fme.getDataType());
							field.setNullAble(fme.isNullAble());
							if(varType!=null){
								field.setDefEditFormular(varType);
							}else
								field.setDefEditFormular("MD");
							field.setDefaultValue(defaultValue);
							ds.getFieldSet().addField(field);
						}
					}
				}
				Iterator<FilterMeta> its = requiredMetas.iterator();
				while (its.hasNext()) {
					FilterMeta meta = its.next();
					String tmp_field = meta.getFieldCode();
					if(CheckUtils.isExist(tmp_field, fieldCodes)){
						meta.setLangSeq(LanguageUtil.getCurrentLangSeq());
						
					}
					if(!meta.isDefault()){
						continue;
					}
					if(meta.isDefault() && !CpQueryMetasFilter.notExist(meta, defaultMetas)){
						continue;
					}
					FormElement fme = QuerySchemeUtils.setMetaToFormEle(meta, view, queryController,sysProvider,refCodes);
					if (fme == null)
						continue;
					form.addElement(fme);
					String defaultCondition = meta.getDefaultCondition();
					int dataType = meta.getDataType();
					String varType = meta.getVarType();
					String defaultValue = meta.getDefaultValue();
					if(dataType==IQueryConstants.DATE||dataType==IQueryConstants.TIME || dataType==IQueryConstants.LITERALDATE || dataType == IQueryConstants.INTEGER || dataType == IQueryConstants.DECIMAL || dataType==IQueryConstants.UFTIME){
						if(null!=defaultCondition && "between".equals(defaultCondition)){
							if(StringUtils.isNotEmpty(defaultValue) && sysProvider!=null){
								String[] dateValues = defaultValue.split(",");
								if(dateValues!=null && dateValues.length>1){
									Object value = sysProvider.parseSysvar((String)dateValues[1]);
									if(value!=null){
										defaultValue = value.toString();
									}
								}
							}
							FormElement fme1 = new FormElement();
							fme1.setEditorType(fme.getEditorType());
							fme1.setId(meta.getFieldCode().replaceAll("\\.", "_")+"_end");
							fme1.setField(meta.getFieldCode().replaceAll("\\.", "_")+"_end");
							fme1.setText(LfwResBundle.getInstance().getStrByID("uapquerytemplate", "SimpleQueryWidgetProvider-000004")/*至*/);
							fme1.setDataType(fme.getDataType());
//							fme1.setI18nName(meta.getResId());
//							fme1.setLangDir(meta.getLangdir());
							form.addElement(fme1);
							Field field = new Field();
							field.setId(meta.getFieldCode().replaceAll("\\.", "_")+"_end");
							field.setText(meta.getFieldCode()+"_end");
							field.setDataType(fme.getDataType());
							if(varType!=null){
								field.setDefEditFormular("MD");
							}
							field.setDefaultValue(defaultValue);
							ds.getFieldSet().addField(field);
						}
					}
				}
				Iterator<FilterMeta> alls = usedBMetas.iterator();
				while(alls.hasNext()){
					FilterMeta meta = alls.next();
					String tmp_field = meta.getFieldCode();
					if(CheckUtils.isExist(tmp_field, fieldCodes)){
						meta.setLangSeq(LanguageUtil.getCurrentLangSeq());
						
					}
					Field field = new Field();
					field.setId(meta.getFieldCode());
					field.setText(meta.getFieldCode());
					String dataType = getDataType(meta);
					field.setDataType(dataType);
					field.setField(meta.getDefaultCondition());
					field.setDefEditFormular(meta.getVarType());
					if(master.getFieldSet()!=null && master.getFieldSet().getDataSet()==null){
						master.getFieldSet().setDataSet(master);
					}
					master.getFieldSet().addField(field);
				}
				
				ds.setCurrentKey(Dataset.MASTER_KEY);
				master.setCurrentKey(Dataset.MASTER_KEY);
				ds.setLazyLoad(true);
				master.setLazyLoad(true);
				Row row = ds.getEmptyRow();
				Row masterRow = master.getEmptyRow();
				Field[] fields = ds.getFieldSet().getFields();
				if(fields!=null){
					String sShiftYear = LfwRuntimeEnvironment.getWebContext().getOriginalParameter("yearShift");
					int iShiftYear = sShiftYear==null?0:Integer.valueOf(sShiftYear);
					String sShiftMonth = LfwRuntimeEnvironment.getWebContext().getOriginalParameter("monthShift");
					int iShiftMonth = sShiftMonth==null?0:Integer.valueOf(sShiftMonth);
					String sQuarterSpan = LfwRuntimeEnvironment.getWebContext().getOriginalParameter("quarterSpan");
					int iQuarterSpan = sQuarterSpan==null?0:Integer.valueOf(sQuarterSpan);
					String sShiftQuarter = LfwRuntimeEnvironment.getWebContext().getOriginalParameter("quarterShift");
					int iShiftQuarter = sShiftQuarter == null ? 0 : Integer.valueOf(sShiftQuarter);
					String queryByTableType = LfwRuntimeEnvironment.getWebContext().getOriginalParameter("tableType");
					
					//弹出月度财务预警分析时带出原有的日期及组织，并赋到查询条件中——徐俊晔
					String lmonth = LfwRuntimeEnvironment.getWebContext().getOriginalParameter("lmonth");
					String lpk_org = LfwRuntimeEnvironment.getWebContext().getOriginalParameter("pk_org");
					
					Map m= (Map) AppUtil.getAppAttr("report");
					if(m==null){
						m=new HashMap();
					}
					for(Field f : fields){
						/*调整内容：初始化查询条件默认值 By 陶冶*/
						if(f.getId().equals("type")) {
							ComboData combo = (ComboData) view.getViewModels().getComboData("comb_type_value");
							if(combo != null) {
								CombItem[] items = combo.getAllCombItems();
								if(items != null && items.length > 0) {
									row.setValue(ds.nameToIndex(f.getId()), items[0].getValue());
								}
							}
						}else if(f.getId().equals("pk_org")) {
							if (null != lpk_org && !"".equals(lpk_org)) {
								row.setValue(ds.nameToIndex(f.getId()), lpk_org);
							} else {
								String pk_org = CntUtil.getCntOrgPk();
								if(!iscur){
									row.setValue(ds.nameToIndex(f.getId()), CpOrgUtil.getDefaultQueryOrgValue().getPk_org());
								}else{
									row.setValue(ds.nameToIndex(f.getId()), pk_org);
								}
							}
						}
						//modify by liyuchen 20140106  begin 为特殊查询条件赋初始值,等研发打补丁后可删除
						else if(f.getId().equals("pk_org_org")){
							String pk_org = CntUtil.getCntOrgPk();
							row.setValue(ds.nameToIndex(f.getId()), pk_org);
						}
						//modify by liyuchen 20140106    end 为特殊查询条件赋初始值,等研发打补丁后可删除
						else if(f.getId().equals("pk_org_mc")) {
							String org_name = CntUtil.getCntOrgName();
							if(StringUtils.isNotBlank(lpk_org)){
								row.setValue(ds.nameToIndex(f.getId()), CpOrgUtil.getOrgVoByPk(lpk_org).getName());
							}else{
								if(!iscur){
									row.setValue(ds.nameToIndex(f.getId()), CpOrgUtil.getDefaultQueryOrgValue().getName());
								}else{
									row.setValue(ds.nameToIndex(f.getId()), org_name);
								}
							}
						}else if(f.getId().equals("syear")) {
							row.setValue(ds.nameToIndex(f.getId()), new UFDate().getYear()-4+iShiftYear);
						}else if(f.getId().equals("year") || f.getId().equals("eyear")) {
							row.setValue(ds.nameToIndex(f.getId()), new UFDate().getYear()+iShiftYear);
							AppUtil.addAppAttr(f.getId(), (Serializable) row.getValue(ds.nameToIndex(f.getId())));
							m.put("year", (new UFDate().getYear()+iShiftYear)+"");
							if(queryByTableType!=null&&!queryByTableType.equals("")){//按照table最新查询 2016年1月5日15:05:47
								String ym = (String) ScapDAO.executeQuery("select max(vyear"+(queryByTableType.equals("scapfd_js_base")?"":"||'-'||vmonth")+")  from "+queryByTableType+" where sys_org = 'HZQYB0014'", new ColumnProcessor());
								
								if(ym!=null&&!ym.equals("")){
									int ym_y = Integer.parseInt(ym.split("-")[0]);
									row.setValue(ds.nameToIndex(f.getId()),ym_y);
									m.put("year", ym_y);
								}
							}
						}else if(f.getId().equals("month") || f.getId().equals("emonth")) {
							 String provinceId= ScapSysinitUtil.getSysinitStrByCode(ISysinitConstant.PROVINCE_ID);
								if("HN".equals(provinceId)){
									Calendar c = Calendar.getInstance();
									c.set(Calendar.MONTH, 0);
									Date d = c.getTime();
									row.setValue(ds.nameToIndex(f.getId()), new SimpleDateFormat("yyyy-MM").format(d));
									m.put("month", "1");
								}else if(queryByTableType!=null&&!queryByTableType.equals("")){//按照table最新查询 2016年1月5日15:05:47
									String ym = (String) ScapDAO.executeQuery("select max(vyear||'-'||vmonth)  from "+queryByTableType+" where sys_org = 'HZQYB0014'", new ColumnProcessor());
									
									if(ym!=null&&!ym.equals("")){
										int ym_y = Integer.parseInt(ym.split("-")[0]);
										int ym_m = Integer.parseInt(ym.split("-")[1]);
										row.setValue(ds.nameToIndex(f.getId()),ym);
										m.put("month", ""+ym_m);
										m.put("year", ym_y);
									}
								}else{
									if(new UFDate().getMonth()==1){
										row.setValue(ds.nameToIndex(f.getId()), new SimpleDateFormat("yyyy-MM").format(new Date()));
										m.put("month", "1");
									}else{
										Calendar c = Calendar.getInstance();
										c.add(Calendar.MONTH, iShiftMonth);
										Date d = c.getTime();
										row.setValue(ds.nameToIndex(f.getId()), new SimpleDateFormat("yyyy-MM").format(d));
										m.put("month", ""+d.getMonth());
									}
								}
							
						}else if(f.getId().equals("smonth")) {
							row.setValue(ds.nameToIndex(f.getId()), Integer.toString(new UFDateTime().getYear()) + "-01");
							if(queryByTableType!=null&&!queryByTableType.equals("")){//按照table最新查询 2016年1月5日15:05:47
								String ym = (String) ScapDAO.executeQuery("select max(vyear||'-'||vmonth)  from "+queryByTableType+" where sys_org = 'HZQYB0014'", new ColumnProcessor());
								if(ym!=null&&!ym.equals("")){
									int ym_y = Integer.parseInt(ym.split("-")[0]);
									row.setValue(ds.nameToIndex(f.getId()),ym_y+ "-01");
									m.put("year", ym_y);
								}
							}
							m.put("month", "1");
						}else if(f.getId().equals("lmonth")) {
							if (null != lmonth && !"".equals(lmonth)) {
								row.setValue(ds.nameToIndex(f.getId()), lmonth);
							} else {
								//上一个月
								if(new UFDate().getMonth()==1){
									row.setValue(ds.nameToIndex(f.getId()), new SimpleDateFormat("yyyy-MM").format(new Date()));
									m.put("month", "1");
								}else{
									Calendar c = Calendar.getInstance();
									c.add(Calendar.MONTH, -1);
									Date d = c.getTime();
									row.setValue(ds.nameToIndex(f.getId()), new SimpleDateFormat("yyyy-MM").format(d));
									m.put("month", ""+d.getMonth()); 
								}
							}
							
						}else if(f.getId().equals("lyear")) {
							//上一年
							row.setValue(ds.nameToIndex(f.getId()), Integer.toString(new UFDateTime().getYear()-1) );
							m.put("year", ""+(new UFDateTime().getYear()-1)); 
						}
						else if(f.getId().equals("period")) {
							row.setValue(ds.nameToIndex(f.getId()), '1');
						}else if(f.getId().equals("equarter")){
							
							Calendar calendar = Calendar.getInstance();
							calendar.setTimeInMillis(new UFDate().getMillis());
							calendar.add(Calendar.YEAR, iShiftYear);
							calendar.add(Calendar.MONTH, iShiftMonth + iShiftQuarter * 3);
							
							Calendar spanedCalendar = (Calendar) calendar.clone();
							spanedCalendar.add(Calendar.MONTH, -3 * iQuarterSpan);
							
							row.setValue(ds.nameToIndex(f.getId()), this.getCurrentSeason(calendar));
							if(ds.nameToIndex("eyear") != -1) {
								row.setValue(ds.nameToIndex("eyear"), calendar.get(Calendar.YEAR));
							}
							if(ds.nameToIndex("syear") != -1){
								row.setValue(ds.nameToIndex("syear"),  spanedCalendar.get(Calendar.YEAR));
							}
							if(ds.nameToIndex("squarter")!=-1){
								row.setValue(ds.nameToIndex("squarter"), this.getCurrentSeason(spanedCalendar));
							}
						}else if(f.getId().equals("date")) {
							Calendar c = Calendar.getInstance();
							Date d = c.getTime();
							row.setValue(ds.nameToIndex(f.getId()), new SimpleDateFormat("yyyy-MM-dd").format(d));
						}else if(f.getId().equals("currtype")) {
							ComboData combo = (ComboData) view.getViewModels().getComboData("comb_currtype_value");
							if(combo != null) {
								CombItem[] items = combo.getAllCombItems();
								if(items != null && items.length > 0) {
									row.setValue(ds.nameToIndex(f.getId()), items[0].getValue());
								}
							}
						}else {
							row.setValue(ds.nameToIndex(f.getId()), f.getDefaultValue());
						}
						
//						if("Boolean".equals(f.getDataType())){
//							row.setValue(ds.nameToIndex(f.getId()), "true");
//						}
					}
					
				}
				alls = usedBMetas.iterator();
				while(alls.hasNext()){
					FilterMeta meta = alls.next();
					String defaultValue = meta.getDefaultValue();
					String field = meta.getFieldCode();
					masterRow.setValue(master.nameToIndex(field), defaultValue);
				}
				ds.addRow(row);
				ds.setRowSelectIndex(ds.getRowIndex(row));
				master.addRow(masterRow);
				master.setRowSelectIndex(master.getRowIndex(masterRow));
				ds.setEnabled(true);
				master.setEnabled(true);
				
				
				//如果自己配置了查询的controller，添加加载事件，valuechanged事件
				if(queryController != null){
					EventSubmitRule sr = new EventSubmitRule();
					ViewRule wr = new ViewRule();
					wr.setId(currViewId);
					sr.addViewRule(wr);
					
					EventConf dsLoadevent = DatasetEvent.getOnDataLoadEvent();
					ds.setLazyLoad(false);
					//ds加载事件
					dsLoadevent.setSubmitRule(sr);
					dsLoadevent.setControllerClazz(queryController);
					dsLoadevent.setMethodName(METHOD_SIMPLE_QUERYON_DATA_LOAD);
					ds.addEventConf(dsLoadevent);	
					
					//ds数据改变事件
					EventConf valueChangEvent = DatasetEvent.getOnAfterDataChangeEvent();
					valueChangEvent.setSubmitRule(sr);
					valueChangEvent.setControllerClazz(queryController);
					valueChangEvent.setMethodName(METHOD_SIMPLE_VALUE_CHANGED);
					ds.addEventConf(valueChangEvent);	
				}
				
				//增加清空功能
				ImageComp clearImg = new ImageComp();
				clearImg.setId("clearImg");
				clearImg.setAlt(LfwResBundle.getInstance().getStrByID("uapquerytemplate", "SimpleQueryWidgetProvider-000005")/*清空值*/);
				clearImg.setImage1("frame/device_pc/themes/${theme}/ui/ctrl/qry/images/clear_normal.png");
				
				view.getViewComponents().addComponent(clearImg);
				EventConf clearConf = MouseEvent.getOnClickEvent();
				clearConf.setMethodName("onCleanBtOk");
				clearImg.addEventConf(clearConf);
				
				ButtonComp qryBt = new ButtonComp();
				qryBt.setId("queryBt");
				qryBt.setText(LfwResBundle.getInstance().getStrByID("uapquerytemplate", "SimpleQueryWidgetProvider-000001")/*搜索*/);
				view.getViewComponents().addComponent(qryBt);
				EventConf okClickConf = MouseEvent.getOnClickEvent();
				okClickConf.setMethodName("onQryBtOk");
				qryBt.addEventConf(okClickConf);
				
				LinkComp advLink = new LinkComp();
				advLink.setId("advlink");
				String currentLanguage = LanguageUtil.getCurrentLangVO().getLocallang();
				if(currentLanguage!=null && "EN".equals(currentLanguage)){
					advLink.setText("Advanced");
				}
				else
					advLink.setText(LfwResBundle.getInstance().getStrByID("uapquerytemplate", "SimpleQueryController-000001")/*高级查询*/);
				view.getViewComponents().addComponent(advLink);
				
				EventConf eventConf = MouseEvent.getOnClickEvent();
				eventConf.setMethodName(METHOD_ON_QUERY_CLICK);
				advLink.addEventConf(eventConf);
			}
		}
		if(totalVO != null){
			CpQueryTemplateVO parentVO = (CpQueryTemplateVO) totalVO.getParentVO();
			String etag = parentVO.getPk_query_template() + parentVO.getTs().toString();
			if(queryPlanKeys != null){
				etag += queryPlanKeys;
			}
			//设置动态view的ETag,pageETagUtil最后会拼接各个view的ETag
			view.setEtag(etag);
		}
		return view;
	}


	private void getQueryTemplateTotalVO(LfwWindow pm, LfwView conf) {
		String queryNode = null;
		String queryNodeKey = null;
		if (pm != null) {
			if (conf.getExtendAttribute(LfwWindow.$QUERYTEMPLATE) != null)
				queryNodeKey = (String) conf.getExtendAttribute(
						LfwWindow.$QUERYTEMPLATE).getValue();
			if (queryNodeKey == null) {
				if (pm.getExtendAttribute(LfwWindow.$QUERYTEMPLATE) != null) {
					queryNodeKey = (String) pm.getExtendAttribute(
							LfwWindow.$QUERYTEMPLATE).getValue();
				}
			}
			//设置查询模板的nodekey
			AppLifeCycleContext.current().getApplicationContext().addAppAttribute(LfwWindow.$QUERYTEMPLATE, queryNodeKey);
			//if((queryNode == null || "".equals(queryNode) ) && AppLifeCycleContext.current() != null)
			{
				queryNode = (String) AppLifeCycleContext.current().getApplicationContext().getAppAttribute(AppControlPlugin.NODECODE);
			}
		}
		//如果url没有配置nodecode
		if(queryNode == null || "".equals(queryNode)){
			String pageId = (String)pm.getId();
			String pagePath = LFWAllComponetsFetcher.getWindowPath(pageId);
			if(pagePath != null){
//				String nodePath = "html/nodes/" + pagePath + "/node.properties";
				String nodePath = pagePath + "/node.properties";
				Properties props = AbstractPresentPlugin.loadNodePropertie(nodePath);
				if(props != null){
					queryNode = props.getProperty(AppControlPlugin.NODECODE);
					AppLifeCycleContext.current().getApplicationContext().addAppAttribute(AppControlPlugin.NODECODE, queryNode);
				}
			}
		}
		totalVO = SimpleQueryUtils.getQueryTotalVO(queryNode,queryNodeKey);
		ICpQryTemplateInnerQryService service = ServiceLocator.getService(ICpQryTemplateInnerQryService.class);
		CpQueryTemplateTotalVO sysTotalVO = SimpleQueryUtils.getSysQueryTotalVO(queryNode, queryNodeKey);
		if(sysTotalVO !=null){
			sys_metaclass = sysTotalVO.getTempletVO().getMetaclass();
		}
		if(StringUtils.isNotBlank(sys_metaclass)){
			try {
				fieldCodes = service.getMultiLangFieldCodeListByMetaClasses(sys_metaclass);
			} catch (TplBusinessException e) {
				CpLogger.error(e.getMessage(), e);
			}
		}
	}


	private void genAdvancedWindow(LfwView view) {
		WindowConfig winConf = new WindowConfig();
		winConf.setCaption(LfwResBundle.getInstance().getStrByID("imp", "SimpleQueryWidgetProvider-000000")/*高级查询*/);
		winConf.setId(SelfQueryController.ADVANCED_QUERY_NODE);
		view.addInlineWindow(winConf);
		
		//高级查询(确定)与简单查询(搜索)触发信号-槽连接器
		Connector conn = new Connector();
		conn.setId("adv_simple_conn");
		conn.setConnType(Connector.INLINEWINDOW_VIEW);
		conn.setPluginId(COND_QUERY_PLUGIN);
		conn.setPlugoutId(IPluginDesc.PROXY_PRE + QRYOUT);
		conn.setSource(winConf.getId());
		conn.setTarget(view.getId());
		view.addConnector(conn);
		
		//高级查询(查询方案修改、添加、删除)与简单查询(查询方案)动态刷新信号-槽连接器
		Connector refreshConn = new Connector();
		refreshConn.setId("adv_refreshview_conn");
		refreshConn.setConnType(Connector.INLINEWINDOW_VIEW);
		refreshConn.setPluginId(REFRESH_VIEW_PLUGIN);
		refreshConn.setPlugoutId(IPluginDesc.PROXY_PRE + REFRESH_VIEW_PLUGOUT);
		refreshConn.setSource(winConf.getId());
		refreshConn.setTarget(view.getId());
		view.addConnector(refreshConn);
	}


	private void genPlugoutAndPlugins(LfwView view) {
		PlugoutDesc plugoutDesc = new PlugoutDesc();
		plugoutDesc.setId(QRYOUT);
		view.addPlugoutDescs(plugoutDesc);
		
		//高级查询确定按钮触发接入
		PluginDesc pluginDesc = new PluginDesc();
		pluginDesc.setId(COND_QUERY_PLUGIN);
		view.addPluginDescs(pluginDesc);
		pluginDesc.setMethodName(COND_QUERY_PLUGIN);
		
		
		PluginDesc winPluginDesc = new PluginDesc();
		winPluginDesc.setId(NOCOND_QUERY_PLUGIN);
		view.addPluginDescs(winPluginDesc);
		winPluginDesc.setMethodName(NOCOND_QUERY_PLUGIN);
		
		//更新公共查询模板plugin
		PluginDesc refreshViewPluginDesc = new PluginDesc();
		refreshViewPluginDesc.setId(REFRESH_VIEW_PLUGIN);
		view.addPluginDescs(refreshViewPluginDesc);
		refreshViewPluginDesc.setMethodName(REFRESH_VIEW_PLUGIN);
	}

	
		
	private void mockNullWidget(LfwView widget) {
		widget.setExtendAttribute("mock", "1");
		LabelComp label = new LabelComp();
		label.setId("label");
		label.setText(LfwResBundle.getInstance().getStrByID("uapquerytemplate", "SimpleQueryWidgetProvider-000003")/*没有配置查询项*/);
		widget.getViewComponents().addComponent(label);
		Dataset ds = new Dataset();
		ds.setId(MAINDS);
		widget.getViewModels().addDataset(ds);
		FormComp form = new FormComp();
		form.setId(MAINFORM);
		form.setDataset(MAINDS);
		form.setEllipsis(true);
		form.setVisible(false);
		if(widget.getViewComponents().getComponent(MAINFORM)==null)
			widget.getViewComponents().addComponent(form);
	}
	
	private void advanceSearchWidget(LfwView widget){
		Dataset ds = new Dataset();
		ds.setId(MAINDS);
		widget.getViewModels().addDataset(ds);
		FormComp form = new FormComp();
		form.setId(MAINFORM);
		form.setDataset(MAINDS);
		form.setEllipsis(true);
		form.setVisible(false);
		if(widget.getViewComponents().getComponent(MAINFORM)==null)
			widget.getViewComponents().addComponent(form);
		
		ButtonComp qryBt = new ButtonComp();
		qryBt.setId("queryBt");
		qryBt.setText(LfwResBundle.getInstance().getStrByID("uapquerytemplate", "SimpleQueryWidgetProvider-000001")/*搜索*/);
		qryBt.setVisible(false);
		widget.getViewComponents().addComponent(qryBt);
		EventConf okClickConf = MouseEvent.getOnClickEvent();
		okClickConf.setMethodName("onQryBtOk");
//		okClickConf.setName("onclick");
		qryBt.addEventConf(okClickConf);
		
		widget.setExtendAttribute("mock", "2");
		LinkComp advLink = new LinkComp();
		advLink.setId("advlink");
//		advLink.setText(LfwResBundle.getInstance().getNCLangRes().getStrByID("uapquerytemplate", "SimpleQueryWidgetProvider-000002")/*高级搜索*/);
		advLink.setText(LfwResBundle.getInstance().getStrByID("uapquerytemplate", "SimpleQueryController-000001")/*高级查询*/);
//		advLink.setI18nName("SimpleQueryWidgetProvider-000002");
//		advLink.setLangDir("uapquerytemplate");
		//advLink.setHref("##");
		widget.getViewComponents().addComponent(advLink);
		
		EventConf eventConf = MouseEvent.getOnClickEvent();
		eventConf.setMethodName(METHOD_ON_QUERY_CLICK);
		advLink.addEventConf(eventConf);
	}
	
	private String getDataType(FilterMeta meta){
		String editorType = null;
		switch(meta.getDataType()){
			case IQueryConstants.STRING:
				editorType = EditorTypeConst.STRINGTEXT;
				break;
			case IQueryConstants.INTEGER:
				editorType = EditorTypeConst.INTEGERTEXT;
				break;
			case IQueryConstants.DECIMAL:
				editorType = EditorTypeConst.DECIMALTEXT;
				break;
			case IQueryConstants.DATE:
				editorType = EditorTypeConst.DATETEXT;
				break;
			case IQueryConstants.LITERALDATE:
				editorType = EditorTypeConst.DATETEXT;
				break;
			case IQueryConstArgs.YEARDATE:
				editorType = EditorTypeConst.YEARTEXT;
				break;
			case IQueryConstants.TIME:
				editorType = EditorTypeConst.DATETIMETEXT;
				break;
			case IQueryConstants.BOOLEAN:
				editorType = StringDataTypeConst.BOOLEAN;
				break;
			case IQueryConstants.UFREF:
				editorType = EditorTypeConst.REFERENCE;
				break;
			case IQueryConstants.USERCOMBO:
				editorType = EditorTypeConst.COMBODATA;
				break;
			default:
				editorType = EditorTypeConst.STRINGTEXT;
		}
		return editorType;
	}
	
	private String createQueryPlan(LfwView widget, LfwWindow pm, LfwView conf){
		//查询方案的plugout
		PlugoutDesc plugoutDesc = new PlugoutDesc();
		plugoutDesc.setId("queryPlanPlugout");
		widget.addPlugoutDescs(plugoutDesc);
			
		LabelComp label = new LabelComp();
		label.setId("label");
		label.setText(LfwResBundle.getInstance().getStrByID("uapquerytemplate", "QueryPlanWidgetProvider-000000")/*没有配置查询方案*/);
		widget.getViewComponents().addComponent(label);
		
		String schemaKeys = "";
		if (totalVO != null){
			boolean issys = totalVO.getTempletVO().getIssys().booleanValue();
			String pk_org = CpUserManager.getPk_org();
			String pk_user = CpUserManager.getPk_user();
			try {
				String pk_template = totalVO.getParentVO().getPrimaryKey();
				ICpQryTemplateInnerQryService service = ServiceLocator.getService(ICpQryTemplateInnerQryService.class);
				QuerySchemeVO[] vos = service.getQuerySchemeVOsBy(pk_org, pk_template, pk_user);
				if(!issys){
					//增加全局查询方案
					String funcode = totalVO.getTempletVO().getNodecode();
					String funkey = totalVO.getTempletVO().getNodekey();
					QuerySchemeVO[] sysVos = service.getQuerySchemeVosBy(funcode, funkey);
					if(sysVos!=null && sysVos.length>0){
						vos = QuerySchemeValidHelper.getQuerySchemes(sysVos, vos, false);
					}
				}else{
					if(StringUtils.isNotBlank(pk_org)){
						//处理不同于系统模板组织的模板，寻找其中的全局查询方案
						String funcode = totalVO.getTempletVO().getNodecode();
						String funkey = totalVO.getTempletVO().getNodekey();
						QuerySchemeVO[] sysVos = service.getQuerySchemeVosBy(funcode, funkey);
						if(sysVos!=null && sysVos.length>0){
							vos = QuerySchemeValidHelper.getQuerySchemes(sysVos, vos, false);
						}
					}
				}
				if(vos != null){
					AppLifeCycleContext ctx = AppLifeCycleContext.current();
					LfwView view = null;
					if(ctx!=null && ctx.getWindowContext()!=null){
						view = ctx.getWindowContext().getCurrentViewContext().getView();
					}
					ISysvarProvider sysProvider = ServiceLocator.getService(ISysvarProvider.class);
					for (int i = 0; i < vos.length; i++) {
						QuerySchemeVO vo = vos[i];
						QuerySchemeObject object = null;
						try{
							object = vo.getQSObject4Blob();
						}catch(Exception e){
							CpLogger.error(e.getMessage(),e);
						}
						if(object==null){
							continue;
						}
						schemaKeys += vo.getPrimaryKey() + " ";
						QueryResult queryResult = (QueryResult)QueryTemplateUtils.readFromXML(object.getQueryResult());
						IRule resultRule = queryResult.getResultRule();
						if (resultRule instanceof ContainerRule) {
							//只处理并且
							if (resultRule instanceof AndRule){
								AndOperator.getInstance();
							}else{
								OrOperator.getInstance();
							}
							LinkComp link = new LinkComp();
							link.setId("link" + i);
							link.setWidth("148");
							String name = null;
							String currentLanguage = LanguageUtil.getCurrentLangVO().getLocallang();
							LfwLanguageVO[] enabledLanguages = LanguageUtil.getEnableLangVOs();
							Map<Integer,String> valueMap = new HashMap<Integer,String>();
							valueMap.clear();
							valueMap.put(1, vo.getName());
							valueMap.put(2, vo.getName2());
							valueMap.put(3, vo.getName3());
							valueMap.put(4, vo.getName4());
							valueMap.put(5, vo.getName5());
							valueMap.put(6, vo.getName6());
							for (int index = 0; index < enabledLanguages.length; index++) {
								String locallang = enabledLanguages[index].getLocallang();
								if (currentLanguage.equals(locallang)) {
									int langseq = enabledLanguages[index].getLangseq();
									name = valueMap.get(langseq);
									break;
								}
							}
							if(StringUtils.isEmpty(name)){
								name = vo.getName();
							}
							link.setText(name);
							StringBuffer buf = new StringBuffer();
							IRule[] rules = ((ContainerRule) resultRule).getChildrenRules();
							boolean first = true;
							Map<String,String> fieldAndSqlMap = new HashMap<String,String>();
							for (IRule rule : rules) {
								DefaultFilter filter = (DefaultFilter)((QueryRule) rule).getFormatObject();
								FilterMeta meta = (FilterMeta)filter.getFilterMeta();
								String operatorValue = "=";
								if(filter.getOperator() != null)
									operatorValue = filter.getOperator().getOperatorCode();
								String tmp_field = meta.getFieldCode();
								if(CheckUtils.isExist(tmp_field, fieldCodes)){
									meta.setLangSeq(LanguageUtil.getCurrentLangSeq());
									
								}
								String fieldCode = meta.getFieldCode();
								String defaultValue = meta.getDefaultValue();
								int dataType = meta.getDataType();
								
								if(defaultValue != null && !"".equals(defaultValue)){
									if(!first){
										buf.append(" and ");
									}
									if(dataType==5){
										String[] values = defaultValue.split(",");
										if(values!=null){
											if(values.length==1){
												buf.append(fieldCode);
												buf.append(" " + operatorValue);
												if("like".equals(operatorValue)){
													buf.append(" '%" + defaultValue + "%'");
												}else{
													buf.append(" '" + defaultValue + "'");
												}
											}else if(values.length>1){
												buf.append(" (").append(fieldCode).append(" in (");
												for(int index=0;index<values.length;index++){
													buf.append("'").append(values[index]).append("'");
													if(index!=values.length-1)
														buf.append(",");
													else
														buf.append(")");
												}
												buf.append(") ");
											}
										}
									}else{
										if(dataType != 6){
											buf.append(fieldCode);
										}
										if("between".equals(operatorValue)){
											String[] values = defaultValue.split(",");
											Object value = sysProvider.parseSysvar((String)values[0]);
											if(value!=null){
												values[0] = value.toString();
												if(StringUtils.isNotEmpty(defaultValue) && values.length > 1){
													Object value_t = sysProvider.parseSysvar((String)values[1]);
													if(value_t!=null){
														values[1] = value_t.toString();
													}
												}
											}
											if(values.length==1){
												if(dataType==3 || dataType==10){
													String smallValue = (new UFDate(values[0])).asBegin().toString();
													buf.append(" >= '").append(smallValue).append("'");
												}else{
													buf.append(" >= '").append(values[0]).append("'");
												}
											}else if(values.length==2){
												if(dataType == 3){
													String smallValue = (new UFDate(values[0])).asBegin().toString();
													String bigValue = (new UFDate(values[1])).asEnd().toString();
													buf.append(" "+operatorValue);
													buf.append(" '"+smallValue+"'");
													buf.append(" and");
													buf.append(" '"+bigValue+"'");
												}else{
													buf.append(" "+operatorValue);
													buf.append(" '"+values[0]+"'");
													buf.append(" and");
													buf.append(" '"+values[1]+"'");
												}
											}
										}else if("like".equals(operatorValue)){
											String[] values = defaultValue.split(",");
											if(values != null && values.length >0){
												Object value = sysProvider.parseSysvar((String)values[0]);
												if(value != null){
													defaultValue = value.toString();
												}
											}
											if(dataType == 3){
												String smallValue = (new UFDate(defaultValue)).asBegin().toString();
												String bigValue = (new UFDate(defaultValue)).asEnd().toString();
												buf.append(" >= '").append(smallValue).append("' and ").append(fieldCode).append(" <= '").append(bigValue).append("' ");
											}else{
												buf.append(" " + operatorValue);
												buf.append(" '%" + defaultValue + "%'");
											}
										}else if("left like".equals(operatorValue)){
											buf.append(" like '" + defaultValue + "%' ");
										}else if("right like".equals(operatorValue)){
											buf.append(" like '%" + defaultValue + "' ");
										}else{
											if(dataType == 3){
												String[] values = defaultValue.split(",");
												if(values != null && values.length >0){
													Object value = sysProvider.parseSysvar((String)values[0]);
													if(value != null){
														defaultValue = value.toString();
													}
												}
												if("=".equals(operatorValue)){
													String smallValue = (new UFDate(defaultValue)).asBegin().toString();
													String bigValue = (new UFDate(defaultValue)).asEnd().toString();
													buf.append(" >= '").append(smallValue).append("' and ").append(fieldCode).append(" <= '").append(bigValue).append("' ");
												}else if(">=".equals(operatorValue) || "<".equals(operatorValue)){
													String smallValue = (new UFDate(defaultValue)).asBegin().toString();
													buf.append(" "+operatorValue);
													buf.append(" '" + smallValue + "'");
												}else if(">".equals(operatorValue) || "<=".equals(operatorValue)){
													String bigValue = (new UFDate(defaultValue)).asEnd().toString();
													buf.append(" "+operatorValue);
													buf.append(" '" + bigValue + "'");
												}
											}else{
												String[] values = defaultValue.split(",");
												if(values != null && values.length >0){
													defaultValue = values[0];
												}
												if(dataType ==6){
													if("N".equals(defaultValue)){
														buf.append(" (").append(fieldCode);
														buf.append(" "+operatorValue);
														buf.append(" '" + defaultValue + "' or ").append(fieldCode).append(" is null )");
													}else{
														buf.append(fieldCode);
														buf.append(" "+operatorValue);
														buf.append(" '" + defaultValue + "'");
													}
												}else{
													buf.append(" "+operatorValue);
													buf.append(" '" + defaultValue + "'");
												}
												
											}
										}
									}
									first = false;
									fieldAndSqlMap.put(fieldCode, defaultValue);
								}
							}						
							
							ExtAttribute ext = new ExtAttribute();
							ext.setKey("queryResult");
							ext.setValue(buf.toString());
							ExtAttribute mapExt = new ExtAttribute();
							mapExt.setKey("queryFieldMap");
							mapExt.setValue(fieldAndSqlMap.toString());
							//whisper 增加传参
							ExtAttribute queryIdExt = new ExtAttribute();
							queryIdExt.setKey("queryId");
							queryIdExt.setValue(vo.getPk_queryscheme().toString());
							ExtAttribute queryNameExt = new ExtAttribute();
							queryNameExt.setKey("queryName");
							queryNameExt.setValue(name.toString());
							link.addAttribute(ext);
							link.addAttribute(mapExt);
							link.addAttribute(queryIdExt);
							link.addAttribute(queryNameExt);
							
							widget.getViewComponents().addComponent(link);
							if(view!=null){
								if(view.getViewComponents().getComponent(link.getId())!=null){
									view.getViewComponents().removeComponent(link.getId());
								}
								view.getViewComponents().addComponent(link);
							}
							EventConf eventConf = MouseEvent.getOnClickEvent();
							eventConf.setMethodName("onQueryPlanClick");
							link.addEventConf(eventConf);
							}
						}
					if(vos.length > 5){
//						LabelComp expandLabel = new LabelComp();
//						expandLabel.setId("expandLabel");
//						expandLabel.setText(LfwResBundle.getInstance().getStrByID("imp", "SimpleQueryWidgetProvider-000001")/*更多*/);
//						expandLabel.setColor("#0086B2");
//						widget.getViewComponents().addComponent(expandLabel);
						LinkComp expandLink = new LinkComp();
						expandLink.setId("expandLink");
						expandLink.setText(LfwResBundle.getInstance().getStrByID("imp", "SimpleQueryWidgetProvider-000001")/*更多*/);
						widget.getViewComponents().addComponent(expandLink);
						
						
						EventConf expandLinkEvent = MouseEvent.getOnClickEvent();
						expandLinkEvent.setMethodName("onMoreQueryPlanLinkClick");
						expandLink.addEventConf(expandLinkEvent);
						
						ImageComp expandImg = new ImageComp();
						expandImg.setId("expandImg");
						expandImg.setImage1("frame/device_pc/themes/${theme}/ui/ctrl/qry/images/dropdown.png");
						
						widget.getViewComponents().addComponent(expandImg);
						EventConf expandEvent = MouseEvent.getOnClickEvent();
						expandEvent.setMethodName("onMoreQueryPlanClick");
						expandImg.addEventConf(expandEvent);
						if(view!=null){
							if(view.getViewComponents().getComponent(expandLink.getId())==null){
								view.getViewComponents().addComponent(expandLink);
							}
							if(view.getViewComponents().getComponent(expandImg.getId())==null){
								view.getViewComponents().addComponent(expandImg);
							}
						}
					}
				}
					
			} catch (BusinessException e) {
				CpLogger.error(e.getMessage(),e);
			}
		}
		return schemaKeys;
	}


	@Override
	public LfwView buildViewForDesign(LfwWindow pm, LfwView conf, Map<String, Object> paramMap, String currViewId) {
		LfwView view = new LfwView();
		view.setId(currViewId);
		view.setControllerClazz(SelfQueryController.class.getName());
		//生成输入和输出
		genPlugoutAndPlugins(view);
		mockNullWidget(view);
		return view;
	}

	public String getCurrentSeason() {
		return getCurrentSeason(Calendar.getInstance());
	}

	public String getCurrentSeason(Calendar now) {
		int month = now.get(Calendar.MONTH) + 1;
		String quarter = "1";
		if (month == 1 || month == 2 || month == 3) {
			quarter = "1";
		} else if (month == 4 || month == 5 || month == 6) {
			quarter = "2";
		} else if (month == 7 || month == 8 || month == 9) {
			quarter = "3";
		} else if (month == 10 || month == 11 || month == 12) {
			quarter = "4";
		}
		return quarter;
	}
}
