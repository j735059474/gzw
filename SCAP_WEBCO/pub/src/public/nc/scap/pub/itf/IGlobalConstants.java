package nc.scap.pub.itf;

public interface IGlobalConstants {

	// ���λ���ո���ģ����������̬����д�ڸ�ģ���Ӧ�Ĵ�����У���������д
	// ��������˫б�ܡ�//��ע�ͣ�������Ҫ�ö���ע��

	/**
	 * ͨ��ģ��SCAP_WEBCO��̬�������忪ʼ
	 */
	public static final String PROJECT_NAME = "����ί��Ϣ��ϵͳ";
	public static final String LICENSE_NAME = "licensegz.lic";//��Ȩ�ļ�����
	public static final String MD5_KEY ="md5Str";//������Կ
	public static final String NODE_KEY ="node";//���ܽڵ�
	public static final String DATE_KEY = "date";//���ܵ�������
	// ��ť����
	public static final String BTN_ADD = "add";// ������ť
	public static final String BTN_EDIT = "edit";// �༭��ť
	public static final String BTN_DEL = "del";// ɾ����ť
	public static final String BTN_ATTACHFILE = "attachfile";// ������ť
	public static final String BTN_COPY = "copy"; // ���ư�ť
	public static final String BTN_SAVE = "save"; // ���水ť
	public static final String BTN_STOP = "stop"; // ��ӡ��ť
	public static final String BTN_BACK = "back"; // ���ذ�ť
	public static final String BTN_AUDIT = "audit"; // ��˰�ť
	public static final String BTN_RECALL = "recall"; // �ջذ�ť
	public static final String BTN_APPROVE = "approve"; // ��˰�ť
	public static final String BTN_WF = "wf"; // ���̰�ť
	public static final String BTN_SYNCHRO = "synchro";// ͬ����ť
	public static final String BTN_CANCLE = "cancle";// ������ť
	public static final String BTN_LOOK = "look";// �鿴��ť
	public static final String BTN_PRINT = "print";// ��ӡ��ť
	public static final String BTN_STARTORSTROP = "startorstrop";// ����ͣ�ð�ť
	// public static final String BTN_EDITAPPLY="editapply";//
	public static final String BTN_APPLYEDIT = "applyedit";// �޶��޸�
	public static final String BTN_SUBMITDOUBLE = "submitdouble";// �����ύ
	public static final String BTN_HISTORY = "history"; // ��ʷ�汾
	public static final String BTN_SUBMIT = "submit"; // �ύ
	public static final String BTN_DETAIL = "detail"; // ��ϸ��Ϣ
	public static final String BTN_WRITE = "write";
	public static final String BTN_ASSIGN = "assign";//���䰴ť
	public static final String AUDIT_FLAG = "audit_flag"; // �Ƿ����flag

	// �������
	public static String MENUBAR = "menubar";
	public static String MAIN = "main";
	public final static String OPERATIONCMD = "operation";
	public final static String GZWMANAGER = "gzwmanager"; // ����ί����
	public final static String GZWLOOK = "gzwlook";// ����ί�鿴
	public final static String GZWAPPROVE = "gzwapprove";// ����ί���
	public final static String QYMANAGER = "qymanager";// ��ҵ����

	public final static String MODULE_CODE = "module";// ģ�����
	public final static String SCAP_GZW = "gzw";// ֪ͨ�����ڵ�
	public final static String SCAP_QY = "qy";// ֪ͨ�����ڵ�

	// ģ������ �ϴ� ����
	public static final String NDXS_TEMPLATE = "scapfop01";// �Ѷ�ϵ������ģ��
	public static final String YJKH_TEMPLATE = "scapfop02";// ҵ�����˷���ģ��
	// ����
	public static final String ATTACH_PARAM = "attach_param";
	public static final String ATTACH_BILL_ID = "attach_bill_id";
	public static final String ATTACH_BILL_TYPE = "attach_bill_type";
	public static final String ATTACH_CAN_UPLOAD = "attach_can_upload";
	public static final String ATTACH_CAN_DOWNLOAD = "attach_can_download";
	public static final String ATTACH_CAN_EDIT = "attach_can_edit";
	public static final String ATTACH_CAN_DEL = "attach_can_del";

	// ��Ϣ�ϳ���崰��ID������
	public static final String INFO_COMPOSE_WIN_ID = "com.scap.pub.infoCompose.infoCompose_win";
	public static final String INFO_COMPOSE_WIN_NAME = "��Ϣ�ϳ����";
	public static final String MAIN_BILL_ID = "main_bill_id";

