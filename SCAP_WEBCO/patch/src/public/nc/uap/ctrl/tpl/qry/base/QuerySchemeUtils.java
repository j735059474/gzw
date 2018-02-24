package nc.uap.ctrl.tpl.qry.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.ml.NCLangResOnserver;
import nc.md.innerservice.MDQueryService;
import nc.md.model.IEnumValue;
import nc.md.model.MetaDataException;
import nc.md.model.impl.Component;
import nc.md.model.type.IEnumType;
import nc.uap.cpb.log.CpLogger;
import nc.uap.ctrl.tpl.IAddressService;
import nc.uap.ctrl.tpl.exp.TplBusinessException;
import nc.uap.ctrl.tpl.gz.RuleSchemeVO;
import nc.uap.ctrl.tpl.gz.operator.GzOperatorFactory;
import nc.uap.ctrl.tpl.qry.QueryTemplateUtils;
import nc.uap.ctrl.tpl.qry.SimpleQueryWidgetProvider;
import nc.uap.ctrl.tpl.qry.meta.AndRule;
import nc.uap.ctrl.tpl.qry.meta.ConditionVO;
import nc.uap.ctrl.tpl.qry.meta.ContainerRule;
import nc.uap.ctrl.tpl.qry.meta.DefaultConstEnum;
import nc.uap.ctrl.tpl.qry.meta.DefaultFilter;
import nc.uap.ctrl.tpl.qry.meta.FilterMeta;
import nc.uap.ctrl.tpl.qry.meta.IFieldValueElement;
import nc.uap.ctrl.tpl.qry.meta.IFilter;
import nc.uap.ctrl.tpl.qry.meta.IQueryConstants;
import nc.uap.ctrl.tpl.qry.meta.IRule;
import nc.uap.ctrl.tpl.qry.meta.QueryConditionVO;
import nc.uap.ctrl.tpl.qry.meta.QueryRule;
import nc.uap.ctrl.tpl.qry.meta.RefResultVO;
import nc.uap.ctrl.tpl.qry.meta.RefValueObject;
import nc.uap.ctrl.tpl.qry.operator.AndOperator;
import nc.uap.ctrl.tpl.qry.operator.IOperator;
import nc.uap.ctrl.tpl.qry.operator.OperatorFactory;
import nc.uap.ctrl.tpl.qry.operator.OrOperator;
import nc.uap.ctrl.tpl.qry.value.DefaultFieldValue;
import nc.uap.ctrl.tpl.qry.value.DefaultFieldValueElement;
import nc.uap.lfw.app.plugin.AppControlPlugin;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.combodata.CombItem;
import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.combodata.DynamicComboDataConf;
import nc.uap.lfw.core.combodata.StaticComboData;
import nc.uap.lfw.core.common.CompIdGenerator;
import nc.uap.lfw.core.common.EditorTypeConst;
import nc.uap.lfw.core.common.StringDataTypeConst;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.data.RowData;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.core.refnode.LfwRefNode;
import nc.uap.lfw.core.refnode.NCRefNode;
import nc.uap.lfw.core.refnode.RefNodeGenerator;
import nc.uap.lfw.core.sysvar.ISysvarProvider;
import nc.uap.lfw.format.LfwFormater;
import nc.uap.lfw.reference.ILfwRefModel;
import nc.uap.lfw.reference.base.RefValueVO;
import nc.uap.lfw.reference.util.LfwRefUtil;
import nc.uap.lfw.util.LanguageUtil;
import nc.vo.bd.address.AddressFormatVO;
import nc.vo.bd.address.AddressVO;
import nc.vo.ml.MultiLangContext;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.format.FormatResult;
import nc.vo.pub.format.exception.FormatException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

import org.apache.commons.lang.StringUtils;

import uap.lfw.core.locator.ServiceLocator;
import uap.lfw.core.ml.LfwResBundle;
import uap.lfw.imp.base.IRefNodeDataQryService;
import uap.lfw.imp.base.LfwRefModelManager;
import uap.lfw.imp.math.CheckUtils;
import uap.lfw.ref.model.LfwAbstractRefModel;
import uap.lfw.ref.util.LfwReferenceUtil;

/**
 * 查询方案vo工具类
 * 
 */

public class QuerySchemeUtils {

	/**
	 * 模板查询条件VO 转换为 FilterMeta
	 * 
	 * @param vo
	 * @return
	 */
	public static FilterMeta queryCondition2Meta(QueryConditionVO vo){  
		FilterMeta meta = new FilterMeta();
		meta.setFieldCode(vo.getFieldCode());
		meta.setFieldName(vo.getFieldName());
		meta.setOperators(OperatorFactory.getInstance().getOperators(vo.getOperaCode()));
		meta.setTableCode(vo.getTableCode());
		meta.setTableName(vo.getTableName());
		meta.setIfAutoCheck(vo.getIfAutoCheck()==null?false:vo.getIfAutoCheck().booleanValue());
		meta.setIsEditable(vo.getIfImmobility()==null?false:vo.getIfImmobility().booleanValue());
		meta.setDispType(vo.getDispType());
		meta.setReturnType(vo.getReturnType());
		meta.setValueEditorDescription(vo.getConsultCode());
		meta.setDataType(vo.getDataType());
		meta.setDefaultValue(vo.getValue());
		meta.setFixCondition(vo.getIfImmobility()==null?false:vo.getIfImmobility().booleanValue());
		meta.setRequired(vo.getIfMust()==null?false:vo.getIfMust().booleanValue());
		meta.setDefault(vo.getIfDefault()==null?false:vo.getIfDefault().booleanValue());
		meta.setDataPower(vo.getIfDataPower().booleanValue());
		meta.setUserDef(vo.getUserDef());
		meta.setCondition(vo.getIsCondition() == null ? true : vo.getIsCondition().booleanValue());
		meta.setInstrumentedsql(vo.getInstrumentedsql());
		meta.setOrder(vo.getIfOrder().booleanValue());
		meta.setResId(vo.getResid());
		return meta;
	}
	
	public static List<DefaultFilter> gzConditionDsFilter(Dataset ds){
		RowData rowData = ds.getCurrentRowSet().getCurrentRowData();
		Row[] rows = rowData==null?null:rowData.getRows();
		List<DefaultFilter> filters = new ArrayList<DefaultFilter>();
		for (Row row : rows){
			DefaultFilter filter = new DefaultFilter();
			FilterMeta meta = new FilterMeta();
			String code = (String)row.getValue(ds.nameToIndex("query_condition_field"));
			meta.setFieldCode(code);
			String name = (String)row.getValue(ds.nameToIndex("query_condition_label"));
			meta.setFieldName(name);
			String resId = (String)row.getValue(ds.nameToIndex("resId"));
			meta.setResId(resId);
			String langDir = (String)row.getValue(ds.nameToIndex("langDir"));
			meta.setLangdir(langDir);
			String operatorValue = (String)row.getValue(ds.nameToIndex("query_condition_condition"));
			meta.setOperators(GzOperatorFactory.getInstance().getOperators(operatorValue));
			if(ds.nameToIndex("operators")!=-1){
				String usedOperatorsValue = (String)row.getValue(ds.nameToIndex("operators"));
				meta.setOperators(GzOperatorFactory.getInstance().getOperators(usedOperatorsValue));
			}
			String defaultValue = (String)row.getValue(ds.nameToIndex("query_condition_value"));
			defaultValue = getValidSqlValue(defaultValue);
			meta.setDefaultValue(defaultValue);
			String dataType = (String)row.getValue(ds.nameToIndex("query_condition_editorType"));
			String data_type = (String)row.getValue(ds.nameToIndex("query_condition_dataType"));
			String parent_field = (String)row.getValue(ds.nameToIndex("parent_field"));
			String tableName = (String)row.getValue(ds.nameToIndex("tableName"));
			meta.setTableCode(tableName);
			meta.setWhere(parent_field);
			if(ds.nameToIndex("trueFieldCode")!=-1){
				String trueFieldCode = (String)row.getValue(ds.nameToIndex("trueFieldCode"));
				meta.setTrueFieldCode(trueFieldCode);
			}
			if(ds.nameToIndex("varType")!=-1){
				String varType = row.getString(ds.nameToIndex("varType"));
				meta.setVarType(varType);
			}
			int type = 0;
			if (EditorTypeConst.STRINGTEXT.equals(dataType))
				type = IQueryConstants.STRING;
			else if (EditorTypeConst.INTEGERTEXT.equals(dataType))
				type = IQueryConstants.INTEGER;
			else if (EditorTypeConst.DECIMALTEXT.equals(dataType))
				type = IQueryConstants.DECIMAL;
			else if (EditorTypeConst.DATETEXT.equals(dataType))
				type = IQueryConstants.DATE;
			else if (EditorTypeConst.LITERALDATE.equals(dataType))
				type = IQueryConstants.LITERALDATE;
			else if(EditorTypeConst.DATETIMETEXT.equals(dataType))
				type = IQueryConstants.TIME;
			else if (EditorTypeConst.COMBODATA.equals(dataType)){
				type = IQueryConstants.USERCOMBO;
				if("4".equals(data_type)){
					type = IQueryConstants.BOOLEAN;
				}
			}else if (EditorTypeConst.REFERENCE.equals(dataType))
				type = IQueryConstants.UFREF;
			else if (EditorTypeConst.CHECKBOX.equals(dataType))
				type = IQueryConstants.BOOLEAN;
			else
				type = IQueryConstants.STRING;
			meta.setDataType(type);
			if  (EditorTypeConst.REFERENCE.equals(dataType)){
				defaultValue = (String)row.getValue(ds.nameToIndex("refpk"));
				meta.setDefaultValue(defaultValue);
				String refCode = (String)row.getValue(ds.nameToIndex("refCode"));
				if(refCode==null || "".equals(refCode)){
					String refNodeId = row.getValue(7)==null?null:row.getValue(7).toString();
					if(refNodeId!=null){
						IRefNode refNode = getCurrentWidget().getViewModels().getRefNode(refNodeId);
						if(refNode != null){
							if(refNode instanceof NCRefNode){
								refCode = ((NCRefNode)refNode).getRefcode();
							}else if(refNode instanceof LfwRefNode){
								refCode = ((LfwRefNode)refNode).getLfwRefCode();
							}
						}
					}
				}
				meta.setValueEditorDescription(refCode);
			}
			String conditionType = (String)row.getValue(ds.nameToIndex("query_condition_type"));
			if ("0".equals(conditionType)){
				meta.setRequired(true);
			}
			else if ("1".equals(conditionType)){
				meta.setFixCondition(true);
			}
			else if ("2".equals(conditionType)){
				meta.setDefault(true);
			}else if("4".equals(conditionType)){
				meta.setFixCondition(true);
				meta.setRequired(true);
			}else{ 
				meta.setCondition(false);
			}
			String logicType = (String)row.getValue(ds.nameToIndex("query_condition_is"));
			if ("1".equals(logicType)){
				meta.setCondition(false);
			}
			filter.setFilterMeta(meta);
			IOperator operator = GzOperatorFactory.getInstance().getOperator(operatorValue);
			if(operator!=null)
				filter.setOperator(operator);
			else{
				if(meta.getOperators() != null && meta.getOperators().length > 0)
					filter.setOperator(meta.getOperators()[0]);
			}
			filters.add(filter);
		}
		return filters;
	}

