package org.hamster.sunflower_v2.controllers;

import org.hamster.sunflower_v2.domain.models.Product;
import org.hamster.sunflower_v2.domain.models.User;
import org.hamster.sunflower_v2.domain.models.UserDTO;
import org.hamster.sunflower_v2.services.ProductService;
import org.hamster.sunflower_v2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by ONB-CZEIDE on 02/19/2018
 */
@Controller
@RequestMapping(value = "/")
public class SunflowerController {

    private UserService userService;
    private ProductService productService;

    @Autowired
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public SunflowerController(UserService userService, ProductService productService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.productService = productService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String index(ModelMap modelMap, Authentication authentication) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedUser = userService.findByUsername(authentication.getName());

        modelMap.put("products", productService.findAll());
        modelMap.put("loggedUser", loggedUser);
        return "index";
    }

    @GetMapping(value = "/login")
    public String loginForm(HttpSession session, Authentication auth) {
        auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken)) {

            /* The user is logged in :) */
            return "redirect: ";
        }

        if(session.getAttribute("cart") == null) {
            Map<Long, Product> cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }

        return "login";
    }

    @GetMapping(value = "/registration")
    public String showRegistrationForm(Model model, Authentication auth) {
        auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken)) {

            /* The user is logged in :) */
            return "redirect: ";
        }

        UserDTO userDto = new UserDTO();
        model.addAttribute("user", userDto);
        return "registration";
    }

    @GetMapping(value = "/forgotPassword")
    public String forgotPasswordForm(Authentication auth) {
        auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken)) {

            /* The user is logged in :) */
            return "redirect: ";
        }

        return "forgotPassword";
    }

    @GetMapping(value = "profile/{id}")
    public String profile(@PathVariable("id") Long id, ModelMap modelMap, Authentication authentication) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedUser = userService.findByUsername(authentication.getName());
        User user = userService.findById(id);

        modelMap.put("loggedUser", loggedUser);
        modelMap.put("user", user);
        return "profile";
    }

    @GetMapping(value = "/reauthorize")
    public String reAuthorize(ModelMap modelMap, Authentication authentication) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedUser = userService.findByUsername(authentication.getName());

        modelMap.put("loggedUser", loggedUser);
        return "reAuthorize";
    }

    @PostMapping(value = "/reauthorize")
    public String reAuthorizePOST(@RequestParam("password") String password, Authentication authentication) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(authentication.getName(), password);
        Authentication authenticate = authenticationManager.authenticate(token);

        if(authenticate.isAuthenticated() && isCurrentUser(authentication, authenticate)) {
            return "redirect:/cart/confirm";
        }

        return "/reauthorize?invalid";
    }

    private boolean isCurrentUser(Authentication left, Authentication right) {
        return left.getPrincipal().equals(right.getPrincipal());
    }
}
