// API Repositories
// Mock repositories for development

import type {
  CountrySalesUser,
  LoginRequest,
  AuthResponse,
  CountryDashboardSummary,
  TeamRanking,
  TopPerformer,
  Deal,
  CountryAlert,
  ForecastSummary,
  SalesTeam,
  TeamPerformance,
  TeamFilters,
  DealFilters,
  CountryPipeline,
  Lead,
  LeadFilters,
  LeadAssignment,
  CountryPartner,
  PartnerApplication,
  PartnerFilters,
  PartnerCommission,
  Customer,
  CustomerFilters,
  CustomerAnalytics,
  PaginationInfo,
} from '@domain/types';
import {
  mockDashboardSummary,
  mockSalesTeams,
  mockDeals,
  mockLeads,
  mockCustomers,
  mockPartners,
  mockPartnerApplications,
  mockPipelineData,
  mockUsers,
} from '@shared/mock-data';

// Simulate API delay
const delay = (ms: number = 500) => new Promise(resolve => setTimeout(resolve, ms));

// Helper to create pagination response
function createResponse<T>(data: T[], page: number = 1, pageSize: number = 20) {
  const totalItems = data.length;
  const totalPages = Math.ceil(totalItems / pageSize);
  const start = (page - 1) * pageSize;
  const end = start + pageSize;
  const paginatedData = data.slice(start, end);

  return {
    data: paginatedData,
    pagination: {
      page,
      pageSize,
      totalItems,
      totalPages,
      hasNext: page < totalPages,
      hasPrevious: page > 1,
    } as PaginationInfo,
  };
}

// ============================================================
// Auth Repository
// ============================================================

export const authRepository = {
  async login(credentials: LoginRequest): Promise<AuthResponse> {
    await delay();

    // Mock login - find user by email
    const user = mockUsers.find(u => u.email === credentials.email);

    if (!user || credentials.password !== 'password') {
      throw new Error('Invalid credentials');
    }

    return {
      user,
      accessToken: 'mock-access-token-' + Date.now(),
      refreshToken: 'mock-refresh-token-' + Date.now(),
      expiresIn: 3600,
    };
  },

  async logout(): Promise<void> {
    await delay(200);
    // Mock logout
  },

  async refreshToken(refreshToken: string): Promise<AuthResponse> {
    await delay(200);

    return {
      user: mockUsers[0],
      accessToken: 'mock-access-token-' + Date.now(),
      refreshToken: refreshToken,
      expiresIn: 3600,
    };
  },

  async getCurrentUser(): Promise<CountrySalesUser> {
    await delay(200);
    return mockUsers[0];
  },
};

// ============================================================
// Dashboard Repository
// ============================================================

export const dashboardRepository = {
  async getDashboardSummary(period?: string): Promise<CountryDashboardSummary> {
    await delay();
    return mockDashboardSummary;
  },

  async getTeamRankings(period?: string): Promise<TeamRanking[]> {
    await delay();
    return mockDashboardSummary.teamRankings;
  },

  async getTopPerformers(period?: string, limit?: number): Promise<TopPerformer[]> {
    await delay();
    return mockDashboardSummary.topPerformers.slice(0, limit);
  },

  async getMajorDeals(minValue?: number): Promise<Deal[]> {
    await delay();
    let deals = mockDeals.filter(d => d.stage !== 'WON' && d.stage !== 'LOST');
    if (minValue) {
      deals = deals.filter(d => d.value >= minValue);
    }
    return deals.slice(0, 5);
  },

  async getAlerts(): Promise<CountryAlert[]> {
    await delay();
    return mockDashboardSummary.alerts;
  },

  async markAlertAsRead(alertId: string): Promise<void> {
    await delay(200);
    // Mock update
  },
};

// ============================================================
// Teams Repository
// ============================================================

