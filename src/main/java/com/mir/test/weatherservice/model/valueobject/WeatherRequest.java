package com.mir.test.weatherservice.model.valueobject;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record WeatherRequest(

    @NotBlank(message = "userName can not be null or empty.")
    String userName,

    @NotNull(message = "postalCode/zipCode can not be null or empty.")
    @Pattern(regexp = "^\\d{5}(?:-\\d{4})?$", message = "Not a valid postalCode/ZipCode pattern")
    String postalCode) {
}