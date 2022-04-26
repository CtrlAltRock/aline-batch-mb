package com.smoothstack.transactionbatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TransactionbatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionbatchApplication.class, args);
	}

}
