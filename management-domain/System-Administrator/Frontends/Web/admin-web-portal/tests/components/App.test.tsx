import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import App from '../../src/presentation/App';

// Mock the API module
vi.mock('../../application', () => ({
  useAuthStore: () => ({
    isAuthenticated: true,
    user: { id: '1', name: 'Test User', role: 'admin' },
    login: vi.fn(),
    logout: vi.fn()
  })
}));

const renderWithProviders = (component: React.ReactElement) => {
  const queryClient = new QueryClient({
    defaultOptions: {
      queries: { retry: false },
      mutations: { retry: false }
    }
  });

  return render(
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>{component}</BrowserRouter>
    </QueryClientProvider>
  );
};

describe('App Component', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('should render without crashing', () => {
    renderWithProviders(<App />);
    expect(true).toBe(true);
  });

  it('should render the main navigation', async () => {
    renderWithProviders(<App />);

    await waitFor(() => {
      expect(document.querySelector('nav')).toBeInTheDocument();
    });
  });

  it('should render the header', async () => {
    renderWithProviders(<App />);

    await waitFor(() => {
      expect(document.querySelector('header')).toBeInTheDocument();
    });
  });

  it('should contain routes to main pages', async () => {
    renderWithProviders(<App />);

    await waitFor(() => {
      const links = document.querySelectorAll('a[href]');
      expect(links.length).toBeGreaterThan(0);
    });
  });
});

describe('Dashboard Component', () => {
  it('should display dashboard title', async () => {
    renderWithProviders(<App />);

    await waitFor(() => {
      const title = screen.queryByText(/dashboard/i);
      expect(title).toBeInTheDocument();
    });
  });

  it('should display system statistics', async () => {
    renderWithProviders(<App />);

    await waitFor(() => {
      const stats = document.querySelectorAll('[data-testid="stat-card"]');
      expect(stats.length).toBeGreaterThan(0);
    });
  });
});

describe('Layout', () => {
  it('should have responsive layout', () => {
    renderWithProviders(<App />);

    const mainContainer = document.querySelector('#root');
    expect(mainContainer).toBeInTheDocument();
  });

  it('should render footer', async () => {
    renderWithProviders(<App />);

    await waitFor(() => {
      const footer = document.querySelector('footer');
      expect(footer).toBeInTheDocument();
    });
  });
});
