package com.db.challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Francisco San Roman
 *
 */
@SpringBootApplication
public class ShopsApiApplication {

	public static final Logger logger = LoggerFactory.getLogger(ShopsApiApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ShopsApiApplication.class, args);
	}
}
