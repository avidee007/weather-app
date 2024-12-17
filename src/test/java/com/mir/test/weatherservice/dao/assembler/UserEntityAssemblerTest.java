package com.mir.test.weatherservice.dao.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.mir.test.weatherservice.dao.entity.UserEntity;
import com.mir.test.weatherservice.model.valueobject.User;
import com.mir.test.weatherservice.model.valueobject.UserRole;
import com.mir.test.weatherservice.model.valueobject.UserStatus;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class UserEntityAssemblerTest {

  private final UserEntityAssembler assembler = new UserEntityAssembler();

  @Test
  void toEntity_should_return_UserEntity() {


    String userName = "testUser";
    String encodedPassword = "encodedPassword";

    var userEntity = assembler.toEntity(userName, encodedPassword);

    assertEquals(userName, userEntity.getUserName());
    assertEquals(encodedPassword, userEntity.getPassword());
    assertEquals(UserStatus.ACTIVE, userEntity.getUserStatus());
    assertEquals(UserRole.USER, userEntity.getUserRole());


  }

  @Test
  void fromEntity_should_return_user() {

    UserEntity entity = getUserEntity();
    User user = assembler.fromEntity(entity);

    assertEquals(user.userName(), entity.getUserName());
    assertEquals(user.status(), entity.getUserStatus());
    assertEquals(user.createdAt(), entity.getCreatedAt());
    assertNull(user.deactivateAt());
  }

  @Test
  void deactivateEntity() {
    UserEntity activeUserEntity = getUserEntity();


    UserEntity deactivateEntity = assembler.deactivateEntity(activeUserEntity);

    assertEquals(UserStatus.DEACTIVATED, deactivateEntity.getUserStatus());
    assertNotNull(deactivateEntity.getDeactivateAt());
  }

  private UserEntity getUserEntity() {
    UserEntity mockEntity = new UserEntity();
    mockEntity.setUserName("newUser");
    mockEntity.setPassword("encodedPassword");
    mockEntity.setUserRole(UserRole.USER);
    mockEntity.setUserStatus(UserStatus.ACTIVE);
    mockEntity.setCreatedAt(Instant.ofEpochMilli(1734025174));
    mockEntity.setModifiedAt(Instant.ofEpochMilli(1734025174));
    return mockEntity;
  }
}