package com.mir.test.weatherservice.dao.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mir.test.weatherservice.dao.entity.WeatherEntity;
import com.mir.test.weatherservice.dao.entity.WeatherRequestHistoryEntity;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class WeatherRequestHistoryRepositoryTest {
  @Autowired
  private WeatherRequestHistoryRepository historyRepository;

  @BeforeEach
  public void setUp() {
    historyRepository.save(getHistoryEntity());
  }

  @AfterEach
  public void tearDown() {
    historyRepository.deleteAll();
  }

  @Test
  void findByUserName_should_return_listOfEntities_request_present_with_username() {
    var entities = historyRepository.findByUserName("testUser");

    assertFalse(entities.isEmpty());
    assertEquals(1, entities.size());
    assertEquals("testUser", entities.get(0).getUserName());
  }

  @Test
  void findByUserName_should_return_empty_list_if_record_with_username_does_not_exists() {
    var entities = historyRepository.findByUserName("anotherUser");

    assertTrue(entities.isEmpty());
  }

  @Test
  void findByPostalCode_should_return_listOfEntities_request_present_with_postalCode() {
    var entities = historyRepository.findByPostalCode("98034");

    assertFalse(entities.isEmpty());
    assertEquals(1, entities.size());
  }

  @Test
  void findByPostalCode_should_return_empty_list_if_record_with_ZipCode_does_not_exists() {
    var entities = historyRepository.findByPostalCode("123456");

    assertTrue(entities.isEmpty());
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
}