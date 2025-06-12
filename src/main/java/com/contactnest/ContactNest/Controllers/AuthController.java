package com.contactnest.ContactNest.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.contactnest.ContactNest.Entities.User;
import com.contactnest.ContactNest.Helpers.Message;
import com.contactnest.ContactNest.Helpers.MessageType;
import com.contactnest.ContactNest.Repositories.UserRepository;

import jakarta.servlet.http.HttpSession;



// For Email Verification

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token, HttpSession session){
        User user = userRepository.findByEmailToken(token).orElse(null);

        if(user != null){
            
            if(user.getEmailToken().equals(token)){
                user.setEmailVerified(true);
                user.setEnabled(true);
                userRepository.save(user);

                session.setAttribute("message",
                    Message.builder()
                    .content("Email verified!! Now you can login.")
                    .type(MessageType.success)
                    .build()
                );

                return "successPage";
            }

            session.setAttribute("message",
                Message.builder()
                .content("Email not verified!! Please verify using verification link.")
                .type(MessageType.danger)
                .build()
            );

            return "errorPage";
        }

        session.setAttribute("message",
            Message.builder()
            .content("Email not verified!! Please verify using verification link.")
            .type(MessageType.danger)
            .build()
        );

        return "errorPage";
    }

}
