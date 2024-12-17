package com.mir.test.weatherservice.exception.error;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Error extends BaseError {

  private final String errorDetails;

  public Error(String errorType, int statusCode, String errorDetails, Instant timestamp) {
    super(errorType, statusCode, timestamp);
    this.errorDetails = errorDetails;

  }
}