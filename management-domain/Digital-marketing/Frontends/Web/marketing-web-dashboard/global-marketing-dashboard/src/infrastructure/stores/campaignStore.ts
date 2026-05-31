// Campaign Store - Zustand
// Manages campaign state

import { create } from 'zustand';
import { devtools, persist } from 'zustand/middleware';
import { Campaign, CampaignStatus } from '../../domain/entities/Campaign.entity';
import mockCampaigns from '../../shared/mock-data/campaigns.mock';

interface CampaignFilters {
  status?: CampaignStatus;
  search?: string;
  country?: string;
  channel?: string;
  dateRange?: {
    start: Date;
    end: Date;
  };
}

interface CampaignState {
  campaigns: Campaign[];
  selectedCampaign: Campaign | null;
  filters: CampaignFilters;
  isLoading: boolean;
  error: string | null;

  // Actions
  setCampaigns: (campaigns: Campaign[]) => void;
  setSelectedCampaign: (campaign: Campaign | null) => void;
  setFilters: (filters: Partial<CampaignFilters>) => void;
  resetFilters: () => void;
  addCampaign: (campaign: Campaign) => void;
  updateCampaign: (id: string, updates: Partial<Campaign>) => void;
  deleteCampaign: (id: string) => void;
  setLoading: (isLoading: boolean) => void;
  setError: (error: string | null) => void;

  // Computed
  getFilteredCampaigns: () => Campaign[];
  getCampaignById: (id: string) => Campaign | undefined;
}

export const useCampaignStore = create<CampaignState>()(
  devtools(
    persist(
      (set, get) => ({
        campaigns: mockCampaigns,
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

        resetFilters: () =>
          set({
            filters: {},
          }),

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
              state.selectedCampaign?.id === id
                ? null
                : state.selectedCampaign,
          })),

        setLoading: (isLoading) => set({ isLoading }),

        setError: (error) => set({ error }),

        getFilteredCampaigns: () => {
          const { campaigns, filters } = get();
          return campaigns.filter((campaign) => {
            if (filters.status && campaign.status !== filters.status) {
              return false;
            }
            if (
              filters.search &&
              !campaign.name.toLowerCase().includes(filters.search.toLowerCase()) &&
              !campaign.description.toLowerCase().includes(filters.search.toLowerCase())
            ) {
              return false;
            }
            if (
              filters.country &&
              !campaign.countries.some((c) => c.countryCode === filters.country)
            ) {
              return false;
            }
            if (filters.channel && !campaign.channels.includes(filters.channel as any)) {
              return false;
            }
            if (filters.dateRange) {
              const campaignStart = new Date(campaign.dates.start);
              const campaignEnd = new Date(campaign.dates.end);
              const filterStart = new Date(filters.dateRange.start);
              const filterEnd = new Date(filters.dateRange.end);
              if (campaignEnd < filterStart || campaignStart > filterEnd) {
                return false;
              }
            }
            return true;
          });
        },

        getCampaignById: (id) => {
          return get().campaigns.find((c) => c.id === id);
        },
      }),
      {
        name: 'campaign-storage',
        partialize: (state) => ({
          filters: state.filters,
        }),
      }
    ),
    { name: 'CampaignStore' }
  )
);

// Selectors
export const selectAllCampaigns = (state: CampaignState) => state.campaigns;
export const selectActiveCampaigns = (state: CampaignState) =>
  state.campaigns.filter((c) => c.status === CampaignStatus.ACTIVE);
export const selectCampaignById = (id: string) => (state: CampaignState) =>
  state.campaigns.find((c) => c.id === id);