export const teamsRepository = {
  async getTeams(filters?: TeamFilters) {
    await delay();
    let teams = [...mockSalesTeams];

    // Apply filters
    if (filters?.search) {
      const search = filters.search.toLowerCase();
      teams = teams.filter(t =>
        t.name.toLowerCase().includes(search) ||
        t.teamLeadName.toLowerCase().includes(search)
      );
    }

    if (filters?.status) {
      teams = teams.filter(t => filters.status!.includes(t.status));
    }

    if (filters?.region) {
      teams = teams.filter(t => t.region && filters.region!.includes(t.region.id));
    }

    return createResponse(teams, filters?.page, filters?.pageSize);
  },

  async getTeamById(teamId: string): Promise<SalesTeam> {
    await delay();
    const team = mockSalesTeams.find(t => t.id === teamId);
    if (!team) throw new Error('Team not found');
    return team;
  },

  async getTeamPerformance(teamId: string, period?: string): Promise<TeamPerformance> {
    await delay();
    const team = mockSalesTeams.find(t => t.id === teamId);
    if (!team) throw new Error('Team not found');

    return {
      teamId: team.id,
      teamName: team.name,
      period: {
        start: new Date('2025-02-01'),
        end: new Date('2025-02-28'),
        type: 'monthly',
        label: 'February 2025',
      },
      metrics: {
        revenue: team.metrics.revenueThisMonth,
        quota: team.quota.monthly,
        attainment: team.metrics.quotaAttainment,
        dealsClosed: team.metrics.dealsClosed,
        avgDealSize: team.metrics.revenueThisMonth / team.metrics.dealsClosed,
        pipelineValue: team.metrics.pipelineValue,
        activities: {
          calls: Math.floor(Math.random() * 500) + 200,
          emails: Math.floor(Math.random() * 800) + 400,
          meetings: Math.floor(Math.random() * 80) + 30,
          demos: Math.floor(Math.random() * 40) + 10,
        },
      },
      members: team.members.map(m => ({
        userId: m.userId,
        name: m.name,
        avatar: m.avatar,
        role: m.role,
        metrics: {
          revenue: m.revenueGenerated,
          quota: m.individualQuota,
          attainment: m.attainment,
          dealsClosed: Math.floor(m.revenueGenerated / 80000),
          activities: {
            calls: Math.floor(Math.random() * 200) + 50,
            emails: Math.floor(Math.random() * 300) + 100,
            meetings: Math.floor(Math.random() * 30) + 10,
          },
        },
      })),
      trend: team.metrics.quotaAttainment >= 100 ? 'up' : team.metrics.quotaAttainment >= 90 ? 'neutral' : 'down',
    };
  },

  async createTeam(team: Partial<SalesTeam>): Promise<SalesTeam> {
    await delay(300);
    const newTeam: SalesTeam = {
      id: 'team-' + Date.now(),
      name: team.name || 'New Team',
      code: team.code || 'NEW-' + Date.now(),
      description: team.description,
      country: team.country || mockSalesTeams[0].country,
      region: team.region,
      teamLeadId: team.teamLeadId || mockUsers[0].id,
      teamLeadName: team.teamLeadName || mockUsers[0].firstName + ' ' + mockUsers[0].lastName,
      members: team.members || [],
      status: team.status || 'active',
      quota: team.quota || { monthly: 1000000, quarterly: 3000000, annual: 12000000 },
      metrics: {
        revenueThisMonth: 0,
        revenueThisQuarter: 0,
        quotaAttainment: 0,
        dealsClosed: 0,
        pipelineValue: 0,
        winRate: 0,
      },
      createdAt: new Date(),
      updatedAt: new Date(),
    };
    return newTeam;
  },

  async updateTeam(teamId: string, updates: Partial<SalesTeam>): Promise<SalesTeam> {
    await delay(300);
    const team = mockSalesTeams.find(t => t.id === teamId);
    if (!team) throw new Error('Team not found');
    return { ...team, ...updates, updatedAt: new Date() };
  },

  async deleteTeam(teamId: string): Promise<void> {
    await delay(300);
    // Mock delete
  },
};

// ============================================================
// Deals Repository
// ============================================================

