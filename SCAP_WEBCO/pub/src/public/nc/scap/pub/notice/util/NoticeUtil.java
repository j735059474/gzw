package nc.scap.pub.notice.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.message.util.MessageCenter;
import nc.message.vo.MessageVO;
import nc.message.vo.NCMessage;
import nc.scap.def.util.ScapDefUtil;
import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.model.ScapCoConstants;
import nc.scap.pub.sms.SmsManageService;
import nc.scap.pub.sms.itf.ISmsExtentionService;
import nc.scap.pub.sms.tools.ScapSmsExtentionUtil;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.ScapDAO;
import nc.scap.pub.vos.ScapSmsStatusEnum;
import nc.scap.pub.vos.ScapSmsTaskVO;
import nc.uap.cpb.org.orgs.CpOrgVO;
import nc.uap.cpb.persist.dao.PtBaseDAO;
import nc.uap.lfw.core.AppInteractionUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.constants.AppConsts;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.log.ScapLogger;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.scap.work_report_type.Work_report_type;
import nc.vo.scapco.pub_visualorganization.visualOrganization;
import nc.vo.scapco.pub_visualorganization.visualUserInfo;
import nc.vo.scapco.work_notice_contacts.notice_contact_info;
import nc.vo.scapco.work_notice_contacts.notice_contact_type;
import nc.vo.scapco.work_notice_manager.Notice_manager;
import nc.vo.scapco.work_notice_manager.Receive_man;
import nc.vo.scapco.work_notice_manager.Receive_notice;
import nc.vo.scapco.work_report_type.receive_org;
import nc.vo.scapco.work_urge_notice.urge_notice;
import nc.vo.scapjj.dsm.Datetype_HVO;
import nc.vo.scapjj.userpsn.V_userpsnVO;

import org.apache.commons.lang.StringUtils;

import uap.web.bd.pub.AppUtil;

/**
 * 组装SQL语句
 * 2014-10-16
 * @author luqzh
 *
 */
