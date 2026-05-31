import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import type { User, UserRole, Department, Permission } from '@/types/common'

interface AuthState {
  user: User | null
  isAuthenticated: boolean
  login: (email: string, password: string) => Promise<void>
  logout: () => void
  hasPermission: (permission: Permission) => boolean
  hasRole: (role: UserRole) => boolean
  hasDepartment: (department: Department) => boolean
}

// Mock users for each department
const MOCK_USERS: Record<string, User> = {
  // Executive Domain
  'ceo@gogidix.com': {
    id: 'usr-exec-001',
    email: 'ceo@gogidix.com',
    firstName: 'John',
    lastName: 'Anderson',
    displayName: 'John Anderson',
    role: 'CEO',
    department: 'executive',
    avatar: '',
    permissions: [
      'view:all',
      'view:department',
      'view:country',
      'approve:budget',
      'approve:hiring',
      'approve:initiative',
      'create:kpi',
      'edit:kpi',
      'delete:kpi',
      'view:reports',
      'create:reports',
      'manage:users',
      'manage:settings',
      'view:audit_logs',
    ],
    country: 'US',
    isActive: true,
    createdAt: '2024-01-01T00:00:00Z',
    updatedAt: '2024-01-01T00:00:00Z',
  },
  'cfo@gogidix.com': {
    id: 'usr-exec-002',
    email: 'cfo@gogidix.com',
    firstName: 'Sarah',
    lastName: 'Mitchell',
    displayName: 'Sarah Mitchell',
    role: 'CFO',
    department: 'executive',
    avatar: '',
    permissions: [
      'view:all',
      'view:department',
      'view:country',
      'approve:budget',
      'view:reports',
      'create:reports',
      'manage:settings',
    ],
    country: 'US',
    isActive: true,
    createdAt: '2024-01-01T00:00:00Z',
    updatedAt: '2024-01-01T00:00:00Z',
  },
  'coo@gogidix.com': {
    id: 'usr-exec-003',
    email: 'coo@gogidix.com',
    firstName: 'Michael',
    lastName: 'Chen',
    displayName: 'Michael Chen',
    role: 'COO',
    department: 'executive',
    avatar: '',
    permissions: [
      'view:all',
      'view:department',
      'view:country',
      'approve:initiative',
      'view:reports',
      'manage:settings',
    ],
    country: 'US',
    isActive: true,
    createdAt: '2024-01-01T00:00:00Z',
    updatedAt: '2024-01-01T00:00:00Z',
  },
  'cto@gogidix.com': {
    id: 'usr-exec-004',
    email: 'cto@gogidix.com',
    firstName: 'Emily',
    lastName: 'Rodriguez',
    displayName: 'Emily Rodriguez',
    role: 'CTO',
    department: 'executive',
    avatar: '',
    permissions: [
      'view:all',
      'view:department',
      'view:country',
      'manage:users',
      'manage:settings',
      'view:audit_logs',
    ],
    country: 'US',
    isActive: true,
    createdAt: '2024-01-01T00:00:00Z',
    updatedAt: '2024-01-01T00:00:00Z',
  },

  // Finance Department
  'accountant@gogidix.com': {
    id: 'usr-fin-001',
    email: 'accountant@gogidix.com',
    firstName: 'David',
    lastName: 'Wilson',
    displayName: 'David Wilson',
    role: 'EMPLOYEE',
    department: 'finance',
    avatar: '',
    permissions: [
      'view:department',
      'view:reports',
    ],
    country: 'NG',
    isActive: true,
    createdAt: '2024-01-01T00:00:00Z',
    updatedAt: '2024-01-01T00:00:00Z',
  },
  'finance-manager@gogidix.com': {
    id: 'usr-fin-002',
    email: 'finance-manager@gogidix.com',
    firstName: 'Amanda',
    lastName: 'Peters',
    displayName: 'Amanda Peters',
    role: 'MANAGER',
    department: 'finance',
    avatar: '',
    permissions: [
      'view:department',
      'view:country',
      'approve:budget',
      'view:reports',
      'create:reports',
    ],
    country: 'GB',
    isActive: true,
    createdAt: '2024-01-01T00:00:00Z',
    updatedAt: '2024-01-01T00:00:00Z',
  },

  // HR Department
  'hr-manager@gogidix.com': {
    id: 'usr-hr-001',
    email: 'hr-manager@gogidix.com',
    firstName: 'Jennifer',
    lastName: 'Lee',
    displayName: 'Jennifer Lee',
    role: 'MANAGER',
    department: 'human-resource',
    avatar: '',
    permissions: [
      'view:department',
      'view:country',
      'approve:hiring',
      'view:reports',
      'manage:users',
    ],
    country: 'KE',
    isActive: true,
    createdAt: '2024-01-01T00:00:00Z',
    updatedAt: '2024-01-01T00:00:00Z',
  },
  'employee@gogidix.com': {
    id: 'usr-hr-002',
    email: 'employee@gogidix.com',
    firstName: 'Robert',
    lastName: 'Taylor',
    displayName: 'Robert Taylor',
    role: 'EMPLOYEE',
    department: 'human-resource',
    avatar: '',
    permissions: [
      'view:department',
    ],
    country: 'ZA',
    isActive: true,
    createdAt: '2024-01-01T00:00:00Z',
    updatedAt: '2024-01-01T00:00:00Z',
  },

  // Sales Department
  'sales-director@gogidix.com': {
    id: 'usr-sales-001',
    email: 'sales-director@gogidix.com',
    firstName: 'Christopher',
    lastName: 'Brown',
    displayName: 'Christopher Brown',
    role: 'DIRECTOR',
    department: 'sales',
    avatar: '',
    permissions: [
      'view:all',
      'view:country',
      'view:reports',
      'create:reports',
    ],
    country: 'IE',
    isActive: true,
    createdAt: '2024-01-01T00:00:00Z',
    updatedAt: '2024-01-01T00:00:00Z',
  },
  'sales-rep@gogidix.com': {
    id: 'usr-sales-002',
    email: 'sales-rep@gogidix.com',
    firstName: 'Lisa',
    lastName: 'Garcia',
    displayName: 'Lisa Garcia',
    role: 'EMPLOYEE',
    department: 'sales',
    avatar: '',
    permissions: [
      'view:department',
    ],
    country: 'NG',
    isActive: true,
    createdAt: '2024-01-01T00:00:00Z',
    updatedAt: '2024-01-01T00:00:00Z',
  },

  // Customer Support
  'support-lead@gogidix.com': {
    id: 'usr-support-001',
    email: 'support-lead@gogidix.com',
    firstName: 'Michelle',
    lastName: 'Davis',
    displayName: 'Michelle Davis',
    role: 'SUPERVISOR',
    department: 'customer-support',
    avatar: '',
    permissions: [
      'view:department',
      'view:country',
      'view:reports',
    ],
    country: 'GH',
    isActive: true,
    createdAt: '2024-01-01T00:00:00Z',
    updatedAt: '2024-01-01T00:00:00Z',
  },

  // System Administrator
  'admin@gogidix.com': {
    id: 'usr-admin-001',
    email: 'admin@gogidix.com',
    firstName: 'System',
    lastName: 'Administrator',
    displayName: 'System Admin',
    role: 'SUPER_ADMIN',
    department: 'system-administrator',
    avatar: '',
    permissions: [
      'view:all',
      'manage:users',
      'manage:settings',
      'view:audit_logs',
    ],
    country: 'US',
    isActive: true,
    createdAt: '2024-01-01T00:00:00Z',
    updatedAt: '2024-01-01T00:00:00Z',
  },

  // Global Business Management
  'gbm-analyst@gogidix.com': {
    id: 'usr-gbm-001',
    email: 'gbm-analyst@gogidix.com',
    firstName: 'Thomas',
    lastName: 'Miller',
    displayName: 'Thomas Miller',
    role: 'MANAGER',
    department: 'global-business-management',
    avatar: '',
    permissions: [
      'view:all',
      'view:reports',
      'create:reports',
    ],
    country: 'US',
    isActive: true,
    createdAt: '2024-01-01T00:00:00Z',
    updatedAt: '2024-01-01T00:00:00Z',
  },
}

