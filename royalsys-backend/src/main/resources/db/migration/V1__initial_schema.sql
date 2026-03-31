-- ============================================================
-- RoyalSys V1: Core Schema
-- Certification Body Operations Platform
-- ============================================================

-- Extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- ============================================================
-- 1. ORGANIZATION & USERS
-- ============================================================

CREATE TABLE organizations (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name            VARCHAR(255) NOT NULL,
    legal_name      VARCHAR(255),
    tax_id          VARCHAR(100),
    default_currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    address_line1   VARCHAR(255),
    address_line2   VARCHAR(255),
    city            VARCHAR(100),
    state           VARCHAR(100),
    postal_code     VARCHAR(20),
    country         VARCHAR(3),
    phone           VARCHAR(50),
    email           VARCHAR(255),
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by      VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_by      VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE TABLE users (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    organization_id UUID REFERENCES organizations(id),
    username        VARCHAR(100) NOT NULL UNIQUE,
    email           VARCHAR(255) NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    first_name      VARCHAR(100) NOT NULL,
    last_name       VARCHAR(100) NOT NULL,
    role_type       VARCHAR(50) NOT NULL,
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    last_login_at   TIMESTAMPTZ,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by      VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_by      VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE INDEX idx_users_org ON users(organization_id);
CREATE INDEX idx_users_role ON users(role_type);

-- ============================================================
-- 2. CLIENTS
-- ============================================================

CREATE TABLE clients (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    organization_id UUID NOT NULL REFERENCES organizations(id),
    company_name    VARCHAR(255) NOT NULL,
    legal_name      VARCHAR(255),
    tax_id          VARCHAR(100),
    industry_sector VARCHAR(100),
    nace_code       VARCHAR(20),
    ea_code         VARCHAR(20),
    employee_count  INTEGER,
    address_line1   VARCHAR(255),
    address_line2   VARCHAR(255),
    city            VARCHAR(100),
    state           VARCHAR(100),
    postal_code     VARCHAR(20),
    country         VARCHAR(3),
    contact_name    VARCHAR(255),
    contact_email   VARCHAR(255),
    contact_phone   VARCHAR(50),
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by      VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_by      VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE INDEX idx_clients_org ON clients(organization_id);

CREATE TABLE client_sites (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    client_id       UUID NOT NULL REFERENCES clients(id),
    site_name       VARCHAR(255) NOT NULL,
    address_line1   VARCHAR(255),
    address_line2   VARCHAR(255),
    city            VARCHAR(100),
    state           VARCHAR(100),
    postal_code     VARCHAR(20),
    country         VARCHAR(3),
    employee_count  INTEGER,
    is_main_site    BOOLEAN NOT NULL DEFAULT FALSE,
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_client_sites_client ON client_sites(client_id);

-- ============================================================
-- 3. SCHEMES & SCOPES
-- ============================================================

CREATE TABLE schemes (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    code            VARCHAR(20) NOT NULL UNIQUE,
    name            VARCHAR(255) NOT NULL,
    description     TEXT,
    accreditation_body VARCHAR(100),
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE client_scopes (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    client_id       UUID NOT NULL REFERENCES clients(id),
    scheme_id       UUID NOT NULL REFERENCES schemes(id),
    scope_statement TEXT NOT NULL,
    nace_code       VARCHAR(20),
    ea_code         VARCHAR(20),
    risk_level      VARCHAR(20),
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_client_scopes_client ON client_scopes(client_id);
CREATE INDEX idx_client_scopes_scheme ON client_scopes(scheme_id);

-- ============================================================
-- 4. MANDAY RULE TABLES (Rules-driven calculator)
-- ============================================================

CREATE TABLE manday_rule_sets (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    scheme_id       UUID NOT NULL REFERENCES schemes(id),
    name            VARCHAR(255) NOT NULL,
    version         INTEGER NOT NULL DEFAULT 1,
    effective_from  DATE NOT NULL,
    effective_to    DATE,
    is_current      BOOLEAN NOT NULL DEFAULT TRUE,
    approved_by     UUID REFERENCES users(id),
    approved_at     TIMESTAMPTZ,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(scheme_id, version)
);

CREATE TABLE manday_rules (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    rule_set_id     UUID NOT NULL REFERENCES manday_rule_sets(id),
    risk_level      VARCHAR(20),
    employee_min    INTEGER NOT NULL,
    employee_max    INTEGER NOT NULL,
    base_mandays    NUMERIC(6,2) NOT NULL,
    stage1_ratio    NUMERIC(4,3) NOT NULL DEFAULT 0.300,
    stage2_ratio    NUMERIC(4,3) NOT NULL DEFAULT 0.700,
    surveillance_divisor NUMERIC(4,2) NOT NULL DEFAULT 3.00,
    recert_multiplier NUMERIC(4,3) NOT NULL DEFAULT 0.667,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_manday_rules_ruleset ON manday_rules(rule_set_id);
CREATE INDEX idx_manday_rules_range ON manday_rules(employee_min, employee_max);

CREATE TABLE nace_risk_mappings (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    rule_set_id     UUID NOT NULL REFERENCES manday_rule_sets(id),
    scheme_id       UUID NOT NULL REFERENCES schemes(id),
    nace_code       VARCHAR(20) NOT NULL,
    risk_level      VARCHAR(20) NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_nace_risk_scheme ON nace_risk_mappings(scheme_id, nace_code);

-- ============================================================
-- 5. MANDAY CALCULATIONS
-- ============================================================

CREATE TABLE manday_calculations (
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    project_id          UUID,  -- FK added after projects table
    client_id           UUID NOT NULL REFERENCES clients(id),
    scheme_id           UUID NOT NULL REFERENCES schemes(id),
    rule_set_id         UUID NOT NULL REFERENCES manday_rule_sets(id),
    employee_count      INTEGER NOT NULL,
    nace_code           VARCHAR(20),
    risk_level          VARCHAR(20),
    base_mandays        NUMERIC(6,2) NOT NULL,
    stage1_mandays      NUMERIC(6,2) NOT NULL,
    stage2_mandays      NUMERIC(6,2) NOT NULL,
    surveillance_mandays NUMERIC(6,2) NOT NULL,
    recertification_mandays NUMERIC(6,2) NOT NULL,
    adjustment_type     VARCHAR(20),  -- INCREASE, DECREASE, NONE
    adjustment_percent  NUMERIC(5,2) DEFAULT 0,
    adjustment_reasons  TEXT,
    adjusted_stage1     NUMERIC(6,2),
    adjusted_stage2     NUMERIC(6,2),
    adjusted_surveillance NUMERIC(6,2),
    adjusted_recertification NUMERIC(6,2),
    -- Site calculations
    site_id             UUID REFERENCES client_sites(id),
    site_employee_count INTEGER,
    site_base_mandays   NUMERIC(6,2),
    site_stage1         NUMERIC(6,2),
    site_stage2         NUMERIC(6,2),
    site_surveillance   NUMERIC(6,2),
    site_recertification NUMERIC(6,2),
    site_adjusted_stage1 NUMERIC(6,2),
    site_adjusted_stage2 NUMERIC(6,2),
    site_adjusted_surveillance NUMERIC(6,2),
    site_adjusted_recertification NUMERIC(6,2),
    -- Approval
    override_approved_by UUID REFERENCES users(id),
    override_approved_at TIMESTAMPTZ,
    override_reason     TEXT,
    calculated_by       UUID REFERENCES users(id),
    calculated_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_manday_calc_project ON manday_calculations(project_id);
CREATE INDEX idx_manday_calc_client ON manday_calculations(client_id);

-- ============================================================
-- 6. APPLICATIONS & QUOTES
-- ============================================================

CREATE TABLE applications (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    organization_id UUID NOT NULL REFERENCES organizations(id),
    client_id       UUID NOT NULL REFERENCES clients(id),
    application_number VARCHAR(50) NOT NULL UNIQUE,
    scheme_ids      UUID[] NOT NULL,
    scope_statement TEXT,
    employee_count  INTEGER,
    nace_code       VARCHAR(20),
    received_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    reviewed_by     UUID REFERENCES users(id),
    reviewed_at     TIMESTAMPTZ,
    review_notes    TEXT,
    status          VARCHAR(50) NOT NULL DEFAULT 'APPLICATION_RECEIVED',
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by      VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_by      VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE INDEX idx_applications_client ON applications(client_id);
CREATE INDEX idx_applications_status ON applications(status);

CREATE TABLE quotes (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    application_id  UUID NOT NULL REFERENCES applications(id),
    quote_number    VARCHAR(50) NOT NULL UNIQUE,
    client_id       UUID NOT NULL REFERENCES clients(id),
    currency        VARCHAR(3) NOT NULL DEFAULT 'USD',
    total_amount    NUMERIC(12,2) NOT NULL,
    valid_until     DATE,
    issued_at       TIMESTAMPTZ,
    accepted_at     TIMESTAMPTZ,
    rejected_at     TIMESTAMPTZ,
    rejection_reason TEXT,
    status          VARCHAR(50) NOT NULL DEFAULT 'QUOTE_DRAFTED',
    notes           TEXT,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by      VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_by      VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE INDEX idx_quotes_application ON quotes(application_id);
CREATE INDEX idx_quotes_client ON quotes(client_id);
CREATE INDEX idx_quotes_status ON quotes(status);

CREATE TABLE quote_line_items (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    quote_id        UUID NOT NULL REFERENCES quotes(id) ON DELETE CASCADE,
    description     VARCHAR(500) NOT NULL,
    quantity        NUMERIC(6,2) NOT NULL DEFAULT 1,
    unit_price      NUMERIC(12,2) NOT NULL,
    amount          NUMERIC(12,2) NOT NULL,
    sort_order      INTEGER NOT NULL DEFAULT 0
);

CREATE INDEX idx_quote_items_quote ON quote_line_items(quote_id);

-- ============================================================
-- 7. CONTRACTS
-- ============================================================

CREATE TABLE contracts (
    id                      UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    organization_id         UUID NOT NULL REFERENCES organizations(id),
    client_id               UUID NOT NULL REFERENCES clients(id),
    quote_id                UUID REFERENCES quotes(id),
    contract_number         VARCHAR(50) NOT NULL UNIQUE,
    scheme_ids              UUID[],
    scope_statement         TEXT,
    currency                VARCHAR(3) NOT NULL DEFAULT 'USD',
    total_value             NUMERIC(12,2),
    billing_schedule        VARCHAR(50),  -- UPFRONT, MILESTONE, ANNUAL
    package_sent_at         TIMESTAMPTZ,
    signed_at               TIMESTAMPTZ,
    effective_at            TIMESTAMPTZ,
    expires_at              DATE,
    terminated_at           TIMESTAMPTZ,
    termination_reason      TEXT,
    status                  VARCHAR(50) NOT NULL DEFAULT 'CONTRACT_DRAFTED',
    created_at              TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by              VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_by              VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE INDEX idx_contracts_client ON contracts(client_id);
CREATE INDEX idx_contracts_status ON contracts(status);

-- ============================================================
-- 8. PROJECTS (Certification Cycles)
-- ============================================================

CREATE TABLE projects (
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    organization_id     UUID NOT NULL REFERENCES organizations(id),
    client_id           UUID NOT NULL REFERENCES clients(id),
    contract_id         UUID REFERENCES contracts(id),
    project_number      VARCHAR(50) NOT NULL UNIQUE,
    scheme_id           UUID NOT NULL REFERENCES schemes(id),
    scope_statement     TEXT,
    cycle_type          VARCHAR(50),  -- INITIAL, SURVEILLANCE_1, SURVEILLANCE_2, RECERTIFICATION
    planned_start_date  DATE,
    planned_end_date    DATE,
    status              VARCHAR(50) NOT NULL DEFAULT 'PROJECT_CREATED',
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by          VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_by          VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE INDEX idx_projects_client ON projects(client_id);
CREATE INDEX idx_projects_contract ON projects(contract_id);
CREATE INDEX idx_projects_status ON projects(status);

-- Add FK from manday_calculations to projects
ALTER TABLE manday_calculations ADD CONSTRAINT fk_manday_calc_project
    FOREIGN KEY (project_id) REFERENCES projects(id);

-- ============================================================
-- 9. AUDITOR COMPETENCE & AUTHORIZATION
-- ============================================================

CREATE TABLE auditor_profiles (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id         UUID NOT NULL REFERENCES users(id) UNIQUE,
    auditor_number  VARCHAR(50) UNIQUE,
    qualification   TEXT,
    years_experience INTEGER,
    monitoring_due_date DATE,
    last_monitored_at DATE,
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE auditor_authorizations (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    auditor_id      UUID NOT NULL REFERENCES auditor_profiles(id),
    scheme_id       UUID NOT NULL REFERENCES schemes(id),
    role_type       VARCHAR(50) NOT NULL,  -- LEAD_AUDITOR, AUDITOR, TECHNICAL_EXPERT
    ea_codes        VARCHAR(20)[],
    nace_codes      VARCHAR(20)[],
    authorized_by   UUID REFERENCES users(id),
    authorized_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    valid_from      DATE NOT NULL,
    valid_to        DATE,
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_auditor_auth_auditor ON auditor_authorizations(auditor_id);
CREATE INDEX idx_auditor_auth_scheme ON auditor_authorizations(scheme_id);

CREATE TABLE authorization_snapshots (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    auditor_id      UUID NOT NULL REFERENCES auditor_profiles(id),
    audit_id        UUID,  -- FK added after audits table
    snapshot_data   JSONB NOT NULL,
    captured_at     TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- ============================================================
-- 10. AUDITS
-- ============================================================

CREATE TABLE audits (
    id                      UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    project_id              UUID NOT NULL REFERENCES projects(id),
    audit_type              VARCHAR(50) NOT NULL,
    audit_number            VARCHAR(50) NOT NULL UNIQUE,
    planned_start_date      DATE,
    planned_end_date        DATE,
    actual_start_date       DATE,
    actual_end_date         DATE,
    lead_auditor_id         UUID REFERENCES auditor_profiles(id),
    plan_issued_at          TIMESTAMPTZ,
    started_at              TIMESTAMPTZ,
    completed_at            TIMESTAMPTZ,
    report_submitted_at     TIMESTAMPTZ,
    status                  VARCHAR(50) NOT NULL DEFAULT 'AUDIT_PLANNED',
    total_mandays           NUMERIC(6,2),
    notes                   TEXT,
    created_at              TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by              VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_by              VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE INDEX idx_audits_project ON audits(project_id);
CREATE INDEX idx_audits_lead ON audits(lead_auditor_id);
CREATE INDEX idx_audits_status ON audits(status);

-- Add FK from authorization_snapshots to audits
ALTER TABLE authorization_snapshots ADD CONSTRAINT fk_auth_snapshot_audit
    FOREIGN KEY (audit_id) REFERENCES audits(id);

CREATE TABLE audit_team_members (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    audit_id        UUID NOT NULL REFERENCES audits(id),
    auditor_id      UUID NOT NULL REFERENCES auditor_profiles(id),
    role_type       VARCHAR(50) NOT NULL,
    mandays         NUMERIC(6,2),
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_audit_team_audit ON audit_team_members(audit_id);

-- ============================================================
-- 11. TECHNICAL REVIEW & CERTIFICATION DECISION
-- (Segregation of duties enforced at application level)
-- ============================================================

CREATE TABLE technical_reviews (
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    audit_id            UUID NOT NULL REFERENCES audits(id),
    reviewer_id         UUID NOT NULL REFERENCES users(id),
    started_at          TIMESTAMPTZ,
    completed_at        TIMESTAMPTZ,
    outcome             VARCHAR(50),  -- APPROVED, RETURNED, REJECTED
    comments            TEXT,
    status              VARCHAR(50) NOT NULL DEFAULT 'TECHNICAL_REVIEW_PENDING',
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by          VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_by          VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE INDEX idx_tech_review_audit ON technical_reviews(audit_id);
CREATE INDEX idx_tech_review_reviewer ON technical_reviews(reviewer_id);

CREATE TABLE certification_decisions (
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    audit_id            UUID NOT NULL REFERENCES audits(id),
    technical_review_id UUID REFERENCES technical_reviews(id),
    decision_maker_id   UUID NOT NULL REFERENCES users(id),
    started_at          TIMESTAMPTZ,
    completed_at        TIMESTAMPTZ,
    decision            VARCHAR(50),  -- APPROVED, REJECTED, DEFERRED
    conditions          TEXT,
    justification       TEXT,
    status              VARCHAR(50) NOT NULL DEFAULT 'DECISION_PENDING',
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by          VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_by          VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE INDEX idx_cert_decision_audit ON certification_decisions(audit_id);

-- ============================================================
-- 12. CERTIFICATES
-- ============================================================

CREATE TABLE certificates (
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    organization_id     UUID NOT NULL REFERENCES organizations(id),
    client_id           UUID NOT NULL REFERENCES clients(id),
    project_id          UUID NOT NULL REFERENCES projects(id),
    decision_id         UUID REFERENCES certification_decisions(id),
    certificate_number  VARCHAR(100) NOT NULL UNIQUE,
    scheme_id           UUID NOT NULL REFERENCES schemes(id),
    scope_statement     TEXT NOT NULL,
    issued_at           TIMESTAMPTZ,
    valid_from          DATE NOT NULL,
    valid_to            DATE NOT NULL,
    suspended_at        TIMESTAMPTZ,
    suspension_reason   TEXT,
    withdrawn_at        TIMESTAMPTZ,
    withdrawal_reason   TEXT,
    status              VARCHAR(50) NOT NULL DEFAULT 'CERTIFICATE_DRAFTED',
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by          VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_by          VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE INDEX idx_certificates_client ON certificates(client_id);
CREATE INDEX idx_certificates_project ON certificates(project_id);
CREATE INDEX idx_certificates_status ON certificates(status);

-- ============================================================
-- 13. FINDINGS / NON-CONFORMITIES
-- ============================================================

CREATE TABLE findings (
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    audit_id            UUID NOT NULL REFERENCES audits(id),
    finding_number      VARCHAR(50) NOT NULL,
    severity            VARCHAR(50) NOT NULL,
    clause_reference    VARCHAR(50),
    description         TEXT NOT NULL,
    objective_evidence  TEXT,
    issued_at           TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    response_due_date   DATE,
    response_received_at TIMESTAMPTZ,
    corrective_action   TEXT,
    reviewed_by         UUID REFERENCES users(id),
    reviewed_at         TIMESTAMPTZ,
    closed_at           TIMESTAMPTZ,
    closed_by           UUID REFERENCES users(id),
    status              VARCHAR(50) NOT NULL DEFAULT 'FINDING_ISSUED',
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by          VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_by          VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE INDEX idx_findings_audit ON findings(audit_id);
CREATE INDEX idx_findings_status ON findings(status);
CREATE INDEX idx_findings_severity ON findings(severity);

-- ============================================================
-- 14. INVOICING & ACCOUNTS RECEIVABLE
-- ============================================================

CREATE TABLE invoices (
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    organization_id     UUID NOT NULL REFERENCES organizations(id),
    client_id           UUID NOT NULL REFERENCES clients(id),
    project_id          UUID REFERENCES projects(id),
    contract_id         UUID REFERENCES contracts(id),
    invoice_number      VARCHAR(50) NOT NULL UNIQUE,
    currency            VARCHAR(3) NOT NULL DEFAULT 'USD',
    subtotal            NUMERIC(12,2) NOT NULL,
    tax_rate            NUMERIC(5,2) DEFAULT 0,
    tax_amount          NUMERIC(12,2) DEFAULT 0,
    total_amount        NUMERIC(12,2) NOT NULL,
    amount_paid         NUMERIC(12,2) NOT NULL DEFAULT 0,
    balance_due         NUMERIC(12,2) NOT NULL,
    issued_at           TIMESTAMPTZ,
    due_date            DATE,
    sent_at             TIMESTAMPTZ,
    billing_milestone   VARCHAR(100),
    notes               TEXT,
    status              VARCHAR(50) NOT NULL DEFAULT 'INVOICE_DRAFTED',
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by          VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    updated_by          VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE INDEX idx_invoices_client ON invoices(client_id);
CREATE INDEX idx_invoices_project ON invoices(project_id);
CREATE INDEX idx_invoices_status ON invoices(status);
CREATE INDEX idx_invoices_due ON invoices(due_date);

CREATE TABLE invoice_line_items (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    invoice_id      UUID NOT NULL REFERENCES invoices(id) ON DELETE CASCADE,
    description     VARCHAR(500) NOT NULL,
    quantity        NUMERIC(6,2) NOT NULL DEFAULT 1,
    unit_price      NUMERIC(12,2) NOT NULL,
    amount          NUMERIC(12,2) NOT NULL,
    sort_order      INTEGER NOT NULL DEFAULT 0
);

CREATE INDEX idx_invoice_items_invoice ON invoice_line_items(invoice_id);

CREATE TABLE payments (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    invoice_id      UUID NOT NULL REFERENCES invoices(id),
    amount          NUMERIC(12,2) NOT NULL,
    payment_method  VARCHAR(50),
    payment_reference VARCHAR(255),
    received_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    notes           TEXT,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by      VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE INDEX idx_payments_invoice ON payments(invoice_id);

CREATE TABLE credit_notes (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    invoice_id      UUID NOT NULL REFERENCES invoices(id),
    credit_note_number VARCHAR(50) NOT NULL UNIQUE,
    amount          NUMERIC(12,2) NOT NULL,
    reason          TEXT NOT NULL,
    issued_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by      VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE INDEX idx_credit_notes_invoice ON credit_notes(invoice_id);

CREATE TABLE ar_reminders (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    invoice_id      UUID NOT NULL REFERENCES invoices(id),
    reminder_number INTEGER NOT NULL DEFAULT 1,
    sent_at         TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    channel         VARCHAR(20),  -- EMAIL, SMS, LETTER
    notes           TEXT,
    created_by      VARCHAR(100) NOT NULL DEFAULT 'SYSTEM'
);

CREATE INDEX idx_ar_reminders_invoice ON ar_reminders(invoice_id);

-- ============================================================
-- 15. WORKFLOW EVENTS
-- ============================================================

CREATE TABLE workflow_events (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    entity_type     VARCHAR(50) NOT NULL,  -- APPLICATION, QUOTE, CONTRACT, PROJECT, AUDIT, etc.
    entity_id       UUID NOT NULL,
    from_state      VARCHAR(50),
    to_state        VARCHAR(50) NOT NULL,
    event_type      VARCHAR(100) NOT NULL,
    occurred_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    performed_by    UUID REFERENCES users(id),
    notes           TEXT,
    metadata        JSONB,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_wf_events_entity ON workflow_events(entity_type, entity_id);
CREATE INDEX idx_wf_events_type ON workflow_events(event_type);
CREATE INDEX idx_wf_events_time ON workflow_events(occurred_at);

-- ============================================================
-- 16. DOCUMENT MANAGEMENT
-- ============================================================

CREATE TABLE document_templates (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    organization_id UUID NOT NULL REFERENCES organizations(id),
    category        VARCHAR(50) NOT NULL,
    name            VARCHAR(255) NOT NULL,
    description     TEXT,
    template_path   VARCHAR(500),
    version         INTEGER NOT NULL DEFAULT 1,
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE documents (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    organization_id UUID NOT NULL REFERENCES organizations(id),
    template_id     UUID REFERENCES document_templates(id),
    entity_type     VARCHAR(50) NOT NULL,
    entity_id       UUID NOT NULL,
    category        VARCHAR(50) NOT NULL,
    title           VARCHAR(255) NOT NULL,
    file_name       VARCHAR(255),
    file_path       VARCHAR(500),
    file_size       BIGINT,
    mime_type       VARCHAR(100),
    version         INTEGER NOT NULL DEFAULT 1,
    is_generated    BOOLEAN NOT NULL DEFAULT FALSE,
    generated_at    TIMESTAMPTZ,
    uploaded_by     UUID REFERENCES users(id),
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_documents_entity ON documents(entity_type, entity_id);
CREATE INDEX idx_documents_category ON documents(category);

-- ============================================================
-- 17. NOTIFICATIONS
-- ============================================================

CREATE TABLE notifications (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id         UUID NOT NULL REFERENCES users(id),
    title           VARCHAR(255) NOT NULL,
    message         TEXT NOT NULL,
    link            VARCHAR(500),
    is_read         BOOLEAN NOT NULL DEFAULT FALSE,
    read_at         TIMESTAMPTZ,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_notifications_user ON notifications(user_id, is_read);

-- ============================================================
-- 18. AUDIT TRAIL
-- ============================================================

CREATE TABLE audit_trail (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    entity_type     VARCHAR(50) NOT NULL,
    entity_id       UUID NOT NULL,
    action          VARCHAR(20) NOT NULL,  -- CREATE, UPDATE, DELETE
    field_name      VARCHAR(100),
    old_value       TEXT,
    new_value       TEXT,
    performed_by    VARCHAR(100) NOT NULL,
    performed_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    ip_address      VARCHAR(45)
);

CREATE INDEX idx_audit_trail_entity ON audit_trail(entity_type, entity_id);
CREATE INDEX idx_audit_trail_time ON audit_trail(performed_at);
