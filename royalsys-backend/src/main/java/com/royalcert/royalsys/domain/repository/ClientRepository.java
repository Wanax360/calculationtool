package com.royalcert.royalsys.domain.repository;

import com.royalcert.royalsys.domain.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
    Page<Client> findByOrganizationIdAndActiveTrue(UUID organizationId, Pageable pageable);

    @Query("SELECT c FROM Client c WHERE c.organization.id = :orgId AND " +
           "(LOWER(c.companyName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.contactName) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Client> search(UUID orgId, String search, Pageable pageable);
}
