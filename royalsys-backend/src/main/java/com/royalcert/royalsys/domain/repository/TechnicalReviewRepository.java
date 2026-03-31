package com.royalcert.royalsys.domain.repository;

import com.royalcert.royalsys.domain.entity.TechnicalReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TechnicalReviewRepository extends JpaRepository<TechnicalReview, UUID> {
    List<TechnicalReview> findByAuditId(UUID auditId);
    List<TechnicalReview> findByReviewerId(UUID reviewerId);
}