	// ����ά��������
	public static final String CENTRALLY_MAINTAINED_WIN_ID = "com.scap.pub.centrallyMaintained.centrallyMaintained_listwin";
	public static final String CENTRALLY_MAINTAINED_WIN_NAME = "����ά�����";
	public static final String MAIN_BILL_IDS = "main_bill_ids";
	public static final String CURRENT��DATASET_ID = "current_dataset_id";
	public static final String[][] MAINTAINED_TYPE_ARR = {
			{ "psndoc", "��Ա������Ϣ" }, { "psndoc_spetech", "רҵ�˲���Ϣ" },
			{ "psndoc_abroad", "���������" }, { "psndoc_family", "��ͥ������ϵ" },
			{ "psndoc_training", "��ѵ���" }, { "psndoc_reserve", "�󱸸ɲ��������" },
			{ "psndoc_work", "��������" }, { "psndoc_edu", "ѧ��ѧλ" },
			{ "psndoc_enc", "�������" }, { "psndoc_ass", "��ȿ��˽��" },
			{ "psndoc_duty", "�������" }, { "psndoc_part", "��ְ���" },
			{ "psndoc_letter", "�ŷ����" } }; // [datasetId, gridId, ��ʾ����]
	public static final String IS_DEFDOCLIST_SELECTED = "is_defdoclist_selected";
	public static final String XML_VALUE = "xml_value";

	/** ��ʱͨѶר�ñ��� */

	public static final String DOMAIN_NAME = "���ʼ���";
	public static final String ROOT_PK_ORG = "0001GZ10000000000IQY"; // ������pk(Ĭ��Ϊ����ί)
	/**
	 * ͨ��ģ��SCAP_WEBCO��̬�����������
	 */

	/**
	 * ���¹���ģ��SCAP_WEBPM��̬�������忪ʼ
	 */
	public static final String DELAULT_PHOTO_PATH = "/portal/touxiang.jpg";// Ĭ��ͷ��·��
	public static final String PHOTO_PATH = "/portal/sync/scappm/psnm/html/photo";// ͼƬ���·��
	public static final String JJPHOTO_PATH = "/portal/sync/scapjj/jjim/html/photo";// �ͼ����쵼�˵���ͼƬ���·��
	public static final String PSNTYPE_ZGRY = "9"; // ��Ա���:������Ա
	public static final String PSNTYPE_QTRY = "8"; // ��Ա���:������Ҫ��Ա
	public static final String PSNTYPE_WBDS = "7"; // ��Ա���:�ⲿ����
	public static final String PSNTYPE_JGGB = "6"; // ��Ա���:���ظɲ�
	public static final String PSNTYPE_QYLD = "5"; // ��Ա���:��ҵ�쵼��
	public static final String PSNTYPE_HBGB = "4"; // ��Ա���:�󱸸ɲ�
	public static final String PSNTYPE_QYPTRY = "3";// ��Ա���:��ҵ�˲�(��ͨ��Ա)
	public static final String PSNTYPE_TXGB = "2"; // ��Ա���:���ݸɲ�
	public static final String PSNTYPE_QT = "1"; // ��Ա���:����

	public static final String PSNOUTDIRECTORS_YES = "Y";// �ⲿ����
	public static final String PSNOUTDIRECTORS_NO = "N";// ���ⲿ����

	// �������ܳ���
	public static final String OPEN_REPORT_CONDITION = "'xx',' left=0,top=0,width='+ (screen.availWidth - 10) +',height='+ (screen.availHeight-50) +',scrollbars=no,resizable=no,toolbar=no,location=no'"; // �򿪱���JS
	// �����̵�����״̬
	final static String SCAPPM_APPROVESTATE_ADD = "1";// ����
	final static String SCAPPM_APPROVESTATE_APPROVING = "2";// ������
	final static String SCAPPM_APPROVESTATE_APPROVED = "3";// ������
	final static String SCAPPM_APPROVESTATE_CANCELLATION = "4";// ����
	// �������̵�����״̬
	final static String SCAPPM_NOAPPROVESTATE_ADD = "1";// ����
	final static String SCAPPM_APPROVESTATE_SAVED = "2";// �ѱ���
	final static String SCAPPM_APPROVESTATE_SUBMIT = "3";// ���ύ
	final static String SCAPPM_NOAPPROVESTATE_APPROVED = "4";// ������
	final static String SCAPPM_NOAPPROVESTATE_CANCELLATION = "5";// ����

	// ѡƸ��������״̬
	final static String SCAPTCS_STATE_ADD = "01";// ����
	final static String SCAPTCS_STATE_SAVED = "02";// �ѱ���
	final static String SCAPTCS_STATE_SUBMIT = "03";// ���ύ
	final static String SCAPTCS_STATE_APPROVED = "04";// ������
	final static String SCAPTCS_STATE_RECORD = "05";// �ѱ���
	final static String SCAPTCS_STATE_EDITAPPLY = "06";// �޶���(�ѱ���)
	final static String SCAPTCS_STATE_EDITING = "07";// �޶��У���������

	final static String SCAPTCS_TATE_CANCELLATION = "08";// ����

