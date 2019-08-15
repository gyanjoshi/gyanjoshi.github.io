package com.example.projectx.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.projectx.model.Journal;

public interface JournalRepository extends JpaRepository<Journal , Integer> {
	
	@Modifying(clearAutomatically = true)
	@Transactional
    @Query("UPDATE Journal SET coverImageFileName = :fileName WHERE Id = :jid")
	public void updateCoverPage(@Param("jid") int jid, @Param("fileName") String fileName);
	
	@Query("SELECT coverImageFileName FROM Journal WHERE Id = :jid")
	public String getCoverPageFileName(@Param("jid") int jid);

}
