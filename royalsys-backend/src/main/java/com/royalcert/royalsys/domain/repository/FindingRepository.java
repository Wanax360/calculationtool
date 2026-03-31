package com.royalcert.royalsys.domain.repository;

import com.royalcert.royalsys.domain.entity.Finding;
import com.royalcert.royalsys.domain.enums.WorkflowState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FindingRepository extends JpaRepository<Finding, UUID> {
    Page<Finding> findByAuditId(UUID auditId, Pageable pageable);
    List<Finding> findByAuditIdAndStatus(UUID auditId, WorkflowState status);
    long countByAuditIdAndStatusNot(UUID auditId, WorkflowState closedStatus);
}
