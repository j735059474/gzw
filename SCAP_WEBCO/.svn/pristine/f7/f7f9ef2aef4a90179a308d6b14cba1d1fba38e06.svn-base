package nc.scap.pub.noticeTimer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.pub.pa.IPreAlertPlugin;
import nc.bs.pub.pa.PreAlertContext;
import nc.bs.pub.pa.PreAlertObject;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.notice.util.NoticeUtil;
import nc.scap.pub.util.ScapDAO;
import nc.uap.cpb.persist.dao.PtBaseDAO;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.constants.AppConsts;
import nc.uap.portal.log.ScapLogger;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.scap.work_report_type.Work_report_type;
import nc.vo.scapco.work_notice_contacts.notice_contact_info;
import nc.vo.scapco.work_notice_manager.Notice_manager;
import nc.vo.scapco.work_notice_manager.Receive_man;
import nc.vo.scapco.work_notice_manager.Receive_notice;

public class AutoNoticeTimerPlugin implements IPreAlertPlugin {
	public AutoNoticeTimerPlugin() {
	}

	public PreAlertObject executeTask(PreAlertContext arg0)
			throws BusinessException {

		// 根据报告类型表中是否报送方式是定期，和是否关联通知，进行通知的下发
		taskReportType();
		// 根据报告表中未上报完 的通知进行催报
		taskNoticeUndo();
		return null;
	}

	/**
	 * 根据报告类型表中是否自动催报及相关信息 进行催报
	 */
	private void taskReportType() {
		BaseDAO baseDAO = new BaseDAO();
		// 先检索报告类型表，定期报告需要催报的报告类型 两个检索条件：定期,是否关联通知
		List<Work_report_type> work_report_typeList = new ArrayList<Work_report_type>();
		StringBuffer sqlReportType = new StringBuffer();
		//  and t.report_code like 'testlu%'
		sqlReportType
				.append("select t.* from scapco_work_reportType t where t.report_way = '1' and t.is_ass_noti = '0' and t.dr = '0' ");
		try {
			work_report_typeList = (List<Work_report_type>) baseDAO
					.executeQuery(sqlReportType.toString(),
							new BeanListProcessor(Work_report_type.class));
		} catch (DAOException e) {
			e.printStackTrace();
			ScapLogger.error("检索需要定期上报的报告类型时出错" + e.getMessage());
		}

		// 当前系统时间
		UFDate sysDate = new UFDate();
		for (Work_report_type report_type_vo : work_report_typeList) {

			// 获得报告截止日期，根据相应约定进行解析
			// 催报时间格式约定：[Y].[Q].[MM].[DD]，共8位，中间用英文逗号相隔。
			String expiration_date = report_type_vo.getExpiration_date();
			// 获得报送频率 (年：1，季:2，月:3，旬：4，日:5)
			String submitFrecy = report_type_vo.getAttrname();
			// 解析出来生成UFDate类型的报告截止日期
			UFDate expiration_dateMake = getExpirationDate(expiration_date,
					submitFrecy, sysDate);
			
			// 首先需要确认该报告类型是否已经手动发送了
			Boolean isUnique = NoticeUtil.checkIsUnique(report_type_vo.getYe_type(), report_type_vo.getPk_work_report_type(), expiration_dateMake,AppConsts.OPE_ADD);
			if (Boolean.FALSE.equals(isUnique)) {
				continue;
			}
			// 催报提前天数(天)
			String urge_time = report_type_vo.getUrge_time();
			// 催报开始时间
			UFDate urgeStartDay = expiration_dateMake.getDateBefore(Integer
					.valueOf(urge_time));
			// 日报需要特殊处理
			if ("5".equals(submitFrecy)) {
				urgeStartDay = sysDate;
				expiration_dateMake = expiration_dateMake.getDateAfter(Integer
						.valueOf(urge_time));
			}
			// 如果已经到了催报开始时间，则自动发送通知(已下发)
			if (compareDate(urgeStartDay,sysDate) == 0) {
				// 自动给该报告类型相关的企业和联系人下发通知
				autoNoticeIssue(report_type_vo, expiration_dateMake);
			}
		}
	}

