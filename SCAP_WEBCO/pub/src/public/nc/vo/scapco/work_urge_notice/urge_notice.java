/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product. It is based on Velocity.     *
\***************************************************************/
package nc.vo.scapco.work_urge_notice;
	
import nc.vo.pub.*;

/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 * <p>
 *     �ڴ˴����Ӵ����������Ϣ
 * </p>
 * ��������:
 * @author 
 * @version NCPrj ??
 */
@SuppressWarnings("serial")
public class urge_notice extends SuperVO {
	private java.lang.String pk_urge_notice;
	private java.lang.String pk_notice;
	private java.lang.String urge_notice_title;
	private java.lang.String business_type;
	private java.lang.String notice_type;
	private java.lang.String urge_notice_org;
	private java.lang.String urge_content_qy;
	private java.lang.String urge_info_trans_way;
	private java.lang.String urge_recept_man;
	private java.lang.String urge_recept_addr;
	private java.lang.String urge_man;
	private java.lang.String urge_org;
	private nc.vo.pub.lang.UFDate urge_time;
	private java.lang.String is_auto_urge;
	private java.lang.String def1;
	private java.lang.String def2;
	private java.lang.String def3;
	private java.lang.String def4;
	private java.lang.String def5;
	private java.lang.String def6;
	private java.lang.String def7;
	private java.lang.String def8;
	private java.lang.String def9;
	private java.lang.String def10;
	private java.lang.Integer dr = 0;
	private nc.vo.pub.lang.UFDateTime ts;

	public static final String PK_URGE_NOTICE = "pk_urge_notice";
	public static final String PK_NOTICE = "pk_notice";
	public static final String URGE_NOTICE_TITLE = "urge_notice_title";
	public static final String BUSINESS_TYPE = "business_type";
	public static final String NOTICE_TYPE = "notice_type";
	public static final String URGE_NOTICE_ORG = "urge_notice_org";
	public static final String URGE_CONTENT_QY = "urge_content_qy";
	public static final String URGE_INFO_TRANS_WAY = "urge_info_trans_way";
	public static final String URGE_RECEPT_MAN = "urge_recept_man";
	public static final String URGE_RECEPT_ADDR = "urge_recept_addr";
	public static final String URGE_MAN = "urge_man";
	public static final String URGE_ORG = "urge_org";
	public static final String URGE_TIME = "urge_time";
	public static final String IS_AUTO_URGE = "is_auto_urge";
	public static final String DEF1 = "def1";
	public static final String DEF2 = "def2";
	public static final String DEF3 = "def3";
	public static final String DEF4 = "def4";
	public static final String DEF5 = "def5";
	public static final String DEF6 = "def6";
	public static final String DEF7 = "def7";
	public static final String DEF8 = "def8";
	public static final String DEF9 = "def9";
	public static final String DEF10 = "def10";
			
