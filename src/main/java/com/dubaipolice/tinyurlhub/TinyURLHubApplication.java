package com.dubaipolice.tinyurlhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TinyURLHubApplication {

	public static void main(String[] args) {
	    System.setProperty("server.servlet.context-path", "/tinyURLHub");
		SpringApplication.run(TinyURLHubApplication.class, args);
	}

}
