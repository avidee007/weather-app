package com.mir.test.weatherservice.exception.error;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class BaseError {
  private String errorType;
  private int statusCode;
  private Instant timestamp;
}