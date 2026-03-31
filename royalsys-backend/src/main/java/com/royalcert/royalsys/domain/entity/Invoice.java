package com.royalcert.royalsys.domain.entity;

import com.royalcert.royalsys.domain.enums.WorkflowState;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
public class Invoice extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    private Contract contract;

    @Column(name = "invoice_number", nullable = false, unique = true)
    private String invoiceNumber;

    @Column(nullable = false)
    private String currency = "USD";

    @Column(nullable = false)
    private BigDecimal subtotal;

    @Column(name = "tax_rate")
    private BigDecimal taxRate = BigDecimal.ZERO;

    @Column(name = "tax_amount")
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "amount_paid", nullable = false)
    private BigDecimal amountPaid = BigDecimal.ZERO;

    @Column(name = "balance_due", nullable = false)
    private BigDecimal balanceDue;

    @Column(name = "issued_at")
    private Instant issuedAt;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "sent_at")
    private Instant sentAt;

    @Column(name = "billing_milestone")
    private String billingMilestone;

    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkflowState status = WorkflowState.INVOICE_DRAFTED;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder")
    private List<InvoiceLineItem> lineItems = new ArrayList<>();

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<Payment> payments = new ArrayList<>();

    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization o) { this.organization = o; }
    public Client getClient() { return client; }
    public void setClient(Client c) { this.client = c; }
    public Project getProject() { return project; }
    public void setProject(Project p) { this.project = p; }
    public Contract getContract() { return contract; }
    public void setContract(Contract c) { this.contract = c; }
    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String s) { this.invoiceNumber = s; }
    public String getCurrency() { return currency; }
    public void setCurrency(String c) { this.currency = c; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal v) { this.subtotal = v; }
    public BigDecimal getTaxRate() { return taxRate; }
    public void setTaxRate(BigDecimal v) { this.taxRate = v; }
    public BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(BigDecimal v) { this.taxAmount = v; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal v) { this.totalAmount = v; }
    public BigDecimal getAmountPaid() { return amountPaid; }
    public void setAmountPaid(BigDecimal v) { this.amountPaid = v; }
    public BigDecimal getBalanceDue() { return balanceDue; }
    public void setBalanceDue(BigDecimal v) { this.balanceDue = v; }
    public Instant getIssuedAt() { return issuedAt; }
    public void setIssuedAt(Instant i) { this.issuedAt = i; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate d) { this.dueDate = d; }
    public Instant getSentAt() { return sentAt; }
    public void setSentAt(Instant i) { this.sentAt = i; }
    public String getBillingMilestone() { return billingMilestone; }
    public void setBillingMilestone(String s) { this.billingMilestone = s; }
    public String getNotes() { return notes; }
    public void setNotes(String s) { this.notes = s; }
    public WorkflowState getStatus() { return status; }
    public void setStatus(WorkflowState s) { this.status = s; }
    public List<InvoiceLineItem> getLineItems() { return lineItems; }
    public void setLineItems(List<InvoiceLineItem> l) { this.lineItems = l; }
    public List<Payment> getPayments() { return payments; }
    public void setPayments(List<Payment> p) { this.payments = p; }
}
