package com.weathermap.repository;

import com.weathermap.model.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;


@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {

    Optional<WeatherData> findByCity(String city);
    Optional<WeatherData> findByCityAndCountry(String city, String country);
    Optional<WeatherData> findByCityAndStateAndCountry(String city, String state, String country);

//    public void saveWeatherData(String city, String country, String description) {
//        repository.save(new WeatherData(city, country, description));
//    }
}