	/**
	 * 查询条件数据集 转换为 DefaultFilter
	 * 
	 * @param ds
	 * @return
	 */
	public static List<DefaultFilter> queryConditionDs2Filter(Dataset ds){
		RowData rowData = ds.getCurrentRowSet().getCurrentRowData();
		Row[] rows = rowData==null?null:rowData.getRows();
		List<DefaultFilter> filters = new ArrayList<DefaultFilter>();
		if(rows==null || rows.length==0){
			return filters;
		}
		for (Row row : rows){
			DefaultFilter filter = new DefaultFilter();
			FilterMeta meta = new FilterMeta();
			String code = (String)row.getValue(ds.nameToIndex("query_condition_field"));
			meta.setFieldCode(code);
			String name = (String)row.getValue(ds.nameToIndex("query_condition_label"));
			meta.setFieldName(name);
			String resId = (String)row.getValue(ds.nameToIndex("resId"));
			meta.setResId(resId);
			String langDir = (String)row.getValue(ds.nameToIndex("langDir"));
			meta.setLangdir(langDir);
			String operatorValue = (String)row.getValue(ds.nameToIndex("query_condition_condition"));
			if(StringUtils.isNotBlank(operatorValue)){
				if(operatorValue.contains("&lt;")){
					operatorValue = operatorValue.replace("&lt;", "<");
				}
			}
			meta.setOperators(OperatorFactory.getInstance().getOperators(operatorValue));
			if(ds.nameToIndex("operators")!=-1){
				String usedOperatorsValue = (String)row.getValue(ds.nameToIndex("operators"));
				meta.setOperators(OperatorFactory.getInstance().getOperators(usedOperatorsValue));
			}
			String defaultValue = (String)row.getValue(ds.nameToIndex("query_condition_value"));
			int sysVarIndex = ds.nameToIndex("sys_var");
			if(sysVarIndex !=-1 ){
				String sysVar = (String)row.getValue(sysVarIndex);
				if(StringUtils.isNotEmpty(sysVar) && sysVar.contains("$")){
					defaultValue = sysVar;
				}
			}
			if(StringUtils.isNotBlank(defaultValue)){
				String[] defaultValues = defaultValue.split(",");
				if(defaultValues != null && defaultValues.length > 1 && !"between".equals(operatorValue)){
					defaultValue = defaultValues[0];
				}
			}
//			defaultValue = getValidSqlValue(defaultValue);
			meta.setDefaultValue(defaultValue);
			String dataType = (String)row.getValue(ds.nameToIndex("query_condition_editorType"));
			String data_type = (String)row.getValue(ds.nameToIndex("query_condition_dataType"));
			String parent_field = (String)row.getValue(ds.nameToIndex("parent_field"));
			String tableName = (String)row.getValue(ds.nameToIndex("tableName"));
			meta.setTableCode(tableName);
			meta.setWhere(parent_field);
			if(ds.nameToIndex("trueFieldCode")!=-1){
				String trueFieldCode = (String)row.getValue(ds.nameToIndex("trueFieldCode"));
				meta.setTrueFieldCode(trueFieldCode);
			}
			if(ds.nameToIndex("varType")!=-1){
				String varType = row.getString(ds.nameToIndex("varType"));
				meta.setVarType(varType);
			}
			int type = 0;
			if (EditorTypeConst.STRINGTEXT.equals(dataType))
				type = IQueryConstants.STRING;
			else if (EditorTypeConst.INTEGERTEXT.equals(dataType))
				type = IQueryConstants.INTEGER;
			else if (EditorTypeConst.DECIMALTEXT.equals(dataType))
				type = IQueryConstants.DECIMAL;
			else if (EditorTypeConst.DATETEXT.equals(dataType))
				type = IQueryConstants.DATE;
			else if (EditorTypeConst.LITERALDATE.equals(dataType))
				type = IQueryConstants.LITERALDATE;
			else if(EditorTypeConst.DATETIMETEXT.equals(dataType))
				type = IQueryConstants.TIME;
			else if (EditorTypeConst.COMBODATA.equals(dataType)){
				type = IQueryConstants.USERCOMBO;
				if("4".equals(data_type)){
					type = IQueryConstants.BOOLEAN;
				}
			}else if (EditorTypeConst.REFERENCE.equals(dataType))
				type = IQueryConstants.UFREF;
			else if (EditorTypeConst.CHECKBOX.equals(dataType))
				type = IQueryConstants.BOOLEAN;
			else if(EditorTypeConst.YEARMONTHTEXT.equals(dataType)){
				type = IQueryConstArgs.SHORTDATE;
			}else if(EditorTypeConst.YEARTEXT.equals(dataType)){
				type = IQueryConstArgs.YEARDATE;
			}else if(EditorTypeConst.MONTHTEXT.equals(dataType)){
				type = IQueryConstArgs.MONTHDATE;
			}
			else
				type = IQueryConstants.STRING;
			meta.setDataType(type);
			if  (EditorTypeConst.REFERENCE.equals(dataType)){
				defaultValue = (String)row.getValue(ds.nameToIndex("refpk"));
				meta.setDefaultValue(defaultValue);
				String refCode = (String)row.getValue(ds.nameToIndex("refCode"));
				if(refCode==null || "".equals(refCode)){
					String refNodeId = row.getValue(7)==null?null:row.getValue(7).toString();
					if(refNodeId!=null){
						IRefNode refNode = getCurrentWidget().getViewModels().getRefNode(refNodeId);
						if(refNode != null){
							if(refNode instanceof NCRefNode){
								refCode = ((NCRefNode)refNode).getRefcode();
							}else if(refNode instanceof LfwRefNode){
								refCode = ((LfwRefNode)refNode).getLfwRefCode();
							}
						}
					}
				}
				meta.setValueEditorDescription(refCode);
			}
			String conditionType = (String)row.getValue(ds.nameToIndex("query_condition_type"));
			if ("0".equals(conditionType)){
				meta.setRequired(true);
			}
			else if ("1".equals(conditionType)){
				meta.setFixCondition(true);
			}
			else if ("2".equals(conditionType)){
				meta.setDefault(true);
			}else if("4".equals(conditionType)){
				meta.setFixCondition(true);
				meta.setRequired(true);
			}else{ 
				meta.setCondition(false);
			}
			String logicType = (String)row.getValue(ds.nameToIndex("query_condition_is"));
			if ("1".equals(logicType)){
				meta.setCondition(false);
			}
//			meta.setTableCode(vo.getTableCode());
//			meta.setTableName(vo.getTableName());
//			meta.setIfAutoCheck(vo.getIfAutoCheck()==null?false:vo.getIfAutoCheck().booleanValue());
//			meta.setIsEditable(vo.getIfImmobility()==null?false:vo.getIfImmobility().booleanValue());
//			meta.setDispType(vo.getDispType());
//			meta.setReturnType(vo.getReturnType());
//			meta.setValueEditorDescription(vo.getConsultCode());
//			meta.setDataPower(vo.getIfDataPower().booleanValue());
//			meta.setUserDef(vo.getUserDef());
//			meta.setInstrumentedsql(vo.getInstrumentedsql());
//			meta.setOrder(vo.getIfOrder().booleanValue());
			filter.setFilterMeta(meta);
			IOperator operator = OperatorFactory.getInstance().getOperator(operatorValue);
			if(operator!=null)
				filter.setOperator(operator);
			else{
				if(meta.getOperators() != null && meta.getOperators().length > 0)
					filter.setOperator(meta.getOperators()[0]);
			}
//			String strValue = vo.getValue();
//			RefResultVO refVO = vo.getRefResult();
//			DefaultFieldValue value = createDefaultFieldValue(meta, strValue, refVO);				
//			filter.setFieldValue(value);
			filters.add(filter);
		}
		return filters;
	}
	
	public static LfwView getCurrentWidget() {
		return AppLifeCycleContext.current().getApplicationContext()
				.getCurrentWindowContext().getCurrentViewContext().getView();
	}
	
	/**
	 * 生成默认DefaultFilter
	 * @param meta
	 * @return
	 */
	public static IFilter createDefaultFilter(FilterMeta meta){
		String strValue = meta.getDefaultValue();
		if(strValue==null || strValue.trim().length() == 0)
			return null;
		else{
			DefaultFilter filter = new DefaultFilter();
			filter.setFilterMeta(meta);
			filter.setOperator(meta.getOperators()[0]);
			DefaultFieldValue fieldValue = createDefaultFieldValue(meta, strValue, null);
			filter.setFieldValue(fieldValue);
			return filter;
		}
	}
	
	/**
	 * 生成DefaultFilter
	 * 
	 * @param meta
	 * @param vo
	 * @return
	 */
	public static IFilter createFilterByConditionVo(FilterMeta meta, ConditionVO vo){
		DefaultFilter filter = new DefaultFilter();
		filter.setFilterMeta(meta);
		IOperator operator = OperatorFactory.getInstance().getOperator(vo.getOperaCode());
		if(operator!=null)
			filter.setOperator(operator);
		else{
			filter.setOperator(meta.getOperators()[0]);
		}
		String strValue = vo.getValue();
		RefResultVO refVO = vo.getRefResult();
		DefaultFieldValue value = createDefaultFieldValue(meta, strValue, refVO);				

		filter.setFieldValue(value);
		return filter;
	}
	