	// ѡƸѡ�η�ʽ
	final static String SCAPTCS_XBFS_JZSG = "�����ϸ�";
	final static String SCAPTCS_XBFS_GKXB = "����ѡ��";
	final static String SCAPTCS_XBFS_GBTB = "�������";

	// ����״̬
	final static String SCAPPM_BILLSTATE_NOTTSTART = "NottStart";// ����̬
	final static String SCAPPM_BILLSTATE_RUN = "Run";// ����
	final static String SCAPPM_BILLSTATE_END = "End";// ����
	final static String SCAPPM_BILLSTATE_CANCELLATION = "Cancellation";// ����
	final static String SCAPPM_BILLSTATE_SUSPENDED = "Suspended";// ����
	// ��Ա״̬
	public static final String SCAPPM_PSNCONDITION_WORK = "01"; // ��ְ
	public static final String SCAPPM_PSNCONDITION_RERIRED = "02"; // ������
	public static final String SCAPPM_PSNCONDITION_RESIGNATION = "03"; // ��ְ
	public static final String SCAPPM_PSNCONDITION_BORROW = "04"; // ����
	public static final String SCAPPM_PSNCONDITION_DIE = "05"; // ȥ��
	// ����/ͣ��
	public static final String STRAT = "02"; // ����
	public static final String STOP = "03"; // ͣ��
	// Ĭ��֤������Ϊ����֤
	public static final String SCAPPM_ZJTYPE = "0";

	final static String NODE_TYPE = "node_type";// �ڵ����
	final static String METHOD_TYPE = "method_type";// ��������
	final static String MODEL = "model";//

	final static String NODE_TYPE_LEADER = "1";// ��ҵ�쵼��
	final static String NODE_TYPE_CADRE = "2";// ���ظɲ�
	final static String NODE_TYPE_CADRE_AUDIT = "cadre_audit";// ���ظɲ����
	final static String NODE_TYPE_APPLY = "apply";// ����
	final static String NODE_TYPE_AUDIT = "audit";// ���

	final static String RM_TYPE_XR = "1";// ����
	final static String RM_TYPE_NR = "2";// ����
	final static String RM_TYPE_NM = "3";// ����
	final static String RM_TYPE_BR = "4";// ����

	final static String CHINA = "0001Z010000000079UJJ";// �й�
	final static String HANZU = "1001AA10000000000430";// ����
	final static String DANGYUAN = "1001AA1000000000048O";// ��Ա
	final static String TUANYUAN = "1001AA1000000000048P";// ��Բ
	final static String QUNZHONG = "1001AA1000000000048Q";// Ⱥ��
	final static String QYLDRLX = "1001AA100000000004CX";// ��ҵ�쵼����Ա����
	final static String QRZJY = "1001AA1000000000049X";// ȫ���ƽ���(��������)
	final static String ZZJY = "1001AA1000000000049Y";// ��ְ����(��������)
	final static String XL_QT = "1001AA100000000003YZ"; // ������ѧ����
	// ��ҵ�쵼���޸ĺͲ鿴��ť�������峣��
	public static final String COMLEADER_BTN_TYPE = "comleader_btn_type"; // ��ť����
	public static final String COMLEADER_EDIT = "edit";
	public static final String COMLEADER_VIEW = "view";
	public static final String PSNADU_AUDIT_ID = "E9A30209";
	public static final String PSNADU_AUDIT_TYPECODE = "psnm_psnadu";
	/**
	 * ���¹���ģ��SCAP_WEBPM��̬�����������
	 */

	/**
	 * ��Ŀ����ģ��SCAP_WEBIPM��̬�������忪ʼ
	 */
	public static final String BTN_IPM_YEAR_PLAN_ADJUST = "plan_adjust";// ��ȼƻ�����
	public static final String PROJECT_WHERESQL = "projectsql";// ��ȼƻ�����

	/**
	 * ��Ŀ����ģ��SCAP_WEBIPM��̬�����������
	 */

	/**
	 * �������ģ��SCAP_WEBFD��̬�������忪ʼ
	 */
	public static final String PRO_SAC = ""; // ʡ����ί
	public static final String PRO_COMPANY = ""; // ʡ����ҵ
	public static final String PRO_SAC_COMPANY = ""; // ʡ����ίʡ�������ҵ
	public static final String PRO_SAC_OTHER_COMPANY = ""; // ʡ����ί���������ҵ
	public static final String CITY_COUNTY_COMPANY = ""; // ���ؼ����ҵ
	public static final String CITY_COMPANY = ""; // ���м������ҵ
	public static final String COUNT_COMPANY = "";// ���ؼ������ҵ

