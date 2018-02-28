package org.hamster.sunflower_v2.controllers;

import org.hamster.sunflower_v2.domain.models.User;
import org.hamster.sunflower_v2.services.ProductService;
import org.hamster.sunflower_v2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by ONB-CZEIDE on 02/27/2018
 */
@Controller
@RequestMapping(value = "admin")
public class AdminController {

    private UserService userService;
    private ProductService productService;

    private static String ADMIN_PATH = "admin/";

    @Autowired
    public AdminController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping
    public String index(ModelMap modelMap) {
        User loggedUser = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<User> users = userService.findAll();
        users.remove(loggedUser);

        modelMap.put("users", users);
        modelMap.put("products", productService.findAll());
        modelMap.put("loggedUser", loggedUser);

        return ADMIN_PATH + "index";
    }
}
