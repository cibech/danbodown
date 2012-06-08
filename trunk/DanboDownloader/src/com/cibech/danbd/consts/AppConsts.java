package com.cibech.danbd.consts;

public class AppConsts {

	public final static String APP_NAME = "Danbooru Downloader";
	
	//任务状态定义
	public static final int STATUS_FINISH = 0;
	public static final int STATUS_FAILED = 1;
	
	public final static String MAINFORM_LOGINGROUP = "参数设定";
	public final static String MAINFORM_USERNAME = "用户名";
	public final static String MAINFORM_PASS = "密码";
	public final static String MAINFORM_LOGIN = "添加";
	public final static String MAINFORM_TAGS = "Tags";
	public final static String MAINFORM_STARTPAGE = "起始页码";
	public final static String MAINFORM_ENDPAGE = "终止页码";
	public final static String MAINFORM_POSTPERPAGE = "每页数量";
	public final static String MAINFORM_LOCALPATH = "本地路径";
	public final static String MAINFORM_BROWSE = "浏览";
	public final static String MAINFORM_GETFILELIST = "刷新列表";
	public final static String MAINFORM_START = "开始";
	public final static String MAINFORM_PAUSE = "暂停";
	
	public final static String MAINFORM_TALBE_FILEINDEX = "序号";
	public final static String MAINFORM_TALBE_FILENAME = "文件名";
	public final static String MAINFORM_TALBE_FILESIZE = "体积(MB)";
	public final static String MAINFORM_TALBE_RESOLUTION = "分辨率";
	public final static String MAINFORM_TALBE_PROGRESS = "进度";
	
	public final static String MAINFORM_TALBE_TIME = "时间";
	public final static String MAINFORM_TALBE_LOG = "事件";
	
	public final static String DERECTORY_BROWSE = "选择一个文件夹";
	public final static String DERECTORY_BROWSE_MSG = "选择或者新建一个文件夹来存放下载文件";
	
	//错误定义
	public final static int RESULT_OK = 0;
	public final static int ERROR_CON_ERROR = 1;			//通信错误
	public final static int ERROR_STATE_ERROR = 2;			//返回状态错误
	public final static int ERROR_ENCODING_ERROR = 3;		//编码错误
	public final static int ERROR_CONTENT_UNEXPECT = 4;		//内容错误
	public final static int ERROR_FILE_EXIST = 5;			//文件已存在
	public final static int ERROR_RECEIVE_ERROR = 6;		//接收数据时错误
}