	/**
	 * 从规则模板数据集得到QuerySchemeObject
	 * @param dataset
	 * @return
	 */
	public static QuerySchemeObject fetchGzSchemeFormDs(Dataset dataset){
		QuerySchemeObject qsobject = new QuerySchemeObject();
		fetchGzCriteria(qsobject, dataset);							
		fetchQueryTypeInfo(qsobject);
		
		return qsobject;
	}
	
	/**
	 * 从数据集得到QuerySchemeObject
	 * 
	 * @param dataset
	 * @return
	 */
	public static QuerySchemeObject fetchQuerySchemeFromDs(Dataset dataset){
		QuerySchemeObject qsobject = new QuerySchemeObject();
		
		fetchCriteria(qsobject, dataset);							

		fetchQueryTypeInfo(qsobject);

		fetchNormalCondition(qsobject);
		
		fetchLogicalCondtion(qsobject);
		
		return qsobject;
	}
	
	/**
	 * 设置查询条件到数据集中
	 * @param qsobject
	 * @param dataset
	 */
	public static void setQuerySchemeObjectToDs(QuerySchemeObject qsobject, Dataset dataset){
		QueryResult queryResult = (QueryResult)QueryTemplateUtils.readFromXML(qsobject.getQueryResult());
		IRule resultRule = queryResult.getResultRule();
		if (resultRule instanceof ContainerRule) {
			//只处理并且
			if (resultRule instanceof AndRule)
				AndOperator.getInstance();
			else
				OrOperator.getInstance();

			dataset.clear();
			dataset.setCurrentKey("0");
			IRule[] rules = ((ContainerRule) resultRule).getChildrenRules();
			if (rules != null) {
				//只处理一层条件
				for (IRule rule : rules) {
					DefaultFilter filter = (DefaultFilter)((QueryRule) rule).getFormatObject();
					FilterMeta meta = (FilterMeta)filter.getFilterMeta();
					meta.setOperator(filter.getOperator());
					setMetaToDs(meta, dataset, null, true);
				}
			}
		}	
	}
	
