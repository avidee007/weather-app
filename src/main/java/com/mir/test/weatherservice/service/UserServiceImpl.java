package com.mir.test.weatherservice.service;

import com.mir.test.weatherservice.dao.assembler.UserEntityAssembler;
import com.mir.test.weatherservice.dao.repository.UserEntityRepository;
import com.mir.test.weatherservice.exception.UserNameAlreadyExistsException;
import com.mir.test.weatherservice.model.valueobject.User;
import com.mir.test.weatherservice.model.valueobject.UserSignup;
import com.mir.test.weatherservice.model.valueobject.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
  private final UserEntityRepository repository;
  private final UserEntityAssembler userAssembler;
  private final PasswordEncoder passwordEncoder;

  @Override
  public User registerUser(UserSignup signup) {
    String userName = signup.userName();
    if (repository.isActiveUserName(userName, UserStatus.ACTIVE)) {
      String message =
          String.format("UserName : %s already exists. Create unique userName.", userName);
      throw new UserNameAlreadyExistsException(message);
    }
    var savedEntity = repository.save(userAssembler.toEntity(userName, passwordEncoder.encode(signup.password())));
    log.info("Successfully registered new user with userName : {}",signup.userName());
    return userAssembler.fromEntity(savedEntity);
  }

  @Override
  public User deactivateUser(String userName) {
    var active = repository.findByUserName(userName)
        .orElseThrow(() -> new UsernameNotFoundException("No user with userName: " + userName + " found"));
    log.info("Successfully deactivated user with userName : {}", userName);
    return userAssembler.fromEntity(repository.save(userAssembler.deactivateEntity(active)));

  }
}