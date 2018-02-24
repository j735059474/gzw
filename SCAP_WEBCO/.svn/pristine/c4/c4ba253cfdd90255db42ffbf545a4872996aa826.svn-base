package nc.scap.pub.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.logging.Logger;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.uap.cpb.persist.dao.PtBaseDAO;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.portal.log.ScapLogger;
import nc.vo.pub.SuperVO;
/**
 * 封装数据库访问的常用方法
 * 在此工具类中统一截获数据库异常
 * @create at 2013-7-23 下午3:36:41 
 */
public class ScapDAO {

	private static BaseDAO basedao = new BaseDAO();
	private static PtBaseDAO ptdao = new PtBaseDAO();
	
	public static PtBaseDAO getPtBaseDAO(){
		return ptdao;
	}
	public static BaseDAO getBaseDAO(){
		return basedao;
	}
	
	
	/**
	 * 根据VO、单据主键获取SuperVO对象。调用处需判断对象是否为空
	 * 
	 * @param className
	 * @param pk
	 * @return 返回SuperVO，若出错返回空对象
	 */
	public static SuperVO retrieveByPK(Class className, String pk) {
		
		SuperVO vo = null;
		try {
			vo =  (SuperVO)basedao.retrieveByPK(className, pk);
		} catch (Exception e) {
			ScapLogger.error("==============ScapCo Error, ScapDAO.retrieveByPK :"+e.getMessage());
		}
		
		return vo;
	}
	
	/**
	 * 根据 VO对象，查询条件，排序条件 获取 SuperVO对象数组。调用处需判断对象是否为空
	 * 
	 * @create at 2013-7-23 下午4:06:53    
	 * @param className
	 * @param condition
	 * @param orderBy
	 * @return 返回SuperVO数组，若出错返回空对象。
	 */
	public static SuperVO[] retrieveByCondition(Class className, String condition, String orderBy){
		SuperVO[] vos = null;
		try {
			Collection<SuperVO[]> collection = basedao.retrieveByClause(className, condition.trim(), orderBy.trim());
			if ( collection!=null && collection.size()>0 ) {
				vos = (SuperVO[]) collection.toArray((SuperVO[]) Array.newInstance(className, 0));
			}
		} catch (Exception e) {
			ScapLogger.error("==============ScapCo Error, ScapDAO.retrieveByCondition :"+e.getMessage());
		}
		
		return vos;
	}
	
	
	/**
	 * 根据 VO对象，查询条件 获取 SuperVO对象数组。调用处需判断对象是否为空
	 *  
	 * @create at 2013-7-23 下午4:08:43    
	 * @param className
	 * @param condition
	 * @return 返回SuperVO数组，若出错返回空对象。
	 */
	public static SuperVO[] retrieveByCondition( Class className, String condition ) {
		SuperVO[] vos = null;
		try {
			Collection<SuperVO[]> collection = basedao.retrieveByClause(className, condition);
			if ( collection!=null && collection.size()>0 ) {
				vos = (SuperVO[]) collection.toArray((SuperVO[]) Array.newInstance(className, 0));
			}
		} catch (Exception e) {
			ScapLogger.error("==============ScapCo Error, ScapDAO.retrieveByCondition :"+e.getMessage());
		}
		
		return vos;
	}
	
	/**
	 * 根据 VO对象，查询条件，排序条件 获取  SuperVO对象数组(强制DR=0)。调用处需判断对象是否为空
	 * 
	 * @create at 2013-7-23 下午5:55:43    
	 * @param className
	 * @param condition
	 * @param orderBy
	 * @return 返回SuperVO数组，若出错返回空对象。
	 */
	public static SuperVO[] retrieveByConditionDR(Class className, String condition){
		
		try {
			return ptdao.queryByCondition(className, condition.trim());
		} catch (Exception e) {
			ScapLogger.error("==============ScapCo Error, ScapDAO.retrieveByConditionDR :"+e.getMessage());
			return null;
		}
	}
	

	
	public static void deleteByPk(Class className, String pk) {
		try {
			basedao.deleteByPK(className, pk);
		} catch (Exception e) {
			ScapLogger.error("==============ScapCo Error, ScapDAO.isTableExisted :"+e.getMessage());
		}
	}
	
	/**
	 * 更新VO数组
	 * 
	 * @create at 2013-7-23 下午8:16:38    
	 * @param vo
	 * @return 成功返回0，执行失败返回-1
	 */
	public static int updateVOArray(SuperVO[] vo)  {
		try {
			return ptdao.updateVOArray(vo);
		} catch (Exception e) {
			ScapLogger.error("==============ScapCo Error, ScapDAO.updateVOArray :"+e.getMessage());
			return -1;
		}
	}
	
	/**
	 * 更新单个VO对象
	 * 
	 * @create at 2013-7-23 下午8:19:22    
	 * @param vo
	 * @return 返回更新记录数量，执行失败返回-1
	 */
	public static int updateVO(SuperVO vo)  {
		
		try {
			return ptdao.updateVO(vo);
		} catch (DAOException e) {
			ScapLogger.error("==============ScapCo Error, ScapDAO.updateVO :"+e.getMessage());
			return -1;
		}
	}

