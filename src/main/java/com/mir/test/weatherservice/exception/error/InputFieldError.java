package com.mir.test.weatherservice.exception.error;

import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class InputFieldError extends BaseError {
  private final List<FieldError> fieldErrors;

  public InputFieldError(String errorType, int statusCode, List<FieldError> fieldErrors, Instant timestamp) {
    super(errorType, statusCode, timestamp);
    this.fieldErrors = fieldErrors;

  }
}