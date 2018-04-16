package org.hamster.sunflower_v2.controllers;

import org.hamster.sunflower_v2.domain.models.Category;
import org.hamster.sunflower_v2.domain.models.Product;
import org.hamster.sunflower_v2.services.CategoryService;
import org.hamster.sunflower_v2.services.ProductService;
import org.hamster.sunflower_v2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String index(ModelMap modelMap, Authentication authentication) {
        authentication = SecurityContextHolder.getContext().getAuthentication();

        List<Product> products = productService.findAll();

        Set<Product> items = new HashSet<>();
        Set<Set<Product>> pages = new HashSet<>();

        for (Product product : products) {
            items.add(product);

            if (items.size() == 14) {
                pages.add(items);
                items = new TreeSet<>();
            }
        }

        if (pages.size() == 0) {
            pages.add(items);
        }

        modelMap.addAttribute("loggedUser", userService.findByUsername(authentication.getName()));
        modelMap.addAttribute("pages", pages);
        return CATEGORY_PATH + "index";
    }
}
