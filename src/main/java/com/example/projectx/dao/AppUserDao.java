package com.example.projectx.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.example.projectx.model.AppRole;
import com.example.projectx.model.AppUser;
import com.example.projectx.model.Qualification;
import com.example.projectx.model.Title;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
 
@Repository
@Transactional
public class AppUserDao {
 
	@PersistenceContext
    private EntityManager entityManager;
 
	public AppUser getActiveUser(String userName) {
		AppUser activeUserInfo = new AppUser();
		short enabled = 1;
		List<?> list = entityManager.createQuery("SELECT u FROM "+AppUser.class.getName()+" u WHERE userName=:userName and enabled=:enabled")
				.setParameter("userName", userName).setParameter("enabled", enabled).getResultList();
		if(!list.isEmpty()) {
			activeUserInfo = (AppUser)list.get(0);
			return activeUserInfo;
		}
		else return null;
		
	}
	
    public AppUser findUserAccount(String userName) {
        try {
            String sql = "Select e from " + AppUser.class.getName() + " e " //
                    + " Where e.userName = :userName ";
 
            Query query = entityManager.createQuery(sql, AppUser.class);
            query.setParameter("userName", userName);
 
            return (AppUser) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public List<AppUser> getAllEditors()
    {
    	
    	short enabled = 1;
    	String role="ROLE_EDITOR";
    	
    	List<AppUser> users = new ArrayList<AppUser>();
    	
		List<?> list = entityManager.createQuery("SELECT u FROM "+AppUser.class.getName()+" u WHERE enabled=:enabled AND role=:role")
				.setParameter("role", role).setParameter("enabled", enabled).getResultList();
		
		if(!list.isEmpty()) 
		{
			for(Object user: list)
			{
				AppUser au = (AppUser)user;
				users.add(au);
			}
			return users;
		}
    	
		else
			return null;
    	
    }

	public List<AppUser> getAllAuthors() {
		short enabled = 1;
    	String role="ROLE_AUTHOR";
    	
    	List<AppUser> users = new ArrayList<AppUser>();
    	
		List<?> list = entityManager.createQuery("SELECT u FROM "+AppUser.class.getName()+" u WHERE enabled=:enabled AND role=:role ")
				.setParameter("role", role).setParameter("enabled", enabled).getResultList();
		
		if(!list.isEmpty()) 
		{
			for(Object user: list)
			{
				AppUser au = (AppUser)user;
				users.add(au);
			}
			return users;
		}
    	
		else
			return null;
	}
    
	public List<AppRole> getAllRoles()
	{
		List<AppRole> roles = new ArrayList<AppRole>();

			String sql = "select r from "+AppRole.class.getName()+" r";
			Query query = entityManager.createQuery(sql, AppRole.class);            
 
            List<?> list =  query.getResultList();
            if(!list.isEmpty()) 
    		{
    			for(Object r: list)
    			{
    				AppRole ar = (AppRole)r;
    				roles.add(ar);
    			}
    			
    			return roles;
    		}
        	
    		else
    			return null;
	}
	public List<Title> getAllTitles()
	{
		List<Title> titles = new ArrayList<Title>();

			String sql = "select r from "+Title.class.getName()+" r";
			Query query = entityManager.createQuery(sql, Title.class);            
 
            List<?> list =  query.getResultList();
            if(!list.isEmpty()) 
    		{
    			for(Object r: list)
    			{
    				Title ar = (Title)r;
    				titles.add(ar);
    			}
    			
    			return titles;
    		}
        	
    		else
    			return null;
	}
	public List<Qualification> getAllQualifications()
	{
		List<Qualification> qualifications = new ArrayList<Qualification>();

			String sql = "select r from "+Qualification.class.getName()+" r";
			Query query = entityManager.createQuery(sql, Qualification.class);            
 
            List<?> list =  query.getResultList();
            if(!list.isEmpty()) 
    		{
    			for(Object r: list)
    			{
    				Qualification ar = (Qualification)r;
    				qualifications.add(ar);
    			}
    			
    			return qualifications;
    		}
        	
    		else
    			return null;
	}

	public List<AppUser> getAllUsers() {
		
		List<AppUser> users = new ArrayList<AppUser>();
    	
		List<?> list = entityManager.createQuery("SELECT u FROM "+AppUser.class.getName()+" u ").getResultList();
		
		if(!list.isEmpty()) 
		{
			for(Object user: list)
			{
				AppUser au = (AppUser)user;
				users.add(au);
			}
			return users;
		}
    	
		else
			return null;
	}

	
}
