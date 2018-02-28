package org.hamster.sunflower_v2.controllers;

import org.hamster.sunflower_v2.domain.models.ProductDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;

/**
 * Created by ONB-CZEIDE on 02/28/2018
 */
@Controller
@RequestMapping(value = "product")
public class ProductController {

    @GetMapping(value = "/sell")
    public String sellProductForm(WebRequest request, Model model) {
        ProductDTO product = new ProductDTO();
        model.addAttribute("product", product);
        return "registration";
    }

    @PostMapping(value = "/sell")
    public String sellProduct(@ModelAttribute("product") @Valid ProductDTO productDTO, BindingResult result,
                              WebRequest request, Errors errors) {
        return "";
    }
}