	// ���������Զ��嵵��
	final static String TASK_NODE_TYPE = "GZ050_0xxx";
	final static String TASK_COMMIT_STATUS = "GZ051_0xxx";
	// ��������
	final static String NODE_TYPE_YBS = "01";// ȫ��Ԥ��-Ԥ���� ���
	final static String NODE_TYPE_YSS = "02";// ȫ��Ԥ��-Ԥ���� ���
	final static String NODE_TYPE_YSTZS = "03";// ȫ��Ԥ��-Ԥ������� ���
	final static String NODE_TYPE_KBSJ = "04";// �챨���� �¶�
	final static String NODE_TYPE_JJYXXB = "05";// ��������Ѯ�� Ѯ
	final static String NODE_TYPE_JSSJ = "06";// �������� ���
	final static String NODE_TYPE_GZTJSJ = "07";// �����ʲ�ͳ������ ���
	// ����״̬

	final static String RW_ALL = "-1";// ȫ��
	final static String RW_SUBMITED = "1";// ���ύ
	final static String RW_NOSUBMIT = "2";// δ�ύ
	final static String RW_APPLY_BACK = "3";// �����˻�
	final static String RW_NOSEND = "4";// δ�·�
	final static String RW_SENDED = "5";// ���·�
	final static String RW_DOWNLOADED = "6";// ������

	public static final String GZW_PK_ORG = "0001A110000000000JCX"; // ����ίpk_org
	public static final String GZWZS_PK_ORG = "0001A31000000003S2CD"; // ����ʡ����ίpk_org
	public static final String GZJG_PK_ORG = "0001A11000000002A41P"; // ���ʼ��pk_org
	public static final String GZW_PK_ORG_ZJ = "0001AZ10000000000CNQ";

	public static final String HYDB = "1001AA100001100003YZ"; // //��ҵ���������ڵ�

	// ָ������
	public static final String ZB_TYPE_SC = "1"; // ������
	public static final String ZB_TYPE_FZ = "2"; // ��չ��

	// Ԥ������(������ֵ����)
	public static final String SC_WARNING = "1"; // ����Ԥ��
	public static final String CD_WARNING = "2"; // �嶯Ԥ��
	public static final String XJ_WARNING = "3"; // ����Ԥ��
	public static final String SF_WARNING = "4"; // ˫��Ԥ��
	public static final String CHENGDU_WARNING = "5";// �̶�Ԥ��
	public static final String QS_WARNING = "6"; // ����Ԥ��
	public static final String BD_WARNING = "7"; // ����Ԥ��

	// ҵ���ж�
	public static final String YES = "0"; // ��
	public static final String NO = "1"; // ��

	// ��Ŀ����
	public static final String AH = "AH"; // ����
	public static final String HN = "HN"; // ����
	public static final String XJ = "XJ"; // �½�
	public static final String SC = "SC"; // �Ĵ�
	public static final String ZJ = "ZJ"; // �Ĵ�
	public static final String FJ = "FJ"; // ����

	// ������������
	public static final String KB = "1"; // �챨
	public static final String YS = "2"; // Ԥ��
	public static final String Js = "3"; // ����
	public static final String GYZC = "4"; // �����ʲ�
	public static final String JXPJ = "5"; // ��Ч����

	/**
	 * �������ģ��SCAP_WEBFD��̬�����������
	 */

	/**
	 * �������ģ��SCAP_WEBAN��̬�������忪ʼ
	 */
	public static String MG_PK = "150509144"; // ����
	public static String WBMD_PK = "152388171"; // �ú��
	public static String HNKY_PK = "150230004"; // ���Ͽ�ҵ
	public static String HBKY_PK = "150820039"; // ������ҵ
	public static String HLJT_PK = "149492322"; // ���ݼ���
	public static String JQJT_PK = "148975605"; // ��������
	public static String CCJT_PK = "148943240"; // �泵����
	public static String JGJT_PK = "148940170"; // ��������
	public static String ZMKJJT_PK = "670904113"; // ��ú�󽨼���
	public static String TLJT_PK = "151105774"; // ͭ����ɫ����
	public static String WWJT_PK = "153580560"; // ��ά����
	public static String NYJT_PK = "148941608"; // ��Դ����
	public static String SMQY_PK = "444444444"; // ��ó��ҵ
	public static String TZJJRLQY_PK = "555555555"; // Ͷ�ʼ���������ҵ
	public static String GZW_PK = "HZQYB0014"; // ����ί����
	public static String GZWSS_PK = "HZQYB0025"; // ����ίʡ������
	public static String GZWSX_PK = "C22222222"; // ����ί���ػ���
	public static String GZW_PK_JS = "999999999"; // ����ί����
	public static String GZW_PK_KB = "000000025"; // ����ί���Ͽ챨����
	public static String GZW12_PK = "HZQYB00257"; // ����12��ʡ��pk
	public static String GZW13_PK = "75436138X7"; // ����13��ʡ��pk
	// �Ĵ�ʡ��֯������
	public static String SCKB_GZW_PK = "000000028"; // ����ί����
	public static String SCKB_GZWJG_PK = "SC0000001"; // ʡ�����PK
	public static String SCYS_GZWJG_PK = "SC00000167"; // ʡ�����PK

