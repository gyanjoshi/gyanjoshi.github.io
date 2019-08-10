package com.example.projectx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.projectx.model.AppUser;
import com.example.projectx.model.Page;
import com.example.projectx.repository.PageRepository;

@Controller
public class PageController {
	
	
	@Autowired
	private PageRepository pagerepo;
	
	
	  @RequestMapping(value = "pages", method = RequestMethod.GET) 
	  public String userPage(Model model) { 
	  model.addAttribute("pages", pagerepo.findAll());
	  return "admin/page/page-list"; 
	  }
	 
	  @RequestMapping(value = "edit-page", method = RequestMethod.GET) 
	  public String editUser(@RequestParam Integer id,Model model) { 
		  Page page = pagerepo.findById(id).get(); 
		  model.addAttribute("page", page);
		  System.out.println("id is :: "+ id);
		  return "admin/page/edit-page"; 
		  }
	  
	  @RequestMapping(value = "edit-page", method = RequestMethod.POST) 
	  public String addUserPage(@ModelAttribute Page  page,Model model) {
	  System.out.println(page);
	  pagerepo.save(page);
	  model.addAttribute("page", page);
	  return "redirect:/pages"; 
	  }
	
	  
}
