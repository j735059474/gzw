package nc.scap.jjpub.util;

import nc.scap.pub.util.CntUtil;
import nc.uap.wfm.model.Task;

/**
 * 组装SQL语句
 * 2014-10-16
 * @author thx
 *
 */
public class SqlUtil {
	
	/**
	 * 组装sql like语句
	 * 例如
	 * fields[0] like '%value%' oper fields[1] like '%value%'
	 * @param fields
	 * @param value
	 * @param oper and / or
	 * @return
	 */
	public static String sqlLike(String[] fields,String value,String oper){
		if(fields == null || fields.length == 0||value==null||value.equals("")){
			return "(1=1)";
		}
		StringBuilder sb=new StringBuilder(" (").append(fields[0]).append(" like '%").append(value).append("%'");
		for (int i = 1; i < fields.length; i++) {
			String field = fields[i];
			sb.append(" ").append(oper).append(" ").append(field).append(" like '%").append(value).append("%'");
		}
		sb.append(") ");
		return sb.toString();
	}
	
	/**
	 * 返回sql in 语句  
	 * field in ('values[0]','values[1]')
	 * @param field 字段
	 * @param values 值
	 * @return
	 */
	public static String getSqlIn(String field,String[] values){
		if(values==null||values.length==0){
			return field+" in ()";
		}
		StringBuilder sb=new StringBuilder(" ").append(field).append(" in (");
		for (String value : values) {
			sb.append("'").append(value).append("',");
		}
		sb.setLength(sb.length()-1);
		sb.append(") ");
		return sb.toString();
		
	}
	
	
	public static String getApprSql(String pkfield){
		String sql = pkfield+" in (SELECT pk_frmins FROM wfm_task WHERE pk_owner = '"+CntUtil.getCntUserPk()+"' and (state = '" + Task.State_Run + "' "+"or state = '" + Task.State_BeforeAddSignPlmnt + "'"+" or state = '" + Task.State_Plmnt + "'"+")" + 
				" and (pk_dynamiconwer is null or pk_dynamiconwer = '~'))";
		return sql;
	}

}
