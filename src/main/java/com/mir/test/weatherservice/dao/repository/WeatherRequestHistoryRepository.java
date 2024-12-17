package com.mir.test.weatherservice.dao.repository;

import com.mir.test.weatherservice.dao.entity.WeatherRequestHistoryEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WeatherRequestHistoryRepository extends JpaRepository<WeatherRequestHistoryEntity, UUID> {

  @Query("select w from WeatherRequestHistoryEntity w where w.userName = ?1")
  List<WeatherRequestHistoryEntity> findByUserName(String userName);

  @Query("select w from WeatherRequestHistoryEntity w where w.postalCode = ?1")
  List<WeatherRequestHistoryEntity> findByPostalCode(String postalCode);

}