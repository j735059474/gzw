package nc.scap.def.util;

import nc.bs.framework.common.NCLocator;
import nc.itf.bd.defdoc.IDefdocQryService;
import nc.itf.bd.defdoc.IDefdoclistQryService;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.CpOrgUtil;
import nc.scap.pub.util.ScapDAO;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.portal.log.ScapLogger;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.bd.defdoc.DefdoclistVO;
import nc.vo.pub.BusinessException;

public class ScapDefUtil {
	
	/**
	 * 根据自定义项档案编码查询自定义项档案列表
	 * 
	 * @author taoye 2013-7-16
	 */
	public static String getDefDocNameByDefCodeAndDataCode(String defcode,String datacode) {
		String pk_defdoclist = null;
		String name = null;
		ScapLogger.debug("scap_debug:根据档案编码：[" + defcode + "]查询档案。");
		try {
			DefdoclistVO[] vos = getDefDocListQryService()
					.queryDefdoclistVOsByCondition(CpOrgUtil.getGlobalOrgPk(),
							"code = '" + defcode + "'");
			if (vos == null || vos.length == 0) {
				ScapLogger.error("根据档案编码：[" + defcode + "]查询档案出错，请与管理员联系。");
				throw new LfwRuntimeException("根据档案编码：[" + defcode
						+ "]查询档案出错，请与管理员联系。");
			}
			if (vos.length > 0) {
				pk_defdoclist = vos[0].getPk_defdoclist();
				DefdocVO[] defDocVos = (DefdocVO[]) ScapDAO.retrieveByCondition(DefdocVO.class, "pk_defdoclist='"+pk_defdoclist+"' and code='"+datacode+"'");
				if(defDocVos != null && defDocVos.length > 0) {
					ScapLogger.debug("根据自定义项档案列表主键和编码"+ pk_defdoclist+" and "+ datacode + " 生成自定义项档案列表DefdocVO正常。");
					name = defDocVos[0].getName();
				}
			}
		} catch (BusinessException e) {
			ScapLogger
					.error("查询异常！根据自定义项档案类型编码： " + defcode + " 查询自定义项档案列表异常！", e);
		}
		return name;
	}

	/**
	 * 根据自定义项档案编码查询自定义项档案列表
	 * 
	 * @author taoye 2013-7-16
	 */
	public static DefdocVO[] getDefDocVosByCode(String code) {
		String pk_defdoclist = null;
		ScapLogger.debug("scap_debug:根据档案编码：[" + code + "]查询档案。");
		try {
			DefdoclistVO[] vos = getDefDocListQryService()
					.queryDefdoclistVOsByCondition(CpOrgUtil.getGlobalOrgPk(),
							"code = '" + code + "'");
			if (vos == null || vos.length == 0) {
				ScapLogger.error("根据档案编码：[" + code + "]查询档案出错，请与管理员联系。");
				throw new LfwRuntimeException("根据档案编码：[" + code
						+ "]查询档案出错，请与管理员联系。");
			}
			if (vos.length > 0) {
				pk_defdoclist = vos[0].getPk_defdoclist();
				ScapLogger.debug("根据自定义项档案类型编码： " + code + " 查询自定义项档案列表");
				return getDefDocVosByDefDocListPk(pk_defdoclist);
			}
		} catch (BusinessException e) {
			ScapLogger
					.error("查询异常！根据自定义项档案类型编码： " + code + " 查询自定义项档案列表异常！", e);
		}
		return new DefdocVO[0];
	}

	/**
	 * 根据自定义项档案编码查询自定义项档案列表
	 * 
	 * @author taoye 2013-7-16
	 */
	public static DefdoclistVO getDefDocListVosByCode(String code) {
		DefdoclistVO vo = null;
		try {
			DefdoclistVO[] vos = getDefDocListQryService()
					.queryDefdoclistVOsByCondition(CpOrgUtil.getGlobalOrgPk(),
							"code = '" + code + "'");
			if (vos.length > 0)
				vo = vos[0];
			ScapLogger.debug("根据自定义项档案类型编码： " + code + " 查询自定义项档案列表");
		} catch (BusinessException e) {
			ScapLogger
					.error("查询异常！根据自定义项档案类型编码： " + code + " 查询自定义项档案列表异常！", e);
		}
		return vo;
	}

