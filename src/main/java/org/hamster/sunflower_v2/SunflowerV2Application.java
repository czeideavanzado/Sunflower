package org.hamster.sunflower_v2;

import org.hamster.sunflower_v2.domain.models.*;
import org.hamster.sunflower_v2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@SpringBootApplication
public class SunflowerV2Application {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(SunflowerV2Application.class, args);
	}

	@Autowired
	public void authenticationManager(AuthenticationManagerBuilder builder, UserRepository userRepository, UserService userService) throws Exception {
		if (userRepository.count()==0)
			userService.save(new User("admin", "adminPassword", "", ""));
		builder.userDetailsService(userDetailsService(userRepository)).passwordEncoder(passwordEncoder);
	}

	private UserDetailsService userDetailsService(final UserRepository userRepository) {
		return username -> new CustomUserDetails(userRepository.findByUsername(username));
	}
}
