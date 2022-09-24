package com.aupas.wallet;

import com.aupas.wallet.services.WalletService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WalletApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletApplication.class, args);
	}

	@Bean
	CommandLineRunner start(WalletService walletService){
		return args -> {
			walletService.loadData();
		};
	}
}
