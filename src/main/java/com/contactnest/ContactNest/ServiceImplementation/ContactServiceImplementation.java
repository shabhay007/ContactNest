package com.contactnest.ContactNest.ServiceImplementation;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.contactnest.ContactNest.Entities.Contact;
import com.contactnest.ContactNest.Entities.User;
import com.contactnest.ContactNest.Helpers.ResourceNotFoundException;
import com.contactnest.ContactNest.Repositories.ContactRepository;
import com.contactnest.ContactNest.Services.ContactService;


@Service
public class ContactServiceImplementation implements ContactService {

    @Autowired
    private ContactRepository contactRepo;

    // saving the contact
    @Override
    public Contact save(Contact contact) {
        String contactId = UUID.randomUUID().toString();
        contact.setId(contactId);

        return contactRepo.save(contact);
    }


    // updating the contact
    @Override
    public Contact update(Contact contact) {

        var contactOld = contactRepo.findById(contact.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found"));
        contactOld.setName(contact.getName());
        contactOld.setEmail(contact.getEmail());
        contactOld.setPhoneNumber(contact.getPhoneNumber());
        contactOld.setAddress(contact.getAddress());
        contactOld.setDescription(contact.getDescription());
        contactOld.setPicture(contact.getPicture());
        contactOld.setFavourite(contact.isFavourite());
        contactOld.setWebsiteLink(contact.getWebsiteLink());
        contactOld.setLinkedinLink(contact.getLinkedinLink());
        contactOld.setCloudinaryImagePublicId(contact.getCloudinaryImagePublicId());

        return contactRepo.save(contactOld);
    }


    // view contact
    @Override
    public List<Contact> getAll() {
        return contactRepo.findAll();
    }


    // get contact by Id
    @Override
    public Contact getById(String id) {
        return contactRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with given id " + id));
    }


    // delete contact by Id
    @Override
    public void delete(String id) {
        var contact = contactRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with given id " + id));

        contactRepo.delete(contact);

    }

    

    // get all contacts of a uses
    @Override
    public List<Contact> getByUserId(String userId) {
        return contactRepo.findByUserId(userId);
    }


    // for pagination of all the contacts
    @Override
    public Page<Contact> getByUser(User user, int page, int size, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        
        return contactRepo.findByUser(user, pageable);
    }


    // ******** searching a contact by name/email/phoneNumber and pagination ********
    @Override
    public Page<Contact> searchByName(String nameKeyword, int size, int page, String sortBy, String order, User user) {

        Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByUserAndNameContaining(user, nameKeyword, pageable);
    }

    @Override
    public Page<Contact> searchByEmail(String emailKeyword, int size, int page, String sortBy, String order,
            User user) {
        Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByUserAndEmailContaining(user, emailKeyword, pageable);
    }

    @Override
    public Page<Contact> searchByPhoneNumber(String phoneNumberKeyword, int size, int page, String sortBy,
            String order, User user) {

        Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByUserAndPhoneNumberContaining(user, phoneNumberKeyword, pageable);
    }

}
