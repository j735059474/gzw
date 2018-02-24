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
public class ScapOfficialdocVO extends SuperVO {
	private java.lang.String pk_officialdoc;
	private java.lang.String pk_org;
	private java.lang.String pk_group;
	private java.lang.String title;
	private java.lang.String doctype;
	private java.lang.String urgency;
	private java.lang.String senddocorg;
	private nc.vo.pub.lang.UFDate senddocdate;
	private java.lang.String senddocno;
	private java.lang.String isfeedback;
	private java.lang.String ismessage;
	private java.lang.String messagecc;
	private java.lang.String message;
	private java.lang.String memo;
	private java.lang.String state;
	private java.lang.String vbdef1;
	private java.lang.String vbdef2;
	private java.lang.String vbdef3;
	private java.lang.String vbdef4;
	private java.lang.String vbdef5;
	private java.lang.String vbdef6;
	private java.lang.String vbdef7;
	private java.lang.String vbdef8;
	private nc.vo.pub.lang.UFDate vbdef9;
	private nc.vo.pub.lang.UFDate vbdef10;
	private java.lang.Integer dr = 0;
	private nc.vo.pub.lang.UFDateTime ts;
	private nc.scap.pub.vos.ScapFeedBackVO[] feedbacks;
	private nc.scap.pub.vos.ScapFeedBackUserVO[] feedbackusers;

