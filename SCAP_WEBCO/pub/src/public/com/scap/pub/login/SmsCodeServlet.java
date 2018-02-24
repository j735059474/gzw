package com.scap.pub.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import nc.jdbc.framework.processor.MapProcessor;
import nc.scap.pub.sms.SmsManageService;
import nc.scap.pub.util.ScapDAO;
import nc.scap.pub.vos.ScapSmsTaskVO;
import nc.scap.pub.vos.ScapSmsVO;
import nc.scap.sms.SendService;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.servlet.LfwServletBase;
import nc.uap.ws.console.utils.vo.Json;
import nc.vo.pubapp.pattern.log.Log;

import org.apache.commons.lang.StringUtils;

import uap.json.JSONObject;


public class SmsCodeServlet extends LfwServletBase {
    private static final long serialVersionUID = 1L;
	public static final String ERRMSG = "errmsg";
	public static final String USE_SMSCODE_FLAG = "smscodeflag";
	public static final String USE_SMSCODE_OK = "smscodeok";
	public static final String USE_SMSPWD_FLAG = "smspwdflag";
	public static final String USE_SMSCODE = "smscode";
	public static final String VALIDATEFLAG = "validate_flag";
	public static final String LOGINURL = "/portal/login.jsp?lrid=1";
	/**
	 * GET������POST�ﴦ��
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	/**
	 * ������POST�ﴦ��
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		service(req, resp);
	}
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//ת��
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
  	    resp.setContentType("text/html; charset=utf-8");
  	    String action = req.getParameter("action");
  	    if ("ifSmsCode".equalsIgnoreCase(action)) {
  	    	ifSmsCode(req, resp);
  	    } else if ("getSmsCode".equalsIgnoreCase(action)) {
  	    	getSmsCode(req, resp);
  	    } else if ("validateSmsCode".equalsIgnoreCase(action)) {
  	    	validateSmsCode(req, resp);
  	    } else if ("overSmsCode".equalsIgnoreCase(action)) {
  	    	overSmsCode(req, resp);
  	    }
	}
	/**
	 * login���������д���û���ȷ���Ƿ���Ҫ������֤��
	 * ���¼����û���Ҫ��֤��
	 * һ��������ҵ�û������й�˾�û���(�ŷù���ϵͳ�û�����ҵ�쵼��Աϵͳ�û�����ҵ�˲�ϵͳ�û��ȡ�)
	 * ע��һ��������ҵ cp_orgs.def13=0001Z0100000000005CH
	 * 	  ���й�˾           org_orgs.def1=Y  
	 *   �û���������   cp_user.identityverifycode = 'smscode'
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void ifSmsCode(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String,String> result = new HashMap<String,String>();
		String usercode = req.getParameter("uid");
		String message = "";
		//�ж��Ƿ��û���������
		String userinfosql = "select t.pk_org, t.identityverifycode from cp_user t where t.user_code = '" + usercode + "'";
		Map<String,String> userinfosresult = (Map<String, String>)ScapDAO.executeQuery(userinfosql, new MapProcessor());
		if (userinfosresult != null && userinfosresult.get("identityverifycode") != null && "smscode".equals(userinfosresult.get("identityverifycode"))) {
			// ��Ҫ������֤
			message = "��Ϊ������֤�û�";
		}
		//�ж��Ƿ���һ��������ҵ���û�
		// ȡ�õ�ǰ�û���������ҵ�ģ�portal��֯��pk��
		String cporgsql = "select t.def13 from cp_orgs t where ";
		if (userinfosresult != null && userinfosresult.get("pk_org") != null) {
			cporgsql = cporgsql + " t.pk_org = '" + result.get("pk_org") + "'";
			Map<String,String> cporgresult = (Map<String, String>)ScapDAO.executeQuery(cporgsql, new MapProcessor());
			if (cporgresult != null && cporgresult.get("def13") != null && "0001Z0100000000005CH".equals(cporgresult.get("def13"))) {
				// ��Ҫ������֤
				message = "��Ϊ������֤�û�";
			}
		}
		//�ж��Ƿ���������ҵ
		String orgsorgsql = "select t.def1 from org_orgs t where ";
		if (userinfosresult != null && userinfosresult.get("pk_org") != null) {
			orgsorgsql = orgsorgsql + " t.pk_org = '" + result.get("pk_org") + "'";
			Map<String,String> cporgresult = (Map<String, String>)ScapDAO.executeQuery(cporgsql, new MapProcessor());
			if (cporgresult != null && cporgresult.get("def1") != null && "Y".equals(cporgresult.get("def1"))) {
				// ��Ҫ������֤
				message = "��Ϊ������֤�û�";
			}
		}
		if (StringUtils.isNotBlank(message)){
			result.put(ERRMSG, message);
			result.put(USE_SMSCODE_FLAG, "true");
			result.put(USE_SMSPWD_FLAG, "true");
			req.getSession().setAttribute(USE_SMSCODE_FLAG, "true");
		} else {
			result.put(ERRMSG, "");
			result.put(USE_SMSCODE_FLAG, "false");
			result.put(USE_SMSPWD_FLAG, "false");
			req.getSession().removeAttribute(USE_SMSCODE_FLAG);
		}
		resp.getWriter().write(Json.map2json(result));
		
	}
	private void getSmsCode(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		String data = StringUtils.EMPTY;
//		BufferedReader in = new BufferedReader(new InputStreamReader(req.getInputStream(),"UTF-8"));
//        String line;  
//        while ((line = in.readLine()) != null) {  
//     	   data += line;  
//        }  
//        try {
//			JSONObject jsonObject = new JSONObject(data);
//		} catch (ParseException e) {
//			// TODO �Զ����ɵ� catch ��
//			e.printStackTrace();
//		}
        
		String usercode = req.getParameter("uid");
		Map<String,String> result = new HashMap<String,String>();
		Map<String, String> map = getMobile(usercode);
		if (map == null || map.size() == 0) {
			req.getSession().setAttribute(ERRMSG, "��ǰ�û�������");
		} else if (map.get("mobile") == null) {
			result.put(ERRMSG, "��ǰ�û���Ϣ��û���ֻ�����,���ܷ��Ͷ�����֤��");
			resp.getWriter().write(Json.map2json(result));
		} else {
			String randomNo = getRandomNo();
			
			String username = map.get("username");
			String mobile = map.get("mobile");
			String content = "����ʡ���ʼ����Ϣϵͳ��֤�룺" + randomNo + "�������Ʊ��ܣ���Ҫ���������ˡ�";
			
			// ���ö���ƽ̨
			ScapSmsTaskVO task = SmsManageService.addTask(content, mobile, "��ȡ������֤��", "UAP");
			task.setContent(content);
			ScapSmsVO sms = new ScapSmsVO();
			sms.setAddress(mobile);
			
			realSendSms(task);
			
			HttpSession session = req.getSession();
			String sessionID = session.getId();
			session.setAttribute(USE_SMSCODE, randomNo);
			result.put(ERRMSG, "�뾡��������֤��");
			result.put(USE_SMSCODE_OK, "true");
			result.put("SessionID", sessionID);
//			Log.info("START");
//			Log.info("randomNo:" + randomNo);
//			Log.info("sessionID:" + sessionID);
//			Log.info("END");
////			resp.encodeURL(req.getRequestURI());
			
			resp.getWriter().write(Json.map2json(result));
		}

	}
	private void resetSmsPwd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String usercode = req.getParameter("uid");
		Map<String,String> result = new HashMap<String,String>();
		Map<String, String> map = getMobile(usercode);
		if (map == null || map.size() == 0) {
			req.getSession().setAttribute(ERRMSG, "��ǰ�û�������");
		} else if (map.get("mobile") == null) {
			result.put(ERRMSG, "��ǰ�û���Ϣ��û���ֻ�����,���ܷ��Ͷ�����֤��");
			resp.getWriter().write(Json.map2json(result));
		} else {
			String randomNo = getRandomNo();
			
			String username = map.get("username");
			String mobile = map.get("mobile");
			String content = "������ί��" + username + "�������������룺" + randomNo + "����Ǳ��˲�������ԡ�";
			
			// ���ö���ƽ̨
			ScapSmsTaskVO task = SmsManageService.addTask(content, mobile, "��ȡ��������", "UAP");
			task.setContent(content);
			ScapSmsVO sms = new ScapSmsVO();
			sms.setAddress(mobile);
			realSendSms(task);
			req.getSession().setAttribute(USE_SMSCODE, randomNo);
			
			result.put(ERRMSG, "���������ѷ��ͣ��뾡����д");
			resp.getWriter().write(Json.map2json(result));
		}
	}
	private void validateSmsCode(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String,String> result = new HashMap<String,String>();
	
		String msg = "";
		String smscheckcodeFromPage = req.getParameter("smscheckcode");
		String smscheckcodeFromSession = (String)req.getSession().getAttribute(USE_SMSCODE);
		if (StringUtils.isBlank(smscheckcodeFromPage)) {
			result.put(ERRMSG, "�����������֤��");
			result.put(VALIDATEFLAG, "false");
		}
		if (StringUtils.isNotBlank(smscheckcodeFromSession) && smscheckcodeFromPage.equals(smscheckcodeFromSession)){
			result.put(ERRMSG, "������֤����ȷ");
			result.put(VALIDATEFLAG, "true");
		} else {
			result.put(ERRMSG, "������֤�����");
			result.put(VALIDATEFLAG, "false");
		}
		resp.getWriter().write(Json.map2json(result));
		return;

	}
	private void overSmsCode(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getSession().removeAttribute(USE_SMSCODE);
		req.getSession().invalidate();
	}
	private Map<String, String> getMobile(String usercode) {
		String sql = "select u.user_code,u.user_name as username,psn.mobile as mobile from cp_user u " + 
		" join bd_psndoc psn on psn.pk_psndoc=u.pk_base_doc ";
		sql = sql + " where u.user_code ='" + usercode + "' ";
		Map<String, String> map = (Map<String, String>) ScapDAO.executeQuery(sql, new MapProcessor());
		return map;
	}
//	/**
//	 * ���շ��Ͷ���
//	 * @return
//	 */
//	private void realSendSms(ScapSmsTaskVO task){
//		List<ISmsExtentionService> exts = ScapSmsExtentionUtil.getSmsServiceList();
//		if (exts.size() != 1) {
//			throw new LfwRuntimeException("δ���û������˶��������չ��");
//		}
//		ISmsExtentionService ext = exts.get(0);
//		try {
//			ext.send(task);
//		} catch (Exception e) {
//			throw new LfwRuntimeException("����ʧ��", e);
//		}
//
//	}
	/**
	 * ���շ��Ͷ���
	 * @return
	 */
	private void realSendSms(ScapSmsTaskVO task){
		SendService service = new SendService();
		try {
			service.getSendServiceSoap().sendSms("AHHF1295173", "admin", "ff8aaa8a2dde9154", task.getAddress(), task.getContent());
		} catch (Exception e) {
			throw new LfwRuntimeException("����ʧ��", e);
		}

	}
	private String getRandomNo() {
		Random random = new Random(System.currentTimeMillis());
		int num = random.nextInt(10000);
		if(num<1000)num+=1000;
		return String.valueOf(num);
	}
	
}
