package nc.scap.pub.util;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.uap.portal.log.ScapLogger;
import nc.vo.bd.currtype.CurrtypeVO;
import nc.vo.pub.BusinessException;

/**
 *  币种工具类
 * @author xulong 
 *
 */
public class CurrtypeUtil {

	/**
	 * 通过主键获得VO对象。
	 * @param key
	 * @return
	 * @throws BusinessException
	 */
	public static CurrtypeVO findCurrtypeVOByPK(String key) throws BusinessException {

		CurrtypeVO currtype = null;
		try {
			Object vo = new BaseDAO().retrieveByPK(CurrtypeVO.class, key);
			currtype = (vo == null ? null : (CurrtypeVO) vo);

		} catch (Exception e) {
			ScapLogger.error(e.getMessage(), e);
			throw new BusinessException(
					"CurrtypeBean::findByPrimaryKey(CurrtypePK) Exception!", e);
		}
		return currtype;
	}
	
	/**
	 *  通过Code查找币种PK
	 * @param code
	 * @return
	 */
	public static String findCurrtypeByCode(String code){
		String currtypePk="";
		CurrtypeVO currtypeVO=findCurrtypeBySql(" code='"+code+"'");
		if (currtypeVO!=null){
			currtypePk=currtypeVO.getPk_currtype();
		}
		return currtypePk;
	}
	/**
	 * 通过name 查找币种PK
	 * @param name
	 * @return
	 */
	public static String findCurrtypeByName(String name){
		String currtypePk="";
		CurrtypeVO currtypeVO=findCurrtypeBySql(" name='"+name+"'");
		if (currtypeVO!=null){
			currtypePk=currtypeVO.getPk_currtype();
		}
		return currtypePk;
	}
	
	private static CurrtypeVO findCurrtypeBySql(String wheresql){
		CurrtypeVO currtype = null;
		try {
		List<CurrtypeVO> currtypelist=(List<CurrtypeVO>) new BaseDAO().retrieveByClause(CurrtypeVO.class, wheresql);
		   if (currtypelist!=null && currtypelist.size()>0){
			   currtype=currtypelist.get(0);
		   }
		} catch (DAOException e) {
			ScapLogger.error(e.getMessage(), e);
		}
		return currtype;
		
	}
}
