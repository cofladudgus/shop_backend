package com.shopping.mall.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.shopping.mall", "com.shopping.mall.core"})
public class ShoppingMallSystemApiServerApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ShoppingMallSystemApiServerApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ShoppingMallSystemApiServerApplication.class, args);
    }
}