	/*
	 * ���Ϲ�˾pk
	 */
	public static String HNFZKG = "767474690";// ����ʡ��չ�ع����޹�˾
	public static String HNHGJT = "201244001";// ���Ϻ��ּ��Ź�˾
	public static String HNJSJT = "698925151";// ����ʡ���輯�����޹�˾
	public static String HNGSGL = "284082887";// ���ϸ��ٹ�·�ɷ����޹�˾
	public static String HNSLDL = "760394356";// ����ʡˮ�������������޹�˾
	public static String HNLQTZ = "780749062";// ����ʡ·��Ͷ�ʽ������޹�˾
	public static String HNHNJJ = "201486713";// ���Ϻ������÷�չ�ܹ�˾
	public static String HNLHZC = "747784800";// ���������ʲ�������˾
	public static String HNXHSD = "798747452";// ����ʡ�»���꼯�����޹�˾
	public static String HNWHTZ = "730046924";// ����ʡ�Ļ�Ͷ�ʹ������޹�˾
	public static String HNHXWHTZ = "573051278";// ���ϻ����Ļ�Ͷ�ʹ������޹�˾
	public static String HNHYTZ = "713809723";// ���ϻ�ӯͶ�ʿع����޹�˾
	public static String HNNHYY = "793132408";// ����ʡ�Ϻ��ִ���ҵ�������޹�˾
	public static String HNCQJY = "767456441";// ���ϲ�Ȩ���������޹�˾
	public static String HNJYQY = "747761844";// ����ʡ������ҵ�����������ι�˾
	public static String HNJLTZ = "573066402";// ����ʡ����Ͷ�����޹�˾
	public static String HNSJTTZ = "578725200";// ���Ϻ���Ͷ�ʿع����޹�˾
	public static String HNHQTZ = "665137976";// ����ʡ��ͨͶ�ʿع����޹�˾
	public static String HNXSMY = "56797982X";// ��������ó�����޹�˾
	public static String HNGJLYD = "562403880";// ���Ϲ������ε������������޹�˾
	public static String HNLSXT = "101010102";// ����ʡ��ʳϵͳ��ҵ
	/*
	 * ������˾pk
	 */
	public static String FJQY = "290000000";// ������ҵ
	public static String FJSYHG = "158166801";// ����ʯ�ͻ��������������ι�˾
	public static String FJJTYS = "733600839";// ����ʡ��ͨ���伯���������ι�˾
	public static String FJYJKG = "158145023";// ����ʡұ��(�ع�)�������ι�˾(����)
	public static String FJCBGY = "158155897";// ����ʡ������ҵ���Ź�˾���ϲ���
	public static String FJQCGY = "158142690";// ����ʡ������ҵ�������޹�˾���ϲ���
	public static String FJXMJM = "260105714";// �������ž�ó�������޹�˾���ϲ���
	public static String FJJGJT = "158143183";// �������������ܹ�˾
	public static String FJXMGJ = "612017727";// ���Ź������У�ģ��ϲ���
	public static String FJWMZX = "158147643";// �й�����������ó���ļ��ţ��ϲ� ��
	public static String FJHMSY = "777544015";// ��������ʵҵ�����ţ����޹�˾���ϲ���
	public static String FJGSGL = "158165606";// ����ʡ���ٹ�·�������ι�˾(�ϲ�)
	public static String FJZLJT = "158142375";// �������ü��Ź�˾��������
	public static String FJDZXX = "717397615";// ����ʡ������Ϣ(����)�������ι�˾���ϲ���
	public static String FJJDSB = "158141735";// ����ʡ�����豸�б깫˾���ϲ���
	public static String FJQFKG = "003591811";// ����ʡ���(�ع�)�������ι�˾
	public static String FJNYJT = "003592267";// ����ʡ��Դ�����������ι�˾(�ϲ�)
	public static String FJTZKF = "68753848X";// ����ʡͶ�ʿ��������������ι�˾���ϲ���
	public static String FJGYZC = "782188289";// ����ʡ�����ʲ��������޹�˾���ϲ���
	public static String FJJDKG = "72644687X";// ����ʡ���磨�عɣ��������ι�˾
	public static String FJDS = "888888888";// ����ʡ������ҵ
	/*
	 * ��������pk
	 */
	public static String FJFZS = "003604934";// ������
	public static String FJXMS = "000000016";// ������
	public static String FJZZS = "350600000";// ������
	public static String FJQZS = "784506762";// Ȫ����
	public static String FJSMS = "000000000";// ������
	public static String FJPTS = "784528021";// ������
	public static String FJNPS = "350702000";// ��ƽ��
	public static String FJLYS = "350800000";// ������
	public static String FJNDS = "111111111";// ������
	public static String FJPTZH = "PTSYCZJRJ";// ƽ̶�ۺ�ʵ����

	/**
	 * �������ģ��SCAP_WEBAN��̬�����������
	 */

