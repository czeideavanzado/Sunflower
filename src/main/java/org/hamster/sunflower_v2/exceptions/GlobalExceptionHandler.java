package org.hamster.sunflower_v2.exceptions;

import org.hamster.sunflower_v2.domain.models.User;
import org.hamster.sunflower_v2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ControllerAdvice
public class GlobalExceptionHandler {

//    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @Autowired
    private UserService userService;

//    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public void handleInternalServer(HttpServletResponse response, Exception e) {
//        log.error("Unhandled exception occurred", e);
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

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(Exception.class)
    public void handleForbiddenAccess(HttpServletResponse response, Exception e) {
        try {
            response.sendRedirect("/error/403");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
