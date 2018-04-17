package org.hamster.sunflower_v2.controllers;

import org.hamster.sunflower_v2.domain.models.*;
import org.hamster.sunflower_v2.services.OrderService;
import org.hamster.sunflower_v2.services.ProductService;
import org.hamster.sunflower_v2.services.TransactionService;
import org.hamster.sunflower_v2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
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
    private OrderService orderService;
    private TransactionService transactionService;

    private Map<Long, Product> cart;

    @Autowired
    public CartController(UserService userService, ProductService productService, OrderService orderService, TransactionService transactionService) {
        this.userService = userService;
        this.productService = productService;
        this.orderService = orderService;
        this.transactionService = transactionService;
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

    @GetMapping("/placeOrder")
    public String placeOrder(ModelMap modelMap, HttpSession session, Authentication authentication) {
        authentication = SecurityContextHolder.getContext().getAuthentication();

        Map<Long, Product> cart = (HashMap) session.getAttribute("cart");

        if (cart.isEmpty()) {
            return "redirect:/error/404";
        }

        User loggedUser = userService.findByUsername(authentication.getName());

        BigDecimal subtotal = (BigDecimal) session.getAttribute("subtotal");

        if (loggedUser.getWallet().getSeeds().compareTo(subtotal) == -1) {
            return "redirect:/error/404";
        }

        modelMap.put("loggedUser", loggedUser);

        BillingInformationDTO billingInformationDTO = new BillingInformationDTO();
        modelMap.put("billingInformationDto", billingInformationDTO);
        return CART_PATH + "billing";
    }

    @PostMapping(value = "processOrder")
    public ModelAndView processOrder(@ModelAttribute("billingInformationDto") @Valid BillingInformationDTO billingInformationDTO, ModelMap modelMap,
                                     HttpSession session) {
        cart = (HashMap) session.getAttribute("cart");

        if (cart.size() == 0) {
            return new ModelAndView( "redirect:/error/404");
        }

        session.setAttribute("billingInformationDto", billingInformationDTO);

        User loggedUser = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        modelMap.put("loggedUser", loggedUser);
        modelMap.put("billingInformationDto", billingInformationDTO);
        modelMap.put("total", total(session));

        return new ModelAndView( CART_PATH + "confirm");
    }

    @GetMapping(value = "confirm")
    public String confirm(HttpSession session) {
        User loggedUser = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        cart = (HashMap) session.getAttribute("cart");

        if (cart.size() == 0) {
            return  "/errors/404";
        }

        Set<Product> products = new HashSet<>(cart.values());

        Order order = new Order(loggedUser, "PENDING");
        orderService.saveOrder(order);
        Set<OrderDetail> orderDetailSet = new HashSet<>();

        for (Product product : products) {
            OrderDetail orderDetail = new OrderDetail(order, product);
            orderDetailSet.add(orderService.createOrderDetail(orderDetail));
            productService.setPending(product.getId());
        }

        order.setOrderDetails(orderDetailSet);
        userService.addOrder(loggedUser, orderService.saveOrder(order));
        transactionService.createTransaction(order);

        cart.clear();

        session.setAttribute("cart", cart);

        return "redirect:/?orderSuccess";
    }

    @GetMapping(value = "buy/{id}")
    public String buy(@PathVariable("id") Long id, HttpSession session, ModelMap modelMap) {
        cart = (HashMap) session.getAttribute("cart");

        if(!cart.containsKey(id)) {
            cart.put(id, productService.find(id));
            session.setAttribute("cart", cart);

            BigDecimal subtotal = BigDecimal.ZERO;

            for (Product product : cart.values()) {
                subtotal = subtotal.add(product.getPrice());
            }

            session.setAttribute("subtotal", subtotal);

            modelMap.put("success", "success");
            return CART_PATH + "index :: resultsList";
        }

        modelMap.put("error", "error");
        return CART_PATH + "index :: resultsList";
    }

    @GetMapping(value = "remove/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void remove(@PathVariable("id") Long id, HttpSession session) {
        cart = (HashMap) session.getAttribute("cart");

        cart.remove(id);
        session.setAttribute("cart", cart);

//        return "redirect:../../cart";
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
