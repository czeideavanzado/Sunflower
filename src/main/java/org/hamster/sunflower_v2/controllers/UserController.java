package org.hamster.sunflower_v2.controllers;

import org.hamster.sunflower_v2.domain.models.User;
import org.hamster.sunflower_v2.domain.models.UserDTO;
import org.hamster.sunflower_v2.exceptions.EmailExistsException;
import org.hamster.sunflower_v2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * Created by ONB-CZEIDE on 02/19/2018
 */
@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/registration")
    public ModelAndView registerUserAccount(@ModelAttribute("user") @Valid UserDTO accountDto, BindingResult result,
                                            WebRequest request, Errors errors) {
        User registered = new User();

        if (!result.hasErrors()) {
            registered = createUserAccount(accountDto, result);
        }

        if (registered == null) {
            result.rejectValue("username", "error.user", "An account already exists for this email");
        }

        if (result.hasErrors()) {
            return new ModelAndView("registration", "user", accountDto);
        } else {
            return new ModelAndView("successRegistration", "user", accountDto);
        }
    }

    private User createUserAccount(UserDTO accountDto, BindingResult result) {
        User registered;

        try {
            registered = userService.registerNewUserAccount(accountDto);
        } catch (EmailExistsException e) {
            return null;
        }

        return registered;
    }

    @GetMapping(value = "/getUsername")
    public String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
