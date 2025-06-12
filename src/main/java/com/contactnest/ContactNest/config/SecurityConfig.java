package com.contactnest.ContactNest.config;

// import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.AuthenticationException;
// import org.springframework.security.core.userdetails.User;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.AuthenticationFailureHandler;
// import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.contactnest.ContactNest.Services.SecurityCustomUserDetailService;

// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

    // 1st method, setting user in application.properties
    // 2st method, but it is in memory and not from database


    // @Bean
    // public UserDetailsService userDetailsService(){
    //     // here we are doing password encoding
    //     // and this method is only for testing and not for production
    //     UserDetails user1 = User
    //         .withDefaultPasswordEncoder()
    //         .username("admin")
    //         .password("admin")
    //         .roles("ADMIN", "USER")
    //         .build();


    //     // here we are not doing any password encoding
    //     // so it will throw an error upon login
    //     UserDetails user2 = User
    //         .withUsername("user")
    //         .password("user")
    //         // .roles("ADMIN", "USER")
    //         .build();
        
    //     // UserDetailsService inMemoryUserDetailsManager = new InMemoryUserDetailsManager(user1, user2);
    //     return new InMemoryUserDetailsManager(user1, user2);
        
    //     // return inMemoryUserDetailsManager;
    // }




    // 3rd method

    @Autowired
    private SecurityCustomUserDetailService userDetailService;

    @Autowired
    private OAuthAuthenticationSuccessHandler oauthHandler;

    @Autowired
    private AuthFaliureHandler authFaliureHandler;


    // configuration for authentication provider for spring security
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        // user detail service ka object
        daoAuthenticationProvider.setUserDetailsService(userDetailService);

        // password encoder ka object
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        // configurations


        // configured url's that which will be public or private
        httpSecurity.authorizeHttpRequests(authorize -> {
            // authorize.requestMatchers("/home", "/signup", "/services").permitAll();

            // url starts with /user will be secured
            authorize.requestMatchers("/user/**").authenticated();

            // rest will be accessible
            authorize.anyRequest().permitAll();
        });


        // default form login
        // httpSecurity.formLogin(Customizer.withDefaults());

        // without default login
        httpSecurity.formLogin(formLogin -> {

            formLogin.loginPage("/login");
            formLogin.loginProcessingUrl("/authenticate");
            formLogin.successForwardUrl("/user/dashboard");
            formLogin.failureForwardUrl("/login?error=true");
            formLogin.usernameParameter("email");
            formLogin.passwordParameter("password");

            // handler
            // formLogin.failureHandler(new AuthenticationFailureHandler() {

            //     @Override
            //     public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            //             AuthenticationException exception) throws IOException, ServletException {

            //         throw new UnsupportedOperationException("Unimplemented method 'onAuthenticationFailure'");
            //     }
                
            // });

            // formLogin.successHandler(new AuthenticationSuccessHandler() {

            //     @Override
            //     public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            //             Authentication authentication) throws IOException, ServletException {

            //         throw new UnsupportedOperationException("Unimplemented method 'onAuthenticationSuccess'");
            //     }
                
            // });


            formLogin.failureHandler(authFaliureHandler);

        });


        // by default csrf is enabled by Spring Security
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        // handling logout
        httpSecurity.logout(logoutForm -> {
            logoutForm.logoutUrl("/do-logout");
            logoutForm.logoutSuccessUrl("/login?logout=true");
        });


        // oauth configuration
        httpSecurity.oauth2Login(oauth -> {
            oauth.loginPage("/login");
            oauth.successHandler(oauthHandler);
        });

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
