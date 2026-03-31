package com.royalcert.royalsys.service;

import com.royalcert.royalsys.domain.enums.WorkflowState;
import com.royalcert.royalsys.domain.repository.*;
import com.royalcert.royalsys.dto.DashboardStats;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class DashboardService {

    private final ProjectRepository projectRepository;
    private final ApplicationRepository applicationRepository;
    private final CertificateRepository certificateRepository;
    private final InvoiceRepository invoiceRepository;

    public DashboardService(ProjectRepository projectRepository,
                             ApplicationRepository applicationRepository,
                             CertificateRepository certificateRepository,
                             InvoiceRepository invoiceRepository) {
        this.projectRepository = projectRepository;
        this.applicationRepository = applicationRepository;
        this.certificateRepository = certificateRepository;
        this.invoiceRepository = invoiceRepository;
    }

    public DashboardStats getStats(UUID orgId) {
        DashboardStats stats = new DashboardStats();

        stats.setActiveProjects(projectRepository.countByOrganizationIdAndStatus(orgId, WorkflowState.PROJECT_ACTIVE));
        stats.setPendingApplications(applicationRepository.countByOrganizationIdAndStatus(orgId, WorkflowState.APPLICATION_RECEIVED));
        stats.setActiveCertificates(certificateRepository.countByOrganizationIdAndStatus(orgId, WorkflowState.CERTIFICATE_ISSUED));

        BigDecimal outstanding = invoiceRepository.sumOutstandingBalance(orgId,
                List.of(WorkflowState.INVOICE_SENT, WorkflowState.INVOICE_PARTIALLY_PAID, WorkflowState.INVOICE_OVERDUE));
        stats.setOutstandingAR(outstanding);

        BigDecimal totalPaid = invoiceRepository.sumByStatus(orgId, WorkflowState.INVOICE_PAID);
        stats.setTotalRevenue(totalPaid);

        return stats;
    }
}
