package com.mir.test.weatherservice.exception;

import java.io.Serial;

public class WeatherApiException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 4084163796577L;

  public WeatherApiException(String message, Throwable cause) {
    super(message, cause);
  }

  public WeatherApiException(String message) {
    super(message);
  }
}