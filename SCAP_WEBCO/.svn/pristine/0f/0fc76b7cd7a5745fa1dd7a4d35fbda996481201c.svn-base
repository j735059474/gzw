package nc.sms;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import nc.sfbase.client.ClientToolKit;
import uap.json.JSONObject;

public class SmsClient {
   /**
    * @param args
    */
   private static String URL = "http://luqzh/portal/SmsCode";
   private static String sessionID = null;
   public static JSONObject service(Map<String,String> params) {

       String postData = "?action="+params.get("action")+"&uid=" +params.get("uid");
       if(params.get("smscheckcode") != null) {
    	   postData = postData + "&smscheckcode=" + params.get("smscheckcode");
       }
       JSONObject var = GetResponseDataByID(URL, postData);
       if (var != null && var.opt("SessionID") != null) {
    	   sessionID = (String)var.get("SessionID");
       }
		return var;
   }

   public static void PrintStrs(String[] str) {
       for (String s : str) {
           System.out.print(s + ",");
       }
       System.out.println();
   }

   public static JSONObject GetResponseDataByID(String url, String postData) {
	   JSONObject jsonObject = null;
       String data = StringUtils.EMPTY;
       try {
    	   ClientToolKit.getServerURL();
    	   StringBuilder sbUrl = new StringBuilder(ClientToolKit.getServerURL())
			.append("portal/SmsCode");
//    	   if (sessionID != null) {
//    		   sbUrl.append(";jsessionid=").append(sessionID);
//    	   }
           URL dataUrl = new URL(sbUrl.toString()+postData);
		   HttpURLConnection con = (HttpURLConnection) dataUrl.openConnection();
		   con.setDoInput(true);
		   con.setDoOutput(true);
           con.setRequestMethod("POST");
           con.setRequestProperty("Proxy-Connection", "Keep-Alive");
           String cookie = "JSESSIONID="+sessionID;
           con.setRequestProperty("Cookie", cookie); 
           OutputStream os = con.getOutputStream();
           DataOutputStream dos = new DataOutputStream(os);
           //dos.write(postData.getBytes());
           dos.write("luqingzhitest".getBytes());
           dos.flush();
           dos.close();
           
           //con.connect();
           
           BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
           String line;  
           while ((line = in.readLine()) != null) {  
        	   data += line;  
           }  
           jsonObject = new JSONObject(data);
           in.close();  
           //JSONObject jsonObject = JSONObject.fromObject(line); 
           con.disconnect();
       } catch (Exception ex) {
           ex.printStackTrace();
       }
       return jsonObject;
   }
}