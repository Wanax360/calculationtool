export interface DashboardStats {
  activeProjects: number
  pendingApplications: number
  activeCertificates: number
  outstandingAR: number
  totalRevenue: number
}

export interface Scheme {
  id: string
  code: string
  name: string
  description: string
  accreditationBody: string
}

export interface Client {
  id: string
  companyName: string
  legalName: string
  industrySector: string
  naceCode: string
  eaCode: string
  employeeCount: number
  city: string
  country: string
  contactName: string
  contactEmail: string
  contactPhone: string
  active: boolean
}

export interface MandayCalculationRequest {
  clientId: string
  schemeId: string
  employeeCount: number
  naceCode?: string
  riskLevel?: string
  adjustmentType?: string
  adjustmentPercent?: number
  adjustmentReasons?: string
  siteEmployeeCount?: number
}

export interface MandayCalculationResponse {
  id: string
  baseMandays: number
  stage1Mandays: number
  stage2Mandays: number
  surveillanceMandays: number
  recertificationMandays: number
  adjustmentType?: string
  adjustmentPercent?: number
  adjustedStage1?: number
  adjustedStage2?: number
  adjustedSurveillance?: number
  adjustedRecertification?: number
  siteStage1?: number
  siteStage2?: number
  siteSurveillance?: number
  siteRecertification?: number
  siteAdjustedStage1?: number
  siteAdjustedStage2?: number
  siteAdjustedSurveillance?: number
  siteAdjustedRecertification?: number
  calculatedAt: string
}

export interface Project {
  id: string
  projectNumber: string
  status: string
  cycleType: string
  plannedStartDate: string
  plannedEndDate: string
}

export interface Invoice {
  id: string
  invoiceNumber: string
  currency: string
  totalAmount: number
  amountPaid: number
  balanceDue: number
  status: string
  dueDate: string
  sentAt: string
}
