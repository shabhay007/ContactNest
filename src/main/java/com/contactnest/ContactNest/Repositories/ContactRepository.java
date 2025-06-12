package com.contactnest.ContactNest.Repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.contactnest.ContactNest.Entities.Contact;
import com.contactnest.ContactNest.Entities.User;





@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {

    // custom finder method
    // find contact by use
    // since we have user field in contact class, so we will get it's implementation by default
    // List<Contact> findByUser(User user);
    Page<Contact> findByUser(User user, Pageable pageable); // for pagination


    // custom query method
    // find contact by user id
    // but there is no field named userId, so we have to write it's implementation/query
    @Query("SELECT c FROM Contact c WHERE c.user.id = :userId")
    List<Contact> findByUserId(@Param("userId") String userId);


    // for search name
    Page<Contact> findByUserAndNameContaining(User user, String namekeyword, Pageable pageable);

    Page<Contact> findByUserAndEmailContaining(User user, String emailkeyword, Pageable pageable);

    Page<Contact> findByUserAndPhoneNumberContaining(User user, String phonekeyword, Pageable pageable);

}
