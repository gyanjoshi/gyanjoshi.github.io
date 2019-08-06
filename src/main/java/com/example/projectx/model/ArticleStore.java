package com.example.projectx.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "Article_Location")
public class ArticleStore {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="articlestore_seq_gen")
	@SequenceGenerator(name="articlestore_seq_gen", sequenceName="articlest_seq")
	
	private Integer Id;
	
	@Column(name="article_id")
	private Integer articleId;
	
	@Column(name="filename")
	private String fileName;
	
	@Column(name="fileUploadDate")
	private Date uploadDate;
	
	@Column(name="uploadedBy")
	private String uploadedBy;

	public Integer getArticleId() {
		return articleId;
	}

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
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
}
