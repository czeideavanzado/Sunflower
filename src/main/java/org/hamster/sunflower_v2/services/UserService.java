package org.hamster.sunflower_v2.services;

import org.hamster.sunflower_v2.domain.models.User;
import org.hamster.sunflower_v2.domain.models.UserDTO;
import org.hamster.sunflower_v2.exceptions.EmailExistsException;

import java.util.List;

/**
 * Created by ONB-CZEIDE on 02/26/2018
 */
public interface UserService {
    User registerNewUserAccount(UserDTO accountDto) throws EmailExistsException;
    User findByUsername(String username);
    List<User> findAll();
}
