package org.hamster.sunflower_v2.controllers;

import org.hamster.sunflower_v2.domain.models.*;
import org.hamster.sunflower_v2.services.*;
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
    private TransactionService transactionService;
    private OrderService orderService;

    private static String ADMIN_PATH = "admin/";
    private static String SEED_PATH = "seed/";

    @Autowired
    public AdminController(OrderService orderService, UserService userService, ProductService productService, SeedService seedService, TransactionService transactionService) {
        this.userService = userService;
        this.productService = productService;
        this.seedService = seedService;
        this.transactionService = transactionService;
        this.orderService = orderService;
    }

    @GetMapping
    public String index(ModelMap modelMap) {
        User loggedUser = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<User> users = userService.findAll();
        users.remove(loggedUser);

        modelMap.put("users", userService.findAll());
        modelMap.put("products", productService.findAll());
        modelMap.put("seeds", seedService.findActiveSeeds());
        modelMap.put("transactions", transactionService.findAll());
        modelMap.put("details", orderService.findAllDetails());
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

    @PostMapping(value = "/buyer/{id}")
    public ModelAndView disableBuyer(@PathVariable("id") Long id, @ModelAttribute("user") @Valid User user, BindingResult result,
                                    WebRequest request, Errors errors) {

        if(!result.hasErrors()) {
            userService.disableBuyer(id);
            transactionService.cancelBuyerTransaction(id);
            return new ModelAndView("redirect:/admin");
        } else {
            return new ModelAndView("redirect:/admin");
        }
    }

    @PostMapping(value = "/seller/{id}")
    public ModelAndView disableSeller(@PathVariable("id") Long id, @ModelAttribute("user") @Valid User user, BindingResult result,
                                   WebRequest request, Errors errors) {

        if(!result.hasErrors()) {
            userService.disableSeller(id);
            productService.archiveSellerProduct(id);
            return new ModelAndView("redirect:/admin");
        } else {
            return new ModelAndView("redirect:/admin");
        }
    }

    @PostMapping(value = "/enableb/{id}")
    public ModelAndView enableBuyer(@PathVariable("id") Long id, @ModelAttribute("user") @Valid User user, BindingResult result,
                                     WebRequest request, Errors errors) {

        if(!result.hasErrors()) {
            userService.enableBuyer(id);
            return new ModelAndView("redirect:/admin");
        } else {
            return new ModelAndView("redirect:/admin");
        }
    }

    @PostMapping(value = "/enables/{id}")
    public ModelAndView enableSeller(@PathVariable("id") Long id, @ModelAttribute("user") @Valid User user, BindingResult result,
                                    WebRequest request, Errors errors) {

        if(!result.hasErrors()) {
            userService.enableSeller(id);
            return new ModelAndView("redirect:/admin");
        } else {
            return new ModelAndView("redirect:/admin");
        }
    }

    @PostMapping(value = "/disablea/{id}")
    public ModelAndView disableAdmin(@PathVariable("id") Long id, @ModelAttribute("user") @Valid User user, BindingResult result,
                                     WebRequest request, Errors errors) {

        if(!result.hasErrors()) {
            userService.disableAdmin(id);
            return new ModelAndView("redirect:/admin");
        } else {
            return new ModelAndView("redirect:/admin");
        }
    }

    @PostMapping(value = "/admine/{id}")
    public ModelAndView enableAdmin(@PathVariable("id") Long id, @ModelAttribute("user") @Valid User user, BindingResult result,
                                     WebRequest request, Errors errors) {

        if(!result.hasErrors()) {
            userService.enableAdmin(id);
            return new ModelAndView("redirect:/admin");
        } else {
            return new ModelAndView("redirect:/admin");
        }
    }

    @PostMapping(value = "/cancel/{id}")
    public ModelAndView cancelTransaction(@PathVariable("id") Long id, @ModelAttribute("transaction") @Valid Transaction transaction, BindingResult result,
                                          WebRequest request, Errors errors) {

        if(!result.hasErrors()) {
            transactionService.cancelTransaction(id);
            List<OrderDetail> details = orderService.findAllDetails();
            for(OrderDetail detail:details){
                if(detail.getOrder() == transactionService.findById(id).getTransactionOrder())
                    productService.setOpen(detail.getProduct().getId());
            }
            return new ModelAndView("redirect:/admin");
        } else {
            return new ModelAndView("redirect:/admin");
        }
    }

    @PostMapping(value = "/archive/{id}")
    public ModelAndView archiveProduct(@PathVariable("id") Long id, @ModelAttribute("product") @Valid Product product, BindingResult result,
                                          WebRequest request, Errors errors) {
        if(!result.hasErrors()) {
            productService.archiveProduct(id);
            return new ModelAndView("redirect:/admin");
        } else {
            return new ModelAndView("redirect:/admin");
        }
    }

    @PostMapping(value = "/unarchive/{id}")
    public ModelAndView unarchiveProduct(@PathVariable("id") Long id, @ModelAttribute("product") @Valid Product product, BindingResult result,
                                       WebRequest request, Errors errors) {
        if(!result.hasErrors()) {
            productService.unarchiveProduct(id);
            return new ModelAndView("redirect:/admin");
        } else {
            return new ModelAndView("redirect:/admin");
        }
    }
}
