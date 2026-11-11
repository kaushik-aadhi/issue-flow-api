package com.issueflow.repository;

import com.issueflow.entity.Ticket;
import com.issueflow.domain.TicketStatus;
import com.issueflow.domain.Priority;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.*;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
  @Query("select t from Ticket t where (:all = true or t.createdBy.id = :uid or t.assignee.id = :uid) and (:status is null or t.status = :status) and (:priority is null or t.priority = :priority) and (:q is null or lower(t.title) like lower(concat('%',:q,'%')) or lower(t.description) like lower(concat('%',:q,'%')))")
  Page<Ticket> search(@Param("all") boolean all, @Param("uid") Long uid, @Param("status") TicketStatus status, @Param("priority") Priority priority, @Param("q") String q, Pageable pageable);

  @Query("select t from Ticket t join fetch t.createdBy left join fetch t.assignee where t.id=:id")
  Optional<Ticket> findDetailedById(@Param("id") Long id);
}
