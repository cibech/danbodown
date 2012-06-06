package com.cibech.danbd.logic;

import java.io.IOException;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.cibech.danbd.consts.AppConsts;
import com.cibech.danbd.beans.ImagePost;

public class PostResultJSONHandler implements ResponseHandler<Integer> {
	
	private List<ImagePost> _listPosts;
	
	@Override
	public Integer handleResponse(HttpResponse resp)
			throws ClientProtocolException, IOException {
		
		StatusLine stl = resp.getStatusLine();
		
		//如果收到正确的反馈
		if(stl != null && stl.getStatusCode() == 200) {
		
			HttpEntity respEntity = resp.getEntity();
		
			//检查类型
			Header header;
			header = respEntity.getContentType();
			
			//必须有ContentType描述，并且结果必须大于0
			if(header == null || respEntity.getContentLength() == 0) {
				return AppConsts.ERROR_CONTENT_UNEXPECT;
			}
			
			//必须是JSON和UTF-8的编码
			String strHeader = header.getValue();
			if(!strHeader.contains("json") || !strHeader.contains("utf-8")) {
				return AppConsts.ERROR_CONTENT_UNEXPECT;
			}
						
			//转换到Bean对象
			_listPosts = new Gson().fromJson(EntityUtils.toString(respEntity, "UTF-8"), 
																new TypeToken<List<ImagePost>>(){}.getType());
			
			return AppConsts.RESULT_OK;
			
		} else {
			
			return AppConsts.ERROR_CON_ERROR;
		}
	}

	public List<ImagePost> get_listPosts() {
		return _listPosts;
	}
}
