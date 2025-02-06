package com.cyanapp.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(title = "Category check application", version = "1.0", description = "Cyan assignment"))
@SpringBootApplication
public class CategoryCheckApplication {
    private static final Logger logger = LogManager.getLogger(CategoryCheckApplication.class);

    public static void main(String[] args) {
        //logger.debug("application is running...");
        SpringApplication.run(CategoryCheckApplication.class, args);
    }

}
