package com.mir.test.weatherservice;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.mir.test.weatherservice.controller.UserController;
import com.mir.test.weatherservice.controller.WeatherController;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class WeatherserviceApplicationTests {

	@Autowired
	private UserController userController;

	@Autowired
	private WeatherController weatherController;

	@Autowired
	DataSource dataSource;

	@Test
	void contextLoads() {
		assertNotNull(userController);
		assertNotNull(weatherController);
		assertNotNull(dataSource);
	}

}