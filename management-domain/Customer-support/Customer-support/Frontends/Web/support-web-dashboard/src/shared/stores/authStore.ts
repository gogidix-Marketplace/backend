import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import type { SupportUser, SupportUserRole, CountryCode } from '@shared/types'

interface AuthState {
  user: SupportUser | null
  isAuthenticated: boolean
  isLoading: boolean
  currentView: 'global' | 'country'
  selectedCountry: CountryCode | null
  login: (email: string, password: string) => Promise<void>
  logout: () => void
  setUser: (user: SupportUser) => void
  setCurrentView: (view: 'global' | 'country') => void
  setSelectedCountry: (country: CountryCode | null) => void
  hasPermission: (permission: string) => boolean
  hasRole: (roles: SupportUserRole[]) => boolean
  canAccessCountry: (country: CountryCode) => boolean
}

// Role permissions configuration
const ROLE_PERMISSIONS: Record<SupportUserRole, string[]> = {
  SUPPORT_LEAD: [
    'view:all',
    'view:country',
    'view:team',
    'manage:team',
    'manage:agents',
    'assign:tickets',
    'escalate:tickets',
    'view:analytics',
    'view:reports',
    'manage:sla',
    'manage:knowledge_base',
    'perform:qa_reviews',
    'view:all_countries',
    'manage:settings',
  ],
  SUPPORT_AGENT: [
    'view:assigned',
    'view:team',
    'update:tickets',
    'resolve:tickets',
    'view:customer_history',
    'view:knowledge_base',
    'use:chat',
    'use:phone',
  ],
  SPECIALIST_AGENT: [
    'view:assigned',
    'view:team',
    'update:tickets',
    'resolve:tickets',
    'view:customer_history',
    'view:knowledge_base',
    'contribute:knowledge_base',
    'use:chat',
  ],
  CUSTOMER: [
    'create:tickets',
    'view:own_tickets',
    'update:own_tickets',
    'view:knowledge_base',
    'use:chat',
    'rate:interactions',
  ],
  ADMIN: [
    'view:all',
    'manage:all',
    'manage:users',
    'manage:settings',
    'view:all_countries',
  ],
}

