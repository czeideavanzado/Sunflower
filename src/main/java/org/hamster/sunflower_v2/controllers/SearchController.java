package org.hamster.sunflower_v2.controllers;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.hamster.sunflower_v2.domain.models.PageWrapper;
import org.hamster.sunflower_v2.domain.models.Product;
import org.hamster.sunflower_v2.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ONB-CZEIDE on 02/28/2018
 */
@Controller
@RequestMapping(value = "search")
public class SearchController {

    private ProductService productService;

    @Autowired
    public SearchController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "/all/{keyword}")
    public String search(Model model, @PathVariable("keyword") String keyword, Pageable pageable) {
        PageWrapper<Product> page = page = new PageWrapper<>(productService.findAllByNameContaining(keyword, pageable), "/category/all");

        model.addAttribute("page", page);

        return "/search/results :: resultsList";
    }
}
