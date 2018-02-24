package nc.scap.pub.sms;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import nc.scap.pub.util.CntUtil;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.portal.log.ScapLogger;

import org.apache.axis2.AxisFault;
import org.apache.ws.axis2.SmsServiceExceptionException0;
import org.apache.ws.axis2.SmsServiceStub;

public class PhoneMessageMgr {
	
	private static String groupid = null;
	
	private static String user = null;
	
	private static String password = null;
	
	private static String md5s(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i).toUpperCase());
			}
			String str = buf.toString();
			//System.out.println("result: " + buf.toString());// 32位的加密

			return str;
		} catch (NoSuchAlgorithmException e) {
			ScapLogger.debug("MD5加密出错!");
			return "";
		}
	}

	/**
	 * 每个人都发送所有的短信内容
	 * @param groupid
	 * @param user
	 * @param password
	 * @param phones
	 * @param contents
	 * @return
	 */
	public static SmsServiceStub.SmsService_MessageStatus[] sendmsg(String[] phones,String[] contents) {
		if(phones==null||phones.length==0) throw new LfwRuntimeException("发送号码不能为空!");
		if(contents==null||contents.length==0) throw new LfwRuntimeException("发送内容不能为空!");
		getSmsGroupUser();
		SmsServiceStub stub = null;
    	try{
    		SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");
			String timeStamp = sdf.format(new Date());		//时间戳           
    		stub = new SmsServiceStub();
    				
			SmsServiceStub.SendMessage sendMessage=new SmsServiceStub.SendMessage();
			sendMessage.setGroupID(groupid);			//groupid
       		sendMessage.setTimeStamp(timeStamp);	//时间戳
       		
       		char c = 0;
       		String str = user + c+c+c+c+c+c+c+ password + timeStamp;
       		String md5 = md5s(str);
       		sendMessage.setAuthenticatorClient(md5);	//客户端认证码		
			
       		String sendUser = CntUtil.getCntUserName();//发送人姓名--当前用户
       		sdf = new SimpleDateFormat("yyyyMMddHHmmss");
       		String sendTime = sdf.format(new Date());
			for(int i=0;i<phones.length;i++){
				String number = phones[i];
				for(int j=0;j<contents.length;j++){
					String cont = contents[j];
					SmsServiceStub.SmsService_Message message = new  SmsServiceStub.SmsService_Message();
		       		message.setAppMessageID(number);		      	//应用方流水
		       		message.setDestTermID(number);                	//短消息接收号码
		       		message.setMsgContent(cont);          			//短信内容
		       		message.setApplyTime(sendTime);         		//计划发送时间	格式为：YYYYMMDDHHMMSS（年月日时分秒）
		       		message.setSendMan(sendUser);                   //发送人
		       		message.setRem1("");                            //备用字段1
		       		message.setRem2("");                            //备用字段2
		       		sendMessage.addMessage(message); 				//可以一次发发多条
				}
			}
       		
       		//调用Webservice
       		SmsServiceStub.SendMessageResponse resp = stub.sendMessage(sendMessage) ; 
       		//获取结果数据
       		SmsServiceStub.SmsService_MessageStatus[] statusArray = resp.get_return(); 
       		return statusArray;
        }catch(SmsServiceExceptionException0  e){
        	SmsServiceStub.Exception err = e.getFaultMessage().getSmsServiceException();
        	ScapLogger.error("短信发送失败!error:"+err.getMessage());
        	throw new LfwRuntimeException("短信发送失败!error:"+err.getMessage());
        } catch (AxisFault e) {
        	ScapLogger.error("短信发送初始化异常!error:"+e.getMessage());
        	throw new LfwRuntimeException("短信发送初始化异常!error:"+e.getMessage());
		} catch (RemoteException e) {
        	ScapLogger.error("短信发送过程中通信异常!error:"+e.getMessage());
			throw new LfwRuntimeException("短信发送过程中通信异常!error:"+e.getMessage());
		}
	}
	
	/**
	 * 每个人发送一条短信
	 * @param groupid
	 * @param user
	 * @param password
	 * @param phones
	 * @param contents
	 * @return
	 */
	public static SmsServiceStub.SmsService_MessageStatus[] sendOneByOneMsg(String[] phones,String[] contents) {
		if(phones==null||phones.length==0) throw new LfwRuntimeException("发送号码不能为空!");
		if(contents==null||contents.length==0) throw new LfwRuntimeException("发送内容不能为空!");
		getSmsGroupUser();
		SmsServiceStub stub = null;
    	try{
    		SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");
			String timeStamp = sdf.format(new Date());		//时间戳           
    		stub = new SmsServiceStub();
    				
			SmsServiceStub.SendMessage sendMessage=new SmsServiceStub.SendMessage();
			sendMessage.setGroupID(groupid);			//groupid
       		sendMessage.setTimeStamp(timeStamp);	//时间戳
       		
       		char c = 0;
       		String str = user + c+c+c+c+c+c+c+ password + timeStamp;
       		String md5 = md5s(str);
       		sendMessage.setAuthenticatorClient(md5);	//客户端认证码		
			
       		String sendUser = CntUtil.getCntUserName();//发送人姓名--当前用户
       		sdf = new SimpleDateFormat("yyyyMMddHHmmss");
       		String sendTime = sdf.format(new Date());
       		int contengLen = contents.length;
			for(int i=0;i<phones.length;i++){
				String number = phones[i];
				if(i>=contengLen){
					break;
				}
				String cont = contents[i];
				SmsServiceStub.SmsService_Message message = new  SmsServiceStub.SmsService_Message();
				message.setAppMessageID(number);		      	//应用方流水
				message.setDestTermID(number);                	//短消息接收号码
				message.setMsgContent(cont);          			//短信内容
				message.setApplyTime(sendTime);         		//计划发送时间	格式为：YYYYMMDDHHMMSS（年月日时分秒）
				message.setSendMan(sendUser);                   //发送人
				message.setRem1("");                            //备用字段1
				message.setRem2("");                            //备用字段2
				sendMessage.addMessage(message); 
			}
       		
       		//调用Webservice
       		SmsServiceStub.SendMessageResponse resp = stub.sendMessage(sendMessage) ; 
       		//获取结果数据
       		SmsServiceStub.SmsService_MessageStatus[] statusArray = resp.get_return(); 
       		return statusArray;
        }catch(SmsServiceExceptionException0  e){
        	SmsServiceStub.Exception err = e.getFaultMessage().getSmsServiceException();
        	throw new LfwRuntimeException("短信发送失败!error:"+err.getMessage());
        } catch (AxisFault e) {
        	throw new LfwRuntimeException("短信发送初始化异常!error:"+e.getMessage());
		} catch (RemoteException e) {
			throw new LfwRuntimeException("短信发送过程中通信异常!error:"+e.getMessage());
		}
	}
	
	//应用流水号   手机号加上发送内容手机号位置和内容位置
	public static String smsAppId(String phone,int locatX,int locatY){
		return phone+locatX+""+locatY;
	}
	
	private static void getSmsGroupUser(){
		if(groupid==null||user==null||password==null){
			Properties prop = new Properties();
			InputStream inputStream  = PhoneMessageMgr.class.getResourceAsStream("SmsConfig.properties"); 
			try {
				prop.load(inputStream);
				groupid = prop.getProperty("groupid");
				user = prop.getProperty("user");
				password = prop.getProperty("password");
			} catch (IOException e) {
				ScapLogger.error("读取短信集团号失败!");
			}
		}
	}
	
	public static void resetGroupUser(){
		PhoneMessageMgr.groupid = null;
		PhoneMessageMgr.user = null;
		PhoneMessageMgr.password = null;
	}
	
	public static void main(String[] args){
		SmsServiceStub.SmsService_MessageStatus[] aa = sendmsg(new String[]{"18610435158"},new String[]{"test!"});
		System.out.println(aa[0]);
	}
}
