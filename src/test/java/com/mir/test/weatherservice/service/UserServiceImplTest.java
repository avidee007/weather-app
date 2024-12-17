package com.mir.test.weatherservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mir.test.weatherservice.dao.assembler.UserEntityAssembler;
import com.mir.test.weatherservice.dao.entity.UserEntity;
import com.mir.test.weatherservice.dao.repository.UserEntityRepository;
import com.mir.test.weatherservice.exception.UserNameAlreadyExistsException;
import com.mir.test.weatherservice.model.valueobject.User;
import com.mir.test.weatherservice.model.valueobject.UserRole;
import com.mir.test.weatherservice.model.valueobject.UserSignup;
import com.mir.test.weatherservice.model.valueobject.UserStatus;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock
  private UserEntityRepository repository;

  @Mock
  private UserEntityAssembler userAssembler;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private UserServiceImpl userService;

  @Test
  void registerUser_ShouldThrowException_WhenUserNameAlreadyExists() {

    UserSignup signup = new UserSignup("existingUser", "password123");

    when(repository.isActiveUserName("existingUser", UserStatus.ACTIVE)).thenReturn(true);

    var exception = assertThrows(UserNameAlreadyExistsException.class, () -> userService.registerUser(signup));
    assertEquals("UserName : existingUser already exists. Create unique userName.", exception.getMessage());
  }

  @Test
  void registerUser_ShouldRegisterUserSuccessfully_WhenUsernameIsUnique() {

    UserSignup signup = new UserSignup("newUser", "password123");
    UserEntity mockEntity = getNewUserEntity();

    User mockUser = new User("newUser", UserStatus.ACTIVE, Instant.now(), null);

    when(repository.isActiveUserName("newUser", UserStatus.ACTIVE)).thenReturn(false);
    when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
    when(userAssembler.toEntity("newUser", "encodedPassword")).thenReturn(mockEntity);
    when(repository.save(mockEntity)).thenReturn(mockEntity);
    when(userAssembler.fromEntity(mockEntity)).thenReturn(mockUser);


    User registeredUser = userService.registerUser(signup);


    assertNotNull(registeredUser);
    assertEquals("newUser", registeredUser.userName());
    assertEquals(UserStatus.ACTIVE, registeredUser.status());
  }

  private UserEntity getNewUserEntity() {
    UserEntity mockEntity = new UserEntity();
    mockEntity.setUserName("newUser");
    mockEntity.setPassword("encodedPassword");
    mockEntity.setUserRole(UserRole.USER);
    mockEntity.setUserStatus(UserStatus.ACTIVE);
    mockEntity.setCreatedAt(Instant.now());
    mockEntity.setModifiedAt(Instant.now());
    return mockEntity;
  }

  @Test
  void deactivateUser_ShouldDeactivateUserSuccessfully_WhenUserExists() {

    String userName = "existingUser";
    UserEntity mockActiveUserEntity = new UserEntity();
    mockActiveUserEntity.setUserName(userName);
    mockActiveUserEntity.setUserStatus(UserStatus.ACTIVE);

    UserEntity deactivatedEntity = new UserEntity();
    deactivatedEntity.setUserStatus(UserStatus.DEACTIVATED);
    deactivatedEntity.setDeactivateAt(Instant.ofEpochMilli(1734025174));

    User deactivateUser = new User(userName, UserStatus.DEACTIVATED, Instant.now(), Instant.ofEpochMilli(1734025174));

    when(repository.findByUserName(userName)).thenReturn(Optional.of(mockActiveUserEntity));
    when(userAssembler.deactivateEntity(mockActiveUserEntity)).thenReturn(deactivatedEntity);
    when(repository.save(deactivatedEntity)).thenReturn(deactivatedEntity);
    when(userAssembler.fromEntity(deactivatedEntity)).thenReturn(deactivateUser);


    var result = userService.deactivateUser(userName);


    assertNotNull(result);
    assertEquals(UserStatus.DEACTIVATED, result.status());
    assertEquals(Instant.ofEpochMilli(1734025174), result.deactivateAt());
  }

  @Test
  void deactivateUser_ShouldThrowException_WhenUserDoesNotExist() {

    String userName = "nonExistentUser";

    when(repository.findByUserName(userName)).thenReturn(Optional.empty());


    var exception = assertThrows(UsernameNotFoundException.class, () -> userService.deactivateUser(userName));
    assertEquals("No user with userName: nonExistentUser found", exception.getMessage());
  }


  @Test
  void registerUser_ShouldEncodePasswordCorrectly() {

    UserSignup signup = new UserSignup("testUser", "securePassword");

    when(repository.isActiveUserName("testUser", UserStatus.ACTIVE)).thenReturn(false);
    when(passwordEncoder.encode("securePassword")).thenReturn("encodedSecurePassword");

    UserEntity mockEntity = new UserEntity();
    mockEntity.setUserName("testUser");
    mockEntity.setPassword("encodedSecurePassword");

    when(userAssembler.toEntity("testUser", "encodedSecurePassword")).thenReturn(mockEntity);
    when(repository.save(mockEntity)).thenReturn(mockEntity);


    userService.registerUser(signup);


    verify(passwordEncoder).encode("securePassword");
  }
}