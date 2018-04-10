package org.hamster.sunflower_v2.controllers;

import org.hamster.sunflower_v2.domain.models.Product;
import org.hamster.sunflower_v2.domain.models.User;
import org.hamster.sunflower_v2.domain.models.UserDTO;
import org.hamster.sunflower_v2.exceptions.EmailDoesNotExistException;
import org.hamster.sunflower_v2.services.ProductService;
import org.hamster.sunflower_v2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;

/**
 * Created by ONB-CZEIDE on 02/19/2018
 */
@Controller
@RequestMapping(value = "/")
public class SunflowerController {

    private UserService userService;
    private ProductService productService;

    @Autowired
    public SunflowerController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping
    public String index(ModelMap modelMap) {
        User loggedUser = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        modelMap.put("products", productService.findAll());
        modelMap.put("loggedUser", loggedUser);
        return "index";
    }

    @GetMapping(value = "/login")
    public String loginForm(HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken)) {

            /* The user is logged in :) */
            return "redirect: ";
        }

        if(session.getAttribute("cart") == null) {
            Map<Long, Product> cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }

        return "login";
    }

    @GetMapping(value = "/registration")
    public String showRegistrationForm(WebRequest request, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken)) {

            /* The user is logged in :) */
            return "redirect: ";
        }

        UserDTO userDto = new UserDTO();
        model.addAttribute("user", userDto);
        return "registration";
    }

    @GetMapping(value = "/verifyAccount")
    public String verifyAccount(@RequestParam(required=false, value = "token") String token) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken)) {
            /* The user is logged in :) */
            return "redirect: ";
        }

        if (StringUtils.isEmpty(token)) {
            return "redirect: ";
        }

        if (userService.getUserByVerificationToken(token) != null) {
            return "redirect:/login?verified";
        }

        return "redirect:/login?failed";
    }

    @GetMapping(value = "/forgotPassword")
    public String forgotPasswordForm() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken)) {

            /* The user is logged in :) */
            return "redirect: ";
        }

        return "forgotPassword";
    }

    @PostMapping(value = "/forgotPassword/resetPassword")
    public String resetPassword(@RequestParam("email") String email) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken)) {

            /* The user is logged in :) */
            return "redirect: ";
        }

        User user = userService.findByUsername(email);
        return passwordReset(user);
    }

    @GetMapping(value = "/resetPassword")
    public String resetPasswordForm(@RequestParam(value = "token") String token, HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken)) {
            /* The user is logged in :) */
            return "redirect: ";
        }

        if (StringUtils.isEmpty(token)) {
            return "redirect: ";
        }

        session.setAttribute("username", userService.getUserByPasswordResetToken(token).getUsername());
        System.out.println(session.getAttribute("username"));
        return "resetPassword";
    }

    @PostMapping(value = "/resetPassword/confirmPassword")
    public String confirmPassword(@RequestParam("password") String password, @RequestParam("passwordConfirm") String passwordConfirm, HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken)) {

            /* The user is logged in :) */
            return "redirect: ";
        }

        if (!password.equals(passwordConfirm)) {
            return "redirect:/resetPassword?invalid";
        }

        User updateUser = userService.findByUsername((String) session.getAttribute("username"));
        session.removeAttribute("username");
        userService.changeUserPassword(updateUser, password);
        return "redirect:/login?resetPassword";
    }

    @GetMapping(value = "profile/{id}")
    public String profile(@PathVariable("id") Long id, ModelMap modelMap) {
        User loggedUser = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        User user = userService.findById(id);

        modelMap.put("loggedUser", loggedUser);
        modelMap.put("user", user);
        return "profile";
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
}
