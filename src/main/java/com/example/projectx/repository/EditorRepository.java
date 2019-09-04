package com.example.projectx.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.projectx.model.Editor;

public interface EditorRepository extends JpaRepository<Editor,Integer> {

	
	
	@Query("SELECT new com.example.projectx.dto.EditorDto(email,phone, fullName) "
			+ " FROM Editor A "
			+ " WHERE email = :email")
	List<Editor> findEditorByEmail(@Param("email") String email);

}
