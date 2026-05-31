import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { useCfoStore } from '@shared/store'
import LoginPage from './pages/LoginPage'
import TwoFactorPage from './pages/TwoFactorPage'
import CFOLayout from './layouts/CFOLayout'
import StrategicDashboardPage from './pages/StrategicDashboardPage'
import ApprovalsPage from './pages/ApprovalsPage'
import StrategyPage from './pages/StrategyPage'
import RiskPage from './pages/RiskPage'
import InvestorPage from './pages/InvestorPage'
import SettingsPage from './pages/SettingsPage'

function ProtectedRoute({ children }: { children: React.ReactNode }) {
  const { isAuthenticated, is2FAVerified } = useCfoStore()
  if (!isAuthenticated) return <Navigate to="/login" replace />
  if (!is2FAVerified) return <Navigate to="/2fa" replace />
  return <>{children}</>
}

function TwoFARoute({ children }: { children: React.ReactNode }) {
  const { isAuthenticated, is2FAVerified } = useCfoStore()
  if (!isAuthenticated) return <Navigate to="/login" replace />
  if (is2FAVerified) return <Navigate to="/dashboard" replace />
  return <>{children}</>
}

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/2fa" element={<TwoFARoute><TwoFactorPage /></TwoFARoute>} />
        <Route
          path="/"
          element={
            <ProtectedRoute>
              <CFOLayout />
            </ProtectedRoute>
          }
        >
          <Route index element={<Navigate to="/dashboard" replace />} />
          <Route path="dashboard" element={<StrategicDashboardPage />} />
          <Route path="approvals" element={<ApprovalsPage />} />
          <Route path="financial-strategy" element={<StrategyPage />} />
          <Route path="risk-management" element={<RiskPage />} />
          <Route path="investor-relations" element={<InvestorPage />} />
          <Route path="settings" element={<SettingsPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}

export default App
