package nc.modulemap.toolkit;
import java.security.Key;
import java.security.MessageDigest;
import java.security.Provider;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Encoder;

public class Encode {
	static{
		if(Security.getProvider("BC")==null){
			try{
				String clsName = "org.bouncycastle.jce.provider.BouncyCastleProvider";
				Object o = Class.forName(clsName).newInstance();
				Security.addProvider((Provider)o);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	private static final byte[] keyBytes = {49,-70,11,-83,-75,7,42,-123,-63,22,-119,74,-71,98,-83,-26,100,38,-116,124,-92,-14,1,109};
	public static byte[] encode(byte[] dataBytes) throws Exception {
		Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding","BC");
		Key key = new SecretKeySpec(keyBytes, "DESede");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] retrBytes = cipher.doFinal(dataBytes);
		return retrBytes;
	}
	public static byte[] decode(byte[] crypDataBytes)throws Exception {
		Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding","BC");
		Key key = new SecretKeySpec(keyBytes, "DESede");
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] retrBytes = cipher.doFinal(crypDataBytes);
		return retrBytes;
	}
	public static byte[] getMsgDigest(byte[] orginData) throws Exception{
		MessageDigest digest = MessageDigest.getInstance("SHA1", "BC");
		digest.update(orginData);
		return digest.digest();
	}
	public static String getBase64MsgDigest(String str) throws Exception{
		if(str == null){
			return null;
		}
		byte[] orignData = str.getBytes("GBK");
		byte[] digestBytes = getMsgDigest(orignData);
		return new BASE64Encoder().encode(digestBytes);
	}
	static void  createKey() throws Exception{
		KeyGenerator generator = KeyGenerator.getInstance("DESede", "BC");
		generator.init(168);
		Key key = generator.generateKey();
		byte[] keyBytes = key.getEncoded();
		for (int i = 0; i < keyBytes.length; i++) {
			System.out.print(keyBytes[i]+",");
		}
		System.out.println();
	}
	public static void main(String...strings){
		try {
//		    createKey();
			String s = "womÎÒÃÇafdafdsafdsafdsafdsafdsafewfewafds";
			byte[] c = encode(s.getBytes());
			byte[] c2 = decode(c);
			System.out.println(s);
			System.out.println(new String(c2));
//			System.out.println(getBase64MsgDigest(s));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
