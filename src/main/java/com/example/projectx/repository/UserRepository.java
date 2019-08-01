package com.example.projectx.repository;

import java.util.List;

import com.example.projectx.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {
    User getUserByUsername(String username);

	List<User> getAllAdmins();
    
   // List<User> getAllAdmins();
}
