package com.mir.test.weatherservice.security;

import com.mir.test.weatherservice.dao.entity.UserEntity;
import com.mir.test.weatherservice.dao.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserDetailsService implements UserDetailsService {

  private final UserEntityRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUserName(username)
        .map(this::getCurrentUser)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  private CurrentUser getCurrentUser(UserEntity e) {
    return new CurrentUser(e.getUserName(), e.getPassword(), e.getUserRole(), e.getUserStatus());
  }
}