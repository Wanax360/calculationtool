package com.royalcert.royalsys.service;

import com.royalcert.royalsys.domain.entity.*;
import com.royalcert.royalsys.domain.enums.WorkflowState;
import com.royalcert.royalsys.domain.repository.InvoiceRepository;
import com.royalcert.royalsys.domain.repository.CertificateRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final WorkflowService workflowService;

    public InvoiceService(InvoiceRepository invoiceRepository, WorkflowService workflowService) {
        this.invoiceRepository = invoiceRepository;
        this.workflowService = workflowService;
    }

    @Transactional
    public Invoice createInvoice(Invoice invoice) {
        invoice.setBalanceDue(invoice.getTotalAmount());
        Invoice saved = invoiceRepository.save(invoice);
        workflowService.recordTransition("INVOICE", saved.getId(),
                null, WorkflowState.INVOICE_DRAFTED, "INVOICE_CREATED", null, null);
        return saved;
    }

    @Transactional
    public Invoice sendInvoice(UUID invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));
        invoice.setSentAt(Instant.now());
        invoice.setStatus(WorkflowState.INVOICE_SENT);
        workflowService.recordTransition("INVOICE", invoiceId,
                WorkflowState.INVOICE_DRAFTED, WorkflowState.INVOICE_SENT, "INVOICE_SENT", null, null);
        return invoiceRepository.save(invoice);
    }

    @Transactional
    public Invoice recordPayment(UUID invoiceId, Payment payment) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));

        payment.setInvoice(invoice);
        invoice.getPayments().add(payment);

        BigDecimal newPaid = invoice.getAmountPaid().add(payment.getAmount());
        invoice.setAmountPaid(newPaid);
        invoice.setBalanceDue(invoice.getTotalAmount().subtract(newPaid));

        WorkflowState previousStatus = invoice.getStatus();
        if (invoice.getBalanceDue().compareTo(BigDecimal.ZERO) <= 0) {
            invoice.setStatus(WorkflowState.INVOICE_PAID);
            workflowService.recordTransition("INVOICE", invoiceId,
                    previousStatus, WorkflowState.INVOICE_PAID, "PAYMENT_RECEIVED_FULL", null, null);
        } else {
            invoice.setStatus(WorkflowState.INVOICE_PARTIALLY_PAID);
            workflowService.recordTransition("INVOICE", invoiceId,
                    previousStatus, WorkflowState.INVOICE_PARTIALLY_PAID, "PAYMENT_RECEIVED_PARTIAL", null, null);
        }

        return invoiceRepository.save(invoice);
    }

    public Page<Invoice> getInvoicesByOrg(UUID orgId, Pageable pageable) {
        return invoiceRepository.findByOrganizationId(orgId, pageable);
    }

    public BigDecimal getOutstandingBalance(UUID orgId) {
        return invoiceRepository.sumOutstandingBalance(orgId,
                List.of(WorkflowState.INVOICE_SENT, WorkflowState.INVOICE_PARTIALLY_PAID, WorkflowState.INVOICE_OVERDUE));
    }

    public List<Invoice> getOverdueInvoices() {
        return invoiceRepository.findByStatusIn(
                List.of(WorkflowState.INVOICE_SENT, WorkflowState.INVOICE_PARTIALLY_PAID));
    }
}
