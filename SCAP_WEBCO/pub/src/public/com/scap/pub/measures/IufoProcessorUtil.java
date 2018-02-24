package com.scap.pub.measures;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.pub.iufo.cache.UFOCacheManager;
import nc.scap.pub.util.ScapDAO;
import nc.uap.portal.log.ScapLogger;
import nc.ui.iufo.keydef.KeyGroupBO_Client;
import nc.ui.iufo.measure.MeasureBO_Client;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.measure.MeasurePackVO;
import nc.vo.iufo.measure.MeasureVO;
import nc.vo.iuforeport.rep.KeyRefMeasuresVO;
import nc.vo.iuforeport.rep.ReportVO;

//import com.measure.vo.IufoMeasures;
/**
 * @author jizhg
 * 
 */
public class IufoProcessorUtil {

	public static final String SELECT_PKREPORT = "select pk_report from iufo_report where code=:1";

	// 根据报表表样编码获取该表样中所有数据区域信息(动态区、非动态区)
	public static MeasurePackVO[] loadPackageByRepCode(String code) {

		MeasurePackVO[] mpacks = null;

		SQLParameter param = new SQLParameter();
		param.addParam(code);
		try {
			String reportPK = (String) ScapDAO.getBaseDAO().executeQuery(SELECT_PKREPORT, param, new ColumnProcessor());
			mpacks = MeasureBO_Client.loadPackageByRepID(reportPK);
		} catch (Exception e) {
			ScapLogger.error(e);
		}

		return mpacks;
	}
	
	/**
	 * 获取报表中所有引用指标
	 */
	public static List<MeasureVO> loadKeyRefMeasures(String code) {
		
		List<MeasureVO> result = new ArrayList<MeasureVO>();
		
		SQLParameter params = new SQLParameter();
		params.addParam(code);
		try {
			ReportVO rep = (ReportVO) ScapDAO.getBaseDAO().executeQuery("select * from iufo_report where code=?", params, new BeanProcessor(ReportVO.class));
			for(KeyRefMeasuresVO m :rep.getKeyRefMeasures()) {
				MeasureVO[] measures = UFOCacheManager.getSingleton().getMeasureCache().getMeasures((String[]) m.getRefMeasures().toArray(new String[0]));
				result.addAll(Arrays.asList(measures));
			}
		} catch (Exception e) {
			ScapLogger.error(e);
		}
		
		return result;
	}
	
	/**
	 * 根据关键字名称过滤MeasurePackVO
	 */
	public static MeasurePackVO filterPackByKeywords(MeasurePackVO[] packs, String... keywords) {
		for (MeasurePackVO pack : packs) {
			
			KeyVO[] keys = UFOCacheManager.getSingleton().getKeyGroupCache().getByPK(pack.getKeyGroupPK()).getKeys();
			if (keys.length == keywords.length) {
				boolean sameName = true;
				for (int i = 0; i < keys.length; i++) {
					KeyVO keyVO = keys[i];
					if (!keyVO.getName().equals(keywords[i])) {
						sameName = false;
					}
				}
				if (sameName) {
					return pack;
				}
			}
		}
		return null;
	}

	/**
	 * 查询Package中所有指标的数据
	 * @return 
	 * {@code List<Map>}
	 * <br/>
	 * {@code Map<K, V> : K-指标名称, V-数据}
	 */
	public static Map<String, Object> loadPackMeasureData(MeasurePackVO packVO, String aloneId) {
		
		Map<String, Object> result = null;
		String keyCombPK = packVO.getKeyGroupPK();
		String pubPKTab = "iufo_measpub_".concat(keyCombPK.substring(keyCombPK.length() - 4));
		String dataTable = packVO.getMeasures().values().iterator().next().getDbtable();

		String sql = new StringBuilder("select * from ").append(dataTable).append(" left join ").append(pubPKTab).append(" using(alone_id) where alone_id='").append(aloneId).append("'").toString();
		try {
			KeyGroupVO keyGroupVO = KeyGroupBO_Client.loadKeyGroupsByIds((String[]) Arrays.asList(keyCombPK).toArray())[0];
			result =  (Map<String, Object>) ScapDAO.getBaseDAO().executeQuery(sql, new MeasJoinKeyProcessor(keyGroupVO, packVO.getAll()));
		} catch (Exception e) {
			ScapLogger.error(e);
		}

		return result;
	}

