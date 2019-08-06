package com.example.projectx.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.projectx.exception.StorageException;
import com.example.projectx.model.Article;
import com.example.projectx.model.ArticleAuthor;
import com.example.projectx.model.ArticleStore;
import com.example.projectx.repository.ArticleAuthorRepository;
import com.example.projectx.repository.ArticleRepository;
import com.example.projectx.repository.ArticleStorageRepository;

@Service
public class FileStorageService {
	
	@Value("${upload.path}")
    private String path;
	
	@Autowired
    private ArticleRepository articleRepo;
	
	@Autowired
    private ArticleAuthorRepository articleAuthorRepo;
	
	@Autowired
    private ArticleStorageRepository articleStoreRepo;

    public void uploadFile(String topic, String author, MultipartFile file) 
    {
    	
    	String fileName=null;

        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file");
        }

        try {
            fileName = file.getOriginalFilename();
            InputStream is = file.getInputStream();

            Files.copy(is, Paths.get(path + fileName),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {

            String msg = String.format("Failed to store file", file.getName());

            throw new StorageException(msg, e);
        }
        
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
        as.setUploadedBy(author);
        as.setFileName(fileName);
        
        articleStoreRepo.save(as);

    }

}
