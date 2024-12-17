package com.mir.test.weatherservice.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mir.test.weatherservice.dao.entity.UserEntity;
import com.mir.test.weatherservice.dao.repository.UserEntityRepository;
import com.mir.test.weatherservice.model.valueobject.UserRole;
import com.mir.test.weatherservice.model.valueobject.UserStatus;
import com.mir.test.weatherservice.service.UserService;
import java.time.Instant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


/**
 * This is an integration test for user lateraled functionalities.
 * This test included all layers from controller to database as part of integration testing.
 * This also test role-based authorization and authentication as part of security testing.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("test")
class UserControllerIntegrationTest {

  private MockMvc mockMvc;

  @Autowired
  private UserService userService;

  @Autowired
  private UserEntityRepository repository;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @BeforeEach
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
  }

  @AfterEach
  void tearDown() {
    repository.deleteAll();
  }

  @Test
  void signupUser_should_return_200_with_valid_payload() throws Exception {
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
  @WithMockUser(roles = "ADMIN")
  void deactivateUser_should_return_200_for_valid_active_user_with_ADMIN_role() throws Exception {

    String username = "validActiveUser";
    repository.save(getUserEntity(username));


    mockMvc.perform(
            put("/api/v1/users/deactivate")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("username", username))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userName").value(username))
        .andExpect(jsonPath("$.status").value("DEACTIVATED"));
  }

  @Test
  @WithMockUser(roles = "USER")
  void deactivateUser_should_return_401_for_valid_active_user_with_USER_role() throws Exception {

    String username = "validActiveUser";
    repository.save(getUserEntity(username));


    mockMvc.perform(
            put("/api/v1/users/deactivate")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("username", username))
        .andExpect(status().isUnauthorized());
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