	/**
	 * ���ģ��SCAP_WEBAN��̬�������忪ʼ
	 */
	public static final String PK_ORG = "pk_org";// ��ҵ�ṹ����������
	public static final String PK_ORG_NAME = "pk_org_name";// ��ҵ�ṹ����������
	public static final String AUDITYEAR = "audityear";// ��Ƚṹ����������
	public static final String PK_PARTNER = "pk_partner";// �н�ṹ����������
	public static final String CURRENTYEAR = "currentyear";// ��ǰ��Ƚṹ����������
	public static final String ENTRUST_ID = "entrustID";// ���ί������
	public static final int ISSUESTATE_USED = 1; // �ѷ���
	public static final int ISSUESTATE_UNUSED = 0; // δ����

	/**
	 * ���ģ��SCAP_WEBAN��̬�����������
	 */

	/**
	 * ����ƽ̨SCAP_WEBFP��̬�������忪ʼ
	 */
	public static final String PK_PARENT_MEASURE = "pk_parent_measure";
	public static final String MEASURE_CODE = "measure_code";
	public static final String MEASURE_CATEGORY = "measure_category";
	public static final String MEASURE_TYPE = "measure_type";
	/**
	 * ����ƽ̨SCAP_WEBFP��̬�����������
	 */

	public static final int RISK_QUOTA_MAX_VALUE = 33;

	public static final int RISK_QUOTA_MIN_VALUE = 25;

	/**
	 * ���»�ģ��SCAP_WEBBOS��̬�������忪ʼ
	 */
	public static final String DEP_TYPE_GZC = "1"; // ���»Ṥ����
	public static final String DEP_TYPE_BSC = "0"; // ���»���´�

	public static final String NOTICESTATE_NO = "0"; // δ�·�
	public static final String NOTICESTATE_YES = "1"; // ���·�
	public static final String REPORT_GZJHZJ = "0"; // ���´������ƻ�,�ܽᣨ���´������������ˣ�
	public static final String REPORT_JCFA = "1"; // �ල��鷽�������´������������ˣ�
	public static final String REPORT_JCBG = "2"; // �ල��鱨�棨���´������������ˣ��Ƿ�ί�쵼��ˣ�
	public static final String REPORT_ZGFA = "3"; // ��ҵ���ķ�������ҵ������´�һ��ˣ�����������ˣ�
	public static final String REPORT_ZGQK = "4"; // ��ҵ��������ı��棨���´������������ˣ�
	public static final String REPORT_GZBG = "5"; // ��ҵ��ȹ������������ҵ�����������ˣ�
	public static final String REPORT_ZDSX = "6"; // ��ҵ�ش�����ר�������´������������ˣ�
	public static final String REPORT_ZGTZ = "7"; // ��ҵ����̨��(��������,ͬ��ҵ���ķ���)
	public static final String REPORT_JSHGZBG = "8"; // ���»Ṥ������(�㽭����)�����´������������ˣ�//�߹���֪ͨ��������
	public static final String REPORT_ZZJSBG = "9"; // רְ���±���(�㽭����)�����´������������ˣ�//�߹���֪ͨ��������
	public static final String REPORT_JDSB = "10"; // �ල�����ϱ�(�Ĵ�����,���´�������������)
	public static final String REPORT_GKZG = "11"; // �����������(�Ĵ�����,���´�����ҵ������������)
	public static final String REPORT_XXZB = "12"; // ��Ϣר��(�Ĵ�����,���´�������������)

	public static final String RISK_SAVE = "1"; //����
	public static final String RISK_SUBMITED = "2"; //�����
	public static final String RISK_AUDITED = "3"; //�����
	public static final String RISK_SUBTOLEADER = "4"; //����ʾ
	public static final String RISK_INSTRUCTIONED = "5"; //������
	public static final String RISK_ASSIGNED = "6"; //������
	public static final String RISK_CLOSE = "7"; //�Ѵ���

	public static final String RISKLEVEL_HIGH = "1"; //������
	public static final String RISKLEVEL_MIDDLE = "2"; //������
	public static final String RISKLEVEL_LOW = "3"; //�Ѵ���

	public static final String RISKSTUTAS_DOING = "1"; //������
	public static final String RISKSTUTAS_FINISH = "2"; //���
	public static final String RISKSTUTAS_NOTDO = "3"; //û����

	public static final String REPORT_SAVE = "0"; // δ�ύ
	public static final String REPORT_SUBMIT = "1"; // ���ύ
	public static final String REPORT_SENDBACK = "2"; // �˻�

	public static final String NOCTCE_GZJHZJ = "0"; // ���´������ƻ��ܽᣨ���´���
	public static final String NOCTCE_JDJC = "1"; // ��ҵ�ල��飨��ҵ��
	public static final String NOCTCE_GZBG = "2"; // ��ҵ��ȹ������棨��ҵ��
	public static final String NOCTCE_JSGZ = "3"; // ְ�����¹�������ҵ��
	public static final String NOCTCE_JCYJXF = "4"; // �ල�������·�����ҵ��
	public static final String NOCTCE_HSCSTZ = "5"; // ��ҵ�������֪ͨ�����´���

