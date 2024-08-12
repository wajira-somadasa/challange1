package com.weathermap.service;

import com.weathermap.model.WeatherData;
import com.weathermap.repository.WeatherDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WeatherDataService {
    @Autowired
    private WeatherDataRepository repository;

    public Optional<String> getWeatherDescriptionFromDb(String city, String country) {
        return repository.findByCityAndCountry(city, country).map(WeatherData::getWeatherDescription);
    }

    public void saveWeatherData(String city, String country, String description) {
        repository.save(new WeatherData(city, country, description));
    }
}

