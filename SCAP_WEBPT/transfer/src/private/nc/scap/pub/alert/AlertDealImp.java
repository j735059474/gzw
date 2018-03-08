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
		sql.append("select pk_transfer_h,cp_orgs.name,a.pk_org, a.listvalue 申请挂牌,");
		sql.append(" a.dwprice 结果挂牌,a.dpcarrying 评估,");
		sql.append(" a.dwtransprice 结果成交额,");
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
			ScapLogger.debug("挂牌价格异常提示预警:" + sql.toString());
			String userinfos = PtAlertMethod.getContextInfo(context);// 获取预警配置信息 
//			String userinfos = "'1001ZG1000000000151P'"; //测试代码，接受者是mag
			
			List<Map<String, Object>> results = (List<Map<String, Object>>) bd
					.executeQuery(sql.toString(), new MapListProcessor());
			if (results != null && results.size() > 0) {
				List<NCMessage> list = new ArrayList<NCMessage>();
				
				for (Map<String, Object> tmp : results) {
					String pkusers = PtAlertMethod.getPkUserS(tmp.get("pk_org").toString(), userinfos);// 获取公司下对应的预警信息接收人
					if (pkusers == null || "".equals(pkusers)) {
						continue;
					}
					
					String content = this.createContent(tmp); //构建预警消息内容
					list.add(PtAlertMethod.createMsg("text/plain", tmp.get("name").toString() + "挂牌价格异常预警",
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
			ScapLogger.debug("查询异常：" + sql);
		}
	}
	
	/**
	 * 根据查询出的数据构建预警信息内容
	 * 1.	交易申请数据的挂牌价格不得低于评估价格的90%；
	   2.	交易结果数据的挂牌价格不得低于评估价格（表格中的“转让标的评估值（万元）”）的90%；
	   3.	交易申请数据和交易结果数据的挂牌价格是否一致；
	   4.	交易申请数据的挂牌价格和交易结果的成交价格是否一致
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
