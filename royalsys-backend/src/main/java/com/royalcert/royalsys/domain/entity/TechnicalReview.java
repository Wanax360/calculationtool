package com.royalcert.royalsys.domain.entity;

import com.royalcert.royalsys.domain.enums.WorkflowState;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "technical_reviews")
public class TechnicalReview extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "audit_id", nullable = false)
    private Audit audit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    private String outcome;
    private String comments;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkflowState status = WorkflowState.TECHNICAL_REVIEW_PENDING;

    public Audit getAudit() { return audit; }
    public void setAudit(Audit a) { this.audit = a; }
    public User getReviewer() { return reviewer; }
    public void setReviewer(User u) { this.reviewer = u; }
    public Instant getStartedAt() { return startedAt; }
    public void setStartedAt(Instant i) { this.startedAt = i; }
    public Instant getCompletedAt() { return completedAt; }
    public void setCompletedAt(Instant i) { this.completedAt = i; }
    public String getOutcome() { return outcome; }
    public void setOutcome(String s) { this.outcome = s; }
    public String getComments() { return comments; }
    public void setComments(String s) { this.comments = s; }
    public WorkflowState getStatus() { return status; }
    public void setStatus(WorkflowState s) { this.status = s; }
}
