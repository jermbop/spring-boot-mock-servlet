package com.jhealy;

import com.jhealy.spring.CustomResponseErrorHandler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MainClass {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MainClass.class, args);
    }

    @Bean
    RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.setErrorHandler(customResponseErrorHandler());
        return restTemplate;
    }

    @Bean
    CustomResponseErrorHandler customResponseErrorHandler() {
        return new CustomResponseErrorHandler();
    }
}
