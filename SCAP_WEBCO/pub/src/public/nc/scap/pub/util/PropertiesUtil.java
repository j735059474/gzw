package nc.scap.pub.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;

/** 
 * ��ȡProperties������ 
 * @author liyuchen
 * 2015-03-31
 */
public class PropertiesUtil {
	/** 
	 * ����key��ȡvalue
	 * filePath :�ļ�·��
	 * key:keyֵ
	 * isIn �Ƿ��ȡjar���ڲ������ļ� tree=�ڲ���false=�ⲿ
	 * �����swing�����봫��true
	 */
	public static String readProValue(String filePath,String key,boolean isIn){
		Properties properties = new Properties();
		InputStream in = null;
		try {
			if(isIn){
				in = PropertiesUtil.class.getClass().getResourceAsStream(filePath);
			}else{
				in = new BufferedInputStream(new FileInputStream(filePath));
			}
			properties.load(in);
			String value = properties.getProperty(key);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	/** 
	 * ��ȡproperties��������Ϣ
	 * �����swing�����봫��true
	 */
	public static String readProperties(String filePath,boolean isIn) {
		Properties props = new Properties();
		StringBuffer sb = new StringBuffer(); 
		InputStream in = null;
		try {
			if(isIn){
				in = PropertiesUtil.class.getClass().getResourceAsStream(filePath);
			}else{
				in = new BufferedInputStream(new FileInputStream(filePath));
			}
			props.load(in);
			Enumeration en = props.propertyNames();
			while (en.hasMoreElements()) {
				String key = (String) en.nextElement();
				String Property = props.getProperty(key);
				sb.append(key + Property);
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	/** 
	 * д��properties 
	 * �����swing�����봫��true
	 */
	public static void writeProperties(String filePath, String parameterName,String parameterValue,boolean isIn) {
		Properties prop = new Properties();
		InputStream fis = null;
		try {
			if(isIn){
				fis = PropertiesUtil.class.getClass().getResourceAsStream(filePath);
			}else{
				fis = new BufferedInputStream(new FileInputStream(filePath));
			}
			// ���������ж�ȡ�����б�����Ԫ�ضԣ�
			prop.load(fis);
			// ���� Hashtable �ķ��� put��ʹ�� getProperty �����ṩ�����ԡ�
			// ǿ��Ҫ��Ϊ���Եļ���ֵʹ���ַ���������ֵ�� Hashtable ���� put �Ľ����
			OutputStream fos = new FileOutputStream(filePath);
			prop.setProperty(parameterName, parameterValue);
			// ���ʺ�ʹ�� load �������ص� Properties ���еĸ�ʽ��
			// ���� Properties ���е������б�����Ԫ�ضԣ�д�������
			prop.store(fos, "Update '" + parameterName + "' value");
		} catch (IOException e) {
			System.err.println("Visit " + filePath + " for updating "+ parameterName + " value error");
		}
	}
}
