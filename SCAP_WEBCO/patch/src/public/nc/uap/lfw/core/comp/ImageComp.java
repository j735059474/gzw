package nc.uap.lfw.core.comp;

import nc.uap.lfw.core.common.CompAttrConstants;
import nc.uap.lfw.core.exception.LfwRuntimeException;

import uap.json.JSONObject;

/**
 * Image�ؼ�����
 * @author dengjt
 *
 */
public class ImageComp extends WebComponent {

	private static final long serialVersionUID = 5777383343036006996L;
	private String alt = "";
	private String image1;
	private String image2;
	// ͼƬ·���Ƿ�ı�
	private boolean image1Changed = true;
	// ͼƬ��ʵ·������context��ʹ��
	private String realImage1;
	// ͼƬ·���Ƿ�ı�
	private boolean image2Changed = true;
	// ͼƬ��ʵ·������context��ʹ��
	private String realImage2;

	private String imageInact;
	private boolean floatRight = false;
	private boolean floatLeft = false;
	
	private boolean maxShow = false;
	
	//�Ƿ�֧���ϴ�
	private boolean canUpload = false;
	//���ϴ���
	private String sysid;
	//���ϴ���
	private String baModule;
	
	public boolean isMaxShow() {
		return maxShow;
	}
	public void setMaxShow(boolean maxShow) {
		this.maxShow = maxShow;
	}
	public String getAlt() {
		return alt;
	}
	public void setAlt(String alt) {
		this.alt = alt;
	}
	public boolean isFloatLeft() {
		return floatLeft;
	}
	public void setFloatLeft(boolean floatLeft) {
		this.floatLeft = floatLeft;
	}
	public boolean isFloatRight() {
		return floatRight;
	}
	public void setFloatRight(boolean floatRight) {
		this.floatRight = floatRight;
	}
	public String getImage1() {
		return image1;
	}
	public void setImage1(String image1) {
		if (!image1.equals(this.image1)) {
			this.image1 = image1;
			setImage1Changed(true);
//			addCtxChangedProperty("image1");
//			setCtxChanged(true);
			this.notify(CompAttrConstants.IMAGE1, this.getRealImage1());
//			this.notify("realImage1", this.getRealImage1());
			
		}
	}
	public void setImage1Absolute(String image1) {
		if (!image1.equals(this.image1)) {
			this.image1 = image1;
			setImage1Changed(true);
			this.notify(CompAttrConstants.IMAGE1, this.image1);
			
		}
	}
	public String getImage2() {
		return image2;
	}
	public void setImage2(String image2) {
		if (!image2.equals(this.image2)) {
			this.image2 = image2;
			setImage2Changed(true);
			this.notify(CompAttrConstants.IMAGE2, this.getRealImage2());
		}
	}
	public String getImageInact() {
		return imageInact;
	}
	public void setImageInact(String imageInact) {
		this.imageInact = imageInact;
	}	
	public void mergeProperties(WebElement ele) {
		throw new LfwRuntimeException("not implemented");
	}
	
	public boolean isImage1Changed() {
		return image1Changed;
	}
	
	public void setImage1Changed(boolean image1Changed) {
		this.image1Changed = image1Changed;
	}
	
	public String getRealImage1() {
//		if (isImage1Changed()) {
			realImage1 = getRealImgPath(this.image1, null);
			setImage1Changed(false);
//		}
		return realImage1;
	}
	
	public void setRealImage1(String realImage1) {
		this.realImage1 = realImage1;
	}
	
	public boolean isImage2Changed() {
		return image2Changed;
	}
	
	public void setImage2Changed(boolean image2Changed) {
		this.image2Changed = image2Changed;
	}
	
	public String getRealImage2() {
//		if (isImage2Changed()) {
			realImage2 = getRealImgPath(this.image2, null);
			setImage2Changed(false);
//		}
		return realImage2;
	}
	
	public void setRealImage2(String realImage2) {
		this.realImage2 = realImage2;
	}
	
	public boolean isCanUpload() {
		return canUpload;
	}
	public void setCanUpload(boolean canUpload) {
		this.canUpload = canUpload;
	}
	
	public String getSysid() {
		return sysid;
	}
	public void setSysid(String sysid) {
		this.sysid = sysid;
	}
	public String getBaModule() {
		return baModule;
	}
	public void setBaModule(String baModule) {
		this.baModule = baModule;
	}
	
	@Override
	public void setChangedContext(JSONObject changedObj) {
		super.setChangedContext(changedObj);
		if(changedObj.has(CompAttrConstants.IMAGE1)){
			String image1 = changedObj.getString(CompAttrConstants.IMAGE1);
			this.setImage1(image1);
		}
		if(changedObj.has(CompAttrConstants.IMAGE2)){
			String image2 = changedObj.getString(CompAttrConstants.IMAGE2);
			this.setImage2(image2);
		}
	}
}
