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
 * 读取Properties工具类 
 * @author liyuchen
 * 2015-03-31
 */
public class PropertiesUtil {
	/** 
	 * 根据key读取value
	 * filePath :文件路径
	 * key:key值
	 * isIn 是否读取jar包内部配置文件 tree=内部，false=外部
	 * 如果是swing开发请传入true
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
	 * 读取properties的所有信息
	 * 如果是swing开发请传入true
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
	 * 写入properties 
	 * 如果是swing开发请传入true
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
			// 从输入流中读取属性列表（键和元素对）
			prop.load(fis);
			// 调用 Hashtable 的方法 put。使用 getProperty 方法提供并行性。
			// 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
			OutputStream fos = new FileOutputStream(filePath);
			prop.setProperty(parameterName, parameterValue);
			// 以适合使用 load 方法加载到 Properties 表中的格式，
			// 将此 Properties 表中的属性列表（键和元素对）写入输出流
			prop.store(fos, "Update '" + parameterName + "' value");
		} catch (IOException e) {
			System.err.println("Visit " + filePath + " for updating "+ parameterName + " value error");
		}
	}
}
