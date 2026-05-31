import { Routes, Route, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { LoginPage } from './pages/LoginPage';
import { DashboardPage } from './pages/DashboardPage';
import { NewTicketPage } from './pages/NewTicketPage';
import { KnowledgeBasePage } from './pages/KnowledgeBasePage';
import { TicketDetailsPage } from './pages/TicketDetailsPage';
import { Layout } from './components/Layout';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: 1,
      staleTime: 5 * 60 * 1000,
    },
  },
});

export function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <Layout>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/" element={<DashboardPage />} />
          <Route path="/new-ticket" element={<NewTicketPage />} />
          <Route path="/knowledge" element={<KnowledgeBasePage />} />
          <Route path="/ticket/:ticketId" element={<TicketDetailsPage />} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </Layout>
    </QueryClientProvider>
  );
}

export default App;
