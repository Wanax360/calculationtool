package com.royalcert.royalsys.domain.entity;

import com.royalcert.royalsys.domain.enums.WorkflowState;
import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "certificates")
public class Certificate extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "decision_id")
    private CertificationDecision decision;

    @Column(name = "certificate_number", nullable = false, unique = true)
    private String certificateNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheme_id", nullable = false)
    private Scheme scheme;

    @Column(name = "scope_statement", nullable = false)
    private String scopeStatement;

    @Column(name = "issued_at")
    private Instant issuedAt;

    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;

    @Column(name = "valid_to", nullable = false)
    private LocalDate validTo;

    @Column(name = "suspended_at")
    private Instant suspendedAt;

    @Column(name = "suspension_reason")
    private String suspensionReason;

    @Column(name = "withdrawn_at")
    private Instant withdrawnAt;

    @Column(name = "withdrawal_reason")
    private String withdrawalReason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkflowState status = WorkflowState.CERTIFICATE_DRAFTED;

    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization o) { this.organization = o; }
    public Client getClient() { return client; }
    public void setClient(Client c) { this.client = c; }
    public Project getProject() { return project; }
    public void setProject(Project p) { this.project = p; }
    public CertificationDecision getDecision() { return decision; }
    public void setDecision(CertificationDecision d) { this.decision = d; }
    public String getCertificateNumber() { return certificateNumber; }
    public void setCertificateNumber(String s) { this.certificateNumber = s; }
    public Scheme getScheme() { return scheme; }
    public void setScheme(Scheme s) { this.scheme = s; }
    public String getScopeStatement() { return scopeStatement; }
    public void setScopeStatement(String s) { this.scopeStatement = s; }
    public Instant getIssuedAt() { return issuedAt; }
    public void setIssuedAt(Instant i) { this.issuedAt = i; }
    public LocalDate getValidFrom() { return validFrom; }
    public void setValidFrom(LocalDate d) { this.validFrom = d; }
    public LocalDate getValidTo() { return validTo; }
    public void setValidTo(LocalDate d) { this.validTo = d; }
    public Instant getSuspendedAt() { return suspendedAt; }
    public void setSuspendedAt(Instant i) { this.suspendedAt = i; }
    public String getSuspensionReason() { return suspensionReason; }
    public void setSuspensionReason(String s) { this.suspensionReason = s; }
    public Instant getWithdrawnAt() { return withdrawnAt; }
    public void setWithdrawnAt(Instant i) { this.withdrawnAt = i; }
    public String getWithdrawalReason() { return withdrawalReason; }
    public void setWithdrawalReason(String s) { this.withdrawalReason = s; }
    public WorkflowState getStatus() { return status; }
    public void setStatus(WorkflowState s) { this.status = s; }
}
