package com.royalcert.royalsys.dto;

import java.math.BigDecimal;

public class DashboardStats {
    private long activeProjects;
    private long pendingApplications;
    private long activeCertificates;
    private BigDecimal outstandingAR;
    private BigDecimal totalRevenue;

    public long getActiveProjects() { return activeProjects; }
    public void setActiveProjects(long v) { this.activeProjects = v; }
    public long getPendingApplications() { return pendingApplications; }
    public void setPendingApplications(long v) { this.pendingApplications = v; }
    public long getActiveCertificates() { return activeCertificates; }
    public void setActiveCertificates(long v) { this.activeCertificates = v; }
    public BigDecimal getOutstandingAR() { return outstandingAR; }
    public void setOutstandingAR(BigDecimal v) { this.outstandingAR = v; }
    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal v) { this.totalRevenue = v; }
}
