import { Routes, Route, Navigate } from 'react-router-dom'
import { DashboardLayout } from '@shared/components/layout'
import {
  LoginPage,
  OverviewPage,
  LeadsPage,
  OpportunitiesPage,
  CustomersPage,
  AnalyticsPage,
  TerritoriesPage,
  CommissionPage,
  ForecastPage,
  CommunicationsPage,
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
        <Route path="leads" element={<LeadsPage />} />
        <Route path="opportunities" element={<OpportunitiesPage />} />
        <Route path="customers" element={<CustomersPage />} />
        <Route path="analytics" element={<AnalyticsPage />} />
        <Route path="territories" element={<TerritoriesPage />} />
        <Route path="commission" element={<CommissionPage />} />
        <Route path="forecast" element={<ForecastPage />} />
        <Route path="communications" element={<CommunicationsPage />} />
        <Route path="settings" element={<SettingsPage />} />
      </Route>

      {/* Catch all - redirect to dashboard */}
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}
