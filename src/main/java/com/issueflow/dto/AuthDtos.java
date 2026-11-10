package com.issueflow.dto;

import jakarta.validation.constraints.*;

public class AuthDtos {
  public record RegisterRequest(@NotBlank @Size(max = 120) String fullName,
                                @NotBlank @Email @Size(max = 180) String email,
                                @NotBlank @Size(min = 8, max = 72) String password) {
  }

  public record LoginRequest(@NotBlank @Email String email, @NotBlank String password) {
  }

  public record AuthResponse(String token, String tokenType, Long userId, String fullName, String email, String role) {
  }
}
