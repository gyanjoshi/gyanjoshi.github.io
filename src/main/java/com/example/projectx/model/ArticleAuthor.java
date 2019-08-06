package com.example.projectx.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "Article_Author")
public class ArticleAuthor {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="articlauth_seq_gen")
	@SequenceGenerator(name="articlauth_seq_gen", sequenceName="articleauth_seq")
	
	private Integer Id;
	
	@Column(name="article_id")
	private Integer articleId;
	
	@Column(name="author_id")
	private Integer authorId;

	public Integer getArticleId() {
		return articleId;
	}

	public void setArticleId(Integer integer) {
		this.articleId = integer;
	}

	public Integer getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Integer authorId) {
		this.authorId = authorId;
	}
	
	

}
