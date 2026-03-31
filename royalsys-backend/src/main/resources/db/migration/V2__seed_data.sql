-- ============================================================
-- RoyalSys V2: Seed Data
-- ============================================================

-- Default Organization
INSERT INTO organizations (id, name, legal_name, default_currency, country, email)
VALUES ('00000000-0000-0000-0000-000000000001', 'RoyalCert International Registrars',
        'RoyalCert International Registrars Ltd', 'USD', 'GBR', 'info@royalcert.com');

-- Schemes
INSERT INTO schemes (id, code, name, description, accreditation_body) VALUES
('10000000-0000-0000-0000-000000000001', 'ISO_9001', 'ISO 9001:2015', 'Quality Management Systems', 'UKAS'),
('10000000-0000-0000-0000-000000000002', 'ISO_14001', 'ISO 14001:2015', 'Environmental Management Systems', 'UKAS'),
('10000000-0000-0000-0000-000000000003', 'ISO_45001', 'ISO 45001:2018', 'Occupational Health and Safety Management Systems', 'UKAS'),
('10000000-0000-0000-0000-000000000004', 'ISO_22000', 'ISO 22000:2018', 'Food Safety Management Systems', 'UKAS'),
('10000000-0000-0000-0000-000000000005', 'ISO_27001', 'ISO 27001:2022', 'Information Security Management Systems', 'UKAS');

-- Admin User (password: admin123 - BCrypt)
INSERT INTO users (id, organization_id, username, email, password_hash, first_name, last_name, role_type)
VALUES ('20000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001',
        'admin', 'admin@royalcert.com',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'System', 'Administrator', 'ADMINISTRATOR');

-- Sample Auditor Users
INSERT INTO users (id, organization_id, username, email, password_hash, first_name, last_name, role_type) VALUES
('20000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000001',
 'jsmith', 'j.smith@royalcert.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
 'John', 'Smith', 'LEAD_AUDITOR'),
('20000000-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000001',
 'mwilson', 'mwilson@royalcert.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
 'Mary', 'Wilson', 'TECHNICAL_REVIEWER'),
('20000000-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000001',
 'rbrown', 'rbrown@royalcert.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
 'Robert', 'Brown', 'CERTIFICATION_DECISION_MAKER'),
('20000000-0000-0000-0000-000000000005', '00000000-0000-0000-0000-000000000001',
 'sjones', 'sjones@royalcert.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
 'Sarah', 'Jones', 'PLANNER'),
('20000000-0000-0000-0000-000000000006', '00000000-0000-0000-0000-000000000001',
 'finance', 'finance@royalcert.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
 'Finance', 'Team', 'FINANCE');

-- Auditor Profiles
INSERT INTO auditor_profiles (id, user_id, auditor_number, qualification, years_experience) VALUES
('30000000-0000-0000-0000-000000000001', '20000000-0000-0000-0000-000000000002', 'AUD-001', 'Lead Auditor QMS/EMS', 15),
('30000000-0000-0000-0000-000000000002', '20000000-0000-0000-0000-000000000003', 'AUD-002', 'Technical Reviewer', 20);

-- Auditor Authorizations
INSERT INTO auditor_authorizations (auditor_id, scheme_id, role_type, ea_codes, nace_codes, authorized_by, valid_from) VALUES
('30000000-0000-0000-0000-000000000001', '10000000-0000-0000-0000-000000000001', 'LEAD_AUDITOR',
 ARRAY['17','18','19','22','28','29'], ARRAY['25','26','27','28','29'], '20000000-0000-0000-0000-000000000001', '2024-01-01'),
('30000000-0000-0000-0000-000000000001', '10000000-0000-0000-0000-000000000002', 'LEAD_AUDITOR',
 ARRAY['17','18','19'], ARRAY['25','26','27'], '20000000-0000-0000-0000-000000000001', '2024-01-01'),
