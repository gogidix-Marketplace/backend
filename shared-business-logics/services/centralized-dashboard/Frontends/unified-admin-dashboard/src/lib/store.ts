import { create } from 'zustand'

interface DashboardState {
  selectedTenant: string
  setSelectedTenant: (tenant: string) => void
  sidebarOpen: boolean
  toggleSidebar: () => void
  notifications: Notification[]
  addNotification: (notification: Omit<Notification, 'id' | 'timestamp'>) => void
  removeNotification: (id: string) => void
  realTimeEnabled: boolean
  toggleRealTime: () => void
}

export interface Notification {
  id: string
  type: 'info' | 'success' | 'warning' | 'error'
  title: string
  message: string
  timestamp: Date
}

export const useDashboardStore = create<DashboardState>((set) => ({
  selectedTenant: 'default',
  setSelectedTenant: (tenant) => set({ selectedTenant: tenant }),

  sidebarOpen: true,
  toggleSidebar: () => set((state) => ({ sidebarOpen: !state.sidebarOpen })),

  notifications: [],
  addNotification: (notification) =>
    set((state) => ({
      notifications: [
        ...state.notifications,
        {
          ...notification,
          id: crypto.randomUUID(),
          timestamp: new Date(),
        },
      ],
    })),
  removeNotification: (id) =>
    set((state) => ({
      notifications: state.notifications.filter((n) => n.id !== id),
    })),

  realTimeEnabled: true,
  toggleRealTime: () => set((state) => ({ realTimeEnabled: !state.realTimeEnabled })),
}))
