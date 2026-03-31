package com.royalcert.royalsys.domain.repository;

import com.royalcert.royalsys.domain.entity.MandayRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MandayRuleRepository extends JpaRepository<MandayRule, UUID> {

    @Query("SELECT r FROM MandayRule r WHERE r.ruleSet.id = :ruleSetId " +
           "AND (r.riskLevel IS NULL OR r.riskLevel = :riskLevel) " +
           "AND r.employeeMin <= :employeeCount AND r.employeeMax >= :employeeCount")
    Optional<MandayRule> findMatchingRule(UUID ruleSetId, String riskLevel, int employeeCount);

    @Query("SELECT r FROM MandayRule r WHERE r.ruleSet.id = :ruleSetId " +
           "AND r.riskLevel IS NULL " +
           "AND r.employeeMin <= :employeeCount AND r.employeeMax >= :employeeCount")
    Optional<MandayRule> findMatchingRuleNoRisk(UUID ruleSetId, int employeeCount);
}
