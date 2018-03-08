package nc.scap.transfer.trs.wfm;

import nc.uap.wfm.vo.WfmFormInfoCtx;
import nc.scap.pt.vos.ScapptTransferHVO;


public class WfmFlwFormVO extends ScapptTransferHVO implements WfmFormInfoCtx {
	private static final long serialVersionUID = 1L;
             
    /**
     * 返回制单日期
	 * @return
     */   
             
	@Override
	public String getBillMakeDateField() {
		return "billmakedate";
	}


    /**
     * 返回制单人PK
	 * @return
     */   
           
	@Override
	public String getBillMakeUserField() {
		return "billmaker";
	}

    /**
     * 返回单据号
	 * @return
     */   
          
	@Override
	public String getBillMakeNumbField() {
		return "billno";
	}

    /**
     * 返回制单部门PK
	 * @return
     */   
           
	@Override
	public String getBillMakeDeptField() {
		return "pk_org";
	}


    /**
     * 返回审核人PK
	 * @return
     */   
        
	@Override
	public String getFrmAuditUserField() {
		return "approver";
	}


    /**
     * 返回单据审核时间
	 * @return
     */   
     
	@Override
	public String getFrmAuditDateField() {
		return "approvedate";
	}


    /**
     * 返回form单据的状态
	 * @return
     */
   
	@Override
	public String getFrmStateField() {
		return "formstate";
	}


    /**
     * 返回单据标题
	 * @return
     */
      
	@Override
	public String getFrmTitileField() {
		
		return "formtitle";
	}


    /**
     * 返回单据主键
	 * @return
     */   
	@Override
	public String getFrmInsPk() {
		return (String)getAttributeValue("pk_transfer_h");
	}

	@Override
	public Object getAttributeValue(String key) {
		return super.getAttributeValue(key);
	}

	@Override
	public void setAttributeValue(String name, Object value) {
		super.setAttributeValue(name, value);
	}

	@Override
	public String[] getAttributeNames() {
		return super.getAttributeNames();
	}
	
	@Override
	public Object getAllLevelAttributeValue(String attribute,String beanID){		
		return getAttributeValue(attribute);
	}	


}