('30000000-0000-0000-0000-000000000001', '10000000-0000-0000-0000-000000000003', 'AUDITOR',
 ARRAY['17','18'], ARRAY['25','26'], '20000000-0000-0000-0000-000000000001', '2024-01-01');

-- ISO 9001 Manday Rule Set
INSERT INTO manday_rule_sets (id, scheme_id, name, version, effective_from, approved_by, approved_at) VALUES
('40000000-0000-0000-0000-000000000001', '10000000-0000-0000-0000-000000000001',
 'ISO 9001 Manday Table v1', 1, '2024-01-01', '20000000-0000-0000-0000-000000000001', NOW());

-- ISO 9001 Manday Rules (from original tablo_9001 breakpoints)
INSERT INTO manday_rules (rule_set_id, employee_min, employee_max, base_mandays) VALUES
('40000000-0000-0000-0000-000000000001', 1, 5, 1.5),
('40000000-0000-0000-0000-000000000001', 6, 10, 2.0),
('40000000-0000-0000-0000-000000000001', 11, 15, 2.5),
('40000000-0000-0000-0000-000000000001', 16, 25, 3.0),
('40000000-0000-0000-0000-000000000001', 26, 45, 4.0),
('40000000-0000-0000-0000-000000000001', 46, 65, 5.0),
('40000000-0000-0000-0000-000000000001', 66, 85, 6.0),
('40000000-0000-0000-0000-000000000001', 86, 125, 7.0),
('40000000-0000-0000-0000-000000000001', 126, 175, 8.0),
('40000000-0000-0000-0000-000000000001', 176, 275, 9.0),
('40000000-0000-0000-0000-000000000001', 276, 425, 10.0),
('40000000-0000-0000-0000-000000000001', 426, 626, 11.0),
('40000000-0000-0000-0000-000000000001', 627, 875, 12.0),
('40000000-0000-0000-0000-000000000001', 876, 1175, 13.0);

-- ISO 14001/45001 Rule Sets
INSERT INTO manday_rule_sets (id, scheme_id, name, version, effective_from, approved_by, approved_at) VALUES
('40000000-0000-0000-0000-000000000002', '10000000-0000-0000-0000-000000000002',
 'ISO 14001 Manday Table v1', 1, '2024-01-01', '20000000-0000-0000-0000-000000000001', NOW()),
('40000000-0000-0000-0000-000000000003', '10000000-0000-0000-0000-000000000003',
 'ISO 45001 Manday Table v1', 1, '2024-01-01', '20000000-0000-0000-0000-000000000001', NOW());

-- ISO 14001/45001 High Risk Rules
INSERT INTO manday_rules (rule_set_id, risk_level, employee_min, employee_max, base_mandays) VALUES
('40000000-0000-0000-0000-000000000002', 'HIGH', 1, 5, 3.0),
('40000000-0000-0000-0000-000000000002', 'HIGH', 6, 10, 3.5),
('40000000-0000-0000-0000-000000000002', 'HIGH', 11, 15, 4.5),
('40000000-0000-0000-0000-000000000002', 'HIGH', 16, 25, 5.5),
('40000000-0000-0000-0000-000000000002', 'HIGH', 26, 45, 7.0),
('40000000-0000-0000-0000-000000000002', 'HIGH', 46, 65, 8.0),
('40000000-0000-0000-0000-000000000002', 'HIGH', 66, 85, 9.0),
('40000000-0000-0000-0000-000000000002', 'HIGH', 86, 125, 11.0),
('40000000-0000-0000-0000-000000000002', 'HIGH', 126, 175, 12.0),
('40000000-0000-0000-0000-000000000002', 'HIGH', 176, 275, 13.0),
('40000000-0000-0000-0000-000000000002', 'HIGH', 276, 425, 15.0),
('40000000-0000-0000-0000-000000000002', 'HIGH', 426, 625, 16.0),
('40000000-0000-0000-0000-000000000002', 'HIGH', 626, 875, 17.0),
('40000000-0000-0000-0000-000000000002', 'HIGH', 876, 1175, 19.0);

