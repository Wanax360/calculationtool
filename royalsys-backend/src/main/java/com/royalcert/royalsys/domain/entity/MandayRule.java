package com.royalcert.royalsys.domain.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "manday_rules")
public class MandayRule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rule_set_id", nullable = false)
    private MandayRuleSet ruleSet;

    @Column(name = "risk_level")
    private String riskLevel;

    @Column(name = "employee_min", nullable = false)
    private Integer employeeMin;

    @Column(name = "employee_max", nullable = false)
    private Integer employeeMax;

    @Column(name = "base_mandays", nullable = false)
    private BigDecimal baseMandays;

    @Column(name = "stage1_ratio", nullable = false)
    private BigDecimal stage1Ratio = new BigDecimal("0.300");

    @Column(name = "stage2_ratio", nullable = false)
    private BigDecimal stage2Ratio = new BigDecimal("0.700");

    @Column(name = "surveillance_divisor", nullable = false)
    private BigDecimal surveillanceDivisor = new BigDecimal("3.00");

    @Column(name = "recert_multiplier", nullable = false)
    private BigDecimal recertMultiplier = new BigDecimal("0.667");

    @Column(name = "created_at")
    private Instant createdAt;

    @PrePersist
    protected void onCreate() { createdAt = Instant.now(); }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public MandayRuleSet getRuleSet() { return ruleSet; }
    public void setRuleSet(MandayRuleSet ruleSet) { this.ruleSet = ruleSet; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public Integer getEmployeeMin() { return employeeMin; }
    public void setEmployeeMin(Integer employeeMin) { this.employeeMin = employeeMin; }
    public Integer getEmployeeMax() { return employeeMax; }
    public void setEmployeeMax(Integer employeeMax) { this.employeeMax = employeeMax; }
    public BigDecimal getBaseMandays() { return baseMandays; }
    public void setBaseMandays(BigDecimal baseMandays) { this.baseMandays = baseMandays; }
    public BigDecimal getStage1Ratio() { return stage1Ratio; }
    public void setStage1Ratio(BigDecimal stage1Ratio) { this.stage1Ratio = stage1Ratio; }
    public BigDecimal getStage2Ratio() { return stage2Ratio; }
    public void setStage2Ratio(BigDecimal stage2Ratio) { this.stage2Ratio = stage2Ratio; }
    public BigDecimal getSurveillanceDivisor() { return surveillanceDivisor; }
    public void setSurveillanceDivisor(BigDecimal surveillanceDivisor) { this.surveillanceDivisor = surveillanceDivisor; }
    public BigDecimal getRecertMultiplier() { return recertMultiplier; }
    public void setRecertMultiplier(BigDecimal recertMultiplier) { this.recertMultiplier = recertMultiplier; }
}
