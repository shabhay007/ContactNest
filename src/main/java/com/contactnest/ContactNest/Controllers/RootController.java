package com.contactnest.ContactNest.Controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.contactnest.ContactNest.Entities.User;
import com.contactnest.ContactNest.Helpers.Helper;
import com.contactnest.ContactNest.Services.UserService;



@ControllerAdvice
public class RootController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    // adding logged in users information
    @ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authentication){
        if(authentication == null){
            return;
        }
        
        String username = Helper.getEmailOfLoggedInUser(authentication);

        logger.info("User logged in : {}", username);

        User user = userService.getUserByEmail(username);
        System.out.println("User : " + user);
        System.out.println("Name : " + user.getName());
        System.out.println("Email : " + user.getEmail());
        
        model.addAttribute("loggedInUser", user);
    }

}
