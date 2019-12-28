package com.example.projectx.service;

import java.io.File;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;

import org.apache.commons.io.FilenameUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.projectx.dao.JournalDao;
import com.example.projectx.dto.JournalDropDownDto;
import com.example.projectx.dto.PreparedJournalDto;
import com.example.projectx.dto.PublishedJournalDto;
import com.example.projectx.mail.EmailService;
import com.example.projectx.mail.Mail;
import com.example.projectx.model.AppUser;
import com.example.projectx.model.Article;
import com.example.projectx.model.Journal;
import com.example.projectx.model.JournalIssue;
import com.example.projectx.repository.ArticleRepository;
import com.example.projectx.repository.JournalIssueRepository;
import com.example.projectx.repository.JournalRepository;

@Service
public class JournalService {
	
	@Value("${upload.path.journal}")
    private String path;
	
	@Value("${upload.path.coverimage}")
	private String coverpage;
	
	@Value("${upload.path.article}")
	private String articlePath;
	
	@Autowired
    private EmailService emailService;
	
	@Autowired
	private JournalIssueRepository journalIssueRepository;
	
	@Autowired
	private JournalRepository journalRepo;
	
	@Autowired
	private ArticleRepository articleRepo;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	@Autowired
	JournalDao journalDao;
	
	public List<JournalIssue> getAllJournalIssues(int jid)
	{
		return journalDao.getAllJournalIssues(jid);
	}
	
	public List<JournalIssue> getAllPublishedJournalIssues(int jid)
	{
		return journalDao.getAllPublishedJournalIssues(jid);
	}
	
	public List<JournalIssue> getDraftJournalIssues(int jid)
	{
		return journalDao.getDraftJournalIssues(jid);
	}
	
	public Map<Integer,byte[]> getAllCoverImage()
	{
		Map<Integer,byte[]> coverPageMap = new HashMap<Integer,byte[]>();
		
		List<Journal> journals = journalDao.getAllJournals();
		
		if(journals != null)
		{
			for(Journal j: journals)
			{
				byte[] bytes = getCoverPage(j.getId());
				coverPageMap.put(j.getId(), bytes);
			}
		}
		
		
		return coverPageMap;
	}
	
	
	private byte[] getCoverPage(int jid)
	{
		String fileName=journalRepo.getCoverPageFileName(jid);
		String absoultePath = coverpage + fileName;
		
		File f = new File(absoultePath);
		
		if(f.exists())
		{
			
		}
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
		Journal journal = journalRepo.getOne(jid);
		
		String coverPage = journal.getCoverImageFileName();
		
		String fileName = "Journal_Cover_"+journal.getId()+"."+FilenameUtils.getExtension(file.getOriginalFilename());
		
		FileStorageService.deleteFile(coverPage, coverPage);
		FileStorageService.uploadFile(coverpage,fileName, file);
		
		journal.setCoverImageFileName(fileName);
		
		journalRepo.save(journal);
		
	}
	public void createJournalIssue(int jid, String issue, String volume, String message)
	{

		// JournalIssue Object
		JournalIssue journalIssue = new JournalIssue();
		
		Journal journal = journalRepo.findById(jid).get();	
		
		
		journalIssue.setIssueNum(issue);
		journalIssue.setVolumeNum(volume);
		journalIssue.setStatus("created");
		
		journalIssue.setJournal(journal);
        
		journalIssueRepository.save(journalIssue);
        
        List<AppUser> authors = userDetailsService.getAllAuthors();        
        
        if(authors != null)
        {
        	for(AppUser user: authors)
            {
            	Mail mail = new Mail();

                
                 mail.setTo(user.getEmail());
                 mail.setSubject("Call for Articles for journal "+journal.getJournalTopic());
                 mail.setContent(message);
                 
                 try {
          			emailService.sendHtmlMessage(mail);
          		} catch (MessagingException e) {
          			// TODO Auto-generated catch block
          			e.printStackTrace();
          		} catch (Exception e) {
     				// TODO Auto-generated catch block
     				e.printStackTrace();
     			}
            }
        }
        
	}

	public void updateJournalIssue(int jid, String issue, String volume, String year, String month)
	{
		JournalIssue journal = journalIssueRepository.getOne(jid);		
		
		journal.setIssueNum(issue);
		journal.setVolumeNum(volume);
		journal.setYear(year);
		journal.setMonth(month);
		
		journalIssueRepository.save(journal);		
		
	}
	
	public List<JournalDropDownDto> getJournalIssueDropdown(int jid)
	{
		List<JournalIssue> journals = journalDao.getAllJournalIssues(jid);
		
		List<JournalDropDownDto> list = new ArrayList<JournalDropDownDto>();
		
		if(journals != null)
		{
			for(JournalIssue j: journals)
			{
				JournalDropDownDto jdto = new JournalDropDownDto();
				jdto.setJournalId(j.getId());
				jdto.setJournalText(j.getMonth()+"-"+j.getYear()+"(Volume# "+j.getVolumeNum()+", Issue# "+j.getIssueNum()+")");
				list.add(jdto);
			}
		}
		
		
		return list;
		
	}
	
