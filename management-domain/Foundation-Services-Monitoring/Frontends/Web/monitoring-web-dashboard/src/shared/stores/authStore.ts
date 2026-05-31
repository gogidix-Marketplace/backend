import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import type { User, MonitoringRole, ServiceType } from '@shared/types'

interface AuthState {
  user: User | null
  isAuthenticated: boolean
  isLoading: boolean
  activeAlerts: number
  login: (email: string, password: string) => Promise<void>
  logout: () => void
  hasPermission: (permission: string) => boolean
  hasRole: (roles: MonitoringRole[]) => boolean
  canAccessService: (serviceType: ServiceType) => boolean
  setActiveAlerts: (count: number) => void
}

// Mock users for Monitoring Dashboard
const MOCK_USERS: Record<string, { user: User; password: string }> = {
  'devops@gogidix.com': {
    user: {
      id: 'usr-devops-001',
      email: 'devops@gogidix.com',
      firstName: 'Mike',
      lastName: 'Chen',
      displayName: 'Mike Chen',
      role: 'DEVOPS_LEAD',
      department: 'infrastructure',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=MC',
      permissions: [
        'view:all',
        'view:services',
        'view:alerts',
        'view:metrics',
        'view:logs',
        'acknowledge:alerts',
        'create:alert_rules',
        'modify:alert_rules',
        'delete:alert_rules',
        'restart:services',
        'scale:services',
        'configure:notifications',
        'export:reports',
        'manage:users',
        'manage:settings',
      ],
      services: undefined,
      isActive: true,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    password: 'password123',
  },
  'sre@gogidix.com': {
    user: {
      id: 'usr-sre-001',
      email: 'sre@gogidix.com',
      firstName: 'Sarah',
      lastName: 'Johnson',
      displayName: 'Sarah Johnson',
      role: 'SRE_ENGINEER',
      department: 'infrastructure',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=SJ',
      permissions: [
        'view:all',
        'view:services',
        'view:alerts',
        'view:metrics',
        'view:logs',
        'acknowledge:alerts',
        'restart:services',
        'create:incidents',
        'export:reports',
      ],
      services: undefined,
      isActive: true,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    password: 'password123',
  },
  'analyst@gogidix.com': {
    user: {
      id: 'usr-analyst-001',
      email: 'analyst@gogidix.com',
      firstName: 'James',
      lastName: 'Wilson',
      displayName: 'James Wilson',
      role: 'MONITORING_ANALYST',
      department: 'operations',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=JW',
      permissions: [
        'view:services',
        'view:alerts',
        'view:metrics',
        'export:reports',
      ],
      services: undefined,
      isActive: true,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    password: 'password123',
  },
  'service-owner@gogidix.com': {
    user: {
      id: 'usr-so-001',
      email: 'service-owner@gogidix.com',
      firstName: 'Emily',
      lastName: 'Davis',
      displayName: 'Emily Davis',
      role: 'SERVICE_OWNER',
      department: 'infrastructure',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=ED',
      permissions: [
        'view:own_services',
        'view:alerts',
        'acknowledge:own_alerts',
        'restart:own_services',
        'export:reports',
      ],
      services: ['api_gateway', 'auth_service'],
      isActive: true,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    password: 'password123',
  },
  'admin@gogidix.com': {
    user: {
      id: 'usr-admin-001',
      email: 'admin@gogidix.com',
      firstName: 'System',
      lastName: 'Administrator',
      displayName: 'System Admin',
      role: 'ADMIN',
      department: 'infrastructure',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=SA',
      permissions: [
        'view:all',
        'manage:all',
        'configure:all',
      ],
      services: undefined,
      isActive: true,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    password: 'password123',
  },
}

export const MOCK_PASSWORD = 'password123'

export const useAuthStore = create<AuthState>()(
  persist(
    (set, get) => ({
      user: null,
      isAuthenticated: false,
      isLoading: false,
      activeAlerts: 3,

      login: async (email: string, password: string) => {
        set({ isLoading: true })
        // Simulate API delay
        await new Promise(resolve => setTimeout(resolve, 500))

        const mockUser = MOCK_USERS[email]

        if (mockUser && mockUser.password === password) {
          set({
            user: mockUser.user,
            isAuthenticated: true,
            isLoading: false,
            activeAlerts: 3, // Set initial active alerts
          })
        } else {
          set({ isLoading: false })
          throw new Error('Invalid credentials')
        }
      },

      logout: () => {
        set({ user: null, isAuthenticated: false, activeAlerts: 0 })
      },

      hasPermission: (permission: string) => {
        const { user } = get()
        return user?.permissions.includes(permission) ?? false
      },

      hasRole: (roles: MonitoringRole[]) => {
        const { user } = get()
        return user ? roles.includes(user.role) : false
      },

      canAccessService: (serviceType: ServiceType) => {
        const { user } = get()
        if (!user) return false

        // DevOps Lead and Admin can access all services
        if (user.role === 'DEVOPS_LEAD' || user.role === 'ADMIN') return true

        // SRE can access all services
        if (user.role === 'SRE_ENGINEER') return true

        // Service Owner can only access their assigned services
        if (user.role === 'SERVICE_OWNER') {
          return user.services?.includes(serviceType) ?? false
        }

        // Monitoring Analyst can view all services (read-only)
        if (user.role === 'MONITORING_ANALYST') return true

        return false
      },

      setActiveAlerts: (count: number) => {
        set({ activeAlerts: count })
      },
    }),
    {
      name: 'monitoring-auth-storage',
      partialize: state => ({
        user: state.user,
        isAuthenticated: state.isAuthenticated,
      }),
    }
  )
)

// Export mock users for testing
export { MOCK_USERS }
