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
public class ScapLayoutVO extends SuperVO {
	private java.lang.String pk_layout;
	private java.lang.String code;
	private java.lang.String name;
	private java.lang.String controller;
	private java.lang.String external_js;
	private java.lang.String pk_group;
	private java.lang.String pk_org;
	private java.lang.Integer dr = 0;
	private nc.vo.pub.lang.UFDateTime ts;
	private nc.scap.pub.vos.ScapLayoutContainerVO[] layout_container;

	public static final String PK_LAYOUT = "pk_layout";
	public static final String CODE = "code";
	public static final String NAME = "name";
	public static final String CONTROLLER = "controller";
	public static final String EXTERNAL_JS = "external_js";
	public static final String PK_GROUP = "pk_group";
	public static final String PK_ORG = "pk_org";
			
	/**
	 * 属性pk_layout的Getter方法.属性名：主键
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getPk_layout () {
		return pk_layout;
	}   
	/**
	 * 属性pk_layout的Setter方法.属性名：主键
	 * 创建日期:
	 * @param newPk_layout java.lang.String
	 */
	public void setPk_layout (java.lang.String newPk_layout ) {
	 	this.pk_layout = newPk_layout;
	} 	  
	/**
	 * 属性code的Getter方法.属性名：编码
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getCode () {
		return code;
	}   
	/**
	 * 属性code的Setter方法.属性名：编码
	 * 创建日期:
	 * @param newCode java.lang.String
	 */
	public void setCode (java.lang.String newCode ) {
	 	this.code = newCode;
	} 	  
	/**
	 * 属性name的Getter方法.属性名：名称
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getName () {
		return name;
	}   
	/**
	 * 属性name的Setter方法.属性名：名称
	 * 创建日期:
	 * @param newName java.lang.String
	 */
	public void setName (java.lang.String newName ) {
	 	this.name = newName;
	} 	  
	/**
	 * 属性controller的Getter方法.属性名：控制类
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getController () {
		return controller;
	}   
	/**
	 * 属性controller的Setter方法.属性名：控制类
	 * 创建日期:
	 * @param newController java.lang.String
	 */
	public void setController (java.lang.String newController ) {
	 	this.controller = newController;
	} 	  
	/**
	 * 属性external_js的Getter方法.属性名：引用JS
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getExternal_js () {
		return external_js;
	}   
	/**
	 * 属性external_js的Setter方法.属性名：引用JS
	 * 创建日期:
	 * @param newExternal_js java.lang.String
	 */
	public void setExternal_js (java.lang.String newExternal_js ) {
	 	this.external_js = newExternal_js;
	} 	  
	/**
	 * 属性pk_group的Getter方法.属性名：集团
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getPk_group () {
		return pk_group;
	}   
	/**
	 * 属性pk_group的Setter方法.属性名：集团
	 * 创建日期:
	 * @param newPk_group java.lang.String
	 */
	public void setPk_group (java.lang.String newPk_group ) {
	 	this.pk_group = newPk_group;
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
	 * 属性layout_container的Getter方法.属性名：容器
	 * 创建日期:
	 * @return nc.scap.pub.vos.ScapLayoutContainerVO[]
	 */
	public nc.scap.pub.vos.ScapLayoutContainerVO[] getLayout_container () {
		return layout_container;
	}   
	/**
	 * 属性layout_container的Setter方法.属性名：容器
	 * 创建日期:
	 * @param newLayout_container nc.scap.pub.vos.ScapLayoutContainerVO[]
	 */
	public void setLayout_container (nc.scap.pub.vos.ScapLayoutContainerVO[] newLayout_container ) {
	 	this.layout_container = newLayout_container;
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
	  return "pk_layout";
	}
    
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "scap_layout";
	}    
	
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "scap_layout";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:
	  */
     public ScapLayoutVO() {
		super();	
	}    
	
	@nc.vo.annotation.MDEntityInfo(beanFullclassName =  "nc.scap.pub.vos.ScapLayoutVO" )
	public IVOMeta getMetaData() {
   		return null;
  	}
} 


