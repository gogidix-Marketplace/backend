import { useAuthStore } from '@/stores'
import type { Permission, UserRole } from '@/types'

export function useAuth() {
  const { user, isAuthenticated, login, logout, hasPermission, hasRole, hasDepartment } =
    useAuthStore()

  return {
    user,
    isAuthenticated,
    login,
    logout,
    hasPermission: (permission: Permission) => hasPermission(permission),
    hasRole: (role: UserRole) => hasRole(role),
    hasDepartment: hasDepartment,
  }
}

export function useRequireAuth(requirePermission?: Permission[]) {
  const { user, isAuthenticated, hasPermission } = useAuthStore()

  const hasRequiredPermissions = requirePermission
    ? requirePermission.every((p) => hasPermission(p))
    : true

  const isAuthorized = isAuthenticated && hasRequiredPermissions

  return {
    user,
    isAuthenticated,
    isAuthorized,
    hasRequiredPermissions,
  }
}
