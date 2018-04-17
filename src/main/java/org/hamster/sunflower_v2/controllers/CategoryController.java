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
        modelMap.addAttribute("fragment", "all");
        modelMap.addAttribute("categoryName", "All Products");
        return CATEGORY_PATH + "index";
    }

    @GetMapping(value = "/fashion")
    public String fashion(ModelMap modelMap, Authentication authentication, Pageable pageable) {
        authentication = SecurityContextHolder.getContext().getAuthentication();

        Category fashion = categoryService.findByName("Fashion");

        PageWrapper<Product> page = page = new PageWrapper<>(productService.findByCategory(fashion, pageable), "/category/fashion");

        modelMap.addAttribute("page", page);

        modelMap.addAttribute("loggedUser", userService.findByUsername(authentication.getName()));
        modelMap.addAttribute("categories", categoryService.findAll());
        modelMap.addAttribute("fragment", "fashion");
        modelMap.addAttribute("categoryName", "Fashion");
        return CATEGORY_PATH + "index";
    }

    @GetMapping(value = "/mobiles")
    public String mobiles(ModelMap modelMap, Authentication authentication, Pageable pageable) {
        authentication = SecurityContextHolder.getContext().getAuthentication();

        Category mobiles = categoryService.findByName("Mobiles");

        PageWrapper<Product> page = page = new PageWrapper<>(productService.findByCategory(mobiles, pageable), "/category/mobiles");

        modelMap.addAttribute("page", page);

        modelMap.addAttribute("loggedUser", userService.findByUsername(authentication.getName()));
        modelMap.addAttribute("categories", categoryService.findAll());
        modelMap.addAttribute("fragment", "mobiles");
        modelMap.addAttribute("categoryName", "Mobiles");
        return CATEGORY_PATH + "index";
    }

    @GetMapping(value = "/electronics+appliances")
    public String electronicsAndAppliances(ModelMap modelMap, Authentication authentication, Pageable pageable) {
        authentication = SecurityContextHolder.getContext().getAuthentication();

        Category electronicsAndAppliances = categoryService.findByName("Electronics & Appliances");

        PageWrapper<Product> page = page = new PageWrapper<>(productService.findByCategory(electronicsAndAppliances, pageable), "/category/electronics+appliances");

        modelMap.addAttribute("page", page);

        modelMap.addAttribute("loggedUser", userService.findByUsername(authentication.getName()));
        modelMap.addAttribute("categories", categoryService.findAll());
        modelMap.addAttribute("fragment", "electronics_appliances");
        modelMap.addAttribute("categoryName", "Electronics & Appliances");
        return CATEGORY_PATH + "index";
    }

    @GetMapping(value = "/furniture")
    public String furniture(ModelMap modelMap, Authentication authentication, Pageable pageable) {
        authentication = SecurityContextHolder.getContext().getAuthentication();

        Category furniture = categoryService.findByName("Furniture");

        PageWrapper<Product> page = page = new PageWrapper<>(productService.findByCategory(furniture, pageable), "/category/furniture");

        modelMap.addAttribute("page", page);

        modelMap.addAttribute("loggedUser", userService.findByUsername(authentication.getName()));
        modelMap.addAttribute("categories", categoryService.findAll());
        modelMap.addAttribute("fragment", "furniture");
        modelMap.addAttribute("categoryName", "Furniture");
        return CATEGORY_PATH + "index";
    }

    @GetMapping(value = "/books")
    public String books(ModelMap modelMap, Authentication authentication, Pageable pageable) {
        authentication = SecurityContextHolder.getContext().getAuthentication();

        Category books = categoryService.findByName("Books");

        PageWrapper<Product> page = page = new PageWrapper<>(productService.findByCategory(books, pageable), "/category/books");

        modelMap.addAttribute("page", page);

        modelMap.addAttribute("loggedUser", userService.findByUsername(authentication.getName()));
        modelMap.addAttribute("categories", categoryService.findAll());
        modelMap.addAttribute("fragment", "books");
        modelMap.addAttribute("categoryName", "Books");
        return CATEGORY_PATH + "index";
    }
}
