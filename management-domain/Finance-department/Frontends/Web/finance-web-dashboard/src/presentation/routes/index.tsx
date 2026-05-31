import { createBrowserRouter, Navigate } from 'react-router-dom'
import { FinanceLayout } from '@layouts'
import { LoginPage } from '@pages/auth/LoginPage'
import { TwoFactorPage } from '@pages/auth/TwoFactorPage'
import { FinanceDashboardPage } from '@pages/dashboard/FinanceDashboardPage'
import { CountriesPage } from '@pages/countries/CountriesPage'
import { RevenuePage } from '@pages/revenue/RevenuePage'
import { ExpensesPage } from '@pages/expenses/ExpensesPage'
import { BudgetsPage } from '@pages/budgets/BudgetsPage'
import { ReportsPage } from '@pages/reports/ReportsPage'
import { TreasuryPage } from '@pages/treasury/TreasuryPage'
import { TaxCompliancePage } from '@pages/tax/TaxCompliancePage'
import { SettingsPage } from '@pages/settings/SettingsPage'
import { useAuthStore } from '@store'

const ProtectedRoute = ({ children }: { children: React.ReactNode }) => {
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated)
  const is2FAVerified = useAuthStore((state) => state.is2FAVerified)

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />
  }

  if (!is2FAVerified) {
    return <Navigate to="/2fa" replace />
  }

  return <>{children}</>
}

const TwoFARoute = ({ children }: { children: React.ReactNode }) => {
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated)
  const is2FAVerified = useAuthStore((state) => state.is2FAVerified)

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />
  }

  if (is2FAVerified) {
    return <Navigate to="/dashboard" replace />
  }

  return <>{children}</>
}

const PublicRoute = ({ children }: { children: React.ReactNode }) => {
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated)
  const is2FAVerified = useAuthStore((state) => state.is2FAVerified)

  if (isAuthenticated && is2FAVerified) {
    return <Navigate to="/dashboard" replace />
  }

  return <>{children}</>
}

export const router = createBrowserRouter([
  {
    path: '/login',
    element: (
      <PublicRoute>
        <LoginPage />
      </PublicRoute>
    ),
  },
  {
    path: '/2fa',
    element: (
      <TwoFARoute>
        <TwoFactorPage />
      </TwoFARoute>
    ),
  },
  {
    path: '/',
    element: (
      <ProtectedRoute>
        <FinanceLayout />
      </ProtectedRoute>
    ),
    children: [
      {
        index: true,
        element: <Navigate to="/dashboard" replace />,
      },
      {
        path: 'dashboard',
        element: <FinanceDashboardPage />,
      },
      {
        path: 'countries',
        element: <CountriesPage />,
      },
      {
        path: 'revenue',
        element: <RevenuePage />,
      },
      {
        path: 'expenses',
        element: <ExpensesPage />,
      },
      {
        path: 'budgets',
        element: <BudgetsPage />,
      },
      {
        path: 'reports',
        element: <ReportsPage />,
      },
      {
        path: 'treasury',
        element: <TreasuryPage />,
      },
      {
        path: 'tax-compliance',
        element: <TaxCompliancePage />,
      },
      {
        path: 'settings',
        element: <SettingsPage />,
      },
    ],
  },
  {
    path: '*',
    element: <Navigate to="/" replace />,
  },
])
