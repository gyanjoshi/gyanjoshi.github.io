package com.example.projectx.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import com.example.projectx.model.Journal;

@Repository
@Transactional
public class JournalDao {
	
	@PersistenceContext
    private EntityManager entityManager;
	
	public List<Journal> getAllJournals()
    {
    	
    	List<Journal> journals = new ArrayList<Journal>();
    	
		List<?> list = entityManager.createQuery("SELECT u FROM "+Journal.class.getName()+" u ").getResultList();
		
		if(!list.isEmpty()) 
		{
			for(Object journal: list)
			{
				Journal j = (Journal)journal;
				journals.add(j);
			}
			return journals;
		}
    	
		else
			return null;
    	
    }	

}
