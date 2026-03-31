package com.royalcert.royalsys.domain.repository;

import com.royalcert.royalsys.domain.entity.MandayCalculation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MandayCalculationRepository extends JpaRepository<MandayCalculation, UUID> {
    Page<MandayCalculation> findByClientId(UUID clientId, Pageable pageable);
    Page<MandayCalculation> findByProjectId(UUID projectId, Pageable pageable);
}
