import { Routes, Route, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { MainPage } from './pages/MainPage';
import { CountryDetailsPage } from './pages/CountryDetailsPage';
import { TeamPerformancePage } from './pages/TeamPerformancePage';
import { Layout } from './components/Layout';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: 1,
      staleTime: 5 * 60 * 1000, // 5 minutes
    },
  },
});

export function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <Layout>
        <Routes>
          <Route path="/" element={<MainPage />} />
          <Route path="/country/:countryCode" element={<CountryDetailsPage />} />
          <Route path="/team/:teamId" element={<TeamPerformancePage />} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </Layout>
    </QueryClientProvider>
  );
}

export default App;
