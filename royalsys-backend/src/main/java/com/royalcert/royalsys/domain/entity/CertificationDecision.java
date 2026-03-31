package com.royalcert.royalsys.domain.entity;

import com.royalcert.royalsys.domain.enums.WorkflowState;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "certification_decisions")
public class CertificationDecision extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "audit_id", nullable = false)
    private Audit audit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technical_review_id")
    private TechnicalReview technicalReview;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "decision_maker_id", nullable = false)
    private User decisionMaker;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    private String decision;
    private String conditions;
    private String justification;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkflowState status = WorkflowState.DECISION_PENDING;

    public Audit getAudit() { return audit; }
    public void setAudit(Audit a) { this.audit = a; }
    public TechnicalReview getTechnicalReview() { return technicalReview; }
    public void setTechnicalReview(TechnicalReview t) { this.technicalReview = t; }
    public User getDecisionMaker() { return decisionMaker; }
    public void setDecisionMaker(User u) { this.decisionMaker = u; }
    public Instant getStartedAt() { return startedAt; }
    public void setStartedAt(Instant i) { this.startedAt = i; }
    public Instant getCompletedAt() { return completedAt; }
    public void setCompletedAt(Instant i) { this.completedAt = i; }
    public String getDecision() { return decision; }
    public void setDecision(String s) { this.decision = s; }
    public String getConditions() { return conditions; }
    public void setConditions(String s) { this.conditions = s; }
    public String getJustification() { return justification; }
    public void setJustification(String s) { this.justification = s; }
    public WorkflowState getStatus() { return status; }
    public void setStatus(WorkflowState s) { this.status = s; }
}
