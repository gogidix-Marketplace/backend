import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import type { User, UserRole } from '@shared/types'

interface AuthState {
  user: User | null
  isAuthenticated: boolean
  isLoading: boolean
  login: (email: string, password: string) => Promise<void>
  logout: () => void
  complete2FA: () => void
  hasPermission: (permission: string) => boolean
  hasRole: (roles: UserRole[]) => boolean
  _pendingAuth?: { user: User } | null
}

const MOCK_USERS: Record<string, { user: User; password: string }> = {
  'ceo@gogidix.com': {
    user: {
      id: 'usr-ceo-001',
      email: 'ceo@gogidix.com',
      firstName: 'John',
      lastName: 'Mitchell',
      displayName: 'John Mitchell',
      role: 'CEO',
      department: 'executive',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=JM',
      permissions: [
        'view:all', 'view:department', 'view:country', 'approve:budget',
        'approve:hiring', 'approve:initiative', 'create:kpi', 'edit:kpi',
        'delete:kpi', 'view:reports', 'create:reports', 'manage:users',
        'manage:settings', 'view:audit_logs',
      ],
      isActive: true,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    password: 'password123',
  },
  'acting-ceo@gogidix.com': {
    user: {
      id: 'usr-acting-ceo-001',
      email: 'acting-ceo@gogidix.com',
      firstName: 'Sarah',
      lastName: 'Connor',
      displayName: 'Sarah Connor',
      role: 'ACTING_CEO',
      department: 'executive',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=SC',
      permissions: [
        'view:all', 'approve:budget', 'approve:hiring', 'approve:initiative',
        'view:reports', 'manage:settings',
      ],
      isActive: true,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    password: 'password123',
  },
  'cfo@gogidix.com': {
    user: {
      id: 'usr-cfo-001',
      email: 'cfo@gogidix.com',
      firstName: 'Amanda',
      lastName: 'Sterling',
      displayName: 'Amanda Sterling',
      role: 'CFO',
      department: 'finance',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=AS',
      permissions: [
        'view:financial', 'view:budget', 'view:reports', 'approve:budget',
        'approve:expenses', 'manage:financial_settings', 'view:audit_logs',
      ],
      isActive: true,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    password: 'password123',
  },
  'coo@gogidix.com': {
    user: {
      id: 'usr-coo-001',
      email: 'coo@gogidix.com',
      firstName: 'Marcus',
      lastName: 'Chen',
      displayName: 'Marcus Chen',
      role: 'COO',
      department: 'operations',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=MC',
      permissions: [
        'view:operations', 'view:incidents', 'view:resources', 'view:reports',
        'approve:operations', 'manage:incidents', 'manage:resources', 'view:audit_logs',
      ],
      isActive: true,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    password: 'password123',
  },
  'cto@gogidix.com': {
    user: {
      id: 'usr-cto-001',
      email: 'cto@gogidix.com',
      firstName: 'Elena',
      lastName: 'Kovacs',
      displayName: 'Elena Kovacs',
      role: 'CTO',
      department: 'technology',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=EK',
      permissions: [
        'view:technology', 'view:infrastructure', 'view:engineering',
        'view:security', 'view:reports', 'approve:technology',
        'manage:infrastructure', 'manage:engineering', 'view:audit_logs',
      ],
      isActive: true,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    password: 'password123',
  },
}

export const MOCK_PASSWORD = 'password123'

function getStorageKey(): string {
  const port = typeof window !== 'undefined' ? (window.location.port || '5173') : '5173'
  const portToRole: Record<string, string> = {
    '3011': 'ceo', '3012': 'cfo', '3013': 'coo', '3014': 'cto',
  }
  const role = portToRole[port] || 'ceo'
  return `${role}-exec-auth-v2`
}

export const useAuthStore = create<AuthState>()(
  persist(
    (set, get) => ({
      user: null,
      isAuthenticated: false,
      isLoading: false,
      _pendingAuth: null as { user: User } | null,

      login: async (email: string, password: string) => {
        set({ isLoading: true })
        await new Promise(resolve => setTimeout(resolve, 500))

        const mockUser = MOCK_USERS[email]

        if (mockUser && mockUser.password === password) {
          set({
            user: mockUser.user,
            isAuthenticated: false,
            _pendingAuth: { user: mockUser.user },
            isLoading: false,
          })
        } else {
          set({ isLoading: false })
          throw new Error('Invalid credentials')
        }
      },

      complete2FA: () => {
        set({
          isAuthenticated: true,
          _pendingAuth: null,
        })
      },

      logout: () => {
        set({ user: null, isAuthenticated: false, _pendingAuth: null })
      },

      hasPermission: (permission: string) => {
        const { user } = get()
        return user?.permissions.includes(permission) ?? false
      },

      hasRole: (roles: UserRole[]) => {
        const { user } = get()
        return user ? roles.includes(user.role) : false
      },
    }),
    {
      name: getStorageKey(),
      partialize: (state) => ({
        user: state.user,
        isAuthenticated: state.isAuthenticated,
      }),
    }
  )
)

export { MOCK_USERS }
