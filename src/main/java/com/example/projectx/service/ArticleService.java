package com.example.projectx.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.projectx.mail.EmailService;
import com.example.projectx.mail.Mail;
import com.example.projectx.model.AppUser;
import com.example.projectx.model.Article;

import com.example.projectx.repository.ArticleRepository;


@Service
public class ArticleService {
	
	@Value("${upload.path}")
    private String path;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	@Autowired
    private EmailService emailService;
	
	@Autowired
	private FileStorageService fileService;
	
	@Autowired
    private ArticleRepository articleRepo;
	
	
	public void saveArticle(String topic, String articleAbstract, MultipartFile file,String uploadedBy)
	{

		String fileName = file.getOriginalFilename();
		
		fileService.uploadFile(path, file);
		
        // Article Object
        Article article = new Article();
        article.setTopic(topic);
        article.setArticleAbstract(articleAbstract);
        article.setStatus("Submitted");
        article.setAuthorid(uploadedBy);
        article.setUploadedBy(uploadedBy);
        article.setFileName(fileName);
        article.setUploadDate(new java.sql.Date(System.currentTimeMillis()));
        
        
        Article a = articleRepo.save(article);
        
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
     			emailService.sendSimpleMessage(mail,multipartToFile(file,file.getOriginalFilename()));
     		} catch (MessagingException e) {
     			// TODO Auto-generated catch block
     			e.printStackTrace();
     		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
	
	public  static File multipartToFile(MultipartFile multipart, String fileName) throws IllegalStateException, IOException {
	    File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+fileName);
	    multipart.transferTo(convFile);
	    return convFile;
	}
	

}
