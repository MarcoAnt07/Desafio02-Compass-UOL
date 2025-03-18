package com.bugrunners.microsservicob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MicrosservicobApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicrosservicobApplication.class, args);
	}

}