	/**
	 * 根据自定义项档案类型查询自定义项档案列表
	 * 
	 * @author taoye 2013-7-16
	 */
	public static DefdocVO[] getDefDocVosByDefDocListPk(String pk_defdoclist) {
		DefdocVO[] vos = getDefDocVosByDefDocListPk(pk_defdoclist, null, null);
		return vos;
	}

	public static DefdocVO[] getDefDocVosByDefDocListName(String name) {
		try {
			DefdoclistVO lvo = getDefdoclistVoByName(name);
			DefdocVO[] vos = getDefDocVosByDefDocListPk(lvo.getPk_defdoclist());
			return vos;
		} catch (Exception e) {
			ScapLogger.error("查询异常！根据自定义项档案类型名称 " + name + " 查询自定义项档案列表异常！", e);
		}
		return null;
	}

	/**
	 * 根据自定义项档案类型、业务单元主键、集团主键查询自定义项档案列表，其中业务单元主键和集团主键可为null或""
	 * 
	 * @author taoye 2013-7-16
	 */
	public static DefdocVO[] getDefDocVosByDefDocListPk(String pk_defdoclist,
			String pk_org, String pk_group) {
		DefdocVO[] vos = getDefDocVosByDefDocListPkAndWherSql(pk_defdoclist,
				pk_org, pk_group, null);
		return vos;
	}

	/**
	 * 根据自定义项档案类型、业务单元主键、集团主键、查询条件查询自定义项档案列表，其中业务单元主键、集团主键和查询条件可为null或""
	 * 
	 * @author taoye 2013-7-16
	 */
	public static DefdocVO[] getDefDocVosByDefDocListPkAndWherSql(
			String pk_defdoclist, String pk_org, String pk_group,
			String whereSql) {
		IDefdocQryService service = getDefDocQryService();
		try {
			if (pk_org == null || pk_org.equals(""))
				pk_org = CntUtil.getCntOrgPk();
			if (pk_group == null || pk_group.equals(""))
				pk_group = CntUtil.getCntGroupPk();
			if (whereSql == null || whereSql.equals(""))
				whereSql = " 1=1 ";
			/*自定义档案读取方式改为直接读数据库，不走NC接口*/
			DefdocVO[] vos = (DefdocVO[]) ScapDAO.retrieveByCondition(DefdocVO.class, "pk_defdoclist = '" + pk_defdoclist+"'");
//			DefdocVO[] vos = service.queryDefdocVOsByDoclistPkAndWhereSql(
//					pk_defdoclist, pk_org, pk_group, whereSql);
			ScapLogger
					.debug("根据自定义项档案类型主键 " + pk_defdoclist + " 查询自定义项档案列表正常。");
			return vos;
		} catch (Exception e) {
			ScapLogger.error("查询异常！根据自定义项档案类型主键 " + pk_defdoclist
					+ " 查询自定义项档案列表异常！", e);
		}
		return null;

	}

	/**
	 * 根据自定义项档案列表主键查询VO
	 * 
	 * @author taoye 2013-7-16
	 */
	public static DefdocVO getDefDocVo(String pk_defdoc) {
		try {
			String[] defdocPks = { pk_defdoc };
			DefdocVO vo = getDefDocVosByPks(defdocPks)[0];
			ScapLogger.debug("根据自定义项档案列表主键 " + pk_defdoc
					+ " 生成自定义项档案列表DefdocVO正常。");
			return vo;
		} catch (Exception e) {
			ScapLogger.error("查询异常！根据自定义项档案列表主键 " + pk_defdoc
					+ " 生成自定义项档案列表DefdocVO异常！", e);
		}
		return null;
	}
	
