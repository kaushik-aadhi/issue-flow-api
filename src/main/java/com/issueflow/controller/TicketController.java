package com.issueflow.controller;

import com.issueflow.domain.*;
import com.issueflow.dto.TicketDtos.*;
import com.issueflow.service.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
  private final TicketService service;
  private final CommentService comments;

  public TicketController(TicketService service, CommentService comments) {
    this.service = service;
    this.comments = comments;
  }

  private boolean elevated(Authentication a) {
    return a.getAuthorities().stream().anyMatch(x -> x.getAuthority().equals("ROLE_ADMIN") || x.getAuthority().equals("ROLE_MANAGER"));
  }

  @GetMapping
  public Page<TicketResponse> search(Authentication a, @RequestParam(required = false) TicketStatus status, @RequestParam(required = false) Priority priority, @RequestParam(required = false) String q, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "createdAt") String sort) {
    return service.search(a.getName(), elevated(a), status, priority, q, page, size, sort);
  }

  @PostMapping
  public TicketResponse create(Authentication a, @Valid @RequestBody CreateTicketRequest r) {
    return service.create(a.getName(), elevated(a), r);
  }

  @GetMapping("/{id}")
  public TicketResponse get(Authentication a, @PathVariable Long id) {
    return service.get(a.getName(), elevated(a), id);
  }

  @PutMapping("/{id}")
  public TicketResponse update(Authentication a, @PathVariable Long id, @Valid @RequestBody UpdateTicketRequest r) {
    return service.update(a.getName(), elevated(a), id, r);
  }

  @DeleteMapping("/{id}")
  @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
  public void delete(Authentication a, @PathVariable Long id) {
    service.delete(a.getName(), id);
  }

  @GetMapping("/{id}/comments")
  public java.util.List<CommentResponse> comments(Authentication a, @PathVariable Long id) {
    return comments.list(a.getName(), elevated(a), id);
  }

  @PostMapping("/{id}/comments")
  public CommentResponse addComment(Authentication a, @PathVariable Long id, @Valid @RequestBody CommentRequest r) {
    return comments.add(a.getName(), elevated(a), id, r);
  }
}
