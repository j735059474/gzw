package nc.scap.pub.selfquery;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import nc.md.IMDQueryFacade;
import nc.md.MDBaseQueryFacade;
import nc.md.model.IAttribute;
import nc.md.model.IBean;
import nc.md.model.IForeignKey;
import nc.md.model.MetaDataException;
import nc.md.model.impl.BusinessEntity;
import nc.md.model.type.IType;
import nc.md.model.type.impl.CollectionType;
import nc.md.model.type.impl.RefType;
import nc.uap.cpb.log.CpLogger;
import nc.uap.ctrl.tpl.qry.FromWhereSQLImpl;
import nc.uap.ctrl.tpl.qry.SimpleQueryWidgetProvider;
import nc.uap.ctrl.tpl.qry.base.CpQueryTemplateTotalVO;
import nc.uap.ctrl.tpl.qry.base.CpQueryTemplateTranslator;
import nc.uap.lfw.app.plugin.AppControlPlugin;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifPlugoutCmd;
import nc.uap.lfw.core.common.EditorTypeConst;
import nc.uap.lfw.core.common.StringDataTypeConst;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.ViewContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.sysvar.ISysvarProvider;
import nc.uap.lfw.core.uif.delegator.DefaultDataValidator;
import nc.uap.lfw.core.uif.delegator.IDataValidator;
import nc.uap.portal.log.ScapLogger;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;

import org.apache.commons.lang.StringUtils;

import uap.lfw.core.locator.ServiceLocator;
import uap.lfw.imp.query.base.CpSimpleQueryUtils;

public class SelfQuerySearchEventManager {

	/**
	 * 执行快速搜索查询
	 */
	public static void simpleSearch(){
		/**
		 * ctx
		 */
		AppLifeCycleContext ctx = AppLifeCycleContext.current();
		ViewContext viewCtx = ctx.getWindowContext().getCurrentViewContext();
		/***
		 * 默认字段
		 */
		Dataset ds = viewCtx.getView().getViewModels().getDataset(SimpleQueryWidgetProvider.MAINDS);
		/**
		 * 候选条件中排除默认字段后字段
		 */
		Dataset master = viewCtx.getView().getViewModels().getDataset(SimpleQueryWidgetProvider.MASTERDS);
		
		/**
		 * valid dataSet
		 */
		valideDataSet(ds);
		
		/**
		 * get row and field
		 */
		Row row = ds.getSelectedRow();
		if(row == null){
			row = ds.getEmptyRow();
		}
		Field[] fs = ds.getFieldSet().getFields();
		
		
		
		StringBuilder fromPart = new StringBuilder();//child part
		IBean mainMainBean = null;//main bean
		Map<String, String> attrpath_alias_map = new HashMap<String, String>();
		
		/**
		 * 获取 maintable,mainMainBean
		 * 
		 */
		String mainTableName = null;
		
		CpQueryTemplateTranslator loader = new CpQueryTemplateTranslator();
		String queryNode = (String) AppLifeCycleContext.current()
				.getApplicationContext().getAppAttribute(
						AppControlPlugin.NODECODE);
		String queryNodeKey = (String) AppLifeCycleContext.current()
				.getApplicationContext().getAppAttribute(
						LfwWindow.$QUERYTEMPLATE);
		CpQueryTemplateTotalVO totalVO = loader.getQueryTotalVO(null,
				queryNode, queryNodeKey);
		
		String mainMetaClass = null;  //元数据ID
		if(totalVO!=null && totalVO.getTempletVO()!=null){
			mainMetaClass = totalVO.getTempletVO().getMetaclass();
			UFBoolean issys = totalVO.getTempletVO().getIssys();
			if(issys==null || !issys.booleanValue()){
				CpQueryTemplateTotalVO sysTotalVO = loader.getSysQueryTotalVO(null, queryNode, queryNodeKey);
				if(sysTotalVO!=null && sysTotalVO.getTempletVO()!=null){
					mainMetaClass = sysTotalVO.getTempletVO().getMetaclass();
				}
			}
		}	
		if (mainMetaClass != null) {
			IMDQueryFacade qry = MDBaseQueryFacade.getInstance();
			try {
				mainMainBean = qry.getBeanByID(mainMetaClass);
				mainTableName = mainMainBean.getTable().getName();
				attrpath_alias_map.put(".", mainTableName);
				fromPart.append(mainTableName + " " + mainTableName);
			} catch (MetaDataException e1) {
				CpLogger.error(e1.getMessage(), e1);
			}
		}
		/**
		 * 自定义查询条件key,value；带操作符
		 */
		Map<String,String> fieldAndDefineMap = new HashMap<String, String>();
		/**
		 * 自定义查询条件key,value
		 */
		Map<String,String> fieldAndDefineValueMap = new HashMap<String, String>();
		

		buildSql1(ds, row, fs, fieldAndDefineMap, fieldAndDefineValueMap);
		
		StringBuffer wherePart = new StringBuffer(" 1 = 1 ");
		wherePart.append("");
		Set<String> set = fieldAndDefineMap.keySet();
		Object[] keys = set.toArray();
		for(Object key:keys){
			wherePart.append(" and "+key+fieldAndDefineMap.get(key));
		}
		/**
		 * get sql has child table 
		 */
		Map<String,Object> paramMap = new HashMap<String,Object>();
		
		paramMap.put("condition", fieldAndDefineMap);
		paramMap.put("resultValue", fieldAndDefineValueMap);
		paramMap.put("wherePart",wherePart);
		/**
		 * build param
		 */

		CmdInvoker.invoke(new UifPlugoutCmd(viewCtx.getId(), "qryout", paramMap));
	}

	

