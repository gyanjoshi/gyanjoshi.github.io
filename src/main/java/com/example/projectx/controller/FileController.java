package com.example.projectx.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.projectx.form.ArticleUploadForm;
import com.example.projectx.service.ArticleService;


@Controller
public class FileController {

	@Autowired
    private ArticleService articleService;


	
	@RequestMapping(value = { "/upload" }, method = RequestMethod.GET)
    public String singleFileUpload(HttpServletRequest request,Model model) {		
        
		ArticleUploadForm articleUploadForm = new ArticleUploadForm();
	    model.addAttribute("articleUploadForm", articleUploadForm);
//		request.setAttribute("articleUploadForm", articleUploadForm);
	      
        return "fileupload";
    }
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	   public String uploadOneFileHandlerPOST(HttpServletRequest request, //
	         Model model, //
	         @ModelAttribute("articleUploadForm") ArticleUploadForm articleUploadForm,
//	         final @RequestParam("file") MultipartFile file,
//	         @RequestAttribute("articleUploadForm") ArticleUploadForm articleUploadForm,
	         BindingResult errors
	         //RedirectAttributes redirectAttributes
	         ) 
	{
			
	//	System.out.println("file uploaded: "+file.getOriginalFilename());
	 
	      return this.doUpload(request, model, articleUploadForm);
	 
	   }
	
	
//	@RequestMapping(value = { "/upload" }, method = RequestMethod.POST)
//    //@PostMapping("/upload") // //new annotation since 4.3
//    public String singleFileUpload(@RequestParam("file") MultipartFile file,
////    							   @RequestParam("topic") String topic,
////    							   @RequestParam("abstract") String articleAbstract,
//                                   RedirectAttributes redirectAttributes) 
//	{
//
//        if (file.isEmpty()) 
//        {
//            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
//            return "authorhtml/authorindex";
//        } 
//        
//        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
//        String username = loggedInUser.getName();
//        
//    	//String uploadedBy="gyan";
//        String topic="test topic";
//        String articleAbstract="this is abstract";
//
//    	System.out.println("Author="+username);
//    	
//    	articleService.saveArticle(topic,articleAbstract, file, username);
//        return "index";
//    }

//    @GetMapping("/uploadStatus")
//    public String uploadStatus() {
//        return "authorhtml/authorindex";
//    }
	
	private String doUpload(HttpServletRequest request, Model model, ArticleUploadForm articleUploadForm) {
		
			String topic = articleUploadForm.getTopic();
			String abs = articleUploadForm.getArticleAbstract();
			MultipartFile file = articleUploadForm.getFileData();
			
			
			
			System.out.println("topic: " + topic);
			System.out.println("abstract: " + abs);
			
			
	      
		if (file.isEmpty()) 
	        {

				//redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
	            return "authorhtml/authorindex";
	        } 
//	        
	        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
	        String username = loggedInUser.getName();
	    	System.out.println("Author="+username);
//	    	
	    	articleService.saveArticle(topic,abs, file, username);
	        return "index";
//	    }

//	    @GetMapping("/uploadStatus")
//	    public String uploadStatus() {
//	        return "authorhtml/authorindex";
	}

	

}
