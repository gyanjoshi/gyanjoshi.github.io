package com.example.projectx.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.projectx.dto.PreparedJournalDto;
import com.example.projectx.model.Journal;
import com.example.projectx.model.JournalIssue;

@Repository
@Transactional
public class JournalDao {
	
	@PersistenceContext
    private EntityManager entityManager;
	
	public List<JournalIssue> getAllJournalIssues(int jid)
    {
    	
    	List<JournalIssue> journals = new ArrayList<JournalIssue>();
    	
    	javax.persistence.Query query = entityManager.createQuery("SELECT u FROM "+JournalIssue.class.getName()+" u where u.journal.Id=:jid and status not in ('Published', 'Prepared')");
    	
    	query.setParameter("jid", jid);
    	
    	
		List<?> list = query.getResultList();
		
		if(!list.isEmpty()) 
		{
			for(Object journal: list)
			{
				JournalIssue j = (JournalIssue)journal;
				journals.add(j);
			}
			return journals;
		}
    	
		else
			return null;
    	
    }
	
	public List<JournalIssue> getAllPreparedJournalIssues(int jid)
    {
    	
    	List<JournalIssue> journals = new ArrayList<JournalIssue>();
    	
    	javax.persistence.Query query = entityManager.createQuery("SELECT u FROM "+JournalIssue.class.getName()+" u where u.journal.Id=:jid and status ='Prepared'");
    	query.setParameter("jid", jid);
    	
    	List<?> list = query.getResultList();
		
		if(!list.isEmpty()) 
		{
			for(Object journal: list)
			{
				JournalIssue j = (JournalIssue)journal;
				journals.add(j);
			}
			return journals;
		}
    	
		else
			return null;
    	
    }
	
	public List<JournalIssue> getAllPublishedJournalIssues(int jid)
    {
    	
    	List<JournalIssue> journals = new ArrayList<JournalIssue>();
    	javax.persistence.Query query  = entityManager.createQuery("SELECT u FROM "+JournalIssue.class.getName()+" u where u.journal.Id=:jid and status ='Published'");
    	query.setParameter("jid", jid);
		List<?> list = query.getResultList();
		
		
		
		if(!list.isEmpty()) 
		{
			for(Object journal: list)
			{
				JournalIssue j = (JournalIssue)journal;
				journals.add(j);
			}
			return journals;
		}
    	
		else
			return null;
    	
    }

	public List<Journal> getAllJournals() {
		
		List<Journal> journals = new ArrayList<Journal>();
    	
    	javax.persistence.Query query = entityManager.createQuery("SELECT u FROM "+Journal.class.getName()+" u ");
    	
    	    	
    	
		List<?> list = query.getResultList();
		
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
