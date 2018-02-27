package org.hamster.sunflower_v2;

import org.hamster.sunflower_v2.domain.models.*;
import org.hamster.sunflower_v2.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SunflowerV2Application {

	public static void main(String[] args) {
		SpringApplication.run(SunflowerV2Application.class, args);
	}
}
