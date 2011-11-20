package com.gavin.ediCustoms.server.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Service;

import sun.misc.BASE64Encoder;

@Service("encryptor")
public class Encryptor {
	public String encrypt(String plaintext){
		String cryptograph = null;
		try {
			cryptograph=encoderByMd5(plaintext);
			cryptograph=encoderByMd5(cryptograph);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
		return cryptograph;
	}

	private String encoderByMd5(String str) throws NoSuchAlgorithmException,
			UnsupportedEncodingException {
		// 确定计算方法
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		BASE64Encoder base64en = new BASE64Encoder();
		// 加密后的字符串
		String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
		return newstr;
	}
}
