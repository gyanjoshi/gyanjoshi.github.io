package com.example.projectx.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.projectx.model.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {

}
