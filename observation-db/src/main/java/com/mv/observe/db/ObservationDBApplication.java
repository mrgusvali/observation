package com.mv.observe.db;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

import javax.sql.DataSource;

/** Configure a Spring app with REST & H2 persistence with a single database table for the event type */
@SpringBootApplication
public class ObservationDBApplication {

	public static void main(String[] args) {
		SpringApplication.run(ObservationDBApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder
				.setConnectTimeout(Duration.ofMillis(3000))
				.setReadTimeout(Duration.ofMillis(3000))
				.build();
	}
	
	@Bean
	public DataSource dataSource() {
	    return new EmbeddedDatabaseBuilder()
	      .setType(EmbeddedDatabaseType.H2)
	      .build();
	}
}
