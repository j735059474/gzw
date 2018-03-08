package nc.scap.pub.alert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.pub.pa.PreAlertContext;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.message.util.IDefaultMsgConst;
import nc.message.vo.NCMessage;
import nc.scap.ptpub.PtConstants;
import nc.scap.ptpub.method.PtAlertMethod;
import nc.scap.pub.util.ScapDAO;
import nc.uap.portal.log.ScapLogger;
import nc.vo.pub.lang.UFDouble;

public class AlertDealImp implements IAlertDeal {

	@Override
	public void priceDisAert(PreAlertContext context) {
		BaseDAO bd = ScapDAO.getBaseDAO();
		StringBuffer sql = new StringBuffer();
		sql.append("select pk_transfer_h,cp_orgs.name,a.pk_org, a.listvalue �������,");
		sql.append(" a.dwprice �������,a.dpcarrying ����,");
		sql.append(" a.dwtransprice ����ɽ���,");
		sql.append(" a.listvalue / a.dpcarrying yj1,");
		sql.append(" a.dwprice / a.dpcarrying yj2,");
		sql.append(" a.listvalue / a.dwprice yj3,");
		sql.append(" a.listvalue / a.dwtransprice yj4");
		sql.append(" from scappt_transfer_h a");
		sql.append(" left join cp_orgs on cp_orgs.pk_org = a.pk_org");
		sql.append(" where nvl(a.listvalue,-1) > 0 and");
		sql.append(" nvl(a.dwprice,-1) > 0 and nvl(a.dpcarrying,-1) > 0 and");
		sql.append(" nvl(a.dwtransprice,-1) > 0");

		try {
			ScapLogger.debug("���Ƽ۸��쳣��ʾԤ��:" + sql.toString());
			String userinfos = PtAlertMethod.getContextInfo(context);// ��ȡԤ��������Ϣ 
//			String userinfos = "'1001ZG1000000000151P'"; //���Դ��룬��������mag
			
			List<Map<String, Object>> results = (List<Map<String, Object>>) bd
					.executeQuery(sql.toString(), new MapListProcessor());
			if (results != null && results.size() > 0) {
				List<NCMessage> list = new ArrayList<NCMessage>();
				
				for (Map<String, Object> tmp : results) {
					String pkusers = PtAlertMethod.getPkUserS(tmp.get("pk_org").toString(), userinfos);// ��ȡ��˾�¶�Ӧ��Ԥ����Ϣ������
					if (pkusers == null || "".equals(pkusers)) {
						continue;
					}
					
					String content = this.createContent(tmp); //����Ԥ����Ϣ����
					list.add(PtAlertMethod.createMsg("text/plain", tmp.get("name").toString() + "���Ƽ۸��쳣Ԥ��",
							content, InvocationInfoProxy.getInstance()
									.getGroupId(), pkusers, "nc",
							IDefaultMsgConst.PREALERT,
							tmp.get("pk_transfer_h").toString(),
							tmp.get("pk_transfer_h").toString(), tmp.get("pk_org").toString(),
							"scappt"));

				}
				PtAlertMethod.msgSend(list.toArray(new NCMessage[list.size()]));
			}
		} catch (DAOException e) {
			ScapLogger.debug("��ѯ�쳣��" + sql);
		}
	}
	
	/**
	 * ���ݲ�ѯ�������ݹ���Ԥ����Ϣ����
	 * 1.	�����������ݵĹ��Ƽ۸񲻵õ��������۸��90%��
	   2.	���׽�����ݵĹ��Ƽ۸񲻵õ��������۸񣨱���еġ�ת�ñ������ֵ����Ԫ��������90%��
	   3.	�����������ݺͽ��׽�����ݵĹ��Ƽ۸��Ƿ�һ�£�
	   4.	�����������ݵĹ��Ƽ۸�ͽ��׽���ĳɽ��۸��Ƿ�һ��
	 * @param tmp
	 * @return
	 */
	public String createContent(Map<String,Object> tmp){
		StringBuffer content = new StringBuffer();
		int iss = 1;
		for(int i=1;i<=4;i++){
			UFDouble yj_val = new UFDouble(tmp.get("yj" + i).toString());
			switch (i) {
			case 1:
				if(yj_val.compareTo(new UFDouble(0.9)) < 0){
					content.append(iss).append(".");
					content.append(PtConstants.GPYJ_1).append("<br>");
					iss ++ ;
				}
				break;
			case 2:
				if(yj_val.compareTo(new UFDouble(0.9)) < 0){
					content.append(iss).append(".");
					content.append(PtConstants.GPYJ_2).append("<br>");
					iss ++ ;
				}
				break;
			case 3:
				if(yj_val.compareTo(new UFDouble(1)) != 0){
					content.append(iss).append(".");
					content.append(PtConstants.GPYJ_3).append("<br>");
					iss ++ ;
				}
				break;
			case 4:
				if(yj_val.compareTo(new UFDouble(1)) != 0){
					content.append(iss).append(".");
					content.append(PtConstants.GPYJ_4);
				}
				break;
			default:
				break;
			}
		}
		
		return content.toString();
	}
}