	public static void addElementComponent(LfwView widget,FilterMeta meta,String gzClazz){
		String fieldId = meta.getFieldCode().replaceAll("\\.", "_");
		 switch (meta.getDataType()) {
		 	case IQueryConstants.UFREF:
			 	String refCode = meta.getValueEditorDescription();
			 	boolean ifContainOrg = meta.isIfContainOrg();
			 	if("-99".equals(refCode)){
					break;
				}
			 	if("地址簿".equals(refCode)){   //碰到地址簿参照需要特殊处理
					refCode = "地址";
				}
				NCRefNode refNode = new NCRefNode();
				refNode.setId(fieldId + "_refNode");
				refNode.setRefcode(refCode);
				String[] refEles = LfwRefModelManager.getRefEles(refCode);
				String readFields = null;
				if(refEles==null){
					LfwLogger.error(LfwResBundle.getInstance().getStrByID("imp", "AdvancedQueryWidgetProvider-000000")/*参照编码为  */+refCode + LfwResBundle.getInstance().getStrByID("imp", "AdvancedQueryWidgetProvider-000001")/* 的元数据不存在*/);
					break;
//					throw new LfwRuntimeException(LfwResBundle.getInstance().getStrByID("imp", "AdvancedQueryWidgetProvider-000000")/*参照编码为  */+refCode + LfwResBundle.getInstance().getStrByID("imp", "AdvancedQueryWidgetProvider-000001")/* 的元数据不存在*/);
				}
				if(refEles[0] == null){
					if(refEles[1] == null || refEles[2] == null){
						break;
					}
					readFields = refEles[1] + "," + refEles[2];
				//读入字段属性，包含Pk和name字段
				}else{
					if(refEles[0] == null || refEles[2] == null){
						break;
					}
					readFields = refEles[0] + "," + refEles[2];
				}
				String dsId = "masterDs";
				// 写入ds
				refNode.setWriteDs("queryConditionDataset");
				refNode.setReadDs(dsId);
				refNode.setReadFields(readFields);

				// 参照写入字段
				refNode.setWriteFields("refpk,query_condition_value");
				refNode.setI18nName(refCode);
				refNode.setMultiSel(true);
				if(gzClazz!=null && !"".equals(gzClazz.trim()))
					refNode.setDataListener(gzClazz);
				refNode.setPagemeta("reference");
				if(ifContainOrg){
					refNode.setOrgs(true);
				}
				refNode.setFilterRefNodeNames(true);
				widget.getViewModels().addRefNode(refNode);
			 break;
		 case IQueryConstants.BOOLEAN:
			 StaticComboData bcd = new StaticComboData();
				bcd.setId("comb_" + fieldId + "_value");
				CombItem cItem = new CombItem();
				// cItem.setI18nName("");
				// cItem.setValue("");
				// bcd.addCombItem(cItem);

				cItem = new CombItem();
				cItem.setText(NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("uapgztemplate",
								"GZVO2MetaConvertor-000000")/* 是 */);
				// cItem.setI18nName("是");
				cItem.setValue("Y");
				bcd.addCombItem(cItem);
				//					
				//					
				cItem = new CombItem();
				cItem.setText(NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("uapgztemplate",
								"GZVO2MetaConvertor-000001")/* 否 */);
				// cItem.setI18nName("否");
				cItem.setValue("N");
				bcd.addCombItem(cItem);

				widget.getViewModels().addComboData(bcd);
			 break;
		 case IQueryConstants.USERCOMBO:
			 String valueStr = meta.getValueEditorDescription();
				if (valueStr != null) {
					StaticComboData cd = new StaticComboData();
					cd.setId("comb_"+ fieldId+ "_value");
					CombItem item = new CombItem();
					item.setI18nName("");
					item.setText("");
					item.setValue("");
					cd.addCombItem(item);

					if (valueStr.startsWith("I,")) {
						valueStr = valueStr.substring(2);
						String[] values = valueStr.split(",");
						for (int i = 0; i < values.length; i++) {
							item = new CombItem();
							item.setText(values[i]);
							item.setValue("" + i);
							cd.addCombItem(item);
						}
					} else if (valueStr.startsWith("LX,")) {
						valueStr = valueStr.substring(3);
						String[] valuesPair = valueStr.split(",");
						for (int i = 0; i < valuesPair.length; i++) {
							String pair = valuesPair[i];
							String[] values = pair.split("=");
							item = new CombItem();
							item.setText(values[0]);
							item.setValue(values[1]);
							cd.addCombItem(item);
						}
					} else if (valueStr.startsWith("IX,")) {
						valueStr = valueStr.substring(3);
						String[] valuesPair = valueStr.split(",");
						for (int i = 0; i < valuesPair.length; i++) {
							String pair = valuesPair[i];
							String[] values = pair.split("=");
							item = new CombItem();
							item.setText(values[0]);
							item.setValue(values[1]);
							cd.addCombItem(item);
						}
					} else if (valueStr.startsWith("IM,")
							|| valueStr.startsWith("SM,")) {
						valueStr = valueStr.substring(3);
						try {
							IEnumType enumType = MDQueryService
									.lookupMDInnerQueryService()
									.getEnumTypeByID(valueStr);
							if (enumType != null) {
								List<IEnumValue> values = enumType
										.getEnumValues();
								if (null != values) {
									Iterator<IEnumValue> vit = values
											.iterator();
									while (vit.hasNext()) {
										IEnumValue value = vit.next();
										item = new CombItem();
										item.setText(value.getName());
										item.setValue(value.getValue());
										cd.addCombItem(item);
									}
								}
							}
						} catch (MetaDataException e) {
							throw new LfwRuntimeException(e.getMessage(), e);
						}
					} else if (valueStr.startsWith("SX,")) {
						valueStr = valueStr.substring(3);
						String[] valuesPair = valueStr.split(",");
						for (int i = 0; i < valuesPair.length; i++) {
							String pair = valuesPair[i];
							String[] values = pair.split("=");
							item = new CombItem();
							item.setText(values[0]);
							item.setValue(values[1]);
							cd.addCombItem(item);
						}
					}
					widget.getViewModels().addComboData(cd);
				}

			 break;
		 }
		 IOperator[] ops = (IOperator[]) meta.getOperators();
//		 IOperator[] ops = (IOperator[]) meta.getUsedOperators();
			StaticComboData cd = new StaticComboData();
			// 按如下规则构造Id
			
			cd.setId("comb_" + meta.getFieldCode().replaceAll("\\.", "_"));
			if (ops != null) {
				for (IOperator op : ops) {
					CombItem item = new CombItem();
					item.setText(op.toString());
					String operatorValue = op.getOperatorCode();
					// if(operatorValue.equals("<"))
					// operatorValue = "&lt;";
					// else if(operatorValue.equals("<="))
					// operatorValue = "&lt;=";
					item.setValue(operatorValue);
					cd.addCombItem(item);
				}
			}
			widget.getViewModels().addComboData(cd);
	}
	
	
	/**
	 * 将FilterMeta转成成FormElement
	 * @param filterMeta
	 * @return
	 */
	public static FormElement setMetaToFormEle(FilterMeta filterMeta, LfwView widget, String queryClazz,ISysvarProvider sysProvider,String refCodes){
		Dataset ds = widget.getViewModels().getDataset(SimpleQueryWidgetProvider.MAINDS);
		Field f = new Field();
		String defaultCondition = filterMeta.getDefaultCondition();
		int data_type = filterMeta.getDataType();
		String varType = filterMeta.getVarType();
		String resId = filterMeta.getResId();
		String langDir = filterMeta.getLangdir();
		String defaultValue = filterMeta.getDefaultValue();
		int langSeq = filterMeta.getLangSeq();
		String mutilFlag = null;
		if(langSeq > 1){
			mutilFlag = "y";
		}
		boolean isMustCondition = filterMeta.isRequired();
		boolean ifContainOrg = filterMeta.isIfContainOrg();
		boolean ifCheckBox = filterMeta.isIfCheckBox();
		boolean isFixCondition = filterMeta.isFixCondition();
		boolean multiSel = true;
		String[] ref_Codes = null;
		if(refCodes!=null){
			ref_Codes = refCodes.split(",");
		}
		if(data_type==IQueryConstants.DATE || data_type==IQueryConstants.TIME || data_type==IQueryConstants.LITERALDATE || data_type==IQueryConstants.INTEGER || data_type==IQueryConstants.DECIMAL ||  data_type==IQueryConstants.UFTIME || data_type==IQueryConstants.UFYEAR){
			if(null!=defaultCondition && "between".equals(defaultCondition)){
				if(StringUtils.isNotEmpty(defaultValue) && sysProvider!=null){
					String[] dateValues = defaultValue.split(",");
					if(dateValues!=null && dateValues.length>0){
						Object value = sysProvider.parseSysvar((String)dateValues[0]);
						if(value!=null){
							defaultValue = value.toString();
						}
					}
				}
				f.setId(filterMeta.getFieldCode().replaceAll("\\.", "_")+"_start");
				f.setText(filterMeta.getFieldCode()+"_start");
			}else{
				if(StringUtils.isNotEmpty(defaultValue) && sysProvider!=null){
					String[] values = defaultValue.split(",");
					Object value = sysProvider.parseSysvar((String)values[0]);
					if(value!=null){
						defaultValue = value.toString();
						if(StringUtils.isNotEmpty(defaultValue) && values.length > 1){
							Object value_t = sysProvider.parseSysvar((String)values[1]);
							if(value_t!=null){
								defaultValue = defaultValue + "," + value_t.toString();
							}
						}
					}
				}
				f.setId(filterMeta.getFieldCode().replaceAll("\\.", "_"));
				f.setText(filterMeta.getFieldCode());
			}
		}else{
			f.setId(filterMeta.getFieldCode().replaceAll("\\.", "_"));
			f.setText(filterMeta.getFieldCode());
		}
		f.setDefEditFormular(varType);
		
		FormElement formEle = new FormElement();
		String fieldName = filterMeta.getFieldName();
		fieldName = translate(resId,fieldName,langDir);
		String editorType = null;
		String dataType = EditorTypeConst.STRINGTEXT;
			StringBuffer extInfo = new StringBuffer();
			switch(filterMeta.getDataType()){
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
					dataType = StringDataTypeConst.UFDATE;
					break;
				case IQueryConstants.LITERALDATE:
					editorType = EditorTypeConst.DATETEXT;
					f.setDataType(StringDataTypeConst.UFLiteralDate);
					dataType = StringDataTypeConst.UFDATE;
					break;
				case IQueryConstArgs.SHORTDATE:
					editorType = EditorTypeConst.YEARMONTHTEXT;
//					f.setDataType(StringDataTypeConst.UFLiteralDate);
//					dataType = StringDataTypeConst.UFDATE;
					break;
				case IQueryConstArgs.YEARDATE:
					editorType = EditorTypeConst.YEARTEXT;
					dataType = StringDataTypeConst.INTEGER;
					break;
				case IQueryConstArgs.MONTHDATE:
					editorType = EditorTypeConst.MONTHTEXT;
					dataType = StringDataTypeConst.INTEGER;
					break;
				case IQueryConstants.TIME:
					editorType = EditorTypeConst.DATETIMETEXT;
					break;
				case IQueryConstants.BOOLEAN:
//					editorType = EditorTypeConst.COMBODATA;
					if(ifCheckBox){
						editorType = EditorTypeConst.CHECKBOX;
						dataType = StringDataTypeConst.UFBOOLEAN;
					}else{
						editorType = EditorTypeConst.COMBODATA;
//						dataType = StringDataTypeConst.BOOLEAN;
						
						StaticComboData comobData = new StaticComboData();
						comobData.setId("comb_" + filterMeta.getFieldCode().replaceAll("\\.", "_") + "_value");
						
						CombItem itemDefault = new CombItem();
						itemDefault.setText(NCLangRes4VoTransl.getNCLangRes().getStrByID("uapquerytemplate", "QuerySchemeUtils-000004")/*全部*/);
						itemDefault.setValue("");
						if(NCLangRes4VoTransl.getNCLangRes().getStrByID("uapquerytemplate", "QuerySchemeUtils-000004")/*全部*/.equals(defaultValue)){
							defaultValue = "";
						}
						comobData.addCombItem(itemDefault);
						
						CombItem itemY = new CombItem();
						itemY.setText(NCLangRes4VoTransl.getNCLangRes().getStrByID("uapquerytemplate", "QtVO2MetaConvertor-000000")/*是*/);
						itemY.setValue("Y");
						comobData.addCombItem(itemY);
				
						
						CombItem itemN = new CombItem();
						itemN.setText(NCLangRes4VoTransl.getNCLangRes().getStrByID("uapquerytemplate", "QtVO2MetaConvertor-000001")/*否*/);
						itemN.setValue("N");
						comobData.addCombItem(itemN);

						widget.getViewModels().addComboData(comobData);
						formEle.setRefComboData(comobData.getId());
					}
					break;
				case IQueryConstants.UFREF:
					editorType = EditorTypeConst.REFERENCE;
					String refId = CompIdGenerator.generateRefCompId(SimpleQueryWidgetProvider.MAINDS, filterMeta.getFieldCode());

					String queryNode = (String) AppLifeCycleContext.current().getApplicationContext().getAppAttribute(SimpleQueryWidgetProvider.SIMPLEQUERYNODECODE);
					if(StringUtils.isBlank(queryNode) || "null".equals(queryNode)){
						queryNode = (String) AppLifeCycleContext.current().getApplicationContext().getAppSession().getOriginalParamMap().get(AppControlPlugin.NODECODE);
					}
					if(StringUtils.isNotBlank(queryNode)){
						refId = queryNode + "_" + refId;
					}
					formEle.setRefNode(refId);
					formEle.setVisible(false);
					extInfo.append(refId);
					String refCode = filterMeta.getValueEditorDescription();
					String[] refEles = LfwRefModelManager.getRefEles(refCode);
					String readFields = null;
					if(refEles[0] == null){
						readFields = refEles[1] + "," + refEles[2];
					//读入字段属性，包含Pk和name字段
					}else{
						readFields = refEles[0] + "," + refEles[2];
					}
					String shouNameValue = null;
					if(LfwReferenceUtil.isNewRefModel(refCode)){
						LfwAbstractRefModel newRefModel = (LfwAbstractRefModel)LfwReferenceUtil.getRefModel(refCode);
						String[] threeEle = LfwRefModelManager.getRefEles(refCode);
						if(newRefModel!=null){
							String[] defaultValues = defaultValue==null?null:defaultValue.split(",");
							if(defaultValues != null && defaultValues.length>0){
								for(int index = 0; index < defaultValues.length; index++){
									String refValue = defaultValues[index];
									if(StringUtils.isNotBlank(refValue)){
										Object ref_value = sysProvider.parseSysvar((String)refValue);
										if(ref_value!=null){
											defaultValues[index] = ref_value.toString();
										}
									}
								}
							}
							List<List<Object>> datas = newRefModel.getRefResultsByPks(defaultValues, refCode);
							StringBuffer showValue = new StringBuffer();
							int index = newRefModel.getFieldIndex(threeEle[2]);
							if(datas!=null && datas.size()>0){
								int count = 0;
								Iterator<List<Object>> it = datas.iterator();
								while(it.hasNext()){
									List<Object> list = it.next();
									if(count>0){
										showValue.append(";");
									}
									showValue.append(list.get(index));
									count++;
								}
								shouNameValue = showValue.toString();
							}
						}
					}else{
						ILfwRefModel model = LfwRefUtil.getRefModel(refCode);
						if(model != null){
							//LfwRefGenUtil refUtil = new String[]{model.getRefPkField(), model.getRefCodeField(), model.getRefNameField()};
							//new LfwRefGenUtil(model);
							//设置pk_group的值,为了把查询参照数据
							model.setPk_group(LfwRuntimeEnvironment.getLfwSessionBean().getPk_unit());
							//取出参照三要素对应的字段名称(pk, code, name)
							String[] threeEle = new String[]{model.getPkFieldCode(), model.getRefCodeField(), model.getRefNameField()};
							model.setBlurFields(threeEle);
//							model.setBlurValue(defaultValue);
							model.setAddEnvWherePart(false);
							String[] defaultValues = defaultValue==null?null:defaultValue.split(",");
							if(defaultValues!=null && defaultValues.length>0){
								for(int index = 0; index < defaultValues.length; index++){
									String refValue = defaultValues[index];
									if(StringUtils.isNotBlank(refValue)){
										Object ref_value = sysProvider.parseSysvar((String)refValue);
										if(ref_value!=null){
											defaultValues[index] = ref_value.toString();
										}
									}
								}
								Vector dataV = model.matchPKs(defaultValues);
								if(dataV != null && dataV.size() > 0){
									int index = model.getFieldIndex(threeEle[2]);
									StringBuffer showValue = new StringBuffer();
									for(int i=0;i<dataV.size();i++){
										if(i>0)
											showValue.append(";");
										Object value = ((Vector)dataV.get(i)).get(index);
										String show = null;
										if (value instanceof RefValueVO)
											show = (String) ((RefValueVO)value).getNewValue();
										else
											show = (String) ((Vector)dataV.get(i)).get(index);
										
										showValue.append(show);
									}
									shouNameValue = showValue.toString();								
								}
							}
							//row.setString(13, defaultValue);
						}
					}
				
					Field field_mc = ds.getFieldSet().getField(filterMeta.getFieldCode().replaceAll("\\.", "_") + "_mc");
					if(field_mc == null){
						field_mc = new Field();
						field_mc.setId(filterMeta.getFieldCode().replaceAll("\\.", "_") + "_mc");
						field_mc.setText(fieldName);
						if(isMustCondition){
							field_mc.setNullAble(false);
						}
						field_mc.setDefaultValue(shouNameValue);
						ds.getFieldSet().addField(field_mc);
					}
					//生成写入字段，包含字段本身和添加的字段_mc
					String writeFields = filterMeta.getFieldCode().replaceAll("\\.", "_") + "," + filterMeta.getFieldCode().replaceAll("\\.", "_") + "_mc";
					NCRefNode refNode = new RefNodeGenerator().createRefNode(ds, false, filterMeta.getFieldCode(), null, refCode, readFields, writeFields, false, null, null);
					refNode.setId(refId);
					refNode.setText(fieldName);
					refNode.setAllowInput(false);
					refNode.setRead(false);
					if(ifContainOrg){
						refNode.setOrgs(true);
						refNode.setFilterRefNodeNames(true);
					}
					if(StringUtils.isNotEmpty(queryClazz) && !LfwReferenceUtil.isNewRefModel(refCode)){
						refNode.setDataListener(queryClazz);
					}
					multiSel = !CheckUtils.isExist(refCode, ref_Codes);
					refNode.setMultiSel(multiSel);
					widget.getViewModels().addRefNode(refNode);
		
					FormComp form = (FormComp) widget.getViewComponents().getComponent(SimpleQueryWidgetProvider.MAINFORM);
					FormElement formEleMc = form.getElementById(filterMeta.getFieldCode().replaceAll("\\.", "_") + "_mc");
					if(formEleMc == null){
						formEleMc = new FormElement();
						formEleMc.setEditorType(editorType);
						formEleMc.setId(filterMeta.getFieldCode().replaceAll("\\.", "_") + "_mc");
						formEleMc.setText(fieldName);
						formEleMc.setField(filterMeta.getFieldCode().replaceAll("\\.", "_") + "_mc");
						formEleMc.setRefNode(refId);
						formEleMc.setI18nName(resId);
						formEleMc.setLangDir(langDir);
//						formEleMc.setEditable(false);
						if(isMustCondition){
							formEleMc.setNullAble(false);
						}
						if(isFixCondition){
							formEleMc.setEnabled(false);
						}
						form.addElement(formEleMc);
					}										
//					if(refModel != null){
//						//LfwRefGenUtil refUtil = new String[]{model.getRefPkField(), model.getRefCodeField(), model.getRefNameField()};
//						//new LfwRefGenUtil(model);
//						//设置pk_group的值,为了把查询参照数据
//						refModel.setPk_group(LfwRuntimeEnvironment.getLfwSessionBean().getPk_unit());
//						//取出参照三要素对应的字段名称(pk, code, name)
//						String[] threeEle = new String[]{refModel.getPkFieldCode(), refModel.getRefCodeField(), refModel.getRefNameField()};
//					//	model.setBlurFields(threeEle);
//						//model.setBlurValue(defaultValue);
//						//Vector dataV = model.matchBlurData(defaultValue);
////						if(dataV != null && dataV.size() > 0){
////							int index = model.getFieldIndex(threeEle[2]);
////							Object value = ((Vector)dataV.get(0)).get(index);
////							String showValue = null;
////							if (value instanceof RefValueVO)
////								showValue = (String) ((RefValueVO)value).getNewValue();
////							else
////								showValue = (String) ((Vector)dataV.get(0)).get(index);
////							if(showValue != null)
////								row.setString(3, showValue);
////						}
//						//row.setString(13, defaultValue);
//					}
					//存真实值
					//row.setString(13, defaultValue);
					f.setDataType(EditorTypeConst.REFERENCE);
					break;
				case IQueryConstants.USERCOMBO:
					editorType = EditorTypeConst.COMBODATA;
					String valueStr = filterMeta.getValueEditorDescription();
					if(valueStr != null){
						if(valueStr.startsWith("DY,")){
							DynamicComboDataConf cd = new DynamicComboDataConf();
							cd.setId("comb_" + filterMeta.getFieldCode().replaceAll("\\.", "_") + "_value");
							String className = valueStr.substring(3);
							cd.setClassName(className);
							widget.getViewModels().addComboData(cd);
							formEle.setRefComboData(cd.getId());
						}
						else{
							StaticComboData cd = new StaticComboData();
							cd.setId("comb_" + filterMeta.getFieldCode().replaceAll("\\.", "_") + "_value");
							
							CombItem item = new CombItem();
							item.setI18nName("");
							item.setText("");
							item.setValue("");
							cd.addCombItem(item);
	
							
							if(valueStr.startsWith("I,")){
								valueStr = valueStr.substring(2);
								String[] values = valueStr.split(",");
								for (int i = 0; i < values.length; i++) {
									item = new CombItem();
									item.setText(values[i]);
									item.setValue("" + i);
									cd.addCombItem(item);
								}
							}
							else if(valueStr.startsWith("LX,")){
								valueStr = valueStr.substring(3);
								String[] valuesPair = valueStr.split(",");
								for (int i = 0; i < valuesPair.length; i++) {
									String pair = valuesPair[i];
									String[] values = pair.split("=");
									item = new CombItem();
									item.setText(values[0]);
									item.setValue(values[1]);
									cd.addCombItem(item);
								}
							}
							else if(valueStr.startsWith("IX,")){
								valueStr = valueStr.substring(3);
								String[] valuesPair = valueStr.split(",");
								for (int i = 0; i < valuesPair.length; i++) {
									String pair = valuesPair[i];
									String[] values = pair.split("=");
									item = new CombItem();
									item.setText(values[0]);
									item.setValue(values[1]);
									cd.addCombItem(item);
								}
							}
							else if(valueStr.startsWith("IM,") || valueStr.startsWith("SM,")){
								valueStr = valueStr.substring(3);
								try {
									IEnumType enumType = MDQueryService.lookupMDInnerQueryService().getEnumTypeByID(valueStr);
									
									if(enumType != null){
										String langdir = ((Component)(enumType.getContainer())).getResModule();
										List<IEnumValue> values = enumType.getEnumValues();
										Iterator<IEnumValue> vit = values.iterator();
										while(vit.hasNext()){
											IEnumValue value = vit.next();
											item = new CombItem();
											item.setText(translate(value.getResID(), value.getName(), langdir));
											item.setValue(value.getValue());
											cd.addCombItem(item);
										}
									}
								} catch (MetaDataException e) {
									throw new LfwRuntimeException(e.getMessage(), e);
								}
							}else if(valueStr.startsWith("SX,")){
								valueStr = valueStr.substring(3);
								String[] valuesPair = valueStr.split(",");
								for (int i = 0; i < valuesPair.length; i++) {
									String pair = valuesPair[i];
									String[] values = pair.split("=");
									item = new CombItem();
									item.setText(values[0]);
									item.setValue(values[1]);
									cd.addCombItem(item);
								}
							}
							widget.getViewModels().addComboData(cd);
							formEle.setRefComboData(cd.getId());
						}
						
					}
					
					break;
				default:
					editorType = EditorTypeConst.STRINGTEXT;
			}
			
		formEle.setRequired(isMustCondition);
		formEle.setEditorType(editorType);
		formEle.setI18nName(resId);
		formEle.setLangDir(langDir);
		formEle.setId(filterMeta.getFieldCode().replaceAll("\\.", "_"));
		formEle.setText(fieldName);
		if(isFixCondition){
			formEle.setEnabled(false);
			f.setExtSourceAttr("fixCondition");
		}
		if(data_type==IQueryConstants.DATE || data_type==IQueryConstants.TIME || data_type==IQueryConstants.LITERALDATE || data_type==IQueryConstants.INTEGER || data_type==IQueryConstants.DECIMAL ||  data_type==IQueryConstants.UFTIME || data_type==IQueryConstants.UFYEAR){
			if(null!=defaultCondition && "between".equals(defaultCondition)){
				formEle.setField(filterMeta.getFieldCode().replaceAll("\\.", "_")+"_start");
				formEle.setText(fieldName);
				formEle.setId(filterMeta.getFieldCode().replaceAll("\\.", "_")+"_start");
			}else{
				formEle.setField(filterMeta.getFieldCode().replaceAll("\\.", "_"));
			}
			if(null!=defaultCondition && !"".equals(defaultCondition) && !"between".equals(defaultCondition)){
				if("<".equals(defaultCondition)){
					formEle.setText(fieldName);
				}else if(">".equals(defaultCondition)){
					formEle.setText(fieldName);
				}else if(">=".equals(defaultCondition)){
					formEle.setText(fieldName);
				}else if("<=".equals(defaultCondition)){
					formEle.setText(fieldName);
				}
				f.setField(defaultCondition);
			}
		}else{
			formEle.setField(filterMeta.getFieldCode().replaceAll("\\.", "_"));
			formEle.setText(fieldName);
			f.setField(defaultCondition);
		}
			
		if(dataType != null){
			formEle.setDataType(dataType);
		}
		//设置类型
		if(f.getDataType() == null)
			f.setDataType(dataType);
		if(isMustCondition){
			f.setNullAble(false);
			formEle.setNullAble(false);
		}
		f.setExtSource(mutilFlag);
		f.setDefaultValue(defaultValue);
		if(ds.getFieldSet()!=null && ds.getFieldSet().getDataSet()==null){
			ds.getFieldSet().setDataSet(ds);
		}
		ds.getFieldSet().addField(f);
		return formEle;
	}
	

	
	