	/**
	 * 验证数据集
	 * @param ds
	 */
	private static void valideDataSet(Dataset ds) {
		/**
		 * validate ds
		 */
		IDataValidator validator = new DefaultDataValidator();
		validator.validate(ds, new LfwView());
	}
	
	private static String buildSql1(Dataset ds, Row row, Field[] fs,
			Map<String, String> fieldAndDefineMap,
			Map<String, String> fieldAndDefineValueMap) {
		boolean first = true;
		ISysvarProvider sysProvider = ServiceLocator.getService(ISysvarProvider.class);
		for (int i = 0; i < fs.length; i++) {
			Field f = fs[i];
			if (f.getId().endsWith("_mc"))
				continue;
			
			String value = null;
			Object obj = row.getValue(ds.nameToIndex(f.getId()));
			if (null == obj) {
				value = "";
			}
			if (obj == null || obj.equals("")) {
				continue;
			}
			String varType = f.getDefEditFormular();
			value = obj.toString();
			value = CpSimpleQueryUtils.getValidSqlValue(value);
			String dataType = f.getDataType();
			TimeZone timeZone = (TimeZone) LfwRuntimeEnvironment.getLfwSessionBean().getTimeZone();
			if(timeZone==null)
				timeZone = TimeZone.getDefault();
			
			if (value == null || value.equals(""))
				continue;
			
			//判断语言环境，使用多语字段
			String mutilFlag = f.getExtSource();
			String mutilFieldId = f.getId();
			String trueField = f.getText();
			String endField = null;
			boolean isExistChild = false;
			if(trueField.indexOf(".")!=-1){
				isExistChild = true;
				
			}
			String[] defaultValues = value==null?null:value.split(",");
			if(defaultValues != null && defaultValues.length >0 ){
				for(int index = 0; index < defaultValues.length; index++){
					String defaultValue = defaultValues[index];
					if(StringUtils.isNotBlank(defaultValue)){
						Object ref_value = sysProvider.parseSysvar((String)defaultValue);
						if(ref_value!=null){
							defaultValues[index] = ref_value.toString();
						}
					}
				}
				value = StringUtils.join(defaultValues, ",");
			}
			boolean ifMutilField = false;
			if(mutilFlag!=null && "y".equals(mutilFlag.toLowerCase())){
				ifMutilField = true;
				mutilFieldId = CpSimpleQueryUtils.getMutilField(mutilFieldId, ifMutilField);
				trueField = CpSimpleQueryUtils.getMutilField(trueField, ifMutilField);
			}
			if(isExistChild){
				endField = trueField.substring(trueField.lastIndexOf(".")+1);
			}
			String sqlSpatch = "";
			/**
			 * 自定义条件字段处理
			 */
			if(varType!=null){
				String opertatorValue = " like '%" + value + "%' ";
				if(dataType != null&& dataType.equals(StringDataTypeConst.BOOLEAN)){
					opertatorValue = " ='" + value + "' ";
				}else if(EditorTypeConst.REFERENCE.equals(dataType)){
					String[] values = value.split(",");
					if(values.length==1){
						opertatorValue = " ='" + value + "' ";
					}else if(values.length>1){
						StringBuffer operatorBuf = new StringBuffer();
						operatorBuf.append(" in (");
						for(int index =0;index<values.length;index++){
							operatorBuf.append("'").append(values[index]).append("'");
							if(index!=values.length-1){
								operatorBuf.append(",");
							}else{
								operatorBuf.append(")");
							}
						}
						operatorBuf.append(") ");
						opertatorValue = operatorBuf.toString();
					}
				}else{
					String fieldText = f.getField();
					if (f.getId().endsWith("_start")) {
						String field = f.getId().substring(0,
								f.getId().lastIndexOf("_start"));
						mutilFieldId = CpSimpleQueryUtils.getMutilField(field, ifMutilField);
						mutilFieldId += "_start";
						if(StringDataTypeConst.UFDATE.equals(dataType)){
							value = new UFDate(value,timeZone).asBegin(timeZone).toString();
						}
						opertatorValue = " >='" + value + "' ";

					} else if (f.getId().endsWith("_end")) {
						String field = f.getId().substring(0,
								f.getId().lastIndexOf("_end"));
						mutilFieldId = CpSimpleQueryUtils.getMutilField(field, ifMutilField);
						mutilFieldId += "_end";
						if (StringDataTypeConst.UFDATE.equals(dataType)) {
							value = new UFDate(value,timeZone).asEnd(timeZone).toString();
						}
						opertatorValue = " <='" + value + "' ";

					}else if (null != fieldText&& ("=".equals(fieldText) || ">=".equals(fieldText)|| "<=".equals(fieldText)|| "<".equals(fieldText) || ">".equals(fieldText))) {
						if("=".equals(fieldText)){
							if(StringDataTypeConst.UFDATE.equals(dataType)){
								String bigValue = new UFDate(value).asEnd().toString();
								String smallValue = new UFDate(value).asBegin()
										.toString();
								opertatorValue = " >='" + smallValue + "' and " + mutilFieldId + " <='" + bigValue + "' ";
							}else{
								opertatorValue = " " + fieldText + "'" + value + "' ";
							}
						}else{
							if(StringDataTypeConst.UFDATE.equals(dataType)){
								if("<=".equals(fieldText)||"<".equals(fieldText)){
									value = new UFDate(value,timeZone).asEnd(timeZone).toString();
								}
							}
							opertatorValue = " " + fieldText + "'" + value + "' ";
						}
						
					}else {
						if(StringDataTypeConst.UFDATE.equals(dataType)){
							String bigValue = new UFDate(value).asEnd().toString();
							String smallValue = new UFDate(value).asBegin()
									.toString();
							opertatorValue = " >='" + smallValue + "' and " + mutilFieldId + " <='" + bigValue + "' ";
						}else if (StringDataTypeConst.UFLiteralDate.equals(dataType)) {
							String bigValue = new UFDate(value).asEnd().toString();
							String smallValue = new UFDate(value).asBegin()
									.toString();
							opertatorValue = " >='" + smallValue + "' and " + mutilFieldId + " <='" + bigValue + "' ";
						}
					}
				}
				ScapLogger.console(mutilFieldId+":"+opertatorValue);
				fieldAndDefineMap.put(mutilFieldId, opertatorValue);
				fieldAndDefineValueMap.put(mutilFieldId, value);
			}	
			// }
			// 将字段和字段对应的值写入map中
			if(f.getId().equals("_strat")){
				String field = f.getId().substring(0,
						f.getId().lastIndexOf("_start"));
				field = CpSimpleQueryUtils.getMutilField(field, ifMutilField);
				field += "_start";
			}else if(f.getId().endsWith("_end")){
				String field = f.getId().substring(0,
						f.getId().lastIndexOf("_end"));
				field = CpSimpleQueryUtils.getMutilField(field, ifMutilField);
				field += "_end";
			}else{
			}
		}
		return null;
	}
}
