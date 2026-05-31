/**
 * Application Layer: Metrics Store
 * State management using Zustand
 */

import { create } from 'zustand';
import {
  CountryMetrics,
  TeamPerformance,
  TicketTrend,
  TicketDistribution,
  SlaBreach,
  DashboardFilters,
} from '../../domain/entities';

interface MetricsState {
  // Data
  countryMetrics: CountryMetrics[];
  selectedCountry: string | null;
  teamPerformance: TeamPerformance[];
  ticketTrends: TicketTrend[];
  ticketDistribution: TicketDistribution[];
  slaBreaches: SlaBreach[];
  insights: string[];

  // UI State
  isLoading: boolean;
  error: string | null;
  filters: DashboardFilters;

  // Actions
  setCountryMetrics: (metrics: CountryMetrics[]) => void;
  setSelectedCountry: (country: string | null) => void;
  setTeamPerformance: (teams: TeamPerformance[]) => void;
  setTicketTrends: (trends: TicketTrend[]) => void;
  setTicketDistribution: (distribution: TicketDistribution[]) => void;
  setSlaBreaches: (breaches: SlaBreach[]) => void;
  setInsights: (insights: string[]) => void;
  setLoading: (loading: boolean) => void;
  setError: (error: string | null) => void;
  setFilters: (filters: Partial<DashboardFilters>) => void;
  resetFilters: () => void;
}

const defaultFilters: DashboardFilters = {
  dateRange: {
    from: new Date(Date.now() - 30 * 24 * 60 * 60 * 1000), // 30 days ago
    to: new Date(),
  },
};

export const useMetricsStore = create<MetricsState>((set) => ({
  // Initial state
  countryMetrics: [],
  selectedCountry: null,
  teamPerformance: [],
  ticketTrends: [],
  ticketDistribution: [],
  slaBreaches: [],
  insights: [],
  isLoading: false,
  error: null,
  filters: defaultFilters,

  // Actions
  setCountryMetrics: (metrics) => set({ countryMetrics: metrics }),
  setSelectedCountry: (country) => set({ selectedCountry: country }),
  setTeamPerformance: (teams) => set({ teamPerformance: teams }),
  setTicketTrends: (trends) => set({ ticketTrends: trends }),
  setTicketDistribution: (distribution) => set({ ticketDistribution: distribution }),
  setSlaBreaches: (breaches) => set({ slaBreaches: breaches }),
  setInsights: (insights) => set({ insights }),
  setLoading: (isLoading) => set({ isLoading }),
  setError: (error) => set({ error }),
  setFilters: (newFilters) =>
    set((state) => ({
      filters: { ...state.filters, ...newFilters },
    })),
  resetFilters: () => set({ filters: defaultFilters }),
}));
