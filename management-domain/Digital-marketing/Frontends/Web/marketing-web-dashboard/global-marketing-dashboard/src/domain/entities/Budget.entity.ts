// Domain Entity: Budget
// Represents budget allocation and tracking

export enum BudgetStatus {
  DRAFT = 'draft',
  APPROVED = 'approved',
  ACTIVE = 'active',
  EXHAUSTED = 'exhausted',
  CANCELLED = 'cancelled'
}

export enum BudgetPeriod {
  DAILY = 'daily',
  WEEKLY = 'weekly',
  MONTHLY = 'monthly',
  QUARTERLY = 'quarterly',
  ANNUALLY = 'annually',
  CUSTOM = 'custom'
}

export interface BudgetAllocation {
  id: string;
  channel: string;
  amount: number;
  percentage: number;
  spent: number;
  remaining: number;
}

export interface CountryBudget {
  countryCode: string;
  countryName: string;
  allocated: number;
  spent: number;
  remaining: number;
  utilization: number;
}

export interface Budget {
  id: string;
  name: string;
  fiscalYear: number;
  period: BudgetPeriod;
  status: BudgetStatus;
  currency: string;
  totalAmount: number;
  spentAmount: number;
  remainingAmount: number;
  utilizationRate: number;
  allocations: BudgetAllocation[];
  countries: CountryBudget[];
  approvals: {
    currentLevel: number;
    requiredLevels: number;
    approvers: Array<{
      id: string;
      name: string;
      role: string;
      status: 'pending' | 'approved' | 'rejected';
      timestamp?: Date;
    }>;
  };
  dates: {
    start: Date;
    end: Date;
    createdAt: Date;
    updatedAt: Date;
  };
  createdBy: {
    id: string;
    name: string;
    email: string;
  };
  notes: string;
}

export interface BudgetCreateInput {
  name: string;
  fiscalYear: number;
  period: BudgetPeriod;
  currency: string;
  totalAmount: number;
  startDate: Date;
  endDate: Date;
  allocations: Array<{
    channel: string;
    amount: number;
  }>;
  countries: Array<{
    countryCode: string;
    amount: number;
  }>;
}

export interface BudgetUpdateInput {
  name?: string;
  status?: BudgetStatus;
  totalAmount?: number;
  allocations?: BudgetAllocation[];
  notes?: string;
}
