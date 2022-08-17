package com.revlis1353.rebootspec.rebootspec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class RebootspecApplication extends SpringBootServletInitializer{

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
		return application.sources(RebootspecApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(RebootspecApplication.class, args);
	}

}
