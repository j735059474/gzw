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
public class ScapMeetingVO extends SuperVO {
	private java.lang.String pk_meeting;
	private java.lang.String pk_org;
	private java.lang.String pk_group;
	private java.lang.String meetingname;
	private java.lang.String meetingorganizer;
	private java.lang.String meetingplace;
	private nc.vo.pub.lang.UFDate meetingdate;
	private java.lang.Integer meetingdays;
	private java.lang.String meetingreleaseorg;
	private nc.vo.pub.lang.UFDate meetingreleasedate;
	private java.lang.String memo;
	private java.lang.String linkman;
	private java.lang.String linkmantel;
	private java.lang.String state;
	private java.lang.String vbdef1;
	private java.lang.String vbdef2;
	private java.lang.String vbdef3;
	private java.lang.String vbdef4;
	private java.lang.String vbdef5;
	private nc.vo.pub.lang.UFDate vbdef6;
	private nc.vo.pub.lang.UFBoolean vbdef7;
	private java.lang.Integer vbdef8;
	private nc.vo.pub.lang.UFDouble vbdef9;
	private nc.vo.pub.lang.UFDouble vbdef10;
	private java.lang.Integer dr = 0;
	private nc.vo.pub.lang.UFDateTime ts;
	private nc.scap.pub.vos.ScapMeetingReportVO[] meetingreports;

