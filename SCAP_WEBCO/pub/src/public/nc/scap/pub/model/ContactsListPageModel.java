package nc.scap.pub.model;

import uap.web.bd.pub.AppUtil;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.CpOrgUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.model.PageModel;

public class ContactsListPageModel  extends PageModel {
	@Override
	protected void initUIMetaStruct() {
		super.initUIMetaStruct();
		String pageCode = getPageCode();
		//����ί�û�ʱ ,list������ʾ����,�޸�,��ɾ����ť;��ҵ�û�ʱ,list����ֻ��ʾ�޸İ�ť
		boolean flag = CpOrgUtil.isCompanyOrg(CntUtil.getCntOrgPk());
		if (!flag) {// ����ί�û�
			//donothing
		} else if (flag) {//��ҵ�û�
			hideOperateButton(getQyPageHideBtn());
		}

	}

	/**
	 * ��ȡ��ҵ�û�����listҳ��ʱ��Ҫ���صİ�ť
	 * 
	 * @return
	 */
	protected String[] getQyPageHideBtn() {
		return new String[] { ScapCoConstants.ADD, ScapCoConstants.DEL,ScapCoConstants.STARTORSTROP};
	}
	/**
	 * ��ȡҳ�����
	 * 
	 * @return
	 */
	protected String getPageCode() {
		return (String) LfwRuntimeEnvironment.getWebContext()
				.getOriginalParameter("pagecode");
	}
	/**
	 * ��ȡ�û�����
	 * 
	 * @return
	 */
	protected String getUserType() {
		return (String) LfwRuntimeEnvironment.getWebContext()
				.getOriginalParameter("user_type");
	}
	/**
	 * ����listҳ��İ�ť
	 * 
	 * @param btns
	 *            ���ذ�ť������
	 */
	protected void hideOperateButton(String... btns) {
		if (btns == null || btns.length == 0) {
			return;
		}
		MenubarComp menuBar = getPageMeta().getView("main").getViewMenus()
				.getMenuBar("menubar");
		for (String btn : btns) {
			if (menuBar.getItem(btn) != null) {
				menuBar.getItem(btn).setVisible(false);
			}
		}
	}
}
