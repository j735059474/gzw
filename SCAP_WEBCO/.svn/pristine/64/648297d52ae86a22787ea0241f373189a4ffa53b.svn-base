package uap.lfw.ra.render.pc.impl;

import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;

import uap.lfw.itf.security.IRSAService;
import org.apache.commons.codec.binary.Hex;

public class RSAService implements IRSAService{

	@Override
	public RSAPublicKey getDefaultPublicKey() {
		return RSAUtils.getDefaultPublicKey();
	}

	@Override
	public String decryptStringByJs(String encrypttext) {
		return RSAUtils.decryptStringByJs(encrypttext);
	}

	@Override
	public String encodeHex(BigInteger key) {
		return new String(Hex.encodeHex(key.toByteArray()));
	}

}

