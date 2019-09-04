package com.example.projectx.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.projectx.form.ArticleUploadForm;

import com.example.projectx.model.AppUser;
import com.example.projectx.model.Article;
import com.example.projectx.service.ArticleService;
import com.example.projectx.service.UserDetailsServiceImpl;
import com.example.projectx.utils.WebUtils;

@Controller
public class AuthorController {
	
	@Autowired
	private UserDetailsServiceImpl userService;
	
	@Autowired
    private ArticleService articleService;
	
	@RequestMapping(path = "/author", method = RequestMethod.GET)
	public String author(Model model, Principal principal) {
		
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
 
        
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);
       // model.addAttribute("profiles", userService.getAllProfilePictures());
        model.addAttribute("currentProfile", userService.getAllProfilePictures().get(loginedUser.getUsername()));
        
		return "author/authorpage";
	}
	
	@RequestMapping(path = "/author/newarticle", method = RequestMethod.GET)
	public String newArticle(Model model, Principal principal) {
		
		ArticleUploadForm articleUploadForm = new ArticleUploadForm();
	    model.addAttribute("articleUploadForm", articleUploadForm);
	    
	    User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userService.getAllProfilePictures().get(loginedUser.getUsername()));
	    
		return "/author/newarticle";
	}
	
	@RequestMapping(value = "/author/newarticle", method = RequestMethod.POST)
	public String createNewArticle(HttpServletRequest request, //
	         Model model, //
	         @ModelAttribute("articleUploadForm") ArticleUploadForm articleUploadForm,
	         BindingResult errors,
	         RedirectAttributes redirectAttributes) 
	{
		String topic = articleUploadForm.getTopic();
		String abs = articleUploadForm.getArticleAbstract();
		MultipartFile file = articleUploadForm.getFileData();

		String message = null;
		
		if (file.isEmpty()) 
        {

			message =  "Please select a file to upload";
			model.addAttribute("message",message);
			
			return "/author/newarticle";
            
        }
		else if(!file.getOriginalFilename().endsWith(".doc") && !file.getOriginalFilename().endsWith(".docx"))
		{
			message =  "Please select a Microsoft word (.doc or .docx) file to upload";
			model.addAttribute("message",message);
			System.out.println(message);
			return "/author/newarticle";
		}
		else
		{
			Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
	        String username = loggedInUser.getName();
	    	System.out.println("Author="+username);  
	    	
	    	articleService.saveArticle(topic,abs, file, username);
	    	
	    	message = "Article uploaded successfully.";
	    	
	    	model.addAttribute("currentProfile", userService.getAllProfilePictures().get(username));
	    	model.addAttribute("message",message);
	    	
	    	model.addAttribute("pending", articleService.getPendingArticles(username));
		}
		
       
        return "/author/pending";
	 
	}
	
	@RequestMapping(path = "/author/pending", method = RequestMethod.GET)
	public String pendingReview(Model model, Principal principal) {
		
		
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userService.getAllProfilePictures().get(loginedUser.getUsername()));
		
		model.addAttribute("pending", articleService.getPendingArticles(loginedUser.getUsername()));
	    
		return "/author/pending";
	}
	
	@RequestMapping(value = "/author/delete-article", method = RequestMethod.GET)
    public String deleteArticle(@RequestParam int article,Model model, Principal principal) {
    	//userrepo.deleteById(article);
		// also delete file from file system
		
		articleService.deleteArticle(article);
		
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userService.getAllProfilePictures().get(loginedUser.getUsername()));
		
		model.addAttribute("pending", articleService.getPendingArticles(loginedUser.getUsername()));
		
        return "/author/pending";
    }
	
	@RequestMapping(value = "/author/update-article", method = RequestMethod.GET)
    public String updateArticle(@RequestParam("article") int id,Model model, Principal principal) {
    	//userrepo.deleteById(article);
		// also delete file from file system
		
		//articleService.updateArticle(article);
		
		Article a = articleService.getArticleById(id);
		
		
	    model.addAttribute("articleUploadForm", a);
	  //  model.addAttribute("id", id);
	    
	    User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userService.getAllProfilePictures().get(loginedUser.getUsername()));
		
        return "/author/update-article";
    }
	
	@RequestMapping(value = "/author/update-article", method = RequestMethod.POST)
    public String updateArticlePost(@RequestParam("Id") int article,@RequestParam MultipartFile file, Model model, Principal principal) {
    	//userrepo.deleteById(article);
		// also delete file from file system
		
		//articleService.updateArticle(article);
		
		Article a = articleService.getArticleById(article);
		
		String topic = a.getTopic();
		String abs = a.getArticleAbstract();
	

		String message = null;
		
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userService.getAllProfilePictures().get(loginedUser.getUsername()));
		
		
		if (file.isEmpty()) 
        {

			message =  "Please select a file to upload";
			
            
        }
		else
		{
			articleService.updateArticle(article, topic, abs, file, loginedUser.getUsername());
			message =  "Article updated successfully!";
		}

	    
	    model.addAttribute("message", message);
		
        return "/author/pending";
    }
	
	@RequestMapping(value = "/author/editprofile", method = RequestMethod.GET)
    public String editAuthor(Model model, Principal principal) {
		
		User loginedUser = (User) ((Authentication) principal).getPrincipal();	
    	
    	
    	AppUser user = userService.getUserByUsername(loginedUser.getUsername());
    	
		model.addAttribute("author", user);
		model.addAttribute("profiles", userService.getAllProfilePictures());
		model.addAttribute("roles", userService.getAllRoles());
    	model.addAttribute("titles", userService.getAllTitles());
    	model.addAttribute("qualifications", userService.getAllQualifications());
    	
		return "/author/editprofile";
    }
    
    @RequestMapping(value = "/author/editprofile", method = RequestMethod.POST)
    public String editAuthorPost(@ModelAttribute AppUser author, Model model, Principal principal) {

    	User loginedUser = (User) ((Authentication) principal).getPrincipal();	    	
    	userService.editUser(loginedUser.getUsername(), author, "ROLE_AUTHOR");
    	model.addAttribute("profiles", userService.getAllProfilePictures());
        return "redirect:/author";
    }
    
    @RequestMapping(path = "/author/changeprofilepicture", method = RequestMethod.GET)
	public String changeProfilePicture(Model model, Principal principal) {
		
		
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userService.getAllProfilePictures().get(loginedUser.getUsername()));

		return "/author/changeprofile";
	}
	
	@RequestMapping(path = "/author/changeprofile", method = RequestMethod.POST)
	public String changeProfilePicturePost(MultipartFile file, Model model, Principal principal) {
		
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		String userName = loginedUser.getUsername();
		
		userService.updateProfilePicture(userName, file);
		

	    
		model.addAttribute("currentProfile", userService.getAllProfilePictures().get(userName));
		
	    
		return "/author/authorpage";
	}
	
	@RequestMapping(path = "/author/changepassword", method = RequestMethod.GET)
	public String changePassword(Model model, Principal principal) {
		
		
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userService.getAllProfilePictures().get(loginedUser.getUsername()));
		
		model.addAttribute("uname", loginedUser.getUsername());

		return "/author/changepassword";
	}
	
	@RequestMapping(path = "/author/changepassword", method = RequestMethod.POST)
	public String ChangePassowrdPost(@RequestParam String password, @RequestParam String confirmPassword,  Model model, Principal principal) {
		
		
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		String userName = loginedUser.getUsername();
		
		System.out.println("Updated User ID="+userName);
		
		String message = null;
		
		
		model.addAttribute("currentProfile", userService.getAllProfilePictures().get(loginedUser.getUsername()));
		model.addAttribute("uname", userName);
		
		if(password != null && confirmPassword != null)
		{
			if(password.equals(confirmPassword))
			{
				userService.changePassword(userName, password);				
		    	
		    	model.addAttribute("message", message);
			    
				return "/author/authorpage";
			}
			else
			{
				message = "Passwords do not match !!";
				model.addAttribute("message", message);
				return "/author/changepassword";
			}
				
		}
		else
		{
			message = "Passowrd can not be empty";
			model.addAttribute("message", message);
			return "/author/changepassword";
		}	   
		
	}

}
