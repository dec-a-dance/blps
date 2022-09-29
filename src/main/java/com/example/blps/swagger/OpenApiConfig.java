package com.example.blps.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi(@Value("BLPS lab. Alexey Kuznechenkov, Dmitriy Makhnin.")String appDescription,
                                 @Value("dev")String appVersion) {
        return new OpenAPI().info(new Info().title("Chip & Dip API")
                        .version(appVersion)
                        .description(appDescription)
                        .license(new License().name("not licensed yet")
                                .url("http://localhost/"))
                        .contact(new Contact().name("Alexey \"dec-a-dance\" Kuznechenkov")
                                .url("https://t.me/dec_a_dance")));
    }
}
