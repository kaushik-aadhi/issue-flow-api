package com.issueflow.dto;

import com.issueflow.domain.*;
import jakarta.validation.constraints.*;

import java.time.Instant;

public class TicketDtos {
  public record CreateTicketRequest(@NotBlank @Size(max = 180) String title,
                                    @NotBlank @Size(max = 10000) String description, @NotNull Priority priority,
                                    Long assigneeId) {
  }

  public record UpdateTicketRequest(@NotBlank @Size(max = 180) String title,
                                    @NotBlank @Size(max = 10000) String description, @NotNull TicketStatus status,
                                    @NotNull Priority priority, Long assigneeId) {
  }

  public record TicketResponse(Long id, String title, String description, TicketStatus status, Priority priority,
                               Long createdById, String createdByName, Long assigneeId, String assigneeName,
                               Instant createdAt, Instant updatedAt) {
  }

  public record CommentRequest(@NotBlank @Size(max = 3000) String body) {
  }

  public record CommentResponse(Long id, String body, Long authorId, String authorName, Instant createdAt) {
  }
}
