package com.royalcert.royalsys.domain.repository;

import com.royalcert.royalsys.domain.entity.Certificate;
import com.royalcert.royalsys.domain.enums.WorkflowState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, UUID> {
    Page<Certificate> findByOrganizationId(UUID orgId, Pageable pageable);
    Page<Certificate> findByClientId(UUID clientId, Pageable pageable);
    List<Certificate> findByStatusAndValidToBefore(WorkflowState status, LocalDate date);
    long countByOrganizationIdAndStatus(UUID orgId, WorkflowState status);
}
