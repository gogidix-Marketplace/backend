import { Routes, Route, Navigate } from 'react-router-dom'
import { useAuthStore } from '@shared/stores/authStore'
import { DashboardLayout } from '@shared/components/layout'
import LoginPage from './pages/login'
import TwoFactorAuthPage from './pages/two-factor-auth'
import OverviewPage from './pages/overview'
import EmployeesPage from './pages/employees'
import LeavePage from './pages/leave'
import PayrollPage from './pages/payroll'
import RecruitmentPage from './pages/recruitment'
import OnboardingPage from './pages/onboarding'
import PerformancePage from './pages/performance'
import TrainingPage from './pages/training'
import BenefitsPage from './pages/benefits'
import AttendancePage from './pages/attendance'
import CompliancePage from './pages/compliance'
import ApprovalsPage from './pages/approvals'
import SettingsPage from './pages/settings'

function ProtectedRoute({ children }: { children: React.ReactNode }) {
  const { isAuthenticated, _pendingAuth } = useAuthStore()
  if (_pendingAuth) return <Navigate to="/2fa" replace />
  if (!isAuthenticated) return <Navigate to="/login" replace />
  return <>{children}</>
}

function App() {
  const { _pendingAuth } = useAuthStore()

  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/2fa" element={_pendingAuth ? <TwoFactorAuthPage /> : <Navigate to="/" replace />} />
      <Route
        element={
          <ProtectedRoute>
            <DashboardLayout />
          </ProtectedRoute>
        }
      >
        <Route path="/" element={<OverviewPage />} />
        <Route path="/employees" element={<EmployeesPage />} />
        <Route path="/leave" element={<LeavePage />} />
        <Route path="/payroll" element={<PayrollPage />} />
        <Route path="/recruitment" element={<RecruitmentPage />} />
        <Route path="/onboarding" element={<OnboardingPage />} />
        <Route path="/performance" element={<PerformancePage />} />
        <Route path="/training" element={<TrainingPage />} />
        <Route path="/benefits" element={<BenefitsPage />} />
        <Route path="/attendance" element={<AttendancePage />} />
        <Route path="/compliance" element={<CompliancePage />} />
        <Route path="/approvals" element={<ApprovalsPage />} />
        <Route path="/settings" element={<SettingsPage />} />
      </Route>
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}

export default App