	/**
	 * 根据报告表中为上报完 的通知进行催报
	 */
	private void taskNoticeUndo() {
		BaseDAO baseDAO = new BaseDAO();
		// 取得所有已下发的通知
		List<Notice_manager> noticeList = new ArrayList<Notice_manager>();
		StringBuffer sqlNotice = new StringBuffer();
		sqlNotice
				.append(" SELECT a.* FROM ")
				.append(" SCAPCO_NOTICE_MANAGER a  ")
				.append(" where a.NOTICE_STATUS = '2' and a.IS_AUTO_URGE = '0' and a.expiration_date >= '" + new UFDate() + "'"); // 已下发的通知
		try {
			noticeList = (List<Notice_manager>) baseDAO.executeQuery(sqlNotice
					.toString(), new BeanListProcessor(Notice_manager.class));
		} catch (DAOException e) {
			e.printStackTrace();
			ScapLogger.error("检索尚未上报的通知时出错" + e.getMessage());
		}
		// 当前系统时间
		UFDate sysDate = new UFDate();
		for (Notice_manager notice_vo : noticeList) {
			// 根据催报频率，确认该通知是否到了催报时间
			UFDate expiration_date = notice_vo.getExpiration_date();
			// 提前催报天数
			Integer day_num = notice_vo.getDay_num();
			// 计算出催报开始时间
			UFDate urge_start_date = expiration_date.getDateBefore(day_num);
			// 催报频率 单次催报:1;每天催报:2;隔天催报3；
			String urge_frequency = notice_vo.getUrge_frequency();
			// 系统时间必须在催报开始时间和报告截止时间之间
			if (sysDate.compareTo(expiration_date) <= 0 && sysDate.compareTo(urge_start_date) >= 0) {
				// 单次催报
				if(IGlobalConstants.URGE_FREQUENCY_SINGLE.equals(urge_frequency)) {
					// 只有当系统时间等于催报开始时间时 发送催报消息.其他时候不发送
					if (compareDate(urge_start_date,sysDate) != 0) {
						continue;
					}
				// 每天催报	
				} else if (IGlobalConstants.URGE_FREQUENCY_DAILY.equals(urge_frequency)) {
					// 每天都催报
				// 隔天催报	
				} else if (IGlobalConstants.URGE_FREQUENCY_GETIAN.equals(urge_frequency)) {
					// 需要判断，当前系统时间是否从催报开始时间算起的话是隔天；例子：1，3，5
					int day = sysDate.getDaysAfter(urge_start_date);
					// 间隔天数不是偶数，那就不是隔天了，呵呵
					if (day%2!=0){
						continue;
					}
				}
			} else {
				continue;
			}
			// 调用共同功能进行催报具体操作
			NoticeUtil.urgeByNoticePK(notice_vo.getPk_notice());
		}
	}

