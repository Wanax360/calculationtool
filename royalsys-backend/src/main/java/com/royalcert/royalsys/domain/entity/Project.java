package com.royalcert.royalsys.domain.entity;

import com.royalcert.royalsys.domain.enums.WorkflowState;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "projects")
public class Project extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    private Contract contract;

    @Column(name = "project_number", nullable = false, unique = true)
    private String projectNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheme_id", nullable = false)
    private Scheme scheme;

    @Column(name = "scope_statement")
    private String scopeStatement;

    @Column(name = "cycle_type")
    private String cycleType;

    @Column(name = "planned_start_date")
    private LocalDate plannedStartDate;

    @Column(name = "planned_end_date")
    private LocalDate plannedEndDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkflowState status = WorkflowState.PROJECT_CREATED;

    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization o) { this.organization = o; }
    public Client getClient() { return client; }
    public void setClient(Client c) { this.client = c; }
    public Contract getContract() { return contract; }
    public void setContract(Contract c) { this.contract = c; }
    public String getProjectNumber() { return projectNumber; }
    public void setProjectNumber(String s) { this.projectNumber = s; }
    public Scheme getScheme() { return scheme; }
    public void setScheme(Scheme s) { this.scheme = s; }
    public String getScopeStatement() { return scopeStatement; }
    public void setScopeStatement(String s) { this.scopeStatement = s; }
    public String getCycleType() { return cycleType; }
    public void setCycleType(String s) { this.cycleType = s; }
    public LocalDate getPlannedStartDate() { return plannedStartDate; }
    public void setPlannedStartDate(LocalDate d) { this.plannedStartDate = d; }
    public LocalDate getPlannedEndDate() { return plannedEndDate; }
    public void setPlannedEndDate(LocalDate d) { this.plannedEndDate = d; }
    public WorkflowState getStatus() { return status; }
    public void setStatus(WorkflowState s) { this.status = s; }
}
