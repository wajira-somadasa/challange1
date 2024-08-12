package com.weathermap.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class WeatherData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;

    // openweathermap supports state as well
    private String state;
    String country;
    @Column(name = "weather_description")
    private String weatherDescription;

    public WeatherData() {}

    public WeatherData(String city, String country, String weatherDescription) {
        this.city = city;
        this.country = country;
        this.weatherDescription = weatherDescription;
    }

    // Constructor with all fields
    public WeatherData(String city, String state, String country, String weatherDescription) {
        this.city = city;
        this.state = state;
        this.country = country;
        this.weatherDescription = weatherDescription;
    }

    private WeatherData(Builder builder) {
        this.city = builder.city;
        this.state = builder.state;
        this.country = builder.country;
        this.weatherDescription = builder.weatherDescription;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public static class Builder {
        private String city;
        private String state;
        private String country;
        private String weatherDescription;

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder state(String state) {
            this.state = state;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder weatherDescription(String weatherDescription) {
            this.weatherDescription = weatherDescription;
            return this;
        }

        public WeatherData build() {
            return new WeatherData(this);
        }
    }

}