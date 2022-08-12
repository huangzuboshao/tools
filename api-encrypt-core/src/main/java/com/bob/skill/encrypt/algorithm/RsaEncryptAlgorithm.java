package com.bob.skill.encrypt.algorithm;


import com.bob.skill.encrypt.util.RsaEncryptUtils;

/**
 * 自定义RSA算法
 * 
 * @author bob
 *
 */
public class RsaEncryptAlgorithm implements EncryptAlgorithm {

	@Override
	public String encrypt(String content, String encryptKey) throws Exception {
		return RsaEncryptUtils.encryptByPublicKey(content);
	}

	@Override
	public String decrypt(String encryptStr, String decryptKey) throws Exception {
		return RsaEncryptUtils.decryptByPrivateKey(encryptStr);
	}
}
