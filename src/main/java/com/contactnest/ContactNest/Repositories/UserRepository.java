package com.contactnest.ContactNest.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.contactnest.ContactNest.Entities.User;

public interface UserRepository extends JpaRepository<User, String> {
    // extra methods related to db operation
    // custom query methods
    // custom finder methods


    // we don't have to implement it as implementation will be provided by Spring Data JPA
    // we need not to do anything apart from declaration
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndPassword(String email, String password);
    Optional<User> findByEmailToken(String id);
}
