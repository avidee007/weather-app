package com.mir.test.weatherservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.mir.test.weatherservice.config.WeatherApiConfigs;
import com.mir.test.weatherservice.exception.WeatherApiException;
import com.mir.test.weatherservice.model.valueobject.Condition;
import com.mir.test.weatherservice.model.valueobject.Current;
import com.mir.test.weatherservice.model.valueobject.Location;
import com.mir.test.weatherservice.model.valueobject.Weather;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class WeatherApiExternalServiceTest {

  @Mock
  private RestTemplate restTemplate;

  @Mock
  private WeatherApiConfigs configs;

  @InjectMocks
  private WeatherApiExternalService externalService;

  @Test
  void test_getCurrentWeatherFromWeatherApi_success() {
    var mockBaseUri = "https://mock.weatherapi.com";
    when(configs.getBaseUri()).thenReturn(mockBaseUri);
    when(configs.getApiKey()).thenReturn("12345apikey");

    var fullUri = mockBaseUri+"?q=98100&key=12345apikey";


    Weather weather = getMockWeather();
    var response = new ResponseEntity<>(weather, HttpStatus.OK);

    when(restTemplate.getForEntity(fullUri, Weather.class)).thenReturn(response);


    Weather result = externalService.getCurrentWeatherFromWeatherApi("98100");

    assertNotNull(result);
    assertEquals(result, response.getBody());
  }

  @Test
  void test_getCurrentWeatherFromWeatherApi_failure() {
    var mockBaseUri = "https://mock.weatherapi.com";
    when(configs.getBaseUri()).thenReturn(mockBaseUri);
    when(configs.getApiKey()).thenReturn("12345apikey");

    var fullUri = mockBaseUri+"?q=98100&key=12345apikey";

    when(restTemplate.getForEntity(fullUri, Weather.class)).thenThrow(new RestClientException("Rest exception"));


    var ex = assertThrows(WeatherApiException.class, () -> externalService.getCurrentWeatherFromWeatherApi("98100"));

    assertTrue(ex.getMessage().contains("Rest exception"));
  }


  private Weather getMockWeather() {
    var location = new Location("New York", "New York", "USA", 40.752, -73.9945,
        "America/New_York", 1734025174, "2024-12-12 12:39");
    var current = new Current(1734024600, "2024-12-12 12:30", 3.9f, 39, 1,
        new Condition("Sunny", "//cdn.weatherapi.com/weather/64x64/day/113.png", 1000), 24, 20.6f,
        33.1f, 251.0f, "WSW", 1020, 30.11f, 0.0f, 0.0f, 0, 1.4f);
    return new Weather(location, current);

  }
}