package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class TestMvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestMvcApplication.class, args);
	}
}

@Configuration
class MvcConfig extends WebMvcConfigurerAdapter {
	public MvcConfig () {
		System.out.println("%%%%%%%%%%%%% " + getClass() + " loaded %%%%%%%%%%%%%%%");
	}

	@Bean
	public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
	    ServletRegistrationBean registration = new ServletRegistrationBean(
	            dispatcherServlet);
	    System.out.println("%%%%%%%%%%%%%%%%% Adding dispatcher servletmapping");
	    registration.addUrlMappings("/");
	    return registration;
	}
}

@Controller
class TestController {
	@RequestMapping("test")
	public String test() {
		return "test";
	}
}