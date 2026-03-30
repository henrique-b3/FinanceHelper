package com.app.FinanceHelper.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FinanceHelper API 💰")
                        .version("1.0.0")
                        .description("API RESTful para gestão de despesas pessoais e controlo financeiro."));
    }
}