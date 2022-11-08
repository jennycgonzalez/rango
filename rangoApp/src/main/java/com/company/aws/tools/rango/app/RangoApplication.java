package com.company.aws.tools.rango.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.company.aws.tools")
public class RangoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RangoApplication.class, args);
	}

}
