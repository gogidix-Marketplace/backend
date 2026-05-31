// App Component
// Main application with routing and TanStack Query setup

import { BrowserRouter, Routes, Route, Navigate, useLocation } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import { useEffect } from 'react';
import { useAuthStore } from '@infrastructure';
import { MainLayout } from './components/layouts/MainLayout';
import { AuthLayout } from './components/layouts/AuthLayout';
import { LoginPage } from './pages/LoginPage';
import { GlobalOverviewPage } from './pages/GlobalOverviewPage';
import { CountriesPage, CountriesOverview, CountryDetail } from './pages/CountriesPage';
import { TeamsPage, TeamsOverview, TeamMemberDetail } from './pages/TeamsPage';
import { PartnersPage } from './pages/PartnersPage';
import { PipelinePage } from './pages/PipelinePage';
import { CustomersPage } from './pages/CustomersPage';
import { PerformancePage } from './pages/PerformancePage';
import { TerritoriesPage } from './pages/TerritoriesPage';
import { ForecastingPage } from './pages/ForecastingPage';
import { ReportsPage } from './pages/ReportsPage';
import { SettingsPage } from './pages/SettingsPage';
import './App.css';

// Create React Query client
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: 1,
      staleTime: 5 * 60 * 1000, // 5 minutes
    },
  },
});

// Protected Route Component
function ProtectedRoute({ children }: { children: React.ReactNode }) {
  const { isAuthenticated, isLoading, loadUser } = useAuthStore();
  const location = useLocation();

  useEffect(() => {
    loadUser();
  }, []);

  if (isLoading) {
    return (
      <div className="loading-screen">
        <div className="loading-spinner"></div>
        <p>Loading...</p>
      </div>
    );
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  return <>{children}</>;
}

// Public Route (redirect to dashboard if authenticated)
function PublicRoute({ children }: { children: React.ReactNode }) {
  const { isAuthenticated, isLoading } = useAuthStore();

  if (isLoading) {
    return (
      <div className="loading-screen">
        <div className="loading-spinner"></div>
        <p>Loading...</p>
      </div>
    );
  }

  if (isAuthenticated) {
    return <Navigate to="/" replace />;
  }

  return <>{children}</>;
}

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <Routes>
          {/* Public Routes */}
          <Route
            path="/login"
            element={
              <PublicRoute>
                <AuthLayout>
                  <LoginPage />
                </AuthLayout>
              </PublicRoute>
            }
          />

          {/* Protected Routes */}
          <Route
            path="/"
            element={
              <ProtectedRoute>
                <MainLayout />
              </ProtectedRoute>
            }
          >
            <Route index element={<GlobalOverviewPage />} />
            <Route path="countries" element={<CountriesPage />}>
              <Route index element={<CountriesOverview />} />
              <Route path=":countryCode" element={<CountryDetail />} />
            </Route>
            <Route path="teams" element={<TeamsPage />}>
              <Route index element={<TeamsOverview />} />
              <Route path=":memberId" element={<TeamMemberDetail />} />
            </Route>
            <Route path="partners/*" element={<PartnersPage />} />
            <Route path="pipeline" element={<PipelinePage />} />
            <Route path="customers" element={<CustomersPage />} />
            <Route path="performance" element={<PerformancePage />} />
            <Route path="territories" element={<TerritoriesPage />} />
            <Route path="forecasting" element={<ForecastingPage />} />
            <Route path="reports" element={<ReportsPage />} />
            <Route path="settings" element={<SettingsPage />} />
          </Route>

          {/* Catch All - 404 */}
          <Route
            path="*"
            element={
              <div className="error-page">
                <h1>404</h1>
                <p>Page not found</p>
                <a href="/">Go to Dashboard</a>
              </div>
            }
          />
        </Routes>

        {/* React Query DevTools (only in development) */}
        {import.meta.env.DEV && <ReactQueryDevtools initialIsOpen={false} position="bottom-right" />}
      </BrowserRouter>
    </QueryClientProvider>
  );
}

export default App;
