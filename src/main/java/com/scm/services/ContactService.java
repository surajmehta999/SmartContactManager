package com.scm.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.scm.entities.Contact;
import com.scm.entities.User;

public interface ContactService {

    //save contact
    Contact save(Contact contact);

    //update contact
    Contact update(Contact contact);

    //get contacts
    List<Contact> getAll();

    //get contact by id
    Contact getById(String id);

    //delete Contact
    void delete(String id);

    //search contact
    // List<Contact> search(String name, String email, String phoneNumber);
    Page<Contact> search(String field, String query,int page, int size,String sortField, String direction, User user);

    //get contacts by userid
    List<Contact> getByUserId(String userId);

    Page<Contact> getByUser(User user,int page, int size,String sortField, String direction);

}
