package com.example.projectx.model;

import java.sql.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "Journal_Issues")

public class JournalIssue {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="journal_seq_gen")
	@SequenceGenerator(name="journal_seq_gen", sequenceName="journal_seq")
	
	@Column(name="id")
	private Integer Id;
	
	@Column(name="volume")
	private String VolumeNum;
	@Column(name="issue")
	private String IssueNum;
	@Column(name="year")
	private String year;
	@Column(name="month")
	private String month;
	
	@Column(name="editorialfile")
	private String editorialFileName;
	
	@Column(name="journalfile")
	private String journalFileName;
	
	
	@Column(name="publishdate")
	private Date uploaded_date;
	@Column(name="uploadedby")
	private String uploaded_by;
	
	@Column(name="status")
	private String status;
	
	@ManyToOne
    @JoinColumn
    private Journal journal;
	
	@OneToMany(mappedBy = "journalissue", cascade = CascadeType.ALL)
    private Set<Article> articles;
	
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
	public Set<Article> getArticles() {
		return articles;
	}
	public void setArticles(Set<Article> articles) {
		this.articles = articles;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public void addArticle(Article a)
	{
		articles.add(a);
	}
	public String getEditorialFileName() {
		return editorialFileName;
	}
	public void setEditorialFileName(String editorialFileName) {
		this.editorialFileName = editorialFileName;
	}
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public Journal getJournal() {
		return journal;
	}
	public void setJournal(Journal journal) {
		this.journal = journal;
	}
	
	

}
