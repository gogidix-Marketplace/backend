// UI Store - Zustand
// Manages UI state across the application

import { create } from 'zustand';
import { persist } from 'zustand/middleware';

interface UIState {
  // Sidebar
  sidebarOpen: boolean;

  // Theme
  theme: 'light' | 'dark';

  // Modals
  activeModal: string | null;
  modalData: any;

  // Filters
  selectedPeriod: string;
  selectedTeam: string | null;
  selectedTerritory: string | null;

  // Actions
  toggleSidebar: () => void;
  setSidebarOpen: (open: boolean) => void;
  setTheme: (theme: 'light' | 'dark') => void;
  openModal: (modalId: string, data?: any) => void;
  closeModal: () => void;
  setSelectedPeriod: (period: string) => void;
  setSelectedTeam: (teamId: string | null) => void;
  setSelectedTerritory: (territoryId: string | null) => void;
}

export const useUIStore = create<UIState>()(
  persist(
    (set) => ({
      // Initial state
      sidebarOpen: true,
      theme: 'light',
      activeModal: null,
      modalData: null,
      selectedPeriod: 'month',
      selectedTeam: null,
      selectedTerritory: null,

      // Sidebar actions
      toggleSidebar: () => set((state) => ({ sidebarOpen: !state.sidebarOpen })),
      setSidebarOpen: (open: boolean) => set({ sidebarOpen: open }),

      // Theme actions
      setTheme: (theme: 'light' | 'dark') => set({ theme }),

      // Modal actions
      openModal: (modalId: string, data?: any) => set({ activeModal: modalId, modalData: data }),
      closeModal: () => set({ activeModal: null, modalData: null }),

      // Filter actions
      setSelectedPeriod: (period: string) => set({ selectedPeriod: period }),
      setSelectedTeam: (teamId: string | null) => set({ selectedTeam: teamId }),
      setSelectedTerritory: (territoryId: string | null) => set({ selectedTerritory: territoryId }),
    }),
    {
      name: 'country-ui-storage',
      partialize: (state) => ({
        sidebarOpen: state.sidebarOpen,
        theme: state.theme,
        selectedPeriod: state.selectedPeriod,
      }),
    }
  )
);
