package com.mir.test.weatherservice.service;

import com.mir.test.weatherservice.model.valueobject.User;
import com.mir.test.weatherservice.model.valueobject.UserSignup;

public interface UserService {

  User registerUser(UserSignup signup);

  User deactivateUser(String userName);


}