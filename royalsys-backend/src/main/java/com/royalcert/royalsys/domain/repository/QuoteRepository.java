package com.royalcert.royalsys.domain.repository;

import com.royalcert.royalsys.domain.entity.Quote;
import com.royalcert.royalsys.domain.enums.WorkflowState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, UUID> {
    Page<Quote> findByClientId(UUID clientId, Pageable pageable);
    long countByClientIdAndStatus(UUID clientId, WorkflowState status);
}
