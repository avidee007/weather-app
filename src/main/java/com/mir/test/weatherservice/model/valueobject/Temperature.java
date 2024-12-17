package com.mir.test.weatherservice.model.valueobject;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Temperature(
    @JsonProperty(value = "degree_celsius")
    float celsius, float fahrenheit) {
}