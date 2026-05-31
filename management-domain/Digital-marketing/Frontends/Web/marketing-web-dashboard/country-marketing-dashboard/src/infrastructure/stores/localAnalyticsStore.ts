// Local Analytics Store - Zustand
import { create } from 'zustand';
import { devtools } from 'zustand/middleware';
import { CountryMetrics } from '../../domain/types';

interface LocalAnalyticsState {
  metrics: CountryMetrics | null;
  isLoading: boolean;
  error: string | null;

  loadMetrics: (countryCode: string, period: { start: Date; end: Date }) => Promise<void>;
  setLoading: (loading: boolean) => void;
  setError: (error: string | null) => void;
}

export const useLocalAnalyticsStore = create<LocalAnalyticsState>()(
  devtools(
    (set, get) => ({
      metrics: null,
      isLoading: false,
      error: null,

      loadMetrics: async (countryCode, period) => {
        set({ isLoading: true, error: null });
        try {
          // Mock data - in production this would fetch from API
          const mockMetrics: CountryMetrics = {
            period,
            country: {
              code: countryCode,
              name: countryCode === 'US' ? 'United States' : 'Country',
            },
            summary: {
              budget: 50000,
              spent: 37500,
              revenue: 112500,
              leads: 1250,
              conversions: 350,
              roi: 200,
            },
            topCampaigns: [
              {
                id: 'local-camp-001',
                name: 'Spring Festival Promotion',
                revenue: 52500,
                roi: 180,
                leads: 875,
              },
            ],
          };
          set({ metrics: mockMetrics, isLoading: false });
        } catch (error: {
          set({
            error: error instanceof Error ? error.message : 'Failed to load metrics',
            isLoading: false,
          });
        })
      },

      setLoading: (loading) => set({ isLoading: loading }),
      setError: (error) => set({ error }),
    }),
    { name: 'LocalAnalyticsStore' }
  )
);
