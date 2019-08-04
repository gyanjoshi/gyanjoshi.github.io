package com.example.projectx.service;


import java.util.Arrays;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;


import com.example.projectx.dao.AppUserDao;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
 
    @Autowired
    private AppUserDao userDao;
 
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		com.example.projectx.model.AppUser user = userDao.getActiveUser(userName);
		
		if(user == null){
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		
		System.out.println("Role="+user.getRole());
		GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());
		UserDetails userDetails = (UserDetails)new User(user.getUserName(),
				user.getPassword(), Arrays.asList(authority));
		return userDetails;		
		
	} 
}
