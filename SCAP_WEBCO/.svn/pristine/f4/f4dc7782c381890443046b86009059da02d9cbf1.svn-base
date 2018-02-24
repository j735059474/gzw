package nc.scap.pub.sms.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import nc.scap.pub.sms.SmsManageService;
import nc.scap.pub.sms.itf.ISmsExtentionService;
import nc.scap.pub.util.ScapDAO;
import nc.scap.pub.vos.ScapMoSmsVO;
import nc.scap.pub.vos.ScapSmsStatusEnum;
import nc.scap.pub.vos.ScapSmsTaskStatusEnum;
import nc.scap.pub.vos.ScapSmsTaskVO;
import nc.scap.pub.vos.ScapSmsVO;
import nc.uap.portal.log.ScapLogger;
import nc.vo.pub.lang.UFDateTime;

import com.jasson.mas.api.ApiClientFactory;
import com.jasson.mas.api.ApiException;
import com.jasson.mas.api.common.ConnectStatus;
import com.jasson.mas.api.sms.SmsApiClient;
import com.jasson.mas.api.sms.SmsApiClientHandler;
import com.jasson.mas.api.sms.data.ApiSmsSendRequest;
import com.jasson.mas.api.sms.data.ApiSmsSendResponse;
import com.jasson.mas.api.smsapi.MsgFmt;
import com.jasson.mas.api.smsapi.Report;
import com.jasson.mas.api.smsapi.Sms;
import com.jasson.mas.api.smsapi.SmsType;

/**
 * 四川短信平台扩展点
 * @author jizhg
 * 
 */
public class SCSmsExtServiceImpl implements ISmsExtentionService, SmsApiClientHandler {

	@Override
	public void send(ScapSmsTaskVO task) throws Exception {

		UFDateTime taskSendtime = new UFDateTime(System.currentTimeMillis());
		Integer taskSendtimes = task.getSendtimes();
		if (taskSendtimes == null) {
			taskSendtimes = 0;
		}
		task.setSendtime(taskSendtime);
		task.setSendtimes(++taskSendtimes);
		task.setSendstatus(ScapSmsTaskStatusEnum.FINISHED);

		ScapSmsVO[] smses = task.getSmses();
		if (smses != null) {
			for (ScapSmsVO sms : smses) {
				UFDateTime smsSendtime = new UFDateTime(System.currentTimeMillis());
				Integer smsSendtimes = sms.getSendtimes();
				if (smsSendtimes == null) {
					smsSendtimes = 0;
				}
				sms.setSendtime(smsSendtime);
				sms.setSendtimes(++smsSendtimes);
				sms.setSendstatus(ScapSmsStatusEnum.SENT);
			}
		}

		try {
			if (!MasClient.INSTANCE.isConnected()) {
				MasClient.INSTANCE.start(this);
			}
			MasClient.INSTANCE.sendSms(task);
		} catch (Exception e) {
			for (ScapSmsVO sms : smses) {
				sms.setSendstatus(ScapSmsStatusEnum.FAILED);
			}
			task.setSendstatus(ScapSmsTaskStatusEnum.NOT_FINISHED);
			ScapLogger.error("调用四川短信网关失败", e);
		}
	}

	@Override
	public void send(ScapSmsVO sms) throws Exception {

		ScapSmsTaskVO task = (ScapSmsTaskVO) ScapDAO.retrieveByPK(ScapSmsTaskVO.class, sms.getPk_task());

		UFDateTime sendtime = new UFDateTime(System.currentTimeMillis());
		Integer sendtimes = sms.getSendtimes();
		if (sendtimes == null) {
			sendtimes = 0;
		}
		sms.setSendtime(sendtime);
		sms.setSendtimes(++sendtimes);
		task.setSmses(new ScapSmsVO[] { sms });

		try {
			if (!MasClient.INSTANCE.isConnected()) {
				MasClient.INSTANCE.start(this);
			}
			MasClient.INSTANCE.sendSms(task);
			sms.setSendstatus(ScapSmsStatusEnum.SENT);
		} catch (Exception e) {
			sms.setSendstatus(ScapSmsStatusEnum.FAILED);
			ScapLogger.error("调用四川短信网关失败", e);
		}
	}

	/**
	 * 接收状态报告
	 * @param submitId - 提交到MAS服务平台的标识，sendSms方法返回值
	 */
	@Override
	public void notifySmsDeliveryStatus(String submitId, Report[] reports) {
		String pkTask = MasClient.INSTANCE.getRequestTaskMap().remove(submitId);
		// 回写短信状态
		if (reports != null) {
			ScapSmsTaskVO task = SmsManageService.findTask(pkTask);
			if (task.getSmses() != null) {
				Map<String, ScapSmsVO> smsMap = new HashMap<String, ScapSmsVO>();
				for (ScapSmsVO sms : task.getSmses()) {
					smsMap.put(sms.getAddress(), sms);
				}
				for (Report report : reports) {
					ScapSmsVO sms = smsMap.get(report.address);
					if (sms != null) {
						if (report.sendResult == 0 && "DELIVRD".equalsIgnoreCase(report.stat)) {
							sms.setSendstatus(ScapSmsStatusEnum.SUCCEED);
						} else {
							sms.setSendstatus(ScapSmsStatusEnum.FAILED);
							ScapLogger.error("ScapSmsVO[" + sms.getPk_sms() + "]发送失败。" + report.errDesc);
						}
					}
				}
			}
		}
	}