// Default password for all mock users
const MOCK_PASSWORD = 'password123'

export const useAuthStore = create<AuthState>()(
  persist(
    (set, get) => ({
      user: null,
      isAuthenticated: false,

      login: async (email: string, password: string) => {
        // Simulate API call delay
        await new Promise((resolve) => setTimeout(resolve, 500))

        const mockUser = MOCK_USERS[email.toLowerCase()]

        if (!mockUser) {
          throw new Error('Invalid credentials')
        }

        if (password !== MOCK_PASSWORD) {
          throw new Error('Invalid credentials')
        }

        if (!mockUser.isActive) {
          throw new Error('Account is inactive')
        }

        set({ user: mockUser, isAuthenticated: true })
      },

      logout: () => {
        set({ user: null, isAuthenticated: false })
      },

      hasPermission: (permission: Permission) => {
        const { user } = get()
        return user?.permissions.includes(permission) ?? false
      },

      hasRole: (role: UserRole) => {
        const { user } = get()
        return user?.role === role
      },

      hasDepartment: (department: Department) => {
        const { user } = get()
        return user?.department === department
      },
    }),
    {
      name: 'gogidix-auth-storage',
    }
  )
)

// Export mock users for testing purposes
export { MOCK_USERS, MOCK_PASSWORD }
