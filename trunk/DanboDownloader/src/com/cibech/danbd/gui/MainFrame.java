package com.cibech.danbd.gui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.cibech.danbd.beans.ImagePost;
import com.cibech.danbd.beans.ImageTaskBean;
import com.cibech.danbd.consts.AppConsts;
import com.cibech.danbd.logic.DanbooruAPILogic;
import com.cibech.danbd.logic.DownloadListWorker;

import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.GridData;

public class MainFrame {
	
	private static DownloadListWorker _runDownImage;
	private static Display _display;
	private static Shell _mainShell;
	
	private static Text textUserName;
	private static Text textPass;
	private static Text textTags;
	private static Text textStartPage;
	private static Text textEndPage;
	private static Text textCountPerPage;
	private static Text textBasePath;

	public static void main(String[] args) {

		_display = new Display();

		//主对话框
		_mainShell = new Shell(_display);
		_mainShell.setSize(841, 601);
		_mainShell.setText(AppConsts.APP_NAME);
		_mainShell.setLayout(new FormLayout());
		
		//登录框
		Group groupLogin = new Group(_mainShell, SWT.NONE);
		FormData fd_groupLogin = new FormData();
		fd_groupLogin.top = new FormAttachment(0, 10);
		fd_groupLogin.left = new FormAttachment(0, 10);
		fd_groupLogin.bottom = new FormAttachment(0, 127);
		fd_groupLogin.right = new FormAttachment(0, 823);
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
		
		//设置参数
		Button btnSetParameter = new Button(groupLogin, SWT.NONE);
		GridData gd_btnSetParameter = new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1);
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
					
					DanbooruAPILogic logic = new DanbooruAPILogic();
					logic.SetUserInfo(strUserName, strPassword, strBasePath, nLimit);
					List<ImagePost> lstPosts = new ArrayList<ImagePost>();
					logic.RequestTaggedPosts(strTags, 1, lstPosts);
					
					//将所有的任务加入列表
					for(int i = 0; i< lstPosts.size(); i++) {
						ImageTaskBean task = new ImageTaskBean(lstPosts.get(i));
						_runDownImage.AddToTaskQueue(task);	
					}	
				}
			}
		});
		
		btnSetParameter.setText(AppConsts.MAINFORM_LOGIN);
		
		_mainShell.open();
		
		//初始化程序
		InitApp();
		
		while(!_mainShell.isDisposed()) {
			if(!_display.readAndDispatch())
				_display.sleep();
		}
		
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
}
