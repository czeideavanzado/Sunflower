package org.hamster.sunflower_v2;

import org.hamster.sunflower_v2.services.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class SunflowerV2Application implements CommandLineRunner {

	@Resource
	StorageService storageService;

	public static void main(String[] args) {
		SpringApplication.run(SunflowerV2Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		storageService.deleteAll();
		storageService.init();
	}
}
