package com.example.projectx.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "journaltbl")
public class Journal {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer Id;
	private String JournalTopic;
	private Integer VolumeNum;
	private Integer IssueNum;
	private String coverImgPath;
	private String JornalFilePath;
	private Date uploaded_date;
	private String uploaded_by;
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public String getJournalTopic() {
		return JournalTopic;
	}
	public void setJournalTopic(String journalTopic) {
		JournalTopic = journalTopic;
	}
	public Integer getVolumeNum() {
		return VolumeNum;
	}
	public void setVolumeNum(Integer volumeNum) {
		VolumeNum = volumeNum;
	}
	public Integer getIssueNum() {
		return IssueNum;
	}
	public void setIssueNum(Integer issueNum) {
		IssueNum = issueNum;
	}
	public String getCoverImgPath() {
		return coverImgPath;
	}
	public void setCoverImgPath(String coverImgPath) {
		this.coverImgPath = coverImgPath;
	}
	public String getJornalFilePath() {
		return JornalFilePath;
	}
	public void setJornalFilePath(String jornalFilePath) {
		JornalFilePath = jornalFilePath;
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
	
}
