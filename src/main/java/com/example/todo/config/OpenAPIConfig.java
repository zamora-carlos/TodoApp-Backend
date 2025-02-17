package com.example.todo.config;

import io.swagger.v3.oas.models.servers.Server;
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
                        .title("TodoApp API")
                        .description("This API helps to manage your todo list.")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Carlos Zamora")
                                .email("czamora5@ucol.mx")
                                .url("https://carlos-zamora.netlify.app")))
                .addServersItem(new Server()
                        .description("Local environment")
                        .url("http://localhost:9090"));
    }
}


