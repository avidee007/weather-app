package com.mir.test.weatherservice.controller;

import com.mir.test.weatherservice.exception.InvalidInputException;
import com.mir.test.weatherservice.exception.error.Error;
import com.mir.test.weatherservice.exception.error.FieldError;
import com.mir.test.weatherservice.model.domain.WeatherData;
import com.mir.test.weatherservice.model.domain.WeatherHistory;
import com.mir.test.weatherservice.model.valueobject.Weather;
import com.mir.test.weatherservice.model.valueobject.WeatherRequest;
import com.mir.test.weatherservice.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Weather controller class exposing REST APIs for weather operations.
 */
@RestController
@RequestMapping("/api/v1/weather")
@RequiredArgsConstructor
@Tag(name = "Weather", description = "Weather related apis. All valid active user can access.")
public class WeatherController {

  private final WeatherService weatherService;

  @PostMapping
  @Operation(summary = "Get current weather of given postal code.")
  @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Weather.class)))
  @ApiResponse(responseCode = "400", description = "Client Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FieldError.class)))
  @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class)))
  @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN') and #request.userName == authentication.name")
  public ResponseEntity<WeatherData> getWeather(@RequestBody @Valid WeatherRequest request) {
    return ResponseEntity.ok(weatherService.getCurrentWeather(request));
  }


  @GetMapping("history")
  @Operation(summary = "Get weather request history by postalCode or user name.")
  @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = WeatherHistory.class)))
  @ApiResponse(responseCode = "400", description = "Client Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class)))
  @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class)))
  @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
  public ResponseEntity<WeatherHistory> getWeatherHistory(
      @RequestParam(value = "postalCode", required = false) String postalCode,
      @RequestParam(value = "userName", required = false) String userName
      ) {

    validateQueryParams(postalCode, userName);


    if (Objects.nonNull(postalCode)) {
      return ResponseEntity.ok(weatherService.getWeatherHistoryByPostalCode(postalCode));
    }
    return ResponseEntity.ok(weatherService.getWeatherHistoryByUserName(userName));
  }

  private void validateQueryParams(String postalCode, String userName) {
    if ((postalCode != null && userName != null) || (postalCode == null && userName == null)) {
      throw new InvalidInputException(
          "Provide either 'postalCode' or 'userName', but not both.");
    }
  }

}