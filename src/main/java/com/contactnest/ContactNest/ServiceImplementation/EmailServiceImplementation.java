package com.contactnest.ContactNest.ServiceImplementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.contactnest.ContactNest.Services.EmailService;


@Service
public class EmailServiceImplementation implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.properties.domainName}")
    private String domainName;

    @Override
    public void sendEmail(String to, String subject, String body) {

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(body);
        mail.setFrom(domainName);
        
        // now send the mail
        javaMailSender.send(mail);
        
    }

}
