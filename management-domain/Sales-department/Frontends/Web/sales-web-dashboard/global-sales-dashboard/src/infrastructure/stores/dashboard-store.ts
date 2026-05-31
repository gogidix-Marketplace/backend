// Dashboard Store - Zustand
// Manages global dashboard state

import { create } from 'zustand';
import type { GlobalDashboardSummary, CountrySummary, GlobalTopPerformer, MajorDeal, GlobalAlert } from '@domain/types';
import { globalDashboardRepository } from '../api';

interface DashboardState {
  // Data
  summary: GlobalDashboardSummary | null;
  countries: CountrySummary[];
  topPerformers: GlobalTopPerformer[];
  majorDeals: MajorDeal[];
  alerts: GlobalAlert[];

  // UI State
  selectedPeriod: string;
  isLoading: boolean;
  error: string | null;

  // Actions
  loadGlobalSummary: (period?: string) => Promise<void>;
  loadCountrySummaries: (period?: string) => Promise<void>;
  loadTopPerformers: (period?: string, limit?: number) => Promise<void>;
  loadMajorDeals: (minValue?: number) => Promise<void>;
  loadAlerts: () => Promise<void>;
  setSelectedPeriod: (period: string) => void;
  clearError: () => void;
}

export const useDashboardStore = create<DashboardState>()((set, get) => ({
  // Initial state
  summary: null,
  countries: [],
  topPerformers: [],
  majorDeals: [],
  alerts: [],
  selectedPeriod: 'month',
  isLoading: false,
  error: null,

  // Load global summary
  loadGlobalSummary: async (period?: string) => {
    set({ isLoading: true, error: null });

    try {
      const selectedPeriod = period || get().selectedPeriod;
      const summary = await globalDashboardRepository.getGlobalSummary(selectedPeriod);

      set({
        summary,
        countries: summary.countriesSummary,
        topPerformers: summary.topPerformers,
        majorDeals: summary.majorDeals,
        alerts: summary.alerts,
        isLoading: false,
      });
    } catch (error: any) {
      set({
        error: error.message || 'Failed to load dashboard data',
        isLoading: false,
      });
    }
  },

  // Load country summaries
  loadCountrySummaries: async (period?: string) => {
    try {
      const countries = await globalDashboardRepository.getCountrySummaries(period);
      set({ countries });
    } catch (error: any) {
      set({ error: error.message || 'Failed to load country data' });
    }
  },

  // Load top performers
  loadTopPerformers: async (period?: string, limit?: number) => {
    try {
      const topPerformers = await globalDashboardRepository.getTopPerformers(period, limit);
      set({ topPerformers });
    } catch (error: any) {
      set({ error: error.message || 'Failed to load top performers' });
    }
  },

  // Load major deals
  loadMajorDeals: async (minValue?: number) => {
    try {
      const majorDeals = await globalDashboardRepository.getMajorDeals(minValue);
      set({ majorDeals });
    } catch (error: any) {
      set({ error: error.message || 'Failed to load major deals' });
    }
  },

  // Load alerts
  loadAlerts: async () => {
    try {
      const alerts = await globalDashboardRepository.getAlerts();
      set({ alerts });
    } catch (error: any) {
      set({ error: error.message || 'Failed to load alerts' });
    }
  },

  // Set selected period
  setSelectedPeriod: (period: string) => {
    set({ selectedPeriod: period });
  },

  // Clear error
  clearError: () => set({ error: null }),
}));
