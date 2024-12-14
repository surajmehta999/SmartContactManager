package com.scm.controller;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.helpers.AppConstants;
import com.scm.helpers.Helper;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;
    
    @Autowired
    private ContactService contactService;

    private Logger logger = LoggerFactory.getLogger(ContactController.class);

    //add contact page: handler
    @RequestMapping("/add")
    public String addContactView(Model model){

        ContactForm contactForm = new ContactForm();
        // contactForm.setAddress("Prem Nagar");
        // contactForm.setFavourite(true);

        model.addAttribute("contactForm", contactForm);

        return "user/add_contact";
    }

    @RequestMapping(value = "/add", method=RequestMethod.POST)
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult bindingResult, Authentication authentication,HttpSession session){


        //validate the processing data
        if(bindingResult.hasErrors()){
            //  System.out.println("eroor : "+bindingResult.toString());

           session.setAttribute("message", Message.builder().content("Please correct the following errors").type(MessageType.red).build());
             return "user/add_contact";
        }

        //get the user from login
        String username = Helper.getEmailOfLoggedInUser(authentication);

        User user = userService.getUserByEmail(username);


        logger.info("file information : {}",contactForm.getContactImage().getOriginalFilename());
       
       //image random name
        String fileName = UUID.randomUUID().toString();

        String fileURL =imageService.uploadImage(contactForm.getContactImage(),fileName);

         //saving data into DB
        Contact contact=new Contact();
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setFavourite(contactForm.isFavourite());
        contact.setUser(user);
        contact.setPicture(fileURL);
        contact.setCloudinaryImagePublicId(fileName);
       
        contactService.save(contact);
        System.out.println(contactForm);

        //set the contact picture


        //set the message to be displayed on the view

        Message message =  Message.builder().content("Contact Saved Successfully").type(MessageType.green).build();
        session.setAttribute("message",message);


        return "redirect:/user/contacts/add";
    }

    @RequestMapping
    public String viewContact(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = ""+AppConstants.PAGE_SIZE) int size,
        @RequestParam(value = "sortBy", defaultValue = "name") String sortby,
        @RequestParam(value = "direction", defaultValue = "asc") String direction,
        Model model,Authentication authentication){

       
        String username = Helper.getEmailOfLoggedInUser(authentication);

        User user = userService.getUserByEmail(username);

        Page<Contact> pageContact= contactService.getByUser(user,page,size,sortby,direction);
        System.out.println("Suraj Page :"+pageContact);
        model.addAttribute("pageContact", pageContact);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

    

        return "user/contacts";
    }

// search functionality
    @GetMapping("/search")
    public String searchContact(
        @RequestParam("field") String field,
        @RequestParam("query") String query,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = ""+AppConstants.PAGE_SIZE) int size,
        @RequestParam(value = "sortBy", defaultValue = "name") String sortby,
        @RequestParam(value = "direction", defaultValue = "asc") String direction,
        Model model,
        Authentication authentication
    )
    {
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);

        Page<Contact> pageContact = contactService.search(field, query, page, size, sortby, direction,user);
        model.addAttribute("pageContact", pageContact);
        model.addAttribute("field", field);
        model.addAttribute("query", query);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

        System.out.println(field);
        System.out.println(query);
        System.out.println(pageContact);
        return  "user/searchResults";
    }

    //delete contacts
    @RequestMapping("/delete/{contactId}")
    public String deleteContact(
        @PathVariable String contactId,
        HttpSession session
    ){
        contactService.delete(contactId);
        logger.info("Contact deleted :" +contactId);

        Message message =  Message.builder().content("Contact is Deleted Successfully !!").type(MessageType.green).build();

        session.setAttribute("message",message);
        logger.info("Message :" +message);
        
        return "redirect:/user/contacts";
    }

    //update contact form view
    @GetMapping("/view/{contactId}")
    public String updateContactFormView(
        @PathVariable("contactId") String contactId,
        Model model
    )
    {
        var contact =contactService.getById(contactId);
        ContactForm contactForm = new ContactForm();
        contactForm.setName(contact.getName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setAddress(contact.getAddress());
        contactForm.setDescription(contact.getDescription());
        contactForm.setFavourite(contact.isFavourite());
        contactForm.setWebsiteLink(contact.getWebsiteLink());
        contactForm.setLinkedInLink(contact.getLinkedInLink());
        contactForm.setPicture(contact.getPicture());

        contactForm.setPicture(contact.getPicture());
        
        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId", contactId);


        return "user/update_contact_view";
    }

}
