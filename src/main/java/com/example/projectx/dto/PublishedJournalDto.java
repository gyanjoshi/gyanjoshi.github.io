package com.example.projectx.dto;

import java.util.Date;

public class PublishedJournalDto {
	
	private Integer journalId;
	private Integer journalIssueId;
	private String JournalTopic;
	private String coverImageFileName;
	private String VolumeNum;
	private String IssueNum;
	private String year;
	private String month;
	private String editorialFileName;
	private String journalFileName;	
	private Date uploaded_date;
	private String uploaded_by;
	private String status;
	
//	Caused by: org.hibernate.hql.internal.ast.QuerySyntaxException: Unable to locate appropriate constructor on class 
//	[com.example.projectx.dto.PublishedJournalDto]. 
//	Expected arguments are: int, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, 
	//java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Date, java.lang.String, java.lang.String [SELECT new com.example.projectx.dto.PublishedJournalDto(b.Id, a.Id, b.JournalTopic, 
//			b.coverImageFileName, a.VolumeNum, a.IssueNum, a.year, a.month, 
//			a.editorialFileName, a.journalFileName, a.uploaded_date, a.uploaded_by, a.status) 
//			FROM com.example.projectx.model.JournalIssue a 
//			LEFT JOIN com.example.projectx.model.Journal b 
//			ON a.journal.Id = b.Id 
//			WHERE a.status ='Published']
	
	public PublishedJournalDto()
	{
		
	}
	public PublishedJournalDto(int journalId, int journalIssueId, String journalTopic,
			String coverImageFileName, String volumeNum, String issueNum, String year, String month,
			String editorialFileName, String journalFileName, java.util.Date uploaded_date, String uploaded_by, String status) {

		this.journalId = journalId;
		this.journalIssueId = journalIssueId;
		JournalTopic = journalTopic;
		this.coverImageFileName = coverImageFileName;
		VolumeNum = volumeNum;
		IssueNum = issueNum;
		this.year = year;
		this.month = month;
		this.editorialFileName = editorialFileName;
		this.journalFileName = journalFileName;
		this.uploaded_date = uploaded_date;
		this.uploaded_by = uploaded_by;
		this.status = status;
	}
	public Integer getJournalId() {
		return journalId;
	}
	public void setJournalId(Integer journalId) {
		this.journalId = journalId;
	}
	public Integer getJournalIssueId() {
		return journalIssueId;
	}
	public void setJournalIssueId(Integer journalIssueId) {
		this.journalIssueId = journalIssueId;
	}
	public String getJournalTopic() {
		return JournalTopic;
	}
	public void setJournalTopic(String journalTopic) {
		JournalTopic = journalTopic;
	}
	public String getCoverImageFileName() {
		return coverImageFileName;
	}
	public void setCoverImageFileName(String coverImageFileName) {
		this.coverImageFileName = coverImageFileName;
	}
	public String getVolumeNum() {
		return VolumeNum;
	}
	public void setVolumeNum(String volumeNum) {
		VolumeNum = volumeNum;
	}
	public String getIssueNum() {
		return IssueNum;
	}
	public void setIssueNum(String issueNum) {
		IssueNum = issueNum;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getEditorialFileName() {
		return editorialFileName;
	}
	public void setEditorialFileName(String editorialFileName) {
		this.editorialFileName = editorialFileName;
	}
	public String getJournalFileName() {
		return journalFileName;
	}
	public void setJournalFileName(String journalFileName) {
		this.journalFileName = journalFileName;
	}
	public Date getUploaded_date() {
		return uploaded_date;
	}
	public void setUploaded_date(Date uploaded_date) {
		this.uploaded_date = uploaded_date;
	}
	public String getUploaded_by() {
		return uploaded_by;
	}
	public void setUploaded_by(String uploaded_by) {
		this.uploaded_by = uploaded_by;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
