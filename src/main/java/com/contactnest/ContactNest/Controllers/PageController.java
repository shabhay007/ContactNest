package com.contactnest.ContactNest.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.contactnest.ContactNest.Entities.User;
import com.contactnest.ContactNest.Forms.UserForm;
import com.contactnest.ContactNest.Helpers.Message;
import com.contactnest.ContactNest.Helpers.MessageType;
import com.contactnest.ContactNest.Services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class PageController {

    @Autowired
    private UserService userService;



    @GetMapping("/")
    public String index(){
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String home(Model model){
        System.out.println("Redirecting to home page.");

        // sending data to view
        model.addAttribute("name", "Substring Technologies.");
        model.addAttribute("work", "Learning Technologies.");
        model.addAttribute("interests", "Other Technologies.");

        return "home";
    }

    @RequestMapping("/about")
    public String about(){
        return "about";
    }


    @RequestMapping("/base")
    public String base(){
        return "base";
    }


    @RequestMapping("/services")
    public String services(){
        return "services";
    }


    @RequestMapping("/contact")
    public String contact(){
        return "contact";
    }


    // login page
    @RequestMapping("/login")
    public String login(){
        return "login";
    }


    // registration/signup page
    @RequestMapping("/signup")
    public String signup(Model model){
        UserForm userForm = new UserForm();
        // we can send default value from here

        model.addAttribute("userForm", userForm);

        return "signup";
    }



    

    /* 

        ********** Always keep BindingResult right after @Valid @ModelAttribute *************

        order should be this i.e. BindingResult should come before HttpSession, else gives error
        (@Valid @ModelAttribute UserForm userForm, BindingResult bindingResult, HttpSession session)


        I encountered error, when used this
        (@Valid @ModelAttribute UserForm userForm, HttpSession session, BindingResult bindingResult)
    
    */

    // processing registration/signup
    @RequestMapping(value = "/do-register", method = RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult bindingResult, HttpSession session){
        System.out.println("Processing registration.");

        // fetch form data
        System.out.println(userForm);


        // validate form data (used @Valid and BindingResult)
        if(bindingResult.hasErrors()){
            return "signup";
        }


        // save to database
        // User user = User.builder()
        //         .name(userForm.getName())
        //         .email(userForm.getEmail())
        //         .phoneNumber(userForm.getPhoneNumber())
        //         .password(userForm.getPassword())
        //         .about(userForm.getAbout())
        //         .profilePicLink("https://drive.google.com/file/d/1WAihHMQwoYkqRdY5mLwTuPZKMX2kzNuG/view?usp=sharing")
        //         .build();

        User existingUser = userService.getUserByEmail(userForm.getEmail());

        if(existingUser != null){
            session.setAttribute("message",
                Message.builder()
                .content("User already exists with this mail!!")
                .type(MessageType.danger)
                .build()
            );

            return "redirect:/signup";
        }

        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setProfilePicLink("https://drive.google.com/file/d/1WAihHMQwoYkqRdY5mLwTuPZKMX2kzNuG/view?usp=sharing");


        // user.setProvider(Providers.SELF);
        // user.setEnabled(false);
        // user.setEmailVerified(false);
        // user.setPhoneVerified(false);

        userService.saveUser(user);
        System.out.println("User Saved successfully.");

        // message: "Registration successfull"
        // using HttpSession
        Message message = Message.builder().content("Registration Successful!! Now you can login.").type(MessageType.success).build();
        session.setAttribute("message", message);


        // redirect to login page

        return "redirect:/login";
    }

}
