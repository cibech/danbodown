package com.cibech.danbd.logic;

import java.util.ArrayList;
import java.util.List;

import com.cibech.danbd.beans.ImagePost;
import com.cibech.danbd.beans.ImageTaskBean;
import com.cibech.danbd.consts.AppConsts;
import com.cibech.danbd.gui.MainFrame;
import com.cibech.danbd.util.DanTools;

public class SnifferPostPagesWorker implements Runnable {
	
	private DanbooruAPILogic _logic;

	@Override
	public void run() {
		
		List<ImagePost> lstPosts = new ArrayList<ImagePost>();
		
		//逐个嗅探页面
		for(int i = _logic._nStartPage; i <= _logic._nStopPage; i++) {
			
			lstPosts.clear();
			int nRet = _logic.RequestTaggedPosts(i, lstPosts);
			
			if(nRet == AppConsts.RESULT_OK) {
				
				//将所有的任务加入列表
				for(int j = 0; j < lstPosts.size(); j++) {
									
					ImageTaskBean task = new ImageTaskBean(lstPosts.get(j));
					
					//检查是否已存在本地
					String strLocalFile = MainFrame.CheckFileNameExist(task.getImage_filename());
					
					//如果已存在，检查MD5，如果一致则忽略
					if(strLocalFile != null) {
						String localMd5 = DanTools.getFileMd5(strLocalFile);
						
						//MD5一致，忽略
						if(localMd5.compareToIgnoreCase(task.getFile_md5()) == 0) {
							
							MainFrame.AddLogInformation(this, "项目 " + task.getImage_filename() + " 与本地路径 " + strLocalFile + " 同名，且MD5校验一致，忽略本项目");
							
							continue;
						} else {
							
							MainFrame.AddLogInformation(this, "项目  " + task.getImage_filename() + " 同名文件已存在，但MD5校验不一致，添加到下载列表");
						}
					}
					
					//添加到下载线程
					MainFrame.AddImageDownloadTask(task);
					
					//添加到界面
					MainFrame.AddTaskItemToTable(task);
				}
				
				//等待一秒后启动另外的任务
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void set_logic(DanbooruAPILogic logic) {
		_logic = logic;
	}
}
