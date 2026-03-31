package com.royalcert.royalsys.domain.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "manday_calculations")
public class MandayCalculation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheme_id", nullable = false)
    private Scheme scheme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rule_set_id", nullable = false)
    private MandayRuleSet ruleSet;

    @Column(name = "employee_count", nullable = false)
    private Integer employeeCount;

    @Column(name = "nace_code")
    private String naceCode;

    @Column(name = "risk_level")
    private String riskLevel;

    @Column(name = "base_mandays", nullable = false)
    private BigDecimal baseMandays;

    @Column(name = "stage1_mandays", nullable = false)
    private BigDecimal stage1Mandays;

    @Column(name = "stage2_mandays", nullable = false)
    private BigDecimal stage2Mandays;

    @Column(name = "surveillance_mandays", nullable = false)
    private BigDecimal surveillanceMandays;

    @Column(name = "recertification_mandays", nullable = false)
    private BigDecimal recertificationMandays;

    @Column(name = "adjustment_type")
    private String adjustmentType;

    @Column(name = "adjustment_percent")
    private BigDecimal adjustmentPercent;

    @Column(name = "adjustment_reasons")
    private String adjustmentReasons;

    @Column(name = "adjusted_stage1")
    private BigDecimal adjustedStage1;

    @Column(name = "adjusted_stage2")
    private BigDecimal adjustedStage2;

    @Column(name = "adjusted_surveillance")
    private BigDecimal adjustedSurveillance;

    @Column(name = "adjusted_recertification")
    private BigDecimal adjustedRecertification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id")
    private ClientSite site;

    @Column(name = "site_employee_count")
    private Integer siteEmployeeCount;

    @Column(name = "site_base_mandays")
    private BigDecimal siteBaseMandays;

    @Column(name = "site_stage1")
    private BigDecimal siteStage1;

    @Column(name = "site_stage2")
    private BigDecimal siteStage2;

    @Column(name = "site_surveillance")
    private BigDecimal siteSurveillance;

    @Column(name = "site_recertification")
    private BigDecimal siteRecertification;

    @Column(name = "site_adjusted_stage1")
    private BigDecimal siteAdjustedStage1;

    @Column(name = "site_adjusted_stage2")
    private BigDecimal siteAdjustedStage2;

    @Column(name = "site_adjusted_surveillance")
    private BigDecimal siteAdjustedSurveillance;

    @Column(name = "site_adjusted_recertification")
    private BigDecimal siteAdjustedRecertification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "override_approved_by")
    private User overrideApprovedBy;

    @Column(name = "override_approved_at")
    private Instant overrideApprovedAt;

    @Column(name = "override_reason")
    private String overrideReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calculated_by")
    private User calculatedBy;

    @Column(name = "calculated_at")
    private Instant calculatedAt;

    @Column(name = "created_at")
    private Instant createdAt;

    @PrePersist
    protected void onCreate() { createdAt = calculatedAt = Instant.now(); }

    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    public Scheme getScheme() { return scheme; }
    public void setScheme(Scheme scheme) { this.scheme = scheme; }
    public MandayRuleSet getRuleSet() { return ruleSet; }
    public void setRuleSet(MandayRuleSet ruleSet) { this.ruleSet = ruleSet; }
    public Integer getEmployeeCount() { return employeeCount; }
    public void setEmployeeCount(Integer employeeCount) { this.employeeCount = employeeCount; }
    public String getNaceCode() { return naceCode; }
    public void setNaceCode(String naceCode) { this.naceCode = naceCode; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public BigDecimal getBaseMandays() { return baseMandays; }
    public void setBaseMandays(BigDecimal baseMandays) { this.baseMandays = baseMandays; }
    public BigDecimal getStage1Mandays() { return stage1Mandays; }
    public void setStage1Mandays(BigDecimal stage1Mandays) { this.stage1Mandays = stage1Mandays; }
    public BigDecimal getStage2Mandays() { return stage2Mandays; }
    public void setStage2Mandays(BigDecimal stage2Mandays) { this.stage2Mandays = stage2Mandays; }
    public BigDecimal getSurveillanceMandays() { return surveillanceMandays; }
    public void setSurveillanceMandays(BigDecimal surveillanceMandays) { this.surveillanceMandays = surveillanceMandays; }
    public BigDecimal getRecertificationMandays() { return recertificationMandays; }
    public void setRecertificationMandays(BigDecimal recertificationMandays) { this.recertificationMandays = recertificationMandays; }
    public String getAdjustmentType() { return adjustmentType; }
    public void setAdjustmentType(String adjustmentType) { this.adjustmentType = adjustmentType; }
    public BigDecimal getAdjustmentPercent() { return adjustmentPercent; }
    public void setAdjustmentPercent(BigDecimal adjustmentPercent) { this.adjustmentPercent = adjustmentPercent; }
    public String getAdjustmentReasons() { return adjustmentReasons; }
    public void setAdjustmentReasons(String adjustmentReasons) { this.adjustmentReasons = adjustmentReasons; }
    public BigDecimal getAdjustedStage1() { return adjustedStage1; }
    public void setAdjustedStage1(BigDecimal adjustedStage1) { this.adjustedStage1 = adjustedStage1; }
    public BigDecimal getAdjustedStage2() { return adjustedStage2; }
    public void setAdjustedStage2(BigDecimal adjustedStage2) { this.adjustedStage2 = adjustedStage2; }
    public BigDecimal getAdjustedSurveillance() { return adjustedSurveillance; }
    public void setAdjustedSurveillance(BigDecimal adjustedSurveillance) { this.adjustedSurveillance = adjustedSurveillance; }
    public BigDecimal getAdjustedRecertification() { return adjustedRecertification; }
    public void setAdjustedRecertification(BigDecimal adjustedRecertification) { this.adjustedRecertification = adjustedRecertification; }
    public ClientSite getSite() { return site; }
    public void setSite(ClientSite site) { this.site = site; }
    public Integer getSiteEmployeeCount() { return siteEmployeeCount; }
    public void setSiteEmployeeCount(Integer siteEmployeeCount) { this.siteEmployeeCount = siteEmployeeCount; }
    public BigDecimal getSiteBaseMandays() { return siteBaseMandays; }
    public void setSiteBaseMandays(BigDecimal siteBaseMandays) { this.siteBaseMandays = siteBaseMandays; }
    public BigDecimal getSiteStage1() { return siteStage1; }
    public void setSiteStage1(BigDecimal siteStage1) { this.siteStage1 = siteStage1; }
    public BigDecimal getSiteStage2() { return siteStage2; }
    public void setSiteStage2(BigDecimal siteStage2) { this.siteStage2 = siteStage2; }
    public BigDecimal getSiteSurveillance() { return siteSurveillance; }
    public void setSiteSurveillance(BigDecimal siteSurveillance) { this.siteSurveillance = siteSurveillance; }
    public BigDecimal getSiteRecertification() { return siteRecertification; }
    public void setSiteRecertification(BigDecimal siteRecertification) { this.siteRecertification = siteRecertification; }
    public BigDecimal getSiteAdjustedStage1() { return siteAdjustedStage1; }
    public void setSiteAdjustedStage1(BigDecimal v) { this.siteAdjustedStage1 = v; }
    public BigDecimal getSiteAdjustedStage2() { return siteAdjustedStage2; }
    public void setSiteAdjustedStage2(BigDecimal v) { this.siteAdjustedStage2 = v; }
    public BigDecimal getSiteAdjustedSurveillance() { return siteAdjustedSurveillance; }
    public void setSiteAdjustedSurveillance(BigDecimal v) { this.siteAdjustedSurveillance = v; }
    public BigDecimal getSiteAdjustedRecertification() { return siteAdjustedRecertification; }
    public void setSiteAdjustedRecertification(BigDecimal v) { this.siteAdjustedRecertification = v; }
    public User getOverrideApprovedBy() { return overrideApprovedBy; }
    public void setOverrideApprovedBy(User u) { this.overrideApprovedBy = u; }
    public Instant getOverrideApprovedAt() { return overrideApprovedAt; }
    public void setOverrideApprovedAt(Instant i) { this.overrideApprovedAt = i; }
    public String getOverrideReason() { return overrideReason; }
    public void setOverrideReason(String s) { this.overrideReason = s; }
    public User getCalculatedBy() { return calculatedBy; }
    public void setCalculatedBy(User u) { this.calculatedBy = u; }
    public Instant getCalculatedAt() { return calculatedAt; }
    public void setCalculatedAt(Instant i) { this.calculatedAt = i; }
}
