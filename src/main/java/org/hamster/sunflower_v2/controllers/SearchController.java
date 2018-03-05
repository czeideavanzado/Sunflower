package org.hamster.sunflower_v2.controllers;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.hamster.sunflower_v2.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ONB-CZEIDE on 02/28/2018
 */
@RestController
@RequestMapping(value = "search")
public class SearchController {

    private ProductService productService;

    @Autowired
    public SearchController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String search() {
        return "hi";
    }
}
