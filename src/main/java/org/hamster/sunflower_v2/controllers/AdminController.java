package org.hamster.sunflower_v2.controllers;

import org.hamster.sunflower_v2.domain.models.Seed;
import org.hamster.sunflower_v2.domain.models.SeedDTO;
import org.hamster.sunflower_v2.domain.models.User;
import org.hamster.sunflower_v2.services.ProductService;
import org.hamster.sunflower_v2.services.SeedService;
import org.hamster.sunflower_v2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
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
import java.util.List;

/**
 * Created by ONB-CZEIDE on 02/27/2018
 */
@Controller
@RequestMapping(value = "admin")
public class AdminController {

    private UserService userService;
    private ProductService productService;
    private SeedService seedService;

    private static String ADMIN_PATH = "admin/";
    private static String SEED_PATH = "seed/";

    @Autowired
    public AdminController(UserService userService, ProductService productService, SeedService seedService) {
        this.userService = userService;
        this.productService = productService;
        this.seedService = seedService;
    }

    @GetMapping
    public String index(ModelMap modelMap) {
        User loggedUser = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<User> users = userService.findAll();
        users.remove(loggedUser);

        modelMap.put("users", users);
        modelMap.put("products", productService.findAll());
        modelMap.put("loggedUser", loggedUser);

        return ADMIN_PATH + "index";
    }

    @GetMapping(value = "/addSeed")
    public String addSeedForm(ModelMap modelMap) {
        User loggedUser = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        SeedDTO seedDTO = new SeedDTO();

        modelMap.put("loggedUser", loggedUser);
        modelMap.put("seedDTO", seedDTO);
        return ADMIN_PATH + "seed/add";
    }

    @PostMapping(value = "/addSeed")
    public ModelAndView addSeed(@ModelAttribute("seedDTO") @Valid SeedDTO seedDTO, BindingResult result,
                                    WebRequest request, Errors errors) {

        Seed seed = new Seed();

        if (!result.hasErrors()) {
            seed = seedService.registerSeed(seedDTO);
        }

        if (seed == null) {
            result.rejectValue("serialCode", "message.seedError");
        }

        if(!result.hasErrors()) {
            return new ModelAndView(ADMIN_PATH + SEED_PATH + "addConfirmation", "seed", seed);
        } else {
            return new ModelAndView(ADMIN_PATH + SEED_PATH + "add", "seedDTO", seedDTO);
        }
    }
}