	/**
	 * 接收MO（Mobile Originated）短信
	 * @param moSms.destID - MO流水号和发送时的xCode对应
	 */
	@Override
	public void notifySmsReception(Sms moSms) {
		ScapMoSmsVO vo = new ScapMoSmsVO();
		vo.setPk_ext(moSms.destID);
		vo.setAddress(moSms.mobile);
		vo.setContent(moSms.content);
		
		ScapDAO.insertOrUpdateVO(vo);
	}

	/**
	 * MAS短信网关配置信息
	 */
	public static class MasClient {

		private MasClient() {
			try {
				InputStream in = MasClient.class.getResourceAsStream(PROPS_FILE);
				props = new Properties();
				props.load(in);
			} catch (Exception e) {
				ScapLogger.error("读取四川短信网关配置出现错误", e);
			}
		}

		public static final String PROPS_FILE = "SC.properties";
		public static final String PROP_APIID = "apiId";
		public static final String PROP_APPPWD = "appPwd";
		public static final String PROP_XCODE = "xCode";
		public static final String PROP_IP = "ip";
		public static final String PROP_PORT = "port";

		public static final MasClient INSTANCE = new MasClient();

		private Properties props;
		private final Map<String, String> requestTaskMap = new HashMap<String, String>();
		private SmsApiClient client;

		/**
		 * 和MAS网关创建连接
		 * @param handler 处理短信通知、MO短信
		 */
		@SuppressWarnings("deprecation")
        public boolean start(SmsApiClientHandler handler) {
			if (client == null) {
				client = ApiClientFactory.createSmsApiClient(
				        handler,
				        MasClient.INSTANCE.getIp(),
				        MasClient.INSTANCE.getPort(),
				        MasClient.INSTANCE.getApiId(),
				        MasClient.INSTANCE.getAppPwd());
			}
			try {
				if (client != null) {
					client.start();
					client.setAutoConnect(true);
				}
			} catch (ApiException e) {
				ScapLogger.error("MAS API登录失败", e);
				return false;
			}
			return true;
		}

		public boolean isConnected() {

			if (client == null) {
				return false;
			}

			try {
				ConnectStatus status = client.getConnStatusIAGW();
				return ConnectStatus.Connect.equals(status);
			} catch (Exception e) {
				return false;
			}
		}

		public String sendSms(ScapSmsTaskVO task) throws Exception {

			// 组装request
			ApiSmsSendRequest request = new ApiSmsSendRequest();
			List<String> destList = new ArrayList<String>();
			if (task.getSmses() != null) {
				for (ScapSmsVO sms : task.getSmses()) {
					destList.add(sms.getAddress());
				}
			}
			request.setDestAddrs(destList);
			request.setValidTime(10000);
			request.setXCode(getXCode());
			request.setMessage(task.getContent());
			request.setMsgFormat(MsgFmt.GB2312);
			request.setNeedReport(true);
			request.setPriority(1);
			request.setType(SmsType.Normal);
			request.setAppID(getApiId());

			// 提交request
			ApiSmsSendResponse response = client.sendSms(request);
			if (response.getErrCode() != null && response.getErrCode().trim().length() > 0) {
				throw new Exception("API提交短信失败，错误代码：" + response.getErrCode() + "。" + response.getDesc());
			}

			// 成功提交request后将requestId记录在requestTaskMap中
			requestTaskMap.put(response.getRequestID(), task.getPk_task());
			return response.getRequestID();
		}

		public Map<String, String> getRequestTaskMap() {
			return requestTaskMap;
		}

		/**
		 * API插件标识ID
		 */
		public String getApiId() {
			return props.getProperty(PROP_APIID);
		}

		/**
		 * API插件密码
		 */
		public String getAppPwd() {
			return props.getProperty(PROP_APPPWD);
		}

		/**
		 * 短信扩展码
		 */
		public String getXCode() {
			return props.getProperty(PROP_XCODE);
		}

		/**
		 * 服务端IP地址
		 */
		public String getIp() {
			return props.getProperty(PROP_IP);
		}

		/**
		 * API端口
		 */
		public Integer getPort() {
			String port = props.getProperty(PROP_PORT);
			try {
				return Integer.parseInt(port);
			} catch (Exception e) {
				return 0;
			}
		}
	}
}
