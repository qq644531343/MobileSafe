package com.itheima.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.R.integer;

/**
 *  MD5加密工具
 * @author libo
 *
 */
public class MD5Tool {

	public static String getMD5(String string)
	{
		byte[]  hash;
		try {
			hash = MessageDigest.getInstance("md5").digest(string.getBytes());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	
		 StringBuilder hex = new StringBuilder(hash.length * 2);  
		    for (byte b : hash) {  
			 int number = b & 0xFF;
		        if (number < 0x10)  
		            hex.append("0");  
		        hex.append(Integer.toHexString(number));  
		    }  
		
		return hex.toString();
	}
	
}
