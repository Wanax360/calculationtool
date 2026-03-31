package com.royalcert.royalsys.domain.repository;

import com.royalcert.royalsys.domain.entity.WorkflowEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkflowEventRepository extends JpaRepository<WorkflowEvent, UUID> {
    List<WorkflowEvent> findByEntityTypeAndEntityIdOrderByOccurredAtAsc(String entityType, UUID entityId);
    Page<WorkflowEvent> findByEntityTypeAndEntityId(String entityType, UUID entityId, Pageable pageable);

    @Query("SELECT w FROM WorkflowEvent w WHERE w.entityType = :entityType AND w.entityId = :entityId " +
           "AND w.toState = :state ORDER BY w.occurredAt ASC")
    Optional<WorkflowEvent> findFirstEventForState(String entityType, UUID entityId, String state);

    @Query("SELECT w FROM WorkflowEvent w WHERE w.entityType = :entityType " +
           "AND w.occurredAt BETWEEN :from AND :to ORDER BY w.occurredAt ASC")
    List<WorkflowEvent> findEventsBetween(String entityType, Instant from, Instant to);
}
