// Dashboard Store - Zustand
// Manages country dashboard state

import { create } from 'zustand';
import type {
  CountryDashboardSummary,
  TeamRanking,
  TopPerformer,
  Deal,
  CountryAlert,
  ForecastSummary,
} from '@domain/types';
import { dashboardRepository } from '../api';

interface DashboardState {
  // Data
  summary: CountryDashboardSummary | null;
  teamRankings: TeamRanking[];
  topPerformers: TopPerformer[];
  majorDeals: Deal[];
  alerts: CountryAlert[];
  forecastSummary: ForecastSummary | null;

  // UI State
  isLoading: boolean;
  error: string | null;

  // Actions
  loadDashboardSummary: (period?: string) => Promise<void>;
  loadTeamRankings: (period?: string) => Promise<void>;
  loadTopPerformers: (period?: string, limit?: number) => Promise<void>;
  loadMajorDeals: (minValue?: number) => Promise<void>;
  loadAlerts: () => Promise<void>;
  markAlertAsRead: (alertId: string) => Promise<void>;
  clearError: () => void;
}

export const useDashboardStore = create<DashboardState>()((set, get) => ({
  // Initial state
  summary: null,
  teamRankings: [],
  topPerformers: [],
  majorDeals: [],
  alerts: [],
  forecastSummary: null,
  isLoading: false,
  error: null,

  // Load dashboard summary
  loadDashboardSummary: async (period?: string) => {
    set({ isLoading: true, error: null });

    try {
      const summary = await dashboardRepository.getDashboardSummary(period);

      set({
        summary,
        teamRankings: summary.teamRankings,
        topPerformers: summary.topPerformers,
        majorDeals: summary.majorDeals,
        alerts: summary.alerts,
        forecastSummary: summary.forecastSummary,
        isLoading: false,
      });
    } catch (error: any) {
      set({
        error: error.message || 'Failed to load dashboard data',
        isLoading: false,
      });
    }
  },

  // Load team rankings
  loadTeamRankings: async (period?: string) => {
    try {
      const teamRankings = await dashboardRepository.getTeamRankings(period);
      set({ teamRankings });
    } catch (error: any) {
      set({ error: error.message || 'Failed to load team rankings' });
    }
  },

  // Load top performers
  loadTopPerformers: async (period?: string, limit?: number) => {
    try {
      const topPerformers = await dashboardRepository.getTopPerformers(period, limit);
      set({ topPerformers });
    } catch (error: any) {
      set({ error: error.message || 'Failed to load top performers' });
    }
  },

  // Load major deals
  loadMajorDeals: async (minValue?: number) => {
    try {
      const majorDeals = await dashboardRepository.getMajorDeals(minValue);
      set({ majorDeals });
    } catch (error: any) {
      set({ error: error.message || 'Failed to load major deals' });
    }
  },

  // Load alerts
  loadAlerts: async () => {
    try {
      const alerts = await dashboardRepository.getAlerts();
      set({ alerts });
    } catch (error: any) {
      set({ error: error.message || 'Failed to load alerts' });
    }
  },

  // Mark alert as read
  markAlertAsRead: async (alertId: string) => {
    try {
      await dashboardRepository.markAlertAsRead(alertId);
      set((state) => ({
        alerts: state.alerts.map((alert) =>
          alert.id === alertId ? { ...alert, isRead: true } : alert
        ),
      }));
    } catch (error: any) {
      set({ error: error.message || 'Failed to mark alert as read' });
    }
  },

  // Clear error
  clearError: () => set({ error: null }),
}));
