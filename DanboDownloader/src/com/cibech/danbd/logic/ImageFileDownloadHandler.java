package com.cibech.danbd.logic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

import com.cibech.danbd.consts.AppConsts;
import com.cibech.danbd.gui.MainFrame;

public class ImageFileDownloadHandler implements ResponseHandler<Integer> {
	
	private String _strFilePath;
	private int _nTaskId;
	private final static int READ_BUFF_SIZE = 32*1024;		//32K
	private final static int FAIL_RETY_COUNT = 10;
	
	public ImageFileDownloadHandler(String strPath, int nTaskId) {
		_strFilePath = strPath;
		_nTaskId = nTaskId;
	}

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
			
			//必须是image类型，并且必须是可接受的串流类型
			String strHeader = header.getValue();
			if(!strHeader.contains("image")) {
				return AppConsts.ERROR_CONTENT_UNEXPECT;
			}
			
			//记录读取进度
			long nFileSize = respEntity.getContentLength();
			long nReadedSize = 0;
			
			//开始读取并写入图片，检查是否存在
			File picFile = new File(_strFilePath);
            if(picFile.exists()) {
            	
            	//如果存在则尝试删除，因为MD5比较失败
            	if(!picFile.delete()) {
            		//如果删除失败，则毫无办法
            		return AppConsts.ERROR_FILE_EXIST;
            	}
            }
            
            //创建读写对象
            FileOutputStream fileWriter = new FileOutputStream(picFile);
            InputStream is = respEntity.getContent();
            
            //循环读取
            int nFailCount = 0;
            byte[] pBuff = new byte[READ_BUFF_SIZE];
            while(nReadedSize < nFileSize) {
            	//尝试阻塞读取
        		int nThisRead = is.read(pBuff);
        		
        		//出现异常
        		if(nThisRead < 0) {
        			break;
        		}
        		
        		if(nThisRead == 0) {
        			nFailCount++;
        			
        			//最大重试次数
        			if(nFailCount >= FAIL_RETY_COUNT) {
        				break;
        			}
        		}
        		
        		//写入文件
        		if(nThisRead > 0) {
        			fileWriter.write(pBuff, 0, nThisRead);
        			nReadedSize += nThisRead;
        		}
        		
        		//更新界面进度
        		MainFrame.UpdateImageProgress(_nTaskId, nReadedSize*100.0f/nFileSize);
            }
            
            is.close();
            fileWriter.close();
            
            if(nReadedSize >= nFileSize) {
            	return AppConsts.RESULT_OK;
            }
            else {
            	return AppConsts.ERROR_RECEIVE_ERROR;
            }
		} else {
			return AppConsts.ERROR_CON_ERROR;
		}
	}
}
