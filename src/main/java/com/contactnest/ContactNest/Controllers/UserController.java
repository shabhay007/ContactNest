package com.contactnest.ContactNest.Controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.contactnest.ContactNest.Services.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    // user dashboard page
    @RequestMapping("/dashboard")
    public String userDashboard(){
        return "user/dashboard";
    }


    

    // user profile page
    @RequestMapping("/profile")
    public String userProfile(Model model, Authentication authentication){
        // fething data from database

        return "user/profile";
    }

    // user add contact page

    // user view contact page

    // user edit contact page

    // user delete contact page

    // user search contact page

}
