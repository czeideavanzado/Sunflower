package org.hamster.sunflower_v2.controllers;

import org.hamster.sunflower_v2.domain.models.User;
import org.hamster.sunflower_v2.domain.models.UserDTO;
import org.hamster.sunflower_v2.exceptions.EmailDoesNotExistException;
import org.hamster.sunflower_v2.exceptions.EmailExistsException;
import org.hamster.sunflower_v2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.UUID;

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
        if (!accountDto.getPassword().equals(accountDto.getPasswordConfirm())) {
            return new ModelAndView("redirect:/registration?password");
        }

        User registered = new User();

        if (!result.hasErrors()) {
            registered = createUserAccount(accountDto, result);
        }

        if (registered == null) {
            result.rejectValue("username", "error.user", "An account already exists for this email");
            return new ModelAndView("registration", "user", accountDto);
        }

        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(registered, token);

        return new ModelAndView("successRegistration", "user", accountDto);
    }

    @GetMapping(value = "/registration/resend")
    public ModelAndView resendVerificationLink(@RequestParam("token") String token) {
        User registered = userService.getUserByVerificationToken(token);

        String newToken = UUID.randomUUID().toString();
        userService.createVerificationToken(registered, newToken);

        return new ModelAndView("successRegistration");
    }

    @GetMapping(value = "/verifyAccount")
    public ModelAndView verifyAccount(@RequestParam(required=false, value = "token") String token) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken)) {
            /* The user is logged in :) */
            return new ModelAndView("redirect: ");
        }

        if (StringUtils.isEmpty(token)) {
            return new ModelAndView("redirect: ");
        }

        if (!userService.isVerificationTokenExpired(token)) {
            userService.verifyUser(token);
            return new ModelAndView("redirect:/login?verified");
        }

        return new ModelAndView("redirect:/login?expired=" + token);
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

    @PostMapping(value = "/forgotPassword/resetPassword")
    public ModelAndView resetPassword(@RequestParam("email") String email) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken)) {

            /* The user is logged in :) */
            return new ModelAndView("redirect: ");
        }

        User user = userService.findByUsername(email);
        return new ModelAndView(passwordReset(user));
    }

    @GetMapping(value = "/forgotPassword/resetPassword")
    public ModelAndView retryResetPassword(@RequestParam("token") String token) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken)) {

            /* The user is logged in :) */
            return new ModelAndView("redirect: ");
        }

        User user = userService.getUserByPasswordResetToken(token);
        return new ModelAndView(passwordReset(user));
    }

    private String passwordReset(User user) {
        String token = UUID.randomUUID().toString();

        try {
            userService.createPasswordResetToken(user, token);
        } catch (EmailDoesNotExistException e) {
            e.printStackTrace();
            return "redirect:/forgotPassword?failed";
        }

        return "redirect:/forgotPassword?success";
    }

    @GetMapping(value = "/resetPassword")
    public ModelAndView resetPasswordForm(@RequestParam(value = "token") String token, HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken)) {
            /* The user is logged in :) */
            return new ModelAndView("redirect: ");
        }

        if (StringUtils.isEmpty(token)) {
            return new ModelAndView("redirect: ");
        }

        if(!userService.isPasswordResetTokenExpired(token)) {
            session.setAttribute("username", userService.getUserByPasswordResetToken(token).getUsername());
            return new ModelAndView("resetPassword");
        }

        return new ModelAndView("redirect:/forgotPassword?expired");
    }

    @PostMapping(value = "/resetPassword/confirmPassword")
    public ModelAndView confirmPassword(@RequestParam("password") String password, @RequestParam("passwordConfirm") String passwordConfirm, HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken)) {

            /* The user is logged in :) */
            return new ModelAndView("redirect: ");
        }

        if (!password.equals(passwordConfirm)) {
            return new ModelAndView("redirect:/resetPassword?invalid");
        }

        User updateUser = userService.findByUsername((String) session.getAttribute("username"));
        session.removeAttribute("username");

        if (userService.changeUserPassword(updateUser, password) != null) {
            return new ModelAndView("redirect:/login?resetPassword");
        }

        return new ModelAndView("redirect:/resetPassword?error");
    }
}
