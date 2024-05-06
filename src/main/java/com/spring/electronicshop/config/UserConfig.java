package com.spring.electronicshop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.spring.electronicshop.repository.CategoriesRepository;
import com.spring.electronicshop.repository.ProductsRepository;
import com.spring.electronicshop.repository.RoleRepository;
import com.spring.electronicshop.repository.UserRepository;
import com.spring.electronicshop.storage.StorageService;

@Configuration
public class UserConfig {
	@Autowired
	PasswordEncoder passwordEncoder;

	@Bean
	CommandLineRunner commandLineRunner(UserRepository repository, ProductsRepository productsRepository,
			CategoriesRepository categoriesRepository, RoleRepository roleRepository, StorageService storageService) {
		return arg -> {
			storageService.init();
		};
	}
}
