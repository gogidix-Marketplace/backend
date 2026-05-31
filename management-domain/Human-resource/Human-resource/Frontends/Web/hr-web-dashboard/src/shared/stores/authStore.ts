import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import type { User, HRRole, CountryCode } from '@shared/types'

interface AuthState {
  user: User | null
  isAuthenticated: boolean
  isLoading: boolean
  selectedCountry: CountryCode | 'ALL'
  _pendingAuth: boolean
  login: (email: string, password: string) => Promise<void>
  complete2FA: () => void
  logout: () => void
  setUser: (user: User | null) => void
  setSelectedCountry: (country: CountryCode | 'ALL') => void
  hasPermission: (permission: string) => boolean
  hasRole: (roles: HRRole[]) => boolean
  canAccessCountry: (country: CountryCode) => boolean
}

// Role permissions mapping
const ROLE_PERMISSIONS: Record<HRRole, string[]> = {
  CHRO: [
    // Full access to everything
    'view:all',
    'view:global',
    'view:country',
    'manage:employees',
    'manage:payroll',
    'manage:recruitment',
    'manage:performance',
    'manage:training',
    'manage:benefits',
    'manage:compliance',
    'approve:leaves',
    'approve:expenses',
    'approve:promotions',
    'approve: salary_changes',
    'create:reports',
    'export:data',
    'manage:users',
    'manage:settings',
    'view:audit_logs',
  ],
  HR_DIRECTOR: [
    // Department level access
    'view:all',
    'view:department',
    'manage:employees',
    'manage:payroll',
    'manage:recruitment',
    'manage:performance',
    'manage:training',
    'manage:benefits',
    'manage:compliance',
    'approve:leaves',
    'approve:expenses',
    'approve:promotions',
    'create:reports',
    'export:data',
  ],
  HR_MANAGER: [
    // Manager level access
    'view:department',
    'view:team',
    'manage:employees',
    'manage:recruitment',
    'manage:performance',
    'manage:training',
    'approve:leaves',
    'approve:expenses',
    'create:reports',
  ],
  RECRUITER: [
    // Recruitment focused
    'view:all',
    'manage:recruitment',
    'view:candidate_pipeline',
    'create:job_requisition',
    'schedule:interviews',
    'make_offers',
    'create:reports',
  ],
  PAYROLL_SPECIALIST: [
    // Payroll focused
    'view:all',
    'manage:payroll',
    'manage:benefits',
    'view:compensation',
    'process:payroll',
    'generate:payslips',
    'create:reports',
  ],
  HR_GENERALIST: [
    // General HR operations
    'view:department',
    'view:team',
    'manage:employees',
    'manage:leaves',
    'manage:attendance',
    'answer:queries',
    'create:reports',
  ],
  TRAINING_MANAGER: [
    // Training focused
    'view:all',
    'manage:training',
    'create:programs',
    'assign:training',
    'track:progress',
    'create:reports',
  ],
  COMPLIANCE_OFFICER: [
    // Compliance focused
    'view:all',
    'manage:compliance',
    'view:policies',
    'create:audits',
    'track:certifications',
    'create:reports',
    'view:audit_logs',
  ],
  EMPLOYEE: [
    // Self-service only
    'view:own_profile',
    'view:own_payroll',
    'view:own_benefits',
    'request:leave',
    'view:own_performance',
    'enroll:training',
    'update:own_info',
  ],
}

