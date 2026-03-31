package com.royalcert.royalsys.domain.repository;

import com.royalcert.royalsys.domain.entity.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContractRepository extends JpaRepository<Contract, UUID> {
    Page<Contract> findByOrganizationId(UUID orgId, Pageable pageable);
    Page<Contract> findByClientId(UUID clientId, Pageable pageable);
}
