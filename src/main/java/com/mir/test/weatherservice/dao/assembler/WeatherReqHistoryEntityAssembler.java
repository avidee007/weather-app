package com.mir.test.weatherservice.dao.assembler;

import com.mir.test.weatherservice.dao.entity.WeatherEntity;
import com.mir.test.weatherservice.dao.entity.WeatherRequestHistoryEntity;
import com.mir.test.weatherservice.model.domain.WeatherData;
import com.mir.test.weatherservice.model.valueobject.CurrentWeather;
import com.mir.test.weatherservice.model.valueobject.LocationData;
import com.mir.test.weatherservice.model.valueobject.Precipitation;
import com.mir.test.weatherservice.model.valueobject.Pressure;
import com.mir.test.weatherservice.model.valueobject.Temperature;
import com.mir.test.weatherservice.model.valueobject.Weather;
import com.mir.test.weatherservice.model.valueobject.WeatherRequest;
import com.mir.test.weatherservice.model.valueobject.Wind;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WeatherReqHistoryEntityAssembler {

  private final WeatherEntityAssembler weatherEntityAssembler;


  public WeatherRequestHistoryEntity toEntity(WeatherRequest request, Weather weather) {
    WeatherRequestHistoryEntity entity = new WeatherRequestHistoryEntity();
    entity.setUserName(request.userName());
    entity.setPostalCode(request.postalCode());
    entity.setWeatherEntity(weatherEntityAssembler.toEntity(weather));
    return entity;
  }


  public WeatherData fromEntity(WeatherRequestHistoryEntity entity) {
    return new WeatherData(getLocationData(entity), getWeatherAttributes(entity.getWeatherEntity()));
  }

  private CurrentWeather getWeatherAttributes(WeatherEntity entity) {
    return new CurrentWeather(entity.getLastUpdated(),
        entity.getConditionText(), new Temperature(entity.getTempC(), entity.getTempF()),
        entity.getHumidity(),
        entity.getCloud(), entity.getUv(),
        new Precipitation(entity.getPrecipIn(), entity.getPrecipMm()),
        new Pressure(entity.getPressureMb(),entity.getPressureIn()),
        new Wind(entity.getWindMph(), entity.getWindKph(), entity.getWindDegree(),entity.getWindDir())
    );
  }

  private LocationData getLocationData(WeatherRequestHistoryEntity entity) {
    WeatherEntity weatherEntity = entity.getWeatherEntity();
    return new LocationData(entity.getPostalCode(),
        weatherEntity.getName(),
        weatherEntity.getRegion(),
        weatherEntity.getCountry(), weatherEntity.getLocalDateTime());
  }
}