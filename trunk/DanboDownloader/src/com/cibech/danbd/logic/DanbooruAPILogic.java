package com.cibech.danbd.logic;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;

import com.cibech.danbd.beans.ImagePost;
import com.cibech.danbd.beans.ImageTaskBean;
import com.cibech.danbd.consts.AppConsts;
import com.cibech.danbd.util.DanTools;

public class DanbooruAPILogic {
	
	private static String _strUserName = "";		//当前用户名
	private static String _strPassword = "";		//当前密码HASH
	private static int _nPageLimit = 5;				//单页数量
	private static String _strBasePath = "C:\\";	//默认路径
	
	private final static String THIS_PROTOCAL = "http"; 
	private final static String BASE_URL = "danbooru.donmai.us";
	
	//POST地址和参数
	private final static String POST_URL = "/post/index.json";
	private final static String POST_PARA_LIMIT = "limit";
	private final static String POST_PARA_PAGE = "page";
	private final static String POST_PARA_TAG = "tags";
	
	//用户认证参数
	private final static String LOGIN_KEY = "login";
	private final static String PASS_KEY = "password_hash";
	
	public static String GetBasePath() {
		return _strBasePath;
	}

	//生成认证信息
	public void SetUserInfo(String strUserName, String strPass, String strBase, int nPageLimit) {
		
		_strBasePath = (strBase.lastIndexOf(0) == '\\' ? strBase : strBase + '\\');
		_nPageLimit = nPageLimit;
		_strUserName = strUserName;
		_strPassword = DanTools.getStringSHA1("choujin-steiner--" + strPass + "--");
	}
	
	//处理Post Tag
	public int RequestTaggedPosts(String strTag, int nPage, List<ImagePost> lstPosts) {
		
		HttpClient httpclient = new DefaultHttpClient();
		Integer nRet = AppConsts.RESULT_OK;
		
		try {			
			//构建地址
			URIBuilder builder = new URIBuilder();
			builder.setScheme(THIS_PROTOCAL).setHost(BASE_URL).setPath(POST_URL)
				.setParameter(LOGIN_KEY, _strUserName)
				.setParameter(PASS_KEY, _strPassword)
			    .setParameter(POST_PARA_LIMIT, String.valueOf(_nPageLimit))
			    .setParameter(POST_PARA_PAGE, String.valueOf(nPage))
			    .setParameter(POST_PARA_TAG, strTag);			
		
			HttpGet httpget = new HttpGet(builder.build());
			PostResultJSONHandler postHandler = new PostResultJSONHandler();
            nRet = httpclient.execute(httpget, postHandler);
            
            lstPosts.addAll(postHandler.get_listPosts());
            
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		
		return nRet;
	}
	
	//处理Image
	public int DownloadPostImage(ImageTaskBean bean) {
		
		HttpClient httpclient = new DefaultHttpClient();
		Integer nRet = AppConsts.RESULT_OK;
		
		try {			
			//构建请求		
			HttpGet httpget = new HttpGet(bean.getImage_url());
			ImageFileDownloadHandler fileHandler = new ImageFileDownloadHandler(bean.getImage_fullpath());
            nRet = httpclient.execute(httpget, fileHandler);
             
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		
		return nRet;
	}
}
