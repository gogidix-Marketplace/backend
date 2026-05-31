// Local UI Store - Zustand
import { create } from 'zustand';
import { devtools, persist } from 'zustand/middleware';

interface LocalUIState {
  theme: 'light' | 'dark' | 'system';
  sidebarCollapsed: boolean;
  notifications: Array<{
    id: string;
    type: 'info' | 'success' | 'warning' | 'error';
    title: string;
    message: string;
  }>;
  activeModal: string | null;

  setTheme: (theme: 'light' | 'dark' | 'system') => void;
  toggleSidebar: () => void;
  addNotification: (notification: Omit<LocalUIState['notifications'][0]>) => void;
  removeNotification: (id: string) => void;
  openModal: (modalId: string) => void;
  closeModal: () => void;
}

export const useLocalUIStore = create<LocalUIState>()(
  devtools(
    persist(
      (set) => ({
        theme: 'system',
        sidebarCollapsed: false,
        notifications: [],
        activeModal: null,

        setTheme: (theme) => set({ theme }),
        toggleSidebar: () =>
          set((state) => ({ sidebarCollapsed: !state.sidebarCollapsed })),
        addNotification: (notification) => {
          const id = Math.random().toString(36).substring(7);
          set((state) => ({
            notifications: [...state.notifications, { ...notification, id }],
          }));
        },
        removeNotification: (id) =>
          set((state) => ({
            notifications: state.notifications.filter((n) => n.id !== id),
          })),
        openModal: (modalId) => set({ activeModal: modalId }),
        closeModal: () => set({ activeModal: null }),
      }),
      {
        name: 'local-ui-storage',
        partialize: (state) => ({
          theme: state.theme,
          sidebarCollapsed: state.sidebarCollapsed,
        }),
      }
    ),
    { name: 'LocalUIStore' }
  )
);
