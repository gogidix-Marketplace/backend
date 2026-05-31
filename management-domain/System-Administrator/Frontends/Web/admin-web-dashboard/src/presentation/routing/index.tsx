import { Routes, Route, Navigate } from 'react-router-dom'
import { DashboardLayout } from '@shared/components/layout'
import LoginPage from '@pages/login'
import OverviewPage from '@pages/overview'
import AccessControlPage from '@pages/access-control'
import AccessRequestsPage from '@pages/access-requests'
import IncidentsPage from '@pages/incidents'
import InfrastructurePage from '@pages/infrastructure'
import SecurityPage from '@pages/security'
import DeploymentsPage from '@pages/deployments'
import CompliancePage from '@pages/compliance'
import ProvisioningPage from '@pages/provisioning'
import SettingsPage from '@pages/settings'
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
        <Route path="access-control" element={<AccessControlPage />} />
        <Route path="access-requests" element={<AccessRequestsPage />} />
        <Route path="incidents" element={<IncidentsPage />} />
        <Route path="infrastructure" element={<InfrastructurePage />} />
        <Route path="security" element={<SecurityPage />} />
        <Route path="deployments" element={<DeploymentsPage />} />
        <Route path="compliance" element={<CompliancePage />} />
        <Route path="provisioning" element={<ProvisioningPage />} />
        <Route path="settings" element={<SettingsPage />} />
      </Route>

      {/* Catch all - redirect to dashboard */}
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}
