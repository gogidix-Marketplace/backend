// React Query Hooks
// Data fetching hooks using TanStack Query

import { useQuery, useMutation, useQueryClient, type UseQueryOptions } from '@tanstack/react-query';
import { mockCampaigns, mockBudgets, mockLeads, mockSocialPosts, mockContent } from '@shared/mock-data';
import type {
  Campaign,
  Budget,
  Lead,
  SocialPost,
  Content,
  CampaignFilters,
  LeadFilters,
} from '@domain/types';

// Query Keys
export const queryKeys = {
  // Dashboard
  globalDashboard: (period: string) => ['dashboard', 'global', period] as const,
  countrySummaries: () => ['countries', 'summaries'] as const,

  // Campaigns
  campaigns: (filters?: CampaignFilters) => ['campaigns', filters] as const,
  campaign: (id: string) => ['campaign', id] as const,

  // Budgets
  budgets: () => ['budgets'] as const,
  budget: (id: string) => ['budget', id] as const,

  // Leads
  leads: (filters?: LeadFilters) => ['leads', filters] as const,
  lead: (id: string) => ['lead', id] as const,

  // Social Posts
  socialPosts: () => ['socialPosts'] as const,
  socialPost: (id: string) => ['socialPost', id] as const,

  // Content
  content: () => ['content'] as const,
  contentItem: (id: string) => ['content', id] as const,

  // Analytics
  channelMetrics: (period: string) => ['analytics', 'channel', period] as const,
} as const;

// Dashboard Hooks
export const useGlobalDashboard = (
  period?: string,
  options?: Omit<UseQueryOptions<any>, 'queryKey' | 'queryFn'>
) => {
  return useQuery({
    queryKey: queryKeys.globalDashboard(period || 'month'),
    queryFn: async () => {
      // Simulate API call
      await new Promise(resolve => setTimeout(resolve, 500));
      const { mockGlobalDashboardSummary } = await import('@shared/mock-data');
      return mockGlobalDashboardSummary;
    },
    staleTime: 5 * 60 * 1000,
    ...options,
  });
};

// Campaign Hooks
export const useCampaigns = (
  filters?: CampaignFilters,
  options?: Omit<UseQueryOptions<Campaign[]>, 'queryKey' | 'queryFn'>
) => {
  return useQuery({
    queryKey: queryKeys.campaigns(filters),
    queryFn: async () => {
      await new Promise(resolve => setTimeout(resolve, 300));
      let filtered = [...mockCampaigns];

      if (filters?.status) {
        filtered = filtered.filter(c => filters.status!.includes(c.status));
      }
      if (filters?.type) {
        filtered = filtered.filter(c => filters.type!.includes(c.type));
      }
      if (filters?.channel) {
        filtered = filtered.filter(c => c.channels.some(ch => filters.channel!.includes(ch)));
      }

      return filtered;
    },
    ...options,
  });
};

export const useCampaign = (
  id: string,
  options?: Omit<UseQueryOptions<Campaign>, 'queryKey' | 'queryFn'>
) => {
  return useQuery({
    queryKey: queryKeys.campaign(id),
    queryFn: async () => {
      await new Promise(resolve => setTimeout(resolve, 200));
      const campaign = mockCampaigns.find(c => c.id === id);
      if (!campaign) throw new Error('Campaign not found');
      return campaign;
    },
    enabled: !!id,
    ...options,
  });
};

export const useCreateCampaign = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (data: Partial<Campaign>) => {
      await new Promise(resolve => setTimeout(resolve, 500));
      return data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.campaigns() });
    },
  });
};

export const useUpdateCampaign = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async ({ id, data }: { id: string; data: Partial<Campaign> }) => {
      await new Promise(resolve => setTimeout(resolve, 500));
      return { id, data };
    },
    onSuccess: (variables) => {
      queryClient.invalidateQueries({ queryKey: queryKeys.campaign(variables.id) });
      queryClient.invalidateQueries({ queryKey: queryKeys.campaigns() });
    },
  });
};

export const useDeleteCampaign = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (id: string) => {
      await new Promise(resolve => setTimeout(resolve, 300));
      return id;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.campaigns() });
    },
  });
};

// Budget Hooks
export const useBudgets = (
  options?: Omit<UseQueryOptions<Budget[]>, 'queryKey' | 'queryFn'>
) => {
  return useQuery({
    queryKey: queryKeys.budgets(),
    queryFn: async () => {
      await new Promise(resolve => setTimeout(resolve, 300));
      return mockBudgets;
    },
    ...options,
  });
};

// Lead Hooks
export const useLeads = (
  filters?: LeadFilters,
  options?: Omit<UseQueryOptions<Lead[]>, 'queryKey' | 'queryFn'>
) => {
  return useQuery({
    queryKey: queryKeys.leads(filters),
    queryFn: async () => {
      await new Promise(resolve => setTimeout(resolve, 300));
      let filtered = [...mockLeads];

      if (filters?.status) {
        filtered = filtered.filter(l => filters.status!.includes(l.status));
      }
      if (filters?.quality) {
        filtered = filtered.filter(l => filters.quality!.includes(l.quality));
      }

      return filtered;
    },
    ...options,
  });
};

// Social Posts Hooks
export const useSocialPosts = (
  options?: Omit<UseQueryOptions<SocialPost[]>, 'queryKey' | 'queryFn'>
) => {
  return useQuery({
    queryKey: queryKeys.socialPosts(),
    queryFn: async () => {
      await new Promise(resolve => setTimeout(resolve, 300));
      return mockSocialPosts;
    },
    ...options,
  });
};

// Content Hooks
export const useContent = (
  options?: Omit<UseQueryOptions<Content[]>, 'queryKey' | 'queryFn'>
) => {
  return useQuery({
    queryKey: queryKeys.content(),
    queryFn: async () => {
      await new Promise(resolve => setTimeout(resolve, 300));
      return mockContent;
    },
    ...options,
  });
};
