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

//imported for download part
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class FileController {

	private static final String EXTERNAL_FILE_PATH = "D://FileStore//Downloads/";
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
	
	

	
	//start of download controller

	@RequestMapping("/download/file/{fileName:.+}")
	public void downloadPDFResource(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("fileName") String fileName) throws IOException {

		File file = new File(EXTERNAL_FILE_PATH + fileName);
		if (file.exists()) {

			//get the mimetype
			String mimeType = URLConnection.guessContentTypeFromName(file.getName());
			if (mimeType == null) {
				//unknown mimetype so set the mimetype to application/octet-stream
				mimeType = "application/octet-stream";
			}

			response.setContentType(mimeType);

			/**
			 * In a regular HTTP response, the Content-Disposition response header is a
			 * header indicating if the content is expected to be displayed inline in the
			 * browser, that is, as a Web page or as part of a Web page, or as an
			 * attachment, that is downloaded and saved locally.
			 * 
			 */

			/**
			 * Here we have mentioned it to show inline
			 */
			response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));

			 //Here we have mentioned it to show as attachment
			// response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));

			response.setContentLength((int) file.length());

			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

			FileCopyUtils.copy(inputStream, response.getOutputStream());

		}
	}

	

}
