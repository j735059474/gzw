package nc.scap.ptpub.method;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.pub.pa.PreAlertContext;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.message.util.MessageCenter;
import nc.message.vo.MessageVO;
import nc.message.vo.NCMessage;
import nc.scap.pub.util.ScapDAO;
import nc.uap.cpb.org.vos.CpUserVO;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.portal.log.ScapLogger;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.pa.AlertsendconfigVO;

public class PtAlertMethod {
	
	/***
	 * 根据公司主键获取预警配置用户信息
	 * 
	 * @param context
	 * @param pk_org
	 * @return
	 */
	public static String getPkUserS(String pk_org, String pkusers) {
		BaseDAO bd = ScapDAO.getBaseDAO();
		String sql = " select * from cp_user c where c.ncpk in (" + pkusers
				+ ") and pk_org='" + pk_org + "'";
		StringBuffer sb = new StringBuffer();
		try {
			ScapLogger.error("根据公司查找用户信息" + sql);
			List<CpUserVO> cpusers = (List<CpUserVO>) bd.executeQuery(sql,
					new BeanListProcessor(CpUserVO.class));
			if (cpusers != null && cpusers.size() > 0) {
				for (CpUserVO tmpvo : cpusers) {
					sb.append(tmpvo.getCuserid())
							.append(',');
				}
				sb = new StringBuffer(sb.toString().substring(0,
						sb.toString().length() - 1));
			}
		} catch (DAOException e) {
			ScapLogger.error("查询异常：" + sql);
		}
		return sb.toString();
	}

	/***
	 * 根据预警上下文获取配置用户信息
	 * 
	 * @param context
	 * @return
	 */
	public static String getContextInfo(PreAlertContext context) {
		AlertsendconfigVO[] sendinfo = context.getEngineContext()
				.getAlertRegistry().getSendConfigVo();
		StringBuffer sb = new StringBuffer();
		if (sendinfo != null && sendinfo.length > 0) {
			for (int i = 0; i < sendinfo.length; i++) {
				if (sendinfo[i].getReceivername() == null
						|| "".equals(sendinfo[i].getReceivername())) {
					continue;
				}
				sb.append('\'').append(sendinfo[i].getReceivername())
						.append('\'').append(',');
			}
			sb = new StringBuffer(sb.toString().substring(0,
					sb.toString().length() - 1));
		}
		return sb.toString();
	}
	
	/***
	 * 消息创建
	 * 
	 * @param contenttype
	 * @param subject
	 * @param content
	 * @param pk_group
	 * @param pk_cpusers
	 * @param msgType
	 * @param msgSourceType
	 */
	public static NCMessage createMsg(String contenttype, String subject,
			String content, String pk_group, String pk_cpusers, String msgType,
			String msgSourceType, String billId, String billChildId,
			String billpkOrg,String selftype) {
		NCMessage ncmsg = new NCMessage();
		MessageVO msgvo = new MessageVO();
		msgvo.setContenttype(contenttype);
		msgvo.setSubject(subject);
		msgvo.setContent(content);
		msgvo.setPk_group(pk_group);
		msgvo.setReceiver(pk_cpusers);
		msgvo.setSendtime(new UFDateTime());
		msgvo.setMsgtype(msgType);
		msgvo.setSender("NC_USER0000000000000");
		msgvo.setMsgsourcetype(msgSourceType);
		msgvo.setBillid(billId);
		msgvo.setBilltype(billChildId);
		msgvo.setDetail(billpkOrg);
		msgvo.setPk_detail(selftype);
		ncmsg.setMessage(msgvo);
		return ncmsg;
	}

	/***
	 * 消息发送
	 * 
	 * @param msgs
	 */
	public static void msgSend(NCMessage[] msgs) {
		try {
			ScapLogger.debug("消息开始发送");
			// 支持同时给多个集团发送消息，参数传NCMessage[] 数组
			MessageCenter.sendMessage(msgs);
			ScapLogger.debug("消息发送成功");
		} catch (Exception e) {
			throw new LfwRuntimeException("消息发送失败");
		}
	}

}
