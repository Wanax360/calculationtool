package com.royalcert.royalsys.domain.repository;

import com.royalcert.royalsys.domain.entity.AuditorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuditorProfileRepository extends JpaRepository<AuditorProfile, UUID> {
    Optional<AuditorProfile> findByUserId(UUID userId);
    Optional<AuditorProfile> findByAuditorNumber(String auditorNumber);
}
