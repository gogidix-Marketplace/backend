import { create } from 'zustand';
import { persist, createJSONStorage } from 'zustand/middleware';

interface Region {
  id: string;
  name: string;
  code: string;
  manager: string;
  headquarters: string;
  countries: Country[];
}

interface Country {
  id: string;
  name: string;
  code: string;
  currency: string;
  revenue: number;
  growth: number;
  customers: number;
}

interface RegionalStore {
  selectedRegion: string | null;
  selectedCountry: string | null;
  drillDownLevel: 'global' | 'regional' | 'country';
  viewMode: 'overview' | 'detailed' | 'comparative';
  sidebarOpen: boolean;
  darkMode: boolean;
  compareMode: boolean;
  compareList: string[];

  setSelectedRegion: (region: string | null) => void;
  setSelectedCountry: (country: string | null) => void;
  setDrillDownLevel: (level: 'global' | 'regional' | 'country') => void;
  setViewMode: (mode: 'overview' | 'detailed' | 'comparative') => void;
  toggleSidebar: () => void;
  setDarkMode: (dark: boolean) => void;
  toggleCompareMode: () => void;
  addToCompare: (regionId: string) => void;
  removeFromCompare: (regionId: string) => void;
  clearCompareList: () => void;
  resetFilters: () => void;
}

export const useRegionalStore = create<RegionalStore>()(
  persist(
    (set, get) => ({
      selectedRegion: null,
      selectedCountry: null,
      drillDownLevel: 'global',
      viewMode: 'overview',
      sidebarOpen: true,
      darkMode: false,
      compareMode: false,
      compareList: [],

      setSelectedRegion: (region) => {
        set({ selectedRegion: region, selectedCountry: null });
        if (region) {
          set({ drillDownLevel: 'regional' });
        } else {
          set({ drillDownLevel: 'global' });
        }
      },

      setSelectedCountry: (country) => {
        set({ selectedCountry: country });
        if (country) {
          set({ drillDownLevel: 'country' });
        } else {
          set({ drillDownLevel: 'regional' });
        }
      },

      setDrillDownLevel: (level) => set({ drillDownLevel: level }),

      setViewMode: (mode) => set({ viewMode: mode }),

      toggleSidebar: () => set((state) => ({ sidebarOpen: !state.sidebarOpen })),

      setDarkMode: (dark) => {
        set({ darkMode: dark });
        if (dark) {
          document.documentElement.classList.add('dark');
        } else {
          document.documentElement.classList.remove('dark');
        }
      },

      toggleCompareMode: () => set((state) => ({ compareMode: !state.compareMode })),

      addToCompare: (regionId) => {
        const compareList = get().compareList;
        if (!compareList.includes(regionId) && compareList.length < 4) {
          set({ compareList: [...compareList, regionId] });
        }
      },

      removeFromCompare: (regionId) => {
        set({ compareList: get().compareList.filter((id) => id !== regionId) });
      },

      clearCompareList: () => set({ compareList: [] }),

      resetFilters: () => {
        set({
          selectedRegion: null,
          selectedCountry: null,
          drillDownLevel: 'global',
          viewMode: 'overview',
          compareMode: false,
          compareList: [],
        });
      },
    }),
    {
      name: 'regional-dashboard-storage',
      storage: createJSONStorage(() => localStorage),
      partialize: (state) => ({
        darkMode: state.darkMode,
        sidebarOpen: state.sidebarOpen,
      }),
    }
  )
);
