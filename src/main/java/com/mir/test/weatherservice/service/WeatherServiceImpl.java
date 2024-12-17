package com.mir.test.weatherservice.service;

import com.mir.test.weatherservice.dao.assembler.WeatherReqHistoryEntityAssembler;
import com.mir.test.weatherservice.dao.entity.WeatherRequestHistoryEntity;
import com.mir.test.weatherservice.dao.repository.WeatherRequestHistoryRepository;
import com.mir.test.weatherservice.model.domain.WeatherData;
import com.mir.test.weatherservice.model.domain.WeatherHistory;
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
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherServiceImpl implements WeatherService {
  private final WeatherApiExternalService weatherApiService;
  private final WeatherRequestHistoryRepository historyRepository;
  private final WeatherReqHistoryEntityAssembler historyEntityAssembler;

  @Override
  @Transactional
  public WeatherData getCurrentWeather(WeatherRequest request) {
    var weather = weatherApiService.getCurrentWeatherFromWeatherApi(request.postalCode());
    log.info("Successfully fetched weather, saving weather request in db.");
    historyRepository.save(historyEntityAssembler.toEntity(request, weather));
    log.info("Saved weather request in db.");
    return toWeatherData(request.postalCode(), weather);
  }

  @Override
  public WeatherHistory getWeatherHistoryByUserName(String userName) {
    var byUserNameEntities = historyRepository.findByUserName(userName);
    log.info("Successfully fetched weather history by userName : {}", userName);
    return new WeatherHistory(getWeatherDataList(byUserNameEntities));
  }

  @Override
  public WeatherHistory getWeatherHistoryByPostalCode(String postalCode) {
    var byPostalCodeEntities = historyRepository.findByPostalCode(postalCode);
    log.info("Successfully fetched weather history by postalCode : {}", postalCode);
    return new WeatherHistory(getWeatherDataList(byPostalCodeEntities));
  }

  private List<WeatherData> getWeatherDataList(List<WeatherRequestHistoryEntity> byPostalCode) {
    return byPostalCode.stream()
            .map(historyEntityAssembler::fromEntity)
            .toList();
  }

  public WeatherData toWeatherData(String postalCode, Weather weather) {
    return new WeatherData(getLocationData(postalCode, weather.location()),
        getWeatherData(weather.current()));

  }

  private CurrentWeather getWeatherData(Current current) {
    return new CurrentWeather(current.lastUpdated(),
        current.condition().text(), new Temperature(current.tempCelsius(), current.tempFahrenheit()),
        current.humidity(),
        current.cloud(),
        current.uv(),
        new Precipitation(current.precipitationIn(), current.precipitationMm()),
        new Pressure(current.pressureMb(), current.pressureIn()),
        new Wind(current.windMph(), current.windKph(), current.windDegree(), current.windDirection())
    );
  }

  private LocationData getLocationData(String postalCode, Location location) {
    return new LocationData(postalCode, location.name(), location.region(), location.country(),
        location.localtime());
  }

}