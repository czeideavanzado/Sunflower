package org.hamster.sunflower_v2.controllers;

import org.hamster.sunflower_v2.domain.models.Seed;
import org.hamster.sunflower_v2.domain.models.SeedDTO;
import org.hamster.sunflower_v2.domain.models.User;
import org.hamster.sunflower_v2.exceptions.SeedDoesNotExistException;
import org.hamster.sunflower_v2.exceptions.SeedIsNotActiveException;
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

/**
 * Created by ONB-CZEIDE on 03/07/2018
 */
@Controller
@RequestMapping(value = "wallet")
public class WalletController {

    private UserService userService;
    private SeedService seedService;

    private static String WALLET_PATH = "wallet/";

    @Autowired
    public WalletController(UserService userService, SeedService seedService) {
        this.userService = userService;
        this.seedService = seedService;
    }

    @GetMapping
    public String index(ModelMap modelMap) {
        User loggedUser = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        SeedDTO seedDTO = new SeedDTO();

        modelMap.put("loggedUser", loggedUser);
        modelMap.put("seedDTO", seedDTO);
        return WALLET_PATH + "index";
    }

    @PostMapping(value = "/add")
    public ModelAndView addWallet(@ModelAttribute("seedDTO") @Valid SeedDTO seedDTO, BindingResult result,
                                  WebRequest request, Errors errors) {
        Seed seed = new Seed();

        if (!result.hasErrors()) {
            seed = addSeedToWallet(seedDTO, result);
        }

        if (seed == null) {
            return new ModelAndView("redirect:/?walletInvalid");
        }

        if(!result.hasErrors()) {
            return new ModelAndView("redirect:/?walletSuccess");
        } else {
            return new ModelAndView("redirect:/?walletError");
        }
    }

    private Seed addSeedToWallet(SeedDTO seedDTO, BindingResult result) {
        Seed seed;

        User loggedUser = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        try {
            seed = seedService.addToWallet(seedDTO, loggedUser.getId());
        } catch (SeedDoesNotExistException | SeedIsNotActiveException e) {
            return null;
        }

        return seed;
    }
}
