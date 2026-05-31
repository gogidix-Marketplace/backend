// Dashboard Store - Zustand
// Manages global dashboard state

import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import type { GlobalDashboardSummary, CountrySummary } from '@domain/types';
import { mockGlobalDashboardSummary } from '@shared/mock-data';

interface DashboardState {
  // Data
  summary: GlobalDashboardSummary | null;
  countries: CountrySummary[];
  alerts: any[];

  // UI State
  selectedPeriod: string;
  isLoading: boolean;
  error: string | null;

  // Actions
  loadGlobalSummary: (period?: string) => Promise<void>;
  setSelectedPeriod: (period: string) => void;
  clearError: () => void;
}

export const useDashboardStore = create<DashboardState>()(
  persist(
    (set, get) => ({
      // Initial state
      summary: null,
      countries: [],
      alerts: [],
      selectedPeriod: 'month',
      isLoading: false,
      error: null,

      // Load global summary
      loadGlobalSummary: async (period?: string) => {
        set({ isLoading: true, error: null });

        try {
          // Simulate API call
          await new Promise(resolve => setTimeout(resolve, 500));

          const summary = mockGlobalDashboardSummary;

          set({
            summary,
            countries: summary.countriesSummary,
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

      // Set selected period
      setSelectedPeriod: (period: string) => {
        set({ selectedPeriod: period });
      },

      // Clear error
      clearError: () => set({ error: null }),
    }),
    {
      name: 'dashboard-storage',
      partialize: (state) => ({ selectedPeriod: state.selectedPeriod }),
    }
  )
);
