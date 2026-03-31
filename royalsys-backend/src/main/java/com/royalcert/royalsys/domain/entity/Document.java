package com.royalcert.royalsys.domain.entity;

import com.royalcert.royalsys.domain.enums.DocumentCategory;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(name = "template_id")
    private UUID templateId;

    @Column(name = "entity_type", nullable = false)
    private String entityType;

    @Column(name = "entity_id", nullable = false)
    private UUID entityId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentCategory category;

    @Column(nullable = false)
    private String title;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(nullable = false)
    private Integer version = 1;

    @Column(name = "is_generated")
    private boolean generated;

    @Column(name = "generated_at")
    private Instant generatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by")
    private User uploadedBy;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() { createdAt = updatedAt = Instant.now(); }

    @PreUpdate
    protected void onUpdate() { updatedAt = Instant.now(); }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization o) { this.organization = o; }
    public UUID getTemplateId() { return templateId; }
    public void setTemplateId(UUID u) { this.templateId = u; }
    public String getEntityType() { return entityType; }
    public void setEntityType(String s) { this.entityType = s; }
    public UUID getEntityId() { return entityId; }
    public void setEntityId(UUID u) { this.entityId = u; }
    public DocumentCategory getCategory() { return category; }
    public void setCategory(DocumentCategory c) { this.category = c; }
    public String getTitle() { return title; }
    public void setTitle(String s) { this.title = s; }
    public String getFileName() { return fileName; }
    public void setFileName(String s) { this.fileName = s; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String s) { this.filePath = s; }
    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long l) { this.fileSize = l; }
    public String getMimeType() { return mimeType; }
    public void setMimeType(String s) { this.mimeType = s; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer i) { this.version = i; }
    public boolean isGenerated() { return generated; }
    public void setGenerated(boolean g) { this.generated = g; }
    public Instant getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(Instant i) { this.generatedAt = i; }
    public User getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(User u) { this.uploadedBy = u; }
}
