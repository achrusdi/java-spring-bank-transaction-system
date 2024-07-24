package com.ilu.spring_starter;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.ilu.spring_starter.entities.Role;
import com.ilu.spring_starter.entities.User;
import com.ilu.spring_starter.services.RoleService;
import com.ilu.spring_starter.services.UserService;

@SpringBootApplication
public class Application {
	
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> {
			Set<Role> roles = new HashSet<>();
			roles.add(Role.builder().name("admin").description("Admin role").isActive(true).build());
			roles.add(Role.builder().name("general").description("General role").isActive(true).build());

			for (Role role : roles) {
				if (roleService.findByName(role.getName()) == null) {
					roleService.create(role);
				}
			}

			User admin = userService.findByUsernameOrEmail("admin");

			if (admin == null) {
				admin = User.builder().username("admin").email("admin@gmail.com").password("admin").isActive(true).build();
				userService.create(admin, new String[] { "admin" });
			}
		};
	}

}
