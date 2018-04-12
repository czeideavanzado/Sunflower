package org.hamster.sunflower_v2.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by ONB-CZEIDE on 03/07/2018
 */
@Controller
@RequestMapping("/error")
public class ErrorController {

    public static String ERROR_PATH = "errors/";

    @GetMapping("/403")
    public String error403() {
        return ERROR_PATH + "403";
    }

    @GetMapping("/404")
    public String error404() {
        return ERROR_PATH + "404";
    }

    @GetMapping("/500")
    public String error500() {
        return ERROR_PATH + "500";
    }
}
