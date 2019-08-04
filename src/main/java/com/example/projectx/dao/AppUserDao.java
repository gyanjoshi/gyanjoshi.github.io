package com.example.projectx.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
 
import com.example.projectx.model.AppUser;
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
 
}
