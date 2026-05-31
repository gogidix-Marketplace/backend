import { Routes, Route, Navigate } from 'react-router-dom'
import { useEffect } from 'react'
import { DashboardLayout } from '@shared/components/layout'
import LoginPage from '@pages/login'
import ForgotPasswordPage from '@pages/forgot-password'
import TwoFactorAuthPage from '@pages/two-factor-auth'
import { StaffActivationPage, OnboardingPage } from '@features/auth'
import OverviewPage from '@pages/overview'
import StrategyPage from '@pages/strategy'
import AnalyticsPage from '@pages/analytics'
import ApprovalsPage from '@pages/approvals'
import ReportsPage from '@pages/reports'
import SettingsPage from '@pages/settings'
import DomainOverviewPage from '@pages/domain-overview-page'
import {
  CFOOverviewPage,
  CFOBudgetPage,
  CFOFinancialsPage,
  CFOForecastPage,
  CFOCompliancePage,
  CFOReportsPage,
} from '@pages/index'
import COOOverviewPage from '@features/coo/pages/overview'
import COOOperationsPage from '@features/coo/pages/operations'
import COOIncidentsPage from '@features/coo/pages/incidents'
import COOResourcesPage from '@features/coo/pages/resources'
import COOReportsPage from '@features/coo/pages/reports'
import COOApprovalsPage from '@features/coo/pages/approvals'
import CTOOverviewPage from '@features/cto/pages/overview'
import CTOInfrastructurePage from '@features/cto/pages/infrastructure'
import CTOEngineeringPage from '@features/cto/pages/engineering'
import CTOSecurityPage from '@features/cto/pages/security'
import CTORoadmapPage from '@features/cto/pages/roadmap'
import CTOUsersPage from '@features/cto/pages/users'
import CTOReportsPage from '@features/cto/pages/reports'
import CTOApprovalsPage from '@features/cto/pages/approvals'
import DepartmentPage from '@pages/department-page'
import BusinessUnitPage from '@pages/business-unit-page'
import { useAuthStore } from '@shared/stores/authStore'

// Protected Route Wrapper
function ProtectedRoute({ children, expectedRole }: { children: React.ReactNode; expectedRole: string }) {
  const { isAuthenticated, user, _pendingAuth } = useAuthStore()
  if (!isAuthenticated || _pendingAuth) return <Navigate to="/login" replace />
  if (user && user.role !== expectedRole && user.role !== 'CEO' && expectedRole !== 'CEO') {
    return <Navigate to="/login" replace />
  }
  return <>{children}</>
}

/**
 * Executive Dashboard Routing
 * CEO: Port 3011 | CFO: Port 3012 | COO: Port 3013 | CTO: Port 3014
 */
