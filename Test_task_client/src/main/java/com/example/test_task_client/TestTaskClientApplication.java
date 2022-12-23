package com.example.test_task_client;

import com.example.test_task_client.service.RestBalanceService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootApplication
public class TestTaskClientApplication {

    private static String url = "http://localhost:8080/api/balance/";

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(TestTaskClientApplication.class, args);

        RestBalanceService balanceService = context.getBean(RestBalanceService.class);

        balanceService.test();
    }



    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
