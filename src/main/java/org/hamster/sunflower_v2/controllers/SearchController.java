package org.hamster.sunflower_v2.controllers;

import org.hamster.sunflower_v2.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
