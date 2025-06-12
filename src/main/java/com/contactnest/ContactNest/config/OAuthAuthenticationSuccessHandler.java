package com.contactnest.ContactNest.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.contactnest.ContactNest.Entities.Providers;
import com.contactnest.ContactNest.Entities.User;
import com.contactnest.ContactNest.Helpers.AppConstants;
import com.contactnest.ContactNest.Repositories.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    @Autowired
    UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        

                logger.info("OAuthAuthenticationSuccessHandler");

                // identifying the provider
                var oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
                String authorizedClientRegistrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();

                logger.info(authorizedClientRegistrationId);



                var oauthUser = (DefaultOAuth2User) authentication.getPrincipal();

                oauthUser.getAttributes().forEach((key, value) -> {
                    logger.info(key + " : " + value);
                });

                User user = new User();
                user.setUserId(UUID.randomUUID().toString());
                user.setRolesList(List.of(AppConstants.ROLE_USER));
                user.setEmailVerified(true);
                user.setEnabled(true);
                user.setPassword("dummy");

                if (authorizedClientRegistrationId.equalsIgnoreCase("google")) {

                    // google
                    // google attributes

                    user.setEmail(oauthUser.getAttribute("email").toString());
                    user.setProfilePicLink(oauthUser.getAttribute("picture").toString());
                    user.setName(oauthUser.getAttribute("name").toString());
                    user.setProviderUserId(oauthUser.getName());
                    user.setProvider(Providers.GOOGLE);
                    user.setAbout("This account is created using google.");

                } else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {

                    // github
                    // github attributes
                    String email = oauthUser.getAttribute("email") != null ? oauthUser.getAttribute("email").toString()
                            : oauthUser.getAttribute("login").toString() + "@gmail.com";
                    String picture = oauthUser.getAttribute("avatar_url").toString();
                    String name = oauthUser.getAttribute("login").toString();
                    String providerUserId = oauthUser.getName();

                    user.setEmail(email);
                    user.setProfilePicLink(picture);
                    user.setName(name);
                    user.setProviderUserId(providerUserId);
                    user.setProvider(Providers.GITHUB);

                    user.setAbout("This account is created using github");
                }

                else if (authorizedClientRegistrationId.equalsIgnoreCase("linkedin")) {

                }

                else {
                    logger.info("OAuthAuthenicationSuccessHandler: Unknown provider");
                }








                // fetching all data

                // DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
                // logger.info(user.getName());

                // // iterate in map to fetch all data
                // user.getAttributes().forEach((key, value) -> {
                //     logger.info("{} => {}", key, value);
                // });

                // logger.info(user.getAuthorities().toString());

                // // saving the data
                // String name = user.getAttribute("name");
                // String email = user.getAttribute("email");
                // String profilePic = user.getAttribute("profilePic");

                // // creating a user to save in database
                // User user1 = new User();
                // user1.setName(name);
                // user1.setEmail(email);
                // user1.setProfilePicLink(profilePic);
                // user1.setPassword("password");
                // user1.setUserId(UUID.randomUUID().toString());
                // user1.setProvider(Providers.GOOGLE);
                // user1.setEnabled(true);
                // user1.setEmailVerified(true);
                // user1.setProviderUserId(user.getName());
                // user1.setRolesList(List.of(AppConstants.ROLE_USER));
                // user1.setAbout("This profile is created using Google");


                // if user already exists, it will return null else get saved into database
                User user2 = userRepository.findByEmail(user.getEmail()).orElse(null);

                if(user2 == null){
                    userRepository.save(user);
                    logger.info("User saved successfully : " + user.getEmail());
                }

                new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
    }

}
