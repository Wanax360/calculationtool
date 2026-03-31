package com.royalcert.royalsys.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public class MandayCalculationRequest {

    @NotNull private UUID clientId;
    @NotNull private UUID schemeId;
    @NotNull @Min(1) private Integer employeeCount;
    private String naceCode;
    private String riskLevel;
    private String adjustmentType;
    private BigDecimal adjustmentPercent;
    private String adjustmentReasons;
    private Integer siteEmployeeCount;
    private UUID siteId;

    public UUID getClientId() { return clientId; }
    public void setClientId(UUID clientId) { this.clientId = clientId; }
    public UUID getSchemeId() { return schemeId; }
    public void setSchemeId(UUID schemeId) { this.schemeId = schemeId; }
    public Integer getEmployeeCount() { return employeeCount; }
    public void setEmployeeCount(Integer employeeCount) { this.employeeCount = employeeCount; }
    public String getNaceCode() { return naceCode; }
    public void setNaceCode(String naceCode) { this.naceCode = naceCode; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public String getAdjustmentType() { return adjustmentType; }
    public void setAdjustmentType(String adjustmentType) { this.adjustmentType = adjustmentType; }
    public BigDecimal getAdjustmentPercent() { return adjustmentPercent; }
    public void setAdjustmentPercent(BigDecimal adjustmentPercent) { this.adjustmentPercent = adjustmentPercent; }
    public String getAdjustmentReasons() { return adjustmentReasons; }
    public void setAdjustmentReasons(String adjustmentReasons) { this.adjustmentReasons = adjustmentReasons; }
    public Integer getSiteEmployeeCount() { return siteEmployeeCount; }
    public void setSiteEmployeeCount(Integer siteEmployeeCount) { this.siteEmployeeCount = siteEmployeeCount; }
    public UUID getSiteId() { return siteId; }
    public void setSiteId(UUID siteId) { this.siteId = siteId; }
}
