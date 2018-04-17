package org.hamster.sunflower_v2.controllers;

import org.hamster.sunflower_v2.domain.models.Category;
import org.hamster.sunflower_v2.domain.models.PageWrapper;
import org.hamster.sunflower_v2.domain.models.Product;
import org.hamster.sunflower_v2.services.CategoryService;
import org.hamster.sunflower_v2.services.ProductService;
import org.hamster.sunflower_v2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by ONB-CZEIDE on 04/16/2018
 */
@Controller
@RequestMapping(value = "category")
public class CategoryController {

    private static String CATEGORY_PATH = "category/";

    private UserService userService;
    private ProductService productService;
    private CategoryService categoryService;

    @Autowired
    public CategoryController(UserService userService, ProductService productService, CategoryService categoryService) {
        this.userService = userService;
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping(value = "/all")
    public String index(ModelMap modelMap, Authentication authentication, Pageable pageable) {
        authentication = SecurityContextHolder.getContext().getAuthentication();

        PageWrapper<Product> page = page = new PageWrapper<>(productService.findAll(pageable), "/category/all");

        modelMap.addAttribute("page", page);

        modelMap.addAttribute("loggedUser", userService.findByUsername(authentication.getName()));
        modelMap.addAttribute("categories", categoryService.findAll());
        return CATEGORY_PATH + "index";
    }
}
