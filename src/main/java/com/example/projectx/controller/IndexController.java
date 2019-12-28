package com.example.projectx.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.projectx.dto.ArticleDto;
import com.example.projectx.dto.UserDto;
import com.example.projectx.model.Article;
import com.example.projectx.model.Journal;
import com.example.projectx.model.JournalIssue;
import com.example.projectx.model.Notice;
import com.example.projectx.repository.DownloadRepository;
import com.example.projectx.repository.NoticeRepository;
import com.example.projectx.repository.PageRepository;
import com.example.projectx.repository.UserRepository;
import com.example.projectx.service.ArticleService;
import com.example.projectx.service.EditorService;
import com.example.projectx.service.JournalService;
import com.example.projectx.service.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;

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
	private ArticleService articleService;
	
	@Autowired
	private EditorService editorService;

	
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
        
        model.addAttribute("notices",noticerepo.findAll());
        
        model.addAttribute("journals",journalService.getAllPublishedJournals());
        model.addAttribute("coverimages", journalService.getAllCoverImage());
        
        model.addAttribute("profiles", userService.getEditorsProfilePictures());
        model.addAttribute("editors",userService.getAllEditors());
        model.addAttribute("boardeditors",editorService.getAllEditors());
        model.addAttribute("boardprofiles", editorService.getAllProfilePictures());
        
  
        return "index";
    }
	
	@RequestMapping(value = { "/index"}, method = RequestMethod.GET)
    public String indexPage1(Model model) {	
		model.addAttribute("page", pagerepo.getOne(1));
        model.addAttribute("downloads",downloadrepo.findAll() );
        
        model.addAttribute("notices",noticerepo.findAll() );
        
        model.addAttribute("journals",journalService.getAllPublishedJournals());
        model.addAttribute("coverimages", journalService.getAllCoverImage());
        model.addAttribute("profiles", userService.getEditorsProfilePictures());
        model.addAttribute("editors",userService.getAllEditors());
        model.addAttribute("boardeditors",editorService.getAllEditors());
        model.addAttribute("boardprofiles", editorService.getAllProfilePictures());
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
	
	@RequestMapping(value="/error", method = RequestMethod.GET)
	public ModelAndView errorPage() {
	    ModelAndView mav = new ModelAndView();
	    String errorMessage= "Error occured. Please contact site administrator @ editor.craiaj2019@gmail.com";
	    mav.addObject("errorMsg", errorMessage);
	    mav.setViewName("error");
	    return mav;
        }
    
	@RequestMapping(value = { "/contact" }, method = RequestMethod.GET)
    public String contactPage(Model model) {
    	
        return "contactpage";
    }
    
    @RequestMapping(value = "/guidelines", method = RequestMethod.GET)
    public String authorGuideline(Model model) {
    	model.addAttribute("guidelines", pagerepo.getOne(3));
    	model.addAttribute("page", pagerepo.getOne(1));
        return "/author-guidelines";
    }
    
    @RequestMapping(value = "/aboutus", method = RequestMethod.GET)
    public String aboutUs(Model model) {
    	model.addAttribute("aboutus", pagerepo.getOne(1));
    	model.addAttribute("page", pagerepo.getOne(1));
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

    	//model.addAttribute("notices",noticerepo.findAll());
    	//model.findFirst4ByUploadeddate();

    	model.addAttribute("notice", n);
 	
        return "noticedetail";
    }
    
    @RequestMapping(value = "/listnotices", method = RequestMethod.GET)
    public String getNoticeList(Model model) {
    	
    	//Notice n = noticerepo.findById(id).get();

    	model.addAttribute("notices",noticerepo.findAll());

    	//model.addAttribute("notice", n);
 	
        return "notice";
    }

    @RequestMapping(value = "/viewjournal", method = RequestMethod.GET)
	public String viewJournal( @RequestParam("jid") int jid , @RequestParam("jiid") int jiid,  Model model)
    {
    	
    	//List<JournalIssue> journals = journalService.getAllPublishedJournalIssues(id);
    	
    	
    	JournalIssue selectedJournal = journalService.getJournalIssueById(jiid);
    	
    	String editorial = selectedJournal.getEditorialFileName();
    	int editorpagecount = selectedJournal.getEditorialpageCount();
    	
    	
    	
    	List<ArticleDto> mainList = new ArrayList<ArticleDto>();
    	
    	mainList = articleService.getPublishedArticles(jiid);
    	
    	mainList.sort(new Comparator<ArticleDto>()
		{
    		public int compare(ArticleDto o1, ArticleDto o2){
    	         if(o1.getTocorder() == o2.getTocorder())
    	             return 0;
    	         return o1.getTocorder() < o2.getTocorder() ? -1 : 1;
    		}
		});
    	
    	// update Page From and To for each item (for shwoing in Table of content)
    	int curIdx = editorpagecount;
    	for (ArticleDto a: mainList)
    	{
    		
    		int numPages = a.getPageCount();
    		
    		int from = curIdx+1;
    		int to = from+numPages-1;
    		
    		curIdx = to;
    		
    		String pagefromto = from + " - " + to;
    		a.setPageFromTo(pagefromto);
    	}
    	
//    	mainList.addAll(selectedJournal.getArticles());
    	
    	
    	model.addAttribute("selectedJournal", selectedJournal);
    	model.addAttribute("editorial", editorial);
    	model.addAttribute("epages", editorpagecount);
    	model.addAttribute("articles", mainList);
    	model.addAttribute("journals",journalService.getAllPublishedJournals());
        model.addAttribute("coverpage", journalService.getCoverImage(jid));
        model.addAttribute("currentjournal", selectedJournal.getJournal());
       
    	
    	return "viewjournal";
    }
    
    @RequestMapping(value = "/current", method = RequestMethod.GET)
	public String viewJournal( @RequestParam("jid") int jid , Model model)
    {
    	JournalIssue selectedJournal = journalService.getCurrentJournalIssue(jid);
    	
    	int pagecnt = selectedJournal.getEditorialpageCount();
    	
    	List<ArticleDto> mainList = new ArrayList<ArticleDto>();
    	
    	mainList = articleService.getPublishedArticles(selectedJournal.getId());
    	
    	mainList.sort(new Comparator<ArticleDto>()
		{
    		public int compare(ArticleDto o1, ArticleDto o2){
    	         if(o1.getTocorder() == o2.getTocorder())
    	             return 0;
    	         return o1.getTocorder() < o2.getTocorder() ? -1 : 1;
    		}
		});
    	
    	// update Page From and To for each item (for shwoing in Table of content)
    	int curIdx = pagecnt;
    	for (ArticleDto a: mainList)
    	{
    		
    		int numPages = a.getPageCount();
    		
    		int from = curIdx+1;
    		int to = from+numPages-1;
    		
    		curIdx = to;
    		
    		String pagefromto = from + " - " + to;
    		a.setPageFromTo(pagefromto);
    	}   	
    	
    	model.addAttribute("selectedJournal", selectedJournal);
    	model.addAttribute("epages", pagecnt);
    	model.addAttribute("editorial", selectedJournal.getEditorialFileName());
    	model.addAttribute("articles", mainList);
    	model.addAttribute("journals",journalService.getAllPublishedJournals());
        model.addAttribute("coverpage", journalService.getCoverImage(jid)); 
        model.addAttribute("currentjournal", selectedJournal.getJournal());
    	
    	return "viewjournal";
    }
    
//test journal view
    @RequestMapping(value = "/testjournal", method = RequestMethod.GET)
    public String testJournal( Model model)
    {
    	model.addAttribute("journals",journalService.getAllPublishedJournals());
        model.addAttribute("coverimages", journalService.getAllCoverImage());
    	return "testjournal";
    }
    
  //test journal archives
    @RequestMapping(value = "/journalarchives", method = RequestMethod.GET)
    public String getJournalArchives(@RequestParam("jid") int jid, Model model)
    {
    	
    	List<JournalIssue> journals = journalService.getAllPublishedJournalIssues(jid);
    	JournalIssue selectedJournal = journalService.getCurrentJournalIssue(jid);
    	if(selectedJournal == null)
    		model.addAttribute("currentjournal", null);
    	else
    		model.addAttribute("currentjournal", selectedJournal.getJournal());
    		
    	model.addAttribute("journals",journals);
    	model.addAttribute("selectedJournal", selectedJournal);
    	
        model.addAttribute("coverpage", journalService.getCoverImage(jid));
        
    	return "journalarchive";
    }
    
    @RequestMapping(value = "/alljournals", method = RequestMethod.GET)
    public String getAllJournalArchives(Model model)
    {
    	
    	List<Journal> journals = journalService.getAllJournals();
    	
    	model.addAttribute("journals",journals);
        model.addAttribute("coverimages", journalService.getAllCoverImage());
        model.addAttribute("currentjournal", null);
        
    	return "alljournals";
    }
    
  //test editorboard
    @RequestMapping(value = "/editorboard", method = RequestMethod.GET)
    public String testEditorTeam( Model model)
    {
    	model.addAttribute("exeditors",userService.getAllEditors());
        model.addAttribute("boardeditors",editorService.getAllEditors());
        model.addAttribute("profiles", userService.getEditorsProfilePictures());
        model.addAttribute("boardprofiles", editorService.getAllProfilePictures());
        model.addAttribute("page", pagerepo.getOne(1));
    	return "editorial-board";
    }
    
    
    
    
}
