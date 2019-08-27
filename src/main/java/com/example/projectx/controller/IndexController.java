package com.example.projectx.controller;

import java.security.Principal;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.projectx.dto.UserDto;
import com.example.projectx.form.ArticleUploadForm;
import com.example.projectx.mail.EmailService;
import com.example.projectx.mail.Mail;
import com.example.projectx.model.AppUser;
import com.example.projectx.model.Journal;
import com.example.projectx.model.JournalIssue;
import com.example.projectx.model.Notice;
import com.example.projectx.repository.DownloadRepository;
import com.example.projectx.repository.NoticeRepository;
import com.example.projectx.repository.PageRepository;
import com.example.projectx.repository.UserRepository;
import com.example.projectx.service.JournalService;
import com.example.projectx.service.UserDetailsServiceImpl;
import com.example.projectx.utils.WebUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

@Controller
public class IndexController {
	

	@Autowired
	private PageRepository pagerepo;
	@Autowired
	private DownloadRepository downloadrepo;
	@Autowired
	private NoticeRepository noticerepo;

	@Autowired
	private UserRepository userrepo;
	
	@Autowired
	private JournalService journalService;

	
	@Autowired
	private UserDetailsServiceImpl userService;
	
	

	
	@RequestMapping(path = "register", method = RequestMethod.GET)
	public String register(Model model) {
		return "registration";
	}
	
	@RequestMapping(path = "register", method = RequestMethod.POST)
	public String registerPost(@RequestParam("email") String email,
			@RequestParam("name") String fullName,
			@RequestParam("username") String username, 
			@RequestParam("password") String password,
			@RequestParam("cpassword") String cpassword,			
			Model model) {
		String message = null;
		if(password != null && cpassword != null && email != null && username != null
			&& password !="" &&cpassword !="" &&email !="" &&username !="")
		{
			if(password.equals(cpassword))
			{
				List<UserDto>  users = userrepo.findByEmail(email);
				if(users.size() > 0)
				{
					message = "This Email is already registered.";
					model.addAttribute("message", message);
					return "registration";
				}
				else
				{
					userService.registerAuthor(email, username, password, fullName);
					message = "Your are now registered ! Please log on";
					model.addAttribute("message", message);
					return "login";
				}
				
			}
			else
			{
				message = "Password did not match. Please re-enter";
				
			}
		}
		else
		{
			if(email==null || email=="")
				message="Email must be entered. ";
			if(username==null || username=="")
				message=message+ "Username must be entered. ";
			if(password==null || password=="")
				message=message+"Password must be entered. ";
			if(cpassword==null || cpassword=="")
				message=message+"Confirm password must be entered. ";
		}
		model.addAttribute("message", message);
		
		return "registration";
	}
	
	@RequestMapping(path = "/resetpassword", method = RequestMethod.GET)
	public String resetPassword(Model model) {		

		return "resetpassword";
	}
	
	@RequestMapping(path = "/resetpassword", method = RequestMethod.POST)
	public String resetPassowrdPost(@RequestParam String email,  Model model) {
		
		
		String message = null;

		if(email != null)
		{
			UserDto user = userService.getUserByEmail(email);
			
			if(user != null)
			{
				userService.resetPassword(user);
				
				message = "Your password has been sent to your email. Please check email and login using new credentials.";
				model.addAttribute("message", message);
				return "/login";
				
			}
			else
			{
				message = "Your email is not found. Please enter valid email address";
				
			}
				
		}
		else
		{
			message = "Please enter valid email address";
			
		}
		model.addAttribute("message", message);

		return "/resetpassword";
   
		
	}
		
	@RequestMapping(value = { "/"}, method = RequestMethod.GET)
    public String indexPage(Model model) {
		
		model.addAttribute("title", "Welcome");
        model.addAttribute("page", pagerepo.getOne(1));
        model.addAttribute("downloads",downloadrepo.findAll() );
        
        model.addAttribute("notices",noticerepo.findAll() );
        
        model.addAttribute("journals",journalService.getAllPublishedJournals());
        
        model.addAttribute("profiles", userService.getEditorsProfilePictures());
        model.addAttribute("editors",userService.getAllEditors());
  
        return "index";
    }
	
	@RequestMapping(value = { "/index"}, method = RequestMethod.GET)
    public String indexPage1(Model model) {	
		model.addAttribute("page", pagerepo.getOne(1));
        model.addAttribute("downloads",downloadrepo.findAll() );
        
        model.addAttribute("notices",noticerepo.findAll() );
        
        model.addAttribute("journals",journalService.getAllPublishedJournals());
        
        model.addAttribute("profiles", userService.getEditorsProfilePictures());
        model.addAttribute("editors",userService.getAllEditors());
        return "index";
    }
	@RequestMapping(value="403", method = RequestMethod.GET)
	public ModelAndView error() {
	    ModelAndView mav = new ModelAndView();
	    String errorMessage= "You are not authorized for the requested data.";
	    mav.addObject("errorMsg", errorMessage);
	    mav.setViewName("403");
	    return mav;
        }
    
    
    @RequestMapping(value = { "contact" }, method = RequestMethod.GET)
    public String contactPage(Model model) {
    	
        return "contactpage/contact-page-index";
    }
    
    
    
    @RequestMapping(value = "/guidelines", method = RequestMethod.GET)
    public String authorGuideline(Model model) {
    	model.addAttribute("guidelines", pagerepo.getOne(2));
    	
        return "/author-guidelines";
    }
    
    @RequestMapping(value = "/aboutus", method = RequestMethod.GET)
    public String aboutUs(Model model) {
    	model.addAttribute("aboutus", pagerepo.getOne(1));
    	
        return "aboutus";
    }
    
   
    @RequestMapping(value = "/downloads", method = RequestMethod.GET)
    public String getDownloads(Model model) {
    	model.addAttribute("downloads",downloadrepo.findAll() );
    	
        return "/downloads";
    }    

    @RequestMapping(value = "/notices", method = RequestMethod.GET)
    public String getNotices(@RequestParam("id") int id, Model model) {
    	
    	Notice n = noticerepo.findById(id).get();

    	model.addAttribute("notices",noticerepo.findAll());

    	model.addAttribute("notice", n);
 	
        return "noticedetail";
    }
    
    @RequestMapping(value = "/viewjournal", method = RequestMethod.GET)
    public String viewJournal(@RequestParam("id") int id, Model model)
    {
    	
    	List<JournalIssue> journals = journalService.getAllPublishedJournalIssues(id);
    	
    	Journal selectedJournal = journalService.getJournalById(id);
    	
    	model.addAttribute("journals", journals);
    	model.addAttribute("selectedJournal", selectedJournal);
    	
    	return "viewjournal";
    }
   
    
    
}
