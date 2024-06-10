package com.northcoders.recordshopapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.northcoders.recordshopapi.models")
public class RecordshopapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecordshopapiApplication.class, args);
	}

}