-- ISO 14001/45001 Medium Risk Rules
INSERT INTO manday_rules (rule_set_id, risk_level, employee_min, employee_max, base_mandays) VALUES
('40000000-0000-0000-0000-000000000002', 'MEDIUM', 1, 5, 2.5),
('40000000-0000-0000-0000-000000000002', 'MEDIUM', 6, 10, 3.0),
('40000000-0000-0000-0000-000000000002', 'MEDIUM', 11, 15, 3.5),
('40000000-0000-0000-0000-000000000002', 'MEDIUM', 16, 25, 4.5),
('40000000-0000-0000-0000-000000000002', 'MEDIUM', 26, 45, 5.5),
('40000000-0000-0000-0000-000000000002', 'MEDIUM', 46, 65, 6.0),
('40000000-0000-0000-0000-000000000002', 'MEDIUM', 66, 85, 7.0),
('40000000-0000-0000-0000-000000000002', 'MEDIUM', 86, 125, 8.0),
('40000000-0000-0000-0000-000000000002', 'MEDIUM', 126, 175, 9.0),
('40000000-0000-0000-0000-000000000002', 'MEDIUM', 176, 275, 10.0),
('40000000-0000-0000-0000-000000000002', 'MEDIUM', 276, 425, 11.0),
('40000000-0000-0000-0000-000000000002', 'MEDIUM', 426, 625, 12.0),
('40000000-0000-0000-0000-000000000002', 'MEDIUM', 626, 875, 13.0),
('40000000-0000-0000-0000-000000000002', 'MEDIUM', 876, 1175, 15.0);

-- ISO 14001/45001 Low Risk Rules
INSERT INTO manday_rules (rule_set_id, risk_level, employee_min, employee_max, base_mandays) VALUES
('40000000-0000-0000-0000-000000000002', 'LOW', 1, 5, 2.5),
('40000000-0000-0000-0000-000000000002', 'LOW', 6, 15, 3.0),
('40000000-0000-0000-0000-000000000002', 'LOW', 16, 25, 3.5),
('40000000-0000-0000-0000-000000000002', 'LOW', 26, 45, 4.0),
('40000000-0000-0000-0000-000000000002', 'LOW', 46, 65, 4.5),
('40000000-0000-0000-0000-000000000002', 'LOW', 66, 85, 5.0),
('40000000-0000-0000-0000-000000000002', 'LOW', 86, 125, 5.5),
('40000000-0000-0000-0000-000000000002', 'LOW', 126, 175, 6.0),
('40000000-0000-0000-0000-000000000002', 'LOW', 176, 275, 7.0),
('40000000-0000-0000-0000-000000000002', 'LOW', 276, 425, 8.0),
('40000000-0000-0000-0000-000000000002', 'LOW', 426, 625, 9.0),
('40000000-0000-0000-0000-000000000002', 'LOW', 626, 875, 10.0),
('40000000-0000-0000-0000-000000000002', 'LOW', 876, 1175, 11.0);

-- ISO 14001 Limited Risk Rules
INSERT INTO manday_rules (rule_set_id, risk_level, employee_min, employee_max, base_mandays) VALUES
('40000000-0000-0000-0000-000000000002', 'LIMITED', 1, 5, 2.5),
('40000000-0000-0000-0000-000000000002', 'LIMITED', 6, 45, 3.0),
('40000000-0000-0000-0000-000000000002', 'LIMITED', 46, 85, 3.5),
('40000000-0000-0000-0000-000000000002', 'LIMITED', 86, 125, 4.0),
('40000000-0000-0000-0000-000000000002', 'LIMITED', 126, 175, 4.5),
('40000000-0000-0000-0000-000000000002', 'LIMITED', 176, 275, 5.0),
('40000000-0000-0000-0000-000000000002', 'LIMITED', 276, 425, 5.5),
('40000000-0000-0000-0000-000000000002', 'LIMITED', 426, 625, 6.0),
('40000000-0000-0000-0000-000000000002', 'LIMITED', 626, 875, 6.5),
('40000000-0000-0000-0000-000000000002', 'LIMITED', 876, 1175, 7.0);

