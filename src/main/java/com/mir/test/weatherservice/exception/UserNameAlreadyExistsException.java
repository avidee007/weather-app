package com.mir.test.weatherservice.exception;

import java.io.Serial;

public class UserNameAlreadyExistsException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 1000084163796577L;

  public UserNameAlreadyExistsException(String message) {
    super(message);
  }
}