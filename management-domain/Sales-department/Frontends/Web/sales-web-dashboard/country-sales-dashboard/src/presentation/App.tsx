import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { DashboardLayout } from './components/layouts'
import { CountryOverviewPage } from './pages/CountryOverviewPage'
import { SalesTeamsPage } from './pages/SalesTeamsPage'
import { PartnersPage } from './pages/PartnersPage'
import { PipelinePage } from './pages/PipelinePage'
import { CustomersPage } from './pages/CustomersPage'
import { PerformancePage } from './pages/PerformancePage'
import { ForecastingPage } from './pages/ForecastingPage'
import { ReportsPage } from './pages/ReportsPage'
import { SettingsPage } from './pages/SettingsPage'
import './styles/global/index.css'

// Create a client for TanStack Query
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: 1,
      staleTime: 5 * 60 * 1000, // 5 minutes
    },
  },
})

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <div className="app">
          <Routes>
            {/* Main app routes with layout */}
            <Route path="/" element={<DashboardLayout />}>
              <Route index element={<CountryOverviewPage />} />
              <Route path="teams" element={<SalesTeamsPage />} />
              <Route path="partners" element={<PartnersPage />} />
              <Route path="pipeline" element={<PipelinePage />} />
              <Route path="customers" element={<CustomersPage />} />
              <Route path="performance" element={<PerformancePage />} />
              <Route path="forecasting" element={<ForecastingPage />} />
              <Route path="reports" element={<ReportsPage />} />
              <Route path="settings" element={<SettingsPage />} />
              <Route path="settings/*" element={<SettingsPage />} />
              {/* Catch all - redirect to dashboard */}
              <Route path="*" element={<Navigate to="/" replace />} />
            </Route>

            {/* Standalone routes (login, etc.) */}
            <Route path="/login" element={
              <div className="login-page">
                <div className="login-container">
                  <h1>Country Sales Dashboard</h1>
                  <p>Please log in to continue</p>
                  <button className="btn btn-primary">Login</button>
                </div>
              </div>
            } />
          </Routes>
        </div>
      </BrowserRouter>
    </QueryClientProvider>
  )
}

export default App