-- Copy same rules for ISO 45001 (High/Medium/Low only, no Limited)
INSERT INTO manday_rules (rule_set_id, risk_level, employee_min, employee_max, base_mandays)
SELECT '40000000-0000-0000-0000-000000000003', risk_level, employee_min, employee_max, base_mandays
FROM manday_rules WHERE rule_set_id = '40000000-0000-0000-0000-000000000002' AND risk_level IN ('HIGH', 'MEDIUM', 'LOW');

-- Sample NACE Risk Mappings for ISO 9001
INSERT INTO nace_risk_mappings (rule_set_id, scheme_id, nace_code, risk_level) VALUES
('40000000-0000-0000-0000-000000000001', '10000000-0000-0000-0000-000000000001', '1.1', 'MEDIUM'),
('40000000-0000-0000-0000-000000000001', '10000000-0000-0000-0000-000000000001', '25.1', 'HIGH'),
('40000000-0000-0000-0000-000000000001', '10000000-0000-0000-0000-000000000001', '46.1', 'LOW'),
('40000000-0000-0000-0000-000000000002', '10000000-0000-0000-0000-000000000002', '1.1', 'MEDIUM'),
('40000000-0000-0000-0000-000000000002', '10000000-0000-0000-0000-000000000002', '25.1', 'HIGH'),
('40000000-0000-0000-0000-000000000003', '10000000-0000-0000-0000-000000000003', '1.1', 'MEDIUM'),
('40000000-0000-0000-0000-000000000003', '10000000-0000-0000-0000-000000000003', '25.1', 'HIGH');

-- Sample Client
INSERT INTO clients (id, organization_id, company_name, legal_name, industry_sector, nace_code, ea_code, employee_count,
                     city, country, contact_name, contact_email, contact_phone) VALUES
('50000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001',
 'Acme Manufacturing Ltd', 'Acme Manufacturing Limited', 'Manufacturing', '25.1', '17', 150,
 'London', 'GBR', 'James Taylor', 'j.taylor@acme.com', '+44 20 1234 5678');

-- Sample Client Site
INSERT INTO client_sites (id, client_id, site_name, city, country, employee_count, is_main_site) VALUES
('51000000-0000-0000-0000-000000000001', '50000000-0000-0000-0000-000000000001',
 'Acme HQ', 'London', 'GBR', 120, TRUE),
('51000000-0000-0000-0000-000000000002', '50000000-0000-0000-0000-000000000001',
 'Acme Birmingham Plant', 'Birmingham', 'GBR', 30, FALSE);

-- Document Templates
INSERT INTO document_templates (organization_id, category, name, description, template_path) VALUES
('00000000-0000-0000-0000-000000000001', 'CONTRACT', 'Standard Certification Contract', 'Default contract template', '/templates/contract_standard.docx'),
('00000000-0000-0000-0000-000000000001', 'CERTIFICATE', 'ISO Certificate Template', 'Standard certificate template', '/templates/certificate_iso.docx'),
('00000000-0000-0000-0000-000000000001', 'AUDIT_REPORT', 'Audit Report Template', 'Standard audit report', '/templates/audit_report.docx'),
('00000000-0000-0000-0000-000000000001', 'AUDIT_PLAN', 'Audit Plan Template', 'Standard audit plan', '/templates/audit_plan.docx'),
('00000000-0000-0000-0000-000000000001', 'NC_REPORT', 'NC Report Template', 'Non-conformity report', '/templates/nc_report.docx'),
('00000000-0000-0000-0000-000000000001', 'LETTER', 'General Letter Template', 'General correspondence', '/templates/letter_general.docx'),
('00000000-0000-0000-0000-000000000001', 'APPLICATION_FORM', 'Application Form Template', 'Client application form', '/templates/application_form.docx');
