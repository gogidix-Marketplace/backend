import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import type { GBMUser, GBMUserRole } from '@shared/types'

interface AuthState {
  user: GBMUser | null
  isAuthenticated: boolean
  isLoading: boolean
  login: (email: string, password: string) => Promise<void>
  logout: () => void
  hasPermission: (permission: string) => boolean
  hasRole: (roles: GBMUserRole[]) => boolean
  hasAccessToRegion: (region: string) => boolean
  hasAccessToCountry: (country: string) => boolean
}

// Mock users for GBM Portal
const MOCK_USERS: Record<string, { user: GBMUser; password: string }> = {
  'gbm-admin@gogidix.com': {
    user: {
      id: 'usr-gbm-admin-001',
      email: 'gbm-admin@gogidix.com',
      firstName: 'Amanda',
      lastName: 'Sterling',
      displayName: 'Amanda Sterling',
      role: 'GBM_ADMIN',
      department: 'global-business-management',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=AS',
      permissions: [
        'view:all',
        'view:global',
        'view:regional',
        'view:country',
        'manage:regions',
        'manage:countries',
        'manage:currencies',
        'manage:aggregation',
        'create:reports',
        'edit:reports',
        'delete:reports',
        'schedule:reports',
        'manage:exports',
        'manage:localization',
        'manage:users',
        'manage:settings',
        'view:audit_logs',
        'create:dashboard',
        'edit:dashboard',
        'delete:dashboard',
      ],
      regions: undefined,
      countries: undefined,
      isActive: true,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    password: 'password123',
  },
  'regional-manager-africa@gogidix.com': {
    user: {
      id: 'usr-reg-mgr-001',
      email: 'regional-manager-africa@gogidix.com',
      firstName: 'Chinedu',
      lastName: 'Okonkwo',
      displayName: 'Chinedu Okonkwo',
      role: 'REGIONAL_MANAGER',
      department: 'global-business-management',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=CO',
      permissions: [
        'view:regional',
        'view:country',
        'manage:countries',
        'create:reports',
        'edit:reports',
        'schedule:reports',
        'manage:exports',
        'create:dashboard',
        'edit:dashboard',
      ],
      regions: ['africa-north', 'africa-west', 'africa-east', 'africa-south'],
      countries: ['NG', 'KE', 'ZA', 'GH', 'EG'],
      isActive: true,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    password: 'password123',
  },
  'country-admin-nigeria@gogidix.com': {
    user: {
      id: 'usr-ctry-admin-001',
      email: 'country-admin-nigeria@gogidix.com',
      firstName: 'Ngozi',
      lastName: 'Adewale',
      displayName: 'Ngozi Adewale',
      role: 'COUNTRY_ADMIN',
      department: 'global-business-management',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=NA',
      permissions: [
        'view:country',
        'manage:country',
        'create:reports',
        'edit:reports',
        'schedule:reports',
        'manage:exports',
        'create:dashboard',
      ],
      regions: ['africa-west'],
      countries: ['NG'],
      isActive: true,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    password: 'password123',
  },
  'gbm-analyst@gogidix.com': {
    user: {
      id: 'usr-analyst-001',
      email: 'gbm-analyst@gogidix.com',
      firstName: 'David',
      lastName: 'Kim',
      displayName: 'David Kim',
      role: 'GBM_ANALYST',
      department: 'global-business-management',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=DK',
      permissions: [
        'view:all',
        'view:regional',
        'view:country',
        'create:reports',
        'create:dashboard',
        'view:bi',
      ],
      regions: undefined,
      countries: undefined,
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
          })
        } else {
          set({ isLoading: false })
          throw new Error('Invalid credentials')
        }
      },

      logout: () => {
        set({ user: null, isAuthenticated: false })
      },

      hasPermission: (permission: string) => {
        const { user } = get()
        return user?.permissions.includes(permission) ?? false
      },

      hasRole: (roles: GBMUserRole[]) => {
        const { user } = get()
        return user ? roles.includes(user.role) : false
      },

      hasAccessToRegion: (region: string) => {
        const { user } = get()
        if (!user) return false
        if (user.role === 'GBM_ADMIN' || user.role === 'GBM_ANALYST') return true
        return user.regions?.includes(region as any) ?? false
      },

      hasAccessToCountry: (country: string) => {
        const { user } = get()
        if (!user) return false
        if (user.role === 'GBM_ADMIN' || user.role === 'GBM_ANALYST') return true
        return user.countries?.includes(country as any) ?? false
      },
    }),
    {
      name: 'gbm-auth-storage',
      partialize: state => ({
        user: state.user,
        isAuthenticated: state.isAuthenticated,
      }),
    }
  )
)

// Export mock users for testing
export { MOCK_USERS }
