package com.scm.repositories;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.scm.entities.Contact;
import com.scm.entities.User;

import java.util.List;



@Repository
public interface ContactRepo extends JpaRepository<Contact,String>{

    //find the contacts by user
    
    //Custom finder method
    Page<Contact> findByUser(User user, Pageable pageable);

    //custom query method
    @Query("SELECT c FROM Contact c WHERE c.user.id = :userId")
    List<Contact> findByUserId(@Param("userId") String userId);
    
    Page<Contact> findByUserAndNameContainingIgnoreCase(User user, String name, Pageable pageable);
    Page<Contact> findByUserAndEmailContainingIgnoreCase(User user, String email, Pageable pageable);
    Page<Contact> findByUserAndPhoneNumberContaining(User user, String phoneNumber, Pageable pageable);
}
