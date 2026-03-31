package com.royalcert.royalsys.domain.repository;

import com.royalcert.royalsys.domain.entity.Audit;
import com.royalcert.royalsys.domain.enums.WorkflowState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuditRepository extends JpaRepository<Audit, UUID> {
    Page<Audit> findByProjectId(UUID projectId, Pageable pageable);
    List<Audit> findByStatusIn(List<WorkflowState> statuses);

    @Query("SELECT a FROM Audit a WHERE a.project.organization.id = :orgId")
    Page<Audit> findByOrganizationId(UUID orgId, Pageable pageable);

    @Query("SELECT a FROM Audit a WHERE a.leadAuditor.id = :auditorId AND a.status IN :statuses")
    List<Audit> findByLeadAuditorAndStatuses(UUID auditorId, List<WorkflowState> statuses);
}
