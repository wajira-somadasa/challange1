package com.weathermap.controller;

import com.weathermap.service.OpenWeatherMapService;
import com.weathermap.service.WeatherDataService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WeatherMapControllerTest {
    @Mock
    private OpenWeatherMapService openWeatherMapService;

    @Mock
    private WeatherDataService weatherDataService;

    @InjectMocks
    private WeatherMapController weatherController;

    @Test
    public void testGetWeatherDescriptionCachedData() {
        String city = "London";
        String country = "uk";
        String apiKey = "1bc1f676cc973d0c3e73278483ffdc00";
        String expectedDescription = "{\"coord\":{\"lon\":-80.6081,\"lat\":28.0836},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"base\":\"stations\",\"main\":{\"temp\":24.77,\"feels_like\":25.65,\"temp_min\":23.91,\"temp_max\":26.13,\"pressure\":1017,\"humidity\":90,\"sea_level\":1017,\"grnd_level\":1017},\"visibility\":10000,\"wind\":{\"speed\":3.6,\"deg\":230},\"clouds\":{\"all\":0},\"dt\":1723428236,\"sys\":{\"type\":1,\"id\":4922,\"country\":\"US\",\"sunrise\":1723373438,\"sunset\":1723421077},\"timezone\":-14400,\"id\":4163971,\"name\":\"Melbourne\",\"cod\":200}";

        when(weatherDataService.getWeatherDescriptionFromDb(city, country)).thenReturn(Optional.of(expectedDescription));

        ResponseEntity<String> response = weatherController.getWeatherDescription(city, country, apiKey);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDescription, response.getBody());
        // Verify that OpenWeatherMapService was not called
        Mockito.verify(openWeatherMapService, Mockito.never()).getWeatherDescription(city, country, apiKey);
    }

    @Test
    public void testGetWeatherDescriptionCachedDataNotFound() {
        String city = "New York";
        String country = "US";
        String apiKey = "validApiKey";
        String expectedDescription = "Sunny";

        when(weatherDataService.getWeatherDescriptionFromDb(city, country)).thenReturn(Optional.empty());
        when(openWeatherMapService.getWeatherDescription(city, country, apiKey)).thenReturn(Optional.of(expectedDescription));

        ResponseEntity<String> response = weatherController.getWeatherDescription(city, country, apiKey);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDescription, response.getBody());
        // Verify both services were called
        Mockito.verify(weatherDataService, Mockito.times(1)).getWeatherDescriptionFromDb(city, country);
        Mockito.verify(openWeatherMapService, Mockito.times(1)).getWeatherDescription(city, country, apiKey);
    }

    @Test
    public void testGetWeatherDescriptionApiError() {
        String city = "Tokyo";
        String country = "JP";
        String apiKey = "1bc1f676cc973d0c3e73278483ffdc00";

        when(weatherDataService.getWeatherDescriptionFromDb(city, country)).thenReturn(Optional.empty());
        when(openWeatherMapService.getWeatherDescription(city, country, apiKey)).thenReturn(Optional.empty());

        ResponseEntity<String> response = weatherController.getWeatherDescription(city, country, apiKey);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        //assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

    }

    @Test
    public void testGetWeatherDescriptionInvalidApiKey() {
        String city = "Paris";
        String country = "FR";
        String apiKey = "invalidApiKey";

        ResponseEntity<String> response = weatherController.getWeatherDescription(city, country, apiKey);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        //assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
