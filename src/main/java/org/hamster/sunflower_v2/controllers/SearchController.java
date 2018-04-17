package org.hamster.sunflower_v2.controllers;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.hamster.sunflower_v2.domain.models.Category;
import org.hamster.sunflower_v2.domain.models.PageWrapper;
import org.hamster.sunflower_v2.domain.models.Product;
import org.hamster.sunflower_v2.services.CategoryService;
import org.hamster.sunflower_v2.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ONB-CZEIDE on 02/28/2018
 */
@Controller
@RequestMapping(value = "search")
public class SearchController {

    private ProductService productService;
    private CategoryService categoryService;

    @Autowired
    public SearchController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping(value = "/all")
    public String search(Model model, @RequestParam("keyword") String keyword, Pageable pageable) {
        keyword = keyword.replace("+", " ");

        PageWrapper<Product> page = page = new PageWrapper<>(productService.findAllByNameContaining(keyword, pageable), "/category/all");

        model.addAttribute("page", page);

        return "/search/results :: resultsList";
    }

    @GetMapping(value = "/fashion")
    public String searchFashion(Model model, @RequestParam("keyword") String keyword, Pageable pageable) {
        keyword = keyword.replace("+", " ");

        Category category = categoryService.findByName("Fashion");

        PageWrapper<Product> page = new PageWrapper<>(productService.findByCategoryAndArchivedIsFalseAndNameContaining(category, keyword, pageable), "/category/fashion");

        model.addAttribute("page", page);

        return "/search/results :: resultsList";
    }

    @GetMapping(value = "/mobiles")
    public String searchMobiles(Model model, @RequestParam("keyword") String keyword, Pageable pageable) {
        keyword = keyword.replace("+", " ");

        Category category = categoryService.findByName("Mobiles");

        PageWrapper<Product> page = new PageWrapper<>(productService.findByCategoryAndArchivedIsFalseAndNameContaining(category, keyword, pageable), "/category/mobiles");

        model.addAttribute("page", page);

        return "/search/results :: resultsList";
    }

    @GetMapping(value = "/electronics+appliances")
    public String searchElectronicsAndAppliances(Model model, @RequestParam("keyword") String keyword, Pageable pageable) {
        keyword = keyword.replace("+", " ");

        Category category = categoryService.findByName("Electronics & Appliances");

        PageWrapper<Product> page = new PageWrapper<>(productService.findByCategoryAndArchivedIsFalseAndNameContaining(category, keyword, pageable), "/category/electronics+appliances");

        model.addAttribute("page", page);

        return "/search/results :: resultsList";
    }

    @GetMapping(value = "/furniture")
    public String searchFurniture(Model model, @RequestParam("keyword") String keyword, Pageable pageable) {
        keyword = keyword.replace("+", " ");

        Category category = categoryService.findByName("Furniture");

        PageWrapper<Product> page = new PageWrapper<>(productService.findByCategoryAndArchivedIsFalseAndNameContaining(category, keyword, pageable), "/category/furniture");

        model.addAttribute("page", page);

        return "/search/results :: resultsList";
    }

    @GetMapping(value = "/books")
    public String searchBooks(Model model, @RequestParam("keyword") String keyword, Pageable pageable) {
        keyword = keyword.replace("+", " ");

        Category category = categoryService.findByName("Books");

        PageWrapper<Product> page = new PageWrapper<>(productService.findByCategoryAndArchivedIsFalseAndNameContaining(category, keyword, pageable), "/category/books");

        model.addAttribute("page", page);

        return "/search/results :: resultsList";
    }
}
