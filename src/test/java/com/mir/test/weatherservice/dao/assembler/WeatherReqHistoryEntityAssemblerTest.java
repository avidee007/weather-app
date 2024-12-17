package com.mir.test.weatherservice.dao.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.mir.test.weatherservice.dao.entity.WeatherEntity;
import com.mir.test.weatherservice.dao.entity.WeatherRequestHistoryEntity;
import com.mir.test.weatherservice.model.domain.WeatherData;
import com.mir.test.weatherservice.model.valueobject.Condition;
import com.mir.test.weatherservice.model.valueobject.Current;
import com.mir.test.weatherservice.model.valueobject.Location;
import com.mir.test.weatherservice.model.valueobject.Weather;
import com.mir.test.weatherservice.model.valueobject.WeatherRequest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WeatherReqHistoryEntityAssemblerTest {
  private final WeatherEntityAssembler weatherEntityAssembler = new WeatherEntityAssembler();
  private final WeatherReqHistoryEntityAssembler historyAssembler = new WeatherReqHistoryEntityAssembler(weatherEntityAssembler);

  @Test
  void toEntity_should_return_entity() {

    var request = new WeatherRequest("testUser", "10001");
    var entity = historyAssembler.toEntity(request,getMockWeather());

    assertNotNull(entity);
    assertEquals(entity.getPostalCode(),request.postalCode());
    assertEquals(entity.getUserName(),request.userName());
    assertNotNull(entity.getWeatherEntity());

  }

  @Test
  void fromEntity_should_return_model() {

    WeatherData weatherData = historyAssembler.fromEntity(getHistoryEntity());

    assertNotNull(weatherData);
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
    entity.setCountry("USA");
    entity.setLocalDateTime("2024-12-16 08:56");
    entity.setLastUpdated("2024-12-16 08:45");
    entity.setConditionText("Light rain");
    entity.setIsDay(1);
    entity.setTempC(5.6f);
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

  private Weather getMockWeather() {
    var location = new Location("New York", "New York", "USA", 40.752, -73.9945,
        "America/New_York", 1734025174, "2024-12-12 12:39");
    var current = new Current(1734024600, "2024-12-12 12:30", 3.9f, 39, 1,
        new Condition("Sunny", "//cdn.weatherapi.com/weather/64x64/day/113.png", 1000), 24, 20.6f,
        33.1f, 251.0f, "WSW", 1020, 30.11f, 0.0f, 0.0f, 0, 1.4f);
    return new Weather(location, current);

  }
}