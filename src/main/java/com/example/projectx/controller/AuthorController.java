package com.example.projectx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthorController {
	
	
	@RequestMapping(path = "/author", method = RequestMethod.GET)
	public String author(Model model) {
		return "author/authorindex";
	}

}
