import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { useAccountantStore } from '@shared/store'
import AccountantLayout from './layouts/AccountantLayout'
import LoginPage from './pages/LoginPage'
import TwoFactorPage from './pages/TwoFactorPage'
import DashboardPage from './pages/DashboardPage'
import JournalEntriesPage from './pages/JournalEntriesPage'
import ReconciliationPage from './pages/ReconciliationPage'
import InvoicesPage from './pages/InvoicesPage'
import PaymentsPage from './pages/PaymentsPage'
import VendorsPage from './pages/VendorsPage'
import SettingsPage from './pages/SettingsPage'

function ProtectedRoute({ children }: { children: React.ReactNode }) {
  const isAuthenticated = useAccountantStore((s) => s.isAuthenticated)
  const is2FAVerified = useAccountantStore((s) => s.is2FAVerified)
  if (!isAuthenticated) return <Navigate to="/login" replace />
  if (!is2FAVerified) return <Navigate to="/2fa" replace />
  return <>{children}</>
}

function TwoFARoute({ children }: { children: React.ReactNode }) {
  const isAuthenticated = useAccountantStore((s) => s.isAuthenticated)
  const is2FAVerified = useAccountantStore((s) => s.is2FAVerified)
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
              <AccountantLayout />
            </ProtectedRoute>
          }
        >
          <Route index element={<Navigate to="/dashboard" replace />} />
          <Route path="dashboard" element={<DashboardPage />} />
          <Route path="journal-entries" element={<JournalEntriesPage />} />
          <Route path="reconciliation" element={<ReconciliationPage />} />
          <Route path="invoices" element={<InvoicesPage />} />
          <Route path="payments" element={<PaymentsPage />} />
          <Route path="vendors" element={<VendorsPage />} />
          <Route path="settings" element={<SettingsPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}

export default App
