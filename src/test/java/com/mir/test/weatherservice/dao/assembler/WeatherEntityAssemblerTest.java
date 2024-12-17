package com.mir.test.weatherservice.dao.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.mir.test.weatherservice.dao.entity.WeatherEntity;
import com.mir.test.weatherservice.model.valueobject.Condition;
import com.mir.test.weatherservice.model.valueobject.Current;
import com.mir.test.weatherservice.model.valueobject.Location;
import com.mir.test.weatherservice.model.valueobject.Weather;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WeatherEntityAssemblerTest {

  private final WeatherEntityAssembler assembler = new WeatherEntityAssembler();

  @Test
  void toEntity_should_return_weather_entity() {

    var weather = getMockWeather();


    WeatherEntity entity = assembler.toEntity(weather);

    assertNotNull(entity);
    assertEquals(entity.getCountry(),weather.location().country());
    assertEquals(entity.getName(),weather.location().name());
    assertEquals(entity.getRegion(),weather.location().region());
    assertEquals(entity.getTempC(),weather.current().tempCelsius());
    assertEquals(entity.getTempF(),weather.current().tempFahrenheit());
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