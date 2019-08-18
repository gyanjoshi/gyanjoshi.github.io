package com.example.projectx.form;

import org.springframework.web.multipart.MultipartFile;

public class AddNoticeForm {
	
	private String NoticeNumber;
	private String NoticeTitle;
	//private MultipartFile NoticeFileName;
	private String noticeText;  //filetext in case if file is not uploaded
	
	
	public String getNoticeNumber() {
		return NoticeNumber;
	}
	public void setNoticeNumber(String noticeNumber) {
		NoticeNumber = noticeNumber;
	}
	public String getNoticeTitle() {
		return NoticeTitle;
	}
	public void setNoticeTitle(String noticeTitle) {
		NoticeTitle = noticeTitle;
	}
//	public MultipartFile getNoticeFileName() {
//		return NoticeFileName;
//	}
//	public void setNoticeFileName(MultipartFile noticeFileName) {
//		NoticeFileName = noticeFileName;
//	}
	public String getNoticeText() {
		return noticeText;
	}
	public void setNoticeText(String noticeText) {
		this.noticeText = noticeText;
	}
	

}
