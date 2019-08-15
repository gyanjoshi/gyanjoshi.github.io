package com.example.projectx.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.projectx.model.Download;

public interface DownloadRepository extends JpaRepository<Download, Integer> {

}
