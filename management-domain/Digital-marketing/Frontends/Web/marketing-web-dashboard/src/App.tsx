// ============================================
// DIGITAL MARKETING - APP COMPONENT
// ============================================

import { Routes, Route, Navigate } from 'react-router-dom'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { MarketingLayout } from '@layouts/MarketingLayout'
import { MarketingLoginPage } from '@pages/auth/LoginPage'
import { MarketingDashboardPage } from '@pages/dashboard/MarketingDashboardPage'

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 5 * 60 * 1000,
      retry: 1,
    },
  },
})

function ProtectedRoute({ children }: { children: React.ReactNode }) {
  const isAuthenticated = localStorage.getItem('gogidix-marketing-auth-storage')?.includes('"isAuthenticated":true')

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />
  }

  return <>{children}</>
}

function PublicRoute({ children }: { children: React.ReactNode }) {
  const isAuthenticated = localStorage.getItem('gogidix-marketing-auth-storage')?.includes('"isAuthenticated":true')

  if (isAuthenticated) {
    return <Navigate to="/dashboard" replace />
  }

  return <>{children}</>
}

export function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <Routes>
        <Route
          path="/login"
          element={
            <PublicRoute>
              <MarketingLoginPage />
            </PublicRoute>
          }
        />
        <Route
          path="/"
          element={
            <ProtectedRoute>
              <MarketingLayout />
            </ProtectedRoute>
          }
        >
          <Route index element={<Navigate to="/dashboard" replace />} />
          <Route path="dashboard" element={<MarketingDashboardPage />} />
          <Route path="campaigns" element={<div className="p-6"><h1 className="text-2xl font-bold">Campaigns</h1><p className="text-slate-500 mt-2">Campaign management page coming soon</p></div>} />
          <Route path="content" element={<div className="p-6"><h1 className="text-2xl font-bold">Content</h1><p className="text-slate-500 mt-2">Content management page coming soon</p></div>} />
          <Route path="social" element={<div className="p-6"><h1 className="text-2xl font-bold">Social Media</h1><p className="text-slate-500 mt-2">Social media management page coming soon</p></div>} />
          <Route path="analytics" element={<div className="p-6"><h1 className="text-2xl font-bold">Analytics</h1><p className="text-slate-500 mt-2">Analytics page coming soon</p></div>} />
          <Route path="settings" element={<div className="p-6"><h1 className="text-2xl font-bold">Settings</h1><p className="text-slate-500 mt-2">Settings page coming soon</p></div>} />
        </Route>
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </QueryClientProvider>
  )
}
