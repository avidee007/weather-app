package com.mir.test.weatherservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mir.test.weatherservice.dao.assembler.WeatherReqHistoryEntityAssembler;
import com.mir.test.weatherservice.dao.entity.WeatherEntity;
import com.mir.test.weatherservice.dao.entity.WeatherRequestHistoryEntity;
import com.mir.test.weatherservice.dao.repository.WeatherRequestHistoryRepository;
import com.mir.test.weatherservice.exception.WeatherApiException;
import com.mir.test.weatherservice.model.domain.WeatherData;
import com.mir.test.weatherservice.model.valueobject.Condition;
import com.mir.test.weatherservice.model.valueobject.Current;
import com.mir.test.weatherservice.model.valueobject.CurrentWeather;
import com.mir.test.weatherservice.model.valueobject.Location;
import com.mir.test.weatherservice.model.valueobject.LocationData;
import com.mir.test.weatherservice.model.valueobject.Precipitation;
import com.mir.test.weatherservice.model.valueobject.Pressure;
import com.mir.test.weatherservice.model.valueobject.Temperature;
import com.mir.test.weatherservice.model.valueobject.Weather;
import com.mir.test.weatherservice.model.valueobject.WeatherRequest;
import com.mir.test.weatherservice.model.valueobject.Wind;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WeatherServiceImplTest {
  @Mock
  private WeatherApiExternalService externalService;
  @Mock
  private WeatherRequestHistoryRepository historyRepository;
  @Mock
  private WeatherReqHistoryEntityAssembler historyEntityAssembler;

  @InjectMocks
  private WeatherServiceImpl weatherService;


  @Test
  void test_getCurrentWeather_successful() {
    WeatherRequest request = new WeatherRequest("test", "98034");

    when(externalService.getCurrentWeatherFromWeatherApi("98034")).thenReturn(getMockWeather());
    when(historyEntityAssembler.toEntity(any(),any())).thenReturn(getHistoryEntity());
    when(historyRepository.save(any())).thenReturn(getHistoryEntity());


    var expectedWeather = weatherService.getCurrentWeather(request);

    assertNotNull(expectedWeather);
  }

  @Test
  void test_getCurrentWeather_failed() {
    WeatherRequest request = new WeatherRequest("test", "98034");

    when(externalService.getCurrentWeatherFromWeatherApi("98034")).thenThrow(
        new WeatherApiException("WeatherApi exception"));


    var exception =
        assertThrows(WeatherApiException.class, () -> weatherService.getCurrentWeather(request));

    assertTrue(exception.getMessage().contains("WeatherApi exception"));
    verify(historyRepository, times(0)).save(any());

  }

  @Test
  void test_getWeatherHistoryByPostalCode() {


    when(historyRepository.findByPostalCode(anyString()))
        .thenReturn(List.of(getHistoryEntity()));
    when(historyEntityAssembler.fromEntity(any())).thenReturn(getMockWeatherData());


    var result = weatherService.getWeatherHistoryByPostalCode("98034");

    assertEquals(1, result.weatherHistory().size());
  }

  @Test
  void getWeatherHistoryByUserName() {
    when(historyRepository.findByUserName(anyString()))
        .thenReturn(List.of(getHistoryEntity()));
    when(historyEntityAssembler.fromEntity(any())).thenReturn(getMockWeatherData());


    var result = weatherService.getWeatherHistoryByUserName("test");

    assertEquals(1, result.weatherHistory().size());
  }


  private Weather getMockWeather() {
    var location = new Location("New York", "New York", "USA", 40.752, -73.9945,
        "America/New_York", 1734025174, "2024-12-12 12:39");
    var current = new Current(1734024600, "2024-12-12 12:30", 3.9f, 39, 1,
        new Condition("Sunny", "//cdn.weatherapi.com/weather/64x64/day/113.png", 1000), 24, 20.6F,
        33.1F, 251.0F, "WSW", 1020, 30.11F, 0.0F, 0.0F, 0, 1.4F);
    return new Weather(location, current);

  }

  private WeatherRequestHistoryEntity getHistoryEntity(){
    WeatherRequestHistoryEntity historyEntity = new WeatherRequestHistoryEntity();
    historyEntity.setPostalCode("98034");
    historyEntity.setUserName("testUser");
    historyEntity.setRequestedAt(LocalDateTime.now());
    historyEntity.setWeatherEntity(getWeatherEntity());
    return historyEntity;
  }

  private WeatherEntity getWeatherEntity() {
    WeatherEntity entity = new WeatherEntity();
    entity.setRegion("New York");
    entity.setName("New York");
    entity.setTempF(5.6f);
    entity.setTempF(41.1f);
    entity.setHumidity(42);
    entity.setUv(1.0f);
    entity.setWindMph(4.9f);
    entity.setWindKph(7.9f);
    entity.setWindDegree(91.0f);
    entity.setWindDir("E");
    entity.setPressureMb(1033);
    entity.setPressureIn(30.49f);
    entity.setPrecipIn(0.8f);
    entity.setPrecipMm(2.02f);
    entity.setCloud(0);
    return entity;
  }

  private WeatherData getMockWeatherData(){
    return new WeatherData(getLocationData(),getWeatherAttributes());
  }

  private CurrentWeather getWeatherAttributes() {
    return new CurrentWeather("2024-12-16 08:45",
        "Light Rain", new Temperature(5.6f,42.1f),
        92,
        100,
        0.0f,
        new Precipitation(0.08f,2.02f), new Pressure(1033,30.49f), new Wind(4.9f,7.9f,91.0f,"E")
    );
  }

  private LocationData getLocationData() {
    return new LocationData("98034","New York","New York",
        "USA","2024-12-16 08:56");
  }


}