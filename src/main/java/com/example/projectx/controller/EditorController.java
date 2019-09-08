package com.example.projectx.controller;

import java.security.Principal;

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

import org.springframework.web.multipart.MultipartFile;

import com.example.projectx.dto.ArticleDto;
import com.example.projectx.dto.PreparedJournalDto;
import com.example.projectx.form.EditorForm;
import com.example.projectx.form.NewJournalIssueForm;

import com.example.projectx.model.Article;
import com.example.projectx.model.Editor;
import com.example.projectx.model.Journal;
import com.example.projectx.model.JournalIssue;
import com.example.projectx.repository.EditorRepository;
import com.example.projectx.repository.JournalIssueRepository;
import com.example.projectx.repository.JournalRepository;
import com.example.projectx.service.ArticleService;
import com.example.projectx.service.EditorService;
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
	
	@Autowired
	private EditorService editorService;
	
	@Autowired
	private EditorRepository editorRepo;
	
	
	@RequestMapping(path = "/editor", method = RequestMethod.GET)
	public String editor(Model model, Principal principal) {
		
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
		return "/editor/editorpage";
	}
	
	@RequestMapping(path = "/editor/editors", method = RequestMethod.GET)
	public String editors(Model model, Principal principal) {
		
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));		
		List<Editor> editors = editorService.getAllEditors();		
		model.addAttribute("editors", editors);
		model.addAttribute("profiles", editorService.getAllProfilePictures());
		return "/editor/editors";
	}
	
	@RequestMapping(path = "/editor/add-editor", method = RequestMethod.GET)
	public String addEditor(Model model, Principal principal) {
		
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));		
		model.addAttribute("editor", new EditorForm());
		model.addAttribute("titles", userDetailsService.getAllTitles());
    	model.addAttribute("qualifications", userDetailsService.getAllQualifications());
    	
		return "/editor/add-editor";
	}
	
	@RequestMapping(value = "/editor/add-editor", method = RequestMethod.POST)
    public String addEditorPost(@ModelAttribute EditorForm editor,BindingResult result, Model model) {
    	
    	String returnPage;
    	
    	List<Editor> existing = editorRepo.findEditorByEmail(editor.getEmail());
    	if (existing != null && existing.size() > 0){
    		
    		System.out.println("Email already found occured "+existing.get(0).getEmail());
            result.rejectValue("email", null, "This email is already registered.");
        }
    	if (result.hasErrors()){
    		model.addAttribute("editor", new EditorForm());
    		model.addAttribute("titles", userDetailsService.getAllTitles());
        	model.addAttribute("qualifications", userDetailsService.getAllQualifications());
        	
            return "/editor/add-editor";
        }
    	
    	String message = editorService.addEditor(editor);
    	
    	if(message.startsWith("ERROR"))
    	{
    		model.addAttribute("editor", new EditorForm());
        	
        	model.addAttribute("titles", userDetailsService.getAllTitles());
        	model.addAttribute("qualifications", userDetailsService.getAllQualifications());
    		
    		returnPage = "/editor/add-editor";
    	}
    	else
    		returnPage = "/editor/editors";
    	
    	model.addAttribute("message", message);
    			
    	model.addAttribute("editors", editorService.getAllEditors());
    	model.addAttribute("profiles", editorService.getAllProfilePictures());
    	
        return returnPage;
    }
	
	@RequestMapping(value = "/editor/edit-editor", method = RequestMethod.GET)
    public String editEditor(@RequestParam Integer id, Model model) {
    	
		Editor editor = editorRepo.findById(id).get();
    	
		model.addAttribute("editor", editor);
		model.addAttribute("profiles", editorService.getAllProfilePictures());
		model.addAttribute("roles", userDetailsService.getAllRoles());
    	model.addAttribute("titles", userDetailsService.getAllTitles());
    	model.addAttribute("qualifications", userDetailsService.getAllQualifications());
    	
		return "/editor/edit-editor";
    }
    
    @RequestMapping(value = "/editor/edit-editor", method = RequestMethod.POST)
    public String editEditorPost(@RequestParam Integer id, @ModelAttribute EditorForm editor, Model model) {

    	editorService.editEditor(id, editor);
    	model.addAttribute("editors", editorService.getAllEditors());
    	model.addAttribute("profiles", editorService.getAllProfilePictures());
        return "redirect:/editor/editors";
    }
    
    @RequestMapping(value = "editor/delete-editor", method = RequestMethod.GET)
    public String deleteEditor(@RequestParam Integer id,Model model) {
    	editorRepo.deleteById(id);
    	model.addAttribute("editors", editorService.getAllEditors());
    	model.addAttribute("profiles", editorService.getAllProfilePictures());
        return "/editor/editors";
    }
    
    @RequestMapping(path = "/editor/add-profile", method = RequestMethod.GET)
	public String addProfile(@RequestParam String id, Model model, Principal principal) {		
		model.addAttribute("id", id);		
		return "/editor/add-profile";
	}
	
	@RequestMapping(path = "/editor/add-profile", method = RequestMethod.POST)
	public String addProfilePost(@RequestParam("id") Integer id, MultipartFile file, Model model, Principal principal) {
		
		editorService.updateProfilePicture(id, file);
		

	    User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
		model.addAttribute("editors", editorService.getAllEditors());
    	model.addAttribute("profiles", editorService.getAllProfilePictures());
	    
		return "/editor/editors";
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
	public String addCoverPage(@RequestParam int jid, @RequestParam MultipartFile file, Model model, Principal principal) {
		
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
	    
		List<JournalIssue> list = journalService.getDraftJournalIssues(journalid);
		
		model.addAttribute("journalissues", list);
		
		System.out.println("Journal Id:"+journalid);		
	    
	    return "/editor/getIssuesList :: resultsList";
	}
	@RequestMapping(path = "/editor/uploadPDF", method = RequestMethod.GET)
	public String uploadPDF(@RequestParam("article") Integer article, Model model) 
	{		
		model.addAttribute("article", article);		
		return "/editor/uploadPDF";
	}
	
	@RequestMapping(path = "/editor/uploadPDF", method = RequestMethod.POST)
	public String uploadPDFPost(@RequestParam("article") Integer article, MultipartFile file, Model model, Principal principal) {
		
		
		Article a = articleService.getArticleById(article);
		String message = null;		
		if (file.isEmpty()) 
        {

			message =  "Please select a file to upload";
			
            
        }
		else
		{
			articleService.updateArticle(article, file);
			message =  "Article updated successfully!";
		}

	    
	    model.addAttribute("message", message);
	    model.addAttribute("pending", articleService.getPendingArticles());
	    
	    return "editor/pendingreview";
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
									 @RequestParam MultipartFile file,
									 Model model,
			Principal principal) {
		
		articleService.approve(Id, userName, message,journalId,jissueid,file );
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
		
		List<ArticleDto> list = articleService.getApprovedArticles(jissueid);
		String message = null;
		if(list != null)
		{
			if(list.size() == 0)
			{
				message = "There are no articles approved for this issue. Please approve and assign the articles for this issue.";
				returnPage = "editor/preparejournal";
				model.addAttribute("message", message);	
				model.addAttribute("journals", journalService.getAllJournals());
			}
			else
			{
				journalService.prepare(jissueid,editorial);	
				model.addAttribute("message", message);	
				model.addAttribute("journals", journalService.getAllJournals());
				returnPage = "/editor/publishjournal";
				
			}
			  
		}
		else
		{
			model.addAttribute("journals", journalService.getAllJournals());
			model.addAttribute("message", "Journal Issue is prepared for publishing");	
			message = "There are no articles approved for this issue. Please approve and assign the articles for this issue.";
			returnPage = "editor/preparejournal";
		}			
		
			
	    
		return returnPage;
	}
	
	@RequestMapping(value = "/editor/getPreparedIssuesList/{journalId}", method = RequestMethod.GET)
	public String getPreparedIssuesList(Model model, @PathVariable("journalId") int journalid, Principal principal) {
	    
		List<PreparedJournalDto> list = journalService.getPreparedJournalIssues(journalid);
		
		//List<JournalIssue> list = journalService.getA(journalid);
		if(list == null)
			model.addAttribute("message","There are no Journal Issues prepared for publishing.");
		else if (list.size() == 0)
			model.addAttribute("message","There are no Journal Issues prepared for publishing.");		
		else
			model.addAttribute("message",null);
		
		model.addAttribute("journalissues", list);
		model.addAttribute("journal", journalRepo.findById(journalid).get());
		
		model.addAttribute("coverimages", journalService.getAllCoverImage());
		
		System.out.println("Journal Id:"+journalid);		
	    
	    return "/editor/getPreparedIssuesList :: resultsList";
	}
	
	@RequestMapping(path = "/editor/publishjournal", method = RequestMethod.GET)
	public String publish(Model model, Principal principal) {
		
		model.addAttribute("journals", journalService.getAllJournals());	
		
		
	    
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
	    
		List<ArticleDto> list = articleService.getApprovedArticles(journalid);
		String message = null;
		if(list != null)
		{
			if(list.size() == 0)
			  message = "There are no articles approved for this issue. Please approve and assign the articles for this issue.";	
		}
		else
			message = "There are no articles approved for this issue. Please approve and assign the articles for this issue.";
		
		model.addAttribute("message", message);
		model.addAttribute("articles", list);
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
	
	// send to reviewer
	@RequestMapping(path = "/editor/to-reviewer", method = RequestMethod.GET)
	public String toReviewer(@RequestParam int article,@RequestParam String author, Model model, Principal principal) {
		
		model.addAttribute("article", articleService.getArticleById(article));
		model.addAttribute("author", userDetailsService.getUserByUsername(author));		
		model.addAttribute("editors", editorService.getAllEditors());
		
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("currentProfile", userDetailsService.getAllProfilePictures().get(loginedUser.getUsername()));
	    
		
		return "/editor/to-reviewer";
	}
	
	@RequestMapping(path = "/editor/to-reviewer", method = RequestMethod.POST)
	public String toReviewerPost(@RequestParam int Id,@RequestParam String userName, @RequestParam String message,
								 @RequestParam String selectedReviewers, Model model, Principal principal) {
		
		System.out.println("Reviewers:"+selectedReviewers);
		editorService.toReviewer(Id, userName, message, selectedReviewers);
		
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
