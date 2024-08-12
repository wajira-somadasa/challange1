package com.weathermap.controller;

import com.weathermap.service.OpenWeatherMapService;
import com.weathermap.service.WeatherDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/weather")
public class WeatherMapController {

    @Autowired
    private OpenWeatherMapService openWeatherMapService;

    @Autowired
    private WeatherDataService weatherDataService;

    @GetMapping
    public ResponseEntity<String> getWeatherDescription(@RequestParam(required = true) String city,
                                                        @RequestParam(required = true) String country,
                                                        @RequestHeader("X-API-KEY") String apiKey) {

        // Check if the weather data is found in the database
        Optional<String> description = weatherDataService.getWeatherDescriptionFromDb(city, country);
        if ( !description.isPresent()) {
            // If not, retrieve from the opwenweather web service
            description = openWeatherMapService.getWeatherDescription(city, country, apiKey);
            // If data was received save to the database
            if (description.isPresent()) {
                weatherDataService.saveWeatherData(city, country, description.get());
            }
        }

        if (description.isPresent()) {
            return ResponseEntity.ok(description.get()); // Return cached data
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).build(); // Return cached data
        }
    }
}

