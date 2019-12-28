package com.example.projectx.dto;

public class ArticleDto {
	
	private Integer articleId;
	private String authorId;
	private String topic;
	private String authorName;
	private String address;
	private String city;
	private String email;
	private String status;
	
	private String articleAbstract;
	private String fileName;
	
	private Integer tocorder;
	private Integer pageCount;
	private String pageFromTo;
	
	public ArticleDto(int id, String authorid, String topic, String fullName, String address1, String city, String email, String status, String articleAbstract, String fileName) 
	
	{
		this.articleId = id;
		this.authorId = authorid;
		this.topic = topic;
		this.authorName = fullName;
		this.address = address1;
		this.city = city;
		this.email = email;
		this.status = status;
		this.articleAbstract = articleAbstract;
		this.fileName  = fileName;
	}
	
public ArticleDto(int id, String authorid, String topic, String fullName,String address1, Integer tocorder, Integer pageCount, String status,String fileName) 
	
	{
		this.articleId = id;
		this.authorId = authorid;
		this.topic = topic;
		this.authorName = fullName;
		this.address = address1;
		this.tocorder = tocorder;
		this.pageCount = pageCount;
		
		this.status = status;
		this.fileName = fileName;
	}
	
	public Integer getArticleId() {
		return articleId;
	}
	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}
	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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

	public Integer getTocorder() {
		return tocorder;
	}

	public void setTocorder(Integer tocorder) {
		this.tocorder = tocorder;
	}

	public Integer getPageCount() {
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public String getPageFromTo() {
		return pageFromTo;
	}

	public void setPageFromTo(String pageFromTo) {
		this.pageFromTo = pageFromTo;
	}
	
	
	

}
