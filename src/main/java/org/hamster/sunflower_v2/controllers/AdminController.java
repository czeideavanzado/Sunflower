package org.hamster.sunflower_v2.controllers;

import org.hamster.sunflower_v2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by ONB-CZEIDE on 02/27/2018
 */
@Controller
@RequestMapping("admin")
public class AdminController {

    private static String ADMIN_PATH = "admin/";

    private UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String index(ModelMap modelMap) {
        modelMap.put("users", userService.findAll());
        return ADMIN_PATH + "index";
    }
}
