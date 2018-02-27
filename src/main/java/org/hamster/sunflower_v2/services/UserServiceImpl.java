package org.hamster.sunflower_v2.services;

import org.hamster.sunflower_v2.domain.models.RoleRepository;
import org.hamster.sunflower_v2.domain.models.User;
import org.hamster.sunflower_v2.domain.models.UserDTO;
import org.hamster.sunflower_v2.domain.models.UserRepository;
import org.hamster.sunflower_v2.exceptions.EmailExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by ONB-CZEIDE on 02/19/2018
 */
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    @Override
    public User registerNewUserAccount(UserDTO accountDto)
            throws EmailExistsException {

        if (emailExist(accountDto.getUsername())) {
            throw new EmailExistsException("There is an account with that email address:"
                    + accountDto.getUsername());
        }
        User user = new User();
        user.setFirstName(accountDto.getFirst_name());
        user.setLastName(accountDto.getLast_name());
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        user.setUsername(accountDto.getUsername());
        user.setRoles(new HashSet<>(Collections.singletonList(roleRepository.findByRole("BUYER"))));
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userRepository.findAll());
    }

    private boolean emailExist(String username) {
        User user = userRepository.findByUsername(username);

        if (user != null) {
            return true;
        }

        return false;
    }

//    public void save(User user) {
//        user.setPassword(getPasswordEncoder().encode(user.getPassword()));
//        user.setRoles(new HashSet<>(Arrays.asList(roleRepository.findByRole("BUYER"))));
//            user.setEnabled(true);
//        userRepository.save(user);
//    }

    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
