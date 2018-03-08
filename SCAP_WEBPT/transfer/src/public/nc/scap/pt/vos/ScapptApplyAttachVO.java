/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product. It is based on Velocity.     *
\***************************************************************/
package nc.scap.pt.vos;
	
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
public class ScapptApplyAttachVO extends SuperVO {
	private java.lang.String pk_apply_h;
	private java.lang.String pk_apply_attach;
	private java.lang.String attach_name;
	private java.lang.Integer num;
	private java.lang.String submitstate;
	private java.lang.String def1;
	private java.lang.String def2;
	private java.lang.String def3;
	private java.lang.String def4;
	private java.lang.String def5;
	private java.lang.Integer dr = 0;
	private nc.vo.pub.lang.UFDateTime ts;

	public static final String PK_APPLY_H = "pk_apply_h";
	public static final String PK_APPLY_ATTACH = "pk_apply_attach";
	public static final String ATTACH_NAME = "attach_name";
	public static final String NUM = "num";
	public static final String SUBMITSTATE = "submitstate";
	public static final String DEF1 = "def1";
	public static final String DEF2 = "def2";
	public static final String DEF3 = "def3";
	public static final String DEF4 = "def4";
	public static final String DEF5 = "def5";
			
	/**
	 * 属性pk_apply_h的Getter方法.属性名：parentPK
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getPk_apply_h () {
		return pk_apply_h;
	}   
	/**
	 * 属性pk_apply_h的Setter方法.属性名：parentPK
	 * 创建日期:
	 * @param newPk_apply_h java.lang.String
	 */
	public void setPk_apply_h (java.lang.String newPk_apply_h ) {
	 	this.pk_apply_h = newPk_apply_h;
	} 	  
	/**
	 * 属性pk_apply_attach的Getter方法.属性名：主键
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getPk_apply_attach () {
		return pk_apply_attach;
	}   
	/**
	 * 属性pk_apply_attach的Setter方法.属性名：主键
	 * 创建日期:
	 * @param newPk_apply_attach java.lang.String
	 */
	public void setPk_apply_attach (java.lang.String newPk_apply_attach ) {
	 	this.pk_apply_attach = newPk_apply_attach;
	} 	  
	/**
	 * 属性attach_name的Getter方法.属性名：名称
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getAttach_name () {
		return attach_name;
	}   
	/**
	 * 属性attach_name的Setter方法.属性名：名称
	 * 创建日期:
	 * @param newAttach_name java.lang.String
	 */
	public void setAttach_name (java.lang.String newAttach_name ) {
	 	this.attach_name = newAttach_name;
	} 	  
	/**
	 * 属性num的Getter方法.属性名：数量
	 * 创建日期:
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getNum () {
		return num;
	}   
	/**
	 * 属性num的Setter方法.属性名：数量
	 * 创建日期:
	 * @param newNum java.lang.Integer
	 */
	public void setNum (java.lang.Integer newNum ) {
	 	this.num = newNum;
	} 	  
	/**
	 * 属性submitstate的Getter方法.属性名：是否报送
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getSubmitstate () {
		return submitstate;
	}   
	/**
	 * 属性submitstate的Setter方法.属性名：是否报送
	 * 创建日期:
	 * @param newSubmitstate java.lang.String
	 */
	public void setSubmitstate (java.lang.String newSubmitstate ) {
	 	this.submitstate = newSubmitstate;
	} 	  
	/**
	 * 属性def1的Getter方法.属性名：自定义字段1
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getDef1 () {
		return def1;
	}   
	/**
	 * 属性def1的Setter方法.属性名：自定义字段1
	 * 创建日期:
	 * @param newDef1 java.lang.String
	 */
	public void setDef1 (java.lang.String newDef1 ) {
	 	this.def1 = newDef1;
	} 	  
	/**
	 * 属性def2的Getter方法.属性名：自定义字段2
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getDef2 () {
		return def2;
	}   
	/**
	 * 属性def2的Setter方法.属性名：自定义字段2
	 * 创建日期:
	 * @param newDef2 java.lang.String
	 */
	public void setDef2 (java.lang.String newDef2 ) {
	 	this.def2 = newDef2;
	} 	  
	/**
	 * 属性def3的Getter方法.属性名：自定义字段3
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getDef3 () {
		return def3;
	}   
	/**
	 * 属性def3的Setter方法.属性名：自定义字段3
	 * 创建日期:
	 * @param newDef3 java.lang.String
	 */
	public void setDef3 (java.lang.String newDef3 ) {
	 	this.def3 = newDef3;
	} 	  
	/**
	 * 属性def4的Getter方法.属性名：自定义字段4
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getDef4 () {
		return def4;
	}   
	/**
	 * 属性def4的Setter方法.属性名：自定义字段4
	 * 创建日期:
	 * @param newDef4 java.lang.String
	 */
	public void setDef4 (java.lang.String newDef4 ) {
	 	this.def4 = newDef4;
	} 	  
	/**
	 * 属性def5的Getter方法.属性名：自定义字段5
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getDef5 () {
		return def5;
	}   
	/**
	 * 属性def5的Setter方法.属性名：自定义字段5
	 * 创建日期:
	 * @param newDef5 java.lang.String
	 */
	public void setDef5 (java.lang.String newDef5 ) {
	 	this.def5 = newDef5;
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
		return "pk_apply_h";
	}   
    
	/**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	  return "pk_apply_attach";
	}
    
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "scappt_apply_attach";
	}    
	
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "scappt_apply_attach";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:
	  */
     public ScapptApplyAttachVO() {
		super();	
	}    
	
	@nc.vo.annotation.MDEntityInfo(beanFullclassName =  "nc.scap.pt.vos.ScapptApplyAttachVO" )
	public IVOMeta getMetaData() {
   		return null;
  	}
} 