	public static final String NOTICE_UNSEND = "0"; // δ����
	public static final String NOTICE_SENDED = "1"; // �ѷ���
	public static final String NOTICE_READED = "2"; // �Ѷ�
	public static final String NOTICE_FKED = "3"; // �ѷ���

	/**
	 * ���»�ģ��SCAP_WEBBOS��̬�����������
	 */

	/**
	 * ������� --��Ŀ״̬
	 */
	public static final String CEP_PROSTATES_NEW = "0"; // ��ǩԼ
	public static final String CEP_PROSTATES_EARLY = "1"; // ǰ��
	public static final String CEP_PROSTATES_START = "2"; // �ѿ���
	public static final String CEP_PROSTATES_END = "3"; // �ѿ���

	/**
	 * ���»�ģ��SCAP_WEBBOD��̬�������忪ʼ
	 */
	// ��������
	public static final String ATTCHFILE_RESOLUTION = "_1"; // ���鸽��
	public static final String ATTCHFILE_ISSUES = "_2"; // �������ϸ���
	public static final String ATTCHFILE_INFORMATION = "_3"; // ���鹤������
	/**
	 * ���»�ģ��SCAP_WEBBOD��̬�����������
	 */
	/**
	 * ֪ͨģ�龲̬�������忪ʼ(֪ͨ�����������֪ͨ���ͷ���Ҳ���Ǳ��������������������������֪ͨ���շ���Ҳ���Ǳ������)
	 */
	public static final String REPORT_BODY_QY = "1"; // ��������_��ҵ
	public static final String REPORT_BODY_VISORG = "2"; // ��������_��֯
	public static final String REPORT_BODY_MAN = "3"; // ��������_����

	public static final String NOTICE_STYLE_REPORT = "1"; // ֪ͨ����_�֪ͨ
	public static final String NOTICE_STYLE_MESSAGE = "2"; // ֪ͨ����_������Ϣ֪ͨ
	public static final String NOTICE_STYLE_NORMAL_MESSAGE = "3"; // ֪ͨ����_��ͨ��Ϣ֪ͨ

	public static final String REPORT_STATUS_SAVE = "0"; // �״̬_�ѱ���
	public static final String REPORT_STATUS_UNCOMMIT = "1"; // �״̬_δ�ύ
	public static final String REPORT_STATUS_COMMIT = "2"; // �״̬_���ύ
	public static final String REPORT_STATUS_RECEIVED = "3"; // �״̬_�ѽ���
	public static final String REPORT_STATUS_RETURN = "4"; // �״̬_���˻�
	public static final String REPORT_STATUS_NOSAVE = "5"; // �״̬_δ����

	public static final String URGE_FREQUENCY_SINGLE = "1"; // �߱�Ƶ��_���δ߱�
	public static final String URGE_FREQUENCY_DAILY = "2"; // �߱�Ƶ��_ÿ��߱�
	public static final String URGE_FREQUENCY_GETIAN = "3"; // �߱�Ƶ��_����߱�

	public static final String PARAMMAP_KEY_BUSINESS_TYPE = "PARAMMAP_KEY_BUSINESS_TYPE"; // ����mapkey_ҵ������
	public static final String PARAMMAP_KEY_REPORT_TYPE= "PARAMMAP_KEY_REPORT_TYPE"; // ����mapkey_��������
	public static final String PARAMMAP_KEY_DATA_TYPE = "PARAMMAP_KEY_DATA_TYPE"; // ����mapkey_��������
	public static final String PARAMMAP_KEY_MESSAGE_TYPE = "PARAMMAP_KEY_MESSAGE_TYPE"; // ����mapkey_��Ϣ����
	public static final String PARAMMAP_KEY_REPORTBODY = "PARAMMAP_KEY_REPORTBODY"; // ����mapkey_��������
	public static final String PARAMMAP_KEY_PK_NOTICE = "PARAMMAP_KEY_PK_NOTICE"; // ����mapkey_֪ͨ����

	public static final String APPATTR_BUSINESS_TYPE = "business_type"; // ҵ������pk����������view��session����
	public static final String APPATTR_REPORT_TYPE = "report_type"; // ��������pk����������view��session����
	public static final String APPATTR_REPORT_CODE = "report_code"; // ��������code����������view��session����
	public static final String APPATTR_REPORT_BODY = "report_body"; // �����������壺��������view��session����
	public static final String APPATTR_DATA_TYPE = "data_type"; // �������ͣ���������view��session����
	public static final String APPATTR_MESSAGE_TYPE = "message_type";// ��Ϣ���ͣ���������view��session����
	public static final String APPATTR_BUSINESS_TYPE_FUNCNODE = "business_type_funcnode"; // ҵ������pk�����ܽڵ�ע�ᣩ����������view��session����
	public static final String APPATTR_BUSINESS_TYPE_LIANXIREN = "business_type_lianxiren"; // ҵ������pk����ϵ�˹����м�������������������view��session����
	public static final String APPATTR_REPORT_TYPE_LIANXIREN = "report_type_lianxiren"; // ��������pk����ϵ�˹����м�������������������view��session����
	public static final String APPATTR_DATA_TYPE_LIANXIREN = "data_type_lianxiren"; // ��������pk����ϵ�˹����м�������������������view��session����

