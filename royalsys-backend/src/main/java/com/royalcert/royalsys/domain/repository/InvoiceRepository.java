package com.royalcert.royalsys.domain.repository;

import com.royalcert.royalsys.domain.entity.Invoice;
import com.royalcert.royalsys.domain.enums.WorkflowState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
    Page<Invoice> findByOrganizationId(UUID orgId, Pageable pageable);
    Page<Invoice> findByClientId(UUID clientId, Pageable pageable);
    List<Invoice> findByStatusIn(List<WorkflowState> statuses);

    @Query("SELECT COALESCE(SUM(i.balanceDue), 0) FROM Invoice i WHERE i.organization.id = :orgId AND i.status IN :statuses")
    BigDecimal sumOutstandingBalance(UUID orgId, List<WorkflowState> statuses);

    @Query("SELECT COALESCE(SUM(i.totalAmount), 0) FROM Invoice i WHERE i.organization.id = :orgId AND i.status = :status")
    BigDecimal sumByStatus(UUID orgId, WorkflowState status);
}
