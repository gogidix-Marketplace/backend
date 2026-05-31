import React, { Suspense, useEffect } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { HelmetProvider } from 'react-helmet-async';
import { I18nextProvider } from 'react-i18next';
import { useDashboardStore } from './stores/dashboard-store';
import { MainLayout } from './components/layout/MainLayout';
import { Dashboard } from './pages/Dashboard';
import { Regions } from './pages/Regions';
import { Countries } from './pages/Countries';
import { Loading } from './components/common/Loading';
import i18n from './i18n';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 5 * 60 * 1000,
      retry: 1,
      refetchOnWindowFocus: false,
    },
  },
});

// Lazy load pages
const Reports = React.lazy(() => import('./pages/Reports'));
const Financial = React.lazy(() => import('./pages/Financial'));
const Customers = React.lazy(() => import('./pages/Customers'));
const Operations = React.lazy(() => import('./pages/Operations'));
const Compliance = React.lazy(() => import('./pages/Compliance'));
const Settings = React.lazy(() => import('./pages/Settings'));

const LoadingFallback = () => (
  <div className="flex items-center justify-center min-h-screen">
    <Loading size="lg" text="Loading..." />
  </div>
);

const App: React.FC = () => {
  const { darkMode } = useDashboardStore();

  useEffect(() => {
    // Apply dark mode on mount
    if (darkMode) {
      document.documentElement.classList.add('dark');
    }
  }, [darkMode]);

  return (
    <HelmetProvider>
      <QueryClientProvider client={queryClient}>
        <I18nextProvider i18n={i18n}>
          <BrowserRouter>
            <Suspense fallback={<LoadingFallback />}>
              <Routes>
                <Route path="/" element={<MainLayout />}>
                  <Route index element={<Navigate to="/dashboard" replace />} />
                  <Route path="dashboard" element={<Dashboard />} />
                  <Route path="regions" element={<Regions />} />
                  <Route path="countries" element={<Countries />} />
                  <Route path="reports" element={<Reports />} />
                  <Route path="financial" element={<Financial />} />
                  <Route path="customers" element={<Customers />} />
                  <Route path="operations" element={<Operations />} />
                  <Route path="compliance" element={<Compliance />} />
                  <Route path="settings" element={<Settings />} />
                </Route>
              </Routes>
            </Suspense>
          </BrowserRouter>
        </I18nextProvider>
      </QueryClientProvider>
    </HelmetProvider>
  );
};

export default App;
