package org.hamster.sunflower_v2.controllers;

import org.hamster.sunflower_v2.domain.models.ProductDTO;
import org.hamster.sunflower_v2.domain.models.User;
import org.hamster.sunflower_v2.services.ProductService;
import org.hamster.sunflower_v2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * Created by ONB-CZEIDE on 02/28/2018
 */
@Controller
@RequestMapping(value = "product")
public class ProductController {

    private ProductService productService;
    private UserService userService;

    private static String PRODUCT_PATH = "product/";

    @Autowired
    public ProductController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping(value = "/sell")
    public String sellProductForm(ModelMap modelMap) {
        ProductDTO product = new ProductDTO();
        User loggedUser = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        modelMap.put("loggedUser", loggedUser);
        modelMap.put("product", product);
        return PRODUCT_PATH + "sell";
    }

    @PostMapping(value = "/sell")
    public ModelAndView sellProduct(@ModelAttribute("product") @Valid ProductDTO productDTO, BindingResult result,
                                    WebRequest request, Errors errors) {
        if(!result.hasErrors()) {
            productService.sellProduct(productDTO);
            return new ModelAndView(PRODUCT_PATH + "sellConfirmation", "product", productDTO);
        } else {
            return new ModelAndView(PRODUCT_PATH + "sell", "product", productDTO);
        }
    }
}
