package com.example.projectx.service;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.projectx.mail.EmailService;
import com.example.projectx.mail.Mail;
import com.example.projectx.model.Article;
import com.example.projectx.model.ArticleAuthor;
import com.example.projectx.model.ArticleStore;
import com.example.projectx.repository.ArticleAuthorRepository;
import com.example.projectx.repository.ArticleRepository;
import com.example.projectx.repository.ArticleStorageRepository;

@Service
public class ArticleService {
	
	@Value("${upload.path}")
    private String path;
	
	@Autowired
    private EmailService emailService;
	
	@Autowired
	private FileStorageService fileService;
	
	@Autowired
    private ArticleRepository articleRepo;
	
	@Autowired
    private ArticleAuthorRepository articleAuthorRepo;
	
	@Autowired
    private ArticleStorageRepository articleStoreRepo;
	
	public void saveArticle(String topic, String articleAbstract, MultipartFile file,String uploadedBy)
	{

		String fileName = file.getOriginalFilename();
		
		fileService.uploadFile(path, file);
		// create 3 objects to be stores in database.
        
        // Article Object
        Article article = new Article();
        article.setTopic(topic);
        article.setArticleAbstract(articleAbstract);
        article.setStatus("Submitted");
        
        Article a = articleRepo.save(article);
        
        //Article Author Object
        ArticleAuthor aa = new ArticleAuthor();
        
        aa.setArticleId(a.getId());
        aa.setAuthorId(1);
        
        articleAuthorRepo.save(aa);
        
        //Article Storage Object
        
        ArticleStore as = new ArticleStore();
        
        as.setArticleId(a.getId());
        as.setUploadedBy(uploadedBy);
        as.setFileName(fileName);
        
        articleStoreRepo.save(as);
        
        // After successful upload, send email to editor.
        
//        Mail mail = new Mail();
//
//        mail.setSubject("Sending Email Attachment Configuration Example");
//        mail.setContent("This tutorial demonstrates how to send an email with attachment using Spring Framework.");
//
//        try {
//			emailService.sendSimpleMessage(mail);
//		} catch (MessagingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
	}
	

}
