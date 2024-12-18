package com.scm.entities;

import java.util.*;
import java.util.stream.Collector;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails{
    @Id
    private String userId;
    @Column(name="user_name",nullable = false)
    private String name;
    @Column(unique = true,nullable = false)
    private String email;
    private String password;
    @Column(length = 10000)
    private String about;
    @Column(length = 1000)
    private String profilePic;
    private String phoneNumber;

    private boolean enabled=true;

    private boolean emailVerified =false;
    private String verificationToken;
    private boolean phoneVerified=false;

    //login SELF,GOOGLE,GUTHUB
    @Enumerated(value = EnumType.STRING) 
    private Providers provider=Providers.SELF;
    private String providerUserId;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
    private List<Contact> contacts = new ArrayList<>();


    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roleList=new ArrayList<>();

    // @Override
    // public Collection<? extends GrantedAuthority> getAuthorities() {
    // // List of roles [USER,ADMIN]
    // // Collection of SimpleGrantedAuthority[roles{ADMIN,USER}]
    //     Collection<SimpleGrantedAuthority> roles=  roleList.stream().map(role -> new SimpleGrantedAuthority(about))
    //     .collect(Collectors.toList());
    //     return roles;
    // }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roleList.stream()
            .map(SimpleGrantedAuthority::new) // Map each role to a SimpleGrantedAuthority
            .collect(Collectors.toList());
    }

    //Username
    @Override
    public String getUsername() {
        return this.email;    
    }

}
