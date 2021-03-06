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
public class ScapSmsVO extends SuperVO {
	private java.lang.String pk_task;
	private java.lang.String pk_sms;
	private java.lang.String address;
	private java.lang.String sendstatus;
	private java.lang.Integer sendtimes;
	private nc.vo.pub.lang.UFDateTime createtime;
	private nc.vo.pub.lang.UFDateTime sendtime;
	private java.lang.Integer dr = 0;
	private nc.vo.pub.lang.UFDateTime ts;

	public static final String PK_TASK = "pk_task";
	public static final String PK_SMS = "pk_sms";
	public static final String ADDRESS = "address";
	public static final String SENDSTATUS = "sendstatus";
	public static final String SENDTIMES = "sendtimes";
	public static final String CREATETIME = "createtime";
	public static final String SENDTIME = "sendtime";
			
	/**
	 * 属性pk_task的Getter方法.属性名：parentPK
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getPk_task () {
		return pk_task;
	}   
	/**
	 * 属性pk_task的Setter方法.属性名：parentPK
	 * 创建日期:
	 * @param newPk_task java.lang.String
	 */
	public void setPk_task (java.lang.String newPk_task ) {
	 	this.pk_task = newPk_task;
	} 	  
	/**
	 * 属性pk_sms的Getter方法.属性名：主键
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getPk_sms () {
		return pk_sms;
	}   
	/**
	 * 属性pk_sms的Setter方法.属性名：主键
	 * 创建日期:
	 * @param newPk_sms java.lang.String
	 */
	public void setPk_sms (java.lang.String newPk_sms ) {
	 	this.pk_sms = newPk_sms;
	} 	  
	/**
	 * 属性address的Getter方法.属性名：收件人
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getAddress () {
		return address;
	}   
	/**
	 * 属性address的Setter方法.属性名：收件人
	 * 创建日期:
	 * @param newAddress java.lang.String
	 */
	public void setAddress (java.lang.String newAddress ) {
	 	this.address = newAddress;
	} 	  
	/**
	 * 属性sendstatus的Getter方法.属性名：发送状态
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getSendstatus () {
		return sendstatus;
	}   
	/**
	 * 属性sendstatus的Setter方法.属性名：发送状态
	 * 创建日期:
	 * @param newSendstatus java.lang.String
	 */
	public void setSendstatus (java.lang.String newSendstatus ) {
	 	this.sendstatus = newSendstatus;
	} 	  
	/**
	 * 属性sendtimes的Getter方法.属性名：发送次数
	 * 创建日期:
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getSendtimes () {
		return sendtimes;
	}   
	/**
	 * 属性sendtimes的Setter方法.属性名：发送次数
	 * 创建日期:
	 * @param newSendtimes java.lang.Integer
	 */
	public void setSendtimes (java.lang.Integer newSendtimes ) {
	 	this.sendtimes = newSendtimes;
	} 	  
	/**
	 * 属性createtime的Getter方法.属性名：创建日期
	 * 创建日期:
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getCreatetime () {
		return createtime;
	}   
	/**
	 * 属性createtime的Setter方法.属性名：创建日期
	 * 创建日期:
	 * @param newCreatetime nc.vo.pub.lang.UFDateTime
	 */
	public void setCreatetime (nc.vo.pub.lang.UFDateTime newCreatetime ) {
	 	this.createtime = newCreatetime;
	} 	  
	/**
	 * 属性sendtime的Getter方法.属性名：发送日期
	 * 创建日期:
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getSendtime () {
		return sendtime;
	}   
	/**
	 * 属性sendtime的Setter方法.属性名：发送日期
	 * 创建日期:
	 * @param newSendtime nc.vo.pub.lang.UFDateTime
	 */
	public void setSendtime (nc.vo.pub.lang.UFDateTime newSendtime ) {
	 	this.sendtime = newSendtime;
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
		return "pk_task";
	}   
    
	/**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	  return "pk_sms";
	}
    
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "scapco_sms";
	}    
	
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "scapco_sms";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:
	  */
     public ScapSmsVO() {
		super();	
	}    
	
	@nc.vo.annotation.MDEntityInfo(beanFullclassName =  "nc.scap.pub.vos.ScapSmsVO" )
	public IVOMeta getMetaData() {
   		return null;
  	}
} 


