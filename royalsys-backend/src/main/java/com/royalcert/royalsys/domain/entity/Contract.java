package com.royalcert.royalsys.domain.entity;

import com.royalcert.royalsys.domain.enums.WorkflowState;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "contracts")
public class Contract extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quote_id")
    private Quote quote;

    @Column(name = "contract_number", nullable = false, unique = true)
    private String contractNumber;

    @Column(name = "scope_statement")
    private String scopeStatement;

    @Column(nullable = false)
    private String currency = "USD";

    @Column(name = "total_value")
    private BigDecimal totalValue;

    @Column(name = "billing_schedule")
    private String billingSchedule;

    @Column(name = "package_sent_at")
    private Instant packageSentAt;

    @Column(name = "signed_at")
    private Instant signedAt;

    @Column(name = "effective_at")
    private Instant effectiveAt;

    @Column(name = "expires_at")
    private LocalDate expiresAt;

    @Column(name = "terminated_at")
    private Instant terminatedAt;

    @Column(name = "termination_reason")
    private String terminationReason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkflowState status = WorkflowState.CONTRACT_DRAFTED;

    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization o) { this.organization = o; }
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    public Quote getQuote() { return quote; }
    public void setQuote(Quote quote) { this.quote = quote; }
    public String getContractNumber() { return contractNumber; }
    public void setContractNumber(String s) { this.contractNumber = s; }
    public String getScopeStatement() { return scopeStatement; }
    public void setScopeStatement(String s) { this.scopeStatement = s; }
    public String getCurrency() { return currency; }
    public void setCurrency(String c) { this.currency = c; }
    public BigDecimal getTotalValue() { return totalValue; }
    public void setTotalValue(BigDecimal v) { this.totalValue = v; }
    public String getBillingSchedule() { return billingSchedule; }
    public void setBillingSchedule(String s) { this.billingSchedule = s; }
    public Instant getPackageSentAt() { return packageSentAt; }
    public void setPackageSentAt(Instant i) { this.packageSentAt = i; }
    public Instant getSignedAt() { return signedAt; }
    public void setSignedAt(Instant i) { this.signedAt = i; }
    public Instant getEffectiveAt() { return effectiveAt; }
    public void setEffectiveAt(Instant i) { this.effectiveAt = i; }
    public LocalDate getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDate d) { this.expiresAt = d; }
    public Instant getTerminatedAt() { return terminatedAt; }
    public void setTerminatedAt(Instant i) { this.terminatedAt = i; }
    public String getTerminationReason() { return terminationReason; }
    public void setTerminationReason(String s) { this.terminationReason = s; }
    public WorkflowState getStatus() { return status; }
    public void setStatus(WorkflowState s) { this.status = s; }
}
