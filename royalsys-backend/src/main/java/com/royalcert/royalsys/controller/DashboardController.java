package com.royalcert.royalsys.controller;

import com.royalcert.royalsys.dto.DashboardStats;
import com.royalcert.royalsys.dto.LeadTimeReport;
import com.royalcert.royalsys.service.DashboardService;
import com.royalcert.royalsys.service.LeadTimeAnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final LeadTimeAnalyticsService leadTimeService;

    public DashboardController(DashboardService dashboardService, LeadTimeAnalyticsService leadTimeService) {
        this.dashboardService = dashboardService;
        this.leadTimeService = leadTimeService;
    }

    @GetMapping("/stats/{orgId}")
    public ResponseEntity<DashboardStats> getStats(@PathVariable UUID orgId) {
        return ResponseEntity.ok(dashboardService.getStats(orgId));
    }

    @GetMapping("/lead-times/{projectId}")
    public ResponseEntity<LeadTimeReport> getLeadTimes(@PathVariable UUID projectId) {
        return ResponseEntity.ok(leadTimeService.getProjectLeadTimes(projectId));
    }
}
