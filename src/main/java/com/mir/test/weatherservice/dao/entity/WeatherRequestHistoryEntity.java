package com.mir.test.weatherservice.dao.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "WEATHER_REQUEST_HISTORY", indexes = {
    @Index(name = "idx_user_id", columnList = "userName"),
    @Index(name = "idx_postal_code", columnList = "postalCode")
})
@Getter
@Setter
@NoArgsConstructor
public class WeatherRequestHistoryEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String userName;

  private String postalCode;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "weather_id")
  private WeatherEntity weatherEntity;

  @Column(nullable = false, updatable = false)
  private LocalDateTime requestedAt;

  @Version
  private Long version;

  @PrePersist
  public void prePersist() {
    this.requestedAt = LocalDateTime.now();
  }
}