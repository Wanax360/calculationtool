package com.royalcert.royalsys.domain.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "payment_reference")
    private String paymentReference;

    @Column(name = "received_at", nullable = false)
    private Instant receivedAt;

    private String notes;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @PrePersist
    protected void onCreate() { createdAt = Instant.now(); if (receivedAt == null) receivedAt = Instant.now(); }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Invoice getInvoice() { return invoice; }
    public void setInvoice(Invoice i) { this.invoice = i; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal a) { this.amount = a; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String s) { this.paymentMethod = s; }
    public String getPaymentReference() { return paymentReference; }
    public void setPaymentReference(String s) { this.paymentReference = s; }
    public Instant getReceivedAt() { return receivedAt; }
    public void setReceivedAt(Instant i) { this.receivedAt = i; }
    public String getNotes() { return notes; }
    public void setNotes(String s) { this.notes = s; }
}
