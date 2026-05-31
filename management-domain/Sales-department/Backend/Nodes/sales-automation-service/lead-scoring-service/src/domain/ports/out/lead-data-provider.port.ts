export interface LeadDataProviderPort {
  getLeadData(leadId: string, tenantId: string): Promise<LeadData | null>;
  getLeadDataBatch(leadIds: string[], tenantId: string): Promise<Map<string, LeadData>>;
  getLeadActivity(leadId: string, tenantId: string, days?: number): Promise<LeadActivity[]>;
  getLeadDemographics(leadId: string, tenantId: string): Promise<DemographicData | null>;
  getLeadFirmographics(leadId: string, tenantId: string): Promise<FirmographicData | null>;
}

export interface LeadData {
  id: string;
  tenantId: string;
  email: string;
  firstName?: string;
  lastName?: string;
  phone?: string;
  company?: string;
  jobTitle?: string;
  industry?: string;
  companySize?: string;
  country?: string;
  state?: string;
  city?: string;
  source?: string;
  createdAt: Date;
  lastActivityAt?: Date;
  customFields?: Record<string, any>;
}

export interface DemographicData {
  age?: number;
  gender?: string;
  education?: string;
  income?: string;
  interests?: string[];
  jobLevel?: string;
  department?: string;
}

export interface FirmographicData {
  company: string;
  industry?: string;
  employeeCount?: number;
  revenue?: number;
  website?: string;
  linkedinUrl?: string;
  foundedYear?: number;
  companyType?: string;
  locations?: number;
}

export interface LeadActivity {
  id: string;
  leadId: string;
  tenantId: string;
  type: string;
  description: string;
  timestamp: Date;
  metadata?: Record<string, any>;
}
