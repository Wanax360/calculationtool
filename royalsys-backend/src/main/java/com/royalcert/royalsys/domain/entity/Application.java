package com.royalcert.royalsys.domain.entity;

import com.royalcert.royalsys.domain.enums.WorkflowState;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "applications")
public class Application extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "application_number", nullable = false, unique = true)
    private String applicationNumber;

    @Column(name = "scope_statement")
    private String scopeStatement;

    @Column(name = "employee_count")
    private Integer employeeCount;

    @Column(name = "nace_code")
    private String naceCode;

    @Column(name = "received_at")
    private Instant receivedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    private User reviewedBy;

    @Column(name = "reviewed_at")
    private Instant reviewedAt;

    @Column(name = "review_notes")
    private String reviewNotes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkflowState status = WorkflowState.APPLICATION_RECEIVED;

    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization organization) { this.organization = organization; }
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    public String getApplicationNumber() { return applicationNumber; }
    public void setApplicationNumber(String applicationNumber) { this.applicationNumber = applicationNumber; }
    public String getScopeStatement() { return scopeStatement; }
    public void setScopeStatement(String scopeStatement) { this.scopeStatement = scopeStatement; }
    public Integer getEmployeeCount() { return employeeCount; }
    public void setEmployeeCount(Integer employeeCount) { this.employeeCount = employeeCount; }
    public String getNaceCode() { return naceCode; }
    public void setNaceCode(String naceCode) { this.naceCode = naceCode; }
    public Instant getReceivedAt() { return receivedAt; }
    public void setReceivedAt(Instant receivedAt) { this.receivedAt = receivedAt; }
    public User getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(User reviewedBy) { this.reviewedBy = reviewedBy; }
    public Instant getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(Instant reviewedAt) { this.reviewedAt = reviewedAt; }
    public String getReviewNotes() { return reviewNotes; }
    public void setReviewNotes(String reviewNotes) { this.reviewNotes = reviewNotes; }
    public WorkflowState getStatus() { return status; }
    public void setStatus(WorkflowState status) { this.status = status; }
}
