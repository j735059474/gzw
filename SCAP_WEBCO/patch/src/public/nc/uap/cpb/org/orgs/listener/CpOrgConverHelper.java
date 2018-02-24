package nc.uap.cpb.org.orgs.listener;

import nc.itf.org.IOrgConst;
import nc.uap.cpb.org.orgs.CpOrgVO;
import nc.vo.org.OrgVO;

/**
 * ��֯����ת��������
 * @author lisw
 *
 */
public class CpOrgConverHelper {
	/**
	 * NULL	ҵ��Ԫ+����	businessunitanddept
NULL	ҵ��Ԫ+�ɱ�����	orgunitandcostcenter
NULL	ҵ��Ԫ+����/�ɱ�����	orgunitanddeptandcc
	ȫ��	global
isbusinessunit	ҵ��Ԫ	businessunit
orgtype1	����	group
orgtype10	����	trafficorg
orgtype11	�ʼ�	qccenter
orgtype12	�ʲ�	assetorg
orgtype13	����	reportorg
orgtype14	ά��	maintainorg
orgtype15	��������	liabilitycenter
orgtype16	��Ŀ	itemorg
orgtype17	Ԥ��	planbudget
orgtype18	�ܿط�Χ	controlarea
orgtype19	�ɱ���	costregion
orgtype2	���˹�˾	corp
orgtype20	���ÿ�����	creditctlregion
orgtype21	�ʽ������ϵ	fundmanasystem
orgtype22	������֯��ϵ	reportmanastru
orgtype23	���ͳ����ϵ	stockstatstru
orgtype24	ҵ��㱨��ϵ	busireportstru
orgtype25	Ԥ����֯��ϵ	budgetorgstru
orgtype26	��������˲�	accountingbook
orgtype27	���κ����˲�	liabilitybook
orgtype28	�ɹ��ƻ���ϵ	purplanstru
orgtype29	����	adminorg
orgtype3	����	dept
orgtype30	����������ϵ	saleappendstru
orgtype31	�ϲ�����	conbineentity
orgtype32	�ɱ�����	resacostcenter
orgtype33	����	factory
orgtype34	�ƻ�����	plancenter
orgtype35	�����ƻ�	produceplan
orgtype36	���/�ƻ���֯	stockplan
orgtype37	����ϲ���ϵ	reportcombinestru
orgtype4	������Դ	hrorg
orgtype5	����	financeorg
orgtype6	�ʽ�	fundorg
orgtype7	�ɹ�	purchaseorg
orgtype8	����	saleorg
orgtype9	���	stockorg
	 * @param orgvo
	 * @return
	 */
	public static CpOrgVO ConvertOrgVo2CP(OrgVO orgvo){
		if(orgvo == null) return null;
		CpOrgVO neworgvo =  new CpOrgVO();
		
		//����
		neworgvo.setPk_org(orgvo.getPk_org());
		//����
		neworgvo.setCode(orgvo.getCode());
		//����
		neworgvo.setName(orgvo.getName());
		neworgvo.setName2(orgvo.getName2());
		neworgvo.setName3(orgvo.getName3());
		neworgvo.setName4(orgvo.getName4());
		neworgvo.setName5(orgvo.getName5());
		neworgvo.setName6(orgvo.getName6());
		//����֯
		neworgvo.setPk_fatherorg(orgvo.getPk_fatherorg());		
		//����״̬
		neworgvo.setEnablestate(orgvo.getEnablestate());
		//��֯����
		neworgvo.setOrglevel(getorgLevel(orgvo));
		//һ����֯
		neworgvo.setPk_orglevel1(orgvo.getPk_group());
		//������֯
		neworgvo.setPk_orglevel2(orgvo.getPk_corp());
		//40����֯����
		neworgvo.setOrgtype1(orgvo.getOrgtype1());
		neworgvo.setOrgtype2(orgvo.getOrgtype2());
		neworgvo.setOrgtype3(orgvo.getOrgtype3());
		neworgvo.setOrgtype4(orgvo.getOrgtype4());
		neworgvo.setOrgtype5(orgvo.getOrgtype5());
		neworgvo.setOrgtype6(orgvo.getOrgtype6());
		neworgvo.setOrgtype7(orgvo.getOrgtype7());
		neworgvo.setOrgtype8(orgvo.getOrgtype8());
		neworgvo.setOrgtype9(orgvo.getOrgtype9());
		neworgvo.setOrgtype10(orgvo.getOrgtype10());
		neworgvo.setOrgtype11(orgvo.getOrgtype11());
		neworgvo.setOrgtype12(orgvo.getOrgtype12());
		neworgvo.setOrgtype13(orgvo.getOrgtype13());
		neworgvo.setOrgtype14(orgvo.getOrgtype14());
		neworgvo.setOrgtype15(orgvo.getOrgtype15());
		neworgvo.setOrgtype16(orgvo.getOrgtype16());
		neworgvo.setOrgtype17(orgvo.getOrgtype17());
		neworgvo.setOrgtype18(orgvo.getOrgtype18());
		neworgvo.setOrgtype19(orgvo.getOrgtype19());
		neworgvo.setOrgtype20(orgvo.getOrgtype20());
		neworgvo.setOrgtype21(orgvo.getOrgtype21());
		neworgvo.setOrgtype22(orgvo.getOrgtype22());
		neworgvo.setOrgtype23(orgvo.getOrgtype23());
		neworgvo.setOrgtype24(orgvo.getOrgtype24());
		neworgvo.setOrgtype25(orgvo.getOrgtype25());
		neworgvo.setOrgtype26(orgvo.getOrgtype26());
		neworgvo.setOrgtype27(orgvo.getOrgtype27());
		neworgvo.setOrgtype28(orgvo.getOrgtype28());
		neworgvo.setOrgtype29(orgvo.getOrgtype29());
		neworgvo.setOrgtype30(orgvo.getOrgtype30());
		neworgvo.setOrgtype31(orgvo.getOrgtype31());
		neworgvo.setOrgtype32(orgvo.getOrgtype32());
		neworgvo.setOrgtype33(orgvo.getOrgtype33());
		neworgvo.setOrgtype34(orgvo.getOrgtype34());
		neworgvo.setOrgtype35(orgvo.getOrgtype35());
		neworgvo.setOrgtype36(orgvo.getOrgtype36());
		neworgvo.setOrgtype37(orgvo.getOrgtype37());
		neworgvo.setOrgtype38(orgvo.getOrgtype38());
		neworgvo.setOrgtype39(orgvo.getOrgtype39());
		neworgvo.setOrgtype40(orgvo.getOrgtype40());
		//20����չ��
		neworgvo.setInnercode(orgvo.getInnercode());
		
		//add by jizhg ��֯������Ͷ���ͬ�� 
		neworgvo.setDef11(orgvo.getShortname());
		neworgvo.setDef12(orgvo.getOrganizationcode());
		//ʵ������ �ǹ�˾��Ϊʡ�������ҵ
		neworgvo.setDef13(orgvo.getEntitytype());
		//ʡ�������ҵ�����
		neworgvo.setDef20(orgvo.getDef20());
		//�Ƿ�Ϊ�н����
		neworgvo.setDef14(orgvo.getDef14());
		//������Ϊ����������ͬ����
		neworgvo.setDef10(orgvo.getMnecode());
		//��������״̬ͬ�� add by jizhg 2014-09-11
		neworgvo.setEnablestate(orgvo.getEnablestate());
		return neworgvo;
	} 
	/**
	 * ��ȡ��֯����
	 * @param orgvo
	 * @return
	 */
	public static String getorgLevel(OrgVO orgvo){
		if(orgvo.getPk_org().equals(IOrgConst.GLOBEORG))//ȫ�� 
			return "0";
		if(orgvo.getOrgtype1().booleanValue()) //����
			return "1";
		if(orgvo.getIsbusinessunit().booleanValue())//ҵ��Ԫ
			return "2";
		if(orgvo.getOrgtype3().booleanValue())//����
			return "3";
		return "10";//������֯������֯�ṹ����֯��
		
	}
}
