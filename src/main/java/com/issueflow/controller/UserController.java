package com.issueflow.controller;

import com.issueflow.dto.UserDtos.UserResponse;
import com.issueflow.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
public class UserController {
  private final UserRepository users;

  public UserController(UserRepository users) {
    this.users = users;
  }

  @GetMapping
  public List<UserResponse> list() {
    return users.findAll(org.springframework.data.domain.Sort.by("fullName")).stream().map(u -> new UserResponse(u.getId(), u.getFullName(), u.getEmail(), u.getRole().name(), u.getCreatedAt())).toList();
  }
}
