package com.mariam.BankingApplication;

import com.mariam.BankingApplication.config.AccountConfiguration;
import com.mariam.BankingApplication.model.AccountUser;
import com.mariam.BankingApplication.model.Role;
import com.mariam.BankingApplication.service.AccountUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableAsync
@SpringBootApplication
public class BankingApplication implements CommandLineRunner {

	@Autowired
	private AccountUserService accountUserService;

	@Autowired
	private AccountConfiguration accountConfiguration;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(BankingApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		PasswordEncoder encoder = accountConfiguration.passwordEncoder();

		AccountUser adminUser = new AccountUser();
		adminUser.setFirstName("Admin");
		adminUser.setLastName("Admin");
		adminUser.setRole(Role.ADMIN);
		adminUser.setUsername("admin@gmail.com");
		adminUser.setPhoneNumber("08022445566");
		adminUser.setPassword(passwordEncoder.encode("password"));

		AccountUser exist = accountUserService.getByUsername("admin@gmail.com").getBody();

		if( exist == null ){
			accountUserService.postAccountUser(adminUser);
		}

	}
}
