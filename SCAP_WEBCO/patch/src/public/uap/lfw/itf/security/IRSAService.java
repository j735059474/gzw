package uap.lfw.itf.security;

import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;

/**
 * rsa 安全服务
 * 
 * @author tianxq1
 *
 */
public interface IRSAService {
	/**
	 * 返回默认的公钥
	 * @return
	 */
	public RSAPublicKey getDefaultPublicKey();
	
	/**
	 * 使用默认的私钥解密由JS加密的字符串
	 * @param encrypttext
	 * @return
	 */
	public String decryptStringByJs(String encrypttext);
	
	
	public String encodeHex(BigInteger key);
}

