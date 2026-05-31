// TanStack Query Hooks
// Custom hooks using @tanstack/react-query for data fetching

import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import {
  dashboardRepository,
  teamsRepository,
  dealsRepository,
  leadsRepository,
  partnersRepository,
  customersRepository,
} from '../api';
import { useDashboardStore, useTeamsStore, useDealsStore, useLeadsStore, usePartnersStore, useCustomersStore } from '../stores';

// ============================================================
// Dashboard Hooks
// ============================================================

export function useDashboardSummary(period?: string) {
  return useQuery({
    queryKey: ['dashboard', 'summary', period],
    queryFn: () => dashboardRepository.getDashboardSummary(period),
    staleTime: 5 * 60 * 1000, // 5 minutes
  });
}

export function useTeamRankings(period?: string) {
  return useQuery({
    queryKey: ['dashboard', 'team-rankings', period],
    queryFn: () => dashboardRepository.getTeamRankings(period),
    staleTime: 5 * 60 * 1000,
  });
}

export function useTopPerformers(period?: string, limit?: number) {
  return useQuery({
    queryKey: ['dashboard', 'top-performers', period, limit],
    queryFn: () => dashboardRepository.getTopPerformers(period, limit),
    staleTime: 5 * 60 * 1000,
  });
}

export function useMajorDeals(minValue?: number) {
  return useQuery({
    queryKey: ['dashboard', 'major-deals', minValue],
    queryFn: () => dashboardRepository.getMajorDeals(minValue),
    staleTime: 5 * 60 * 1000,
  });
}

export function useAlerts() {
  return useQuery({
    queryKey: ['dashboard', 'alerts'],
    queryFn: () => dashboardRepository.getAlerts(),
    staleTime: 2 * 60 * 1000, // 2 minutes
    refetchInterval: 2 * 60 * 1000, // Refetch every 2 minutes
  });
}

// ============================================================
// Teams Hooks
// ============================================================

export function useTeams(filters?: any) {
  return useQuery({
    queryKey: ['teams', filters],
    queryFn: () => teamsRepository.getTeams(filters),
    staleTime: 5 * 60 * 1000,
  });
}

export function useTeam(teamId: string) {
  return useQuery({
    queryKey: ['teams', teamId],
    queryFn: () => teamsRepository.getTeamById(teamId),
    enabled: !!teamId,
    staleTime: 5 * 60 * 1000,
  });
}

export function useTeamPerformance(teamId: string, period?: string) {
  return useQuery({
    queryKey: ['teams', teamId, 'performance', period],
    queryFn: () => teamsRepository.getTeamPerformance(teamId, period),
    enabled: !!teamId,
    staleTime: 5 * 60 * 1000,
  });
}

export function useCreateTeam() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (team: Parameters<typeof teamsRepository.createTeam>[0]) =>
      teamsRepository.createTeam(team),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['teams'] });
    },
  });
}

export function useUpdateTeam() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ teamId, updates }: { teamId: string; updates: Parameters<typeof teamsRepository.updateTeam>[1] }) =>
      teamsRepository.updateTeam(teamId, updates),
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: ['teams'] });
      queryClient.invalidateQueries({ queryKey: ['teams', variables.teamId] });
    },
  });
}

export function useDeleteTeam() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (teamId: string) => teamsRepository.deleteTeam(teamId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['teams'] });
    },
  });
}

// ============================================================
// Deals Hooks
// ============================================================

export function useDeals(filters?: any) {
  return useQuery({
    queryKey: ['deals', filters],
    queryFn: () => dealsRepository.getDeals(filters),
    staleTime: 5 * 60 * 1000,
  });
}

export function useDeal(dealId: string) {
  return useQuery({
    queryKey: ['deals', dealId],
    queryFn: () => dealsRepository.getDealById(dealId),
    enabled: !!dealId,
    staleTime: 5 * 60 * 1000,
  });
}

export function usePipeline() {
  return useQuery({
    queryKey: ['deals', 'pipeline'],
    queryFn: () => dealsRepository.getPipeline(),
    staleTime: 5 * 60 * 1000,
  });
}

export function useCreateDeal() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (deal: Parameters<typeof dealsRepository.createDeal>[0]) =>
      dealsRepository.createDeal(deal),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['deals'] });
      queryClient.invalidateQueries({ queryKey: ['deals', 'pipeline'] });
    },
  });
}

export function useUpdateDeal() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ dealId, updates }: { dealId: string; updates: Parameters<typeof dealsRepository.updateDeal>[1] }) =>
      dealsRepository.updateDeal(dealId, updates),
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: ['deals'] });
      queryClient.invalidateQueries({ queryKey: ['deals', variables.dealId] });
    },
  });
}

