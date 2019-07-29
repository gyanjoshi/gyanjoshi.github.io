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
}
