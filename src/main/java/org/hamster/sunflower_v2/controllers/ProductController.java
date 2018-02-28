package org.hamster.sunflower_v2.controllers;

import org.hamster.sunflower_v2.domain.models.Product;
import org.hamster.sunflower_v2.domain.models.ProductDTO;
import org.hamster.sunflower_v2.domain.models.User;
import org.hamster.sunflower_v2.services.ProductService;
import org.hamster.sunflower_v2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
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

    private static String PRODUCT_PATH = "product/";

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "/buy/{id}")
    public ModelAndView buyProduct(@PathVariable("id") Long id) {
        productService.orderProduct(id);
        return new ModelAndView(PRODUCT_PATH + "orderConfirmation", "", "");
    }

    @GetMapping(value = "/sell")
    public String sellProductForm(ModelMap modelMap) {
        ProductDTO product = new ProductDTO();
        User loggedUser = productService.findBySellerByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
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

    @GetMapping(value = "/edit/{id}")
    public String editProductForm(@PathVariable("id") Long id, ModelMap modelMap) {
        User loggedUser = productService.findBySellerByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        modelMap.put("loggedUser", loggedUser);
        modelMap.put("product", productService.find(id));
        return PRODUCT_PATH + "edit";
    }

    @PostMapping(value = "/edit/{id}")
    public ModelAndView editProduct(@PathVariable("id") Long id, @ModelAttribute("product") @Valid Product product, BindingResult result,
                                    WebRequest request, Errors errors) {
        if(!result.hasErrors()) {
            productService.updateProduct(product, id);
            return new ModelAndView(PRODUCT_PATH + "editConfirmation", "product", product);
        } else {
            return new ModelAndView(PRODUCT_PATH + "edit", "product", product);
        }
    }

    @GetMapping(value = "/remove/{id}")
    public ModelAndView removeProduct(@PathVariable("id") Long id) {
        productService.removeProduct(id);
        return new ModelAndView(PRODUCT_PATH + "removeConfirmation", "", "");
    }

    @GetMapping(value = "/myProducts")
    public String myProducts(ModelMap modelMap) {
        User seller = productService.findBySellerByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        modelMap.put("loggedUser", seller);
        modelMap.put("products", seller.getProducts());
        return PRODUCT_PATH + "my_products";
    }
}
