package com.example.Project.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

import static java.awt.SystemColor.info;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Restaurant/Hotel Management API",
                version = "1.0",
                description = "APIs for Orders, Bookings, Payments, and Menu Management"
        )
)
public class SwaggerConfig {
}
