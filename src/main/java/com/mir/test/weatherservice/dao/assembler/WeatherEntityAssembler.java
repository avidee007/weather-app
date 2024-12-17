package com.mir.test.weatherservice.dao.assembler;

import com.mir.test.weatherservice.dao.entity.WeatherEntity;
import com.mir.test.weatherservice.model.valueobject.Current;
import com.mir.test.weatherservice.model.valueobject.Location;
import com.mir.test.weatherservice.model.valueobject.Weather;
import org.springframework.stereotype.Component;

@Component
public class WeatherEntityAssembler {

  WeatherEntity toEntity(Weather weather) {
    WeatherEntity entity = new WeatherEntity();
    Location location = weather.location();
    entity.setName(location.name());
    entity.setRegion(location.region());
    entity.setCountry(location.country());
    entity.setLocalDateTime(location.localtime());

    Current current = weather.current();
    entity.setLastUpdated(current.lastUpdated());
    entity.setCloud(current.cloud());
    entity.setUv(current.uv());
    entity.setConditionText(current.condition().text());
    entity.setHumidity(current.humidity());
    entity.setIsDay(current.isDay());
    //
    entity.setTempC(current.tempCelsius());
    entity.setTempF(current.tempFahrenheit());
    //setting wind attributes
    entity.setWindMph(current.windMph());
    entity.setWindKph(current.windKph());
    entity.setWindDegree(current.windDegree());
    entity.setWindDir(current.windDirection());
    //
    entity.setPrecipIn(current.precipitationIn());
    entity.setPrecipMm(current.precipitationMm());
    //
    entity.setPressureMb(current.pressureMb());
    entity.setPressureIn(current.pressureIn());

    return entity;
  }
}