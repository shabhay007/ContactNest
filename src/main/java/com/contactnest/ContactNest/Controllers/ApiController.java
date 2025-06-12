package com.contactnest.ContactNest.Controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.contactnest.ContactNest.Entities.Contact;
import com.contactnest.ContactNest.Services.ContactService;




@RestController
@RequestMapping("/api")
public class ApiController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ContactService contactService;


    @GetMapping("/contacts/{contactId}")
    public Contact getContact(@PathVariable String contactId){
        logger.info("contactId {}", contactId);
        
        return contactService.getById(contactId);
    }

}
