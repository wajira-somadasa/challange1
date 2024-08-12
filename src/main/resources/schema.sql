CREATE TABLE weather_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    city VARCHAR(100),
    state VARCHAR(100),
    country VARCHAR(100),
    weather_description VARCHAR(2500)
);