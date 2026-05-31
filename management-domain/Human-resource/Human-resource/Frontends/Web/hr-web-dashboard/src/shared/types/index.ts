// User Roles for HR Dashboard
export type HRRole =
  | 'CHRO' // Chief Human Resources Officer
  | 'HR_DIRECTOR' // Head of HR Department
  | 'HR_MANAGER' // HR Manager
  | 'RECRUITER' // Talent Acquisition
  | 'PAYROLL_SPECIALIST' // Compensation & Benefits
  | 'HR_GENERALIST' // General HR
  | 'TRAINING_MANAGER' // Learning & Development
  | 'COMPLIANCE_OFFICER' // HR Compliance
  | 'EMPLOYEE' // Regular employee view

export type EmploymentStatus =
  | 'ACTIVE'
  | 'ON_LEAVE'
  | 'PENDING_ONBOARDING'
  | 'PENDING_OFFBOARDING'
  | 'TERMINATED'
  | 'RESIGNED'
  | 'RETIRED'

export type EmploymentType =
  | 'FULL_TIME'
  | 'PART_TIME'
  | 'CONTRACT'
  | 'INTERN'
  | 'CONSULTANT'

export type LeaveType =
  | 'ANNUAL'
  | 'SICK'
  | 'MATERNITY'
  | 'PATERNITY'
  | 'UNPAID'
  | 'COMPASSIONATE'
  | 'STUDY'
  | 'SABBATICAL'

export type LeaveStatus = 'PENDING' | 'APPROVED' | 'REJECTED' | 'CANCELLED'

export type PerformanceRating = 1 | 2 | 3 | 4 | 5

export type CountryCode =
  | 'US'
  | 'NG'
  | 'KE'
  | 'ZA'
  | 'GB'
  | 'IE'
  | 'CA'
  | 'IN'
  | 'PH'
  | 'DE'
  | 'FR'
  | 'OTHER'

export interface User {
  id: string
  email: string
  firstName: string
  lastName: string
  displayName: string
  role: HRRole
  department: string
  avatar?: string
  permissions: string[]
  country?: CountryCode
  employeeId?: string
  isActive: boolean
  createdAt: string
  updatedAt: string
}

export interface Employee {
  id: string
  employeeId: string
  firstName: string
  lastName: string
  email: string
  phone?: string
  department: string
  position: string
  manager?: string
  country: CountryCode
  location: string
  status: EmploymentStatus
  employmentType: EmploymentType
  joinDate: string
  terminationDate?: string
  salary: number
  currency: string
  avatar: string
  skills: string[]
  education: Education[]
  experience: WorkExperience[]
}

export interface Education {
  id: string
  institution: string
  degree: string
  field: string
  startDate: string
  endDate: string
}

export interface WorkExperience {
  id: string
  company: string
  position: string
  startDate: string
  endDate?: string
}

// Leave Management
export interface LeaveRequest {
  id: string
  employeeId: string
  employeeName: string
  type: LeaveType
  startDate: string
  endDate: string
  days: number
  reason: string
  status: LeaveStatus
  approvedBy?: string
  approvedAt?: string
  createdAt: string
}

export interface LeaveBalance {
  employeeId: string
  annual: { used: number; total: number }
  sick: { used: number; total: number }
  maternity: { used: number; total: number }
  paternity: { used: number; total: number }
  unpaid: { used: number; total: number }
}

// Payroll
export interface PayrollEntry {
  id: string
  employeeId: string
  employeeName: string
  department: string
  position: string
  grossSalary: number
  basicSalary: number
  allowances: number
  overtimePay: number
  bonuses: number
  deductions: number
  tax: number
  netPay: number
  currency: string
}

export interface Payslip {
  id: string
  employeeId: string
  period: string // e.g., "2024-01"
  payDate: string
  earnings: {
    basic: number
    housing: number
    transport: number
    medical: number
    other: number
    total: number
  }
  deductions: {
    tax: number
    pension: number
    insurance: number
    other: number
    total: number
  }
  netPay: number
  currency: string
}

// Performance
export interface PerformanceReview {
  id: string
  employeeId: string
  employeeName: string
  reviewerId: string
  reviewerName: string
  period: string
  overallRating: PerformanceRating
  goals: Goal[]
  strengths: string[]
  improvements: string[]
  comments: string
  status: 'DRAFT' | 'SUBMITTED' | 'REVIEWED'
  createdAt: string
}

