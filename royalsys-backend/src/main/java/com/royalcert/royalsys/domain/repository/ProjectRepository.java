package com.royalcert.royalsys.domain.repository;

import com.royalcert.royalsys.domain.entity.Project;
import com.royalcert.royalsys.domain.enums.WorkflowState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    Page<Project> findByOrganizationId(UUID organizationId, Pageable pageable);
    Page<Project> findByClientId(UUID clientId, Pageable pageable);
    List<Project> findByStatusIn(List<WorkflowState> statuses);
    long countByOrganizationIdAndStatus(UUID orgId, WorkflowState status);
}