export const dealsRepository = {
  async getDeals(filters?: DealFilters) {
    await delay();
    let deals = [...mockDeals];

    // Apply filters
    if (filters?.search) {
      const search = filters.search.toLowerCase();
      deals = deals.filter(d =>
        d.name.toLowerCase().includes(search) ||
        d.accountName.toLowerCase().includes(search)
      );
    }

    if (filters?.stage) {
      deals = deals.filter(d => filters.stage!.includes(d.stage));
    }

    if (filters?.owner) {
      deals = deals.filter(d => filters.owner!.includes(d.owner.id));
    }

    if (filters?.valueMin) {
      deals = deals.filter(d => d.value >= filters.valueMin!);
    }

    if (filters?.valueMax) {
      deals = deals.filter(d => d.value <= filters.valueMax!);
    }

    // Sort
    if (filters?.sortBy) {
      deals.sort((a, b) => {
        const aVal = a[filters.sortBy! as keyof Deal];
        const bVal = b[filters.sortBy! as keyof Deal];
        if (typeof aVal === 'number' && typeof bVal === 'number') {
          return filters.sortOrder === 'desc' ? bVal - aVal : aVal - bVal;
        }
        return 0;
      });
    }

    return createResponse(deals, filters?.page, filters?.pageSize);
  },

  async getDealById(dealId: string): Promise<Deal> {
    await delay();
    const deal = mockDeals.find(d => d.id === dealId);
    if (!deal) throw new Error('Deal not found');
    return deal;
  },

  async getPipeline(): Promise<CountryPipeline> {
    await delay();
    return mockPipelineData;
  },

  async createDeal(deal: Partial<Deal>): Promise<Deal> {
    await delay(300);
    const newDeal: Deal = {
      id: 'deal-' + Date.now(),
      dealNumber: 'NG-2025-' + String(mockDeals.length + 1).padStart(4, '0'),
      name: deal.name || 'New Deal',
      accountId: deal.accountId || '',
      accountName: deal.accountName || '',
      contactId: deal.contactId,
      contactName: deal.contactName,
      value: deal.value || 0,
      currency: deal.currency || 'USD',
      stage: deal.stage || 'PROSPECTING',
      probability: deal.probability || 10,
      expectedCloseDate: deal.expectedCloseDate || new Date(),
      owner: deal.owner || { id: mockUsers[0].id, name: mockUsers[0].firstName + ' ' + mockUsers[0].lastName },
      priority: deal.priority || 'medium',
      source: deal.source || 'OTHER',
      products: deal.products || [],
      competitors: deal.competitors,
      nextStep: deal.nextStep,
      lastActivityAt: new Date(),
      createdAt: new Date(),
      updatedAt: new Date(),
    };
    return newDeal;
  },

  async updateDeal(dealId: string, updates: Partial<Deal>): Promise<Deal> {
    await delay(300);
    const deal = mockDeals.find(d => d.id === dealId);
    if (!deal) throw new Error('Deal not found');
    return { ...deal, ...updates, updatedAt: new Date() };
  },

  async updateDealStage(dealId: string, stage: string): Promise<Deal> {
    await delay(300);
    const deal = mockDeals.find(d => d.id === dealId);
    if (!deal) throw new Error('Deal not found');

    const probabilities: Record<string, number> = {
      PROSPECTING: 10,
      QUALIFICATION: 25,
      PROPOSAL: 50,
      NEGOTIATION: 75,
      CLOSING: 90,
      WON: 100,
      LOST: 0,
    };

    return {
      ...deal,
      stage: stage as any,
      probability: probabilities[stage] || 10,
      updatedAt: new Date(),
    };
  },

  async deleteDeal(dealId: string): Promise<void> {
    await delay(300);
    // Mock delete
  },
};

// ============================================================
// Leads Repository
// ============================================================

