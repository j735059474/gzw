package nc.uap.portal.comm.setting.impl;

import nc.uap.cpb.org.constant.DialogConstant;
import nc.uap.portal.comm.setting.PtSettingVO;
import nc.uap.portal.comm.setting.itf.IPortalSetting;
import nc.vo.ml.NCLangRes4VoTransl;
/**
 * Portal设置项
 * @author licza
 *
 */
public class PtPortalSystemSetting implements IPortalSetting {

	@Override
	public PtSettingVO[] getSettings() {
		//freenode：主题、皮肤、语言设置使用
		String freenode="/portal/app/mockapp/cdref?model=nc.uap.portal.comm.setting.SetMorePageModel&pagename='+window.top.CUR_PAGE_NAME+'&pagemodule='+window.top.CUR_PPAGE_MODULE+'";
		return new  PtSettingVO[]{
				new PtSettingVO("changePassWd",NCLangRes4VoTransl.getNCLangRes().getStrByID("portal", "PtPortalSystemSetting-000000")/*修改密码*/,"changePassWd","/portal/app/mockapp/passwordmng",Boolean.TRUE,"480","280"),
//				new PtSettingVO("changePassWd","文档中心"/*文档中心*/,"changePassWd","/portal/doccentre.jsp",Boolean.TRUE,"600","500"),
//				new PtSettingVO("changeUserinfo","用户信息变更"/*用户信息变更*/,"changeUserinfo","/portal/sync/scapfd/fa/html/jsp/tab/ChangeUserInfo.jsp",Boolean.TRUE,"480","250")
		};
	}

}
