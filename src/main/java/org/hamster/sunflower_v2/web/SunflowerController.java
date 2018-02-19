package org.hamster.sunflower_v2.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by ONB-CZEIDE on 02/19/2018
 */
@Controller
public class SunflowerController {

    @GetMapping(value = "/")
    public String index() {
        return "index";
    }

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    @GetMapping(value = "/registration")
    public String register() {
        return "register";
    }
}
