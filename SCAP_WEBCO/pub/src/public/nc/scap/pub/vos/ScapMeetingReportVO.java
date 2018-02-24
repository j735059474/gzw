/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product. It is based on Velocity.     *
\***************************************************************/
package nc.scap.pub.vos;
	
import nc.vo.pub.*;

/**
 * <b> 在此处简要描述此类的功能 </b>
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 * 创建日期:
 * @author 
 * @version NCPrj ??
 */
@SuppressWarnings("serial")
public class ScapMeetingReportVO extends SuperVO {
	private java.lang.String pk_meeting;
	private java.lang.String pk_meetingreport;
	private java.lang.String reportperson;
	private nc.vo.pub.lang.UFDate reportdate;
	private java.lang.String reporttel;
	private java.lang.String acceptorg;
	private nc.vo.pub.lang.UFDateTime acceptdate;
	private java.lang.String ismeeting;
	private java.lang.String vbdef1;
	private java.lang.String vbdef2;
	private java.lang.String vbdef3;
	private java.lang.String vbdef4;
	private java.lang.String vbdef5;
	private nc.vo.pub.lang.UFDateTime vbdef6;
	private nc.vo.pub.lang.UFBoolean vbdef7;
	private java.lang.Integer vbdef8;
	private nc.vo.pub.lang.UFDouble vbdef9;
	private nc.vo.pub.lang.UFDouble vbdef10;
	private java.lang.Integer dr = 0;
	private nc.vo.pub.lang.UFDateTime ts;
	private nc.scap.pub.vos.ScapMeetingUserVO[] meetingusers;
	private nc.scap.pub.vos.ScapMeetingStaffVO[] meetingstaffs;

	public static final String PK_MEETING = "pk_meeting";
	public static final String PK_MEETINGREPORT = "pk_meetingreport";
	public static final String REPORTPERSON = "reportperson";
	public static final String REPORTDATE = "reportdate";
	public static final String REPORTTEL = "reporttel";
	public static final String ACCEPTORG = "acceptorg";
	public static final String ACCEPTDATE = "acceptdate";
	public static final String ISMEETING = "ismeeting";
	public static final String VBDEF1 = "vbdef1";
	public static final String VBDEF2 = "vbdef2";
	public static final String VBDEF3 = "vbdef3";
	public static final String VBDEF4 = "vbdef4";
	public static final String VBDEF5 = "vbdef5";
	public static final String VBDEF6 = "vbdef6";
	public static final String VBDEF7 = "vbdef7";
	public static final String VBDEF8 = "vbdef8";
	public static final String VBDEF9 = "vbdef9";
	public static final String VBDEF10 = "vbdef10";
			
