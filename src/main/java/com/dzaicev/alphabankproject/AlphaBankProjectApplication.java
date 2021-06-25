package com.dzaicev.alphabankproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = {"com.dzaicev.alphabankproject.feign",
        "com.dzaicev.alphabankproject.controllers"})
public class AlphaBankProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlphaBankProjectApplication.class, args);
    }

}
