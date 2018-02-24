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
public class NewsVO extends SuperVO {
	private java.lang.String pk_news;
	private java.lang.String pk_org;
	private java.lang.String pk_group;
	private java.lang.String type;
	private java.lang.String title;
	private java.lang.String s_title;
	private java.lang.String l_title;
	private java.lang.String kyes;
	private java.lang.String desc;
	private java.lang.Object content;
	private java.lang.String billno;
	private java.lang.String formtitle;
	private java.lang.String billmaker;
	private nc.vo.pub.lang.UFDateTime billmakedate;
	private java.lang.String approver;
	private nc.vo.pub.lang.UFDateTime approvedate;
	private java.lang.String formstate;
	private java.lang.String xiajia;
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

	public static final String PK_NEWS = "pk_news";
	public static final String PK_ORG = "pk_org";
	public static final String PK_GROUP = "pk_group";
	public static final String TYPE = "type";
	public static final String TITLE = "title";
	public static final String S_TITLE = "s_title";
	public static final String L_TITLE = "l_title";
	public static final String KYES = "kyes";
	public static final String DESC = "desc";
	public static final String CONTENT = "content";
	public static final String BILLNO = "billno";
	public static final String FORMTITLE = "formtitle";
	public static final String BILLMAKER = "billmaker";
	public static final String BILLMAKEDATE = "billmakedate";
	public static final String APPROVER = "approver";
	public static final String APPROVEDATE = "approvedate";
	public static final String FORMSTATE = "formstate";
	public static final String XIAJIA = "xiajia";
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
	 * 属性pk_news的Getter方法.属性名：主键
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getPk_news () {
		return pk_news;
	}   
	/**
	 * 属性pk_news的Setter方法.属性名：主键
	 * 创建日期:
	 * @param newPk_news java.lang.String
	 */
	public void setPk_news (java.lang.String newPk_news ) {
	 	this.pk_news = newPk_news;
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
	 * 属性type的Getter方法.属性名：分类
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getType () {
		return type;
	}   
	/**
	 * 属性type的Setter方法.属性名：分类
	 * 创建日期:
	 * @param newType java.lang.String
	 */
	public void setType (java.lang.String newType ) {
	 	this.type = newType;
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
	 * 属性s_title的Getter方法.属性名：短标题
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getS_title () {
		return s_title;
	}   
	/**
	 * 属性s_title的Setter方法.属性名：短标题
	 * 创建日期:
	 * @param newS_title java.lang.String
	 */
	public void setS_title (java.lang.String newS_title ) {
	 	this.s_title = newS_title;
	} 	  
	/**
	 * 属性l_title的Getter方法.属性名：长标题
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getL_title () {
		return l_title;
	}   
	/**
	 * 属性l_title的Setter方法.属性名：长标题
	 * 创建日期:
	 * @param newL_title java.lang.String
	 */
	public void setL_title (java.lang.String newL_title ) {
	 	this.l_title = newL_title;
	} 	  
	/**
	 * 属性kyes的Getter方法.属性名：关键字
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getKyes () {
		return kyes;
	}   
	/**
	 * 属性kyes的Setter方法.属性名：关键字
	 * 创建日期:
	 * @param newKyes java.lang.String
	 */
	public void setKyes (java.lang.String newKyes ) {
	 	this.kyes = newKyes;
	} 	  
	/**
	 * 属性desc的Getter方法.属性名：简介
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getDesc () {
		return desc;
	}   
	/**
	 * 属性desc的Setter方法.属性名：简介
	 * 创建日期:
	 * @param newDesc java.lang.String
	 */
	public void setDesc (java.lang.String newDesc ) {
	 	this.desc = newDesc;
	} 	  
	/**
	 * 属性content的Getter方法.属性名：内容
	 * 创建日期:
	 * @return java.lang.Object
	 */
	public java.lang.Object getContent () {
		return content;
	}   
	/**
	 * 属性content的Setter方法.属性名：内容
	 * 创建日期:
	 * @param newContent java.lang.Object
	 */
	public void setContent (java.lang.Object newContent ) {
	 	this.content = newContent;
	} 	  
	/**
	 * 属性billno的Getter方法.属性名：单据编号
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getBillno () {
		return billno;
	}   
	/**
	 * 属性billno的Setter方法.属性名：单据编号
	 * 创建日期:
	 * @param newBillno java.lang.String
	 */
	public void setBillno (java.lang.String newBillno ) {
	 	this.billno = newBillno;
	} 	  
	/**
	 * 属性formtitle的Getter方法.属性名：单据标题
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getFormtitle () {
		return formtitle;
	}   
	/**
	 * 属性formtitle的Setter方法.属性名：单据标题
	 * 创建日期:
	 * @param newFormtitle java.lang.String
	 */
	public void setFormtitle (java.lang.String newFormtitle ) {
	 	this.formtitle = newFormtitle;
	} 	  
	/**
	 * 属性billmaker的Getter方法.属性名：制单人
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getBillmaker () {
		return billmaker;
	}   
	/**
	 * 属性billmaker的Setter方法.属性名：制单人
	 * 创建日期:
	 * @param newBillmaker java.lang.String
	 */
	public void setBillmaker (java.lang.String newBillmaker ) {
	 	this.billmaker = newBillmaker;
	} 	  
	/**
	 * 属性billmakedate的Getter方法.属性名：制单日期
	 * 创建日期:
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getBillmakedate () {
		return billmakedate;
	}   
	/**
	 * 属性billmakedate的Setter方法.属性名：制单日期
	 * 创建日期:
	 * @param newBillmakedate nc.vo.pub.lang.UFDateTime
	 */
	public void setBillmakedate (nc.vo.pub.lang.UFDateTime newBillmakedate ) {
	 	this.billmakedate = newBillmakedate;
	} 	  
	/**
	 * 属性approver的Getter方法.属性名：审批人
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getApprover () {
		return approver;
	}   
	/**
	 * 属性approver的Setter方法.属性名：审批人
	 * 创建日期:
	 * @param newApprover java.lang.String
	 */
	public void setApprover (java.lang.String newApprover ) {
	 	this.approver = newApprover;
	} 	  
	/**
	 * 属性approvedate的Getter方法.属性名：审批时间
	 * 创建日期:
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getApprovedate () {
		return approvedate;
	}   
	/**
	 * 属性approvedate的Setter方法.属性名：审批时间
	 * 创建日期:
	 * @param newApprovedate nc.vo.pub.lang.UFDateTime
	 */
	public void setApprovedate (nc.vo.pub.lang.UFDateTime newApprovedate ) {
	 	this.approvedate = newApprovedate;
	} 	  
	/**
	 * 属性formstate的Getter方法.属性名：单据状态
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getFormstate () {
		return formstate;
	}   
	/**
	 * 属性formstate的Setter方法.属性名：单据状态
	 * 创建日期:
	 * @param newFormstate java.lang.String
	 */
	public void setFormstate (java.lang.String newFormstate ) {
	 	this.formstate = newFormstate;
	} 	  
	/**
	 * 属性xiajia的Getter方法.属性名：是否显示
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getXiajia () {
		return xiajia;
	}   
	/**
	 * 属性xiajia的Setter方法.属性名：是否显示
	 * 创建日期:
	 * @param newXiajia java.lang.String
	 */
	public void setXiajia (java.lang.String newXiajia ) {
	 	this.xiajia = newXiajia;
	} 	  
	/**
	 * 属性def1的Getter方法.属性名：备用字段1
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getDef1 () {
		return def1;
	}   
	/**
	 * 属性def1的Setter方法.属性名：备用字段1
	 * 创建日期:
	 * @param newDef1 java.lang.String
	 */
	public void setDef1 (java.lang.String newDef1 ) {
	 	this.def1 = newDef1;
	} 	  
	/**
	 * 属性def2的Getter方法.属性名：备用字段2
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getDef2 () {
		return def2;
	}   
	/**
	 * 属性def2的Setter方法.属性名：备用字段2
	 * 创建日期:
	 * @param newDef2 java.lang.String
	 */
	public void setDef2 (java.lang.String newDef2 ) {
	 	this.def2 = newDef2;
	} 	  
	/**
	 * 属性def3的Getter方法.属性名：备用字段3
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getDef3 () {
		return def3;
	}   
	/**
	 * 属性def3的Setter方法.属性名：备用字段3
	 * 创建日期:
	 * @param newDef3 java.lang.String
	 */
	public void setDef3 (java.lang.String newDef3 ) {
	 	this.def3 = newDef3;
	} 	  
	/**
	 * 属性def4的Getter方法.属性名：备用字段4
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getDef4 () {
		return def4;
	}   
	/**
	 * 属性def4的Setter方法.属性名：备用字段4
	 * 创建日期:
	 * @param newDef4 java.lang.String
	 */
	public void setDef4 (java.lang.String newDef4 ) {
	 	this.def4 = newDef4;
	} 	  
	/**
	 * 属性def5的Getter方法.属性名：备用字段5
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getDef5 () {
		return def5;
	}   
	/**
	 * 属性def5的Setter方法.属性名：备用字段5
	 * 创建日期:
	 * @param newDef5 java.lang.String
	 */
	public void setDef5 (java.lang.String newDef5 ) {
	 	this.def5 = newDef5;
	} 	  
	/**
	 * 属性def6的Getter方法.属性名：备用字段6
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getDef6 () {
		return def6;
	}   
	/**
	 * 属性def6的Setter方法.属性名：备用字段6
	 * 创建日期:
	 * @param newDef6 java.lang.String
	 */
	public void setDef6 (java.lang.String newDef6 ) {
	 	this.def6 = newDef6;
	} 	  
	/**
	 * 属性def7的Getter方法.属性名：备用字段7
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getDef7 () {
		return def7;
	}   
	/**
	 * 属性def7的Setter方法.属性名：备用字段7
	 * 创建日期:
	 * @param newDef7 java.lang.String
	 */
	public void setDef7 (java.lang.String newDef7 ) {
	 	this.def7 = newDef7;
	} 	  
	/**
	 * 属性def8的Getter方法.属性名：备用字段8
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getDef8 () {
		return def8;
	}   
	/**
	 * 属性def8的Setter方法.属性名：备用字段8
	 * 创建日期:
	 * @param newDef8 java.lang.String
	 */
	public void setDef8 (java.lang.String newDef8 ) {
	 	this.def8 = newDef8;
	} 	  
	/**
	 * 属性def9的Getter方法.属性名：备用字段9
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getDef9 () {
		return def9;
	}   
	/**
	 * 属性def9的Setter方法.属性名：备用字段9
	 * 创建日期:
	 * @param newDef9 java.lang.String
	 */
	public void setDef9 (java.lang.String newDef9 ) {
	 	this.def9 = newDef9;
	} 	  
	/**
	 * 属性def10的Getter方法.属性名：备用字段10
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getDef10 () {
		return def10;
	}   
	/**
	 * 属性def10的Setter方法.属性名：备用字段10
	 * 创建日期:
	 * @param newDef10 java.lang.String
	 */
	public void setDef10 (java.lang.String newDef10 ) {
	 	this.def10 = newDef10;
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
	  return "pk_news";
	}
    
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "scap_news";
	}    
	
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "scap_news";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:
	  */
     public NewsVO() {
		super();	
	}    
	
	@nc.vo.annotation.MDEntityInfo(beanFullclassName =  "nc.scap.pub.vos.NewsVO" )
	public IVOMeta getMetaData() {
   		return null;
  	}
} 


