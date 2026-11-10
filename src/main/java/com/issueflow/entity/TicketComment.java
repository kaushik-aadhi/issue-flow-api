package com.issueflow.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "ticket_comments")
public class TicketComment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "ticket_id")
  private Ticket ticket;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "author_id")
  private AppUser author;

  @Column(nullable = false, length = 3000)
  private String body;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt = Instant.now();

  protected TicketComment() {
  }

  public TicketComment(Ticket ticket, AppUser author, String body) {
    this.ticket = ticket;
    this.author = author;
    this.body = body;
  }

  public Long getId() {
    return id;
  }

  public Ticket getTicket() {
    return ticket;
  }

  public AppUser getAuthor() {
    return author;
  }

  public String getBody() {
    return body;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}
