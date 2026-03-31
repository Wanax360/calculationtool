package com.royalcert.royalsys.dto;

import com.royalcert.royalsys.domain.entity.MandayCalculation;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class MandayCalculationResponse {

    private UUID id;
    private UUID clientId;
    private UUID schemeId;
    private UUID ruleSetId;
    private Integer employeeCount;
    private String naceCode;
    private String riskLevel;
    private BigDecimal baseMandays;
    private BigDecimal stage1Mandays;
    private BigDecimal stage2Mandays;
    private BigDecimal surveillanceMandays;
    private BigDecimal recertificationMandays;
    private String adjustmentType;
    private BigDecimal adjustmentPercent;
    private String adjustmentReasons;
    private BigDecimal adjustedStage1;
    private BigDecimal adjustedStage2;
    private BigDecimal adjustedSurveillance;
    private BigDecimal adjustedRecertification;
    private Integer siteEmployeeCount;
    private BigDecimal siteBaseMandays;
    private BigDecimal siteStage1;
    private BigDecimal siteStage2;
    private BigDecimal siteSurveillance;
    private BigDecimal siteRecertification;
    private BigDecimal siteAdjustedStage1;
    private BigDecimal siteAdjustedStage2;
    private BigDecimal siteAdjustedSurveillance;
    private BigDecimal siteAdjustedRecertification;
    private Instant calculatedAt;

    public static MandayCalculationResponse from(MandayCalculation calc) {
        MandayCalculationResponse r = new MandayCalculationResponse();
        r.id = calc.getId();
        r.clientId = calc.getClient().getId();
        r.schemeId = calc.getScheme().getId();
        r.ruleSetId = calc.getRuleSet().getId();
        r.employeeCount = calc.getEmployeeCount();
        r.naceCode = calc.getNaceCode();
        r.riskLevel = calc.getRiskLevel();
        r.baseMandays = calc.getBaseMandays();
        r.stage1Mandays = calc.getStage1Mandays();
        r.stage2Mandays = calc.getStage2Mandays();
        r.surveillanceMandays = calc.getSurveillanceMandays();
        r.recertificationMandays = calc.getRecertificationMandays();
        r.adjustmentType = calc.getAdjustmentType();
        r.adjustmentPercent = calc.getAdjustmentPercent();
        r.adjustmentReasons = calc.getAdjustmentReasons();
        r.adjustedStage1 = calc.getAdjustedStage1();
        r.adjustedStage2 = calc.getAdjustedStage2();
        r.adjustedSurveillance = calc.getAdjustedSurveillance();
        r.adjustedRecertification = calc.getAdjustedRecertification();
        r.siteEmployeeCount = calc.getSiteEmployeeCount();
        r.siteBaseMandays = calc.getSiteBaseMandays();
        r.siteStage1 = calc.getSiteStage1();
        r.siteStage2 = calc.getSiteStage2();
        r.siteSurveillance = calc.getSiteSurveillance();
        r.siteRecertification = calc.getSiteRecertification();
        r.siteAdjustedStage1 = calc.getSiteAdjustedStage1();
        r.siteAdjustedStage2 = calc.getSiteAdjustedStage2();
        r.siteAdjustedSurveillance = calc.getSiteAdjustedSurveillance();
        r.siteAdjustedRecertification = calc.getSiteAdjustedRecertification();
        r.calculatedAt = calc.getCalculatedAt();
        return r;
    }

    // Getters
    public UUID getId() { return id; }
    public UUID getClientId() { return clientId; }
    public UUID getSchemeId() { return schemeId; }
    public UUID getRuleSetId() { return ruleSetId; }
    public Integer getEmployeeCount() { return employeeCount; }
    public String getNaceCode() { return naceCode; }
    public String getRiskLevel() { return riskLevel; }
    public BigDecimal getBaseMandays() { return baseMandays; }
    public BigDecimal getStage1Mandays() { return stage1Mandays; }
    public BigDecimal getStage2Mandays() { return stage2Mandays; }
    public BigDecimal getSurveillanceMandays() { return surveillanceMandays; }
    public BigDecimal getRecertificationMandays() { return recertificationMandays; }
    public String getAdjustmentType() { return adjustmentType; }
    public BigDecimal getAdjustmentPercent() { return adjustmentPercent; }
    public String getAdjustmentReasons() { return adjustmentReasons; }
    public BigDecimal getAdjustedStage1() { return adjustedStage1; }
    public BigDecimal getAdjustedStage2() { return adjustedStage2; }
    public BigDecimal getAdjustedSurveillance() { return adjustedSurveillance; }
    public BigDecimal getAdjustedRecertification() { return adjustedRecertification; }
    public Integer getSiteEmployeeCount() { return siteEmployeeCount; }
    public BigDecimal getSiteBaseMandays() { return siteBaseMandays; }
    public BigDecimal getSiteStage1() { return siteStage1; }
    public BigDecimal getSiteStage2() { return siteStage2; }
    public BigDecimal getSiteSurveillance() { return siteSurveillance; }
    public BigDecimal getSiteRecertification() { return siteRecertification; }
    public BigDecimal getSiteAdjustedStage1() { return siteAdjustedStage1; }
    public BigDecimal getSiteAdjustedStage2() { return siteAdjustedStage2; }
    public BigDecimal getSiteAdjustedSurveillance() { return siteAdjustedSurveillance; }
    public BigDecimal getSiteAdjustedRecertification() { return siteAdjustedRecertification; }
    public Instant getCalculatedAt() { return calculatedAt; }
}
