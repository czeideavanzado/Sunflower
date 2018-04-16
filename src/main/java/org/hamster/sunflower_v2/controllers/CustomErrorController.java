//package org.hamster.sunflower_v2.controllers;
//
//import org.springframework.boot.autoconfigure.web.ErrorController;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * Created by ONB-CZEIDE on 03/07/2018
// */
//@Controller
////@RequestMapping("/error")
//public class CustomErrorController implements ErrorController {
//
//    public static String ERROR_PATH = "errors/";
//
//    @RequestMapping(value = "/error")
//    String error(HttpServletRequest request, HttpServletResponse response) {
//        // Appropriate HTTP response code (e.g. 404 or 500) is automatically set by Spring.
//        // Here we just define response body.
//        if (response.getStatus() == 403) {
//            return "errors/403";
//        } else if (response.getStatus() == 404) {
//            return  "errors/404";
//        } else if (response.getStatus() == 500){
//            return  "errors/500";
//        }
//
//        return "errors/404";
//    }
//
//    @Override
//    public String getErrorPath() {
//        return "/error";
//    }
//}
