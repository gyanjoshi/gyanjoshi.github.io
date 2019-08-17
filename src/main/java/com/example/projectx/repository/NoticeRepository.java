package com.example.projectx.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.projectx.model.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {

	
	@Modifying(clearAutomatically = true)
	@Transactional
    @Query("UPDATE Notice SET NoticeNumber =:noticenumber,NoticeTitle=:noticetitle  WHERE Id =:id")
	
	void updatenotice(@Param("id") int id, @Param("noticenumber") String noticenumber,@Param("noticetitle") String noticetitle);

	
	

	/*
	 * @Modifying(clearAutomatically = true)
	 * 
	 * @Transactional
	 * 
	 * @Query("UPDATE Download SET downloadTopic =:title, DownloadFilePath=:fileName, UploadedDate=:updatedate WHERE Id = :id"
	 * ) void editDownload(@Param("id") int id,@Param("title") String
	 * title, @Param("fileName") String fileName, @Param("updatedate") Date
	 * updatedate);
	 * 
	 * }
	 */

}
