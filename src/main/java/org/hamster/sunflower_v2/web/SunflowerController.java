package org.hamster.sunflower_v2.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ONB-CZEIDE on 02/19/2018
 */
@Controller
public class SunflowerController {

    @GetMapping(value = "/")
    public String index() {
        return "development/index";
    }

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    @GetMapping(value = "/registration")
    public String register() {
        return "register";
    }

    @GetMapping(value = "/home")
    public String home() {
        return "index";
    }

    @RequestMapping("404")
    public String get404ErrorPage() {
        return "error/404";
    }

    @RequestMapping("401")
    public String get401ErrorPage() {
        return "error/401";
    }
}
