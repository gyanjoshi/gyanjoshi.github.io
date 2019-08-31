package com.example.projectx.service;


import java.util.List;


import javax.mail.MessagingException;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import com.example.projectx.dto.ApprovedArticleDto;
import com.example.projectx.dto.ArticleDto;
import com.example.projectx.mail.EmailService;
import com.example.projectx.mail.Mail;
import com.example.projectx.model.AppUser;
import com.example.projectx.model.Article;

import com.example.projectx.model.JournalIssue;
import com.example.projectx.repository.ArticleRepository;
import com.example.projectx.repository.JournalIssueRepository;

@Service
public class ArticleService {
	
	@Value("${upload.path.article}")
    private String path;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	@Autowired
    private EmailService emailService;
	
	@Autowired
    private ArticleRepository articleRepo;
	
	@Autowired
	private JournalIssueRepository journalIssueRepository;
	
	@Autowired
	private JournalService journalService;
	
	
	public void saveArticle(String topic, String articleAbstract, MultipartFile file,String uploadedBy)
	{

		
        // Article Object
        Article article = new Article();
        article.setTopic(topic);
        article.setArticleAbstract(articleAbstract);
        article.setStatus("Submitted");
        article.setAuthorid(uploadedBy);
        article.setUploadedBy(uploadedBy);
       // article.setFileName(fileName);
        article.setUploadDate(new java.sql.Date(System.currentTimeMillis()));
        
        
        Article a = articleRepo.save(article);
        
        int id = a.getId();
        String articleFileName = "Article_"+id+"."+FilenameUtils.getExtension(file.getOriginalFilename());
        
        System.out.println("Article file="+articleFileName);
        System.out.println("Original file="+file.getOriginalFilename());
        
        FileStorageService.uploadFile(path, articleFileName, file);
        
        article.setFileName(articleFileName);
        
        articleRepo.save(article);
        
        
        List<AppUser> editors = userDetailsService.getAllEditors();
        
        AppUser author = userDetailsService.getUserByUsername(uploadedBy);
        
        for(AppUser user: editors)
        {
        	Mail mail = new Mail();

            
             mail.setTo(user.getEmail());
             mail.setSubject("Article submited: "+a.getTopic());
             mail.setContent("Hi "+user.getFullName()+","+
            		 		 author.getFullName()+" has uploaded new article titled "+a.getTopic()+
            		 		 ". Please review this article.");
             
             
             
             try {
     			emailService.sendSimpleMessage(mail,FileStorageService.multipartToFile(file,file.getOriginalFilename()));
     		} catch (MessagingException e) {
     			// TODO Auto-generated catch block
     			e.printStackTrace();
     		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
	
	public List<ArticleDto> getPendingArticles()
	{
		return articleRepo.getPendingArticles();
	}
	
	public Article getArticleById(int id)
	{
		return articleRepo.findById(id).get();
	}
	
	public void sendFeedback(int article,String authorid, String message)
	{
		Article a = articleRepo.findById(article).get();
		AppUser user = userDetailsService.getUserByUsername(authorid);
		
		
		Mail mail = new Mail();

        
        mail.setTo(user.getEmail());
        mail.setSubject("Review feedback for your article "+a.getTopic());
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
        
        a.setStatus("feedback-sent");
        
        articleRepo.save(a);
	}

	public void approve(int id, String userName, String message, int journalId, int jissueid) {
		// TODO Auto-generated method stub
		
		Article a = articleRepo.findById(id).get();
		AppUser user = userDetailsService.getUserByUsername(userName);		
		JournalIssue j = journalIssueRepository.findById(jissueid).get();		
		
		Mail mail = new Mail();

        
        mail.setTo(user.getEmail());
        mail.setSubject("Your article "+a.getTopic()+" is approved");
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
        
        a.setStatus("Approved");
        a.setJournal(j);
        
        journalService.addArticle(j, a);        
        
        articleRepo.save(a);
        journalIssueRepository.saveAndFlush(j);
		
	}


	
	public List<ArticleDto> getApprovedArticles(int journalId)
	{
		return articleRepo.getApprovedArticles(journalId);
	}

	public void updateArticle(int id, String topic, String articleAbstract, MultipartFile file,String uploadedBy)
	{

		Article article = getArticleById(id);
		String afilename = article.getFileName();
		
		String articleFileName = "Article_"+id+"."+FilenameUtils.getExtension(file.getOriginalFilename());
        
        article.setTopic(topic);
        article.setArticleAbstract(articleAbstract);
        article.setStatus("Re-submitted");
        article.setAuthorid(uploadedBy);
        article.setUploadedBy(uploadedBy);
        article.setFileName(articleFileName);

        article.setUploadDate(new java.sql.Date(System.currentTimeMillis()));
        
        FileStorageService.deleteFile(path, afilename);
        
        FileStorageService.uploadFile(path, articleFileName, file);
        
        
        
        articleRepo.save(article);
        
        
        List<AppUser> editors = userDetailsService.getAllEditors();
        
        AppUser author = userDetailsService.getUserByUsername(uploadedBy);
        
        for(AppUser user: editors)
        {
        	Mail mail = new Mail();

            
             mail.setTo(user.getEmail());
             mail.setSubject("Article re-submited: "+topic);
             mail.setContent("Hi "+user.getFullName()+","+
            		 		 author.getFullName()+" has re-submitted new article titled "+topic+
            		 		 ". Please review this article.");
             
             
             
             try {
     			emailService.sendSimpleMessage(mail,FileStorageService.multipartToFile(file,file.getOriginalFilename()));
     		} catch (MessagingException e) {
     			// TODO Auto-generated catch block
     			e.printStackTrace();
     		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}

	public List<ArticleDto> getPendingArticles(String username) {
		// TODO Auto-generated method stub
		return articleRepo.getPendingArticles(username);
	}

	public void deleteArticle(int id) {
		// TODO Auto-generated method stub
		
		Article article = getArticleById(id);
		
		String fileName = article.getFileName();
		
		FileStorageService.deleteFile(path, fileName);
		
		articleRepo.deleteById(id);
		
		
	}

	public List<ApprovedArticleDto> getApprovedArticles() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
