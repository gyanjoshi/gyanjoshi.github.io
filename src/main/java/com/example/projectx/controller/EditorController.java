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
import com.example.projectx.form.NewJournalIssueForm;
import com.example.projectx.model.AppUser;
import com.example.projectx.model.Journal;
import com.example.projectx.model.JournalIssue;
import com.example.projectx.repository.JournalIssueRepository;
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
	private JournalIssueRepository journalIssueRepo;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	
	@RequestMapping(path = "/editor", method = RequestMethod.GET)
	public String editor(Model model, Principal principal) {
		
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
		return "/editor/editorpage";
	}
	
	@RequestMapping(path = "/editor/createjournal", method = RequestMethod.GET)
	public String createNewJournal(Model model, Principal principal) {
		
		
	    User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
	    
		return "/editor/createjournal";
	}
	
	@RequestMapping(path = "/editor/createjournal", method = RequestMethod.POST)
	public String createNewJournalPost(@RequestParam String title, @RequestParam MultipartFile coverPage, Model model, Principal principal) {
		
		journalService.createJournal(title, coverPage);
		
		List<Journal> list = journalService.getAllJournals();
		
	    User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
		
		model.addAttribute("journals", list);
		model.addAttribute("coverimages", journalService.getAllCoverImage());
	    
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
	
	@RequestMapping(value = "/editor/editjournal", method = RequestMethod.GET)
    public String editJournal(@RequestParam int id, Model model, Principal principal) {
		
		Journal journal = journalRepo.findById(id).get();    	
		
		model.addAttribute("journal", journal);
		return "/editor/editjournal";
    }
	
	@RequestMapping(value = "/editor/editjournal", method = RequestMethod.POST)
    
	public String editJournalPost(@RequestParam int id, @RequestParam String JournalTopic, Model model, Principal principal) {
    	
		
		journalService.updateJournal(id,JournalTopic);
		
		model.addAttribute("journals", journalService.getAllJournals());
	    model.addAttribute("coverimages", journalService.getAllCoverImage());
	    
	    User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
	    
	   
        return "/editor/viewjournals";
    }
	@RequestMapping(path = "/editor/editjournalcover", method = RequestMethod.GET)
	public String editJournalCover(@RequestParam int id, Model model, Principal principal) {
		
		model.addAttribute("id", id);
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
	    
		return "/editor/editjournalcover";
	}
	
	@RequestMapping(path = "/editor/editjournalcover", method = RequestMethod.POST)
	public String editJournalCoverPost(@RequestParam int id,@RequestParam MultipartFile file, Model model, Principal principal) {
		
		System.out.println("Updated JID="+id);
		
		journalService.updateCoverPage(id, file);
		
		model.addAttribute("journals", journalService.getAllJournals());
	    model.addAttribute("coverimages", journalService.getAllCoverImage());
	    
	    User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
	    
		return "/editor/viewjournals";
	}
	
	@RequestMapping(path = "/editor/add-cover", method = RequestMethod.GET)
	public String addCover(@RequestParam String jid, Model model, Principal principal) {
		
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
	
	@RequestMapping(path = "/editor/newissue", method = RequestMethod.GET)
	public String newJournalIssue(Model model, Principal principal) {		
	    
	    model.addAttribute("journals", journalService.getAllJournals());
	    User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
	    
		return "/editor/newissue";
	}
	
	@RequestMapping(path = "/editor/newissue", method = RequestMethod.POST)
	public String newJournalIssuePost(@RequestParam("journalId") int id,@RequestParam String volume, @RequestParam String issue, @RequestParam String message, Model model, Principal principal) {		
	    
		journalService.createJournalIssue(id, issue, volume, message);
		
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
		
		model.addAttribute("journals", journalService.getAllJournals());
		model.addAttribute("id", id);
		model.addAttribute("title", journalRepo.findById(id).get());
		model.addAttribute("coverimage", journalService.getCoverImage(id));
	    model.addAttribute("journalissues", journalService.getAllJournalIssues(id));
	    
		return "/editor/viewjournalissues";
	}
	
	@RequestMapping(path = "/editor/viewjournalissues", method = RequestMethod.GET)
	public String viewJournalIssues(Model model, Principal principal) {
		
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
	    
		return "/editor/viewjournalissues";
	}
	
	@RequestMapping(value = "/editor/journalissues/{journalid}", method = RequestMethod.GET)
	public String showJournalIssues(Model model, @PathVariable("journalid") int journalid, Principal principal) {
	    
		List<JournalIssue> list = journalService.getAllJournalIssues(journalid);
		
		model.addAttribute("journalissues", list);
		
		System.out.println("Journal Id:"+journalid);
		
		model.addAttribute("id", journalid);
		model.addAttribute("title", journalRepo.findById(journalid).get().getJournalTopic());
		model.addAttribute("coverimage", journalService.getCoverImage(journalid));
		
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
	    
	    return "/editor/journalissues :: resultsList";
	}
	
	
	@RequestMapping(value = "/editor/editjournalissue", method = RequestMethod.GET)
    public String editJournalIssue(@RequestParam int jid, Model model, Principal principal) {
		JournalIssue journal = journalIssueRepo.findById(jid).get();    	
		model.addAttribute("journalissue", journal);
		return "/editor/editjournalissue";
    }
	
	@RequestMapping(value = "/editor/editjournalissue", method = RequestMethod.POST)
    
	public String editJournalIssuePost(@RequestParam int Id, @RequestParam String IssueNum, @RequestParam String VolumeNum ,
    		@RequestParam String year, @RequestParam String month, Model model, Principal principal) {
    	
		model.addAttribute("journals", journalService.getAllJournals());
	    model.addAttribute("coverimages", journalService.getAllCoverImage());
	    
	    User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
	    
	    
		journalService.updateJournalIssue(Id,IssueNum,VolumeNum,year,month);
        return "/editor/viewjournalissues";
    }
	
	
	@RequestMapping(path = "/editor/pendingreview", method = RequestMethod.GET)
	public String pendingReview(Model model, Principal principal) {
		
		model.addAttribute("pending", articleService.getPendingArticles());
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
	    
		return "editor/pendingreview";
	}
	@RequestMapping(value = "/editor/getIssuesList/{journalid}", method = RequestMethod.GET)
	public String getIssuesList(Model model, @PathVariable("journalid") int journalid, Principal principal) {
	    
		List<JournalIssue> list = journalService.getAllJournalIssues(journalid);
		
		model.addAttribute("journalissues", list);
		
		System.out.println("Journal Id:"+journalid);		
	    
	    return "/editor/getIssuesList :: resultsList";
	}
	@RequestMapping(path = "/editor/approve", method = RequestMethod.GET)
	public String approveArticle(@RequestParam int article,@RequestParam String author, Model model, Principal principal) {
		
		model.addAttribute("journals", journalService.getAllJournals());
		model.addAttribute("article", articleService.getArticleById(article));
		model.addAttribute("author", userDetailsService.getUserByUsername(author));
		
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
	    
		return "/editor/approve";
	}
	
	@RequestMapping(path = "/editor/approve", method = RequestMethod.POST)
	public String approveArticlePost(@RequestParam int Id,@RequestParam String userName, @RequestParam String message, 
									 @RequestParam int journalId,
									 @RequestParam int jissueid,
									 Model model,
			Principal principal) {
		
		articleService.approve(Id, userName, message,journalId,jissueid );
		model.addAttribute("pending", articleService.getPendingArticles());
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
	    
		return "editor/pendingreview";
	}
	
	@RequestMapping(path = "/editor/approvedlist", method = RequestMethod.GET)
	public String editApprovedArticle(Model model, Principal principal) {
		
		model.addAttribute("approved", articleService.getApprovedArticles());
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
	    
		return "editor/pendingreview";
	}
	
	@RequestMapping(path = "/editor/preparejournal", method = RequestMethod.GET)
	public String prepare(Model model, Principal principal) {
	
		model.addAttribute("journals", journalService.getAllJournals());
		
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
	    
		return "/editor/preparejournal";
	}
	
	@RequestMapping(path = "/editor/preparejournal", method = RequestMethod.POST)
	public String prepareJournal(
			@RequestParam int journalId,
			@RequestParam int jissueid,
			@RequestParam MultipartFile editorial,
			Model model, Principal principal) 
	{
		
		String returnPage;
		File f = null;
		
		try {
			f = journalService.prepare(jissueid,editorial);
			
			
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
			model.addAttribute("journals", journalService.getPreparedJournalIssues(journalId));
			returnPage = "/editor/publishjournal";
		}
		
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
	    
		return returnPage;
	}
	
	@RequestMapping(value = "/editor/getPreparedIssuesList/{journalId}", method = RequestMethod.GET)
	public String getPreparedIssuesList(Model model, @PathVariable("journalId") int journalid, Principal principal) {
	    
		List<PreparedJournalDto> list = journalService.getPreparedJournalIssues(journalid);
		
		//List<JournalIssue> list = journalService.getA(journalid);
		
		model.addAttribute("journalissues", list);
		model.addAttribute("journal", journalRepo.findById(journalid).get());
		
		model.addAttribute("coverimages", journalService.getAllCoverImage());
		
		System.out.println("Journal Id:"+journalid);		
	    
	    return "/editor/getPreparedIssuesList :: resultsList";
	}
	
	@RequestMapping(path = "/editor/publishjournal", method = RequestMethod.GET)
	public String publish(Model model, Principal principal) {
		
		model.addAttribute("journals", journalService.getAllJournals());	
		
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
	    
		return "/editor/publishjournal";
	}
	
	@RequestMapping(path = "/editor/publish", method = RequestMethod.GET)
	public String publishFinal(@RequestParam("jid") int journalId, Model model, Principal principal) {
		
		journalService.publish(journalId);
		
		model.addAttribute("journals", journalService.getAllJournals());
		
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
	
	

	
	@RequestMapping(value = "/editor/newjournal", method = RequestMethod.POST)
	public String createNewJournalIssue(HttpServletRequest request,Model model,@ModelAttribute("newJournal") NewJournalIssueForm newJournal, Principal principal )
	{
	
		
		String issue = newJournal.getIssue();
		String volume = newJournal.getVolume();
		String message = newJournal.getMessage();
		int jid = newJournal.getJournalId();
		
		journalService.createJournalIssue(jid, issue, volume, message);
		
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
	    
		
		return "/editor/editorpage";
	 
	}
	

}
