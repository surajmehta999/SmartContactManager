package com.scm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.UserService;
import com.scm.services.impl.EmailService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;



@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }
    

    @GetMapping("/home")
    public String home(Model model){
        System.out.println("Home page handler");
        //sending data to view
        model.addAttribute("name", "Substring technologies");
        model.addAttribute("youtubechannel", "Learn code with Suraj");
        model.addAttribute("githubrepo", "https://github.com/surajmehta999");
        return "home";
    }

    //About route
    @GetMapping("/about")
    public String about(Model model){
        model.addAttribute("isLogin", false);
        System.out.println("About page loading");
        return "about";
    }

    //Services route
    @GetMapping("/services")
    public String servicesPage(){
        System.out.println("Services page loading");
        return "services";
    }

     //contact route
     @GetMapping("/contact")
     public String contactPage(){
         System.out.println("Contact page loading");
         return new String( "contact");
     }
     //Login route
     @GetMapping("/signin")
     public String signinPage(){

         System.out.println("Login page loading");
         return new String( "signin");
      }
      //Register route
      @GetMapping("/register")
      public String register(Model model){

        UserForm userForm = new UserForm();
         /* 
        //Sending data to the fronend--
        userForm.setName("suraj");
        userForm.setEmail("surajmehta999@gmail.com");
        userForm.setPassword("qwerty");
        userForm.setAbout("Trying to learn SpringBoot");
        userForm.setPhoneNumber("123456");
        */
         model.addAttribute("userForm", userForm);
          return "register";
      }

    //processing register
    @RequestMapping(value = "/do-register",method = RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult result1, HttpSession session){
        System.out.println("processing registration");
        //fetch from data
        //userForm
        //System.out.println(userForm);

        if(result1.hasErrors()){
            //System.out.println("eroor : "+result1.toString());
            // model.addAttribute("userForm", userForm);
            return "register";
        }

        //check if email is already exists
        if(userService.isUserExistByEmail(userForm.getEmail())){
            Message errorMessage = Message.builder()
                    .content("Email already exists please login to continue!")
                    .type(MessageType.red)
                    .build();
            session.setAttribute("message", errorMessage);
            return "register";

        }

        //validate from data

        //save the database
        //userService
        //UserForm --> User
        /* 
        User user=User.builder()
        .name(userForm.getName())
        .email(userForm.getEmail())
        .password(userForm.getPassword())
        .about(userForm.getAbout())
        .phoneNumber(userForm.getPhoneNumber())
        .profilePic("https://www.istockphoto.com/vector/profile-picture-vector-illustration-gm587805156-100912283")
        .build();
        */
        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setProfilePic("https://www.istockphoto.com/vector/profile-picture-vector-illustration-gm587805156-100912283");

         // Generate verification token
        String token = java.util.UUID.randomUUID().toString();
        // Save the token to the user object
        user.setVerificationToken(token);
        User savedUser=userService.saveUser(user);
        //System.out.println("Registration Successfull");

        // Create a verification link
        String verificationLink = UriComponentsBuilder.fromHttpUrl("http://localhost:8081/verify")
                .queryParam("token", token)
                .toUriString();

         // Send the verification email
         emailService.sendVerificationEmail(savedUser.getEmail(), verificationLink);
        
          // Create success message
          Message message = Message.builder()
          .content("Registration successful! Please check your email for verification.")
          .type(MessageType.green)
          .build();

        session.setAttribute("message", message);
    

        //message="Registration Successfull"
        //add the message
    //   Message message =  Message.builder().content("Registration Successfull").type(MessageType.green).build();

    //    session.setAttribute("message",message);

        //redirect to login page
        return "redirect:/register";
    }
      
     // Email verification endpoint
    @GetMapping("/verify")
    public String verifyEmail(@RequestParam String token, HttpSession session) {
        User user = userService.findByVerificationToken(token);

        if (user == null) {
            Message errorMessage = Message.builder()
                    .content("Invalid or expired verification token.")
                    .type(MessageType.red)
                    .build();
            session.setAttribute("message", errorMessage);
            return "redirect:/signin";
        }

        // Mark user as verified and remove the token
        user.setEmailVerified(true);
        user.setVerificationToken(null);
        userService.verifiedSaveUser(user);

        // Success message
        Message successMessage = Message.builder()
                .content("Email successfully verified!")
                .type(MessageType.green)
                .build();
        session.setAttribute("message", successMessage);

        return "redirect:/signin";
    }
}
