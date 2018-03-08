package nc.scap.transfer.land.utils;

public interface ILandConstants {
	//�����
	public static final String PK_RMB = "1002Z0100000000001K1";
	//����״̬
	public static final String FOMRSTATE_END = "End";//����
	public static final String FOMRSTATE_NOTSTART = "NottStart";// ����С�
	public static final String FOMRSTATE_RUN = "Run";// ������
	public static final String FOMRSTATE_SUSPENDED = "Suspended";// ����
	public static final String FOMRSTATE_CANCELLATION = "Cancellation";// ����
	
	// �����̵�����״̬
	public final static String APPROVESTATE_ADD = "0";//����
	public final static String APPROVESTATE_APPROVING = "1";//������
	public final static String APPROVESTATE_BACK = "2";//����
	public final static String APPROVESTATE_APPROVED = "3";//������
	public final static String APPROVESTATE_CANCELLATION = "4";//����
	//�ڵ����
	public static final String IS_AUDIT = "isAudit";
	public static final String AUDIT_FALSE = "false";//�
	public static final String AUDIT_VIEW = "view";//�鿴
	public static final String AUDIT_TRUE = "true";//���
	
	//��������FORM
	public static final String FORM_ID_LAND = "scappt_land_form";
	
	//DTASET
	public static final String DATASET_ID_LAND = "scappt_land";

	//��VIEW
	public static final String MAIN_VIEW_ID="main"; 

	//����
	public static final String SAVE_OPE = "OPE";//�����־
	public static final String SAVE_SIGN_ADD = "add";
	public static final String SAVE_SIGN_EDIT = "edit";
	public static final String SAVE_SIGN_VIEW = "view";
	public static final String SAVE_SIGN_AUDIT = "audit";
	
	public static final String SIGN_FINEREPORT = "fineReport"; // FineReport��־
	
	//��ť
	public static final String MENUBAR = "menubar";
	public static final String EXEMENUBAR = "simpleExeMenubar";
	
	public static final String BTN_ADD = "add";//������ť
	public static final String BTN_EDIT = "edit";//�޸İ�ť
	public static final String BTN_DEL = "del";//ɾ����ť
	
	public static final String BTN_ATTACHFILE = "attachfile";//������ť
	public static final String BTN_PRINT="print"; //��ӡ��ť
	public static final String BTN_QYTY = "startorstrop";//����ͣ��
	
	
	public static final String BTN_COPY="copy"; //���ư�ť
	public static final String BTN_SAVE="save"; //���水ť
	public static final String BTN_BACK="back"; //���ذ�ť
	public static final String BTN_AUDIT="audit"; //��˰�ť
	public static final String BTN_WF="wf"; //���̰�ť
	public static final String BTN_SYNCHRO="synchro";//ͬ����ť
	public static final String BTN_CANCEL="cancel";//������ť
	public static final String BTN_VIEW="view";//�鿴��ť
	
	public static final String BTN_ZANCUN = "btn_save";
	public static final String BTN_TIJIAO = "btn_ok";
	public static final String BTN_FUJIAN = "link_addattach";
	public static final String BTN_LIUCHENG = "allFlow";
	public static final String BTN_BOHUI = "reject";
	
}
