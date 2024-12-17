package com.mir.test.weatherservice.model.valueobject;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserSignup(

    @NotBlank(message = "userName can not be null or empty.")
    String userName,

    @NotBlank
    @Pattern(regexp = "^(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[^\\w\\s:])(\\S){8,16}$",
        message = "password must contain 1 number,1 uppercase letters,1 lowercase letters,1 non-alpha numeric number and must be total 8-16 characters in length.")
    String password) {
}