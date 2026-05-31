import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import type { AdminUser, AdminRole, Permission } from '@shared/types'

interface AuthState {
  user: AdminUser | null
  isAuthenticated: boolean
  isLoading: boolean
  login: (email: string, password: string) => Promise<void>
  logout: () => void
  hasPermission: (permission: Permission) => boolean
  hasAnyPermission: (permissions: Permission[]) => boolean
  hasRole: (roles: AdminRole[]) => boolean
  isSuperAdmin: () => boolean
  isSystemAdmin: () => boolean
}

// Role-based permissions mapping
const ROLE_PERMISSIONS: Record<AdminRole, Permission[]> = {
  SYSTEM_ADMINISTRATOR: [
    'view:dashboard',
    'view:users',
    'create:users',
    'edit:users',
    'delete:users',
    'view:roles',
    'create:roles',
    'edit:roles',
    'delete:roles',
    'view:permissions',
    'manage:permissions',
    'view:access_requests',
    'approve:access_requests',
    'reject:access_requests',
    'view:incidents',
    'create:incidents',
    'assign:incidents',
    'resolve:incidents',
    'view:monitoring',
    'manage:monitoring',
    'view:security',
    'manage:security',
    'view:threats',
    'manage:threats',
    'view:vulnerabilities',
    'manage:vulnerabilities',
    'view:configuration',
    'edit:configuration',
    'view:deployments',
    'create:deployments',
    'rollback:deployments',
    'view:compliance',
    'manage:compliance',
    'view:audit_logs',
    'export:audit_logs',
    'provision:users',
    'deprovision:users',
    'view:maintenance',
    'schedule:maintenance',
    'manage:notifications',
    'view:reports',
    'create:reports',
    'manage:settings',
  ],
  SUPER_ADMIN: [
    'view:dashboard',
    'view:users',
    'create:users',
    'edit:users',
    'delete:users',
    'view:roles',
    'create:roles',
    'edit:roles',
    'delete:roles',
    'view:permissions',
    'manage:permissions',
    'view:access_requests',
    'approve:access_requests',
    'reject:access_requests',
    'view:incidents',
    'create:incidents',
    'assign:incidents',
    'resolve:incidents',
    'view:monitoring',
    'manage:monitoring',
    'view:security',
    'manage:security',
    'view:threats',
    'manage:threats',
    'view:vulnerabilities',
    'manage:vulnerabilities',
    'view:configuration',
    'edit:configuration',
    'view:deployments',
    'create:deployments',
    'rollback:deployments',
    'view:compliance',
    'manage:compliance',
    'view:audit_logs',
    'export:audit_logs',
    'provision:users',
    'deprovision:users',
    'view:maintenance',
    'schedule:maintenance',
    'manage:notifications',
    'view:reports',
    'create:reports',
    'manage:settings',
  ],
  GLOBAL_SECURITY_OFFICER: [
    'view:dashboard',
    'view:users',
    'create:users',
    'edit:users',
    'view:roles',
    'view:permissions',
    'manage:permissions',
    'view:access_requests',
    'approve:access_requests',
    'reject:access_requests',
    'view:incidents',
    'create:incidents',
    'assign:incidents',
    'resolve:incidents',
    'view:monitoring',
    'view:security',
    'manage:security',
    'view:threats',
    'manage:threats',
    'view:vulnerabilities',
    'manage:vulnerabilities',
    'view:compliance',
    'manage:compliance',
    'view:audit_logs',
    'export:audit_logs',
    'view:reports',
    'create:reports',
  ],
  DEVOPS_ENGINEER: [
    'view:dashboard',
    'view:incidents',
    'create:incidents',
    'assign:incidents',
    'resolve:incidents',
    'view:monitoring',
    'manage:monitoring',
    'view:configuration',
    'edit:configuration',
    'view:deployments',
    'create:deployments',
    'rollback:deployments',
    'view:maintenance',
    'schedule:maintenance',
    'view:audit_logs',
    'view:reports',
  ],
  DOMAIN_ADMINISTRATOR: [
    'view:dashboard',
    'view:users',
    'create:users',
    'edit:users',
    'view:access_requests',
    'approve:access_requests',
    'reject:access_requests',
    'view:incidents',
    'create:incidents',
    'assign:incidents',
    'resolve:incidents',
    'view:monitoring',
    'view:audit_logs',
    'view:reports',
  ],
  IT_SUPPORT: [
    'view:dashboard',
    'view:incidents',
    'create:incidents',
    'resolve:incidents',
    'view:monitoring',
    'view:configuration',
  ],
  OPERATOR: [
    'view:dashboard',
    'view:incidents',
    'view:monitoring',
    'view:audit_logs',
  ],
  SECURITY_ANALYST: [
    'view:dashboard',
    'view:security',
    'view:threats',
    'view:vulnerabilities',
    'view:incidents',
    'view:audit_logs',
    'view:compliance',
    'view:reports',
  ],
}