export interface Goal {
  id: string
  title: string
  description: string
  category: string
  targetDate: string
  progress: number
  status: 'NOT_STARTED' | 'IN_PROGRESS' | 'COMPLETED' | 'OVERDUE'
}

// Training
export interface TrainingProgram {
  id: string
  title: string
  description: string
  category: string
  instructor: string
  duration: number
  startDate: string
  endDate: string
  capacity: number
  enrolled: number
  status: 'DRAFT' | 'PUBLISHED' | 'IN_PROGRESS' | 'COMPLETED'
}

export interface Enrollment {
  id: string
  programId: string
  employeeId: string
  employeeName: string
  progress: number
  status: 'ENROLLED' | 'IN_PROGRESS' | 'COMPLETED' | 'DROPPED'
  enrolledAt: string
  completedAt?: string
  certificateUrl?: string
}

// Recruitment
export interface JobRequisition {
  id: string
  title: string
  department: string
  location: string
  country: CountryCode
  type: EmploymentType
  description: string
  requirements: string[]
  salaryMin: number
  salaryMax: number
  status: 'DRAFT' | 'PENDING_APPROVAL' | 'APPROVED' | 'FILLED' | 'CANCELLED'
  createdAt: string
  createdBy: string
}

export interface Candidate {
  id: string
  name: string
  email: string
  phone: string
  position: string
  department: string
  stage: 'APPLIED' | 'SCREENING' | 'INTERVIEW' | 'OFFER' | 'HIRED' | 'REJECTED'
  appliedDate: string
  resumeUrl?: string
  notes: string
}

// Benefits
export interface BenefitEnrollment {
  id: string
  employeeId: string
  benefitType: 'HEALTH' | 'DENTAL' | 'VISION' | 'LIFE' | 'RETIREMENT' | 'OTHER'
  planName: string
  coverage: 'EMPLOYEE_ONLY' | 'EMPLOYEE_SPOUSE' | 'FAMILY'
  premium: number
  startDate: string
  endDate?: string
  dependents: Dependent[]
}

export interface Dependent {
  id: string
  name: string
  relationship: 'SPOUSE' | 'CHILD' | 'PARENT' | 'OTHER'
  dateOfBirth: string
}

// Time & Attendance
export interface TimeEntry {
  id: string
  employeeId: string
  employeeName: string
  date: string
  clockIn: string
  clockOut?: string
  hours: number
  overtimeHours: number
  status: 'PRESENT' | 'ABSENT' | 'LATE' | 'HALF_DAY'
}

export interface AttendanceSummary {
  employeeId: string
  period: string
  presentDays: number
  absentDays: number
  lateDays: number
  overtimeHours: number
}

// Compliance
export interface ComplianceItem {
  id: string
  type: 'POLICY' | 'TRAINING' | 'DOCUMENT' | 'CERTIFICATION'
  title: string
  description: string
  dueDate: string
  status: 'COMPLIANT' | 'PENDING' | 'OVERDUE' | 'EXEMPT'
  assignee?: string
}

// Dashboard Metrics
export interface HRMetrics {
  headcount: {
    total: number
    permanent: number
    contract: number
    interns: number
    change: number
    changePercent: number
  }
  growth: {
    newHires: number
    departures: number
    netChange: number
    growthRate: number
  }
  turnover: {
    rate: number
    voluntary: number
    involuntary: number
    trend: 'up' | 'down' | 'stable'
  }
  diversity: {
    gender: { male: number; female: number; other: number }
    ageGroups: { under30: number; thirtyTo40: number; fortyTo50: number; over50: number }
    regions: Record<string, number>
  }
  openPositions: {
    total: number
    urgent: number
    timeToFill: number
  }
  engagement: {
    score: number
    participation: number
    trend: 'up' | 'down' | 'stable'
  }
}

// Approvals
export interface ApprovalRequest {
  id: string
  type: 'LEAVE' | 'SALARY_CHANGE' | 'PROMOTION' | 'EXPENSE' | 'TRAINING'
  title: string
  description: string
  requestedBy: string
  requestedAt: string
  priority: 'low' | 'medium' | 'high' | 'urgent'
  status: 'PENDING' | 'APPROVED' | 'REJECTED'
  amount?: number
}

// Country Selector
export interface Country {
  code: CountryCode
  name: string
  flag: string
  currency: string
  headcount: number
}
