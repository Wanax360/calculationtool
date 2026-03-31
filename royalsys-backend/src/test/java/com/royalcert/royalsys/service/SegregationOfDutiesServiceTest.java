package com.royalcert.royalsys.service;

import com.royalcert.royalsys.domain.entity.*;
import com.royalcert.royalsys.domain.enums.AuditType;
import com.royalcert.royalsys.domain.enums.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SegregationOfDutiesServiceTest {

    private SegregationOfDutiesService service;
    private Audit audit;
    private User auditorUser;
    private User reviewerUser;
    private User decisionMakerUser;

    @BeforeEach
    void setUp() {
        service = new SegregationOfDutiesService();

        auditorUser = new User();
        auditorUser.setId(UUID.randomUUID());

        reviewerUser = new User();
        reviewerUser.setId(UUID.randomUUID());

        decisionMakerUser = new User();
        decisionMakerUser.setId(UUID.randomUUID());

        AuditorProfile leadAuditorProfile = new AuditorProfile();
        leadAuditorProfile.setId(UUID.randomUUID());
        leadAuditorProfile.setUser(auditorUser);

        audit = new Audit();
        audit.setId(UUID.randomUUID());
        audit.setAuditType(AuditType.STAGE_2);
        audit.setAuditNumber("AUD-001");
        audit.setLeadAuditor(leadAuditorProfile);

        AuditorProfile teamMemberProfile = new AuditorProfile();
        teamMemberProfile.setId(UUID.randomUUID());
        teamMemberProfile.setUser(auditorUser);

        AuditTeamMember member = new AuditTeamMember();
        member.setAudit(audit);
        member.setAuditor(teamMemberProfile);
        member.setRoleType(RoleType.LEAD_AUDITOR);
        audit.getTeamMembers().add(member);
    }

    @Test
    void testReviewerCannotBeAuditor() {
        assertThrows(IllegalStateException.class,
                () -> service.validateReviewerNotOnAuditTeam(audit, auditorUser.getId()));
    }

    @Test
    void testIndependentReviewerAllowed() {
        assertDoesNotThrow(
                () -> service.validateReviewerNotOnAuditTeam(audit, reviewerUser.getId()));
    }

    @Test
    void testDecisionMakerCannotBeAuditor() {
        assertThrows(IllegalStateException.class,
                () -> service.validateDecisionMakerIndependence(audit, auditorUser.getId(), reviewerUser.getId()));
    }

    @Test
    void testDecisionMakerCannotBeReviewer() {
        assertThrows(IllegalStateException.class,
                () -> service.validateDecisionMakerIndependence(audit, reviewerUser.getId(), reviewerUser.getId()));
    }

    @Test
    void testIndependentDecisionMakerAllowed() {
        assertDoesNotThrow(
                () -> service.validateDecisionMakerIndependence(audit, decisionMakerUser.getId(), reviewerUser.getId()));
    }
}
