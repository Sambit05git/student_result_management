package com.srms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.srms.models.Admin;
import com.srms.repository.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SrmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SrmsApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (adminRepository.count() == 0) {
				Admin admin = new Admin();
				admin.setUsername("admin");
				admin.setPassword(passwordEncoder.encode("Admin@123"));
				admin.setName("Super Admin");
				admin.setEmail("admin@srms.com");
				admin.setContactNumber(9999999999L);
				admin.setDepartment("Computer");
				adminRepository.save(admin);
				System.out.println("====== DEFAULT ADMIN SEEDED: admin / admin ======");
			}
		};
	}
}