// Mock users for Admin Dashboard
const MOCK_USERS: Record<string, { user: AdminUser; password: string }> = {
  'admin@gogidix.com': {
    user: {
      id: 'usr-admin-001',
      email: 'admin@gogidix.com',
      firstName: 'Robert',
      lastName: 'Chen',
      displayName: 'Robert Chen',
      role: 'SYSTEM_ADMINISTRATOR',
      department: 'system-administrator',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=RC',
      permissions: ROLE_PERMISSIONS.SYSTEM_ADMINISTRATOR,
      isActive: true,
      lastLogin: new Date(Date.now() - 1000 * 60 * 30).toISOString(),
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    password: 'password123',
  },
  'superadmin@gogidix.com': {
    user: {
      id: 'usr-super-001',
      email: 'superadmin@gogidix.com',
      firstName: 'Maria',
      lastName: 'Garcia',
      displayName: 'Maria Garcia',
      role: 'SUPER_ADMIN',
      department: 'system-administrator',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=MG',
      permissions: ROLE_PERMISSIONS.SUPER_ADMIN,
      isActive: true,
      lastLogin: new Date(Date.now() - 1000 * 60 * 60).toISOString(),
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    password: 'password123',
  },
  'security@gogidix.com': {
    user: {
      id: 'usr-sec-001',
      email: 'security@gogidix.com',
      firstName: 'James',
      lastName: 'Wilson',
      displayName: 'James Wilson',
      role: 'GLOBAL_SECURITY_OFFICER',
      department: 'system-administrator',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=JW',
      permissions: ROLE_PERMISSIONS.GLOBAL_SECURITY_OFFICER,
      isActive: true,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    password: 'password123',
  },
  'devops@gogidix.com': {
    user: {
      id: 'usr-devops-001',
      email: 'devops@gogidix.com',
      firstName: 'Lisa',
      lastName: 'Park',
      displayName: 'Lisa Park',
      role: 'DEVOPS_ENGINEER',
      department: 'system-administrator',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=LP',
      permissions: ROLE_PERMISSIONS.DEVOPS_ENGINEER,
      isActive: true,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    password: 'password123',
  },
  'operator@gogidix.com': {
    user: {
      id: 'usr-op-001',
      email: 'operator@gogidix.com',
      firstName: 'David',
      lastName: 'Brown',
      displayName: 'David Brown',
      role: 'OPERATOR',
      department: 'system-administrator',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=DB',
      permissions: ROLE_PERMISSIONS.OPERATOR,
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
            user: {
              ...mockUser.user,
              lastLogin: new Date().toISOString(),
            },
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

      hasPermission: (permission: Permission) => {
        const { user } = get()
        return user?.permissions.includes(permission) ?? false
      },

      hasAnyPermission: (permissions: Permission[]) => {
        const { user } = get()
        return permissions.some(p => user?.permissions.includes(p))
      },

      hasRole: (roles: AdminRole[]) => {
        const { user } = get()
        return user ? roles.includes(user.role) : false
      },

      isSuperAdmin: () => {
        const { user } = get()
        return user?.role === 'SUPER_ADMIN' || user?.role === 'SYSTEM_ADMINISTRATOR'
      },

      isSystemAdmin: () => {
        const { user } = get()
        return user?.role === 'SYSTEM_ADMINISTRATOR'
      },
    }),
    {
      name: 'admin-auth-storage',
      partialize: state => ({
        user: state.user,
        isAuthenticated: state.isAuthenticated,
      }),
    }
  )
)

// Export mock users for testing
export { MOCK_USERS }
