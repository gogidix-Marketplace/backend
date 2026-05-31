// Analytics Store - Zustand
// Manages analytics state

import { create } from 'zustand';
import { devtools } from 'zustand/middleware';
import { Alert } from '../../domain/entities/Analytics.entity';
import {
  mockChannelMetrics,
  mockCountryPerformance,
  mockCampaignAttribution,
  mockConversionFunnel,
  mockAlerts,
  mockTimeSeriesData,
} from '../../shared/mock-data/analytics.mock';

interface DateRange {
  start: Date;
  end: Date;
}

interface AnalyticsState {
  dateRange: DateRange;
  selectedCountries: string[];
  selectedChannels: string[];
  alerts: Alert[];
  unreadAlertCount: number;
  isLoading: boolean;
  error: string | null;

  // Computed data
  channelMetrics: typeof mockChannelMetrics;
  countryPerformance: typeof mockCountryPerformance;
  campaignAttribution: typeof mockCampaignAttribution;
  conversionFunnel: typeof mockConversionFunnel;
  timeSeriesData: typeof mockTimeSeriesData;

  // Actions
  setDateRange: (dateRange: DateRange) => void;
  setSelectedCountries: (countries: string[]) => void;
  setSelectedChannels: (channels: string[]) => void;
  markAlertAsRead: (alertId: string) => void;
  markAllAlertsAsRead: () => void;
  dismissAlert: (alertId: string) => void;
  setLoading: (isLoading: boolean) => void;
  setError: (error: string | null) => void;

  // Refresh actions
  refreshData: () => Promise<void>;
}

const getDefaultDateRange = (): DateRange => {
  const end = new Date();
  const start = new Date();
  start.setMonth(start.getMonth() - 3);
  return { start, end };
};

export const useAnalyticsStore = create<AnalyticsState>()(
  devtools((set, get) => ({
    dateRange: getDefaultDateRange(),
    selectedCountries: [],
    selectedChannels: [],
    alerts: mockAlerts,
    unreadAlertCount: mockAlerts.filter((a) => !a.isRead).length,
    isLoading: false,
    error: null,

    channelMetrics: mockChannelMetrics,
    countryPerformance: mockCountryPerformance,
    campaignAttribution: mockCampaignAttribution,
    conversionFunnel: mockConversionFunnel,
    timeSeriesData: mockTimeSeriesData,

    setDateRange: (dateRange) =>
      set({
        dateRange,
      }),

    setSelectedCountries: (countries) =>
      set({
        selectedCountries: countries,
      }),

    setSelectedChannels: (channels) =>
      set({
        selectedChannels: channels,
      }),

    markAlertAsRead: (alertId) =>
      set((state) => ({
        alerts: state.alerts.map((a) =>
          a.id === alertId ? { ...a, isRead: true } : a
        ),
        unreadAlertCount: Math.max(
          0,
          state.unreadAlertCount - 1
        ),
      })),

    markAllAlertsAsRead: () =>
      set((state) => ({
        alerts: state.alerts.map((a) => ({ ...a, isRead: true })),
        unreadAlertCount: 0,
      })),

    dismissAlert: (alertId) =>
      set((state) => {
        const alert = state.alerts.find((a) => a.id === alertId);
        return {
          alerts: state.alerts.filter((a) => a.id !== alertId),
          unreadAlertCount: alert && !alert.isRead
            ? Math.max(0, state.unreadAlertCount - 1)
            : state.unreadAlertCount,
        };
      }),

    setLoading: (isLoading) => set({ isLoading }),

    setError: (error) => set({ error }),

    refreshData: async () => {
      set({ isLoading: true, error: null });
      try {
        // Simulate API call
        await new Promise((resolve) => setTimeout(resolve, 1000));
        set({ isLoading: false });
      } catch (error) {
        set({
          isLoading: false,
          error: error instanceof Error ? error.message : 'Failed to refresh data',
        });
      }
    },
  }),
  { name: 'AnalyticsStore' }
));

// Selectors
export const selectFilteredCountryPerformance = (state: AnalyticsState) => {
  if (state.selectedCountries.length === 0) {
    return state.countryPerformance;
  }
  return state.countryPerformance.filter((c) =>
    state.selectedCountries.includes(c.countryCode)
  );
};

export const selectFilteredChannelMetrics = (state: AnalyticsState) => {
  if (state.selectedChannels.length === 0) {
    return state.channelMetrics;
  }
  return state.channelMetrics.filter((c) =>
    state.selectedChannels.includes(c.channel)
  );
};

export const selectCriticalAlerts = (state: AnalyticsState) =>
  state.alerts.filter((a) => a.severity === 'critical' && !a.isRead);

export const selectHighSeverityAlerts = (state: AnalyticsState) =>
  state.alerts.filter((a) => a.severity === 'high' && !a.isRead);