	public static final String PK_OFFICIALDOC = "pk_officialdoc";
	public static final String PK_ORG = "pk_org";
	public static final String PK_GROUP = "pk_group";
	public static final String TITLE = "title";
	public static final String DOCTYPE = "doctype";
	public static final String URGENCY = "urgency";
	public static final String SENDDOCORG = "senddocorg";
	public static final String SENDDOCDATE = "senddocdate";
	public static final String SENDDOCNO = "senddocno";
	public static final String ISFEEDBACK = "isfeedback";
	public static final String ISMESSAGE = "ismessage";
	public static final String MESSAGECC = "messagecc";
	public static final String MESSAGE = "message";
	public static final String MEMO = "memo";
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
	 * 属性pk_officialdoc的Getter方法.属性名：主键
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getPk_officialdoc () {
		return pk_officialdoc;
	}   
	/**
	 * 属性pk_officialdoc的Setter方法.属性名：主键
	 * 创建日期:
	 * @param newPk_officialdoc java.lang.String
	 */
	public void setPk_officialdoc (java.lang.String newPk_officialdoc ) {
	 	this.pk_officialdoc = newPk_officialdoc;
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
	 * 属性title的Getter方法.属性名：标题
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getTitle () {
		return title;
	}   
	/**
	 * 属性title的Setter方法.属性名：标题
	 * 创建日期:
	 * @param newTitle java.lang.String
	 */
	public void setTitle (java.lang.String newTitle ) {
	 	this.title = newTitle;
	} 	  
	/**
	 * 属性doctype的Getter方法.属性名：文种
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getDoctype () {
		return doctype;
	}   
	/**
	 * 属性doctype的Setter方法.属性名：文种
	 * 创建日期:
	 * @param newDoctype java.lang.String
	 */
	public void setDoctype (java.lang.String newDoctype ) {
	 	this.doctype = newDoctype;
	} 	  
	/**
	 * 属性urgency的Getter方法.属性名：缓急
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getUrgency () {
		return urgency;
	}   
	/**
	 * 属性urgency的Setter方法.属性名：缓急
	 * 创建日期:
	 * @param newUrgency java.lang.String
	 */
	public void setUrgency (java.lang.String newUrgency ) {
	 	this.urgency = newUrgency;
	} 	  
	/**
	 * 属性senddocorg的Getter方法.属性名：发文机关
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getSenddocorg () {
		return senddocorg;
	}   
	/**
	 * 属性senddocorg的Setter方法.属性名：发文机关
	 * 创建日期:
	 * @param newSenddocorg java.lang.String
	 */
	public void setSenddocorg (java.lang.String newSenddocorg ) {
	 	this.senddocorg = newSenddocorg;
	} 	  
	/**
	 * 属性senddocdate的Getter方法.属性名：发文日期
	 * 创建日期:
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getSenddocdate () {
		return senddocdate;
	}   
	/**
	 * 属性senddocdate的Setter方法.属性名：发文日期
	 * 创建日期:
	 * @param newSenddocdate nc.vo.pub.lang.UFDate
	 */
	public void setSenddocdate (nc.vo.pub.lang.UFDate newSenddocdate ) {
	 	this.senddocdate = newSenddocdate;
	} 	  
	/**
	 * 属性senddocno的Getter方法.属性名：发文字号
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getSenddocno () {
		return senddocno;
	}   
	/**
	 * 属性senddocno的Setter方法.属性名：发文字号
	 * 创建日期:
	 * @param newSenddocno java.lang.String
	 */
	public void setSenddocno (java.lang.String newSenddocno ) {
	 	this.senddocno = newSenddocno;
	} 	  
	/**
	 * 属性isfeedback的Getter方法.属性名：是否反馈
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getIsfeedback () {
		return isfeedback;
	}   
	/**
	 * 属性isfeedback的Setter方法.属性名：是否反馈
	 * 创建日期:
	 * @param newIsfeedback java.lang.String
	 */
	public void setIsfeedback (java.lang.String newIsfeedback ) {
	 	this.isfeedback = newIsfeedback;
	} 	  
	/**
	 * 属性ismessage的Getter方法.属性名：是否发短信
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getIsmessage () {
		return ismessage;
	}   
	/**
	 * 属性ismessage的Setter方法.属性名：是否发短信
	 * 创建日期:
	 * @param newIsmessage java.lang.String
	 */
	public void setIsmessage (java.lang.String newIsmessage ) {
	 	this.ismessage = newIsmessage;
	} 	  
	/**
	 * 属性messagecc的Getter方法.属性名：短信抄送
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getMessagecc () {
		return messagecc;
	}   
	/**
	 * 属性messagecc的Setter方法.属性名：短信抄送
	 * 创建日期:
	 * @param newMessagecc java.lang.String
	 */
	public void setMessagecc (java.lang.String newMessagecc ) {
	 	this.messagecc = newMessagecc;
	} 	  
	/**
	 * 属性message的Getter方法.属性名：短信内容
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getMessage () {
		return message;
	}   
	/**
	 * 属性message的Setter方法.属性名：短信内容
	 * 创建日期:
	 * @param newMessage java.lang.String
	 */
	public void setMessage (java.lang.String newMessage ) {
	 	this.message = newMessage;
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
	 * 属性state的Getter方法.属性名：状态
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getState () {
		return state;
	}   
	/**
	 * 属性state的Setter方法.属性名：状态
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
	 * @return java.lang.String
	 */
	public java.lang.String getVbdef6 () {
		return vbdef6;
	}   
	/**
	 * 属性vbdef6的Setter方法.属性名：自定义项6
	 * 创建日期:
	 * @param newVbdef6 java.lang.String
	 */
	public void setVbdef6 (java.lang.String newVbdef6 ) {
	 	this.vbdef6 = newVbdef6;
	} 	  
	/**
	 * 属性vbdef7的Getter方法.属性名：自定义项7
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getVbdef7 () {
		return vbdef7;
	}   
	/**
	 * 属性vbdef7的Setter方法.属性名：自定义项7
	 * 创建日期:
	 * @param newVbdef7 java.lang.String
	 */
	public void setVbdef7 (java.lang.String newVbdef7 ) {
	 	this.vbdef7 = newVbdef7;
	} 	  
	/**
	 * 属性vbdef8的Getter方法.属性名：自定义项8
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getVbdef8 () {
		return vbdef8;
	}   
	/**
	 * 属性vbdef8的Setter方法.属性名：自定义项8
	 * 创建日期:
	 * @param newVbdef8 java.lang.String
	 */
	public void setVbdef8 (java.lang.String newVbdef8 ) {
	 	this.vbdef8 = newVbdef8;
	} 	  
	/**
	 * 属性vbdef9的Getter方法.属性名：自定义项9
	 * 创建日期:
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getVbdef9 () {
		return vbdef9;
	}   
	/**
	 * 属性vbdef9的Setter方法.属性名：自定义项9
	 * 创建日期:
	 * @param newVbdef9 nc.vo.pub.lang.UFDate
	 */
	public void setVbdef9 (nc.vo.pub.lang.UFDate newVbdef9 ) {
	 	this.vbdef9 = newVbdef9;
	} 	  
	/**
	 * 属性vbdef10的Getter方法.属性名：自定义项10
	 * 创建日期:
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getVbdef10 () {
		return vbdef10;
	}   
	/**
	 * 属性vbdef10的Setter方法.属性名：自定义项10
	 * 创建日期:
	 * @param newVbdef10 nc.vo.pub.lang.UFDate
	 */
	public void setVbdef10 (nc.vo.pub.lang.UFDate newVbdef10 ) {
	 	this.vbdef10 = newVbdef10;
	} 	  
	/**
	 * 属性feedbacks的Getter方法.属性名：feedbacks
	 * 创建日期:
	 * @return nc.scap.pub.vos.ScapFeedBackVO[]
	 */
	public nc.scap.pub.vos.ScapFeedBackVO[] getFeedbacks () {
		return feedbacks;
	}   
	/**
	 * 属性feedbacks的Setter方法.属性名：feedbacks
	 * 创建日期:
	 * @param newFeedbacks nc.scap.pub.vos.ScapFeedBackVO[]
	 */
	public void setFeedbacks (nc.scap.pub.vos.ScapFeedBackVO[] newFeedbacks ) {
	 	this.feedbacks = newFeedbacks;
	} 	  
	/**
	 * 属性feedbackusers的Getter方法.属性名：feedbackusers
	 * 创建日期:
	 * @return nc.scap.pub.vos.ScapFeedBackUserVO[]
	 */
	public nc.scap.pub.vos.ScapFeedBackUserVO[] getFeedbackusers () {
		return feedbackusers;
	}   
	/**
	 * 属性feedbackusers的Setter方法.属性名：feedbackusers
	 * 创建日期:
	 * @param newFeedbackusers nc.scap.pub.vos.ScapFeedBackUserVO[]
	 */
	public void setFeedbackusers (nc.scap.pub.vos.ScapFeedBackUserVO[] newFeedbackusers ) {
	 	this.feedbackusers = newFeedbackusers;
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
	  return "pk_officialdoc";
	}
    
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "scapco_officialdoc";
	}    
	
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "scapco_officialdoc";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:
	  */
     public ScapOfficialdocVO() {
		super();	
	}    
	
	@nc.vo.annotation.MDEntityInfo(beanFullclassName =  "nc.scap.pub.vos.ScapOfficialdocVO" )
	public IVOMeta getMetaData() {
   		return null;
  	}
} 


