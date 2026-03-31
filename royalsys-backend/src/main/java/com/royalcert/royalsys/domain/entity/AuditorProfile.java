package com.royalcert.royalsys.domain.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "auditor_profiles")
public class AuditorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "auditor_number", unique = true)
    private String auditorNumber;

    private String qualification;

    @Column(name = "years_experience")
    private Integer yearsExperience;

    @Column(name = "monitoring_due_date")
    private LocalDate monitoringDueDate;

    @Column(name = "last_monitored_at")
    private LocalDate lastMonitoredAt;

    @Column(name = "is_active")
    private boolean active = true;

    @OneToMany(mappedBy = "auditor", cascade = CascadeType.ALL)
    private List<AuditorAuthorization> authorizations = new ArrayList<>();

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
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getAuditorNumber() { return auditorNumber; }
    public void setAuditorNumber(String s) { this.auditorNumber = s; }
    public String getQualification() { return qualification; }
    public void setQualification(String s) { this.qualification = s; }
    public Integer getYearsExperience() { return yearsExperience; }
    public void setYearsExperience(Integer i) { this.yearsExperience = i; }
    public LocalDate getMonitoringDueDate() { return monitoringDueDate; }
    public void setMonitoringDueDate(LocalDate d) { this.monitoringDueDate = d; }
    public LocalDate getLastMonitoredAt() { return lastMonitoredAt; }
    public void setLastMonitoredAt(LocalDate d) { this.lastMonitoredAt = d; }
    public boolean isActive() { return active; }
    public void setActive(boolean a) { this.active = a; }
    public List<AuditorAuthorization> getAuthorizations() { return authorizations; }
    public void setAuthorizations(List<AuditorAuthorization> a) { this.authorizations = a; }
}
