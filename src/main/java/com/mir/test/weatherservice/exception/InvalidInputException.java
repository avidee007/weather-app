package com.mir.test.weatherservice.exception;

import java.io.Serial;

public class InvalidInputException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = -3796577L;

  public InvalidInputException(String message) {
    super(message);
  }
}