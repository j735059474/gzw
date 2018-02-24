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
	 * �����Զ�����������ѯ�Զ�������б�
	 * 
	 * @author taoye 2013-7-16
	 */
	public static String getDefDocNameByDefCodeAndDataCode(String defcode,String datacode) {
		String pk_defdoclist = null;
		String name = null;
		ScapLogger.debug("scap_debug:���ݵ������룺[" + defcode + "]��ѯ������");
		try {
			DefdoclistVO[] vos = getDefDocListQryService()
					.queryDefdoclistVOsByCondition(CpOrgUtil.getGlobalOrgPk(),
							"code = '" + defcode + "'");
			if (vos == null || vos.length == 0) {
				ScapLogger.error("���ݵ������룺[" + defcode + "]��ѯ���������������Ա��ϵ��");
				throw new LfwRuntimeException("���ݵ������룺[" + defcode
						+ "]��ѯ���������������Ա��ϵ��");
			}
			if (vos.length > 0) {
				pk_defdoclist = vos[0].getPk_defdoclist();
				DefdocVO[] defDocVos = (DefdocVO[]) ScapDAO.retrieveByCondition(DefdocVO.class, "pk_defdoclist='"+pk_defdoclist+"' and code='"+datacode+"'");
				if(defDocVos != null && defDocVos.length > 0) {
					ScapLogger.debug("�����Զ�������б������ͱ���"+ pk_defdoclist+" and "+ datacode + " �����Զ�������б�DefdocVO������");
					name = defDocVos[0].getName();
				}
			}
		} catch (BusinessException e) {
			ScapLogger
					.error("��ѯ�쳣�������Զ���������ͱ��룺 " + defcode + " ��ѯ�Զ�������б��쳣��", e);
		}
		return name;
	}

	/**
	 * �����Զ�����������ѯ�Զ�������б�
	 * 
	 * @author taoye 2013-7-16
	 */
	public static DefdocVO[] getDefDocVosByCode(String code) {
		String pk_defdoclist = null;
		ScapLogger.debug("scap_debug:���ݵ������룺[" + code + "]��ѯ������");
		try {
			DefdoclistVO[] vos = getDefDocListQryService()
					.queryDefdoclistVOsByCondition(CpOrgUtil.getGlobalOrgPk(),
							"code = '" + code + "'");
			if (vos == null || vos.length == 0) {
				ScapLogger.error("���ݵ������룺[" + code + "]��ѯ���������������Ա��ϵ��");
				throw new LfwRuntimeException("���ݵ������룺[" + code
						+ "]��ѯ���������������Ա��ϵ��");
			}
			if (vos.length > 0) {
				pk_defdoclist = vos[0].getPk_defdoclist();
				ScapLogger.debug("�����Զ���������ͱ��룺 " + code + " ��ѯ�Զ�������б�");
				return getDefDocVosByDefDocListPk(pk_defdoclist);
			}
		} catch (BusinessException e) {
			ScapLogger
					.error("��ѯ�쳣�������Զ���������ͱ��룺 " + code + " ��ѯ�Զ�������б��쳣��", e);
		}
		return new DefdocVO[0];
	}

	/**
	 * �����Զ�����������ѯ�Զ�������б�
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
			ScapLogger.debug("�����Զ���������ͱ��룺 " + code + " ��ѯ�Զ�������б�");
		} catch (BusinessException e) {
			ScapLogger
					.error("��ѯ�쳣�������Զ���������ͱ��룺 " + code + " ��ѯ�Զ�������б��쳣��", e);
		}
		return vo;
	}

	/**
	 * �����Զ���������Ͳ�ѯ�Զ�������б�
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
			ScapLogger.error("��ѯ�쳣�������Զ�������������� " + name + " ��ѯ�Զ�������б��쳣��", e);
		}
		return null;
	}

	/**
	 * �����Զ���������͡�ҵ��Ԫ����������������ѯ�Զ�������б�����ҵ��Ԫ�����ͼ���������Ϊnull��""
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
	 * �����Զ���������͡�ҵ��Ԫ������������������ѯ������ѯ�Զ�������б�����ҵ��Ԫ���������������Ͳ�ѯ������Ϊnull��""
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
			/*�Զ��嵵����ȡ��ʽ��Ϊֱ�Ӷ����ݿ⣬����NC�ӿ�*/
			DefdocVO[] vos = (DefdocVO[]) ScapDAO.retrieveByCondition(DefdocVO.class, "pk_defdoclist = '" + pk_defdoclist+"'");