// Mock users for HR Dashboard
const MOCK_USERS: Record<string, { user: User; password: string }> = {
  'chro@gogidix.com': {
    user: {
      id: 'usr-chro-001',
      email: 'chro@gogidix.com',
      firstName: 'Amanda',
      lastName: 'Sterling',
      displayName: 'Amanda Sterling',
      role: 'CHRO',
      department: 'human-resources',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=AS',
      permissions: ROLE_PERMISSIONS.CHRO,
      employeeId: 'EMP-CHRO-001',
      isActive: true,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    password: 'password123',
  },
  'hr-director@gogidix.com': {
    user: {
      id: 'usr-hr-dir-001',
      email: 'hr-director@gogidix.com',
      firstName: 'Robert',
      lastName: 'Chen',
      displayName: 'Robert Chen',
      role: 'HR_DIRECTOR',
      department: 'human-resources',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=RC',
      permissions: ROLE_PERMISSIONS.HR_DIRECTOR,
      employeeId: 'EMP-HR-DIR-001',
      isActive: true,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    password: 'password123',
  },
  'hr-manager@gogidix.com': {
    user: {
      id: 'usr-hr-mgr-001',
      email: 'hr-manager@gogidix.com',
      firstName: 'Sarah',
      lastName: 'Johnson',
      displayName: 'Sarah Johnson',
      role: 'HR_MANAGER',
      department: 'human-resources',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=SJ',
      permissions: ROLE_PERMISSIONS.HR_MANAGER,
      employeeId: 'EMP-HR-MGR-001',
      country: 'NG',
      isActive: true,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    password: 'password123',
  },
  'recruiter@gogidix.com': {
    user: {
      id: 'usr-rec-001',
      email: 'recruiter@gogidix.com',
      firstName: 'Michael',
      lastName: 'Brown',
      displayName: 'Michael Brown',
      role: 'RECRUITER',
      department: 'human-resources',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=MB',
      permissions: ROLE_PERMISSIONS.RECRUITER,
      employeeId: 'EMP-REC-001',
      isActive: true,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    password: 'password123',
  },
  'payroll@gogidix.com': {
    user: {
      id: 'usr-pay-001',
      email: 'payroll@gogidix.com',
      firstName: 'Emily',
      lastName: 'Davis',
      displayName: 'Emily Davis',
      role: 'PAYROLL_SPECIALIST',
      department: 'human-resources',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=ED',
      permissions: ROLE_PERMISSIONS.PAYROLL_SPECIALIST,
      employeeId: 'EMP-PAY-001',
      isActive: true,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    password: 'password123',
  },
  'employee@gogidix.com': {
    user: {
      id: 'usr-emp-001',
      email: 'employee@gogidix.com',
      firstName: 'John',
      lastName: 'Smith',
      displayName: 'John Smith',
      role: 'EMPLOYEE',
      department: 'engineering',
      avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=JS',
      permissions: ROLE_PERMISSIONS.EMPLOYEE,
      employeeId: 'EMP-001',
      country: 'NG',
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
      selectedCountry: 'ALL',
      _pendingAuth: false,

      login: async (email: string, password: string) => {
        set({ isLoading: true })
        await new Promise(resolve => setTimeout(resolve, 500))

        const mockUser = MOCK_USERS[email]

        if (mockUser && mockUser.password === password) {
          set({
            user: mockUser.user,
            _pendingAuth: true,
            isLoading: false,
          })
        } else {
          set({ isLoading: false })
          throw new Error('Invalid credentials')
        }
      },

      complete2FA: () => {
        set({ _pendingAuth: false, isAuthenticated: true })
      },

      logout: () => {
        set({ user: null, isAuthenticated: false, _pendingAuth: false })
      },

      setUser: (user: User | null) => {
        set({ user, isAuthenticated: !!user })
      },

      setSelectedCountry: (country: CountryCode | 'ALL') => {
        set({ selectedCountry: country })
      },

      hasPermission: (permission: string) => {
        const { user } = get()
        return user?.permissions.includes(permission) ?? false
      },

      hasRole: (roles: HRRole[]) => {
        const { user } = get()
        return user ? roles.includes(user.role) : false
      },

      canAccessCountry: (country: CountryCode) => {
        const { user } = get()
        // CHRO and HR_DIRECTOR can access all countries
        if (user?.role === 'CHRO' || user?.role === 'HR_DIRECTOR') {
          return true
        }
        // Other roles can only access their assigned country
        return user?.country === country || user?.country === undefined
      },
    }),
    {
      name: 'hr-auth-storage',
      partialize: state => ({
        user: state.user,
        isAuthenticated: state.isAuthenticated,
        selectedCountry: state.selectedCountry,
        _pendingAuth: state._pendingAuth,
      }),
    }
  )
)

// Export mock users for testing
export { MOCK_USERS, ROLE_PERMISSIONS }
