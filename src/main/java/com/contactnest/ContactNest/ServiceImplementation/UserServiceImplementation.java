package com.contactnest.ContactNest.ServiceImplementation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.contactnest.ContactNest.Entities.User;
import com.contactnest.ContactNest.Helpers.AppConstants;
import com.contactnest.ContactNest.Helpers.Helper;
import com.contactnest.ContactNest.Helpers.ResourceNotFoundException;
import com.contactnest.ContactNest.Repositories.UserRepository;
import com.contactnest.ContactNest.Services.EmailService;
import com.contactnest.ContactNest.Services.UserService;

@Service
public class UserServiceImplementation implements UserService {

    // recommended to use construction injection and not property injection which is done here
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private Helper helper;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public User saveUser(User user) {
        // we have to generate user id
        System.out.println("User service to save is running.");
        String userId = UUID.randomUUID().toString();
        user.setUserId(userId);

        // encoding password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // setting the user role
        user.setRolesList(List.of(AppConstants.ROLE_USER));

        logger.info(user.getProvider().toString());

        
        // generating email token
        String emailToken = UUID.randomUUID().toString();
        user.setEmailToken(emailToken);

        // saving the user with user details
        User savedUser = userRepository.save(user);


        String emailLink = helper.getEmailVerificationLink(emailToken);
        
        emailService.sendEmail(savedUser.getEmail(), "Verify Account : ContactNest", emailLink);
        
        return savedUser;
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> updateUser(User user) {
        User user2 = userRepository.findById(user.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not Found."));

        user2.setName(user.getName());
        user2.setEmail(user.getEmail());
        user2.setPassword(user.getPassword());
        user2.setAbout(user.getAbout());
        user2.setProfilePicLink(user.getProfilePicLink());
        user2.setPhoneNumber(user.getPhoneNumber());
        user2.setEnabled(user.isEnabled());
        user2.setEmailVerified(user.isEmailVerified());
        user2.setPhoneVerified(user.isPhoneVerified());
        user2.setProvider(user.getProvider());
        user2.setProviderUserId(user.getProviderUserId());

        // saving the user in database
        User saveUser = userRepository.save(user2);

        return Optional.ofNullable(saveUser);
    }

    @Override
    public void deleteUser(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User Not Found."));
        userRepository.delete(user);
    }

    @Override
    public boolean isUserExists(String id) {
        User user = userRepository.findById(id).orElse(null);

        return (user != null) ? true : false;
    }

    @Override
    public boolean isUserExistsByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);

        return user != null ? true : false;
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }



}
