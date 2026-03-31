package com.royalcert.royalsys.domain.entity;

import com.royalcert.royalsys.domain.enums.RoleType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_team_members")
public class AuditTeamMember {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "audit_id", nullable = false)
    private Audit audit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auditor_id", nullable = false)
    private AuditorProfile auditor;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", nullable = false)
    private RoleType roleType;

    private BigDecimal mandays;

    @Column(name = "created_at")
    private Instant createdAt;

    @PrePersist
    protected void onCreate() { createdAt = Instant.now(); }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Audit getAudit() { return audit; }
    public void setAudit(Audit a) { this.audit = a; }
    public AuditorProfile getAuditor() { return auditor; }
    public void setAuditor(AuditorProfile a) { this.auditor = a; }
    public RoleType getRoleType() { return roleType; }
    public void setRoleType(RoleType r) { this.roleType = r; }
    public BigDecimal getMandays() { return mandays; }
    public void setMandays(BigDecimal m) { this.mandays = m; }
}