export function useUpdateDealStage() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ dealId, stage }: { dealId: string; stage: string }) =>
      dealsRepository.updateDealStage(dealId, stage),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['deals'] });
      queryClient.invalidateQueries({ queryKey: ['deals', 'pipeline'] });
    },
  });
}

// ============================================================
// Leads Hooks
// ============================================================

export function useLeads(filters?: any) {
  return useQuery({
    queryKey: ['leads', filters],
    queryFn: () => leadsRepository.getLeads(filters),
    staleTime: 5 * 60 * 1000,
  });
}

export function useLead(leadId: string) {
  return useQuery({
    queryKey: ['leads', leadId],
    queryFn: () => leadsRepository.getLeadById(leadId),
    enabled: !!leadId,
    staleTime: 5 * 60 * 1000,
  });
}

export function useCreateLead() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (lead: Parameters<typeof leadsRepository.createLead>[0]) =>
      leadsRepository.createLead(lead),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['leads'] });
    },
  });
}

export function useUpdateLead() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ leadId, updates }: { leadId: string; updates: Parameters<typeof leadsRepository.updateLead>[1] }) =>
      leadsRepository.updateLead(leadId, updates),
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: ['leads'] });
      queryClient.invalidateQueries({ queryKey: ['leads', variables.leadId] });
    },
  });
}

export function useAssignLead() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ leadId, assignedTo, note }: { leadId: string; assignedTo: string; note?: string }) =>
      leadsRepository.assignLead(leadId, assignedTo, note),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['leads'] });
    },
  });
}

export function useConvertLeadToDeal() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (leadId: string) => leadsRepository.convertLeadToDeal(leadId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['leads'] });
      queryClient.invalidateQueries({ queryKey: ['deals'] });
    },
  });
}

// ============================================================
// Partners Hooks
// ============================================================

export function usePartners(filters?: any) {
  return useQuery({
    queryKey: ['partners', filters],
    queryFn: () => partnersRepository.getPartners(filters),
    staleTime: 5 * 60 * 1000,
  });
}

export function usePartner(partnerId: string) {
  return useQuery({
    queryKey: ['partners', partnerId],
    queryFn: () => partnersRepository.getPartnerById(partnerId),
    enabled: !!partnerId,
    staleTime: 5 * 60 * 1000,
  });
}

export function usePartnerApplications(filters?: any) {
  return useQuery({
    queryKey: ['partners', 'applications', filters],
    queryFn: () => partnersRepository.getApplications(filters),
    staleTime: 5 * 60 * 1000,
  });
}

export function usePartnerCommissions(period?: string) {
  return useQuery({
    queryKey: ['partners', 'commissions', period],
    queryFn: () => partnersRepository.getCommissions(period),
    staleTime: 5 * 60 * 1000,
  });
}

export function useApproveApplication() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ applicationId, partnerId }: { applicationId: string; partnerId?: string }) =>
      partnersRepository.approveApplication(applicationId, partnerId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['partners', 'applications'] });
      queryClient.invalidateQueries({ queryKey: ['partners'] });
    },
  });
}

export function useRejectApplication() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ applicationId, reason }: { applicationId: string; reason: string }) =>
      partnersRepository.rejectApplication(applicationId, reason),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['partners', 'applications'] });
    },
  });
}

// ============================================================
// Customers Hooks
// ============================================================

export function useCustomers(filters?: any) {
  return useQuery({
    queryKey: ['customers', filters],
    queryFn: () => customersRepository.getCustomers(filters),
    staleTime: 5 * 60 * 1000,
  });
}

export function useCustomer(customerId: string) {
  return useQuery({
    queryKey: ['customers', customerId],
    queryFn: () => customersRepository.getCustomerById(customerId),
    enabled: !!customerId,
    staleTime: 5 * 60 * 1000,
  });
}

export function useCustomerAnalytics() {
  return useQuery({
    queryKey: ['customers', 'analytics'],
    queryFn: () => customersRepository.getAnalytics(),
    staleTime: 15 * 60 * 1000, // 15 minutes
  });
}

export function useCreateCustomer() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (customer: Parameters<typeof customersRepository.createCustomer>[0]) =>
      customersRepository.createCustomer(customer),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['customers'] });
    },
  });
}

export function useUpdateCustomer() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ customerId, updates }: { customerId: string; updates: Parameters<typeof customersRepository.updateCustomer>[1] }) =>
      customersRepository.updateCustomer(customerId, updates),
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: ['customers'] });
      queryClient.invalidateQueries({ queryKey: ['customers', variables.customerId] });
    },
  });
}
