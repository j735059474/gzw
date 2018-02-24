package nc.scap.jjpub.checkutil;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.vo.pub.SuperVO;

/**
 * 校验 档案是否被引用
 * 
 * @author thx
 *
 */
public class CheckDocIsRef {

	BaseDAO dao;

	BaseDAO getBaseDAO() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	/**
	 * 校验 ds 选中行的数据是否被引用
	 * @param ds
	 * @throws LfwRuntimeException
	 */
	public void execute(Dataset ds)throws LfwRuntimeException{
		Row row=ds.getSelectedRow();
		if(row==null)
			return;
		String tablename=ds.getTableName();
		String pkfiled=ds.getPrimaryKeyField();
		String pkValue = (String) row.getValue(ds.nameToIndex(ds.getPrimaryKeyField()));
		pkValue=pkValue==null?" ":pkValue;//如果为null或"" 会导致sql 在 != 号情况下 查询不出结果
		
		execute(tablename, pkfiled,pkValue);
	}
	/**
	 *
	 * @param tablename 被检查的表名
	 * @param pkfiled 主键字段
	 *  @param pkValue 主键值
	 */
	public void execute(String tablename, String pkfiled,String pkValue) {
		String sql="select * from scap_refinfo where table1 = ? ";
		SQLParameter par=new SQLParameter();
		par.addParam(tablename);
		try {
			List<Scap_refinfoVO> list=(List<Scap_refinfoVO>) getBaseDAO().executeQuery(sql, par, new BeanListProcessor(Scap_refinfoVO.class));
			if(list == null || list.size() == 0){
				return;
			}
			par=new SQLParameter();
			par.addParam(pkValue);
			for (Scap_refinfoVO refvo : list) {
				StringBuilder sb=new StringBuilder("select count(1) nnum from ");
				sb.append(refvo.getTable1()).append(" join ").append(refvo.getTable2())
				.append(" on ").append(refvo.getTable1()).append(".").append(refvo.getPkfield())
				.append(" = ").append(refvo.getTable2()).append(".").append(refvo.getFkfield())
				.append(" where ").append(tablename).append(".").append(pkfiled)
				.append(" = ? and nvl(").append(refvo.getTable2()).append(".dr,0) = 0");
				
				Object obj=getBaseDAO().executeQuery(sb.toString(), par,new ColumnProcessor());
				if(obj!=null && Integer.parseInt(obj.toString())>0){
					String errmsg=refvo.getMsg();
					if(errmsg == null || errmsg.trim().length() == 0){
						errmsg = refvo.getName1()+" 被 "+refvo.getName2() +" 引用,不能删除当前信息! ";
					}
					throw new LfwRuntimeException(errmsg);
				}
				
			}
			
			
		} catch (DAOException e) {
			e.printStackTrace();
			throw new LfwRuntimeException(e.getMessage());
		}
	}
	
	
}
