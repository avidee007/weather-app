package com.mir.test.weatherservice.service;

import com.mir.test.weatherservice.config.WeatherApiConfigs;
import com.mir.test.weatherservice.exception.WeatherApiException;
import com.mir.test.weatherservice.model.valueobject.Weather;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherApiExternalService {

  private final RestTemplate restTemplate;
  private final WeatherApiConfigs configs;

  /**
   * Sends api request to external public API exposed by WeatherApi.
   *
   * @param postalCode US PostalCode.
   * @return Weather details of given postal code.
   * @throws WeatherApiException if exception happened in calling external public API.
   */
  public Weather getCurrentWeatherFromWeatherApi(String postalCode) {
    var requestUri = getRequestUri(postalCode);
    log.info("Request Uri created : {}", requestUri);
    try {
      var response = restTemplate.getForEntity(requestUri, Weather.class);
      log.info("Request to WeatherApi was successful.");
      return response.getBody();
    } catch (RestClientException ex) {
      log.error("Exception happened in Weather Api call, error : {}", ex.getMessage());
      throw new WeatherApiException(ex.getMessage(), ex);
    }

  }

  private String getRequestUri(String postalCode) {
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(configs.getBaseUri());
    uriBuilder.queryParam("q", postalCode);
    uriBuilder.queryParam("key", configs.getApiKey());
    return uriBuilder.toUriString();
  }
}