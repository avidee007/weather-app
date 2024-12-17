package com.mir.test.weatherservice.model.valueobject;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CurrentWeather(
    @JsonProperty("last_updated")
    String lastUpdated,
    String weatherCondition,
    Temperature temperature,
    int humidity,
    int cloud,
    @JsonProperty(value = "uv_index")
    float uvIndex,
    Precipitation precipitation,
    Pressure pressure,
    Wind wind) {
}