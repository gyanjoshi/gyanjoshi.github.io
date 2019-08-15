package com.example.projectx.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.projectx.dao.JournalDao;
import com.example.projectx.mail.EmailService;
import com.example.projectx.mail.Mail;
import com.example.projectx.model.AppUser;
import com.example.projectx.model.Article;
import com.example.projectx.model.Journal;
import com.example.projectx.repository.JournalRepository;

@Service
public class JournalService {
	
	@Value("${upload.path.journal}")
    private String path;
	
	@Value("${upload.path.coverimage}")
	private String coverpage;
	
	@Autowired
    private EmailService emailService;
	
	@Autowired
	private JournalRepository journalRepo;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	@Autowired
	JournalDao journalDao;
	
	public List<Journal> getAllJournals()
	{
		return journalDao.getAllJournals();
	}
	
	public Map<Integer,byte[]> getAllCoverImage()
	{
		Map<Integer,byte[]> coverPageMap = new HashMap<Integer,byte[]>();
		
		List<Journal> journals = journalDao.getAllJournals();
		
		for(Journal j: journals)
		{
			byte[] bytes = getCoverPage(j.getId());
			coverPageMap.put(j.getId(), bytes);
		}
		
		return coverPageMap;
	}
	
	private byte[] getCoverPage(int jid)
	{
		String fileName=journalRepo.getCoverPageFileName(jid);
		String absoultePath = coverpage + fileName;
		
		File f = new File(absoultePath);
		byte[] bytes=null;
		try {
			bytes =  Files.readAllBytes(f.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bytes;
	}
	
	public String getCoverPageLocation()
	{
		return coverpage;
		
	}
	
	public void updateCoverPage(int jid, MultipartFile file)
	{
		String fileName = file.getOriginalFilename();
		
		FileStorageService.uploadFile(coverpage, file);
		
		journalRepo.updateCoverPage(jid,fileName);
	}
	public void createJournal(String topic, String issue, String volume, String message)
	{

		// Journal Object
		Journal journal = new Journal();
		journal.setJournalTopic(topic);
		journal.setIssueNum(issue);
		journal.setVolumeNum(volume);
		journal.setStatus("created");
        
        Journal j = journalRepo.save(journal);
        
        List<AppUser> authors = userDetailsService.getAllAuthors();        
        
        
        for(AppUser user: authors)
        {
        	Mail mail = new Mail();

            
             mail.setTo(user.getEmail());
             mail.setSubject("Call for Articles for journal "+j.getJournalTopic());
             mail.setContent(message);
             
             try {
      			emailService.sendSimpleMessage(mail);
      		} catch (MessagingException e) {
      			// TODO Auto-generated catch block
      			e.printStackTrace();
      		} catch (Exception e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
        }
	}

	public void updateJournal(int jid, String topic, String issue, String volume, String year, String month)
	{
		Journal journal = journalRepo.getOne(jid);
		
		journal.setJournalTopic(topic);
		journal.setIssueNum(issue);
		journal.setVolumeNum(volume);
		journal.setYear(year);
		journal.setMonth(month);
		
		journalRepo.save(journal);
		
		
		
	}
}
