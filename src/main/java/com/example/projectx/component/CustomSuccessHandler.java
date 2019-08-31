package com.example.projectx.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import com.example.projectx.service.UserDetailsServiceImpl;


public class CustomSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler  {

	@Autowired
	private UserDetailsServiceImpl userInfoService;
    
        public CustomSuccessHandler(String defaultTargetUrl) {
            setDefaultTargetUrl(defaultTargetUrl);
            setUseReferer(true);
        }

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException 
        {
           
            String targetUrl = determineTargetUrl(authentication);
            
            Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
            String username = loggedInUser.getName();
            
            System.out.println("LoggedIn User: "+username);
            
            
            HttpSession session = request.getSession();
            session.setAttribute("userInfo", userInfoService.loadUserByUsername(username));      
            
            //authentication.getPrincipal().getUser().getFullName()
          
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
            
            
        }


    /*
     * This method extracts the roles of currently logged-in user and returns
     * appropriate URL according to his/her role.
     */
    protected String determineTargetUrl(Authentication authentication) {
        String url = "";

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        List<String> roles = new ArrayList<String>();
        
      //  String redirectUrl = null;
        
        for (GrantedAuthority a : authorities) {
            roles.add(a.getAuthority());
        }

        if (isAdmin(roles)) {
            url = "/admin/user";
        } else if (isEditor(roles)) {
            url = "/editor";
        }
        else if (isAuthor(roles)) {
            url = "/author";
        }
        else {
            url = "/";
        }

        return url;
    }

    private boolean isEditor(List<String> roles) {
    	if (roles.contains("ROLE_EDITOR")) {
            return true;
        }
		return false;
	}

	private boolean isAuthor(List<String> roles) {
        if (roles.contains("ROLE_AUTHOR")) {
            return true;
        }
        return false;
    }

    private boolean isAdmin(List<String> roles) {
        if (roles.contains("ROLE_ADMIN")) {
            return true;
        }
        return false;
    }
}
