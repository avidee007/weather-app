package com.mir.test.weatherservice.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mir.test.weatherservice.dao.repository.WeatherRequestHistoryRepository;
import com.mir.test.weatherservice.service.WeatherService;
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
 * This is an integration test for weather lateraled functionalities.
 * This test included all layers from controller to database as part of integration testing.
 * This also tests role-based authorization and authentication as part of security testing.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("test")
class WeatherControllerIntegrationTest {
  private MockMvc mockMvc;

  @Autowired
  private WeatherService weatherService;

  @Autowired
  private WeatherRequestHistoryRepository historyRepository;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @BeforeEach
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
  }

  @AfterEach
  void tearDown() {
    historyRepository.deleteAll();
  }

  @Test
  @WithMockUser(username = "testUser", roles = "USER")
  void getWeather_should_return_200_with_valid_payload_and_user_with_USER_role() throws Exception {
    var validPayload = """
        {
            "userName": "testUser",
            "postalCode": "98034"
        }
        """;
    mockMvc.perform(
            post("/api/v1/weather")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validPayload))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "admin", roles = "ADMIN")
  void getWeather_should_return_200_with_valid_payload_and_user_with_ADMIN_role() throws Exception {
    var validPayload = """
        {
            "userName": "admin",
            "postalCode": "98034"
        }
        """;
    mockMvc.perform(
            post("/api/v1/weather")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validPayload))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "testUser", roles = "USER")
  void getWeather_should_return_400_with_using_other_user_username() throws Exception {
    var validPayload = """
        {
            "userName": "otherUser",
            "postalCode": "98034"
        }
        """;
    mockMvc.perform(
            post("/api/v1/weather")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validPayload))
         .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "testUser", roles = "USER")
  void getWeatherHistory_should_return_200_with_valid_query_param_and_user_with_USER_role()
      throws Exception {
    mockMvc.perform(
            get("/api/v1/weather/history")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("postalCode","98034"))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "admin", roles = "ADMIN")
  void getWeatherHistory_should_return_200_with_valid_query_param_and_user_with_ADMIN_role()
      throws Exception {
    mockMvc.perform(
            get("/api/v1/weather/history")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("postalCode","98034"))
        .andExpect(status().isOk());
  }
}