	public static String translate(String i18nName, String defaultI18nName, String langDir) {
		if(langDir == null || langDir.equals("")){
			if(defaultI18nName == null)
				return "";
			return defaultI18nName;
		}
		if (i18nName == null && defaultI18nName == null)
			return "";
//		if (langDir == null)
//			langDir = LfwRuntimeEnvironment.getLangDir();
		String result = LanguageUtil.getWithDefaultByProductCode(langDir, defaultI18nName, i18nName);
		if(result == null)
			return "";
		return result;
	}
	
	/**
	 * 把FilterMeta转换到数据集中
	 * 
	 * @param meta
	 * @param ds
	 */
	public static void setMetaToDs(FilterMeta meta, Dataset ds, Map<String, IFilter> fieldCodeFilterMap, boolean designMode){
		
//		IFilter filter = null;
//		if (fieldCodeFilterMap != null)
//			filter = fieldCodeFilterMap.get(meta.getFieldCode());
		if (!meta.isCondition() || meta.isDefault() || meta.isFixCondition() || meta.isRequired()) {
			int idIndex = ds.nameToIndex("query_condition_id");
			int labelIndex = ds.nameToIndex("query_condition_label");
			int conditionIndex = ds.nameToIndex("query_condition_condition");
			int valueIndex = ds.nameToIndex("query_condition_value");
			int parentIdIndex = ds.nameToIndex("query_condition_parentId");
			int editorTypeIndex = ds.nameToIndex("query_condition_editorType");
			int dataTypeIndex = ds.nameToIndex("query_condition_dataType");
			int editorInfoIndex = ds.nameToIndex("query_condition_editorInfo");
			int fieldIndex = ds.nameToIndex("query_condition_field");
			int typeIndex = ds.nameToIndex("query_condition_type");
			int trueidIndex = ds.nameToIndex("query_condition_trueid");
			int parentFieldIndex = ds.nameToIndex("parent_field");
			int tableNameIndex = ds.nameToIndex("tableName");
			int refpkIndex = ds.nameToIndex("refpk");
			int isIndex = ds.nameToIndex("query_condition_is");
			int refCodeIndex = ds.nameToIndex("refCode");
			int trueFieldCodeIndex = ds.nameToIndex("trueFieldCode");
			int operatorsIndex = ds.nameToIndex("operators");
			int resIdIndex = ds.nameToIndex("resId");
			int langDirIndex = ds.nameToIndex("langDir");
			int varTypeIndex = ds.nameToIndex("varType");
			int sysVarIndex = ds.nameToIndex("sys_var");
			String parentId = "queryTemplateSpecialParentId";
			Row row = ds.getEmptyRow();
			String fieldCode = meta.getTrueFieldCode();
			if(StringUtils.isEmpty(fieldCode)){
				fieldCode = meta.getFieldCode();
			}
			fieldCode = fieldCode.replaceAll("\\.", "_");
			long time = new Date().getTime();
			row.setString(idIndex, fieldCode+String.valueOf(time));
			row.setString(tableNameIndex, meta.getTableCode());
			row.setString(parentFieldIndex, meta.getWhere());
			row.setString(resIdIndex, meta.getResId());
			row.setString(langDirIndex, meta.getLangdir());
			
			//在繁体环境下，将文本先转换成中文
			String currentLanguage = MultiLangContext.getInstance().getCurrentLangVO().getLocallang();
			String defaultName = meta.getFieldName();
			if("ZF".equals(currentLanguage)){
				if(meta.getResId()!=null && meta.getLangdir()!=null){
					defaultName = NCLangResOnserver.getInstance().getString(MultiLangContext.getInstance().getCurrentLangVO().getDislpayname(), meta.getLangdir(), defaultName, meta.getResId());
				}
			}
			
			//设置多语
			String fieldName = translate(meta.getResId(), defaultName, meta.getLangdir());
			row.setString(labelIndex, fieldName);
			row.setString(parentIdIndex, parentId);
			if(trueFieldCodeIndex!=-1){
				row.setString(trueFieldCodeIndex, meta.getTrueFieldCode());
			}
			if(varTypeIndex!=-1){
				row.setString(varTypeIndex, meta.getVarType());
			}
			String defaultValue = null;
//			String dynamicDefaultValue = null;
//			if (listener != null)
//				dynamicDefaultValue = listener.getDynamicDefaultValue(meta.getFieldCode());
			//defaultValue = dynamicDefaultValue == null ? meta.getDefaultValue() : dynamicDefaultValue;
			defaultValue = meta.getDefaultValue();
//			String defaultValue = meta.getDefaultValue();
			//存显示值
			//wujd 转义单引号
//			if(null!=defaultValue && !"".equals(defaultValue)){
//				defaultValue = defaultValue.replaceAll("'", "''");
//			}
//			defaultValue = getValidSqlValue(defaultValue);
			row.setString(valueIndex, defaultValue);
			String editorType;
			StringBuffer extInfo = new StringBuffer();
			ISysvarProvider sysProvider = ServiceLocator.getService(ISysvarProvider.class);
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
					editorType = EditorTypeConst.LITERALDATE;
					break;
				case IQueryConstants.TIME:
					editorType = EditorTypeConst.DATETIMETEXT;
					break;
				case IQueryConstArgs.SHORTDATE:
					editorType = EditorTypeConst.YEARMONTHTEXT;
					break;
				case IQueryConstArgs.YEARDATE:
					editorType = EditorTypeConst.YEARTEXT;
					break;
				case IQueryConstArgs.MONTHDATE:
					editorType = EditorTypeConst.MONTHTEXT;
					break;
				case IQueryConstants.BOOLEAN:
					editorType = EditorTypeConst.COMBODATA;
					break;
				case IQueryConstants.UFREF:
					editorType = EditorTypeConst.REFERENCE;
					String refId = meta.getTrueFieldCode();
					if(refId==null || "".equals(refId.trim()))
						refId = meta.getFieldCode();
					refId = refId.replaceAll("\\.", "_") + "_refNode";
					extInfo.append(refId);
					String refCode = meta.getValueEditorDescription();
					if(refCode==null){
						String refNodeId = extInfo.toString();
						if(refNodeId!=null){
							IRefNode refNode = getCurrentWidget().getViewModels().getRefNode(refNodeId);
							if(refNode != null){
								if(refNode instanceof NCRefNode){
									refCode = ((NCRefNode)refNode).getRefcode();
								}else if(refNode instanceof LfwRefNode){
									refCode = ((LfwRefNode)refNode).getLfwRefCode();
								}
							}
						}
					}
					if(refCode == null){
						break;
					}
					if("地址簿".equals(refCode)){   //碰到地址簿参照需要特殊处理
						refCode = "地址";
					}
					if(LfwReferenceUtil.isNewRefModel(refCode)){
						LfwAbstractRefModel newRefModel = (LfwAbstractRefModel)LfwReferenceUtil.getRefModel(refCode);
						String[] threeEle = LfwRefModelManager.getRefEles(refCode);
						if(newRefModel!=null){
							String[] defaultValues = defaultValue==null?null:defaultValue.split(",");
							if(defaultValues != null && defaultValues.length>0){
								for(int index = 0; index < defaultValues.length; index++){
									String refValue = defaultValues[index];
									if(StringUtils.isNotBlank(refValue)){
										Object ref_value = sysProvider.parseSysvar((String)refValue);
										if(ref_value!=null){
											defaultValues[index] = ref_value.toString();
										}
									}
								}
							}
							List<List<Object>> datas = newRefModel.getRefResultsByPks(defaultValues, refCode);
							StringBuffer showValue = new StringBuffer();
							int index = newRefModel.getFieldIndex(threeEle[2]);
							if(datas!=null && datas.size()>0){
								int count = 0;
								Iterator<List<Object>> it = datas.iterator();
								while(it.hasNext()){
									List<Object> list = it.next();
									if(count>0){
										showValue.append(";");
									}
									showValue.append(list.get(index));
									count++;
								}
							}
							if("地址".equals(refCode)){
								AddressVO vo = null;
								if (defaultValues != null && defaultValues.length >0) {
									try {
										vo = ServiceLocator.getService(IAddressService.class).getAddressVOByPk(defaultValues[0]);
									} catch (TplBusinessException e) {
										CpLogger.error(e);
										throw new LfwRuntimeException(e.getMessage());
									}
								}
								if(vo != null){
									ILfwRefModel model = LfwRefUtil.getRefModel("地址簿");
									if(model != null){
										Vector dataV = model.matchPKs(defaultValues);
										IRefNodeDataQryService dataQryService = ServiceLocator.getService(IRefNodeDataQryService.class);
										dataV = dataQryService.getQueryRes(model, dataV, defaultValues);
										if(dataV != null && dataV.size() >0){
											Object value = ((Vector)dataV.get(0)).get(index);
											showValue.append(value);
										}
									}else{
										AddressFormatVO formatVo = new AddressFormatVO(vo);
										try {
											FormatResult result = LfwFormater.formatAddress(formatVo);
											if(result != null){
												showValue.append(result.getValue());
											}
										} catch (FormatException e) {
											LfwLogger.error(e.getMessage(), e);
										}
									}
								}
							}
							row.setString(valueIndex, showValue.toString());
						}
					}else{
						ILfwRefModel model = LfwRefUtil.getRefModel(refCode);
						if(model != null){
							//LfwRefGenUtil refUtil = new String[]{model.getRefPkField(), model.getRefCodeField(), model.getRefNameField()};
							//new LfwRefGenUtil(model);
							//设置pk_group的值,为了把查询参照数据
							model.setPk_group(LfwRuntimeEnvironment.getLfwSessionBean().getPk_unit());
							//取出参照三要素对应的字段名称(pk, code, name)
							String[] threeEle = LfwRefModelManager.getRefEles(refCode);
							model.setBlurFields(threeEle);
//							model.setBlurValue(defaultValue);
							model.setAddEnvWherePart(false);
							String[] defaultValues = defaultValue==null?null:defaultValue.split(",");
							if(defaultValues!=null && defaultValues.length>0){
								for(int index = 0; index < defaultValues.length; index++){
									String refValue = defaultValues[index];
									if(StringUtils.isNotBlank(refValue)){
										Object ref_value = sysProvider.parseSysvar((String)refValue);
										if(ref_value!=null){
											defaultValues[index] = ref_value.toString();
										}
									}
								}
								Vector dataV = model.matchPKs(defaultValues);
								IRefNodeDataQryService dataQryService = ServiceLocator.getService(IRefNodeDataQryService.class);
								dataV = dataQryService.getQueryRes(model, dataV, defaultValues);
								if(dataV != null && dataV.size() > 0){
									int index = model.getFieldIndex(threeEle[2]);
									if(index == -1){
										index = model.getFieldIndex(model.getRefNameField());
									}
									StringBuffer showValue = new StringBuffer();
									for(int i=0;i<dataV.size();i++){
										if(i>0)
											showValue.append(";");
										Object value = ((Vector)dataV.get(i)).get(index);
										String show = null;
										if (value instanceof RefValueVO)
											show = (String) ((RefValueVO)value).getNewValue();
										else
											show = (String) ((Vector)dataV.get(i)).get(index);
										
										showValue.append(show);
									}
									if(showValue != null)
										row.setString(valueIndex, showValue.toString());
									
								}else{
									StringBuffer showValue = new StringBuffer();
									int count = 0;
									for(int index = 0; index < defaultValues.length; index++){
										int i = model.getFieldIndex(threeEle[2]);
										if(i == -1){
											i = model.getFieldIndex(model.getRefNameField());
										}
										if(count > 0){
											showValue.append(";");
										}
										Vector dataV1 = model.matchBlurData(defaultValues[index]);
										if(dataV1 !=null && dataV1.size() >0){
											Object value = ((Vector)dataV1.get(0)).get(i);
											if(value != null){
												showValue.append(value);
												count++;
											}
										}
									}
									row.setString(valueIndex, showValue.toString());
								}
							}
							//row.setString(13, defaultValue);
						}
					}
					
					//存真实值
					row.setString(refpkIndex, defaultValue);
					row.setString(refCodeIndex, refCode);
					break;
					
				case IQueryConstants.USERCOMBO:
					editorType = EditorTypeConst.COMBODATA;
					break;
				default:
					editorType = EditorTypeConst.STRINGTEXT;
			}
			
			row.setString(editorTypeIndex, editorType);
			String operatorValue = null;
			if(meta.getOperator() != null){
				operatorValue = meta.getOperator().getOperatorCode();
				if(designMode && sysProvider!=null && sysVarIndex!=-1 && (editorType == EditorTypeConst.DATETEXT || editorType == EditorTypeConst.DATETIMETEXT)){
					if(sysVarIndex!=-1){
						row.setValue(sysVarIndex, defaultValue);
					}
					if(StringUtils.isNotEmpty(defaultValue) && "between".equals(operatorValue)){
						String[] dates = defaultValue.split(",");
						String default_value = "";
						if(dates!=null && dates.length>1){
							Object value = sysProvider.parseSysvar((String)dates[0]);
							if(value!=null){
								default_value = value.toString();
							}
							Object value1 = sysProvider.parseSysvar((String)dates[1]);
							if(value1!=null){
								default_value = default_value + "," + value1.toString();
							}
							row.setString(valueIndex, default_value);
						}else if(dates!=null && dates.length==1){
							Object value = sysProvider.parseSysvar((String)dates[0]);
							if(value!=null){
								default_value = value.toString();
							}
							if(editorType == EditorTypeConst.DATETIMETEXT){
								UFDateTime endDate = new UFDateTime(default_value); 
								UFDateTime beginDate = getBeforeOneMonthDate(endDate);
								row.setString(valueIndex, beginDate.toString() + "," + endDate.toString());
							}else{
								UFDate date = new UFDate(default_value);
								row.setString(valueIndex, date.asBegin().toString() + "," + date.getDateAfter(1).asEnd().toString());
							}
						}
//						else{
//							if(editorType == EditorTypeConst.DATETIMETEXT){
//								UFDateTime endDate = new UFDateTime(); 
//								UFDateTime beginDate = getBeforeOneMonthDate(endDate);
//								row.setString(valueIndex, beginDate.getMillis() + "," + endDate.getMillis());
//							}else{
//								UFDate date = new UFDate();
//								row.setString(valueIndex, date.asBegin().getMillis() + "," + date.getDateAfter(1).asEnd().getMillis());
//							}
//						}
					}
				}
			}else{
				IOperator[] operators = meta.getOperators();
				for(int i = 0; i < operators.length; i++) {
					if (editorType == EditorTypeConst.DATETEXT || editorType == EditorTypeConst.DATETIMETEXT){
						if (StringUtils.isNotEmpty(defaultValue)){
							if(sysProvider!=null){
								if(sysVarIndex!=-1){
									row.setValue(sysVarIndex, defaultValue);
								}
								String[] dates = defaultValue.split(",");
								String default_value = "";
								if(dates.length>1){
									Object value = sysProvider.parseSysvar((String)dates[0]);
									if(value!=null){
										default_value = value.toString();
									}
									Object value1 = sysProvider.parseSysvar((String)dates[1]);
									if(value1!=null){
										default_value = default_value + "," + value1.toString();
									}
									row.setString(valueIndex, default_value);
								}else if(dates.length==1){
									Object value = sysProvider.parseSysvar((String)dates[0]);
									if(value!=null){
										default_value = value.toString();
									}
									if(editorType == EditorTypeConst.DATETIMETEXT){
										UFDateTime endDate = new UFDateTime(default_value); 
										UFDateTime beginDate = getBeforeOneMonthDate(endDate);
										row.setString(valueIndex, beginDate.toString() + "," + endDate.toString());
									}else{
										UFDate date = new UFDate(default_value);
										row.setString(valueIndex, date.asBegin().toString() + "," + date.getDateAfter(1).asEnd().toString());
									}
								}
							}
						}
						if(operators[i].getOperatorCode().equals("between")){
							operatorValue = "between";
							break;
						}
					}
					else if(operators[i].getOperatorCode().equals("like")){
						operatorValue = "like";
						break;
					}else if(operators[i].getOperatorCode().equals("=")){
						operatorValue = "=";
						break;
					}
				}
			}
			
			row.setString(conditionIndex, operatorValue);
			
			String defaultCondition = meta.getDefaultCondition();
			if(operatorsIndex!=-1){
				String comId = meta.getFieldCode().substring(meta.getFieldCode().lastIndexOf(".")+1);
				ComboData combData = getCurrentWidget().getViewModels().getComboData("comb_" + comId);
				if (combData != null) {
					CombItem[] comboItems = combData.getAllCombItems();
					StringBuffer allValue = new StringBuffer();
					if(comboItems!=null && comboItems.length>0){
						for(int index = 0;index < comboItems.length;index++){
							if(index > 0 )
								allValue.append("@");
							allValue.append(comboItems[index].getValue());
						}
						row.setValue(operatorsIndex, allValue.toString());
					}
				}
					
			}
			if (editorType == EditorTypeConst.DATETEXT || editorType == EditorTypeConst.DATETIMETEXT){
				if(StringUtils.isNotEmpty(defaultCondition) && StringUtils.isNotEmpty(defaultValue)){
					if(sysProvider!=null){
						if(sysVarIndex!=-1){
							row.setValue(sysVarIndex, defaultValue);
						}
						if("between".equals(defaultCondition)){
							String[] dates = defaultValue.split(",");
							if(dates!=null && dates.length>0){
								Object value = sysProvider.parseSysvar((String)dates[0]);
								if(value!=null){
									defaultValue = value.toString();
								}
								Object value1 = sysProvider.parseSysvar((String)dates[1]);
								if(value1!=null){
									defaultValue = defaultValue + "," + value1.toString();
								}
							}
						}else{
							Object value = sysProvider.parseSysvar((String)defaultValue);
							if(value!=null){
								defaultValue = value.toString();
							}
						}
						row.setString(valueIndex, defaultValue);
					}
				}
			}
			String comId = meta.getFieldCode().substring(meta.getFieldCode().lastIndexOf(".")+1);
			ComboData combData = getCurrentWidget().getViewModels().getComboData("comb_" + comId);
			if(defaultCondition!=null && combData!=null){
				if(isExistOperator(defaultCondition,combData)){
					row.setString(conditionIndex, defaultCondition);
				}
			}
//			row.setString(6, "String");
			row.setString(dataTypeIndex, String.valueOf(meta.getDataType()));
			row.setString(editorInfoIndex, extInfo.toString());
			//row.setString(10, extInfo.toString());
			row.setString(fieldIndex, meta.getFieldCode());
			//存放真实ID
			if(operatorsIndex!=-1)
				row.setString(trueidIndex, meta.getFieldCode().replaceAll("\\.", "_"));
			else
				row.setString(trueidIndex, fieldCode);
			String fieldType = "3";
			String logicType = "0";
			if (!meta.isCondition())
				logicType = "2";
			if (meta.isRequired()){
				fieldType = "0";
				if(meta.isFixCondition()){
					fieldType = "4";
				}
			}else if (meta.isFixCondition()){
				fieldType = "1";
			}else if (meta.isDefault()){
				fieldType = "2";
			}
			if(trueFieldCodeIndex==-1){
				row.setString(typeIndex, fieldType);
				row.setString(isIndex, logicType);
			}else{
				row.setString(typeIndex, fieldType);
				row.setString(isIndex, "0");
			}
			ds.addRow(row);
		}
	}
	
	private static boolean isExistOperator(String condtion, ComboData combData){
		CombItem[] items = combData.getAllCombItems();
		if(items!=null && items.length>0){
			for(int i = 0;i< items.length;i++){
				CombItem item = items[i];
				if(item!=null && item.getValue().equals(condtion))
					return true;
			}
		}
		return false;
	}
	
	private static UFDateTime getBeforeOneMonthDate(UFDateTime endDate) {
		int year = endDate.getYear();
		int month = endDate.getMonth();
		int day = endDate.getDay();
		
		month = month == 1 ? 12 : month -1;
		UFDateTime tempDate = new UFDateTime(year + "-" + month + "-01 00:00:00");
		
		int beginDaysMonth = tempDate.getDaysMonth();
		int offsetDays = day > beginDaysMonth ? day : beginDaysMonth;  
		tempDate = endDate.getDateTimeBefore(offsetDays);
		return tempDate; 
	}
	
	public static boolean isNotExist(String fieldCode, Dataset ds){
		if(ds!=null){
			Row[] rows = ds.getCurrentRowData().getRows();
			if(rows!=null && rows.length>0){
				int fieldcodeIndex = ds.nameToIndex("query_condition_field");
				for(Row row : rows){
					if(row==null)
						 continue;
					String fieldcode = row.getString(fieldcodeIndex);
					if(fieldcode!=null && fieldcode.equals(fieldCode))
						return false;
				}
			}
		}
		return true;
	}
	
	public static boolean isNotExist(String fieldCode,List<String> fieldList){
		if(fieldList!=null){
			Iterator<String> it = fieldList.iterator();
			while(it.hasNext()){
				String field = it.next();
				if(field!=null && field.equals(fieldCode))
					return false;
			}
		}
		return true;
	}

	public static QuerySchemeVO getQuerySchemeVoByPk(String pk){
		String where = "pk_queryscheme = '"+  pk + "'";
		Collection<QuerySchemeVO> results = null;
		BaseDAO basedao = new BaseDAO();
		try {
			results = basedao.retrieveByClause(QuerySchemeVO.class, where);
		} catch (DAOException e) {
			CpLogger.error(e.getMessage(), e);
		}
		if(results == null || results.size() == 0) 
			return null;
		return results.toArray(new QuerySchemeVO[0])[0];
	}
	
	public static RuleSchemeVO getRuleSchemeVoPk(String pk){
		String where = "pk_rulescheme = '"+  pk + "'";
		Collection<RuleSchemeVO> results = null;
		BaseDAO basedao = new BaseDAO();
		try {
			results = basedao.retrieveByClause(RuleSchemeVO.class, where);
		} catch (DAOException e) {
			CpLogger.error(e.getMessage(), e);
		}
		if(results == null || results.size() == 0) 
			return null;
		return results.toArray(new RuleSchemeVO[0])[0];
	}
	
	protected static DefaultFieldValue createDefaultFieldValue(FilterMeta meta, String strValue, RefResultVO refVO){
		DefaultFieldValue value = new DefaultFieldValue();

		String[] strValues = strValue.split(",");
		List<IFieldValueElement> elems = createFieldValueElements(meta, strValues, refVO);
		for (IFieldValueElement element : elems){
			value.add(element);
		}
		return value;
	}
	
	private static List<IFieldValueElement> createFieldValueElements(FilterMeta meta, String[] strValues, RefResultVO refVO){
		List<IFieldValueElement> elementList = new ArrayList<IFieldValueElement>();
		for (int i = 0; i < strValues.length; i++){
			final String strValue = strValues[i];
			if (isSysFuntin(strValue)){
				elementList.addAll(createFieldValueElementsForSysFunction(meta, strValue));
			}
			else{
				elementList.add(createFieldValueElement(meta, strValue, null));
			}
		}
		return elementList;
	}
	
	private static List<IFieldValueElement> createFieldValueElementsForSysFunction(FilterMeta meta, final String strValue){
		List<IFieldValueElement> tempElementList =  new ArrayList<IFieldValueElement>();
		return tempElementList;
	}

	private static IFieldValueElement createFieldValueElement(FilterMeta meta, String strValue, RefResultVO refVO) {
		IFieldValueElement elem = null;
		switch (meta.getDataType()) {
			case IQueryConstants.STRING: {
				elem = new DefaultFieldValueElement(strValue,strValue,strValue);
				break;
			}
	
			case IQueryConstants.INTEGER: {
				Integer i = Integer.valueOf(strValue);
				elem = new DefaultFieldValueElement(strValue,strValue,i);
				break;				
			}
			case IQueryConstants.DECIMAL: {
				UFDouble dbl = new UFDouble(strValue);
				elem = new DefaultFieldValueElement(strValue,strValue,dbl);
				break;					
			}
			case IQueryConstants.BOOLEAN: {
				UFBoolean b = UFBoolean.valueOf(strValue);
				DefaultConstEnum enu = new DefaultConstEnum(b,b.booleanValue()?NCLangRes4VoTransl.getNCLangRes().getStrByID("uapquerytemplate", "QtVO2MetaConvertor-000000")/*是*/:NCLangRes4VoTransl.getNCLangRes().getStrByID("uapquerytemplate", "QtVO2MetaConvertor-000001")/*否*/);
				elem = new DefaultFieldValueElement(enu);
				break;				
			}
			case IQueryConstants.USERCOMBO: {
				DefaultConstEnum enu = null;
				if(meta.getValueEditorDescription().startsWith(IQueryConstants.COMBO_INDEX))
					enu = new DefaultConstEnum(Integer.valueOf(strValue),strValue);
				else{
					enu = new DefaultConstEnum(strValue,strValue);
				}
				elem = new DefaultFieldValueElement(enu);
				break;
			}
			case IQueryConstants.UFREF: {
				if(refVO==null){ //认为strValue是PK
					RefValueObject r = new RefValueObject();
					r.setPk(strValue);
					elem = new DefaultFieldValueElement(strValue,strValue,r);
				}
				else
				{
					//返回编码 返回名称
					RefValueObject r = new RefValueObject();
					r.setPk(refVO.getRefPK());
					String showString = meta.getDispType()==IQueryConstants.DISPCODE?refVO.getRefCode():refVO.getRefName();
					String sqlString = (meta.getReturnType()==IQueryConstants.RETURNCODE)?refVO.getRefCode():
						((meta.getReturnType()==IQueryConstants.RETURNNAME?
							refVO.getRefName():refVO.getRefPK()));
					r.setCode(refVO.getRefCode());
					r.setName(refVO.getRefName());
					elem = new DefaultFieldValueElement(showString,sqlString,r);
				}
				break;
			}
			case IQueryConstants.DATE: // 日期
			{
				UFDate date = UFDate.getDate(strValue);
				elem = new DefaultFieldValueElement(date.toString(),date.toString(),date);
				break;
			}
			default:
				// 处理异常情况
				break;
		}
		return elem;
	}
	
	/**
	 * 取规则模板的查询条件
	 * @param qsobject
	 * @param dataset
	 */
	private static void fetchGzCriteria(QuerySchemeObject qsobject, Dataset dataset){
		qsobject.setComplete(true);
		QueryResult queryResult = new QueryResult();
		AndRule rule = new AndRule();
		List<DefaultFilter> filters =  gzConditionDsFilter(dataset);
		for (DefaultFilter filter : filters){
			QueryRule sqlRule = new QueryRule();
			sqlRule.setFormatObject(filter);
			((ContainerRule) rule).addRule(sqlRule);
		}						
		queryResult.setEditorType(0);
		queryResult.setResultRule(rule);
		qsobject.setQueryResult(QueryTemplateUtils.writeToXML(queryResult));
	}
	
	/**
	 * 取查询条件
	 * @param qsobject
	 * @param dataset
	 */
	private static void fetchCriteria(QuerySchemeObject qsobject, Dataset dataset) {
		qsobject.setComplete(true);
		QueryResult queryResult = new QueryResult();
		AndRule rule = new AndRule();
//		Row[] rows = dataset.getCurrentRowData().getRows();
//		for (Row row : rows){
//			row.getValue(dataset.nameToIndex(""));
//		}
		List<DefaultFilter> filters =  queryConditionDs2Filter(dataset);
		for (DefaultFilter filter : filters){
			QueryRule sqlRule = new QueryRule();
			sqlRule.setFormatObject(filter);
			((ContainerRule) rule).addRule(sqlRule);
		}
//		queryResult.setResultRule(tree.ripFixCondition().getQueryResultRule());//保存时将树中固定条件和必输条件去掉							
		queryResult.setEditorType(0);
		queryResult.setResultRule(rule);
		qsobject.setQueryResult(QueryTemplateUtils.writeToXML(queryResult));
	}
	
	private static void fetchQueryTypeInfo(QuerySchemeObject qsobject) {
		qsobject.setQueryType(0);
		
	}
	
	private static void fetchNormalCondition(QuerySchemeObject qsobject) {
		
	}
	
	private static void fetchLogicalCondtion(QuerySchemeObject qsobject) {
		
	}

	private static boolean isSysFuntin(String strValue){
		return strValue==null?false:strValue.startsWith("#")&&strValue.endsWith("#");
	}
	
	private static String getValidSqlValue(String value){
		if(null!=value && !"".equals(value.trim()) && value.contains("'")){
			value = value.replaceAll("'", "''");
		}
		return value;
	}
	
	/**
	 * 验证高级查询中必输字段
	 * @param ds
	 */
	public static void validate(Dataset ds){
		Map<String, Map<Integer,String>> validatorMap = validator(ds);
		if(validatorMap!=null && validatorMap.size()>0){
			StringBuffer buf = new StringBuffer();
			buf.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("uapquerytemplate", "QuerySchemeUtils-000006")/*查询条件*/).append(":");
			Map<Integer,String> errorTypeMap = validatorMap.get("errorTypeMap");
			for(int index=0;index<errorTypeMap.size();index++){
				buf.append(errorTypeMap.get(index)).append("\n");
			}
			buf.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("uapquerytemplate", "QuerySchemeUtils-000007")/*不能为空*/);
			throw new LfwRuntimeException(buf.toString());
		}
	}
	
	/**
	 * 获取ds中必输字段所有信息
	 * @param ds
	 * @return
	 */
	private static Map<String,Map<Integer,String>> validator(Dataset ds){
		Map<String, Map<Integer,String>> validatorMap = new HashMap<String, Map<Integer,String>>();
		Map<Integer,String> errorTypeMap = new HashMap<Integer,String>();
		if(ds==null || ds.getCurrentRowData()==null)
			return null;
		Row[] rows = ds.getCurrentRowData().getRows();
		if(rows==null || rows.length==0)
			return null;
		int labelIndex = ds.nameToIndex("query_condition_label");
		int valueIndex = ds.nameToIndex("query_condition_value");
		int typeIndex = ds.nameToIndex("query_condition_type");
		int resIdIndex = ds.nameToIndex("resId");
		int langDirIndex = ds.nameToIndex("langDir");
		int count = 0;
		for(int index =0; index < rows.length; index++){
			Row row = rows[index];
			if(row==null)
				continue;
			String fieldType = row.getString(typeIndex);
			if(fieldType!=null && "0".equals(fieldType)){
				String fieldValue = row.getString(valueIndex);
				if(StringUtils.isBlank(fieldValue)){
					String fieldName = row.getString(labelIndex);
					String resId = row.getString(resIdIndex);
					String langDir = row.getString(langDirIndex);
					translate(resId, fieldName, langDir);
					errorTypeMap.put(count,fieldName);
					count++;
				}
					
			}
		}
		if(errorTypeMap.size()>0)
			validatorMap.put("errorTypeMap", errorTypeMap);
		return validatorMap;
	}
}