	/**
	 * 属性pk_meeting的Getter方法.属性名：parentPK
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getPk_meeting () {
		return pk_meeting;
	}   
	/**
	 * 属性pk_meeting的Setter方法.属性名：parentPK
	 * 创建日期:
	 * @param newPk_meeting java.lang.String
	 */
	public void setPk_meeting (java.lang.String newPk_meeting ) {
	 	this.pk_meeting = newPk_meeting;
	} 	  
	/**
	 * 属性pk_meetingreport的Getter方法.属性名：主键
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getPk_meetingreport () {
		return pk_meetingreport;
	}   
	/**
	 * 属性pk_meetingreport的Setter方法.属性名：主键
	 * 创建日期:
	 * @param newPk_meetingreport java.lang.String
	 */
	public void setPk_meetingreport (java.lang.String newPk_meetingreport ) {
	 	this.pk_meetingreport = newPk_meetingreport;
	} 	  
	/**
	 * 属性reportperson的Getter方法.属性名：上报人
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getReportperson () {
		return reportperson;
	}   
	/**
	 * 属性reportperson的Setter方法.属性名：上报人
	 * 创建日期:
	 * @param newReportperson java.lang.String
	 */
	public void setReportperson (java.lang.String newReportperson ) {
	 	this.reportperson = newReportperson;
	} 	  
	/**
	 * 属性reportdate的Getter方法.属性名：上报时间
	 * 创建日期:
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getReportdate () {
		return reportdate;
	}   
	/**
	 * 属性reportdate的Setter方法.属性名：上报时间
	 * 创建日期:
	 * @param newReportdate nc.vo.pub.lang.UFDate
	 */
	public void setReportdate (nc.vo.pub.lang.UFDate newReportdate ) {
	 	this.reportdate = newReportdate;
	} 	  
	/**
	 * 属性reporttel的Getter方法.属性名：联系电话
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getReporttel () {
		return reporttel;
	}   
	/**
	 * 属性reporttel的Setter方法.属性名：联系电话
	 * 创建日期:
	 * @param newReporttel java.lang.String
	 */
	public void setReporttel (java.lang.String newReporttel ) {
	 	this.reporttel = newReporttel;
	} 	  
	/**
	 * 属性acceptorg的Getter方法.属性名：接收机构
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getAcceptorg () {
		return acceptorg;
	}   
	/**
	 * 属性acceptorg的Setter方法.属性名：接收机构
	 * 创建日期:
	 * @param newAcceptorg java.lang.String
	 */
	public void setAcceptorg (java.lang.String newAcceptorg ) {
	 	this.acceptorg = newAcceptorg;
	} 	  
	/**
	 * 属性acceptdate的Getter方法.属性名：接收时间
	 * 创建日期:
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDateTime getAcceptdate () {
		return acceptdate;
	}   
	/**
	 * 属性acceptdate的Setter方法.属性名：接收时间
	 * 创建日期:
	 * @param newAcceptdate nc.vo.pub.lang.UFDate
	 */
	public void setAcceptdate (nc.vo.pub.lang.UFDateTime newAcceptdate ) {
	 	this.acceptdate = newAcceptdate;
	} 	  
	/**
	 * 属性ismeeting的Getter方法.属性名：参会与否
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getIsmeeting () {
		return ismeeting;
	}   
	/**
	 * 属性ismeeting的Setter方法.属性名：参会与否
	 * 创建日期:
	 * @param newIsmeeting java.lang.String
	 */
	public void setIsmeeting (java.lang.String newIsmeeting ) {
	 	this.ismeeting = newIsmeeting;
	} 	  
	/**
	 * 属性vbdef1的Getter方法.属性名：自定义项1
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getVbdef1 () {
		return vbdef1;
	}   
	/**
	 * 属性vbdef1的Setter方法.属性名：自定义项1
	 * 创建日期:
	 * @param newVbdef1 java.lang.String
	 */
	public void setVbdef1 (java.lang.String newVbdef1 ) {
	 	this.vbdef1 = newVbdef1;
	} 	  
	/**
	 * 属性vbdef2的Getter方法.属性名：自定义项2
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getVbdef2 () {
		return vbdef2;
	}   
	/**
	 * 属性vbdef2的Setter方法.属性名：自定义项2
	 * 创建日期:
	 * @param newVbdef2 java.lang.String
	 */
	public void setVbdef2 (java.lang.String newVbdef2 ) {
	 	this.vbdef2 = newVbdef2;
	} 	  
	/**
	 * 属性vbdef3的Getter方法.属性名：自定义项3
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getVbdef3 () {
		return vbdef3;
	}   
	/**
	 * 属性vbdef3的Setter方法.属性名：自定义项3
	 * 创建日期:
	 * @param newVbdef3 java.lang.String
	 */
	public void setVbdef3 (java.lang.String newVbdef3 ) {
	 	this.vbdef3 = newVbdef3;
	} 	  
	/**
	 * 属性vbdef4的Getter方法.属性名：自定义项4
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getVbdef4 () {
		return vbdef4;
	}   
	/**
	 * 属性vbdef4的Setter方法.属性名：自定义项4
	 * 创建日期:
	 * @param newVbdef4 java.lang.String
	 */
	public void setVbdef4 (java.lang.String newVbdef4 ) {
	 	this.vbdef4 = newVbdef4;
	} 	  
	/**
	 * 属性vbdef5的Getter方法.属性名：自定义项5
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getVbdef5 () {
		return vbdef5;
	}   
	/**
	 * 属性vbdef5的Setter方法.属性名：自定义项5
	 * 创建日期:
	 * @param newVbdef5 java.lang.String
	 */
	public void setVbdef5 (java.lang.String newVbdef5 ) {
	 	this.vbdef5 = newVbdef5;
	} 	  
	/**
	 * 属性vbdef6的Getter方法.属性名：自定义项6
	 * 创建日期:
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getVbdef6 () {
		return vbdef6;
	}   
	/**
	 * 属性vbdef6的Setter方法.属性名：自定义项6
	 * 创建日期:
	 * @param newVbdef6 nc.vo.pub.lang.UFDateTime
	 */
	public void setVbdef6 (nc.vo.pub.lang.UFDateTime newVbdef6 ) {
	 	this.vbdef6 = newVbdef6;
	} 	  
	/**
	 * 属性vbdef7的Getter方法.属性名：自定义项7
	 * 创建日期:
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public nc.vo.pub.lang.UFBoolean getVbdef7 () {
		return vbdef7;
	}   
	/**
	 * 属性vbdef7的Setter方法.属性名：自定义项7
	 * 创建日期:
	 * @param newVbdef7 nc.vo.pub.lang.UFBoolean
	 */
	public void setVbdef7 (nc.vo.pub.lang.UFBoolean newVbdef7 ) {
	 	this.vbdef7 = newVbdef7;
	} 	  
	/**
	 * 属性vbdef8的Getter方法.属性名：自定义项8
	 * 创建日期:
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getVbdef8 () {
		return vbdef8;
	}   
	/**
	 * 属性vbdef8的Setter方法.属性名：自定义项8
	 * 创建日期:
	 * @param newVbdef8 java.lang.Integer
	 */
	public void setVbdef8 (java.lang.Integer newVbdef8 ) {
	 	this.vbdef8 = newVbdef8;
	} 	  
	/**
	 * 属性vbdef9的Getter方法.属性名：自定义项9
	 * 创建日期:
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getVbdef9 () {
		return vbdef9;
	}   
	/**
	 * 属性vbdef9的Setter方法.属性名：自定义项9
	 * 创建日期:
	 * @param newVbdef9 nc.vo.pub.lang.UFDouble
	 */
	public void setVbdef9 (nc.vo.pub.lang.UFDouble newVbdef9 ) {
	 	this.vbdef9 = newVbdef9;
	} 	  
	/**
	 * 属性vbdef10的Getter方法.属性名：自定义项10
	 * 创建日期:
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getVbdef10 () {
		return vbdef10;
	}   
	/**
	 * 属性vbdef10的Setter方法.属性名：自定义项10
	 * 创建日期:
	 * @param newVbdef10 nc.vo.pub.lang.UFDouble
	 */
	public void setVbdef10 (nc.vo.pub.lang.UFDouble newVbdef10 ) {
	 	this.vbdef10 = newVbdef10;
	} 	  
	/**
	 * 属性meetingusers的Getter方法.属性名：meetingusers
	 * 创建日期:
	 * @return nc.scap.pub.vos.ScapMeetingUserVO[]
	 */
	public nc.scap.pub.vos.ScapMeetingUserVO[] getMeetingusers () {
		return meetingusers;
	}   
	/**
	 * 属性meetingusers的Setter方法.属性名：meetingusers
	 * 创建日期:
	 * @param newMeetingusers nc.scap.pub.vos.ScapMeetingUserVO[]
	 */
	public void setMeetingusers (nc.scap.pub.vos.ScapMeetingUserVO[] newMeetingusers ) {
	 	this.meetingusers = newMeetingusers;
	} 	  
	/**
	 * 属性meetingstaffs的Getter方法.属性名：meetingstaffs
	 * 创建日期:
	 * @return nc.scap.pub.vos.ScapMeetingStaffVO[]
	 */
	public nc.scap.pub.vos.ScapMeetingStaffVO[] getMeetingstaffs () {
		return meetingstaffs;
	}   
	/**
	 * 属性meetingstaffs的Setter方法.属性名：meetingstaffs
	 * 创建日期:
	 * @param newMeetingstaffs nc.scap.pub.vos.ScapMeetingStaffVO[]
	 */
	public void setMeetingstaffs (nc.scap.pub.vos.ScapMeetingStaffVO[] newMeetingstaffs ) {
	 	this.meetingstaffs = newMeetingstaffs;
	} 	  
	/**
	 * 属性dr的Getter方法.属性名：dr
	 * 创建日期:
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getDr () {
		return dr;
	}   
	/**
	 * 属性dr的Setter方法.属性名：dr
	 * 创建日期:
	 * @param newDr java.lang.Integer
	 */
	public void setDr (java.lang.Integer newDr ) {
	 	this.dr = newDr;
	} 	  
	/**
	 * 属性ts的Getter方法.属性名：ts
	 * 创建日期:
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getTs () {
		return ts;
	}   
	/**
	 * 属性ts的Setter方法.属性名：ts
	 * 创建日期:
	 * @param newTs nc.vo.pub.lang.UFDateTime
	 */
	public void setTs (nc.vo.pub.lang.UFDateTime newTs ) {
	 	this.ts = newTs;
	} 	  
 
	/**
	  * <p>取得父VO主键字段.
	  * <p>
	  * 创建日期:
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
		return "pk_meeting";
	}   
    
	/**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	  return "pk_meetingreport";
	}
    
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "scapco_meetingreport";
	}    
	
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "scapco_meetingreport";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:
	  */
     public ScapMeetingReportVO() {
		super();	
	}    
	
	@nc.vo.annotation.MDEntityInfo(beanFullclassName =  "nc.scap.pub.vos.ScapMeetingReportVO" )
	public IVOMeta getMetaData() {
   		return null;
  	}
} 


