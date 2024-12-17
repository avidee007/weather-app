package com.mir.test.weatherservice.model.valueobject;

public record LocationData(String postalCode, String name, String region, String country,
                           String localtime) {
}