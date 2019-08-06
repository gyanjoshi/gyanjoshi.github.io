package com.example.projectx.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.projectx.model.ArticleAuthor;

public interface ArticleAuthorRepository extends JpaRepository<ArticleAuthor,Long> {

}