public class NoticeUtil {
	/**
	 * 根据当前登陆用户查询常用联系人表，确定该用户可以操作的业务类型
	 * 例如
	 * fields[0] like '%value%' oper fields[1] like '%value%'
	 * @param fields
	 * @param value
	 * @param oper and / or
	 * @return
	 */
	public static String getBusinessTypeByUser(String field,String value){
		String result = "";
		return result;
	}
	/**
	 * 取得资料类型相应的name
	 */
	public static String getNameByDataType(String data_type){
		Datetype_HVO datetype_HVO = (Datetype_HVO) ScapDAO
				.retrieveByPK(Datetype_HVO.class, data_type);
		String name = datetype_HVO.getName();
		return name;
	}
	/**
	 * 取得报告类型相应的报告主体
	 */
	public static String getReportBodyByNoticeType(String report_type){
		Work_report_type workReportType = (Work_report_type) ScapDAO
				.retrieveByPK(Work_report_type.class, report_type);
		String report_body = workReportType.getReport_body();
		return report_body;
	}
	/**
	 * 取得通知主键取得相应的报告主体(必须是通知主表保存的情况下才能用这个函数)
	 */
	public static String getReportBodyByNoticePk(String notice_pk){
		Notice_manager notice_manager = (Notice_manager) ScapDAO
				.retrieveByPK(Notice_manager.class, notice_pk);
		String reportBody = "";
		String notice_style = notice_manager.getNotice_style();
		// 如果是填报通知，那个根据报告类型取得报告主体
		if (notice_style.equals("1")) {
			String notice_type = notice_manager.getNotice_type();
			Work_report_type WorkReportType = (Work_report_type) ScapDAO
					.retrieveByPK(Work_report_type.class, notice_type);
			reportBody = WorkReportType.getReport_body();
		} else if (notice_style.equals("2")) {
			// 如果是资料消息通知，则默认报告主体是个人
			reportBody = IGlobalConstants.REPORT_BODY_MAN;
		} else if (notice_style.equals("3")) {
			// 如果是普通消息通知，则默认报告主体是个人
			reportBody = IGlobalConstants.REPORT_BODY_MAN;
		}
		return reportBody;


	}
	/**
	 * 取得报告类型相应的名称
	 */
	public static String getNameByNoticeType(String notice_type){
		Work_report_type WorkReportType = (Work_report_type) ScapDAO
				.retrieveByPK(Work_report_type.class, notice_type);
		String report_type = WorkReportType.getReport_type();
		return report_type;
	}
	/**
	 * 取得根据pk_org或pk_visualorg取得企业名字或组织名字
	 */
	public static String getOrgNameByPK(String pk){
		CpOrgVO orgVo = (CpOrgVO) ScapDAO
				.retrieveByPK(CpOrgVO.class, pk);
		if ( orgVo != null ) {
			return orgVo.getName();
		}
		visualOrganization visOrgVo = (visualOrganization)ScapDAO
				.retrieveByPK(visualOrganization.class, pk);
		if ( visOrgVo != null ) {
			return visOrgVo.getVisualorg_name();
		}
		return "";
	}
	/**
	 * 取得业务类型相应的名称
	 */
	public static String getNameByBusinessType(String business_type){
		String name = "";
        String defCode = "scapco_0003";
        DefdocVO[] vos = ScapDefUtil.getDefDocVosByCode(defCode);
        if (vos != null && vos.length != 0) {
            String pk_defdoc = null;
            for (int i = 0; i < vos.length; i++) {
            	pk_defdoc = vos[i].getPk_defdoc();
                if (pk_defdoc != null && pk_defdoc.equals(business_type)) {
                    // 取得定义值
                	name = vos[i].getName();
                }
            }
        }
		return name;
	}
	/**
	 * 取得业务类型相应的code
	 */
	public static String getCodeByBusinessType(String business_type){
		String code = "";
        String defCode = "scapco_0003";
        DefdocVO[] vos = ScapDefUtil.getDefDocVosByCode(defCode);
        if (vos != null && vos.length != 0) {
            String pk_defdoc = null;
            for (int i = 0; i < vos.length; i++) {
            	pk_defdoc = vos[i].getPk_defdoc();
                if (pk_defdoc != null && pk_defdoc.equals(business_type)) {
                    // 取得定义值
                	code = vos[i].getCode();
                }
            }
        }
		return code;
	}
	/**
	 * 取得相应消息类型的名字
	 */
	public static String getMessageTypeNameByCode(String code){
		String name = "";
        String defCode = "scapco_message_type";
        DefdocVO[] vos = ScapDefUtil.getDefDocVosByCode(defCode);
        if (vos != null && vos.length != 0) {
            String codei = null;
            for (int i = 0; i < vos.length; i++) {
            	codei = vos[i].getCode();
                if (codei != null && codei.equals(code)) {
                    // 取得定义值
                	name = vos[i].getName();
                }
            }
        }
		return name;
	}
	/**
	 * 取得所有消息类型
	 */
	public static DefdocVO[] getMessageTypeAll(){
		String name = "";
        String defCode = "scapco_message_type";
        DefdocVO[] vos = ScapDefUtil.getDefDocVosByCode(defCode);
//        if (vos != null && vos.length != 0) {
//            String codei = null;
//            for (int i = 0; i < vos.length; i++) {
//            	codei = vos[i].getCode();
//                if (codei != null && codei.equals(code)) {
//                    // 取得定义值
//                	name = vos[i].getName();
//                }
//            }
//        }
		return vos;
	}
	/**
	 * 根据用户主键取得性别
	 */
	public static String getSexByPkUser(String pk_user){
		String sex = "";
        if (pk_user != null) {
        	V_userpsnVO v_userpsnVO = new V_userpsnVO();
    		StringBuilder sql = new StringBuilder();
    		sql.append(" select * from v_userpsn t where t.cuserid = '")
    		.append(pk_user)
    		.append("'");
    		v_userpsnVO = (V_userpsnVO) ScapDAO.executeQuery(sql.toString(),
					new BeanProcessor(V_userpsnVO.class));
    		// 应该能唯一确定一条数据
			if(v_userpsnVO != null) {
				sex = v_userpsnVO.getSex();
			}
        }
		return sex;
	}
	/**
	 * 根据用户主键取得pk_org
	 */
	public static String getPkOrgByPkUser(String pk_user){
		String pk_org = "";
        if (pk_user != null) {
        	V_userpsnVO v_userpsnVO = new V_userpsnVO();
    		StringBuilder sql = new StringBuilder();
    		sql.append(" select * from v_userpsn t where t.cuserid = '")
    		.append(pk_user)
    		.append("'");
    		v_userpsnVO = (V_userpsnVO) ScapDAO.executeQuery(sql.toString(),
					new BeanProcessor(V_userpsnVO.class));
    		// 应该能唯一确定一条数据
			if(v_userpsnVO != null) {
				pk_org = v_userpsnVO.getPk_org();
			}
        }
		return pk_org;
	}
	/**
	 * 根据用户主键取得姓名
	 */
	public static String getNameByPkUser(String pk_user){
		String name = "";
        if (pk_user != null) {
        	V_userpsnVO v_userpsnVO = new V_userpsnVO();
    		StringBuilder sql = new StringBuilder();
    		sql.append(" select * from v_userpsn t where t.cuserid = '")
    		.append(pk_user)
    		.append("'");
    		v_userpsnVO = (V_userpsnVO) ScapDAO.executeQuery(sql.toString(),
					new BeanProcessor(V_userpsnVO.class));
    		// 应该能唯一确定一条数据
			if(v_userpsnVO != null) {
				name = v_userpsnVO.getUser_name();
			}
        }
		return name;
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
		StringBuilder sb=new StringBuilder(" ");
		if(values.length > 0) {
			sb.append(field).append(" in (");
			for (String value : values) {
				String pk = getBusinessTypePKByCode(value);
				if (pk != null) {
					sb.append("'").append(pk).append("',");
				}
			}
			sb.setLength(sb.length()-1);
			sb.append(") ");
		}
		return sb.toString();
		
	}
	/**
	 * 根据业务类型和报告类型中的code检索出相应的pk（现在一个业务类型下的报表编码需要不同；不同业务类型下的业务编码，可以一样）
	 * @return
	 */
	public static String getNoticeTypePKByBusinessTypeAndCode(String business_type,String codeFromView){
        String noticeTypePK = null;
        if (business_type != null && codeFromView != null) {
//        	if (!codeFromView.startsWith("0") && Integer.valueOf(codeFromView) < 10){
//        		codeFromView = "0" + codeFromView;
//        	}
    		Work_report_type work_report_type = new Work_report_type();
    		StringBuilder sql = new StringBuilder();
    		sql.append(" select * from scapco_work_reporttype t where t.YE_TYPE = '")
    		.append(business_type)
    		.append("'")
    		.append(" and t.REPORT_CODE = '")
    		.append(codeFromView)
    		.append("'");
			work_report_type = (Work_report_type) ScapDAO.executeQuery(sql.toString(),
					new BeanProcessor(Work_report_type.class));
    		// 应该能唯一确定一条数据
			if(work_report_type != null) {
				noticeTypePK = work_report_type.getPk_work_report_type();
			}
        }
        return noticeTypePK;
	}
	/**
	 * 根据业务类型和报告类型中的code检索出相应的报告是否是关联报告
	 * @return
	 */
	public static String isAssNoti(String business_type,String codeFromView){
        String is_ass_noti = null;
        if (business_type != null && codeFromView != null) {
        	if (!codeFromView.startsWith("0") && Integer.valueOf(codeFromView) < 10){
        		codeFromView = "0" + codeFromView;
        	}
    		Work_report_type  work_report_type = new Work_report_type();
    		StringBuilder sql = new StringBuilder();
    		sql.append(" select * from scapco_work_reporttype t where t.YE_TYPE = '")
    		.append(business_type)
    		.append("'")
    		.append(" and t.REPORT_CODE = '")
    		.append(codeFromView)
    		.append("'");
			work_report_type = (Work_report_type) ScapDAO.executeQuery(sql.toString(),
					new BeanProcessor(Work_report_type.class));
    		// 应该能唯一确定一条数据
			if(work_report_type != null) {
				is_ass_noti = work_report_type.getIs_ass_noti();
			}
        }
        return is_ass_noti;
	}
	/**
	 * 根据业务类型和报告类型中的code检索出相应的报告是否是关联报告
	 * @return
	 */
	public static String isAssNotiByReportType(String business_type,String report_type){
        String is_ass_noti = null;
        if (business_type != null && report_type != null) {
    		Work_report_type  work_report_type = new Work_report_type();
    		StringBuilder sql = new StringBuilder();
    		sql.append(" select * from scapco_work_reporttype t where t.YE_TYPE = '")
    		.append(business_type)
    		.append("'")
    		.append(" and t.pk_work_report_type = '")
    		.append(report_type)
    		.append("'");
			work_report_type = (Work_report_type) ScapDAO.executeQuery(sql.toString(),
					new BeanProcessor(Work_report_type.class));
    		// 应该能唯一确定一条数据
			if(work_report_type != null) {
				is_ass_noti = work_report_type.getIs_ass_noti();
			}
        }
        return is_ass_noti;
	}
//	/**
//	 * 根据报告类型中的code检索出相应的报告主体
//	 * 例如
//	 * code = '01' 
//	 * @param fields
//	 * @param value
//	 * @param oper and / or
//	 * @return
//	 */
//	public static String getReportBodyPKByCode(String business_type,String codeFromView){
//        String reportBody = null;
//        if (codeFromView != null) {
//        	if (!codeFromView.startsWith("0") && Integer.valueOf(codeFromView) < 10){
//        		codeFromView = "0" + codeFromView;
//        	}
//    		Work_report_type work_report_type = new Work_report_type();
//    		StringBuilder sql = new StringBuilder();
//    		sql.append(" select * from scapco_work_reporttype t where t.YE_TYPE = '")
//    		.append(business_type)
//    		.append("'")
//    		.append(" and t.REPORT_CODE = '")
//    		.append(codeFromView)
//    		.append("'");
//			work_report_type = (Work_report_type) ScapDAO.executeQuery(sql.toString(),
//					new BeanProcessor(Work_report_type.class));
//			if(work_report_type != null) {
//				reportBody = work_report_type.getReport_body();
//			}
//        }
//        return reportBody;
//	}
	/**
	 * 根据资料类型的code检索出资料类型的pk
	 * 例如
	 * code = '01' 
	 * @param fields
	 * @param value
	 * @param oper and / or
	 * @return
	 */
	public static String getDataTypePKByCode(String dataTypeCode){
        String pk_datetype_h = null;
        if (dataTypeCode != null) {
        	Datetype_HVO datetype_HVO = new Datetype_HVO();
    		StringBuilder sql = new StringBuilder();
    		sql.append(" select * from scapjj_datetype_h t where t.code = '")
    		.append(dataTypeCode)
    		.append("'");
    		datetype_HVO = (Datetype_HVO) ScapDAO.executeQuery(sql.toString(),
					new BeanProcessor(Datetype_HVO.class));
			if(datetype_HVO != null) {
				pk_datetype_h = datetype_HVO.getPk_datetype_h();
			}
        }
        return pk_datetype_h;
	}
	/**
	 * 根据业务类型和报告类型中的code检索出相应的报告是否是关联报告
	 * @return
	 */
	public static String getBusinessTypePKByCode(){
		// 取得功能节点注册时传入的业务类型编码
        String business_type = (String)AppUtil.getAppAttr(IGlobalConstants.APPATTR_BUSINESS_TYPE_FUNCNODE);
       String pk_defdoc = null;
       if (business_type != null) {
           String defCode = "scapco_0003";
           DefdocVO[] vos = ScapDefUtil.getDefDocVosByCode(defCode);
           if (vos != null && vos.length != 0) {
               /*
                * vos[i].getName(); vos[i].getPk_defdoc();
                * vos[i].getCode();
                */
               String itemValue = null;
               String code = null;
               for (int i = 0; i < vos.length; i++) {
                   code = vos[i].getCode();
                   if (code != null && code.equals(business_type)) {
                       // 取得定义值
                       pk_defdoc = vos[i].getPk_defdoc();
                   }
               }
           }
       }

       if (business_type == null || "".equals(business_type)) {
           throw new LfwRuntimeException("功能节点未设置 \""
                   + ScapCoConstants.BUSINESS_TYPE + "\"参数!");
       } else if (business_type != null && pk_defdoc == null) {
           throw new LfwRuntimeException("功能节点设置的 \""
                   + ScapCoConstants.BUSINESS_TYPE + "\"参数的值未定义!");
       }

       AppUtil.addAppAttr(ScapCoConstants.BUSINESS_TYPE, pk_defdoc);
       return pk_defdoc;
	}
	/**
	 * 根据报告类型中的主键检索出相应的接收企业
	 */
	public static List<String> getOrgsPKByPkWorkReportType(String pk_work_report_type){
	    List<String> result = new ArrayList<String>();
        if (pk_work_report_type != null) {
    		List<receive_org> list = new ArrayList<receive_org>();
    		StringBuilder sql = new StringBuilder();
    		sql.append(" select * from scapco_receive_org_type t where t.pk_work_report_type = '");
    		sql.append(pk_work_report_type);
    		sql.append("'");
    		try {
    			list = (List<receive_org>) new BaseDAO().executeQuery(sql.toString(),
    					new BeanListProcessor(receive_org.class));
    		} catch (DAOException e) {
    			LfwLogger.error("检索报告主体时出错!");
    		}
    		for (receive_org vo : list) {
    			if (StringUtils.isNotEmpty(vo.getNotice_org()) && (!"~".equals(vo.getNotice_org()))) {
        			result.add(vo.getNotice_org());
    			}
    		}
        }
        return result;
	}
	/**
	 * 根据报告类型中的主键检索出相应的接收组织
	 */
	public static List<String> getVisualOrgsPKByPkWorkReportType(String pk_work_report_type){
	    List<String> result = new ArrayList<String>();
        if (pk_work_report_type != null) {
    		List<receive_org> list = new ArrayList<receive_org>();
    		StringBuilder sql = new StringBuilder();
    		sql.append(" select * from scapco_receive_org_type t where t.pk_work_report_type = '");
    		sql.append(pk_work_report_type);
    		sql.append("'");
    		try {
    			list = (List<receive_org>) new BaseDAO().executeQuery(sql.toString(),
    					new BeanListProcessor(receive_org.class));
    		} catch (DAOException e) {
    			LfwLogger.error("检索报告主体时出错!");
    		}
    		for (receive_org vo : list) {
    			if (StringUtils.isNotEmpty(vo.getNotice_visual_org()) && (!"~".equals(vo.getNotice_visual_org()))) {
        			result.add(vo.getNotice_visual_org());
    			}
    		}
        }
        return result;
	}
	/**
	 * 根据业务类型的自定义档案中的code检索出相应的pk
	 * 例如
	 * code = '01' 
	 * @param fields
	 * @param value
	 * @param oper and / or
	 * @return
	 */
	public static String getBusinessTypePKByCode(String codeFromView){
        String pk_defdoc = null;
        if (codeFromView != null) {
//        	if (!codeFromView.startsWith("0") && Integer.valueOf(codeFromView) < 10){
//        		codeFromView = "0" + codeFromView;
//        	}
            String defCode = "scapco_0003";
            DefdocVO[] vos = ScapDefUtil.getDefDocVosByCode(defCode);
            if (vos != null && vos.length != 0) {
                String code = null;
                for (int i = 0; i < vos.length; i++) {
                    code = vos[i].getCode();
                    if (code != null && code.equals(codeFromView)) {
                        // 取得定义值
                        pk_defdoc = vos[i].getPk_defdoc();
                    }
                }
            }
        }
        return pk_defdoc;
	}
	/**
	 * 根据当前用户的企业pk_org 检索出所有其子企业的pk_org（包括当前企业的pk_org）
	 * 例如
	 * code = '01' 
	 * @param fields
	 * @param value
	 * @param oper and / or
	 * @return
	 */
	public static List<String> getAllChildPk_OrgByCurrentPk(String pk_org){
        List<String> result = new ArrayList<String>();
		List<CpOrgVO> list = new ArrayList<CpOrgVO>();
		StringBuilder sql = new StringBuilder();
		sql.append(" select t.pk_org from cp_orgs t connect by t.pk_fatherorg = prior t.pk_org start with ");
		sql.append(" t.pk_org = '");
		sql.append(pk_org);
		sql.append("'");
		try {
			list = (List<CpOrgVO>) new BaseDAO().executeQuery(sql.toString(),
					new BeanListProcessor(CpOrgVO.class));
		} catch (DAOException e) {
			LfwLogger.error("检索子企业时出错!");
		}
		for (CpOrgVO vo : list) {
			result.add(vo.getPk_org());
		}
        return result;
	}
	/**
	 * 根据当前用户的组织pk_visualorg  检索出所有其子组织的pk_visualorg（包括当前组织的pk_visualorg）
	 * 例如
	 * code = '01' 
	 * @param fields
	 * @param value
	 * @param oper and / or
	 * @return
	 */
	public static List<String> getAllVisualChildPk_OrgByCurrentPk(String pk_visualorg){
		List<String> result = new ArrayList<String>();
		List<visualOrganization> list = new ArrayList<visualOrganization>();
		StringBuilder sql = new StringBuilder();
		sql.append(" select t.pk_visualorg from scapco_visualOrganization t connect by t.pk_parent = prior t.pk_visualorg start with ");
		sql.append(" t.pk_visualorg = '");
		sql.append(pk_visualorg);
		sql.append("'");
		try {
			list = (List<visualOrganization>) new BaseDAO().executeQuery(sql.toString(),
					new BeanListProcessor(visualOrganization.class));
		} catch (DAOException e) {
			LfwLogger.error("检索子组织时出错!");
		}
		for (visualOrganization vo : list) {
			result.add(vo.getPk_visualorg());
		}
        return result;
	}
	/**
	 * 根据当前登陆用户的pk_user 检索出所属的所有组织（虚拟组织）
	 * 例如
	 * code = '01' 
	 * @param fields
	 * @param value
	 * @param oper and / or
	 * @return
	 */
	public static List<String> getAllPKVisualorgByPkUser(String pkUser){
		List<String> result = new ArrayList<String>();
		List<visualUserInfo> list = new ArrayList<visualUserInfo>();
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from scapco_visualUserInfo t where  ");
		sql.append(" t.pk_user = '");
		sql.append(pkUser);
		sql.append("'");
		list = (List<visualUserInfo>) ScapDAO.executeQuery(sql.toString(),
				new BeanListProcessor(visualUserInfo.class));
		for (visualUserInfo vo : list) {
			result.add(vo.getPk_visualorg());
		}
        return result;
	}
	/**
	 * 根据当前登陆用户的pk_user 检索出所属的所有组织及其子组织（虚拟组织）(注:没有去重处理)
	 */
	public static List<String> getAllChildPKVisualorgByPkUser(String pkUser){

		List<String> visualOrganizationList = new ArrayList<String>();
		
		List<String> pkList = getAllPKVisualorgByPkUser(pkUser);
		for (String pk_visualorg : pkList) {
			List<String> listtemp = getAllVisualChildPk_OrgByCurrentPk(pk_visualorg);
			visualOrganizationList.addAll(listtemp);
		}
		return visualOrganizationList;
	}
	/**
	 *  把超过1000的in条件集合拆分成数量splitNum的多组sql的in 集合。
	  *
	  * @param sqhList
	  *            in条件的List
	  * @param splitNum
	  *            拆分的间隔数目,例如： 1000
	  * @param columnName
	  *            SQL中引用的字段名例如： Z.SHENQINGH
	 */  
	 public static String getSqlStrByList(List<String> sqhList, int splitNum,String columnName) {
	  if (splitNum > 1000) // 因为数据库的列表sql限制，不能超过1000.
	   return null;
	  StringBuffer sql = new StringBuffer("");
	  if (sqhList != null) {
	   sql.append(" ").append(columnName).append(" IN ( ");
	   for (int i = 0; i < sqhList.size(); i++) {
	    sql.append("'").append(sqhList.get(i) + "',");
	    if ((i + 1) % splitNum == 0 && (i + 1) < sqhList.size()) {
	     sql.deleteCharAt(sql.length() - 1);
	     sql.append(" ) OR ").append(columnName).append(" IN (");
	    }
	   }
	   sql.deleteCharAt(sql.length() - 1);
	   sql.append(" )");
	  }
	  return sql.toString();
	 }
	/**
	 * 根据当前用户的pk_user和pk_org去检索常用联系人那两张表，取出当前用户可以维护的业务类型和报告类型的sql句
	 * 例如
	 * code = '01' 
	 * @param fields
	 * @param value
	 * @param oper and / or
	 * @return
	 */
	public static String getContactTypeSqlByCurrentUser(String pk_user,String pk_org){
		List<notice_contact_type> list = getContactTypeByCurrentUser(pk_user,pk_org);
		StringBuilder whereSql = new StringBuilder();
		StringBuilder yeTypeSql = new StringBuilder();
		StringBuilder reportTypeSql = new StringBuilder();
		for(notice_contact_type vo : list) {
			yeTypeSql.append("'").append(vo.getYe_type()).append("'").append(",");
			reportTypeSql.append("'").append(vo.getReport_type()).append("'").append(",");
		}
        return whereSql.toString();
	}
	/**
	 * 根据当前用户的pk_user和pk_org去检索常用联系人那两张表，取出当前用户可以维护的业务类型和报告类型
	 * 例如
	 * code = '01' 
	 * @param fields
	 * @param value
	 * @param oper and / or
	 * @return
	 */
	public static List<notice_contact_type> getContactTypeByCurrentUser(String pk_user,String pk_org){
		List<notice_contact_type> list = new ArrayList<notice_contact_type>();
		StringBuilder sql = new StringBuilder();
		sql.append(" select t.* from scapco_notice_contact_type t ")
		.append(" inner join scapco_notice_contact_info p ")
		.append(" on t.pk_contact_type = p.pk_contact_type ")
		.append(" where ")
		.append(" p.pk_org = '")
		.append(pk_org)
		.append("'")
		.append(" and p.pk_user = '")
		.append(pk_user)
		.append("'")
		.append(" and t.dr = '0'  and t.enablestate = '2' ");
		try {
			list = (List<notice_contact_type>) new BaseDAO().executeQuery(sql.toString(),
					new BeanListProcessor(notice_contact_type.class));
		} catch (DAOException e) {
			LfwLogger.error("检索当前用户的联系人类型时出错!");
		}
        return list;
	}
	/**
	 * 根据当前用户所有联系人信息
	 */
	public static List<notice_contact_info> getContactInfoByCurrentUser(String pk_user){
		List<notice_contact_info> list = new ArrayList<notice_contact_info>();
		StringBuilder sql = new StringBuilder();
		sql.append(" select p.* from scapco_notice_contact_info p ")
		.append(" where ")
		.append(" p.pk_user = '")
		.append(pk_user)
		.append("'");
		try {
			list = (List<notice_contact_info>) new BaseDAO().executeQuery(sql.toString(),
					new BeanListProcessor(notice_contact_info.class));
		} catch (DAOException e) {
			LfwLogger.error("检索当前用户的联系人信息时出错!");
		}
        return list;
	}
	/**
	 * 取得某个（业务类型，报告类型）或资料类型或消息类型的所有联系人
	 */
	public static List<notice_contact_info> getContactInfoByParam(Map<String, String> paramMap) {
		List<notice_contact_info> list = new ArrayList<notice_contact_info>();
  	  String besinss_type = paramMap.get(IGlobalConstants.PARAMMAP_KEY_BUSINESS_TYPE);
  	  String report_type = paramMap.get(IGlobalConstants.PARAMMAP_KEY_REPORT_TYPE);
  	  String data_type = paramMap.get(IGlobalConstants.PARAMMAP_KEY_DATA_TYPE);
      String message_type = paramMap.get(IGlobalConstants.PARAMMAP_KEY_MESSAGE_TYPE);
      String sqlAppend = "";
      if (StringUtils.isNotBlank(besinss_type) && StringUtils.isNotBlank(report_type)) {
    	  sqlAppend = " ye_type = '"+ besinss_type +"' and report_type = '" + report_type + "' ";
      } else if (StringUtils.isNotBlank(data_type)) {
    	  sqlAppend = " data_type = '"+ data_type +"' ";
      } else if (StringUtils.isNotBlank(message_type)) {
    	  sqlAppend = " vdef1 = '"+ message_type +"' ";
      } else {
    	  return list;
      }

		StringBuilder sql = new StringBuilder();
		sql.append(" select p.* from scapco_notice_contact_type t inner join scapco_notice_contact_info p ")
		.append(" on t.pk_contact_type = p.pk_contact_type ")
		.append(" where ")
		.append( sqlAppend );
		try {
			list = (List<notice_contact_info>) new BaseDAO().executeQuery(sql.toString(),
					new BeanListProcessor(notice_contact_info.class));
		} catch (DAOException e) {
			LfwLogger.error("检索联系人信息时出错!");
		}
        return list;
	}
	/**
	 * 消息类型是否在联系人中维护了人
	 */
	public static Boolean ifExitedMessageType(String message_type) {
		List<notice_contact_info> list = new ArrayList<notice_contact_info>();
		StringBuilder sql = new StringBuilder();
		sql.append(" select t.* from scapco_notice_contact_type t")
		.append(" where ")
		.append( " vdef1 = '"+ message_type +"' " );
		try {
			list = (List<notice_contact_info>) new BaseDAO().executeQuery(sql.toString(),
					new BeanListProcessor(notice_contact_info.class));
		} catch (DAOException e) {
			LfwLogger.error("检索是否存在消息类型时出错!");
		}
		Boolean result = false;
		if (list != null && list.size() > 0) {
			result = true;
		}
        return result;
	}
	/**
	 * 判断当前用户是否为发送方(2)
	 * @return
	 */
	public static Boolean isSender(List<notice_contact_info> list){
		Boolean result = false;
		for(notice_contact_info info : list) {
			if (info.getContacts_type() != null && "2".equals(info.getContacts_type())) {
				result = true;
			}
		}
		return result;
	}
	/**
	 * 判断当前用户是否为接收方(1)
	 * @return
	 */
	public static Boolean isReceiver(List<notice_contact_info> list){
		Boolean result = false;
		for(notice_contact_info info : list) {
			if (info.getContacts_type() != null && "1".equals(info.getContacts_type())) {
				result = true;
			}
		}
		return result;
	}
	
