package com.cibech.danbd.consts;

public class AppConsts {

	public final static String APP_NAME = "Danbooru Downloader";
	
	public final static String MAINFORM_LOGINGROUP = "当前用户";
	public final static String MAINFORM_USERNAME = "用户名";
	public final static String MAINFORM_PASS = "密码";
	public final static String MAINFORM_LOGIN = "设置";
	
	//错误定义
	public final static int RESULT_OK = 0;
	public final static int ERROR_CON_ERROR = 1;			//通信错误
	public final static int ERROR_STATE_ERROR = 2;			//返回状态错误
	public final static int ERROR_ENCODING_ERROR = 3;		//编码错误
	public final static int ERROR_CONTENT_UNEXPECT = 4;		//内容错误
	public final static int ERROR_FILE_EXIST = 5;			//文件已存在
	public final static int ERROR_RECEIVE_ERROR = 6;		//接收数据时错误
}