export default function Routing() {
  // Detect role from port
  const port = window.location.port || import.meta.env.VITE_PORT || ''
  const isCFO = port === '3012'
  const isCOO = port === '3013'
  const isCTO = port === '3014'
  const isCEO = port === '3011' || port === '' || port === '5173'

  // Get current role
  const getRole = () => {
    if (isCFO) return 'CFO'
    if (isCOO) return 'COO'
    if (isCTO) return 'CTO'
    return 'CEO'
  }

  // Get redirect path
  const getRedirectPath = () => {
    if (isCFO) return '/cfo'
    if (isCOO) return '/coo'
    if (isCTO) return '/cto'
    return '/'
  }

  const role = getRole()

  // Set document title
  useEffect(() => {
    document.title = `${role} Dashboard | Gogidix Executive Suite`
  }, [role])

  return (
    <Routes>
      {/* Public Routes - Login, Forgot Password, 2FA, Staff Activation, Onboarding */}
      <Route path="/login" element={<LoginPage />} />
      <Route path="/forgot-password" element={<ForgotPasswordPage />} />
      <Route path="/two-factor" element={<TwoFactorAuthPage />} />
      <Route path="/onboarding" element={<OnboardingPage />} />
      <Route path="/activate" element={<StaffActivationPage />} />
      <Route path="/activate/:token" element={<StaffActivationPage />} />

      {/* CEO Dashboard Routes */}
      {isCEO && (
        <>
          <Route
            path="/"
            element={
              <ProtectedRoute expectedRole="CEO">
                <DashboardLayout role="CEO" />
              </ProtectedRoute>
            }
          >
            <Route index element={<OverviewPage />} />
            <Route path="strategy" element={<StrategyPage />} />
            <Route path="analytics" element={<AnalyticsPage />} />
            <Route path="approvals" element={<ApprovalsPage />} />
            <Route path="reports" element={<ReportsPage />} />
            <Route path="settings" element={<SettingsPage />} />
            <Route path="departments/:departmentSlug" element={<DepartmentPage />} />
            <Route path="business-units/:unitSlug" element={<BusinessUnitPage />} />
          </Route>

          <Route
            path="/domain/:domainId"
            element={
              <ProtectedRoute expectedRole="CEO">
                <DashboardLayout role="CEO" />
              </ProtectedRoute>
            }
          >
            <Route index element={<DomainOverviewPage />} />
          </Route>
        </>
      )}

      {/* CFO Dashboard Routes */}
      {isCFO && (
        <>
          <Route path="/" element={<Navigate to="/cfo" replace />} />
          <Route
            path="/cfo"
            element={
              <ProtectedRoute expectedRole="CFO">
                <DashboardLayout role="CFO" />
              </ProtectedRoute>
            }
          >
            <Route index element={<CFOOverviewPage />} />
            <Route path="budget" element={<CFOBudgetPage />} />
            <Route path="financials" element={<CFOFinancialsPage />} />
            <Route path="forecast" element={<CFOForecastPage />} />
            <Route path="compliance" element={<CFOCompliancePage />} />
            <Route path="reports" element={<CFOReportsPage />} />
            <Route path="settings" element={<SettingsPage />} />
            <Route path="departments/:departmentSlug" element={<DepartmentPage />} />
            <Route path="business-units/:unitSlug" element={<BusinessUnitPage />} />
            <Route path="service-health" element={<CTOOverviewPage />} />
          </Route>
        </>
      )}

      {/* COO Dashboard Routes */}
      {isCOO && (
        <>
          <Route path="/" element={<Navigate to="/coo" replace />} />
          <Route
            path="/coo"
            element={
              <ProtectedRoute expectedRole="COO">
                <DashboardLayout role="COO" />
              </ProtectedRoute>
            }
          >
            <Route index element={<COOOverviewPage />} />
            <Route path="operations" element={<COOOperationsPage />} />
            <Route path="incidents" element={<COOIncidentsPage />} />
            <Route path="resources" element={<COOResourcesPage />} />
            <Route path="reports" element={<COOReportsPage />} />
            <Route path="approvals" element={<COOApprovalsPage />} />
            <Route path="settings" element={<SettingsPage />} />
            <Route path="departments/:departmentSlug" element={<DepartmentPage />} />
            <Route path="business-units/:unitSlug" element={<BusinessUnitPage />} />
            <Route path="service-health" element={<CTOOverviewPage />} />
          </Route>
        </>
      )}

      {/* CTO Dashboard Routes */}
      {isCTO && (
        <>
          <Route path="/" element={<Navigate to="/cto" replace />} />
          <Route
            path="/cto"
            element={
              <ProtectedRoute expectedRole="CTO">
                <DashboardLayout role="CTO" />
              </ProtectedRoute>
            }
          >
            <Route index element={<CTOOverviewPage />} />
            <Route path="infrastructure" element={<CTOInfrastructurePage />} />
            <Route path="engineering" element={<CTOEngineeringPage />} />
            <Route path="security" element={<CTOSecurityPage />} />
            <Route path="roadmap" element={<CTORoadmapPage />} />
            <Route path="users" element={<CTOUsersPage />} />
            <Route path="reports" element={<CTOReportsPage />} />
            <Route path="approvals" element={<CTOApprovalsPage />} />
            <Route path="service-health" element={<CTOOverviewPage />} />
            <Route path="settings" element={<SettingsPage />} />
            <Route path="departments/:departmentSlug" element={<DepartmentPage />} />
            <Route path="business-units/:unitSlug" element={<BusinessUnitPage />} />
          </Route>
        </>
      )}

      {/* Fallback */}
      {isCEO && <Route path="*" element={<Navigate to="/" replace />} />}
      {isCFO && <Route path="*" element={<Navigate to="/cfo" replace />} />}
      {isCOO && <Route path="*" element={<Navigate to="/coo" replace />} />}
      {isCTO && <Route path="*" element={<Navigate to="/cto" replace />} />}
    </Routes>
  )
}
