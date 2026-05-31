import { Routes, Route, Navigate } from 'react-router-dom'
import { PortalLayout } from './layouts/PortalLayout'
import { LoginPage } from './pages/LoginPage'
import { TwoFactorPage } from './pages/TwoFactorPage'
import { PortalHomePage } from './pages/PortalHomePage'
import { MyExpensesPage } from './pages/MyExpensesPage'
import { ExpenseSubmitPage } from './pages/ExpenseSubmitPage'
import { useAuthStore } from '@shared/store/portalStore'
import { InvoicesPage } from './pages/InvoicesPage'
import { PaymentStatusPage } from './pages/PaymentStatusPage'
import { ProfilePage } from './pages/ProfilePage'

function ProtectedRoute({ children }: { children: React.ReactNode }) {
  const isAuthenticated = useAuthStore((s) => s.isAuthenticated)
  const is2FAVerified = useAuthStore((s) => s.is2FAVerified)
  if (!isAuthenticated) return <Navigate to="/login" replace />
  if (!is2FAVerified) return <Navigate to="/2fa" replace />
  return <>{children}</>
}

function PublicRoute({ children }: { children: React.ReactNode }) {
  const isAuthenticated = useAuthStore((s) => s.isAuthenticated)
  const is2FAVerified = useAuthStore((s) => s.is2FAVerified)
  if (isAuthenticated && is2FAVerified) return <Navigate to="/dashboard" replace />
  return <>{children}</>
}

function TwoFARoute({ children }: { children: React.ReactNode }) {
  const isAuthenticated = useAuthStore((s) => s.isAuthenticated)
  const is2FAVerified = useAuthStore((s) => s.is2FAVerified)
  if (!isAuthenticated) return <Navigate to="/login" replace />
  if (is2FAVerified) return <Navigate to="/dashboard" replace />
  return <>{children}</>
}

export function App() {
  return (
    <div className="app">
      <Routes>
        <Route path="/login" element={<PublicRoute><LoginPage /></PublicRoute>} />
        <Route path="/2fa" element={<TwoFARoute><TwoFactorPage /></TwoFARoute>} />
        <Route path="/" element={<ProtectedRoute><PortalLayout /></ProtectedRoute>}>
          <Route index element={<Navigate to="/dashboard" replace />} />
          <Route path="dashboard" element={<PortalHomePage />} />
          <Route path="my-expenses" element={<MyExpensesPage />} />
          <Route path="submit" element={<ExpenseSubmitPage />} />
          <Route path="my-invoices" element={<InvoicesPage />} />
          <Route path="payment-status" element={<PaymentStatusPage />} />
          <Route path="profile" element={<ProfilePage />} />
        </Route>
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </div>
  )
}
