package com.example.projectx.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.projectx.form.ArticleUploadForm;
import com.example.projectx.form.NewJournalForm;
import com.example.projectx.model.AppUser;
import com.example.projectx.model.Journal;
import com.example.projectx.repository.JournalRepository;
import com.example.projectx.service.ArticleService;
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
	public String editor(Model model) {
		return "/editor/editorpage";
	}
	
	@RequestMapping(path = "/editor/newjournal", method = RequestMethod.GET)
	public String newJournal(Model model) {
		
		NewJournalForm newJournal = new NewJournalForm();
	    model.addAttribute("newJournal", newJournal);
	    
		return "/editor/newjournal";
	}
	@RequestMapping(path = "/editor/add-cover", method = RequestMethod.GET)
	public String add_cover(@RequestParam String jid, Model model) {
		
		model.addAttribute("jid", jid);
	    
		return "/editor/add-cover";
	}
	
	@RequestMapping(path = "/editor/add-cover", method = RequestMethod.POST)
	public String addCoverPage(@RequestParam int jid, MultipartFile file, Model model) {
		
		System.out.println("Updated JID="+jid);
		journalService.updateCoverPage(jid, file);
		
		model.addAttribute("journals", journalService.getAllJournals());
	    model.addAttribute("coverimages", journalService.getAllCoverImage());
	    
		return "/editor/viewjournals";
	}
	@RequestMapping(path = "/editor/viewjournals", method = RequestMethod.GET)
	public String viewJournal(Model model) {
		
		
		model.addAttribute("journals", journalService.getAllJournals());
	    model.addAttribute("coverimages", journalService.getAllCoverImage());
	    
		return "/editor/viewjournals";
	}
	
	@RequestMapping(value = "/editor/edit-journal", method = RequestMethod.GET)
    public String editJournal(@RequestParam int jid, Model model) {
		Journal journal = journalRepo.findById(jid).get();    	
		model.addAttribute("journal", journal);
		return "/editor/edit-journal";
    }
	
	@RequestMapping(value = "/editor/edit-journal", method = RequestMethod.POST)
    public String updateJournal(@RequestParam int Id, @RequestParam String JournalTopic, @RequestParam String IssueNum, @RequestParam String VolumeNum ,
    		@RequestParam String year, @RequestParam String month, Model model) {
    	
		model.addAttribute("journals", journalService.getAllJournals());
	    model.addAttribute("coverimages", journalService.getAllCoverImage());
	    
		journalService.updateJournal(Id,JournalTopic,IssueNum,VolumeNum,year,month);
        return "/editor/viewjournals";
    }
	
	
	@RequestMapping(path = "/editor/pendingreview", method = RequestMethod.GET)
	public String pendingReview(Model model) {
		
		model.addAttribute("pending", articleService.getPendingArticles());
		return "editor/pendingreview";
	}
	@RequestMapping(path = "/editor/publishjournal", method = RequestMethod.GET)
	public String publish(Model model) {
		return "/editor/publishjournal";
	}
	
	@RequestMapping(path = "/editor/send-feedback", method = RequestMethod.GET)
	public String sendFeedback(@RequestParam int article,@RequestParam String author, Model model) {
		
		model.addAttribute("article", articleService.getArticleById(article));
		model.addAttribute("author", userDetailsService.getUserByUsername(author));
		
		return "/editor/send-feedback";
	}
	
	@RequestMapping(path = "/editor/send-feedback", method = RequestMethod.POST)
	public String postFeedback(@RequestParam int Id,@RequestParam String userName, @RequestParam String message, Model model) {
		
		articleService.sendFeedback(Id, userName, message);
		model.addAttribute("pending", articleService.getPendingArticles());
		return "editor/pendingreview";
	}
	
	
	@RequestMapping(value = "/editor/newjournal", method = RequestMethod.POST)
	public String createNewJournal(HttpServletRequest request,Model model,@ModelAttribute("newJournal") NewJournalForm newJournal )
	{
	
		String topic = newJournal.getTitle();
		String issue = newJournal.getIssue();
		String volume = newJournal.getVolume();
		String message = newJournal.getMessage();
		
		journalService.createJournal(topic, issue, volume, message);
		
		return "/editor/editorpage";
	 
	}
	

}
