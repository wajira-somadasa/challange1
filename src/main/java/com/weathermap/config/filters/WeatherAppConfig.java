package com.weathermap.config.filters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WeatherAppConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder, @Value("${openweathermap.api.url}") String apiUrl) {
        return builder
                .rootUri(apiUrl) // Set base URL from properties
                .build();
    }
}