	/**
	 * ����pk_urge_notice��Getter����.������������
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getPk_urge_notice () {
		return pk_urge_notice;
	}   
	/**
	 * ����pk_urge_notice��Setter����.������������
	 * ��������:
	 * @param newPk_urge_notice java.lang.String
	 */
	public void setPk_urge_notice (java.lang.String newPk_urge_notice ) {
	 	this.pk_urge_notice = newPk_urge_notice;
	} 	  
	/**
	 * ����pk_notice��Getter����.��������֪ͨ����
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getPk_notice () {
		return pk_notice;
	}   
	/**
	 * ����pk_notice��Setter����.��������֪ͨ����
	 * ��������:
	 * @param newPk_notice java.lang.String
	 */
	public void setPk_notice (java.lang.String newPk_notice ) {
	 	this.pk_notice = newPk_notice;
	} 	  
	/**
	 * ����urge_notice_title��Getter����.���������߱�֪ͨ����
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getUrge_notice_title () {
		return urge_notice_title;
	}   
	/**
	 * ����urge_notice_title��Setter����.���������߱�֪ͨ����
	 * ��������:
	 * @param newUrge_notice_title java.lang.String
	 */
	public void setUrge_notice_title (java.lang.String newUrge_notice_title ) {
	 	this.urge_notice_title = newUrge_notice_title;
	} 	  
	/**
	 * ����business_type��Getter����.��������ҵ������
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getBusiness_type () {
		return business_type;
	}   
	/**
	 * ����business_type��Setter����.��������ҵ������
	 * ��������:
	 * @param newBusiness_type java.lang.String
	 */
	public void setBusiness_type (java.lang.String newBusiness_type ) {
	 	this.business_type = newBusiness_type;
	} 	  
	/**
	 * ����notice_type��Getter����.����������������
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getNotice_type () {
		return notice_type;
	}   
	/**
	 * ����notice_type��Setter����.����������������
	 * ��������:
	 * @param newNotice_type java.lang.String
	 */
	public void setNotice_type (java.lang.String newNotice_type ) {
	 	this.notice_type = newNotice_type;
	} 	  
	/**
	 * ����urge_notice_org��Getter����.�����������߱���λ
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getUrge_notice_org () {
		return urge_notice_org;
	}   
	/**
	 * ����urge_notice_org��Setter����.�����������߱���λ
	 * ��������:
	 * @param newUrge_notice_org java.lang.String
	 */
	public void setUrge_notice_org (java.lang.String newUrge_notice_org ) {
	 	this.urge_notice_org = newUrge_notice_org;
	} 	  
	/**
	 * ����urge_content_qy��Getter����.���������߱�����
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getUrge_content_qy () {
		return urge_content_qy;
	}   
	/**
	 * ����urge_content_qy��Setter����.���������߱�����
	 * ��������:
	 * @param newUrge_content_qy java.lang.String
	 */
	public void setUrge_content_qy (java.lang.String newUrge_content_qy ) {
	 	this.urge_content_qy = newUrge_content_qy;
	} 	  
	/**
	 * ����urge_info_trans_way��Getter����.���������߱���ʽ
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getUrge_info_trans_way () {
		return urge_info_trans_way;
	}   
	/**
	 * ����urge_info_trans_way��Setter����.���������߱���ʽ
	 * ��������:
	 * @param newUrge_info_trans_way java.lang.String
	 */
	public void setUrge_info_trans_way (java.lang.String newUrge_info_trans_way ) {
	 	this.urge_info_trans_way = newUrge_info_trans_way;
	} 	  
	/**
	 * ����urge_recept_man��Getter����.��������������
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getUrge_recept_man () {
		return urge_recept_man;
	}   
	/**
	 * ����urge_recept_man��Setter����.��������������
	 * ��������:
	 * @param newUrge_recept_man java.lang.String
	 */
	public void setUrge_recept_man (java.lang.String newUrge_recept_man ) {
	 	this.urge_recept_man = newUrge_recept_man;
	} 	  
	/**
	 * ����urge_recept_addr��Getter����.�����������յ�ַ
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getUrge_recept_addr () {
		return urge_recept_addr;
	}   
	/**
	 * ����urge_recept_addr��Setter����.�����������յ�ַ
	 * ��������:
	 * @param newUrge_recept_addr java.lang.String
	 */
	public void setUrge_recept_addr (java.lang.String newUrge_recept_addr ) {
	 	this.urge_recept_addr = newUrge_recept_addr;
	} 	  
	/**
	 * ����urge_man��Getter����.���������߱���
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getUrge_man () {
		return urge_man;
	}   
	/**
	 * ����urge_man��Setter����.���������߱���
	 * ��������:
	 * @param newUrge_man java.lang.String
	 */
	public void setUrge_man (java.lang.String newUrge_man ) {
	 	this.urge_man = newUrge_man;
	} 	  
	/**
	 * ����urge_org��Getter����.���������߱����͵�λ
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getUrge_org () {
		return urge_org;
	}   
	/**
	 * ����urge_org��Setter����.���������߱����͵�λ
	 * ��������:
	 * @param newUrge_org java.lang.String
	 */
	public void setUrge_org (java.lang.String newUrge_org ) {
	 	this.urge_org = newUrge_org;
	} 	  
	/**
	 * ����urge_time��Getter����.���������߱�ʱ��
	 * ��������:
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getUrge_time () {
		return urge_time;
	}   
	/**
	 * ����urge_time��Setter����.���������߱�ʱ��
	 * ��������:
	 * @param newUrge_time nc.vo.pub.lang.UFDate
	 */
	public void setUrge_time (nc.vo.pub.lang.UFDate newUrge_time ) {
	 	this.urge_time = newUrge_time;
	} 	  
	/**
	 * ����is_auto_urge��Getter����.���������Ƿ��Զ��߱�
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getIs_auto_urge () {
		return is_auto_urge;
	}   
	/**
	 * ����is_auto_urge��Setter����.���������Ƿ��Զ��߱�
	 * ��������:
	 * @param newIs_auto_urge java.lang.String
	 */
	public void setIs_auto_urge (java.lang.String newIs_auto_urge ) {
	 	this.is_auto_urge = newIs_auto_urge;
	} 	  
	/**
	 * ����def1��Getter����.�������������ֶ�1
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getDef1 () {
		return def1;
	}   
	/**
	 * ����def1��Setter����.�������������ֶ�1
	 * ��������:
	 * @param newDef1 java.lang.String
	 */
	public void setDef1 (java.lang.String newDef1 ) {
	 	this.def1 = newDef1;
	} 	  
	/**
	 * ����def2��Getter����.�������������ֶ�2
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getDef2 () {
		return def2;
	}   
	/**
	 * ����def2��Setter����.�������������ֶ�2
	 * ��������:
	 * @param newDef2 java.lang.String
	 */
	public void setDef2 (java.lang.String newDef2 ) {
	 	this.def2 = newDef2;
	} 	  
	/**
	 * ����def3��Getter����.�������������ֶ�3
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getDef3 () {
		return def3;
	}   
	/**
	 * ����def3��Setter����.�������������ֶ�3
	 * ��������:
	 * @param newDef3 java.lang.String
	 */
	public void setDef3 (java.lang.String newDef3 ) {
	 	this.def3 = newDef3;
	} 	  
	/**
	 * ����def4��Getter����.�������������ֶ�4
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getDef4 () {
		return def4;
	}   
	/**
	 * ����def4��Setter����.�������������ֶ�4
	 * ��������:
	 * @param newDef4 java.lang.String
	 */
	public void setDef4 (java.lang.String newDef4 ) {
	 	this.def4 = newDef4;
	} 	  
	/**
	 * ����def5��Getter����.�������������ֶ�5
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getDef5 () {
		return def5;
	}   
	/**
	 * ����def5��Setter����.�������������ֶ�5
	 * ��������:
	 * @param newDef5 java.lang.String
	 */
	public void setDef5 (java.lang.String newDef5 ) {
	 	this.def5 = newDef5;
	} 	  
	/**
	 * ����def6��Getter����.�������������ֶ�6
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getDef6 () {
		return def6;
	}   
	/**
	 * ����def6��Setter����.�������������ֶ�6
	 * ��������:
	 * @param newDef6 java.lang.String
	 */
	public void setDef6 (java.lang.String newDef6 ) {
	 	this.def6 = newDef6;
	} 	  
	/**
	 * ����def7��Getter����.�������������ֶ�7
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getDef7 () {
		return def7;
	}   
	/**
	 * ����def7��Setter����.�������������ֶ�7
	 * ��������:
	 * @param newDef7 java.lang.String
	 */
	public void setDef7 (java.lang.String newDef7 ) {
	 	this.def7 = newDef7;
	} 	  
	/**
	 * ����def8��Getter����.�������������ֶ�8
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getDef8 () {
		return def8;
	}   
	/**
	 * ����def8��Setter����.�������������ֶ�8
	 * ��������:
	 * @param newDef8 java.lang.String
	 */
	public void setDef8 (java.lang.String newDef8 ) {
	 	this.def8 = newDef8;
	} 	  
	/**
	 * ����def9��Getter����.�������������ֶ�9
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getDef9 () {
		return def9;
	}   
	/**
	 * ����def9��Setter����.�������������ֶ�9
	 * ��������:
	 * @param newDef9 java.lang.String
	 */
	public void setDef9 (java.lang.String newDef9 ) {
	 	this.def9 = newDef9;
	} 	  
	/**
	 * ����def10��Getter����.�������������ֶ�10
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getDef10 () {
		return def10;
	}   
	/**
	 * ����def10��Setter����.�������������ֶ�10
	 * ��������:
	 * @param newDef10 java.lang.String
	 */
	public void setDef10 (java.lang.String newDef10 ) {
	 	this.def10 = newDef10;
	} 	  
	/**
	 * ����dr��Getter����.��������dr
	 * ��������:
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getDr () {
		return dr;
	}   
	/**
	 * ����dr��Setter����.��������dr
	 * ��������:
	 * @param newDr java.lang.Integer
	 */
	public void setDr (java.lang.Integer newDr ) {
	 	this.dr = newDr;
	} 	  
	/**
	 * ����ts��Getter����.��������ts
	 * ��������:
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getTs () {
		return ts;
	}   
	/**
	 * ����ts��Setter����.��������ts
	 * ��������:
	 * @param newTs nc.vo.pub.lang.UFDateTime
	 */
	public void setTs (nc.vo.pub.lang.UFDateTime newTs ) {
	 	this.ts = newTs;
	} 	  
 
	/**
	  * <p>ȡ�ø�VO�����ֶ�.
	  * <p>
	  * ��������:
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	    return null;
	}   
    
	/**
	  * <p>ȡ�ñ�����.
	  * <p>
	  * ��������:
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	  return "pk_urge_notice";
	}
    
	/**
	 * <p>���ر�����.
	 * <p>
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "scapco_urge_notice";
	}    
	
	/**
	 * <p>���ر�����.
	 * <p>
	 * ��������:
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "scapco_urge_notice";
	}    
    
    /**
	  * ����Ĭ�Ϸ�ʽ����������.
	  *
	  * ��������:
	  */
     public urge_notice() {
		super();	
	}    
	
	@nc.vo.annotation.MDEntityInfo(beanFullclassName =  "nc.vo.scapco.work_urge_notice.urge_notice" )
	public IVOMeta getMetaData() {
   		return null;
  	}
} 

