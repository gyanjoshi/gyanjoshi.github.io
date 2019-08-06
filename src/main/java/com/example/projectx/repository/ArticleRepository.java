package com.example.projectx.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.projectx.model.Article;

public interface ArticleRepository extends JpaRepository<Article , Long> {

}
