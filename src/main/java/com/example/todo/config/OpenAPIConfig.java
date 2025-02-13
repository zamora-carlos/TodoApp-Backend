package com.example.todo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ToDo API")
                        .description("This API helps to manage your ToDo list.")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Your Name")
                                .email("your-email@example.com")
                                .url("your-website.com"))
                        .termsOfService("Terms of service URL")
                        .license(new io.swagger.v3.oas.models.info.License()
                                .name("License")
                                .url("License URL")));
    }
}


