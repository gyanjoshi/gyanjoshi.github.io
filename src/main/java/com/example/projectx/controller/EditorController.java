package com.example.projectx.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.projectx.dto.JournalDropDownDto;
import com.example.projectx.dto.PreparedJournalDto;
import com.example.projectx.form.ArticleUploadForm;
import com.example.projectx.form.NewJournalForm;
import com.example.projectx.model.AppUser;
import com.example.projectx.model.Journal;
import com.example.projectx.repository.JournalRepository;
import com.example.projectx.service.ArticleService;
import com.example.projectx.service.DownloadService;
import com.example.projectx.service.JournalService;
import com.example.projectx.service.UserDetailsServiceImpl;

@Controller
public class EditorController {
	
	@Autowired
	private JournalService journalService;
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private JournalRepository journalRepo;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	
	@RequestMapping(path = "/editor", method = RequestMethod.GET)
	public String editor(Model model, Principal principal) {
		
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
		return "/editor/editorpage";
	}
	
	@RequestMapping(path = "/editor/newjournal", method = RequestMethod.GET)
	public String newJournal(Model model, Principal principal) {
		
		NewJournalForm newJournal = new NewJournalForm();
	    model.addAttribute("newJournal", newJournal);
	    
	    User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
	    
		return "/editor/newjournal";
	}
	@RequestMapping(path = "/editor/add-cover", method = RequestMethod.GET)
	public String add_cover(@RequestParam String jid, Model model, Principal principal) {
		
		model.addAttribute("jid", jid);
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
	    
		return "/editor/add-cover";
	}
	
	@RequestMapping(path = "/editor/add-cover", method = RequestMethod.POST)
	public String addCoverPage(@RequestParam int jid, MultipartFile file, Model model, Principal principal) {
		
		System.out.println("Updated JID="+jid);
		journalService.updateCoverPage(jid, file);
		
		model.addAttribute("journals", journalService.getAllJournals());
	    model.addAttribute("coverimages", journalService.getAllCoverImage());
	    
	    User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
	    
		return "/editor/viewjournals";
	}
	@RequestMapping(path = "/editor/viewjournals", method = RequestMethod.GET)
	public String viewJournal(Model model, Principal principal) {
		
		List<Journal> list = journalService.getAllJournals();
		
		if(list != null)
		{
			if(list.size() > 0)
			{
				model.addAttribute("journals",list );
			    model.addAttribute("coverimages", journalService.getAllCoverImage());
			}
		}
		else
		{
			model.addAttribute("message", "No active journals found. Please create journal");
		}		
		
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
	    
		return "/editor/viewjournals";
	}
	
	@RequestMapping(value = "/editor/edit-journal", method = RequestMethod.GET)
    public String editJournal(@RequestParam int jid, Model model, Principal principal) {
		Journal journal = journalRepo.findById(jid).get();    	
		model.addAttribute("journal", journal);
		return "/editor/edit-journal";
    }
	
	@RequestMapping(value = "/editor/edit-journal", method = RequestMethod.POST)
    public String updateJournal(@RequestParam int Id, @RequestParam String JournalTopic, @RequestParam String IssueNum, @RequestParam String VolumeNum ,
    		@RequestParam String year, @RequestParam String month, Model model, Principal principal) {
    	
		model.addAttribute("journals", journalService.getAllJournals());
	    model.addAttribute("coverimages", journalService.getAllCoverImage());
	    
	    User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
	    
	    
		journalService.updateJournal(Id,JournalTopic,IssueNum,VolumeNum,year,month);
        return "/editor/viewjournals";
    }
	
	
	@RequestMapping(path = "/editor/pendingreview", method = RequestMethod.GET)
	public String pendingReview(Model model, Principal principal) {
		
		model.addAttribute("pending", articleService.getPendingArticles());
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
	    
		return "editor/pendingreview";
	}
	@RequestMapping(path = "/editor/preparejournal", method = RequestMethod.GET)
	public String prepare(Model model, Principal principal) {
		
		List<JournalDropDownDto> list = journalService.getJournalsDropdown();
		
		if(list != null)
		{
			if(list.size() > 0)
			{
				model.addAttribute("journals", list);
				
			}
			else
				{
					model.addAttribute("message", "No active journals found. Please create journal");
				}
		}
		else
		{
			model.addAttribute("message", "No active journals found. Please create journal");
		}	
		
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
	    
		return "/editor/preparejournal";
	}
	
