package com.example.projectx.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.projectx.model.ArticleStore;

public interface ArticleStorageRepository extends JpaRepository<ArticleStore,Long>{

}
