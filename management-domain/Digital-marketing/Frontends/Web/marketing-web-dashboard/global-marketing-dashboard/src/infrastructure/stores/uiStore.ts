// UI Store - Zustand
// Manages global UI state

import { create } from 'zustand';
import { devtools, persist } from 'zustand/middleware';

interface UIState {
  // Theme
  theme: 'light' | 'dark' | 'system';
  setTheme: (theme: 'light' | 'dark' | 'system') => void;

  // Sidebar
  sidebarCollapsed: boolean;
  toggleSidebar: () => void;
  setSidebarCollapsed: (collapsed: boolean) => void;

  // Notifications
  notifications: Array<{
    id: string;
    type: 'info' | 'success' | 'warning' | 'error';
    title: string;
    message: string;
    duration?: number;
  }>;
  addNotification: (notification: Omit<UIState['notifications'][0], 'id'>) => void;
  removeNotification: (id: string) => void;
  clearNotifications: () => void;

  // Modal
  activeModal: string | null;
  openModal: (modalId: string) => void;
  closeModal: () => void;

  // Loading
  globalLoading: boolean;
  setGlobalLoading: (loading: boolean) => void;

  // User Preferences
  preferences: {
    dateFormat: string;
    timeFormat: '12h' | '24h';
    timezone: string;
    currency: string;
  };
  setPreferences: (preferences: Partial<UIState['preferences']>) => void;

  // Dashboard Layout
  dashboardWidgets: string[];
  setDashboardWidgets: (widgets: string[]) => void;
}

export const useUIStore = create<UIState>()(
  devtools(
    persist(
      (set, get) => ({
        // Theme
        theme: 'system',
        setTheme: (theme) => set({ theme }),

        // Sidebar
        sidebarCollapsed: false,
        toggleSidebar: () =>
          set((state) => ({ sidebarCollapsed: !state.sidebarCollapsed })),
        setSidebarCollapsed: (collapsed) => set({ sidebarCollapsed: collapsed }),

        // Notifications
        notifications: [],
        addNotification: (notification) => {
          const id = Math.random().toString(36).substring(7);
          set((state) => ({
            notifications: [...state.notifications, { ...notification, id }],
          }));

          // Auto-remove notification if duration is specified
          if (notification.duration && notification.duration > 0) {
            setTimeout(() => {
              get().removeNotification(id);
            }, notification.duration);
          }
        },
        removeNotification: (id) =>
          set((state) => ({
            notifications: state.notifications.filter((n) => n.id !== id),
          })),
        clearNotifications: () => set({ notifications: [] }),

        // Modal
        activeModal: null,
        openModal: (modalId) => set({ activeModal: modalId }),
        closeModal: () => set({ activeModal: null }),

        // Loading
        globalLoading: false,
        setGlobalLoading: (loading) => set({ globalLoading: loading }),

        // User Preferences
        preferences: {
          dateFormat: 'MMM d, yyyy',
          timeFormat: '12h',
          timezone: 'UTC',
          currency: 'USD',
        },
        setPreferences: (newPreferences) =>
          set((state) => ({
            preferences: { ...state.preferences, ...newPreferences },
          })),

        // Dashboard Layout
        dashboardWidgets: [
          'campaigns',
          'budget',
          'leads',
          'social',
          'email',
          'content',
        ],
        setDashboardWidgets: (widgets) => set({ dashboardWidgets: widgets }),
      }),
      {
        name: 'ui-storage',
        partialize: (state) => ({
          theme: state.theme,
          sidebarCollapsed: state.sidebarCollapsed,
          preferences: state.preferences,
          dashboardWidgets: state.dashboardWidgets,
        }),
      }
    ),
    { name: 'UIStore' }
  )
);

// Selectors
export const selectTheme = (state: UIState) => state.theme;
export const selectNotifications = (state: UIState) => state.notifications;
export const selectSidebarCollapsed = (state: UIState) => state.sidebarCollapsed;
