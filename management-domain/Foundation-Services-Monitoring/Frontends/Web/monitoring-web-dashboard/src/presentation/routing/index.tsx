import { Routes, Route, Navigate } from 'react-router-dom'
import { DashboardLayout } from '@shared/components/layout'
import {
  LoginPage,
  OverviewPage,
  ServicesPage,
  ServiceDetailPage,
  AlertsPage,
  PerformancePage,
  DependenciesPage,
  SettingsPage,
} from '@pages'
import { useAuthStore } from '@shared/stores/authStore'

// Protected Route Wrapper
function ProtectedRoute({ children }: { children: React.ReactNode }) {
  const { isAuthenticated } = useAuthStore()
  return isAuthenticated ? <>{children}</> : <Navigate to="/login" replace />
}

export default function Routing() {
  return (
    <Routes>
      {/* Public Routes */}
      <Route path="/login" element={<LoginPage />} />

      {/* Protected Routes */}
      <Route
        path="/"
        element={
          <ProtectedRoute>
            <DashboardLayout />
          </ProtectedRoute>
        }
      >
        <Route index element={<OverviewPage />} />
        <Route path="services" element={<ServicesPage />} />
        <Route path="services/:id" element={<ServiceDetailPage />} />
        <Route path="alerts" element={<AlertsPage />} />
        <Route path="performance" element={<PerformancePage />} />
        <Route path="dependencies" element={<DependenciesPage />} />
        <Route path="settings" element={<SettingsPage />} />
      </Route>

      {/* Catch all - redirect to dashboard */}
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}
