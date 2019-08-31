package com.example.projectx.repository;

import java.awt.print.Pageable;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.projectx.model.Download;

public interface Top2DownloadsRepository extends CrudRepository<Download, Integer> {
	List<Download> findAllByUploadedDate(Integer id, org.springframework.data.domain.Pageable secondPageWithFiveElements);
	
	List<Download> findFirst10ById();

}
