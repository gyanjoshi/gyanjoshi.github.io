package com.example.projectx.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.projectx.dto.ArticleDto;
import com.example.projectx.model.Article;

public interface ArticleRepository extends JpaRepository<Article , Integer> {

	@Query("SELECT new com.example.projectx.dto.ArticleDto(a.Id, a.authorid, a.topic, b.fullName, b.address1, b.city, b.email, a.status, a.articleAbstract, a.fileName) \r\n" + 
			"FROM Article a\r\n" + 
			"left join AppUser b\r\n" + 
			"on a.authorid=b.userName\r\n" + 
			"where a.status in ('Submitted','resubmitted','feedback-sent','Sent to Reviewer','Updated to PDF')")
	public List<ArticleDto> getPendingArticles();
	
	@Query("SELECT new com.example.projectx.dto.ArticleDto(a.Id, a.authorid, a.topic, b.fullName, b.address1, b.city, b.email, a.status, a.articleAbstract, a.fileName) \r\n" + 
			"FROM Article a\r\n" + 
			"left join AppUser b\r\n" + 
			"on a.authorid=b.userName\r\n" + 
			"where a.status='Approved'")
	
	public List<ArticleDto> getApprovedArticles(int journalId);
	
	@Query("SELECT new com.example.projectx.dto.ArticleDto(a.Id, a.authorid, a.topic, b.fullName, b.address1, b.city, b.email, a.status, a.articleAbstract, a.fileName) \r\n" + 
			"FROM Article a\r\n" + 
			"left join AppUser b\r\n" + 
			"on a.authorid=b.userName\r\n" + 
			"where a.status in ('Submitted','resubmitted','feedback-sent','Re-submitted')"
			+ "AND a.authorid=:username")
	public List<ArticleDto> getPendingArticles(@Param("username") String username);
}

;