package com.issueflow.entity;

import com.issueflow.domain.Role;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "app_users")
public class AppUser {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "full_name", nullable = false, length = 120)
  private String fullName;

  @Column(nullable = false, unique = true, length = 180)
  private String email;

  @Column(name = "password_hash", nullable = false)
  private String passwordHash;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private Role role = Role.EMPLOYEE;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt = Instant.now();

  protected AppUser() {
  }

  public AppUser(String fullName, String email, String passwordHash) {
    this.fullName = fullName;
    this.email = email;
    this.passwordHash = passwordHash;
  }

  public Long getId() {
    return id;
  }

  public String getFullName() {
    return fullName;
  }

  public String getEmail() {
    return email;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public Role getRole() {
    return role;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setRole(Role role) {
    this.role = role;
  }
}
