// Local Campaign Store - Zustand
// Manages local campaign state for country-level dashboard

import { create } from 'zustand';
import { devtools } from 'zustand/middleware';
import { LocalCampaign, LocalCampaignStatus } from '../../domain/types';

const mockLocalCampaigns: LocalCampaign[] = [
  {
    id: 'local-camp-001',
    name: 'Spring Festival Promotion 2026',
    description: 'Local spring festival promotional campaign for the region',
    status: 'active',
    type: 'local_promotion',
    channels: ['social', 'email', 'display'],
    budget: {
      total: 25000,
      spent: 18750,
      remaining: 6250,
      currency: 'USD',
      approvedAt: new Date('2026-01-15'),
    },
    dates: {
      start: new Date('2026-02-01'),
      end: new Date('2026-02-28'),
      createdAt: new Date('2026-01-10'),
      updatedAt: new Date('2026-02-18'),
    },
    metrics: {
      impressions: 1250000,
      clicks: 87500,
      conversions: 3500,
      cost: 18750,
      revenue: 52500,
      leads: 875,
      ctr: 7.0,
      cpa: 5.36,
      roas: 2.8,
    },
    owner: {
      id: 'user-local-001',
      name: 'John Smith',
      email: 'john.smith@gogidix.com',
    },
    country: {
      code: 'US',
      name: 'United States',
    },
    targetAudience: 'Local customers aged 25-45',
    objectives: ['Increase foot traffic', 'Launch spring products', 'Generate 500 leads'],
    tags: ['spring', 'festival', 'promotion', 'local'],
    globalCampaignId: 'camp-001',
  },
  {
    id: 'local-camp-002',
    name: 'Community Event Sponsorship',
    description: 'Sponsorship of local community event',
    status: 'approved',
    type: 'event_marketing',
    channels: ['events', 'social', 'email'],
    budget: {
      total: 15000,
      spent: 0,
      remaining: 15000,
      currency: 'USD',
    },
    dates: {
      start: new Date('2026-03-15'),
      end: new Date('2026-03-15'),
      createdAt: new Date('2026-02-01'),
      updatedAt: new Date('2026-02-18'),
    },
    metrics: {
      impressions: 0,
      clicks: 0,
      conversions: 0,
      cost: 0,
      revenue: 0,
      leads: 0,
      ctr: 0,
      cpa: 0,
      roas: 0,
    },
    owner: {
      id: 'user-local-001',
      name: 'John Smith',
      email: 'john.smith@gogidix.com',
    },
    country: {
      code: 'US',
      name: 'United States',
    },
    targetAudience: 'Local community members',
    objectives: ['Brand visibility', 'Community engagement', 'Generate 200 leads'],
    tags: ['event', 'community', 'sponsorship'],
  },
  },
];

interface LocalCampaignState {
  campaigns: LocalCampaign[];
  selectedCampaign: LocalCampaign | null;
  filters: {
    status?: LocalCampaignStatus[];
    search?: string;
    dateRange?: { start: Date; end: Date };
  };
  isLoading: boolean;
  error: string | null;

  setCampaigns: (campaigns: LocalCampaign[]) => void;
  setSelectedCampaign: (campaign: LocalCampaign | null) => void;
  setFilters: (filters: Partial<LocalCampaignState['filters']>) => void;
  addCampaign: (campaign: LocalCampaign) => void;
  updateCampaign: (id: string, updates: Partial<LocalCampaign>) => void;
  deleteCampaign: (id: string) => void;
  setLoading: (loading: boolean) => void;
  setError: (error: string | null) => void;
  getFilteredCampaigns: () => LocalCampaign[];
}

export const useLocalCampaignStore = create<LocalCampaignState>()(
  devtools(
    (set, get) => ({
      campaigns: mockLocalCampaigns,
      selectedCampaign: null,
      filters: {},
      isLoading: false,
      error: null,

      setCampaigns: (campaigns) => set({ campaigns }),

      setSelectedCampaign: (campaign) => set({ selectedCampaign: campaign }),

      setFilters: (newFilters) =>
        set((state) => ({
          filters: { ...state.filters, ...newFilters },
        })),

      addCampaign: (campaign) =>
        set((state) => ({
          campaigns: [...state.campaigns, campaign],
        })),

      updateCampaign: (id, updates) =>
        set((state) => ({
          campaigns: state.campaigns.map((c) =>
            c.id === id ? { ...c, ...updates } : c
          ),
          selectedCampaign:
            state.selectedCampaign?.id === id
              ? { ...state.selectedCampaign, ...updates }
              : state.selectedCampaign,
        })),

      deleteCampaign: (id) =>
        set((state) => ({
          campaigns: state.campaigns.filter((c) => c.id !== id),
          selectedCampaign:
            state.selectedCampaign?.id === id ? null : state.selectedCampaign,
        })),

      setLoading: (loading) => set({ isLoading: loading }),

      setError: (error) => set({ error }),

      getFilteredCampaigns: () => {
        const { campaigns, filters } = get();
        return campaigns.filter((campaign) => {
          if (filters.status && filters.status.length > 0 && !filters.status.includes(campaign.status)) {
            return false;
          }
          if (
            filters.search &&
            !campaign.name.toLowerCase().includes(filters.search.toLowerCase())
          ) {
            return false;
          }
          return true;
        });
      },
    }),
    { name: 'LocalCampaignStore' }
  )
);
