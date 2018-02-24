package nc.scap.pub.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
/** 
 * 加密算法工具类
 * @author liyuchen
 * 2015-03-23
 **/
public class EncryptUtil {
	// md5加密
	public static String makeMD5(String password) {
		MessageDigest md;
		try {
			// 生成一个MD5加密计算摘要
			md = MessageDigest.getInstance("MD5");
			// 计算md5函数
			md.update(password.getBytes());
			// digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
			// BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
			String pwd = new BigInteger(1, md.digest()).toString(16);
			System.err.println(pwd);
			return pwd;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return password;
	}

	// 获得本机mac地址
	public static String getLocalMac() throws Exception {
		InetAddress ia = InetAddress.getLocalHost();
		// TODO Auto-generated method stub
		// 获取网卡，获取地址
		byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < mac.length; i++) {
			if (i != 0) {
				sb.append("-");
			}
			// 字节转换为整数
			int temp = mac[i] & 0xff;
			String str = Integer.toHexString(temp);
			if (str.length() == 1) {
				sb.append("0" + str);
			} else {
				sb.append(str);
			}
		}
		return sb.toString().toUpperCase();
	}

	// 获得硬盘序列号
	public static String getHdSerialInfo() {
		String line = "";
		String HdSerial = "";// 记录硬盘序列号
		try {
			Process proces = Runtime.getRuntime().exec("cmd /c dir c:");// 获取命令行参数
			BufferedReader buffreader = new BufferedReader(
					new InputStreamReader(proces.getInputStream()));
			while ((line = buffreader.readLine()) != null) {
				if (line.indexOf("卷的序列号是 ") != -1) { // 读取参数并获取硬盘序列号
					HdSerial = line.substring(line.indexOf("卷的序列号是 ")
							+ "卷的序列号是 ".length(), line.length());
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return HdSerial;
	}
}
