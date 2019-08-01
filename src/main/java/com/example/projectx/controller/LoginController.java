package com.example.projectx.controller;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.example.projectx.model.MyUserDetails;
import org.springframework.security.core.userdetails.User;
import com.example.projectx.utils.WebUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.Principal;

import javax.servlet.http.HttpSession;

//@SessionAttributes({"currentUser"})
@Controller
public class LoginController {
    private static final Logger log = LogManager.getLogger(LoginController.class);
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }
    @RequestMapping(value = "/loginFailed", method = RequestMethod.GET)
    public String loginError(Model model) {
        log.info("Login attempt failed");
        model.addAttribute("error", "true");
        return "login";
    }
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(SessionStatus session) {
        SecurityContextHolder.getContext().setAuthentication(null);
        session.setComplete();
        return "redirect:/index";
    }
    @RequestMapping(value = "/doLogin", method = RequestMethod.POST)
    public String postLogin(Model model,  Principal principal) {
        log.info("doLogin()");
        
     // After user login successfully.
        String userName = principal.getName();
 
        System.out.println("User Name: " + userName);
 
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
 
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);
        System.out.println("thia is user info:" +userInfo);
        System.out.println("user info found");
//        // read principal out of security context and set it to session
//        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
//        validatePrinciple(authentication.getPrincipal());
//        User loggedInUser = ((MyUserDetails) authentication.getPrincipal()).getUserDetails();
//        model.addAttribute("currentUser", loggedInUser.getUsername());
//        session.setAttribute("userId", loggedInUser.getId());
        return "redirect:/index";
    }
//    private void validatePrinciple(Object principal) {
//        if (!(principal instanceof MyUserDetails)) {
//            throw new  IllegalArgumentException("Principal can not be null!");
//        }
//    }
}
