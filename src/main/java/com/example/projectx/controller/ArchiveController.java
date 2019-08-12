package com.example.projectx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.projectx.form.ArticleUploadForm;
import com.example.projectx.repository.ArchiveRepository;

@Controller
public class ArchiveController {

	@Autowired
	 ArchiveRepository archiverepo;
	
	 @RequestMapping(value =  "archive", method = RequestMethod.GET)
	    public String archivePage(Model model) {
	    	ArticleUploadForm articleUploadForm = new ArticleUploadForm();
		    model.addAttribute("articleUploadForm", articleUploadForm);
		    model.addAttribute("archives",archiverepo.findAll());
	        return "journal-archives";
	    }
}
