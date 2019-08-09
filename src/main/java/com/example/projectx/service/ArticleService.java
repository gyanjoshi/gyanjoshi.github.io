package com.example.projectx.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    private ArticleRepository articleRepo;
	
	@Autowired
    private ArticleAuthorRepository articleAuthorRepo;
	
	@Autowired
    private ArticleStorageRepository articleStoreRepo;
	
	public void saveArticle(String topic, String uploadedBy, MultipartFile file)
	{

		String fileName = file.getOriginalFilename();
		// create 3 objects to be stores in database.
        
        // Article Object
        Article article = new Article();
        article.setTopic(topic);
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
	}

}
