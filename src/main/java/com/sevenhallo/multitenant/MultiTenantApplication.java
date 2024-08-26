package com.sevenhallo.multitenant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication

@ComponentScan(basePackages = {"com.sevenhallo.multitenant", "com.sevenhallo.example"})
@EnableJpaRepositories({"com.sevenhallo.multitenant", "com.sevenhallo.example"})
@EntityScan(basePackages = {"com.sevenhallo.multitenant", "com.sevenhallo.example"})
public class MultiTenantApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultiTenantApplication.class, args);
    }

}
