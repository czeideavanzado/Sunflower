package org.hamster.sunflower_v2.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ConcurrentModificationException;

@ControllerAdvice
public class GlobalExceptionHandler {

//    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ConcurrentModificationException.class)
    public void handleInternalServer(HttpServletResponse response, Exception e) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth instanceof AnonymousAuthenticationToken) {

                response.sendRedirect("/error/500");
                return;
            }

            for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
                if (authority.getAuthority().equals("ADMIN")) {
                    response.sendRedirect("/admin");
                    return;
                }
            }

            response.sendRedirect("/");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
