# RoyalSys Technical Runbook

## Architecture Overview

RoyalSys is a certification body operations platform for RoyalCert International Registrars.

| Layer | Technology |
|-------|-----------|
| Backend | Java 21, Spring Boot 3.2, Spring Data JPA, Spring Security |
| Database | PostgreSQL 16, Flyway migrations |
| Frontend | React 18, TypeScript, Vite |
| Deployment | Docker, Docker Compose, Nginx |

## Project Structure

```
royalsys-backend/           # Spring Boot application
  src/main/java/com/royalcert/royalsys/
    config/                 # Security, CORS, Jackson, Auditing
    controller/             # REST API endpoints
    domain/entity/          # JPA entities (18 entity classes)
    domain/enums/           # WorkflowState, SchemeType, RoleType, etc.
    domain/repository/      # Spring Data JPA repositories
    dto/                    # Request/Response DTOs
    service/                # Business logic services
  src/main/resources/
    db/migration/           # Flyway SQL migrations
    application.yml         # Configuration

royalsys-frontend/          # React SPA
  src/
    api/                    # Axios API client
    pages/                  # Dashboard, Clients, Calculator, Projects, Invoices
    types/                  # TypeScript interfaces

docker/                     # Docker Compose, Dockerfiles, Nginx config
```

## Database Schema (18 tables)

| Table | Purpose |
|-------|---------|
| organizations | Multi-entity CB organizations |
| users | System users with roles |
| clients | Certification clients |
| client_sites | Client site locations |
| schemes | ISO certification schemes |
| client_scopes | Client-scheme scope assignments |
| manday_rule_sets | Versioned manday rule tables |
| manday_rules | Employee-range to manday mappings |
| nace_risk_mappings | NACE code to risk level |
| manday_calculations | Stored calculation results with inputs/outputs |
| applications | Client applications with review workflow |
| quotes | Quotations with line items |
| contracts | Client contracts |
| projects | Certification projects (cycles) |
| auditor_profiles | Auditor competence records |
| auditor_authorizations | Scheme/EA/NACE authorizations |
| authorization_snapshots | Point-in-time authorization records |
| audits | Audit events with team |
| audit_team_members | Audit team composition |
| technical_reviews | Post-audit technical reviews |
| certification_decisions | Certification decision records |
| certificates | Issued certificates lifecycle |
| findings | Non-conformities and observations |
| invoices | Invoices with line items |
| invoice_line_items | Dynamic invoice lines |
| payments | Payment records |
| credit_notes | Credit note records |
| ar_reminders | AR reminder tracking |
| workflow_events | All state transition events |
| document_templates | DMS template registry |
| documents | Document metadata and tracking |
| notifications | User notifications |
| audit_trail | Field-level change tracking |

## Key Services

### MandayCalculatorService
Rules-driven calculator that:
- Loads current rule set for the scheme
- Matches employee count to manday range
- Applies stage ratios (30/70, 1/3, 2/3)
- Applies increase/decrease adjustments
- Calculates site mandays separately
- Stores calculation with rule version, inputs, and approvals

### WorkflowService
Records every state transition as a WorkflowEvent with:
- Entity type and ID
- From/to states
- Timestamp, performer, notes
- Supports lead-time calculation between any two states

### LeadTimeAnalyticsService
Computes all 11 required lead-time metrics:
- Quote, contract, audit, planning, report, review, decision, certificate, AR, NC closure

### SegregationOfDutiesService
Enforces accreditation rules:
- Technical reviewer must not be on the audit team
- Decision maker must not be auditor or reviewer
- Validated before assignment

### InvoiceService
Full AR lifecycle:
- Create, send, record payments (partial/full)
- Automatic status transitions via workflow events
- Outstanding balance tracking

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | /api/manday-calculator/calculate | Run manday calculation |
| GET | /api/dashboard/stats/{orgId} | Dashboard statistics |
| GET | /api/dashboard/lead-times/{projectId} | Lead-time analytics |
| GET | /api/clients?orgId= | List clients (paginated) |
| GET | /api/clients/{id} | Get client |
| POST | /api/clients | Create client |
| PUT | /api/clients/{id} | Update client |
| GET | /api/clients/search?orgId=&q= | Search clients |
| GET | /api/projects?orgId= | List projects |
| GET | /api/projects/{id} | Get project |
| POST | /api/projects | Create project |
| GET | /api/invoices?orgId= | List invoices |
| POST | /api/invoices | Create invoice |
| POST | /api/invoices/{id}/send | Send invoice |
| POST | /api/invoices/{id}/payments | Record payment |
| GET | /api/schemes | List active schemes |
| GET | /api/workflow/history/{type}/{id} | Workflow event history |
| GET | /swagger-ui.html | OpenAPI/Swagger UI |

## Quick Start

### Docker (recommended)
```bash
cd docker
docker-compose up --build
```
- Frontend: http://localhost:5173
- Backend API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- PostgreSQL: localhost:5432

### Local Development

**Backend:**
```bash
# Start PostgreSQL
docker run -d --name royalsys-pg -e POSTGRES_DB=royalsys \
  -e POSTGRES_USER=royalsys -e POSTGRES_PASSWORD=royalsys \
  -p 5432:5432 postgres:16-alpine

cd royalsys-backend
./mvnw spring-boot:run
```

**Frontend:**
```bash
cd royalsys-frontend
npm install
npm run dev
```

## Default Credentials

| User | Password | Role |
|------|----------|------|
| admin | admin123 | Administrator |
| jsmith | admin123 | Lead Auditor |
| mwilson | admin123 | Technical Reviewer |
| rbrown | admin123 | Decision Maker |
| sjones | admin123 | Planner |
| finance | admin123 | Finance |

## Running Tests
```bash
cd royalsys-backend
./mvnw test
```

## Seed Data
Flyway migration V2 loads:
- 1 organization (RoyalCert)
- 6 users across all roles
- 5 ISO schemes
- Manday rule sets for ISO 9001, 14001, 45001
- NACE risk mappings
- 1 sample client with 2 sites
- 7 document templates
