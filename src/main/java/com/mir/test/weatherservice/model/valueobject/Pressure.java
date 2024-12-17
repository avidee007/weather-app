package com.mir.test.weatherservice.model.valueobject;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Pressure(int millibars,
                       @JsonProperty(value = "inches_of_mercury")
                       float inchesOfMercury) {
}