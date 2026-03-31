package com.royalcert.royalsys.service;

import com.royalcert.royalsys.domain.entity.Audit;
import com.royalcert.royalsys.domain.entity.AuditTeamMember;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SegregationOfDutiesService {

    public void validateReviewerNotOnAuditTeam(Audit audit, UUID reviewerUserId) {
        Set<UUID> auditTeamUserIds = audit.getTeamMembers().stream()
                .map(AuditTeamMember::getAuditor)
                .map(ap -> ap.getUser().getId())
                .collect(Collectors.toSet());

        if (audit.getLeadAuditor() != null) {
            auditTeamUserIds.add(audit.getLeadAuditor().getUser().getId());
        }

        if (auditTeamUserIds.contains(reviewerUserId)) {
            throw new IllegalStateException(
                    "Segregation of duties violation: Technical reviewer must not be a member of the audit team");
        }
    }

    public void validateDecisionMakerIndependence(Audit audit, UUID decisionMakerUserId, UUID reviewerUserId) {
        Set<UUID> auditTeamUserIds = audit.getTeamMembers().stream()
                .map(AuditTeamMember::getAuditor)
                .map(ap -> ap.getUser().getId())
                .collect(Collectors.toSet());

        if (audit.getLeadAuditor() != null) {
            auditTeamUserIds.add(audit.getLeadAuditor().getUser().getId());
        }

        if (auditTeamUserIds.contains(decisionMakerUserId)) {
            throw new IllegalStateException(
                    "Segregation of duties violation: Decision maker must not be a member of the audit team");
        }

        if (decisionMakerUserId.equals(reviewerUserId)) {
            throw new IllegalStateException(
                    "Segregation of duties violation: Decision maker must not be the technical reviewer");
        }
    }
}
