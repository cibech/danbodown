package com.cibech.danbd.beans;

import com.cibech.danbd.logic.DanbooruAPILogic;

public class ImageTaskBean {

	private static int taskcount = 0;
	
	private int taskid;
	private String image_url;
	private int image_size;
	private int width;
	private int height;
	private String image_filename;
	private String image_fullpath;
	private String file_md5;
	
	public static void ResetTask() {
		taskcount = 0;
	}
	
	public ImageTaskBean(ImagePost post) {
		
		image_url = post.getFile_url();
		image_size = post.getFile_size();
		
		width = post.getWidth();
		height = post.getHeight();
		
		image_filename = image_url.substring(image_url.lastIndexOf('/') + 1);
		image_fullpath = DanbooruAPILogic.GetBasePath() + image_filename;
		file_md5 = post.getMd5();
	}
	
	public String getImage_url() {
		return image_url;
	}
	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
	public int getImage_size() {
		return image_size;
	}
	public void setImage_size(int image_size) {
		this.image_size = image_size;
	}
	public String getImage_filename() {
		return image_filename;
	}
	public void setImage_filename(String image_filename) {
		this.image_filename = image_filename;
	}
	public String getImage_fullpath() {
		return image_fullpath;
	}
	public void setImage_fullpath(String image_fullpath) {
		this.image_fullpath = image_fullpath;
	}
	public String getFile_md5() {
		return file_md5;
	}
	public void setFile_md5(String file_md5) {
		this.file_md5 = file_md5;
	}

	public int getTaskid() {
		return taskid;
	}

	public void setTaskid(int taskid) {
		this.taskid = taskid;
	}
	
	public void GenTaskId() {
		taskid = taskcount++;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
