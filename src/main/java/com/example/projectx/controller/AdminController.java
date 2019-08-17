package com.example.projectx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.projectx.form.AddDownloadForm;
import com.example.projectx.form.AddNoticeForm;
import com.example.projectx.model.AppUser;
import com.example.projectx.model.Download;
import com.example.projectx.repository.DownloadRepository;
import com.example.projectx.repository.NoticeRepository;
import com.example.projectx.service.DownloadService;

@Controller
public class AdminController {
	@Autowired
	private DownloadRepository downloadrepo;
	
	@Autowired
	private DownloadService downloadService;
	
	@Autowired
	private NoticeRepository noticerepo;
	
	
	
	 @RequestMapping(value = "/admin/downloads", method = RequestMethod.GET)
	    public String getDownloads(Model model) {
	    	model.addAttribute("downloads",downloadrepo.findAll() );
	        return "admin/downloads/download-list";
	    }
	    
	 @RequestMapping(value = "/admin/downloads/add-download", method = RequestMethod.GET)
	    public String addDownload(Model model) {
	    	model.addAttribute("download", new AddDownloadForm());
	        return "admin/downloads/add-download";
	    }
	    @RequestMapping(value = "/admin/downloads/add-download", method = RequestMethod.POST)
	    public String postDownload(@ModelAttribute AddDownloadForm download ,Model model) {
	    	
	    	downloadService.addDownload(download);
	    	model.addAttribute("users", downloadrepo.findAll());
	        return "admin/downloads/download-list";
	    }
	    
	    
	    
	    
	    @RequestMapping(value = "/admin/notices/add-notice", method = RequestMethod.GET)
	    public String addNotice(Model model) {
	    	model.addAttribute("notice", new AddNoticeForm());
	        return "admin/notices/add-notice";
	    }
	    @RequestMapping(value = "/admin/downloads/add-notice", method = RequestMethod.POST)
	    public String postNotice(@ModelAttribute AddNoticeForm notice ,Model model) {
	    	//download service provides downloads and notice related services,hence downloadservice is used here
	    	downloadService.addNotice(notice);
	    	model.addAttribute("notices", noticerepo.findAll());
	        return "admin/downloads/notice-list";
	    }
	    
}
