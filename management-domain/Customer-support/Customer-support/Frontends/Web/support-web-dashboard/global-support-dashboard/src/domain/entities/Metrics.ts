/**
 * Domain Entity: Metrics
 * Represents key performance indicators for country-level support
 */

export interface MetricValue {
  value: number;
  change: number;
  trend: 'up' | 'down' | 'stable';
}

export interface CountryMetrics {
  country: string;
  countryCode: string;
  totalTickets: MetricValue;
  openTickets: MetricValue;
  resolvedTickets: MetricValue;
  avgResolutionTime: MetricValue;
  customerSatisfaction: MetricValue;
  slaCompliance: MetricValue;
  firstContactResolution: MetricValue;
  agentUtilization: MetricValue;
}

export interface RegionalPerformance {
  region: string;
  metrics: CountryMetrics[];
  overallScore: number;
  ranking: number;
}

export interface TeamPerformance {
  teamId: string;
  teamName: string;
  teamLead: string;
  memberCount: number;
  totalTicketsHandled: number;
  avgResolutionTime: number;
  customerSatisfaction: number;
  slaComplianceRate: number;
  topPerformers: AgentPerformance[];
}

export interface AgentPerformance {
  agentId: string;
  agentName: string;
  avatar?: string;
  ticketsResolved: number;
  avgResolutionTime: number;
  customerRating: number;
  slaCompliance: number;
  activeTickets: number;
}

export interface TicketTrend {
  date: string;
  new: number;
  resolved: number;
  open: number;
  escalated: number;
}

export interface TicketDistribution {
  category: string;
  count: number;
  percentage: number;
  color: string;
}

export interface SlaBreach {
  ticketId: string;
  ticketSubject: string;
  severity: 'low' | 'medium' | 'high' | 'critical';
  overdueBy: number;
  assignedTo: string;
  customer: string;
}

export interface DashboardFilters {
  country?: string;
  dateRange: {
    from: Date;
    to: Date;
  };
  team?: string;
  category?: string;
}
