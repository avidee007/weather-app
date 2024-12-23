package com.mir.test.weatherservice.exception;

import com.mir.test.weatherservice.exception.error.Error;
import com.mir.test.weatherservice.exception.error.FieldError;
import com.mir.test.weatherservice.exception.error.InputFieldError;
import java.time.Instant;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler class which handles any exception happened while processing any request.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<InputFieldError> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex) {
    var inputError =
        new InputFieldError(HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST.value(),
            mapFieldErrors(ex),
            Instant.now());
    log.error("Input Error : {}", inputError);
    return new ResponseEntity<>(inputError, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InvalidInputException.class)
  public ResponseEntity<com.mir.test.weatherservice.exception.error.Error> handleInvalidInputException(
      InvalidInputException ex) {
    var inputError =
        new Error(HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST.value(), ex.getMessage(),
            Instant.now());
    log.error("InvalidInputException exception happened. Error : {}",ex.getMessage());
    return new ResponseEntity<>(inputError, HttpStatus.BAD_REQUEST);
  }

  private List<FieldError> mapFieldErrors(MethodArgumentNotValidException ex) {
    List<org.springframework.validation.FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
    return fieldErrors.stream()
        .map(fieldError -> new FieldError(fieldError.getField(), fieldError.getRejectedValue(),
            fieldError.getDefaultMessage()))
        .toList();
  }

  @ExceptionHandler(WeatherApiException.class)
  public ResponseEntity<Error> handleWeatherApiException(WeatherApiException wae) {
    var serverError =
        new Error(HttpStatus.INTERNAL_SERVER_ERROR.name(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
            wae.getMessage(), Instant.now()
        );
    log.error("WeatherApiException exception happened. Error : {}", wae.getMessage());
    return new ResponseEntity<>(serverError, HttpStatus.INTERNAL_SERVER_ERROR);

  }

  @ExceptionHandler(UserNameAlreadyExistsException.class)
  public ResponseEntity<Error> handleUserNameAlreadyExistsException(
      UserNameAlreadyExistsException ex) {
    var serverError =
        new Error(HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST.value(), ex.getMessage(),
            Instant.now()
        );
    log.error("UserNameAlreadyExistsException exception happened. Error : {}", ex.getMessage());
    return new ResponseEntity<>(serverError, HttpStatus.BAD_REQUEST);

  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<Error> handleUsernameNotFoundException(UsernameNotFoundException ex) {
    var serverError =
        new Error(HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST.value(), ex.getMessage(),
            Instant.now()
        );
    log.error("UsernameNotFoundException exception happened. Error : {}", ex.getMessage());
    return new ResponseEntity<>(serverError, HttpStatus.BAD_REQUEST);

  }

  @ExceptionHandler(AuthorizationDeniedException.class)
  public ResponseEntity<Error> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
    var errorMsg = String.format("%s : You are not authorized to access this resource.", ex.getMessage());
    var serverError =
        new Error(HttpStatus.FORBIDDEN.name(), HttpStatus.FORBIDDEN.value(), errorMsg,
            Instant.now()
        );
    log.error("AuthorizationDeniedException exception happened. Error : {}", errorMsg);
    return new ResponseEntity<>(serverError, HttpStatus.FORBIDDEN);

  }


}