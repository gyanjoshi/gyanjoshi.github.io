package com.example.projectx.model;

import java.io.File;
import java.sql.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "Journals")
public class Journal {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="journal_seq_gen")
	@SequenceGenerator(name="journal_seq_gen", sequenceName="journal_seq")
	
	@Column(name="journal_id")
	private Integer Id;
	@Column(name="journal_title")
	private String JournalTopic;
	
	@Column(name="coverimagefile")
	private String coverImageFileName;
	
	@OneToMany(mappedBy = "journal", cascade = CascadeType.ALL)
    private Set<JournalIssue> issues;
	
	//private File coverPage;
	
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
	
	public String getCoverImageFileName() {
		return coverImageFileName;
	}
	public void setCoverImageFileName(String coverImageFileName) {
		this.coverImageFileName = coverImageFileName;
	}	
}
