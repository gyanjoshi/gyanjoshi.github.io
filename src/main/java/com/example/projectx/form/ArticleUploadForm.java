package com.example.projectx.form;

import org.springframework.web.multipart.MultipartFile;

public class ArticleUploadForm {
	
	private String topic;
	private String articleAbstract;   
    private MultipartFile fileData;
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getArticleAbstract() {
		return articleAbstract;
	}
	public void setArticleAbstract(String articleAbstract) {
		this.articleAbstract = articleAbstract;
	}
	public MultipartFile getFileData() {
		return fileData;
	}
	public void setFileData(MultipartFile file) {
		this.fileData = file;
	}
}
