package com.contactnest.ContactNest.Entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity(name = "user")
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails { // UserDetails to use Spring Security features

    @Id
    public String userId;

    @Column(name = "user_name", nullable = false)
    public String name;

    @Column(unique = true, nullable = false)
    public String email;
    public String password;

    @Column(length = 1000)
    public String about;

    @Column(length = 1000)
    public String profilePicLink;
    public String phoneNumber;

    // information
    @Getter(value = AccessLevel.NONE)
    public boolean enabled = false;

    public String emailToken;
    public boolean emailVerified = false;
    public boolean phoneVerified = false;

    // sign-up provider
    @Enumerated(value = EnumType.STRING)
    public Providers provider = Providers.SELF;
    public String providerUserId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Contact> contacts = new ArrayList<>();




    // these are the methods provided by spring security i.e. UserDetais

    /*
        When we annotate a field with @ElementCollection, JPA (Hibernate) knows 
        this is not a separate entity but still needs to be stored in a relational 
        structure. For collections of basic types like List<String>, Hibernate 
        creates a collection table with two columns user_id & user_role.
    */

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> rolesList = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // list of roles [USER, ADMIN]
        // collection of SimpleGrantedAuthority[roles{ADMIN, USER}]

        Collection<SimpleGrantedAuthority> roles = rolesList.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
        
        return roles;
    }

    // in this project, we are using email as our username
    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked(){
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }


    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

}
