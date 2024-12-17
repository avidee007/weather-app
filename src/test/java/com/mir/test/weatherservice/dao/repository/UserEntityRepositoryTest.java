package com.mir.test.weatherservice.dao.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mir.test.weatherservice.dao.entity.UserEntity;
import com.mir.test.weatherservice.model.valueobject.UserRole;
import com.mir.test.weatherservice.model.valueobject.UserStatus;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserEntityRepositoryTest {

  @Autowired
  private UserEntityRepository userRepo;

  @BeforeEach
  public void setUp() {
    userRepo.save(getUserEntity());

  }

  @AfterEach
  public void tearDown() {
    userRepo.deleteAll();

  }

  @Test
  void isActiveUserName_should_return_true_if_active_user_exists_with_username() {

    boolean doesExists = userRepo.isActiveUserName("testUser", UserStatus.ACTIVE);

    assertTrue(doesExists);

  }

  @Test
  void isActiveUserName_should_return_false_if_active_user_does_not_exists_with_username() {

    boolean doesExists = userRepo.isActiveUserName("anotherUser", UserStatus.ACTIVE);

    assertFalse(doesExists);

  }

  @Test
  void isActiveUserName_should_return_false_if_deactivated_user_exists_with_username() {

    boolean doesExists = userRepo.isActiveUserName("testUser", UserStatus.DEACTIVATED);

    assertFalse(doesExists);

  }

  @Test
  void findByUserName_should_return_entity_if_user_exists_with_username() {
    Optional<UserEntity> entity = userRepo.findByUserName("testUser");

    assertTrue(entity.isPresent());
    assertEquals("testUser",entity.get().getUserName());
  }

  @Test
  void findByUserName_should_return_empty_optional_if_user_does_not_exists_with_username() {
    Optional<UserEntity> entity = userRepo.findByUserName("anotherUser");

    assertTrue(entity.isEmpty());
  }

  private UserEntity getUserEntity() {
    UserEntity entity = new UserEntity();
    entity.setUserName("testUser");
    entity.setUserStatus(UserStatus.ACTIVE);
    entity.setUserRole(UserRole.USER);
    entity.setPassword("encodedPassword");
    entity.setCreatedAt(Instant.now());
    entity.setModifiedAt(Instant.now());
    return entity;
  }
}