export const leadsRepository = {
  async getLeads(filters?: LeadFilters) {
    await delay();
    let leads = [...mockLeads];

    // Apply filters
    if (filters?.search) {
      const search = filters.search.toLowerCase();
      leads = leads.filter(l =>
        l.firstName.toLowerCase().includes(search) ||
        l.lastName.toLowerCase().includes(search) ||
        l.email?.toLowerCase().includes(search) ||
        l.company?.toLowerCase().includes(search)
      );
    }

    if (filters?.status) {
      leads = leads.filter(l => filters.status!.includes(l.status));
    }

    if (filters?.rating) {
      leads = leads.filter(l => filters.rating!.includes(l.rating));
    }

    if (filters?.assignedTo) {
      leads = leads.filter(l => l.assignedTo && filters.assignedTo!.includes(l.assignedTo));
    }

    return createResponse(leads, filters?.page, filters?.pageSize);
  },

  async getLeadById(leadId: string): Promise<Lead> {
    await delay();
    const lead = mockLeads.find(l => l.id === leadId);
    if (!lead) throw new Error('Lead not found');
    return lead;
  },

  async createLead(lead: Partial<Lead>): Promise<Lead> {
    await delay(300);
    const newLead: Lead = {
      id: 'lead-' + Date.now(),
      leadNumber: 'NG-L-2025-' + String(mockLeads.length + 1).padStart(4, '0'),
      firstName: lead.firstName || '',
      lastName: lead.lastName || '',
      email: lead.email,
      phone: lead.phone,
      company: lead.company,
      title: lead.title,
      industry: lead.industry,
      status: lead.status || 'NEW',
      source: lead.source || 'OTHER',
      rating: lead.rating || 'cold',
      estimatedValue: lead.estimatedValue || 0,
      currency: lead.currency || 'USD',
      assignedTo: lead.assignedTo,
      assignedToName: lead.assignedToName,
      assignedTeam: lead.assignedTeam,
      territory: lead.territory,
      tags: lead.tags || [],
      notes: lead.notes,
      activities: [],
      createdAt: new Date(),
      updatedAt: new Date(),
    };
    return newLead;
  },

  async updateLead(leadId: string, updates: Partial<Lead>): Promise<Lead> {
    await delay(300);
    const lead = mockLeads.find(l => l.id === leadId);
    if (!lead) throw new Error('Lead not found');
    return { ...lead, ...updates, updatedAt: new Date() };
  },

  async deleteLead(leadId: string): Promise<void> {
    await delay(300);
    // Mock delete
  },

  async assignLead(leadId: string, assignedTo: string, note?: string): Promise<void> {
    await delay(300);
    // Mock assign
  },

  async convertLeadToDeal(leadId: string): Promise<void> {
    await delay(300);
    // Mock convert
  },
};

// ============================================================
// Partners Repository
// ============================================================

export const partnersRepository = {
  async getPartners(filters?: PartnerFilters) {
    await delay();
    let partners = [...mockPartners];

    // Apply filters
    if (filters?.search) {
      const search = filters.search.toLowerCase();
      partners = partners.filter(p =>
        p.user.firstName.toLowerCase().includes(search) ||
        p.user.lastName.toLowerCase().includes(search) ||
        p.user.email.toLowerCase().includes(search)
      );
    }

    if (filters?.partnerType) {
      partners = partners.filter(p => filters.partnerType!.includes(p.partnerType));
    }

    if (filters?.status) {
      partners = partners.filter(p => filters.status!.includes(p.status));
    }

    if (filters?.tier) {
      partners = partners.filter(p => filters.tier!.includes(p.tier));
    }

    return createResponse(partners, filters?.page, filters?.pageSize);
  },

  async getPartnerById(partnerId: string): Promise<CountryPartner> {
    await delay();
    const partner = mockPartners.find(p => p.id === partnerId);
    if (!partner) throw new Error('Partner not found');
    return partner;
  },

  async getApplications(filters?: PartnerFilters) {
    await delay();
    let applications = [...mockPartnerApplications];

    if (filters?.search) {
      const search = filters.search.toLowerCase();
      applications = applications.filter(a =>
        a.applicant.firstName.toLowerCase().includes(search) ||
        a.applicant.lastName.toLowerCase().includes(search) ||
        a.applicant.email.toLowerCase().includes(search)
      );
    }

    return createResponse(applications, filters?.page, filters?.pageSize);
  },

  async getApplicationById(applicationId: string): Promise<PartnerApplication> {
    await delay();
    const application = mockPartnerApplications.find(a => a.id === applicationId);
    if (!application) throw new Error('Application not found');
    return application;
  },

  async approveApplication(applicationId: string, partnerId?: string): Promise<void> {
    await delay(300);
    // Mock approve
  },

  async rejectApplication(applicationId: string, reason: string): Promise<void> {
    await delay(300);
    // Mock reject
  },

  async getCommissions(period?: string): Promise<PartnerCommission[]> {
    await delay();
    // Mock commissions
    return mockPartners.map(p => ({
      id: 'comm-' + p.id,
      partnerId: p.id,
      partner: {
        name: p.user.firstName + ' ' + p.user.lastName,
        partnerType: p.partnerType,
        tier: p.tier,
      },
      referralBonus: {
        partnersReferred: p.partnersRecruited,
        totalBonus: p.referralBonusEarned,
        paidBonus: p.referralBonusEarned * 0.8,
        pendingBonus: p.referralBonusEarned * 0.2,
      },
      salesCommission: {
        customersAcquired: p.customersAcquired,
        totalRevenue: p.revenueGenerated,
        totalCommission: p.commissionEarned,
        paidCommission: p.commissionEarned * 0.7,
        pendingCommission: p.commissionEarned * 0.3,
      },
      paymentSummary: {
        totalEarned: p.commissionEarned + p.referralBonusEarned,
        totalPaid: (p.commissionEarned + p.referralBonusEarned) * 0.75,
        totalPending: (p.commissionEarned + p.referralBonusEarned) * 0.25,
        nextPayoutDate: new Date('2025-03-01'),
        nextPayoutAmount: p.commissionEarned * 0.3,
      },
      period: {
        type: 'MONTHLY',
        start: new Date('2025-02-01'),
        end: new Date('2025-02-28'),
        label: 'February 2025',
      },
      generatedAt: new Date(),
    }));
  },
};

