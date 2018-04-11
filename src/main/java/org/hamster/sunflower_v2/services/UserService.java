package org.hamster.sunflower_v2.services;

import org.hamster.sunflower_v2.domain.models.*;
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
    VerificationToken getVerificationToken(String token);
    User getUserByToken(String token);
    User verifyUser(User user);

    void disableBuyer(Long id);
    void disableSeller(Long id);
    void disableAdmin(Long id);
    void enableBuyer(Long id);
    void enableSeller(Long id);
    void enableAdmin(Long id);
}
