package nc.scap.jjpub.checkutil;

import java.util.HashMap;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.scap.jjpub.util.JjUtil;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.util.LfwClassUtil;
import nc.vo.pub.SuperVO;

public class CheckUnion {

	BaseDAO dao;

	BaseDAO getBaseDAO() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	public void execute_selectRow(Dataset ds,String[] fields,String wheresql,String msg)throws LfwRuntimeException{
		execute_Row(ds, ds.getSelectedRow(), fields, wheresql, msg);
	}
	
	
	/**
	 * 校验 fields 字段 不能同时重复
	 * @param ds
	 * @param fields
	 * @param msg 错误信息提示
	 * @throws LfwRuntimeException
	 */
	public void execute_Row(Dataset ds,Row row,String[] fields,String wheresql,String msg)throws LfwRuntimeException{
		String flag=JjUtil.getDSvalue(ds, row, "sfqy");//是否启用
		String enablestate=JjUtil.getDSvalue(ds, row, "enablestate");//是否启用
		if(!("Y".equals(flag) || "2".equals(enablestate))){
			return;
		}
		
		String pkValue = (String) row.getValue(ds.nameToIndex(ds.getPrimaryKeyField()));
		pkValue=pkValue==null?" ":pkValue;//如果为null或"" 会导致sql 在 != 号情况下 查询不出结果
		SuperVO svo = (SuperVO) LfwClassUtil.newInstance(ds.getVoMeta());
		
		StringBuilder sb=new StringBuilder("select count(1) nnum from ").append(svo.getTableName()).append(" where dr = 0 and ");
		SQLParameter par = new SQLParameter();
		sb.append(ds.getPrimaryKeyField()).append(" != ?");
		par.addParam(pkValue);
		for (int i = 0; i < fields.length; i++) {
			int index=ds.nameToIndex(fields[i]);
			if(index >= 0){
				sb.append(" and ").append(fields[i]).append(" = ?");
				String str=JjUtil.getDSvalue(ds, row, fields[i]);
				par.addParam(str);
			}
		}
		try {
			if(wheresql != null && wheresql.trim().length() > 0) 
				sb.append(" and ").append(wheresql);
			Object obj = getBaseDAO().executeQuery(sb.toString(), par,new ColumnProcessor());
			if(obj!=null && !obj.toString().trim().equals("0")){
				throw new LfwRuntimeException(msg==null?"当前信息重复,保存失败!":msg);
			}
		} catch (DAOException e) {
			e.printStackTrace();
			LfwLogger.error(e.getMessage());
			throw new LfwRuntimeException(e);
		}
		
	}
	
	/**
	 * 
	 * 校验子表界面数据不重复,数据库数据不重复
	 * @param ds
	 * @param fields
	 * @param wheresql 校验数据库内数据是加的where 条件,用于是否启用,是否封存这类情况
	 * @param msg
	 * @throws LfwRuntimeException
	 */
	public void execute_allRow(Dataset ds,String[] fields,String wheresql,String msg)throws LfwRuntimeException{
		Row[] rows = ds.getAllRow();
		HashMap<String,Integer> map=new HashMap<String, Integer>();
		for (int i = 0; i < rows.length; i++) {
			Row row = rows[i];
			String key="key";
			String flag=JjUtil.getDSvalue(ds, row, "sfqy");//是否启用
			String enablestate=JjUtil.getDSvalue(ds, row, "enablestate");//是否启用
			if(!("Y".equals(flag) || "2".equals(enablestate))){
				continue;
			}
			
			for (String field : fields) {
				key+=JjUtil.getDSvalue(ds, row, field);
			}
			if(map.containsKey(key)){
				String errmsg=msg==null ? "第"+(i+1)+"行与第"+map.get(key)+"行信息重复" : msg;
				LfwLogger.error(errmsg);
				throw new LfwRuntimeException(errmsg);
			}else{
				map.put(key, (i+1));
				execute_Row(ds,row, fields,wheresql, "第"+(i+1)+"行已存在,不允许重复");//校验数据库中是否存在
			}
			
		}
		
		
	}
	
	

}
