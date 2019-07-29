package com.example.projectx.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "articletbl")
public class Article {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer articleId;
	private String articleTopic;
	private String articleFilePath;
	private String articleTag;
	private String articleStatus;

}
