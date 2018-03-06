package org.hamster.sunflower_v2.controllers;

import org.hamster.sunflower_v2.domain.models.Product;
import org.hamster.sunflower_v2.domain.models.User;
import org.hamster.sunflower_v2.services.ProductService;
import org.hamster.sunflower_v2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ONB-CZEIDE on 03/01/2018
 */
@Controller
@RequestMapping(value = "cart")
public class CartController {

    private static String CART_PATH = "cart/";

    private UserService userService;
    private ProductService productService;

    private Map<Long, Product> cart;

    @Autowired
    public CartController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping
    public String index(ModelMap modelMap, HttpSession session) {
        User loggedUser = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if(session.getAttribute("cart") != null) {
            modelMap.put("total", total(session));
        }
        modelMap.put("loggedUser", loggedUser);
        return CART_PATH + "index";
    }

    private BigDecimal total(HttpSession session) {
        cart = (HashMap) session.getAttribute("cart");
        BigDecimal total = BigDecimal.ZERO;

        for (Product product : cart.values()) {
            BigDecimal price = product.getPrice();
            total = total.add(price);
        }

        return total;
    }
}
