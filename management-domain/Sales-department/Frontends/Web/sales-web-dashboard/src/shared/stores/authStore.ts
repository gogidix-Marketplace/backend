import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import type { SalesUser, SalesUserRole } from '@shared/types'

interface AuthState {
  user: SalesUser | null
  isAuthenticated: boolean
  isLoading: boolean
  selectedCountry: string | null
  selectedView: 'global' | 'country'
  login: (email: string, password: string) => Promise<void>
  logout: () => void
  hasPermission: (permission: string) => boolean
  hasRole: (roles: SalesUserRole[]) => boolean
  setSelectedCountry: (country: string | null) => void
  setSelectedView: (view: 'global' | 'country') => void
}

// Mock users for Sales Dashboard
const MOCK_USERS: Record<string, { user: SalesUser; password: string }> = {
  'vp.sales@gogidix.com': {
    user: {
      id: 'usr-vp-sales-001',
      email: 'vp.sales@gogidix.com',
      firstName: 'Michael',
      lastName: 'Chambers',
      displayName: 'Michael Chambers',
      role: 'VP_SALES',
      department: 'sales',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=MC',
      permissions: [
        'view:all_countries',
        'view:global_dashboard',
        'manage:global_targets',
        'approve:deals',
        'manage:territories',
        'manage:commissions',
        'view:reports',
        'create:reports',
        'manage:users',
        'manage:settings',
        'view:forecasts',
        'create:forecasts',
      ],
      country: undefined,
      targets: {
        annualQuota: 50000000,
        monthlyQuota: 4166667,
        quarterlyQuota: 12500000,
        currentRevenue: 42800000,
        commissionRate: 0.05,
      },
      isActive: true,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    password: 'password123',
  },
  'director.ng@gogidix.com': {
    user: {
      id: 'usr-dir-ng-001',
      email: 'director.ng@gogidix.com',
      firstName: 'Adebayo',
      lastName: 'Okafor',
      displayName: 'Adebayo Okafor',
      role: 'COUNTRY_DIRECTOR',
      department: 'sales',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=AO',
      permissions: [
        'view:country_dashboard',
        'view:country_data',
        'manage:country_targets',
        'approve:country_deals',
        'manage:country_territories',
        'view:country_reports',
        'manage:country_users',
        'view:forecasts',
      ],
      country: 'NG',
      region: 'West Africa',
      territory: ['Lagos', 'Abuja', 'Port Harcourt'],
      targets: {
        annualQuota: 12000000,
        monthlyQuota: 1000000,
        quarterlyQuota: 3000000,
        currentRevenue: 11400000,
        commissionRate: 0.08,
      },
      isActive: true,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    password: 'password123',
  },
  'manager.west@gogidix.com': {
    user: {
      id: 'usr-mgr-west-001',
      email: 'manager.west@gogidix.com',
      firstName: 'Sarah',
      lastName: 'Johnson',
      displayName: 'Sarah Johnson',
      role: 'SALES_MANAGER',
      department: 'sales',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=SJ',
      permissions: [
        'view:regional_dashboard',
        'view:regional_data',
        'manage:regional_targets',
        'approve:regional_deals',
        'view:regional_reports',
        'manage:regional_team',
        'view:forecasts',
      ],
      country: 'NG',
      region: 'West Africa',
      territory: ['Lagos'],
      targets: {
        annualQuota: 3000000,
        monthlyQuota: 250000,
        quarterlyQuota: 750000,
        currentRevenue: 2750000,
        commissionRate: 0.06,
      },
      isActive: true,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    password: 'password123',
  },
  'rep.john@gogidix.com': {
    user: {
      id: 'usr-rep-john-001',
      email: 'rep.john@gogidix.com',
      firstName: 'John',
      lastName: 'Doe',
      displayName: 'John Doe',
      role: 'SALES_REPRESENTATIVE',
      department: 'sales',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=JD',
      permissions: [
        'view:own_dashboard',
        'view:own_leads',
        'manage:own_opportunities',
        'view:own_customers',
        'create:activities',
        'view:own_commission',
      ],
      country: 'NG',
      region: 'West Africa',
      territory: ['Lagos Island'],
      targets: {
        annualQuota: 500000,
        monthlyQuota: 41667,
        quarterlyQuota: 125000,
        currentRevenue: 425000,
        commissionRate: 0.1,
      },
      isActive: true,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    password: 'password123',
  },
  'analyst.sales@gogidix.com': {
    user: {
      id: 'usr-analyst-001',
      email: 'analyst.sales@gogidix.com',
      firstName: 'Emily',
      lastName: 'Chen',
      displayName: 'Emily Chen',
      role: 'SALES_ANALYST',
      department: 'sales',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=EC',
      permissions: [
        'view:all_reports',
        'create:reports',
        'view:all_data',
        'export:data',
        'view:forecasts',
      ],
      targets: {
        annualQuota: 0,
        monthlyQuota: 0,
        quarterlyQuota: 0,
        currentRevenue: 0,
        commissionRate: 0,
      },
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
      selectedCountry: null,
      selectedView: 'global',

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
            selectedCountry: mockUser.user.country || null,
          })
        } else {
          set({ isLoading: false })
          throw new Error('Invalid credentials')
        }
      },

      logout: () => {
        set({
          user: null,
          isAuthenticated: false,
          selectedCountry: null,
          selectedView: 'global',
        })
      },

      hasPermission: (permission: string) => {
        const { user } = get()
        return user?.permissions.includes(permission) ?? false
      },

      hasRole: (roles: SalesUserRole[]) => {
        const { user } = get()
        return user ? roles.includes(user.role) : false
      },

      setSelectedCountry: (country: string | null) => {
        set({ selectedCountry: country })
      },

      setSelectedView: (view: 'global' | 'country') => {
        set({ selectedView: view })
      },
    }),
    {
      name: 'sales-auth-storage',
      partialize: state => ({
        user: state.user,
        isAuthenticated: state.isAuthenticated,
        selectedCountry: state.selectedCountry,
        selectedView: state.selectedView,
      }),
    }
  )
)

// Export mock users for testing
export { MOCK_USERS }