	public static final String PK_MEETING = "pk_meeting";
	public static final String PK_ORG = "pk_org";
	public static final String PK_GROUP = "pk_group";
	public static final String MEETINGNAME = "meetingname";
	public static final String MEETINGORGANIZER = "meetingorganizer";
	public static final String MEETINGPLACE = "meetingplace";
	public static final String MEETINGDATE = "meetingdate";
	public static final String MEETINGDAYS = "meetingdays";
	public static final String MEETINGRELEASEORG = "meetingreleaseorg";
	public static final String MEETINGRELEASEDATE = "meetingreleasedate";
	public static final String MEMO = "memo";
	public static final String LINKMAN = "linkman";
	public static final String LINKMANTEL = "linkmantel";
	public static final String STATE = "state";
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
	 * 属性pk_meeting的Getter方法.属性名：主键
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getPk_meeting () {
		return pk_meeting;
	}   
	/**
	 * 属性pk_meeting的Setter方法.属性名：主键
	 * 创建日期:
	 * @param newPk_meeting java.lang.String
	 */
	public void setPk_meeting (java.lang.String newPk_meeting ) {
	 	this.pk_meeting = newPk_meeting;
	} 	  
	/**
	 * 属性pk_org的Getter方法.属性名：组织
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getPk_org () {
		return pk_org;
	}   
	/**
	 * 属性pk_org的Setter方法.属性名：组织
	 * 创建日期:
	 * @param newPk_org java.lang.String
	 */
	public void setPk_org (java.lang.String newPk_org ) {
	 	this.pk_org = newPk_org;
	} 	  
	/**
	 * 属性pk_group的Getter方法.属性名：所属集团
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getPk_group () {
		return pk_group;
	}   
	/**
	 * 属性pk_group的Setter方法.属性名：所属集团
	 * 创建日期:
	 * @param newPk_group java.lang.String
	 */
	public void setPk_group (java.lang.String newPk_group ) {
	 	this.pk_group = newPk_group;
	} 	  
	/**
	 * 属性meetingname的Getter方法.属性名：会议名称
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getMeetingname () {
		return meetingname;
	}   
	/**
	 * 属性meetingname的Setter方法.属性名：会议名称
	 * 创建日期:
	 * @param newMeetingname java.lang.String
	 */
	public void setMeetingname (java.lang.String newMeetingname ) {
	 	this.meetingname = newMeetingname;
	} 	  
	/**
	 * 属性meetingorganizer的Getter方法.属性名：承办单位
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getMeetingorganizer () {
		return meetingorganizer;
	}   
	/**
	 * 属性meetingorganizer的Setter方法.属性名：承办单位
	 * 创建日期:
	 * @param newMeetingorganizer java.lang.String
	 */
	public void setMeetingorganizer (java.lang.String newMeetingorganizer ) {
	 	this.meetingorganizer = newMeetingorganizer;
	} 	  
	/**
	 * 属性meetingplace的Getter方法.属性名：会议地点
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getMeetingplace () {
		return meetingplace;
	}   
	/**
	 * 属性meetingplace的Setter方法.属性名：会议地点
	 * 创建日期:
	 * @param newMeetingplace java.lang.String
	 */
	public void setMeetingplace (java.lang.String newMeetingplace ) {
	 	this.meetingplace = newMeetingplace;
	} 	  
	/**
	 * 属性meetingdate的Getter方法.属性名：会议时间
	 * 创建日期:
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getMeetingdate () {
		return meetingdate;
	}   
	/**
	 * 属性meetingdate的Setter方法.属性名：会议时间
	 * 创建日期:
	 * @param newMeetingdate nc.vo.pub.lang.UFDate
	 */
	public void setMeetingdate (nc.vo.pub.lang.UFDate newMeetingdate ) {
	 	this.meetingdate = newMeetingdate;
	} 	  
	/**
	 * 属性meetingdays的Getter方法.属性名：会期
	 * 创建日期:
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getMeetingdays () {
		return meetingdays;
	}   
	/**
	 * 属性meetingdays的Setter方法.属性名：会期
	 * 创建日期:
	 * @param newMeetingdays java.lang.Integer
	 */
	public void setMeetingdays (java.lang.Integer newMeetingdays ) {
	 	this.meetingdays = newMeetingdays;
	} 	  
	/**
	 * 属性meetingreleaseorg的Getter方法.属性名：发文机关
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getMeetingreleaseorg () {
		return meetingreleaseorg;
	}   
	/**
	 * 属性meetingreleaseorg的Setter方法.属性名：发文机关
	 * 创建日期:
	 * @param newMeetingreleaseorg java.lang.String
	 */
	public void setMeetingreleaseorg (java.lang.String newMeetingreleaseorg ) {
	 	this.meetingreleaseorg = newMeetingreleaseorg;
	} 	  
	/**
	 * 属性meetingreleasedate的Getter方法.属性名：发文日期
	 * 创建日期:
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getMeetingreleasedate () {
		return meetingreleasedate;
	}   
	/**
	 * 属性meetingreleasedate的Setter方法.属性名：发文日期
	 * 创建日期:
	 * @param newMeetingreleasedate nc.vo.pub.lang.UFDate
	 */
	public void setMeetingreleasedate (nc.vo.pub.lang.UFDate newMeetingreleasedate ) {
	 	this.meetingreleasedate = newMeetingreleasedate;
	} 	  
	/**
	 * 属性memo的Getter方法.属性名：备注
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getMemo () {
		return memo;
	}   
	/**
	 * 属性memo的Setter方法.属性名：备注
	 * 创建日期:
	 * @param newMemo java.lang.String
	 */
	public void setMemo (java.lang.String newMemo ) {
	 	this.memo = newMemo;
	} 	  
	/**
	 * 属性linkman的Getter方法.属性名：联系人
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getLinkman () {
		return linkman;
	}   
	/**
	 * 属性linkman的Setter方法.属性名：联系人
	 * 创建日期:
	 * @param newLinkman java.lang.String
	 */
	public void setLinkman (java.lang.String newLinkman ) {
	 	this.linkman = newLinkman;
	} 	  
	/**
	 * 属性linkmantel的Getter方法.属性名：联系电话
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getLinkmantel () {
		return linkmantel;
	}   
	/**
	 * 属性linkmantel的Setter方法.属性名：联系电话
	 * 创建日期:
	 * @param newLinkmantel java.lang.String
	 */
	public void setLinkmantel (java.lang.String newLinkmantel ) {
	 	this.linkmantel = newLinkmantel;
	} 	  
	/**
	 * 属性state的Getter方法.属性名：上报情况
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getState () {
		return state;
	}   
	/**
	 * 属性state的Setter方法.属性名：上报情况
	 * 创建日期:
	 * @param newState java.lang.String
	 */
	public void setState (java.lang.String newState ) {
	 	this.state = newState;
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
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getVbdef6 () {
		return vbdef6;
	}   
	/**
	 * 属性vbdef6的Setter方法.属性名：自定义项6
	 * 创建日期:
	 * @param newVbdef6 nc.vo.pub.lang.UFDate
	 */
	public void setVbdef6 (nc.vo.pub.lang.UFDate newVbdef6 ) {
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
	 * 属性meetingreports的Getter方法.属性名：meetingreports
	 * 创建日期:
	 * @return nc.scap.pub.vos.ScapMeetingReportVO[]
	 */
	public nc.scap.pub.vos.ScapMeetingReportVO[] getMeetingreports () {
		return meetingreports;
	}   
	/**
	 * 属性meetingreports的Setter方法.属性名：meetingreports
	 * 创建日期:
	 * @param newMeetingreports nc.scap.pub.vos.ScapMeetingReportVO[]
	 */
	public void setMeetingreports (nc.scap.pub.vos.ScapMeetingReportVO[] newMeetingreports ) {
	 	this.meetingreports = newMeetingreports;
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
	    return null;
	}   
    
	/**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	  return "pk_meeting";
	}
    
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "scapco_meeting";
	}    
	
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "scapco_meeting";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:
	  */
     public ScapMeetingVO() {
		super();	
	}    
	
	@nc.vo.annotation.MDEntityInfo(beanFullclassName =  "nc.scap.pub.vos.ScapMeetingVO" )
	public IVOMeta getMetaData() {
   		return null;
  	}
} 


