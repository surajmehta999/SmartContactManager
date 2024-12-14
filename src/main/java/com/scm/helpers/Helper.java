package com.scm.helpers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import com.scm.config.OAuthAuthenticationSuccessHandler;


public class Helper {
    public static String getEmailOfLoggedInUser(Authentication authentication)
    {
        Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);
        //agr humne email id and password se login kiya hai to email kaise nikalenge
        if(authentication instanceof OAuth2AuthenticationToken)
        {
            var oauth2AuthenticationToken =  (OAuth2AuthenticationToken) authentication;
            var clientId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();

            var oauthUser = (DefaultOAuth2User)authentication.getPrincipal();

            //sign with google
            if(clientId.equalsIgnoreCase("google"))
           { 
                // oauthUser.getAttributes().forEach((key,value)->{
                //     logger.info(key+" : "+value);
                // });
                //System.out.println("Getting email from google");
                return oauthUser.getAttribute("email").toString();
           }
           //sing in with github
           else if(clientId.equalsIgnoreCase("github"))
           {
            String email = oauthUser.getAttribute("email") != null ? oauthUser.getAttribute("email").toString() :oauthUser.getAttribute("login").toString()+"@gmail.com";
                // oauthUser.getAttributes().forEach((key,value)->{
                //     logger.info(key+" : "+value);
                // });
                //System.out.println("Getting email from github");
                return email;
           }
            
        }
        else {
            //manual registration
            System.out.println("Getting data from local database");
            return authentication.getName();
        }
        return "";
    }
}
