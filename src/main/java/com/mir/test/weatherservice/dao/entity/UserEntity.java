package com.mir.test.weatherservice.dao.entity;

import com.mir.test.weatherservice.model.valueobject.UserRole;
import com.mir.test.weatherservice.model.valueobject.UserStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "USERS", uniqueConstraints = {
    @UniqueConstraint(name = "user_name_uc", columnNames = "userName")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  private String userName;
  private String password;
  @Enumerated
  private UserRole userRole;
  @Enumerated
  private UserStatus userStatus;
  @Version
  private Long version;
  @CreatedDate
  private Instant createdAt;
  @LastModifiedDate
  private Instant modifiedAt;
  @LastModifiedDate
  private Instant deactivateAt;
}