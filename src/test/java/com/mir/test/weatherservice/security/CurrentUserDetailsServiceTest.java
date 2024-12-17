package com.mir.test.weatherservice.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.mir.test.weatherservice.dao.entity.UserEntity;
import com.mir.test.weatherservice.dao.repository.UserEntityRepository;
import com.mir.test.weatherservice.model.valueobject.UserRole;
import com.mir.test.weatherservice.model.valueobject.UserStatus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class CurrentUserDetailsServiceTest {

  @Mock
  private UserEntityRepository repository;

  @InjectMocks
  private CurrentUserDetailsService currentUserDetailsService;


  @Test
  void loadUserByUsername_should_return_current_logged_in_user() {

    String username = "username";
    when(repository.findByUserName(username)).thenReturn(Optional.of(getUserEntity(username)));

    UserDetails currentUser = currentUserDetailsService.loadUserByUsername(username);

    assertNotNull(currentUser);
    assertEquals(username, currentUser.getUsername());
    assertEquals("encodedPassword", currentUser.getPassword());
    assertEquals(List.of(new SimpleGrantedAuthority("ROLE_USER")), currentUser.getAuthorities());
    assertTrue(currentUser.isEnabled());
  }

  @Test
  void loadUserByUsername_should_throw_UsernameNotFoundException_if_user_not_found() {

    String username = "username";
    when(repository.findByUserName(username)).thenReturn(Optional.empty());

    var exception = assertThrows(UsernameNotFoundException.class, () -> currentUserDetailsService.loadUserByUsername(username));

    assertTrue(exception.getMessage().contains("User not found"));
  }


  private UserEntity getUserEntity(String userName) {
    UserEntity entity = new UserEntity();
    entity.setUserName(userName);
    entity.setUserStatus(UserStatus.ACTIVE);
    entity.setUserRole(UserRole.USER);
    entity.setPassword("encodedPassword");
    entity.setCreatedAt(Instant.now());
    entity.setModifiedAt(Instant.now());
    return entity;
  }
}