import { create } from 'zustand';
import { persist, createJSONStorage } from 'zustand/middleware';
import {
  KPIMetric,
  Region,
  Country,
  RegionalMetrics,
  FilterOptions,
  SortOptions,
  DashboardSettings,
  Notification,
  User,
} from '../types';

interface DashboardState {
  // UI State
  isLoading: boolean;
  sidebarOpen: boolean;
  selectedPeriod: string;
  selectedRegion: string | null;
  selectedCountry: string | null;
  darkMode: boolean;
  language: string;
  currency: string;

  // Data
  kpiMetrics: KPIMetric[];
  regions: Region[];
  countries: Country[];
  regionalMetrics: RegionalMetrics[];
  notifications: Notification[];
  unreadNotifications: number;

  // User
  user: User | null;

  // Settings
  settings: DashboardSettings;

  // Actions
  setLoading: (loading: boolean) => void;
  toggleSidebar: () => void;
  setSidebarOpen: (open: boolean) => void;
  setSelectedPeriod: (period: string) => void;
  setSelectedRegion: (regionId: string | null) => void;
  setSelectedCountry: (countryId: string | null) => void;
  setDarkMode: (dark: boolean) => void;
  setLanguage: (language: string) => void;
  setCurrency: (currency: string) => void;
  setKPIMetrics: (metrics: KPIMetric[]) => void;
  setRegions: (regions: Region[]) => void;
  setCountries: (countries: Country[]) => void;
  setRegionalMetrics: (metrics: RegionalMetrics[]) => void;
  addNotification: (notification: Notification) => void;
  markNotificationRead: (id: string) => void;
  markAllNotificationsRead: () => void;
  removeNotification: (id: string) => void;
  setUser: (user: User | null) => void;
  updateSettings: (settings: Partial<DashboardSettings>) => void;
  resetFilters: () => void;

  // Data refresh actions
  refreshData: () => Promise<void>;
}

const defaultSettings: DashboardSettings = {
  theme: 'system',
  language: 'en',
  currency: 'USD',
  notifications: {
    email: true,
    push: true,
    reportReady: true,
    deadlineReminder: true,
    complianceAlert: true,
  },
  display: {
    compactMode: false,
    showTrends: true,
    showTargets: true,
    chartAnimations: true,
    itemsPerPage: 10,
  },
};

export const useDashboardStore = create<DashboardState>()(
  persist(
    (set, get) => ({
      // Initial State
      isLoading: false,
      sidebarOpen: true,
      selectedPeriod: 'month',
      selectedRegion: null,
      selectedCountry: null,
      darkMode: false,
      language: 'en',
      currency: 'USD',
      kpiMetrics: [],
      regions: [],
      countries: [],
      regionalMetrics: [],
      notifications: [],
      unreadNotifications: 0,
      user: null,
      settings: defaultSettings,

      // Actions
      setLoading: (loading) => set({ isLoading: loading }),

      toggleSidebar: () => set((state) => ({ sidebarOpen: !state.sidebarOpen })),

      setSidebarOpen: (open) => set({ sidebarOpen: open }),

      setSelectedPeriod: (period) => set({ selectedPeriod: period }),

      setSelectedRegion: (regionId) => {
        set({ selectedRegion: regionId, selectedCountry: null });
      },

      setSelectedCountry: (countryId) => set({ selectedCountry: countryId }),

      setDarkMode: (dark) => {
        set({ darkMode: dark });
        // Update document class for dark mode
        if (dark) {
          document.documentElement.classList.add('dark');
        } else {
          document.documentElement.classList.remove('dark');
        }
      },

      setLanguage: (language) => set({ language }),

      setCurrency: (currency) => set({ currency }),

      setKPIMetrics: (metrics) => set({ kpiMetrics: metrics }),

      setRegions: (regions) => set({ regions }),

      setCountries: (countries) => set({ countries }),

      setRegionalMetrics: (metrics) => set({ regionalMetrics: metrics }),

      addNotification: (notification) => {
        const notifications = [notification, ...get().notifications];
        const unreadNotifications = notifications.filter(n => !n.read).length;
        set({ notifications, unreadNotifications });
      },

      markNotificationRead: (id) => {
        const notifications = get().notifications.map(n =>
          n.id === id ? { ...n, read: true } : n
        );
        const unreadNotifications = notifications.filter(n => !n.read).length;
        set({ notifications, unreadNotifications });
      },

      markAllNotificationsRead: () => {
        const notifications = get().notifications.map(n => ({ ...n, read: true }));
        set({ notifications, unreadNotifications: 0 });
      },

      removeNotification: (id) => {
        const notifications = get().notifications.filter(n => n.id !== id);
        const unreadNotifications = notifications.filter(n => !n.read).length;
        set({ notifications, unreadNotifications });
      },

      setUser: (user) => set({ user }),

      updateSettings: (newSettings) => {
        set({
          settings: { ...get().settings, ...newSettings }
        });
      },

      resetFilters: () => {
        set({
          selectedPeriod: 'month',
          selectedRegion: null,
          selectedCountry: null,
        });
      },

      refreshData: async () => {
        set({ isLoading: true });
        try {
          // This would trigger data refetching
          await Promise.all([
            // Refetch all data sources
          ]);
        } finally {
          set({ isLoading: false });
        }
      },
    }),
    {
      name: 'global-dashboard-storage',
      partialize: (state) => ({
        darkMode: state.darkMode,
        language: state.language,
        currency: state.currency,
        settings: state.settings,
        user: state.user,
        sidebarOpen: state.sidebarOpen,
      }),
    }
  )
);

// Selector hooks for optimized component re-renders
export const useKPIMetrics = () => useDashboardStore((state) => state.kpiMetrics);
export const useRegions = () => useDashboardStore((state) => state.regions);
export const useCountries = () => useDashboardStore((state) => state.countries);
export const useRegionalMetrics = () => useDashboardStore((state) => state.regionalMetrics);
export const useNotifications = () => useDashboardStore((state) => state.notifications);
export const useUnreadNotifications = () => useDashboardStore((state) => state.unreadNotifications);
export const useUser = () => useDashboardStore((state) => state.user);
export const useSettings = () => useDashboardStore((state) => state.settings);
export const useIsLoading = () => useDashboardStore((state) => state.isLoading);
export const useDarkMode = () => useDashboardStore((state) => state.darkMode);
export const useLanguage = () => useDashboardStore((state) => state.language);
export const useCurrency = () => useDashboardStore((state) => state.currency);
export const useSelectedPeriod = () => useDashboardStore((state) => state.selectedPeriod);
export const useSelectedRegion = () => useDashboardStore((state) => state.selectedRegion);
export const useSelectedCountry = () => useDashboardStore((state) => state.selectedCountry);
