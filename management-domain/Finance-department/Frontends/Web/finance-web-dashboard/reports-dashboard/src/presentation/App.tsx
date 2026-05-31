import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { useReportsStore } from '@shared/store/reportsStore'
import ReportsLayout from './layouts/ReportsLayout'
import LoginPage from './pages/LoginPage'
import TwoFactorPage from './pages/TwoFactorPage'
import DashboardPage from './pages/DashboardPage'
import TemplatesPage from './pages/TemplatesPage'
import ScheduledReportsPage from './pages/ScheduledReportsPage'
import GenerateReportPage from './pages/GenerateReportPage'
import HistoryPage from './pages/HistoryPage'
import SettingsPage from './pages/SettingsPage'

function ProtectedRoute({ children }: { children: React.ReactNode }) {
  const { isAuthenticated, is2FAVerified } = useReportsStore()
  if (!isAuthenticated) return <Navigate to="/login" replace />
  if (!is2FAVerified) return <Navigate to="/2fa" replace />
  return <>{children}</>
}

function TwoFARoute({ children }: { children: React.ReactNode }) {
  const { isAuthenticated, is2FAVerified } = useReportsStore()
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
          element={
            <ProtectedRoute>
              <ReportsLayout />
            </ProtectedRoute>
          }
        >
          <Route path="/dashboard" element={<DashboardPage />} />
          <Route path="/templates" element={<TemplatesPage />} />
          <Route path="/scheduled" element={<ScheduledReportsPage />} />
          <Route path="/generate" element={<GenerateReportPage />} />
          <Route path="/history" element={<HistoryPage />} />
          <Route path="/settings" element={<SettingsPage />} />
        </Route>
        <Route path="*" element={<Navigate to="/dashboard" replace />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
