package com.supermarket.supermarket_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI supermarketOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("supermarket-api")
                        .description("REST API for managing supermarket sales, products and branches.")
                        .version("v1"));
    }
}
