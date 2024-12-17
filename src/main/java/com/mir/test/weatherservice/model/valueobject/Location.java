package com.mir.test.weatherservice.model.valueobject;

public record Location(String name, String region, String country, double lat,
                       double lon, String tz_id, long localtime_epoch, String localtime) {
}