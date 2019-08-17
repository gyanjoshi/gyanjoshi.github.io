package com.example.projectx.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;



@Entity
@Table(name = "Notices")
public class Notice {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer Id;
	private String  Notice_Number;
	private String NoticeTitle;
	private String NoticeFileName;
	private Boolean hasAttachment;
	private Boolean uploadedDate;
	public Boolean getUploadedDate() {
		return uploadedDate;
	}
	public void setUploadedDate(Boolean uploadedDate) {
		this.uploadedDate = uploadedDate;
	}
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public String getNotice_Number() {
		return Notice_Number;
	}
	public void setNotice_Number(String notice_Number) {
		Notice_Number = notice_Number;
	}
	public String getNoticeTitle() {
		return NoticeTitle;
	}
	public void setNoticeTitle(String noticeTitle) {
		NoticeTitle = noticeTitle;
	}
	public String getNoticeFileName() {
		return NoticeFileName;
	}
	public void setNoticeFileName(String noticeFileName) {
		NoticeFileName = noticeFileName;
	}
	public Boolean getHasAttachment() {
		return hasAttachment;
	}
	public void setHasAttachment(Boolean hasAttachment) {
		this.hasAttachment = hasAttachment;
	}
	
}
