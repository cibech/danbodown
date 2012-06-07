package com.cibech.danbd.logic;

import java.util.concurrent.LinkedBlockingQueue;

import com.cibech.danbd.beans.ImageTaskBean;
import com.cibech.danbd.consts.AppConsts;
import com.cibech.danbd.util.DanTools;

public class DownloadListWorker implements Runnable {
	
	private DanbooruAPILogic _logic = new DanbooruAPILogic();
	private LinkedBlockingQueue<ImageTaskBean> _queueTask = new LinkedBlockingQueue<ImageTaskBean>();
	private boolean _bNeedEnd = false;
	private boolean _bIsProcessing = false;

	@Override
	public void run() {
		
		while(!_bNeedEnd) {
			
			//检查是否开始队列, 否则等待
			if(!_bIsProcessing) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}
			
			//开始队列
			ImageTaskBean taskbean = _queueTask.poll();
			if(taskbean != null) {
				
				//开始任务
				Integer nRet = _logic.DownloadPostImage(taskbean);
				//如果提示成功，检查MD5
				if(nRet == AppConsts.RESULT_OK) {
				
					String fileMd5 = DanTools.getFileMd5(taskbean.getImage_fullpath());
					if(!taskbean.getFile_md5().equalsIgnoreCase(fileMd5)) {
						//校验出错
						
					}
				}
				// 文件已存在
				else if(nRet == AppConsts.ERROR_FILE_EXIST) {
					
				}
			}
			
			//暂停后开始下一个任务
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean AddToTaskQueue(ImageTaskBean task) {
		
		return _queueTask.offer(task);
	}
	
	public void StartQueue() {
		_bIsProcessing = true;
	}
	
	public void PauseQueue() {
		_bIsProcessing = false;
	}
	
	public void MarkQuit() {
		_bNeedEnd = true;
	}
}
