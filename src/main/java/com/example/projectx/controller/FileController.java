package com.example.projectx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.projectx.service.ArticleService;


@Controller
public class FileController {

	@Autowired
    private ArticleService articleService;

//
//    @GetMapping("/upload")
//    public String index() {
//        return "authorhtml/authorindex";
//    }

    @PostMapping("/upload") // //new annotation since 4.3
    public String singleFileUpload(@RequestParam("file") MultipartFile file, /*@RequestParam("topic") String topic,*/
                                   RedirectAttributes redirectAttributes) {

//        if (file.isEmpty()) 
//        {
//            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
//            return "authorhtml/authorindex";
//        } 
    	String author="gyan";
    	String topic = "test topic";
    	System.out.println("Author="+author);
    	
    	articleService.saveArticle(topic, author, file);
        return "index";
    }

//    @GetMapping("/uploadStatus")
//    public String uploadStatus() {
//        return "authorhtml/authorindex";
//    }

}
