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
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class MainFrame {
	
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
		_mainShell.setLayout(new FormLayout());
		
		//登录框
		Group groupLogin = new Group(_mainShell, SWT.NONE);
		FormData fd_groupLogin = new FormData();
		fd_groupLogin.top = new FormAttachment(0, 10);
		fd_groupLogin.left = new FormAttachment(0, 10);
		fd_groupLogin.right = new FormAttachment(0, 842);
		groupLogin.setLayoutData(fd_groupLogin);
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
		gd_textTags.widthHint = 102;
		textTags.setLayoutData(gd_textTags);
		
		Label lblNewLabel = new Label(groupLogin, SWT.NONE);
		lblNewLabel.setAlignment(SWT.RIGHT);
		GridData gd_lblNewLabel = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel.widthHint = 57;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		lblNewLabel.setText(AppConsts.MAINFORM_STARTPAGE);
		
		textStartPage = new Text(groupLogin, SWT.BORDER);
		GridData gd_textStartPage = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_textStartPage.widthHint = 61;
		textStartPage.setLayoutData(gd_textStartPage);
		
		Label label = new Label(groupLogin, SWT.NONE);
		label.setAlignment(SWT.RIGHT);
		GridData gd_label = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_label.widthHint = 53;
		label.setLayoutData(gd_label);
		label.setText(AppConsts.MAINFORM_ENDPAGE);
		
		textEndPage = new Text(groupLogin, SWT.BORDER);
		GridData gd_textEndPage = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_textEndPage.widthHint = 42;
		textEndPage.setLayoutData(gd_textEndPage);
		
		Label lblCountPerpage = new Label(groupLogin, SWT.NONE);
		lblCountPerpage.setAlignment(SWT.RIGHT);
		lblCountPerpage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblCountPerpage.setText(AppConsts.MAINFORM_POSTPERPAGE);
		
		textCountPerPage = new Text(groupLogin, SWT.BORDER);
		GridData gd_textCountPerPage = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_textCountPerPage.widthHint = 34;
		textCountPerPage.setLayoutData(gd_textCountPerPage);
		
		Label lblPath = new Label(groupLogin, SWT.NONE);
		lblPath.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPath.setText(AppConsts.MAINFORM_LOCALPATH);
		
		textBasePath = new Text(groupLogin, SWT.BORDER);
		textBasePath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 7, 1));
		
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
		btnBrowseBase.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnBrowseBase.setText(AppConsts.MAINFORM_BROWSE);
		
		//文件列表
		tableFileList = new Table(_mainShell, SWT.BORDER | SWT.FULL_SELECTION);
		fd_groupLogin.bottom = new FormAttachment(100, -506);
		FormData fd_tableFileList = new FormData();
		fd_tableFileList.top = new FormAttachment(groupLogin, 6);
		fd_tableFileList.right = new FormAttachment(groupLogin, 0, SWT.RIGHT);
		fd_tableFileList.left = new FormAttachment(0, 10);
		tableFileList.setLayoutData(fd_tableFileList);
		tableFileList.setHeaderVisible(true);
		tableFileList.setLinesVisible(true);
		
		TableColumn tblclmnFileIndex = new TableColumn(tableFileList, SWT.LEFT);
		tblclmnFileIndex.setWidth(50);
		tblclmnFileIndex.setText(AppConsts.MAINFORM_TALBE_FILEINDEX);
		
		TableColumn tblclmnFileName = new TableColumn(tableFileList, SWT.CENTER);
		tblclmnFileName.setWidth(300);
		tblclmnFileName.setText(AppConsts.MAINFORM_TALBE_FILENAME);
		
		TableColumn tblclmnFileSize = new TableColumn(tableFileList, SWT.RIGHT);
		tblclmnFileSize.setWidth(80);
		tblclmnFileSize.setText(AppConsts.MAINFORM_TALBE_FILESIZE);
		
		TableColumn tblclmnProgress = new TableColumn(tableFileList, SWT.CENTER);
		tblclmnProgress.setWidth(200);
		tblclmnProgress.setText(AppConsts.MAINFORM_TALBE_PROGRESS);
		fd_tableFileList.bottom = new FormAttachment(100, -142);
		
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
		btnGetFileList.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnGetFileList.setText(AppConsts.MAINFORM_GETFILELIST);
		
		//设置参数
		Button btnSetParameter = new Button(groupLogin, SWT.NONE);
		GridData gd_btnSetParameter = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_btnSetParameter.widthHint = 87;
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
		
		listLog = new Table(_mainShell, SWT.BORDER | SWT.FULL_SELECTION);
		FormData fd_listLog = new FormData();
		fd_listLog.right = new FormAttachment(groupLogin, 0, SWT.RIGHT);
		fd_listLog.bottom = new FormAttachment(tableFileList, 132, SWT.BOTTOM);
		fd_listLog.top = new FormAttachment(tableFileList, 6);
		fd_listLog.left = new FormAttachment(0, 10);
		listLog.setLayoutData(fd_listLog);
		listLog.setHeaderVisible(true);
		listLog.setLinesVisible(true);
		
		TableColumn tableColumnTime = new TableColumn(listLog, SWT.NONE);
		tableColumnTime.setWidth(200);
		tableColumnTime.setText(AppConsts.MAINFORM_TALBE_TIME);
		
		TableColumn tableColumnLog = new TableColumn(listLog, SWT.NONE);
		tableColumnLog.setWidth(400);
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
		
		//开始工作
		_runDownImage.StartQueue();
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
                
                item.setText(0, String.valueOf(nCurTableItemCount));
                item.setText(1, task.getImage_filename());
                item.setText(2, String.format("%.2f M", task.getImage_size() / 1024.0 / 1024.0));
                item.setText(3, "0.00%");
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
}
