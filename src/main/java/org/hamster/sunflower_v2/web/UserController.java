package org.hamster.sunflower_v2.web;

import org.hamster.sunflower_v2.domain.models.User;
import org.hamster.sunflower_v2.domain.models.UserDTO;
import org.hamster.sunflower_v2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by ONB-CZEIDE on 02/19/2018
 */
@RestController
public class UserController {

    private UserService userService;
    private TokenStore tokenStore;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/register")
    public String register(@RequestBody UserDTO userDto) {
        if (!userDto.getPassword().equals(userDto.getPasswordConfirm())) {
            return "Passwords do not match";
        } else if (userService.getUser(userDto.getUsername()) != null) {
            return "User Already exists";
        }

        userService.save(new User(userDto.getUsername(), userDto.getPassword(), userDto.getFirst_name(), userDto.getLast_name()));
        return "User created";
    }

    @GetMapping(value = "/users")
    public List<User> users() {
        return userService.getAllUsers();
    }
}
