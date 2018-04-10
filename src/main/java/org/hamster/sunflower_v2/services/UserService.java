package org.hamster.sunflower_v2.services;

import org.hamster.sunflower_v2.domain.models.*;
import org.hamster.sunflower_v2.exceptions.EmailDoesNotExistException;
import org.hamster.sunflower_v2.exceptions.EmailExistsException;

import java.util.List;

/**
 * Created by ONB-CZEIDE on 02/26/2018
 */
public interface UserService {
    User registerNewUserAccount(UserDTO accountDto) throws EmailExistsException;
    User findById(Long id);
    User findByUsername(String username);
    List<User> findAll();

    void addOrder(User user, Order order);



    void createVerificationToken(User user, String token);
    User getUserByVerificationToken(String token);
    void verifyUser(String token);
    void verifyMockUser(User user);
    boolean isVerificationTokenExpired(String token);

    void createPasswordResetToken(User user, String token) throws EmailDoesNotExistException;
    User getUserByPasswordResetToken(String token);
    User changeUserPassword(User user, String password);
    boolean isPasswordResetTokenExpired(String token);
}
