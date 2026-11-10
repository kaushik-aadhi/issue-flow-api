package com.issueflow.service;

import com.issueflow.dto.TicketDtos.*;
import com.issueflow.entity.*;
import com.issueflow.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {
  private final CommentRepository comments;
  private final TicketService tickets;

  public CommentService(CommentRepository comments, TicketService tickets) {
    this.comments = comments;
    this.tickets = tickets;
  }

  @Transactional(readOnly = true)
  public List<CommentResponse> list(String email, boolean elevated, Long id) {
    Ticket t = tickets.find(id);
    tickets.authorize(t, tickets.user(email), elevated);
    return comments.findByTicketIdOrderByCreatedAtAsc(id).stream().map(this::map).toList();
  }

  @Transactional
  public CommentResponse add(String email, boolean elevated, Long id, CommentRequest r) {
    Ticket t = tickets.find(id);
    AppUser u = tickets.user(email);
    tickets.authorize(t, u, elevated);
    return map(comments.save(new TicketComment(t, u, r.body().trim())));
  }

  private CommentResponse map(TicketComment c) {
    return new CommentResponse(c.getId(), c.getBody(), c.getAuthor().getId(), c.getAuthor().getFullName(), c.getCreatedAt());
  }
}