//			DefdocVO[] vos = service.queryDefdocVOsByDoclistPkAndWhereSql(
//					pk_defdoclist, pk_org, pk_group, whereSql);
			ScapLogger
					.debug("�����Զ�������������� " + pk_defdoclist + " ��ѯ�Զ�������б�������");
			return vos;
		} catch (Exception e) {
			ScapLogger.error("��ѯ�쳣�������Զ�������������� " + pk_defdoclist
					+ " ��ѯ�Զ�������б��쳣��", e);
		}
		return null;

	}

	/**
	 * �����Զ�������б�������ѯVO
	 * 
	 * @author taoye 2013-7-16
	 */
	public static DefdocVO getDefDocVo(String pk_defdoc) {
		try {
			String[] defdocPks = { pk_defdoc };
			DefdocVO vo = getDefDocVosByPks(defdocPks)[0];
			ScapLogger.debug("�����Զ�������б����� " + pk_defdoc
					+ " �����Զ�������б�DefdocVO������");
			return vo;
		} catch (Exception e) {
			ScapLogger.error("��ѯ�쳣�������Զ�������б����� " + pk_defdoc
					+ " �����Զ�������б�DefdocVO�쳣��", e);
		}
		return null;
	}
	
	/**
	 * �����Զ�������б�������ѯVO
	 * @author taoye 2013-7-16
	 */
	public static DefdocVO getDefDocVoByCode(String code) {
		try {
			String condition = "code = '" + code + "'";
			DefdocVO[] vos = (DefdocVO[]) ScapDAO.retrieveByCondition(DefdocVO.class, condition);
			if(vos != null && vos.length > 0) {
				ScapLogger.debug("�����Զ�������б����� " + code + " �����Զ�������б�DefdocVO������");
				return vos[0];
			}
		}catch(Exception e) {
			ScapLogger.error("��ѯ�쳣�������Զ�������б����� " + code + " �����Զ�������б�DefdocVO�쳣��", e);
		}
		return null;
	}

	/**
	 * ���ݶ���Զ�������б����������ѯ�Զ�������б�VO����
	 * 
	 * @author taoye 2013-7-16
	 */
	public static DefdocVO[] getDefDocVosByPks(String[] defdocPks) {
		IDefdocQryService service = getDefDocQryService();
		try {
			DefdocVO[] vos = service.queryDefdocByPk(defdocPks);
			ScapLogger.debug("�����Զ�������б����� " + defdocPks
					+ " �����Զ�������б�DefdocVO������");
			return vos;
		} catch (Exception e) {
			ScapLogger.error("��ѯ�쳣�������Զ�������б����� " + defdocPks
					+ " �����Զ�������б�DefdocVO�쳣��", e);
		}
		return null;
	}

	/**
	 * ���ݶ���Զ�������б�������ѯ�Զ�������б�VO���飬��������ô���Ĳ����ָ�
	 * 
	 * @author taoye 2013-7-16
	 */
	public static DefdocVO[] getDefDocVosSpliteByPks(String defdocPks,
			String splite) {
		IDefdocQryService service = getDefDocQryService();
		try {
			String[] defdocs = defdocPks.split(splite);
			DefdocVO[] vos = service.queryDefdocByPk(defdocs);
			ScapLogger.debug("�����Զ�������б����� " + defdocPks
					+ " �����Զ�������б�DefdocVO������");
			return vos;
		} catch (Exception e) {
			ScapLogger.error("��ѯ�쳣�������Զ�������б����� " + defdocPks
					+ " �����Զ�������б�DefdocVO�쳣��", e);
		}
		return null;
	}

	/**
	 * ���ݶ���Զ�������б�������ѯ�������飨������ʾ�������飩����������ô���Ĳ����ָ�
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
			ScapLogger.error("��ѯ�쳣�������Զ�������б����� " + defdocPks
					+ " �����Զ�������б�DefdocVO�쳣��", e);
		}
		return null;
	}

	/**
	 * �����Զ�������б�������ѯ���ƣ�������ʾ���ƣ�
	 * 
	 * @author taoye 2013-7-16
	 */
	public static String getDefDocNamesByPks(String pk_defdoc) {
		DefdocVO vo = getDefDocVo(pk_defdoc);
		try {
			String name = vo.getName();
			return name;
		} catch (Exception e) {
			ScapLogger.error("��ѯ�쳣�������Զ�������б����� " + pk_defdoc
					+ " ��ѯ�Զ�������б������쳣��", e);
		}
		return null;
	}

	/**
	 * �����Զ�������б����������ѯ�������飨������ʾ�������飩
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
			ScapLogger.error("��ѯ�쳣�������Զ�������б����� " + defdocPks
					+ " ��ѯ�Զ�������б������쳣��", e);
		}
		return null;
	}

	/**
	 * �����Զ�������������������Զ����������VO
	 * 
	 * @author taoye 2013-7-16
	 */
	public static DefdoclistVO getDefdoclistVoByPk(String pk_defdoclist) {
		IDefdoclistQryService service = getDefDocListQryService();
		try {
			DefdoclistVO vo = service.queryDefdoclistVOByPk(pk_defdoclist);
			return vo;
		} catch (Exception e) {
			ScapLogger.error("��ѯ�쳣�������Զ�������������� " + pk_defdoclist
					+ " ��ѯ�Զ����������VO�쳣��", e);
		}
		return null;
	}

	/**
	 * �����Զ�������������������Զ����������VO
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
			ScapLogger.error("��ѯ�쳣�������Զ�������������� " + name + " ��ѯ�Զ����������VO�쳣��",
					e);
		}
		return null;
	}

	/**
	 * �����Զ����������������ȡ�Զ��������������
	 * 
	 * @author taoye 2013-7-16
	 */
	public static String getDefdoclistNameByPk(String pk_defdoclist) {
		DefdoclistVO vo = getDefdoclistVoByPk(pk_defdoclist);
		return vo == null ? null : vo.getName();
	}

	/**
	 * ��ȡ�Զ���������ͽӿ�ʵ��
	 * 
	 * @author taoye 2013-7-16
	 */
	public static IDefdoclistQryService getDefDocListQryService() {
		return NCLocator.getInstance().lookup(IDefdoclistQryService.class);
	}

	/**
	 * ��ȡ�Զ�������б�ӿ�ʵ��
	 * 
	 * @author taoye 2013-7-16
	 */
	public static IDefdocQryService getDefDocQryService() {
		return NCLocator.getInstance().lookup(IDefdocQryService.class);
	}

	/**
	 * �����Զ�����������ѯ�Զ�������б�
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

			ScapLogger.debug("�����Զ���������ͱ��룺 " + code + " ��ѯ�Զ�������б�");
		} catch (BusinessException e) {
			ScapLogger
					.error("��ѯ�쳣�������Զ���������ͱ��룺 " + code + " ��ѯ�Զ�������б��쳣��", e);
		}
		return getDefDocVosByDefDocListPkAndWherSql(pk_defdoclist, null, null,
				wheresql);
	}
	
	/**
	 * ����list code ��doc code ����
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