// ============================================================
// Customers Repository
// ============================================================

export const customersRepository = {
  async getCustomers(filters?: CustomerFilters) {
    await delay();
    let customers = [...mockCustomers];

    // Apply filters
    if (filters?.search) {
      const search = filters.search.toLowerCase();
      customers = customers.filter(c =>
        c.name.toLowerCase().includes(search) ||
        c.industry.toLowerCase().includes(search)
      );
    }

    if (filters?.status) {
      customers = customers.filter(c => filters.status!.includes(c.status));
    }

    if (filters?.tier) {
      customers = customers.filter(c => filters.tier!.includes(c.tier));
    }

    return createResponse(customers, filters?.page, filters?.pageSize);
  },

  async getCustomerById(customerId: string): Promise<Customer> {
    await delay();
    const customer = mockCustomers.find(c => c.id === customerId);
    if (!customer) throw new Error('Customer not found');
    return customer;
  },

  async getAnalytics(): Promise<CustomerAnalytics> {
    await delay();
    return {
      totalCustomers: mockCustomers.length,
      activeCustomers: mockCustomers.filter(c => c.status === 'active').length,
      atRiskCustomers: mockCustomers.filter(c => c.status === 'at_risk').length,
      churnedCustomers: mockCustomers.filter(c => c.status === 'churned').length,
      totalRevenue: mockCustomers.reduce((sum, c) => sum + c.metrics.totalRevenue, 0),
      avgRevenuePerCustomer: mockCustomers.reduce((sum, c) => sum + c.metrics.totalRevenue, 0) / mockCustomers.length,
      byIndustry: {
        Manufacturing: 12500000,
        'E-commerce': 4800000,
        Fashion: 350000,
        Construction: 120000,
      },
      byTier: {
        enterprise: 17300000,
        mid_market: 350000,
        small_business: 120000,
      },
      retentionRate: 92,
      acquisitionRate: 15,
      churnRate: 3,
    };
  },

  async createCustomer(customer: Partial<Customer>): Promise<Customer> {
    await delay(300);
    const newCustomer: Customer = {
      id: 'cust-' + Date.now(),
      customerNumber: 'NG-C-2025-' + String(mockCustomers.length + 1).padStart(4, '0'),
      name: customer.name || 'New Customer',
      industry: customer.industry || 'Other',
      tier: customer.tier || 'small_business',
      status: customer.status || 'prospect',
      address: customer.address || {
        street: '',
        city: '',
        state: '',
        postalCode: '',
        country: 'Nigeria',
      },
      contacts: customer.contacts || [],
      accountOwner: customer.accountOwner || mockUsers[0],
      teamOwner: customer.teamOwner,
      metrics: {
        totalRevenue: 0,
        dealsCount: 0,
        avgDealSize: 0,
        ltv: 0,
        firstPurchaseDate: new Date(),
        lastPurchaseDate: new Date(),
      },
      territory: customer.territory,
      tags: customer.tags || [],
      notes: customer.notes,
      createdAt: new Date(),
      updatedAt: new Date(),
    };
    return newCustomer;
  },

  async updateCustomer(customerId: string, updates: Partial<Customer>): Promise<Customer> {
    await delay(300);
    const customer = mockCustomers.find(c => c.id === customerId);
    if (!customer) throw new Error('Customer not found');
    return { ...customer, ...updates, updatedAt: new Date() };
  },

  async deleteCustomer(customerId: string): Promise<void> {
    await delay(300);
    // Mock delete
  },
};
