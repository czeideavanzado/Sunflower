package org.hamster.sunflower_v2.controllers;

import org.hamster.sunflower_v2.domain.models.BillingInformationDTO;
import org.hamster.sunflower_v2.domain.models.Product;
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

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    private Set<String> billingInformation;

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

        BillingInformationDTO billingInformationDTO = new BillingInformationDTO();
        modelMap.put("billingInformationDto", billingInformationDTO);
        return CART_PATH + "index";
    }

    @PostMapping(value = "processOrder")
    public ModelAndView processOrder(@ModelAttribute("billingInformationDto") @Valid BillingInformationDTO billingInformationDTO, BindingResult result,
                                     WebRequest request, Errors errors) {
        return new ModelAndView(CART_PATH + "confirm", "billingInformationDto", billingInformationDTO);
    }

    @PostMapping(value = "confirm")
    public String confirmForm(ModelMap modelMap, HttpSession session) {
        User loggedUser = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if(session.getAttribute("cart") != null) {
            modelMap.put("total", total(session));
        }

        modelMap.put("loggedUser", loggedUser);
        return CART_PATH + "confirm";
    }

    @GetMapping(value = "buy/{id}")
    public String buy(@PathVariable("id") Long id, HttpSession session) {
        cart = (HashMap) session.getAttribute("cart");

        if(!cart.containsKey(id)) {
            cart.put(id, productService.find(id));
        }

        return "redirect:../../cart";
    }

    @GetMapping(value = "remove/{id}")
    public String remove(@PathVariable("id") Long id, HttpSession session) {
        cart = (HashMap) session.getAttribute("cart");

        cart.remove(id);
        session.setAttribute("cart", cart);

        return "redirect:../../cart";
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