	/**
	 * 插入vo数组数据
	 * 
	 * @create at 2013-7-23 下午8:25:56    
	 * @param vo
	 * @return 成功插入记录的 pk 数组，失败返回null
	 */
	public static String[] insertVOArray(SuperVO[] vo)  {
		try {
			return ptdao.insertVOs(vo);
		} catch (Exception e) {
			ScapLogger.error("==============ScapCo Error, ScapDAO.insertVOArray :"+e.getMessage());
			return null;
		}
	}
	
	/**
	 * 插入单个vo数据
	 * 
	 * @create at 2013-7-23 下午8:26:37    
	 * @param vo
	 * @return 成功插入记录的pk，是否返回null
	 */
	public static String insertVO(SuperVO vo) {
		try {
			return ptdao.insertVO(vo);
		} catch (DAOException e) {
			ScapLogger.error("==============ScapCo Error, ScapDAO.insertVO :"+e.getMessage());
			return null;
		}
	}
	
	/**
	 * 插入单个vo数据
	 * 
	 * @create at 2013-7-23 下午8:26:37    
	 * @param vo
	 * @return 成功插入记录的pk，是否返回null
	 */
	public static String insertOrUpdateVO(SuperVO vo) {
		try {
			if(vo.getPrimaryKey() != null) {
				ptdao.updateVO(vo);
				return vo.getPrimaryKey();
			}else {
				return ptdao.insertVO(vo);
			}
		} catch (DAOException e) {
			ScapLogger.error("==============ScapCo Error, ScapDAO.insertVO :"+e.getMessage());
			return null;
		}
	}
	
	/**
	 * 插入单个vo数据
	 * 
	 * @create at 2013-7-23 下午8:26:37    
	 * @param vo
	 * @return 成功插入记录的pk，是否返回null
	 */
	public static boolean insertOrUpdateVOs(SuperVO[] vos) {
		for(SuperVO vo : vos) {
			insertOrUpdateVO(vo);
		}
		return true;
	}
	
	/**
	 * 根据SQL语句查询结果集数量，异常返回-1
	 * @author taoye 2013-7-24
	 */
	public static Integer getCount(String sql) {
		try {
			List list = (List) ptdao.executeQuery(sql, new ColumnListProcessor());
			return (Integer) list.get(0);
		} catch (DAOException e) {
			ScapLogger.error("==============ScapCo Error, ScapDAO.getCount :"+e.getMessage());
			return -1;
		}
	}
	
	/**
	 * 根据SQL语句查询结果集数量，异常返回-1
	 * @author taoye 2013-7-24
	 */
	public static Integer getCount(Class className, String condition) {
		try {
			SuperVO vo = (SuperVO) className.newInstance();
			String tableName = vo.getTableName();
			String sql = "select count(*) from " + tableName + " where " + condition;
			List list = (List) ptdao.executeQuery(sql, new ColumnListProcessor());
			return (Integer) list.get(0);
		} catch (Exception e) {
			ScapLogger.error("==============ScapCo Error, ScapDAO.getCount :"+e.getMessage());
			return -1;
		}
	}
	
	public static void executeUpdate(String sql) {
		try {
			basedao.executeUpdate(sql);
		} catch (Exception e) {
			//ScapLogger.console(sql);
			ScapLogger.error(sql);
			ScapLogger.error("==============ScapCo Error, ScapDAO.executeUpdate :"+e.getMessage());
		}
	}
	public static void executeUpdate(String sql, SQLParameter para) {
		try {
			basedao.executeUpdate(sql, para);
		} catch (Exception e) {
			//ScapLogger.console(sql);
			ScapLogger.error(sql);
			ScapLogger.error("==============ScapCo Error, ScapDAO.executeUpdate :"+e.getMessage());
		}
	}
	public static Object executeQuery(String sql, ResultSetProcessor processor) {
		//ScapLogger.console(sql);
		try {
			return basedao.executeQuery(sql, processor);
		} catch (Exception e) {
			ScapLogger.error(sql);
			ScapLogger.error("==============ScapCo Error, ScapDAO.executeUpdate :"+e.getMessage());
			return null;
		}
	}
	
	public static boolean isTableExisted(String tableName) {
		try {
			boolean flag = basedao.isTableExisted(tableName);
			return flag;
		} catch (Exception e) {
			ScapLogger.error(tableName);
			ScapLogger.error("==============ScapCo Error, ScapDAO.isTableExisted :"+e.getMessage());
		}
		return true;
	}

	public static List queryVOs(Class className, String condition) {
		try {
			
			List PersonContratVOs = (List) getBaseDAO().executeQuery(condition,
					new BeanListProcessor(className));
			return PersonContratVOs;
		} catch (DAOException e) {
			ScapLogger.error(condition);
			ScapLogger.error(e.getMessage());
			throw new LfwRuntimeException("查询出现错误");
		}

	}
}
