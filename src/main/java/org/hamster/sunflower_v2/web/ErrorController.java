package org.hamster.sunflower_v2.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ONB-CZEIDE on 02/20/2018
 */
@RestController
public class ErrorController implements org.springframework.boot.autoconfigure.web.ErrorController{

    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public void error(HttpServletResponse response) throws IOException {
        int status = response.getStatus();
        switch (status) {
            case 401:
                response.sendRedirect("/401");
                break;
            case 403:
                response.sendRedirect("/403");
                break;
            default:
                response.sendRedirect("/404");
        }
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
