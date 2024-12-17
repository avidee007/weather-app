package com.mir.test.weatherservice.exception.error;

public record FieldError(String fieldName, Object rejectedValue, String message) {
}