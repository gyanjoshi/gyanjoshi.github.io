package com.example.projectx.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "Articles")
public class Article {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="article_seq_gen")
	@SequenceGenerator(name="article_seq_gen", sequenceName="article_seq")
	
	@Column(name="article_id")
	private Integer Id;
	@Column(name="topic")
	private String topic;
	@Column(name="status")
	private String status;
	
	@Lob
	@Column(name="abstract")	
	private String articleAbstract;
	
	@Column(name="pageCount")
	private Integer pageCount;
	
	@Column(name="tocOrder")
	private Integer tocOrder;
	
	@Column(name="authorid")
	private String authorid;// matches with username of AppUser
	
	@Column(name="filename")
	private String fileName;
	
	@Column(name="fileUploadDate")
	private Date uploadDate;
	
	@Column(name="uploadedBy")
	private String uploadedBy;
	
	@ManyToOne
    @JoinColumn
    private JournalIssue journalissue;
	
	public JournalIssue getJournal() {
		return journalissue;
	}
	public void setJournal(JournalIssue journal) {
		this.journalissue = journal;
	}
	
	public Integer getJournalIssueId()
	{
		return journalissue.getId();
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public String getArticleAbstract() {
		return articleAbstract;
	}
	public void setArticleAbstract(String articleAbstract) {
		this.articleAbstract = articleAbstract;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}
	public String getAuthorid() {
		return authorid;
	}
	public void setAuthorid(String authorid) {
		this.authorid = authorid;
	}
	public Integer getPageCount() {
		return pageCount;
	}
	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}
	public Integer getTocOrder() {
		return tocOrder;
	}
	public void setTocOrder(Integer tocOrder) {
		this.tocOrder = tocOrder;
	}
	
	
	

}
