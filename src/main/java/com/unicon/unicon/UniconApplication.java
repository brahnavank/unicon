package com.unicon.unicon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.unicon.unicon.property.FileStorageProperties;

/**
 * Main class init spring boot application
 * @author brahnavan
 *
 */
@SpringBootApplication
@EnableConfigurationProperties({
    FileStorageProperties.class
})
public class UniconApplication {

	/**
	 * Program starts from here
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(UniconApplication.class, args);
	}
}
