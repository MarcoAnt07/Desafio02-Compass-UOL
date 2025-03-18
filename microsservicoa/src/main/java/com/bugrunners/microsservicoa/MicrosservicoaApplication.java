package com.bugrunners.microsservicoa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MicrosservicoaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicrosservicoaApplication.class, args);
	}

}