	@RequestMapping(path = "/editor/preparejournal", method = RequestMethod.POST)
	public String prepareJournal(@RequestParam int journalId,
			@RequestParam MultipartFile editorial,
			Model model, Principal principal) {
		
		String returnPage;
		File f = null;
		//String destPath = downloadService.getDownloadPath("journal");
		
		try {
			f = journalService.prepare(journalId,editorial);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(f==null || !f.exists())
		{
			returnPage = "error";
		}
		else
		{
			//model.addAttribute("downloadpath", journalService.getJournalsDropdown());
			model.addAttribute("journals", journalService.getPreparedJournals());
			model.addAttribute("coverimages", journalService.getAllCoverImage());
			returnPage = "/editor/publishjournal";
		}
		
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
	    
		return returnPage;
	}
	
	@RequestMapping(path = "/editor/publishjournal", method = RequestMethod.GET)
	public String publish(Model model, Principal principal) {
		
		List<PreparedJournalDto> list = journalService.getPreparedJournals();
		
		if(list != null)
		{
			if(list.size() > 0)
			{
				model.addAttribute("journals", list);
			
				model.addAttribute("coverimages", journalService.getAllCoverImage());
			}
			else
				{
					model.addAttribute("message", "No Prepared journals found. Please prepare journal for publishing.");
				}
		}
		else
		{
			model.addAttribute("message", "No Prepared journals found. Please prepare journal for publishing.");
			
		}	
		
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
	    
		return "/editor/publishjournal";
	}
	
	@RequestMapping(path = "/editor/publish", method = RequestMethod.GET)
	public String publishFinal(@RequestParam("jid") int journalId, Model model, Principal principal) {
		
		journalService.publish(journalId);
		
		List<PreparedJournalDto> list = journalService.getPreparedJournals();
		
		if(list != null)
		{
			if(list.size() > 0)
			{
				model.addAttribute("journals", list);				
				model.addAttribute("coverimages", journalService.getAllCoverImage());
			}
			else
				{
					model.addAttribute("message", "No Prepared journals found. Please prepare journal for publishing.");
				}
		}
		else
		{
			model.addAttribute("message", "No Prepared journals found. Please prepare journal for publishing.");
		}
		
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
	    
		return "/editor/publishjournal";
	
	}
	
	@RequestMapping(value = "/editor/approved-articles/{journalid}", method = RequestMethod.GET)
	public String showApprovedArticles(Model model, @PathVariable("journalid") int journalid, Principal principal) {
	    
		model.addAttribute("articles", articleService.getApprovedArticles(journalid));
		System.out.println("Journal Id:"+journalid);
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
	    
	    return "/editor/approved-articles :: resultsList";
	}
	
	
	@RequestMapping(path = "/editor/send-feedback", method = RequestMethod.GET)
	public String sendFeedback(@RequestParam int article,@RequestParam String author, Model model, Principal principal) {
		
		model.addAttribute("article", articleService.getArticleById(article));
		model.addAttribute("author", userDetailsService.getUserByUsername(author));
		
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
	    
		
		return "/editor/send-feedback";
	}
	
	@RequestMapping(path = "/editor/send-feedback", method = RequestMethod.POST)
	public String postFeedback(@RequestParam int Id,@RequestParam String userName, @RequestParam String message, Model model, Principal principal) {
		
		articleService.sendFeedback(Id, userName, message);
		model.addAttribute("pending", articleService.getPendingArticles());
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
	    
		return "editor/pendingreview";
	}
	
	@RequestMapping(path = "/editor/approve", method = RequestMethod.GET)
	public String approveArticle(@RequestParam int article,@RequestParam String author, Model model, Principal principal) {
		
		List<JournalDropDownDto> list = journalService.getJournalsDropdown();
		if(list==null || list.size()==0)
			model.addAttribute("message", "No active journals found. Please create journal");
		else
		{
			model.addAttribute("journals", journalService.getJournalsDropdown());
		}
		model.addAttribute("article", articleService.getArticleById(article));
		model.addAttribute("author", userDetailsService.getUserByUsername(author));
		
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
	    
		return "/editor/approve";
	}
	
	@RequestMapping(path = "/editor/approve", method = RequestMethod.POST)
	public String approveArticlePost(@RequestParam int Id,@RequestParam String userName, @RequestParam String message, @RequestParam String journalId, Model model,
			Principal principal) {
		
		articleService.approve(Id, userName, message, Integer.valueOf(journalId));
		model.addAttribute("pending", articleService.getPendingArticles());
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
	    
		return "editor/pendingreview";
	}
	
	

	
	@RequestMapping(value = "/editor/newjournal", method = RequestMethod.POST)
	public String createNewJournal(HttpServletRequest request,Model model,@ModelAttribute("newJournal") NewJournalForm newJournal, Principal principal )
	{
	
		String topic = newJournal.getTitle();
		String issue = newJournal.getIssue();
		String volume = newJournal.getVolume();
		String message = newJournal.getMessage();
		
		journalService.createJournal(topic, issue, volume, message);
		
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
	    
		
		return "/editor/editorpage";
	 
	}
	

}
