/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product. It is based on Velocity.     *
\***************************************************************/
package nc.scap.pt.vos;
	
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
public class ScapptTransferAssessVO extends SuperVO {
	private java.lang.String pk_transfer_h;
	private java.lang.String pk_transfer_assess;
	private java.lang.String vproject;
	private java.lang.String vunit;
	private java.lang.String pk_currency;
	private nc.vo.pub.lang.UFDouble dcarryingvalue;
	private nc.vo.pub.lang.UFDouble dassessvalue;
	private nc.vo.pub.lang.UFDouble dchangepercent;
	private java.lang.String vbdef5;
	private java.lang.String vbdef4;
	private java.lang.String vbdef3;
	private java.lang.String vbdef1;
	private java.lang.String vbdef2;
	private java.lang.Integer dr = 0;
	private nc.vo.pub.lang.UFDateTime ts;

	public static final String PK_TRANSFER_H = "pk_transfer_h";
	public static final String PK_TRANSFER_ASSESS = "pk_transfer_assess";
	public static final String VPROJECT = "vproject";
	public static final String VUNIT = "vunit";
	public static final String PK_CURRENCY = "pk_currency";
	public static final String DCARRYINGVALUE = "dcarryingvalue";
	public static final String DASSESSVALUE = "dassessvalue";
	public static final String DCHANGEPERCENT = "dchangepercent";
	public static final String VBDEF5 = "vbdef5";
	public static final String VBDEF4 = "vbdef4";
	public static final String VBDEF3 = "vbdef3";
	public static final String VBDEF1 = "vbdef1";
	public static final String VBDEF2 = "vbdef2";
			
