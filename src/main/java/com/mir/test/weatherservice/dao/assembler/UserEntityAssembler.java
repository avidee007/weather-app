package com.mir.test.weatherservice.dao.assembler;

import com.mir.test.weatherservice.dao.entity.UserEntity;
import com.mir.test.weatherservice.model.valueobject.User;
import com.mir.test.weatherservice.model.valueobject.UserRole;
import com.mir.test.weatherservice.model.valueobject.UserStatus;
import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class UserEntityAssembler {

  public UserEntity toEntity(String userName, String encodedPassword) {
    UserEntity entity = new UserEntity();
    entity.setUserName(userName);
    entity.setPassword(encodedPassword);
    entity.setUserRole(UserRole.USER);
    entity.setUserStatus(UserStatus.ACTIVE);
    entity.setCreatedAt(Instant.now());
    entity.setModifiedAt(Instant.now());
    return entity;
  }

  public User fromEntity(UserEntity savedEntity) {
    return new User(savedEntity.getUserName(), savedEntity.getUserStatus(),
        savedEntity.getCreatedAt(), savedEntity.getDeactivateAt());
  }

  public UserEntity deactivateEntity(UserEntity entity) {
    entity.setUserStatus(UserStatus.DEACTIVATED);
    entity.setDeactivateAt(Instant.now());
    return entity;
  }
}