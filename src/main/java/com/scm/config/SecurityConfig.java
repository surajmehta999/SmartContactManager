package com.scm.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.scm.services.SecurityCustomUserDetailsService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

    //user create and  login using java code with in memory services
    /* 
    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails user1 = User
        .withDefaultPasswordEncoder()
        .username("admin123")
        .password("admin123")
        .roles("ADMIN","USER")
        .build();

        UserDetails user2 = User
        .withDefaultPasswordEncoder()
        .username("user123")
        .password("password")
        .build();

        var inMemoryUserDetailsManager = new InMemoryUserDetailsManager(user1,user2);
        return inMemoryUserDetailsManager;
        */
        @Autowired
        private OAuthAuthenticationSuccessHandler handler;

        @Autowired
        private SecurityCustomUserDetailsService userDetailsService;

        //Configuration of authentication provider spring security
        @Bean
        public DaoAuthenticationProvider authenticationProvider (){
            DaoAuthenticationProvider daoAuthenticationProvider  =new DaoAuthenticationProvider();
            // user deatils service ka object
            daoAuthenticationProvider.setUserDetailsService(userDetailsService);

            // password encoder ka object
            daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

            return daoAuthenticationProvider;
        }
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
            
            //Configuration

            httpSecurity.authorizeHttpRequests(authorize -> {
               // authorize.requestMatchers("/home","/register","/services","/about").permitAll();
                authorize.requestMatchers("/user/**").authenticated();
                authorize.anyRequest().permitAll();
            });
             //Default Login page of spring boot
            //httpSecurity.formLogin(Customizer.withDefaults());
            //customized login form
            httpSecurity.formLogin(formLogin ->{
                //login is /login page is route pr bnega aur 
                formLogin.loginPage("/signin");

                //is route pr hmara form submit hoga
                formLogin.loginProcessingUrl("/authenticated");
                
                //login successfull hone pr is url pr redirect hoga
                formLogin.successForwardUrl("/user/profile");

                // Erro Handling
                // formLogin.failureForwardUrl("/signin?eroor=true");

                formLogin.usernameParameter("email");

                formLogin.passwordParameter("password");
                
                // formLogin.failureHandler(new AuthenticationFailureHandler() {

                //     @Override
                //     public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                //             AuthenticationException exception) throws IOException, ServletException {
                //         // TODO Auto-generated method stub
                //         throw new UnsupportedOperationException("Unimplemented method 'onAuthenticationFailure'");
                //     }
                    
                // });

                // formLogin.successHandler(new AuthenticationSuccessHandler() {

                //     @Override
                //     public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                //             Authentication authentication) throws IOException, ServletException {
                //         // TODO Auto-generated method stub
                //         throw new UnsupportedOperationException("Unimplemented method 'onAuthenticationSuccess'");
                //     }
                    
                // });
            });
            httpSecurity.csrf(AbstractHttpConfigurer::disable);
            httpSecurity.logout(logoutFrom ->{
                logoutFrom.logoutUrl("/do-logout");
                logoutFrom.logoutSuccessUrl("/signin?logout=true");
            });

            //oauth2 Configuration
           httpSecurity.oauth2Login(oauth ->{
                oauth.loginPage("/signin");
                oauth.successHandler(handler);

           });

            return httpSecurity.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder(){
            return new BCryptPasswordEncoder();
        }
}
