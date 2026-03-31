package com.royalcert.royalsys.controller;

import com.royalcert.royalsys.domain.entity.Invoice;
import com.royalcert.royalsys.domain.entity.Payment;
import com.royalcert.royalsys.domain.repository.InvoiceRepository;
import com.royalcert.royalsys.service.InvoiceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final InvoiceRepository invoiceRepository;

    public InvoiceController(InvoiceService invoiceService, InvoiceRepository invoiceRepository) {
        this.invoiceService = invoiceService;
        this.invoiceRepository = invoiceRepository;
    }

    @GetMapping
    public Page<Invoice> list(@RequestParam UUID orgId, Pageable pageable) {
        return invoiceService.getInvoicesByOrg(orgId, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> get(@PathVariable UUID id) {
        return invoiceRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Invoice> create(@RequestBody Invoice invoice) {
        return ResponseEntity.ok(invoiceService.createInvoice(invoice));
    }

    @PostMapping("/{id}/send")
    public ResponseEntity<Invoice> send(@PathVariable UUID id) {
        return ResponseEntity.ok(invoiceService.sendInvoice(id));
    }

    @PostMapping("/{id}/payments")
    public ResponseEntity<Invoice> recordPayment(@PathVariable UUID id, @RequestBody Payment payment) {
        return ResponseEntity.ok(invoiceService.recordPayment(id, payment));
    }
}