	  /** 
	 * 通过报告类型获取联系人
	 * @param besinss_type报告业务类型
	 * @param notice_type报告实例类型
	 * @param pk_orgArray接收企业数组
	 * @param refer 是否关联通知接收人子表（
	 * 因为这个函数在两种情况下调用，
	 * 在（人员自动带出时）和（自动下发通知部分）都是检索联系人子表，然后往通知接收人子表插入数据）
	 * 这个时候 不用关联，refer = false；
	 * 在remind提醒时，因为通知人接受子表已经有相应的接收人了，大部分情况不会是联系人子表里面的人了。
	 * 这个时候 关联,refer = true;
	 */
	  public static List<notice_contact_info> getContactsList(Map<String, String> paramMap, String[] pk_orgArray, Boolean refer){
    	  String besinss_type = paramMap.get(IGlobalConstants.PARAMMAP_KEY_BUSINESS_TYPE);
    	  String report_type = paramMap.get(IGlobalConstants.PARAMMAP_KEY_REPORT_TYPE);
    	  String data_type = paramMap.get(IGlobalConstants.PARAMMAP_KEY_DATA_TYPE);
       	  String message_type = paramMap.get(IGlobalConstants.PARAMMAP_KEY_MESSAGE_TYPE);
    	  String reportBody = paramMap.get(IGlobalConstants.PARAMMAP_KEY_REPORTBODY);	
       	  String pkNotice = paramMap.get(IGlobalConstants.PARAMMAP_KEY_PK_NOTICE);	
		  // 遍历所有企业的联络人
			StringBuffer orgs = new StringBuffer();
			String sql = " select b.* from scapco_notice_contact_type a inner join scapco_notice_contact_info b "
					+ " on a.pk_contact_type = b.pk_contact_type ";
			if (Boolean.TRUE.equals(refer)) {
				sql =  sql + " inner join scapco_receive_man c "
				+ " on b.pk_user = c.receive_man ";
			}
			if (StringUtils.isNotEmpty(besinss_type) && StringUtils.isNotEmpty(report_type)) {
  				sql =  sql + " where  a.ye_type = '"
    					+ besinss_type
    					+ "' "
    					+ " and a.report_type ='"
    					+ report_type
    					+ "' ";
			}
			if (StringUtils.isNotEmpty(data_type)) {
				sql =  sql + " where  a.data_type ='"
    	      			+ data_type
    	      			+ "' ";
			}
			if (StringUtils.isNotEmpty(message_type)) {
				sql =  sql + " where  a.vdef1 ='"
    	      			+ message_type
    	      			+ "' ";
			}
			if (Boolean.TRUE.equals(refer)) {
				sql =  sql + " and c.pk_notice = '" + pkNotice + "' ";
			}
			sql =  sql + " and b.dr = '0'    and a.enablestate = '2' ";
			List<notice_contact_info> listAll = new ArrayList<notice_contact_info>();
			StringBuffer errorMessage = new StringBuffer();
			Boolean errorFlag = false;
			for (int i = 0; i < pk_orgArray.length; i++) {
				List<notice_contact_info> listTemp = new ArrayList<notice_contact_info>();
				String pk_org = pk_orgArray[i];
				try {
					String conditionAppend = ""; 
					if (reportBody != null) {
						if ("1".equals(reportBody)) {
							conditionAppend =  sql + " and b.pk_org ='" + pk_org + "' ";
						}
						if ("2".equals(reportBody)) {
							conditionAppend =  sql + " and b.PK_VISUALORG ='" + pk_org + "' ";
						}
						if ("3".equals(reportBody)) {
							conditionAppend =  sql + " and (b.pk_org ='" + pk_org + "' or  b.PK_VISUALORG ='" + pk_org + "' ) ";
						}
    				} else {
    					conditionAppend = sql + " and (b.pk_org ='" + pk_org + "' or  b.PK_VISUALORG ='" + pk_org + "' ) ";
    				}
					listTemp = (List<notice_contact_info>) ScapDAO
							.getBaseDAO()
							.executeQuery(
									conditionAppend,
									new BeanListProcessor(notice_contact_info.class));
				} catch (DAOException e) {
					e.printStackTrace();
				}
				if (listTemp != null && listTemp.size() == 0) {
					errorFlag = true;
					errorMessage.append(NoticeUtil.getOrgNameByPK(pk_org))
							.append(",");
				} else {
					listAll.addAll(listTemp);
				}
			}

			if (errorFlag.equals(true)) {
				// 将errormessage用appattr传回"通知下发方法"(onNoticeIssueClick)，
				// 在其中根据message有无内容进行判断,是否将通知的状态设置为已下发；
				// 这么做的原因是不能在接收人子表插入数据时抛message
				// 举例：有三个企业，a，b，c。其中只有c没有联系人。那么此时应该是
				// 接受企业子表中插入三条数据，abc都插入；
				// 而接收人子表中直插入ab两个企业的联系人，c企业没有联系人，弹出message提示用户
				// message 的抛出 不应该影响到其他两个企业的接收人子表的插入；
				String messsageOld = (String)AppUtil.getAppAttr(IGlobalConstants.NOTICE_ERROR_MESSAGE);
				if (messsageOld != null && StringUtils.isNotEmpty(messsageOld)) {
					errorMessage.append(messsageOld);
				}
				AppUtil.addAppAttr(IGlobalConstants.NOTICE_ERROR_MESSAGE, errorMessage.toString());
			}
			return listAll;
	  }
	  
