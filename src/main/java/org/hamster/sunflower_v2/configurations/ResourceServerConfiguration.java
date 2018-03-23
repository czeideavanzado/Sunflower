package org.hamster.sunflower_v2.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * Created by ONB-CZEIDE on 03/02/2018
 */
@EnableResourceServer
@Configuration
public class ResourceServerConfiguration extends WebSecurityConfigurerAdapter {

    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;

    @Autowired
    public ResourceServerConfiguration(@Lazy AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
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
                "/", "/registration", "/verifyAccount", "/login", "/profile/**", "/css/**", "/images/**","/js/**","/fonts/**", "/search/**"
        };

        http.authorizeRequests()
                .antMatchers(anonymousResources).permitAll()
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .antMatchers("/product/**", "/cart/**").hasAuthority("BUYER")
                .anyRequest().authenticated()
                    .and()
                .formLogin().loginPage("/login").permitAll()
                    .and()
                .logout().permitAll();

//        http.authorizeRequests()
//                .antMatchers("/", "/registration", "/oauth/authorize").permitAll()
//                    .antMatchers("/admin").hasRole("ADMIN")
//                    .antMatchers("/product/**").hasRole("BUYER")
//
        http.exceptionHandling().accessDeniedPage("/403");
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

    //    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(authProvider());
//    }
}
