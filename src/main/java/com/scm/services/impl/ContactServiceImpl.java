package com.scm.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.helpers.ResourceNotFoundException;
import com.scm.repositories.ContactRepo;
import com.scm.repositories.UserRepo;
import com.scm.services.ContactService;

import lombok.var;

@Service
public class ContactServiceImpl implements ContactService {
    
    @Autowired
    private ContactRepo contactRepo;

    @Autowired
    private UserRepo userRepo;

    @Override
    public Contact save(Contact contact) {
       
        String contactId = UUID.randomUUID().toString();
        contact.setId(contactId);

       return contactRepo.save(contact);
        
    }

    @Override
    public Contact update(Contact contact) {
        var contactOld= contactRepo.findById(contact.getId()).orElseThrow(()-> new ResourceNotFoundException("Contact not found"));
        contactOld.setName(contact.getName());
        contactOld.setPhoneNumber(contact.getPhoneNumber());
        contactOld.setEmail(contact.getEmail());
        contactOld.setAddress(contact.getAddress());
        contactOld.setDescription(contact.getDescription());
        contactOld.setFavourite(contact.isFavourite());
        contactOld.setLinkedInLink(contact.getLinkedInLink());
        contactOld.setWebsiteLink(contact.getWebsiteLink());
        if(contact.getPicture() != null){
            contactOld.setPicture(contact.getPicture());
            contactOld.setCloudinaryImagePublicId(contact.getCloudinaryImagePublicId());
        }    
        return contactRepo.save(contactOld);
    }

    @Override
    public List<Contact> getAll() {
        return contactRepo.findAll();
    }

    @Override
    public Contact getById(String id) {
        return contactRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact not found with given id "+id));
    }

    @Override
    public List<Contact> getByUserId(String userId) {
        return contactRepo.findByUserId(userId);
    }

    @Override
    public Page<Contact> getByUser(User user,int page, int size,String sortBy, String direction) {

        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
       
        var pageable=PageRequest.of(page,size,sort);

        return contactRepo.findByUser(user,pageable);
    }

    @Override
    public Page<Contact> search(String field, String query,int page, int size,String sortBy, String direction, User user) {

        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable=PageRequest.of(page,size,sort);
        
        switch (field) {
            case "name":
                return contactRepo.findByUserAndNameContainingIgnoreCase(user,query,pageable);
            case "email":
                return contactRepo.findByUserAndEmailContainingIgnoreCase(user,query,pageable);
            case "phoneNumber":
                return contactRepo.findByUserAndPhoneNumberContaining(user,query,pageable);
            default:
                throw new IllegalArgumentException("Invalid search field: " + field);
        }
    }

    @Override
    public void delete(String id) {
        var contact = contactRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact not found with given id "+id));
        contactRepo.delete(contact);
    }

}
