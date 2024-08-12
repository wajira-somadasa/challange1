package com.weathermap.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weathermap.exceptions.ApiKeyInvalidException;
import com.weathermap.exceptions.WeatherNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class OpenWeatherMapService {

    @Autowired
    private Environment env;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String apiUrl = "http://api.openweathermap.org/data/2.5/weather";
    private static final Set<String> VALID_API_KEYS = new HashSet<>();

    @Value("${openweathermap.api.key1}")
    private String apiKey1;
    @Value("${openweathermap.api.key2}")
    private String apiKey2;
    @Value("${openweathermap.api.key3}")
    private String apiKey3;
    @Value("${openweathermap.api.key4}")
    private String apiKey4;
    @Value("${openweathermap.api.key5}")
    private String apiKey5;

    @Autowired
    private RestTemplate restTemplate;

    public Optional<String> getWeatherDescription(String city, String country, String apiKey) {

        if (VALID_API_KEYS.size() == 0) {
            VALID_API_KEYS.add(env.getProperty("openweathermap.api.key1"));
            VALID_API_KEYS.add(env.getProperty("openweathermap.api.key2"));
            VALID_API_KEYS.add(env.getProperty("openweathermap.api.key3"));
            VALID_API_KEYS.add(env.getProperty("openweathermap.api.key4"));
            VALID_API_KEYS.add(env.getProperty("openweathermap.api.key5"));
        }

        if (!VALID_API_KEYS.contains(apiKey)) {
            throw new ApiKeyInvalidException("Invalid API Key : " + apiKey);
        }
        String url = buildUrl(city, country, apiKey);
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return Optional.ofNullable(response.getBody());
        } catch (RestClientResponseException e) {
            // Handle potential errors during API call
            throw new WeatherNotFoundException(e.getMessage());
        }
    }

    private String buildUrl(String city, String country, String apiKey) {
        StringBuilder urlBuilder = new StringBuilder(apiUrl);
        urlBuilder.append("?q=").append(city).append(",").append(country);
        urlBuilder.append("&appid=").append(apiKey);
        urlBuilder.append("&units=metric"); // Optional: to get temperature in Celsius

        return urlBuilder.toString();
    }
}
