package com.example.projectx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {

	@RequestMapping(path = "/", method = RequestMethod.GET)
	public String students(Model model) {
		return "index";
	}
	@RequestMapping(path = "register", method = RequestMethod.GET)
	public String register(Model model) {
		return "registration";
	}
	@RequestMapping(path = "login", method = RequestMethod.GET)
	public String login(Model model) {
		return "login";
	}	
}
