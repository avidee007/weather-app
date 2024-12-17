package com.mir.test.weatherservice.dao.repository;

import com.mir.test.weatherservice.dao.entity.UserEntity;
import com.mir.test.weatherservice.model.valueobject.UserStatus;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository class for providing query methods for user related database operations.
 */
public interface UserEntityRepository extends JpaRepository<UserEntity, UUID> {

  @Query("""
      select (count(u) > 0) from UserEntity u
      where upper(u.userName) = upper(?1) and u.userStatus = ?2""")
  boolean isActiveUserName(String userName, UserStatus userStatus);

  Optional<UserEntity> findByUserName(String userName);

}