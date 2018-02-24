package nc.uap.portal.comm.setting.impl;

import nc.uap.cpb.org.constant.DialogConstant;
import nc.uap.portal.comm.setting.PtSettingVO;
import nc.uap.portal.comm.setting.itf.IPortalSetting;
import nc.vo.ml.NCLangRes4VoTransl;
/**
 * Portal������
 * @author licza
 *
 */
public class PtPortalSystemSetting implements IPortalSetting {

	@Override
	public PtSettingVO[] getSettings() {
		//freenode�����⡢Ƥ������������ʹ��
		String freenode="/portal/app/mockapp/cdref?model=nc.uap.portal.comm.setting.SetMorePageModel&pagename='+window.top.CUR_PAGE_NAME+'&pagemodule='+window.top.CUR_PPAGE_MODULE+'";
		return new  PtSettingVO[]{
				new PtSettingVO("changePassWd",NCLangRes4VoTransl.getNCLangRes().getStrByID("portal", "PtPortalSystemSetting-000000")/*�޸�����*/,"changePassWd","/portal/app/mockapp/passwordmng",Boolean.TRUE,"480","280"),
//				new PtSettingVO("changePassWd","�ĵ�����"/*�ĵ�����*/,"changePassWd","/portal/doccentre.jsp",Boolean.TRUE,"600","500"),
//				new PtSettingVO("changeUserinfo","�û���Ϣ���"/*�û���Ϣ���*/,"changeUserinfo","/portal/sync/scapfd/fa/html/jsp/tab/ChangeUserInfo.jsp",Boolean.TRUE,"480","250")
		};
	}

}
