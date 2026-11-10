package com.issueflow.repository;

import com.issueflow.entity.TicketComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<TicketComment, Long> {
  List<TicketComment> findByTicketIdOrderByCreatedAtAsc(Long ticketId);
}
