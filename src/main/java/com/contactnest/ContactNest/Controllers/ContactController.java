package com.contactnest.ContactNest.Controllers;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.contactnest.ContactNest.Entities.Contact;
import com.contactnest.ContactNest.Entities.User;
import com.contactnest.ContactNest.Forms.ContactForm;
import com.contactnest.ContactNest.Forms.ContactSearchForm;
import com.contactnest.ContactNest.Helpers.AppConstants;
import com.contactnest.ContactNest.Helpers.Helper;
import com.contactnest.ContactNest.Helpers.Message;
import com.contactnest.ContactNest.Helpers.MessageType;
import com.contactnest.ContactNest.Services.ContactService;
import com.contactnest.ContactNest.Services.ImageService;
import com.contactnest.ContactNest.Services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;



@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private ImageService imageService;



    @RequestMapping("/add")
    public String addContactView(Model model){
        ContactForm contactForm = new ContactForm();
        model.addAttribute("contactForm", contactForm);

        return "user/addContact";
    }


    // adding contact
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult result,
            Authentication authentication, HttpSession session) {

        // process the form data

        // 1. validating form
        if(result.hasErrors()){
            // for debugging -> finding errors
            result.getAllErrors().forEach(error -> logger.info(error.toString()));

            session.setAttribute("message", Message.builder()
            .content("Correct the following errors")
            .type(MessageType.danger)
            .build());

            return "user/addContact";
        }


        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);


        // 2 process the contact picture
        // image process
        logger.info("file info {}", contactForm.getContactImage().getOriginalFilename());


        // 3. setting the details to save
        Contact contact = new Contact();
        contact.setName(contactForm.getName());
        contact.setFavourite(contactForm.isFavourite());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setUser(user);
        contact.setLinkedinLink(contactForm.getLinkedinLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        

        // some part of processing of step 2 & 3
        // 2. image processing
        // 3 set the contact picture url
        if (contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()) {
            String filename = UUID.randomUUID().toString();
            String fileURL = imageService.uploadImage(contactForm.getContactImage(), filename);
            contact.setPicture(fileURL);
            contact.setCloudinaryImagePublicId(filename);

        }

        contactService.save(contact);
        System.out.println(contactForm);


        // 4 set message to be displayed on the view
        session.setAttribute("message", 
                Message.builder()
                    .content("Contact added successfully")
                    .type(MessageType.success)
                    .build());

        return "redirect:/user/contacts/add";

    }



    // listing of all contacts of a user
    @RequestMapping()
    public String viewContact(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
        @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
        @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection,
        Model model, Authentication authentication){
            
        String userName = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(userName);
        
        Page<Contact> contacts = contactService.getByUser(user, page, size, sortBy, sortDirection);
        model.addAttribute("pageContact", contacts);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

        // for search form
        model.addAttribute("contactSearchForm", new ContactSearchForm());

        return "user/contacts";
    }



    // search method
    @RequestMapping("/search")
    public String searchHandler(
            @ModelAttribute ContactSearchForm contactSearchForm,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            Model model,
            Authentication authentication, HttpSession session) {

        logger.info("field {} keyword {}", contactSearchForm.getField(), contactSearchForm.getValue());

        // checking for null or empty field
        if(contactSearchForm.getField() == null || contactSearchForm.getField().isEmpty()){
            session.setAttribute("message", 
                Message.builder()
                .content("Select Field cannot be empty!!")
                .type(MessageType.danger)
                .build()
            );


            return "redirect:/user/contacts";
        }

        var user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));

        Page<Contact> pageContact = null;

        if (contactSearchForm.getField().equalsIgnoreCase("name")) {
            pageContact = contactService.searchByName(contactSearchForm.getValue(), size, page, sortBy, direction,
                    user);
        } 
        else if (contactSearchForm.getField().equalsIgnoreCase("email")) {
            pageContact = contactService.searchByEmail(contactSearchForm.getValue(), size, page, sortBy, direction,
                    user);
        } 
        else if (contactSearchForm.getField().equalsIgnoreCase("phone")) {
            pageContact = contactService.searchByPhoneNumber(contactSearchForm.getValue(), size, page, sortBy,
                    direction, user);
        }


        logger.info("pageContact {}", pageContact);

        model.addAttribute("contactSearchForm", contactSearchForm);

        model.addAttribute("pageContact", pageContact);

        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

        return "user/search";
    }



    // delete contact
    @RequestMapping("/delete/{id}")
    public String deleteContact(@PathVariable String id, HttpSession session){
        contactService.delete(id);
        logger.info("contactId {} deleted ", id);

        // adding message to session
        session.setAttribute("message", 
                Message.builder()
                .content("Contact Deleted successfully")
                .type(MessageType.success)
                .build()
                );

        return "redirect:/user/contacts";
    }



    // update contact form view
    @GetMapping("/view/{id}")
    public String updateContactFormView(@PathVariable String id, Model model){

        // fetching contact from contact id
        var contact = contactService.getById(id);

        // then populating form data to update
        ContactForm contactForm = new ContactForm();
        contactForm.setName(contact.getName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setAddress(contact.getAddress());
        contactForm.setDescription(contact.getDescription());
        contactForm.setWebsiteLink(contact.getWebsiteLink());
        contactForm.setLinkedinLink(contact.getLinkedinLink());
        contactForm.setPicture(contact.getPicture());
        contactForm.setFavourite(contact.isFavourite());


        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId", contact.getId());


        return "user/updateContactView";
    }



    @RequestMapping(value = "/update/{contactId}", method = RequestMethod.POST)
    public String updateContact(@PathVariable("contactId") String contactId,
            @Valid @ModelAttribute ContactForm contactForm,
            BindingResult bindingResult,
            Model model, HttpSession session) {

        // update the contact
        if (bindingResult.hasErrors()) {
            session.setAttribute("message", 
                Message.builder()
                .content("Please correct the following errors")
                .type(MessageType.danger)
                .build()
            );

            return "user/updateContactView";
        }

        var con = contactService.getById(contactId);
        con.setId(contactId);
        con.setName(contactForm.getName());
        con.setEmail(contactForm.getEmail());
        con.setPhoneNumber(contactForm.getPhoneNumber());
        con.setAddress(contactForm.getAddress());
        con.setDescription(contactForm.getDescription());
        con.setFavourite(contactForm.isFavourite());
        con.setWebsiteLink(contactForm.getWebsiteLink());
        con.setLinkedinLink(contactForm.getLinkedinLink());

        // processing image
        if (contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()) {
            logger.info("file is not empty");

            String fileName = UUID.randomUUID().toString();
            String imageUrl = imageService.uploadImage(contactForm.getContactImage(), fileName);
            con.setCloudinaryImagePublicId(fileName);
            con.setPicture(imageUrl);
            contactForm.setPicture(imageUrl);

        } else {
            logger.info("file is empty");
        }

        var updateCon = contactService.update(con);
        logger.info("updated contact {}", updateCon);

        model.addAttribute("message", 
            Message.builder()
            .content("Contact Updated !!")
            .type(MessageType.success)
            .build()
        );

        // return "redirect:/user/contacts/view/" + contactId;
        return "redirect:/user/contacts";
    }


}
