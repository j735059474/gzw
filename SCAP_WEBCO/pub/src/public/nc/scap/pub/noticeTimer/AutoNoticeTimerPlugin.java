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

		// ���ݱ������ͱ����Ƿ��ͷ�ʽ�Ƕ��ڣ����Ƿ����֪ͨ������֪ͨ���·�
		taskReportType();
		// ���ݱ������δ�ϱ��� ��֪ͨ���д߱�
		taskNoticeUndo();
		return null;
	}

	/**
	 * ���ݱ������ͱ����Ƿ��Զ��߱��������Ϣ ���д߱�
	 */
	private void taskReportType() {
		BaseDAO baseDAO = new BaseDAO();
		// �ȼ����������ͱ����ڱ�����Ҫ�߱��ı������� ������������������,�Ƿ����֪ͨ
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
			ScapLogger.error("������Ҫ�����ϱ��ı�������ʱ����" + e.getMessage());
		}

		// ��ǰϵͳʱ��
		UFDate sysDate = new UFDate();
		for (Work_report_type report_type_vo : work_report_typeList) {

			// ��ñ����ֹ���ڣ�������ӦԼ�����н���
			// �߱�ʱ���ʽԼ����[Y].[Q].[MM].[DD]����8λ���м���Ӣ�Ķ��������
			String expiration_date = report_type_vo.getExpiration_date();
			// ��ñ���Ƶ�� (�꣺1����:2����:3��Ѯ��4����:5)
			String submitFrecy = report_type_vo.getAttrname();
			// ������������UFDate���͵ı����ֹ����
			UFDate expiration_dateMake = getExpirationDate(expiration_date,
					submitFrecy, sysDate);
			
			// ������Ҫȷ�ϸñ��������Ƿ��Ѿ��ֶ�������
			Boolean isUnique = NoticeUtil.checkIsUnique(report_type_vo.getYe_type(), report_type_vo.getPk_work_report_type(), expiration_dateMake,AppConsts.OPE_ADD);
			if (Boolean.FALSE.equals(isUnique)) {
				continue;
			}
			// �߱���ǰ����(��)
			String urge_time = report_type_vo.getUrge_time();
			// �߱���ʼʱ��
			UFDate urgeStartDay = expiration_dateMake.getDateBefore(Integer
					.valueOf(urge_time));
			// �ձ���Ҫ���⴦��
			if ("5".equals(submitFrecy)) {
				urgeStartDay = sysDate;
				expiration_dateMake = expiration_dateMake.getDateAfter(Integer
						.valueOf(urge_time));
			}
			// ����Ѿ����˴߱���ʼʱ�䣬���Զ�����֪ͨ(���·�)
			if (compareDate(urgeStartDay,sysDate) == 0) {
				// �Զ����ñ���������ص���ҵ����ϵ���·�֪ͨ
				autoNoticeIssue(report_type_vo, expiration_dateMake);
			}
		}
	}

	/**
	 * ���ݱ������Ϊ�ϱ��� ��֪ͨ���д߱�
	 */
	private void taskNoticeUndo() {
		BaseDAO baseDAO = new BaseDAO();
		// ȡ���������·���֪ͨ
		List<Notice_manager> noticeList = new ArrayList<Notice_manager>();
		StringBuffer sqlNotice = new StringBuffer();
		sqlNotice
				.append(" SELECT a.* FROM ")
				.append(" SCAPCO_NOTICE_MANAGER a  ")
				.append(" where a.NOTICE_STATUS = '2' and a.IS_AUTO_URGE = '0' and a.expiration_date >= '" + new UFDate() + "'"); // ���·���֪ͨ
		try {
			noticeList = (List<Notice_manager>) baseDAO.executeQuery(sqlNotice
					.toString(), new BeanListProcessor(Notice_manager.class));
		} catch (DAOException e) {
			e.printStackTrace();
			ScapLogger.error("������δ�ϱ���֪ͨʱ����" + e.getMessage());
		}
		// ��ǰϵͳʱ��
		UFDate sysDate = new UFDate();
		for (Notice_manager notice_vo : noticeList) {
			// ���ݴ߱�Ƶ�ʣ�ȷ�ϸ�֪ͨ�Ƿ��˴߱�ʱ��
			UFDate expiration_date = notice_vo.getExpiration_date();
			// ��ǰ�߱�����
			Integer day_num = notice_vo.getDay_num();
			// ������߱���ʼʱ��
			UFDate urge_start_date = expiration_date.getDateBefore(day_num);
			// �߱�Ƶ�� ���δ߱�:1;ÿ��߱�:2;����߱�3��
			String urge_frequency = notice_vo.getUrge_frequency();
			// ϵͳʱ������ڴ߱���ʼʱ��ͱ����ֹʱ��֮��
			if (sysDate.compareTo(expiration_date) <= 0 && sysDate.compareTo(urge_start_date) >= 0) {
				// ���δ߱�
				if(IGlobalConstants.URGE_FREQUENCY_SINGLE.equals(urge_frequency)) {
					// ֻ�е�ϵͳʱ����ڴ߱���ʼʱ��ʱ ���ʹ߱���Ϣ.����ʱ�򲻷���
					if (compareDate(urge_start_date,sysDate) != 0) {
						continue;
					}
				// ÿ��߱�	
				} else if (IGlobalConstants.URGE_FREQUENCY_DAILY.equals(urge_frequency)) {
					// ÿ�춼�߱�
				// ����߱�	
				} else if (IGlobalConstants.URGE_FREQUENCY_GETIAN.equals(urge_frequency)) {
					// ��Ҫ�жϣ���ǰϵͳʱ���Ƿ�Ӵ߱���ʼʱ������Ļ��Ǹ��죻���ӣ�1��3��5
					int day = sysDate.getDaysAfter(urge_start_date);
					// �����������ż�����ǾͲ��Ǹ����ˣ��Ǻ�
					if (day%2!=0){
						continue;
					}
				}
			} else {
				continue;
			}
			// ���ù�ͬ���ܽ��д߱��������
			NoticeUtil.urgeByNoticePK(notice_vo.getPk_notice());
		}
	}

	/**
	 * �����·�֪ͨ
	 */
	private void autoNoticeIssue(Work_report_type report_type_vo,
			UFDate expiration_dateMake) {

		// ����֪ͨ��������
		Notice_manager notice_manager = new Notice_manager();
		String pk_primarykey = generatePk();
		// ��������
		notice_manager.setPk_notice(pk_primarykey);
		// ֪ͨ���
		notice_manager.setNotice_no(NoticeUtil.generateNoticeUID());
		// ֪ͨ����
		notice_manager.setNotice_style("1");// �֪ͨ
		// ֪ͨ����
		notice_manager.setNotice_title("ϵͳ�Զ�֪ͨ");
		// ҵ������
		notice_manager.setBusiness_type(report_type_vo.getYe_type());
		// ����ʵ������
		notice_manager.setNotice_type(report_type_vo.getPk_work_report_type());
		// ֪ͨ����
		String yeTypeName = NoticeUtil.getNameByBusinessType(report_type_vo.getYe_type());
		String notice_content = "���û������������á�����δ��ɡ�ҵ�����͡�-�������������͡�������¼ϵͳ�������"
				.replace("ҵ������", yeTypeName)
				.replace("������������", report_type_vo.getReport_type());
		notice_manager
				.setNotice_content(notice_content);
		// �Ƿ�������
		notice_manager.setIs_remind("0");
		// ���ѷ�ʽ
		notice_manager.setRemind_way("1");
		// ��������
		String remind_info = "���û������������á�����δ��ɡ�ҵ�����͡�-�������������͡�������¼ϵͳ�������"
				.replace("ҵ������", yeTypeName)
				.replace("������������", report_type_vo.getReport_type());
		notice_manager
				.setRemind_info(remind_info);
		// �Ƿ��Զ��߱�
		notice_manager.setIs_auto_urge(report_type_vo.getIs_auto_urge());
		// �߱���Ϣ���ͷ�ʽ
		notice_manager.setUrge_info_trans_way(report_type_vo.getUrge_info_trans_way());
		// �߱�Ƶ��
		notice_manager.setUrge_frequency(report_type_vo.getUrge_frequency());
		// ��ǰ�߱�����
		notice_manager
				.setDay_num(Integer.valueOf(report_type_vo.getUrge_time()));
		// ����ͳ�����ݸ�ʽ������ί�û���
		String urge_content_gzw= "��ֹ��Ŀǰ����ҵ�����͡�-���������͡��С�δ���ҵ����������ҵδ��������������ҵ����������ҵ������"
				.replace("ҵ������", yeTypeName)
				.replace("������������", report_type_vo.getReport_type());
		notice_manager
				.setUrge_content_gzw(urge_content_gzw);
		// �߱���Ϣ���ݸ�ʽ����ҵ�û���
		String urge_content_qy= "���û������������á�����δ��ɡ�ҵ�����͡�-�������������͡�������¼ϵͳ�������"
				.replace("ҵ������", yeTypeName)
				.replace("������������", report_type_vo.getReport_type());
		notice_manager
				.setUrge_content_qy(urge_content_qy);
		// ֪ͨ�·�״̬
		notice_manager.setNotice_status("2");// ���·�
		// �ϱ���ֹ����
		notice_manager.setExpiration_date(expiration_dateMake);
		// Ĭ��֪ͨ����
		notice_manager.setNotice_date(new UFDate());
		// ����֪ͨ��
		notice_manager.setNotice_psn("~");
		// ����֪ͨ��ҵ
		notice_manager.setNotice_send_org("~");
		try {
			// ��������
			ScapDAO.getBaseDAO().insertVOWithPK(notice_manager);
		} catch (DAOException e) {
			ScapLogger.error("���²������ִ��󣡴����쳣��" + e.getMessage());
		}
		// ��������ӱ�����
		List<notice_contact_info> receiceManListAll = new ArrayList<notice_contact_info>();
		List<String> pk_orgListAll = new ArrayList<String>();
		// ��ñ������ͻ�����շ�Χ�ӱ�ά���Ľ�����ҵ�ͽ�����֯
		// ������ҵ
		String report_body = report_type_vo.getReport_body();
		// ��ϵ�˼�������map
		Map<String, String> paramMap = new HashMap<String, String>();
    	paramMap.put(IGlobalConstants.PARAMMAP_KEY_BUSINESS_TYPE, report_type_vo.getYe_type());
    	paramMap.put(IGlobalConstants.PARAMMAP_KEY_REPORT_TYPE, report_type_vo.getPk_work_report_type());
    	paramMap.put(IGlobalConstants.PARAMMAP_KEY_REPORTBODY, report_body);

		// ���ݱ������͵ı������壬������Ӧ����
		if (report_body != null && ("1".equals(report_body) || "3".equals(report_body))) {
			List<String> listTemp = new ArrayList<String>();
			listTemp = NoticeUtil.getOrgsPKByPkWorkReportType(report_type_vo.getPk_work_report_type());
			pk_orgListAll.addAll(listTemp);
			String[] pk_orgArray = (String[])listTemp.toArray(new String[listTemp.size()]);
			Receive_notice receive_noticeArray[] = new Receive_notice[pk_orgArray.length];
			// ���������ҵ�ӱ���Ϣ
			for (int i = 0; i < pk_orgArray.length; i++) {
				Receive_notice receive_notice = new Receive_notice();
				String pk_org = pk_orgArray[i];
				// ����
				receive_notice.setPk_notice_org(generatePk());
				// ֪ͨ���� ����pk
				receive_notice.setPk_notice(pk_primarykey);
				// ������ҵ
				receive_notice.setNotice_org(pk_org);
				// �鿴״̬��δ�鿴��
				receive_notice.setReceive_status("1");
				// �ύ״̬��δ�ύ��
				receive_notice.setReport_status(IGlobalConstants.REPORT_STATUS_UNCOMMIT);
				receive_noticeArray[i] = receive_notice;
			}
			try {
				// �����ӱ�
				ScapDAO.getBaseDAO().insertVOArrayWithPK(receive_noticeArray);
			} catch (DAOException e) {
				ScapLogger.error("���²������ִ��󣡴����쳣��" + e.getMessage());
			}
			// ����������ӱ���Ϣ
			// ��ȡ��ϵ��
			List<notice_contact_info> receiceManListTemp = new ArrayList<notice_contact_info>();
			// ����ҵ�����ͺͱ�������ȥѰ����ϵ��
			receiceManListTemp = NoticeUtil.getContactsList(paramMap, pk_orgArray,Boolean.FALSE);
			Receive_man receiceManArray[] = new Receive_man[receiceManListTemp.size()];
			for (int i = 0; i < receiceManArray.length; i++) {
				Receive_man receive_man = new Receive_man();
				// ����
				receive_man.setPk_receive_man(generatePk());
				// ֪ͨ����
				receive_man.setPk_notice(pk_primarykey);
				// ������
				receive_man.setReceive_man(receiceManListTemp.get(i).getPk_user());
				// ���յ�λ
				receive_man.setReceive_org(receiceManListTemp.get(i).getPk_org());
				// // �Ա�
				// receive_man.setSex(receiceManList.get(i).get);
				// �绰����
				receive_man.setTelephone(receiceManListTemp.get(i).getPhone_no());
				// Email
				receive_man.setEmail(receiceManListTemp.get(i).getEmail());
				// �鿴״̬(δ�鿴)
				receive_man.setReceive_status("1");
				// �ϱ�״̬(δ�ύ)
				receive_man.setReport_status(IGlobalConstants.REPORT_STATUS_UNCOMMIT);
				receiceManArray[i] = receive_man;
			}
			try {
				// �����ӱ�
				ScapDAO.getBaseDAO().insertVOArrayWithPK(receiceManArray);
			} catch (DAOException e) {
				ScapLogger.error("���²������ִ��󣡴����쳣��" + e.getMessage());
			}
		}
		// ���ݱ������͵ı������壬������Ӧ����
		if (report_body != null && ("2".equals(report_body) || "3".equals(report_body))) {
			List<String> listTemp = new ArrayList<String>();
			listTemp = NoticeUtil.getVisualOrgsPKByPkWorkReportType(report_type_vo.getPk_work_report_type());
			String[] pk_VisualOrgArray = (String[])listTemp.toArray(new String[listTemp.size()]);
			Receive_notice receive_noticeArray[] = new Receive_notice[pk_VisualOrgArray.length];
			// ���������ҵ�ӱ���Ϣ
			for (int i = 0; i < pk_VisualOrgArray.length; i++) {
				Receive_notice receive_notice = new Receive_notice();
				String pk_VisualOrg = pk_VisualOrgArray[i];
				// ����
				receive_notice.setPk_notice_org(generatePk());
				// ֪ͨ���� ����pk
				receive_notice.setPk_notice(pk_primarykey);
				// ������֯
				receive_notice.setNotice_visual_org(pk_VisualOrg);
				// �鿴״̬��δ�鿴��
				receive_notice.setReceive_status("1");
				// �ύ״̬��δ�ύ��
				receive_notice.setReport_status(IGlobalConstants.REPORT_STATUS_UNCOMMIT);
				receive_noticeArray[i] = receive_notice;
			}
			try {
				// �����ӱ�
				ScapDAO.getBaseDAO().insertVOArrayWithPK(receive_noticeArray);
			} catch (DAOException e) {
				ScapLogger.error("���²������ִ��󣡴����쳣��" + e.getMessage());
			}
			// ����������ӱ���Ϣ
			// ��ȡ��ϵ��
			// ����ҵ�����ͺͱ�������ȥѰ����ϵ��
			List<notice_contact_info> receiceManListTemp = new ArrayList<notice_contact_info>();
			receiceManListTemp = NoticeUtil.getContactsList(paramMap, pk_VisualOrgArray,Boolean.FALSE);
			Receive_man receiceManArray[] = new Receive_man[receiceManListTemp.size()];
			for (int i = 0; i < receiceManArray.length; i++) {
				Receive_man receive_man = new Receive_man();
				// ����
				receive_man.setPk_receive_man(generatePk());
				// ֪ͨ����
				receive_man.setPk_notice(pk_primarykey);
				// ������
				receive_man.setReceive_man(receiceManListTemp.get(i).getPk_user());
				// ������֯
				receive_man.setReceive_visual_org(receiceManListTemp.get(i).getPk_visualorg());
				// // �Ա�
				// receive_man.setSex(receiceManList.get(i).get);
				// �绰����
				receive_man.setTelephone(receiceManListTemp.get(i).getPhone_no());
				// Email
				receive_man.setEmail(receiceManListTemp.get(i).getEmail());
				// �鿴״̬(δ�鿴)
				receive_man.setReceive_status("1");
				// �ϱ�״̬(δ�ύ)
				receive_man.setReport_status(IGlobalConstants.REPORT_STATUS_UNCOMMIT);
				receiceManArray[i] = receive_man;
			}
			try {
				// �����ӱ�
				ScapDAO.getBaseDAO().insertVOArrayWithPK(receiceManArray);
			} catch (DAOException e) {
				ScapLogger.error("���²������ִ��󣡴����쳣��" + e.getMessage());
			}
		}
		// ���ù�ͬ�����ѹ���
		// ֪ͨ�·���������
		// �Զ����ɵı���Ĭ��ƽ̨�����Ŷ�����
		NoticeUtil.remindByNoticePK(pk_primarykey);
	}

	/**
	 * ȡ�ñ����ֹʱ��ufdate��
	 */
	private UFDate getExpirationDate(String expiration_date,
			String submitFrecy, UFDate sysDate) {

		// ʹ��calendar����������,�����õ�ufdate��ȥ
		Calendar calendar = Calendar.getInstance();
		String[] values = expiration_date.split("\\.");
		// ȡ��Լ����ʽ�� ���꡿ �Ĳ���
		String year = values[0];
		// ȡ��Լ����ʽ�� ������ �Ĳ���
		String quarter = values[1];
		// ȡ��Լ����ʽ�� ���¡� �Ĳ���
		String month = values[2];
		// ȡ��Լ����ʽ�� ���ա� �Ĳ���
		String day = values[3];
		if (values.length != 4) {
			ScapLogger.error("�������ڸ�ʽ���ԣ���ȷ��!");
		}
		// �걨
		if ("1".equals(submitFrecy)) {
			// ��ʾÿ�궼��
			if ("Y".equals(year)) {
				// ʹ��calendar����������
				calendar.set(sysDate.getYear(), Integer.valueOf(month) - 1,
						Integer.valueOf(day));
				// �����ǰϵͳʱ����������趨�ı����ֹʱ��������� ��ʱ��˵�������Ե�ǰ���¼�����һ���걨ʱ�䣬��Ҫ������һ���걨ʱ��
				if (compareDate(sysDate,new UFDate(calendar.getTime())) == 1) {
					calendar.set(sysDate.getYear() + 1, Integer.valueOf(month) - 1,
							Integer.valueOf(day));
				}
			}
			// ����
		} else if ("2".equals(submitFrecy)) {
			// ��ʾÿ�����ϱ�
			if ("Q".equals(quarter)) {
				Integer FirstQMonth = getFirstQMonthByMonth(sysDate.getMonth());
				// ʹ��calendar����������
				calendar.set(sysDate.getYear(),
						FirstQMonth + Integer.valueOf(month.substring(1)) - 1 - 1,
						Integer.valueOf(day));
				// �����ǰϵͳʱ����������趨�ı����ֹʱ��������� ��ʱ��˵�������Ե�ǰ���¼�����һ�μ���ʱ�䣬��Ҫ��������һ�μ���ʱ��
				if (compareDate(sysDate,new UFDate(calendar.getTime())) == 1) {
					calendar.add(Calendar.MONTH, 3);
				} 
			}

			// �±�
		} else if ("3".equals(submitFrecy)) {
			// ��ʾÿ���ϱ�
			if ("MM".equals(month)) {
				// ʹ��calendar����������
				// �����·ݵ�ʱ�� ��Ҫ���⴦�������ǰϵͳʱ��"��"�ֶε�ֵ ���� �趨��"��"�ֶε�ֵ��Ӧ���·�����Ϊϵͳ��ǰ�·ݵ��¸��£�
				// ��Ϊʲô��ô������Ϊ��������óɵ�ǰ�·ݵĻ� ����Щ���⴦����
				// ���� ������4��1�� ��ô�ĳ��Ľ�ֹ���ھ���4��5�ţ������ǰ�߱�������5�Ļ� �����֪ͨ�·����ںʹ߱���ʼ���ھ���3��31�š�
				// ��������4��1�ţ��ͷ��������֪ͨ�ˡ�Ϊ�˴������֣������·����ڼ�������ʹ�ϸ��µ����ڵ�������Ը�Ϊ���ڵĴ���
				// ��������Ѯ���걨Ҳ�����ƴ���
				calendar.set(sysDate.getYear(), sysDate.getMonth() - 1,
						Integer.valueOf(day));
				// �����ǰϵͳʱ����������趨�ı����ֹʱ��������� ��ʱ��˵�������Ե�ǰ���¼�����һ���±�ʱ�䣬��Ҫ��������һ���±�ʱ��
				if (compareDate(sysDate,new UFDate(calendar.getTime())) == 1) {
					calendar.add(Calendar.MONTH, 1);
				} 
			}
			// Ѯ��
		} else if ("4".equals(submitFrecy)) {
			// ��ʾÿ����Ѯ�ϱ�
			if ("D1".equals(day)) {
				// ʹ��calendar����������
				calendar.set(sysDate.getYear(), sysDate.getMonth() - 1,
						Integer.valueOf("1"));
				// ���óɱ�����Ѯ
				if (compareDate(sysDate,new UFDate(calendar.getTime())) == 1) {
					calendar.set(sysDate.getYear(), sysDate.getMonth() - 1,
							Integer.valueOf("11"));
				}
				// ��ʾÿ����Ѯ�ϱ�
			} else if ("D2".equals(day)) {
				// ʹ��calendar����������
				calendar.set(sysDate.getYear(), sysDate.getMonth() - 1,
						Integer.valueOf("11"));
				// ����Ϊ������Ѯ
				if (compareDate(sysDate,new UFDate(calendar.getTime())) == 1) {
					calendar.set(sysDate.getYear(), sysDate.getMonth() - 1,
							Integer.valueOf("21"));
				}
				// ��ʾÿ����Ѯ�ϱ�
			} else if ("D3".equals(day)) {
				// ʹ��calendar����������
				calendar.set(sysDate.getYear(), sysDate.getMonth() - 1,
						Integer.valueOf("21"));
				// ����Ϊ�¸��µ���Ѯ
				if (compareDate(sysDate,new UFDate(calendar.getTime())) == 1) {
					calendar.set(sysDate.getYear(), sysDate.getMonth() - 1,Integer.valueOf("1"));
					calendar.add(Calendar.MONTH, 1);
				}
			}
			// �ձ�
		} else if ("5".equals(submitFrecy)) {
			// ��ʾÿ���ϱ�
			if ("DD".equals(day)) {
				// ʹ��calendar����������
				calendar.set(sysDate.getYear(), sysDate.getMonth() - 1,
						sysDate.getDay());
			}
		}
		UFDate result = new UFDate(calendar.getTime());
		return result;
	}

	/**
	 * ���ݱ������Ϊ�ϱ��� ��֪ͨ���д߱�
	 */
	private Integer getFirstQMonthByMonth(Integer month) {
		Integer result = Integer.valueOf(1);
		// ��һ����
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
	 * ͨ���������ͻ�ȡ��ϵ�������͵绰����
	 * 
	 * @param besinss_type����ҵ������
	 * @param notice_type����ʵ������
	 * @param pk_orgArray������ҵ����
	 */
	private Map<String, String> getContactsMap(String besinss_type,
			String notice_type, String[] pk_orgArray) {
		Map<String, String> receivers = new HashMap<String, String>();
		try {
			// ����������ҵ��������
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
	 * ����PKֵ
	 * 
	 * @return
	 */
	protected String generatePk() {
		String datasource = LfwRuntimeEnvironment.getDatasource();
		return PtBaseDAO.generatePK(datasource);
	}
	/**
	 * ͨ���ж��������ֶ��Ƿ���ͬ���ж������Ƿ���ͬ
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
