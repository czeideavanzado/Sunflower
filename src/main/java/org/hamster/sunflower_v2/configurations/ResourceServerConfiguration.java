package org.hamster.sunflower_v2.configurations;

import org.hamster.sunflower_v2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import javax.servlet.http.HttpServletResponse;

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
        };
        http.authorizeRequests()
                .antMatchers(anonymousResources).permitAll()
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .antMatchers("/product/**").hasAnyAuthority("SELLER", "BUYER", "ADMIN")
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
                }).successHandler((request, response, authentication) -> {
                    String username = request.getParameter("username");
                    boolean isAdmin = false;

                    response.setStatus(HttpServletResponse.SC_OK);
                    userService.resetFailedAttempt(username);

                    for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                        if (grantedAuthority.getAuthority().equals("ADMIN")) {
                            isAdmin = true;
                        }
                    }

                    if (isAdmin) {
                        response.sendRedirect("/admin");
                    } else {
                        response.sendRedirect("/");
                    }
                }).permitAll()
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
}
