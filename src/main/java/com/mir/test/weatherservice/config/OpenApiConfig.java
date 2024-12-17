package com.mir.test.weatherservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  public static final String BASIC = "basic";

  @Bean
  public OpenAPI openApiBean() {
    return new OpenAPI()
        .info(getInfo())
        .addSecurityItem(new SecurityRequirement().addList(BASIC))
        .components(new Components().addSecuritySchemes(BASIC, securityScheme()));
  }

  private Info getInfo() {
    return new Info()
        .title("Weather Application APIs")
        .version("1.0")
        .description("Weather App API documentation");
  }


  public SecurityScheme securityScheme() {
    return new SecurityScheme()
        .name(BASIC)
        .in(SecurityScheme.In.HEADER)
        .scheme(BASIC)
        .description("Enter your username and password for authentication.")
        .type(SecurityScheme.Type.HTTP);

  }
}