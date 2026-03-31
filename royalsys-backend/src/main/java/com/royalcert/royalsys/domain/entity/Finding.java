package com.royalcert.royalsys.domain.entity;

import com.royalcert.royalsys.domain.enums.FindingSeverity;
import com.royalcert.royalsys.domain.enums.WorkflowState;
import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "findings")
public class Finding extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "audit_id", nullable = false)
    private Audit audit;

    @Column(name = "finding_number", nullable = false)
    private String findingNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FindingSeverity severity;

    @Column(name = "clause_reference")
    private String clauseReference;

    @Column(nullable = false)
    private String description;

    @Column(name = "objective_evidence")
    private String objectiveEvidence;

    @Column(name = "issued_at")
    private Instant issuedAt;

    @Column(name = "response_due_date")
    private LocalDate responseDueDate;

    @Column(name = "response_received_at")
    private Instant responseReceivedAt;

    @Column(name = "corrective_action")
    private String correctiveAction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    private User reviewedBy;

    @Column(name = "reviewed_at")
    private Instant reviewedAt;

    @Column(name = "closed_at")
    private Instant closedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "closed_by")
    private User closedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkflowState status = WorkflowState.FINDING_ISSUED;

    public Audit getAudit() { return audit; }
    public void setAudit(Audit a) { this.audit = a; }
    public String getFindingNumber() { return findingNumber; }
    public void setFindingNumber(String s) { this.findingNumber = s; }
    public FindingSeverity getSeverity() { return severity; }
    public void setSeverity(FindingSeverity s) { this.severity = s; }
    public String getClauseReference() { return clauseReference; }
    public void setClauseReference(String s) { this.clauseReference = s; }
    public String getDescription() { return description; }
    public void setDescription(String s) { this.description = s; }
    public String getObjectiveEvidence() { return objectiveEvidence; }
    public void setObjectiveEvidence(String s) { this.objectiveEvidence = s; }
    public Instant getIssuedAt() { return issuedAt; }
    public void setIssuedAt(Instant i) { this.issuedAt = i; }
    public LocalDate getResponseDueDate() { return responseDueDate; }
    public void setResponseDueDate(LocalDate d) { this.responseDueDate = d; }
    public Instant getResponseReceivedAt() { return responseReceivedAt; }
    public void setResponseReceivedAt(Instant i) { this.responseReceivedAt = i; }
    public String getCorrectiveAction() { return correctiveAction; }
    public void setCorrectiveAction(String s) { this.correctiveAction = s; }
    public User getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(User u) { this.reviewedBy = u; }
    public Instant getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(Instant i) { this.reviewedAt = i; }
    public Instant getClosedAt() { return closedAt; }
    public void setClosedAt(Instant i) { this.closedAt = i; }
    public User getClosedBy() { return closedBy; }
    public void setClosedBy(User u) { this.closedBy = u; }
    public WorkflowState getStatus() { return status; }
    public void setStatus(WorkflowState s) { this.status = s; }
}
