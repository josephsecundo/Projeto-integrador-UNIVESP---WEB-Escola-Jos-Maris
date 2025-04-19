package com.example.escola.escola_web_site;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EscolaWebSiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(EscolaWebSiteApplication.class, args);
	}

}
