package com.royalcert.royalsys.dto;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class LeadTimeReport {
    private UUID projectId;
    private Map<String, String> metrics;

    public LeadTimeReport(UUID projectId, Map<String, Duration> rawMetrics) {
        this.projectId = projectId;
        this.metrics = new LinkedHashMap<>();
        rawMetrics.forEach((key, duration) -> {
            long hours = duration.toHours();
            long days = duration.toDays();
            if (days > 0) {
                this.metrics.put(key, days + " days, " + (hours % 24) + " hours");
            } else {
                this.metrics.put(key, hours + " hours");
            }
        });
    }

    public UUID getProjectId() { return projectId; }
    public Map<String, String> getMetrics() { return metrics; }
}