	public static final String APPATTR_SENDER_OR_RECEIVER = "senderOrReceiver"; // �ǽ��շ����Ƿ��ͷ�
	// ҵ������
	public static final String CONTENT_REPLACE_BUSINESS_TYPE = "ҵ������";
	// ��������
	public static final String CONTENT_REPLACE_REPORT_TYPE = "��������";
	// ��������
	public static final String CONTENT_REPLACE_DATA_TYPE = "��������";
	// �û�����
	public static final String CONTENT_REPLACE_USERNAME = "�û�����";
	// ��ϵ�����͡������շ�
	public static final String CONTENT_TYPE_RECEIVER = "1";
	// ��ϵ�����͡������ͷ�
	public static final String CONTENT_TYPE_SENDER = "2";
	// ����ͳ�����ݸ�ʽ������ί�û���
	public static final String CONTENT_URGE_CONTENT_GZW = "��ֹ��Ŀǰ����ҵ�����͡�-���������͡��С�δ���ҵ����������ҵδ��������������ҵ����������ҵ������";
	// �߱���Ϣ���ݸ�ʽ����ҵ�û���
	public static final String CONTENT_URGE_CONTENT_QY = "���û������������á�����δ��ɡ�ҵ�����͡�-�������������͡�������¼ϵͳ�������";
	// ��������(�֪ͨ)
	public static final String CONTENT_REMIND_INFO_REPORT = "���û������������á�����δ��ɡ�ҵ�����͡�-���������͡�������¼ϵͳ�������";
	// ��������(��Ϣ֪ͨ)
	public static final String CONTENT_REMIND_INFO_MESSAGE = "���û������������á������·��ˡ��������͡���֪ͨ�������鿴��";
	// ֪ͨ����(�֪ͨ)
	public static final String CONTENT_NOTICE_CONTENT_REPORT = "���û������������á�����δ��ɡ�ҵ�����͡�-���������͡�������¼ϵͳ�������";
	// ֪ͨ����(��Ϣ֪ͨ)
	public static final String CONTENT_NOTICE_CONTENT_MESSAGE = "���û������������á������·��ˡ��������͡���֪ͨ�������鿴��";
	// �߱��������ί
	public static final String URGE_TITLE_GZW = "��ҵ�����͡�-���������͡����֪ͨ��ͳ����Ϣ��";
	// �߱�������ҵ
	public static final String URGE_TITLE_QY = "�뾡�����ҵ�����͡�-���������͡����֪ͨ�ı��棡";

	// ֪ͨר��errormessage
	public static final String NOTICE_ERROR_MESSAGE = "NOTICE_ERROR_MESSAGE";
	
	// ��׼��Ϣcode
	public static final String STANDARD_MESSAGE_TYPE_CODE = "standardMessage";	
	public static final String STANDARD_MESSAGE_TYPE_NAME = "��׼��Ϣ";	
	
	//֪ͨģ����ع��ܽڵ�Ĳ�������
	// TZXF ��֪ͨ�·�����
	// ֪ͨ���� ����ֵ
	public static final String TZXF_NOTICE_STYLE = "tzxf_notice_style"; 
	// �������� ����ֵ
	public static final String TZXF_REPORTBODY = "tzxf_report_body"; 
	// ��Ϣ���� ����ֵ
	public static final String TZXF_MESSAGE_TYPE_CODE = "tzxf_message_type_code"; 
	
	/**
	 * ֪ͨģ�龲̬�����������
	 */
	/**
	 * ���ֳ���
	 */
	public static final java.lang.String RMB_CODE = "CNY";

	public static final java.lang.String DEM_CODE = "DEM";

	public static final java.lang.String EURO_CODE = "EURO";

	public static final java.lang.String GBP_CODE = "GBP";

	public static final java.lang.String HKD_CODE = "HKD";

	public static final java.lang.String JPY_CODE = "JPY";

	public static final java.lang.String SF_CODE = "SF";

	public static final java.lang.String USD_CODE = "USD";

	public static final String ORG_TREE_ROOT = "all"; // ��֯�����ڵ� pk_orgĬ��ֵ ��all�滻
														// null��

	public static final String GZWORGCODE = "GzwOrgCode";
	public static final String FUNC_TYPE = "func_type";
	public static final String PROVINCE_ID = "provinceId";
}