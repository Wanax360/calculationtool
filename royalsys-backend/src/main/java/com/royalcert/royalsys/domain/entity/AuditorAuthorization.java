package com.royalcert.royalsys.domain.entity;

import com.royalcert.royalsys.domain.enums.RoleType;
import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "auditor_authorizations")
public class AuditorAuthorization {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auditor_id", nullable = false)
    private AuditorProfile auditor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheme_id", nullable = false)
    private Scheme scheme;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", nullable = false)
    private RoleType roleType;

    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;

    @Column(name = "valid_to")
    private LocalDate validTo;

    @Column(name = "is_active")
    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorized_by")
    private User authorizedBy;

    @Column(name = "authorized_at")
    private Instant authorizedAt;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() { createdAt = updatedAt = authorizedAt = Instant.now(); }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public AuditorProfile getAuditor() { return auditor; }
    public void setAuditor(AuditorProfile a) { this.auditor = a; }
    public Scheme getScheme() { return scheme; }
    public void setScheme(Scheme s) { this.scheme = s; }
    public RoleType getRoleType() { return roleType; }
    public void setRoleType(RoleType r) { this.roleType = r; }
    public LocalDate getValidFrom() { return validFrom; }
    public void setValidFrom(LocalDate d) { this.validFrom = d; }
    public LocalDate getValidTo() { return validTo; }
    public void setValidTo(LocalDate d) { this.validTo = d; }
    public boolean isActive() { return active; }
    public void setActive(boolean a) { this.active = a; }
    public User getAuthorizedBy() { return authorizedBy; }
    public void setAuthorizedBy(User u) { this.authorizedBy = u; }
    public Instant getAuthorizedAt() { return authorizedAt; }
    public void setAuthorizedAt(Instant i) { this.authorizedAt = i; }
}
