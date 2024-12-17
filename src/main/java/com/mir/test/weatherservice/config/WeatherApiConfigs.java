package com.mir.test.weatherservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
@ConfigurationProperties(prefix = "weather.api")
public class WeatherApiConfigs {
  private String baseUri;
  private String apiKey;
}