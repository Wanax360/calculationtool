package com.royalcert.royalsys.domain.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "workflow_events")
public class WorkflowEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "entity_type", nullable = false)
    private String entityType;

    @Column(name = "entity_id", nullable = false)
    private UUID entityId;

    @Column(name = "from_state")
    private String fromState;

    @Column(name = "to_state", nullable = false)
    private String toState;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performed_by")
    private User performedBy;

    private String notes;

    @Column(name = "created_at")
    private Instant createdAt;

    @PrePersist
    protected void onCreate() { createdAt = Instant.now(); if (occurredAt == null) occurredAt = Instant.now(); }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getEntityType() { return entityType; }
    public void setEntityType(String s) { this.entityType = s; }
    public UUID getEntityId() { return entityId; }
    public void setEntityId(UUID u) { this.entityId = u; }
    public String getFromState() { return fromState; }
    public void setFromState(String s) { this.fromState = s; }
    public String getToState() { return toState; }
    public void setToState(String s) { this.toState = s; }
    public String getEventType() { return eventType; }
    public void setEventType(String s) { this.eventType = s; }
    public Instant getOccurredAt() { return occurredAt; }
    public void setOccurredAt(Instant i) { this.occurredAt = i; }
    public User getPerformedBy() { return performedBy; }
    public void setPerformedBy(User u) { this.performedBy = u; }
    public String getNotes() { return notes; }
    public void setNotes(String s) { this.notes = s; }
}
