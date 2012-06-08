package com.cibech.danbd.gui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.cibech.danbd.beans.ImageFileBean;
import com.cibech.danbd.beans.ImageTaskBean;
import com.cibech.danbd.consts.AppConsts;
import com.cibech.danbd.logic.DanbooruAPILogic;
import com.cibech.danbd.logic.DownloadListWorker;
import com.cibech.danbd.logic.SnifferPostPagesWorker;

import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class MainFrame {
	
	//定义Table列
	private static final int COL_FILE_INEDEX = 0;
	private static final int COL_FILE_IMAGENAME = 1;
	private static final int COL_FILE_SIZE = 2;
	private static final int COL_FILE_RESOLUTION = 3;
	private static final int COL_FILE_PROGRESS = 4;
	
	private static DownloadListWorker _runDownImage;
	private static Thread _snifferThread = null;
	private static Display _display;
	private static Shell _mainShell;
	private static List<ImageFileBean> _lstImages = new ArrayList<ImageFileBean>();
	
	private static Text textUserName;
	private static Text textPass;
	private static Text textTags;
	private static Text textStartPage;
	private static Text textEndPage;
	private static Text textCountPerPage;
	private static Text textBasePath;
	
	private static Table tableFileList;
	private static int nCurTableItemCount = 0;
	private static int nListLogCount = 0;
	private static Table listLog;

	public static void main(String[] args) {

		_display = new Display();

		//主对话框
		_mainShell = new Shell(_display);
		_mainShell.setSize(860, 614);
		_mainShell.setText(AppConsts.APP_NAME);
		_mainShell.setLayout(new GridLayout(1, false));
		
		//登录框
		Group groupLogin = new Group(_mainShell, SWT.NONE);
		GridData gd_groupLogin = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_groupLogin.widthHint = 835;
		groupLogin.setLayoutData(gd_groupLogin);
		groupLogin.setText(AppConsts.MAINFORM_LOGINGROUP);
		groupLogin.setLayout(new GridLayout(12, false));
		
		//用户名密码
		Label lblName = new Label(groupLogin, SWT.NONE);
		lblName.setAlignment(SWT.RIGHT);
		GridData gd_lblName = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_lblName.widthHint = 38;
		lblName.setLayoutData(gd_lblName);
		lblName.setText(AppConsts.MAINFORM_USERNAME);
		
		textUserName = new Text(groupLogin, SWT.BORDER);
		GridData gd_textUserName = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_textUserName.widthHint = 86;
		textUserName.setLayoutData(gd_textUserName);
		
		Label lblPass = new Label(groupLogin, SWT.NONE);
		lblPass.setAlignment(SWT.RIGHT);
		GridData gd_lblPass = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_lblPass.widthHint = 36;
		lblPass.setLayoutData(gd_lblPass);
		lblPass.setText(AppConsts.MAINFORM_PASS);
		
		textPass = new Text(groupLogin, SWT.BORDER);
		GridData gd_textPass = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_textPass.widthHint = 98;
		textPass.setLayoutData(gd_textPass);
		
		Label lblTag = new Label(groupLogin, SWT.NONE);
		GridData gd_lblTag = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_lblTag.widthHint = 31;
		lblTag.setLayoutData(gd_lblTag);
		lblTag.setAlignment(SWT.RIGHT);
		lblTag.setText(AppConsts.MAINFORM_TAGS);
		
		textTags = new Text(groupLogin, SWT.BORDER);
		GridData gd_textTags = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_textTags.widthHint = 132;
		textTags.setLayoutData(gd_textTags);
		
		Label lblNewLabel = new Label(groupLogin, SWT.NONE);
		lblNewLabel.setAlignment(SWT.RIGHT);
		GridData gd_lblNewLabel = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel.widthHint = 57;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		lblNewLabel.setText(AppConsts.MAINFORM_STARTPAGE);
		
		textStartPage = new Text(groupLogin, SWT.BORDER);
		GridData gd_textStartPage = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_textStartPage.widthHint = 48;
		textStartPage.setLayoutData(gd_textStartPage);
		
		Label label = new Label(groupLogin, SWT.NONE);
		label.setAlignment(SWT.RIGHT);
		GridData gd_label = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_label.widthHint = 53;
		label.setLayoutData(gd_label);
		label.setText(AppConsts.MAINFORM_ENDPAGE);
		
		textEndPage = new Text(groupLogin, SWT.BORDER);
		GridData gd_textEndPage = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_textEndPage.widthHint = 43;
		textEndPage.setLayoutData(gd_textEndPage);
		
		Label lblCountPerpage = new Label(groupLogin, SWT.NONE);
		lblCountPerpage.setAlignment(SWT.RIGHT);
		lblCountPerpage.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblCountPerpage.setText(AppConsts.MAINFORM_POSTPERPAGE);
		
		textCountPerPage = new Text(groupLogin, SWT.BORDER);
		GridData gd_textCountPerPage = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_textCountPerPage.widthHint = 34;
		textCountPerPage.setLayoutData(gd_textCountPerPage);
		
		Label lblPath = new Label(groupLogin, SWT.NONE);
		lblPath.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPath.setText(AppConsts.MAINFORM_LOCALPATH);
		
		textBasePath = new Text(groupLogin, SWT.BORDER);
		GridData gd_textBasePath = new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1);
		gd_textBasePath.widthHint = 421;
		textBasePath.setLayoutData(gd_textBasePath);
		
		//浏览文件夹
		Button btnBrowseBase = new Button(groupLogin, SWT.NONE);
		btnBrowseBase.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				//左键按下
				if(e.button == 1) {
					//弹出文件夹选择
					DirectoryDialog dialog = new DirectoryDialog(_mainShell);
					dialog.setText(AppConsts.DERECTORY_BROWSE);
					dialog.setMessage(AppConsts.DERECTORY_BROWSE_MSG);

					//如果选择了确定
					if(dialog.open() != null) {

				        String strPath = dialog.getFilterPath();
				        textBasePath.setText(strPath);
				    }
				}
			}
		});
		GridData gd_btnBrowseBase = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_btnBrowseBase.widthHint = 43;
		btnBrowseBase.setLayoutData(gd_btnBrowseBase);
		btnBrowseBase.setText(AppConsts.MAINFORM_BROWSE);
		
		//获取文件列表
		Button btnGetFileList = new Button(groupLogin, SWT.NONE);
		btnGetFileList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				//左键按下
				if(e.button == 1) {

					//获取路径
					String strBasePath = textBasePath.getText();
					
					//清空列表
			        _lstImages.clear(); 
			        
			        //刷新
					refreshFileList(strBasePath, _lstImages);
				}
			}
		});
		btnGetFileList.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		btnGetFileList.setText(AppConsts.MAINFORM_GETFILELIST);
		
		//文件列表
		tableFileList = new Table(_mainShell, SWT.BORDER | SWT.FULL_SELECTION);
		GridData gd_tableFileList = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_tableFileList.heightHint = 313;
		tableFileList.setLayoutData(gd_tableFileList);
		tableFileList.setHeaderVisible(true);
		tableFileList.setLinesVisible(true);
		
		TableColumn tblclmnFileIndex = new TableColumn(tableFileList, SWT.LEFT);
		tblclmnFileIndex.setWidth(50);
		tblclmnFileIndex.setText(AppConsts.MAINFORM_TALBE_FILEINDEX);
		
		TableColumn tblclmnFileName = new TableColumn(tableFileList, SWT.CENTER);
		tblclmnFileName.setWidth(250);
		tblclmnFileName.setText(AppConsts.MAINFORM_TALBE_FILENAME);
		
		TableColumn tblclmnFileSize = new TableColumn(tableFileList, SWT.RIGHT);
		tblclmnFileSize.setWidth(80);
		tblclmnFileSize.setText(AppConsts.MAINFORM_TALBE_FILESIZE);
		
		TableColumn tblclmnResolution = new TableColumn(tableFileList, SWT.RIGHT);
		tblclmnResolution.setWidth(100);
		tblclmnResolution.setText(AppConsts.MAINFORM_TALBE_RESOLUTION);
		
		TableColumn tblclmnProgress = new TableColumn(tableFileList, SWT.RIGHT);
		tblclmnProgress.setWidth(66);
		tblclmnProgress.setText(AppConsts.MAINFORM_TALBE_PROGRESS);
		new Label(groupLogin, SWT.NONE);
		
		//设置参数
		Button btnSetParameter = new Button(groupLogin, SWT.NONE);
		GridData gd_btnSetParameter = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btnSetParameter.widthHint = 51;
		btnSetParameter.setLayoutData(gd_btnSetParameter);
		btnSetParameter.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				//左键按下
				if(e.button == 1) {
					String strUserName = textUserName.getText();
					String strPassword = textPass.getText();
					String strTags = textTags.getText();
					String strBasePath = textBasePath.getText();
					int nLimit = Integer.parseInt(textCountPerPage.getText());
					int nStartPage = Integer.parseInt(textStartPage.getText());;
					int nStopPage = Integer.parseInt(textEndPage.getText());;
					
					DanbooruAPILogic logic = new DanbooruAPILogic();
					logic.SetUserInfo(strUserName, strPassword, strBasePath, strTags, nStartPage, nStopPage, nLimit);
					
					//建立并开启嗅探线程
					SnifferPostPagesWorker woker = new SnifferPostPagesWorker();
					woker.set_logic(logic);
					
					_snifferThread = new Thread(woker);
					_snifferThread.start();	
				}
			}
		});
		
		btnSetParameter.setText(AppConsts.MAINFORM_LOGIN);
		
		//开始任务
		Button btnBeginTask = new Button(groupLogin, SWT.NONE);
		btnBeginTask.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				
				if(e.button == 1) {
					
					//开始下载
					_runDownImage.StartQueue();
				}
			}
		});
		GridData gd_btnBeginTask = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btnBeginTask.widthHint = 46;
		btnBeginTask.setLayoutData(gd_btnBeginTask);
		btnBeginTask.setText(AppConsts.MAINFORM_START);
		
		//暂停任务
		Button btnPauseTask = new Button(groupLogin, SWT.NONE);
		btnPauseTask.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				
					if(e.button == 1) {
					
					//开始下载
					_runDownImage.PauseQueue();
				}
			}
		});
		GridData gd_btnPauseTask = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btnPauseTask.widthHint = 37;
		btnPauseTask.setLayoutData(gd_btnPauseTask);
		btnPauseTask.setText(AppConsts.MAINFORM_PAUSE);
		
		listLog = new Table(_mainShell, SWT.BORDER | SWT.FULL_SELECTION);
		GridData gd_listLog = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_listLog.heightHint = 138;
		listLog.setLayoutData(gd_listLog);
		listLog.setHeaderVisible(true);
		listLog.setLinesVisible(true);
		
		TableColumn tableColumnTime = new TableColumn(listLog, SWT.NONE);
		tableColumnTime.setWidth(150);
		tableColumnTime.setText(AppConsts.MAINFORM_TALBE_TIME);
		
		TableColumn tableColumnLog = new TableColumn(listLog, SWT.NONE);
		tableColumnLog.setWidth(650);
		tableColumnLog.setText(AppConsts.MAINFORM_TALBE_LOG);
		
		_mainShell.open();
		
		//初始化程序
		InitApp();
		
		while(!_mainShell.isDisposed()) {
			if(!_display.readAndDispatch())
				_display.sleep();
		}
		
		//结束线程
		_runDownImage.MarkQuit();
		
		_display.dispose();
	}
	
	//初始化程序
	public static void InitApp() {
		
		//建立并运行下载线程
		_runDownImage = new DownloadListWorker();
		new Thread(_runDownImage).start();
	}
	
	//界面程序
	public static void AddTaskItemToTable(final ImageTaskBean task) {
		
		//异步更新
		_display.asyncExec(new Runnable() {

            public void run() {
            	
            	//检查界面
                if(tableFileList.isDisposed()) return;
                
                TableItem item = new TableItem(tableFileList, SWT.NULL);
                nCurTableItemCount++;
                
                item.setText(COL_FILE_INEDEX, String.valueOf(nCurTableItemCount));
                item.setText(COL_FILE_IMAGENAME, task.getImage_filename());
                item.setText(COL_FILE_SIZE, String.format("%.2f M", task.getImage_size() / 1024.0 / 1024.0));
                item.setText(COL_FILE_RESOLUTION, String.valueOf(task.getWidth()) + " x " + String.valueOf(task.getHeight()));
                item.setText(COL_FILE_PROGRESS, "0.00 %");                
            }	
		});
	}
	
	//添加任务到图片下载线程
	public static void AddImageDownloadTask(final ImageTaskBean task) {
		
		_runDownImage.AddToTaskQueue(task);
	}
	
	//添加一行日志
	public static void AddLogInformation(Object s, final String strLog) {
		
		//异步更新
		_display.asyncExec(new Runnable() {

            public void run() {
            	
            	//检查界面
                if(listLog.isDisposed()) return;
                
                SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                
                TableItem item = new TableItem(listLog, SWT.NULL);
                item.setText(0, tempDate.format(new java.util.Date()));
                item.setText(1, strLog);

                listLog.select(nListLogCount++);
                listLog.showSelection();
            }	
		});
	}
	
	//遍历文件列表
	public static void refreshFileList(String strPath, List<ImageFileBean> lstImages) { 
		
        File dir = new File(strPath);
        File[] files = dir.listFiles();
        
        if (files == null) 
            return;
               
        for(int i = 0; i < files.length; i++) { 
            
        	if (files[i].isDirectory()) {
        		
                refreshFileList(files[i].getAbsolutePath(), lstImages); 
            } else {
            	
                String strFileName = files[i].getAbsolutePath().substring(files[i].getAbsolutePath().lastIndexOf('\\') + 1);
                
                if(strFileName.contains(".jpg") || strFileName.contains(".JPG") 
                		|| strFileName.contains(".png") || strFileName.contains(".PNG")) {
                	
                	ImageFileBean image = new ImageFileBean();
                	image.setFilename(strFileName);
                	image.setFilefullpath(files[i].getAbsolutePath());
                	
                	lstImages.add(image);
                }                 
            } 
        }
    }
	
	//检查文件名是否存在，如果存在，返回全路径
	public static String CheckFileNameExist(String strFileName) {
		
		for(int i = 0; i < _lstImages.size(); i++) {
			
			if(strFileName.compareTo(_lstImages.get(i).getFilename()) == 0) {
				
				return _lstImages.get(i).getFilefullpath();
			}
		}
		
		return null;
	}
	
	//更新文件下载进度
	public static void UpdateImageProgress(final int nTaskId, final float fProgres) {
		
		//异步更新
		_display.asyncExec(new Runnable() {

            public void run() {
            	
            	//检查界面
                if(tableFileList.isDisposed()) return;
                
                try {
                	
                	TableItem item = tableFileList.getItem(nTaskId);
                	
                	//更新进度
                	item.setText(COL_FILE_PROGRESS, String.format("%.2f", fProgres) + " %");
                	
                } catch(IllegalArgumentException e) {
                
                	e.printStackTrace();
                }
            }	
		});
	}
	
	//设置单条任务完成
	public static void MarkTaskStatus(final int nTaskId, final int nStatus) {
		//异步更新
		_display.asyncExec(new Runnable() {

            public void run() {
            	
            	//检查界面
                if(tableFileList.isDisposed()) return;
                
                try {
                	
                	TableItem item = tableFileList.getItem(nTaskId);
                	
                	if(nStatus == AppConsts.STATUS_FINISH) {
                		item.setBackground(_display.getSystemColor(SWT.COLOR_GREEN));
                	} else if(nStatus == AppConsts.STATUS_FAILED) {
                		item.setBackground(_display.getSystemColor(SWT.COLOR_RED));
                	}
                	
                } catch(IllegalArgumentException e) {
                
                	e.printStackTrace();
                }
            }	
		});
	}
}
