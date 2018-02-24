package nc.uap.portal.comm.setting.impl;

import nc.uap.cpb.org.constant.DialogConstant;
import nc.uap.portal.comm.setting.PtSettingVO;
import nc.uap.portal.comm.setting.itf.IPortalSetting;
import nc.vo.ml.NCLangRes4VoTransl;

public class OtherPortalSystemSetting  implements IPortalSetting{

	@Override
	public PtSettingVO[] getSettings() {
		//freenode�����⡢Ƥ������������ʹ��
		String freenode="/portal/app/mockapp/cdref?model=nc.uap.portal.comm.setting.SetMorePageModel&pagename='+window.top.CUR_PAGE_NAME+'&pagemodule='+window.top.CUR_PPAGE_MODULE+'";
		return new  PtSettingVO[]{
				new PtSettingVO("langSetting",NCLangRes4VoTransl.getNCLangRes().getStrByID("portal", "PtPortalSystemSetting-000001")/*��������*/,"langSetting",freenode+"&settype=language","400",DialogConstant.DEFAULT_HEIGHT),
				new PtSettingVO("themeSetting",NCLangRes4VoTransl.getNCLangRes().getStrByID("portal", "PtPortalSystemSetting-000002")/*��������*/,"themeSetting",freenode+"&settype=theme","400",DialogConstant.DEFAULT_HEIGHT),
				new PtSettingVO("templateSetting",NCLangRes4VoTransl.getNCLangRes().getStrByID("portal", "PtPortalSystemSetting-000003")/*������ʽ*/,"templateSetting",freenode+"&settype=skin","400",DialogConstant.DEFAULT_HEIGHT),
				new PtSettingVO("addPortlet",NCLangRes4VoTransl.getNCLangRes().getStrByID("portal", "PtPortalSystemSetting-000004")/*����Portlet*/,"addPortlet","/portal/app/mockapp/portlet?pageName='+window.top.CUR_PAGE_NAME+'&pageModule='+window.top.CUR_PPAGE_MODULE+'","960","550"),
				new PtSettingVO("watchlog",NCLangRes4VoTransl.getNCLangRes().getStrByID("pserver", "PtPortalSystemSetting-000000")/*��־����*/,"watchlog","/portal/app/mockapp/watchlog", Boolean.FALSE,"480","320"),
				new PtSettingVO("renewPage",NCLangRes4VoTransl.getNCLangRes().getStrByID("pserver", "PtPortalSystemSetting-000001")/*�ָ�����*/,"renewPage","/portal/pt/setting/renew?pageName='+window.top.CUR_PAGE_NAME+'&pageModule='+window.top.CUR_PPAGE_MODULE+'","640","340")
		};
	}

}
