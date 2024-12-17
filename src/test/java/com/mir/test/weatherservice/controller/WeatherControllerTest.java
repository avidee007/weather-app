package com.mir.test.weatherservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mir.test.weatherservice.exception.WeatherApiException;
import com.mir.test.weatherservice.model.domain.WeatherData;
import com.mir.test.weatherservice.model.domain.WeatherHistory;
import com.mir.test.weatherservice.model.valueobject.CurrentWeather;
import com.mir.test.weatherservice.model.valueobject.LocationData;
import com.mir.test.weatherservice.model.valueobject.Precipitation;
import com.mir.test.weatherservice.model.valueobject.Pressure;
import com.mir.test.weatherservice.model.valueobject.Temperature;
import com.mir.test.weatherservice.model.valueobject.WeatherRequest;
import com.mir.test.weatherservice.model.valueobject.Wind;
import com.mir.test.weatherservice.security.TestSecurityConfig;
import com.mir.test.weatherservice.service.WeatherService;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(WeatherController.class)
@Import(TestSecurityConfig.class)
class WeatherControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private WeatherService weatherService;

  @Test
  void getWeather_should_return_200_with_valid_payload() throws Exception {
    when(weatherService.getCurrentWeather(any(WeatherRequest.class))).thenReturn(getMockWeatherData());

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
  void getWeather_should_return_400_with_null_userName() throws Exception {

    var nullUsernamePayload = """
        {
            "postalCode": "10001"
        }
        """;
    mockMvc.perform(
            post("/api/v1/weather")
                .contentType(MediaType.APPLICATION_JSON)
                .content(nullUsernamePayload))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorType").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.statusCode").value(400))
        .andExpect(jsonPath("$.fieldErrors[0].fieldName").value("userName"))
        .andExpect(jsonPath("$.fieldErrors[0].rejectedValue", Matchers.nullValue()))
        .andExpect(
            jsonPath("$.fieldErrors[0].message").value("userName can not be null or empty."));
  }

  @Test
  void getWeather_should_return_400_with_empty_userName() throws Exception {

    var nullUsernamePayload = """
        {
            "userName": "",
            "postalCode": "10001"
        }
        """;
    mockMvc.perform(
            post("/api/v1/weather")
                .contentType(MediaType.APPLICATION_JSON)
                .content(nullUsernamePayload))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorType").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.statusCode").value(400))
        .andExpect(jsonPath("$.fieldErrors[0].fieldName").value("userName"))
        .andExpect(jsonPath("$.fieldErrors[0].rejectedValue").value(""))
        .andExpect(
            jsonPath("$.fieldErrors[0].message").value("userName can not be null or empty."));
  }

  @Test
  void getWeather_should_return_400_with_null_postal_code() throws Exception {

    var invalidPostalCodePayload = """
        {
            "userName": "testUser"
        }
        """;
    mockMvc.perform(
            post("/api/v1/weather")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidPostalCodePayload))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorType").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.statusCode").value(400))
        .andExpect(jsonPath("$.fieldErrors[0].fieldName").value("postalCode"))
        .andExpect(jsonPath("$.fieldErrors[0].rejectedValue", Matchers.nullValue()))
        .andExpect(jsonPath("$.fieldErrors[0].message").value(
            "postalCode/zipCode can not be null or empty."));
  }

  @Test
  void getWeather_should_return_400_with_empty_postal_code() throws Exception {

    var invalidPostalCodePayload = """
        {
            "userName": "testUser",
            "postalCode": ""
        }
        """;
    mockMvc.perform(
            post("/api/v1/weather")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidPostalCodePayload))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorType").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.statusCode").value(400))
        .andExpect(jsonPath("$.fieldErrors[0].fieldName").value("postalCode"))
        .andExpect(jsonPath("$.fieldErrors[0].rejectedValue").value(""))
        .andExpect(
            jsonPath("$.fieldErrors[0].message").value("Not a valid postalCode/ZipCode pattern"));
  }

  @Test
  void getWeather_should_return_400_with_wrong_postal_code_pattern() throws Exception {

    var invalidPostalCodePayload = """
        {
            "userName": "testUser",
            "postalCode": "9803400000000"
        }
        """;
    mockMvc.perform(
            post("/api/v1/weather")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidPostalCodePayload))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorType").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.statusCode").value(400))
        .andExpect(jsonPath("$.fieldErrors[0].fieldName").value("postalCode"))
        .andExpect(jsonPath("$.fieldErrors[0].rejectedValue").value("9803400000000"))
        .andExpect(
            jsonPath("$.fieldErrors[0].message").value("Not a valid postalCode/ZipCode pattern"));
  }

  @Test
  void getWeather_should_return_500_when_external_weatherApi_fails() throws Exception {
    when(weatherService.getCurrentWeather(any(WeatherRequest.class)))
        .thenThrow(new WeatherApiException("WeatherApi failed"));

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
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.errorType").value("INTERNAL_SERVER_ERROR"))
        .andExpect(jsonPath("$.statusCode").value(500))
        .andExpect(jsonPath("$.errorDetails").value("WeatherApi failed"));

  }

  @Test
  void getWeatherHistory_should_return_200_with_valid_postalCode_in_request_param() throws Exception {
    when(weatherService.getWeatherHistoryByPostalCode(anyString())).thenReturn(getWeatherHistory());


    mockMvc.perform(
            get("/api/v1/weather/history")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("postalCode","98034"))
        .andExpect(status().isOk());
  }

  @Test
  void getWeatherHistory_should_return_200_with_valid_username_in_request_param() throws Exception {
    when(weatherService.getWeatherHistoryByPostalCode(anyString())).thenReturn(getWeatherHistory());


    mockMvc.perform(
            get("/api/v1/weather/history")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("userName","testUser"))
        .andExpect(status().isOk());
  }

  @Test
  void getWeatherHistory_should_return_400_when_both_username_and_postalCode_is_sent()
      throws Exception {
    mockMvc.perform(get("/api/v1/weather/history")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("postalCode","98034")
                .queryParam("userName","testUser")
        )
        .andExpect(status().isBadRequest());
  }

  @Test
  void getWeatherHistory_should_return_400_when_both_username_and_postalCode_is_null()
      throws Exception {
    mockMvc.perform(get("/api/v1/weather/history")
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest());
  }


  private WeatherHistory getWeatherHistory() {
    return new WeatherHistory(List.of(getMockWeatherData()));
  }

  private WeatherData getMockWeatherData(){
    return new WeatherData(getLocationData(),getWeatherAttributes());
  }



  private CurrentWeather getWeatherAttributes() {
    return new CurrentWeather("2024-12-16 08:45",
        "Light Rain", new Temperature(5.6f,42.1f),
        92,
        100,
        0.0f,
        new Precipitation(0.08f,2.02f), new Pressure(1033,30.49f), new Wind(4.9f,7.9f,91.0f,"E")
    );
  }

  private LocationData getLocationData() {
    return new LocationData("98034","New York","New York",
        "USA","2024-12-16 08:56");
  }

}