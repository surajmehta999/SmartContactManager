package com.scm.config;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.core.AuthenticationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, 
                                        AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = "Invalid username or password";
        System.out.println(exception.getMessage().toString());                                  
         if (exception.getMessage().contains("Email not verified")) {
            errorMessage = "Your email is not verified. Please verify your email to login.";
        }

        response.sendRedirect("/signin?error=" + errorMessage);
    }

}
