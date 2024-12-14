package com.scm.controller;
import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.entities.User;
import com.scm.helpers.Helper;
import com.scm.services.UserService;


@Controller
@RequestMapping("/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;


    //user dashboard Page
    @RequestMapping(value = "/dashboard")
    public String userDashboard() {
        return "user/dashboard";
    }
    
     //user profile Page
     @RequestMapping(value = "/profile")
     public String userProfile(Model model, Authentication authentication) {
        /* 
        System.out.println("User Profile");
        String username = Helper.getEmailOfLoggedInUser(authentication);
        //logger.info("User logged in: {}",username);

        //Fetch the data from db : email, name, address
        User user = userService.getUserByEmail(username);

        System.out.println(user.getEmail());
        System.out.println(user.getName());
        System.out.println(user.getPhoneNumber());

        //data ko frontend ke html mein send krna
        model.addAttribute("loggedInUser", user);
        */
         return "user/profile";
     }
    //user add contact page

    //user view contacts

    //user edit contact

    //user delete contact

    //user search contact
    
}