	public List<PreparedJournalDto> getPreparedJournalIssues(int jid)
	{
		List<JournalIssue> journals = journalDao.getAllPreparedJournalIssues(jid);
		
		List<PreparedJournalDto> list = new ArrayList<PreparedJournalDto>();
		
		if(journals != null)
		{
			for(JournalIssue j: journals)
			{
				PreparedJournalDto jdto = new PreparedJournalDto();
				
				jdto.setJournalId(j.getId());
				jdto.setTitle(j.getJournal().getJournalTopic());
				jdto.setIssue(j.getIssueNum());
				jdto.setVolume(j.getVolumeNum());
				jdto.setYear(j.getYear());
				jdto.setMonth(j.getMonth());
				jdto.setFileName(j.getJournalFileName());
				list.add(jdto);
			}
		}
		
		
		return list;
		
	}
	public void addArticle(JournalIssue j, Article a)
	{
		j.addArticle(a);
	}

	public void publish(int journalId) {
		// TODO Auto-generated method stub
		JournalIssue journal = journalIssueRepository.getOne(journalId);
		
		journal.setStatus("Published");
		journal.setUploaded_date(new java.sql.Date(System.currentTimeMillis()));
		
		
		File file = new File(path+journal.getJournalFileName());
		
		Set<Article> sa  = journal.getArticles();
		
		for(Article a: sa)
		{
			a.setStatus("Published");
			AppUser user = userDetailsService.getUserByUsername(a.getAuthorid());
			
			// send email to the author
			
			Mail mail = new Mail();

            
            mail.setTo(user.getEmail());
            mail.setSubject("Article "+a.getTopic()+" is published");
            mail.setContent("Dear "+user.getFullName()+",<br>"
            		+ "Congratulations!! Your article has been published. "
            		+ " Regards,<br>"
            		+ "Editorial Borad<br>"
            		+ journal.getJournal().getJournalTopic());
            
            try {
     			emailService.sendHtmlMessage(mail,file);
     		} catch (MessagingException e) {
     			// TODO Auto-generated catch block
     			e.printStackTrace();
     		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            // save article
            articleRepo.save(a);
			
		}
		
		journalIssueRepository.save(journal);
		
		
	}


	public void prepare(int journalId, MultipartFile editorial) {
		
		JournalIssue journal = journalIssueRepository.getOne(journalId);
		
		String editorialFileName = "Editorial_"+journal.getId()+"."+FilenameUtils.getExtension(editorial.getOriginalFilename());
		
		journal.setEditorialFileName(editorialFileName);
		
		FileStorageService.uploadFile(path, editorialFileName,editorial);
		
		journal.setEditorialpageCount(FileStorageService.getPageCount(path, editorialFileName));

		journal.setStatus("Prepared");
		
		journalIssueRepository.save(journal);
		
		
	}

	public Journal getJournalById(int id) {
		// TODO Auto-generated method stub
		Journal journal = journalRepo.getOne(id);
		
		
		return journal;
	}
	
	public JournalIssue getJournalIssueById(int id) {
		// TODO Auto-generated method stub
		JournalIssue journal = journalIssueRepository.getOne(id);
		
		
		return journal;
	}

	public List<Journal> getAllJournals() {
		return journalDao.getAllJournals();
	}

	public void createJournal(String title, MultipartFile coverPage) 
	{
		Journal journal = new Journal();
		
		journal.setJournalTopic(title);
		
		Journal j = journalRepo.save(journal);
		String fileName = "Journal_Cover_"+journal.getId()+"."+FilenameUtils.getExtension(coverPage.getOriginalFilename());
		
		FileStorageService.uploadFile(coverpage,fileName, coverPage);
		
		j.setCoverImageFileName(fileName);
		
		journalRepo.save(j);
	}

	public void updateJournal(int id, String journalTopic) {
		// TODO Auto-generated method stub
		Journal journal = journalRepo.getOne(id);		
		journal.setJournalTopic(journalTopic);
				
		journalRepo.save(journal);
	}

	public byte[] getCoverImage(int id) {
		byte[] bytes = getCoverPage(id);
		return bytes;
	}

	public List<PublishedJournalDto> getAllPublishedJournals() {
		
		List<PublishedJournalDto> list = journalRepo.getPublishedJournals();

		if(list == null)
		{
			System.out.println("no published journals");
		}
		else
		{
			System.out.println("Total Published journals "+list.size());
		}
		
		return list;
	}

	public JournalIssue getCurrentJournalIssue(int jid) {
		List<JournalIssue> issues = journalDao.getAllPublishedJournalIssues(jid);
		if(issues != null)
		{
			issues.sort(Comparator.comparing(JournalIssue::getUploaded_date).reversed());
			return issues.get(0);
		}
		else
			return null;
		
		
		
	}
}