	// public final String SELECT = "select ";
	// public final String FROM = " from ";
	// public final String IUFO_MEASPUB = "IUFO_MEASPUB_";
	// public final String ALONEID = "alone_id";
	// public final String AND = " and ";
	// public final String WHERE = " where ";
	// public final String AS = " as M_";
	// public final String LEFTOUTERJOIN = " left outer join ";
	// public final String KEYWORD = "keyword";
	// public final String KEYWORD2 = "keyword2";
	//
	// private String firstTable = "";
	// private String selectFields = " ";
	// private String tableFields = " ";
	// private String tables = "";
	// private String singleWhere =" ";
	// public String getSingleWhere() {
	// return singleWhere;
	// }
	// public void setSingleWhere(String singleWhere) {
	// this.singleWhere = singleWhere;
	// }
	// public String getTables() {
	// return tables;
	// }
	// public void setTables(String tables) {
	// this.tables = tables;
	// }
	// private Map<String, IufoMeasures> map = new HashMap<String,
	// IufoMeasures>();
	//
	// public IufoProcessorUtil(String[] codes){
	// init(codes);
	// }
	// //@Override
	// public static MeasExeUtil getInstance(String[] codes) {
	// return new MeasExeUtil(codes);
	// }
	// /**
	// *
	// * @param codes
	// */
	// public void init(String[] codes){
	// IufoMeasures[] vos = this.map2MeasureVOArray(codes);
	// if(vos.length==0)
	// return;
	// String pubTable = getAloneIDTable(vos[0].getPk_keycombo().trim());
	// if(pubTable==null || pubTable.equals("")){
	// return;
	// }
	// String dbColumn = "";
	// String dbTable = "";
	// String tables = "";
	// String where =" where 1=1 ";
	// tables = pubTable;
	// this.setFirstTable(pubTable);
	// for (int i = 0; i < vos.length; i++) {
	// if(vos[i]!=null){
	// if (vos[i].getDbcolumn() != null&& !vos[i].getDbcolumn().equals("")
	// && vos[i].getDbtable() != null&& !vos[i].getDbtable().equals("")){
	// if(i !=vos.length-1){
	// dbColumn += vos[i].getDbtable() + "."+ vos[i].getDbcolumn() + AS+
	// vos[i].getMeasure_name()+",";
	// }else{
	// dbColumn += vos[i].getDbtable() + "."+ vos[i].getDbcolumn() + AS +
	// vos[i].getMeasure_name();
	// }
	// }
	//
	// dbTable += LEFTOUTERJOIN + vos[i].getDbtable() + " on "
	// + vos[i].getDbtable() +"." +ALONEID + "="+firstTable +"."+ALONEID;
	// tables += "," + vos[i].getDbtable();
	// where += AND + firstTable+"." + ALONEID + "=" + vos[i].getDbtable() + "."
	// + ALONEID;
	// }
	// }
	// where += AND + firstTable + ".ver = '0'" ;
	// this.setSelectFields(dbColumn);
	// this.setTableFields(firstTable + MergeString(dbTable,LEFTOUTERJOIN));
	// this.setTables(MergeString(tables,","));
	// this.setSingleWhere(MergeString(where," and "));
	// }
	// public String getSelectFields() {
	// return selectFields;
	// }
	//
	// public void setSelectFields(String selectFields) {
	// this.selectFields = selectFields;
	// }
	//
	// public String getTableFields() {
	// return tableFields;
	// }
	//
	// public void setTableFields(String tableFields) {
	// this.tableFields = tableFields;
	// }
	//
	// public String getWhereFields(int count) {
	// String where = "";
	// if(count >=1){
	// where = WHERE + " 1=1 ";
	// for(int i=1;i<=count;i++){
	// where += AND + KEYWORD + i + " = ?";
	// }
	// }
	// return where;
	// }
	//
	// public Map<String, IufoMeasures> getMap() {
	// return map;
	// }
	//
	// public void setMap(Map<String, IufoMeasures> map) {
	// this.map = map;
	// }
	// public String getFirstTable() {
	// return firstTable;
	// }
	// public void setFirstTable(String firstTable) {
	// this.firstTable = firstTable;
	// }
	// /**
	// * @param code
	// * @return
	// */
	//
	// public Map<String, IufoMeasures> getMeasureVOs(String[] code) {
	// BaseDAO dao = new BaseDAO();
	// String cond = " measure_code in(";
	//
	// for (int i = 0; i < code.length; i++) {
	// if (i == code.length - 1) {
	// cond += "'" + code[i] + "'";
	// } else {
	// cond += "'" + code[i] + "',";
	// }
	// }
	// cond += ")";
	// try {
	// Collection<IufoMeasures> col = dao.retrieveByClause(
	// IufoMeasures.class, cond);
	// java.util.Iterator<IufoMeasures> it = col.iterator();
	// IufoMeasures vo = new IufoMeasures();
	// while (it.hasNext()) {
	// vo = it.next();
	// map.put(vo.getMeasure_code(), vo);
	// }
	// } catch (DAOException e) {
	// Logger.error(e.getMessage());
	// }
	// map.values().toArray();
	// return map;
	// }
	//
	// /**
	// *
	// * @param vos
	// * @return
	// */
	// public String generalSql(int WhereCount) {
	// return SELECT + getSelectFields() + FROM + getTableFields() +
	// getWhereFields(WhereCount);
	// }
	// /**
	// * @param str
	// * @param spl
	// * @return
	// */
	// public String MergeString(String str,String spl) {
	// String[] tableNames = str.split(spl);
	// List<String> list = new ArrayList<String>();
	// for (int i = 0; i < tableNames.length; i++) {
	// if (!list.contains(tableNames[i])) {
	// list.add(tableNames[i]);
	// }
	// }
	// String[] strArray = list.toArray(new String[list.size()]);
	// int length = strArray.length;
	// int size = length - 1;
	// StringBuilder sb = new StringBuilder();
	// for (int i = 0; i < length; i++) {
	// sb.append(strArray[i]);
	// if (i != size) {
	// sb.append(spl);
	// }
	// }
	// return sb.toString();
	// }
	//
	// /**
	// *
	// * @param keyCombo
	// * @return
	// */
	// public String getAloneIDTable(String keyCombo) {
	// return IUFO_MEASPUB
	// + keyCombo.substring(keyCombo.length() - 4, keyCombo.length());
	// }
	// /**
	// *
	// * @param code
	// * @return
	// */
	// public IufoMeasures getPubMeasVo(String code) {
	// return map.get(code);
	// }
	// /**
	// * @param code
	// * @return
	// */
	// public String getDBTable(String code){
	// return map.get(code).getDbtable();
	// }
	// /**
	// * @param code
	// * @return
	// */
	// public String getDBColumn(String code){
	// return map.get(code).getDbcolumn();
	// }
	// /**
	// * @param code
	// * @return
	// */
	// public String getDBKeyComb(String code){
	// return map.get(code).getPk_keycombo();
	// }
	// /**
	// * @param code
	// * @return
	// */
	// public String getDBColumnWithTable(String code){
	// return getDBTable(code)+"." +map.get(code).getDbcolumn();
	// }
	// /**
	// *
	// * @param codes
	// * @return
	// */
	// public IufoMeasures[] map2MeasureVOArray(String[] codes) {
	// Map<String, IufoMeasures> map = this.getMeasureVOs(codes);
	// IufoMeasures[] vo = new IufoMeasures[map.size()];
	// for(int i=0;i<map.size();i++){
	// vo[i] = map.get(codes[i]);
	// }
	// return vo;
	// }
	// /**
	// * 以下为根据关键字获取SQL语句
	// * @return
	// */
	// public String getSimpleSQL(){
	// StringBuffer sb = new StringBuffer();
	// sb.append(SELECT);
	// sb.append(getSelectFields());
	// sb.append(FROM);
	// sb.append(getTables());
	// sb.append(getSingleWhere());
	// return sb.toString();
	// }
	// public String getSQLWithKeyword1(){
	// StringBuffer sb = new StringBuffer();
	// sb.append(SELECT);
	// sb.append(getFirstTable()+".KEYWORD1,");
	// sb.append(getSelectFields());
	// sb.append(FROM);
	// sb.append(getTables());
	// sb.append(getSingleWhere());
	// return sb.toString();
	// }
	// public String getSQLWithKeyword2(){
	// StringBuffer sb = new StringBuffer();
	// sb.append(SELECT);
	// sb.append("SUBSTR("+getFirstTable()+".KEYWORD2,1,7),");
	// sb.append(getSelectFields());
	// sb.append(FROM);
	// sb.append(getTables());
	// sb.append(getSingleWhere());
	// return sb.toString();
	// }
	// public String getSQLWithKeyword3(){
	// StringBuffer sb = new StringBuffer();
	// sb.append(SELECT);
	// sb.append(getFirstTable()+".KEYWORD3,");
	// sb.append(getSelectFields());
	// sb.append(FROM);
	// sb.append(getTables());
	// sb.append(getSingleWhere());
	// return sb.toString();
	// }
	// public String getSQLWithKeyword12(){
	// StringBuffer sb = new StringBuffer();
	// sb.append(SELECT);
	// sb.append(getFirstTable()+".KEYWORD1,"+"SUBSTR("+getFirstTable()+".KEYWORD2,1,7),");
	// sb.append(getSelectFields());
	// sb.append(FROM);
	// sb.append(getTables());
	// sb.append(getSingleWhere());
	// return sb.toString();
	// }
	// public String getSQLWithKeyword13(){
	// StringBuffer sb = new StringBuffer();
	// sb.append(SELECT);
	// sb.append(getFirstTable()+".KEYWORD1,"+getFirstTable()+".KEYWORD3,");
	// sb.append(getSelectFields());
	// sb.append(FROM);
	// sb.append(getTables());
	// sb.append(getSingleWhere());
	// return sb.toString();
	// }
	// public String getSQLWithKeyword23(){
	// StringBuffer sb = new StringBuffer();
	// sb.append(SELECT);
	// sb.append("SUBSTR("+getFirstTable()+".KEYWORD2,1,7),"+getFirstTable()+".KEYWORD3,");
	// sb.append(getSelectFields());
	// sb.append(FROM);
	// sb.append(getTables());
	// sb.append(getSingleWhere());
	// return sb.toString();
	// }
	// public String getSQLWithKeyword123(){
	// StringBuffer sb = new StringBuffer();
	// sb.append(SELECT);
	// sb.append(getFirstTable()+".KEYWORD1,"+"SUBSTR("+getFirstTable()+".KEYWORD2,1,7),"+getFirstTable()+".KEYWORD3,");
	// sb.append(getSelectFields());
	// sb.append(FROM);
	// sb.append(getTables());
	// sb.append(getSingleWhere());
	// return sb.toString();
	// }

}
