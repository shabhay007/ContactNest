package com.contactnest.ContactNest.Helpers;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;


@Component
public class SessionHelper {

    // this will remove message from session when we refresh the page
    // so that the alert get removed from backend

    public static void removeMessage(){
        try {
            System.out.println("Removing message from session...");
            
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            session.removeAttribute("message");
            
        } catch (Exception e) {
            System.out.println("Error in session helper: " + e);
            e.printStackTrace();
        }
    }

}
