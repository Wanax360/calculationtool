package com.royalcert.royalsys.domain.repository;

import com.royalcert.royalsys.domain.entity.Application;
import com.royalcert.royalsys.domain.enums.WorkflowState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, UUID> {
    Page<Application> findByOrganizationId(UUID orgId, Pageable pageable);
    Page<Application> findByClientId(UUID clientId, Pageable pageable);
    long countByOrganizationIdAndStatus(UUID orgId, WorkflowState status);
}
