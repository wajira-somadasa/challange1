package com.weathermap;

import com.weathermap.config.filters.ApiKeyRateLimitFilter;
import com.weathermap.service.ApiKeyRateLimiterService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WeatherMapApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherMapApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean<ApiKeyRateLimitFilter> apiKeyRateLimitingFilter(ApiKeyRateLimiterService rateLimiterService) {
		FilterRegistrationBean<ApiKeyRateLimitFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new ApiKeyRateLimitFilter(rateLimiterService));
		registrationBean.addUrlPatterns("/weather/*"); // This should be adequate if we do not use other API providers other than this weather service
		registrationBean.setName("WeatherMapFilter");
		registrationBean.setOrder(1); // Optional: as we have one filter only

		return registrationBean;
	}
}
