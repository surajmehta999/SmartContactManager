package com.scm.services;
import java.util.List;
import java.util.Optional;

import com.scm.entities.User;

public interface UserService {
    User saveUser(User user);
    User verifiedSaveUser(User user);
    Optional<User> getUserById(String id);
    Optional<User> updateUser(User user);
    void deleteUser(String id);
    boolean isUserExist(String userId);
    boolean isUserExistByEmail(String email);
    User findByVerificationToken(String token );

    List <User> getAllUser();
    
    User getUserByEmail(String email);

    //if needed add more methods here--
}
