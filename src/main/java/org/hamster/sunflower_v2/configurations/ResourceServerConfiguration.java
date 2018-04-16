package org.hamster.sunflower_v2.configurations;

import org.hamster.sunflower_v2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ONB-CZEIDE on 03/02/2018
 */
@EnableResourceServer
@Configuration
public class ResourceServerConfiguration extends WebSecurityConfigurerAdapter {

    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private UserService userService;

    @Autowired
    public ResourceServerConfiguration(@Lazy AuthenticationManager authenticationManager, UserDetailsService userDetailsService, @Lazy UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.requestMatchers()
//                .antMatchers("/oauth/authorize", "/login")
//                .antMatchers("/css/**", "/images/**", "/fonts/**", "/js/**")
//                    .and()
//                .authorizeRequests().anyRequest().authenticated()
//                    .and()
//                .formLogin().loginPage("/login").permitAll()
//                    .and()
//                .logout().permitAll();

        String[] anonymousResources = new String[]{
                "/", "/registration/**", "/verifyAccount",
                "/forgotPassword/**", "/resetPassword/**", "/login",
                "/profile/**", "/css/**", "/images/**",
                "/js/**", "/fonts/**", "/search/**",
                "/category/**", "/product/**"
        };
        http.authorizeRequests()
                .antMatchers(anonymousResources).permitAll()
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .antMatchers("/product/sell").hasAnyAuthority("SELLER")
                .antMatchers("/cart/**").hasAnyAuthority("BUYER", "ADMIN")
                .anyRequest().authenticated()
                    .and()
//                .formLogin().loginPage("/login").failureUrl("/login?error=incorrect").permitAll()
                .formLogin().loginPage("/login").failureHandler((request, response, exception) -> {
                    String username = request.getParameter("username");

                    String error = "incorrect";

                    error = userService.updateFailedAttempt(username);

//                    if(exception.getClass().isAssignableFrom(BadCredentialsException.class)){
//                        error = "email";
//                    } else {
//                        error = userService.updateFailedAttempt(username);
//                    }

                    response.sendRedirect("/login?error=" + error);
                }).successHandler(new MySavedRequestAwareAuthenticationSuccessHandler()).permitAll()
                    .and()
                .logout().logoutUrl("/logout").deleteCookies("JSESSIONID").invalidateHttpSession(true).permitAll()
                    .and()
                .rememberMe().key("AppKey").tokenValiditySeconds(604800);

        http.sessionManagement().maximumSessions(1).expiredUrl("/login?sessionExpired");

//        http.authorizeRequests()
//                .antMatchers("/", "/registration", "/oauth/authorize").permitAll()
//                    .antMatchers("/admin").hasRole("ADMIN")
//                    .antMatchers("/product/**").hasRole("BUYER")
//
//        http.exceptionHandling().accessDeniedPage("/");
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.parentAuthenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);

        auth.authenticationProvider(authProvider());
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private class MySavedRequestAwareAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

        private RequestCache requestCache = new HttpSessionRequestCache();
        private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

        public MySavedRequestAwareAuthenticationSuccessHandler() {
            super();
            setUseReferer(true);
        }

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
            String username = request.getParameter("username");
            StringBuilder url = new StringBuilder("https://localhost:8443/");
            boolean isAdmin = false;

            userService.resetFailedAttempt(username);

            for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                if (grantedAuthority.getAuthority().equals("ADMIN")) {
                    isAdmin = true;
                }
            }

            if (isAdmin) {
                url.append("admin");
            }

            SavedRequest savedRequest = requestCache.getRequest(request, response);

            if (savedRequest == null) {
//                String targetUrl = determineTargetUrl(request, response);
                String targetUrl = url.toString();

                if (response.isCommitted()) {
                    logger.debug("Response has already been committed. Unable to redirect to "
                            + targetUrl);
                    return;
                }

                redirectStrategy.sendRedirect(request, response, targetUrl);

                super.clearAuthenticationAttributes(request);
                return;
            }

            String targetUrlParameter = getTargetUrlParameter();
            if (isAlwaysUseDefaultTargetUrl()
                    || (targetUrlParameter != null && org.springframework.util.StringUtils.hasText(request
                    .getParameter(targetUrlParameter)))) {
                requestCache.removeRequest(request, response);
//                String targetUrl = determineTargetUrl(request, response);
                String targetUrl = url.toString();
                if (response.isCommitted()) {
                    logger.debug("Response has already been committed. Unable to redirect to "
                            + targetUrl);
                    return;
                }

                redirectStrategy.sendRedirect(request, response, targetUrl);
                super.clearAuthenticationAttributes(request);

                return;
            }

            clearAuthenticationAttributes(request);

            // Use the DefaultSavedRequest URL
            String targetUrl = savedRequest.getRedirectUrl();
            logger.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }
    }
}
