package com.royalcert.royalsys.domain.repository;

import com.royalcert.royalsys.domain.entity.MandayRuleSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MandayRuleSetRepository extends JpaRepository<MandayRuleSet, UUID> {
    Optional<MandayRuleSet> findBySchemeIdAndCurrentTrue(UUID schemeId);
}
