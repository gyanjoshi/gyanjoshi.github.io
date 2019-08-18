package com.example.projectx.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.projectx.dao.JournalDao;
import com.example.projectx.dto.JournalDropDownDto;
import com.example.projectx.dto.PreparedJournalDto;
import com.example.projectx.mail.EmailService;
import com.example.projectx.mail.Mail;
import com.example.projectx.model.AppUser;
import com.example.projectx.model.Article;
import com.example.projectx.model.Journal;
import com.example.projectx.repository.ArticleRepository;
import com.example.projectx.repository.JournalRepository;
import com.example.projectx.utils.PdfMerger;

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
	private JournalRepository journalRepo;
	
	@Autowired
	private ArticleRepository articleRepo;
	
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
		
		String fileName = "Journal_Cover_"+journal.getId()+FilenameUtils.getExtension(file.getOriginalFilename());
		
		FileStorageService.uploadFile(coverpage,fileName, file);		
		
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
	
	public List<JournalDropDownDto> getJournalsDropdown()
	{
		List<Journal> journals = journalDao.getAllJournals();
		
		List<JournalDropDownDto> list = new ArrayList<JournalDropDownDto>();
		
		if(journals != null)
		{
			for(Journal j: journals)
			{
				JournalDropDownDto jdto = new JournalDropDownDto();
				jdto.setJournalId(j.getId());
				jdto.setJournalText("Journal-(year-"+j.getYear()+", month-"+j.getMonth()+") (Volume# "+j.getVolumeNum()+", Issue# "+j.getIssueNum()+")");
				list.add(jdto);
			}
		}
		
		
		return list;
		
	}
	
	public List<PreparedJournalDto> getPreparedJournals()
	{
		List<Journal> journals = journalDao.getAllPreparedJournals();
		
		List<PreparedJournalDto> list = new ArrayList<PreparedJournalDto>();
		
		if(journals != null)
		{
			for(Journal j: journals)
			{
				PreparedJournalDto jdto = new PreparedJournalDto();
				
				jdto.setJournalId(j.getId());
				jdto.setTitle(j.getJournalTopic());
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
	public void addArticle(Journal j, Article a)
	{
		j.addArticle(a);
	}

	public void publish(int journalId) {
		// TODO Auto-generated method stub
		Journal journal = journalRepo.getOne(journalId);
		
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
            mail.setContent("Your article has been published");
            
            try {
     			emailService.sendSimpleMessage(mail,file);
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
		
		journalRepo.save(journal);
		
		
	}


	public File prepare(int journalId, MultipartFile editorial) throws IOException {
		// TODO Auto-generated method stub
		Journal journal = journalRepo.getOne(journalId);
		
		String editorialFileName = "Editorial_"+journal.getId()+FilenameUtils.getExtension(editorial.getOriginalFilename());
		String journalFileName = "Journal_"+journal.getId()+FilenameUtils.getExtension(editorial.getOriginalFilename());
		
		journal.setEditorialFileName(editorialFileName);
		
		journal.setJournalFileName(editorialFileName);
		//journal.set
		journal.setStatus("Prepared");
		
		FileStorageService.uploadFile(path, editorialFileName,editorial);
		
		//FileStorageService.uploadFile(path, journalfile);
		
		// prepare for merging pdf files
		List<File> files = new ArrayList<File>();
		
		String coverFile=journalRepo.getCoverPageFileName(journalId);
		
		File coverPage = new File(coverpage+coverFile);
		
		files.add(coverPage);
		
		
		File editorialPage = new File(path+editorialFileName);
		
		files.add(editorialPage);		
		
		Set<Article> sa  = journal.getArticles();
		
	
		
		for(Article a: sa)
		{
			a.setStatus("Prepared");
			File articleFile = new File(articlePath+a.getFileName());			
			files.add(articleFile);
			
			articleRepo.save(a);
			
		}
		
		File f = PdfMerger.mergePdfs(files, path+journalFileName);
		
		journalRepo.save(journal);
		
		return f;
	}
}
