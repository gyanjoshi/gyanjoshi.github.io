package com.example.projectx.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.projectx.model.AppUser;


public interface UserRepository extends JpaRepository<AppUser , String>{

}
