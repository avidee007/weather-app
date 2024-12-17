package com.mir.test.weatherservice.dao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "WEATHER")
@Getter
@Setter
@NoArgsConstructor
public class WeatherEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String region;

  @Column(nullable = false)
  private String country;

  @Column(nullable = false)
  private String localDateTime;

  @Column(nullable = false)
  private Float tempC;

  @Column(nullable = false)
  private Float tempF;

  @Column(nullable = false)
  private Integer isDay;

  @Column(nullable = false)
  private String conditionText;

  @Column(nullable = false)
  private Integer humidity;

  @Column(nullable = false)
  private Float windMph;

  @Column(nullable = false)
  private Float windKph;

  @Column(nullable = false)
  private Float windDegree;

  @Column(nullable = false, length = 10)
  private String windDir;

  @Column(nullable = false)
  private Integer pressureMb;

  @Column(nullable = false)
  private Float pressureIn;

  @Column(nullable = false)
  private Float precipMm;

  @Column(nullable = false)
  private Float precipIn;

  @Column(nullable = false)
  private Integer cloud;

  @Column(nullable = false)
  private Float uv;

  @Column(nullable = false)
  private String lastUpdated;

  @Version
  private Long version;

  @CreatedDate
  private LocalDateTime createdAt;

  @PrePersist
  public void prePersist() {
    this.createdAt = LocalDateTime.now();
  }


}