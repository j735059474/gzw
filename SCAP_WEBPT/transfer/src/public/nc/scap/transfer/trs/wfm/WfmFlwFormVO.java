package nc.scap.transfer.trs.wfm;

import nc.uap.wfm.vo.WfmFormInfoCtx;
import nc.scap.pt.vos.ScapptTransferHVO;


public class WfmFlwFormVO extends ScapptTransferHVO implements WfmFormInfoCtx {
	private static final long serialVersionUID = 1L;
             
    /**
     * �����Ƶ�����
	 * @return
     */   
             
	@Override
	public String getBillMakeDateField() {
		return "billmakedate";
	}


    /**
     * �����Ƶ���PK
	 * @return
     */   
           
	@Override
	public String getBillMakeUserField() {
		return "billmaker";
	}

    /**
     * ���ص��ݺ�
	 * @return
     */   
          
	@Override
	public String getBillMakeNumbField() {
		return "billno";
	}

    /**
     * �����Ƶ�����PK
	 * @return
     */   
           
	@Override
	public String getBillMakeDeptField() {
		return "pk_org";
	}


    /**
     * ���������PK
	 * @return
     */   
        
	@Override
	public String getFrmAuditUserField() {
		return "approver";
	}


    /**
     * ���ص������ʱ��
	 * @return
     */   
     
	@Override
	public String getFrmAuditDateField() {
		return "approvedate";
	}


    /**
     * ����form���ݵ�״̬
	 * @return
     */
   
	@Override
	public String getFrmStateField() {
		return "formstate";
	}


    /**
     * ���ص��ݱ���
	 * @return
     */
      
	@Override
	public String getFrmTitileField() {
		
		return "formtitle";
	}


    /**
     * ���ص�������
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