	/**
	 * 定期下发通知
	 */
	private void autoNoticeIssue(Work_report_type report_type_vo,
			UFDate expiration_dateMake) {

		// 插入通知主表数据
		Notice_manager notice_manager = new Notice_manager();
		String pk_primarykey = generatePk();
		// 设置主键
		notice_manager.setPk_notice(pk_primarykey);
		// 通知编号
		notice_manager.setNotice_no(NoticeUtil.generateNoticeUID());
		// 通知类型
		notice_manager.setNotice_style("1");// 填报通知
		// 通知标题
		notice_manager.setNotice_title("系统自动通知");
		// 业务类型
		notice_manager.setBusiness_type(report_type_vo.getYe_type());
		// 报告实例类型
		notice_manager.setNotice_type(report_type_vo.getPk_work_report_type());
		// 通知内容
		String yeTypeName = NoticeUtil.getNameByBusinessType(report_type_vo.getYe_type());
		String notice_content = "【用户姓名】，您好。您尚未完成【业务类型】-【工作报告类型】填报，请登录系统进行填报。"
				.replace("业务类型", yeTypeName)
				.replace("工作报告类型", report_type_vo.getReport_type());
		notice_manager
				.setNotice_content(notice_content);
		// 是否发送提醒
		notice_manager.setIs_remind("0");
		// 提醒方式
		notice_manager.setRemind_way("1");
		// 提醒内容
		String remind_info = "【用户姓名】，您好。您尚未完成【业务类型】-【工作报告类型】填报，请登录系统进行填报。"
				.replace("业务类型", yeTypeName)
				.replace("工作报告类型", report_type_vo.getReport_type());
		notice_manager
				.setRemind_info(remind_info);
		// 是否自动催报
		notice_manager.setIs_auto_urge(report_type_vo.getIs_auto_urge());
		// 催报信息发送方式
		notice_manager.setUrge_info_trans_way(report_type_vo.getUrge_info_trans_way());
		// 催报频率
		notice_manager.setUrge_frequency(report_type_vo.getUrge_frequency());
		// 提前催报天数
		notice_manager
				.setDay_num(Integer.valueOf(report_type_vo.getUrge_time()));
		// 报送统计内容格式（国资委用户）
		String urge_content_gzw= "截止到目前，【业务类型】-【报告类型】有【未填报企业个数】家企业未进行填报，【已填报企业个数】家企业完成填报。"
				.replace("业务类型", yeTypeName)
				.replace("工作报告类型", report_type_vo.getReport_type());
		notice_manager
				.setUrge_content_gzw(urge_content_gzw);
		// 催报消息内容格式（企业用户）
		String urge_content_qy= "【用户姓名】，您好。您尚未完成【业务类型】-【工作报告类型】填报，请登录系统进行填报。"
				.replace("业务类型", yeTypeName)
				.replace("工作报告类型", report_type_vo.getReport_type());
		notice_manager
				.setUrge_content_qy(urge_content_qy);
		// 通知下发状态
		notice_manager.setNotice_status("2");// 已下发
		// 上报截止日期
		notice_manager.setExpiration_date(expiration_dateMake);
		// 默认通知日期
		notice_manager.setNotice_date(new UFDate());
		// 发送通知人
		notice_manager.setNotice_psn("~");
		// 发送通知企业
		notice_manager.setNotice_send_org("~");
		try {
			// 保存主表
			ScapDAO.getBaseDAO().insertVOWithPK(notice_manager);
		} catch (DAOException e) {
			ScapLogger.error("更新操作出现错误！错误异常：" + e.getMessage());
		}
		// 设置相关子表数据
		List<notice_contact_info> receiceManListAll = new ArrayList<notice_contact_info>();
		List<String> pk_orgListAll = new ArrayList<String>();
		// 获得报告类型画面接收范围子表维护的接收企业和接收组织
		// 接收企业
		String report_body = report_type_vo.getReport_body();
		// 联系人检索参数map
		Map<String, String> paramMap = new HashMap<String, String>();
    	paramMap.put(IGlobalConstants.PARAMMAP_KEY_BUSINESS_TYPE, report_type_vo.getYe_type());
    	paramMap.put(IGlobalConstants.PARAMMAP_KEY_REPORT_TYPE, report_type_vo.getPk_work_report_type());
    	paramMap.put(IGlobalConstants.PARAMMAP_KEY_REPORTBODY, report_body);

		// 根据报告类型的报告主体，进行相应处理
		if (report_body != null && ("1".equals(report_body) || "3".equals(report_body))) {
			List<String> listTemp = new ArrayList<String>();
			listTemp = NoticeUtil.getOrgsPKByPkWorkReportType(report_type_vo.getPk_work_report_type());
			pk_orgListAll.addAll(listTemp);
			String[] pk_orgArray = (String[])listTemp.toArray(new String[listTemp.size()]);
			Receive_notice receive_noticeArray[] = new Receive_notice[pk_orgArray.length];
			// 保存接受企业子表信息
			for (int i = 0; i < pk_orgArray.length; i++) {
				Receive_notice receive_notice = new Receive_notice();
				String pk_org = pk_orgArray[i];
				// 主键
				receive_notice.setPk_notice_org(generatePk());
				// 通知主键 主表pk
				receive_notice.setPk_notice(pk_primarykey);
				// 接收企业
				receive_notice.setNotice_org(pk_org);
				// 查看状态（未查看）
				receive_notice.setReceive_status("1");
				// 提交状态（未提交）
				receive_notice.setReport_status(IGlobalConstants.REPORT_STATUS_UNCOMMIT);
				receive_noticeArray[i] = receive_notice;
			}
			try {
				// 保存子表
				ScapDAO.getBaseDAO().insertVOArrayWithPK(receive_noticeArray);
			} catch (DAOException e) {
				ScapLogger.error("更新操作出现错误！错误异常：" + e.getMessage());
			}
			// 保存接收人子表信息
			// 获取联系人
			List<notice_contact_info> receiceManListTemp = new ArrayList<notice_contact_info>();
			// 按照业务类型和报告类型去寻找联系人
			receiceManListTemp = NoticeUtil.getContactsList(paramMap, pk_orgArray,Boolean.FALSE);
			Receive_man receiceManArray[] = new Receive_man[receiceManListTemp.size()];
			for (int i = 0; i < receiceManArray.length; i++) {
				Receive_man receive_man = new Receive_man();
				// 主键
				receive_man.setPk_receive_man(generatePk());
				// 通知主键
				receive_man.setPk_notice(pk_primarykey);
				// 接收人
				receive_man.setReceive_man(receiceManListTemp.get(i).getPk_user());
				// 接收单位
				receive_man.setReceive_org(receiceManListTemp.get(i).getPk_org());
				// // 性别
				// receive_man.setSex(receiceManList.get(i).get);
				// 电话号码
				receive_man.setTelephone(receiceManListTemp.get(i).getPhone_no());
				// Email
				receive_man.setEmail(receiceManListTemp.get(i).getEmail());
				// 查看状态(未查看)
				receive_man.setReceive_status("1");
				// 上报状态(未提交)
				receive_man.setReport_status(IGlobalConstants.REPORT_STATUS_UNCOMMIT);
				receiceManArray[i] = receive_man;
			}
			try {
				// 保存子表
				ScapDAO.getBaseDAO().insertVOArrayWithPK(receiceManArray);
			} catch (DAOException e) {
				ScapLogger.error("更新操作出现错误！错误异常：" + e.getMessage());
			}
		}
		// 根据报告类型的报告主体，进行相应处理
		if (report_body != null && ("2".equals(report_body) || "3".equals(report_body))) {
			List<String> listTemp = new ArrayList<String>();
			listTemp = NoticeUtil.getVisualOrgsPKByPkWorkReportType(report_type_vo.getPk_work_report_type());
			String[] pk_VisualOrgArray = (String[])listTemp.toArray(new String[listTemp.size()]);
			Receive_notice receive_noticeArray[] = new Receive_notice[pk_VisualOrgArray.length];
			// 保存接受企业子表信息
			for (int i = 0; i < pk_VisualOrgArray.length; i++) {
				Receive_notice receive_notice = new Receive_notice();
				String pk_VisualOrg = pk_VisualOrgArray[i];
				// 主键
				receive_notice.setPk_notice_org(generatePk());
				// 通知主键 主表pk
				receive_notice.setPk_notice(pk_primarykey);
				// 接收组织
				receive_notice.setNotice_visual_org(pk_VisualOrg);
				// 查看状态（未查看）
				receive_notice.setReceive_status("1");
				// 提交状态（未提交）
				receive_notice.setReport_status(IGlobalConstants.REPORT_STATUS_UNCOMMIT);
				receive_noticeArray[i] = receive_notice;
			}
			try {
				// 保存子表
				ScapDAO.getBaseDAO().insertVOArrayWithPK(receive_noticeArray);
			} catch (DAOException e) {
				ScapLogger.error("更新操作出现错误！错误异常：" + e.getMessage());
			}
			// 保存接收人子表信息
			// 获取联系人
			// 按照业务类型和报告类型去寻找联系人
			List<notice_contact_info> receiceManListTemp = new ArrayList<notice_contact_info>();
			receiceManListTemp = NoticeUtil.getContactsList(paramMap, pk_VisualOrgArray,Boolean.FALSE);
			Receive_man receiceManArray[] = new Receive_man[receiceManListTemp.size()];
			for (int i = 0; i < receiceManArray.length; i++) {
				Receive_man receive_man = new Receive_man();
				// 主键
				receive_man.setPk_receive_man(generatePk());
				// 通知主键
				receive_man.setPk_notice(pk_primarykey);
				// 接收人
				receive_man.setReceive_man(receiceManListTemp.get(i).getPk_user());
				// 接收组织
				receive_man.setReceive_visual_org(receiceManListTemp.get(i).getPk_visualorg());
				// // 性别
				// receive_man.setSex(receiceManList.get(i).get);
				// 电话号码
				receive_man.setTelephone(receiceManListTemp.get(i).getPhone_no());
				// Email
				receive_man.setEmail(receiceManListTemp.get(i).getEmail());
				// 查看状态(未查看)
				receive_man.setReceive_status("1");
				// 上报状态(未提交)
				receive_man.setReport_status(IGlobalConstants.REPORT_STATUS_UNCOMMIT);
				receiceManArray[i] = receive_man;
			}
			try {
				// 保存子表
				ScapDAO.getBaseDAO().insertVOArrayWithPK(receiceManArray);
			} catch (DAOException e) {
				ScapLogger.error("更新操作出现错误！错误异常：" + e.getMessage());
			}
		}
		// 调用共同的提醒功能
		// 通知下发后发送提醒
		// 自动生成的报告默认平台、短信都发送
		NoticeUtil.remindByNoticePK(pk_primarykey);
	}

