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
	
	private static Text textUserName;
	private static Text textPass;

	public static void main(String[] args) {

		Display display = new Display();

		//主对话框
		Shell mainShell = new Shell(display);
		mainShell.setSize(800, 600);
		mainShell.setText(AppConsts.APP_NAME);
		mainShell.setLayout(new FormLayout());
		
		//登录框
		Group groupLogin = new Group(mainShell, SWT.NONE);
		FormData fd_groupLogin = new FormData();
		fd_groupLogin.top = new FormAttachment(0, 10);
		fd_groupLogin.left = new FormAttachment(0, 10);
		fd_groupLogin.bottom = new FormAttachment(0, 58);
		fd_groupLogin.right = new FormAttachment(0, 782);
		groupLogin.setLayoutData(fd_groupLogin);
		groupLogin.setText(AppConsts.MAINFORM_LOGINGROUP);
		groupLogin.setLayout(new GridLayout(5, false));
		
		//用户名密码
		Label lblName = new Label(groupLogin, SWT.NONE);
		lblName.setAlignment(SWT.RIGHT);
		GridData gd_lblName = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblName.widthHint = 52;
		lblName.setLayoutData(gd_lblName);
		lblName.setText(AppConsts.MAINFORM_USERNAME);
		
		textUserName = new Text(groupLogin, SWT.BORDER);
		GridData gd_textUserName = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_textUserName.widthHint = 142;
		textUserName.setLayoutData(gd_textUserName);
		
		Label lblPass = new Label(groupLogin, SWT.NONE);
		lblPass.setAlignment(SWT.RIGHT);
		GridData gd_lblPass = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblPass.widthHint = 49;
		lblPass.setLayoutData(gd_lblPass);
		lblPass.setText(AppConsts.MAINFORM_PASS);
		
		textPass = new Text(groupLogin, SWT.BORDER);
		GridData gd_textPass = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_textPass.widthHint = 151;
		textPass.setLayoutData(gd_textPass);
		
		//设置
		Button btnLogin = new Button(groupLogin, SWT.NONE);
		GridData gd_btnLogin = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnLogin.widthHint = 62;
		btnLogin.setLayoutData(gd_btnLogin);
		btnLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				//左键按下
				if(e.button == 1) {
					String strUserName = textUserName.getText();
					String strPassword = textPass.getText();
					
					DanbooruAPILogic logic = new DanbooruAPILogic();
					logic.SetUserInfo(strUserName, strPassword);
					List<ImagePost> lstPosts = new ArrayList<ImagePost>();
					logic.RequestTaggedPosts("megami", 1, lstPosts);
					
					//将所有的任务加入列表
					for(int i = 0; i< lstPosts.size(); i++) {
						ImageTaskBean task = new ImageTaskBean(lstPosts.get(i));
						_runDownImage.AddToTaskQueue(task);	
					}	
				}
			}
		});
		
		btnLogin.setText(AppConsts.MAINFORM_LOGIN);
		
		mainShell.open();
		
		//初始化程序
		InitApp();
		
		while(!mainShell.isDisposed()) {
			if(!display.readAndDispatch())
				display.sleep();
		}
		
		display.dispose();
	}
	
	public static void InitApp() {
		
		//建立并运行下载线程
		_runDownImage = new DownloadListWorker();
		new Thread(_runDownImage).start();
		
		//开始工作
		_runDownImage.StartQueue();
	}
}
