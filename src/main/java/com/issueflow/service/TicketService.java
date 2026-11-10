package com.issueflow.service;

import com.issueflow.domain.*;
import com.issueflow.dto.TicketDtos.*;
import com.issueflow.entity.*;
import com.issueflow.exception.ApiException;
import com.issueflow.repository.*;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketService {
  private final TicketRepository tickets;
  private final UserRepository users;

  public TicketService(TicketRepository tickets, UserRepository users) {
    this.tickets = tickets;
    this.users = users;
  }

  @Transactional(readOnly = true)
  public Page<TicketResponse> search(String email, boolean elevated, TicketStatus status, Priority priority, String q, int page, int size, String sort) {
    AppUser me = user(email);
    String field = switch (sort == null ? "createdAt" : sort) {
      case "title" -> "title";
      case "priority" -> "priority";
      case "updatedAt" -> "updatedAt";
      default -> "createdAt";
    };
    Pageable pageable = PageRequest.of(Math.max(0, page), Math.min(Math.max(1, size), 100), Sort.by(Sort.Direction.DESC, field));
    return tickets.search(elevated, me.getId(), status, priority, (q == null || q.isBlank()) ? null : q.trim(), pageable).map(this::map);
  }

  @Transactional
  public TicketResponse create(String email, boolean elevated, CreateTicketRequest r) {
    AppUser creator = user(email);
    if (!elevated && r.assigneeId() != null)
      throw new ApiException(HttpStatus.FORBIDDEN, "Only managers can assign tickets");
    AppUser assignee = r.assigneeId() == null ? null : findUser(r.assigneeId());
    return map(tickets.save(new Ticket(r.title().trim(), r.description().trim(), r.priority(), creator, assignee)));
  }

  @Transactional(readOnly = true)
  public TicketResponse get(String email, boolean elevated, Long id) {
    Ticket t = find(id);
    authorize(t, user(email), elevated);
    return map(t);
  }

  @Transactional
  public TicketResponse update(String email, boolean elevated, Long id, UpdateTicketRequest r) {
    Ticket t = find(id);
    AppUser me = user(email);
    authorize(t, me, elevated);
    if (!elevated && r.assigneeId() != null && !r.assigneeId().equals(t.getAssignee() == null ? null : t.getAssignee().getId()))
      throw new ApiException(HttpStatus.FORBIDDEN, "Only managers can reassign tickets");
    AppUser assignee = elevated ? (r.assigneeId() == null ? null : findUser(r.assigneeId())) : t.getAssignee();
    t.update(r.title().trim(), r.description().trim(), r.status(), r.priority(), assignee);
    return map(t);
  }

  @Transactional
  public void delete(String email, Long id) {
    Ticket t = find(id);
    tickets.delete(t);
  }

  public Ticket find(Long id) {
    return tickets.findDetailedById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Ticket not found"));
  }

  public AppUser user(String email) {
    return users.findByEmailIgnoreCase(email).orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "User not found"));
  }

  public AppUser findUser(Long id) {
    return users.findById(id).orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Assignee not found"));
  }

  public void authorize(Ticket t, AppUser u, boolean elevated) {
    if (!elevated && !t.getCreatedBy().getId().equals(u.getId()) && (t.getAssignee() == null || !t.getAssignee().getId().equals(u.getId())))
      throw new ApiException(HttpStatus.FORBIDDEN, "You cannot access this ticket");
  }

  public TicketResponse map(Ticket t) {
    return new TicketResponse(t.getId(), t.getTitle(), t.getDescription(), t.getStatus(), t.getPriority(), t.getCreatedBy().getId(), t.getCreatedBy().getFullName(), t.getAssignee() == null ? null : t.getAssignee().getId(), t.getAssignee() == null ? null : t.getAssignee().getFullName(), t.getCreatedAt(), t.getUpdatedAt());
  }
}
