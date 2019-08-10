package com.example.projectx.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


public class CustomSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler  {

   // private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    
        public CustomSuccessHandler(String defaultTargetUrl) {
            setDefaultTargetUrl(defaultTargetUrl);
            setUseReferer(true);
        }

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException 
        {
            HttpSession session = request.getSession();
            
           
            String referer = request.getRequestURL().toString();
            String targetUrl = determineTargetUrl(session, authentication);
            
            
            
            System.out.println("Request Url="+referer);
            
            System.out.println("Target Url="+targetUrl);
            
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
            
            
        }


    /*
     * This method extracts the roles of currently logged-in user and returns
     * appropriate URL according to his/her role.
     */
    protected String determineTargetUrl(HttpSession session, Authentication authentication) {
        String url = "";

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        List<String> roles = new ArrayList<String>();
        
        String redirectUrl = null;
        
        if (session != null) 
        {
            redirectUrl = (String) session.getAttribute("url_prior_login");
            if (redirectUrl != null) 
            {
                // we do not forget to clean this attribute from session
                session.removeAttribute("url_prior_login");     
                
            } 
            
        }

        for (GrantedAuthority a : authorities) {
            roles.add(a.getAuthority());
        }

        if (isAuthor(roles)) 
        {
        	if (redirectUrl != null)
        		url = redirectUrl;
        	else
        		url = "/author";
        } else if (isAdmin(roles)) {
            url = "/admin";
        } else if (isEditor(roles)) {
            url = "/editor";
        } else {
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
