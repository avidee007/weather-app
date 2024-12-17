package com.mir.test.weatherservice.model.domain;

import com.mir.test.weatherservice.model.valueobject.CurrentWeather;
import com.mir.test.weatherservice.model.valueobject.LocationData;

public record WeatherData(LocationData location, CurrentWeather weather) {

}