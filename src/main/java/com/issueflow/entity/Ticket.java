package com.issueflow.entity;

import com.issueflow.domain.*;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "tickets")
public class Ticket {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 180)
  private String title;

  @Column(nullable = false, columnDefinition = "text")
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private TicketStatus status = TicketStatus.OPEN;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private Priority priority = Priority.MEDIUM;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "created_by")
  private AppUser createdBy;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "assignee_id")
  private AppUser assignee;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt = Instant.now();

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt = Instant.now();

  protected Ticket() {
  }

  public Ticket(String title, String description, Priority priority, AppUser creator, AppUser assignee) {
    this.title = title;
    this.description = description;
    this.priority = priority;
    this.createdBy = creator;
    this.assignee = assignee;
  }

  @PreUpdate
  void touch() {
    updatedAt = Instant.now();
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public TicketStatus getStatus() {
    return status;
  }

  public Priority getPriority() {
    return priority;
  }

  public AppUser getCreatedBy() {
    return createdBy;
  }

  public AppUser getAssignee() {
    return assignee;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void update(String title, String description, TicketStatus status, Priority priority, AppUser assignee) {
    this.title = title;
    this.description = description;
    this.status = status;
    this.priority = priority;
    this.assignee = assignee;
    this.updatedAt = Instant.now();
  }
}
