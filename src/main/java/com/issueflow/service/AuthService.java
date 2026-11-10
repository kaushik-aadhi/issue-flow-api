package com.issueflow.service;

import com.issueflow.dto.AuthDtos.*;
import com.issueflow.entity.AppUser;
import com.issueflow.repository.UserRepository;
import com.issueflow.security.JwtService;
import com.issueflow.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
  private final UserRepository users;
  private final PasswordEncoder encoder;
  private final AuthenticationManager auth;
  private final JwtService jwt;

  public AuthService(UserRepository users, PasswordEncoder encoder, AuthenticationManager auth, JwtService jwt) {
    this.users = users;
    this.encoder = encoder;
    this.auth = auth;
    this.jwt = jwt;
  }

  @Transactional
  public AuthResponse register(RegisterRequest r) {
    String email = r.email().trim().toLowerCase();
    if (users.existsByEmailIgnoreCase(email))
      throw new ApiException(HttpStatus.CONFLICT, "Email is already registered");
    AppUser u = users.save(new AppUser(r.fullName().trim(), email, encoder.encode(r.password())));
    return response(u);
  }

  public AuthResponse login(LoginRequest r) {
    try {
      auth.authenticate(new UsernamePasswordAuthenticationToken(r.email().trim().toLowerCase(), r.password()));
    } catch (AuthenticationException e) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
    }
    return response(users.findByEmailIgnoreCase(r.email()).orElseThrow());
  }

  private AuthResponse response(AppUser u) {
    return new AuthResponse(jwt.generate(u.getEmail()), "Bearer", u.getId(), u.getFullName(), u.getEmail(), u.getRole().name());
  }
}