	/**
	 * 根据自定义项档案列表主键查询VO
	 * @author taoye 2013-7-16
	 */
	public static DefdocVO getDefDocVoByCode(String code) {
		try {
			String condition = "code = '" + code + "'";
			DefdocVO[] vos = (DefdocVO[]) ScapDAO.retrieveByCondition(DefdocVO.class, condition);
			if(vos != null && vos.length > 0) {
				ScapLogger.debug("根据自定义项档案列表主键 " + code + " 生成自定义项档案列表DefdocVO正常。");
				return vos[0];
			}
		}catch(Exception e) {
			ScapLogger.error("查询异常！根据自定义项档案列表主键 " + code + " 生成自定义项档案列表DefdocVO异常！", e);
		}
		return null;
	}

	/**
	 * 根据多个自定义项档案列表主键数组查询自定义项档案列表VO数组
	 * 
	 * @author taoye 2013-7-16
	 */
	public static DefdocVO[] getDefDocVosByPks(String[] defdocPks) {
		IDefdocQryService service = getDefDocQryService();
		try {
			DefdocVO[] vos = service.queryDefdocByPk(defdocPks);
			ScapLogger.debug("根据自定义项档案列表主键 " + defdocPks
					+ " 生成自定义项档案列表DefdocVO正常。");
			return vos;
		} catch (Exception e) {
			ScapLogger.error("查询异常！根据自定义项档案列表主键 " + defdocPks
					+ " 生成自定义项档案列表DefdocVO异常！", e);
		}
		return null;
	}

	/**
	 * 根据多个自定义项档案列表主键查询自定义项档案列表VO数组，多个主键用传入的参数分割
	 * 
	 * @author taoye 2013-7-16
	 */
	public static DefdocVO[] getDefDocVosSpliteByPks(String defdocPks,
			String splite) {
		IDefdocQryService service = getDefDocQryService();
		try {
			String[] defdocs = defdocPks.split(splite);
			DefdocVO[] vos = service.queryDefdocByPk(defdocs);
			ScapLogger.debug("根据自定义项档案列表主键 " + defdocPks
					+ " 生成自定义项档案列表DefdocVO正常。");
			return vos;
		} catch (Exception e) {
			ScapLogger.error("查询异常！根据自定义项档案列表主键 " + defdocPks
					+ " 生成自定义项档案列表DefdocVO异常！", e);
		}
		return null;
	}

	/**
	 * 根据多个自定义项档案列表主键查询名称数组（中文显示名称数组），多个主键用传入的参数分割
	 * 
	 * @author liutaob 2014-7-17
	 */
	public static String[] getDefDocNamesSpliteByPks(String defdocPks,
			String splite) {
		IDefdocQryService service = getDefDocQryService();
		try {
			String[] defdocs = defdocPks.split(splite);
			DefdocVO[] vos = service.queryDefdocByPk(defdocs);
			if(vos==null)return null;
			String[] names = new String[vos.length];
			for (int i = 0; i < vos.length; i++) {
				names[i] = vos[i].getName();
			}
			return names;
		} catch (Exception e) {
			ScapLogger.error("查询异常！根据自定义项档案列表主键 " + defdocPks
					+ " 生成自定义项档案列表DefdocVO异常！", e);
		}
		return null;
	}

	/**
	 * 根据自定义项档案列表主键查询名称（中文显示名称）
	 * 
	 * @author taoye 2013-7-16
	 */
	public static String getDefDocNamesByPks(String pk_defdoc) {
		DefdocVO vo = getDefDocVo(pk_defdoc);
		try {
			String name = vo.getName();
			return name;
		} catch (Exception e) {
			ScapLogger.error("查询异常！根据自定义项档案列表主键 " + pk_defdoc
					+ " 查询自定义项档案列表名称异常！", e);
		}
		return null;
	}

	/**
	 * 根据自定义项档案列表主键数组查询名称数组（中文显示名称数组）
	 * 
	 * @author taoye 2013-7-16
	 */
	public static String[] getDefDocNamesByPks(String[] defdocPks) {
		DefdocVO[] vos = getDefDocVosByPks(defdocPks);
		try {
			String[] names = new String[vos.length];
			for (int i = 0; i < vos.length; i++) {
				names[i] = vos[i].getName();
			}
			return names;
		} catch (Exception e) {
			ScapLogger.error("查询异常！根据自定义项档案列表主键 " + defdocPks
					+ " 查询自定义项档案列表名称异常！", e);
		}
		return null;
	}