	/**
	 * 取得报告截止时间ufdate型
	 */
	private UFDate getExpirationDate(String expiration_date,
			String submitFrecy, UFDate sysDate) {

		// 使用calendar设置年月日,在设置到ufdate中去
		Calendar calendar = Calendar.getInstance();
		String[] values = expiration_date.split("\\.");
		// 取出约定格式中 【年】 的部分
		String year = values[0];
		// 取出约定格式中 【季】 的部分
		String quarter = values[1];
		// 取出约定格式中 【月】 的部分
		String month = values[2];
		// 取出约定格式中 【日】 的部分
		String day = values[3];
		if (values.length != 4) {
			ScapLogger.error("报告日期格式不对，请确认!");
		}
		// 年报
		if ("1".equals(submitFrecy)) {
			// 表示每年都报
			if ("Y".equals(year)) {
				// 使用calendar设置年月日
				calendar.set(sysDate.getYear(), Integer.valueOf(month) - 1,
						Integer.valueOf(day));
				// 如果当前系统时间大于正常设定的报告截止时间的年月日 的时候，说明不能以当前年月计算下一次年报时间，需要用再下一次年报时间
				if (compareDate(sysDate,new UFDate(calendar.getTime())) == 1) {
					calendar.set(sysDate.getYear() + 1, Integer.valueOf(month) - 1,
							Integer.valueOf(day));
				}
			}
			// 季报
		} else if ("2".equals(submitFrecy)) {
			// 表示每季度上报
			if ("Q".equals(quarter)) {
				Integer FirstQMonth = getFirstQMonthByMonth(sysDate.getMonth());
				// 使用calendar设置年月日
				calendar.set(sysDate.getYear(),
						FirstQMonth + Integer.valueOf(month.substring(1)) - 1 - 1,
						Integer.valueOf(day));
				// 如果当前系统时间大于正常设定的报告截止时间的年月日 的时候，说明不能以当前年月计算下一次季报时间，需要计算再下一次季报时间
				if (compareDate(sysDate,new UFDate(calendar.getTime())) == 1) {
					calendar.add(Calendar.MONTH, 3);
				} 
			}

			// 月报
		} else if ("3".equals(submitFrecy)) {
			// 表示每月上报
			if ("MM".equals(month)) {
				// 使用calendar设置年月日
				// 设置月份的时候 需要特殊处理：如果当前系统时间"日"字段的值 大于 设定的"日"字段的值。应该月份设置为系统当前月份的下个月；
				// （为什么这么处理？因为如果是设置成当前月份的话 。有些问题处理不了
				// 比如 今天是4月1号 那么的出的截止日期就是4月5号，如果提前催报天数是5的话 算出的通知下发日期和催报开始日期就是3月31号。
				// 而今天是4月1号，就发不了这个通知了。为了处理这种，报告下发日期计算过后会使上个月的日期的情况所以改为现在的处理）
				// 其他季报旬报年报也做类似处理
				calendar.set(sysDate.getYear(), sysDate.getMonth() - 1,
						Integer.valueOf(day));
				// 如果当前系统时间大于正常设定的报告截止时间的年月日 的时候，说明不能以当前年月计算下一次月报时间，需要计算再下一次月报时间
				if (compareDate(sysDate,new UFDate(calendar.getTime())) == 1) {
					calendar.add(Calendar.MONTH, 1);
				} 
			}
			// 旬报
		} else if ("4".equals(submitFrecy)) {
			// 表示每月上旬上报
			if ("D1".equals(day)) {
				// 使用calendar设置年月日
				calendar.set(sysDate.getYear(), sysDate.getMonth() - 1,
						Integer.valueOf("1"));
				// 设置成本月中旬
				if (compareDate(sysDate,new UFDate(calendar.getTime())) == 1) {
					calendar.set(sysDate.getYear(), sysDate.getMonth() - 1,
							Integer.valueOf("11"));
				}
				// 表示每月中旬上报
			} else if ("D2".equals(day)) {
				// 使用calendar设置年月日
				calendar.set(sysDate.getYear(), sysDate.getMonth() - 1,
						Integer.valueOf("11"));
				// 设置为本月下旬
				if (compareDate(sysDate,new UFDate(calendar.getTime())) == 1) {
					calendar.set(sysDate.getYear(), sysDate.getMonth() - 1,
							Integer.valueOf("21"));
				}
				// 表示每月下旬上报
			} else if ("D3".equals(day)) {
				// 使用calendar设置年月日
				calendar.set(sysDate.getYear(), sysDate.getMonth() - 1,
						Integer.valueOf("21"));
				// 设置为下个月的上旬
				if (compareDate(sysDate,new UFDate(calendar.getTime())) == 1) {
					calendar.set(sysDate.getYear(), sysDate.getMonth() - 1,Integer.valueOf("1"));
					calendar.add(Calendar.MONTH, 1);
				}
			}
			// 日报
		} else if ("5".equals(submitFrecy)) {
			// 表示每天上报
			if ("DD".equals(day)) {
				// 使用calendar设置年月日
				calendar.set(sysDate.getYear(), sysDate.getMonth() - 1,
						sysDate.getDay());
			}
		}
		UFDate result = new UFDate(calendar.getTime());
		return result;
	}

