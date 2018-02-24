package uap.lfw.itf.security;

import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;

/**
 * rsa ��ȫ����
 * 
 * @author tianxq1
 *
 */
public interface IRSAService {
	/**
	 * ����Ĭ�ϵĹ�Կ
	 * @return
	 */
	public RSAPublicKey getDefaultPublicKey();
	
	/**
	 * ʹ��Ĭ�ϵ�˽Կ������JS���ܵ��ַ���
	 * @param encrypttext
	 * @return
	 */
	public String decryptStringByJs(String encrypttext);
	
	
	public String encodeHex(BigInteger key);
}

