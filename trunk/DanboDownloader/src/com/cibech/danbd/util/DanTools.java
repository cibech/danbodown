package com.cibech.danbd.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;

public class DanTools {

	public static String getFileMd5(String strFilePath) {
		
		String strMd5 = "";
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(new File(strFilePath));
			strMd5 = DigestUtils.md5Hex(fis);
			
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return strMd5;
	}
	
	public static String getStringSHA1(String strText) {
		
		String strSHA1 = "";
		
		strSHA1 = DigestUtils.shaHex(strText);
		
		return strSHA1;
	}
}