// Mock users for Support Dashboard
const MOCK_USERS: Record<string, { user: SupportUser; password: string }> = {
  // Global Support Lead
  'support.lead@gogidix.com': {
    user: {
      id: 'usr-support-lead-001',
      email: 'support.lead@gogidix.com',
      firstName: 'Sarah',
      lastName: 'Mitchell',
      displayName: 'Sarah Mitchell',
      role: 'SUPPORT_LEAD',
      department: 'customer-support',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=SM',
      permissions: ROLE_PERMISSIONS.SUPPORT_LEAD,
      country: undefined,
      isActive: true,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    password: 'password123',
  },

  // US Support Lead
  'us.lead@gogidix.com': {
    user: {
      id: 'usr-us-lead-001',
      email: 'us.lead@gogidix.com',
      firstName: 'James',
      lastName: 'Wilson',
      displayName: 'James Wilson',
      role: 'SUPPORT_LEAD',
      department: 'customer-support',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=JW',
      permissions: ROLE_PERMISSIONS.SUPPORT_LEAD,
      country: 'US',
      isActive: true,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    password: 'password123',
  },

  // Nigeria Support Lead
  'ng.lead@gogidix.com': {
    user: {
      id: 'usr-ng-lead-001',
      email: 'ng.lead@gogidix.com',
      firstName: 'Chinedu',
      lastName: 'Okonkwo',
      displayName: 'Chinedu Okonkwo',
      role: 'SUPPORT_LEAD',
      department: 'customer-support',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=CO',
      permissions: ROLE_PERMISSIONS.SUPPORT_LEAD,
      country: 'NG',
      isActive: true,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    password: 'password123',
  },

  // US Support Agent
  'us.agent@gogidix.com': {
    user: {
      id: 'usr-us-agent-001',
      email: 'us.agent@gogidix.com',
      firstName: 'Emily',
      lastName: 'Chen',
      displayName: 'Emily Chen',
      role: 'SUPPORT_AGENT',
      department: 'customer-support',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=EC',
      permissions: ROLE_PERMISSIONS.SUPPORT_AGENT,
      country: 'US',
      agentId: 'agt-us-001',
      isActive: true,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    password: 'password123',
  },

  // Nigeria Support Agent
  'ng.agent@gogidix.com': {
    user: {
      id: 'usr-ng-agent-001',
      email: 'ng.agent@gogidix.com',
      firstName: 'Adaeze',
      lastName: 'Nwachukwu',
      displayName: 'Adaeze Nwachukwu',
      role: 'SUPPORT_AGENT',
      department: 'customer-support',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=AN',
      permissions: ROLE_PERMISSIONS.SUPPORT_AGENT,
      country: 'NG',
      agentId: 'agt-ng-001',
      isActive: true,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    password: 'password123',
  },

  // Kenya Support Agent
  'ke.agent@gogidix.com': {
    user: {
      id: 'usr-ke-agent-001',
      email: 'ke.agent@gogidix.com',
      firstName: 'Brian',
      lastName: 'Omondi',
      displayName: 'Brian Omondi',
      role: 'SUPPORT_AGENT',
      department: 'customer-support',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=BO',
      permissions: ROLE_PERMISSIONS.SUPPORT_AGENT,
      country: 'KE',
      agentId: 'agt-ke-001',
      isActive: true,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    password: 'password123',
  },

  // Technical Specialist
  'specialist.tech@gogidix.com': {
    user: {
      id: 'usr-specialist-tech-001',
      email: 'specialist.tech@gogidix.com',
      firstName: 'Michael',
      lastName: 'Park',
      displayName: 'Michael Park',
      role: 'SPECIALIST_AGENT',
      department: 'technical',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=MP',
      permissions: ROLE_PERMISSIONS.SPECIALIST_AGENT,
      country: undefined,
      agentId: 'agt-spec-001',
      isActive: true,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    password: 'password123',
  },

  // Customer (for portal access)
  'customer@example.com': {
    user: {
      id: 'usr-customer-001',
      email: 'customer@example.com',
      firstName: 'John',
      lastName: 'Doe',
      displayName: 'John Doe',
      role: 'CUSTOMER',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=JD',
      permissions: ROLE_PERMISSIONS.CUSTOMER,
      country: 'US',
      customerId: 'cust-001',
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
      currentView: 'global',
      selectedCountry: null,

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
            currentView: mockUser.user.country ? 'country' : 'global',
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
          currentView: 'global',
          selectedCountry: null,
        })
      },

      setUser: (user: SupportUser) => {
        set({ user })
      },

      setCurrentView: (view: 'global' | 'country') => {
        set({ currentView: view })
      },

      setSelectedCountry: (country: CountryCode | null) => {
        set({ selectedCountry: country })
      },

      hasPermission: (permission: string) => {
        const { user } = get()
        return user?.permissions.includes(permission) ?? false
      },

      hasRole: (roles: SupportUserRole[]) => {
        const { user } = get()
        return user ? roles.includes(user.role) : false
      },

      canAccessCountry: (country: CountryCode) => {
        const { user } = get()
        // Admins and global leads can access all countries
        if (user?.role === 'ADMIN' || user?.role === 'SUPPORT_LEAD') {
          return !user.country || user.country === 'GLOBAL'
        }
        // Users can access their assigned country
        return user?.country === country
      },
    }),
    {
      name: 'support-auth-storage',
      partialize: state => ({
        user: state.user,
        isAuthenticated: state.isAuthenticated,
        currentView: state.currentView,
        selectedCountry: state.selectedCountry,
      }),
    }
  )
)

// Export mock users for testing
export { MOCK_USERS }

// Export mock credentials helper
export function getMockCredentials() {
  return {
    globalLead: { email: 'support.lead@gogidix.com', password: 'password123' },
    usLead: { email: 'us.lead@gogidix.com', password: 'password123' },
    ngLead: { email: 'ng.lead@gogidix.com', password: 'password123' },
    usAgent: { email: 'us.agent@gogidix.com', password: 'password123' },
    ngAgent: { email: 'ng.agent@gogidix.com', password: 'password123' },
    keAgent: { email: 'ke.agent@gogidix.com', password: 'password123' },
    specialist: { email: 'specialist.tech@gogidix.com', password: 'password123' },
    customer: { email: 'customer@example.com', password: 'password123' },
  }
}
