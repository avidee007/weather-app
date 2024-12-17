package com.mir.test.weatherservice.model.valueobject;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Wind(
    @JsonProperty(value = "speed_mph")
    float speedMph,
    @JsonProperty(value = "speed_kph")
    float speedKph,
    float degree,
    String direction) {
}