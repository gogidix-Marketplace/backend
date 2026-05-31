// UI Store - Zustand
// Manages global UI state

import { create } from 'zustand';
import type { RegionScope } from '@domain/types';

interface UIState {
  // Navigation
  sidebarOpen: boolean;
  currentPage: string;
  breadcrumbs: Array<{ label: string; path: string }>;

  // Filters
  selectedPeriod: string;
  selectedCountries: string[];
  selectedRegion: RegionScope;

  // Theme
  theme: 'light' | 'dark';

  // Modals
  activeModal: string | null;
  modalData: any;

  // Notifications
  notifications: Array<{
    id: string;
    type: 'success' | 'error' | 'warning' | 'info';
    message: string;
    duration?: number;
  }>;

  // Actions
  toggleSidebar: () => void;
  setSidebarOpen: (open: boolean) => void;
  setCurrentPage: (page: string) => void;
  setBreadcrumbs: (breadcrumbs: Array<{ label: string; path: string }>) => void;
  setSelectedPeriod: (period: string) => void;
  setSelectedCountries: (countries: string[]) => void;
  setSelectedRegion: (region: RegionScope) => void;
  setTheme: (theme: 'light' | 'dark') => void;
  openModal: (modal: string, data?: any) => void;
  closeModal: () => void;
  addNotification: (notification: {
    type: 'success' | 'error' | 'warning' | 'info';
    message: string;
    duration?: number;
  }) => void;
  removeNotification: (id: string) => void;
  clearNotifications: () => void;
}

export const useUIStore = create<UIState>((set) => ({
  // Initial state
  sidebarOpen: true,
  currentPage: 'overview',
  breadcrumbs: [{ label: 'Home', path: '/' }],
  selectedPeriod: 'month',
  selectedCountries: [],
  selectedRegion: 'GLOBAL' as RegionScope,
  theme: 'light',
  activeModal: null,
  modalData: null,
  notifications: [],

  // Sidebar actions
  toggleSidebar: () => set((state) => ({ sidebarOpen: !state.sidebarOpen })),
  setSidebarOpen: (open: boolean) => set({ sidebarOpen: open }),

  // Navigation actions
  setCurrentPage: (page: string) => set({ currentPage: page }),
  setBreadcrumbs: (breadcrumbs) => set({ breadcrumbs }),

  // Filter actions
  setSelectedPeriod: (period: string) => set({ selectedPeriod: period }),
  setSelectedCountries: (countries: string[]) => set({ selectedCountries: countries }),
  setSelectedRegion: (region) => set({ selectedRegion: region }),

  // Theme action
  setTheme: (theme: 'light' | 'dark') => {
    set({ theme });
    // Apply theme to document
    document.documentElement.setAttribute('data-theme', theme);
  },

  // Modal actions
  openModal: (modal: string, data?: any) => set({ activeModal: modal, modalData: data }),
  closeModal: () => set({ activeModal: null, modalData: null }),

  // Notification actions
  addNotification: (notification) => {
    const id = `notification-${Date.now()}-${Math.random().toString(36).substring(2, 9)}`;
    set((state) => ({
      notifications: [...state.notifications, { ...notification, id }],
    }));

    // Auto-remove notification after duration
    if (notification.duration !== 0) {
      setTimeout(() => {
        set((state) => ({
          notifications: state.notifications.filter((n) => n.id !== id),
        }));
      }, notification.duration || 5000);
    }
  },

  removeNotification: (id: string) =>
    set((state) => ({
      notifications: state.notifications.filter((n) => n.id !== id),
    })),

  clearNotifications: () => set({ notifications: [] }),
}));