	/**
	 * 根据自定义项档案类型主键生成自定义项档案类型VO
	 * 
	 * @author taoye 2013-7-16
	 */
	public static DefdoclistVO getDefdoclistVoByPk(String pk_defdoclist) {
		IDefdoclistQryService service = getDefDocListQryService();
		try {
			DefdoclistVO vo = service.queryDefdoclistVOByPk(pk_defdoclist);
			return vo;
		} catch (Exception e) {
			ScapLogger.error("查询异常！根据自定义项档案类型主键 " + pk_defdoclist
					+ " 查询自定义项档案类型VO异常！", e);
		}
		return null;
	}

	/**
	 * 根据自定义项档案类型名称生成自定义项档案类型VO
	 * 
	 * @author taoye 2013-7-16
	 */
	public static DefdoclistVO getDefdoclistVoByName(String name) {
		IDefdoclistQryService service = getDefDocListQryService();
		try {
			DefdoclistVO vo = service.queryDefdoclistVOsByCondition(
					CpOrgUtil.getGlobalOrgPk(), "name = '" + name + "'")[0];
			return vo;
		} catch (Exception e) {
			ScapLogger.error("查询异常！根据自定义项档案类型名称 " + name + " 查询自定义项档案类型VO异常！",
					e);
		}
		return null;
	}

	/**
	 * 根据自定义项档案类型主键获取自定义项档案类型名称
	 * 
	 * @author taoye 2013-7-16
	 */
	public static String getDefdoclistNameByPk(String pk_defdoclist) {
		DefdoclistVO vo = getDefdoclistVoByPk(pk_defdoclist);
		return vo == null ? null : vo.getName();
	}

	/**
	 * 获取自定义项档案类型接口实例
	 * 
	 * @author taoye 2013-7-16
	 */
	public static IDefdoclistQryService getDefDocListQryService() {
		return NCLocator.getInstance().lookup(IDefdoclistQryService.class);
	}

	/**
	 * 获取自定义项档案列表接口实例
	 * 
	 * @author taoye 2013-7-16
	 */
	public static IDefdocQryService getDefDocQryService() {
		return NCLocator.getInstance().lookup(IDefdocQryService.class);
	}

	/**
	 * 根据自定义项档案编码查询自定义项档案列表
	 * 
	 * @author taoye 2013-7-16
	 */
	public static DefdocVO[] getDefDocVosByCode(String code, String pk_parent) {
		String pk_defdoclist = null;
		String wheresql = " (pid='" + pk_parent + "' or pk_defdoc='"
				+ pk_parent + "')";
		try {
			DefdoclistVO[] vos = getDefDocListQryService()
					.queryDefdoclistVOsByCondition(CpOrgUtil.getGlobalOrgPk(),
							"code = '" + code + "'");
			if (vos.length > 0)
				pk_defdoclist = vos[0].getPk_defdoclist();

			ScapLogger.debug("根据自定义项档案类型编码： " + code + " 查询自定义项档案列表");
		} catch (BusinessException e) {
			ScapLogger
					.error("查询异常！根据自定义项档案类型编码： " + code + " 查询自定义项档案列表异常！", e);
		}
		return getDefDocVosByDefDocListPkAndWherSql(pk_defdoclist, null, null,
				wheresql);
	}
	
	/**
	 * 根据list code 和doc code 查找
	 * @param docListCode
	 * @param docCode
	 * @return
	 */
	public static DefdocVO getDefdocByListCodeAndCode(String docListCode,String docCode){
		DefdocVO defdocvo=null;
		String sql="select * from bd_defdoc bd where BD.PK_DEFDOCLIST =(select B.PK_DEFDOCLIST from bd_defdoclist b where B.CODE='"+docListCode+"') and BD.CODE='"+docCode+"' ";
		defdocvo=(DefdocVO) ScapDAO.executeQuery(sql, new BeanProcessor(DefdocVO.class));
		return defdocvo;
		
	}
}
