package com.scap.pub.attlist;



public class AttachParam {
	
	public String billItem;
	public String billType;
	public boolean canUpload = false;
	public boolean canDownload = true;
	public boolean canDel = false;
	public boolean canEdit = false;
	
	/**
	 * 构造附件控制参数类对象，传入主单据主键
	 * @param pk_bill_item
	 */
	public AttachParam(String pk_bill_item) {
		this.billItem = pk_bill_item;
	}
	
	public AttachParam(String pk_bill_item, boolean canUpload, boolean canDownload, boolean canDel) {
		this.billItem = pk_bill_item;
		this.canUpload = canUpload;
		this.canDownload = canDownload;
		this.canDel = canDel;
	}

	public String getBillItem() {
		return billItem;
	}

	public void setBillItem(String billItem) {
		this.billItem = billItem;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public boolean getCanUpload() {
		return canUpload;
	}

	public void setCanUpload(boolean canUpload) {
		this.canUpload = canUpload;
	}

	public boolean getCanDownload() {
		return canDownload;
	}

	public void setCanDownload(boolean canDownload) {
		this.canDownload = canDownload;
	}

	public boolean getCanEdit() {
		return canEdit;
	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}

	public boolean getCanDel() {
		return canDel;
	}

	public void setCanDel(boolean canDel) {
		this.canDel = canDel;
	}
	
}
