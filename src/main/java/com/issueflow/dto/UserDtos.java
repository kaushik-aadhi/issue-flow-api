package com.issueflow.dto;

import java.time.Instant;

public class UserDtos {
  public record UserResponse(Long id, String fullName, String email, String role, Instant createdAt) {
  }
}
