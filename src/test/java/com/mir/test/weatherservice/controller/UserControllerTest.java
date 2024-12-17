package com.mir.test.weatherservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mir.test.weatherservice.exception.UserNameAlreadyExistsException;
import com.mir.test.weatherservice.model.valueobject.User;
import com.mir.test.weatherservice.model.valueobject.UserSignup;
import com.mir.test.weatherservice.model.valueobject.UserStatus;
import com.mir.test.weatherservice.security.TestSecurityConfig;
import com.mir.test.weatherservice.service.UserService;
import java.time.Instant;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@Import(TestSecurityConfig.class)
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UserService userService;

  @Test
  void signupUser_should_return_200_with_valid_payload() throws Exception {

    var user = new User("testUser", UserStatus.ACTIVE, Instant.now(), null);
    when(userService.registerUser(any(UserSignup.class))).thenReturn(user);

    var validPayload = """
        {
            "userName": "testUser",
            "password": "Askzxc007@pass"
        }
        """;
    mockMvc.perform(
            post("/api/v1/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validPayload))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userName").value("testUser"));
  }

  @Test
  void signupUser_should_return_400_with_null_username() throws Exception {

    var nullUserNamePayload = """
        {
            "password": "Askzxc007@pass"
        }
        """;
    mockMvc.perform(
            post("/api/v1/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(nullUserNamePayload))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorType").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.statusCode").value(400))
        .andExpect(jsonPath("$.fieldErrors[0].fieldName").value("userName"))
        .andExpect(jsonPath("$.fieldErrors[0].rejectedValue", Matchers.nullValue()))
        .andExpect(jsonPath("$.fieldErrors[0].message").value("userName can not be null or empty."));

  }

  @Test
  void signupUser_should_return_400_with_empty_username() throws Exception {

    var emptyUserNamePayload = """
        {
            "userName": "",
            "password": "Askzxc007@pass"
        }
        """;
    mockMvc.perform(
            post("/api/v1/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(emptyUserNamePayload))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorType").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.statusCode").value(400))
        .andExpect(jsonPath("$.fieldErrors[0].fieldName").value("userName"))
        .andExpect(jsonPath("$.fieldErrors[0].rejectedValue").value(""))
        .andExpect(jsonPath("$.fieldErrors[0].message").value("userName can not be null or empty."));

  }

  @Test
  void signupUser_should_return_400_with_duplicate_username() throws Exception {

    when(userService.registerUser(any(UserSignup.class)))
        .thenThrow(new UserNameAlreadyExistsException("UserName : existingUser already exists. Create unique userName."));

    var existingUserPayload = """
        {
            "userName": "existingUser",
            "password": "Askzxc007@pass"
        }
        """;
    mockMvc.perform(
            post("/api/v1/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(existingUserPayload))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorType").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.statusCode").value(400))
        .andExpect(jsonPath("$.errorDetails").value("UserName : existingUser already exists. Create unique userName."));
  }

  @Test
  void signupUser_should_return_400_with_weak_password() throws Exception {

    var weakPasswordPayload = """
        {
            "userName": "testUser",
            "password": "pass"
        }
        """;
    mockMvc.perform(
            post("/api/v1/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(weakPasswordPayload))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorType").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.statusCode").value(400))
        .andExpect(jsonPath("$.fieldErrors[0].fieldName").value("password"))
        .andExpect(jsonPath("$.fieldErrors[0].rejectedValue").value("pass"))
        .andExpect(jsonPath("$.fieldErrors[0].message").value("password must contain 1 number,1 uppercase letters,1 lowercase letters,1 non-alpha numeric number and must be total 8-16 characters in length."));
  }

  @Test
  void deactivateUser_should_return_200_for_valid_active_user() throws Exception {

    var user = new User("validActiveUser", UserStatus.DEACTIVATED, Instant.now(), Instant.now());
    when(userService.deactivateUser(anyString())).thenReturn(user);

    mockMvc.perform(
            put("/api/v1/users/deactivate")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("username", "validActiveUser"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userName").value("validActiveUser"))
        .andExpect(jsonPath("$.status").value("DEACTIVATED"));
  }

  @Test
  void deactivateUser_should_return_400_for_valid_active_user() throws Exception {

    when(userService.deactivateUser(anyString()))
        .thenThrow(new UsernameNotFoundException("No user with userName: nonExistingUser found"));

    mockMvc.perform(
            put("/api/v1/users/deactivate")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("username", "nonExistingUser"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorType").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.statusCode").value(400))
        .andExpect(jsonPath("$.errorDetails").value("No user with userName: nonExistingUser found"));
  }


}