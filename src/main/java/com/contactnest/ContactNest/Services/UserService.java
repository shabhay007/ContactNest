package com.contactnest.ContactNest.Services;

import java.util.List;
import java.util.Optional;

import com.contactnest.ContactNest.Entities.User;

public interface UserService {

    User saveUser(User user);

    // Optional is a class which tells if a  user exists of not
    Optional<User> getUserById(String id);

    Optional<User> updateUser(User user);

    void deleteUser(String id);

    boolean isUserExists(String id);

    boolean isUserExistsByEmail(String email);

    List<User> getAllUser();

    User getUserByEmail(String email);

}