	/**
	 * ����pk_transfer_h��Getter����.��������parentPK
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getPk_transfer_h () {
		return pk_transfer_h;
	}   
	/**
	 * ����pk_transfer_h��Setter����.��������parentPK
	 * ��������:
	 * @param newPk_transfer_h java.lang.String
	 */
	public void setPk_transfer_h (java.lang.String newPk_transfer_h ) {
	 	this.pk_transfer_h = newPk_transfer_h;
	} 	  
	/**
	 * ����pk_transfer_assess��Getter����.������������
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getPk_transfer_assess () {
		return pk_transfer_assess;
	}   
	/**
	 * ����pk_transfer_assess��Setter����.������������
	 * ��������:
	 * @param newPk_transfer_assess java.lang.String
	 */
	public void setPk_transfer_assess (java.lang.String newPk_transfer_assess ) {
	 	this.pk_transfer_assess = newPk_transfer_assess;
	} 	  
	/**
	 * ����vproject��Getter����.����������Ŀ
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getVproject () {
		return vproject;
	}   
	/**
	 * ����vproject��Setter����.����������Ŀ
	 * ��������:
	 * @param newVproject java.lang.String
	 */
	public void setVproject (java.lang.String newVproject ) {
	 	this.vproject = newVproject;
	} 	  
	/**
	 * ����vunit��Getter����.����������λ
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getVunit () {
		return vunit;
	}   
	/**
	 * ����vunit��Setter����.����������λ
	 * ��������:
	 * @param newVunit java.lang.String
	 */
	public void setVunit (java.lang.String newVunit ) {
	 	this.vunit = newVunit;
	} 	  
	/**
	 * ����pk_currency��Getter����.������������
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getPk_currency () {
		return pk_currency;
	}   
	/**
	 * ����pk_currency��Setter����.������������
	 * ��������:
	 * @param newPk_currency java.lang.String
	 */
	public void setPk_currency (java.lang.String newPk_currency ) {
	 	this.pk_currency = newPk_currency;
	} 	  
	/**
	 * ����dcarryingvalue��Getter����.�������������ֵ(��Ԫ)
	 * ��������:
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getDcarryingvalue () {
		return dcarryingvalue;
	}   
	/**
	 * ����dcarryingvalue��Setter����.�������������ֵ(��Ԫ)
	 * ��������:
	 * @param newDcarryingvalue nc.vo.pub.lang.UFDouble
	 */
	public void setDcarryingvalue (nc.vo.pub.lang.UFDouble newDcarryingvalue ) {
	 	this.dcarryingvalue = newDcarryingvalue;
	} 	  
	/**
	 * ����dassessvalue��Getter����.��������������ֵ(��Ԫ)
	 * ��������:
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getDassessvalue () {
		return dassessvalue;
	}   
	/**
	 * ����dassessvalue��Setter����.��������������ֵ(��Ԫ)
	 * ��������:
	 * @param newDassessvalue nc.vo.pub.lang.UFDouble
	 */
	public void setDassessvalue (nc.vo.pub.lang.UFDouble newDassessvalue ) {
	 	this.dassessvalue = newDassessvalue;
	} 	  
	/**
	 * ����dchangepercent��Getter����.��������������ֵ��(%)
	 * ��������:
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getDchangepercent () {
		return dchangepercent;
	}   
	/**
	 * ����dchangepercent��Setter����.��������������ֵ��(%)
	 * ��������:
	 * @param newDchangepercent nc.vo.pub.lang.UFDouble
	 */
	public void setDchangepercent (nc.vo.pub.lang.UFDouble newDchangepercent ) {
	 	this.dchangepercent = newDchangepercent;
	} 	  
	/**
	 * ����vbdef5��Getter����.���������Զ�����5
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getVbdef5 () {
		return vbdef5;
	}   
	/**
	 * ����vbdef5��Setter����.���������Զ�����5
	 * ��������:
	 * @param newVbdef5 java.lang.String
	 */
	public void setVbdef5 (java.lang.String newVbdef5 ) {
	 	this.vbdef5 = newVbdef5;
	} 	  
	/**
	 * ����vbdef4��Getter����.���������Զ�����4
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getVbdef4 () {
		return vbdef4;
	}   
	/**
	 * ����vbdef4��Setter����.���������Զ�����4
	 * ��������:
	 * @param newVbdef4 java.lang.String
	 */
	public void setVbdef4 (java.lang.String newVbdef4 ) {
	 	this.vbdef4 = newVbdef4;
	} 	  
	/**
	 * ����vbdef3��Getter����.���������Զ�����3
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getVbdef3 () {
		return vbdef3;
	}   
	/**
	 * ����vbdef3��Setter����.���������Զ�����3
	 * ��������:
	 * @param newVbdef3 java.lang.String
	 */
	public void setVbdef3 (java.lang.String newVbdef3 ) {
	 	this.vbdef3 = newVbdef3;
	} 	  
	/**
	 * ����vbdef1��Getter����.���������Զ�����1
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getVbdef1 () {
		return vbdef1;
	}   
	/**
	 * ����vbdef1��Setter����.���������Զ�����1
	 * ��������:
	 * @param newVbdef1 java.lang.String
	 */
	public void setVbdef1 (java.lang.String newVbdef1 ) {
	 	this.vbdef1 = newVbdef1;
	} 	  
	/**
	 * ����vbdef2��Getter����.���������Զ�����2
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getVbdef2 () {
		return vbdef2;
	}   
	/**
	 * ����vbdef2��Setter����.���������Զ�����2
	 * ��������:
	 * @param newVbdef2 java.lang.String
	 */
	public void setVbdef2 (java.lang.String newVbdef2 ) {
	 	this.vbdef2 = newVbdef2;
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
		return "pk_transfer_h";
	}   
    
	/**
	  * <p>ȡ�ñ�����.
	  * <p>
	  * ��������:
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	  return "pk_transfer_assess";
	}
    
	/**
	 * <p>���ر�����.
	 * <p>
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "scappt_transfer_assess";
	}    
	
	/**
	 * <p>���ر�����.
	 * <p>
	 * ��������:
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "scappt_transfer_assess";
	}    
    
    /**
	  * ����Ĭ�Ϸ�ʽ����������.
	  *
	  * ��������:
	  */
     public ScapptTransferAssessVO() {
		super();	
	}    
	
	@nc.vo.annotation.MDEntityInfo(beanFullclassName =  "nc.scap.pt.vos.ScapptTransferAssessVO" )
	public IVOMeta getMetaData() {
   		return null;
  	}
} 

