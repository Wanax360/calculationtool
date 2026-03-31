package com.royalcert.royalsys.service;

import com.royalcert.royalsys.domain.enums.WorkflowState;
import com.royalcert.royalsys.dto.LeadTimeReport;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class LeadTimeAnalyticsService {

    private final WorkflowService workflowService;

    public LeadTimeAnalyticsService(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    public LeadTimeReport getProjectLeadTimes(UUID projectId) {
        Map<String, Duration> metrics = new LinkedHashMap<>();

        // Lead time to quote
        addMetric(metrics, "lead_time_to_quote", "APPLICATION", projectId,
                WorkflowState.APPLICATION_RECEIVED.name(), WorkflowState.QUOTE_ISSUED.name());

        // Quote acceptance time
        addMetric(metrics, "quote_acceptance_time", "QUOTE", projectId,
                WorkflowState.QUOTE_ISSUED.name(), WorkflowState.QUOTE_ACCEPTED.name());

        // Lead time to contract
        addMetric(metrics, "lead_time_to_contract", "CONTRACT", projectId,
                WorkflowState.CONTRACT_SENT.name(), WorkflowState.CONTRACT_EFFECTIVE.name());

        // Lead time to initial audit
        addMetric(metrics, "lead_time_to_initial_audit", "PROJECT", projectId,
                WorkflowState.CONTRACT_EFFECTIVE.name(), WorkflowState.AUDIT_IN_PROGRESS.name());

        // Planning lead time
        addMetric(metrics, "planning_lead_time", "PROJECT", projectId,
                WorkflowState.PROJECT_CREATED.name(), WorkflowState.AUDIT_PLAN_ISSUED.name());

        // Audit report turnaround
        addMetric(metrics, "audit_report_turnaround", "AUDIT", projectId,
                WorkflowState.AUDIT_COMPLETED.name(), WorkflowState.AUDIT_REPORT_SUBMITTED.name());

        // Technical review turnaround
        addMetric(metrics, "technical_review_turnaround", "TECHNICAL_REVIEW", projectId,
                WorkflowState.TECHNICAL_REVIEW_IN_PROGRESS.name(), WorkflowState.TECHNICAL_REVIEW_COMPLETED.name());

        // Decision turnaround
        addMetric(metrics, "decision_turnaround", "CERTIFICATION_DECISION", projectId,
                WorkflowState.DECISION_IN_PROGRESS.name(), WorkflowState.DECISION_APPROVED.name());

        // Lead time to certificate
        addMetric(metrics, "lead_time_to_certificate", "AUDIT", projectId,
                WorkflowState.AUDIT_COMPLETED.name(), WorkflowState.CERTIFICATE_ISSUED.name());

        return new LeadTimeReport(projectId, metrics);
    }

    public Optional<Duration> getInvoiceARDays(UUID invoiceId) {
        return workflowService.calculateLeadTime("INVOICE", invoiceId,
                WorkflowState.INVOICE_SENT.name(), WorkflowState.INVOICE_PAID.name());
    }

    public Optional<Duration> getNCClosureTime(UUID findingId) {
        return workflowService.calculateLeadTime("FINDING", findingId,
                WorkflowState.FINDING_ISSUED.name(), WorkflowState.FINDING_CLOSED.name());
    }

    private void addMetric(Map<String, Duration> metrics, String name,
                            String entityType, UUID entityId, String fromState, String toState) {
        workflowService.calculateLeadTime(entityType, entityId, fromState, toState)
                .ifPresent(duration -> metrics.put(name, duration));
    }
}
