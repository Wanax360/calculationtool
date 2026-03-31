package com.royalcert.royalsys.domain.repository;

import com.royalcert.royalsys.domain.entity.Document;
import com.royalcert.royalsys.domain.enums.DocumentCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {
    List<Document> findByEntityTypeAndEntityId(String entityType, UUID entityId);
    Page<Document> findByOrganizationIdAndCategory(UUID orgId, DocumentCategory category, Pageable pageable);
}
