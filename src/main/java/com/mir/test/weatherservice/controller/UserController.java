package com.mir.test.weatherservice.controller;

import com.mir.test.weatherservice.exception.error.Error;
import com.mir.test.weatherservice.exception.error.InputFieldError;
import com.mir.test.weatherservice.model.valueobject.User;
import com.mir.test.weatherservice.model.valueobject.UserSignup;
import com.mir.test.weatherservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * User controller class exposing REST APIs for user management like creating and deactivating user
 * from Weather App.
 *
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "User management related apis.")
public class UserController {

  private final UserService userService;

  @PostMapping("signup")
  @Operation(summary = "Activate/Signup a new user to weather application.")
  @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
  @ApiResponse(responseCode = "400", description = "Client Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = InputFieldError.class)))
  @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class)))
  public ResponseEntity<User> signupUser(@RequestBody @Valid UserSignup signup) {
    return ResponseEntity.ok(userService.registerUser(signup));
  }

  @PutMapping("deactivate")
  @Operation(summary = "Deactivate user in weather application. Only Admin user can access.")
  @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
  @ApiResponse(responseCode = "400", description = "Client Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = InputFieldError.class)))
  @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class)))
  @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
  public ResponseEntity<User> deactivateUser(@RequestParam("username") String userName) {
    return ResponseEntity.ok(userService.deactivateUser(userName));
  }


}