	/**
	 * 根据报告表中为上报完 的通知进行催报
	 */
	private Integer getFirstQMonthByMonth(Integer month) {
		Integer result = Integer.valueOf(1);
		// 第一季度
		switch (month) {
		case 1:
		case 2:
		case 3:
			result = Integer.valueOf(1);
			break;
		case 4:
		case 5:
		case 6:
			result = Integer.valueOf(4);
			break;
		case 7:
		case 8:
		case 9:
			result = Integer.valueOf(7);
			break;
		case 10:
		case 11:
		case 12:
			result = Integer.valueOf(10);
			break;
		}
		return result;
	}

	/**
	 * 通过报告类型获取联系人姓名和电话号码
	 * 
	 * @param besinss_type报告业务类型
	 * @param notice_type报告实例类型
	 * @param pk_orgArray接收企业数组
	 */
	private Map<String, String> getContactsMap(String besinss_type,
			String notice_type, String[] pk_orgArray) {
		Map<String, String> receivers = new HashMap<String, String>();
		try {
			// 遍历所有企业的联络人
			for (int i = 0; i < pk_orgArray.length; i++) {
				String pk_org = pk_orgArray[i];
				String sql = " select b.* from scapco_notice_contact_type a inner join scapco_notice_contact_info b " +
						  " on a.pk_contact_type = b.pk_contact_type " +
						  " where  a.ye_type ='"+besinss_type+"' " +
						  " and a.report_type ='"+notice_type+"' " +
						  " and b.pk_org ='" + pk_org + "' " +
						  " and a.dr = '0'  and a.enablestate = '2' ";
				List<Map<String, Object>> list;
				list = (List<Map<String, Object>>)ScapDAO.getBaseDAO().executeQuery(sql, new MapListProcessor());
				for (int j = 0; j < list.size(); j++) {
					Map value = list.get(j);
					receivers.put((String)value.get(notice_contact_info.CONTACTS_NAME),
							(String)value.get(notice_contact_info.PHONE_NO));
				}
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return receivers;
	}
	/**
	 * 生成PK值
	 * 
	 * @return
	 */
	protected String generatePk() {
		String datasource = LfwRuntimeEnvironment.getDatasource();
		return PtBaseDAO.generatePK(datasource);
	}
	/**
	 * 通过判断年月日字段是否相同，判断日期是否相同
	 * 
	 */
	private int compareDate(UFDate dateF, UFDate dateL) {
		int result = 0;
		Integer yearF = dateF.getYear();
		Integer yearL = dateL.getYear();
		String monthF = dateF.getStrMonth();
		String monthL = dateL.getStrMonth();
		String dayF = dateF.getStrDay();
		String dayL = dateL.getStrDay();
		if (yearF.compareTo(yearL) > 0){
			result = 1;
		} else if (yearF.compareTo(yearL) == 0){
			if (monthF.compareTo(monthL) > 0){
				result = 1;
			} else if (monthF.compareTo(monthL) == 0){
				if (dayF.compareTo(dayL) > 0){
					result = 1;
				} else if (dayF.compareTo(dayL) == 0){
					result = 0;
				} else if (dayF.compareTo(dayL) < 0){
					result = -1;
				}
			} else if (monthF.compareTo(monthL) < 0){
				result = -1;
			}
		} else if (yearF.compareTo(yearL) < 0){
			result = -1;
		}

		return result;
	}
}
