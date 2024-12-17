package com.mir.test.weatherservice.model.valueobject;

import java.time.Instant;

public record User(String userName, UserStatus status, Instant createdAt,
                   Instant deactivateAt) {
}