	    /** 
	     * 通过报告类型获取联系人姓名和电话号码
	     * @param paramMap : besinss_type报告业务类型 ;notice_type报告实例类型
	     * @param pk_orgArray接收企业数组
	     * @param refer 是否关联通知接收人子表（
	     * 因为这个函数在两种情况下调用，
	     * 在（人员自动带出时）和（自动下发通知部分）都是检索联系人子表，然后往通知接收人子表插入数据）
	     * 这个时候 不用关联，refer = false；
	     * 在remind提醒时，因为通知人接受子表已经有相应的接收人了，大部分情况不会是联系人子表里面的人了。
	     * 这个时候 关联,refer = true;
	   */
	      public static Map<String,String> getContactsMap(  Map<String, String> paramMap,  String[] pk_orgArray, Boolean refer){
	    	  String besinss_type = paramMap.get(IGlobalConstants.PARAMMAP_KEY_BUSINESS_TYPE);
	    	  String report_type = paramMap.get(IGlobalConstants.PARAMMAP_KEY_REPORT_TYPE);
	    	  String data_type = paramMap.get(IGlobalConstants.PARAMMAP_KEY_DATA_TYPE);
	    	  String reportBody = paramMap.get(IGlobalConstants.PARAMMAP_KEY_REPORTBODY);
	       	  String pkNotice = paramMap.get(IGlobalConstants.PARAMMAP_KEY_PK_NOTICE);	
	    	  Map<String, String> receivers = new HashMap<String, String>();
	    		try {
	    			String sql = " select b.* from scapco_notice_contact_type a inner join scapco_notice_contact_info b "
	    					+ " on a.pk_contact_type = b.pk_contact_type ";
	    	  				if (Boolean.TRUE.equals(refer)) {
	    	  					sql =  sql + " inner join scapco_receive_man c "
	    	  					+ " on b.pk_user = c.receive_man ";
	    	  				}
	    	  				if (StringUtils.isNotEmpty(besinss_type) && StringUtils.isNotEmpty(report_type)) {
		    	  				sql =  sql + " where  a.ye_type ='"
		    	    					+ besinss_type
		    	    					+ "' "
		    	    					+ " and a.report_type ='"
		    	    					+ report_type
		    	    					+ "' ";
	    	  				}
	    	  				if (StringUtils.isNotEmpty(data_type)) {
	    	  					sql =  sql + " where  a.data_type ='"
	    		    	      			+ data_type
	    		    	      			+ "' ";
	    	  				}
	    	  				if (Boolean.TRUE.equals(refer)) {
	    	  					sql =  sql + " and c.pk_notice = '" + pkNotice + "' ";
	    	  				}
	    	  				sql =  sql + " and b.dr = '0'    and a.enablestate = '2' ";
	    			// 遍历所有企业的联络人
	    			for (int i = 0; i < pk_orgArray.length; i++) {
	    				String pk_org = pk_orgArray[i];

	    				String conditionAppend = ""; 
	    				if (reportBody != null) {
	    					if ("1".equals(reportBody)) {
	    						conditionAppend = sql + " and b.pk_org ='" + pk_org + "' ";
	    					}
	    					if ("2".equals(reportBody)) {
	    						conditionAppend = sql + " and b.PK_VISUALORG ='" + pk_org + "' ";
	    					}
	    					if ("3".equals(reportBody)) {
	    						conditionAppend = sql + " and (b.pk_org ='" + pk_org + "' or  b.PK_VISUALORG ='" + pk_org + "' ) ";
	    					}
	    				} else {
	    					conditionAppend = sql + " and (b.pk_org ='" + pk_org + "' or  b.PK_VISUALORG ='" + pk_org + "' ) ";
	    				}
	    				List<Map<String, Object>> list;
	    				list = (List<Map<String, Object>>) ScapDAO.getBaseDAO()
	    						.executeQuery(conditionAppend, new MapListProcessor());
	    				for (int j = 0; j < list.size(); j++) {
	    					Map value = list.get(j);
	    					receivers.put((String) value
	    							.get(notice_contact_info.CONTACTS_NAME),
	    							(String) value.get(notice_contact_info.PHONE_NO));
	    				}
	    			}
	    		} catch (DAOException e) {
	    			e.printStackTrace();
	    		}
	    		return receivers;
	      }
			/**
			 * 根据通知的pk进行提醒操作
			 * 此时通知的两个子表应该都已经保存了（自动下发通知中的提醒部分和下发通知时的提醒部分都是用该函数进行具体的提醒工作）
			 */
			public static void remindByNoticePK(String pk_notice){
				BaseDAO baseDAO = new BaseDAO();
				// 取得相应的通知
				Notice_manager notice_manager = (Notice_manager) ScapDAO
						.retrieveByPK(Notice_manager.class, pk_notice);
					
	        		// 是否发送提醒
	        		String is_remind = notice_manager.getIs_remind();
	        		// 提醒方式
	        		// 1:平台；2:短信；3:email
	        		String remind_way = notice_manager.getRemind_way();
	        		if (StringUtils.isBlank(is_remind)) {
	        			return;
	        		}
	        		if (is_remind.equals("0")) {
	        			// 获得通知类型(必填项) 1=填报通知 2=消息通知
	        			String notice_style = notice_manager.getNotice_style();
	        			// 获得报告业务类型
	        			String besinss_type = notice_manager.getBusiness_type();
	        			// 获得报告实例类型
	        			String notice_type = notice_manager.getNotice_type();
	        			// 获得资料类型
	        			String data_type = notice_manager.getData_type();
	        			// 通知标题
	        			String notice_title = notice_manager.getNotice_title();
	        			// 提醒内容
	        			String Remind_info = notice_manager.getRemind_info();
	        			String reportBody = getReportBodyByNoticeType(notice_type);
	        			// 获得下发企业pk列表
	        			List<String> listTemp = new ArrayList<String>();
	        			if (reportBody != null && ("1".equals(reportBody))) {
	        				listTemp = selectCompSubTableByNoticePK_pkOrg(pk_notice);
	        			} else if (reportBody != null && ("2".equals(reportBody))) {
	        				listTemp = selectCompSubTableByNoticePK_VisiualOrg(pk_notice);
	        			} else if ( reportBody != null && "3".equals(reportBody)) {
	        				List<String> listTempPkOrg = new ArrayList<String>();
	        				listTempPkOrg = selectCompSubTableByNoticePK_pkOrg(pk_notice);
	        				listTemp.addAll(listTempPkOrg);
	        				List<String> listTempPkVisiualOrg = new ArrayList<String>();
	        				listTempPkVisiualOrg = selectCompSubTableByNoticePK_VisiualOrg(pk_notice);
	        				listTemp.addAll(listTempPkVisiualOrg);
	        			}
	        			String[] pk_orgArray = (String[])listTemp.toArray(new String[listTemp.size()]);

	        			/** 发送消息提醒=是，则发送相关通知企业的所有接收人员 */
        				if (notice_style.equals("1")) {
    				    	Map<String, String> paramMap = new HashMap<String, String>();
    				    	paramMap.put(IGlobalConstants.PARAMMAP_KEY_BUSINESS_TYPE, besinss_type);
    				    	paramMap.put(IGlobalConstants.PARAMMAP_KEY_REPORT_TYPE, notice_type);
    				    	paramMap.put(IGlobalConstants.PARAMMAP_KEY_REPORTBODY, reportBody);
    				    	paramMap.put(IGlobalConstants.PARAMMAP_KEY_PK_NOTICE, pk_notice);
    				    	
        					if ("1".equals(remind_way)) { // 平台
        						List<notice_contact_info> receiceManList = new ArrayList<notice_contact_info>();
        						receiceManList = NoticeUtil.getContactsList(
        								paramMap, pk_orgArray,Boolean.TRUE);
        						NCMessage[] message = new NCMessage[receiceManList
        								.size()];
        						for (int i = 0; i < receiceManList.size(); i++) {
        							NCMessage ncMessage = new NCMessage();
        							MessageVO messageVO = new MessageVO();
        							String pk_user = receiceManList.get(i).getPk_user();
        							String username = NoticeUtil.getNameByPkUser(pk_user);
        							messageVO.setSubject(notice_title);
        							messageVO.setContent(Remind_info.replace("用户姓名",
        									username));
        							messageVO.setMsgsourcetype("notice");
        							messageVO.setSendtime(new UFDateTime());
        							// /portal/app/pub_notice_manager?pagecode=manager&model=nc.scap.pub.model.NoticeListPageModel&nodecode=E9AA0117&$portletWind=pint_task_pint_TaskCenterPortlet&_h3ra=localhost/portal&$langcode=simpchn&$themeid=webclassic&lrid=2633612309
        							messageVO.setReceiver(receiceManList.get(i)
        									.getPk_user());
        							messageVO.setPk_group(InvocationInfoProxy.getInstance().getGroupId());
        							messageVO.setIsread(UFBoolean.FALSE);
        							ncMessage.setMessage(messageVO);
        							
        							message[i] = ncMessage;
        						}

        						try {
        							MessageCenter.sendMessage(message);
        						} catch (Exception e) {
        							// TODO 自动生成的 catch 块
        							e.printStackTrace();
        						}
        					} else if ("2".equals(remind_way)) { // 短信
        						// 获取联系人
        				    	Map<String, String> receivers = NoticeUtil.getContactsMap(
        				    			paramMap, pk_orgArray,Boolean.TRUE);
        						for (Map.Entry<String, String> entry : receivers
        								.entrySet()) {
        							Map<String, String> receiver = new HashMap<String, String>();
        							receiver.put(entry.getKey(), entry.getValue());
        							// 调用短信平台
        							// modified by ydyanyh 20150309
        							ScapSmsTaskVO task = SmsManageService.addTask(Remind_info.replace("用户姓名", entry.getKey()), entry.getValue(), "通知管理", "UAP");
        							realSendSms(task.getPk_task());
//	        							smsManageService
//	        									.addTask(
//	        											"scapco",
//	        											"短信标题",
//	        											Remind_info.replace("用户姓名",
//	        													entry.getKey()), receiver);
        						}
        					}
        				} else if (notice_style.equals("2")) {// 消息通知时按照资料类型去寻找相关联系人
    				    	Map<String, String> paramMap = new HashMap<String, String>();
    				    	paramMap.put(IGlobalConstants.PARAMMAP_KEY_DATA_TYPE, data_type);
    				    	paramMap.put(IGlobalConstants.PARAMMAP_KEY_PK_NOTICE, pk_notice);
        					if ("1".equals(remind_way)) { // 平台
        						List<notice_contact_info> receiceManList = new ArrayList<notice_contact_info>();
        						receiceManList = NoticeUtil.getContactsList(paramMap,
        								pk_orgArray,Boolean.TRUE);
        						NCMessage[] message = new NCMessage[receiceManList
        								.size()];
        						for (int i = 0; i < receiceManList.size(); i++) {
        							NCMessage ncMessage = new NCMessage();
        							MessageVO messageVO = new MessageVO();
        							String pk_user = receiceManList.get(i).getPk_user();
        							String username = NoticeUtil.getNameByPkUser(pk_user);
        							messageVO.setSubject(notice_title);
        							messageVO.setContent(Remind_info.replace("用户姓名",
        									username));
        							messageVO.setMsgsourcetype("notice");
        							messageVO.setSendtime(new UFDateTime());
        							messageVO.setReceiver(receiceManList.get(i)
        									.getPk_user());
        							messageVO.setPk_group(InvocationInfoProxy.getInstance().getGroupId());
        							messageVO.setIsread(UFBoolean.FALSE);
        							ncMessage.setMessage(messageVO);
        							
        							message[i] = ncMessage;
        						}

        						try {
        							MessageCenter.sendMessage(message);
        						} catch (Exception e) {
        							// TODO 自动生成的 catch 块
        							e.printStackTrace();
        						}
        					} else if ("2".equals(remind_way)) { // 短信
        						// 获取联系人

        						Map<String, String> receivers = NoticeUtil.getContactsMap(
        								paramMap, pk_orgArray,Boolean.TRUE);
        						for (Map.Entry<String, String> entry : receivers
        								.entrySet()) {
        							Map<String, String> receiver = new HashMap<String, String>();
        							receiver.put(entry.getKey(), entry.getValue());
        							// 调用短信平台
        							// modified by ydyanyh 20150309
        							SmsManageService.addTask(Remind_info.replace("用户姓名", entry.getKey()), entry.getValue(), "通知管理", "UAP");
//	        							smsManageService
//	        									.addTask(
//	        											"scapco",
//	        											"短信标题",
//	        											Remind_info.replace("用户姓名",
//	        													entry.getKey()), receiver);
        						}
        					}
        				}
	        		}
	          }
			/*
			 * 根据通知的pk进行检索接收范围子表检索出存在的pk_org
			 */
			public static List<String> selectCompSubTableByNoticePK_pkOrg(String pk_notice){
				    List<String> result = new ArrayList<String>();
				  BaseDAO baseDAO = new BaseDAO();
					String condition = " pk_notice = '" + pk_notice + "'";
					String conditionComp = condition + " and notice_org != '~' ";
					// 取得接收范围子表中的数据
					List<Receive_notice> list = new ArrayList<Receive_notice>();
					try {
						list = (List<Receive_notice>) baseDAO
								.retrieveByClause(Receive_notice.class, conditionComp);
					} catch (DAOException e) {
						ScapLogger.error("检索操作出现错误！错误异常：" + e.getMessage());
					}
		    		for (Receive_notice vo : list) {
		    			if (StringUtils.isNotEmpty(vo.getNotice_org())) {
		        			result.add(vo.getNotice_org());
		    			}
		    		}
		    		return result;
				  }
			public static List<String> selectCompSubTableByNoticePK_VisiualOrg(String pk_notice){
				    List<String> result = new ArrayList<String>();
				  BaseDAO baseDAO = new BaseDAO();
					String condition = " pk_notice = '" + pk_notice + "'";
					String conditionComp = condition + " and notice_visual_org != '~' ";
					// 取得接收范围子表中的数据
					List<Receive_notice> list = new ArrayList<Receive_notice>();
					try {
						list = (List<Receive_notice>) baseDAO
								.retrieveByClause(Receive_notice.class, conditionComp);
					} catch (DAOException e) {
						ScapLogger.error("检索操作出现错误！错误异常：" + e.getMessage());
					}
		    		for (Receive_notice vo : list) {
		    			if (StringUtils.isNotEmpty(vo.getNotice_visual_org())) {
		        			result.add(vo.getNotice_visual_org());
		    			}
		    		}
		    		return result;
				  }
			/*
			 * 根据pk_org进行检索接收范围子表检索出存在的那条数据
			 */
			public static Receive_notice selectCompSubTableByPkOrg(String pk_notice,String pk_org){
				    BaseDAO baseDAO = new BaseDAO();
				    Receive_notice result = null;
					String conditionComp = " PK_NOTICE = '" + pk_notice + "'" + " and  notice_org = '" + pk_org + "'";
					// 取得接收范围子表中的数据
					List<Receive_notice> list = new ArrayList<Receive_notice>();
					try {
						list = (List<Receive_notice>) baseDAO
								.retrieveByClause(Receive_notice.class, conditionComp);
					} catch (DAOException e) {
						ScapLogger.error("检索操作出现错误！错误异常：" + e.getMessage());
					}
					if (list != null && list.size() > 0) {
						result = list.get(0);
					}
					return result;
				  }
			/*
			 * 根据pk_visual_org进行检索接收范围子表检索出存在的那条数据
			 */
			public static Receive_notice selectCompSubTableByPkVisualOrg(String pk_notice,String pk_visual_org){
				    BaseDAO baseDAO = new BaseDAO();
				    Receive_notice result = null;
					String conditionComp = " PK_NOTICE = '" + pk_notice + "'" + " and notice_visual_org = '" + pk_visual_org + "'";
					// 取得接收范围子表中的数据
					List<Receive_notice> list = new ArrayList<Receive_notice>();
					try {
						list = (List<Receive_notice>) baseDAO
								.retrieveByClause(Receive_notice.class, conditionComp);
					} catch (DAOException e) {
						ScapLogger.error("检索操作出现错误！错误异常：" + e.getMessage());
					}
					if (list != null && list.size() > 0) {
						result = list.get(0);
					}
					return result;
				  }
			/*
			 * 根据pkUser进行检索接收人子表检索出存在的那条数据
			 */
			public static List<Receive_man> selectUserSubTableByPkUser(String pk_notice,String pkUser){
				    BaseDAO baseDAO = new BaseDAO();
					String conditionMan = " PK_NOTICE = '" + pk_notice + "'" + " and RECEIVE_MAN = '" + pkUser + "'";
					// 取得接收人子表中的数据
					List<Receive_man> list = new ArrayList<Receive_man>();
					try {
						list = (List<Receive_man>) baseDAO
								.retrieveByClause(Receive_man.class, conditionMan);
					} catch (DAOException e) {
						ScapLogger.error("检索操作出现错误！错误异常：" + e.getMessage());
					}
					return list;
				  }
			/*
			 * 根据pkUser进行检索联系人表，检索出消息类型
			 */
			public static List<String> selectMessageTypeByPkUser(String pkUser){
				List<notice_contact_type> contact_typeList = new ArrayList<notice_contact_type>();
				String sql = " select a.* from scapco_notice_contact_type a inner join scapco_notice_contact_info b "
						+ " on a.pk_contact_type = b.pk_contact_type "
						+ " where  b.pk_user ='"
						+ pkUser
						+ "' and b.dr = '0' "
						+ " and a.vdef1 != '~' and a.dr = '0'  and a.enablestate = '2' ";
				try {
				contact_typeList = (List<notice_contact_type>) ScapDAO.getBaseDAO()
						.executeQuery(sql,
								new BeanListProcessor(notice_contact_type.class));
				} catch (DAOException e) {
					ScapLogger.error("查询操作出现错误！错误异常：" + e.getMessage());
				}
				List<String> result = new ArrayList<String>();
				if (contact_typeList != null && contact_typeList.size() > 0) {
					for (notice_contact_type vo : contact_typeList) {
						if (StringUtils.isNotBlank(vo.getVdef1())) {
							result.add(vo.getVdef1());
						}
					}
				}
				return result;
			}
		/**
		 * 根据通知的pk进行催报操作（自动催报部分和手动催报部分都是用该函数进行具体的催报工作）
		 */
		public static void urgeByNoticePK(String pk_notice){
			
			BaseDAO baseDAO = new BaseDAO();
			// 取得相应的通知
			Notice_manager notice_manager = (Notice_manager) ScapDAO
					.retrieveByPK(Notice_manager.class, pk_notice);
			// 需要催报的人员的列表
			List<Receive_man> receive_manList = new ArrayList<Receive_man>();
			// 向催报表中插入给国资委联系人的报送统计内容（国资委用户）(只有一条数据)
			urge_notice urge_gzw = new urge_notice();
			int countOver = 0;
			int countNotOver = 0;
			// 如果是填报通知，进行催报操作
			if (IGlobalConstants.NOTICE_STYLE_REPORT.equals(notice_manager.getNotice_style())) {
				if (StringUtils.isNotEmpty(notice_manager.getNotice_type())) {
					// 取得通知相应报告类型的报告主体
					String report_body = NoticeUtil.getReportBodyByNoticeType(notice_manager.getNotice_type());
					String condition = " pk_notice = '" + pk_notice + "'";
					// 取得接收范围子表中的数据
					List<Receive_notice> list = new ArrayList<Receive_notice>();
					// 报告主体是企业
					if (IGlobalConstants.REPORT_BODY_QY.equals(report_body)) {
						String conditionComp = condition + " and notice_org != '~' ";
						try {
							list = (List<Receive_notice>) baseDAO
									.retrieveByClause(Receive_notice.class, conditionComp);
						} catch (DAOException e) {
							ScapLogger.error("检索操作出现错误！错误异常：" + e.getMessage());
						}
						for (Receive_notice comp : list) {
							// 取得企业填报状态
							String report_status = comp.getReport_status();
							// 如果该企业的通知填报状态是已提交或已接收，那么不需要催报
							if (StringUtils.isNotEmpty(report_status) 
									&& (IGlobalConstants.REPORT_STATUS_COMMIT.equals(report_status)
									|| IGlobalConstants.REPORT_STATUS_RECEIVED.equals(report_status))) {
								countOver++;
								continue;
							} 
							// 如果该企业的通知填报状态是未提交或已退回，那么需要催报
							if (StringUtils.isNotEmpty(report_status) 
									&& (IGlobalConstants.REPORT_STATUS_UNCOMMIT.equals(report_status)
									|| IGlobalConstants.REPORT_STATUS_RETURN.equals(report_status))) {
								// 取得需要催报的企业pk
								String notice_org = comp.getNotice_org();
								String conditionMan = condition + " and RECEIVE_ORG = '" + notice_org + "'";
								try {
									List<Receive_man> receive_manTemp = (List<Receive_man>) baseDAO
											.retrieveByClause(Receive_man.class, conditionMan);
									receive_manList.addAll(receive_manTemp);
								} catch (DAOException e) {
									ScapLogger.error("检索操作出现错误！错误异常：" + e.getMessage());
								}
								countNotOver++;
							}
						}
					}
					// 报告主体是组织
					if (IGlobalConstants.REPORT_BODY_VISORG.equals(report_body)) {
						String conditionVisualComp = condition + " and notice_visual_org != '~' ";
						try {
							list = (List<Receive_notice>) baseDAO
									.retrieveByClause(Receive_notice.class, conditionVisualComp);
						} catch (DAOException e) {
							ScapLogger.error("检索操作出现错误！错误异常：" + e.getMessage());
						}
						for (Receive_notice visualComp : list) {
							// 取得组织填报状态
							String report_status = visualComp.getReport_status();
							// 如果该组织的通知填报状态是已提交或已接收，那么不需要催报
							if (StringUtils.isNotEmpty(report_status) 
									&& (IGlobalConstants.REPORT_STATUS_COMMIT.equals(report_status)
									|| IGlobalConstants.REPORT_STATUS_RECEIVED.equals(report_status))) {
								countOver++;
								continue;
							}
							// 如果该组织的通知填报状态是未提交或已退回，那么需要催报
							if (StringUtils.isNotEmpty(report_status) 
									&& (IGlobalConstants.REPORT_STATUS_UNCOMMIT.equals(report_status)
									|| IGlobalConstants.REPORT_STATUS_RETURN.equals(report_status))) {
								// 取得需要催报的组织pk
								String notice_visual_org = visualComp.getNotice_visual_org();
								String conditionMan = condition + " and RECEIVE_VISUAL_ORG = '" + notice_visual_org + "'";
								try {
									List<Receive_man> receive_manTemp = (List<Receive_man>) baseDAO
											.retrieveByClause(Receive_man.class, conditionMan);
									receive_manList.addAll(receive_manTemp);
								} catch (DAOException e) {
									ScapLogger.error("检索操作出现错误！错误异常：" + e.getMessage());
								}
								countNotOver++;
							}
						}
					}
					// 报告主体是个人（此时只需要判断个人的填报状态（即不需要检索接收范围子表中的填报状态））
					if (IGlobalConstants.REPORT_BODY_MAN.equals(report_body)) {
						List<Receive_man> receive_manTemp = new ArrayList<Receive_man>();
						try {
							receive_manTemp = (List<Receive_man>) baseDAO
									.retrieveByClause(Receive_man.class, condition);
						} catch (DAOException e) {
							ScapLogger.error("检索操作出现错误！错误异常：" + e.getMessage());
						}
						for (Receive_man man : receive_manTemp) {
							// 取得个人填报状态
							String report_status = man.getReport_status();
							// 如果该个人的通知填报状态是已提交或已接收，那么不需要催报
							if (StringUtils.isNotEmpty(report_status) 
									&& (IGlobalConstants.REPORT_STATUS_COMMIT.equals(report_status)
									|| IGlobalConstants.REPORT_STATUS_RECEIVED.equals(report_status))) {
								countOver++;
								continue;
							} 
							// 如果该个人的通知填报状态是未提交或已退回，那么需要催报
							if (StringUtils.isNotEmpty(report_status) 
									&& (IGlobalConstants.REPORT_STATUS_UNCOMMIT.equals(report_status)
									|| IGlobalConstants.REPORT_STATUS_RETURN.equals(report_status))) {
								receive_manList.add(man);
								countNotOver++;
							}
						}
					}
				}
			} else {// 如果是消息通知，不用催报
				AppInteractionUtil.showMessageDialog("消息通知不需要催报!");
				return;
			}
			if (receive_manList == null || receive_manList.size() <= 0) {
				throw new LfwRuntimeException("没有找到需要催报的人!");
			}
			// 创建催报信息
			List<urge_notice> urgeList = new ArrayList<urge_notice>();

			for (Receive_man vo : receive_manList) {
				urge_notice urge = new urge_notice();
				String pk_primarykey = generatePk();
				// 设置主键
				urge.setPk_urge_notice(pk_primarykey);
				// 通知主键
				urge.setPk_notice(notice_manager.getPk_notice());
				// 接收人
				urge.setUrge_recept_man(vo.getReceive_man());
				// 催报消息内容格式（企业用户）
				String urge_content_qy = notice_manager.getUrge_content_qy();
				urge.setUrge_content_qy(urge_content_qy.replace("用户姓名",
						NoticeUtil.getNameByPkUser(vo.getReceive_man())));
				// 业务类型
				urge.setBusiness_type(notice_manager.getBusiness_type());
				// 报告类型
				urge.setNotice_type(notice_manager.getNotice_type());
				// 催报方式
				urge.setUrge_info_trans_way(notice_manager.getUrge_info_trans_way());
				// 被催报单位
				urge.setUrge_notice_org(vo.getReceive_org());
				// 催报通知标题
				String urgeTitleQy = IGlobalConstants.URGE_TITLE_QY.replace("业务类型", getNameByBusinessType(notice_manager.getBusiness_type())).replace("报告类型", getNameByNoticeType(notice_manager.getNotice_type()));
				urge.setUrge_notice_title(urgeTitleQy);
				// // 接收地址
				// urge.setUrge_recept_addr(vo.getReceive_org());
				// 催报人
				urge.setUrge_man(CntUtil.getCntUserPk());
				// 催报发送单位
				urge.setUrge_org(CntUtil.getCntOrgPk());
				// 催报发送单位
				urge.setUrge_time(new UFDate());
				
				// 如果催报信息发送方式 为短信，给联系人发送短信
				if ("2".equals(urge.getUrge_info_trans_way())) {
					Map<String, String> receivers = new HashMap<String, String>();
					// 根据接收人信息查询联系人表,查询联系人姓名和电话号码
					notice_contact_info contact_info = new notice_contact_info();
					try {
						String condition = " pk_user = '" + vo.getReceive_man() + "'";
						List<notice_contact_info> list = new ArrayList<notice_contact_info>();
						list = (List<notice_contact_info>) baseDAO
								.retrieveByClause(notice_contact_info.class, condition);
						contact_info = list.get(0);
					} catch (DAOException e) {
						e.printStackTrace();
						ScapLogger.error("发送催报信息到催报表中时出错" + e.getMessage());
					}
					receivers.put(contact_info.getContacts_name(),
							contact_info.getPhone_no());
					// 调用短信平台
					// modified by ydyanyh 20150309
					SmsManageService.addTask(urge_content_qy, contact_info.getPhone_no(), "通知管理", "UAP");
//					smsManageService.addTask("scapco", "短信标题",
//							urge_content_qy, receivers);
				}
				urgeList.add(urge);
			}
			// 发送催报信息到催报表中
			try {
				ScapDAO.getBaseDAO().insertVOList(urgeList);// 新建操作
			} catch (DAOException e) {
				e.printStackTrace();
				ScapLogger.error("发送催报信息到催报表中时出错" + e.getMessage());
			}
			// 向催报表中插入给国资委联系人的报送统计内容（国资委用户）
			urge_notice urge = new urge_notice();
			String pk_primarykey = generatePk();
			// 设置主键
			urge.setPk_urge_notice(pk_primarykey);
			// 通知主键
			urge.setPk_notice(notice_manager.getPk_notice());
			// 接收人
			urge.setUrge_recept_man(notice_manager.getNotice_psn());
			// 报送统计内容格式（国资委用户）
			// 截止到目前，【业务类型】-【报告类型】有【未填报企业个数】家企业未进行填报，【已填报企业个数】家企业完成填报。
			String urge_content_gzw = notice_manager.getUrge_content_gzw();
			urge_content_gzw = urge_content_gzw
					.replace("业务类型", getNameByBusinessType(notice_manager.getBusiness_type()))
					.replace("报告类型", getNameByNoticeType(notice_manager.getNotice_type()))
					.replace("未填报企业个数", String.valueOf(countNotOver))
					.replace("已填报企业个数", String.valueOf(countOver));
			urge.setUrge_content_qy(urge_content_gzw);
			// 业务类型
			urge.setBusiness_type(notice_manager.getBusiness_type());
			// 报告类型
			urge.setNotice_type(notice_manager.getNotice_type());
			// 催报方式
			urge.setUrge_info_trans_way(notice_manager.getUrge_info_trans_way());
			// 被催报单位
			urge.setUrge_notice_org(notice_manager.getNotice_send_org());
			// 催报通知标题
			String urgeTileGzw = IGlobalConstants.URGE_TITLE_GZW.replace("业务类型", getNameByBusinessType(notice_manager.getBusiness_type())).replace("报告类型", getNameByNoticeType(notice_manager.getNotice_type()));
			urge.setUrge_notice_title(urgeTileGzw);
			// // 接收地址
			// urge.setUrge_recept_addr(vo.getReceive_org());
		 // 催报人
		 urge.setUrge_man(notice_manager.getNotice_psn());
		 // 催报发送单位
		 urge.setUrge_org(notice_manager.getNotice_send_org());
			// 催报发送事件
			urge.setUrge_time(new UFDate());
			try {
				ScapDAO.getBaseDAO().insertVO(urge);
			} catch (DAOException e) {
				e.printStackTrace();
				ScapLogger.error("发送报送统计内容到催报表中时出错" + e.getMessage());
			}
		}
		/*
		 * 报告填报过程中的通知相关的状态的回写
		 */
		public static void noticeWriteBack(String pk_notice,String pkUser, String receive_status){
			// 如果pk_notice is null 不进行任何操作
			if (pk_notice == null || StringUtils.isEmpty(pk_notice)) {
				return ;
			}
			// 取得相应的通知
			Notice_manager notice_manager = (Notice_manager) ScapDAO
					.retrieveByPK(Notice_manager.class, pk_notice);
			// 获得通知类型(必填项) 1=填报通知 2=消息通知
			String notice_style = notice_manager.getNotice_style();
			// 获得报告实例类型
			String notice_type = notice_manager.getNotice_type();
			
			Work_report_type work_report_type = (Work_report_type) ScapDAO
			.retrieveByPK(Work_report_type.class, notice_type);
			// "0":是；"1":否；
			if ("1".equals(work_report_type.getIs_ass_noti())){
				return;
			}
			// 报告主体
			String reportBody = NoticeUtil.getReportBodyByNoticeType(notice_type);
			// 如果是填报通知，那个根据报告类型取得报告主体
			if (notice_style.equals("1")) {
				reportBody = NoticeUtil.getReportBodyByNoticeType(notice_type);
			} else if (notice_style.equals("2")) {
				// 如果是消息通知，则默认报告主体是个人
				reportBody = IGlobalConstants.REPORT_BODY_MAN;
			}
			// 根据报告主体进行相关处理
			// 报告主体是企业的时候 ，只需回写接受范围子表相关企业的填报状态
			if (IGlobalConstants.REPORT_BODY_QY.equals(reportBody)) {
				// 取得当前登录用户的pkUser和pkOrg
				String pkOrg = getPkOrgByPkUser(pkUser);
				Receive_notice receive_notice = selectCompSubTableByPkOrg(pk_notice,pkOrg);
				if (receive_notice != null) {

					if (IGlobalConstants.REPORT_STATUS_SAVE.equals(receive_status)) {
						// 设置这项的目的是为了报告填报是选择通知的方便处理;
						// 选择通知时 已经填报的通知和已经别人保存的通知要求当前人都不能看到
						// 而已保存这块的判断 在报告填报主表里面判断太复杂，所以挪到这里设置个字段进行保存
						// 接收范围和接收人子表根据报告主体都要进行相应设置
						// 有报告填报代码在保存的时候设置为“0”
						receive_notice.setDef1("0");
					} else if (IGlobalConstants.REPORT_STATUS_NOSAVE.equals(receive_status)) {
						receive_notice.setDef1(null);
						receive_notice.setReport_status(IGlobalConstants.REPORT_STATUS_UNCOMMIT);
					} else {
						receive_notice.setReport_status(receive_status);
					}
					receive_notice.setReport_time(new UFDate());
					ScapDAO.updateVO(receive_notice);
				}
				List<Receive_man> result = selectUserSubTableByPkUser(pk_notice,pkUser);
				for (Receive_man receive_man : result) {
					if (IGlobalConstants.REPORT_STATUS_SAVE.equals(receive_status)) {
						// 设置这项的目的是为了报告填报是选择通知的方便处理;
						// 选择通知时 已经填报的通知和已经别人保存的通知要求当前人都不能看到
						// 而已保存这块的判断 在报告填报主表里面判断太复杂，所以挪到这里设置个字段进行保存
						// 接收范围和接收人子表根据报告主体都要进行相应设置
						// 有报告填报代码在保存的时候设置为“0”
						receive_man.setDef1("0");
					}  else if (IGlobalConstants.REPORT_STATUS_NOSAVE.equals(receive_status)) {
						receive_man.setDef1(null);
						receive_man.setReport_status(IGlobalConstants.REPORT_STATUS_UNCOMMIT);
					} else {
						receive_man.setReport_status(receive_status);
					}
					receive_man.setReport_time(new UFDate());
					ScapDAO.updateVO(receive_man);
				}
			}
			// 报告主体是组织的时候 ，只需回写接受范围子表相关组织的填报状态
			if (IGlobalConstants.REPORT_BODY_VISORG.equals(reportBody)) {
				List<String> result = getAllPKVisualorgByPkUser(pkUser);
				for (String pk_visualorg : result) {
					Receive_notice receive_notice = selectCompSubTableByPkVisualOrg(pk_notice,pk_visualorg);
					if (receive_notice != null) {
						if (IGlobalConstants.REPORT_STATUS_SAVE.equals(receive_status)) {
							// 设置这项的目的是为了报告填报是选择通知的方便处理;
							// 选择通知时 已经填报的通知和已经别人保存的通知要求当前人都不能看到
							// 而已保存这块的判断 在报告填报主表里面判断太复杂，所以挪到这里设置个字段进行保存
							// 接收范围和接收人子表根据报告主体都要进行相应设置
							// 有报告填报代码在保存的时候设置为“0”
							receive_notice.setDef1("0");
						}  else if (IGlobalConstants.REPORT_STATUS_NOSAVE.equals(receive_status)) {
							receive_notice.setDef1(null);
							receive_notice.setReport_status(IGlobalConstants.REPORT_STATUS_UNCOMMIT);
						}  else {
							receive_notice.setReport_status(receive_status);
						}
						receive_notice.setReport_time(new UFDate());
						ScapDAO.updateVO(receive_notice);
					}
				}
				List<Receive_man> resultMan = selectUserSubTableByPkUser(pk_notice,pkUser);
				for (Receive_man receive_man : resultMan) {
					if (IGlobalConstants.REPORT_STATUS_SAVE.equals(receive_status)) {
						// 设置这项的目的是为了报告填报是选择通知的方便处理;
						// 选择通知时 已经填报的通知和已经别人保存的通知要求当前人都不能看到
						// 而已保存这块的判断 在报告填报主表里面判断太复杂，所以挪到这里设置个字段进行保存
						// 接收范围和接收人子表根据报告主体都要进行相应设置
						// 有报告填报代码在保存的时候设置为“0”
						receive_man.setDef1("0");
					}   else if (IGlobalConstants.REPORT_STATUS_NOSAVE.equals(receive_status)) {
						receive_man.setDef1(null);
						receive_man.setReport_status(IGlobalConstants.REPORT_STATUS_UNCOMMIT);
					}  else {
						receive_man.setReport_status(receive_status);
					}
					receive_man.setReport_time(new UFDate());
					ScapDAO.updateVO(receive_man);
				}
			}
			// 报告主体是个人的时候 ，只需回写接收人子表相关人员的填报状态
			if (IGlobalConstants.REPORT_BODY_MAN.equals(reportBody)) {
				List<Receive_man> result = selectUserSubTableByPkUser(pk_notice,pkUser);
				for (Receive_man receive_man : result) {
					if (IGlobalConstants.REPORT_STATUS_SAVE.equals(receive_status)) {
						// 设置这项的目的是为了报告填报是选择通知的方便处理;
						// 选择通知时 已经填报的通知和已经别人保存的通知要求当前人都不能看到
						// 而已保存这块的判断 在报告填报主表里面判断太复杂，所以挪到这里设置个字段进行保存
						// 接收范围和接收人子表根据报告主体都要进行相应设置
						// 有报告填报代码在保存的时候设置为“0”
						receive_man.setDef1("0");
					}   else if (IGlobalConstants.REPORT_STATUS_NOSAVE.equals(receive_status)) {
						receive_man.setDef1(null);
						receive_man.setReport_status(IGlobalConstants.REPORT_STATUS_UNCOMMIT);
					} else {
						receive_man.setReport_status(receive_status);
					}
					receive_man.setReport_time(new UFDate());
					ScapDAO.updateVO(receive_man);
				}
			}
		}
	/**
	 * 是否是唯一的通知check：
	 * 按照业务类型，报告类型，截止日期三个字段确定一个通知。如果这三个字段不同的话，就认为是两个通知。
	 * 否则的话，认为是一个通知，不能保存
	 */
	  public static Boolean checkIsUnique(String business_type,String notice_type,UFDate expiration_date,String oper){
		  Boolean isUnique = true;
		  StringBuffer sql = new StringBuffer();
		  sql.append( " select * from SCAPCO_NOTICE_MANAGER where ")
		  	 .append( " BUSINESS_TYPE = '").append(business_type).append("' ")
		  	 .append( " and NOTICE_TYPE = '").append(notice_type).append("' ")
		  	 .append( " and EXPIRATION_DATE = '").append(expiration_date).append("' ");
		  List<Notice_manager> list = (List<Notice_manager>) ScapDAO.executeQuery(sql.toString(), new BeanListProcessor(
							Notice_manager.class));
		  if (AppConsts.OPE_ADD.equals(oper)) {
			  if (list != null && list.size() > 0) {
				  isUnique = false;
			  }
		  } else if (AppConsts.OPE_EDIT.equals(oper)) {
			  if (list != null && list.size() > 1) {
				  isUnique = false;
			  }
		  }

		  return isUnique;
	  }
		/**
		 * 生成PK值
		 * @return
		 */
		public static String generatePk(){
			String datasource = LfwRuntimeEnvironment.getDatasource();
			return PtBaseDAO.generatePK(datasource);
		}
		/**
		 * 生成唯一编号
		 * @return
		 */
		public static String generateNoticeUID(){
			String curDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
			
			String sql = "select notice_no.nextval as sequence from dual;";
			Map<String, Object> map = (Map<String, Object>) ScapDAO.executeQuery(sql, new MapProcessor());
			String sequence = "000";
			if(map != null && map.get("sequence") != null) {
				sequence = String.format("%05d", map.get("sequence"));
						String.valueOf(map.get("sequence"));
			}

			String result = curDate + "_" + sequence;
			return result;
		}
		/**
		 * 生成唯一编号
		 * @return
		 */
		public static String generateUID(){
			String curDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
			
			String sql = "select standard_no.nextval as sequence from dual;";
			Map<String, Object> map = (Map<String, Object>) ScapDAO.executeQuery(sql, new MapProcessor());
			String sequence = "000";
			if(map != null && map.get("sequence") != null) {
				sequence = String.format("%05d", map.get("sequence"));
						String.valueOf(map.get("sequence"));
			}

			String result = curDate + "_" + sequence;
			return result;
		}
		/**
		 * 最终发送短信
		 * @return
		 */
		public static void realSendSms(String pkTask){
			List<ISmsExtentionService> exts = ScapSmsExtentionUtil.getSmsServiceList();
			if (exts.size() != 1) {
				throw new LfwRuntimeException("未启用或启用了多个短信扩展点");
			}
			ISmsExtentionService ext = exts.get(0);

			ScapSmsTaskVO task = null;
			try {
				task = SmsManageService.findTask(pkTask, ScapSmsStatusEnum.NOT_SENT, ScapSmsStatusEnum.FAILED);
				ext.send(task);
			} catch (Exception e) {
				throw new LfwRuntimeException("发送失败", e);
			}

			try {
				ScapDAO.updateVO(task);
				if (task.getSmses() != null) {
					ScapDAO.updateVOArray(task.getSmses());
				}
			} catch (Exception e) {
				throw new LfwRuntimeException("回写短信过程出现异常", e);
			}
		}
}
