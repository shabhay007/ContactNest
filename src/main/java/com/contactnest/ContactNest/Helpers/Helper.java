package com.contactnest.ContactNest.Helpers;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;


@Component
public class Helper {

    @Value("${server.baseUrl}")
    private String baseURL;

    public static String getEmailOfLoggedInUser(Authentication authentication){

        // Principal principal = (Principal) authentication.getPrincipal();

        // in case of logging in using email and password
        if(authentication instanceof OAuth2AuthenticationToken){

            var oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
            var clientId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

            var oauth2User = (OAuth2User) authentication.getPrincipal();
            String username = "";

            // in case of google login
            if(clientId.equalsIgnoreCase("google")){
                username = oauth2User.getAttribute("email").toString();
                
                System.out.println("Getting email from google");
            }

            // in case of github login
            else if(clientId.equalsIgnoreCase("github")){ 
                username = oauth2User.getAttribute("email") != null ? oauth2User.getAttribute("email").toString()
                            : oauth2User.getAttribute("login").toString() + "@gmail.com";

                System.out.println("Getting email from github");
            }

            return username;

        }
        else{
            System.out.println("Getting data from local database");
            return authentication.getName();
        }

    }


    public String getEmailVerificationLink(String emailToken){

        String link = this.baseURL + "/auth/verify-email?token=" + emailToken;

        return link;
        
    }


}
