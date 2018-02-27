package org.hamster.sunflower_v2.controllers;

import org.hamster.sunflower_v2.domain.models.User;
import org.hamster.sunflower_v2.domain.models.UserDTO;
import org.hamster.sunflower_v2.services.ProductService;
import org.hamster.sunflower_v2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.WebRequest;

/**
 * Created by ONB-CZEIDE on 02/19/2018
 */
@Controller
public class SunflowerController {

    private UserService userService;
    private ProductService productService;

    @Autowired
    public SunflowerController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping(value = "/")
    public String index(ModelMap modelMap) {
        User loggedUser = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        modelMap.put("products", productService.findAll());
        modelMap.put("loggedUser", loggedUser);
        return "index";
    }

    @GetMapping(value = "/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping(value = "/registration")
    public String showRegistrationForm(WebRequest request, Model model) {
        UserDTO userDto = new UserDTO();
        model.addAttribute("user", userDto);
        return "registration";
    }
}
