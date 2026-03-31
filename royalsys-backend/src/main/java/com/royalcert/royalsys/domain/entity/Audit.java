package com.royalcert.royalsys.domain.entity;

import com.royalcert.royalsys.domain.enums.AuditType;
import com.royalcert.royalsys.domain.enums.WorkflowState;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "audits")
public class Audit extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(name = "audit_type", nullable = false)
    private AuditType auditType;

    @Column(name = "audit_number", nullable = false, unique = true)
    private String auditNumber;

    @Column(name = "planned_start_date")
    private LocalDate plannedStartDate;

    @Column(name = "planned_end_date")
    private LocalDate plannedEndDate;

    @Column(name = "actual_start_date")
    private LocalDate actualStartDate;

    @Column(name = "actual_end_date")
    private LocalDate actualEndDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_auditor_id")
    private AuditorProfile leadAuditor;

    @Column(name = "plan_issued_at")
    private Instant planIssuedAt;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "report_submitted_at")
    private Instant reportSubmittedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkflowState status = WorkflowState.AUDIT_PLANNED;

    @Column(name = "total_mandays")
    private BigDecimal totalMandays;

    private String notes;

    @OneToMany(mappedBy = "audit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AuditTeamMember> teamMembers = new ArrayList<>();

    public Project getProject() { return project; }
    public void setProject(Project p) { this.project = p; }
    public AuditType getAuditType() { return auditType; }
    public void setAuditType(AuditType t) { this.auditType = t; }
    public String getAuditNumber() { return auditNumber; }
    public void setAuditNumber(String s) { this.auditNumber = s; }
    public LocalDate getPlannedStartDate() { return plannedStartDate; }
    public void setPlannedStartDate(LocalDate d) { this.plannedStartDate = d; }
    public LocalDate getPlannedEndDate() { return plannedEndDate; }
    public void setPlannedEndDate(LocalDate d) { this.plannedEndDate = d; }
    public LocalDate getActualStartDate() { return actualStartDate; }
    public void setActualStartDate(LocalDate d) { this.actualStartDate = d; }
    public LocalDate getActualEndDate() { return actualEndDate; }
    public void setActualEndDate(LocalDate d) { this.actualEndDate = d; }
    public AuditorProfile getLeadAuditor() { return leadAuditor; }
    public void setLeadAuditor(AuditorProfile a) { this.leadAuditor = a; }
    public Instant getPlanIssuedAt() { return planIssuedAt; }
    public void setPlanIssuedAt(Instant i) { this.planIssuedAt = i; }
    public Instant getStartedAt() { return startedAt; }
    public void setStartedAt(Instant i) { this.startedAt = i; }
    public Instant getCompletedAt() { return completedAt; }
    public void setCompletedAt(Instant i) { this.completedAt = i; }
    public Instant getReportSubmittedAt() { return reportSubmittedAt; }
    public void setReportSubmittedAt(Instant i) { this.reportSubmittedAt = i; }
    public WorkflowState getStatus() { return status; }
    public void setStatus(WorkflowState s) { this.status = s; }
    public BigDecimal getTotalMandays() { return totalMandays; }
    public void setTotalMandays(BigDecimal m) { this.totalMandays = m; }
    public String getNotes() { return notes; }
    public void setNotes(String s) { this.notes = s; }
    public List<AuditTeamMember> getTeamMembers() { return teamMembers; }
    public void setTeamMembers(List<AuditTeamMember> t) { this.teamMembers = t; }
}
