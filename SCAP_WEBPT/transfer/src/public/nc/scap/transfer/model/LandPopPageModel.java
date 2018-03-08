package nc.scap.transfer.model;

import java.util.UUID;

import nc.scap.transfer.land.utils.ILandConstants;
import nc.scap.transfer.land.utils.LandUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.constants.AppConsts;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.jsp.uimeta.UIElementFinder;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UISplitter;
import nc.uap.lfw.jsp.uimeta.UIView;

public class LandPopPageModel extends PageModel{
	@Override
	protected void initUIMetaStruct() {
		super.initUIMetaStruct();
		String saveOpe = (String) LfwRuntimeEnvironment.getWebContext().getOriginalParameter(AppConsts.OPE_SIGN);
		UIMeta uimeta = (UIMeta) this.getUIMeta();
		//��ϸ��Ϣ����
		if((saveOpe != null && saveOpe.equals(ILandConstants.SAVE_SIGN_VIEW))
				||(saveOpe != null && saveOpe.equals(ILandConstants.SAVE_SIGN_ADD))
				||(saveOpe != null && saveOpe.equals(ILandConstants.SAVE_SIGN_EDIT))){
			UIView mainView = UIElementFinder.findUIWidget(uimeta, "main");
			UISplitter split = (UISplitter) UIElementFinder
					.findElementById(uimeta, "spliterlayout1");
			uimeta.removeElement(split);
			uimeta.setElement(mainView);
		}
	}
	@Override
	protected void initPageMetaStruct(){
		super.initPageMetaStruct();
//		this.getPageMeta().setEtag(UUID.randomUUID().toString());
		LfwView editView = getWebContext().getPageMeta().getView(ILandConstants.MAIN_VIEW_ID);
		editView.setEtag(UUID.randomUUID().toString());
		FormComp fc= (FormComp) editView.getViewComponents().getComponent(ILandConstants.FORM_ID_LAND);
//		String[] gridIds = new String[]{
//				ILandConstants.GRID_ID_B
//		};
		//���ؿ�Ƭ����������ť
		LandUtil.setBtnFalse(editView, ILandConstants.MENUBAR, new String[]{
				ILandConstants.BTN_ADD, ILandConstants.BTN_COPY, ILandConstants.BTN_PRINT, ILandConstants.BTN_DEL, ILandConstants.BTN_ATTACHFILE, ILandConstants.BTN_QYTY
		});
		//������ť
		LfwView pubview_simpleexetask = getPageMeta().getWidget("pubview_simpleexetask");
		LandUtil.setBtnFalse(pubview_simpleexetask, ILandConstants.EXEMENUBAR, new String[]{
				ILandConstants.BTN_ZANCUN, ILandConstants.BTN_FUJIAN,  ILandConstants.BTN_LIUCHENG, 
		});
		//�����ӱ�����ɾ����ť
//		LandUtil.setGridBtnFalse(editView, ILandConstants.GRID_ID_B, new String[]{
//				ILandConstants.BTN_GRID_ADD, ILandConstants.BTN_GRID_DELETE, ILandConstants.BTN_GRID_EDIT
//				
//		});
		String saveOpe = (String) LfwRuntimeEnvironment.getWebContext().getOriginalParameter(AppConsts.OPE_SIGN);
		
//		if(saveOpe != null && (saveOpe.equals(ILandConstants.SAVE_SIGN_ADD)||saveOpe.equals(ILandConstants.SAVE_SIGN_EDIT))){
//			fc.getElementById("pk_org_name").setEnabled(true);
//		}
		//��ϸ��Ϣ����
		if(saveOpe != null && saveOpe.equals(ILandConstants.SAVE_SIGN_VIEW)){
			LandUtil.setBtnFalse(editView, ILandConstants.MENUBAR, new String[]{
					ILandConstants.BTN_SAVE
			});
			//������ť
			LandUtil.setBtnFalse(pubview_simpleexetask, ILandConstants.EXEMENUBAR, new String[]{
					ILandConstants.BTN_TIJIAO, ILandConstants.BTN_LIUCHENG, ILandConstants.BTN_BOHUI
			});
//			HideGridBtn(editView, gridIds);
		}
		//���
		if(saveOpe != null && saveOpe.equals(ILandConstants.SAVE_SIGN_AUDIT)){
			LandUtil.setBtnFalse(editView, ILandConstants.MENUBAR, new String[]{
					ILandConstants.BTN_SAVE
			});
//			HideGridBtn(editView, gridIds);
		} 
		//���̽���
		String audits = LfwRuntimeEnvironment.getWebContext().getOriginalParameter("audits");
		if(audits != null && audits.equals("audit")){
			LandUtil.setBtnFalse(editView, ILandConstants.MENUBAR, new String[]{
					ILandConstants.BTN_SAVE
			});
//			HideGridBtn(editView, gridIds);
		}
	}
	/**
	 * �����ӱ������༭ɾ����ť
	 * @param editView
	 * @param gridIds
	 */
	private void HideGridBtn(LfwView editView, String[] gridIds){
		for(String gridId : gridIds){
			GridComp gc = (GridComp) editView.getViewComponents().getComponent(gridId);
			if(gc!=null){
				gc.setShowImageBtn(false);
			}
		}
	}
}
