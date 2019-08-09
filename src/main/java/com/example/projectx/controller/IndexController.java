package com.example.projectx.controller;

import java.security.Principal;

import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.projectx.model.AppUser;
import com.example.projectx.repository.UserRepository;
import com.example.projectx.utils.WebUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

@Controller
public class IndexController {
	
	@Autowired
	private UserRepository userrepo;

	
	@RequestMapping(path = "register", method = RequestMethod.GET)
	public String register(Model model) {
		return "registration";
	}
		
	@RequestMapping(value = { "/"}, method = RequestMethod.GET)
    public String indexPage(Model model) {
        model.addAttribute("title", "Welcome");
        model.addAttribute("message", "This is welcome page!");
        return "index";
    }
	
	@RequestMapping(value = { "/welcome" }, method = RequestMethod.GET)
    public String welcomePage(Model model) {
        model.addAttribute("title", "Welcome");
        model.addAttribute("message", "This is welcome page!");
        return "welcome";
    }
 
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage(Model model, Principal principal) {
         
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
 
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);
        
         
        return "/admin/adminpage";
    }
    @RequestMapping(value = "/editor", method = RequestMethod.GET)
    public String editorPage(Model model, Principal principal) {
         
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
 
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userinfo", userInfo);
     
         
        return "/editor/editorPage";
    }
    
 
 
    @RequestMapping(value="403", method = RequestMethod.GET)
	public ModelAndView error() {
	    ModelAndView mav = new ModelAndView();
	    String errorMessage= "You are not authorized for the requested data.";
	    mav.addObject("errorMsg", errorMessage);
	    mav.setViewName("403");
	    return mav;
        }
    
    //testing purpose only
    
    @RequestMapping(value = "add-user", method = RequestMethod.GET)
    public String addUser(Model model) {
    	model.addAttribute("user", new AppUser());
        return "admin/user/user-form";
    }
    @RequestMapping(value = "add-user", method = RequestMethod.POST)
    public String addUser(@ModelAttribute AppUser user,Model model) {
    	userrepo.save(user);
    	model.addAttribute("users", userrepo.findAll());
        return "admin/user/user-list";
    }
    
    
	
	  @RequestMapping(value = "user", method = RequestMethod.GET) 
	  public String userPage(Model model) { 
	  model.addAttribute("users", userrepo.findAll());
	  return "admin/user/user-list"; }
	 
    
    
    @RequestMapping(value = "edit-user", method = RequestMethod.GET)
    public String editUser(@RequestParam String uname, Model model) {
    	AppUser user = userrepo.findById(uname).get();
		model.addAttribute("user", user);
		return "admin/user/user-form";
    }
    
    @RequestMapping(value = "edit-user", method = RequestMethod.POST)
    public String addUserPage(@ModelAttribute AppUser user) {
    	System.out.println(user);
    	userrepo.save(user);
        return "redirect:/user";
    }
    
    @RequestMapping(value = "delete-user", method = RequestMethod.GET)
    public String delUserPage(@RequestParam String uname,Model model) {
    	userrepo.deleteById(uname);
    	model.addAttribute("users", userrepo.findAll());
    	System.out.println(uname);
        return "admin/user/user-list";
    }
    
   
    
    
    
}
