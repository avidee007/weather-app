package com.mir.test.weatherservice.service;

import com.mir.test.weatherservice.model.domain.WeatherData;
import com.mir.test.weatherservice.model.domain.WeatherHistory;
import com.mir.test.weatherservice.model.valueobject.WeatherRequest;

public interface WeatherService {

  WeatherData getCurrentWeather(WeatherRequest request);

  WeatherHistory getWeatherHistoryByPostalCode(String postalCode);

  WeatherHistory getWeatherHistoryByUserName(String userName);

}