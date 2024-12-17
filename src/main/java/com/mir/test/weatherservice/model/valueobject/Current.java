package com.mir.test.weatherservice.model.valueobject;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Current(
    @JsonProperty(value = "last_updated_epoch")
    long lastUpdatedEpoch,
    @JsonProperty(value = "last_updated")
    String lastUpdated,
    @JsonProperty(value = "temp_c")
    float tempCelsius,
    @JsonProperty(value = "temp_f")
    float tempFahrenheit,
    @JsonProperty(value = "is_day")
    int isDay,
    Condition condition,
    int humidity,
    @JsonProperty(value = "wind_mph")
    float windMph,
    @JsonProperty(value = "wind_kph")
    float windKph,
    @JsonProperty(value = "wind_degree")
    float windDegree,
    @JsonProperty(value = "wind_dir")
    String windDirection,
    @JsonProperty(value = "pressure_mb")
    int pressureMb,
    @JsonProperty(value = "pressure_in")
    float pressureIn,
    @JsonProperty(value = "precip_mm")
    float precipitationMm,
    @JsonProperty(value = "precip_in")
    float precipitationIn,
    int cloud,
    float uv) {
}